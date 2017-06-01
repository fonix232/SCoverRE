package com.samsung.android.mateservice.util;

import android.util.Log;
import com.samsung.android.mateservice.common.FwDependency;
import com.samsung.android.smartface.SmartFaceManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilLog {
    private static final String DEBUG_LEVEL_HIGH = "0x4948";
    private static final String DEBUG_LEVEL_LOW = "0x4f4c";
    private static final String DEBUG_LEVEL_MID = "0x494d";
    private static final int LEVEL_DEBUG = 4;
    private static final int LEVEL_ERROR = 1;
    private static final int LEVEL_INFO = 3;
    private static final int LEVEL_NONE = 0;
    private static final int LEVEL_VERBOSE = 5;
    private static final int LEVEL_WARN = 2;
    private static final long TIME_DIFF = 5000000000L;
    private static int sCurLogLevel = internalLogLevel();
    private static long sLateUpdated = System.nanoTime();
    private static boolean sSafeString = internalUseSafeString();

    public static String m6d(String str, String str2, Object... objArr) {
        updateLogLevel();
        if (str2 == null || sCurLogLevel < 4) {
            return null;
        }
        if (objArr.length > 0) {
            str2 = getMsg(str2, objArr);
        }
        Log.d(getTag(str), str2);
        return str2;
    }

    public static String m7e(String str, String str2, Object... objArr) {
        updateLogLevel();
        if (str2 == null || sCurLogLevel < 1) {
            return null;
        }
        if (objArr.length > 0) {
            str2 = getMsg(str2, objArr);
        }
        Log.e(getTag(str), str2);
        return str2;
    }

    public static String getDateString(long j) {
        return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US).format(new Date(j));
    }

    public static String getMsg(String str, Object... objArr) {
        return String.format(Locale.ENGLISH, str, objArr);
    }

    private static String getRoDebugLevel() {
        return FwDependency.getSystemProperty("ro.debug_level", "");
    }

    public static String getSafe(String str) {
        return sSafeString ? "..." : str == null ? "" : str;
    }

    private static String getTag(String str) {
        return "MateSvc|" + str;
    }

    public static String m8i(String str, String str2, Object... objArr) {
        updateLogLevel();
        if (str2 == null || sCurLogLevel < 3) {
            return null;
        }
        if (objArr.length > 0) {
            str2 = getMsg(str2, objArr);
        }
        Log.i(getTag(str), str2);
        return str2;
    }

    private static int internalLogLevel() {
        int i = (FwDependency.isProductDev() || isRoDebugLevelMid()) ? 5 : 3;
        String systemProperty = FwDependency.getSystemProperty("debug.mate.log.service", "");
        if ("".equals(systemProperty)) {
            return i;
        }
        String[] strArr = new String[]{"none", "error", "warning", "info", "debug", "verbose"};
        int i2 = -1;
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (strArr[i3].equals(systemProperty)) {
                i2 = i3;
                break;
            }
        }
        return (i2 <= -1 || i2 >= strArr.length) ? i : i2;
    }

    private static boolean internalUseSafeString() {
        boolean z = !FwDependency.isProductDev();
        String systemProperty = FwDependency.getSystemProperty("debug.mate.log.safe_string", "");
        return SmartFaceManager.FALSE.equals(systemProperty) ? false : SmartFaceManager.TRUE.equals(systemProperty) ? true : z;
    }

    public static boolean isDebugLogLevel() {
        return sCurLogLevel >= 4;
    }

    public static boolean isRoDebugLevelMid() {
        String roDebugLevel = getRoDebugLevel();
        return !roDebugLevel.equals(DEBUG_LEVEL_MID) ? roDebugLevel.equals(DEBUG_LEVEL_HIGH) : true;
    }

    public static int logLevel() {
        updateLogLevel();
        return sCurLogLevel;
    }

    public static void printThrowableStackTrace(Throwable th) {
        if (isDebugLogLevel() || isRoDebugLevelMid()) {
            th.printStackTrace();
        }
    }

    private static void updateLogLevel() {
        long nanoTime = System.nanoTime();
        if (nanoTime - sLateUpdated > TIME_DIFF) {
            sCurLogLevel = internalLogLevel();
            sSafeString = internalUseSafeString();
            sLateUpdated = nanoTime;
        }
    }

    public static boolean useSafeString() {
        return sSafeString;
    }

    public static String m9v(String str, String str2, Object... objArr) {
        updateLogLevel();
        if (str2 == null || sCurLogLevel < 5) {
            return null;
        }
        if (objArr.length > 0) {
            str2 = getMsg(str2, objArr);
        }
        Log.v(getTag(str), str2);
        return str2;
    }

    public static String m10w(String str, String str2, Object... objArr) {
        updateLogLevel();
        if (str2 == null || sCurLogLevel < 2) {
            return null;
        }
        if (objArr.length > 0) {
            str2 = getMsg(str2, objArr);
        }
        Log.w(getTag(str), str2);
        return str2;
    }
}
