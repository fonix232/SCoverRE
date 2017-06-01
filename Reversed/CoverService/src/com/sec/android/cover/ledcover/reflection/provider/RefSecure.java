package com.sec.android.cover.ledcover.reflection.provider;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefSecure extends AbstractBaseReflection {
    private static RefSecure sInstance;
    public String SMS_DEFAULT_APPLICATION;

    public static synchronized RefSecure get() {
        RefSecure refSecure;
        synchronized (RefSecure.class) {
            if (sInstance == null) {
                sInstance = new RefSecure();
            }
            refSecure = sInstance;
        }
        return refSecure;
    }

    protected void loadStaticFields() {
        this.SMS_DEFAULT_APPLICATION = getStringStaticValue("SMS_DEFAULT_APPLICATION");
    }

    protected String getBaseClassName() {
        return "android.provider.Settings$Secure";
    }
}
