package com.samsung.android.contextaware.utilbundle;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CaCurrentUtcTimeManager {
    private static volatile CaCurrentUtcTimeManager instance;

    public static CaCurrentUtcTimeManager getInstance() {
        if (instance == null) {
            synchronized (CaCurrentUtcTimeManager.class) {
                if (instance == null) {
                    instance = new CaCurrentUtcTimeManager();
                }
            }
        }
        return instance;
    }

    public final int[] getUtcTime() {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        int[] iArr = new int[3];
        int i = 0;
        if (gregorianCalendar.get(9) == 1) {
            i = 12;
        }
        iArr[0] = gregorianCalendar.get(10) + i;
        iArr[1] = gregorianCalendar.get(12);
        iArr[2] = gregorianCalendar.get(13);
        return iArr;
    }
}
