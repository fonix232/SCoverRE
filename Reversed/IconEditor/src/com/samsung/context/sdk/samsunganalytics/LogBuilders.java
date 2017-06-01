package com.samsung.context.sdk.samsunganalytics;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0294e;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0313b;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0313b.C0312a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0315d;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LogBuilders {

    protected static abstract class LogBuilder<T extends LogBuilder> {
        protected Map<String, String> logs;

        private LogBuilder() {
            this.logs = new HashMap();
        }

        public Map<String, String> build() {
            set("ts", String.valueOf(getTimeStamp()));
            return this.logs;
        }

        protected abstract T getThis();

        public long getTimeStamp() {
            return System.currentTimeMillis();
        }

        public final T set(String str, String str2) {
            if (str != null) {
                this.logs.put(str, str2);
            }
            return getThis();
        }

        public T setDimension(Map<String, String> map) {
            set("cd", new C0313b().m152a(map, C0312a.TWO_DEPTH));
            return getThis();
        }

        public T setMetrics(Map<String, Integer> map) {
            set("cm", new C0313b().m152a(map, C0312a.TWO_DEPTH));
            return getThis();
        }

        public T setReferral(String str) {
            set("ch", "rf");
            set("so", str);
            return getThis();
        }

        public T setScreenView(Activity activity) {
            try {
                setScreenView(activity.getComponentName().getShortClassName());
            } catch (Exception e) {
                C0311a.m142a(getClass(), e);
            }
            return getThis();
        }

        @Deprecated
        public T setScreenView(Fragment fragment) {
            try {
                setScreenView(fragment.getActivity().getLocalClassName());
                setScreenViewDetail(fragment.getClass().getSimpleName());
            } catch (Exception e) {
                C0311a.m142a(getClass(), e);
            }
            return getThis();
        }

        public T setScreenView(String str) {
            C0294e.m83a("pn", str);
            set("pn", str);
            return getThis();
        }

        public T setScreenViewDetail(String str) {
            set("pnd", str);
            return getThis();
        }

        public final T setSessionEnd() {
            set("sc", "e");
            return getThis();
        }

        public final T setSessionStart() {
            set("sc", "s");
            return getThis();
        }
    }

    public static class SettingBuilder {
        private Map<String, String> map;

        public SettingBuilder() {
            this.map = new HashMap();
        }

        public Map<String, String> build() {
            this.map.put("t", "st");
            return this.map;
        }

        public final SettingBuilder set(String str, float f) {
            return set(str, Float.toString(f));
        }

        public final SettingBuilder set(String str, int i) {
            return set(str, Integer.toString(i));
        }

        public final SettingBuilder set(String str, String str2) {
            if (str == null) {
                C0315d.m156a("Failure to build logs [setting] : Key cannot be null.");
            } else if (str.equalsIgnoreCase("t")) {
                C0315d.m156a("Failure to build logs [setting] : 't' is reserved word, choose another word.");
            } else {
                this.map.put(str, str2);
            }
            return this;
        }

        public final SettingBuilder set(String str, Set<String> set) {
            String str2 = C0316a.f163d;
            for (String str3 : set) {
                if (!TextUtils.isEmpty(str2)) {
                    str2 = str2 + C0312a.THREE_DEPTH.m150a();
                }
                str2 = str2 + str3;
            }
            return set(str, str2);
        }

        public final SettingBuilder set(String str, boolean z) {
            return set(str, Boolean.toString(z));
        }
    }

    public static class SettingPrefBuilder {
        private Map<String, Set<String>> map;

        public SettingPrefBuilder() {
            this.map = new HashMap();
        }

        private SettingPrefBuilder addAppPref(String str) {
            if (!this.map.containsKey(str) && !TextUtils.isEmpty(str)) {
                this.map.put(str, new HashSet());
            } else if (TextUtils.isEmpty(str)) {
                C0315d.m156a("Failure to build logs [setting preference] : Preference name cannot be null.");
            }
            return this;
        }

        public SettingPrefBuilder addKey(String str, String str2) {
            if (TextUtils.isEmpty(str2)) {
                C0315d.m156a("Failure to build logs [setting preference] : Setting key cannot be null.");
            }
            addAppPref(str);
            ((Set) this.map.get(str)).add(str2);
            return this;
        }

        public SettingPrefBuilder addKeys(String str, Set<String> set) {
            if (set == null || set.isEmpty()) {
                C0315d.m156a("Failure to build logs [setting preference] : Setting keys cannot be null.");
            }
            addAppPref(str);
            Set set2 = (Set) this.map.get(str);
            for (String str2 : set) {
                if (!TextUtils.isEmpty(str2)) {
                    set2.add(str2);
                }
            }
            return this;
        }

        public Map<String, Set<String>> build() {
            C0311a.m143a(this.map.toString());
            return this.map;
        }
    }

    public static class CustomBuilder extends LogBuilder<CustomBuilder> {
        public CustomBuilder() {
            super();
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        protected CustomBuilder getThis() {
            return this;
        }

        public /* bridge */ /* synthetic */ long getTimeStamp() {
            return super.getTimeStamp();
        }
    }

    public static class EventBuilder extends LogBuilder<EventBuilder> {
        public EventBuilder() {
            super();
        }

        public Map<String, String> build() {
            if (!this.logs.containsKey("en")) {
                C0315d.m156a("Failure to build Log : Event name cannot be null");
            }
            set("t", "ev");
            return super.build();
        }

        protected EventBuilder getThis() {
            return this;
        }

        public /* bridge */ /* synthetic */ long getTimeStamp() {
            return super.getTimeStamp();
        }

        public EventBuilder setEventDetail(String str) {
            if (TextUtils.isEmpty(str)) {
                C0315d.m156a("Failure to build Log : Event detail cannot be null");
            }
            set("ed", str);
            return this;
        }

        public EventBuilder setEventName(String str) {
            if (TextUtils.isEmpty(str)) {
                C0315d.m156a("Failure to build Log : Event name cannot be null");
            }
            set("en", str);
            return this;
        }

        public EventBuilder setEventValue(long j) {
            set("ev", String.valueOf(j));
            return this;
        }
    }

    public static class ExceptionBuilder extends LogBuilder<ExceptionBuilder> {
        public ExceptionBuilder() {
            super();
        }

        public Map<String, String> build() {
            set("t", "ex");
            return super.build();
        }

        protected ExceptionBuilder getThis() {
            return this;
        }

        public /* bridge */ /* synthetic */ long getTimeStamp() {
            return super.getTimeStamp();
        }

        public ExceptionBuilder isCrash(boolean z) {
            if (z) {
                set("ext", "cr");
            } else {
                set("ext", "ex");
            }
            return this;
        }

        public ExceptionBuilder setDescription(String str) {
            set("exd", str);
            return this;
        }

        public ExceptionBuilder setMessage(String str) {
            set("exm", str);
            return this;
        }
    }

    public static class ScreenViewBuilder extends LogBuilder<ScreenViewBuilder> {
        public ScreenViewBuilder() {
            super();
        }

        public Map<String, String> build() {
            if (TextUtils.isEmpty((CharSequence) this.logs.get("pn"))) {
                C0315d.m156a("Failure to build Log : Screen name cannot be null");
            } else {
                set("t", "pv");
            }
            return super.build();
        }

        protected ScreenViewBuilder getThis() {
            return this;
        }

        public /* bridge */ /* synthetic */ long getTimeStamp() {
            return super.getTimeStamp();
        }

        public ScreenViewBuilder setScreenValue(int i) {
            set("pv", String.valueOf(i));
            return this;
        }

        public ScreenViewBuilder setScreenViewDepth(int i) {
            set("pd", String.valueOf(i));
            return this;
        }
    }

    private LogBuilders() {
    }
}
