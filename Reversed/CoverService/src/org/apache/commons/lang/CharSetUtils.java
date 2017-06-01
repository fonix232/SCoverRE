package org.apache.commons.lang;

public class CharSetUtils {
    public static CharSet evaluateSet(String[] set) {
        if (set == null) {
            return null;
        }
        return new CharSet(set);
    }

    public static String squeeze(String str, String set) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(set)) {
            return str;
        }
        return squeeze(str, new String[]{set});
    }

    public static String squeeze(String str, String[] set) {
        if (StringUtils.isEmpty(str) || ArrayUtils.isEmpty((Object[]) set)) {
            return str;
        }
        CharSet chars = evaluateSet(set);
        StringBuffer buffer = new StringBuffer(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;
        char lastChar = ' ';
        int i = 0;
        while (i < sz) {
            char ch = chrs[i];
            if (!chars.contains(ch) || ch != lastChar || i == 0) {
                buffer.append(ch);
                lastChar = ch;
            }
            i++;
        }
        return buffer.toString();
    }

    public static int count(String str, String set) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(set)) {
            return 0;
        }
        return count(str, new String[]{set});
    }

    public static int count(String str, String[] set) {
        if (StringUtils.isEmpty(str) || ArrayUtils.isEmpty((Object[]) set)) {
            return 0;
        }
        CharSet chars = evaluateSet(set);
        int count = 0;
        for (char contains : str.toCharArray()) {
            if (chars.contains(contains)) {
                count++;
            }
        }
        return count;
    }

    public static String keep(String str, String set) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0 || StringUtils.isEmpty(set)) {
            return StringUtils.EMPTY;
        }
        return keep(str, new String[]{set});
    }

    public static String keep(String str, String[] set) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0 || ArrayUtils.isEmpty((Object[]) set)) {
            return StringUtils.EMPTY;
        }
        return modify(str, set, true);
    }

    public static String delete(String str, String set) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(set)) {
            return str;
        }
        return delete(str, new String[]{set});
    }

    public static String delete(String str, String[] set) {
        return (StringUtils.isEmpty(str) || ArrayUtils.isEmpty((Object[]) set)) ? str : modify(str, set, false);
    }

    private static String modify(String str, String[] set, boolean expect) {
        CharSet chars = evaluateSet(set);
        StringBuffer buffer = new StringBuffer(str.length());
        char[] chrs = str.toCharArray();
        int sz = chrs.length;
        for (int i = 0; i < sz; i++) {
            if (chars.contains(chrs[i]) == expect) {
                buffer.append(chrs[i]);
            }
        }
        return buffer.toString();
    }

    public static String translate(String str, String searchChars, String replaceChars) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(str.length());
        char[] chrs = str.toCharArray();
        char[] withChrs = replaceChars.toCharArray();
        int sz = chrs.length;
        int withMax = replaceChars.length() - 1;
        for (int i = 0; i < sz; i++) {
            int idx = searchChars.indexOf(chrs[i]);
            if (idx != -1) {
                if (idx > withMax) {
                    idx = withMax;
                }
                buffer.append(withChrs[idx]);
            } else {
                buffer.append(chrs[i]);
            }
        }
        return buffer.toString();
    }
}
