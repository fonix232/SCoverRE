package com.samsung.android.graphics.spr.animation.interpolator;

import android.view.animation.Interpolator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class ElasticEaseInOut implements Interpolator {
    private float amplitude;
    private float period;

    public ElasticEaseInOut(float f, float f2) {
        this.amplitude = f;
        this.period = f2;
    }

    private float inout(float f, float f2, float f3) {
        if (f == 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        float f4;
        if (f3 == 0.0f) {
            f3 = 0.45000002f;
        }
        if (f2 == 0.0f || f2 < 1.0f) {
            f2 = 1.0f;
            f4 = f3 / 4.0f;
        } else {
            f4 = (float) ((((double) f3) / 6.283185307179586d) * Math.asin((double) (1.0f / f2)));
        }
        f *= SprDocument.DEFAULT_DENSITY_SCALE;
        if (f < 1.0f) {
            f -= 1.0f;
            return (float) (((((double) f2) * Math.pow(2.0d, (double) (10.0f * f))) * Math.sin((((double) (f - f4)) * 6.283185307179586d) / ((double) f3))) * -0.5d);
        }
        f -= 1.0f;
        return (float) ((((((double) f2) * Math.pow(2.0d, (double) (-10.0f * f))) * Math.sin((((double) (f - f4)) * 6.283185307179586d) / ((double) f3))) * 0.5d) + 1.0d);
    }

    public float getInterpolation(float f) {
        return inout(f, this.amplitude, this.period);
    }
}
