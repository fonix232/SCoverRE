package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class QuartEaseOut implements Interpolator {
    public QuartEaseOut(Context context, AttributeSet attributeSet) {
    }

    private float out(float f) {
        f -= 1.0f;
        return -((((f * f) * f) * f) - 1.0f);
    }

    public float getInterpolation(float f) {
        return out(f);
    }
}
