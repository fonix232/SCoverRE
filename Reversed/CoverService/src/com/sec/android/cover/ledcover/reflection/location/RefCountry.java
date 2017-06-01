package com.sec.android.cover.ledcover.reflection.location;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefCountry extends AbstractBaseReflection {
    private static RefCountry sInstance;

    public static synchronized RefCountry get() {
        RefCountry refCountry;
        synchronized (RefCountry.class) {
            if (sInstance == null) {
                sInstance = new RefCountry();
            }
            refCountry = sInstance;
        }
        return refCountry;
    }

    public String getCountryIso(Object instance) {
        return checkString(invokeNormalMethod(instance, "getCountryIso"));
    }

    protected String getBaseClassName() {
        return "android.location.Country";
    }
}
