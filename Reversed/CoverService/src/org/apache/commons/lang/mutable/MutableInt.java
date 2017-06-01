package org.apache.commons.lang.mutable;

public class MutableInt extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = 512176391864L;
    private int value;

    public MutableInt(int value) {
        this.value = value;
    }

    public MutableInt(Number value) {
        this.value = value.intValue();
    }

    public Object getValue() {
        return new Integer(this.value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(Object value) {
        setValue(((Number) value).intValue());
    }

    public void increment() {
        this.value++;
    }

    public void decrement() {
        this.value--;
    }

    public void add(int operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.intValue();
    }

    public void subtract(int operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.intValue();
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

    public Integer toInteger() {
        return new Integer(intValue());
    }

    public boolean equals(Object obj) {
        if ((obj instanceof MutableInt) && this.value == ((MutableInt) obj).intValue()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(Object obj) {
        int anotherVal = ((MutableInt) obj).value;
        if (this.value < anotherVal) {
            return -1;
        }
        return this.value == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
