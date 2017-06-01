package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.attribute.impl.SprGradientBase;
import java.io.IOException;

public class SprAttributeFill extends SprAttributeColor {
    public SprAttributeFill() {
        super((byte) 32);
    }

    public SprAttributeFill(byte b, int i) {
        super((byte) 32, b, i);
    }

    public SprAttributeFill(byte b, SprGradientBase sprGradientBase) {
        super((byte) 32, b, sprGradientBase);
    }

    public SprAttributeFill(SprInputStream sprInputStream) throws IOException {
        super((byte) 32, sprInputStream);
    }
}
