package com.samsung.android.animation;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.Transformation;

class SemDragAndDropAnimationCore {
    static final int SELECT_HIGHLIGHT_ANIM_DURATION = 150;
    private static final String TAG = "SemDragAndDropAnimationCore";
    static final int TRANSLATE_ITEM_ANIM_DURATION = 300;
    ItemAnimator itemAnimator = new ItemAnimator();
    private ItemAnimationListener mItemAnimationListener;
    private View mView;

    static abstract class ItemAnimation {
        int mDuration;
        float mProgress;
        long mStartTime;

        ItemAnimation() {
        }

        void computeAnimation(long j) {
            this.mProgress = ((float) (j - this.mStartTime)) / ((float) this.mDuration);
            if (this.mProgress > 1.0f) {
                this.mProgress = 1.0f;
            }
        }

        int getDuration() {
            return this.mDuration;
        }

        float getProgress() {
            return this.mProgress;
        }

        abstract void getTransformation(Transformation transformation);

        boolean isFinished() {
            return this.mStartTime + ((long) this.mDuration) <= SystemClock.uptimeMillis();
        }
    }

    interface ItemAnimationListener {
        void onItemAnimatorEnd();
    }

    class ItemAnimator implements Runnable {
        private SparseArray<ItemAnimation> mAnimations = new SparseArray();
        private boolean mIsAnimating;

        ItemAnimator() {
        }

        ItemAnimation getItemAnimation(int i) {
            return (ItemAnimation) this.mAnimations.get(i, null);
        }

        void putItemAnimation(int i, ItemAnimation itemAnimation) {
            this.mAnimations.put(i, itemAnimation);
        }

        void removeAll() {
            this.mAnimations.clear();
        }

        void removeItemAnimation(int i) {
            this.mAnimations.delete(i);
        }

        public void run() {
            long uptimeMillis = SystemClock.uptimeMillis();
            int i = 1;
            for (int size = this.mAnimations.size() - 1; size >= 0; size--) {
                ItemAnimation itemAnimation = (ItemAnimation) this.mAnimations.get(this.mAnimations.keyAt(size), null);
                if (itemAnimation != null) {
                    itemAnimation.computeAnimation(uptimeMillis);
                    i &= itemAnimation.isFinished();
                }
            }
            SemDragAndDropAnimationCore.this.mView.invalidate();
            if (i == 0) {
                SemDragAndDropAnimationCore.this.mView.postOnAnimation(this);
            } else if (this.mIsAnimating) {
                this.mIsAnimating = false;
                if (SemDragAndDropAnimationCore.this.mItemAnimationListener != null) {
                    SemDragAndDropAnimationCore.this.mItemAnimationListener.onItemAnimatorEnd();
                }
            }
        }

        void start() {
            this.mIsAnimating = true;
            SemDragAndDropAnimationCore.this.mView.removeCallbacks(this);
            run();
        }
    }

    static class ItemSelectHighlightingAnimation extends ItemAnimation {
        private static final float DEFAULT_FROM_X = 1.0f;
        private static final float DEFAULT_FROM_Y = 1.0f;
        private static final float DEFAULT_TO_X = 1.08f;
        private static final float DEFAULT_TO_Y = 1.08f;
        private float mFromX;
        private float mFromY;
        private boolean mHalfOfAnimationPassed = false;
        private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
        private float mPivotX;
        private float mPivotY;
        private float mToX;
        private float mToY;

        ItemSelectHighlightingAnimation(Rect rect) {
            this.mPivotX = rect.exactCenterX();
            this.mPivotY = rect.exactCenterY();
            this.mFromX = 1.0f;
            this.mToX = 1.08f;
            this.mFromY = 1.0f;
            this.mToY = 1.08f;
        }

        private void switchToScaleDown() {
            float f = this.mFromX;
            this.mFromX = this.mToX;
            this.mToX = f;
            f = this.mFromY;
            this.mFromY = this.mToY;
            this.mToY = f;
        }

        void computeAnimation(long j) {
            super.computeAnimation(j);
            if (this.mProgress > 0.5f && !this.mHalfOfAnimationPassed) {
                switchToScaleDown();
                this.mHalfOfAnimationPassed = true;
            }
        }

        void getTransformation(Transformation transformation) {
            transformation.setTransformationType(2);
            Matrix matrix = transformation.getMatrix();
            matrix.reset();
            if (this.mProgress > 1.0f) {
                this.mProgress = 1.0f;
            }
            float interpolation = this.mInterpolator.getInterpolation(this.mProgress);
            float f = 1.0f;
            float f2 = 1.0f;
            if (!(this.mFromX == 1.0f && this.mToX == 1.0f)) {
                f = this.mFromX + ((this.mToX - this.mFromX) * interpolation);
            }
            if (!(this.mFromY == 1.0f && this.mToY == 1.0f)) {
                f2 = this.mFromY + ((this.mToY - this.mFromY) * interpolation);
            }
            matrix.setScale(f, f2, this.mPivotX, this.mPivotY);
        }

        void setScaleUpParameters(float f, float f2, float f3, float f4, float f5, float f6) {
            this.mFromX = f;
            this.mToX = f2;
            this.mFromY = f3;
            this.mToY = f4;
            this.mPivotX = f5;
            this.mPivotY = f6;
        }

        void setStartAndDuration(int i) {
            this.mStartTime = SystemClock.uptimeMillis();
            this.mDuration = i;
            if (i == 0) {
                this.mDuration = 150;
            }
        }
    }

    static class TranslateItemAnimation extends ItemAnimation {
        private int mDeltaX;
        private int mDeltaY;
        private Interpolator mInterpolator = new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f);
        private int mOffsetXDest;
        private int mOffsetYDest;

        TranslateItemAnimation() {
        }

        float getCurrentTranslateX() {
            return ((float) this.mOffsetXDest) - (((float) this.mDeltaX) * (1.0f - this.mInterpolator.getInterpolation(this.mProgress)));
        }

        float getCurrentTranslateY() {
            return ((float) this.mOffsetYDest) - (((float) this.mDeltaY) * (1.0f - this.mInterpolator.getInterpolation(this.mProgress)));
        }

        int getDestOffsetX() {
            return this.mOffsetXDest;
        }

        int getDestOffsetY() {
            return this.mOffsetYDest;
        }

        void getTransformation(Transformation transformation) {
            transformation.setTransformationType(2);
            Matrix matrix = transformation.getMatrix();
            matrix.reset();
            float interpolation = this.mInterpolator.getInterpolation(this.mProgress);
            matrix.setTranslate(((float) this.mOffsetXDest) - (((float) this.mDeltaX) * (1.0f - interpolation)), ((float) this.mOffsetYDest) - (((float) this.mDeltaY) * (1.0f - interpolation)));
        }

        void setStartAndDuration(float f) {
            setStartAndDuration(Math.round(300.0f * f));
        }

        void setStartAndDuration(int i) {
            this.mStartTime = SystemClock.uptimeMillis();
            this.mDuration = i;
            if (i == 0) {
                this.mDuration = 300;
            }
        }

        void translate(int i, int i2, int i3, int i4) {
            this.mOffsetXDest = i;
            this.mDeltaX = i2;
            this.mOffsetYDest = i3;
            this.mDeltaY = i4;
        }
    }

    SemDragAndDropAnimationCore(View view) {
        this.mView = view;
    }

    void setAnimationListener(ItemAnimationListener itemAnimationListener) {
        this.mItemAnimationListener = itemAnimationListener;
    }
}
