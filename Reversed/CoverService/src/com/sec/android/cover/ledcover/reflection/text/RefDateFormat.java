package com.sec.android.cover.ledcover.reflection.text;

import android.content.Context;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefDateFormat extends AbstractBaseReflection {
    private static RefDateFormat sInstance;

    public static synchronized RefDateFormat get() {
        RefDateFormat refDateFormat;
        synchronized (RefDateFormat.class) {
            if (sInstance == null) {
                sInstance = new RefDateFormat();
            }
            refDateFormat = sInstance;
        }
        return refDateFormat;
    }

    public boolean is24HourFormat(Context context, int userHandle) {
        return checkBoolean(invokeStaticMethod("is24HourFormat", new Class[]{Context.class, Integer.TYPE}, context, Integer.valueOf(userHandle)));
    }

    protected String getBaseClassName() {
        return "android.text.format.DateFormat";
    }
}
