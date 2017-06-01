package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeStrokeMiterlimit extends SprAttributeBase {
    public float miterLimit = 0.0f;

    public SprAttributeStrokeMiterlimit() {
        super((byte) 41);
    }

    public SprAttributeStrokeMiterlimit(SprInputStream sprInputStream) throws IOException {
        super((byte) 41);
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.miterLimit = sprInputStream.readFloat();
    }

    public int getSPRSize() {
        return 4;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.miterLimit);
    }
}
