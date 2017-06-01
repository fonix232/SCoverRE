package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemColorClampFilter extends SemGenericImageFilter {
    private static final int MAX_ALPHA = 7;
    private static final int MAX_BLUE = 6;
    private static final int MAX_GREEN = 5;
    private static final int MAX_RED = 4;
    private static final int MIN_ALPHA = 3;
    private static final int MIN_BLUE = 2;
    private static final int MIN_GREEN = 1;
    private static final int MIN_RED = 0;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nvoid main(void) {\n  vec4 color = texture2D(baseSampler, outTexCoords);\n  vec4 minVal = vec4(filterParams[0], filterParams[1], filterParams[2], filterParams[3]);\n  vec4 maxVal = vec4(filterParams[4], filterParams[5], filterParams[6], filterParams[7]);\n  gl_FragColor = clamp(color, minVal, maxVal);\n}\n\n";

    public SemColorClampFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
        useFilterParams();
        setMinColor(0.0f, 0.0f, 0.0f, 0.0f);
        setMaxColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public int[] animateMaxColor(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, long j, long j2, TimeInterpolator timeInterpolator) {
        final float f9 = f2;
        final float f10 = f;
        final float f11 = f4;
        final float f12 = f3;
        final float f13 = f6;
        final float f14 = f5;
        final float f15 = f8;
        final float f16 = f7;
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[4];

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemColorClampFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemColorClampFilter animateMaxColor aFraction = " + f);
                }
                float f2 = ((f11 - f12) * f) + f12;
                float f3 = ((f13 - f14) * f) + f14;
                float f4 = ((f15 - f16) * f) + f16;
                float max = Math.max(0.0f, Math.min(((f9 - f10) * f) + f10, 1.0f));
                f2 = Math.max(0.0f, Math.min(f2, 1.0f));
                f3 = Math.max(0.0f, Math.min(f3, 1.0f));
                f4 = Math.max(0.0f, Math.min(f4, 1.0f));
                if (SemColorClampFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemColorClampFilter animateMaxColor redColor = " + max);
                    Log.e("HWUIIF", "SemColorClampFilter animateMaxColor greenColor = " + f2);
                    Log.e("HWUIIF", "SemColorClampFilter animateMaxColor blueColor = " + f3);
                    Log.e("HWUIIF", "SemColorClampFilter animateMaxColor alphaColor = " + f4);
                }
                this.params[0] = max;
                this.params[1] = f2;
                this.params[2] = f3;
                this.params[3] = f4;
                System.arraycopy(this.params, 0, SemColorClampFilter.this.mParams, 4, this.params.length);
                imageFilterAnimator.setUniformf("filterParams", this.params, 4);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateMaxColor(int i, int i2, long j, long j2, TimeInterpolator timeInterpolator) {
        return animateMaxColor(((float) Color.red(i)) / 255.0f, ((float) Color.red(i2)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.green(i2)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.blue(i2)) / 255.0f, ((float) Color.alpha(i)) / 255.0f, ((float) Color.alpha(i2)) / 255.0f, j, j2, timeInterpolator);
    }

    public int[] animateMinColor(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, long j, long j2, TimeInterpolator timeInterpolator) {
        final float f9 = f2;
        final float f10 = f;
        final float f11 = f4;
        final float f12 = f3;
        final float f13 = f6;
        final float f14 = f5;
        final float f15 = f8;
        final float f16 = f7;
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[4];

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemColorClampFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemColorClampFilter animateMinColor aFraction = " + f);
                }
                float f2 = ((f11 - f12) * f) + f12;
                float f3 = ((f13 - f14) * f) + f14;
                float f4 = ((f15 - f16) * f) + f16;
                float max = Math.max(0.0f, Math.min(((f9 - f10) * f) + f10, 1.0f));
                f2 = Math.max(0.0f, Math.min(f2, 1.0f));
                f3 = Math.max(0.0f, Math.min(f3, 1.0f));
                f4 = Math.max(0.0f, Math.min(f4, 1.0f));
                if (SemColorClampFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemColorClampFilter animateMinColor redColor = " + max);
                    Log.e("HWUIIF", "SemColorClampFilter animateMinColor greenColor = " + f2);
                    Log.e("HWUIIF", "SemColorClampFilter animateMinColor blueColor = " + f3);
                    Log.e("HWUIIF", "SemColorClampFilter animateMinColor alphaColor = " + f4);
                }
                this.params[0] = max;
                this.params[1] = f2;
                this.params[2] = f3;
                this.params[3] = f4;
                System.arraycopy(this.params, 0, SemColorClampFilter.this.mParams, 0, this.params.length);
                imageFilterAnimator.setUniformf("filterParams", this.params, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateMinColor(int i, int i2, long j, long j2, TimeInterpolator timeInterpolator) {
        return animateMinColor(((float) Color.red(i)) / 255.0f, ((float) Color.red(i2)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.green(i2)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.blue(i2)) / 255.0f, ((float) Color.alpha(i)) / 255.0f, ((float) Color.alpha(i2)) / 255.0f, j, j2, timeInterpolator);
    }

    public void setMaxColor(float f, float f2, float f3, float f4) {
        this.mParams[4] = Math.max(0.0f, Math.min(f, 1.0f));
        this.mParams[5] = Math.max(0.0f, Math.min(f2, 1.0f));
        this.mParams[6] = Math.max(0.0f, Math.min(f3, 1.0f));
        this.mParams[7] = Math.max(0.0f, Math.min(f4, 1.0f));
        setFilterParamsChanged();
        notifyWorkerFilters();
    }

    public void setMaxColor(int i) {
        setMaxColor(((float) Color.red(i)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.alpha(i)) / 255.0f);
    }

    public void setMinColor(float f, float f2, float f3, float f4) {
        this.mParams[0] = Math.max(0.0f, Math.min(f, 1.0f));
        this.mParams[1] = Math.max(0.0f, Math.min(f2, 1.0f));
        this.mParams[2] = Math.max(0.0f, Math.min(f3, 1.0f));
        this.mParams[3] = Math.max(0.0f, Math.min(f4, 1.0f));
        setFilterParamsChanged();
        notifyWorkerFilters();
    }

    public void setMinColor(int i) {
        setMinColor(((float) Color.red(i)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.alpha(i)) / 255.0f);
    }
}
