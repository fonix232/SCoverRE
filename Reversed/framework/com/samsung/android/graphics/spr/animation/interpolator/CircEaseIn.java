package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class CircEaseIn implements Interpolator {
    public CircEaseIn(Context context, AttributeSet attributeSet) {
    }

    private float in(float f) {
        return (float) (-(Math.sqrt((double) (1.0f - (f * f))) - 1.0d));
    }

    public float getInterpolation(float f) {
        return in(f);
    }
}
