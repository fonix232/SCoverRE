package com.samsung.context.sdk.samsunganalytics.p000a.p013i;

import android.content.Context;
import android.content.SharedPreferences;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.i.c */
public class C0314c {
    public static final String f154a = "SamsungAnalyticsPrefs";
    public static final String f155b = "SASettingPref";
    public static final String f156c = "deviceId";
    public static final String f157d = "auidType";
    public static final String f158e = "AppPrefs";
    public static final String f159f = "status_sent_date";

    private C0314c() {
    }

    public static SharedPreferences m153a(Context context) {
        return context.getSharedPreferences(f154a, 0);
    }
}
