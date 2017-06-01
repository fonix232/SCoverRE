package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class SineEaseIn implements Interpolator {
    public SineEaseIn(Context context, AttributeSet attributeSet) {
    }

    private float in(float f) {
        return (float) ((-Math.cos(((double) f) * 1.5707963267948966d)) + 1.0d);
    }

    public float getInterpolation(float f) {
        return in(f);
    }
}
