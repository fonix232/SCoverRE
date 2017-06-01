package com.samsung.android.graphics.spr.animation.interpolator;

import android.view.animation.Interpolator;

public class BackEaseIn implements Interpolator {
    private float overshot;

    public BackEaseIn(float f) {
        this.overshot = f;
    }

    private float in(float f, float f2) {
        if (f2 == 0.0f) {
            f2 = 1.70158f;
        }
        return (f * f) * (((1.0f + f2) * f) - f2);
    }

    public float getInterpolation(float f) {
        return in(f, this.overshot);
    }
}
