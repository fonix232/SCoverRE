package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SprAttributeBase implements Cloneable {
    public static final byte TYPE_ANIMATOR_SET = (byte) 97;
    public static final byte TYPE_CLIP = (byte) 1;
    public static final byte TYPE_CLIP_PATH = (byte) 3;
    public static final byte TYPE_DURATION = (byte) 96;
    public static final byte TYPE_FILL = (byte) 32;
    public static final byte TYPE_MATRIX = (byte) 64;
    public static final byte TYPE_NONE = (byte) 0;
    public static final byte TYPE_SHADOW = (byte) 112;
    public static final byte TYPE_STROKE = (byte) 35;
    public static final byte TYPE_STROKE_LINECAP = (byte) 37;
    public static final byte TYPE_STROKE_LINEJOIN = (byte) 38;
    public static final byte TYPE_STROKE_MITERLIMIT = (byte) 41;
    public static final byte TYPE_STROKE_WIDTH = (byte) 40;
    protected final SprAttributeBase mIntrinsic = this;
    public final byte mType;

    protected SprAttributeBase(byte b) {
        this.mType = b;
    }

    public SprAttributeBase clone() throws CloneNotSupportedException {
        return (SprAttributeBase) super.clone();
    }

    public abstract void fromSPR(SprInputStream sprInputStream) throws IOException;

    public int getSPRSize() {
        return 0;
    }

    public abstract void toSPR(DataOutputStream dataOutputStream) throws IOException;
}
