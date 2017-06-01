package org.apache.commons.lang.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HashCodeBuilder {
    private final int iConstant;
    private int iTotal;

    public HashCodeBuilder() {
        this.iTotal = 0;
        this.iConstant = 37;
        this.iTotal = 17;
    }

    public HashCodeBuilder(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
        this.iTotal = 0;
        if (initialNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero initial value");
        } else if (initialNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
        } else if (multiplierNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero multiplier");
        } else if (multiplierNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
        } else {
            this.iConstant = multiplierNonZeroOddNumber;
            this.iTotal = initialNonZeroOddNumber;
        }
    }

    public static int reflectionHashCode(Object object) {
        return reflectionHashCode(17, 37, object, false, null, null);
    }

    public static int reflectionHashCode(Object object, Collection excludeFields) {
        return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static int reflectionHashCode(Object object, String[] excludeFields) {
        return reflectionHashCode(17, 37, object, false, null, excludeFields);
    }

    public static int reflectionHashCode(Object object, boolean testTransients) {
        return reflectionHashCode(17, 37, object, testTransients, null, null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null, null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null, null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients, Class reflectUpToClass) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, null);
    }

    public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients, Class reflectUpToClass, String[] excludeFields) {
        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        }
        HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        Class clazz = object.getClass();
        reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        }
        return builder.toHashCode();
    }

    private static void reflectionAppend(Object object, Class clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) {
        Field[] fields = clazz.getDeclaredFields();
        List excludedFieldList = excludeFields != null ? Arrays.asList(excludeFields) : Collections.EMPTY_LIST;
        AccessibleObject.setAccessible(fields, true);
        for (Field f : fields) {
            if (!excludedFieldList.contains(f.getName()) && f.getName().indexOf(36) == -1 && ((useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers()))) {
                try {
                    builder.append(f.get(object));
                } catch (IllegalAccessException e) {
                    throw new InternalError("Unexpected IllegalAccessException");
                }
            }
        }
    }

    public HashCodeBuilder appendSuper(int superHashCode) {
        this.iTotal = (this.iTotal * this.iConstant) + superHashCode;
        return this;
    }

    public HashCodeBuilder append(Object object) {
        if (object == null) {
            this.iTotal *= this.iConstant;
        } else if (!object.getClass().isArray()) {
            this.iTotal = (this.iTotal * this.iConstant) + object.hashCode();
        } else if (object instanceof long[]) {
            append((long[]) object);
        } else if (object instanceof int[]) {
            append((int[]) object);
        } else if (object instanceof short[]) {
            append((short[]) object);
        } else if (object instanceof char[]) {
            append((char[]) object);
        } else if (object instanceof byte[]) {
            append((byte[]) object);
        } else if (object instanceof double[]) {
            append((double[]) object);
        } else if (object instanceof float[]) {
            append((float[]) object);
        } else if (object instanceof boolean[]) {
            append((boolean[]) object);
        } else {
            append((Object[]) object);
        }
        return this;
    }

    public HashCodeBuilder append(long value) {
        this.iTotal = (this.iTotal * this.iConstant) + ((int) ((value >> 32) ^ value));
        return this;
    }

    public HashCodeBuilder append(int value) {
        this.iTotal = (this.iTotal * this.iConstant) + value;
        return this;
    }

    public HashCodeBuilder append(short value) {
        this.iTotal = (this.iTotal * this.iConstant) + value;
        return this;
    }

    public HashCodeBuilder append(char value) {
        this.iTotal = (this.iTotal * this.iConstant) + value;
        return this;
    }

    public HashCodeBuilder append(byte value) {
        this.iTotal = (this.iTotal * this.iConstant) + value;
        return this;
    }

    public HashCodeBuilder append(double value) {
        return append(Double.doubleToLongBits(value));
    }

    public HashCodeBuilder append(float value) {
        this.iTotal = (this.iTotal * this.iConstant) + Float.floatToIntBits(value);
        return this;
    }

    public HashCodeBuilder append(boolean value) {
        this.iTotal = (value ? 0 : 1) + (this.iConstant * this.iTotal);
        return this;
    }

    public HashCodeBuilder append(Object[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (Object append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(long[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (long append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(int[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(short[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (short append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(char[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (char append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(byte[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (byte append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(double[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (double append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(float[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (float append : array) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(boolean[] array) {
        if (array == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (boolean append : array) {
                append(append);
            }
        }
        return this;
    }

    public int toHashCode() {
        return this.iTotal;
    }
}
