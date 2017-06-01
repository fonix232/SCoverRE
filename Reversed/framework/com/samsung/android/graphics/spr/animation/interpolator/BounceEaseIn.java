package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class BounceEaseIn implements Interpolator {
    public BounceEaseIn(Context context, AttributeSet attributeSet) {
    }

    private float in(float f) {
        return 1.0f - out(1.0f - f);
    }

    private float out(float f) {
        if (((double) f) < 0.36363636363636365d) {
            return (7.5625f * f) * f;
        }
        double d;
        if (((double) f) < 0.7272727272727273d) {
            d = ((double) f) - 0.5454545454545454d;
            return (float) (((d * 7.5625d) * ((double) ((float) d))) + 0.75d);
        } else if (((double) f) < 0.9090909090909091d) {
            d = ((double) f) - 0.8181818181818182d;
            return (float) (((d * 7.5625d) * ((double) ((float) d))) + 0.9375d);
        } else {
            d = ((double) f) - 0.9545454545454546d;
            return (float) (((d * 7.5625d) * ((double) ((float) d))) + 0.984375d);
        }
    }

    public float getInterpolation(float f) {
        return in(f);
    }
}
