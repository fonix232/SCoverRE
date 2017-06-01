package com.samsung.context.sdk.samsunganalytics.p000a.p013i;

import java.util.Map;
import java.util.Map.Entry;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.i.b */
public class C0313b<K, V> {
    public static final String f153a = "\u000e";

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.i.b.a */
    public enum C0312a {
        ONE_DEPTH("\u0002", "\u0003"),
        TWO_DEPTH("\u0004", "\u0005"),
        THREE_DEPTH("\u0006", "\u0007");
        
        private String f151d;
        private String f152e;

        private C0312a(String str, String str2) {
            this.f151d = str;
            this.f152e = str2;
        }

        public String m150a() {
            return this.f151d;
        }

        public String m151b() {
            return this.f152e;
        }
    }

    public String m152a(Map<K, V> map, C0312a c0312a) {
        String str = null;
        for (Entry entry : map.entrySet()) {
            if (str == null) {
                str = entry.getKey().toString();
            } else {
                str = (str + c0312a.m150a()) + entry.getKey();
            }
            str = (str + c0312a.m151b()) + entry.getValue();
        }
        return str;
    }
}
