package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class SemDirectionalBlurFilter extends SemGenericImageFilter {
    private static final int ANGLE = 3;
    private static final float MAX_RADIUS = 250.0f;
    private static final int RADIUS = 0;
    private static final int STEP = 1;
    private static final int STEP_COUNT = 2;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaW;\n\nvoid main(void) {\n    float widthCoef = 1.0 / areaW;\n    float angle = filterParams[3];\n    highp vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n    float step = filterParams[1];\n    float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n        scaledStep = step * filterData02[i] * widthCoef;\n        texPos.s = outTexCoords.s + (scaledStep * cos(angle)); \n        texPos.t = outTexCoords.t + (scaledStep * sin(angle)); \n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n        texPos.s = outTexCoords.s - (scaledStep * cos(angle));\n        texPos.t = outTexCoords.t - (scaledStep * sin(angle));\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n    gl_FragColor = fragColorBlur;\n}\n";
    private float mAngle = 45.0f;
    private float mQuality = 0.6f;
    private float mRadius = 0.0f;

    public SemDirectionalBlurFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
        useFilterData01();
        useFilterData02();
    }

    public int[] animateAngle(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemDirectionalBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDirectionalBlurFilter animateAngle aFraction = " + f);
                }
                SemDirectionalBlurFilter.this.mAngle = ((f2 - f) * f) + f;
                if (SemDirectionalBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDirectionalBlurFilter animateAngle mAngle = " + SemDirectionalBlurFilter.this.mAngle);
                }
                this.params[0] = (float) Math.toRadians((double) SemDirectionalBlurFilter.this.mAngle);
                imageFilterAnimator.setUniformf("filterParams", this.params, 3);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] data = new float[128];
            private float[] data1 = new float[64];
            private float[] data2 = new float[64];
            private float[] params = new float[3];

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                float -get2;
                if (SemDirectionalBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDirectionalBlurFilter animateRadius aFraction = " + f);
                }
                SemDirectionalBlurFilter.this.mRadius = ((f2 - f) * f) + f;
                if (SemDirectionalBlurFilter.this.mRadius > SemDirectionalBlurFilter.MAX_RADIUS) {
                    SemDirectionalBlurFilter.this.mRadius = SemDirectionalBlurFilter.MAX_RADIUS;
                } else if (SemDirectionalBlurFilter.this.mRadius < 0.0f) {
                    SemDirectionalBlurFilter.this.mRadius = 0.0f;
                }
                int -get22 = (int) (SemDirectionalBlurFilter.this.mRadius * SemDirectionalBlurFilter.this.mQuality);
                if (-get22 > 64) {
                    -get2 = SemDirectionalBlurFilter.this.mRadius / ((float) -get22);
                    -get22 = 64;
                } else if (-get22 < 1) {
                    -get2 = 1.0f;
                    -get22 = 1;
                } else {
                    -get2 = 1.67777f;
                }
                if (SemDirectionalBlurFilter.this.mRadius <= 0.0f) {
                    this.data1[0] = 1.0f;
                    this.data2[0] = 0.0f;
                    return;
                }
                int i;
                float -get23 = (SemDirectionalBlurFilter.this.mRadius * 0.3f) + 0.6f;
                float sqrt = (float) (1.0d / (Math.sqrt(6.283185307179586d) * ((double) -get23)));
                float f2 = -1.0f / ((SprDocument.DEFAULT_DENSITY_SCALE * -get23) * -get23);
                float f3 = 0.0f;
                float[] fArr = new float[128];
                float -get24 = SemDirectionalBlurFilter.this.mRadius / ((float) -get22);
                float f4 = 0.0f;
                for (i = 1; i < -get22; i++) {
                    fArr[i] = (float) ((((double) sqrt) * Math.pow(2.718281828459045d, (double) ((f4 * f4) * f2))) * ((double) -get2));
                    f3 += fArr[i];
                    f4 += -get24;
                }
                fArr[0] = sqrt * -get2;
                f3 = (f3 * SprDocument.DEFAULT_DENSITY_SCALE) + fArr[0];
                for (i = 0; i < -get22; i++) {
                    fArr[i] = fArr[i] / f3;
                }
                f4 = 0.0f;
                for (i = 0; i < -get22; i++) {
                    this.data1[i] = fArr[i];
                    this.data2[i] = f4;
                    f4 += -get24;
                }
                if (SemDirectionalBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDirectionalBlurFilter animateRadius mRadius = " + SemDirectionalBlurFilter.this.mRadius);
                    Log.e("HWUIIF", "SemDirectionalBlurFilter animateRadius interpalationStep = " + -get24);
                    Log.e("HWUIIF", "SemDirectionalBlurFilter animateRadius interFactor = " + -get22);
                }
                this.params[0] = SemDirectionalBlurFilter.this.mRadius;
                this.params[1] = -get24;
                this.params[2] = (float) -get22;
                System.arraycopy(this.params, 0, SemDirectionalBlurFilter.this.mParams, 0, this.params.length);
                System.arraycopy(this.data1, 0, SemDirectionalBlurFilter.this.mData1, 0, this.data1.length);
                System.arraycopy(this.data2, 0, SemDirectionalBlurFilter.this.mData2, 0, this.data2.length);
                ImageFilterAnimator imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterParams", this.params, 0);
                imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterData01", this.data1, 0);
                imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterData02", this.data2, 0);
            }
        }, j, j2, timeInterpolator);
    }

    protected void computeGaussCoefs() {
        float f;
        int i = (int) (this.mRadius * this.mQuality);
        if (i > 64) {
            f = this.mRadius / ((float) i);
            i = 64;
        } else if (i < 1) {
            f = 1.0f;
            i = 1;
        } else {
            f = 1.67777f;
        }
        if (this.mRadius <= 0.0f) {
            this.mData1[0] = 1.0f;
            this.mData2[0] = 0.0f;
            return;
        }
        int i2;
        float f2 = (this.mRadius * 0.3f) + 0.6f;
        float sqrt = (float) (1.0d / (Math.sqrt(6.283185307179586d) * ((double) f2)));
        float f3 = -1.0f / ((SprDocument.DEFAULT_DENSITY_SCALE * f2) * f2);
        float f4 = 0.0f;
        float[] fArr = new float[128];
        float f5 = this.mRadius / ((float) i);
        float f6 = 0.0f;
        for (i2 = 1; i2 < i; i2++) {
            fArr[i2] = (float) ((((double) sqrt) * Math.pow(2.718281828459045d, (double) ((f6 * f6) * f3))) * ((double) f));
            f4 += fArr[i2];
            f6 += f5;
        }
        fArr[0] = sqrt * f;
        f4 = (f4 * SprDocument.DEFAULT_DENSITY_SCALE) + fArr[0];
        for (i2 = 0; i2 < i; i2++) {
            fArr[i2] = fArr[i2] / f4;
        }
        f6 = 0.0f;
        for (i2 = 0; i2 < i; i2++) {
            this.mData1[i2] = fArr[i2];
            this.mData2[i2] = f6;
            f6 += f5;
        }
        setParam(0, this.mRadius);
        setParam(1, f5);
        setParam(2, (float) i);
        setParam(3, (float) Math.toRadians((double) this.mAngle));
        setFilterData01Changed();
        setFilterData02Changed();
    }

    public void setAngle(float f) {
        super.setValue(1, f);
        this.mAngle = f;
        computeGaussCoefs();
        notifyWorkerFilters();
    }

    public void setDistance(float f) {
    }

    public void setRadius(float f) {
        if (this.mRadius != f) {
            if (this.mRadius > MAX_RADIUS) {
                this.mRadius = MAX_RADIUS;
            } else if (this.mRadius < 0.0f) {
                this.mRadius = 0.0f;
            } else {
                this.mRadius = f;
            }
            computeGaussCoefs();
            notifyWorkerFilters();
        }
    }
}
