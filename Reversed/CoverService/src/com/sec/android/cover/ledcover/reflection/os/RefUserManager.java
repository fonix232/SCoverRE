package com.sec.android.cover.ledcover.reflection.os;

import android.os.UserManager;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;
import java.util.List;

public class RefUserManager extends AbstractBaseReflection {
    private static RefUserManager sInstance;

    public static synchronized RefUserManager get() {
        RefUserManager refUserManager;
        synchronized (RefUserManager.class) {
            if (sInstance == null) {
                sInstance = new RefUserManager();
            }
            refUserManager = sInstance;
        }
        return refUserManager;
    }

    public List<Object> getUsers(Object instance) {
        Object result = invokeNormalMethod(instance, "getUsers");
        return result == null ? null : (List) result;
    }

    public UserManager get(Object context) {
        Object result = invokeStaticMethod("get", new Class[]{loadClassIfNeeded("android.content.Context")}, context);
        return result == null ? null : (UserManager) result;
    }

    protected String getBaseClassName() {
        return "android.os.UserManager";
    }
}
