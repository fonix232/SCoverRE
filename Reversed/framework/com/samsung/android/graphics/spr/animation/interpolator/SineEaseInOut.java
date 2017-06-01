package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class SineEaseInOut implements Interpolator {
    public SineEaseInOut(Context context, AttributeSet attributeSet) {
    }

    private float inout(float f) {
        return (float) ((Math.cos(((double) f) * 3.141592653589793d) - 1.0d) * -0.5d);
    }

    public float getInterpolation(float f) {
        return inout(f);
    }
}
