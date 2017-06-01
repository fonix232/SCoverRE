package com.samsung.context.sdk.samsunganalytics.p000a.p013i;

import android.os.Build;
import com.samsung.context.sdk.samsunganalytics.AnalyticsException;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import java.util.GregorianCalendar;
import java.util.Map;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.i.d */
public class C0315d {
    private C0315d() {
    }

    public static long m154a(int i) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
        gregorianCalendar.add(5, i * -1);
        return gregorianCalendar.getTimeInMillis();
    }

    public static String m155a(Map<String, String> map) {
        String str;
        String str2;
        if (((String) map.get("t")).equals("pv")) {
            str = "page: " + ((String) map.get("pn"));
            str2 = "detail: " + ((String) map.get("pd")) + "  value: " + ((String) map.get("pv"));
        } else if (((String) map.get("t")).equals("ev")) {
            str = "event: " + ((String) map.get("en"));
            str2 = "detail: " + ((String) map.get("ed")) + "  value: " + ((String) map.get("ev"));
        } else if (((String) map.get("t")).equals("st")) {
            str = NotificationCompatApi24.CATEGORY_STATUS;
            str2 = (String) map.get("sti");
        } else {
            str = C0316a.f163d;
            str2 = C0316a.f163d;
        }
        return str + "\n" + str2;
    }

    public static void m156a(String str) {
        if (C0315d.m157a()) {
            throw new AnalyticsException(str);
        }
        C0311a.m149e(str);
    }

    public static boolean m157a() {
        return Build.TYPE.equals("eng");
    }

    public static boolean m158a(int i, Long l) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(l.longValue());
        gregorianCalendar.add(5, i);
        return new GregorianCalendar().after(gregorianCalendar);
    }
}
