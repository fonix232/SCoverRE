package com.samsung.android.graphics.spr.document.attribute;

import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeShadow extends SprAttributeBase {
    public float dx;
    public float dy;
    public float radius;
    public int shadowColor;

    public SprAttributeShadow() {
        super(SprAttributeBase.TYPE_SHADOW);
        this.radius = 0.0f;
        this.dx = 0.0f;
        this.dy = 0.0f;
        this.shadowColor = 0;
        this.dy = 0.0f;
        this.dx = 0.0f;
        this.radius = 0.0f;
        this.shadowColor = 0;
    }

    public SprAttributeShadow(float f, float f2, float f3, int i) {
        super(SprAttributeBase.TYPE_SHADOW);
        this.radius = 0.0f;
        this.dx = 0.0f;
        this.dy = 0.0f;
        this.shadowColor = 0;
        this.radius = f;
        this.dx = f2;
        this.dy = f3;
        this.shadowColor = i;
    }

    public SprAttributeShadow(SprInputStream sprInputStream) throws IOException {
        super(SprAttributeBase.TYPE_SHADOW);
        this.radius = 0.0f;
        this.dx = 0.0f;
        this.dy = 0.0f;
        this.shadowColor = 0;
        fromSPR(sprInputStream);
    }

    public SprAttributeShadow clone() throws CloneNotSupportedException {
        SprAttributeShadow sprAttributeShadow = (SprAttributeShadow) super.clone();
        sprAttributeShadow.radius = this.radius;
        sprAttributeShadow.dx = this.dx;
        sprAttributeShadow.dy = this.dy;
        sprAttributeShadow.shadowColor = this.shadowColor;
        return sprAttributeShadow;
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.radius = sprInputStream.readFloat();
        this.dx = sprInputStream.readFloat();
        this.dy = sprInputStream.readFloat();
        this.shadowColor = sprInputStream.readInt();
    }

    public int getSPRSize() {
        return 16;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.radius);
        dataOutputStream.writeFloat(this.dx);
        dataOutputStream.writeFloat(this.dy);
        dataOutputStream.writeInt(this.shadowColor);
    }
}
