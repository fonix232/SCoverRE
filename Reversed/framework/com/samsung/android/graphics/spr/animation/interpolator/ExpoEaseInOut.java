package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class ExpoEaseInOut implements Interpolator {
    public ExpoEaseInOut(Context context, AttributeSet attributeSet) {
    }

    private float inout(float f) {
        if (f == 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        f *= SprDocument.DEFAULT_DENSITY_SCALE;
        return f < 1.0f ? (float) (Math.pow(2.0d, (double) ((f - 1.0f) * 10.0f)) * 0.5d) : (float) (((-Math.pow(2.0d, (double) (-10.0f * (f - 1.0f)))) + 2.0d) * 0.5d);
    }

    public float getInterpolation(float f) {
        return inout(f);
    }
}
