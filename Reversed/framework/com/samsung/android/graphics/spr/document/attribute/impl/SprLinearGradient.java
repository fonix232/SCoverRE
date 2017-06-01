package com.samsung.android.graphics.spr.document.attribute.impl;

import android.graphics.LinearGradient;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprLinearGradient extends SprGradientBase {
    public float x1;
    public float x2;
    public float y1;
    public float y2;

    public SprLinearGradient(SprInputStream sprInputStream) throws IOException {
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.x1 = sprInputStream.readFloat();
        this.y1 = sprInputStream.readFloat();
        this.x2 = sprInputStream.readFloat();
        this.y2 = sprInputStream.readFloat();
        super.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return super.getSPRSize() + 16;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.x1);
        dataOutputStream.writeFloat(this.y1);
        dataOutputStream.writeFloat(this.x2);
        dataOutputStream.writeFloat(this.y2);
        super.toSPR(dataOutputStream);
    }

    public void updateGradient() {
        this.shader = new LinearGradient(this.x1, this.y1, this.x2, this.y2, this.colors, this.positions, sTileModeArray[this.spreadMode - 1]);
        if (this.matrix != null) {
            this.shader.setLocalMatrix(this.matrix);
        }
    }
}
