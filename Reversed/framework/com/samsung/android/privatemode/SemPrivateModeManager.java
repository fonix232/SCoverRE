package com.samsung.android.privatemode;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
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
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.provider.Settings.System;
import android.util.Log;
import com.samsung.android.emergencymode.SemEmergencyManager;
import com.samsung.android.knox.SemPersonaManager;
import com.samsung.android.media.SemSoundAssistantManager;
import com.samsung.android.privatemode.IPrivateModeManager.Stub;
import com.samsung.android.smartface.SmartFaceManager;

public class SemPrivateModeManager implements DeathRecipient {
    public static final String ACTION_PRIVATE_MODE_OFF = "com.samsung.android.intent.action.PRIVATE_MODE_OFF";
    public static final String ACTION_PRIVATE_MODE_ON = "com.samsung.android.intent.action.PRIVATE_MODE_ON";
    private static final boolean DEBUG = Debug.semIsProductDev();
    private static final String ERROR_MSG_SERVICE_NOT_FOUND = "PrivateMode Service is not running!";
    private static final String PRIVATE_PATH = "/storage/Private";
    public static final String PROPERTY_KEY = "sys.samsung.personalpage.mode";
    public static final int STATE_CANCELLED = 3;
    public static final int STATE_ERROR_INTERNAL = 21;
    public static final int STATE_MOUNTED = 1;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_NORMAL_TO_PRIVATE = 2;
    public static final int STATE_PREPARED = 0;
    public static final int STATE_PRIVATE = 1;
    public static final int STATE_UNMOUNTED = 2;
    private static final String TAG = "PPS_SemPrivateModeManager";
    private static Context mContext = null;
    private static Handler mHandler;
    private static boolean mIsServiceBind = false;
    private static IPrivateModeClient mPrivateClient = null;
    private static IPrivateModeManager mService = null;
    private static SemPrivateModeManager sInstance = null;
    private static int versionPrivatemode = -1;
    private ServiceConnection mServiceConn = null;

    static class C02332 implements Runnable {
        C02332() {
        }

        public void run() {
            if (SemPrivateModeManager.mPrivateClient != null) {
                try {
                    Log.d(SemPrivateModeManager.TAG, "getInstance: Calling IPrivateModeClient=" + SemPrivateModeManager.mPrivateClient);
                    Log.d(SemPrivateModeManager.TAG, "getInstance, onStateChange : STATE_PREPARED ");
                    SemPrivateModeManager.mPrivateClient.onStateChange(0, 0);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class C02353 implements ServiceConnection {

        class C02341 implements Runnable {
            C02341() {
            }

            public void run() {
                if (SemPrivateModeManager.mPrivateClient != null) {
                    try {
                        Log.d(SemPrivateModeManager.TAG, "bindPrivateModeManager, onStateChange : STATE_PREPARED ");
                        SemPrivateModeManager.mPrivateClient.onStateChange(0, 0);
                    } catch (Exception e) {
                        SemPrivateModeManager.this.logExceptionInDetail("bindPrivateModeManager", e, null);
                    }
                }
            }
        }

        C02353() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(SemPrivateModeManager.TAG, "onServiceConnected: name=" + componentName + ", Service=" + iBinder);
            SemPrivateModeManager.mIsServiceBind = true;
            SemPrivateModeManager.mService = Stub.asInterface(iBinder);
            try {
                if (SemPrivateModeManager.mHandler != null) {
                    SemPrivateModeManager.mHandler.post(new C02341());
                }
            } catch (Exception e) {
                SemPrivateModeManager.this.logExceptionInDetail("onServiceConnected", e, null);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(SemPrivateModeManager.TAG, "onServiceDisconnected: name=" + componentName);
            SemPrivateModeManager.mIsServiceBind = false;
            SemPrivateModeManager.mService = null;
        }
    }

    public interface StateListener {
        void onStateChanged(int i, int i2);
    }

    private SemPrivateModeManager(Handler handler) {
        mHandler = handler;
        bindPrivateModeManager();
    }

    private synchronized void bindPrivateModeManager() {
        if (mService == null) {
            Log.i(TAG, "bindPrivateModeManager called");
            this.mServiceConn = new C02353();
            Intent intent = new Intent("com.samsung.android.personalpage.service.PersonalPageService");
            intent.setComponent(new ComponentName("com.samsung.android.personalpage.service", "com.samsung.android.personalpage.service.PersonalPageService"));
            if (mContext.bindService(intent, this.mServiceConn, 1)) {
                Log.i(TAG, "bindService: OK");
            }
        }
    }

    public static synchronized SemPrivateModeManager getInstance(Context context, IPrivateModeClient iPrivateModeClient) {
        synchronized (SemPrivateModeManager.class) {
            if (context == null || iPrivateModeClient == null) {
                Log.e(TAG, "getInstance: context or client is null");
                return null;
            } else if (context.getPackageManager().hasSystemFeature("com.sec.feature.secretmode_service")) {
                mContext = context;
                if (sInstance == null) {
                    sInstance = new SemPrivateModeManager(new Handler(context.getMainLooper()));
                } else if (!mIsServiceBind || mService == null) {
                    sInstance = new SemPrivateModeManager(new Handler(context.getMainLooper()));
                } else if (mHandler != null) {
                    mHandler.post(new C02332());
                }
                Log.d(TAG, "getInstance: " + sInstance);
                SemPrivateModeManager semPrivateModeManager = sInstance;
                return semPrivateModeManager;
            } else {
                Log.e(TAG, "getInstance: Not support Private Mode");
                return null;
            }
        }
    }

    public static synchronized SemPrivateModeManager getInstance(Context context, final StateListener stateListener) {
        synchronized (SemPrivateModeManager.class) {
            if (context == null || stateListener == null) {
                Log.e(TAG, "getInstance: context or listener is null");
                return null;
            }
            mPrivateClient = new IPrivateModeClient.Stub() {
                public void onStateChange(int i, int i2) throws RemoteException {
                    stateListener.onStateChanged(i, i2);
                }
            };
            SemPrivateModeManager instance = getInstance(context, mPrivateClient);
            return instance;
        }
    }

    public static String getPrivateStoragePath(Context context) {
        return PRIVATE_PATH;
    }

    public static int getState() {
        int i = 0;
        String str = SystemProperties.get("sys.samsung.personalpage.mode", SmartFaceManager.PAGE_MIDDLE);
        Log.i(TAG, "getState : " + str);
        if (SmartFaceManager.PAGE_BOTTOM.equals(str)) {
            i = 1;
        } else if ("2".equals(str)) {
            i = 2;
        } else if (SmartFaceManager.PAGE_MIDDLE.equals(str)) {
            i = 0;
        }
        Log.i(TAG, "getState(ret) :" + i);
        return i;
    }

    private static boolean isKnoxMode(Context context) {
        int i = -1;
        try {
            BaseBundle knoxInfoForApp = SemPersonaManager.getKnoxInfoForApp(context, "isKnoxMode");
            if ("2.0".equals(knoxInfoForApp.getString(SemSoundAssistantManager.VERSION)) && SmartFaceManager.TRUE.equals(knoxInfoForApp.getString("isKnoxMode"))) {
                i = 2;
            }
        } catch (NoClassDefFoundError e) {
            Log.e(TAG, "not call com.samsung.android.knox.SemPersonaManager;." + e);
        } catch (NoSuchMethodError e2) {
            Log.e(TAG, "not call getKnoxInfoForApp." + e2);
        }
        if (context.getPackageName().contains("sec_container_")) {
            i = 1;
        }
        Log.i(TAG, "isKnoxMode : " + i);
        return i > 0;
    }

    public static boolean isPrivateModeReady(Context context) {
        if (versionPrivatemode < 0) {
            versionPrivatemode = context.getPackageManager().semGetSystemFeatureLevel("com.sec.feature.secretmode_service");
            Log.i(TAG, "isReady: getSystemFeatureLevel : " + versionPrivatemode);
        }
        Log.i(TAG, "isReady: versionPrivatemode : " + versionPrivatemode);
        if (((DevicePolicyManager) context.getSystemService("device_policy")).getDeviceOwner() != null) {
            Log.i(TAG, "isReady: AFW_CL");
            return false;
        } else if (versionPrivatemode <= 1) {
            return false;
        } else {
            if (isKnoxMode(context)) {
                Log.i(TAG, "isReady: private mode does not support in KNOX mode");
                return false;
            } else if (SemEmergencyManager.isEmergencyMode(context)) {
                Log.i(TAG, "isReady: private mode does not support in Emergency(UltraPowerSaving, Emergency) mode");
                return false;
            } else if (System.getIntForUser(context.getContentResolver(), "personal_mode_lock_type", 0, 0) == 0) {
                Log.i(TAG, "isReady: Has no locktype");
                return false;
            } else if (ActivityManager.getCurrentUser() != 0) {
                Log.i(TAG, "isReady: Current User is not Owner User(guest mode)");
                return false;
            } else if (!UserManager.get(context).isManagedProfile()) {
                return true;
            } else {
                Log.i(TAG, "isReady: AFW_BYOD");
                return false;
            }
        }
    }

    public static boolean isPrivateStorageMounted(Context context) {
        boolean z = false;
        if (context == null) {
            Log.i(TAG, "isPrivateStorageMounted: context is null");
            return false;
        }
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        if (DEBUG) {
            Log.i(TAG, "isPrivateStorageMounted: " + context.getPackageName());
        }
        if (isKnoxMode(context)) {
            Log.i(TAG, "isKnoxMode : return false");
            return false;
        } else if (SmartFaceManager.PAGE_MIDDLE.equals(SystemProperties.get("sys.samsung.personalpage.mode", SmartFaceManager.PAGE_MIDDLE))) {
            Log.i(TAG, "PROPERTY_KEY_PRIVATE_MODE[0] : return false");
            return false;
        } else {
            if (storageManager != null) {
                try {
                    String volumeState = storageManager.getVolumeState(PRIVATE_PATH);
                    Log.i(TAG, "getVolumeState[" + volumeState + "]");
                    if ("mounted".equals(volumeState)) {
                        z = true;
                    }
                } catch (Exception e) {
                    z = false;
                }
            }
            return z;
        }
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
    }

    public IBinder registerListener(IPrivateModeClient iPrivateModeClient) {
        if (mService == null) {
            logExceptionInDetail("registerClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else if (getState() == 1) {
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

    public IBinder registerListener(StateListener stateListener) {
        if (stateListener != null) {
            return registerListener(mPrivateClient);
        }
        logExceptionInDetail("registerClient", null, "listener is null");
        return null;
    }

    public boolean unregisterListener(IBinder iBinder, boolean z) {
        if (mService == null) {
            logExceptionInDetail("unregisterListener", null, ERROR_MSG_SERVICE_NOT_FOUND);
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
            logExceptionInDetail("unregisterListener", e, null);
            return false;
        }
    }
}
