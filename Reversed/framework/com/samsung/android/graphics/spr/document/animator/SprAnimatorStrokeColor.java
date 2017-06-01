package com.samsung.android.graphics.spr.document.animator;

import android.animation.ArgbEvaluator;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase.UpdateParameter;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAnimatorStrokeColor extends SprAnimatorBase {
    private int from;
    private int to;

    public SprAnimatorStrokeColor() {
        super((byte) 4);
    }

    public SprAnimatorStrokeColor(SprInputStream sprInputStream) throws IOException {
        super((byte) 4);
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
        updateParameter.isUpdatedStrokeColor = true;
        if (updateParameter.isLastFrame) {
            updateParameter.strokeColor = this.to;
        } else {
            updateParameter.strokeColor = ((Integer) getAnimatedValue()).intValue();
        }
        return true;
    }
}
