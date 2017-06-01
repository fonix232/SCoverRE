package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.attribute.impl.SprGradientBase;
import com.samsung.android.graphics.spr.document.attribute.impl.SprLinearGradient;
import com.samsung.android.graphics.spr.document.attribute.impl.SprRadialGradient;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SprAttributeColor extends SprAttributeBase {
    public static final byte TYPE_ARGB = (byte) 1;
    public static final byte TYPE_LINEAR_GRADIENT = (byte) 3;
    public static final byte TYPE_LINK = (byte) 2;
    public static final byte TYPE_NONE = (byte) 0;
    public static final byte TYPE_RADIAL_GRADIENT = (byte) 4;
    public int color;
    public byte colorType;
    public SprGradientBase gradient;

    public SprAttributeColor(byte b) {
        super(b);
        this.colorType = (byte) 1;
        this.gradient = null;
        this.colorType = (byte) 1;
        this.color = 0;
    }

    public SprAttributeColor(byte b, byte b2, int i) {
        super(b);
        this.colorType = (byte) 1;
        this.gradient = null;
        this.colorType = b2;
        switch (b2) {
            case (byte) 0:
                return;
            case (byte) 1:
            case (byte) 2:
                this.color = i;
                return;
            default:
                throw new RuntimeException("unexpected stroke type:" + b2);
        }
    }

    public SprAttributeColor(byte b, byte b2, SprGradientBase sprGradientBase) {
        super(b);
        this.colorType = (byte) 1;
        this.gradient = null;
        this.colorType = b2;
        switch (b2) {
            case (byte) 0:
                return;
            case (byte) 3:
            case (byte) 4:
                this.gradient = sprGradientBase;
                return;
            default:
                throw new RuntimeException("unexpected stroke type:" + b2);
        }
    }

    public SprAttributeColor(byte b, SprInputStream sprInputStream) throws IOException {
        super(b);
        this.colorType = (byte) 1;
        this.gradient = null;
        fromSPR(sprInputStream);
    }

    public SprAttributeColor clone() throws CloneNotSupportedException {
        SprAttributeColor sprAttributeColor = (SprAttributeColor) super.clone();
        if (this.gradient != null) {
            sprAttributeColor.gradient = this.gradient.clone();
        }
        return sprAttributeColor;
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.colorType = sprInputStream.readByte();
        switch (this.colorType) {
            case (byte) 0:
                sprInputStream.readInt();
                return;
            case (byte) 1:
            case (byte) 2:
                this.color = sprInputStream.readInt();
                return;
            case (byte) 3:
                this.gradient = new SprLinearGradient(sprInputStream);
                return;
            case (byte) 4:
                this.gradient = new SprRadialGradient(sprInputStream);
                return;
            default:
                throw new RuntimeException("unknown fill type:" + this.colorType);
        }
    }

    public int getSPRSize() {
        switch (this.colorType) {
            case (byte) 0:
                return 0;
            case (byte) 1:
            case (byte) 2:
                return 5;
            case (byte) 3:
            case (byte) 4:
                return this.gradient.getSPRSize() + 1;
            default:
                throw new RuntimeException("unknown fill type:" + this.colorType);
        }
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.colorType);
        switch (this.colorType) {
            case (byte) 0:
                dataOutputStream.writeInt(0);
                return;
            case (byte) 1:
            case (byte) 2:
                dataOutputStream.writeInt(this.color);
                return;
            case (byte) 3:
            case (byte) 4:
                this.gradient.toSPR(dataOutputStream);
                return;
            default:
                throw new RuntimeException("unknown fill type:" + this.colorType);
        }
    }
}
