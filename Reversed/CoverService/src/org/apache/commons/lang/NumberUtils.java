package org.apache.commons.lang;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class NumberUtils {
    public static int stringToInt(String str) {
        return stringToInt(str, 0);
    }

    public static int stringToInt(String str, int defaultValue) {
        try {
            defaultValue = Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        return defaultValue;
    }

    public static Number createNumber(String val) throws NumberFormatException {
        if (val == null) {
            return null;
        }
        if (val.length() == 0) {
            throw new NumberFormatException("\"\" is not a valid number.");
        } else if (val.startsWith("--")) {
            return null;
        } else {
            if (val.startsWith("0x") || val.startsWith("-0x")) {
                return createInteger(val);
            }
            String dec;
            String mant;
            char lastChar = val.charAt(val.length() - 1);
            int decPos = val.indexOf(46);
            int expPos = (val.indexOf(101) + val.indexOf(69)) + 1;
            if (decPos > -1) {
                if (expPos <= -1) {
                    dec = val.substring(decPos + 1);
                } else if (expPos < decPos) {
                    throw new NumberFormatException(new StringBuffer().append(val).append(" is not a valid number.").toString());
                } else {
                    dec = val.substring(decPos + 1, expPos);
                }
                mant = val.substring(0, decPos);
            } else {
                if (expPos > -1) {
                    mant = val.substring(0, expPos);
                } else {
                    mant = val;
                }
                dec = null;
            }
            String exp;
            boolean allZeros;
            Number f;
            Number d;
            if (Character.isDigit(lastChar)) {
                if (expPos <= -1 || expPos >= val.length() - 1) {
                    exp = null;
                } else {
                    exp = val.substring(expPos + 1, val.length());
                }
                if (dec == null && exp == null) {
                    try {
                        return createInteger(val);
                    } catch (NumberFormatException e) {
                        try {
                            return createLong(val);
                        } catch (NumberFormatException e2) {
                            return createBigInteger(val);
                        }
                    }
                }
                allZeros = isAllZeros(mant) && isAllZeros(exp);
                try {
                    f = createFloat(val);
                    if (!f.isInfinite() && (f.floatValue() != 0.0f || allZeros)) {
                        return f;
                    }
                } catch (NumberFormatException e3) {
                }
                try {
                    d = createDouble(val);
                    if (!d.isInfinite() && (d.doubleValue() != 0.0d || allZeros)) {
                        return d;
                    }
                } catch (NumberFormatException e4) {
                }
                return createBigDecimal(val);
            }
            if (expPos <= -1 || expPos >= val.length() - 1) {
                exp = null;
            } else {
                exp = val.substring(expPos + 1, val.length() - 1);
            }
            String numeric = val.substring(0, val.length() - 1);
            allZeros = isAllZeros(mant) && isAllZeros(exp);
            switch (lastChar) {
                case 'D':
                case 'd':
                    break;
                case 'F':
                case 'f':
                    try {
                        f = createFloat(numeric);
                        if (!f.isInfinite() && (f.floatValue() != 0.0f || allZeros)) {
                            return f;
                        }
                    } catch (NumberFormatException e5) {
                        break;
                    }
                case 'L':
                case 'l':
                    if (dec == null && exp == null && isDigits(numeric.substring(1)) && (numeric.charAt(0) == '-' || Character.isDigit(numeric.charAt(0)))) {
                        try {
                            return createLong(numeric);
                        } catch (NumberFormatException e6) {
                            return createBigInteger(numeric);
                        }
                    }
                    throw new NumberFormatException(new StringBuffer().append(val).append(" is not a valid number.").toString());
            }
            try {
                d = createDouble(numeric);
                if (!d.isInfinite() && (((double) d.floatValue()) != 0.0d || allZeros)) {
                    return d;
                }
            } catch (NumberFormatException e7) {
            }
            try {
                return createBigDecimal(numeric);
            } catch (NumberFormatException e8) {
                throw new NumberFormatException(new StringBuffer().append(val).append(" is not a valid number.").toString());
            }
        }
    }

    private static boolean isAllZeros(String s) {
        if (s == null) {
            return true;
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != '0') {
                return false;
            }
        }
        if (s.length() <= 0) {
            return false;
        }
        return true;
    }

    public static Float createFloat(String val) {
        return Float.valueOf(val);
    }

    public static Double createDouble(String val) {
        return Double.valueOf(val);
    }

    public static Integer createInteger(String val) {
        return Integer.decode(val);
    }

    public static Long createLong(String val) {
        return Long.valueOf(val);
    }

    public static BigInteger createBigInteger(String val) {
        return new BigInteger(val);
    }

    public static BigDecimal createBigDecimal(String val) {
        return new BigDecimal(val);
    }

    public static long minimum(long a, long b, long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static int minimum(int a, int b, int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static long maximum(long a, long b, long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static int maximum(int a, int b, int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static int compare(double lhs, double rhs) {
        if (lhs < rhs) {
            return -1;
        }
        if (lhs > rhs) {
            return 1;
        }
        long lhsBits = Double.doubleToLongBits(lhs);
        long rhsBits = Double.doubleToLongBits(rhs);
        if (lhsBits == rhsBits) {
            return 0;
        }
        if (lhsBits >= rhsBits) {
            return 1;
        }
        return -1;
    }

    public static int compare(float lhs, float rhs) {
        if (lhs < rhs) {
            return -1;
        }
        if (lhs > rhs) {
            return 1;
        }
        int lhsBits = Float.floatToIntBits(lhs);
        int rhsBits = Float.floatToIntBits(rhs);
        if (lhsBits == rhsBits) {
            return 0;
        }
        if (lhsBits >= rhsBits) {
            return 1;
        }
        return -1;
    }

    public static boolean isDigits(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber(String str) {
        boolean z = true;
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        int start;
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        if (chars[0] == '-') {
            start = 1;
        } else {
            start = 0;
        }
        int i;
        if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
            i = start + 2;
            if (i == sz) {
                return false;
            }
            while (i < chars.length) {
                if ((chars[i] < '0' || chars[i] > '9') && ((chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F'))) {
                    return false;
                }
                i++;
            }
            return true;
        }
        sz--;
        i = start;
        while (true) {
            if (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
                if (chars[i] >= '0' && chars[i] <= '9') {
                    foundDigit = true;
                    allowSigns = false;
                } else if (chars[i] == ClassUtils.PACKAGE_SEPARATOR_CHAR) {
                    if (hasDecPoint || hasExp) {
                        return false;
                    }
                    hasDecPoint = true;
                } else if (chars[i] == 'e' || chars[i] == 'E') {
                    if (hasExp || !foundDigit) {
                        return false;
                    }
                    hasExp = true;
                    allowSigns = true;
                } else if ((chars[i] != '+' && chars[i] != '-') || !allowSigns) {
                    return false;
                } else {
                    allowSigns = false;
                    foundDigit = false;
                }
                i++;
            }
        }
        if (i >= chars.length) {
            if (allowSigns || !foundDigit) {
                z = false;
            }
            return z;
        } else if (chars[i] >= '0' && chars[i] <= '9') {
            return true;
        } else {
            if (chars[i] == 'e' || chars[i] == 'E') {
                return false;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] != 'l' && chars[i] != 'L') {
                return false;
            }
            if (!foundDigit || hasExp) {
                z = false;
            }
            return z;
        }
    }
}
