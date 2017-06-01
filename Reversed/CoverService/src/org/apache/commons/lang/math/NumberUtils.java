package org.apache.commons.lang.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

public class NumberUtils {
    public static final Byte BYTE_MINUS_ONE = new Byte((byte) -1);
    public static final Byte BYTE_ONE = new Byte((byte) 1);
    public static final Byte BYTE_ZERO = new Byte((byte) 0);
    public static final Double DOUBLE_MINUS_ONE = new Double(-1.0d);
    public static final Double DOUBLE_ONE = new Double(1.0d);
    public static final Double DOUBLE_ZERO = new Double(0.0d);
    public static final Float FLOAT_MINUS_ONE = new Float(-1.0f);
    public static final Float FLOAT_ONE = new Float(1.0f);
    public static final Float FLOAT_ZERO = new Float(0.0f);
    public static final Integer INTEGER_MINUS_ONE = new Integer(-1);
    public static final Integer INTEGER_ONE = new Integer(1);
    public static final Integer INTEGER_ZERO = new Integer(0);
    public static final Long LONG_MINUS_ONE = new Long(-1);
    public static final Long LONG_ONE = new Long(1);
    public static final Long LONG_ZERO = new Long(0);
    public static final Short SHORT_MINUS_ONE = new Short((short) -1);
    public static final Short SHORT_ONE = new Short((short) 1);
    public static final Short SHORT_ZERO = new Short((short) 0);

    public static int stringToInt(String str) {
        return toInt(str);
    }

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int stringToInt(String str, int defaultValue) {
        return toInt(str, defaultValue);
    }

    public static int toInt(String str, int defaultValue) {
        if (str != null) {
            try {
                defaultValue = Integer.parseInt(str);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static long toLong(String str) {
        return toLong(str, 0);
    }

    public static long toLong(String str, long defaultValue) {
        if (str != null) {
            try {
                defaultValue = Long.parseLong(str);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static float toFloat(String str) {
        return toFloat(str, 0.0f);
    }

    public static float toFloat(String str, float defaultValue) {
        if (str != null) {
            try {
                defaultValue = Float.parseFloat(str);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static double toDouble(String str) {
        return toDouble(str, 0.0d);
    }

    public static double toDouble(String str, double defaultValue) {
        if (str != null) {
            try {
                defaultValue = Double.parseDouble(str);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static Number createNumber(String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        } else if (str.startsWith("--")) {
            return null;
        } else {
            if (str.startsWith("0x") || str.startsWith("-0x")) {
                return createInteger(str);
            }
            String dec;
            String mant;
            char lastChar = str.charAt(str.length() - 1);
            int decPos = str.indexOf(46);
            int expPos = (str.indexOf(101) + str.indexOf(69)) + 1;
            if (decPos > -1) {
                if (expPos <= -1) {
                    dec = str.substring(decPos + 1);
                } else if (expPos < decPos) {
                    throw new NumberFormatException(new StringBuffer().append(str).append(" is not a valid number.").toString());
                } else {
                    dec = str.substring(decPos + 1, expPos);
                }
                mant = str.substring(0, decPos);
            } else {
                if (expPos > -1) {
                    mant = str.substring(0, expPos);
                } else {
                    mant = str;
                }
                dec = null;
            }
            String exp;
            boolean allZeros;
            Number f;
            Number d;
            if (Character.isDigit(lastChar)) {
                if (expPos <= -1 || expPos >= str.length() - 1) {
                    exp = null;
                } else {
                    exp = str.substring(expPos + 1, str.length());
                }
                if (dec == null && exp == null) {
                    try {
                        return createInteger(str);
                    } catch (NumberFormatException e) {
                        try {
                            return createLong(str);
                        } catch (NumberFormatException e2) {
                            return createBigInteger(str);
                        }
                    }
                }
                allZeros = isAllZeros(mant) && isAllZeros(exp);
                try {
                    f = createFloat(str);
                    if (!f.isInfinite() && (f.floatValue() != 0.0f || allZeros)) {
                        return f;
                    }
                } catch (NumberFormatException e3) {
                }
                try {
                    d = createDouble(str);
                    if (!d.isInfinite() && (d.doubleValue() != 0.0d || allZeros)) {
                        return d;
                    }
                } catch (NumberFormatException e4) {
                }
                return createBigDecimal(str);
            }
            if (expPos <= -1 || expPos >= str.length() - 1) {
                exp = null;
            } else {
                exp = str.substring(expPos + 1, str.length() - 1);
            }
            String numeric = str.substring(0, str.length() - 1);
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
                    throw new NumberFormatException(new StringBuffer().append(str).append(" is not a valid number.").toString());
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
                throw new NumberFormatException(new StringBuffer().append(str).append(" is not a valid number.").toString());
            }
        }
    }

    private static boolean isAllZeros(String str) {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }
        if (str.length() <= 0) {
            return false;
        }
        return true;
    }

    public static Float createFloat(String str) {
        if (str == null) {
            return null;
        }
        return Float.valueOf(str);
    }

    public static Double createDouble(String str) {
        if (str == null) {
            return null;
        }
        return Double.valueOf(str);
    }

    public static Integer createInteger(String str) {
        if (str == null) {
            return null;
        }
        return Integer.decode(str);
    }

    public static Long createLong(String str) {
        if (str == null) {
            return null;
        }
        return Long.valueOf(str);
    }

    public static BigInteger createBigInteger(String str) {
        if (str == null) {
            return null;
        }
        return new BigInteger(str);
    }

    public static BigDecimal createBigDecimal(String str) {
        if (str == null) {
            return null;
        }
        if (!StringUtils.isBlank(str)) {
            return new BigDecimal(str);
        }
        throw new NumberFormatException("A blank string is not a valid number");
    }

    public static long min(long[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            long min = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
            return min;
        }
    }

    public static int min(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            int min = array[0];
            for (int j = 1; j < array.length; j++) {
                if (array[j] < min) {
                    min = array[j];
                }
            }
            return min;
        }
    }

    public static short min(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            short min = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
            return min;
        }
    }

    public static double min(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            double min = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
            return min;
        }
    }

    public static float min(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            float min = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
            return min;
        }
    }

    public static long max(long[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            long max = array[0];
            for (int j = 1; j < array.length; j++) {
                if (array[j] > max) {
                    max = array[j];
                }
            }
            return max;
        }
    }

    public static int max(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            int max = array[0];
            for (int j = 1; j < array.length; j++) {
                if (array[j] > max) {
                    max = array[j];
                }
            }
            return max;
        }
    }

    public static short max(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            short max = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
            return max;
        }
    }

    public static double max(double[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            double max = array[0];
            for (int j = 1; j < array.length; j++) {
                if (array[j] > max) {
                    max = array[j];
                }
            }
            return max;
        }
    }

    public static float max(float[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            float max = array[0];
            for (int j = 1; j < array.length; j++) {
                if (array[j] > max) {
                    max = array[j];
                }
            }
            return max;
        }
    }

    public static long min(long a, long b, long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static int min(int a, int b, int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static short min(short a, short b, short c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static byte min(byte a, byte b, byte c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            return c;
        }
        return a;
    }

    public static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    public static float min(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }

    public static long max(long a, long b, long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static int max(int a, int b, int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static short max(short a, short b, short c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static byte max(byte a, byte b, byte c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            return c;
        }
        return a;
    }

    public static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    public static float max(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
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
        if (StringUtils.isEmpty(str)) {
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
