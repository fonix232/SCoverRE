package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class QuintEaseIn implements Interpolator {
    public QuintEaseIn(Context context, AttributeSet attributeSet) {
    }

    private float in(float f) {
        return (((f * f) * f) * f) * f;
    }

    public float getInterpolation(float f) {
        return in(f);
    }
}
