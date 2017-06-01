package com.samsung.android.edge;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.System;
import com.samsung.android.edge.IEdgeLightingCallback.Stub;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.util.SemLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SemEdgeManager {
    public static final int DISABLE_EDGE_LIGHTING = 1;
    public static final int DISABLE_NONE_EDGE_LIGHTING = 0;
    private static final String EDGE_LIGHTING = "edge_lighting";
    private static final int EDGE_LIGHTING_ALWAYS = 0;
    private static final String EDGE_LIGHTING_EDGE_NOTIFICATIONS = "edge_lighting_edge_notifications";
    public static final boolean EDGE_LIGHTING_ENABLED;
    private static final int EDGE_LIGHTING_SCREEN_OFF = 2;
    private static final int EDGE_LIGHTING_SCREEN_ON = 1;
    private static final String EDGE_LIGHTING_SHOW_CONDITION = "edge_lighting_show_condition";
    public static final int EDGE_LIGHTING_STATE_NONE = 0;
    public static final int EDGE_LIGHTING_STATE_RUNNING = 1;
    public static final boolean SUPPORT_EDGE_LIGHTING = true;
    private static final String TAG = SemEdgeManager.class.getSimpleName();
    private Context mContext;
    private final CopyOnWriteArrayList<EdgeLightingCallbackDelegate> mEdgeLightingCallbackDelegates = new CopyOnWriteArrayList();
    private Object mEdgeLightingDelegatesLock = new Object();
    private final String mPackageName;
    private IEdgeManager mService;
    private final Binder mToken = new Binder();

    private class EdgeLightingCallbackDelegate extends Stub {
        private static final int MSG_EDGE_LIGHTING_START = 0;
        private static final int MSG_EDGE_LIGHTING_STARTED = 2;
        private static final int MSG_EDGE_LIGHTING_STOP = 1;
        private static final int MSG_EDGE_LIGHTING_STOPPED = 3;
        private static final int MSG_SCREEN_CHANGED = 4;
        private final OnEdgeLightingCallback mCallback;
        private Handler mHandler;
        private final OnEdgeLightingListener mListener;

        EdgeLightingCallbackDelegate(OnEdgeLightingCallback onEdgeLightingCallback) {
            this.mCallback = onEdgeLightingCallback;
            this.mListener = null;
            this.mHandler = new Handler(SemEdgeManager.this.mContext.getMainLooper()) {
                public void handleMessage(Message message) {
                    boolean z = true;
                    if (EdgeLightingCallbackDelegate.this.mCallback != null) {
                        switch (message.what) {
                            case 0:
                                Bundle bundle = (Bundle) message.obj;
                                EdgeLightingCallbackDelegate.this.mCallback.onStartEdgeLighting(bundle.getString(FingerprintManager.CLIENTSPEC_KEY_PACKAGE_NAME), (SemEdgeLightingInfo) bundle.getParcelable("info"), message.arg1);
                                return;
                            case 1:
                                EdgeLightingCallbackDelegate.this.mCallback.onStopEdgeLighting((String) message.obj, message.arg1);
                                return;
                            case 4:
                                OnEdgeLightingCallback -get0 = EdgeLightingCallbackDelegate.this.mCallback;
                                if (message.arg1 != 1) {
                                    z = false;
                                }
                                -get0.onScreenChanged(z);
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        EdgeLightingCallbackDelegate(OnEdgeLightingListener onEdgeLightingListener) {
            this.mCallback = null;
            this.mListener = onEdgeLightingListener;
            this.mHandler = new Handler(SemEdgeManager.this.mContext.getMainLooper()) {
                public void handleMessage(Message message) {
                    if (EdgeLightingCallbackDelegate.this.mListener != null) {
                        switch (message.what) {
                            case 2:
                                EdgeLightingCallbackDelegate.this.mListener.onEdgeLightingStarted();
                                return;
                            case 3:
                                EdgeLightingCallbackDelegate.this.mListener.onEdgeLightingStopped();
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        OnEdgeLightingCallback getCallback() {
            return this.mCallback;
        }

        OnEdgeLightingListener getListener() {
            return this.mListener;
        }

        public void onEdgeLightingStarted() throws RemoteException {
            this.mHandler.sendEmptyMessage(2);
        }

        public void onEdgeLightingStopped() throws RemoteException {
            this.mHandler.sendEmptyMessage(3);
        }

        public void onScreenChanged(boolean z) throws RemoteException {
            Message.obtain(this.mHandler, 4, z ? 1 : 0, 0).sendToTarget();
        }

        public void onStartEdgeLighting(String str, SemEdgeLightingInfo semEdgeLightingInfo, int i) throws RemoteException {
            Message obtain = Message.obtain(this.mHandler, 0, i, 0);
            BaseBundle bundle = new Bundle();
            bundle.putString(FingerprintManager.CLIENTSPEC_KEY_PACKAGE_NAME, str);
            bundle.putParcelable("info", semEdgeLightingInfo);
            obtain.obj = bundle;
            obtain.sendToTarget();
        }

        public void onStopEdgeLighting(String str, int i) throws RemoteException {
            Message obtain = Message.obtain(this.mHandler, 1, i, 0);
            obtain.obj = str;
            obtain.sendToTarget();
        }
    }

    static {
        String string = SemFloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_COMMON_CONFIG_EDGE");
        EDGE_LIGHTING_ENABLED = string != null ? string.contains("edgelighting_v2") : false;
    }

    public SemEdgeManager(Context context, IEdgeManager iEdgeManager) {
        this.mContext = context;
        this.mPackageName = context.getOpPackageName();
        this.mService = iEdgeManager;
    }

    private IEdgeManager getService() {
        if (this.mService == null) {
            this.mService = IEdgeManager.Stub.asInterface(ServiceManager.getService("edge"));
        }
        return this.mService;
    }

    private boolean isEdgeLightingEnabled(ContentResolver contentResolver) {
        return System.getInt(contentResolver, EDGE_LIGHTING, 1) == 1;
    }

    private boolean isEdgeLightingEnabledByScreen(ContentResolver contentResolver, boolean z) {
        int i = 1;
        int i2 = System.getInt(contentResolver, EDGE_LIGHTING_SHOW_CONDITION, 0);
        if (i2 == 0) {
            return true;
        }
        if (i2 == 1) {
            i = 0;
        }
        return i ^ z;
    }

    public void bindEdgeLightingService(OnEdgeLightingCallback onEdgeLightingCallback, int i) {
        Throwable th;
        if (getService() != null) {
            if (onEdgeLightingCallback == null) {
                SemLog.m24w(TAG, "bindEdgeLightingService : callback is null");
                return;
            }
            synchronized (this.mEdgeLightingDelegatesLock) {
                try {
                    EdgeLightingCallbackDelegate edgeLightingCallbackDelegate;
                    IBinder edgeLightingCallbackDelegate2;
                    Iterator it = this.mEdgeLightingCallbackDelegates.iterator();
                    while (it.hasNext()) {
                        EdgeLightingCallbackDelegate edgeLightingCallbackDelegate3 = (EdgeLightingCallbackDelegate) it.next();
                        if (edgeLightingCallbackDelegate3.getCallback() != null && edgeLightingCallbackDelegate3.getCallback().equals(onEdgeLightingCallback)) {
                            edgeLightingCallbackDelegate = edgeLightingCallbackDelegate3;
                            break;
                        }
                    }
                    edgeLightingCallbackDelegate = null;
                    if (edgeLightingCallbackDelegate == null) {
                        try {
                            edgeLightingCallbackDelegate2 = new EdgeLightingCallbackDelegate(onEdgeLightingCallback);
                            this.mEdgeLightingCallbackDelegates.add(edgeLightingCallbackDelegate2);
                        } catch (Throwable th2) {
                            th = th2;
                            EdgeLightingCallbackDelegate edgeLightingCallbackDelegate4 = edgeLightingCallbackDelegate;
                            throw th;
                        }
                    }
                    Object obj = edgeLightingCallbackDelegate;
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(edgeLightingCallbackDelegate2 == null || componentName == null)) {
                        this.mService.bindEdgeLightingService(edgeLightingCallbackDelegate2, i, componentName);
                    }
                } catch (Throwable e) {
                    SemLog.m19e(TAG, "bindEdgeLightingService : RemoteException : ", e);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
        return;
    }

    public void disable(int i) {
        if (getService() != null) {
            try {
                this.mService.disable(i, this.mPackageName, this.mToken);
            } catch (Throwable e) {
                throw new RuntimeException("EdgeService dead?", e);
            }
        }
    }

    public void disableEdgeLightingNotification(boolean z) {
        if (getService() != null) {
            try {
                this.mService.disableEdgeLightingNotification(this.mPackageName, z);
            } catch (Throwable e) {
                throw new RuntimeException("EdgeService dead?", e);
            }
        }
    }

    public int getEdgeLightingState() {
        if (getService() == null) {
            return 0;
        }
        try {
            return this.mService.getEdgeLightingState();
        } catch (Throwable e) {
            throw new RuntimeException("EdgeService dead?", e);
        }
    }

    public boolean isEdgeLightingNotificationAllowed() {
        if (getService() == null) {
            return false;
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (!isEdgeLightingEnabled(contentResolver)) {
            return false;
        }
        int callingUid = Binder.getCallingUid();
        if (callingUid != 1000 && callingUid == Process.myUid() && !isEdgeLightingEnabledByScreen(contentResolver, ((PowerManager) this.mContext.getSystemService("power")).isInteractive())) {
            return false;
        }
        try {
            return this.mService.isEdgeLightingNotificationAllowed(this.mPackageName);
        } catch (Throwable e) {
            throw new RuntimeException("EdgeService dead?", e);
        }
    }

    public void registerEdgeLightingListener(OnEdgeLightingListener onEdgeLightingListener) {
        EdgeLightingCallbackDelegate edgeLightingCallbackDelegate;
        Throwable th;
        if (getService() != null) {
            if (onEdgeLightingListener == null) {
                SemLog.m24w(TAG, "registerEdgeLightingListener : listener is null");
                return;
            }
            synchronized (this.mEdgeLightingDelegatesLock) {
                try {
                    IBinder edgeLightingCallbackDelegate2;
                    Iterator it = this.mEdgeLightingCallbackDelegates.iterator();
                    while (it.hasNext()) {
                        EdgeLightingCallbackDelegate edgeLightingCallbackDelegate3 = (EdgeLightingCallbackDelegate) it.next();
                        if (edgeLightingCallbackDelegate3.getListener() != null && edgeLightingCallbackDelegate3.getListener().equals(onEdgeLightingListener)) {
                            edgeLightingCallbackDelegate = edgeLightingCallbackDelegate3;
                            break;
                        }
                    }
                    edgeLightingCallbackDelegate = null;
                    if (edgeLightingCallbackDelegate == null) {
                        try {
                            edgeLightingCallbackDelegate2 = new EdgeLightingCallbackDelegate(onEdgeLightingListener);
                            this.mEdgeLightingCallbackDelegates.add(edgeLightingCallbackDelegate2);
                        } catch (Throwable th2) {
                            th = th2;
                            EdgeLightingCallbackDelegate edgeLightingCallbackDelegate4 = edgeLightingCallbackDelegate;
                            throw th;
                        }
                    }
                    Object obj = edgeLightingCallbackDelegate;
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(edgeLightingCallbackDelegate2 == null || componentName == null)) {
                        this.mService.registerEdgeLightingListener(edgeLightingCallbackDelegate2, componentName);
                    }
                } catch (Throwable e) {
                    SemLog.m19e(TAG, "registerEdgeLightingListener : RemoteException : ", e);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
        return;
    }

    public void startEdgeLighting(SemEdgeLightingInfo semEdgeLightingInfo) {
        if (getService() != null) {
            if (semEdgeLightingInfo == null) {
                throw new IllegalArgumentException("info is null.");
            }
            try {
                this.mService.startEdgeLighting(this.mPackageName, semEdgeLightingInfo, this.mToken);
            } catch (Throwable e) {
                throw new RuntimeException("EdgeService dead?", e);
            }
        }
    }

    public void stopEdgeLighting() {
        if (getService() != null) {
            try {
                this.mService.stopEdgeLighting(this.mPackageName, this.mToken);
            } catch (Throwable e) {
                throw new RuntimeException("EdgeService dead?", e);
            }
        }
    }

    public void unbindEdgeLightingService(OnEdgeLightingCallback onEdgeLightingCallback) {
        if (getService() != null) {
            if (onEdgeLightingCallback == null) {
                SemLog.m24w(TAG, "unbindEdgeLightingService : callback is null");
                return;
            }
            synchronized (this.mEdgeLightingDelegatesLock) {
                IBinder iBinder = null;
                Iterator it = this.mEdgeLightingCallbackDelegates.iterator();
                while (it.hasNext()) {
                    EdgeLightingCallbackDelegate edgeLightingCallbackDelegate = (EdgeLightingCallbackDelegate) it.next();
                    if (edgeLightingCallbackDelegate.getCallback() != null && edgeLightingCallbackDelegate.getCallback().equals(onEdgeLightingCallback)) {
                        iBinder = edgeLightingCallbackDelegate;
                        break;
                    }
                }
                if (iBinder == null) {
                    SemLog.m24w(TAG, "unbindEdgeLightingService : cannot find the callback");
                    return;
                }
                try {
                    this.mService.unbindEdgeLightingService(iBinder, this.mPackageName);
                    this.mEdgeLightingCallbackDelegates.remove(iBinder);
                } catch (Throwable e) {
                    SemLog.m19e(TAG, "unbindEdgeLightingService : RemoteException : ", e);
                }
            }
        } else {
            return;
        }
    }

    public void unregisterEdgeLightingListener(OnEdgeLightingListener onEdgeLightingListener) {
        if (getService() != null) {
            if (onEdgeLightingListener == null) {
                SemLog.m24w(TAG, "unregisterEdgeLightingListener : listener is null");
                return;
            }
            synchronized (this.mEdgeLightingDelegatesLock) {
                IBinder iBinder = null;
                Iterator it = this.mEdgeLightingCallbackDelegates.iterator();
                while (it.hasNext()) {
                    EdgeLightingCallbackDelegate edgeLightingCallbackDelegate = (EdgeLightingCallbackDelegate) it.next();
                    if (edgeLightingCallbackDelegate.getListener() != null && edgeLightingCallbackDelegate.getListener().equals(onEdgeLightingListener)) {
                        iBinder = edgeLightingCallbackDelegate;
                        break;
                    }
                }
                if (iBinder == null) {
                    SemLog.m24w(TAG, "unregisterEdgeLightingListener : cannot find the listener");
                    return;
                }
                try {
                    this.mService.unregisterEdgeLightingListener(iBinder, this.mPackageName);
                    this.mEdgeLightingCallbackDelegates.remove(iBinder);
                } catch (Throwable e) {
                    SemLog.m19e(TAG, "unbindEdgeLightingService : RemoteException : ", e);
                }
            }
        } else {
            return;
        }
    }

    public void updateEdgeLightingPackageList(ArrayList<String> arrayList) {
        if (getService() != null) {
            if (arrayList == null) {
                SemLog.m24w(TAG, "updateEdgeLightingPackageList : list is null");
                return;
            }
            try {
                this.mService.updateEdgeLightingPackageList(this.mPackageName, arrayList);
            } catch (Throwable e) {
                throw new RuntimeException("EdgeService dead?", e);
            }
        }
    }

    public void updateEdgeLightingPolicy(EdgeLightingPolicy edgeLightingPolicy) {
        if (getService() != null) {
            if (edgeLightingPolicy == null) {
                SemLog.m24w(TAG, "updateEdgeLightingPolicy : policy is null");
                return;
            }
            try {
                this.mService.updateEdgeLightingPolicy(this.mPackageName, edgeLightingPolicy);
            } catch (Throwable e) {
                SemLog.m19e(TAG, "updateEdgeLightingPolicy : RemoteException : ", e);
            }
        }
    }
}
