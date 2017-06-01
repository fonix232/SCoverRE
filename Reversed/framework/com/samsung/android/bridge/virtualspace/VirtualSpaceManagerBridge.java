package com.samsung.android.bridge.virtualspace;

import android.content.Context;
import android.util.Log;
import java.lang.reflect.Constructor;

public class VirtualSpaceManagerBridge {
    private static String CLASSNAME = "com.samsung.android.virtualspace.SemVirtualSpaceManager";
    private static String TAG = "SemVirtualSpace";
    private static Constructor sConstructor;

    static {
        try {
            sConstructor = Class.forName(CLASSNAME).getConstructor(new Class[]{Context.class});
        } catch (Throwable e) {
            Log.m32e(TAG, "Couldn't find SemVirtualSpaceManager class or constructor: ", e);
        }
    }

    public static IVirtualSpaceManager newInstance(Context context) {
        if (sConstructor != null) {
            try {
                return (IVirtualSpaceManager) sConstructor.newInstance(new Object[]{context});
            } catch (Throwable e) {
                Log.m32e(TAG, "Couldn't create SemVirtualSpaceManager instance: ", e);
            }
        }
        return null;
    }
}
