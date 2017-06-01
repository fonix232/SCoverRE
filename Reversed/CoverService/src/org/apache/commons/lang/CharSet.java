package org.apache.commons.lang;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CharSet implements Serializable {
    public static final CharSet ASCII_ALPHA = new CharSet("a-zA-Z");
    public static final CharSet ASCII_ALPHA_LOWER = new CharSet("a-z");
    public static final CharSet ASCII_ALPHA_UPPER = new CharSet("A-Z");
    public static final CharSet ASCII_NUMERIC = new CharSet("0-9");
    protected static final Map COMMON = new HashMap();
    public static final CharSet EMPTY = new CharSet((String) null);
    private static final long serialVersionUID = 5947847346149275958L;
    private Set set = new HashSet();

    static {
        COMMON.put(null, EMPTY);
        COMMON.put(StringUtils.EMPTY, EMPTY);
        COMMON.put("a-zA-Z", ASCII_ALPHA);
        COMMON.put("A-Za-z", ASCII_ALPHA);
        COMMON.put("a-z", ASCII_ALPHA_LOWER);
        COMMON.put("A-Z", ASCII_ALPHA_UPPER);
        COMMON.put("0-9", ASCII_NUMERIC);
    }

    public static CharSet getInstance(String setStr) {
        Object set = COMMON.get(setStr);
        if (set != null) {
            return (CharSet) set;
        }
        return new CharSet(setStr);
    }

    protected CharSet(String setStr) {
        add(setStr);
    }

    protected CharSet(String[] set) {
        for (String add : set) {
            add(add);
        }
    }

    protected void add(String str) {
        if (str != null) {
            int len = str.length();
            int pos = 0;
            while (pos < len) {
                int remainder = len - pos;
                if (remainder >= 4 && str.charAt(pos) == '^' && str.charAt(pos + 2) == '-') {
                    this.set.add(new CharRange(str.charAt(pos + 1), str.charAt(pos + 3), true));
                    pos += 4;
                } else if (remainder >= 3 && str.charAt(pos + 1) == '-') {
                    this.set.add(new CharRange(str.charAt(pos), str.charAt(pos + 2)));
                    pos += 3;
                } else if (remainder < 2 || str.charAt(pos) != '^') {
                    this.set.add(new CharRange(str.charAt(pos)));
                    pos++;
                } else {
                    this.set.add(new CharRange(str.charAt(pos + 1), true));
                    pos += 2;
                }
            }
        }
    }

    public CharRange[] getCharRanges() {
        return (CharRange[]) this.set.toArray(new CharRange[this.set.size()]);
    }

    public boolean contains(char ch) {
        for (CharRange range : this.set) {
            if (range.contains(ch)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CharSet)) {
            return false;
        }
        return this.set.equals(((CharSet) obj).set);
    }

    public int hashCode() {
        return this.set.hashCode() + 89;
    }

    public String toString() {
        return this.set.toString();
    }
}
