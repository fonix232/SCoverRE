package com.samsung.context.sdk.samsunganalytics.p000a.p007g;

import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.p008a.C0644b;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.p014b.C0645b;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.c */
public class C0308c {
    private static C0645b f136a;
    private static C0644b f137b;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.c.a */
    public enum C0302a {
        DLC,
        DLS,
        INTENT
    }

    private C0308c() {
    }

    public static C0301b m131a(Context context, C0302a c0302a, Configuration configuration) {
        if (c0302a == null) {
            c0302a = configuration.isEnableUseInAppLogging() ? C0302a.DLS : C0302a.DLC;
        }
        if (c0302a == C0302a.DLS) {
            if (f136a == null) {
                synchronized (C0308c.class) {
                    f136a = new C0645b(context, configuration);
                }
            }
            return f136a;
        } else if (c0302a != C0302a.DLC) {
            return null;
        } else {
            if (f137b == null) {
                synchronized (C0308c.class) {
                    f137b = new C0644b(context, configuration);
                }
            }
            return f137b;
        }
    }
}
