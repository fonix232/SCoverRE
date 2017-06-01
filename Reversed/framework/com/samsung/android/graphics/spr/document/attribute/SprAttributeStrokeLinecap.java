package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeStrokeLinecap extends SprAttributeBase {
    public static byte STROKE_LINECAP_TYPE_BUTT = (byte) 1;
    public static byte STROKE_LINECAP_TYPE_NONE = (byte) 0;
    public static byte STROKE_LINECAP_TYPE_ROUND = (byte) 2;
    public static byte STROKE_LINECAP_TYPE_SQUARE = (byte) 3;
    public byte linecap = STROKE_LINECAP_TYPE_BUTT;

    public SprAttributeStrokeLinecap() {
        super((byte) 37);
    }

    public SprAttributeStrokeLinecap(SprInputStream sprInputStream) throws IOException {
        super((byte) 37);
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.linecap = sprInputStream.readByte();
    }

    public int getSPRSize() {
        return 1;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.linecap);
    }
}
