package org.apache.commons.lang.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang.math.NumberUtils;

public class CompareToBuilder {
    private int comparison = 0;

    public static int reflectionCompare(Object lhs, Object rhs) {
        return reflectionCompare(lhs, rhs, false, null, null);
    }

    public static int reflectionCompare(Object lhs, Object rhs, boolean compareTransients) {
        return reflectionCompare(lhs, rhs, compareTransients, null, null);
    }

    public static int reflectionCompare(Object lhs, Object rhs, Collection excludeFields) {
        return reflectionCompare(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    public static int reflectionCompare(Object lhs, Object rhs, String[] excludeFields) {
        return reflectionCompare(lhs, rhs, false, null, excludeFields);
    }

    public static int reflectionCompare(Object lhs, Object rhs, boolean compareTransients, Class reflectUpToClass) {
        return reflectionCompare(lhs, rhs, false, reflectUpToClass, null);
    }

    public static int reflectionCompare(Object lhs, Object rhs, boolean compareTransients, Class reflectUpToClass, String[] excludeFields) {
        if (lhs == rhs) {
            return 0;
        }
        if (lhs == null || rhs == null) {
            throw new NullPointerException();
        }
        Class lhsClazz = lhs.getClass();
        if (lhsClazz.isInstance(rhs)) {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
            while (lhsClazz.getSuperclass() != null && lhsClazz != reflectUpToClass) {
                lhsClazz = lhsClazz.getSuperclass();
                reflectionAppend(lhs, rhs, lhsClazz, compareToBuilder, compareTransients, excludeFields);
            }
            return compareToBuilder.toComparison();
        }
        throw new ClassCastException();
    }

    private static void reflectionAppend(Object lhs, Object rhs, Class clazz, CompareToBuilder builder, boolean useTransients, String[] excludeFields) {
        Field[] fields = clazz.getDeclaredFields();
        List excludedFieldList = excludeFields != null ? Arrays.asList(excludeFields) : Collections.EMPTY_LIST;
        AccessibleObject.setAccessible(fields, true);
        for (int i = 0; i < fields.length && builder.comparison == 0; i++) {
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

    public CompareToBuilder appendSuper(int superCompareTo) {
        if (this.comparison == 0) {
            this.comparison = superCompareTo;
        }
        return this;
    }

    public CompareToBuilder append(Object lhs, Object rhs) {
        return append(lhs, rhs, null);
    }

    public CompareToBuilder append(Object lhs, Object rhs, Comparator comparator) {
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.getClass().isArray()) {
                if (lhs instanceof long[]) {
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
                    append((Object[]) lhs, (Object[]) rhs, comparator);
                }
            } else if (comparator == null) {
                this.comparison = ((Comparable) lhs).compareTo(rhs);
            } else {
                this.comparison = comparator.compare(lhs, rhs);
            }
        }
        return this;
    }

    public CompareToBuilder append(long lhs, long rhs) {
        if (this.comparison == 0) {
            int i = lhs < rhs ? -1 : lhs > rhs ? 1 : 0;
            this.comparison = i;
        }
        return this;
    }

    public CompareToBuilder append(int lhs, int rhs) {
        if (this.comparison == 0) {
            int i = lhs < rhs ? -1 : lhs > rhs ? 1 : 0;
            this.comparison = i;
        }
        return this;
    }

    public CompareToBuilder append(short lhs, short rhs) {
        if (this.comparison == 0) {
            int i = lhs < rhs ? -1 : lhs > rhs ? 1 : 0;
            this.comparison = i;
        }
        return this;
    }

    public CompareToBuilder append(char lhs, char rhs) {
        if (this.comparison == 0) {
            int i = lhs < rhs ? -1 : lhs > rhs ? 1 : 0;
            this.comparison = i;
        }
        return this;
    }

    public CompareToBuilder append(byte lhs, byte rhs) {
        if (this.comparison == 0) {
            int i = lhs < rhs ? -1 : lhs > rhs ? 1 : 0;
            this.comparison = i;
        }
        return this;
    }

    public CompareToBuilder append(double lhs, double rhs) {
        if (this.comparison == 0) {
            this.comparison = NumberUtils.compare(lhs, rhs);
        }
        return this;
    }

    public CompareToBuilder append(float lhs, float rhs) {
        if (this.comparison == 0) {
            this.comparison = NumberUtils.compare(lhs, rhs);
        }
        return this;
    }

    public CompareToBuilder append(boolean lhs, boolean rhs) {
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs) {
                this.comparison = 1;
            } else {
                this.comparison = -1;
            }
        }
        return this;
    }

    public CompareToBuilder append(Object[] lhs, Object[] rhs) {
        return append(lhs, rhs, null);
    }

    public CompareToBuilder append(Object[] lhs, Object[] rhs, Comparator comparator) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2], comparator);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(long[] lhs, long[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(int[] lhs, int[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(short[] lhs, short[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(char[] lhs, char[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(byte[] lhs, byte[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(double[] lhs, double[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(float[] lhs, float[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public CompareToBuilder append(boolean[] lhs, boolean[] rhs) {
        int i = -1;
        if (this.comparison == 0 && lhs != rhs) {
            if (lhs == null) {
                this.comparison = -1;
            } else if (rhs == null) {
                this.comparison = 1;
            } else if (lhs.length != rhs.length) {
                if (lhs.length >= rhs.length) {
                    i = 1;
                }
                this.comparison = i;
            } else {
                for (int i2 = 0; i2 < lhs.length && this.comparison == 0; i2++) {
                    append(lhs[i2], rhs[i2]);
                }
            }
        }
        return this;
    }

    public int toComparison() {
        return this.comparison;
    }
}
