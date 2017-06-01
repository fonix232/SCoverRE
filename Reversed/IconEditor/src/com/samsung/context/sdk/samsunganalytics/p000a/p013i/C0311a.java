package com.samsung.context.sdk.samsunganalytics.p000a.p013i;

import android.util.Log;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.i.a */
public class C0311a {
    private static final String f146a = "SamsungAnalytics:1.8.22";

    private C0311a() {
    }

    public static void m142a(Class cls, Exception exception) {
        if (exception != null) {
            Log.w(f146a, "[" + cls.getSimpleName() + "] " + exception.getMessage());
        }
    }

    public static void m143a(String str) {
        if (C0315d.m157a()) {
            Log.d(f146a, "[ENG ONLY] " + str);
        }
    }

    public static void m144a(String str, String str2) {
        C0311a.m148d("[" + str + "] " + str2);
    }

    public static void m145b(String str) {
        Log.v(f146a, str);
    }

    public static void m146b(String str, String str2) {
        C0311a.m149e("[" + str + "] " + str2);
    }

    public static void m147c(String str) {
        Log.i(f146a, str);
    }

    public static void m148d(String str) {
        Log.d(f146a, str);
    }

    public static void m149e(String str) {
        Log.e(f146a, str);
    }
}
