package com.samsung.android.emergencymode;

import com.samsung.android.util.SemLog;

public final class Elog {
    private static final boolean DEBUG = true;
    private static final String M_TAG = "EmergencyMode";

    public static void m3d(String str, String str2) {
        SemLog.secD(M_TAG, "[" + str + "] " + str2);
    }

    public static void m4v(String str, String str2) {
        SemLog.m22v(M_TAG, "[" + str + "] " + str2);
    }
}
