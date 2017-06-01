package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeStrokeWidth extends SprAttributeBase {
    public float strokeWidth = 0.0f;

    public SprAttributeStrokeWidth() {
        super((byte) 40);
    }

    public SprAttributeStrokeWidth(float f) {
        super((byte) 40);
        this.strokeWidth = f;
    }

    public SprAttributeStrokeWidth(SprInputStream sprInputStream) throws IOException {
        super((byte) 40);
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.strokeWidth = sprInputStream.readFloat();
        if (this.strokeWidth > 0.0f && this.strokeWidth < 0.3f) {
            this.strokeWidth = 0.3f;
        }
    }

    public int getSPRSize() {
        return 4;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.strokeWidth);
    }
}
