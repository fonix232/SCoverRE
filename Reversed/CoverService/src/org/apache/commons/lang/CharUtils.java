package org.apache.commons.lang;

public class CharUtils {
    private static final Character[] CHAR_ARRAY = new Character[128];
    private static final String CHAR_STRING = "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private static final String[] CHAR_STRING_ARRAY = new String[128];
    public static final char CR = '\r';
    public static final char LF = '\n';

    static {
        for (int i = 127; i >= 0; i--) {
            CHAR_STRING_ARRAY[i] = CHAR_STRING.substring(i, i + 1);
            CHAR_ARRAY[i] = new Character((char) i);
        }
    }

    public static Character toCharacterObject(char ch) {
        if (ch < CHAR_ARRAY.length) {
            return CHAR_ARRAY[ch];
        }
        return new Character(ch);
    }

    public static Character toCharacterObject(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return toCharacterObject(str.charAt(0));
    }

    public static char toChar(Character ch) {
        if (ch != null) {
            return ch.charValue();
        }
        throw new IllegalArgumentException("The Character must not be null");
    }

    public static char toChar(Character ch, char defaultValue) {
        return ch == null ? defaultValue : ch.charValue();
    }

    public static char toChar(String str) {
        if (!StringUtils.isEmpty(str)) {
            return str.charAt(0);
        }
        throw new IllegalArgumentException("The String must not be empty");
    }

    public static char toChar(String str, char defaultValue) {
        return StringUtils.isEmpty(str) ? defaultValue : str.charAt(0);
    }

    public static int toIntValue(char ch) {
        if (isAsciiNumeric(ch)) {
            return ch - 48;
        }
        throw new IllegalArgumentException(new StringBuffer().append("The character ").append(ch).append(" is not in the range '0' - '9'").toString());
    }

    public static int toIntValue(char ch, int defaultValue) {
        return !isAsciiNumeric(ch) ? defaultValue : ch - 48;
    }

    public static int toIntValue(Character ch) {
        if (ch != null) {
            return toIntValue(ch.charValue());
        }
        throw new IllegalArgumentException("The character must not be null");
    }

    public static int toIntValue(Character ch, int defaultValue) {
        return ch == null ? defaultValue : toIntValue(ch.charValue(), defaultValue);
    }

    public static String toString(char ch) {
        if (ch < '') {
            return CHAR_STRING_ARRAY[ch];
        }
        return new String(new char[]{ch});
    }

    public static String toString(Character ch) {
        if (ch == null) {
            return null;
        }
        return toString(ch.charValue());
    }

    public static String unicodeEscaped(char ch) {
        if (ch < '\u0010') {
            return new StringBuffer().append("\\u000").append(Integer.toHexString(ch)).toString();
        }
        if (ch < 'Ā') {
            return new StringBuffer().append("\\u00").append(Integer.toHexString(ch)).toString();
        }
        if (ch < 'က') {
            return new StringBuffer().append("\\u0").append(Integer.toHexString(ch)).toString();
        }
        return new StringBuffer().append("\\u").append(Integer.toHexString(ch)).toString();
    }

    public static String unicodeEscaped(Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());
    }

    public static boolean isAscii(char ch) {
        return ch < '';
    }

    public static boolean isAsciiPrintable(char ch) {
        return ch >= ' ' && ch < '';
    }

    public static boolean isAsciiControl(char ch) {
        return ch < ' ' || ch == '';
    }

    public static boolean isAsciiAlpha(char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
    }

    public static boolean isAsciiAlphaUpper(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isAsciiAlphaLower(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isAsciiNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static boolean isAsciiAlphanumeric(char ch) {
        return (ch >= 'A' && ch <= 'Z') || ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9'));
    }
}
