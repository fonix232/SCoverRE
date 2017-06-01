package com.samsung.android.animation;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import com.samsung.android.animation.SemSweepListAnimator.OnSweepListener;
import com.samsung.android.animation.SemSweepListAnimator.SweepConfiguration;

abstract class SemAbsSweepAnimationFilter {
    protected boolean mIsAnimationBack = false;

    SemAbsSweepAnimationFilter() {
    }

    abstract ValueAnimator createActionUpAnimator(View view, float f, int i, float f2, boolean z);

    abstract void doMoveAction(View view, float f, int i);

    abstract void doRefresh();

    abstract void doUpActionWhenAnimationUpdate(int i, float f);

    abstract void draw(Canvas canvas);

    abstract Rect getBitmapDrawableBound();

    abstract float getEndXOfActionUpAnimator();

    abstract void initAnimationFilter(View view, float f, int i, OnSweepListener onSweepListener, SweepConfiguration sweepConfiguration);

    public boolean isAnimationBack() {
        return this.mIsAnimationBack;
    }

    abstract void setForegroundView(View view);
}
