package com.samsung.android.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ListView;

public class SemListSortAnimator {
    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static int DELAY_BETWEEN_ANIMATIONS = 100;
    private static int FADE_IN_TRANSLATE_ANIMATION_DURATION = 300;
    private static int FADE_OUT_ANIMATION_DURATION = 150;
    AnimatorListener mAnimatorListener;
    final ListView mListView;
    OnSortListener mOnSortListener;

    class C09741 implements OnPreDrawListener {
        C09741() {
        }

        public boolean onPreDraw() {
            SemListSortAnimator.this.mListView.getViewTreeObserver().removeOnPreDrawListener(this);
            SemListSortAnimator.this.startFadeInTranslateAnim();
            return true;
        }
    }

    class C09773 extends AnimatorListenerAdapter {
        C09773() {
        }

        public void onAnimationEnd(Animator animator) {
            SemListSortAnimator.this.mListView.setEnabled(true);
            if (SemListSortAnimator.this.mAnimatorListener != null) {
                SemListSortAnimator.this.mAnimatorListener.onAnimationEnd(null);
            }
        }
    }

    public interface OnSortListener {
        void onSort();
    }

    public SemListSortAnimator(ListView listView, OnSortListener onSortListener) {
        if (listView == null || onSortListener == null) {
            throw new IllegalArgumentException("Constructor arguments should be non-null references.");
        }
        this.mListView = listView;
        this.mOnSortListener = onSortListener;
    }

    private void startFadeInTranslateAnim() {
        int childCount = this.mListView.getChildCount();
        int i = 0;
        if (childCount > this.mListView.getHeaderViewsCount()) {
            i = this.mListView.getChildAt(this.mListView.getHeaderViewsCount()).getHeight();
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.mListView.getChildAt(i2);
            childAt.setTranslationY((-((float) i)) / 2.0f);
            childAt.setAlpha(0.0f);
            childAt.animate().alpha(1.0f).translationY(0.0f).setListener(null).setDuration((long) FADE_IN_TRANSLATE_ANIMATION_DURATION).setStartDelay((long) (DELAY_BETWEEN_ANIMATIONS * i2)).setInterpolator(DECELERATE_INTERPOLATOR).withLayer();
            if (i2 == childCount - 1) {
                childAt.animate().setListener(new C09773());
            }
        }
        if (childCount == 0 && this.mAnimatorListener != null) {
            this.mAnimatorListener.onAnimationEnd(null);
        }
    }

    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    public void sortTheList() {
        int childCount = this.mListView.getChildCount();
        if (this.mAnimatorListener != null) {
            this.mAnimatorListener.onAnimationStart(null);
        }
        if (childCount == 0) {
            this.mOnSortListener.onSort();
            this.mListView.invalidate();
            this.mListView.getViewTreeObserver().addOnPreDrawListener(new C09741());
            return;
        }
        int i = 0;
        while (i < childCount) {
            final boolean z = i == childCount + -1;
            final View childAt = this.mListView.getChildAt(i);
            childAt.animate().alpha(0.0f).setDuration((long) FADE_OUT_ANIMATION_DURATION).setStartDelay(0).setInterpolator(ACCELERATE_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {

                class C09751 implements OnPreDrawListener {
                    C09751() {
                    }

                    public boolean onPreDraw() {
                        SemListSortAnimator.this.mListView.getViewTreeObserver().removeOnPreDrawListener(this);
                        SemListSortAnimator.this.startFadeInTranslateAnim();
                        return true;
                    }
                }

                public void onAnimationEnd(Animator animator) {
                    childAt.setAlpha(1.0f);
                    if (z) {
                        SemListSortAnimator.this.mOnSortListener.onSort();
                        SemListSortAnimator.this.mListView.invalidate();
                        SemListSortAnimator.this.mListView.getViewTreeObserver().addOnPreDrawListener(new C09751());
                    }
                }

                public void onAnimationStart(Animator animator) {
                    if (z) {
                        SemListSortAnimator.this.mListView.setEnabled(false);
                    }
                }
            }).withLayer();
            i++;
        }
    }
}
