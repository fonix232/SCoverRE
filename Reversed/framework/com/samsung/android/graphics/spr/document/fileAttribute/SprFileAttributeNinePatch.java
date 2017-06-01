package com.samsung.android.graphics.spr.document.fileAttribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprFileAttributeNinePatch extends SprFileAttributeBase {
    public float[] xEnd = null;
    public int xSize = 0;
    public float[] xStart = null;
    public float[] yEnd = null;
    public int ySize = 0;
    public float[] yStart = null;

    public SprFileAttributeNinePatch() {
        super((byte) 1);
    }

    public SprFileAttributeNinePatch(SprInputStream sprInputStream) throws IOException {
        super((byte) 1);
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        int i;
        this.xSize = sprInputStream.readInt();
        this.xStart = new float[this.xSize];
        this.xEnd = new float[this.xSize];
        for (i = 0; i < this.xSize; i++) {
            this.xStart[i] = sprInputStream.readFloat();
            this.xEnd[i] = sprInputStream.readFloat();
        }
        this.ySize = sprInputStream.readInt();
        this.yStart = new float[this.ySize];
        this.yEnd = new float[this.ySize];
        for (i = 0; i < this.ySize; i++) {
            this.yStart[i] = sprInputStream.readFloat();
            this.yEnd[i] = sprInputStream.readFloat();
        }
    }

    public int getSPRSize() {
        return (((this.xSize * 8) + 4) + 4) + (this.ySize * 8);
    }

    public boolean isValid() {
        return this.xSize * this.ySize >= 2;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        int i;
        dataOutputStream.writeInt(this.xSize);
        for (i = 0; i < this.xSize; i++) {
            dataOutputStream.writeFloat(this.xStart[i]);
            dataOutputStream.writeFloat(this.xEnd[i]);
        }
        dataOutputStream.writeInt(this.ySize);
        for (i = 0; i < this.ySize; i++) {
            dataOutputStream.writeFloat(this.yStart[i]);
            dataOutputStream.writeFloat(this.yEnd[i]);
        }
    }
}
