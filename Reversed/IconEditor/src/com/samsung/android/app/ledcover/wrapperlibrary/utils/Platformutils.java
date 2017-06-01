package com.samsung.android.app.ledcover.wrapperlibrary.utils;

import android.content.Context;
import android.os.Build.VERSION;

public class Platformutils {
    public static final boolean isSemDevice(Context context) {
        if (context.getPackageManager().hasSystemFeature("com.samsung.feature.samsung_experience_mobile")) {
            return true;
        }
        return false;
    }

    public static final boolean isSemDevice() {
        if (ReflectUtils.getField(VERSION.class, "SEM_INT") != null) {
            return true;
        }
        return false;
    }
}
