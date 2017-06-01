package com.samsung.android.graphics.spr.document.attribute;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorAlpha;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorFillColor;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorRotate;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorScale;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorStrokeColor;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorTranslate;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SprAttributeAnimatorSet extends SprAttributeBase {
    public int duration;
    private ArrayList<Animator> mAnimators = new ArrayList();
    public int repeatCount;
    public int startOffset;

    public SprAttributeAnimatorSet(byte b) {
        super(SprAttributeBase.TYPE_ANIMATOR_SET);
    }

    public SprAttributeAnimatorSet(SprInputStream sprInputStream) throws IOException {
        super(SprAttributeBase.TYPE_ANIMATOR_SET);
        fromSPR(sprInputStream);
    }

    public void addAnimatorData(SprAnimatorBase sprAnimatorBase) {
        this.mAnimators.add(sprAnimatorBase.clone());
    }

    public SprAttributeAnimatorSet clone() throws CloneNotSupportedException {
        SprAttributeAnimatorSet sprAttributeAnimatorSet = (SprAttributeAnimatorSet) super.clone();
        sprAttributeAnimatorSet.mAnimators = new ArrayList();
        for (Animator clone : this.mAnimators) {
            sprAttributeAnimatorSet.mAnimators.add(clone.clone());
        }
        return sprAttributeAnimatorSet;
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.startOffset = sprInputStream.readInt();
        this.duration = sprInputStream.readInt();
        this.repeatCount = sprInputStream.readInt();
        int readInt = sprInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            byte readByte = sprInputStream.readByte();
            int readInt2 = sprInputStream.readInt();
            switch (readByte) {
                case (byte) 1:
                    this.mAnimators.add(new SprAnimatorTranslate(sprInputStream));
                    break;
                case (byte) 2:
                    this.mAnimators.add(new SprAnimatorScale(sprInputStream));
                    break;
                case (byte) 3:
                    this.mAnimators.add(new SprAnimatorRotate(sprInputStream));
                    break;
                case (byte) 4:
                    this.mAnimators.add(new SprAnimatorStrokeColor(sprInputStream));
                    break;
                case (byte) 5:
                    this.mAnimators.add(new SprAnimatorFillColor(sprInputStream));
                    break;
                case (byte) 6:
                    this.mAnimators.add(new SprAnimatorAlpha(sprInputStream));
                    break;
                default:
                    sprInputStream.skip((long) readInt2);
                    break;
            }
        }
    }

    public ArrayList<Animator> getAnimators() {
        return this.mAnimators;
    }

    public int getSPRSize() {
        int i = 16;
        for (Animator animator : this.mAnimators) {
            i += ((SprAnimatorBase) animator).getSPRSize() + 5;
        }
        return i;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.startOffset);
        dataOutputStream.writeInt(this.duration);
        dataOutputStream.writeInt(this.repeatCount);
        dataOutputStream.writeInt(this.mAnimators.size());
        for (Animator animator : this.mAnimators) {
            SprAnimatorBase sprAnimatorBase = (SprAnimatorBase) animator;
            dataOutputStream.writeByte(sprAnimatorBase.mType);
            dataOutputStream.writeInt(sprAnimatorBase.getSPRSize());
            sprAnimatorBase.toSPR(dataOutputStream);
        }
    }

    public void updateAnimatorInterpolator(TimeInterpolator timeInterpolator) {
        for (Animator interpolator : this.mAnimators) {
            interpolator.setInterpolator(timeInterpolator);
        }
    }
}
