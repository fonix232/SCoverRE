package org.apache.commons.lang;

import java.io.Serializable;

public class ObjectUtils {
    public static final Null NULL = new Null();

    public static class Null implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        Null() {
        }

        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }

    public static Object defaultIfNull(Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    public static int hashCode(Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }

    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        return appendIdentityToString(null, object).toString();
    }

    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }
        if (buffer == null) {
            buffer = new StringBuffer();
        }
        return buffer.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
    }

    public static String toString(Object obj) {
        return obj == null ? StringUtils.EMPTY : obj.toString();
    }

    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }
}
