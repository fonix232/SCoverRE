package com.samsung.android.bridge.multiscreen;

import com.samsung.android.bridge.ReflectionHelper;
import java.lang.reflect.Method;

public class MultiScreenManagerBridge {
    public static int ACTIVITY_STACK_ID_MULTIPLIER = 0;
    private static String[] FIELD_NAMES = new String[]{"ACTIVITY_STACK_ID_MULTIPLIER"};
    private static Method METHOD_GET_DISPLAY_ID_FROM_STACK_ID = null;
    private static Method METHOD_GET_STACK_ID_ADJUSTED_TO_DISPLAY_ID = null;
    private static Method METHOD_GET_STACK_ID_EXCLUDING_DISPLAY_ID = null;
    private static final String MULTI_SCREEN_MANAGER_CLASS_NAME = "com.samsung.android.multiscreen.MultiScreenManager";
    public static final String TAG = "MultiScreenMgrBrdg";
    private static Class<?> sKlassMultiScreenManager;

    public interface IMultiScreenManagerBridge {
        int getDisplayId(int i);
    }

    static {
        loadKlass();
        initField();
        loadMethods();
    }

    public static int getDisplayIdFromStackId(int i) {
        if (!(sKlassMultiScreenManager == null || METHOD_GET_DISPLAY_ID_FROM_STACK_ID == null)) {
            Object invokeMethod = ReflectionHelper.invokeMethod(METHOD_GET_DISPLAY_ID_FROM_STACK_ID, null, Integer.valueOf(i));
            if (invokeMethod != null) {
                return ((Integer) invokeMethod).intValue();
            }
        }
        return i;
    }

    public static int getStackIdAdjustedToDisplayId(int i, int i2) {
        if (!(sKlassMultiScreenManager == null || METHOD_GET_STACK_ID_ADJUSTED_TO_DISPLAY_ID == null)) {
            Object invokeMethod = ReflectionHelper.invokeMethod(METHOD_GET_STACK_ID_ADJUSTED_TO_DISPLAY_ID, null, Integer.valueOf(i), Integer.valueOf(i2));
            if (invokeMethod != null) {
                return ((Integer) invokeMethod).intValue();
            }
        }
        return i;
    }

    public static int getStackIdExcludingDisplayId(int i) {
        if (!(sKlassMultiScreenManager == null || METHOD_GET_STACK_ID_EXCLUDING_DISPLAY_ID == null)) {
            Object invokeMethod = ReflectionHelper.invokeMethod(METHOD_GET_STACK_ID_EXCLUDING_DISPLAY_ID, null, Integer.valueOf(i));
            if (invokeMethod != null) {
                return ((Integer) invokeMethod).intValue();
            }
        }
        return i;
    }

    private static void initField() {
        if (sKlassMultiScreenManager != null) {
            ReflectionHelper.loadField(sKlassMultiScreenManager, MultiScreenManagerBridge.class, FIELD_NAMES);
        }
    }

    private static void loadKlass() {
        if (sKlassMultiScreenManager == null) {
            sKlassMultiScreenManager = ReflectionHelper.loadKlass(MULTI_SCREEN_MANAGER_CLASS_NAME);
        }
    }

    private static void loadMethods() {
        if (sKlassMultiScreenManager != null) {
            METHOD_GET_STACK_ID_ADJUSTED_TO_DISPLAY_ID = ReflectionHelper.getMethod(sKlassMultiScreenManager, "getStackIdAdjustedToDisplayId", new Class[]{Integer.TYPE, Integer.TYPE});
            METHOD_GET_STACK_ID_EXCLUDING_DISPLAY_ID = ReflectionHelper.getMethod(sKlassMultiScreenManager, "getStackIdExcludingDisplayId", new Class[]{Integer.TYPE});
            METHOD_GET_DISPLAY_ID_FROM_STACK_ID = ReflectionHelper.getMethod(sKlassMultiScreenManager, "getDisplayIdFromStackId", new Class[]{Integer.TYPE});
        }
    }
}
