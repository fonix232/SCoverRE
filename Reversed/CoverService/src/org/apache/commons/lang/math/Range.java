package org.apache.commons.lang.math;

public abstract class Range {
    public abstract boolean containsNumber(Number number);

    public abstract Number getMaximumNumber();

    public abstract Number getMinimumNumber();

    public long getMinimumLong() {
        return getMinimumNumber().longValue();
    }

    public int getMinimumInteger() {
        return getMinimumNumber().intValue();
    }

    public double getMinimumDouble() {
        return getMinimumNumber().doubleValue();
    }

    public float getMinimumFloat() {
        return getMinimumNumber().floatValue();
    }

    public long getMaximumLong() {
        return getMaximumNumber().longValue();
    }

    public int getMaximumInteger() {
        return getMaximumNumber().intValue();
    }

    public double getMaximumDouble() {
        return getMaximumNumber().doubleValue();
    }

    public float getMaximumFloat() {
        return getMaximumNumber().floatValue();
    }

    public boolean containsLong(Number value) {
        if (value == null) {
            return false;
        }
        return containsLong(value.longValue());
    }

    public boolean containsLong(long value) {
        return value >= getMinimumLong() && value <= getMaximumLong();
    }

    public boolean containsInteger(Number value) {
        if (value == null) {
            return false;
        }
        return containsInteger(value.intValue());
    }

    public boolean containsInteger(int value) {
        return value >= getMinimumInteger() && value <= getMaximumInteger();
    }

    public boolean containsDouble(Number value) {
        if (value == null) {
            return false;
        }
        return containsDouble(value.doubleValue());
    }

    public boolean containsDouble(double value) {
        return NumberUtils.compare(getMinimumDouble(), value) <= 0 && NumberUtils.compare(getMaximumDouble(), value) >= 0;
    }

    public boolean containsFloat(Number value) {
        if (value == null) {
            return false;
        }
        return containsFloat(value.floatValue());
    }

    public boolean containsFloat(float value) {
        return NumberUtils.compare(getMinimumFloat(), value) <= 0 && NumberUtils.compare(getMaximumFloat(), value) >= 0;
    }

    public boolean containsRange(Range range) {
        if (range != null && containsNumber(range.getMinimumNumber()) && containsNumber(range.getMaximumNumber())) {
            return true;
        }
        return false;
    }

    public boolean overlapsRange(Range range) {
        if (range == null) {
            return false;
        }
        if (range.containsNumber(getMinimumNumber()) || range.containsNumber(getMaximumNumber()) || containsNumber(range.getMinimumNumber())) {
            return true;
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Range range = (Range) obj;
        if (getMinimumNumber().equals(range.getMinimumNumber()) && getMaximumNumber().equals(range.getMaximumNumber())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((getClass().hashCode() + 629) * 37) + getMinimumNumber().hashCode()) * 37) + getMaximumNumber().hashCode();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(32);
        buf.append("Range[");
        buf.append(getMinimumNumber());
        buf.append(',');
        buf.append(getMaximumNumber());
        buf.append(']');
        return buf.toString();
    }
}
