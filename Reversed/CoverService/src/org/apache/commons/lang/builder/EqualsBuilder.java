package org.apache.commons.lang.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EqualsBuilder {
    private boolean isEquals = true;

    public static boolean reflectionEquals(Object lhs, Object rhs) {
        return reflectionEquals(lhs, rhs, false, null, null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, Collection excludeFields) {
        return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, String[] excludeFields) {
        return reflectionEquals(lhs, rhs, false, null, excludeFields);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
        return reflectionEquals(lhs, rhs, testTransients, null, null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass) {
        return reflectionEquals(lhs, rhs, testTransients, reflectUpToClass, null);
    }

    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass, String[] excludeFields) {
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null || rhs == null) {
            return false;
        }
        Class testClass;
        Class lhsClass = lhs.getClass();
        Class rhsClass = rhs.getClass();
        if (lhsClass.isInstance(rhs)) {
            testClass = lhsClass;
            if (!rhsClass.isInstance(lhs)) {
                testClass = rhsClass;
            }
        } else if (!rhsClass.isInstance(lhs)) {
            return false;
        } else {
            testClass = rhsClass;
            if (!lhsClass.isInstance(rhs)) {
                testClass = lhsClass;
            }
        }
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        try {
            reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
            while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
                testClass = testClass.getSuperclass();
                reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
            }
            return equalsBuilder.isEquals();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static void reflectionAppend(Object lhs, Object rhs, Class clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
        Field[] fields = clazz.getDeclaredFields();
        List excludedFieldList = excludeFields != null ? Arrays.asList(excludeFields) : Collections.EMPTY_LIST;
        AccessibleObject.setAccessible(fields, true);
        for (int i = 0; i < fields.length && builder.isEquals; i++) {
            Field f = fields[i];
            if (!excludedFieldList.contains(f.getName()) && f.getName().indexOf(36) == -1 && ((useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers()))) {
                try {
                    builder.append(f.get(lhs), f.get(rhs));
                } catch (IllegalAccessException e) {
                    throw new InternalError("Unexpected IllegalAccessException");
                }
            }
        }
    }

    public EqualsBuilder appendSuper(boolean superEquals) {
        if (this.isEquals) {
            this.isEquals = superEquals;
        }
        return this;
    }

    public EqualsBuilder append(Object lhs, Object rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (!lhs.getClass().isArray()) {
                this.isEquals = lhs.equals(rhs);
            } else if (lhs.getClass() != rhs.getClass()) {
                setEquals(false);
            } else if (lhs instanceof long[]) {
                append((long[]) lhs, (long[]) rhs);
            } else if (lhs instanceof int[]) {
                append((int[]) lhs, (int[]) rhs);
            } else if (lhs instanceof short[]) {
                append((short[]) lhs, (short[]) rhs);
            } else if (lhs instanceof char[]) {
                append((char[]) lhs, (char[]) rhs);
            } else if (lhs instanceof byte[]) {
                append((byte[]) lhs, (byte[]) rhs);
            } else if (lhs instanceof double[]) {
                append((double[]) lhs, (double[]) rhs);
            } else if (lhs instanceof float[]) {
                append((float[]) lhs, (float[]) rhs);
            } else if (lhs instanceof boolean[]) {
                append((boolean[]) lhs, (boolean[]) rhs);
            } else {
                append((Object[]) lhs, (Object[]) rhs);
            }
        }
        return this;
    }

    public EqualsBuilder append(long lhs, long rhs) {
        if (this.isEquals) {
            this.isEquals = lhs == rhs;
        }
        return this;
    }

    public EqualsBuilder append(int lhs, int rhs) {
        if (this.isEquals) {
            this.isEquals = lhs == rhs;
        }
        return this;
    }

    public EqualsBuilder append(short lhs, short rhs) {
        if (this.isEquals) {
            this.isEquals = lhs == rhs;
        }
        return this;
    }

    public EqualsBuilder append(char lhs, char rhs) {
        if (this.isEquals) {
            this.isEquals = lhs == rhs;
        }
        return this;
    }

    public EqualsBuilder append(byte lhs, byte rhs) {
        if (this.isEquals) {
            this.isEquals = lhs == rhs;
        }
        return this;
    }

    public EqualsBuilder append(double lhs, double rhs) {
        return !this.isEquals ? this : append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
    }

    public EqualsBuilder append(float lhs, float rhs) {
        return !this.isEquals ? this : append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
    }

    public EqualsBuilder append(boolean lhs, boolean rhs) {
        if (this.isEquals) {
            this.isEquals = lhs == rhs;
        }
        return this;
    }

    public EqualsBuilder append(Object[] lhs, Object[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(long[] lhs, long[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(int[] lhs, int[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(short[] lhs, short[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(char[] lhs, char[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(byte[] lhs, byte[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(double[] lhs, double[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(float[] lhs, float[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
        if (this.isEquals && lhs != rhs) {
            if (lhs == null || rhs == null) {
                setEquals(false);
            } else if (lhs.length != rhs.length) {
                setEquals(false);
            } else {
                for (int i = 0; i < lhs.length && this.isEquals; i++) {
                    append(lhs[i], rhs[i]);
                }
            }
        }
        return this;
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    protected void setEquals(boolean isEquals) {
        this.isEquals = isEquals;
    }
}
