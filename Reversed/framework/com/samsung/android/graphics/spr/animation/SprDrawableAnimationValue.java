package com.samsung.android.graphics.spr.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase.UpdateParameter;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeAnimatorSet;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeBase;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeFill;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeMatrix;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeStroke;
import com.samsung.android.graphics.spr.document.shape.SprObjectBase;
import java.util.ArrayList;

public class SprDrawableAnimationValue extends SprDrawableAnimation {
    private final ArrayList<AnimatorData> mAnimatingList = new ArrayList();

    private static class AnimatorData {
        public AnimatorSet animatorSet;
        public long duration;
        public SprAttributeFill fillPaint;
        public boolean isRunning;
        public SprAttributeMatrix matrix;
        public SprObjectBase object;
        public int repeatCount;
        public long startTime;
        public SprAttributeStroke strokePaint;
        public UpdateParameter updateParameter;

        private AnimatorData() {
            this.updateParameter = new UpdateParameter();
        }
    }

    public SprDrawableAnimationValue(Drawable drawable, SprDocument sprDocument) {
        super((byte) 1, drawable, sprDocument);
    }

    public void run() {
        if (this.mAnimatingList.size() == 0) {
            this.mIsRunning = false;
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        int i = 0;
        while (i < this.mAnimatingList.size()) {
            AnimatorData animatorData = (AnimatorData) this.mAnimatingList.get(i);
            if (animatorData.isRunning) {
                boolean updateAnimatorData;
                if (uptimeMillis > animatorData.startTime + animatorData.duration) {
                    updateAnimatorData = updateAnimatorData(animatorData, true);
                    animatorData.animatorSet.cancel();
                    if (animatorData.repeatCount != 0) {
                        animatorData.animatorSet.start();
                        animatorData.startTime = uptimeMillis;
                        if (animatorData.repeatCount > 0) {
                            animatorData.repeatCount--;
                        }
                    } else {
                        this.mAnimatingList.remove(i);
                        i--;
                    }
                } else {
                    updateAnimatorData = updateAnimatorData(animatorData, false);
                }
                if (updateAnimatorData) {
                    animatorData.object.preDraw(this.mDocument);
                }
            } else if (uptimeMillis > animatorData.startTime) {
                animatorData.animatorSet.start();
                animatorData.startTime = uptimeMillis;
                animatorData.isRunning = true;
            }
            i++;
        }
        if (this.mAnimatingList.size() > 0) {
            this.mDrawable.scheduleSelf(this, ((long) this.mInterval) + uptimeMillis);
        }
        this.mDrawable.invalidateSelf();
    }

    public void start() {
        super.start();
        this.mAnimatingList.clear();
        long uptimeMillis = SystemClock.uptimeMillis();
        for (SprObjectBase sprObjectBase : this.mDocument.getValueAnimationObjects()) {
            SprAttributeAnimatorSet sprAttributeAnimatorSet = null;
            SprAttributeMatrix sprAttributeMatrix = null;
            SprAttributeFill sprAttributeFill = null;
            SprAttributeStroke sprAttributeStroke = null;
            for (SprAttributeBase sprAttributeBase : sprObjectBase.mAttributeList) {
                switch (sprAttributeBase.mType) {
                    case (byte) 32:
                        sprAttributeFill = (SprAttributeFill) sprAttributeBase;
                        break;
                    case (byte) 35:
                        sprAttributeStroke = (SprAttributeStroke) sprAttributeBase;
                        break;
                    case (byte) 64:
                        sprAttributeMatrix = (SprAttributeMatrix) sprAttributeBase;
                        break;
                    case (byte) 97:
                        sprAttributeAnimatorSet = (SprAttributeAnimatorSet) sprAttributeBase;
                        break;
                    default:
                        break;
                }
            }
            if (sprAttributeAnimatorSet != null) {
                AnimatorData animatorData = new AnimatorData();
                animatorData.animatorSet = new AnimatorSet();
                animatorData.animatorSet.playTogether(sprAttributeAnimatorSet.getAnimators());
                if (sprAttributeFill == null) {
                    SprAttributeBase sprAttributeFill2 = sprObjectBase.hasFillAnimation ? new SprAttributeFill() : new SprAttributeFill((byte) 0, 0);
                    sprObjectBase.getIntrinsic().appendAttribute(sprAttributeFill2);
                    try {
                        sprAttributeFill = (SprAttributeFill) sprAttributeFill2.clone();
                        sprObjectBase.appendAttribute(sprAttributeFill);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                if (sprAttributeStroke == null) {
                    SprAttributeBase sprAttributeStroke2 = sprObjectBase.hasStrokeAnimation ? new SprAttributeStroke() : new SprAttributeStroke((byte) 0, 0);
                    sprObjectBase.getIntrinsic().appendAttribute(sprAttributeStroke2);
                    try {
                        sprAttributeStroke = (SprAttributeStroke) sprAttributeStroke2.clone();
                        sprObjectBase.appendAttribute(sprAttributeStroke);
                    } catch (Throwable e2) {
                        throw new RuntimeException(e2);
                    }
                }
                if (sprAttributeMatrix == null) {
                    SprAttributeBase sprAttributeMatrix2 = new SprAttributeMatrix();
                    sprObjectBase.getIntrinsic().appendAttribute(sprAttributeMatrix2);
                    try {
                        sprAttributeMatrix = sprAttributeMatrix2.clone();
                        sprObjectBase.appendAttribute(sprAttributeMatrix);
                    } catch (Throwable e22) {
                        throw new RuntimeException(e22);
                    }
                }
                animatorData.matrix = sprAttributeMatrix;
                animatorData.fillPaint = sprAttributeFill;
                animatorData.strokePaint = sprAttributeStroke;
                animatorData.object = sprObjectBase;
                animatorData.startTime = uptimeMillis;
                animatorData.duration = (long) sprAttributeAnimatorSet.duration;
                animatorData.repeatCount = sprAttributeAnimatorSet.repeatCount;
                this.mAnimatingList.add(animatorData);
            }
        }
        this.mDrawable.scheduleSelf(this, uptimeMillis);
    }

    public void stop() {
        super.stop();
        for (AnimatorData animatorData : this.mAnimatingList) {
            animatorData.animatorSet.cancel();
        }
        this.mAnimatingList.clear();
    }

    public void update() {
        super.update();
        for (AnimatorData updateAnimatorData : this.mAnimatingList) {
            updateAnimatorData(updateAnimatorData, false);
        }
    }

    public boolean updateAnimatorData(AnimatorData animatorData, boolean z) {
        animatorData.updateParameter.isLastFrame = z;
        UpdateParameter updateParameter = animatorData.updateParameter;
        animatorData.updateParameter.isUpdatedStrokeColor = false;
        animatorData.updateParameter.isUpdatedFillColor = false;
        animatorData.updateParameter.isUpdatedTranslate = false;
        animatorData.updateParameter.isUpdatedRotate = false;
        updateParameter.isUpdatedScale = false;
        animatorData.updateParameter.alpha = animatorData.object.alpha;
        boolean z2 = false;
        for (Animator animator : animatorData.animatorSet.getChildAnimations()) {
            if (((SprAnimatorBase) animator).update(animatorData.updateParameter)) {
                z2 = true;
            }
        }
        animatorData.matrix.reset();
        if (animatorData.updateParameter.isUpdatedScale) {
            animatorData.matrix.matrix.postScale(animatorData.updateParameter.scaleX, animatorData.updateParameter.scaleY, animatorData.updateParameter.scalePivotX, animatorData.updateParameter.scalePivotY);
        }
        if (animatorData.updateParameter.isUpdatedRotate) {
            animatorData.matrix.matrix.postRotate(animatorData.updateParameter.rotateDegree, animatorData.updateParameter.rotatePivotX, animatorData.updateParameter.rotatePivotY);
        }
        if (animatorData.updateParameter.isUpdatedTranslate) {
            animatorData.matrix.matrix.postTranslate(animatorData.updateParameter.translateDx, animatorData.updateParameter.translateDy);
        }
        if (animatorData.updateParameter.isUpdatedFillColor) {
            animatorData.fillPaint.color = animatorData.updateParameter.fillColor;
        }
        if (animatorData.updateParameter.isUpdatedStrokeColor) {
            animatorData.strokePaint.color = animatorData.updateParameter.strokeColor;
        }
        animatorData.object.alpha = animatorData.updateParameter.alpha;
        return z2;
    }
}
