package com.samsung.android.graphics.spr.document.animator;

import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase.UpdateParameter;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAnimatorAlpha extends SprAnimatorBase {
    private float from = 0.0f;
    private float to = 0.0f;

    public SprAnimatorAlpha() {
        super((byte) 6);
    }

    public SprAnimatorAlpha(SprInputStream sprInputStream) throws IOException {
        super((byte) 6);
        fromSPR(sprInputStream);
        init();
    }

    private void init() {
        float f = 1.0f;
        float f2 = 0.0f;
        this.from = this.from < 0.0f ? 0.0f : this.from;
        this.from = this.from > 1.0f ? 1.0f : this.from;
        if (this.to >= 0.0f) {
            f2 = this.to;
        }
        this.to = f2;
        if (this.to <= 1.0f) {
            f = this.to;
        }
        this.to = f;
        setFloatValues(new float[]{this.from, this.to});
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        super.fromSPR(sprInputStream);
        this.from = sprInputStream.readFloat();
        this.to = sprInputStream.readFloat();
    }

    public int getSPRSize() {
        return super.getSPRSize() + 8;
    }

    public void set(float f, float f2) {
        this.from = f;
        this.to = f2;
        init();
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        float f = 1.0f;
        float f2 = 0.0f;
        super.toSPR(dataOutputStream);
        this.from = this.from < 0.0f ? 0.0f : this.from;
        this.from = this.from > 1.0f ? 1.0f : this.from;
        if (this.to >= 0.0f) {
            f2 = this.to;
        }
        this.to = f2;
        if (this.to <= 1.0f) {
            f = this.to;
        }
        this.to = f;
        dataOutputStream.writeFloat(this.from);
        dataOutputStream.writeFloat(this.to);
    }

    public boolean updateValues(UpdateParameter updateParameter) {
        if (updateParameter.isLastFrame) {
            updateParameter.alpha = this.to;
        } else {
            updateParameter.alpha = ((Float) getAnimatedValue()).floatValue();
        }
        return false;
    }
}
