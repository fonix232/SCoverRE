package org.apache.commons.lang;

public class WordUtils {
    public static String wrap(String str, int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }

    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = SystemUtils.LINE_SEPARATOR;
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        int inputLineLength = str.length();
        int offset = 0;
        StringBuffer wrappedLine = new StringBuffer(inputLineLength + 32);
        while (inputLineLength - offset > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
            } else {
                int spaceToWrapAt = str.lastIndexOf(32, wrapLength + offset);
                if (spaceToWrapAt >= offset) {
                    wrappedLine.append(str.substring(offset, spaceToWrapAt));
                    wrappedLine.append(newLineStr);
                    offset = spaceToWrapAt + 1;
                } else if (wrapLongWords) {
                    wrappedLine.append(str.substring(offset, wrapLength + offset));
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                } else {
                    spaceToWrapAt = str.indexOf(32, wrapLength + offset);
                    if (spaceToWrapAt >= 0) {
                        wrappedLine.append(str.substring(offset, spaceToWrapAt));
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else {
                        wrappedLine.append(str.substring(offset));
                        offset = inputLineLength;
                    }
                }
            }
        }
        wrappedLine.append(str.substring(offset));
        return wrappedLine.toString();
    }

    public static String capitalize(String str) {
        return capitalize(str, null);
    }

    public static String capitalize(String str, char[] delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            if (isDelimiter(ch, delimiters)) {
                buffer.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    public static String capitalizeFully(String str, char[] delimiters) {
        return (str == null || str.length() == 0 || (delimiters == null ? -1 : delimiters.length) == 0) ? str : capitalize(str.toLowerCase(), delimiters);
    }

    public static String uncapitalize(String str) {
        return uncapitalize(str, null);
    }

    public static String uncapitalize(String str, char[] delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean uncapitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            if (isDelimiter(ch, delimiters)) {
                buffer.append(ch);
                uncapitalizeNext = true;
            } else if (uncapitalizeNext) {
                buffer.append(Character.toLowerCase(ch));
                uncapitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static String swapCase(String str) {
        if (str == null) {
            return str;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(strLen);
        boolean whitespace = true;
        for (int i = 0; i < strLen; i++) {
            char tmp;
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                tmp = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                tmp = Character.toLowerCase(ch);
            } else if (!Character.isLowerCase(ch)) {
                tmp = ch;
            } else if (whitespace) {
                tmp = Character.toTitleCase(ch);
            } else {
                tmp = Character.toUpperCase(ch);
            }
            buffer.append(tmp);
            whitespace = Character.isWhitespace(ch);
        }
        return buffer.toString();
    }

    public static String initials(String str) {
        return initials(str, null);
    }

    public static String initials(String str, char[] delimiters) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (delimiters != null && delimiters.length == 0) {
            return StringUtils.EMPTY;
        }
        int strLen = str.length();
        char[] buf = new char[((strLen / 2) + 1)];
        boolean lastWasGap = true;
        int i = 0;
        int count = 0;
        while (i < strLen) {
            int count2;
            char ch = str.charAt(i);
            if (isDelimiter(ch, delimiters)) {
                lastWasGap = true;
                count2 = count;
            } else if (lastWasGap) {
                count2 = count + 1;
                buf[count] = ch;
                lastWasGap = false;
            } else {
                count2 = count;
            }
            i++;
            count = count2;
        }
        return new String(buf, 0, count);
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (char c : delimiters) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }
}
