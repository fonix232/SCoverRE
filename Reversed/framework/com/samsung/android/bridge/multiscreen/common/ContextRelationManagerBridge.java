package com.samsung.android.bridge.multiscreen.common;

import android.content.Context;
import android.content.res.Resources;
import android.os.Debug;
import android.util.Log;
import android.view.Display;
import android.view.WindowManagerImpl;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContextRelationManagerBridge {
    private static final String CONTEXT_RELATION_MANAGER_CLASS_NAME = "com.samsung.android.multiscreen.common.ContextRelationManager";
    private static final boolean DEBUG = true;
    private static final String TAG = "ContextRelationMgrBrdg";
    private static Class<?> sKlassContextRelationManager;
    private static Method sMethodGetInstance;

    public interface IContextRelationManagerBridge {
        void createContext(Context context, Context context2);

        void createDisplay(Context context, Display display);

        void createResources(Context context, Resources resources);

        void createWindowManager(Context context, WindowManagerImpl windowManagerImpl);

        void dump(PrintWriter printWriter, String str, boolean z, boolean z2);

        int getContextRefSize();

        void propagateChangedContextDisplay(Context context, int i);

        void removeContext(Context context);
    }

    static {
        loadKlass();
    }

    public static IContextRelationManagerBridge getInstance() {
        try {
            return (IContextRelationManagerBridge) sMethodGetInstance.invoke(sKlassContextRelationManager, null);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e2) {
            return null;
        }
    }

    static void loadKlass() {
        if (sKlassContextRelationManager == null) {
            try {
                Log.m29d(TAG, "loadKlass() : caller=" + Debug.getCallers(2));
                sKlassContextRelationManager = Class.forName(CONTEXT_RELATION_MANAGER_CLASS_NAME);
                sMethodGetInstance = sKlassContextRelationManager.getMethod("getInstance", null);
            } catch (Throwable e) {
                e.printStackTrace();
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
    }
}
