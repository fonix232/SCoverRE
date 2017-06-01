package com.samsung.android.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.GridView;

public class SemGridSortAnimator {
    private static int DELAY_BETWEEN_ANIMATIONS = 70;
    private static final Interpolator FADE_IN_INTERPOLATOR = new PathInterpolator(0.33f, 0.0f, 0.2f, 1.0f);
    private static int FADE_IN_TRANSLATE_ANIMATION_DURATION = 400;
    private static int FADE_OUT_ANIMATION_DURATION = 150;
    private static final Interpolator FADE_OUT_INTERPOLATOR = new AccelerateInterpolator();
    AnimatorListener mAnimatorListener;
    final GridView mGridView;
    OnSortListener mOnSortListener;

    class C09701 implements OnPreDrawListener {
        C09701() {
        }

        public boolean onPreDraw() {
            SemGridSortAnimator.this.mGridView.getViewTreeObserver().removeOnPreDrawListener(this);
            SemGridSortAnimator.this.startFadeInTranslateAnim();
            return true;
        }
    }

    class C09733 extends AnimatorListenerAdapter {
        C09733() {
        }

        public void onAnimationEnd(Animator animator) {
            SemGridSortAnimator.this.mGridView.setEnabled(true);
            if (SemGridSortAnimator.this.mAnimatorListener != null) {
                SemGridSortAnimator.this.mAnimatorListener.onAnimationEnd(null);
            }
        }
    }

    public interface OnSortListener {
        void onSort();
    }

    public SemGridSortAnimator(GridView gridView, OnSortListener onSortListener) {
        if (gridView == null || onSortListener == null) {
            throw new IllegalArgumentException("Constructor arguments should be non-null references.");
        }
        this.mGridView = gridView;
        this.mOnSortListener = onSortListener;
    }

    private void startFadeInTranslateAnim() {
        int childCount = this.mGridView.getChildCount();
        int numColumns = this.mGridView.getNumColumns();
        int i = 0;
        if (childCount > 0) {
            i = this.mGridView.getChildAt(0).getHeight();
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.mGridView.getChildAt(i2);
            int i3 = i2 % numColumns;
            childAt.setTranslationY((-((float) i)) * 0.5f);
            childAt.setAlpha(0.0f);
            childAt.animate().alpha(1.0f).translationY(0.0f).setListener(null).setDuration((long) FADE_IN_TRANSLATE_ANIMATION_DURATION).setStartDelay((long) (DELAY_BETWEEN_ANIMATIONS * i3)).setInterpolator(FADE_IN_INTERPOLATOR).withLayer();
            if (i2 == childCount - 1) {
                childAt.animate().setListener(new C09733());
            }
        }
        if (childCount == 0 && this.mAnimatorListener != null) {
            this.mAnimatorListener.onAnimationEnd(null);
        }
    }

    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    public void sortTheGrid() {
        int childCount = this.mGridView.getChildCount();
        if (this.mAnimatorListener != null) {
            this.mAnimatorListener.onAnimationStart(null);
        }
        if (childCount == 0) {
            this.mOnSortListener.onSort();
            this.mGridView.invalidate();
            this.mGridView.getViewTreeObserver().addOnPreDrawListener(new C09701());
            return;
        }
        int i = 0;
        while (i < childCount) {
            final boolean z = i == childCount + -1;
            final View childAt = this.mGridView.getChildAt(i);
            childAt.animate().alpha(0.0f).setDuration((long) FADE_OUT_ANIMATION_DURATION).setStartDelay(0).setInterpolator(FADE_OUT_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {

                class C09711 implements OnPreDrawListener {
                    C09711() {
                    }

                    public boolean onPreDraw() {
                        SemGridSortAnimator.this.mGridView.getViewTreeObserver().removeOnPreDrawListener(this);
                        SemGridSortAnimator.this.startFadeInTranslateAnim();
                        return true;
                    }
                }

                public void onAnimationEnd(Animator animator) {
                    childAt.setAlpha(1.0f);
                    if (z) {
                        SemGridSortAnimator.this.mOnSortListener.onSort();
                        SemGridSortAnimator.this.mGridView.invalidate();
                        SemGridSortAnimator.this.mGridView.getViewTreeObserver().addOnPreDrawListener(new C09711());
                    }
                }

                public void onAnimationStart(Animator animator) {
                    if (z) {
                        SemGridSortAnimator.this.mGridView.setEnabled(false);
                    }
                }
            }).withLayer();
            i++;
        }
    }
}
