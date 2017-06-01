package com.samsung.android.graphics.spr.document.fileAttribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SprFileAttributeBase implements Cloneable {
    public static final byte TYPE_NINE_PATCH = (byte) 1;
    public static final byte TYPE_NONE = (byte) 0;
    protected final SprFileAttributeBase mIntrinsic = this;
    public final byte mType;

    protected SprFileAttributeBase(byte b) {
        this.mType = b;
    }

    public SprFileAttributeBase clone() throws CloneNotSupportedException {
        return (SprFileAttributeBase) super.clone();
    }

    public abstract void fromSPR(SprInputStream sprInputStream) throws IOException;

    public int getSPRSize() {
        return 0;
    }

    public boolean isValid() {
        return false;
    }

    public abstract void toSPR(DataOutputStream dataOutputStream) throws IOException;
}
