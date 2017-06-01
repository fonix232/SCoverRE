package org.apache.commons.lang;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassUtils {
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    private static Map abbreviationMap = new HashMap();
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Short;
    static Class class$org$apache$commons$lang$ClassUtils;
    private static Map primitiveWrapperMap = new HashMap();

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Object class$;
        Map map = primitiveWrapperMap;
        Class cls = Boolean.TYPE;
        if (class$java$lang$Boolean == null) {
            class$ = class$("java.lang.Boolean");
            class$java$lang$Boolean = class$;
        } else {
            class$ = class$java$lang$Boolean;
        }
        map.put(cls, class$);
        map = primitiveWrapperMap;
        cls = Byte.TYPE;
        if (class$java$lang$Byte == null) {
            class$ = class$("java.lang.Byte");
            class$java$lang$Byte = class$;
        } else {
            class$ = class$java$lang$Byte;
        }
        map.put(cls, class$);
        map = primitiveWrapperMap;
        cls = Character.TYPE;
        if (class$java$lang$Character == null) {
            class$ = class$("java.lang.Character");
            class$java$lang$Character = class$;
        } else {
            class$ = class$java$lang$Character;
        }
        map.put(cls, class$);
        map = primitiveWrapperMap;
        cls = Short.TYPE;
        if (class$java$lang$Short == null) {
            class$ = class$("java.lang.Short");
            class$java$lang$Short = class$;
        } else {
            class$ = class$java$lang$Short;
        }
        map.put(cls, class$);
        map = primitiveWrapperMap;
        cls = Integer.TYPE;
        if (class$java$lang$Integer == null) {
            class$ = class$("java.lang.Integer");
            class$java$lang$Integer = class$;
        } else {
            class$ = class$java$lang$Integer;
        }
        map.put(cls, class$);
        map = primitiveWrapperMap;
        cls = Long.TYPE;
        if (class$java$lang$Long == null) {
            class$ = class$("java.lang.Long");
            class$java$lang$Long = class$;
        } else {
            class$ = class$java$lang$Long;
        }
        map.put(cls, class$);
        map = primitiveWrapperMap;
        cls = Double.TYPE;
        if (class$java$lang$Double == null) {
            class$ = class$("java.lang.Double");
            class$java$lang$Double = class$;
        } else {
            class$ = class$java$lang$Double;
        }
        map.put(cls, class$);
        map = primitiveWrapperMap;
        cls = Float.TYPE;
        if (class$java$lang$Float == null) {
            class$ = class$("java.lang.Float");
            class$java$lang$Float = class$;
        } else {
            class$ = class$java$lang$Float;
        }
        map.put(cls, class$);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        abbreviationMap.put("int", "I");
        abbreviationMap.put("boolean", "Z");
        abbreviationMap.put("float", "F");
        abbreviationMap.put("long", "J");
        abbreviationMap.put("short", "S");
        abbreviationMap.put("byte", "B");
        abbreviationMap.put("double", "D");
        abbreviationMap.put("char", "C");
    }

    public static String getShortClassName(Object object, String valueIfNull) {
        return object == null ? valueIfNull : getShortClassName(object.getClass().getName());
    }

    public static String getShortClassName(Class cls) {
        if (cls == null) {
            return StringUtils.EMPTY;
        }
        return getShortClassName(cls.getName());
    }

    public static String getShortClassName(String className) {
        if (className == null) {
            return StringUtils.EMPTY;
        }
        if (className.length() == 0) {
            return StringUtils.EMPTY;
        }
        char[] chars = className.toCharArray();
        int lastDot = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == PACKAGE_SEPARATOR_CHAR) {
                lastDot = i + 1;
            } else if (chars[i] == '$') {
                chars[i] = PACKAGE_SEPARATOR_CHAR;
            }
        }
        return new String(chars, lastDot, chars.length - lastDot);
    }

    public static String getPackageName(Object object, String valueIfNull) {
        return object == null ? valueIfNull : getPackageName(object.getClass().getName());
    }

    public static String getPackageName(Class cls) {
        if (cls == null) {
            return StringUtils.EMPTY;
        }
        return getPackageName(cls.getName());
    }

    public static String getPackageName(String className) {
        if (className == null) {
            return StringUtils.EMPTY;
        }
        int i = className.lastIndexOf(46);
        if (i == -1) {
            return StringUtils.EMPTY;
        }
        return className.substring(0, i);
    }

    public static List getAllSuperclasses(Class cls) {
        if (cls == null) {
            return null;
        }
        List classes = new ArrayList();
        for (Class superclass = cls.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            classes.add(superclass);
        }
        return classes;
    }

    public static List getAllInterfaces(Class cls) {
        if (cls == null) {
            return null;
        }
        List list = new ArrayList();
        while (cls != null) {
            Class[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (!list.contains(interfaces[i])) {
                    list.add(interfaces[i]);
                }
                for (Class intface : getAllInterfaces(interfaces[i])) {
                    if (!list.contains(intface)) {
                        list.add(intface);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return list;
    }

    public static List convertClassNamesToClasses(List classNames) {
        if (classNames == null) {
            return null;
        }
        List classes = new ArrayList(classNames.size());
        for (String className : classNames) {
            try {
                classes.add(Class.forName(className));
            } catch (Exception e) {
                classes.add(null);
            }
        }
        return classes;
    }

    public static List convertClassesToClassNames(List classes) {
        if (classes == null) {
            return null;
        }
        List classNames = new ArrayList(classes.size());
        for (Class cls : classes) {
            if (cls == null) {
                classNames.add(null);
            } else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }

    public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
        if (!ArrayUtils.isSameLength((Object[]) classArray, (Object[]) toClassArray)) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (!isAssignable(classArray[i], toClassArray[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAssignable(Class cls, Class toClass) {
        boolean z = true;
        if (toClass == null) {
            return false;
        }
        if (cls == null) {
            if (toClass.isPrimitive()) {
                z = false;
            }
            return z;
        } else if (cls.equals(toClass)) {
            return true;
        } else {
            if (!cls.isPrimitive()) {
                return toClass.isAssignableFrom(cls);
            }
            if (!toClass.isPrimitive()) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                if (Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass)) {
                    return true;
                }
                return false;
            } else if (Long.TYPE.equals(cls)) {
                if (Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass)) {
                    return true;
                }
                return false;
            } else if (Boolean.TYPE.equals(cls) || Double.TYPE.equals(cls)) {
                return false;
            } else {
                if (Float.TYPE.equals(cls)) {
                    return Double.TYPE.equals(toClass);
                }
                if (Character.TYPE.equals(cls)) {
                    if (Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass)) {
                        return true;
                    }
                    return false;
                } else if (Short.TYPE.equals(cls)) {
                    if (Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass)) {
                        return true;
                    }
                    return false;
                } else if (!Byte.TYPE.equals(cls)) {
                    return false;
                } else {
                    if (Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass)) {
                        return true;
                    }
                    return false;
                }
            }
        }
    }

    public static Class primitiveToWrapper(Class cls) {
        Class convertedClass = cls;
        if (cls == null || !cls.isPrimitive()) {
            return convertedClass;
        }
        return (Class) primitiveWrapperMap.get(cls);
    }

    public static Class[] primitivesToWrappers(Class[] classes) {
        if (classes == null) {
            return null;
        }
        if (classes.length == 0) {
            return classes;
        }
        Class[] convertedClasses = new Class[classes.length];
        for (int i = 0; i < classes.length; i++) {
            convertedClasses[i] = primitiveToWrapper(classes[i]);
        }
        return convertedClasses;
    }

    public static boolean isInnerClass(Class cls) {
        if (cls != null && cls.getName().indexOf(36) >= 0) {
            return true;
        }
        return false;
    }

    public static Class getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
        if (abbreviationMap.containsKey(className)) {
            return Class.forName(new StringBuffer().append("[").append(abbreviationMap.get(className)).toString(), initialize, classLoader).getComponentType();
        }
        return Class.forName(toProperClassName(className), initialize, classLoader);
    }

    public static Class getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }

    public static Class getClass(String className) throws ClassNotFoundException {
        return getClass(className, true);
    }

    public static Class getClass(String className, boolean initialize) throws ClassNotFoundException {
        ClassLoader loader;
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        if (contextCL == null) {
            Class class$;
            if (class$org$apache$commons$lang$ClassUtils == null) {
                class$ = class$("org.apache.commons.lang.ClassUtils");
                class$org$apache$commons$lang$ClassUtils = class$;
            } else {
                class$ = class$org$apache$commons$lang$ClassUtils;
            }
            loader = class$.getClassLoader();
        } else {
            loader = contextCL;
        }
        return getClass(loader, className, initialize);
    }

    public static Method getPublicMethod(Class cls, String methodName, Class[] parameterTypes) throws SecurityException, NoSuchMethodException {
        Method declaredMethod = cls.getMethod(methodName, parameterTypes);
        if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
            return declaredMethod;
        }
        List<Class> candidateClasses = new ArrayList();
        candidateClasses.addAll(getAllInterfaces(cls));
        candidateClasses.addAll(getAllSuperclasses(cls));
        for (Class candidateClass : candidateClasses) {
            if (Modifier.isPublic(candidateClass.getModifiers())) {
                try {
                    Method candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
                    if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
                        return candidateMethod;
                    }
                } catch (NoSuchMethodException e) {
                }
            }
        }
        throw new NoSuchMethodException(new StringBuffer().append("Can't find a public method for ").append(methodName).append(" ").append(ArrayUtils.toString(parameterTypes)).toString());
    }

    private static String toProperClassName(String className) {
        className = StringUtils.deleteWhitespace(className);
        if (className == null) {
            throw new NullArgumentException("className");
        } else if (!className.endsWith("[]")) {
            return className;
        } else {
            StringBuffer classNameBuffer = new StringBuffer();
            while (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
                classNameBuffer.append("[");
            }
            String abbreviation = (String) abbreviationMap.get(className);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append("L").append(className).append(";");
            }
            return classNameBuffer.toString();
        }
    }
}
