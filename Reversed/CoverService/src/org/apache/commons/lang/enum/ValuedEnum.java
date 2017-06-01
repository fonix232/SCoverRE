package org.apache.commons.lang.enum;

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
        return this.iValue - ((ValuedEnum) other).iValue;
    }

    public String toString() {
        if (this.iToString == null) {
            this.iToString = new StringBuffer().append(ClassUtils.getShortClassName(getEnumClass())).append("[").append(getName()).append("=").append(getValue()).append("]").toString();
        }
        return this.iToString;
    }
}
