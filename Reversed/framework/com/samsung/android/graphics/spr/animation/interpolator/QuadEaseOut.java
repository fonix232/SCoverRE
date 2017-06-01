package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class QuadEaseOut implements Interpolator {
    public QuadEaseOut(Context context, AttributeSet attributeSet) {
    }

    private float out(float f) {
        return (-f) * (f - SprDocument.DEFAULT_DENSITY_SCALE);
    }

    public float getInterpolation(float f) {
        return out(f);
    }
}
