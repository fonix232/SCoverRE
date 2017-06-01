package com.samsung.context.sdk.samsunganalytics.p000a.p012h;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0287b;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0308c;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0314c;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.h.a */
public class C0435a implements C0287b {
    private static boolean f209a;
    private Context f210b;
    private Configuration f211c;
    private Map<String, String> f212d;

    static {
        f209a = true;
    }

    public C0435a(Context context, Configuration configuration) {
        this.f210b = context;
        this.f211c = configuration;
    }

    public static void m189a(boolean z) {
        f209a = z;
    }

    public static boolean m190c() {
        return f209a;
    }

    public void m191a() {
        this.f212d = new HashMap();
        CharSequence c0310c = new C0310c(this.f210b).toString();
        if (!TextUtils.isEmpty(c0310c)) {
            this.f212d.put("ts", String.valueOf(System.currentTimeMillis()));
            this.f212d.put("t", "st");
            this.f212d.put("sti", c0310c);
        }
    }

    public int m192b() {
        if (this.f212d.isEmpty()) {
            return -3;
        }
        if (C0308c.m131a(this.f210b, null, this.f211c).m108d(this.f212d) == 0) {
            C0311a.m144a("Setting Sender", "Send success");
            C0311a.m143a("Setting [" + ((String) this.f212d.get("sti")) + "]");
            C0314c.m153a(this.f210b).edit().putLong(C0314c.f159f, System.currentTimeMillis()).commit();
        } else {
            C0311a.m144a("Setting Sender", "Send fail");
        }
        return 0;
    }
}
