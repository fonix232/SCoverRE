package com.samsung.context.sdk.samsunganalytics.p000a.p001a;

import com.samsung.context.sdk.samsunganalytics.C0316a;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.a.b */
public enum C0273b {
    DEVICE_CONTROLLER_DIR("/dc/qtransf"),
    DLS_DIR(C0316a.f163d),
    DLS_DIR_BAT(C0316a.f163d);
    
    String f20d;

    private C0273b(String str) {
        this.f20d = str;
    }

    public String m15a() {
        return this.f20d;
    }

    public void m16a(String str) {
        this.f20d = str;
    }
}
