package org.apache.commons.lang.mutable;

public class MutableShort extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = -2135791679;
    private short value;

    public MutableShort(short value) {
        this.value = value;
    }

    public MutableShort(Number value) {
        this.value = value.shortValue();
    }

    public Object getValue() {
        return new Short(this.value);
    }

    public void setValue(short value) {
        this.value = value;
    }

    public void setValue(Object value) {
        setValue(((Number) value).shortValue());
    }

    public void increment() {
        this.value = (short) (this.value + 1);
    }

    public void decrement() {
        this.value = (short) (this.value - 1);
    }

    public void add(short operand) {
        this.value = (short) (this.value + operand);
    }

    public void add(Number operand) {
        this.value = (short) (this.value + operand.shortValue());
    }

    public void subtract(short operand) {
        this.value = (short) (this.value - operand);
    }

    public void subtract(Number operand) {
        this.value = (short) (this.value - operand.shortValue());
    }

    public short shortValue() {
        return this.value;
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public Short toShort() {
        return new Short(shortValue());
    }

    public boolean equals(Object obj) {
        if ((obj instanceof MutableShort) && this.value == ((MutableShort) obj).shortValue()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(Object obj) {
        short anotherVal = ((MutableShort) obj).value;
        if (this.value < anotherVal) {
            return -1;
        }
        return this.value == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
