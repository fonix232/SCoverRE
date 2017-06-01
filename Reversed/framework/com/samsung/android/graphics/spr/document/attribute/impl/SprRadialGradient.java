package com.samsung.android.graphics.spr.document.attribute.impl;

import android.graphics.RadialGradient;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprRadialGradient extends SprGradientBase {
    public float cx;
    public float cy;
    public float f9r;

    public SprRadialGradient(SprInputStream sprInputStream) throws IOException {
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.cx = sprInputStream.readFloat();
        this.cy = sprInputStream.readFloat();
        this.f9r = sprInputStream.readFloat();
        super.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return super.getSPRSize() + 12;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.cx);
        dataOutputStream.writeFloat(this.cy);
        dataOutputStream.writeFloat(this.f9r);
        super.toSPR(dataOutputStream);
    }

    public void updateGradient() {
        int length = this.positions.length;
        if (this.positions[length - 1] != 1.0f) {
            length++;
        }
        if (this.positions[0] != 0.0f) {
            length++;
        }
        int[] iArr = this.colors;
        float[] fArr = this.positions;
        if (length != this.positions.length) {
            iArr = new int[length];
            fArr = new float[length];
            int i = 0;
            if (this.positions[0] != 0.0f) {
                iArr[0] = this.colors[0];
                fArr[0] = 0.0f;
                i = 1;
            }
            int i2 = 0;
            while (i2 < this.colors.length) {
                iArr[i] = this.colors[i2];
                fArr[i] = this.positions[i2];
                i2++;
                i++;
            }
            if (this.positions[this.positions.length - 1] != 1.0f) {
                iArr[length - 1] = this.colors[this.positions.length - 1];
                fArr[length - 1] = 1.0f;
            }
        }
        this.shader = new RadialGradient(this.cx, this.cy, this.f9r, iArr, fArr, sTileModeArray[this.spreadMode]);
        if (this.matrix != null) {
            this.shader.setLocalMatrix(this.matrix);
        }
    }
}
