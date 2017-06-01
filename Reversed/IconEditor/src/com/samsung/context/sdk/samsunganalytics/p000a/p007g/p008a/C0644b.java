package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p008a;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.C0276a;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0309d;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0432a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import java.util.Map;
import java.util.Queue;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.a.b */
public class C0644b extends C0432a {
    private C0300a f221g;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.a.b.1 */
    class C04301 implements C0276a<Void, Void> {
        final /* synthetic */ C0644b f185a;

        C04301(C0644b c0644b) {
            this.f185a = c0644b;
        }

        public Void m175a(Void voidR) {
            this.f185a.m200b();
            return null;
        }
    }

    public C0644b(Context context, Configuration configuration) {
        super(context, configuration);
        this.f221g = new C0300a(context, new C04301(this));
        this.f221g.m105b();
    }

    private void m200b() {
        Queue d = this.e.m126d();
        while (!d.isEmpty()) {
            this.f.m63a(new C0431c(this.f221g, this.b, (C0309d) d.poll(), null));
        }
    }

    protected Map<String, String> m201a(Map<String, String> map) {
        Map<String, String> a = super.m178a(map);
        a.remove("do");
        a.remove("dm");
        a.remove("v");
        return a;
    }

    public int m202d(Map<String, String> map) {
        if (!m179a()) {
            return -5;
        }
        m181c(map);
        if (this.f221g.m106c()) {
            m200b();
        } else {
            this.f221g.m105b();
        }
        return 0;
    }

    public int m203e(Map<String, String> map) {
        C0311a.m144a("DLCLogSender", "not support sync api");
        m202d(map);
        return -100;
    }
}
