package com.sec.android.cover.ledcover.reflection.cover;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefCoverState extends AbstractBaseReflection {
    private static RefCoverState sInstance;

    public static synchronized RefCoverState get() {
        RefCoverState refCoverState;
        synchronized (RefCoverState.class) {
            if (sInstance == null) {
                sInstance = new RefCoverState();
            }
            refCoverState = sInstance;
        }
        return refCoverState;
    }

    public boolean attached(Object instance) {
        return checkBoolean(getNormalValue(instance, "attached"));
    }

    public int color(Object instance) {
        return checkInt(getNormalValue(instance, "color"));
    }

    public int widthPixel(Object instance) {
        return checkInt(getNormalValue(instance, "widthPixel"));
    }

    public int heightPixel(Object instance) {
        return checkInt(getNormalValue(instance, "heightPixel"));
    }

    public int model(Object instance) {
        return checkInt(getNormalValue(instance, "model"));
    }

    public boolean getSwitchState(Object instance) {
        return checkBoolean(invokeNormalMethod(instance, "getSwitchState"));
    }

    public int getType(Object instance) {
        return checkInt(invokeNormalMethod(instance, "getType"));
    }

    public boolean isFakeCover(Object instance) {
        return checkBoolean(invokeNormalMethod(instance, "isFakeCover"));
    }

    public int fotaMode(Object instance) {
        return checkInt(getNormalValue(instance, "fotaMode"));
    }

    protected String getBaseClassName() {
        return "com.samsung.android.cover.CoverState";
    }
}
