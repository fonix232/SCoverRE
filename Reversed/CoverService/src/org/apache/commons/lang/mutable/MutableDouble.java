package org.apache.commons.lang.mutable;

import org.apache.commons.lang.math.NumberUtils;

public class MutableDouble extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = 1587163916;
    private double value;

    public MutableDouble(double value) {
        this.value = value;
    }

    public MutableDouble(Number value) {
        this.value = value.doubleValue();
    }

    public Object getValue() {
        return new Double(this.value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValue(Object value) {
        setValue(((Number) value).doubleValue());
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return this.value;
    }

    public boolean isNaN() {
        return Double.isNaN(this.value);
    }

    public boolean isInfinite() {
        return Double.isInfinite(this.value);
    }

    public Double toDouble() {
        return new Double(doubleValue());
    }

    public void increment() {
        this.value += 1.0d;
    }

    public void decrement() {
        this.value -= 1.0d;
    }

    public void add(double operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.doubleValue();
    }

    public void subtract(double operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.doubleValue();
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableDouble) && Double.doubleToLongBits(((MutableDouble) obj).value) == Double.doubleToLongBits(this.value);
    }

    public int hashCode() {
        long bits = Double.doubleToLongBits(this.value);
        return (int) ((bits >>> 32) ^ bits);
    }

    public int compareTo(Object obj) {
        return NumberUtils.compare(this.value, ((MutableDouble) obj).value);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
