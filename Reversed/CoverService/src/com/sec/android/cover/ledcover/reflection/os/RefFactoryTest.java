package com.sec.android.cover.ledcover.reflection.os;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefFactoryTest extends AbstractBaseReflection {
    private static RefFactoryTest sInstance;

    public static synchronized RefFactoryTest get() {
        RefFactoryTest refFactoryTest;
        synchronized (RefFactoryTest.class) {
            if (sInstance == null) {
                sInstance = new RefFactoryTest();
            }
            refFactoryTest = sInstance;
        }
        return refFactoryTest;
    }

    public boolean isFactoryBinary() {
        return checkBoolean(invokeStaticMethod("isFactoryBinary"));
    }

    public boolean isRunningFactoryApp() {
        return checkBoolean(invokeStaticMethod("isRunningFactoryApp"));
    }

    protected String getBaseClassName() {
        return "android.os.FactoryTest";
    }
}
