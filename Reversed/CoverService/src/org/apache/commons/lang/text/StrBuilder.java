package org.apache.commons.lang.text;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class StrBuilder implements Cloneable {
    static final int CAPACITY = 32;
    private static final long serialVersionUID = 7628716375283629643L;
    protected char[] buffer;
    private String newLine;
    private String nullText;
    protected int size;

    class StrBuilderReader extends Reader {
        private int mark;
        private int pos;
        private final StrBuilder this$0;

        StrBuilderReader(StrBuilder this$0) {
            this.this$0 = this$0;
        }

        public void close() {
        }

        public int read() {
            if (!ready()) {
                return -1;
            }
            StrBuilder strBuilder = this.this$0;
            int i = this.pos;
            this.pos = i + 1;
            return strBuilder.charAt(i);
        }

        public int read(char[] b, int off, int len) {
            if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            } else {
                if (this.pos >= this.this$0.size()) {
                    return -1;
                }
                if (this.pos + len > this.this$0.size()) {
                    len = this.this$0.size() - this.pos;
                }
                this.this$0.getChars(this.pos, this.pos + len, b, off);
                this.pos += len;
                return len;
            }
        }

        public long skip(long n) {
            if (((long) this.pos) + n > ((long) this.this$0.size())) {
                n = (long) (this.this$0.size() - this.pos);
            }
            if (n < 0) {
                return 0;
            }
            this.pos = (int) (((long) this.pos) + n);
            return n;
        }

        public boolean ready() {
            return this.pos < this.this$0.size();
        }

        public boolean markSupported() {
            return true;
        }

        public void mark(int readAheadLimit) {
            this.mark = this.pos;
        }

        public void reset() {
            this.pos = this.mark;
        }
    }

    class StrBuilderTokenizer extends StrTokenizer {
        private final StrBuilder this$0;

        StrBuilderTokenizer(StrBuilder this$0) {
            this.this$0 = this$0;
        }

        protected List tokenize(char[] chars, int offset, int count) {
            if (chars == null) {
                return super.tokenize(this.this$0.buffer, 0, this.this$0.size());
            }
            return super.tokenize(chars, offset, count);
        }

        public String getContent() {
            String str = super.getContent();
            if (str == null) {
                return this.this$0.toString();
            }
            return str;
        }
    }

    class StrBuilderWriter extends Writer {
        private final StrBuilder this$0;

        StrBuilderWriter(StrBuilder this$0) {
            this.this$0 = this$0;
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(int c) {
            this.this$0.append((char) c);
        }

        public void write(char[] cbuf) {
            this.this$0.append(cbuf);
        }

        public void write(char[] cbuf, int off, int len) {
            this.this$0.append(cbuf, off, len);
        }

        public void write(String str) {
            this.this$0.append(str);
        }

        public void write(String str, int off, int len) {
            this.this$0.append(str, off, len);
        }
    }

    public StrBuilder() {
        this((int) CAPACITY);
    }

    public StrBuilder(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = CAPACITY;
        }
        this.buffer = new char[initialCapacity];
    }

    public StrBuilder(String str) {
        if (str == null) {
            this.buffer = new char[CAPACITY];
            return;
        }
        this.buffer = new char[(str.length() + CAPACITY)];
        append(str);
    }

    public String getNewLineText() {
        return this.newLine;
    }

    public StrBuilder setNewLineText(String newLine) {
        this.newLine = newLine;
        return this;
    }

    public String getNullText() {
        return this.nullText;
    }

    public StrBuilder setNullText(String nullText) {
        if (nullText != null && nullText.length() == 0) {
            nullText = null;
        }
        this.nullText = nullText;
        return this;
    }

    public int length() {
        return this.size;
    }

    public StrBuilder setLength(int length) {
        if (length < 0) {
            throw new StringIndexOutOfBoundsException(length);
        }
        if (length < this.size) {
            this.size = length;
        } else if (length > this.size) {
            ensureCapacity(length);
            int oldEnd = this.size;
            int newEnd = length;
            this.size = length;
            for (int i = oldEnd; i < newEnd; i++) {
                this.buffer[i] = '\u0000';
            }
        }
        return this;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public StrBuilder ensureCapacity(int capacity) {
        if (capacity > this.buffer.length) {
            char[] old = this.buffer;
            this.buffer = new char[capacity];
            System.arraycopy(old, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public StrBuilder minimizeCapacity() {
        if (this.buffer.length > length()) {
            char[] old = this.buffer;
            this.buffer = new char[length()];
            System.arraycopy(old, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public StrBuilder clear() {
        this.size = 0;
        return this;
    }

    public char charAt(int index) {
        if (index >= 0 && index < length()) {
            return this.buffer[index];
        }
        throw new StringIndexOutOfBoundsException(index);
    }

    public StrBuilder setCharAt(int index, char ch) {
        if (index < 0 || index >= length()) {
            throw new StringIndexOutOfBoundsException(index);
        }
        this.buffer[index] = ch;
        return this;
    }

    public StrBuilder deleteCharAt(int index) {
        if (index < 0 || index >= this.size) {
            throw new StringIndexOutOfBoundsException(index);
        }
        deleteImpl(index, index + 1, 1);
        return this;
    }

    public char[] toCharArray() {
        if (this.size == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] chars = new char[this.size];
        System.arraycopy(this.buffer, 0, chars, 0, this.size);
        return chars;
    }

    public char[] toCharArray(int startIndex, int endIndex) {
        int len = validateRange(startIndex, endIndex) - startIndex;
        if (len == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] chars = new char[len];
        System.arraycopy(this.buffer, startIndex, chars, 0, len);
        return chars;
    }

    public char[] getChars(char[] destination) {
        int len = length();
        if (destination == null || destination.length < len) {
            destination = new char[len];
        }
        System.arraycopy(this.buffer, 0, destination, 0, len);
        return destination;
    }

    public void getChars(int startIndex, int endIndex, char[] destination, int destinationIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        } else if (endIndex < 0 || endIndex > length()) {
            throw new StringIndexOutOfBoundsException(endIndex);
        } else if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        } else {
            System.arraycopy(this.buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
        }
    }

    public StrBuilder appendNewLine() {
        if (this.newLine != null) {
            return append(this.newLine);
        }
        append(SystemUtils.LINE_SEPARATOR);
        return this;
    }

    public StrBuilder appendNull() {
        return this.nullText == null ? this : append(this.nullText);
    }

    public StrBuilder append(Object obj) {
        if (obj == null) {
            return appendNull();
        }
        return append(obj.toString());
    }

    public StrBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen <= 0) {
            return this;
        }
        int len = length();
        ensureCapacity(len + strLen);
        str.getChars(0, strLen, this.buffer, len);
        this.size += strLen;
        return this;
    }

    public StrBuilder append(String str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        } else if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        } else if (length <= 0) {
            return this;
        } else {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
            return this;
        }
    }

    public StrBuilder append(StringBuffer str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen <= 0) {
            return this;
        }
        int len = length();
        ensureCapacity(len + strLen);
        str.getChars(0, strLen, this.buffer, len);
        this.size += strLen;
        return this;
    }

    public StrBuilder append(StringBuffer str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        } else if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        } else if (length <= 0) {
            return this;
        } else {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
            return this;
        }
    }

    public StrBuilder append(StrBuilder str) {
        if (str == null) {
            return appendNull();
        }
        int strLen = str.length();
        if (strLen <= 0) {
            return this;
        }
        int len = length();
        ensureCapacity(len + strLen);
        System.arraycopy(str.buffer, 0, this.buffer, len, strLen);
        this.size += strLen;
        return this;
    }

    public StrBuilder append(StrBuilder str, int startIndex, int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        } else if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        } else if (length <= 0) {
            return this;
        } else {
            int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, this.buffer, len);
            this.size += length;
            return this;
        }
    }

    public StrBuilder append(char[] chars) {
        if (chars == null) {
            return appendNull();
        }
        int strLen = chars.length;
        if (strLen <= 0) {
            return this;
        }
        int len = length();
        ensureCapacity(len + strLen);
        System.arraycopy(chars, 0, this.buffer, len, strLen);
        this.size += strLen;
        return this;
    }

    public StrBuilder append(char[] chars, int startIndex, int length) {
        if (chars == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid startIndex: ").append(length).toString());
        } else if (length < 0 || startIndex + length > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid length: ").append(length).toString());
        } else if (length <= 0) {
            return this;
        } else {
            int len = length();
            ensureCapacity(len + length);
            System.arraycopy(chars, startIndex, this.buffer, len, length);
            this.size += length;
            return this;
        }
    }

    public StrBuilder append(boolean value) {
        char[] cArr;
        int i;
        if (value) {
            ensureCapacity(this.size + 4);
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 't';
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 'r';
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 'u';
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 'e';
        } else {
            ensureCapacity(this.size + 5);
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 'f';
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 'a';
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 'l';
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 's';
            cArr = this.buffer;
            i = this.size;
            this.size = i + 1;
            cArr[i] = 'e';
        }
        return this;
    }

    public StrBuilder append(char ch) {
        ensureCapacity(length() + 1);
        char[] cArr = this.buffer;
        int i = this.size;
        this.size = i + 1;
        cArr[i] = ch;
        return this;
    }

    public StrBuilder append(int value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(long value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(float value) {
        return append(String.valueOf(value));
    }

    public StrBuilder append(double value) {
        return append(String.valueOf(value));
    }

    public StrBuilder appendWithSeparators(Object[] array, String separator) {
        if (array != null && array.length > 0) {
            if (separator == null) {
                separator = StringUtils.EMPTY;
            }
            append(array[0]);
            for (int i = 1; i < array.length; i++) {
                append(separator);
                append(array[i]);
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Collection coll, String separator) {
        if (coll != null && coll.size() > 0) {
            if (separator == null) {
                separator = StringUtils.EMPTY;
            }
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(separator);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Iterator it, String separator) {
        if (it != null) {
            if (separator == null) {
                separator = StringUtils.EMPTY;
            }
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(separator);
                }
            }
        }
        return this;
    }

    public StrBuilder appendPadding(int length, char padChar) {
        if (length >= 0) {
            ensureCapacity(this.size + length);
            for (int i = 0; i < length; i++) {
                char[] cArr = this.buffer;
                int i2 = this.size;
                this.size = i2 + 1;
                cArr[i2] = padChar;
            }
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(Object obj, int width, char padChar) {
        if (width > 0) {
            ensureCapacity(this.size + width);
            String str = obj == null ? getNullText() : obj.toString();
            int strLen = str.length();
            if (strLen >= width) {
                str.getChars(strLen - width, strLen, this.buffer, this.size);
            } else {
                int padLen = width - strLen;
                for (int i = 0; i < padLen; i++) {
                    this.buffer[this.size + i] = padChar;
                }
                str.getChars(0, strLen, this.buffer, this.size + padLen);
            }
            this.size += width;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(int value, int width, char padChar) {
        return appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
    }

    public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
        if (width > 0) {
            ensureCapacity(this.size + width);
            String str = obj == null ? getNullText() : obj.toString();
            int strLen = str.length();
            if (strLen >= width) {
                str.getChars(0, strLen, this.buffer, this.size);
            } else {
                int padLen = width - strLen;
                str.getChars(0, strLen, this.buffer, this.size);
                for (int i = 0; i < padLen; i++) {
                    this.buffer[(this.size + strLen) + i] = padChar;
                }
            }
            this.size += width;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadRight(int value, int width, char padChar) {
        return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
    }

    public StrBuilder insert(int index, Object obj) {
        if (obj == null) {
            return insert(index, this.nullText);
        }
        return insert(index, obj.toString());
    }

    public StrBuilder insert(int index, String str) {
        validateIndex(index);
        if (str == null) {
            str = this.nullText;
        }
        int strLen = str == null ? 0 : str.length();
        if (strLen > 0) {
            int newSize = this.size + strLen;
            ensureCapacity(newSize);
            System.arraycopy(this.buffer, index, this.buffer, index + strLen, this.size - index);
            this.size = newSize;
            str.getChars(0, strLen, this.buffer, index);
        }
        return this;
    }

    public StrBuilder insert(int index, char[] chars) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, this.nullText);
        }
        int len = chars.length;
        if (len <= 0) {
            return this;
        }
        ensureCapacity(this.size + len);
        System.arraycopy(this.buffer, index, this.buffer, index + len, this.size - index);
        System.arraycopy(chars, 0, this.buffer, index, len);
        this.size += len;
        return this;
    }

    public StrBuilder insert(int index, char[] chars, int offset, int length) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, this.nullText);
        }
        if (offset < 0 || offset > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid offset: ").append(offset).toString());
        } else if (length < 0 || offset + length > chars.length) {
            throw new StringIndexOutOfBoundsException(new StringBuffer().append("Invalid length: ").append(length).toString());
        } else if (length <= 0) {
            return this;
        } else {
            ensureCapacity(this.size + length);
            System.arraycopy(this.buffer, index, this.buffer, index + length, this.size - index);
            System.arraycopy(chars, offset, this.buffer, index, length);
            this.size += length;
            return this;
        }
    }

    public StrBuilder insert(int index, boolean value) {
        validateIndex(index);
        int i;
        if (value) {
            ensureCapacity(this.size + 4);
            System.arraycopy(this.buffer, index, this.buffer, index + 4, this.size - index);
            i = index + 1;
            this.buffer[index] = 't';
            index = i + 1;
            this.buffer[i] = 'r';
            i = index + 1;
            this.buffer[index] = 'u';
            this.buffer[i] = 'e';
            this.size += 4;
            index = i;
        } else {
            ensureCapacity(this.size + 5);
            System.arraycopy(this.buffer, index, this.buffer, index + 5, this.size - index);
            i = index + 1;
            this.buffer[index] = 'f';
            index = i + 1;
            this.buffer[i] = 'a';
            i = index + 1;
            this.buffer[index] = 'l';
            index = i + 1;
            this.buffer[i] = 's';
            this.buffer[index] = 'e';
            this.size += 5;
        }
        return this;
    }

    public StrBuilder insert(int index, char value) {
        validateIndex(index);
        ensureCapacity(this.size + 1);
        System.arraycopy(this.buffer, index, this.buffer, index + 1, this.size - index);
        this.buffer[index] = value;
        this.size++;
        return this;
    }

    public StrBuilder insert(int index, int value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, long value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, float value) {
        return insert(index, String.valueOf(value));
    }

    public StrBuilder insert(int index, double value) {
        return insert(index, String.valueOf(value));
    }

    private void deleteImpl(int startIndex, int endIndex, int len) {
        System.arraycopy(this.buffer, endIndex, this.buffer, startIndex, this.size - endIndex);
        this.size -= len;
    }

    public StrBuilder delete(int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex);
        int len = endIndex - startIndex;
        if (len > 0) {
            deleteImpl(startIndex, endIndex, len);
        }
        return this;
    }

    public StrBuilder deleteAll(char ch) {
        int i = 0;
        while (i < this.size) {
            if (this.buffer[i] == ch) {
                int start = i;
                do {
                    i++;
                    if (i >= this.size) {
                        break;
                    }
                } while (this.buffer[i] == ch);
                int len = i - start;
                deleteImpl(start, i, len);
                i -= len;
            }
            i++;
        }
        return this;
    }

    public StrBuilder deleteFirst(char ch) {
        for (int i = 0; i < this.size; i++) {
            if (this.buffer[i] == ch) {
                deleteImpl(i, i + 1, 1);
                break;
            }
        }
        return this;
    }

    public StrBuilder deleteAll(String str) {
        int len = str == null ? 0 : str.length();
        if (len > 0) {
            int index = indexOf(str, 0);
            while (index >= 0) {
                deleteImpl(index, index + len, len);
                index = indexOf(str, index);
            }
        }
        return this;
    }

    public StrBuilder deleteFirst(String str) {
        int len = str == null ? 0 : str.length();
        if (len > 0) {
            int index = indexOf(str, 0);
            if (index >= 0) {
                deleteImpl(index, index + len, len);
            }
        }
        return this;
    }

    public StrBuilder deleteAll(StrMatcher matcher) {
        return replace(matcher, null, 0, this.size, -1);
    }

    public StrBuilder deleteFirst(StrMatcher matcher) {
        return replace(matcher, null, 0, this.size, 1);
    }

    private void replaceImpl(int startIndex, int endIndex, int removeLen, String insertStr, int insertLen) {
        int newSize = (this.size - removeLen) + insertLen;
        if (insertLen != removeLen) {
            ensureCapacity(newSize);
            System.arraycopy(this.buffer, endIndex, this.buffer, startIndex + insertLen, this.size - endIndex);
            this.size = newSize;
        }
        if (insertLen > 0) {
            insertStr.getChars(0, insertLen, this.buffer, startIndex);
        }
    }

    public StrBuilder replace(int startIndex, int endIndex, String replaceStr) {
        endIndex = validateRange(startIndex, endIndex);
        replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, replaceStr == null ? 0 : replaceStr.length());
        return this;
    }

    public StrBuilder replaceAll(char search, char replace) {
        if (search != replace) {
            for (int i = 0; i < this.size; i++) {
                if (this.buffer[i] == search) {
                    this.buffer[i] = replace;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(char search, char replace) {
        if (search != replace) {
            for (int i = 0; i < this.size; i++) {
                if (this.buffer[i] == search) {
                    this.buffer[i] = replace;
                    break;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceAll(String searchStr, String replaceStr) {
        int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0) {
            int replaceLen = replaceStr == null ? 0 : replaceStr.length();
            int index = indexOf(searchStr, 0);
            while (index >= 0) {
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
                index = indexOf(searchStr, index + replaceLen);
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(String searchStr, String replaceStr) {
        int replaceLen = 0;
        int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0) {
            int index = indexOf(searchStr, 0);
            if (index >= 0) {
                if (replaceStr != null) {
                    replaceLen = replaceStr.length();
                }
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
            }
        }
        return this;
    }

    public StrBuilder replaceAll(StrMatcher matcher, String replaceStr) {
        return replace(matcher, replaceStr, 0, this.size, -1);
    }

    public StrBuilder replaceFirst(StrMatcher matcher, String replaceStr) {
        return replace(matcher, replaceStr, 0, this.size, 1);
    }

    public StrBuilder replace(StrMatcher matcher, String replaceStr, int startIndex, int endIndex, int replaceCount) {
        return replaceImpl(matcher, replaceStr, startIndex, validateRange(startIndex, endIndex), replaceCount);
    }

    private StrBuilder replaceImpl(StrMatcher matcher, String replaceStr, int from, int to, int replaceCount) {
        if (!(matcher == null || this.size == 0)) {
            int replaceLen = replaceStr == null ? 0 : replaceStr.length();
            char[] buf = this.buffer;
            int i = from;
            while (i < to && replaceCount != 0) {
                int removeLen = matcher.isMatch(buf, i, from, to);
                if (removeLen > 0) {
                    replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
                    to = (to - removeLen) + replaceLen;
                    i = (i + replaceLen) - 1;
                    if (replaceCount > 0) {
                        replaceCount--;
                    }
                }
                i++;
            }
        }
        return this;
    }

    public StrBuilder reverse() {
        if (this.size != 0) {
            int half = this.size / 2;
            char[] buf = this.buffer;
            int leftIdx = 0;
            int rightIdx = this.size - 1;
            while (leftIdx < half) {
                char swap = buf[leftIdx];
                buf[leftIdx] = buf[rightIdx];
                buf[rightIdx] = swap;
                leftIdx++;
                rightIdx--;
            }
        }
        return this;
    }

    public StrBuilder trim() {
        if (this.size != 0) {
            int len = this.size;
            char[] buf = this.buffer;
            int pos = 0;
            while (pos < len && buf[pos] <= ' ') {
                pos++;
            }
            while (pos < len && buf[len - 1] <= ' ') {
                len--;
            }
            if (len < this.size) {
                delete(len, this.size);
            }
            if (pos > 0) {
                delete(0, pos);
            }
        }
        return this;
    }

    public boolean startsWith(String str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (this.buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(String str) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > this.size) {
            return false;
        }
        int pos = this.size - len;
        int i = 0;
        while (i < len) {
            if (this.buffer[pos] != str.charAt(i)) {
                return false;
            }
            i++;
            pos++;
        }
        return true;
    }

    public String substring(int start) {
        return substring(start, this.size);
    }

    public String substring(int startIndex, int endIndex) {
        return new String(this.buffer, startIndex, validateRange(startIndex, endIndex) - startIndex);
    }

    public String leftString(int length) {
        if (length <= 0) {
            return StringUtils.EMPTY;
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, 0, length);
    }

    public String rightString(int length) {
        if (length <= 0) {
            return StringUtils.EMPTY;
        }
        if (length >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, this.size - length, length);
    }

    public String midString(int index, int length) {
        if (index < 0) {
            index = 0;
        }
        if (length <= 0 || index >= this.size) {
            return StringUtils.EMPTY;
        }
        if (this.size <= index + length) {
            return new String(this.buffer, index, this.size - index);
        }
        return new String(this.buffer, index, length);
    }

    public boolean contains(char ch) {
        char[] thisBuf = this.buffer;
        for (char c : thisBuf) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String str) {
        return indexOf(str, 0) >= 0;
    }

    public boolean contains(StrMatcher matcher) {
        return indexOf(matcher, 0) >= 0;
    }

    public int indexOf(char ch) {
        return indexOf(ch, 0);
    }

    public int indexOf(char ch, int startIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (startIndex >= this.size) {
            return -1;
        }
        char[] thisBuf = this.buffer;
        for (int i = startIndex; i < thisBuf.length; i++) {
            if (thisBuf[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    public int indexOf(String str, int startIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (str == null || startIndex >= this.size) {
            return -1;
        }
        int strLen = str.length();
        if (strLen == 1) {
            return indexOf(str.charAt(0), startIndex);
        }
        if (strLen == 0) {
            return startIndex;
        }
        if (strLen > this.size) {
            return -1;
        }
        char[] thisBuf = this.buffer;
        int i = startIndex;
        while (i < thisBuf.length - strLen) {
            int j = 0;
            while (j < strLen) {
                if (str.charAt(j) != thisBuf[i + j]) {
                    i++;
                } else {
                    j++;
                }
            }
            return i;
        }
        return -1;
    }

    public int indexOf(StrMatcher matcher) {
        return indexOf(matcher, 0);
    }

    public int indexOf(StrMatcher matcher, int startIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (matcher == null || startIndex >= this.size) {
            return -1;
        }
        int len = this.size;
        char[] buf = this.buffer;
        for (int i = startIndex; i < len; i++) {
            if (matcher.isMatch(buf, i, startIndex, len) > 0) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(char ch) {
        return lastIndexOf(ch, this.size - 1);
    }

    public int lastIndexOf(char ch, int startIndex) {
        if (startIndex >= this.size) {
            startIndex = this.size - 1;
        }
        if (startIndex < 0) {
            return -1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (this.buffer[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.size - 1);
    }

    public int lastIndexOf(String str, int startIndex) {
        if (startIndex >= this.size) {
            startIndex = this.size - 1;
        }
        if (str == null || startIndex < 0) {
            return -1;
        }
        int strLen = str.length();
        if (strLen <= 0 || strLen > this.size) {
            if (strLen == 0) {
                return startIndex;
            }
        } else if (strLen == 1) {
            return lastIndexOf(str.charAt(0), startIndex);
        } else {
            int i = (startIndex - strLen) + 1;
            while (i >= 0) {
                int j = 0;
                while (j < strLen) {
                    if (str.charAt(j) != this.buffer[i + j]) {
                        i--;
                    } else {
                        j++;
                    }
                }
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(StrMatcher matcher) {
        return lastIndexOf(matcher, this.size);
    }

    public int lastIndexOf(StrMatcher matcher, int startIndex) {
        if (startIndex >= this.size) {
            startIndex = this.size - 1;
        }
        if (matcher == null || startIndex < 0) {
            return -1;
        }
        char[] buf = this.buffer;
        int endIndex = startIndex + 1;
        for (int i = startIndex; i >= 0; i--) {
            if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
                return i;
            }
        }
        return -1;
    }

    public StrTokenizer asTokenizer() {
        return new StrBuilderTokenizer(this);
    }

    public Reader asReader() {
        return new StrBuilderReader(this);
    }

    public Writer asWriter() {
        return new StrBuilderWriter(this);
    }

    public boolean equalsIgnoreCase(StrBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        char[] thisBuf = this.buffer;
        char[] otherBuf = other.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            char c1 = thisBuf[i];
            char c2 = otherBuf[i];
            if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(StrBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        char[] thisBuf = this.buffer;
        char[] otherBuf = other.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            if (thisBuf[i] != otherBuf[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof StrBuilder) {
            return equals((StrBuilder) obj);
        }
        return false;
    }

    public int hashCode() {
        char[] buf = this.buffer;
        int hash = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            hash = (hash * 31) + buf[i];
        }
        return hash;
    }

    public String toString() {
        return new String(this.buffer, 0, this.size);
    }

    public StringBuffer toStringBuffer() {
        return new StringBuffer(this.size).append(this.buffer, 0, this.size);
    }

    protected int validateRange(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > this.size) {
            endIndex = this.size;
        }
        if (startIndex <= endIndex) {
            return endIndex;
        }
        throw new StringIndexOutOfBoundsException("end < start");
    }

    protected void validateIndex(int index) {
        if (index < 0 || index > this.size) {
            throw new StringIndexOutOfBoundsException(index);
        }
    }
}
