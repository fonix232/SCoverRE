package com.samsung.context.sdk.samsunganalytics;

import android.app.Application;
import android.content.Context;
import com.samsung.context.sdk.samsunganalytics.p000a.C0282b;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0294e;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0315d;
import java.util.Map;
import java.util.Set;

public class SamsungAnalytics {
    public static final String SDK_VERSION = "1.8.22";
    private static SamsungAnalytics instance;
    private C0282b tracker;

    private SamsungAnalytics(Application application, Configuration configuration) {
        this.tracker = null;
        if (!C0294e.m86a((Context) application, configuration)) {
            return;
        }
        if (configuration.isEnableUseInAppLogging()) {
            this.tracker = new C0282b(application, configuration);
        } else if (C0294e.m85a()) {
            this.tracker = new C0282b(application, configuration);
        }
    }

    public static SamsungAnalytics getInstance() {
        if (instance == null) {
            C0315d.m156a("call after setConfiguration() method");
            if (!C0315d.m157a()) {
                return getInstanceAndConfig(null, null);
            }
        }
        return instance;
    }

    private static SamsungAnalytics getInstanceAndConfig(Application application, Configuration configuration) {
        if (instance == null) {
            synchronized (SamsungAnalytics.class) {
                instance = new SamsungAnalytics(application, configuration);
            }
        }
        return instance;
    }

    public static void setConfiguration(Application application, Configuration configuration) {
        getInstanceAndConfig(application, configuration);
    }

    public void disableAutoActivityTracking() {
        try {
            this.tracker.m53d();
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
    }

    public void disableUncaughtExceptionLogging() {
        try {
            this.tracker.m51b();
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
    }

    public SamsungAnalytics enableAutoActivityTracking() {
        try {
            this.tracker.m52c();
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
        return this;
    }

    public SamsungAnalytics enableUncaughtExceptionLogging() {
        try {
            this.tracker.m49a();
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
        return this;
    }

    public boolean isEnableAutoActivityTracking() {
        try {
            return this.tracker.m55f();
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
            return false;
        }
    }

    public boolean isEnableUncaughtExceptionLogging() {
        try {
            return this.tracker.m54e();
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
            return false;
        }
    }

    public void registerSettingPref(Map<String, Set<String>> map) {
        try {
            this.tracker.m50a((Map) map);
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
    }

    public void restrictNetworkType(int i) {
        try {
            this.tracker.m56g().setRestrictedNetworkType(i);
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
        }
    }

    public int sendLog(Map<String, String> map) {
        try {
            return this.tracker.m48a((Map) map, false);
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
            return -100;
        }
    }

    public int sendLogSync(Map<String, String> map) {
        try {
            return this.tracker.m48a((Map) map, true);
        } catch (Exception e) {
            C0311a.m142a(getClass(), e);
            return -100;
        }
    }
}
