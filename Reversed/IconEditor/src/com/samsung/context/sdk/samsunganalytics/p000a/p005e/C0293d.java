package com.samsung.context.sdk.samsunganalytics.p000a.p005e;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.C0276a;
import com.samsung.context.sdk.samsunganalytics.p000a.p001a.C0272a;
import com.samsung.context.sdk.samsunganalytics.p000a.p002b.C0281a;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0288c;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0314c;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0315d;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.e.d */
public class C0293d {
    private C0293d() {
    }

    public static int m74a(Context context, int i) {
        int i2;
        int i3 = 0;
        SharedPreferences a = C0314c.m153a(context);
        if (i == 1) {
            i2 = a.getInt(C0292b.f66b, 0);
            i3 = a.getInt(C0292b.f68d, 0);
        } else if (i == 0) {
            i2 = a.getInt(C0292b.f67c, 0);
            i3 = a.getInt(C0292b.f69e, 0);
        } else {
            i2 = 0;
        }
        return i2 - i3;
    }

    public static C0429c m75a(Context context, Configuration configuration, C0281a c0281a, C0276a c0276a) {
        C0429c c0429c = new C0429c(C0272a.GET_POLICY, configuration.getTrackingId(), C0293d.m76a(context, c0281a, configuration), C0314c.m153a(context), c0276a);
        C0311a.m143a("trid: " + configuration.getTrackingId().substring(0, 7) + ", uv: " + configuration.getVersion());
        return c0429c;
    }

    public static Map<String, String> m76a(Context context, C0281a c0281a, Configuration configuration) {
        Map<String, String> hashMap = new HashMap();
        hashMap.put("pkn", context.getPackageName());
        hashMap.put("dm", c0281a.m27g());
        if (!TextUtils.isEmpty(c0281a.m21a())) {
            hashMap.put("mcc", c0281a.m21a());
        }
        if (!TextUtils.isEmpty(c0281a.m22b())) {
            hashMap.put("mnc", c0281a.m22b());
        }
        hashMap.put("uv", configuration.getVersion());
        return hashMap;
    }

    public static void m77a(Context context, Configuration configuration, C0288c c0288c, C0281a c0281a) {
        c0288c.m63a(C0293d.m75a(context, configuration, c0281a, null));
    }

    public static void m78a(Context context, Configuration configuration, C0288c c0288c, C0281a c0281a, C0276a c0276a) {
        c0288c.m63a(C0293d.m75a(context, configuration, c0281a, c0276a));
    }

    public static void m79a(SharedPreferences sharedPreferences) {
        sharedPreferences.edit().putLong(C0292b.f79o, System.currentTimeMillis()).putInt(C0292b.f69e, 0).putInt(C0292b.f68d, 0).commit();
    }

    public static boolean m80a(Context context) {
        SharedPreferences a = C0314c.m153a(context);
        if (C0315d.m158a(1, Long.valueOf(a.getLong(C0292b.f79o, 0)))) {
            C0293d.m79a(a);
        }
        return C0315d.m158a(a.getInt(C0292b.f65a, 1), Long.valueOf(a.getLong(C0292b.f78n, 0)));
    }

    public static boolean m81a(Context context, int i, int i2) {
        int i3;
        int i4;
        int i5;
        SharedPreferences a = C0314c.m153a(context);
        if (i == 1) {
            i3 = a.getInt(C0292b.f66b, 0);
            i4 = a.getInt(C0292b.f68d, 0);
            i5 = a.getInt(C0292b.f70f, 0);
        } else if (i == 0) {
            i3 = a.getInt(C0292b.f67c, 0);
            i4 = a.getInt(C0292b.f69e, 0);
            i5 = a.getInt(C0292b.f71g, 0);
        } else {
            i5 = 0;
            i4 = 0;
            i3 = 0;
        }
        C0311a.m143a("Quota : " + i3 + "/ Uploaded : " + i4 + "/ limit : " + i5 + "/ size : " + i2);
        if (i3 < i4 + i2) {
            C0311a.m144a("DLS Sender", "send result fail : Over daily quota");
            return false;
        } else if (i5 >= i2) {
            return true;
        } else {
            C0311a.m144a("DLS Sender", "send result fail : Over once quota");
            return false;
        }
    }

    public static void m82b(Context context, int i, int i2) {
        SharedPreferences a = C0314c.m153a(context);
        if (i == 1) {
            a.edit().putInt(C0292b.f68d, a.getInt(C0292b.f68d, 0) + i2).apply();
        } else if (i == 0) {
            a.edit().putInt(C0292b.f69e, C0314c.m153a(context).getInt(C0292b.f69e, 0) + i2).apply();
        }
    }
}
