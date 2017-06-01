package com.samsung.context.sdk.samsunganalytics.p000a.p012h;

import android.content.SharedPreferences;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0287b;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0314c;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.h.b */
public class C0436b implements C0287b {
    private SharedPreferences f213a;
    private Map<String, Set<String>> f214b;

    public C0436b(SharedPreferences sharedPreferences, Map<String, Set<String>> map) {
        this.f213a = sharedPreferences;
        this.f214b = map;
    }

    public void m193a() {
        for (String remove : this.f213a.getStringSet(C0314c.f158e, new HashSet())) {
            this.f213a.edit().remove(remove).apply();
        }
        Set hashSet = new HashSet();
        this.f213a.edit().remove(C0314c.f158e).apply();
        for (Entry entry : this.f214b.entrySet()) {
            String str = (String) entry.getKey();
            hashSet.add(str);
            this.f213a.edit().putStringSet(str, (Set) entry.getValue()).commit();
        }
        this.f213a.edit().putStringSet(C0314c.f158e, hashSet).commit();
    }

    public int m194b() {
        C0311a.m143a("RegisterClient:" + this.f213a.getAll().toString());
        return 0;
    }
}
