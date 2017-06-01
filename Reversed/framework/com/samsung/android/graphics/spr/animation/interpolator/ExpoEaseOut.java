package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class ExpoEaseOut implements Interpolator {
    public ExpoEaseOut(Context context, AttributeSet attributeSet) {
    }

    private float out(float f) {
        double d = 1.0d;
        if (f < 1.0f) {
            d = 1.0d + (-Math.pow(2.0d, (double) (-10.0f * f)));
        }
        return (float) d;
    }

    public float getInterpolation(float f) {
        return out(f);
    }
}
