package com.samsung.android.emergencymode;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import com.samsung.android.desktopmode.SemDesktopModeManager;
import com.samsung.android.emergencymode.IEmergencyManager.Stub;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.knox.SemPersonaManager;
import com.samsung.android.media.SemSoundAssistantManager;

public class SemEmergencyManager {
    private static final boolean SERVICE_DBG = false;
    private static final String TAG = "EmergencyManager";
    private static boolean isBootCompleted = false;
    private static boolean mIsLoadedFeatures;
    private static final Object mLock = new Object();
    private static IEmergencyManager mService;
    private static boolean mSupport_BCM;
    private static boolean mSupport_DexMode;
    private static boolean mSupport_EM;
    private static boolean mSupport_UPSM;
    private static SemEmergencyManager sInstance = null;
    private Context mContext;
    private final Handler mHandler;
    private BroadcastReceiver mReceiver = new C00691();

    class C00691 extends BroadcastReceiver {
        C00691() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                Elog.m3d(SemEmergencyManager.TAG, "onReceive : " + intent);
                boolean booleanExtra;
                int intExtra;
                if (action.equals(SemEmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER) || action.equals(SemEmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER_OLD)) {
                    booleanExtra = intent.getBooleanExtra(SemEmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_ENABLE, false);
                    intExtra = intent.getIntExtra(SemEmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_FLAG, -1);
                    boolean booleanExtra2 = intent.getBooleanExtra(SemEmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_SKIPDIALOG, false);
                    if (intExtra != -1) {
                        if ((intExtra != 2048 || SemEmergencyManager.mSupport_BCM) && (!(intExtra == 512 || intExtra == 1024) || SemEmergencyManager.mSupport_UPSM)) {
                            SemEmergencyManager.this.triggerEmergencyMode(booleanExtra, intExtra, booleanExtra2);
                        } else {
                            Elog.m3d(SemEmergencyManager.TAG, "onReceive : trying to ON BCM|UPSM while BCM|UPMS not supported in this model. Flag = " + intExtra);
                        }
                    }
                } else if (action.equals("com.nttdocomo.android.epsmodecontrol.action.CHANGE_MODE")) {
                    booleanExtra = !SemEmergencyManager.isEmergencyMode(SemEmergencyManager.this.mContext);
                    intExtra = 16;
                    if (SemEmergencyManager.this.getModeType() == 1) {
                        intExtra = 512;
                    }
                    SemEmergencyManager.this.triggerEmergencyMode(booleanExtra, intExtra, false);
                }
            }
        }
    }

    private SemEmergencyManager(Handler handler, Context context) {
        this.mHandler = handler;
        this.mContext = context;
        loadFloatingFeatures();
        ensureServiceConnected();
    }

    private void ensureServiceConnected() {
        try {
            if (mService == null) {
                mService = Stub.asInterface(ServiceManager.getService(SemEmergencyConstants.SERVICE_NAME));
            } else if (!mService.asBinder().isBinderAlive()) {
                Elog.m3d(TAG, "mService is not valid so retieve the service again.");
                mService = Stub.asInterface(ServiceManager.getService(SemEmergencyConstants.SERVICE_NAME));
            }
        } catch (Exception e) {
            Elog.m3d(TAG, "ensureServiceConnected e : " + e);
        }
    }

    private static boolean getBootState() {
        boolean z = true;
        if (!isBootCompleted) {
            if (SystemProperties.getInt("sys.boot_completed", 0) != 1) {
                z = false;
            }
            isBootCompleted = z;
        }
        return isBootCompleted;
    }

    public static SemEmergencyManager getInstance(Context context) {
        if (context == null) {
            return null;
        }
        SemEmergencyManager semEmergencyManager;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new SemEmergencyManager(new Handler(context.getMainLooper()), context);
            }
            semEmergencyManager = sInstance;
        }
        return semEmergencyManager;
    }

    public static boolean isBatteryConservingMode(Context context) {
        return System.getInt(context.getContentResolver(), "battery_conserving_mode", 0) == 1;
    }

    public static boolean isBatteryConversingModeSupported() {
        if (!mIsLoadedFeatures) {
            loadFloatingFeatures();
        }
        return mSupport_BCM;
    }

    public static boolean isEmergencyMode(Context context) {
        return System.getInt(context.getContentResolver(), "emergency_mode", 0) == 1;
    }

    public static boolean isEmergencyModeSupported() {
        if (!mIsLoadedFeatures) {
            loadFloatingFeatures();
        }
        return mSupport_EM;
    }

    public static boolean isGrayScreenSupported() {
        Elog.m3d(TAG, "[Temporary Change]support MDNIE [" + true + "]  AMOLED display [" + true + "]  isGrayScreenSupported [" + (1 != null) + "]");
        return 1 != null;
    }

    public static boolean isUltraPowerSavingModeSupported() {
        if (!mIsLoadedFeatures) {
            loadFloatingFeatures();
        }
        return mSupport_UPSM;
    }

    private static void loadFloatingFeatures() {
        mSupport_UPSM = SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_COMMON_SUPPORT_ULTRA_POWER_SAVING");
        mSupport_EM = SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_COMMON_SUPPORT_SAFETYCARE");
        mSupport_BCM = SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_COMMON_SUPPORT_BATTERY_CONVERSING");
        mSupport_DexMode = SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_COMMON_SUPPORT_KNOX_DESKTOP");
        mIsLoadedFeatures = true;
    }

    private void registerReceiver() {
        Elog.m3d(TAG, "registerReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SemEmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER);
        intentFilter.addAction(SemEmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER_OLD);
        String str = SystemProperties.get("ro.csc.sales_code", "unknown");
        Elog.m3d(TAG, "registerReceiver Scode[" + str + "]");
        if ("DCM".equalsIgnoreCase(str)) {
            intentFilter.addAction("com.nttdocomo.android.epsmodecontrol.action.CHANGE_MODE");
        }
        this.mContext.registerReceiver(this.mReceiver, intentFilter, "com.sec.android.emergencymode.permission.LAUNCH_EMERGENCYMODE_SERVICE", null);
    }

    private synchronized void startService(String str, boolean z, int i, boolean z2) {
        try {
            Intent intent = new Intent();
            if (i == -1) {
                intent.putExtra(SemEmergencyConstants.EXTRA_CLEAR_BOOT_TIME, true);
            }
            if (str == null) {
                intent.putExtra(SemEmergencyConstants.EXTRA_INIT_FOR_EM_STATE, true);
            } else if (str.equals(SemEmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER)) {
                intent.setAction(str);
                intent.putExtra(SemEmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_ENABLE, z);
                intent.putExtra(SemEmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_FLAG, i);
                intent.putExtra(SemEmergencyConstants.EXTRA_EMERGENCY_START_SERVICE_SKIPDIALOG, z2);
            } else if (str.equals(SemEmergencyConstants.EMERGENCY_CHECK_ABNORMAL_STATE)) {
                intent.setAction(str);
            }
            intent.setComponent(new ComponentName(SemEmergencyConstants.EMERGENCY_SERVICE_PACKAGE, SemEmergencyConstants.EMERGENCY_SERVICE_STARTER));
            Elog.m3d(TAG, "Starting service: " + intent);
            this.mContext.startServiceAsUser(intent, UserHandle.OWNER);
        } catch (Exception e) {
            Elog.m3d(TAG, "startService e : " + e);
        }
    }

    private void stopService() {
        synchronized (SemEmergencyManager.class) {
            try {
                if (mService != null) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(SemEmergencyConstants.EMERGENCY_SERVICE_PACKAGE, SemEmergencyConstants.EMERGENCY_SERVICE_STARTER));
                    Elog.m3d(TAG, "stopService: " + intent);
                    this.mContext.stopServiceAsUser(intent, UserHandle.OWNER);
                    mService = null;
                }
            } catch (Exception e) {
                Elog.m3d(TAG, "stopService e : " + e);
            }
        }
    }

    private void triggerEmergencyMode(boolean z, int i, boolean z2) {
        ensureServiceConnected();
        startService(SemEmergencyConstants.EMERGENCY_START_SERVICE_BY_ORDER, z, i, z2);
        Elog.m3d(TAG, "req trigger, start Service");
    }

    private void unregisterReceiver() {
        Elog.m3d(TAG, "unregisterReceiver");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public boolean addAppToLauncher(String str, boolean z) {
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.addAppToLauncher(str, z);
        } catch (Exception e) {
            Elog.m3d(TAG, "addAppToLauncher failed e : " + e);
            return false;
        }
    }

    public boolean canSetMode() {
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        SemDesktopModeManager semDesktopModeManager = (SemDesktopModeManager) this.mContext.getSystemService("desktopmode");
        if (mSupport_DexMode && semDesktopModeManager != null && SemDesktopModeManager.isDesktopMode()) {
            return false;
        }
        UserManager userManager = (UserManager) this.mContext.getSystemService("user");
        boolean z = true;
        boolean z2 = false;
        Object obj = null;
        int i = 0;
        String str = "";
        try {
            z2 = isModifying();
            i = ActivityManager.getCurrentUser();
            if ("2.0".equals(SemPersonaManager.getKnoxInfo().getString(SemSoundAssistantManager.VERSION))) {
                SemPersonaManager semPersonaManager = (SemPersonaManager) this.mContext.getSystemService("persona");
                if (semPersonaManager != null && semPersonaManager.exists(i)) {
                    obj = 1;
                }
            }
        } catch (Exception e) {
            Elog.m3d(TAG, "canSetMode Exception : " + e);
        }
        if (!(Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0)) {
            z = false;
            str = str + "SETUP_WIZARD_UNFINISHED;";
        }
        if (z2) {
            z = false;
            str = str + "LLM_ENABLING;";
        }
        if (i != 0 && r5 == null) {
            z = false;
            str = str + "NOT_OWNER_" + i + ";";
        }
        if (!z) {
            Elog.m4v(TAG, "not Allowed EmergencyMode due to " + str);
        }
        return z;
    }

    public boolean checkInvalidBroadcast(String str, String str2) {
        if ((!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) || !getBootState() || !isEmergencyMode(this.mContext)) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.checkInvalidBroadcast(str, str2);
        } catch (Exception e) {
            Elog.m3d(TAG, "checkInvalidBroadcast failed e : " + e);
            return false;
        }
    }

    public boolean checkInvalidProcess(String str) {
        if ((!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) || !getBootState() || !isEmergencyMode(this.mContext)) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.checkInvalidProcess(str);
        } catch (Exception e) {
            Elog.m3d(TAG, "checkInvalidProcess failed e : " + e);
            return false;
        }
    }

    public boolean checkModeType(int i) {
        if ((!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) || !isEmergencyMode(this.mContext)) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.checkModeType(i);
        } catch (Exception e) {
            Elog.m3d(TAG, "checkModeType failed e : " + e);
            return false;
        }
    }

    public boolean checkValidIntentAction(String str, String str2) {
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        if (!isEmergencyMode(this.mContext)) {
            return true;
        }
        ensureServiceConnected();
        if (mService == null) {
            return true;
        }
        try {
            return mService.checkValidIntentAction(str, str2);
        } catch (Exception e) {
            Elog.m3d(TAG, "checkValidIntentAction failed e : " + e);
            return true;
        }
    }

    public boolean checkValidPackage(String str, String str2, int i) {
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        if (!isEmergencyMode(this.mContext)) {
            return true;
        }
        ensureServiceConnected();
        if (mService == null) {
            return true;
        }
        try {
            return mService.checkValidPackage(str, str2, i);
        } catch (Exception e) {
            Elog.m3d(TAG, "checkValidPackage failed e : " + e);
            return true;
        }
    }

    public int getEmergencyState() {
        if ((!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) || !isEmergencyMode(this.mContext)) {
            return -1;
        }
        ensureServiceConnected();
        if (mService == null) {
            return -1;
        }
        try {
            return mService.getEmergencyState();
        } catch (Exception e) {
            Elog.m3d(TAG, "getEmergencyState failed e : " + e);
            return -1;
        }
    }

    public int getModeType() {
        return System.getInt(this.mContext.getContentResolver(), "ultra_powersaving_mode", 0) == 1 ? 1 : System.getInt(this.mContext.getContentResolver(), "battery_conserving_mode", 0) == 1 ? 2 : System.getInt(this.mContext.getContentResolver(), "emergency_mode", 0) == 1 ? 0 : -1;
    }

    public boolean isEmergencyMode() {
        return (mSupport_EM || mSupport_UPSM || mSupport_BCM) ? isEmergencyMode(this.mContext) : false;
    }

    public boolean isModifying() {
        if ((!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) || !isEmergencyMode(this.mContext)) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.isModifying();
        } catch (Exception e) {
            Elog.m3d(TAG, "isModifying failed e : " + e);
            return false;
        }
    }

    public boolean isScreenOn() {
        if ((!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) || !isEmergencyMode(this.mContext)) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.isScreenOn();
        } catch (Exception e) {
            Elog.m3d(TAG, "isScreenOn failed e : " + e);
            return false;
        }
    }

    public boolean isUserPackageBlocked() {
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.isUserPackageBlocked();
        } catch (Exception e) {
            Elog.m3d(TAG, "isUserPackageBlocked failed e : " + e);
            return false;
        }
    }

    public int makePrivilegedCall(String str) {
        try {
            Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.fromParts("tel", str, null));
            intent.setFlags(268435456);
            this.mContext.startActivity(intent);
            Elog.m3d(TAG, "req call, success.");
            return 1;
        } catch (Exception e) {
            Elog.m3d(TAG, "req call, unknown Err : " + e);
            return -9;
        }
    }

    public boolean needMobileDataBlock() {
        if (!mSupport_EM && !mSupport_UPSM && !mSupport_BCM) {
            return false;
        }
        ensureServiceConnected();
        if (mService == null) {
            return false;
        }
        try {
            return mService.needMobileDataBlock();
        } catch (Exception e) {
            Elog.m3d(TAG, "needMobileDataBlock failed e : " + e);
            return false;
        }
    }

    public void readyEmergencyMode() {
        if (isEmergencyMode(this.mContext)) {
            Elog.m3d(TAG, "This is emergency mode.");
            startService(null, false, -1, false);
        } else {
            Elog.m3d(TAG, "This is normal mode.");
            this.mContext.getContentResolver().query(SemEmergencyConstants.URI_UPDATE_TABLE, null, null, null, null, null);
            startService(SemEmergencyConstants.EMERGENCY_CHECK_ABNORMAL_STATE, false, -1, false);
        }
        registerReceiver();
    }

    public void setLocationProviderEnabled(boolean z) {
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    mService.setLocationProviderEnabled(z);
                } catch (Exception e) {
                    Elog.m3d(TAG, "setLocationProviderEnabled failed e : " + e);
                }
            }
        }
    }

    public void setUserPackageBlocked(boolean z, Context context) {
        if (mSupport_EM || mSupport_UPSM || mSupport_BCM) {
            ensureServiceConnected();
            if (mService != null) {
                try {
                    mService.setUserPackageBlocked(z);
                } catch (Exception e) {
                    Elog.m3d(TAG, "setUserPackageBlocked failed e : " + e);
                }
            }
        }
    }
}
