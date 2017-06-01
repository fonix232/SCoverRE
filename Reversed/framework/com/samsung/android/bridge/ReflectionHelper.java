package com.samsung.android.bridge;

import android.os.Debug;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionHelper {
    public static final boolean DEBUG = true;
    public static final boolean DEBUG_EXCEPTION = false;
    public static final boolean DEBUG_FIELD = false;
    public static final String TAG = "ReflectionHelper";

    public static Object getInstance(Class<?> cls) {
        try {
            return cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        } catch (Throwable e3) {
            e3.printStackTrace();
            return null;
        } catch (Throwable e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static Object getInstance(Class<?> cls, Class<?>[] clsArr, Object... objArr) {
        try {
            return cls.getConstructor(clsArr).newInstance(objArr);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        } catch (Throwable e3) {
            e3.printStackTrace();
            return null;
        } catch (Throwable e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) {
        Log.m29d(TAG, "Reflecting method.....  class <" + str + ">");
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Object invokeMethod(Method method, Object obj, Object... objArr) {
        Object obj2 = null;
        if (method == null) {
            return obj2;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            return obj2;
        } catch (InvocationTargetException e2) {
            return obj2;
        }
    }

    public static void loadField(Class<?> cls, Class<?> cls2, String[] strArr) {
        if (strArr != null && cls != null && cls2 != null) {
            Log.m29d(TAG, "Reflecting fields.....  class <" + cls + ">");
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                try {
                    Field declaredField = cls.getDeclaredField(strArr[i]);
                    Field field = cls2.getField(strArr[i]);
                    field.set(field, declaredField.get(declaredField));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e2) {
                } catch (IllegalAccessException e3) {
                }
            }
        }
    }

    public static Class<?> loadKlass(String str) {
        try {
            Log.m29d(TAG, "loadKlass() : caller=" + Debug.getCallers(2));
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
