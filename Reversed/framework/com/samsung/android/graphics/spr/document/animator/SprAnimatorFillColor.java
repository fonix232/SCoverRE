package com.samsung.android.graphics.spr.document.animator;

import android.animation.ArgbEvaluator;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase.UpdateParameter;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAnimatorFillColor extends SprAnimatorBase {
    private int from;
    private int to;

    public SprAnimatorFillColor() {
        super((byte) 5);
    }

    public SprAnimatorFillColor(SprInputStream sprInputStream) throws IOException {
        super((byte) 5);
        fromSPR(sprInputStream);
        init();
    }

    private void init() {
        setIntValues(new int[]{this.from, this.to});
        setEvaluator(new ArgbEvaluator());
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        super.fromSPR(sprInputStream);
        this.from = sprInputStream.readInt();
        this.to = sprInputStream.readInt();
    }

    public int getSPRSize() {
        return super.getSPRSize() + 8;
    }

    public void set(int i, int i2) {
        this.from = i;
        this.to = i2;
        init();
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        super.toSPR(dataOutputStream);
        dataOutputStream.writeInt(this.from);
        dataOutputStream.writeInt(this.to);
    }

    public boolean updateValues(UpdateParameter updateParameter) {
        updateParameter.isUpdatedFillColor = true;
        if (updateParameter.isLastFrame) {
            updateParameter.fillColor = this.to;
        } else {
            updateParameter.fillColor = ((Integer) getAnimatedValue()).intValue();
        }
        return true;
    }
}
