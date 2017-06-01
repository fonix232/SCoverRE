package com.samsung.android.hardware;

import android.content.Context;
import android.os.IBinder;
import android.os.ICustomFrequencyManager;
import android.os.ICustomFrequencyManager.Stub;
import android.os.ServiceManager;

public class SemBatteryUtils {
    public static final int MODE_BIG_DATA_LOGGING = 4;
    public static final int MODE_BIG_DATA_UPLOADING = 5;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_POWER_SAVING = 1;
    public static final int MODE_POWER_SAVING_IN_DARK_THEME = 17;
    public static final int MODE_POWER_SAVING_IN_GRAYSCALE = 16;
    public static final int MODE_POWER_SAVING_WITH_BLOCKING_DATA = 6;
    public static final int MODE_POWER_SAVING_WITH_LIMITING_APPLICATIONS = 18;
    public static final int MODE_POWER_SAVING_WITH_MAX_BRIGHTNESS_100 = 11;
    public static final int MODE_POWER_SAVING_WITH_MAX_BRIGHTNESS_80 = 7;
    public static final int MODE_POWER_SAVING_WITH_MAX_BRIGHTNESS_85 = 8;
    public static final int MODE_POWER_SAVING_WITH_MAX_BRIGHTNESS_90 = 9;
    public static final int MODE_POWER_SAVING_WITH_MAX_BRIGHTNESS_95 = 10;
    public static final int MODE_POWER_SAVING_WITH_RESOLUTIION_FHD = 13;
    public static final int MODE_POWER_SAVING_WITH_RESOLUTIION_HD = 12;
    public static final int MODE_POWER_SAVING_WITH_RESOLUTIION_WQHD = 14;
    public static final int MODE_POWER_SAVING_WITH_RESTRICTING_BG_DATA = 3;
    public static final int MODE_POWER_SAVING_WITH_RESTRICTING_PERFORMANCE = 15;
    public static final int MODE_ULTRA_POWER_SAVING = 2;
    private static ICustomFrequencyManager mService;

    private SemBatteryUtils() {
    }

    public static int getBatteryRemainingUsageTime(Context context, int i) {
        ICustomFrequencyManager service = getService(context);
        int i2 = 0;
        if (service != null) {
            try {
                i2 = service.getBatteryRemainingUsageTime(i);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return i2;
    }

    public static String[] getFrequentlyUsedAppListByLocation(Context context, double d, double d2, int i) {
        ICustomFrequencyManager service = getService(context);
        String[] strArr = null;
        if (service != null) {
            try {
                strArr = service.getFrequentlyUsedAppListByLocation(d, d2, i);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return strArr;
    }

    public static String[] getLeastRecentlyUsedAppList(Context context, int i) {
        ICustomFrequencyManager service = getService(context);
        String[] strArr = null;
        if (service != null) {
            try {
                strArr = service.getLeastRecentlyUsedAppList(i);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return strArr;
    }

    private static synchronized ICustomFrequencyManager getService(Context context) {
        ICustomFrequencyManager iCustomFrequencyManager;
        synchronized (SemBatteryUtils.class) {
            if (mService == null) {
                IBinder service = ServiceManager.getService("CustomFrequencyManagerService");
                if (service != null) {
                    mService = Stub.asInterface(service);
                }
            }
            iCustomFrequencyManager = mService;
        }
        return iCustomFrequencyManager;
    }

    public static int getStandbyTimeInUltraPowerSavingMode(Context context) {
        ICustomFrequencyManager service = getService(context);
        if (service != null) {
            try {
                return service.getStandbyTimeInUltraPowerSavingMode();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
