package com.samsung.android.graphics.spr.animation.interpolator;

import android.view.animation.Interpolator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class BackEaseInOut implements Interpolator {
    private float overshot;

    public BackEaseInOut(float f) {
        this.overshot = f;
    }

    private float inout(float f, float f2) {
        if (f2 == 0.0f) {
            f2 = 1.70158f;
        }
        f *= SprDocument.DEFAULT_DENSITY_SCALE;
        if (f < 1.0f) {
            double d = ((double) f2) * 1.525d;
            return (float) ((((double) (f * f)) * (((d + 1.0d) * ((double) f)) - ((double) ((float) d)))) * 0.5d);
        }
        f -= SprDocument.DEFAULT_DENSITY_SCALE;
        d = ((double) f2) * 1.525d;
        return (float) (((((double) (f * f)) * (((d + 1.0d) * ((double) f)) + ((double) ((float) d)))) + 2.0d) * 0.5d);
    }

    public float getInterpolation(float f) {
        return inout(f, this.overshot);
    }
}
