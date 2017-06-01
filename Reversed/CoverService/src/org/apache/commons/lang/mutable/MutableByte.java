package org.apache.commons.lang.mutable;

public class MutableByte extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = -1585823265;
    private byte value;

    public MutableByte(byte value) {
        this.value = value;
    }

    public MutableByte(Number value) {
        this.value = value.byteValue();
    }

    public Object getValue() {
        return new Byte(this.value);
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public void setValue(Object value) {
        setValue(((Number) value).byteValue());
    }

    public byte byteValue() {
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

    public Byte toByte() {
        return new Byte(byteValue());
    }

    public void increment() {
        this.value = (byte) (this.value + 1);
    }

    public void decrement() {
        this.value = (byte) (this.value - 1);
    }

    public void add(byte operand) {
        this.value = (byte) (this.value + operand);
    }

    public void add(Number operand) {
        this.value = (byte) (this.value + operand.byteValue());
    }

    public void subtract(byte operand) {
        this.value = (byte) (this.value - operand);
    }

    public void subtract(Number operand) {
        this.value = (byte) (this.value - operand.byteValue());
    }

    public boolean equals(Object obj) {
        if ((obj instanceof MutableByte) && this.value == ((MutableByte) obj).byteValue()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(Object obj) {
        byte anotherVal = ((MutableByte) obj).value;
        if (this.value < anotherVal) {
            return -1;
        }
        return this.value == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
