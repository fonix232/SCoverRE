package com.samsung.android.app.ledcover.selibrary;

import com.samsung.android.app.ledcover.interfacelibrary.FloatingFeatureInterface;
import com.samsung.android.feature.SemFloatingFeature;

public class SeFloatingFeature implements FloatingFeatureInterface {
    public boolean getBoolean(String tag) {
        return SemFloatingFeature.getInstance().getBoolean(tag);
    }
}
