package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p008a;

import com.samsung.android.app.ledcover.update.StubCodes;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0286a;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0287b;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0309d;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.a.c */
public class C0431c implements C0287b {
    private static final String f186a = "SAM";
    private C0300a f187b;
    private Configuration f188c;
    private C0309d f189d;
    private C0286a f190e;
    private int f191f;

    public C0431c(C0300a c0300a, Configuration configuration, C0309d c0309d, C0286a c0286a) {
        this.f191f = -1;
        this.f187b = c0300a;
        this.f188c = configuration;
        this.f189d = c0309d;
        this.f190e = c0286a;
    }

    public void m176a() {
        try {
            this.f191f = this.f187b.m107d().requestSend(f186a, this.f188c.getTrackingId().substring(0, 3), this.f189d.m135b(), this.f189d.m132a(), StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION, C0316a.f163d, C0316a.f165f, this.f189d.m137c());
            C0311a.m143a("send to DLC : " + this.f189d.m137c());
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
    }

    public int m177b() {
        if (this.f191f == 0) {
            C0311a.m144a("DLC Sender", "send result success : " + this.f191f);
            return 1;
        }
        C0311a.m144a("DLC Sender", "send result fail : " + this.f191f);
        return -7;
    }
}
