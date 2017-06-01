package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeClip extends SprAttributeBase {
    public float bottom = 0.0f;
    public float left = 0.0f;
    public float right = 0.0f;
    public float top = 0.0f;

    public SprAttributeClip() {
        super((byte) 1);
    }

    public SprAttributeClip(SprInputStream sprInputStream) throws IOException {
        super((byte) 1);
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.left = sprInputStream.readFloat();
        this.top = sprInputStream.readFloat();
        this.right = sprInputStream.readFloat();
        this.bottom = sprInputStream.readFloat();
    }

    public int getSPRSize() {
        return 16;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.left);
        dataOutputStream.writeFloat(this.top);
        dataOutputStream.writeFloat(this.right);
        dataOutputStream.writeFloat(this.bottom);
    }
}
