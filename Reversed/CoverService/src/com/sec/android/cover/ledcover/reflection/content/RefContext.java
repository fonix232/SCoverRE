package com.sec.android.cover.ledcover.reflection.content;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.os.UserHandle;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefContext extends AbstractBaseReflection {
    private static RefContext sInstance;
    public String COUNTRY_DETECTOR;

    public static synchronized RefContext get() {
        RefContext refContext;
        synchronized (RefContext.class) {
            if (sInstance == null) {
                sInstance = new RefContext();
            }
            refContext = sInstance;
        }
        return refContext;
    }

    protected void loadStaticFields() {
        this.COUNTRY_DETECTOR = getStringStaticValue("COUNTRY_DETECTOR");
    }

    public Context createPackageContextAsUser(Object instance, String packageName, int flags, UserHandle user) throws NameNotFoundException, NotFoundException {
        Object result = invokeNormalMethod(instance, "createPackageContextAsUser", new Class[]{String.class, Integer.TYPE, UserHandle.class}, packageName, Integer.valueOf(flags), user);
        return result == null ? null : (Context) result;
    }

    protected String getBaseClassName() {
        return "android.content.Context";
    }
}
