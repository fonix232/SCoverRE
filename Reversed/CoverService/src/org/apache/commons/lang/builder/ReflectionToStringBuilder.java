package org.apache.commons.lang.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;

public class ReflectionToStringBuilder extends ToStringBuilder {
    private static ThreadLocal registry = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new HashSet();
        }
    };
    private boolean appendStatics = false;
    private boolean appendTransients = false;
    private String[] excludeFieldNames;
    private Class upToClass = null;

    static Set getRegistry() {
        return (Set) registry.get();
    }

    static boolean isRegistered(Object value) {
        return getRegistry().contains(value);
    }

    static void register(Object value) {
        getRegistry().add(value);
    }

    public static String toString(Object object) {
        return toString(object, null, false, false, null);
    }

    public static String toString(Object object, ToStringStyle style) {
        return toString(object, style, false, false, null);
    }

    public static String toString(Object object, ToStringStyle style, boolean outputTransients) {
        return toString(object, style, outputTransients, false, null);
    }

    public static String toString(Object object, ToStringStyle style, boolean outputTransients, boolean outputStatics) {
        return toString(object, style, outputTransients, outputStatics, null);
    }

    public static String toString(Object object, ToStringStyle style, boolean outputTransients, boolean outputStatics, Class reflectUpToClass) {
        return new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics).toString();
    }

    public static String toString(Object object, ToStringStyle style, boolean outputTransients, Class reflectUpToClass) {
        return new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients).toString();
    }

    public static String toStringExclude(Object object, String excludeFieldName) {
        return toStringExclude(object, new String[]{excludeFieldName});
    }

    public static String toStringExclude(Object object, Collection excludeFieldNames) {
        return toStringExclude(object, toNoNullStringArray(excludeFieldNames));
    }

    static String[] toNoNullStringArray(Collection collection) {
        if (collection == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return toNoNullStringArray(collection.toArray());
    }

    static String[] toNoNullStringArray(Object[] array) {
        ArrayList list = new ArrayList(array.length);
        for (Object e : array) {
            if (e != null) {
                list.add(e.toString());
            }
        }
        return (String[]) list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static String toStringExclude(Object object, String[] excludeFieldNames) {
        return new ReflectionToStringBuilder(object).setExcludeFieldNames(excludeFieldNames).toString();
    }

    static void unregister(Object value) {
        getRegistry().remove(value);
    }

    public ReflectionToStringBuilder(Object object) {
        super(object);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle style) {
        super(object, style);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer) {
        super(object, style, buffer);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer, Class reflectUpToClass, boolean outputTransients) {
        super(object, style, buffer);
        setUpToClass(reflectUpToClass);
        setAppendTransients(outputTransients);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer, Class reflectUpToClass, boolean outputTransients, boolean outputStatics) {
        super(object, style, buffer);
        setUpToClass(reflectUpToClass);
        setAppendTransients(outputTransients);
        setAppendStatics(outputStatics);
    }

    protected boolean accept(Field field) {
        if (field.getName().indexOf(36) != -1) {
            return false;
        }
        if (Modifier.isTransient(field.getModifiers()) && !isAppendTransients()) {
            return false;
        }
        if (Modifier.isStatic(field.getModifiers()) && !isAppendStatics()) {
            return false;
        }
        if (getExcludeFieldNames() == null || Arrays.binarySearch(getExcludeFieldNames(), field.getName()) < 0) {
            return true;
        }
        return false;
    }

    protected void appendFieldsIn(Class clazz) {
        if (isRegistered(getObject())) {
            appendAsObjectToString(getObject());
            return;
        }
        try {
            registerObject();
            if (clazz.isArray()) {
                reflectionAppendArray(getObject());
            } else {
                Field[] fields = clazz.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (accept(field)) {
                        Object fieldValue = getValue(field);
                        if (!isRegistered(fieldValue) || field.getType().isPrimitive()) {
                            registerObject();
                            append(fieldName, fieldValue);
                            unregisterObject();
                        } else {
                            getStyle().appendFieldStart(getStringBuffer(), fieldName);
                            appendAsObjectToString(fieldValue);
                            getStyle().appendFieldEnd(getStringBuffer(), fieldName);
                        }
                    }
                }
            }
            unregisterObject();
        } catch (IllegalAccessException ex) {
            throw new InternalError(new StringBuffer().append("Unexpected IllegalAccessException: ").append(ex.getMessage()).toString());
        } catch (Throwable th) {
            unregisterObject();
        }
    }

    public String[] getExcludeFieldNames() {
        return this.excludeFieldNames;
    }

    public Class getUpToClass() {
        return this.upToClass;
    }

    protected Object getValue(Field field) throws IllegalArgumentException, IllegalAccessException {
        return field.get(getObject());
    }

    public boolean isAppendStatics() {
        return this.appendStatics;
    }

    public boolean isAppendTransients() {
        return this.appendTransients;
    }

    public ToStringBuilder reflectionAppendArray(Object array) {
        getStyle().reflectionAppendArrayDetail(getStringBuffer(), null, array);
        return this;
    }

    void registerObject() {
        register(getObject());
    }

    public void setAppendStatics(boolean appendStatics) {
        this.appendStatics = appendStatics;
    }

    public void setAppendTransients(boolean appendTransients) {
        this.appendTransients = appendTransients;
    }

    public ReflectionToStringBuilder setExcludeFieldNames(String[] excludeFieldNamesParam) {
        if (excludeFieldNamesParam == null) {
            this.excludeFieldNames = null;
        } else {
            this.excludeFieldNames = toNoNullStringArray((Object[]) excludeFieldNamesParam);
            Arrays.sort(this.excludeFieldNames);
        }
        return this;
    }

    public void setUpToClass(Class clazz) {
        this.upToClass = clazz;
    }

    public String toString() {
        if (getObject() == null) {
            return getStyle().getNullText();
        }
        Class clazz = getObject().getClass();
        appendFieldsIn(clazz);
        while (clazz.getSuperclass() != null && clazz != getUpToClass()) {
            clazz = clazz.getSuperclass();
            appendFieldsIn(clazz);
        }
        return super.toString();
    }

    void unregisterObject() {
        unregister(getObject());
    }
}
