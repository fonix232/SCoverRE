package com.sec.android.cover.ledcover.reflection.hardware;

import android.hardware.input.InputManager;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefInputManager extends AbstractBaseReflection {
    private static RefInputManager sInstance;

    public static synchronized RefInputManager get() {
        RefInputManager refInputManager;
        synchronized (RefInputManager.class) {
            if (sInstance == null) {
                sInstance = new RefInputManager();
            }
            refInputManager = sInstance;
        }
        return refInputManager;
    }

    public InputManager getInstance() {
        Object result = invokeStaticMethod("getInstance");
        return result == null ? null : (InputManager) result;
    }

    public void coverEventFinished(Object instance) {
        invokeNormalMethod(instance, "coverEventFinished");
    }

    protected String getBaseClassName() {
        return "android.hardware.input.InputManager";
    }
}
