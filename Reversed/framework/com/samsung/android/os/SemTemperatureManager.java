package com.samsung.android.os;

import android.content.Context;
import android.os.IBinder;
import android.os.ICustomFrequencyManager;
import android.os.ICustomFrequencyManager.Stub;
import android.os.ServiceManager;

public class SemTemperatureManager {
    public static final int HMT_LEVEL_DANGEROUS = 2;
    public static final int HMT_LEVEL_NORMAL = 0;
    public static final int HMT_LEVEL_WARNING = 1;
    private static final int SSRM_VALUE_HMT_LEVEL = 2;
    private static final int SSRM_VALUE_PST = 0;
    private static final int SSRM_VALUE_SIOP_LEVEL = 1;
    public static final int UNSUPPORTED = -999;
    private static ICustomFrequencyManager mService;
    String LOG_TAG = SemTemperatureManager.class.getSimpleName();

    private SemTemperatureManager() {
    }

    public static int getHeadMountTheaterLevel(Context context) {
        ICustomFrequencyManager service = getService(context);
        if (service != null) {
            try {
                return service.getSsrmStatus(2);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return -999;
    }

    public static int getOverheatingProtectionLevel(Context context) {
        ICustomFrequencyManager service = getService(context);
        if (service != null) {
            try {
                return service.getSsrmStatus(1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return -999;
    }

    public static int getPredictedSurfaceTemperature(Context context) {
        ICustomFrequencyManager service = getService(context);
        if (service != null) {
            try {
                return service.getSsrmStatus(0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return -999;
    }

    private static synchronized ICustomFrequencyManager getService(Context context) {
        ICustomFrequencyManager iCustomFrequencyManager;
        synchronized (SemTemperatureManager.class) {
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
}
