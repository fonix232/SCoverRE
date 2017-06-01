package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class SineEaseOut implements Interpolator {
    public SineEaseOut(Context context, AttributeSet attributeSet) {
    }

    private float out(float f) {
        return (float) Math.sin(((double) f) * 1.5707963267948966d);
    }

    public float getInterpolation(float f) {
        return out(f);
    }
}
