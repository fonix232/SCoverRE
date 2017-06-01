package org.apache.commons.lang.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StrSubstitutor {
    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private StrMatcher suffixMatcher;
    private StrLookup variableResolver;

    public static String replace(Object source, Map valueMap) {
        return new StrSubstitutor(valueMap).replace(source);
    }

    public static String replace(Object source, Map valueMap, String prefix, String suffix) {
        return new StrSubstitutor(valueMap, prefix, suffix).replace(source);
    }

    public static String replaceSystemProperties(Object source) {
        return new StrSubstitutor(StrLookup.systemPropertiesLookup()).replace(source);
    }

    public StrSubstitutor() {
        this((StrLookup) null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map valueMap) {
        this(StrLookup.mapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map valueMap, String prefix, String suffix) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, '$');
    }

    public StrSubstitutor(Map valueMap, String prefix, String suffix, char escape) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
    }

    public StrSubstitutor(StrLookup variableResolver) {
        this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape) {
        setVariableResolver(variableResolver);
        setVariablePrefix(prefix);
        setVariableSuffix(suffix);
        setEscapeChar(escape);
    }

    public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
        setVariableResolver(variableResolver);
        setVariablePrefixMatcher(prefixMatcher);
        setVariableSuffixMatcher(suffixMatcher);
        setEscapeChar(escape);
    }

    public String replace(String source) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(source);
        return substitute(buf, 0, source.length()) ? buf.toString() : source;
    }

    public String replace(String source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (substitute(buf, 0, length)) {
            return buf.toString();
        }
        return source.substring(offset, offset + length);
    }

    public String replace(char[] source) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(source.length).append(source);
        substitute(buf, 0, source.length);
        return buf.toString();
    }

    public String replace(char[] source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    public String replace(StringBuffer source) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(source.length()).append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    public String replace(StringBuffer source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    public String replace(StrBuilder source) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(source.length()).append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    public String replace(StrBuilder source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    public String replace(Object source) {
        if (source == null) {
            return null;
        }
        StrBuilder buf = new StrBuilder().append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    public boolean replaceIn(StringBuffer source) {
        if (source == null) {
            return false;
        }
        return replaceIn(source, 0, source.length());
    }

    public boolean replaceIn(StringBuffer source, int offset, int length) {
        if (source == null) {
            return false;
        }
        StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (!substitute(buf, 0, length)) {
            return false;
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }

    public boolean replaceIn(StrBuilder source) {
        if (source == null) {
            return false;
        }
        return substitute(source, 0, source.length());
    }

    public boolean replaceIn(StrBuilder source, int offset, int length) {
        if (source == null) {
            return false;
        }
        return substitute(source, offset, length);
    }

    protected boolean substitute(StrBuilder buf, int offset, int length) {
        return substitute(buf, offset, length, null) > 0;
    }

    private int substitute(StrBuilder buf, int offset, int length, List priorVariables) {
        StrMatcher prefixMatcher = getVariablePrefixMatcher();
        StrMatcher suffixMatcher = getVariableSuffixMatcher();
        char escape = getEscapeChar();
        boolean top = priorVariables == null;
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = buf.buffer;
        int bufEnd = offset + length;
        int pos = offset;
        while (pos < bufEnd) {
            int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);
            if (startMatchLen == 0) {
                pos++;
            } else if (pos <= offset || chars[pos - 1] != escape) {
                int startPos = pos;
                pos += startMatchLen;
                while (pos < bufEnd) {
                    int endMatchLen = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
                    if (endMatchLen == 0) {
                        pos++;
                    } else {
                        String str = new String(chars, startPos + startMatchLen, (pos - startPos) - startMatchLen);
                        pos += endMatchLen;
                        int endPos = pos;
                        if (priorVariables == null) {
                            priorVariables = new ArrayList();
                            priorVariables.add(new String(chars, offset, length));
                        }
                        checkCyclicSubstitution(str, priorVariables);
                        priorVariables.add(str);
                        String varValue = resolveVariable(str, buf, startPos, endPos);
                        if (varValue != null) {
                            int varLen = varValue.length();
                            buf.replace(startPos, endPos, varValue);
                            altered = true;
                            int change = substitute(buf, startPos, varLen, priorVariables) + (varLen - (endPos - startPos));
                            pos += change;
                            bufEnd += change;
                            lengthChange += change;
                            chars = buf.buffer;
                        }
                        priorVariables.remove(priorVariables.size() - 1);
                    }
                }
            } else {
                buf.deleteCharAt(pos - 1);
                chars = buf.buffer;
                lengthChange--;
                altered = true;
                bufEnd--;
            }
        }
        if (top) {
            return altered ? 1 : 0;
        } else {
            return lengthChange;
        }
    }

    private void checkCyclicSubstitution(String varName, List priorVariables) {
        if (priorVariables.contains(varName)) {
            StrBuilder buf = new StrBuilder(256);
            buf.append("Infinite loop in property interpolation of ");
            buf.append(priorVariables.remove(0));
            buf.append(": ");
            buf.appendWithSeparators((Collection) priorVariables, "->");
            throw new IllegalStateException(buf.toString());
        }
    }

    protected String resolveVariable(String variableName, StrBuilder buf, int startPos, int endPos) {
        StrLookup resolver = getVariableResolver();
        if (resolver == null) {
            return null;
        }
        return resolver.lookup(variableName);
    }

    public char getEscapeChar() {
        return this.escapeChar;
    }

    public void setEscapeChar(char escapeCharacter) {
        this.escapeChar = escapeCharacter;
    }

    public StrMatcher getVariablePrefixMatcher() {
        return this.prefixMatcher;
    }

    public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
        if (prefixMatcher == null) {
            throw new IllegalArgumentException("Variable prefix matcher must not be null!");
        }
        this.prefixMatcher = prefixMatcher;
        return this;
    }

    public StrSubstitutor setVariablePrefix(char prefix) {
        return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
    }

    public StrSubstitutor setVariablePrefix(String prefix) {
        if (prefix != null) {
            return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
        }
        throw new IllegalArgumentException("Variable prefix must not be null!");
    }

    public StrMatcher getVariableSuffixMatcher() {
        return this.suffixMatcher;
    }

    public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
        if (suffixMatcher == null) {
            throw new IllegalArgumentException("Variable suffix matcher must not be null!");
        }
        this.suffixMatcher = suffixMatcher;
        return this;
    }

    public StrSubstitutor setVariableSuffix(char suffix) {
        return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
    }

    public StrSubstitutor setVariableSuffix(String suffix) {
        if (suffix != null) {
            return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
        }
        throw new IllegalArgumentException("Variable suffix must not be null!");
    }

    public StrLookup getVariableResolver() {
        return this.variableResolver;
    }

    public void setVariableResolver(StrLookup variableResolver) {
        this.variableResolver = variableResolver;
    }
}
