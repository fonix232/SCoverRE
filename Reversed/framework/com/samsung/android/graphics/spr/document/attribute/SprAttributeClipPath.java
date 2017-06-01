package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeClipPath extends SprAttributeBase {
    public int link = 0;

    public SprAttributeClipPath() {
        super((byte) 3);
    }

    public SprAttributeClipPath(int i) {
        super((byte) 3);
        this.link = i;
    }

    public SprAttributeClipPath(SprInputStream sprInputStream) throws IOException {
        super((byte) 3);
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.link = sprInputStream.readInt();
    }

    public int getSPRSize() {
        return 4;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.link);
    }
}
