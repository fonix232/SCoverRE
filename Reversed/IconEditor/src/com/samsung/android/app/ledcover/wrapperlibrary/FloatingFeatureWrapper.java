package com.samsung.android.app.ledcover.wrapperlibrary;

import com.samsung.android.app.ledcover.interfacelibrary.FloatingFeatureInterface;
import com.samsung.android.app.ledcover.sdllibrary.SdlFloatingFeature;
import com.samsung.android.app.ledcover.selibrary.SeFloatingFeature;
import com.samsung.android.app.ledcover.wrapperlibrary.utils.Platformutils;

public class FloatingFeatureWrapper {
    private static FloatingFeatureInterface instance;

    static {
        if (Platformutils.isSemDevice()) {
            instance = new SeFloatingFeature();
        } else {
            instance = new SdlFloatingFeature();
        }
    }

    public static boolean getBoolean(String tag) {
        return instance.getBoolean(tag);
    }
}
