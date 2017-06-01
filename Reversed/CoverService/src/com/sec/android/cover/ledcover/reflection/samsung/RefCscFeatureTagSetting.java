package com.sec.android.cover.ledcover.reflection.samsung;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefCscFeatureTagSetting extends AbstractBaseReflection {
    private static RefCscFeatureTagSetting sInstance;
    public String TAG_CSCFEATURE_SETTING_CONFIGOPMENUSTRUCTURE;

    public static synchronized RefCscFeatureTagSetting get() {
        RefCscFeatureTagSetting refCscFeatureTagSetting;
        synchronized (RefCscFeatureTagSetting.class) {
            if (sInstance == null) {
                sInstance = new RefCscFeatureTagSetting();
            }
            refCscFeatureTagSetting = sInstance;
        }
        return refCscFeatureTagSetting;
    }

    protected void loadStaticFields() {
        this.TAG_CSCFEATURE_SETTING_CONFIGOPMENUSTRUCTURE = getStringStaticValue("TAG_CSCFEATURE_SETTING_CONFIGOPMENUSTRUCTURE");
    }

    protected String getBaseClassName() {
        return "com.sec.android.app.CscFeatureTagSetting";
    }
}
