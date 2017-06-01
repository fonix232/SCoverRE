package com.samsung.context.sdk.samsunganalytics.p000a.p002b;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.a */
public class C0281a {
    private String f33a;
    private String f34b;
    private String f35c;
    private String f36d;
    private String f37e;
    private String f38f;
    private String f39g;
    private String f40h;
    private String f41i;
    private String f42j;

    public C0281a(Context context) {
        this.f33a = C0316a.f163d;
        this.f34b = C0316a.f163d;
        this.f35c = C0316a.f163d;
        this.f36d = C0316a.f163d;
        this.f37e = C0316a.f163d;
        this.f38f = C0316a.f163d;
        this.f39g = C0316a.f163d;
        this.f40h = C0316a.f163d;
        this.f41i = C0316a.f163d;
        this.f42j = C0316a.f163d;
        Locale locale = context.getResources().getConfiguration().locale;
        this.f33a = locale.getDisplayCountry();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            String simOperator = telephonyManager.getSimOperator();
            if (simOperator != null && simOperator.length() >= 3) {
                this.f39g = simOperator.substring(0, 3);
                this.f40h = simOperator.substring(3);
            }
        }
        this.f34b = locale.getLanguage();
        this.f35c = VERSION.RELEASE;
        this.f36d = Build.BRAND;
        this.f37e = Build.MODEL;
        this.f42j = VERSION.INCREMENTAL;
        this.f41i = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) TimeZone.getDefault().getRawOffset()));
        try {
            this.f38f = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
    }

    public String m21a() {
        return this.f39g;
    }

    public String m22b() {
        return this.f40h;
    }

    public String m23c() {
        return this.f33a;
    }

    public String m24d() {
        return this.f34b;
    }

    public String m25e() {
        return this.f35c;
    }

    public String m26f() {
        return this.f36d;
    }

    public String m27g() {
        return this.f37e;
    }

    public String m28h() {
        return this.f38f;
    }

    public String m29i() {
        return this.f41i;
    }

    public String m30j() {
        return this.f42j;
    }
}
