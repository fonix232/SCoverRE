package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class CircEaseInOut implements Interpolator {
    public CircEaseInOut(Context context, AttributeSet attributeSet) {
    }

    private float inout(float f) {
        f *= SprDocument.DEFAULT_DENSITY_SCALE;
        if (f < 1.0f) {
            return (float) ((Math.sqrt((double) (1.0f - (f * f))) - 1.0d) * -0.5d);
        }
        f -= SprDocument.DEFAULT_DENSITY_SCALE;
        return (float) ((Math.sqrt((double) (1.0f - (f * f))) + 1.0d) * 0.5d);
    }

    public float getInterpolation(float f) {
        return inout(f);
    }
}
