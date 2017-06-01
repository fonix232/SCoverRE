package com.samsung.context.sdk.samsunganalytics.p000a.p001a;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.a.a */
public enum C0272a {
    GET_POLICY(C0274c.POLICY, C0273b.DEVICE_CONTROLLER_DIR, C0275d.GET),
    SEND_LOG(C0274c.DLS, C0273b.DLS_DIR, C0275d.POST),
    SEND_BUFFERED_LOG(C0274c.DLS, C0273b.DLS_DIR_BAT, C0275d.POST);
    
    C0274c f13d;
    C0273b f14e;
    C0275d f15f;

    private C0272a(C0274c c0274c, C0273b c0273b, C0275d c0275d) {
        this.f13d = c0274c;
        this.f14e = c0273b;
        this.f15f = c0275d;
    }

    public String m13a() {
        return this.f13d.m17a() + this.f14e.m15a();
    }

    public String m14b() {
        return this.f15f.m19a();
    }
}
