package com.samsung.android.os;

import android.content.Context;
import android.os.IBinder;
import android.os.ICustomFrequencyManager;
import android.os.ICustomFrequencyManager.Stub;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

public class SemPerfManager {
    public static final int AMS_RESUME_BOOST_TYPE_ACQUIRE = 1;
    public static final int AMS_RESUME_BOOST_TYPE_RELEASE = 2;
    public static final int AMS_RESUME_BOOST_TYPE_TAIL = 3;
    static final String BASE_MODEL = "";
    static String BOARD_PLATFORM = SystemProperties.get("ro.board.platform");
    public static final String COMMAND_ACTIVITY_EXECUTION = "EXEC_ACTIVITY";
    public static final String COMMAND_BROWSER_DASH_MODE = "SBROWSER_DASH_MODE";
    public static final String COMMAND_BROWSER_PAGE_LOADING = "SBROWSER_PAGE_LOADING";
    public static final String COMMAND_BUS_DCVS_GOVERNOR_CHANGE = "BUS_DCVS_GOVERNOR";
    public static final String COMMAND_FINGER_HOVER_OFF = "FINGER_HOVER_OFF";
    public static final String COMMAND_FINGER_HOVER_ON = "FINGER_HOVER_ON";
    public static final String COMMAND_GENERAL_SHELL = "GENERAL_SHELL";
    public static final String COMMAND_GESTURE_DETECTED = "GESTURE_DETECTED";
    public static final String COMMAND_HOVERING_EVENT = "HOVERING_EVENT";
    public static final String COMMAND_REQUEST_CACHE_DROP = "REQ_DROP_CACHE";
    public static final String COMMAND_SAMSUNG_SIP = "KNOWN_APP_SIP";
    public static final String COMMAND_SCREEN_ROTATION = "TYPE_WINDOW_ORIENTATION";
    public static final String COMMAND_SCROLL = "TYPE_SCROLL";
    public static final String COMMAND_SMOOTH_SCROLL = "SMOOTH_SCROLL";
    public static final String COMMAND_SUSTAINED_PERF = "SUSTAINED_PERF";
    public static final String COMMAND_USB_TETHERING = "USBTETHERING";
    public static final String COMMAND_VR_MODE = "VR_MODE";
    static final String DEVICE_TYPE = SystemProperties.get("ro.build.characteristics");
    private static final String LOG_TAG = "SemPerfManager";
    static final int ROTATION_BOOSTING_TIMEOUT = 500;
    static final int ROTATION_GPU_BOOSTING_TIMEOUT = 2000;
    static final String SIOP_MODEL = "ssrm_dream2l_xx";
    private static volatile SemDvfsManager mAMSCState = null;
    private static volatile SemDvfsManager mAMSCStateTail = null;
    static volatile ICustomFrequencyManager sCfmsService = null;
    static boolean sIsDebugLevelHigh = "0x4948".equals(SystemProperties.get("ro.debug_level", "0x4f4c"));
    int[] mSupportedCPUCoreNum = null;
    int[] mSupportedCPUCoreNumForSSRM = null;
    int[] mSupportedCPUFrequency = null;
    int[] mSupportedCPUFrequencyForSSRM = null;

    protected SemPerfManager() {
    }

    public static void logOnEng(String str, String str2) {
        if (sIsDebugLevelHigh) {
            Log.i(str, str2);
        }
    }

    public static void onActivityResumeEvent(Context context, String str, int i) {
        if (mAMSCState == null) {
            mAMSCState = SemDvfsManager.createInstance(context, str, 23);
        }
        if (mAMSCStateTail == null) {
            mAMSCStateTail = SemDvfsManager.createInstance(context, SemDvfsManager.HINT_AMS_RESUME_TAIL_CSTATE, 21);
        }
        switch (i) {
            case 1:
                if (mAMSCState != null) {
                    mAMSCState.acquire();
                    return;
                }
                return;
            case 2:
                if (mAMSCState != null) {
                    mAMSCState.release();
                    return;
                }
                return;
            case 3:
                if (mAMSCStateTail != null) {
                    mAMSCStateTail.acquire();
                    return;
                }
                return;
            default:
                try {
                    logOnEng(LOG_TAG, "onActivityResumeEvent:: type is not defined");
                    return;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
        }
        e.printStackTrace();
    }

    public static void onScrollEvent(boolean z) {
        sendCommandToSsrm(COMMAND_SCROLL, z ? "TRUE" : "FALSE");
    }

    public static void onSmoothScrollEvent(boolean z) {
        sendCommandToSsrm("SMOOTH_SCROLL", z ? "TRUE" : "FALSE");
    }

    public static void sendCommandToSsrm(String str, String str2) {
        try {
            if (sCfmsService == null) {
                IBinder service = ServiceManager.getService("CustomFrequencyManagerService");
                if (service != null) {
                    sCfmsService = Stub.asInterface(service);
                }
            }
            if (sCfmsService != null) {
                sCfmsService.sendCommandToSSRM(str, str2);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
