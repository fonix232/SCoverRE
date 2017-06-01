package com.samsung.android.content.smartclip;

import android.util.Log;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/* compiled from: SmartClipDataCropperImpl */
class ReflectionUtils {
    public static final int MATCH_TYPE_CLASS_NAME_ONLY = 1;
    public static final int MATCH_TYPE_FULL_NAME = 0;
    private static final String TAG = "ReflectionUtils";

    ReflectionUtils() {
    }

    public static void dumpClassHierarchy(Object obj) {
        Class cls = obj.getClass();
        Log.m29d(TAG, "-------- Class hierarchy dump start : " + obj.toString() + " ----------");
        for (Class cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
            Log.m29d(TAG, "-- Class name : " + cls2.getName());
            Class[] interfaces = cls2.getInterfaces();
            for (Class name : interfaces) {
                Log.m29d(TAG, "   + interfaces : " + name.getName());
            }
        }
        Log.m29d(TAG, "-------- Class hierarchy dump finished ----------");
    }

    public static void dumpObjectFields(Object obj, String str, int i) {
        ArrayList arrayList = new ArrayList();
        Log.m31e(TAG, "-------- Field list dump start : " + obj.toString() + " ----------");
        dumpObjectFields(obj, arrayList, str, null, MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET, 0, i, null, null);
        Log.m31e(TAG, "-------- Field list dump finished ----------");
    }

    protected static void dumpObjectFields(Object obj, ArrayList<Object> arrayList, String str, Field field, String str2, int i, int i2, String str3, String str4) {
        if (obj != null) {
            String str5;
            String indentString;
            String replace;
            String str6;
            int arraySize;
            int i3;
            Object arrayValueObject;
            int i4;
            boolean isAccessible;
            Object obj2;
            Object obj3;
            Object obj4;
            Class cls = obj.getClass();
            String name = cls.getName();
            Object obj5 = findObjFromArrayList(arrayList, obj) != -1 ? 1 : null;
            if (!cls.isPrimitive()) {
                if (!name.contains("java.lang.")) {
                    str5 = "@" + Integer.toHexString(obj.hashCode());
                    if (cls.isArray()) {
                        str5 = str5 + " [arraySize = " + getArraySize(obj, name) + "]";
                    }
                    indentString = getIndentString(i);
                    replace = (field == null ? field.getType().getName() : MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET).replace("[L", MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
                    if (str == null) {
                        str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
                    }
                    str6 = str + (field == null ? field.getName() : MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
                    if (str2 == null) {
                        str2 = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
                    }
                    if ((str3 == null || str3.equals(name)) && (str4 == null || str4.equals(str5))) {
                        if (!cls.isPrimitive() || replace.equals(name)) {
                            Log.m31e(TAG, indentString + str6 + " = " + str5 + " (" + replace + ") : " + str2);
                        } else {
                            Log.m31e(TAG, indentString + str6 + " = " + str5 + " (" + replace + " / " + name + ") : " + str2);
                        }
                    }
                    if (!str2.equals(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET)) {
                        str2 = str2 + ".";
                    }
                    str2 = str2 + str6 + "(" + extractClassNameFromFullClassPath(name) + ")";
                    if (obj5 == null) {
                        if (i + 1 < i2) {
                            arrayList.add(obj);
                        }
                        if (cls.isArray()) {
                            arraySize = getArraySize(obj, name);
                            i3 = 0;
                            while (i3 < arraySize && i3 < 100) {
                                arrayValueObject = getArrayValueObject(obj, i3);
                                if (!(arrayValueObject == null || (arrayValueObject.getClass().isPrimitive() && arrayValueObject.toString().equals("0")))) {
                                    dumpObjectFields(arrayValueObject, arrayList, "[" + i3 + "]", null, str2, i + 1, i2, str3, str4);
                                }
                                i3++;
                            }
                            if (arraySize > 100) {
                                Log.m31e(TAG, indentString + "\t[Dumped until index " + 100 + "]");
                            }
                        } else if (!isPrimitiveDataType(name)) {
                            if (!name.contains("java.lang.")) {
                                while (cls != null) {
                                    for (AccessibleObject accessibleObject : cls.getDeclaredFields()) {
                                        try {
                                            isAccessible = accessibleObject.isAccessible();
                                            accessibleObject.setAccessible(true);
                                            obj2 = accessibleObject.get(obj);
                                            accessibleObject.setAccessible(isAccessible);
                                        } catch (Throwable e) {
                                            obj2 = null;
                                            e.printStackTrace();
                                        } catch (Throwable e2) {
                                            obj2 = null;
                                            e2.printStackTrace();
                                        }
                                        obj3 = (accessibleObject.getModifiers() & 16) == 0 ? 1 : null;
                                        obj4 = (accessibleObject.getModifiers() & 8) == 0 ? 1 : null;
                                        if (!accessibleObject.isEnumConstant() && ((obj4 == null || obj3 == null) && i + 1 < i2)) {
                                            dumpObjectFields(obj2, arrayList, null, accessibleObject, str2, i + 1, i2, str3, str4);
                                        }
                                    }
                                    cls = cls.getSuperclass();
                                }
                            }
                        }
                    }
                }
            }
            str5 = obj.toString();
            if (cls.isArray()) {
                str5 = str5 + " [arraySize = " + getArraySize(obj, name) + "]";
            }
            indentString = getIndentString(i);
            if (field == null) {
            }
            replace = (field == null ? field.getType().getName() : MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET).replace("[L", MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
            if (str == null) {
                str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            }
            if (field == null) {
            }
            str6 = str + (field == null ? field.getName() : MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
            if (str2 == null) {
                str2 = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            }
            if (cls.isPrimitive()) {
            }
            Log.m31e(TAG, indentString + str6 + " = " + str5 + " (" + replace + ") : " + str2);
            if (str2.equals(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET)) {
                str2 = str2 + ".";
            }
            str2 = str2 + str6 + "(" + extractClassNameFromFullClassPath(name) + ")";
            if (obj5 == null) {
                if (i + 1 < i2) {
                    arrayList.add(obj);
                }
                if (cls.isArray()) {
                    arraySize = getArraySize(obj, name);
                    i3 = 0;
                    while (i3 < arraySize) {
                        arrayValueObject = getArrayValueObject(obj, i3);
                        dumpObjectFields(arrayValueObject, arrayList, "[" + i3 + "]", null, str2, i + 1, i2, str3, str4);
                        i3++;
                    }
                    if (arraySize > 100) {
                        Log.m31e(TAG, indentString + "\t[Dumped until index " + 100 + "]");
                    }
                } else if (isPrimitiveDataType(name)) {
                    if (name.contains("java.lang.")) {
                        while (cls != null) {
                            for (i4 = 0; i4 < r30; i4++) {
                                isAccessible = accessibleObject.isAccessible();
                                accessibleObject.setAccessible(true);
                                obj2 = accessibleObject.get(obj);
                                accessibleObject.setAccessible(isAccessible);
                                if ((accessibleObject.getModifiers() & 16) == 0) {
                                }
                                if ((accessibleObject.getModifiers() & 8) == 0) {
                                }
                                dumpObjectFields(obj2, arrayList, null, accessibleObject, str2, i + 1, i2, str3, str4);
                            }
                            cls = cls.getSuperclass();
                        }
                    }
                }
            }
        }
    }

    public static void dumpObjectFieldsWithClassTypeFilter(Object obj, String str, int i, String str2) {
        ArrayList arrayList = new ArrayList();
        Log.m31e(TAG, "-------- Field list dump start : " + obj.toString() + " / Object type filter : " + str2 + " ----------");
        dumpObjectFields(obj, arrayList, str, null, MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET, 0, i, str2, null);
        Log.m31e(TAG, "-------- Field list dump finished ----------");
    }

    public static void dumpObjectFieldsWithValueFilter(Object obj, String str, int i, String str2) {
        ArrayList arrayList = new ArrayList();
        Log.m31e(TAG, "-------- Field list dump start : " + obj.toString() + " / Value filter : " + str2 + " ----------");
        dumpObjectFields(obj, arrayList, str, null, MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET, 0, i, null, str2);
        Log.m31e(TAG, "-------- Field list dump finished ----------");
    }

    public static void dumpObjectMethods(Object obj) {
        Log.m29d(TAG, "-------- Method list dump start : " + obj.toString() + " ----------");
        for (Class cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {
            Log.m29d(TAG, " -- Methods of " + cls.getName() + " class --");
            for (Method toGenericString : cls.getDeclaredMethods()) {
                Log.m29d(TAG, toGenericString.toGenericString());
            }
        }
        Log.m29d(TAG, "-------- Method list dump finished ----------");
    }

    protected static String extractClassNameFromFullClassPath(String str) {
        String[] split = str.split("\\.");
        return split.length == 0 ? MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET : split[split.length - 1];
    }

    protected static int findObjFromArrayList(ArrayList<Object> arrayList, Object obj) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (obj == arrayList.get(i)) {
                return i;
            }
        }
        return -1;
    }

    protected static int getArraySize(Object obj, String str) {
        return str.startsWith("[I") ? ((int[]) obj).length : str.startsWith("[Z") ? ((boolean[]) obj).length : str.startsWith("[J") ? ((long[]) obj).length : str.startsWith("[B") ? ((byte[]) obj).length : str.startsWith("[F") ? ((float[]) obj).length : str.startsWith("[C") ? ((char[]) obj).length : str.startsWith("[S") ? ((short[]) obj).length : str.startsWith("[D") ? ((double[]) obj).length : str.startsWith("[L") ? ((Object[]) obj).length : 0;
    }

    protected static Object getArrayValueObject(Object obj, int i) {
        String name = obj.getClass().getName();
        return name.startsWith("[I") ? new Integer(((int[]) obj)[i]) : name.startsWith("[Z") ? new Boolean(((boolean[]) obj)[i]) : name.startsWith("[J") ? new Long(((long[]) obj)[i]) : name.startsWith("[B") ? new Byte(((byte[]) obj)[i]) : name.startsWith("[F") ? new Float(((float[]) obj)[i]) : name.startsWith("[C") ? new Integer(((char[]) obj)[i]) : name.startsWith("[S") ? new Short(((short[]) obj)[i]) : name.startsWith("[D") ? new Double(((double[]) obj)[i]) : name.startsWith("[L") ? ((Object[]) obj)[i] : "Unknown(" + name + ")";
    }

    public static Object getFieldObjectByFieldName(Object obj, String str) {
        if (obj == null || str == null) {
            return null;
        }
        for (Class cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {
            Field[] declaredFields = cls.getDeclaredFields();
            int i = 0;
            int length = declaredFields.length;
            while (i < length) {
                AccessibleObject accessibleObject = declaredFields[i];
                if (str.equals(accessibleObject.getName())) {
                    try {
                        boolean isAccessible = accessibleObject.isAccessible();
                        accessibleObject.setAccessible(true);
                        Object obj2 = accessibleObject.get(obj);
                        accessibleObject.setAccessible(isAccessible);
                        return obj2;
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e2) {
                    }
                } else {
                    i++;
                }
            }
        }
        return null;
    }

    protected static void getFieldObjectByObjectType(Object obj, int i, String str, int i2, ArrayList<Object> arrayList, int i3, int i4, boolean z) {
        if (obj != null && str != null && i3 != i4) {
            for (Class cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {
                if (z) {
                    String name = cls.getName();
                    if (name != null) {
                        if (!name.startsWith("android.view.")) {
                            if (name.startsWith("java.")) {
                            }
                        }
                    }
                }
                for (AccessibleObject accessibleObject : cls.getDeclaredFields()) {
                    String name2 = accessibleObject.getType().getName();
                    try {
                        boolean isAccessible = accessibleObject.isAccessible();
                        accessibleObject.setAccessible(true);
                        Object obj2 = accessibleObject.get(obj);
                        accessibleObject.setAccessible(isAccessible);
                        if (obj2 != null) {
                            boolean endsWith;
                            switch (i) {
                                case 1:
                                    endsWith = name2.endsWith("." + str);
                                    break;
                                default:
                                    endsWith = name2.equals(str);
                                    break;
                            }
                            if (endsWith) {
                                Object obj3 = null;
                                for (Object obj4 : arrayList) {
                                    if (obj4 == obj2) {
                                        obj3 = 1;
                                        if (obj3 == null) {
                                            arrayList.add(obj2);
                                        }
                                    }
                                }
                                if (obj3 == null) {
                                    arrayList.add(obj2);
                                }
                            } else {
                                getFieldObjectByObjectType(obj2, i, str, i2, arrayList, i3 + 1, i4, z);
                            }
                            if (i2 > 0 && arrayList.size() >= i2) {
                                return;
                            }
                        }
                        continue;
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e2) {
                    }
                }
            }
        }
    }

    public static Object[] getFieldObjectByObjectType(Object obj, int i, String str, int i2, int i3, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (obj == null || str == null) {
            return arrayList.toArray();
        }
        getFieldObjectByObjectType(obj, i, str, i2, arrayList, 0, i3, z);
        return arrayList.toArray();
    }

    public static Object[] getFieldObjectByObjectType(Object obj, int i, String str, int i2, boolean z) {
        return getFieldObjectByObjectType(obj, i, str, i2, 1, z);
    }

    protected static String getIndentString(int i) {
        String str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        for (int i2 = 0; i2 < i; i2++) {
            str = str + "\t";
        }
        return str;
    }

    protected static boolean isPrimitiveDataType(String str) {
        return str.equals("short") || str.equals("int") || str.equals("long") || str.equals("char") || str.equals("byte") || str.equals("float") || str.equals("double") || str.equals("boolean");
    }
}
