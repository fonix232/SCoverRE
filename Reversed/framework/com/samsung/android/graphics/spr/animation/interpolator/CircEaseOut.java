package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class CircEaseOut implements Interpolator {
    public CircEaseOut(Context context, AttributeSet attributeSet) {
    }

    private float out(float f) {
        f -= 1.0f;
        return (float) Math.sqrt((double) (1.0f - (f * f)));
    }

    public float getInterpolation(float f) {
        return out(f);
    }
}
