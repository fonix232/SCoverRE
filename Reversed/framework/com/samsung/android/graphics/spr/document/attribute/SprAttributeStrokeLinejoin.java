package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeStrokeLinejoin extends SprAttributeBase {
    public static byte STROKE_LINEJOIN_TYPE_BEVEL = (byte) 3;
    public static byte STROKE_LINEJOIN_TYPE_MITER = (byte) 1;
    public static byte STROKE_LINEJOIN_TYPE_NONE = (byte) 0;
    public static byte STROKE_LINEJOIN_TYPE_ROUND = (byte) 2;
    public byte linejoin = STROKE_LINEJOIN_TYPE_MITER;

    public SprAttributeStrokeLinejoin() {
        super((byte) 38);
    }

    public SprAttributeStrokeLinejoin(byte b) {
        super((byte) 38);
        this.linejoin = b;
    }

    public SprAttributeStrokeLinejoin(SprInputStream sprInputStream) throws IOException {
        super((byte) 38);
        fromSPR(sprInputStream);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.linejoin = sprInputStream.readByte();
    }

    public int getSPRSize() {
        return 1;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.linejoin);
    }
}
