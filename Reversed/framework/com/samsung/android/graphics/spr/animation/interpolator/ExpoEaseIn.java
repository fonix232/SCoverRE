package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class ExpoEaseIn implements Interpolator {
    public ExpoEaseIn(Context context, AttributeSet attributeSet) {
    }

    private float in(float f) {
        return (float) (f == 0.0f ? 0.0d : Math.pow(2.0d, (double) ((f - 1.0f) * 10.0f)));
    }

    public float getInterpolation(float f) {
        return in(f);
    }
}
