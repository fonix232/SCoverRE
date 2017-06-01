package com.sec.android.cover.ledcover.reflection.location;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefCountryDetector extends AbstractBaseReflection {
    private static RefCountryDetector sInstance;

    public static synchronized RefCountryDetector get() {
        RefCountryDetector refCountryDetector;
        synchronized (RefCountryDetector.class) {
            if (sInstance == null) {
                sInstance = new RefCountryDetector();
            }
            refCountryDetector = sInstance;
        }
        return refCountryDetector;
    }

    protected void loadStaticFields() {
    }

    public Object detectCountry(Object instance) {
        return invokeNormalMethod(instance, "detectCountry");
    }

    protected String getBaseClassName() {
        return "android.location.CountryDetector";
    }
}
