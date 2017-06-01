package com.samsung.android.app.ledcover.wrapperlibrary.utils;

import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {
    private static final String TAG = "ReflectUtils";

    public static Class classForName(String classPath) {
        Throwable e;
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e2) {
            e = e2;
            Log.d(TAG, "Cannot load class: " + classPath + " : " + e.getMessage());
            return null;
        } catch (LinkageError e3) {
            e = e3;
            Log.d(TAG, "Cannot load class: " + classPath + " : " + e.getMessage());
            return null;
        }
    }

    public static Constructor getConstructor(Class baseClass, Class... params) {
        Constructor constructor = null;
        if (baseClass != null) {
            try {
                constructor = baseClass.getConstructor(params);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Cannot load constructor : " + e.getMessage());
            }
        }
        return constructor;
    }

    public static Method getMethod(Class baseClass, String methodName, Class... params) {
        Method method = null;
        if (baseClass != null) {
            try {
                method = baseClass.getMethod(methodName, params);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Cannot load method: " + e.getMessage());
            }
        }
        return method;
    }

    public static Field getField(Class baseClass, String fieldName) {
        Exception e;
        Field field = null;
        if (baseClass != null) {
            try {
                field = baseClass.getField(fieldName);
            } catch (NoSuchFieldException e2) {
                e = e2;
                Log.d(TAG, "Cannot load field: " + e.getMessage());
                return field;
            } catch (SecurityException e3) {
                e = e3;
                Log.d(TAG, "Cannot load field: " + e.getMessage());
                return field;
            }
        }
        return field;
    }
}
