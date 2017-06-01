package com.samsung.android.graphics.spr.document.attribute.impl;

import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SprGradientBase implements Cloneable {
    public static final byte SPREAD_TYPE_NONE = (byte) 0;
    public static final byte SPREAD_TYPE_PAD = (byte) 1;
    public static final byte SPREAD_TYPE_REFLECT = (byte) 2;
    public static final byte SPREAD_TYPE_REPEAT = (byte) 3;
    static final TileMode[] sTileModeArray = new TileMode[]{TileMode.CLAMP, TileMode.CLAMP, TileMode.MIRROR, TileMode.REPEAT};
    public int[] colors;
    protected final SprGradientBase mIntrinsic = this;
    public Matrix matrix = null;
    public float[] positions;
    public Shader shader = null;
    public byte spreadMode = (byte) 0;

    public SprGradientBase clone() throws CloneNotSupportedException {
        SprGradientBase sprGradientBase = (SprGradientBase) super.clone();
        sprGradientBase.colors = new int[this.colors.length];
        sprGradientBase.positions = new float[this.colors.length];
        for (int i = 0; i < this.colors.length; i++) {
            sprGradientBase.colors[i] = this.colors[i];
            sprGradientBase.positions[i] = this.positions[i];
        }
        sprGradientBase.updateGradient();
        return sprGradientBase;
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.spreadMode = sprInputStream.readByte();
        this.colors = new int[sprInputStream.readInt()];
        this.positions = new float[this.colors.length];
        for (int i = 0; i < this.colors.length; i++) {
            float readFloat = sprInputStream.readFloat();
            this.colors[i] = (((int) (255.0f * sprInputStream.readFloat())) << 24) | sprInputStream.readInt();
            this.positions[i] = readFloat;
        }
        byte readByte = sprInputStream.readByte();
        this.matrix = SprMatrix.fromSPR(sprInputStream);
        if (readByte == (byte) 0) {
            this.matrix = null;
        }
        updateGradient();
    }

    public int getSPRSize() {
        return (this.colors.length * 12) + 30;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.spreadMode);
        dataOutputStream.writeInt(this.colors.length);
        for (int i = 0; i < this.colors.length; i++) {
            dataOutputStream.writeFloat(this.positions[i]);
            dataOutputStream.writeInt(this.colors[i] & 16777215);
            dataOutputStream.writeFloat(((float) (this.colors[i] >> 24)) / 255.0f);
        }
        dataOutputStream.writeByte(this.matrix == null ? 0 : 1);
        SprMatrix.toSPR(dataOutputStream, this.matrix);
    }

    public abstract void updateGradient();
}
