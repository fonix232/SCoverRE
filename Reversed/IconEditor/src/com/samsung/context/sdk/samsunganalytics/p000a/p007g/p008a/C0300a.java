package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p008a;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.samsung.context.sdk.samsunganalytics.p000a.C0276a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.sec.spp.push.dlc.api.IDlcService;
import com.sec.spp.push.dlc.api.IDlcService.C0438a;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.a.a */
public class C0300a {
    private static final String f93a = "com.sec.spp.push.REQUEST_REGISTER";
    private static final String f94b = "com.sec.spp.push.REQUEST_DEREGISTER";
    private static String f95c = null;
    private static String f96d = null;
    private static final String f97e = "EXTRA_PACKAGENAME";
    private static final String f98f = "EXTRA_INTENTFILTER";
    private static final String f99g = "EXTRA_STR";
    private static final String f100h = "EXTRA_RESULT_CODE";
    private static final String f101i = "EXTRA_STR_ACTION";
    private static final int f102j = 100;
    private static final int f103k = 200;
    private static final int f104l = -2;
    private static final int f105m = -3;
    private static final int f106n = -4;
    private static final int f107o = -5;
    private static final int f108p = -6;
    private static final int f109q = -7;
    private static final int f110r = -8;
    private Context f111s;
    private BroadcastReceiver f112t;
    private String f113u;
    private C0276a f114v;
    private boolean f115w;
    private boolean f116x;
    private IDlcService f117y;
    private ServiceConnection f118z;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.a.a.1 */
    class C02981 implements ServiceConnection {
        final /* synthetic */ C0300a f91a;

        C02981(C0300a c0300a) {
            this.f91a = c0300a;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            C0311a.m144a("DLC Sender", "DLC Client ServiceConnected");
            this.f91a.f117y = C0438a.m196a(iBinder);
            if (this.f91a.f112t != null) {
                this.f91a.f111s.unregisterReceiver(this.f91a.f112t);
                this.f91a.f112t = null;
            }
            if (this.f91a.f114v != null) {
                this.f91a.f114v.m20a(null);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            C0311a.m144a("DLC Sender", "Client ServiceDisconnected");
            this.f91a.f117y = null;
            this.f91a.f115w = false;
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.a.a.2 */
    class C02992 extends BroadcastReceiver {
        final /* synthetic */ C0300a f92a;

        C02992(C0300a c0300a) {
            this.f92a = c0300a;
        }

        public void onReceive(Context context, Intent intent) {
            this.f92a.f116x = false;
            if (intent == null) {
                C0311a.m144a("DLC Sender", "dlc register reply fail");
                return;
            }
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            if (action == null || extras == null) {
                C0311a.m144a("DLC Sender", "dlc register reply fail");
            } else if (action.equals(this.f92a.f113u)) {
                action = extras.getString(C0300a.f99g);
                int i = extras.getInt(C0300a.f100h);
                C0311a.m144a("DLC Sender", "register DLC result:" + action);
                if (i < 0) {
                    C0311a.m144a("DLC Sender", "register DLC result fail:" + action);
                    return;
                }
                this.f92a.m97a(extras.getString(C0300a.f101i));
            }
        }
    }

    static {
        f95c = "com.sec.spp.push";
        f96d = "com.sec.spp.push.dlc.writer.WriterService";
    }

    public C0300a(Context context) {
        this.f115w = false;
        this.f116x = false;
        this.f118z = new C02981(this);
        this.f111s = context;
        this.f113u = context.getPackageName();
        this.f113u += ".REGISTER_FILTER";
    }

    public C0300a(Context context, C0276a c0276a) {
        this(context);
        this.f114v = c0276a;
    }

    private void m97a(String str) {
        if (this.f115w) {
            m103e();
        }
        try {
            Intent intent = new Intent(str);
            intent.setClassName(f95c, f96d);
            this.f115w = this.f111s.bindService(intent, this.f118z, 1);
            C0311a.m144a("DLCBinder", "bind");
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
    }

    private void m103e() {
        if (this.f115w) {
            try {
                C0311a.m144a("DLCBinder", "unbind");
                this.f111s.unbindService(this.f118z);
                this.f115w = false;
            } catch (Exception e) {
                C0311a.m142a(getClass(), e);
            }
        }
    }

    public void m104a() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(this.f113u);
        if (this.f112t == null) {
            this.f112t = new C02992(this);
        }
        this.f111s.registerReceiver(this.f112t, intentFilter);
    }

    public void m105b() {
        if (this.f112t == null) {
            m104a();
        }
        if (this.f116x) {
            C0311a.m144a("DLCBinder", "already send register request");
            return;
        }
        Intent intent = new Intent(f93a);
        intent.putExtra(f97e, this.f111s.getPackageName());
        intent.putExtra(f98f, this.f113u);
        this.f111s.sendBroadcast(intent);
        this.f116x = true;
        C0311a.m144a("DLCBinder", "send register Request");
        C0311a.m143a("send register Request:" + this.f111s.getPackageName());
    }

    public boolean m106c() {
        return this.f115w;
    }

    public IDlcService m107d() {
        return this.f117y;
    }
}
