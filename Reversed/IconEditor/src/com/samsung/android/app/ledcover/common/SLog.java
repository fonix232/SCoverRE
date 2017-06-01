package com.samsung.android.app.ledcover.common;

import android.util.Log;
import com.samsung.android.app.ledcover.info.Defines;

public class SLog {
    private static final int BRANCH = 3;
    private static final int CRITICAL = 1;
    private static final int PATH = 4;
    public static final String TAG;
    private static final int VALUE = 2;

    static {
        TAG = "[LED_COVER][" + SLog.class.getSimpleName() + "]";
    }

    private static void printLog(String tag, String body, int type) {
        if (Defines.LOG_LEVEL1.booleanValue()) {
            switch (type) {
                case CRITICAL /*1*/:
                    Log.e(tag, body);
                case VALUE /*2*/:
                    Log.d(tag, body);
                case BRANCH /*3*/:
                    Log.i(tag, body);
                case PATH /*4*/:
                    Log.v(tag, body);
                default:
            }
        }
    }

    public static void m10c(String tag, String body) {
        printLog(tag, body, CRITICAL);
    }

    public static void m12v(String tag, String body) {
        printLog(tag, body, VALUE);
    }

    public static void m9b(String tag, String body) {
        printLog(tag, body, BRANCH);
    }

    public static void m11p(String tag, String body) {
        printLog(tag, body, PATH);
    }
}
