package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class ExceptionUtils {
    private static String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
    private static final Method THROWABLE_CAUSE_METHOD;
    private static final Method THROWABLE_INITCAUSE_METHOD;
    static final String WRAPPED_MARKER = " [wrapped] ";
    static Class class$java$lang$Throwable;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class class$;
        Method causeMethod;
        try {
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
            } else {
                class$ = class$java$lang$Throwable;
            }
            causeMethod = class$.getMethod("getCause", null);
        } catch (Exception e) {
            causeMethod = null;
        }
        THROWABLE_CAUSE_METHOD = causeMethod;
        try {
            Class cls;
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
                cls = class$;
            } else {
                cls = class$java$lang$Throwable;
            }
            String str = "initCause";
            Class[] clsArr = new Class[1];
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
            } else {
                class$ = class$java$lang$Throwable;
            }
            clsArr[0] = class$;
            causeMethod = cls.getMethod(str, clsArr);
        } catch (Exception e2) {
            causeMethod = null;
        }
        THROWABLE_INITCAUSE_METHOD = causeMethod;
    }

    public static void addCauseMethodName(String methodName) {
        if (StringUtils.isNotEmpty(methodName) && !isCauseMethodName(methodName)) {
            List list = getCauseMethodNameList();
            if (list.add(methodName)) {
                CAUSE_METHOD_NAMES = toArray(list);
            }
        }
    }

    public static void removeCauseMethodName(String methodName) {
        if (StringUtils.isNotEmpty(methodName)) {
            List list = getCauseMethodNameList();
            if (list.remove(methodName)) {
                CAUSE_METHOD_NAMES = toArray(list);
            }
        }
    }

    public static boolean setCause(Throwable target, Throwable cause) {
        if (target == null) {
            throw new NullArgumentException("target");
        }
        Object[] causeArgs = new Object[]{cause};
        boolean modifiedTarget = false;
        if (THROWABLE_INITCAUSE_METHOD != null) {
            try {
                THROWABLE_INITCAUSE_METHOD.invoke(target, causeArgs);
                modifiedTarget = true;
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
            }
        }
        try {
            Class class$;
            Class cls = target.getClass();
            String str = "setCause";
            Class[] clsArr = new Class[1];
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
            } else {
                class$ = class$java$lang$Throwable;
            }
            clsArr[0] = class$;
            cls.getMethod(str, clsArr).invoke(target, causeArgs);
            return true;
        } catch (NoSuchMethodException e3) {
            return modifiedTarget;
        } catch (IllegalAccessException e4) {
            return modifiedTarget;
        } catch (InvocationTargetException e5) {
            return modifiedTarget;
        }
    }

    private static String[] toArray(List list) {
        return (String[]) list.toArray(new String[list.size()]);
    }

    private static ArrayList getCauseMethodNameList() {
        return new ArrayList(Arrays.asList(CAUSE_METHOD_NAMES));
    }

    public static boolean isCauseMethodName(String methodName) {
        return ArrayUtils.indexOf(CAUSE_METHOD_NAMES, (Object) methodName) >= 0;
    }

    public static Throwable getCause(Throwable throwable) {
        return getCause(throwable, CAUSE_METHOD_NAMES);
    }

    public static Throwable getCause(Throwable throwable, String[] methodNames) {
        if (throwable == null) {
            return null;
        }
        Throwable cause = getCauseUsingWellKnownTypes(throwable);
        if (cause != null) {
            return cause;
        }
        if (methodNames == null) {
            methodNames = CAUSE_METHOD_NAMES;
        }
        for (String methodName : methodNames) {
            if (methodName != null) {
                cause = getCauseUsingMethodName(throwable, methodName);
                if (cause != null) {
                    break;
                }
            }
        }
        if (cause == null) {
            return getCauseUsingFieldName(throwable, "detail");
        }
        return cause;
    }

    public static Throwable getRootCause(Throwable throwable) {
        List list = getThrowableList(throwable);
        return list.size() < 2 ? null : (Throwable) list.get(list.size() - 1);
    }

    private static Throwable getCauseUsingWellKnownTypes(Throwable throwable) {
        if (throwable instanceof Nestable) {
            return ((Nestable) throwable).getCause();
        }
        if (throwable instanceof SQLException) {
            return ((SQLException) throwable).getNextException();
        }
        if (throwable instanceof InvocationTargetException) {
            return ((InvocationTargetException) throwable).getTargetException();
        }
        return null;
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
        Method method = null;
        try {
            method = throwable.getClass().getMethod(methodName, null);
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e2) {
        }
        if (method != null) {
            Class class$;
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
            } else {
                class$ = class$java$lang$Throwable;
            }
            if (class$.isAssignableFrom(method.getReturnType())) {
                try {
                    return (Throwable) method.invoke(throwable, ArrayUtils.EMPTY_OBJECT_ARRAY);
                } catch (IllegalAccessException e3) {
                } catch (IllegalArgumentException e4) {
                } catch (InvocationTargetException e5) {
                }
            }
        }
        return null;
    }

    private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName) {
        Field field = null;
        try {
            field = throwable.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e2) {
        }
        if (field != null) {
            Class class$;
            if (class$java$lang$Throwable == null) {
                class$ = class$("java.lang.Throwable");
                class$java$lang$Throwable = class$;
            } else {
                class$ = class$java$lang$Throwable;
            }
            if (class$.isAssignableFrom(field.getType())) {
                try {
                    return (Throwable) field.get(throwable);
                } catch (IllegalAccessException e3) {
                } catch (IllegalArgumentException e4) {
                }
            }
        }
        return null;
    }

    public static boolean isThrowableNested() {
        return THROWABLE_CAUSE_METHOD != null;
    }

    public static boolean isNestedThrowable(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        if (throwable instanceof Nestable) {
            return true;
        }
        if (throwable instanceof SQLException) {
            return true;
        }
        if (throwable instanceof InvocationTargetException) {
            return true;
        }
        if (isThrowableNested()) {
            return true;
        }
        Class cls = throwable.getClass();
        for (String method : CAUSE_METHOD_NAMES) {
            try {
                Method method2 = cls.getMethod(method, null);
                if (method2 == null) {
                    continue;
                } else {
                    Class class$;
                    if (class$java$lang$Throwable == null) {
                        class$ = class$("java.lang.Throwable");
                        class$java$lang$Throwable = class$;
                    } else {
                        class$ = class$java$lang$Throwable;
                    }
                    if (class$.isAssignableFrom(method2.getReturnType())) {
                        return true;
                    }
                }
            } catch (NoSuchMethodException e) {
            } catch (SecurityException e2) {
            }
        }
        try {
            if (cls.getField("detail") != null) {
                return true;
            }
            return false;
        } catch (NoSuchFieldException e3) {
            return false;
        } catch (SecurityException e4) {
            return false;
        }
    }

    public static int getThrowableCount(Throwable throwable) {
        return getThrowableList(throwable).size();
    }

    public static Throwable[] getThrowables(Throwable throwable) {
        List list = getThrowableList(throwable);
        return (Throwable[]) list.toArray(new Throwable[list.size()]);
    }

    public static List getThrowableList(Throwable throwable) {
        List list = new ArrayList();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = getCause(throwable);
        }
        return list;
    }

    public static int indexOfThrowable(Throwable throwable, Class clazz) {
        return indexOf(throwable, clazz, 0, false);
    }

    public static int indexOfThrowable(Throwable throwable, Class clazz, int fromIndex) {
        return indexOf(throwable, clazz, fromIndex, false);
    }

    public static int indexOfType(Throwable throwable, Class type) {
        return indexOf(throwable, type, 0, true);
    }

    public static int indexOfType(Throwable throwable, Class type, int fromIndex) {
        return indexOf(throwable, type, fromIndex, true);
    }

    private static int indexOf(Throwable throwable, Class type, int fromIndex, boolean subclass) {
        if (throwable == null || type == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        Throwable[] throwables = getThrowables(throwable);
        if (fromIndex >= throwables.length) {
            return -1;
        }
        int i;
        if (subclass) {
            for (i = fromIndex; i < throwables.length; i++) {
                if (type.isAssignableFrom(throwables[i].getClass())) {
                    return i;
                }
            }
        } else {
            for (i = fromIndex; i < throwables.length; i++) {
                if (type.equals(throwables[i].getClass())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void printRootCauseStackTrace(Throwable throwable) {
        printRootCauseStackTrace(throwable, System.err);
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintStream stream) {
        if (throwable != null) {
            if (stream == null) {
                throw new IllegalArgumentException("The PrintStream must not be null");
            }
            String[] trace = getRootCauseStackTrace(throwable);
            for (String println : trace) {
                stream.println(println);
            }
            stream.flush();
        }
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintWriter writer) {
        if (throwable != null) {
            if (writer == null) {
                throw new IllegalArgumentException("The PrintWriter must not be null");
            }
            String[] trace = getRootCauseStackTrace(throwable);
            for (String println : trace) {
                writer.println(println);
            }
            writer.flush();
        }
    }

    public static String[] getRootCauseStackTrace(Throwable throwable) {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        Throwable[] throwables = getThrowables(throwable);
        int count = throwables.length;
        ArrayList frames = new ArrayList();
        List nextTrace = getStackFrameList(throwables[count - 1]);
        int i = count;
        while (true) {
            i--;
            if (i < 0) {
                return (String[]) frames.toArray(new String[0]);
            }
            List trace = nextTrace;
            if (i != 0) {
                nextTrace = getStackFrameList(throwables[i - 1]);
                removeCommonFrames(trace, nextTrace);
            }
            if (i == count - 1) {
                frames.add(throwables[i].toString());
            } else {
                frames.add(new StringBuffer().append(WRAPPED_MARKER).append(throwables[i].toString()).toString());
            }
            for (int j = 0; j < trace.size(); j++) {
                frames.add(trace.get(j));
            }
        }
    }

    public static void removeCommonFrames(List causeFrames, List wrapperFrames) {
        if (causeFrames == null || wrapperFrames == null) {
            throw new IllegalArgumentException("The List must not be null");
        }
        int causeFrameIndex = causeFrames.size() - 1;
        int wrapperFrameIndex = wrapperFrames.size() - 1;
        while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
            if (((String) causeFrames.get(causeFrameIndex)).equals((String) wrapperFrames.get(wrapperFrameIndex))) {
                causeFrames.remove(causeFrameIndex);
            }
            causeFrameIndex--;
            wrapperFrameIndex--;
        }
    }

    public static String getFullStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        Throwable[] ts = getThrowables(throwable);
        for (int i = 0; i < ts.length; i++) {
            ts[i].printStackTrace(pw);
            if (isNestedThrowable(ts[i])) {
                break;
            }
        }
        return sw.getBuffer().toString();
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

    public static String[] getStackFrames(Throwable throwable) {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return getStackFrames(getStackTrace(throwable));
    }

    static String[] getStackFrames(String stackTrace) {
        StringTokenizer frames = new StringTokenizer(stackTrace, SystemUtils.LINE_SEPARATOR);
        List list = new ArrayList();
        while (frames.hasMoreTokens()) {
            list.add(frames.nextToken());
        }
        return toArray(list);
    }

    static List getStackFrameList(Throwable t) {
        StringTokenizer frames = new StringTokenizer(getStackTrace(t), SystemUtils.LINE_SEPARATOR);
        List list = new ArrayList();
        boolean traceStarted = false;
        while (frames.hasMoreTokens()) {
            String token = frames.nextToken();
            int at = token.indexOf("at");
            if (at != -1 && token.substring(0, at).trim().length() == 0) {
                traceStarted = true;
                list.add(token);
            } else if (traceStarted) {
                break;
            }
        }
        return list;
    }

    public static String getMessage(Throwable th) {
        if (th == null) {
            return StringUtils.EMPTY;
        }
        return new StringBuffer().append(ClassUtils.getShortClassName(th, null)).append(": ").append(StringUtils.defaultString(th.getMessage())).toString();
    }

    public static String getRootCauseMessage(Throwable th) {
        Throwable root = getRootCause(th);
        if (root == null) {
            root = th;
        }
        return getMessage(root);
    }
}
