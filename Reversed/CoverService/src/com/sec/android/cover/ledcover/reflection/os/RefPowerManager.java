package com.sec.android.cover.ledcover.reflection.os;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefPowerManager extends AbstractBaseReflection {
    private static RefPowerManager sInstance;
    public int WAKE_UP_REASON_COVER_OPEN;

    public static synchronized RefPowerManager get() {
        RefPowerManager refPowerManager;
        synchronized (RefPowerManager.class) {
            if (sInstance == null) {
                sInstance = new RefPowerManager();
            }
            refPowerManager = sInstance;
        }
        return refPowerManager;
    }

    protected void loadStaticFields() {
        this.WAKE_UP_REASON_COVER_OPEN = getIntStaticValue("WAKE_UP_REASON_COVER_OPEN");
    }

    public void getCurrentBrightness(Object instance, boolean ratio) {
        invokeNormalMethod(instance, "getCurrentBrightness", new Class[]{Boolean.TYPE}, Boolean.valueOf(ratio));
    }

    protected String getBaseClassName() {
        return "android.os.PowerManager";
    }
}
