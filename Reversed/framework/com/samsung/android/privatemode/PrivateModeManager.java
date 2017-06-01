package com.samsung.android.privatemode;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.util.Log;
import com.samsung.android.knox.SemPersonaManager;
import com.samsung.android.media.SemSoundAssistantManager;
import com.samsung.android.privatemode.IPrivateModeManager.Stub;
import com.samsung.android.smartface.SmartFaceManager;

public class PrivateModeManager implements DeathRecipient {
    public static final String ACTION_PRIVATE_MODE_OFF = "com.samsung.android.intent.action.PRIVATE_MODE_OFF";
    public static final String ACTION_PRIVATE_MODE_ON = "com.samsung.android.intent.action.PRIVATE_MODE_ON";
    public static final int CANCELLED = 3;
    private static final boolean DEBUG = Debug.semIsProductDev();
    public static final int ERROR_INTERNAL = 21;
    private static final String ERROR_MSG_SERVICE_NOT_FOUND = "PrivateMode Service is not running!";
    public static final int MOUNTED = 1;
    public static final int PREPARED = 0;
    private static final String PRIVATE_PATH = "/storage/Private";
    public static final String PROPERTY_KEY_PRIVATE_MODE = "sys.samsung.personalpage.mode";
    private static final String TAG = "PPS_PrivateModeManager";
    public static final int UNMOUNTED = 2;
    private static Context mContext = null;
    private static Handler mHandler;
    private static boolean mIsServiceBind = false;
    private static IPrivateModeClient mPrivateClient = null;
    private static IPrivateModeManager mService = null;
    private static PrivateModeManager sInstance = null;
    private static int versionPrivatemode = -1;
    private ServiceConnection mServiceConn = null;

    static class C02291 implements Runnable {
        C02291() {
        }

        public void run() {
            if (PrivateModeManager.mPrivateClient != null) {
                try {
                    Log.d(PrivateModeManager.TAG, "getInstance: Calling IPrivateModeClient=" + PrivateModeManager.mPrivateClient);
                    Log.d(PrivateModeManager.TAG, "getInstance, onStateChange : PREPARED ");
                    PrivateModeManager.mPrivateClient.onStateChange(0, 0);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class C02312 implements ServiceConnection {

        class C02301 implements Runnable {
            C02301() {
            }

            public void run() {
                if (PrivateModeManager.mPrivateClient != null) {
                    try {
                        Log.d(PrivateModeManager.TAG, "bindPrivateModeManager, onStateChange : PREPARED ");
                        PrivateModeManager.mPrivateClient.onStateChange(0, 0);
                    } catch (Exception e) {
                        PrivateModeManager.this.logExceptionInDetail("bindPrivateModeManager", e, null);
                    }
                }
            }
        }

        C02312() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(PrivateModeManager.TAG, "onServiceConnected: name=" + componentName + ", Service=" + iBinder);
            PrivateModeManager.mIsServiceBind = true;
            PrivateModeManager.mService = Stub.asInterface(iBinder);
            try {
                if (PrivateModeManager.mHandler != null) {
                    PrivateModeManager.mHandler.post(new C02301());
                }
            } catch (Exception e) {
                PrivateModeManager.this.logExceptionInDetail("onServiceConnected", e, null);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(PrivateModeManager.TAG, "onServiceDisconnected: name=" + componentName);
            PrivateModeManager.mIsServiceBind = false;
            PrivateModeManager.mService = null;
        }
    }

    private PrivateModeManager(Handler handler) {
        mHandler = handler;
        bindPrivateModeManager();
    }

    private synchronized void bindPrivateModeManager() {
        if (mService == null) {
            Log.i(TAG, "bindPrivateModeManager called");
            this.mServiceConn = new C02312();
            Intent intent = new Intent("com.samsung.android.personalpage.service.PersonalPageService");
            intent.setComponent(new ComponentName("com.samsung.android.personalpage.service", "com.samsung.android.personalpage.service.PersonalPageService"));
            if (mContext.bindService(intent, this.mServiceConn, 1)) {
                Log.i(TAG, "bindService: OK");
            }
        }
    }

    public static synchronized PrivateModeManager getInstance(Context context, IPrivateModeClient iPrivateModeClient) {
        synchronized (PrivateModeManager.class) {
            if (context == null || iPrivateModeClient == null) {
                Log.e(TAG, "getInstance: context or client is null");
                return null;
            } else if (context.getPackageManager().hasSystemFeature("com.sec.feature.secretmode_service")) {
                mContext = context;
                mPrivateClient = iPrivateModeClient;
                if (sInstance == null) {
                    sInstance = new PrivateModeManager(new Handler(context.getMainLooper()));
                } else if (!mIsServiceBind || mService == null) {
                    sInstance = new PrivateModeManager(new Handler(context.getMainLooper()));
                } else if (mHandler != null) {
                    mHandler.post(new C02291());
                }
                Log.d(TAG, "getInstance: " + sInstance);
                PrivateModeManager privateModeManager = sInstance;
                return privateModeManager;
            } else {
                Log.e(TAG, "getInstance: Not support Private Mode");
                return null;
            }
        }
    }

    public static synchronized PrivateModeManager getInstance(Context context, PrivateModeListener privateModeListener) {
        synchronized (PrivateModeManager.class) {
            if (context == null || privateModeListener == null) {
                Log.e(TAG, "getInstance: context or listener is null");
                return null;
            }
            PrivateModeManager instance = getInstance(context, privateModeListener.getClient());
            return instance;
        }
    }

    public static String getPrivateStorageDir(Context context) {
        return SemPrivateModeManager.getPrivateStoragePath(context);
    }

    private static boolean isKnoxMode(Context context) {
        Object obj = -1;
        try {
            BaseBundle knoxInfoForApp = SemPersonaManager.getKnoxInfoForApp(context, "isKnoxMode");
            if ("2.0".equals(knoxInfoForApp.getString(SemSoundAssistantManager.VERSION)) && SmartFaceManager.TRUE.equals(knoxInfoForApp.getString("isKnoxMode"))) {
                obj = 2;
            }
        } catch (NoClassDefFoundError e) {
            Log.e(TAG, "not call com.samsung.android.knox.SemPersonaManager;." + e);
        } catch (NoSuchMethodError e2) {
            Log.e(TAG, "not call getKnoxInfoForApp." + e2);
        }
        if (context.getPackageName().contains("sec_container_")) {
            obj = 1;
        }
        return obj > null;
    }

    public static boolean isM2PActivating() {
        return SemPrivateModeManager.getState() == 2;
    }

    public static boolean isPrivateMode() {
        return SemPrivateModeManager.getState() == 1;
    }

    public static boolean isPrivateStorageMounted(Context context) {
        return SemPrivateModeManager.isPrivateStorageMounted(context);
    }

    public static boolean isReady(Context context) {
        return SemPrivateModeManager.isPrivateModeReady(context);
    }

    private void logExceptionInDetail(String str, Exception exception, String str2) {
        if (DEBUG) {
            Log.e(TAG, str + ": failed " + (str2 == null ? "" : "- " + str2), exception);
        } else {
            Log.e(TAG, str + ": failed " + (str2 == null ? "" : "- " + str2));
        }
    }

    private void unBindPrivateModeManager() {
        Log.d(TAG, "unBindPrivateModeManager called");
        try {
            if (mContext != null && mService != null && this.mServiceConn != null) {
                Log.d(TAG, "unbindService called");
                mContext.unbindService(this.mServiceConn);
                mIsServiceBind = false;
                mService = null;
            }
        } catch (Exception e) {
            logExceptionInDetail("unBindPrivateModeManager", e, null);
        }
    }

    public void binderDied() {
        Log.e(TAG, "====== binderDied =====");
        if (mPrivateClient != null) {
            try {
                Log.d(TAG, "binderDied, onStateChange : ERROR_INTERNAL ");
                mPrivateClient.onStateChange(21, 0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public IBinder registerClient(IPrivateModeClient iPrivateModeClient) {
        if (mService == null) {
            logExceptionInDetail("registerClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else if (isPrivateMode()) {
            logExceptionInDetail("registerClient", null, "Private Mode ON!!");
            return null;
        } else {
            try {
                BaseBundle bundle = new Bundle();
                bundle.putString("package_name", mContext.getPackageName());
                return mService.registerClient(iPrivateModeClient, bundle);
            } catch (Exception e) {
                logExceptionInDetail("registerClient", e, null);
                return null;
            }
        }
    }

    public IBinder registerClient(PrivateModeListener privateModeListener) {
        if (privateModeListener != null) {
            return registerClient(privateModeListener.getClient());
        }
        logExceptionInDetail("registerClient", null, "listener is null");
        return null;
    }

    public boolean unregisterClient(IBinder iBinder) {
        if (mService == null) {
            logExceptionInDetail("unregisterClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            boolean unregisterClient = mService.unregisterClient(iBinder);
            if (mService.asBinder().isBinderAlive() && unregisterClient) {
                mIsServiceBind = false;
                unBindPrivateModeManager();
            } else {
                mIsServiceBind = false;
                mService = null;
            }
            return unregisterClient;
        } catch (Exception e) {
            logExceptionInDetail("unregisterClient", e, null);
            return false;
        }
    }

    public boolean unregisterClient(IBinder iBinder, boolean z) {
        if (mService == null) {
            logExceptionInDetail("unregisterClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            boolean unRegisterClient = mService.unRegisterClient(iBinder, z);
            if (mService.asBinder().isBinderAlive() && unRegisterClient) {
                mIsServiceBind = false;
                unBindPrivateModeManager();
            } else {
                mIsServiceBind = false;
                mService = null;
            }
            return unRegisterClient;
        } catch (Exception e) {
            logExceptionInDetail("unregisterClient", e, null);
            return false;
        }
    }
}
