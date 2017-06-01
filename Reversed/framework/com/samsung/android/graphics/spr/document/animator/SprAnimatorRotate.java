package com.samsung.android.graphics.spr.document.animator;

import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase.UpdateParameter;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAnimatorRotate extends SprAnimatorBase {
    private float from;
    private float pivotX;
    private float pivotY;
    private float to;

    public SprAnimatorRotate() {
        super((byte) 3);
    }

    public SprAnimatorRotate(SprInputStream sprInputStream) throws IOException {
        super((byte) 3);
        fromSPR(sprInputStream);
        init();
    }

    private void init() {
        setFloatValues(new float[]{this.from, this.to});
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        super.fromSPR(sprInputStream);
        this.pivotX = sprInputStream.readFloat();
        this.pivotY = sprInputStream.readFloat();
        this.from = sprInputStream.readFloat();
        this.to = sprInputStream.readFloat();
    }

    public int getSPRSize() {
        return super.getSPRSize() + 16;
    }

    public void set(float f, float f2, float f3, float f4) {
        this.from = f;
        this.to = f2;
        this.pivotX = f3;
        this.pivotY = f4;
        init();
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        super.toSPR(dataOutputStream);
        dataOutputStream.writeFloat(this.pivotX);
        dataOutputStream.writeFloat(this.pivotY);
        dataOutputStream.writeFloat(this.from);
        dataOutputStream.writeFloat(this.to);
    }

    public boolean updateValues(UpdateParameter updateParameter) {
        updateParameter.isUpdatedRotate = true;
        updateParameter.rotatePivotX = this.pivotX;
        updateParameter.rotatePivotY = this.pivotY;
        if (updateParameter.isLastFrame) {
            updateParameter.rotateDegree = this.to;
        } else {
            updateParameter.rotateDegree = ((Float) getAnimatedValue()).floatValue();
        }
        return false;
    }
}
