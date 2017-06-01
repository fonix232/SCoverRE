package org.apache.commons.lang.builder;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;

public class ToStringBuilder {
    private static ToStringStyle defaultStyle = ToStringStyle.DEFAULT_STYLE;
    private final StringBuffer buffer;
    private final Object object;
    private final ToStringStyle style;

    public static ToStringStyle getDefaultStyle() {
        return defaultStyle;
    }

    public static String reflectionToString(Object object) {
        return ReflectionToStringBuilder.toString(object);
    }

    public static String reflectionToString(Object object, ToStringStyle style) {
        return ReflectionToStringBuilder.toString(object, style);
    }

    public static String reflectionToString(Object object, ToStringStyle style, boolean outputTransients) {
        return ReflectionToStringBuilder.toString(object, style, outputTransients, false, null);
    }

    public static String reflectionToString(Object object, ToStringStyle style, boolean outputTransients, Class reflectUpToClass) {
        return ReflectionToStringBuilder.toString(object, style, outputTransients, false, reflectUpToClass);
    }

    public static void setDefaultStyle(ToStringStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("The style must not be null");
        }
        defaultStyle = style;
    }

    public ToStringBuilder(Object object) {
        this(object, getDefaultStyle(), null);
    }

    public ToStringBuilder(Object object, ToStringStyle style) {
        this(object, style, null);
    }

    public ToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer) {
        if (style == null) {
            style = getDefaultStyle();
        }
        if (buffer == null) {
            buffer = new StringBuffer(512);
        }
        this.buffer = buffer;
        this.style = style;
        this.object = object;
        style.appendStart(buffer, object);
    }

    public ToStringBuilder append(boolean value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(boolean[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(byte value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(byte[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(char value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(char[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(double value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(double[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(float value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(float[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(int value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(int[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(long value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(long[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(Object object) {
        this.style.append(this.buffer, null, object, null);
        return this;
    }

    public ToStringBuilder append(Object[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(short value) {
        this.style.append(this.buffer, null, value);
        return this;
    }

    public ToStringBuilder append(short[] array) {
        this.style.append(this.buffer, null, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, boolean[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, byte value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, byte[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, byte[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, char value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, char[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, char[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, double value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, double[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, double[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, float value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, float[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, float[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, int value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, int[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, int[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, long value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, long[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, long[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, Object object) {
        this.style.append(this.buffer, fieldName, object, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, Object object, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, object, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, Object[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, Object[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder append(String fieldName, short value) {
        this.style.append(this.buffer, fieldName, value);
        return this;
    }

    public ToStringBuilder append(String fieldName, short[] array) {
        this.style.append(this.buffer, fieldName, array, null);
        return this;
    }

    public ToStringBuilder append(String fieldName, short[] array, boolean fullDetail) {
        this.style.append(this.buffer, fieldName, array, BooleanUtils.toBooleanObject(fullDetail));
        return this;
    }

    public ToStringBuilder appendAsObjectToString(Object object) {
        ObjectUtils.appendIdentityToString(getStringBuffer(), object);
        return this;
    }

    public ToStringBuilder appendSuper(String superToString) {
        if (superToString != null) {
            this.style.appendSuper(this.buffer, superToString);
        }
        return this;
    }

    public ToStringBuilder appendToString(String toString) {
        if (toString != null) {
            this.style.appendToString(this.buffer, toString);
        }
        return this;
    }

    public Object getObject() {
        return this.object;
    }

    public StringBuffer getStringBuffer() {
        return this.buffer;
    }

    public ToStringStyle getStyle() {
        return this.style;
    }

    public String toString() {
        if (getObject() == null) {
            getStringBuffer().append(getStyle().getNullText());
        } else {
            this.style.appendEnd(getStringBuffer(), getObject());
        }
        return getStringBuffer().toString();
    }
}
