package com.samsung.android.graphics.spr.document.animator;

import android.animation.PropertyValuesHolder;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase.UpdateParameter;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAnimatorScale extends SprAnimatorBase {
    private float fromX;
    private float fromY;
    private float pivotX;
    private float pivotY;
    private float toX;
    private float toY;

    public SprAnimatorScale() {
        super((byte) 2);
    }

    public SprAnimatorScale(SprInputStream sprInputStream) throws IOException {
        super((byte) 2);
        fromSPR(sprInputStream);
        init();
    }

    private void init() {
        r0 = new PropertyValuesHolder[2];
        r0[0] = PropertyValuesHolder.ofFloat("x", new float[]{this.fromX, this.toX});
        r0[1] = PropertyValuesHolder.ofFloat("y", new float[]{this.fromY, this.toY});
        setValues(r0);
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        super.fromSPR(sprInputStream);
        this.pivotX = sprInputStream.readFloat();
        this.pivotY = sprInputStream.readFloat();
        this.fromX = sprInputStream.readFloat();
        this.fromY = sprInputStream.readFloat();
        this.toX = sprInputStream.readFloat();
        this.toY = sprInputStream.readFloat();
    }

    public int getSPRSize() {
        return super.getSPRSize() + 24;
    }

    public void set(float f, float f2, float f3, float f4, float f5, float f6) {
        this.fromX = f;
        this.fromY = f2;
        this.toX = f3;
        this.toY = f4;
        this.pivotX = f5;
        this.pivotY = f6;
        init();
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        super.toSPR(dataOutputStream);
        dataOutputStream.writeFloat(this.pivotX);
        dataOutputStream.writeFloat(this.pivotY);
        dataOutputStream.writeFloat(this.fromX);
        dataOutputStream.writeFloat(this.fromY);
        dataOutputStream.writeFloat(this.toX);
        dataOutputStream.writeFloat(this.toY);
    }

    public boolean updateValues(UpdateParameter updateParameter) {
        updateParameter.isUpdatedScale = true;
        updateParameter.scalePivotX = this.pivotX;
        updateParameter.scalePivotY = this.pivotY;
        if (updateParameter.isLastFrame) {
            updateParameter.scaleX = this.toX;
            updateParameter.scaleY = this.toY;
        } else {
            updateParameter.scaleX = ((Float) getAnimatedValue("x")).floatValue();
            updateParameter.scaleY = ((Float) getAnimatedValue("y")).floatValue();
        }
        return false;
    }
}
