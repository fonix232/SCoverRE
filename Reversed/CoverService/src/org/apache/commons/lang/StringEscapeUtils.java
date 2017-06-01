package org.apache.commons.lang;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.lang.exception.NestableRuntimeException;

public class StringEscapeUtils {
    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false);
    }

    public static void escapeJava(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, false);
    }

    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true);
    }

    public static void escapeJavaScript(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, true);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
        String str2 = null;
        if (str != null) {
            try {
                StringWriter writer = new StringWriter(str.length() * 2);
                escapeJavaStyleString(writer, str, escapeSingleQuotes);
                str2 = writer.toString();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return str2;
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();
            for (int i = 0; i < sz; i++) {
                char ch = str.charAt(i);
                if (ch <= '࿿') {
                    if (ch <= 'ÿ') {
                        if (ch <= '') {
                            if (ch >= ' ') {
                                switch (ch) {
                                    case '\"':
                                        out.write(92);
                                        out.write(34);
                                        break;
                                    case '\'':
                                        if (escapeSingleQuote) {
                                            out.write(92);
                                        }
                                        out.write(39);
                                        break;
                                    case '\\':
                                        out.write(92);
                                        out.write(92);
                                        break;
                                    default:
                                        out.write(ch);
                                        break;
                                }
                            }
                            switch (ch) {
                                case '\b':
                                    out.write(92);
                                    out.write(98);
                                    break;
                                case '\t':
                                    out.write(92);
                                    out.write(116);
                                    break;
                                case '\n':
                                    out.write(92);
                                    out.write(110);
                                    break;
                                case '\f':
                                    out.write(92);
                                    out.write(102);
                                    break;
                                case '\r':
                                    out.write(92);
                                    out.write(114);
                                    break;
                                default:
                                    if (ch <= '\u000f') {
                                        out.write(new StringBuffer().append("\\u000").append(hex(ch)).toString());
                                        break;
                                    } else {
                                        out.write(new StringBuffer().append("\\u00").append(hex(ch)).toString());
                                        break;
                                    }
                            }
                        }
                        out.write(new StringBuffer().append("\\u00").append(hex(ch)).toString());
                    } else {
                        out.write(new StringBuffer().append("\\u0").append(hex(ch)).toString());
                    }
                } else {
                    out.write(new StringBuffer().append("\\u").append(hex(ch)).toString());
                }
            }
        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    public static String unescapeJava(String str) {
        String str2 = null;
        if (str != null) {
            try {
                StringWriter writer = new StringWriter(str.length());
                unescapeJava(writer, str);
                str2 = writer.toString();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return str2;
    }

    public static void unescapeJava(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();
            StringBuffer unicode = new StringBuffer(4);
            boolean hadSlash = false;
            boolean inUnicode = false;
            for (int i = 0; i < sz; i++) {
                char ch = str.charAt(i);
                if (inUnicode) {
                    unicode.append(ch);
                    if (unicode.length() == 4) {
                        try {
                            out.write((char) Integer.parseInt(unicode.toString(), 16));
                            unicode.setLength(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch (NumberFormatException nfe) {
                            throw new NestableRuntimeException(new StringBuffer().append("Unable to parse unicode value: ").append(unicode).toString(), nfe);
                        }
                    }
                    continue;
                } else if (hadSlash) {
                    hadSlash = false;
                    switch (ch) {
                        case '\"':
                            out.write(34);
                            break;
                        case '\'':
                            out.write(39);
                            break;
                        case '\\':
                            out.write(92);
                            break;
                        case 'b':
                            out.write(8);
                            break;
                        case 'f':
                            out.write(12);
                            break;
                        case 'n':
                            out.write(10);
                            break;
                        case 'r':
                            out.write(13);
                            break;
                        case 't':
                            out.write(9);
                            break;
                        case 'u':
                            inUnicode = true;
                            break;
                        default:
                            out.write(ch);
                            break;
                    }
                } else if (ch == '\\') {
                    hadSlash = true;
                } else {
                    out.write(ch);
                }
            }
            if (hadSlash) {
                out.write(92);
            }
        }
    }

    public static String unescapeJavaScript(String str) {
        return unescapeJava(str);
    }

    public static void unescapeJavaScript(Writer out, String str) throws IOException {
        unescapeJava(out, str);
    }

    public static String escapeHtml(String str) {
        String str2 = null;
        if (str != null) {
            try {
                StringWriter writer = new StringWriter((int) (((double) str.length()) * 1.5d));
                escapeHtml(writer, str);
                str2 = writer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str2;
    }

    public static void escapeHtml(Writer writer, String string) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (string != null) {
            Entities.HTML40.escape(writer, string);
        }
    }

    public static String unescapeHtml(String str) {
        String str2 = null;
        if (str != null) {
            try {
                StringWriter writer = new StringWriter((int) (((double) str.length()) * 1.5d));
                unescapeHtml(writer, str);
                str2 = writer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str2;
    }

    public static void unescapeHtml(Writer writer, String string) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (string != null) {
            Entities.HTML40.unescape(writer, string);
        }
    }

    public static void escapeXml(Writer writer, String str) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (str != null) {
            Entities.XML.escape(writer, str);
        }
    }

    public static String escapeXml(String str) {
        if (str == null) {
            return null;
        }
        return Entities.XML.escape(str);
    }

    public static void unescapeXml(Writer writer, String str) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (str != null) {
            Entities.XML.unescape(writer, str);
        }
    }

    public static String unescapeXml(String str) {
        if (str == null) {
            return null;
        }
        return Entities.XML.unescape(str);
    }

    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return StringUtils.replace(str, "'", "''");
    }
}
