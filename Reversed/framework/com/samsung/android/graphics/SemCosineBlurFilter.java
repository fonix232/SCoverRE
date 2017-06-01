package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class SemCosineBlurFilter extends SemGenericImageFilter {
    private static final float MAX_RADIUS = 250.0f;
    private static final int RADIUS = 0;
    private static final int STEP_COUNT = 2;
    private static String[] mFragmentShaderCode = new String[]{"#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaW;\n\nvoid main(void) {\n    highp vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n    float step = 1.0 / areaW ;\n   float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n      scaledStep = step * filterData02[i];\n         texPos.s = outTexCoords.s + scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n       texPos.s = outTexCoords.s - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n   gl_FragColor = fragColorBlur;\n}\n\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaH;\n\nvoid main(void) {\n  highp vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n     float step = 1.0 / areaH;\n    float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n      scaledStep = step * filterData02[i];\n         texPos.t = outTexCoords.t + scaledStep; \n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n      texPos.t = outTexCoords.t - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n   gl_FragColor = fragColorBlur;\n}\n\n"};
    private float mQuality = 0.6f;
    private float mRadius = 0.0f;

    public SemCosineBlurFilter() {
        super(2, new String[]{SemGenericImageFilter.mVertexShaderCodeCommon}, mFragmentShaderCode);
        useFilterData01();
        useFilterData02();
    }

    public int[] animateRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] data = new float[128];
            private float[] data1 = new float[64];
            private float[] data2 = new float[64];
            private float[] params1 = new float[]{0.0f};
            private float[] params2 = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemCosineBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemCosineBlurFilter animateRadius aFraction = " + f);
                }
                SemCosineBlurFilter.this.mRadius = ((f2 - f) * f) + f;
                if (SemCosineBlurFilter.this.mRadius > SemCosineBlurFilter.MAX_RADIUS) {
                    SemCosineBlurFilter.this.mRadius = SemCosineBlurFilter.MAX_RADIUS;
                } else if (SemCosineBlurFilter.this.mRadius < 0.0f) {
                    SemCosineBlurFilter.this.mRadius = 0.0f;
                }
                if (SemCosineBlurFilter.this.mRadius > 60.0f) {
                    SemCosineBlurFilter.this.mRadius = 60.0f;
                }
                if (SemCosineBlurFilter.this.mRadius <= 0.0f) {
                    this.data1[0] = 1.0f;
                    this.data2[0] = 0.0f;
                    return;
                }
                int i;
                float -get0 = 0.5f / SemCosineBlurFilter.this.mRadius;
                float -get02 = 3.1415927f / SemCosineBlurFilter.this.mRadius;
                float f2 = 0.0f;
                float[] fArr = new float[128];
                for (i = 0; ((float) i) <= SemCosineBlurFilter.this.mRadius; i++) {
                    fArr[i] = ((float) (((double) -get0) * Math.cos((double) (-get02 * ((float) i))))) + -get0;
                    if (i > 0) {
                        f2 += fArr[i];
                    }
                }
                f2 = (f2 * SprDocument.DEFAULT_DENSITY_SCALE) + fArr[0];
                for (i = 0; ((float) i) <= SemCosineBlurFilter.this.mRadius; i++) {
                    fArr[i] = fArr[i] / f2;
                }
                for (i = 0; ((float) i) <= SemCosineBlurFilter.this.mRadius; i++) {
                    this.data1[i] = fArr[i];
                    this.data2[i] = (float) i;
                }
                if (SemCosineBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemCosineBlurFilter animateRadius mRadius = " + SemCosineBlurFilter.this.mRadius);
                    Log.e("HWUIIF", "SemCosineBlurFilter animateRadius STEP_COUNT = " + SemCosineBlurFilter.this.mRadius + 1);
                }
                this.params1[0] = SemCosineBlurFilter.this.mRadius;
                this.params2[0] = SemCosineBlurFilter.this.mRadius + 1.0f;
                SemCosineBlurFilter.this.mParams[0] = this.params1[0];
                SemCosineBlurFilter.this.mParams[2] = this.params2[0];
                System.arraycopy(this.data1, 0, SemCosineBlurFilter.this.mData1, 0, this.data1.length);
                System.arraycopy(this.data2, 0, SemCosineBlurFilter.this.mData2, 0, this.data2.length);
                imageFilterAnimator.setUniformf("filterParams", this.params1, 0);
                imageFilterAnimator.setUniformf("filterParams", this.params2, 2);
                imageFilterAnimator.setUniformf("filterData01", this.data1, 0);
                imageFilterAnimator.setUniformf("filterData02", this.data2, 0);
            }
        }, j, j2, timeInterpolator);
    }

    protected void computeCosineCoefs() {
        if (this.mRadius > 60.0f) {
            this.mRadius = 60.0f;
        }
        if (this.mRadius <= 0.0f) {
            this.mData1[0] = 1.0f;
            this.mData2[0] = 0.0f;
            return;
        }
        int i;
        float f = 0.5f / this.mRadius;
        float f2 = 3.1415927f / this.mRadius;
        float f3 = 0.0f;
        float[] fArr = new float[128];
        for (i = 0; ((float) i) <= this.mRadius; i++) {
            fArr[i] = ((float) (((double) f) * Math.cos((double) (f2 * ((float) i))))) + f;
            if (i > 0) {
                f3 += fArr[i];
            }
        }
        f3 = (f3 * SprDocument.DEFAULT_DENSITY_SCALE) + fArr[0];
        for (i = 0; ((float) i) <= this.mRadius; i++) {
            fArr[i] = fArr[i] / f3;
        }
        for (i = 0; ((float) i) <= this.mRadius; i++) {
            this.mData1[i] = fArr[i];
            this.mData2[i] = (float) i;
        }
        setParam(0, this.mRadius);
        setParam(2, this.mRadius + 1.0f);
        setFilterData01Changed();
        setFilterData02Changed();
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
            computeCosineCoefs();
            setupDownSampling();
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
