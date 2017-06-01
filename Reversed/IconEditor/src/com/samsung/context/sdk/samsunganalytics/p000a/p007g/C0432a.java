package com.samsung.context.sdk.samsunganalytics.p000a.p007g;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.android.app.ledcover.update.StubCodes;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.p002b.C0281a;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0288c;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0428d;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.C0306a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0313b;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0313b.C0312a;
import java.util.Map;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.a */
public abstract class C0432a implements C0301b {
    protected Context f192a;
    protected Configuration f193b;
    protected C0281a f194c;
    protected C0313b<String, String> f195d;
    protected C0306a f196e;
    protected C0288c f197f;

    public C0432a(Context context, Configuration configuration) {
        this.f192a = context.getApplicationContext();
        this.f193b = configuration;
        this.f197f = C0428d.m168a();
        this.f194c = new C0281a(context);
        this.f195d = new C0313b();
        this.f196e = C0306a.m116a(context, configuration);
    }

    protected Map<String, String> m178a(Map<String, String> map) {
        map.put("v", C0316a.f165f);
        map.put("tid", this.f193b.getTrackingId());
        map.put("la", this.f194c.m24d());
        if (!TextUtils.isEmpty(this.f194c.m21a())) {
            map.put("mcc", this.f194c.m21a());
        }
        if (!TextUtils.isEmpty(this.f194c.m22b())) {
            map.put("mnc", this.f194c.m22b());
        }
        map.put("dm", this.f194c.m27g());
        map.put("auid", this.f193b.getDeviceId());
        if (this.f193b.isUseAnonymizeIp()) {
            map.put("aip", StubCodes.UPDATE_CHECK_UPDATE_NOT_NECESSARY);
            String overrideIp = this.f193b.getOverrideIp();
            if (overrideIp != null) {
                map.put("oip", overrideIp);
            }
        }
        if (!TextUtils.isEmpty(this.f193b.getUserId())) {
            map.put("uid", this.f193b.getUserId());
        }
        map.put("do", this.f194c.m25e());
        map.put("av", this.f194c.m28h());
        map.put("uv", this.f193b.getVersion());
        map.put("tz", this.f194c.m29i());
        map.put("at", String.valueOf(this.f193b.getAuidType()));
        map.put("fv", this.f194c.m30j());
        return map;
    }

    protected boolean m179a() {
        if (!TextUtils.isEmpty(this.f193b.getDeviceId())) {
            return true;
        }
        C0311a.m144a("Log Sender", "Device id is empty");
        return false;
    }

    protected String m180b(Map<String, String> map) {
        return this.f195d.m152a(map, C0312a.ONE_DEPTH);
    }

    protected void m181c(Map<String, String> map) {
        this.f196e.m120a(Long.valueOf((String) map.get("ts")).longValue(), (String) map.get("t"), m180b(m178a(map)));
    }
}
