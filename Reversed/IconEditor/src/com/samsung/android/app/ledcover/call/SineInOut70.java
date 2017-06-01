package com.samsung.android.app.ledcover.call;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class SineInOut70 implements Interpolator {
    private static final float[][] segments;

    static {
        segments = new float[][]{new float[]{0.0f, 0.01f, 0.45f}, new float[]{0.45f, 0.8f, 0.908f}, new float[]{0.908f, 0.9999f, 1.0f}};
    }

    public SineInOut70(Context context, AttributeSet attrs) {
    }

    public float getInterpolation(float input) {
        float _loc_5 = input / 1.0f;
        int _loc_6 = segments.length;
        int _loc_9 = (int) Math.floor((double) (((float) _loc_6) * _loc_5));
        if (_loc_9 >= segments.length) {
            _loc_9 = segments.length - 1;
        }
        float _loc_7 = (_loc_5 - (((float) _loc_9) * (1.0f / ((float) _loc_6)))) * ((float) _loc_6);
        float[] _loc_8 = segments[_loc_9];
        return 0.0f + ((_loc_8[0] + ((((2.0f * (1.0f - _loc_7)) * (_loc_8[1] - _loc_8[0])) + ((_loc_8[2] - _loc_8[0]) * _loc_7)) * _loc_7)) * 1.0f);
    }
}
