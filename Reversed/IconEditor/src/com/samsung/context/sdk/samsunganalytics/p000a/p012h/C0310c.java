package com.samsung.context.sdk.samsunganalytics.p000a.p012h;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0313b;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0313b.C0312a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0314c;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.h.c */
public class C0310c {
    private Set<String> f141a;
    private Context f142b;
    private final String f143c;
    private final String f144d;
    private final String f145e;

    public C0310c(Context context) {
        this.f142b = context;
        this.f141a = C0314c.m153a(context).getStringSet(C0314c.f158e, new HashSet());
        this.f143c = C0312a.TWO_DEPTH.m151b();
        this.f144d = C0312a.TWO_DEPTH.m150a();
        this.f145e = C0312a.THREE_DEPTH.m150a();
    }

    private SharedPreferences m138a(String str) {
        return this.f142b.getSharedPreferences(str, 0);
    }

    private String m139b() {
        if (this.f141a.isEmpty()) {
            return null;
        }
        String str = C0316a.f163d;
        for (String str2 : this.f141a) {
            SharedPreferences a = m138a(str2);
            Set b = m140b(str2);
            for (Entry entry : a.getAll().entrySet()) {
                if (b.contains(entry.getKey())) {
                    if (!TextUtils.isEmpty(str)) {
                        str = str + this.f144d;
                    }
                    Class cls = entry.getValue().getClass();
                    if (cls.equals(Integer.class) || cls.equals(Float.class) || cls.equals(Long.class) || cls.equals(String.class) || cls.equals(Boolean.class)) {
                        str = str + ((String) entry.getKey()) + this.f143c + entry.getValue();
                    } else {
                        Set<String> set = (Set) entry.getValue();
                        str = str + ((String) entry.getKey()) + this.f143c;
                        String str3 = null;
                        for (String str22 : set) {
                            if (!TextUtils.isEmpty(str3)) {
                                str3 = str3 + this.f145e;
                            }
                            str3 = str3 + str22;
                        }
                        str = str + str3;
                    }
                }
            }
        }
        return str;
    }

    private Set<String> m140b(String str) {
        return C0314c.m153a(this.f142b).getStringSet(str, new HashSet());
    }

    public String m141a() {
        String b = m139b();
        Map all = m138a(C0314c.f155b).getAll();
        if (TextUtils.isEmpty(b) || all == null || all.isEmpty()) {
            return (!TextUtils.isEmpty(b) || all == null || all.isEmpty()) ? b : new C0313b().m152a(all, C0312a.TWO_DEPTH);
        } else {
            return (b + C0312a.TWO_DEPTH.m150a()) + new C0313b().m152a(all, C0312a.TWO_DEPTH);
        }
    }

    public String toString() {
        return m141a();
    }
}
