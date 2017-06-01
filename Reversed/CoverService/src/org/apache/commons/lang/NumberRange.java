package org.apache.commons.lang;

public final class NumberRange {
    private final Number max;
    private final Number min;

    public NumberRange(Number num) {
        if (num == null) {
            throw new NullPointerException("The number must not be null");
        }
        this.min = num;
        this.max = num;
    }

    public NumberRange(Number min, Number max) {
        if (min == null) {
            throw new NullPointerException("The minimum value must not be null");
        } else if (max == null) {
            throw new NullPointerException("The maximum value must not be null");
        } else if (max.doubleValue() < min.doubleValue()) {
            this.max = min;
            this.min = min;
        } else {
            this.min = min;
            this.max = max;
        }
    }

    public Number getMinimum() {
        return this.min;
    }

    public Number getMaximum() {
        return this.max;
    }

    public boolean includesNumber(Number number) {
        if (number != null && this.min.doubleValue() <= number.doubleValue() && this.max.doubleValue() >= number.doubleValue()) {
            return true;
        }
        return false;
    }

    public boolean includesRange(NumberRange range) {
        if (range != null && includesNumber(range.min) && includesNumber(range.max)) {
            return true;
        }
        return false;
    }

    public boolean overlaps(NumberRange range) {
        if (range == null) {
            return false;
        }
        if (range.includesNumber(this.min) || range.includesNumber(this.max) || includesRange(range)) {
            return true;
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NumberRange)) {
            return false;
        }
        NumberRange range = (NumberRange) obj;
        if (this.min.equals(range.min) && this.max.equals(range.max)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.min.hashCode() + 629) * 37) + this.max.hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.min.doubleValue() < 0.0d) {
            sb.append('(').append(this.min).append(')');
        } else {
            sb.append(this.min);
        }
        sb.append('-');
        if (this.max.doubleValue() < 0.0d) {
            sb.append('(').append(this.max).append(')');
        } else {
            sb.append(this.max);
        }
        return sb.toString();
    }
}
