package com.sec.android.cover.ledcover.reflection.content;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefUserInfo extends AbstractBaseReflection {
    private static RefUserInfo sInstance;

    public static synchronized RefUserInfo get() {
        RefUserInfo refUserInfo;
        synchronized (RefUserInfo.class) {
            if (sInstance == null) {
                sInstance = new RefUserInfo();
            }
            refUserInfo = sInstance;
        }
        return refUserInfo;
    }

    public int id(Object instance) {
        return checkInt(getNormalValue(instance, "id"));
    }

    public boolean isBMode(Object instance) {
        return checkBoolean(invokeNormalMethod(instance, "isBMode"));
    }

    protected String getBaseClassName() {
        return "android.content.pm.UserInfo";
    }
}
