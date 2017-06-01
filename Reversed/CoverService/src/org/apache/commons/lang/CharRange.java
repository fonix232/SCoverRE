package org.apache.commons.lang;

import java.io.Serializable;

public final class CharRange implements Serializable {
    private static final long serialVersionUID = 8270183163158333422L;
    private final char end;
    private transient String iToString;
    private final boolean negated;
    private final char start;

    public CharRange(char ch) {
        this(ch, ch, false);
    }

    public CharRange(char ch, boolean negated) {
        this(ch, ch, negated);
    }

    public CharRange(char start, char end) {
        this(start, end, false);
    }

    public CharRange(char start, char end, boolean negated) {
        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }
        this.start = start;
        this.end = end;
        this.negated = negated;
    }

    public char getStart() {
        return this.start;
    }

    public char getEnd() {
        return this.end;
    }

    public boolean isNegated() {
        return this.negated;
    }

    public boolean contains(char ch) {
        boolean z = ch >= this.start && ch <= this.end;
        return z != this.negated;
    }

    public boolean contains(CharRange range) {
        boolean z = false;
        if (range == null) {
            throw new IllegalArgumentException("The Range must not be null");
        } else if (this.negated) {
            if (!range.negated) {
                if (range.end < this.start || range.start > this.end) {
                    z = true;
                }
                return z;
            } else if (this.start < range.start || this.end > range.end) {
                return false;
            } else {
                return true;
            }
        } else if (range.negated) {
            if (this.start == '\u0000' && this.end == '￿') {
                return true;
            }
            return false;
        } else if (this.start > range.start || this.end < range.end) {
            return false;
        } else {
            return true;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CharRange)) {
            return false;
        }
        CharRange other = (CharRange) obj;
        if (this.start == other.start && this.end == other.end && this.negated == other.negated) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.negated ? 1 : 0) + ((this.end * 7) + (this.start + 83));
    }

    public String toString() {
        if (this.iToString == null) {
            StringBuffer buf = new StringBuffer(4);
            if (isNegated()) {
                buf.append('^');
            }
            buf.append(this.start);
            if (this.start != this.end) {
                buf.append('-');
                buf.append(this.end);
            }
            this.iToString = buf.toString();
        }
        return this.iToString;
    }
}
