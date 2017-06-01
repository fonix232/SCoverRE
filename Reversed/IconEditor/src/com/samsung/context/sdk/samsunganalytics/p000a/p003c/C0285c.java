package com.samsung.context.sdk.samsunganalytics.p000a.p003c;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.c.c */
public class C0285c {

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.c.c.a */
    public enum C0284a {
        FULL,
        SIMPLE
    }

    private C0285c() {
    }

    public static C0283a m58a(C0284a c0284a) {
        return c0284a == C0284a.FULL ? new C0426b() : c0284a == C0284a.SIMPLE ? new C0427d() : new C0427d();
    }
}
