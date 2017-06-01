package com.samsung.context.sdk.samsunganalytics.p000a;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.System;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.LogBuilders.ExceptionBuilder;
import com.samsung.context.sdk.samsunganalytics.LogBuilders.ScreenViewBuilder;
import com.samsung.context.sdk.samsunganalytics.UserAgreement;
import com.samsung.context.sdk.samsunganalytics.p000a.p001a.C0273b;
import com.samsung.context.sdk.samsunganalytics.p000a.p001a.C0274c;
import com.samsung.context.sdk.samsunganalytics.p000a.p002b.C0281a;
import com.samsung.context.sdk.samsunganalytics.p000a.p003c.C0285c;
import com.samsung.context.sdk.samsunganalytics.p000a.p003c.C0285c.C0284a;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0287b;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0428d;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0292b;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0293d;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0294e;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0308c;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.C0306a;
import com.samsung.context.sdk.samsunganalytics.p000a.p012h.C0435a;
import com.samsung.context.sdk.samsunganalytics.p000a.p012h.C0436b;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0314c;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0315d;
import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.b */
public class C0282b {
    public static final int f43a = 128;
    private static final int f44b = 0;
    private static final int f45c = 1;
    private static final int f46d = 2;
    private Application f47e;
    private UncaughtExceptionHandler f48f;
    private UncaughtExceptionHandler f49g;
    private boolean f50h;
    private boolean f51i;
    private ActivityLifecycleCallbacks f52j;
    private Configuration f53k;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.2 */
    class C02772 extends BroadcastReceiver {
        final /* synthetic */ C0282b f29a;

        C02772(C0282b c0282b) {
            this.f29a = c0282b;
        }

        public void onReceive(Context context, Intent intent) {
            C0311a.m143a("receive BR");
            this.f29a.m46n();
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.4 */
    class C02784 implements UncaughtExceptionHandler {
        final /* synthetic */ C0282b f30a;

        C02784(C0282b c0282b) {
            this.f30a = c0282b;
        }

        public void uncaughtException(Thread thread, Throwable th) {
            if (this.f30a.f50h) {
                C0311a.m149e("uncaughtException");
                this.f30a.m48a(((ExceptionBuilder) new ExceptionBuilder().setMessage(C0285c.m58a(C0284a.SIMPLE).m57a(thread.getName(), th)).set("pn", thread.getName())).isCrash(true).build(), false);
                this.f30a.f49g.uncaughtException(thread, th);
                return;
            }
            this.f30a.f49g.uncaughtException(thread, th);
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.5 */
    class C02795 implements ActivityLifecycleCallbacks {
        final /* synthetic */ C0282b f31a;

        C02795(C0282b c0282b) {
            this.f31a = c0282b;
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
            this.f31a.m48a(((ScreenViewBuilder) new ScreenViewBuilder().setScreenView(activity.getComponentName().getShortClassName())).build(), false);
        }

        public void onActivityStopped(Activity activity) {
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.7 */
    class C02807 extends BroadcastReceiver {
        final /* synthetic */ C0282b f32a;

        C02807(C0282b c0282b) {
            this.f32a = c0282b;
        }

        public void onReceive(Context context, Intent intent) {
            int i;
            String stringExtra = intent.getStringExtra("DID");
            if (TextUtils.isEmpty(stringExtra)) {
                stringExtra = this.f32a.m45m();
                i = C0282b.f45c;
                C0311a.m148d("Get CF id empty");
            } else {
                i = C0282b.f44b;
                C0311a.m148d("Get CF id");
            }
            this.f32a.m33a(stringExtra, i);
            this.f32a.f47e.getApplicationContext().unregisterReceiver(this);
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.1 */
    class C04231 implements UserAgreement {
        final /* synthetic */ Application f172a;
        final /* synthetic */ C0282b f173b;

        C04231(C0282b c0282b, Application application) {
            this.f173b = c0282b;
            this.f172a = application;
        }

        public boolean isAgreement() {
            return System.getInt(this.f172a.getContentResolver(), "samsung_errorlog_agree", C0282b.f44b) == C0282b.f45c;
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.3 */
    class C04243 implements C0276a<Void, Boolean> {
        final /* synthetic */ C0282b f174a;

        C04243(C0282b c0282b) {
            this.f174a = c0282b;
        }

        public Void m163a(Boolean bool) {
            if (bool.booleanValue()) {
                C0306a.m117a(this.f174a.f47e.getApplicationContext(), Boolean.valueOf(true)).m121a(this.f174a.f47e.getApplicationContext());
            }
            return null;
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.b.6 */
    class C04256 implements C0287b {
        final /* synthetic */ Map f175a;
        final /* synthetic */ C0282b f176b;

        C04256(C0282b c0282b, Map map) {
            this.f176b = c0282b;
            this.f175a = map;
        }

        public void m164a() {
            SharedPreferences sharedPreferences = this.f176b.f47e.getSharedPreferences(C0314c.f155b, C0282b.f44b);
            for (String str : this.f175a.keySet()) {
                sharedPreferences.edit().putString(str, (String) this.f175a.get(str)).apply();
            }
        }

        public int m165b() {
            return C0282b.f44b;
        }
    }

    public C0282b(Application application, Configuration configuration) {
        this.f50h = false;
        this.f51i = false;
        this.f47e = application;
        this.f53k = configuration;
        if (!TextUtils.isEmpty(configuration.getDeviceId())) {
            this.f53k.setAuidType(f46d);
        }
        if (configuration.isEnableAutoDeviceId()) {
            m44l();
        }
        if (configuration.isEnableUseInAppLogging()) {
            m41i();
        } else {
            this.f53k.setUserAgreement(new C04231(this, application));
        }
        if (m47o()) {
            if (configuration.isEnableFastReady()) {
                C0308c.m131a(application, null, configuration);
            }
            m46n();
        }
        C0311a.m144a("Tracker", "Tracker start:1.8.22");
    }

    private void m33a(String str, int i) {
        C0314c.m153a(this.f47e.getApplicationContext()).edit().putString(C0314c.f156c, str).putInt(C0314c.f157d, i).commit();
        this.f53k.setAuidType(i);
        this.f53k.setDeviceId(str);
    }

    private boolean m34a(String str) {
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(this.f47e.getApplicationContext().getPackageManager().getPackageInfo(str, f44b).versionName, ".");
            int parseInt = Integer.parseInt(stringTokenizer.nextToken());
            int parseInt2 = Integer.parseInt(stringTokenizer.nextToken());
            int parseInt3 = Integer.parseInt(stringTokenizer.nextToken());
            if (parseInt < f46d) {
                C0311a.m143a("CF version < 2.0.9");
                return false;
            } else if (parseInt != f46d || parseInt2 != 0 || parseInt3 >= 9) {
                return true;
            } else {
                C0311a.m143a("CF version < 2.0.9");
                return false;
            }
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
            return false;
        }
    }

    private void m36b(Map<String, String> map) {
        map.remove("t");
        C0428d.m168a().m63a(new C04256(this, map));
        if (!C0435a.m190c() || !this.f53k.isAlwaysRunningApp()) {
            return;
        }
        if (this.f53k.isEnableUseInAppLogging() || C0294e.m85a()) {
            m40h();
        }
    }

    private void m40h() {
        if (C0435a.m190c()) {
            C0435a.m189a(false);
        }
        C0311a.m143a("register BR");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        this.f47e.getApplicationContext().registerReceiver(new C02772(this), intentFilter);
    }

    private void m41i() {
        SharedPreferences a = C0314c.m153a(this.f47e);
        C0274c.DLS.m18a(a.getString(C0292b.f72h, C0316a.f163d));
        C0273b.DLS_DIR.m16a(a.getString(C0292b.f73i, C0316a.f163d));
        C0273b.DLS_DIR_BAT.m16a(a.getString(C0292b.f74j, C0316a.f163d));
        if (C0315d.m158a(f45c, Long.valueOf(a.getLong(C0292b.f79o, 0)))) {
            a.edit().putLong(C0292b.f79o, System.currentTimeMillis()).putInt(C0292b.f69e, f44b).putInt(C0292b.f68d, f44b).commit();
        }
        if (C0315d.m158a(a.getInt(C0292b.f65a, f45c), Long.valueOf(a.getLong(C0292b.f78n, 0)))) {
            C0293d.m78a(this.f47e, this.f53k, C0428d.m168a(), new C0281a(this.f47e), new C04243(this));
        }
    }

    private ActivityLifecycleCallbacks m42j() {
        if (this.f52j != null) {
            return this.f52j;
        }
        this.f52j = new C02795(this);
        return this.f52j;
    }

    private boolean m43k() {
        String str = "com.samsung.android.providers.context";
        str = ".log.action.REQUEST_DID";
        str = ".log.action.GET_DID";
        str = "PKGNAME";
        if (!C0294e.m85a() || this.f53k.isEnableUseInAppLogging() || !TextUtils.isEmpty(this.f53k.getUserId()) || !m34a("com.samsung.android.providers.context")) {
            return false;
        }
        Intent intent = new Intent("com.samsung.android.providers.context.log.action.REQUEST_DID");
        intent.putExtra("PKGNAME", this.f47e.getPackageName());
        intent.setPackage("com.samsung.android.providers.context");
        this.f47e.getApplicationContext().sendBroadcast(intent);
        IntentFilter intentFilter = new IntentFilter("com.samsung.android.providers.context.log.action.GET_DID");
        this.f47e.getApplicationContext().registerReceiver(new C02807(this), intentFilter);
        return true;
    }

    private void m44l() {
        SharedPreferences a = C0314c.m153a(this.f47e);
        String string = a.getString(C0314c.f156c, C0316a.f163d);
        int i = a.getInt(C0314c.f157d, -1);
        if ((TextUtils.isEmpty(string) || string.length() != 32) && !m43k()) {
            string = m45m();
            i = f45c;
        }
        m33a(string, i);
    }

    private String m45m() {
        String str = "0123456789abcdefghijklmjopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom secureRandom = new SecureRandom();
        byte[] bArr = new byte[16];
        StringBuilder stringBuilder = new StringBuilder(32);
        int i = f44b;
        while (i < 32) {
            secureRandom.nextBytes(bArr);
            try {
                stringBuilder.append("0123456789abcdefghijklmjopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int) (Math.abs(new BigInteger(bArr).longValue()) % ((long) "0123456789abcdefghijklmjopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length()))));
                i += f45c;
            } catch (Exception e) {
                C0311a.m142a(getClass(), e);
                return null;
            }
        }
        return stringBuilder.toString();
    }

    private void m46n() {
        if (!C0315d.m158a(7, Long.valueOf(C0314c.m153a(this.f47e).getLong(C0314c.f159f, 0)))) {
            return;
        }
        if (m47o()) {
            C0428d.m168a().m63a(new C0435a(this.f47e, this.f53k));
        } else {
            C0311a.m148d("user do not agree");
        }
    }

    private boolean m47o() {
        return this.f53k.getUserAgreement().isAgreement();
    }

    public int m48a(Map<String, String> map, boolean z) {
        if (!m47o()) {
            C0311a.m148d("user do not agree");
            return -2;
        } else if (map == null || map.isEmpty()) {
            C0311a.m148d("Failure to send Logs : No data");
            return -3;
        } else if (!((String) map.get("t")).equalsIgnoreCase("st")) {
            return z ? C0308c.m131a(this.f47e, null, this.f53k).m109e(map) : C0308c.m131a(this.f47e, null, this.f53k).m108d(map);
        } else {
            m36b((Map) map);
            return f44b;
        }
    }

    public void m49a() {
        this.f50h = true;
        if (this.f48f == null) {
            this.f49g = Thread.getDefaultUncaughtExceptionHandler();
            this.f48f = new C02784(this);
            Thread.setDefaultUncaughtExceptionHandler(this.f48f);
        }
    }

    public void m50a(Map<String, Set<String>> map) {
        C0428d.m168a().m63a(new C0436b(C0314c.m153a(this.f47e), map));
        if (!C0435a.m190c() || !this.f53k.isAlwaysRunningApp()) {
            return;
        }
        if (this.f53k.isEnableUseInAppLogging() || C0294e.m85a()) {
            m40h();
        }
    }

    public void m51b() {
        this.f50h = false;
    }

    public void m52c() {
        this.f47e.registerActivityLifecycleCallbacks(m42j());
    }

    public void m53d() {
        if (this.f52j != null) {
            this.f47e.unregisterActivityLifecycleCallbacks(this.f52j);
        }
    }

    public boolean m54e() {
        return this.f50h;
    }

    public boolean m55f() {
        return this.f51i;
    }

    public Configuration m56g() {
        return this.f53k;
    }
}
