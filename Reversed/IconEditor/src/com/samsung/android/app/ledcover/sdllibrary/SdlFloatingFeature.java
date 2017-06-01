package com.samsung.android.app.ledcover.sdllibrary;

import com.samsung.android.app.ledcover.interfacelibrary.FloatingFeatureInterface;
import com.samsung.android.feature.FloatingFeature;

public class SdlFloatingFeature implements FloatingFeatureInterface {
    public boolean getBoolean(String tag) {
        return FloatingFeature.getInstance().getEnableStatus(tag);
    }
}
