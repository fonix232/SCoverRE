package com.samsung.android.contextaware.manager;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.HashSet;

public class ContextAwarePropertyBundle implements Parcelable {
    protected static final Creator<ContextAwarePropertyBundle> CREATOR = new C00021();
    private static final boolean[] booleanVal = new boolean[1];
    private static char[] charArrayVal;
    private static ArrayList<Double> doubleArrayListVal;
    private static double[] doubleArrayVal;
    private static HashSet<Double> doubleHashSetVal;
    private static double doubleVal;
    private static ArrayList<Float> floatArrayListVal;
    private static float[] floatArrayVal;
    private static HashSet<Float> floatHashSetVal;
    private static float floatVal;
    private static ArrayList<Integer> integerArrayListVal;
    private static int[] integerArrayVal;
    private static HashSet<Integer> integerHashSetVal;
    private static int integerVal;
    private static ArrayList<Long> longArrayListVal;
    private static long[] longArrayVal;
    private static HashSet<Long> longHashSetVal;
    private static long longVal;
    private static ArrayList<String> stringArrayListVal;
    private static String[] stringArrayVal;
    private static HashSet<String> stringHashSetVal;
    private static String stringVal;
    private int mType;

    static class C00021 implements Creator<ContextAwarePropertyBundle> {
        C00021() {
        }

        public ContextAwarePropertyBundle createFromParcel(Parcel parcel) {
            return new ContextAwarePropertyBundle(parcel);
        }

        public ContextAwarePropertyBundle[] newArray(int i) {
            return new ContextAwarePropertyBundle[i];
        }
    }

    private enum PropertyType {
        BOOLEAN_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.booleanVal;
            }

            <T> void setValue(T t) {
                if (t instanceof Boolean) {
                    ContextAwarePropertyBundle.booleanVal[0] = t.booleanValue();
                }
            }
        },
        INTEGER_TYPE {
            <E> E getValue() {
                return Integer.valueOf(ContextAwarePropertyBundle.integerVal);
            }

            <T> void setValue(T t) {
                if (t instanceof Integer) {
                    ContextAwarePropertyBundle.integerVal = t.intValue();
                } else if (t instanceof Long) {
                    ContextAwarePropertyBundle.integerVal = t.intValue();
                } else if (t instanceof Float) {
                    ContextAwarePropertyBundle.integerVal = t.intValue();
                } else if (t instanceof Double) {
                    ContextAwarePropertyBundle.integerVal = t.intValue();
                }
            }
        },
        LONG_TYPE {
            <E> E getValue() {
                return Long.valueOf(ContextAwarePropertyBundle.longVal);
            }

            <T> void setValue(T t) {
                if (t instanceof Integer) {
                    ContextAwarePropertyBundle.longVal = t.longValue();
                } else if (t instanceof Long) {
                    ContextAwarePropertyBundle.longVal = t.longValue();
                } else if (t instanceof Float) {
                    ContextAwarePropertyBundle.longVal = t.longValue();
                } else if (t instanceof Double) {
                    ContextAwarePropertyBundle.longVal = t.longValue();
                }
            }
        },
        FLOAT_TYPE {
            <E> E getValue() {
                return Float.valueOf(ContextAwarePropertyBundle.floatVal);
            }

            <T> void setValue(T t) {
                if (t instanceof Integer) {
                    ContextAwarePropertyBundle.floatVal = t.floatValue();
                } else if (t instanceof Long) {
                    ContextAwarePropertyBundle.floatVal = t.floatValue();
                } else if (t instanceof Float) {
                    ContextAwarePropertyBundle.floatVal = t.floatValue();
                } else if (t instanceof Double) {
                    ContextAwarePropertyBundle.floatVal = (float) t.longValue();
                }
            }
        },
        DOUBLE_TYPE {
            <E> E getValue() {
                return Double.valueOf(ContextAwarePropertyBundle.doubleVal);
            }

            <T> void setValue(T t) {
                if (t instanceof Integer) {
                    ContextAwarePropertyBundle.doubleVal = t.doubleValue();
                } else if (t instanceof Long) {
                    ContextAwarePropertyBundle.doubleVal = t.doubleValue();
                } else if (t instanceof Float) {
                    ContextAwarePropertyBundle.doubleVal = t.doubleValue();
                } else if (t instanceof Double) {
                    ContextAwarePropertyBundle.doubleVal = t.doubleValue();
                }
            }
        },
        STRING_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.stringVal;
            }

            <T> void setValue(T t) {
                if (t instanceof String) {
                    ContextAwarePropertyBundle.stringVal = t;
                }
            }
        },
        CHAR_ARRAY_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.charArrayVal;
            }

            <T> void setValue(T t) {
                if (t instanceof char[]) {
                    ContextAwarePropertyBundle.charArrayVal = t;
                }
            }
        },
        INTEGER_ARRAY_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.integerArrayVal;
            }

            <T> void setValue(T t) {
                if (t instanceof int[]) {
                    ContextAwarePropertyBundle.integerArrayVal = t;
                }
            }
        },
        LONG_ARRAY_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.longArrayVal;
            }

            <T> void setValue(T t) {
                if (t instanceof long[]) {
                    ContextAwarePropertyBundle.longArrayVal = t;
                }
            }
        },
        FLOAT_ARRAY_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.floatArrayVal;
            }

            <T> void setValue(T t) {
                if (t instanceof float[]) {
                    ContextAwarePropertyBundle.floatArrayVal = t;
                }
            }
        },
        DOUBLE_ARRAY_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.doubleArrayVal;
            }

            <T> void setValue(T t) {
                if (t instanceof double[]) {
                    ContextAwarePropertyBundle.doubleArrayVal = t;
                }
            }
        },
        STRING_ARRAY_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.stringArrayVal;
            }

            <T> void setValue(T t) {
                if (t instanceof String[]) {
                    ContextAwarePropertyBundle.stringArrayVal = t;
                }
            }
        },
        INTEGER_ARRAY_LIST_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.integerArrayListVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.integerArrayListVal = (ArrayList) t;
            }
        },
        LONG_ARRAY_LIST_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.longArrayListVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.longArrayListVal = (ArrayList) t;
            }
        },
        FLOAT_ARRAY_LIST_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.floatArrayListVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.floatArrayListVal = (ArrayList) t;
            }
        },
        DOUBLE_ARRAY_LIST_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.doubleArrayListVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.doubleArrayListVal = (ArrayList) t;
            }
        },
        STRING_ARRAY_LIST_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.stringArrayListVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.stringArrayListVal = (ArrayList) t;
            }
        },
        INTEGER_HASH_SET_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.integerHashSetVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.integerHashSetVal = (HashSet) t;
            }
        },
        LONG_HASH_SET_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.longHashSetVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.longHashSetVal = (HashSet) t;
            }
        },
        FLOAT_HASH_SET_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.floatHashSetVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.floatHashSetVal = (HashSet) t;
            }
        },
        DOUBLE_HASH_SET_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.doubleHashSetVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.doubleHashSetVal = (HashSet) t;
            }
        },
        STRING_HASH_SET_TYPE {
            <E> E getValue() {
                return ContextAwarePropertyBundle.stringHashSetVal;
            }

            <T> void setValue(T t) {
                ContextAwarePropertyBundle.stringHashSetVal = (HashSet) t;
            }
        };

        protected int getCode() {
            return ordinal();
        }

        abstract <E> E getValue();

        abstract <T> void setValue(T t);
    }

    public ContextAwarePropertyBundle() {
        setType(-1);
        PropertyType.BOOLEAN_TYPE.setValue(Boolean.FALSE);
        PropertyType.INTEGER_TYPE.setValue(Integer.valueOf(0));
        PropertyType.LONG_TYPE.setValue(Long.valueOf(0));
        PropertyType.FLOAT_TYPE.setValue(Float.valueOf(0.0f));
        PropertyType.DOUBLE_TYPE.setValue(Double.valueOf(0.0d));
        PropertyType.STRING_TYPE.setValue("");
    }

    protected ContextAwarePropertyBundle(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        parcel.readBooleanArray((boolean[]) PropertyType.BOOLEAN_TYPE.getValue());
        PropertyType.INTEGER_TYPE.setValue(Integer.valueOf(parcel.readInt()));
        PropertyType.LONG_TYPE.setValue(Long.valueOf(parcel.readLong()));
        PropertyType.FLOAT_TYPE.setValue(Float.valueOf(parcel.readFloat()));
        PropertyType.DOUBLE_TYPE.setValue(Double.valueOf(parcel.readDouble()));
        PropertyType.STRING_TYPE.setValue(parcel.readString());
        PropertyType.CHAR_ARRAY_TYPE.setValue(parcel.createCharArray());
        PropertyType.INTEGER_ARRAY_TYPE.setValue(parcel.createIntArray());
        PropertyType.LONG_ARRAY_TYPE.setValue(parcel.createLongArray());
        PropertyType.FLOAT_ARRAY_TYPE.setValue(parcel.createFloatArray());
        PropertyType.DOUBLE_ARRAY_TYPE.setValue(parcel.createDoubleArray());
        PropertyType.STRING_ARRAY_TYPE.setValue(parcel.createStringArray());
        PropertyType.INTEGER_ARRAY_LIST_TYPE.setValue((ArrayList) parcel.readSerializable());
        PropertyType.LONG_ARRAY_LIST_TYPE.setValue((ArrayList) parcel.readSerializable());
        PropertyType.FLOAT_ARRAY_LIST_TYPE.setValue((ArrayList) parcel.readSerializable());
        PropertyType.DOUBLE_ARRAY_LIST_TYPE.setValue((ArrayList) parcel.readSerializable());
        PropertyType.STRING_ARRAY_LIST_TYPE.setValue((ArrayList) parcel.readSerializable());
        PropertyType.INTEGER_HASH_SET_TYPE.setValue((HashSet) parcel.readSerializable());
        PropertyType.LONG_HASH_SET_TYPE.setValue((HashSet) parcel.readSerializable());
        PropertyType.FLOAT_HASH_SET_TYPE.setValue((HashSet) parcel.readSerializable());
        PropertyType.DOUBLE_HASH_SET_TYPE.setValue((HashSet) parcel.readSerializable());
        PropertyType.STRING_HASH_SET_TYPE.setValue((HashSet) parcel.readSerializable());
        setType(parcel.readInt());
    }

    private void setType(int i) {
        this.mType = i;
    }

    public int describeContents() {
        return 0;
    }

    public int getBooleanTypeCode() {
        return PropertyType.BOOLEAN_TYPE.getCode();
    }

    public int getCharArrayTypeCode() {
        return PropertyType.CHAR_ARRAY_TYPE.getCode();
    }

    public int getDoubleArrayListTypeCode() {
        return PropertyType.DOUBLE_ARRAY_LIST_TYPE.getCode();
    }

    public int getDoubleArrayTypeCode() {
        return PropertyType.DOUBLE_ARRAY_TYPE.getCode();
    }

    public int getDoubleHashSetTypeCode() {
        return PropertyType.DOUBLE_HASH_SET_TYPE.getCode();
    }

    public int getDoubleTypeCode() {
        return PropertyType.DOUBLE_TYPE.getCode();
    }

    public int getFloatArrayListTypeCode() {
        return PropertyType.FLOAT_ARRAY_LIST_TYPE.getCode();
    }

    public int getFloatArrayTypeCode() {
        return PropertyType.FLOAT_ARRAY_TYPE.getCode();
    }

    public int getFloatHashSetTypeCode() {
        return PropertyType.FLOAT_HASH_SET_TYPE.getCode();
    }

    public int getFloatTypeCode() {
        return PropertyType.FLOAT_TYPE.getCode();
    }

    public int getIntegerArrayListTypeCode() {
        return PropertyType.INTEGER_ARRAY_LIST_TYPE.getCode();
    }

    public int getIntegerArrayTypeCode() {
        return PropertyType.INTEGER_ARRAY_TYPE.getCode();
    }

    public int getIntegerHashSetTypeCode() {
        return PropertyType.INTEGER_HASH_SET_TYPE.getCode();
    }

    public int getIntegerTypeCode() {
        return PropertyType.INTEGER_TYPE.getCode();
    }

    public int getLongArrayListTypeCode() {
        return PropertyType.LONG_ARRAY_LIST_TYPE.getCode();
    }

    public int getLongArrayTypeCode() {
        return PropertyType.LONG_ARRAY_TYPE.getCode();
    }

    public int getLongHashSetTypeCode() {
        return PropertyType.LONG_HASH_SET_TYPE.getCode();
    }

    public int getLongTypeCode() {
        return PropertyType.LONG_TYPE.getCode();
    }

    public int getStringArrayListTypeCode() {
        return PropertyType.STRING_ARRAY_LIST_TYPE.getCode();
    }

    public int getStringArrayTypeCode() {
        return PropertyType.STRING_ARRAY_TYPE.getCode();
    }

    public int getStringHashSetTypeCode() {
        return PropertyType.STRING_HASH_SET_TYPE.getCode();
    }

    public int getStringTypeCode() {
        return PropertyType.STRING_TYPE.getCode();
    }

    public int getType() {
        return this.mType;
    }

    public <E> E getValue() {
        for (PropertyType propertyType : PropertyType.values()) {
            if (getType() == propertyType.getCode()) {
                return propertyType.getValue();
            }
        }
        return null;
    }

    public <T> void setValue(int i, T t) {
        for (PropertyType propertyType : PropertyType.values()) {
            if (i == propertyType.getCode()) {
                setType(i);
                propertyType.setValue(t);
                return;
            }
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBooleanArray((boolean[]) PropertyType.BOOLEAN_TYPE.getValue());
        parcel.writeInt(((Integer) PropertyType.INTEGER_TYPE.getValue()).intValue());
        parcel.writeLong(((Long) PropertyType.LONG_TYPE.getValue()).longValue());
        parcel.writeFloat(((Float) PropertyType.FLOAT_TYPE.getValue()).floatValue());
        parcel.writeDouble(((Double) PropertyType.DOUBLE_TYPE.getValue()).doubleValue());
        parcel.writeString((String) PropertyType.STRING_TYPE.getValue());
        parcel.writeCharArray((char[]) PropertyType.CHAR_ARRAY_TYPE.getValue());
        parcel.writeIntArray((int[]) PropertyType.INTEGER_ARRAY_TYPE.getValue());
        parcel.writeLongArray((long[]) PropertyType.LONG_ARRAY_TYPE.getValue());
        parcel.writeFloatArray((float[]) PropertyType.FLOAT_ARRAY_TYPE.getValue());
        parcel.writeDoubleArray((double[]) PropertyType.DOUBLE_ARRAY_TYPE.getValue());
        parcel.writeStringArray((String[]) PropertyType.STRING_ARRAY_TYPE.getValue());
        parcel.writeSerializable((ArrayList) PropertyType.INTEGER_ARRAY_LIST_TYPE.getValue());
        parcel.writeSerializable((ArrayList) PropertyType.LONG_ARRAY_LIST_TYPE.getValue());
        parcel.writeSerializable((ArrayList) PropertyType.FLOAT_ARRAY_LIST_TYPE.getValue());
        parcel.writeSerializable((ArrayList) PropertyType.DOUBLE_ARRAY_LIST_TYPE.getValue());
        parcel.writeSerializable((ArrayList) PropertyType.STRING_ARRAY_LIST_TYPE.getValue());
        parcel.writeSerializable((HashSet) PropertyType.INTEGER_HASH_SET_TYPE.getValue());
        parcel.writeSerializable((HashSet) PropertyType.LONG_HASH_SET_TYPE.getValue());
        parcel.writeSerializable((HashSet) PropertyType.FLOAT_HASH_SET_TYPE.getValue());
        parcel.writeSerializable((HashSet) PropertyType.DOUBLE_HASH_SET_TYPE.getValue());
        parcel.writeSerializable((HashSet) PropertyType.STRING_HASH_SET_TYPE.getValue());
        parcel.writeInt(getType());
    }
}
