package com.sec.android.cover.ledcover.reflection.os;

import android.os.UserHandle;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefUserHandle extends AbstractBaseReflection {
    private static RefUserHandle sInstance;

    public static synchronized RefUserHandle get() {
        RefUserHandle refUserHandle;
        synchronized (RefUserHandle.class) {
            if (sInstance == null) {
                sInstance = new RefUserHandle();
            }
            refUserHandle = sInstance;
        }
        return refUserHandle;
    }

    public UserHandle getUserHandle(int uid) {
        Object result = createInstance(new Class[]{Integer.TYPE}, Integer.valueOf(uid));
        return result == null ? null : (UserHandle) result;
    }

    protected String getBaseClassName() {
        return "android.os.UserHandle";
    }
}
