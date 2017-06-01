package org.apache.commons.lang;

import java.util.Random;

public class RandomStringUtils {
    private static final Random RANDOM = new Random();

    public static String random(int count) {
        return random(count, false, false);
    }

    public static String randomAscii(int count) {
        return random(count, 32, 127, false, false);
    }

    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if (count == 0) {
            return StringUtils.EMPTY;
        }
        if (count < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Requested random string length ").append(count).append(" is less than 0.").toString());
        }
        if (start == 0 && end == 0) {
            end = 123;
            start = 32;
            if (!(letters || numbers)) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }
        char[] buffer = new char[count];
        int gap = end - start;
        int count2 = count;
        while (true) {
            count = count2 - 1;
            if (count2 == 0) {
                return new String(buffer);
            }
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }
            if (!(letters && Character.isLetter(ch)) && (!(numbers && Character.isDigit(ch)) && (letters || numbers))) {
                count2 = count + 1;
            } else if (ch < '?' || ch > '?') {
                if (ch < '?' || ch > '?') {
                    if (ch < '?' || ch > '?') {
                        buffer[count] = ch;
                        count2 = count;
                    } else {
                        count2 = count + 1;
                    }
                } else if (count == 0) {
                    count2 = count + 1;
                } else {
                    buffer[count] = (char) (random.nextInt(128) + 56320);
                    count--;
                    buffer[count] = ch;
                    count2 = count;
                }
            } else if (count == 0) {
                count2 = count + 1;
            } else {
                buffer[count] = ch;
                count--;
                buffer[count] = (char) (random.nextInt(128) + 55296);
                count2 = count;
            }
        }
    }

    public static String random(int count, String chars) {
        if (chars != null) {
            return random(count, chars.toCharArray());
        }
        return random(count, 0, 0, false, false, null, RANDOM);
    }

    public static String random(int count, char[] chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, 0, chars.length, false, false, chars, RANDOM);
    }
}
