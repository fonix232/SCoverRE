package com.samsung.android.graphics.spr.animation.interpolator;

import android.view.animation.Interpolator;

public class BackEaseOut implements Interpolator {
    private float overshot;

    public BackEaseOut(float f) {
        this.overshot = f;
    }

    private float out(float f, float f2) {
        if (f2 == 0.0f) {
            f2 = 1.70158f;
        }
        f -= 1.0f;
        return ((f * f) * (((f2 + 1.0f) * f) + f2)) + 1.0f;
    }

    public float getInterpolation(float f) {
        return out(f, this.overshot);
    }
}
