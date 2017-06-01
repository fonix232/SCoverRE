package com.samsung.android.graphics.spr.animation.interpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class SineIn33 implements Interpolator {
    private static final float[][] segments = new float[][]{new float[]{0.0f, 0.001f, 0.32f}, new float[]{0.32f, 0.59f, 1.0f}};

    public SineIn33(Context context, AttributeSet attributeSet) {
    }

    public float getInterpolation(float f) {
        float f2 = f / 1.0f;
        int length = segments.length;
        int floor = (int) Math.floor((double) (((float) length) * f2));
        if (floor >= segments.length) {
            floor = segments.length - 1;
        }
        float f3 = (f2 - (((float) floor) * (1.0f / ((float) length)))) * ((float) length);
        float[] fArr = segments[floor];
        return 0.0f + ((fArr[0] + (((((1.0f - f3) * SprDocument.DEFAULT_DENSITY_SCALE) * (fArr[1] - fArr[0])) + ((fArr[2] - fArr[0]) * f3)) * f3)) * 1.0f);
    }
}
