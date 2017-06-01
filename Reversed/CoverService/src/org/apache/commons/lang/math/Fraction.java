package org.apache.commons.lang.math;

import java.io.Serializable;
import java.math.BigInteger;

public final class Fraction extends Number implements Serializable, Comparable {
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
    public static final Fraction ONE_HALF = new Fraction(1, 2);
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
    public static final Fraction ONE_THIRD = new Fraction(1, 3);
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
    public static final Fraction ZERO = new Fraction(0, 1);
    private static final long serialVersionUID = 65382027393090L;
    private final int denominator;
    private transient int hashCode = 0;
    private final int numerator;
    private transient String toProperString = null;
    private transient String toString = null;

    private Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction getFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (denominator < 0) {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        return new Fraction(numerator, denominator);
    }

    public static Fraction getFraction(int whole, int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        } else if (denominator < 0) {
            throw new ArithmeticException("The denominator must not be negative");
        } else if (numerator < 0) {
            throw new ArithmeticException("The numerator must not be negative");
        } else {
            long numeratorValue;
            if (whole < 0) {
                numeratorValue = (((long) whole) * ((long) denominator)) - ((long) numerator);
            } else {
                numeratorValue = (((long) whole) * ((long) denominator)) + ((long) numerator);
            }
            if (numeratorValue >= -2147483648L && numeratorValue <= 2147483647L) {
                return new Fraction((int) numeratorValue, denominator);
            }
            throw new ArithmeticException("Numerator too large to represent as an Integer.");
        }
    }

    public static Fraction getReducedFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        } else if (numerator == 0) {
            return ZERO;
        } else {
            if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
                numerator /= 2;
                denominator /= 2;
            }
            if (denominator < 0) {
                if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                    throw new ArithmeticException("overflow: can't negate");
                }
                numerator = -numerator;
                denominator = -denominator;
            }
            int gcd = greatestCommonDivisor(numerator, denominator);
            return new Fraction(numerator / gcd, denominator / gcd);
        }
    }

    public static Fraction getFraction(double value) {
        int sign = value < 0.0d ? -1 : 1;
        value = Math.abs(value);
        if (value > 2.147483647E9d || Double.isNaN(value)) {
            throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
        }
        int wholeNumber = (int) value;
        value -= (double) wholeNumber;
        int numer0 = 0;
        int denom0 = 1;
        int numer1 = 1;
        int denom1 = 0;
        int a1 = (int) value;
        double x1 = 1.0d;
        double y1 = value - ((double) a1);
        double delta2 = Double.MAX_VALUE;
        int i = 1;
        do {
            double delta1 = delta2;
            int a2 = (int) (x1 / y1);
            double x2 = y1;
            double y2 = x1 - (((double) a2) * y1);
            int numer2 = (a1 * numer1) + numer0;
            int denom2 = (a1 * denom1) + denom0;
            delta2 = Math.abs(value - (((double) numer2) / ((double) denom2)));
            a1 = a2;
            x1 = x2;
            y1 = y2;
            numer0 = numer1;
            denom0 = denom1;
            numer1 = numer2;
            denom1 = denom2;
            i++;
            if (delta1 <= delta2 || denom2 > 10000 || denom2 <= 0) {
            }
        } while (i < 25);
        if (i != 25) {
            return getReducedFraction(((wholeNumber * denom0) + numer0) * sign, denom0);
        }
        throw new ArithmeticException("Unable to convert double to fraction");
    }

    public static Fraction getFraction(String str) {
        if (str == null) {
            throw new IllegalArgumentException("The string must not be null");
        } else if (str.indexOf(46) >= 0) {
            return getFraction(Double.parseDouble(str));
        } else {
            int pos = str.indexOf(32);
            if (pos > 0) {
                int whole = Integer.parseInt(str.substring(0, pos));
                str = str.substring(pos + 1);
                pos = str.indexOf(47);
                if (pos >= 0) {
                    return getFraction(whole, Integer.parseInt(str.substring(0, pos)), Integer.parseInt(str.substring(pos + 1)));
                }
                throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
            }
            pos = str.indexOf(47);
            if (pos < 0) {
                return getFraction(Integer.parseInt(str), 1);
            }
            return getFraction(Integer.parseInt(str.substring(0, pos)), Integer.parseInt(str.substring(pos + 1)));
        }
    }

    public int getNumerator() {
        return this.numerator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getProperNumerator() {
        return Math.abs(this.numerator % this.denominator);
    }

    public int getProperWhole() {
        return this.numerator / this.denominator;
    }

    public int intValue() {
        return this.numerator / this.denominator;
    }

    public long longValue() {
        return ((long) this.numerator) / ((long) this.denominator);
    }

    public float floatValue() {
        return ((float) this.numerator) / ((float) this.denominator);
    }

    public double doubleValue() {
        return ((double) this.numerator) / ((double) this.denominator);
    }

    public Fraction reduce() {
        int gcd = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
        return gcd == 1 ? this : getFraction(this.numerator / gcd, this.denominator / gcd);
    }

    public Fraction invert() {
        if (this.numerator == 0) {
            throw new ArithmeticException("Unable to invert zero.");
        } else if (this.numerator == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: can't negate numerator");
        } else if (this.numerator < 0) {
            return new Fraction(-this.denominator, -this.numerator);
        } else {
            return new Fraction(this.denominator, this.numerator);
        }
    }

    public Fraction negate() {
        if (this.numerator != Integer.MIN_VALUE) {
            return new Fraction(-this.numerator, this.denominator);
        }
        throw new ArithmeticException("overflow: too large to negate");
    }

    public Fraction abs() {
        return this.numerator >= 0 ? this : negate();
    }

    public Fraction pow(int power) {
        if (power == 1) {
            return this;
        }
        if (power == 0) {
            return ONE;
        }
        if (power >= 0) {
            Fraction f = multiplyBy(this);
            if (power % 2 == 0) {
                return f.pow(power / 2);
            }
            return f.pow(power / 2).multiplyBy(this);
        } else if (power == Integer.MIN_VALUE) {
            return invert().pow(2).pow(-(power / 2));
        } else {
            return invert().pow(-power);
        }
    }

    private static int greatestCommonDivisor(int u, int v) {
        if (u > 0) {
            u = -u;
        }
        if (v > 0) {
            v = -v;
        }
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
            u /= 2;
            v /= 2;
            k++;
        }
        if (k == 31) {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }
        int t = (u & 1) == 1 ? v : -(u / 2);
        while (true) {
            if ((t & 1) != 0) {
                if (t > 0) {
                    u = -t;
                } else {
                    v = t;
                }
                t = (v - u) / 2;
                if (t == 0) {
                    return (-u) * (1 << k);
                }
            } else {
                t /= 2;
            }
        }
    }

    private static int mulAndCheck(int x, int y) {
        long m = ((long) x) * ((long) y);
        if (m >= -2147483648L && m <= 2147483647L) {
            return (int) m;
        }
        throw new ArithmeticException("overflow: mul");
    }

    private static int mulPosAndCheck(int x, int y) {
        long m = ((long) x) * ((long) y);
        if (m <= 2147483647L) {
            return (int) m;
        }
        throw new ArithmeticException("overflow: mulPos");
    }

    private static int addAndCheck(int x, int y) {
        long s = ((long) x) + ((long) y);
        if (s >= -2147483648L && s <= 2147483647L) {
            return (int) s;
        }
        throw new ArithmeticException("overflow: add");
    }

    private static int subAndCheck(int x, int y) {
        long s = ((long) x) - ((long) y);
        if (s >= -2147483648L && s <= 2147483647L) {
            return (int) s;
        }
        throw new ArithmeticException("overflow: add");
    }

    public Fraction add(Fraction fraction) {
        return addSub(fraction, true);
    }

    public Fraction subtract(Fraction fraction) {
        return addSub(fraction, false);
    }

    private Fraction addSub(Fraction fraction, boolean isAdd) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (this.numerator == 0) {
            if (isAdd) {
                return fraction;
            }
            return fraction.negate();
        } else if (fraction.numerator == 0) {
            return this;
        } else {
            int d1 = greatestCommonDivisor(this.denominator, fraction.denominator);
            if (d1 == 1) {
                int uvp = mulAndCheck(this.numerator, fraction.denominator);
                int upv = mulAndCheck(fraction.numerator, this.denominator);
                return new Fraction(isAdd ? addAndCheck(uvp, upv) : subAndCheck(uvp, upv), mulPosAndCheck(this.denominator, fraction.denominator));
            }
            BigInteger uvp2 = BigInteger.valueOf((long) this.numerator).multiply(BigInteger.valueOf((long) (fraction.denominator / d1)));
            BigInteger upv2 = BigInteger.valueOf((long) fraction.numerator).multiply(BigInteger.valueOf((long) (this.denominator / d1)));
            BigInteger t = isAdd ? uvp2.add(upv2) : uvp2.subtract(upv2);
            int tmodd1 = t.mod(BigInteger.valueOf((long) d1)).intValue();
            int d2 = tmodd1 == 0 ? d1 : greatestCommonDivisor(tmodd1, d1);
            BigInteger w = t.divide(BigInteger.valueOf((long) d2));
            if (w.bitLength() <= 31) {
                return new Fraction(w.intValue(), mulPosAndCheck(this.denominator / d1, fraction.denominator / d2));
            }
            throw new ArithmeticException("overflow: numerator too large after multiply");
        }
    }

    public Fraction multiplyBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (this.numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        } else {
            int d1 = greatestCommonDivisor(this.numerator, fraction.denominator);
            int d2 = greatestCommonDivisor(fraction.numerator, this.denominator);
            return getReducedFraction(mulAndCheck(this.numerator / d1, fraction.numerator / d2), mulPosAndCheck(this.denominator / d2, fraction.denominator / d1));
        }
    }

    public Fraction divideBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (fraction.numerator != 0) {
            return multiplyBy(fraction.invert());
        } else {
            throw new ArithmeticException("The fraction to divide by must not be zero");
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        Fraction other = (Fraction) obj;
        if (getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = ((getNumerator() + 629) * 37) + getDenominator();
        }
        return this.hashCode;
    }

    public int compareTo(Object object) {
        Fraction other = (Fraction) object;
        if (this == other) {
            return 0;
        }
        if (this.numerator == other.numerator && this.denominator == other.denominator) {
            return 0;
        }
        long first = ((long) this.numerator) * ((long) other.denominator);
        long second = ((long) other.numerator) * ((long) this.denominator);
        if (first == second) {
            return 0;
        }
        if (first < second) {
            return -1;
        }
        return 1;
    }

    public String toString() {
        if (this.toString == null) {
            this.toString = new StringBuffer(32).append(getNumerator()).append('/').append(getDenominator()).toString();
        }
        return this.toString;
    }

    public String toProperString() {
        if (this.toProperString == null) {
            if (this.numerator == 0) {
                this.toProperString = "0";
            } else if (this.numerator == this.denominator) {
                this.toProperString = "1";
            } else if (this.numerator == this.denominator * -1) {
                this.toProperString = "-1";
            } else {
                if ((this.numerator > 0 ? -this.numerator : this.numerator) < (-this.denominator)) {
                    int properNumerator = getProperNumerator();
                    if (properNumerator == 0) {
                        this.toProperString = Integer.toString(getProperWhole());
                    } else {
                        this.toProperString = new StringBuffer(32).append(getProperWhole()).append(' ').append(properNumerator).append('/').append(getDenominator()).toString();
                    }
                } else {
                    this.toProperString = new StringBuffer(32).append(getNumerator()).append('/').append(getDenominator()).toString();
                }
            }
        }
        return this.toProperString;
    }
}
