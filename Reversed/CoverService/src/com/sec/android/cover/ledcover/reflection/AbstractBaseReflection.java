package com.sec.android.cover.ledcover.reflection;

import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractBaseReflection {
    private static final String TAG = "AbstractBaseReflection";
    private static final String TYPE_NAME_PREFIX = "class ";
    protected Class<?> mBaseClass = null;
    private HashMap<String, Class<?>> mClassMap = new HashMap();
    private ArrayList<String> mNameList = new ArrayList();
    private ArrayList<Object> mReflectionList = new ArrayList();

    protected abstract String getBaseClassName();

    public AbstractBaseReflection() {
        loadReflection();
    }

    public AbstractBaseReflection(String baseClassName) {
        loadReflection(baseClassName);
    }

    public AbstractBaseReflection(Class<?> baseClass) {
        loadReflection((Class) baseClass);
    }

    protected void loadReflection() {
        loadReflection(getBaseClassName());
    }

    protected void loadReflection(String baseClassName) {
        loadReflection(getClass(baseClassName));
    }

    protected void loadReflection(Class<?> baseClass) {
        this.mBaseClass = baseClass;
        if (this.mBaseClass == null) {
            Log.d(TAG, "There's no class.");
        } else {
            loadStaticFields();
        }
    }

    protected Class<?> getClass(String className) {
        Class<?> curClass = null;
        try {
            curClass = Class.forName(className);
        } catch (ClassNotFoundException cnfEx) {
            Log.e(TAG, className + " Unable to load class " + cnfEx, cnfEx);
        }
        return curClass;
    }

    protected Class<?> loadClassIfNeeded(String className) {
        Class<?> curClass = (Class) this.mClassMap.get(className);
        if (curClass == null) {
            curClass = getClass(className);
            if (curClass != null) {
                this.mClassMap.put(className, curClass);
            }
        }
        return curClass;
    }

    protected void loadStaticFields() {
    }

    private Object getReflectionInstance(String name) {
        Object obj = null;
        synchronized (this.mNameList) {
            if (name == null) {
            } else {
                int totalItemCnt = this.mNameList.size();
                loop0:
                for (int listIdx = 0; listIdx < totalItemCnt; listIdx++) {
                    String storedName = (String) this.mNameList.get(listIdx);
                    int strLength = storedName.length();
                    if (strLength == name.length()) {
                        int strLastIdx = strLength - 1;
                        char[] storedNameCharArray = storedName.toCharArray();
                        char[] targetNameCharArray = name.toCharArray();
                        int strIdx = 0;
                        while (strIdx < strLength && (storedNameCharArray[strIdx] & targetNameCharArray[strIdx]) == storedNameCharArray[strIdx]) {
                            if (strIdx == strLastIdx) {
                                obj = this.mReflectionList.get(listIdx);
                                break loop0;
                            }
                            strIdx++;
                        }
                    }
                }
            }
        }
        return obj;
    }

    private void addReflectionInstance(String name, Object reflection) {
        synchronized (this.mNameList) {
            this.mNameList.add(name);
            this.mReflectionList.add(reflection);
        }
    }

    protected Constructor loadConstructorIfNeeded(Class<?>[] paramTypes) {
        String uniqueConstructorName = getUniqueConstructorName(paramTypes);
        Object constructor = getReflectionInstance(uniqueConstructorName);
        if (constructor != null) {
            return (Constructor) constructor;
        }
        if (this.mBaseClass == null || uniqueConstructorName == null || uniqueConstructorName.isEmpty()) {
            return null;
        }
        if (paramTypes == null) {
            paramTypes = new Class[0];
        }
        Constructor loadedConstructor = null;
        try {
            loadedConstructor = this.mBaseClass.getConstructor(paramTypes);
            addReflectionInstance(uniqueConstructorName, loadedConstructor);
            return loadedConstructor;
        } catch (NoSuchMethodException e) {
            try {
                loadedConstructor = this.mBaseClass.getDeclaredConstructor(paramTypes);
                loadedConstructor.setAccessible(true);
                addReflectionInstance(uniqueConstructorName, loadedConstructor);
                return loadedConstructor;
            } catch (NoSuchMethodException nsmEx2) {
                Log.e(TAG, getBaseClassName() + " No method " + nsmEx2, nsmEx2);
                return loadedConstructor;
            }
        }
    }

    public Object createInstance() {
        return createInstance(new Object[0]);
    }

    protected Object createInstance(Object... parameters) {
        return createInstance(null, parameters);
    }

    protected Object createInstance(Class<?>[] paramTypes, Object... parameters) {
        Object obj = null;
        if (parameters == null) {
            parameters = new Object[0];
        }
        Constructor constructor = loadConstructorIfNeeded(paramTypes);
        if (constructor == null) {
            Log.d(getBaseClassName(), "Cannot invoke there's no constructor.");
        } else {
            try {
                constructor.setAccessible(true);
                obj = constructor.newInstance(parameters);
            } catch (IllegalAccessException illAccEx) {
                Log.e(TAG, getBaseClassName() + " IllegalAccessException encountered invoking constructor " + illAccEx, illAccEx);
            } catch (InvocationTargetException invokeTargetEx) {
                Log.e(TAG, getBaseClassName() + " InvocationTargetException encountered invoking constructor " + invokeTargetEx, invokeTargetEx);
            } catch (InstantiationException instantiationEx) {
                instantiationEx.printStackTrace();
                Log.e(TAG, getBaseClassName() + " InstantiationException encountered invoking constructor " + instantiationEx, instantiationEx);
            }
        }
        return obj;
    }

    protected Field loadFieldIfNeeded(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return null;
        }
        String uniqueFieldName = getUniqueFieldName(fieldName);
        Object field = getReflectionInstance(uniqueFieldName);
        if (field != null) {
            return (Field) field;
        }
        if (this.mBaseClass == null) {
            return null;
        }
        Field loadedField = null;
        try {
            loadedField = this.mBaseClass.getField(fieldName);
            addReflectionInstance(uniqueFieldName, loadedField);
            return loadedField;
        } catch (NoSuchFieldException e) {
            try {
                loadedField = this.mBaseClass.getDeclaredField(fieldName);
                loadedField.setAccessible(true);
                addReflectionInstance(uniqueFieldName, loadedField);
                return loadedField;
            } catch (NoSuchFieldException nsmEx2) {
                Log.e(TAG, getBaseClassName() + " No field " + nsmEx2, nsmEx2);
                return loadedField;
            }
        }
    }

    protected Object getNormalValue(Object instance, String fieldName) {
        Object obj = null;
        if (instance == null || fieldName == null || fieldName.isEmpty()) {
            Log.d(getBaseClassName(), "Cannot get value : " + fieldName);
        } else {
            Field field = loadFieldIfNeeded(fieldName);
            if (field == null) {
                Log.d(getBaseClassName(), "Cannot get value : " + fieldName);
            } else {
                try {
                    obj = field.get(instance);
                } catch (IllegalAccessException illAccEx) {
                    Log.e(TAG, getBaseClassName() + " IllegalAccessException encountered get " + fieldName + illAccEx, illAccEx);
                }
            }
        }
        return obj;
    }

    protected void setNormalValue(Object instance, String fieldName, Object value) {
        if (instance == null || fieldName == null || fieldName.isEmpty()) {
            Log.d(getBaseClassName(), "Cannot set value : " + fieldName);
            return;
        }
        Field field = loadFieldIfNeeded(fieldName);
        if (field == null) {
            Log.d(getBaseClassName(), "Cannot set value : " + fieldName);
            return;
        }
        try {
            field.set(instance, value);
        } catch (IllegalAccessException illAccEx) {
            Log.e(TAG, getBaseClassName() + " IllegalAccessException encountered set " + fieldName + illAccEx, illAccEx);
        }
    }

    protected boolean getBooleanStaticValue(String fieldName) {
        Object result = getStaticValue(fieldName);
        if (result == null) {
            return false;
        }
        return ((Boolean) result).booleanValue();
    }

    protected int getIntStaticValue(String fieldName) {
        Object result = getStaticValue(fieldName);
        if (result == null) {
            return -1;
        }
        return ((Integer) result).intValue();
    }

    protected long getLongStaticValue(String fieldName) {
        Object result = getStaticValue(fieldName);
        if (result == null) {
            return 0;
        }
        return ((Long) result).longValue();
    }

    protected float getFloatStaticValue(String fieldName) {
        Object result = getStaticValue(fieldName);
        if (result == null) {
            return 0.0f;
        }
        return ((Float) result).floatValue();
    }

    protected double getDoubleStaticValue(String fieldName) {
        Object result = getStaticValue(fieldName);
        if (result == null) {
            return 0.0d;
        }
        return ((Double) result).doubleValue();
    }

    protected String getStringStaticValue(String fieldName) {
        Object result = getStaticValue(fieldName);
        if (result == null) {
            return "";
        }
        return (String) result;
    }

    protected Object getStaticValue(String fieldName) {
        Object obj = null;
        if (this.mBaseClass == null || fieldName == null || fieldName.isEmpty()) {
            Log.d(getBaseClassName(), "Cannot get static value : " + fieldName);
        } else {
            try {
                Field staticField = this.mBaseClass.getDeclaredField(fieldName);
                staticField.setAccessible(true);
                obj = staticField.get(null);
            } catch (NoSuchFieldException e) {
                try {
                    obj = this.mBaseClass.getField(fieldName).get(null);
                } catch (NoSuchFieldException nsfEx2) {
                    Log.e(TAG, getBaseClassName() + " No field " + nsfEx2, nsfEx2);
                } catch (IllegalAccessException e2) {
                    Log.e(TAG, getBaseClassName() + " IllegalAccessException encountered get " + fieldName + e2, e2);
                }
            } catch (IllegalAccessException illAccEx) {
                Log.e(TAG, getBaseClassName() + " IllegalAccessException encountered get " + fieldName + illAccEx, illAccEx);
            }
        }
        return obj;
    }

    protected Object getEnum(String enumName) {
        if (this.mBaseClass == null) {
            return null;
        }
        return Enum.valueOf(this.mBaseClass, enumName);
    }

    protected Method loadMethodIfNeeded(String methodName, Class<?>[] paramTypes) {
        Method loadedMethod;
        String uniqueMethodName = getUniqueMethodName(methodName, paramTypes);
        Object method = getReflectionInstance(uniqueMethodName);
        if (method != null) {
            return (Method) method;
        }
        if (this.mBaseClass == null || methodName == null || methodName.isEmpty()) {
            return null;
        }
        if (paramTypes == null) {
            paramTypes = new Class[0];
        }
        try {
            loadedMethod = this.mBaseClass.getMethod(methodName, paramTypes);
            addReflectionInstance(uniqueMethodName, loadedMethod);
            return loadedMethod;
        } catch (NoSuchMethodException e) {
            try {
                loadedMethod = this.mBaseClass.getDeclaredMethod(methodName, paramTypes);
                loadedMethod.setAccessible(true);
                addReflectionInstance(uniqueMethodName, loadedMethod);
                return loadedMethod;
            } catch (NoSuchMethodException nsmEx2) {
                Log.e(TAG, getBaseClassName() + " No method " + nsmEx2, nsmEx2);
                return null;
            }
        }
    }

    protected Object invokeNormalMethod(Object instance, String methodName) {
        return invokeNormalMethod(instance, methodName, new Object[0]);
    }

    protected Object invokeNormalMethod(Object instance, String methodName, Object... parameters) {
        return invokeNormalMethod(instance, methodName, null, parameters);
    }

    protected Object invokeNormalMethod(Object instance, String methodName, Class<?>[] paramTypes, Object... parameters) {
        Object obj = null;
        if (instance == null || methodName == null || methodName.isEmpty()) {
            Log.d(getBaseClassName(), "Cannot invoke " + methodName);
        } else {
            if (parameters == null) {
                parameters = new Object[0];
            }
            Method method = loadMethodIfNeeded(methodName, paramTypes);
            if (method == null) {
                Log.d(getBaseClassName(), "Cannot invoke there's no method reflection : " + methodName);
            } else {
                try {
                    obj = method.invoke(instance, parameters);
                } catch (IllegalAccessException illAccEx) {
                    Log.e(TAG, getBaseClassName() + " IllegalAccessException encountered invoking " + methodName + illAccEx, illAccEx);
                } catch (InvocationTargetException invokeTargetEx) {
                    Log.e(TAG, getBaseClassName() + " InvocationTargetException encountered invoking " + methodName + invokeTargetEx, invokeTargetEx);
                    invokeTargetEx.printStackTrace();
                }
            }
        }
        return obj;
    }

    protected Object invokeStaticMethod(String methodName) {
        return invokeStaticMethod(methodName, new Object[0]);
    }

    protected Object invokeStaticMethod(String methodName, Object... parameters) {
        return invokeStaticMethod(methodName, null, parameters);
    }

    protected Object invokeStaticMethod(String methodName, Class<?>[] paramTypes, Object... parameters) {
        Object obj = null;
        if (methodName == null || methodName.isEmpty()) {
            Log.d(getBaseClassName(), "Cannot invoke " + methodName);
        } else {
            if (parameters == null) {
                parameters = new Object[0];
            }
            Method method = loadMethodIfNeeded(methodName, paramTypes);
            if (method == null) {
                Log.d(getBaseClassName(), "Cannot invoke there's no method reflection : " + methodName);
            } else {
                try {
                    obj = method.invoke(null, parameters);
                } catch (IllegalAccessException illAccEx) {
                    Log.e(TAG, getBaseClassName() + " IllegalAccessException encountered invoking " + methodName + illAccEx, illAccEx);
                } catch (InvocationTargetException invokeTargetEx) {
                    Log.e(TAG, getBaseClassName() + " InvocationTargetException encountered invoking " + methodName + invokeTargetEx, invokeTargetEx);
                }
            }
        }
        return obj;
    }

    private String getUniqueConstructorName(Class<?>[] paramTypes) {
        String uniqueName = getBaseClassName();
        if (paramTypes == null) {
            return uniqueName + "_EMPTY";
        }
        for (Class<?> param : paramTypes) {
            try {
                uniqueName = uniqueName + param.getName();
            } catch (NullPointerException npe) {
                Log.e(TAG, getBaseClassName() + " getUniqueConstructorName " + npe, npe);
            }
        }
        return uniqueName;
    }

    private String getUniqueFieldName(String fieldName) {
        return "FIELD_" + fieldName;
    }

    private String getUniqueMethodName(String methodName, Class<?>[] paramTypes) {
        if (paramTypes == null) {
            return methodName;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(methodName);
        for (Class paramType : paramTypes) {
            if (paramType != null) {
                sb.append(paramType.getName());
            }
        }
        return sb.toString();
    }

    public Class<?> getClassType() {
        return this.mBaseClass;
    }

    public byte checkByte(Object result) {
        return result == null ? (byte) 0 : ((Byte) result).byteValue();
    }

    public boolean checkBoolean(Object result) {
        return result == null ? false : ((Boolean) result).booleanValue();
    }

    public int checkInt(Object result) {
        return result == null ? 0 : ((Integer) result).intValue();
    }

    public long checkLong(Object result) {
        return result == null ? 0 : ((Long) result).longValue();
    }

    public String checkString(Object result) {
        return result == null ? null : (String) result;
    }

    public float checkFloat(Object result) {
        return result == null ? 0.0f : ((Float) result).floatValue();
    }

    public double checkDouble(Object result) {
        return result == null ? 0.0d : ((Double) result).doubleValue();
    }
}
