package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0292b;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0309d;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.p010a.C0303a;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.p011b.C0307a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0314c;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0315d;
import java.util.Queue;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.c.a */
public class C0306a {
    private static C0306a f130d;
    private C0303a f131a;
    private C0307a f132b;
    private boolean f133c;

    private C0306a(Context context, boolean z) {
        if (z) {
            this.f131a = new C0303a(context);
        }
        this.f132b = new C0307a();
        this.f133c = z;
    }

    public static C0306a m116a(Context context, Configuration configuration) {
        if (f130d == null) {
            synchronized (C0306a.class) {
                if (configuration.isEnableUseInAppLogging()) {
                    String string = C0314c.m153a(context).getString(C0292b.f75k, C0316a.f163d);
                    if (string.equals(C0292b.f76l)) {
                        f130d = new C0306a(context, false);
                    } else if (string.equals(C0292b.f77m)) {
                        f130d = new C0306a(context, true);
                    } else {
                        f130d = new C0306a(context, false);
                    }
                } else {
                    f130d = new C0306a(context, false);
                }
            }
        }
        return f130d;
    }

    public static C0306a m117a(Context context, Boolean bool) {
        if (f130d == null) {
            synchronized (C0306a.class) {
                if (bool.booleanValue()) {
                    f130d = new C0306a(context, true);
                } else {
                    f130d = new C0306a(context, false);
                }
            }
        }
        return f130d;
    }

    private void m118f() {
        if (!this.f132b.m128a().isEmpty()) {
            for (C0309d a : this.f132b.m128a()) {
                this.f131a.m112a(a);
            }
            this.f132b.m128a().clear();
        }
    }

    public void m119a() {
        this.f133c = false;
    }

    public void m120a(long j, String str, String str2) {
        m122a(new C0309d(str, j, str2));
    }

    public void m121a(Context context) {
        this.f133c = true;
        if (this.f131a == null) {
            this.f131a = new C0303a(context);
        }
        m118f();
    }

    public void m122a(C0309d c0309d) {
        if (this.f133c) {
            this.f131a.m112a(c0309d);
        } else {
            this.f132b.m129a(c0309d);
        }
    }

    public void m123a(String str) {
        if (this.f133c) {
            this.f131a.m113a(str);
        }
    }

    public boolean m124b() {
        return this.f133c;
    }

    public void m125c() {
        if (this.f133c) {
            this.f131a.m111a(C0315d.m154a(5));
        }
    }

    public Queue<C0309d> m126d() {
        Queue<C0309d> a;
        if (this.f133c) {
            m125c();
            a = this.f131a.m110a();
        } else {
            a = this.f132b.m128a();
        }
        if (!a.isEmpty()) {
            C0311a.m143a("get log from " + (this.f133c ? "Database " : "Queue ") + "(" + a.size() + ")");
        }
        return a;
    }

    public boolean m127e() {
        return this.f133c ? this.f131a.m115c() : this.f132b.m130b();
    }
}
