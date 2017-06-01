package org.apache.commons.lang.mutable;

import java.io.Serializable;

public class MutableBoolean implements Mutable, Serializable, Comparable {
    private static final long serialVersionUID = -4830728138360036487L;
    private boolean value;

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    public MutableBoolean(Boolean value) {
        this.value = value.booleanValue();
    }

    public boolean booleanValue() {
        return this.value;
    }

    public int compareTo(Object obj) {
        if (this.value == ((MutableBoolean) obj).value) {
            return 0;
        }
        return this.value ? 1 : -1;
    }

    public boolean equals(Object obj) {
        if ((obj instanceof MutableBoolean) && this.value == ((MutableBoolean) obj).booleanValue()) {
            return true;
        }
        return false;
    }

    public Object getValue() {
        return new Boolean(this.value);
    }

    public int hashCode() {
        return this.value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValue(Object value) {
        setValue(((Boolean) value).booleanValue());
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
