package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class SemGaussianBlurFilter extends SemGenericImageFilter {
    private static final float MAX_RADIUS = 250.0f;
    private static final int RADIUS = 0;
    private static final int STEP = 1;
    private static final int STEP_COUNT = 2;
    private static String[] mFragmentShaderCode = new String[]{"#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaW;\n\nvoid main(void) {\n   vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n   float step = 1.0 / areaW ;\n   float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n      scaledStep = step * filterData02[i];\n         texPos.s = outTexCoords.s + scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n       texPos.s = outTexCoords.s - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n   gl_FragColor = fragColorBlur;\n}\n\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaH;\n\nvoid main(void) {\n  vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n   float step = 1.0 / areaH;\n    float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n      scaledStep = step * filterData02[i];\n         texPos.t = outTexCoords.t + scaledStep; \n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n      texPos.t = outTexCoords.t - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n   gl_FragColor = fragColorBlur;\n}\n\n"};
    private float mQuality = 0.6f;
    private float mRadius = 0.0f;

    public SemGaussianBlurFilter() {
        super(2, new String[]{SemGenericImageFilter.mVertexShaderCodeCommon}, mFragmentShaderCode);
        useFilterParams();
        useFilterData01();
        useFilterData02();
    }

    public int[] animateRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] data = new float[128];
            private float[] data1 = new float[64];
            private float[] data2 = new float[64];
            private float[] params = new float[3];

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                float -get0;
                if (SemGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGaussianBlurFilter animateRadius aFraction = " + f);
                }
                SemGaussianBlurFilter.this.mRadius = ((f2 - f) * f) + f;
                if (SemGaussianBlurFilter.this.mRadius > SemGaussianBlurFilter.MAX_RADIUS) {
                    SemGaussianBlurFilter.this.mRadius = SemGaussianBlurFilter.MAX_RADIUS;
                } else if (SemGaussianBlurFilter.this.mRadius < 0.0f) {
                    SemGaussianBlurFilter.this.mRadius = 0.0f;
                }
                int -get02 = (int) (SemGaussianBlurFilter.this.mRadius * 0.6f);
                if (-get02 > 64) {
                    -get0 = SemGaussianBlurFilter.this.mRadius / ((float) -get02);
                    -get02 = 64;
                } else if (-get02 < 1) {
                    -get0 = 1.0f;
                    -get02 = 1;
                } else {
                    -get0 = 1.67777f;
                }
                if (SemGaussianBlurFilter.this.mRadius <= 0.0f) {
                    this.data1[0] = 1.0f;
                    this.data2[0] = 0.0f;
                    return;
                }
                int i;
                float -get03 = (SemGaussianBlurFilter.this.mRadius * 0.3f) + 0.6f;
                float sqrt = (float) (1.0d / (Math.sqrt((double) 6.2831855f) * ((double) -get03)));
                float f2 = -1.0f / ((SprDocument.DEFAULT_DENSITY_SCALE * -get03) * -get03);
                float f3 = 0.0f;
                float -get04 = SemGaussianBlurFilter.this.mRadius / ((float) -get02);
                float f4 = 0.0f;
                for (i = 1; i < -get02; i++) {
                    this.data[i] = (float) ((((double) sqrt) * Math.pow(2.7182817459106445d, (double) ((f4 * f4) * f2))) * ((double) -get0));
                    f3 += this.data[i];
                    f4 += -get04;
                }
                this.data[0] = sqrt * -get0;
                f3 = (f3 * SprDocument.DEFAULT_DENSITY_SCALE) + this.data[0];
                for (i = 0; i < -get02; i++) {
                    float[] fArr = this.data;
                    fArr[i] = fArr[i] / f3;
                }
                f4 = 0.0f;
                for (i = 0; i < -get02; i++) {
                    this.data1[i] = this.data[i];
                    this.data2[i] = f4;
                    f4 += -get04;
                }
                if (SemGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGaussianBlurFilter animateRadius mRadius = " + SemGaussianBlurFilter.this.mRadius + "; interFactor = " + -get02);
                }
                this.params[0] = SemGaussianBlurFilter.this.mRadius;
                this.params[1] = 1.0f;
                this.params[2] = (float) -get02;
                System.arraycopy(this.params, 0, SemGaussianBlurFilter.this.mParams, 0, this.params.length);
                System.arraycopy(this.data1, 0, SemGaussianBlurFilter.this.mData1, 0, this.data1.length);
                System.arraycopy(this.data2, 0, SemGaussianBlurFilter.this.mData2, 0, this.data2.length);
                ImageFilterAnimator imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterParams", this.params, 0);
                imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterData01", this.data1, 0);
                imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterData02", this.data2, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public SemGaussianBlurFilter clone() throws CloneNotSupportedException {
        SemGaussianBlurFilter semGaussianBlurFilter = (SemGaussianBlurFilter) super.clone();
        semGaussianBlurFilter.mRadius = this.mRadius;
        return semGaussianBlurFilter;
    }

    protected void computeGaussCoefs() {
        float f;
        int i = (int) (this.mRadius * 0.6f);
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
        float sqrt = (float) (1.0d / (Math.sqrt((double) 6.2831855f) * ((double) f2)));
        float f3 = -1.0f / ((SprDocument.DEFAULT_DENSITY_SCALE * f2) * f2);
        float f4 = 0.0f;
        float[] fArr = new float[128];
        float f5 = this.mRadius / ((float) i);
        float f6 = 0.0f;
        for (i2 = 1; i2 < i; i2++) {
            fArr[i2] = (float) ((((double) sqrt) * Math.pow(2.7182817459106445d, (double) ((f6 * f6) * f3))) * ((double) f));
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
        setParam(1, 1.0f);
        setParam(2, (float) i);
        setFilterData01Changed();
        setFilterData02Changed();
    }

    public void setRadius(float f) {
        if (this.mRadius != f) {
            this.mRadius = Math.max(0.0f, Math.min(f, MAX_RADIUS));
            setupDownSampling();
            computeGaussCoefs();
            notifyWorkerFilters();
        }
    }

    protected void setupDownSampling() {
        float sqrt = (float) Math.sqrt((double) this.mRadius);
        if (sqrt < 1.0f) {
            sqrt = 1.0f;
        }
        setSamplingRate(0, sqrt, 1.0f);
        setSamplingRate(1, sqrt, sqrt);
    }
}
