package org.apache.commons.lang.enums;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.ClassUtils;

public abstract class ValuedEnum extends Enum {
    private static final long serialVersionUID = -7129650521543789085L;
    private final int iValue;

    protected ValuedEnum(String name, int value) {
        super(name);
        this.iValue = value;
    }

    protected static Enum getEnum(Class enumClass, int value) {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        }
        for (ValuedEnum enumeration : Enum.getEnumList(enumClass)) {
            if (enumeration.getValue() == value) {
                return enumeration;
            }
        }
        return null;
    }

    public final int getValue() {
        return this.iValue;
    }

    public int compareTo(Object other) {
        if (other == this) {
            return 0;
        }
        if (other.getClass() == getClass()) {
            return this.iValue - ((ValuedEnum) other).iValue;
        }
        if (other.getClass().getName().equals(getClass().getName())) {
            return this.iValue - getValueInOtherClassLoader(other);
        }
        throw new ClassCastException(new StringBuffer().append("Different enum class '").append(ClassUtils.getShortClassName(other.getClass())).append("'").toString());
    }

    private int getValueInOtherClassLoader(Object other) {
        try {
            return ((Integer) other.getClass().getMethod("getValue", null).invoke(other, null)).intValue();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("This should not happen");
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("This should not happen");
        } catch (InvocationTargetException e3) {
            throw new IllegalStateException("This should not happen");
        }
    }

    public String toString() {
        if (this.iToString == null) {
            this.iToString = new StringBuffer().append(ClassUtils.getShortClassName(getEnumClass())).append("[").append(getName()).append("=").append(getValue()).append("]").toString();
        }
        return this.iToString;
    }
}
