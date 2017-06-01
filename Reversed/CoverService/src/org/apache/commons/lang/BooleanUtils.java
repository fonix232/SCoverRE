package org.apache.commons.lang;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

public class BooleanUtils {
    public static Boolean negate(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
    }

    public static boolean isTrue(Boolean bool) {
        if (bool != null && bool.booleanValue()) {
            return true;
        }
        return false;
    }

    public static boolean isFalse(Boolean bool) {
        if (bool == null || bool.booleanValue()) {
            return false;
        }
        return true;
    }

    public static Boolean toBooleanObject(boolean bool) {
        return bool ? Boolean.TRUE : Boolean.FALSE;
    }

    public static boolean toBoolean(Boolean bool) {
        if (bool != null && bool.booleanValue()) {
            return true;
        }
        return false;
    }

    public static boolean toBooleanDefaultIfNull(Boolean bool, boolean valueIfNull) {
        if (bool == null) {
            return valueIfNull;
        }
        return bool.booleanValue();
    }

    public static boolean toBoolean(int value) {
        return value != 0;
    }

    public static Boolean toBooleanObject(int value) {
        return value == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    public static Boolean toBooleanObject(Integer value) {
        if (value == null) {
            return null;
        }
        return value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    public static boolean toBoolean(int value, int trueValue, int falseValue) {
        if (value == trueValue) {
            return true;
        }
        if (value == falseValue) {
            return false;
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    public static boolean toBoolean(Integer value, Integer trueValue, Integer falseValue) {
        if (value == null) {
            if (trueValue == null) {
                return true;
            }
            if (falseValue == null) {
                return false;
            }
        } else if (value.equals(trueValue)) {
            return true;
        } else {
            if (value.equals(falseValue)) {
                return false;
            }
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    public static Boolean toBooleanObject(int value, int trueValue, int falseValue, int nullValue) {
        if (value == trueValue) {
            return Boolean.TRUE;
        }
        if (value == falseValue) {
            return Boolean.FALSE;
        }
        if (value == nullValue) {
            return null;
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    public static Boolean toBooleanObject(Integer value, Integer trueValue, Integer falseValue, Integer nullValue) {
        if (value == null) {
            if (trueValue == null) {
                return Boolean.TRUE;
            }
            if (falseValue == null) {
                return Boolean.FALSE;
            }
            if (nullValue == null) {
                return null;
            }
        } else if (value.equals(trueValue)) {
            return Boolean.TRUE;
        } else {
            if (value.equals(falseValue)) {
                return Boolean.FALSE;
            }
            if (value.equals(nullValue)) {
                return null;
            }
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    public static int toInteger(boolean bool) {
        return bool ? 1 : 0;
    }

    public static Integer toIntegerObject(boolean bool) {
        return bool ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
    }

    public static Integer toIntegerObject(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool.booleanValue() ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
    }

    public static int toInteger(boolean bool, int trueValue, int falseValue) {
        return bool ? trueValue : falseValue;
    }

    public static int toInteger(Boolean bool, int trueValue, int falseValue, int nullValue) {
        if (bool == null) {
            return nullValue;
        }
        if (!bool.booleanValue()) {
            trueValue = falseValue;
        }
        return trueValue;
    }

    public static Integer toIntegerObject(boolean bool, Integer trueValue, Integer falseValue) {
        return bool ? trueValue : falseValue;
    }

    public static Integer toIntegerObject(Boolean bool, Integer trueValue, Integer falseValue, Integer nullValue) {
        if (bool == null) {
            return nullValue;
        }
        if (!bool.booleanValue()) {
            trueValue = falseValue;
        }
        return trueValue;
    }

    public static Boolean toBooleanObject(String str) {
        if ("true".equalsIgnoreCase(str)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(str)) {
            return Boolean.FALSE;
        }
        if ("on".equalsIgnoreCase(str)) {
            return Boolean.TRUE;
        }
        if ("off".equalsIgnoreCase(str)) {
            return Boolean.FALSE;
        }
        if ("yes".equalsIgnoreCase(str)) {
            return Boolean.TRUE;
        }
        if ("no".equalsIgnoreCase(str)) {
            return Boolean.FALSE;
        }
        return null;
    }

    public static Boolean toBooleanObject(String str, String trueString, String falseString, String nullString) {
        if (str == null) {
            if (trueString == null) {
                return Boolean.TRUE;
            }
            if (falseString == null) {
                return Boolean.FALSE;
            }
            if (nullString == null) {
                return null;
            }
        } else if (str.equals(trueString)) {
            return Boolean.TRUE;
        } else {
            if (str.equals(falseString)) {
                return Boolean.FALSE;
            }
            if (str.equals(nullString)) {
                return null;
            }
        }
        throw new IllegalArgumentException("The String did not match any specified value");
    }

    public static boolean toBoolean(String str) {
        boolean z = false;
        if (str == "true") {
            return true;
        }
        if (str == null) {
            return false;
        }
        char ch;
        switch (str.length()) {
            case 2:
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N')) {
                    z = true;
                }
                return z;
            case 3:
                ch = str.charAt(0);
                if (ch == 'y') {
                    if ((str.charAt(1) == 'e' || str.charAt(1) == 'E') && (str.charAt(2) == 's' || str.charAt(2) == 'S')) {
                        return true;
                    }
                    return false;
                } else if (ch == 'Y') {
                    if ((str.charAt(1) == 'E' || str.charAt(1) == 'e') && (str.charAt(2) == 'S' || str.charAt(2) == 's')) {
                        return true;
                    }
                    return false;
                }
                break;
            case DateUtils.RANGE_WEEK_CENTER /*4*/:
                break;
        }
        ch = str.charAt(0);
        if (ch != 't') {
            if (ch == 'T') {
                if ((str.charAt(1) == 'R' || str.charAt(1) == 'r') && ((str.charAt(2) == 'U' || str.charAt(2) == 'u') && (str.charAt(3) == 'E' || str.charAt(3) == 'e'))) {
                    return true;
                }
                return false;
            }
            return false;
        } else if ((str.charAt(1) == 'r' || str.charAt(1) == 'R') && ((str.charAt(2) == 'u' || str.charAt(2) == 'U') && (str.charAt(3) == 'e' || str.charAt(3) == 'E'))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean toBoolean(String str, String trueString, String falseString) {
        if (str == null) {
            if (trueString == null) {
                return true;
            }
            if (falseString == null) {
                return false;
            }
        } else if (str.equals(trueString)) {
            return true;
        } else {
            if (str.equals(falseString)) {
                return false;
            }
        }
        throw new IllegalArgumentException("The String did not match either specified value");
    }

    public static String toStringTrueFalse(Boolean bool) {
        return toString(bool, "true", "false", null);
    }

    public static String toStringOnOff(Boolean bool) {
        return toString(bool, "on", "off", null);
    }

    public static String toStringYesNo(Boolean bool) {
        return toString(bool, "yes", "no", null);
    }

    public static String toString(Boolean bool, String trueString, String falseString, String nullString) {
        if (bool == null) {
            return nullString;
        }
        if (!bool.booleanValue()) {
            trueString = falseString;
        }
        return trueString;
    }

    public static String toStringTrueFalse(boolean bool) {
        return toString(bool, "true", "false");
    }

    public static String toStringOnOff(boolean bool) {
        return toString(bool, "on", "off");
    }

    public static String toStringYesNo(boolean bool) {
        return toString(bool, "yes", "no");
    }

    public static String toString(boolean bool, String trueString, String falseString) {
        return bool ? trueString : falseString;
    }

    public static boolean xor(boolean[] array) {
        boolean z = true;
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        } else {
            int trueCount = 0;
            for (boolean z2 : array) {
                if (z2) {
                    if (trueCount >= 1) {
                        return false;
                    }
                    trueCount++;
                }
            }
            if (trueCount != 1) {
                z = false;
            }
            return z;
        }
    }

    public static Boolean xor(Boolean[] array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        } else {
            try {
                return xor(ArrayUtils.toPrimitive(array)) ? Boolean.TRUE : Boolean.FALSE;
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("The array must not contain any null elements");
            }
        }
    }
}
