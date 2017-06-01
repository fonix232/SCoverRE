package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.attribute.impl.SprGradientBase;
import java.io.IOException;

public class SprAttributeStroke extends SprAttributeColor {
    public SprAttributeStroke() {
        super((byte) 35);
    }

    public SprAttributeStroke(byte b, int i) {
        super((byte) 35, b, i);
    }

    public SprAttributeStroke(byte b, SprGradientBase sprGradientBase) {
        super((byte) 35, b, sprGradientBase);
    }

    public SprAttributeStroke(SprInputStream sprInputStream) throws IOException {
        super((byte) 35, sprInputStream);
    }
}
