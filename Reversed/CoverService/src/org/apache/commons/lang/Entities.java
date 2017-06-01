package org.apache.commons.lang;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class Entities {
    private static final String[][] APOS_ARRAY;
    private static final String[][] BASIC_ARRAY;
    public static final Entities HTML32 = new Entities();
    public static final Entities HTML40 = new Entities();
    static final String[][] HTML40_ARRAY;
    static final String[][] ISO8859_1_ARRAY;
    public static final Entities XML = new Entities();
    EntityMap map = new LookupEntityMap();

    interface EntityMap {
        void add(String str, int i);

        String name(int i);

        int value(String str);
    }

    static class ArrayEntityMap implements EntityMap {
        protected int growBy;
        protected String[] names;
        protected int size;
        protected int[] values;

        public ArrayEntityMap() {
            this.growBy = 100;
            this.size = 0;
            this.names = new String[this.growBy];
            this.values = new int[this.growBy];
        }

        public ArrayEntityMap(int growBy) {
            this.growBy = 100;
            this.size = 0;
            this.growBy = growBy;
            this.names = new String[growBy];
            this.values = new int[growBy];
        }

        public void add(String name, int value) {
            ensureCapacity(this.size + 1);
            this.names[this.size] = name;
            this.values[this.size] = value;
            this.size++;
        }

        protected void ensureCapacity(int capacity) {
            if (capacity > this.names.length) {
                int newSize = Math.max(capacity, this.size + this.growBy);
                String[] newNames = new String[newSize];
                System.arraycopy(this.names, 0, newNames, 0, this.size);
                this.names = newNames;
                int[] newValues = new int[newSize];
                System.arraycopy(this.values, 0, newValues, 0, this.size);
                this.values = newValues;
            }
        }

        public String name(int value) {
            for (int i = 0; i < this.size; i++) {
                if (this.values[i] == value) {
                    return this.names[i];
                }
            }
            return null;
        }

        public int value(String name) {
            for (int i = 0; i < this.size; i++) {
                if (this.names[i].equals(name)) {
                    return this.values[i];
                }
            }
            return -1;
        }
    }

    static class BinaryEntityMap extends ArrayEntityMap {
        public BinaryEntityMap(int growBy) {
            super(growBy);
        }

        private int binarySearch(int key) {
            int low = 0;
            int high = this.size - 1;
            while (low <= high) {
                int mid = (low + high) >> 1;
                int midVal = this.values[mid];
                if (midVal < key) {
                    low = mid + 1;
                } else if (midVal <= key) {
                    return mid;
                } else {
                    high = mid - 1;
                }
            }
            return -(low + 1);
        }

        public void add(String name, int value) {
            ensureCapacity(this.size + 1);
            int insertAt = binarySearch(value);
            if (insertAt <= 0) {
                insertAt = -(insertAt + 1);
                System.arraycopy(this.values, insertAt, this.values, insertAt + 1, this.size - insertAt);
                this.values[insertAt] = value;
                System.arraycopy(this.names, insertAt, this.names, insertAt + 1, this.size - insertAt);
                this.names[insertAt] = name;
                this.size++;
            }
        }

        public String name(int value) {
            int index = binarySearch(value);
            if (index < 0) {
                return null;
            }
            return this.names[index];
        }
    }

    static abstract class MapIntMap implements EntityMap {
        protected Map mapNameToValue = new TreeMap();
        protected Map mapValueToName = new TreeMap();

        MapIntMap() {
        }

        public void add(String name, int value) {
            this.mapNameToValue.put(name, new Integer(value));
            this.mapValueToName.put(new Integer(value), name);
        }

        public String name(int value) {
            return (String) this.mapValueToName.get(new Integer(value));
        }

        public int value(String name) {
            Object value = this.mapNameToValue.get(name);
            if (value == null) {
                return -1;
            }
            return ((Integer) value).intValue();
        }
    }

    static class HashEntityMap extends MapIntMap {
    }

    static class PrimitiveEntityMap implements EntityMap {
        private Map mapNameToValue = new HashMap();
        private IntHashMap mapValueToName = new IntHashMap();

        PrimitiveEntityMap() {
        }

        public void add(String name, int value) {
            this.mapNameToValue.put(name, new Integer(value));
            this.mapValueToName.put(value, name);
        }

        public String name(int value) {
            return (String) this.mapValueToName.get(value);
        }

        public int value(String name) {
            Object value = this.mapNameToValue.get(name);
            if (value == null) {
                return -1;
            }
            return ((Integer) value).intValue();
        }
    }

    static class LookupEntityMap extends PrimitiveEntityMap {
        private int LOOKUP_TABLE_SIZE = 256;
        private String[] lookupTable;

        LookupEntityMap() {
        }

        public String name(int value) {
            if (value < this.LOOKUP_TABLE_SIZE) {
                return lookupTable()[value];
            }
            return super.name(value);
        }

        private String[] lookupTable() {
            if (this.lookupTable == null) {
                createLookupTable();
            }
            return this.lookupTable;
        }

        private void createLookupTable() {
            this.lookupTable = new String[this.LOOKUP_TABLE_SIZE];
            for (int i = 0; i < this.LOOKUP_TABLE_SIZE; i++) {
                this.lookupTable[i] = super.name(i);
            }
        }
    }

    static class TreeEntityMap extends MapIntMap {
    }

    Entities() {
    }

    static {
        r0 = new String[4][];
        r0[0] = new String[]{"quot", "34"};
        r0[1] = new String[]{"amp", "38"};
        r0[2] = new String[]{"lt", "60"};
        r0[3] = new String[]{"gt", "62"};
        BASIC_ARRAY = r0;
        r0 = new String[1][];
        r0[0] = new String[]{"apos", "39"};
        APOS_ARRAY = r0;
        r0 = new String[96][];
        r0[0] = new String[]{"nbsp", "160"};
        r0[1] = new String[]{"iexcl", "161"};
        r0[2] = new String[]{"cent", "162"};
        r0[3] = new String[]{"pound", "163"};
        r0[4] = new String[]{"curren", "164"};
        r0[5] = new String[]{"yen", "165"};
        r0[6] = new String[]{"brvbar", "166"};
        r0[7] = new String[]{"sect", "167"};
        r0[8] = new String[]{"uml", "168"};
        r0[9] = new String[]{"copy", "169"};
        r0[10] = new String[]{"ordf", "170"};
        r0[11] = new String[]{"laquo", "171"};
        r0[12] = new String[]{"not", "172"};
        r0[13] = new String[]{"shy", "173"};
        r0[14] = new String[]{"reg", "174"};
        r0[15] = new String[]{"macr", "175"};
        r0[16] = new String[]{"deg", "176"};
        r0[17] = new String[]{"plusmn", "177"};
        r0[18] = new String[]{"sup2", "178"};
        r0[19] = new String[]{"sup3", "179"};
        r0[20] = new String[]{"acute", "180"};
        r0[21] = new String[]{"micro", "181"};
        r0[22] = new String[]{"para", "182"};
        r0[23] = new String[]{"middot", "183"};
        r0[24] = new String[]{"cedil", "184"};
        r0[25] = new String[]{"sup1", "185"};
        r0[26] = new String[]{"ordm", "186"};
        r0[27] = new String[]{"raquo", "187"};
        r0[28] = new String[]{"frac14", "188"};
        r0[29] = new String[]{"frac12", "189"};
        r0[30] = new String[]{"frac34", "190"};
        r0[31] = new String[]{"iquest", "191"};
        r0[32] = new String[]{"Agrave", "192"};
        r0[33] = new String[]{"Aacute", "193"};
        r0[34] = new String[]{"Acirc", "194"};
        r0[35] = new String[]{"Atilde", "195"};
        r0[36] = new String[]{"Auml", "196"};
        r0[37] = new String[]{"Aring", "197"};
        r0[38] = new String[]{"AElig", "198"};
        r0[39] = new String[]{"Ccedil", "199"};
        r0[40] = new String[]{"Egrave", "200"};
        r0[41] = new String[]{"Eacute", "201"};
        r0[42] = new String[]{"Ecirc", "202"};
        r0[43] = new String[]{"Euml", "203"};
        r0[44] = new String[]{"Igrave", "204"};
        r0[45] = new String[]{"Iacute", "205"};
        r0[46] = new String[]{"Icirc", "206"};
        r0[47] = new String[]{"Iuml", "207"};
        r0[48] = new String[]{"ETH", "208"};
        r0[49] = new String[]{"Ntilde", "209"};
        r0[50] = new String[]{"Ograve", "210"};
        r0[51] = new String[]{"Oacute", "211"};
        r0[52] = new String[]{"Ocirc", "212"};
        r0[53] = new String[]{"Otilde", "213"};
        r0[54] = new String[]{"Ouml", "214"};
        r0[55] = new String[]{"times", "215"};
        r0[56] = new String[]{"Oslash", "216"};
        r0[57] = new String[]{"Ugrave", "217"};
        r0[58] = new String[]{"Uacute", "218"};
        r0[59] = new String[]{"Ucirc", "219"};
        r0[60] = new String[]{"Uuml", "220"};
        r0[61] = new String[]{"Yacute", "221"};
        r0[62] = new String[]{"THORN", "222"};
        r0[63] = new String[]{"szlig", "223"};
        r0[64] = new String[]{"agrave", "224"};
        r0[65] = new String[]{"aacute", "225"};
        r0[66] = new String[]{"acirc", "226"};
        r0[67] = new String[]{"atilde", "227"};
        r0[68] = new String[]{"auml", "228"};
        r0[69] = new String[]{"aring", "229"};
        r0[70] = new String[]{"aelig", "230"};
        r0[71] = new String[]{"ccedil", "231"};
        r0[72] = new String[]{"egrave", "232"};
        r0[73] = new String[]{"eacute", "233"};
        r0[74] = new String[]{"ecirc", "234"};
        r0[75] = new String[]{"euml", "235"};
        r0[76] = new String[]{"igrave", "236"};
        r0[77] = new String[]{"iacute", "237"};
        r0[78] = new String[]{"icirc", "238"};
        r0[79] = new String[]{"iuml", "239"};
        r0[80] = new String[]{"eth", "240"};
        r0[81] = new String[]{"ntilde", "241"};
        r0[82] = new String[]{"ograve", "242"};
        r0[83] = new String[]{"oacute", "243"};
        r0[84] = new String[]{"ocirc", "244"};
        r0[85] = new String[]{"otilde", "245"};
        r0[86] = new String[]{"ouml", "246"};
        r0[87] = new String[]{"divide", "247"};
        r0[88] = new String[]{"oslash", "248"};
        r0[89] = new String[]{"ugrave", "249"};
        r0[90] = new String[]{"uacute", "250"};
        r0[91] = new String[]{"ucirc", "251"};
        r0[92] = new String[]{"uuml", "252"};
        r0[93] = new String[]{"yacute", "253"};
        r0[94] = new String[]{"thorn", "254"};
        r0[95] = new String[]{"yuml", "255"};
        ISO8859_1_ARRAY = r0;
        r0 = new String[151][];
        r0[0] = new String[]{"fnof", "402"};
        r0[1] = new String[]{"Alpha", "913"};
        r0[2] = new String[]{"Beta", "914"};
        r0[3] = new String[]{"Gamma", "915"};
        r0[4] = new String[]{"Delta", "916"};
        r0[5] = new String[]{"Epsilon", "917"};
        r0[6] = new String[]{"Zeta", "918"};
        r0[7] = new String[]{"Eta", "919"};
        r0[8] = new String[]{"Theta", "920"};
        r0[9] = new String[]{"Iota", "921"};
        r0[10] = new String[]{"Kappa", "922"};
        r0[11] = new String[]{"Lambda", "923"};
        r0[12] = new String[]{"Mu", "924"};
        r0[13] = new String[]{"Nu", "925"};
        r0[14] = new String[]{"Xi", "926"};
        r0[15] = new String[]{"Omicron", "927"};
        r0[16] = new String[]{"Pi", "928"};
        r0[17] = new String[]{"Rho", "929"};
        r0[18] = new String[]{"Sigma", "931"};
        r0[19] = new String[]{"Tau", "932"};
        r0[20] = new String[]{"Upsilon", "933"};
        r0[21] = new String[]{"Phi", "934"};
        r0[22] = new String[]{"Chi", "935"};
        r0[23] = new String[]{"Psi", "936"};
        r0[24] = new String[]{"Omega", "937"};
        r0[25] = new String[]{"alpha", "945"};
        r0[26] = new String[]{"beta", "946"};
        r0[27] = new String[]{"gamma", "947"};
        r0[28] = new String[]{"delta", "948"};
        r0[29] = new String[]{"epsilon", "949"};
        r0[30] = new String[]{"zeta", "950"};
        r0[31] = new String[]{"eta", "951"};
        r0[32] = new String[]{"theta", "952"};
        r0[33] = new String[]{"iota", "953"};
        r0[34] = new String[]{"kappa", "954"};
        r0[35] = new String[]{"lambda", "955"};
        r0[36] = new String[]{"mu", "956"};
        r0[37] = new String[]{"nu", "957"};
        r0[38] = new String[]{"xi", "958"};
        r0[39] = new String[]{"omicron", "959"};
        r0[40] = new String[]{"pi", "960"};
        r0[41] = new String[]{"rho", "961"};
        r0[42] = new String[]{"sigmaf", "962"};
        r0[43] = new String[]{"sigma", "963"};
        r0[44] = new String[]{"tau", "964"};
        r0[45] = new String[]{"upsilon", "965"};
        r0[46] = new String[]{"phi", "966"};
        r0[47] = new String[]{"chi", "967"};
        r0[48] = new String[]{"psi", "968"};
        r0[49] = new String[]{"omega", "969"};
        r0[50] = new String[]{"thetasym", "977"};
        r0[51] = new String[]{"upsih", "978"};
        r0[52] = new String[]{"piv", "982"};
        r0[53] = new String[]{"bull", "8226"};
        r0[54] = new String[]{"hellip", "8230"};
        r0[55] = new String[]{"prime", "8242"};
        r0[56] = new String[]{"Prime", "8243"};
        r0[57] = new String[]{"oline", "8254"};
        r0[58] = new String[]{"frasl", "8260"};
        r0[59] = new String[]{"weierp", "8472"};
        r0[60] = new String[]{"image", "8465"};
        r0[61] = new String[]{"real", "8476"};
        r0[62] = new String[]{"trade", "8482"};
        r0[63] = new String[]{"alefsym", "8501"};
        r0[64] = new String[]{"larr", "8592"};
        r0[65] = new String[]{"uarr", "8593"};
        r0[66] = new String[]{"rarr", "8594"};
        r0[67] = new String[]{"darr", "8595"};
        r0[68] = new String[]{"harr", "8596"};
        r0[69] = new String[]{"crarr", "8629"};
        r0[70] = new String[]{"lArr", "8656"};
        r0[71] = new String[]{"uArr", "8657"};
        r0[72] = new String[]{"rArr", "8658"};
        r0[73] = new String[]{"dArr", "8659"};
        r0[74] = new String[]{"hArr", "8660"};
        r0[75] = new String[]{"forall", "8704"};
        r0[76] = new String[]{"part", "8706"};
        r0[77] = new String[]{"exist", "8707"};
        r0[78] = new String[]{"empty", "8709"};
        r0[79] = new String[]{"nabla", "8711"};
        r0[80] = new String[]{"isin", "8712"};
        r0[81] = new String[]{"notin", "8713"};
        r0[82] = new String[]{"ni", "8715"};
        r0[83] = new String[]{"prod", "8719"};
        r0[84] = new String[]{"sum", "8721"};
        r0[85] = new String[]{"minus", "8722"};
        r0[86] = new String[]{"lowast", "8727"};
        r0[87] = new String[]{"radic", "8730"};
        r0[88] = new String[]{"prop", "8733"};
        r0[89] = new String[]{"infin", "8734"};
        r0[90] = new String[]{"ang", "8736"};
        r0[91] = new String[]{"and", "8743"};
        r0[92] = new String[]{"or", "8744"};
        r0[93] = new String[]{"cap", "8745"};
        r0[94] = new String[]{"cup", "8746"};
        r0[95] = new String[]{"int", "8747"};
        r0[96] = new String[]{"there4", "8756"};
        r0[97] = new String[]{"sim", "8764"};
        r0[98] = new String[]{"cong", "8773"};
        r0[99] = new String[]{"asymp", "8776"};
        r0[100] = new String[]{"ne", "8800"};
        r0[101] = new String[]{"equiv", "8801"};
        r0[102] = new String[]{"le", "8804"};
        r0[103] = new String[]{"ge", "8805"};
        r0[104] = new String[]{"sub", "8834"};
        r0[105] = new String[]{"sup", "8835"};
        r0[106] = new String[]{"sube", "8838"};
        r0[107] = new String[]{"supe", "8839"};
        r0[108] = new String[]{"oplus", "8853"};
        r0[109] = new String[]{"otimes", "8855"};
        r0[110] = new String[]{"perp", "8869"};
        r0[111] = new String[]{"sdot", "8901"};
        r0[112] = new String[]{"lceil", "8968"};
        r0[113] = new String[]{"rceil", "8969"};
        r0[114] = new String[]{"lfloor", "8970"};
        r0[115] = new String[]{"rfloor", "8971"};
        r0[116] = new String[]{"lang", "9001"};
        r0[117] = new String[]{"rang", "9002"};
        r0[118] = new String[]{"loz", "9674"};
        r0[119] = new String[]{"spades", "9824"};
        r0[120] = new String[]{"clubs", "9827"};
        r0[121] = new String[]{"hearts", "9829"};
        r0[122] = new String[]{"diams", "9830"};
        r0[123] = new String[]{"OElig", "338"};
        r0[124] = new String[]{"oelig", "339"};
        r0[125] = new String[]{"Scaron", "352"};
        r0[126] = new String[]{"scaron", "353"};
        r0[127] = new String[]{"Yuml", "376"};
        r0[128] = new String[]{"circ", "710"};
        r0[129] = new String[]{"tilde", "732"};
        r0[130] = new String[]{"ensp", "8194"};
        r0[131] = new String[]{"emsp", "8195"};
        r0[132] = new String[]{"thinsp", "8201"};
        r0[133] = new String[]{"zwnj", "8204"};
        r0[134] = new String[]{"zwj", "8205"};
        r0[135] = new String[]{"lrm", "8206"};
        r0[136] = new String[]{"rlm", "8207"};
        r0[137] = new String[]{"ndash", "8211"};
        r0[138] = new String[]{"mdash", "8212"};
        r0[139] = new String[]{"lsquo", "8216"};
        r0[140] = new String[]{"rsquo", "8217"};
        r0[141] = new String[]{"sbquo", "8218"};
        r0[142] = new String[]{"ldquo", "8220"};
        r0[143] = new String[]{"rdquo", "8221"};
        r0[144] = new String[]{"bdquo", "8222"};
        r0[145] = new String[]{"dagger", "8224"};
        r0[146] = new String[]{"Dagger", "8225"};
        r0[147] = new String[]{"permil", "8240"};
        r0[148] = new String[]{"lsaquo", "8249"};
        r0[149] = new String[]{"rsaquo", "8250"};
        r0[150] = new String[]{"euro", "8364"};
        HTML40_ARRAY = r0;
        XML.addEntities(BASIC_ARRAY);
        XML.addEntities(APOS_ARRAY);
        HTML32.addEntities(BASIC_ARRAY);
        HTML32.addEntities(ISO8859_1_ARRAY);
        fillWithHtml40Entities(HTML40);
    }

    static void fillWithHtml40Entities(Entities entities) {
        entities.addEntities(BASIC_ARRAY);
        entities.addEntities(ISO8859_1_ARRAY);
        entities.addEntities(HTML40_ARRAY);
    }

    public void addEntities(String[][] entityArray) {
        for (int i = 0; i < entityArray.length; i++) {
            addEntity(entityArray[i][0], Integer.parseInt(entityArray[i][1]));
        }
    }

    public void addEntity(String name, int value) {
        this.map.add(name, value);
    }

    public String entityName(int value) {
        return this.map.name(value);
    }

    public int entityValue(String name) {
        return this.map.value(name);
    }

    public String escape(String str) {
        StringBuffer buf = new StringBuffer(str.length() * 2);
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String entityName = entityName(ch);
            if (entityName != null) {
                buf.append('&');
                buf.append(entityName);
                buf.append(';');
            } else if (ch > '') {
                char intValue = ch;
                buf.append("&#");
                buf.append(intValue);
                buf.append(';');
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    public void escape(Writer writer, String str) throws IOException {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            String entityName = entityName(c);
            if (entityName != null) {
                writer.write(38);
                writer.write(entityName);
                writer.write(59);
            } else if (c > '') {
                writer.write("&#");
                writer.write(Integer.toString(c, 10));
                writer.write(59);
            } else {
                writer.write(c);
            }
        }
    }

    public String unescape(String str) {
        StringBuffer buf = new StringBuffer(str.length());
        int i = 0;
        while (i < str.length()) {
            char ch = str.charAt(i);
            if (ch == '&') {
                int semi = str.indexOf(59, i + 1);
                if (semi == -1) {
                    buf.append(ch);
                } else {
                    int amph = str.indexOf(38, i + 1);
                    if (amph == -1 || amph >= semi) {
                        int entityValue;
                        String entityName = str.substring(i + 1, semi);
                        if (entityName.length() == 0) {
                            entityValue = -1;
                        } else if (entityName.charAt(0) != '#') {
                            entityValue = entityValue(entityName);
                        } else if (entityName.length() == 1) {
                            entityValue = -1;
                        } else {
                            char charAt1 = entityName.charAt(1);
                            if (charAt1 == 'x' || charAt1 == 'X') {
                                try {
                                    entityValue = Integer.valueOf(entityName.substring(2), 16).intValue();
                                } catch (NumberFormatException e) {
                                    entityValue = -1;
                                }
                            } else {
                                entityValue = Integer.parseInt(entityName.substring(1));
                            }
                        }
                        if (entityValue == -1) {
                            buf.append('&');
                            buf.append(entityName);
                            buf.append(';');
                        } else {
                            buf.append((char) entityValue);
                        }
                        i = semi;
                    } else {
                        buf.append(ch);
                    }
                }
            } else {
                buf.append(ch);
            }
            i++;
        }
        return buf.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unescape(java.io.Writer r13, java.lang.String r14) throws java.io.IOException {
        /*
        r12 = this;
        r7 = r14.length();
        if (r7 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r5 = 0;
    L_0x0008:
        if (r5 >= r7) goto L_0x0006;
    L_0x000a:
        r1 = r14.charAt(r5);
        r10 = 38;
        if (r1 != r10) goto L_0x0085;
    L_0x0012:
        r8 = r5 + 1;
        r10 = 59;
        r9 = r14.indexOf(r10, r8);
        r10 = -1;
        if (r9 != r10) goto L_0x0023;
    L_0x001d:
        r13.write(r1);
    L_0x0020:
        r5 = r5 + 1;
        goto L_0x0008;
    L_0x0023:
        r10 = 38;
        r11 = r5 + 1;
        r0 = r14.indexOf(r10, r11);
        r10 = -1;
        if (r0 == r10) goto L_0x0034;
    L_0x002e:
        if (r0 >= r9) goto L_0x0034;
    L_0x0030:
        r13.write(r1);
        goto L_0x0020;
    L_0x0034:
        r2 = r14.substring(r8, r9);
        r4 = -1;
        r3 = r2.length();
        if (r3 <= 0) goto L_0x005e;
    L_0x003f:
        r10 = 0;
        r10 = r2.charAt(r10);
        r11 = 35;
        if (r10 != r11) goto L_0x007c;
    L_0x0048:
        r10 = 1;
        if (r3 <= r10) goto L_0x005e;
    L_0x004b:
        r10 = 1;
        r6 = r2.charAt(r10);
        switch(r6) {
            case 88: goto L_0x0070;
            case 120: goto L_0x0070;
            default: goto L_0x0053;
        };
    L_0x0053:
        r10 = 1;
        r10 = r2.substring(r10);	 Catch:{ NumberFormatException -> 0x0089 }
        r11 = 10;
        r4 = java.lang.Integer.parseInt(r10, r11);	 Catch:{ NumberFormatException -> 0x0089 }
    L_0x005e:
        r10 = -1;
        if (r4 != r10) goto L_0x0081;
    L_0x0061:
        r10 = 38;
        r13.write(r10);
        r13.write(r2);
        r10 = 59;
        r13.write(r10);
    L_0x006e:
        r5 = r9;
        goto L_0x0020;
    L_0x0070:
        r10 = 2;
        r10 = r2.substring(r10);	 Catch:{ NumberFormatException -> 0x0089 }
        r11 = 16;
        r4 = java.lang.Integer.parseInt(r10, r11);	 Catch:{ NumberFormatException -> 0x0089 }
        goto L_0x0053;
    L_0x007c:
        r4 = r12.entityValue(r2);
        goto L_0x005e;
    L_0x0081:
        r13.write(r4);
        goto L_0x006e;
    L_0x0085:
        r13.write(r1);
        goto L_0x0020;
    L_0x0089:
        r10 = move-exception;
        goto L_0x005e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.Entities.unescape(java.io.Writer, java.lang.String):void");
    }
}
