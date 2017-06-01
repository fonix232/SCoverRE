package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;
import com.samsung.android.graphics.spr.document.SprDocument;

public class SemGradientGaussianBlurFilter extends SemGenericImageFilter {
    private static final int ANGLE = 10;
    private static final int BOTTOM_Y = 5;
    private static final float MAX_RADIUS = 250.0f;
    private static final int MIN_RADIUS = 3;
    private static final int RADIUS = 0;
    private static final int STEP = 1;
    private static final int STEP_COUNT = 2;
    private static final int TOP_Y = 4;
    private static String[] mFragmentShaderCode = new String[]{"#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaW;\nuniform float areaH;\n\nvoid main(void) {\n    vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    float point1 = filterParams[4] / areaH;\n    float point2 = filterParams[5] / areaH;\n    float UpperRegionSize = point1;\n    float BottomRegionSize = 1.0 - point2;\n    float kx = filterParams[10] * outTexCoords.x - outTexCoords.y;\n    float distanceToUpperLine = (kx + point1) / UpperRegionSize;\n    float distanceToBottomLine = (kx + point2) / BottomRegionSize;\n    float currentGradientStep = mix(0.0, min(abs(distanceToUpperLine), abs(distanceToBottomLine)), sign(distanceToUpperLine * distanceToBottomLine));\n    float kernelCoefSum = filterData01[0];\n    vec2 texPos = vec2(outTexCoords);\n    float step = 1.0 / areaW ;\n    float scaledStep = 0.0;\n    int stepCount = int((filterParams[2] - filterParams[3]) * currentGradientStep + filterParams[3]);\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < stepCount; i++){\n    scaledStep = step * filterData02[i];\n    texPos.s = outTexCoords.s + scaledStep;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    texPos.s = outTexCoords.s - scaledStep;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    kernelCoefSum += 2.0 * filterData01[i];\n    }\n    fragColorBlur *= 1.0 / kernelCoefSum;\n    gl_FragColor = fragColorBlur;\n}\n\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaH;\n\nvoid main(void) {\n    vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    float point1 = filterParams[4] / areaH;\n    float point2 = filterParams[5] / areaH;\n    float UpperRegionSize = point1;\n    float BottomRegionSize = 1.0 - point2;\n    float kx = filterParams[10] * outTexCoords.x - outTexCoords.y;\n    float distanceToUpperLine = (kx + point1) / UpperRegionSize;\n    float distanceToBottomLine = (kx + point2) / BottomRegionSize;\n    float currentGradientStep = mix(0.0, min(abs(distanceToUpperLine), abs(distanceToBottomLine)), sign(distanceToUpperLine * distanceToBottomLine));\n    float kernelCoefSum = filterData01[0];\n    vec2 texPos = vec2(outTexCoords);\n    float step = 1.0 / areaH;\n    float scaledStep = 0.0;\n    int stepCount = int((filterParams[2] - filterParams[3]) * currentGradientStep + filterParams[3]);\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < stepCount; i++){\n    scaledStep = step * filterData02[i];\n    texPos.t = outTexCoords.t + scaledStep; \n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    texPos.t = outTexCoords.t - scaledStep;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    kernelCoefSum += 2.0 * filterData01[i];\n    }\n    fragColorBlur *= 1.0 / kernelCoefSum;\n    gl_FragColor = fragColorBlur;\n}\n\n"};
    private float mAngle = 0.0f;
    private float mBottomY = 0.0f;
    private float mMaxRadius = 1.0f;
    private float mMinRadius = 1.0f;
    private float mQuality = 0.6f;
    private float mTopY = 0.0f;

    public SemGradientGaussianBlurFilter() {
        super(2, new String[]{SemGenericImageFilter.mVertexShaderCodeCommon}, mFragmentShaderCode);
        useFilterParams();
        useFilterData01();
        useFilterData02();
    }

    public int[] animateGradient(float f, float f2, float f3, float f4, long j, long j2, TimeInterpolator timeInterpolator) {
        final float f5 = f2;
        final float f6 = f;
        final float f7 = f4;
        final float f8 = f3;
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[2];

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradient aFraction = " + f);
                }
                SemGradientGaussianBlurFilter.this.mTopY = ((f5 - f6) * f) + f6;
                SemGradientGaussianBlurFilter.this.mBottomY = ((f7 - f8) * f) + f8;
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradient mTopY = " + SemGradientGaussianBlurFilter.this.mTopY);
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradient bottomY = " + SemGradientGaussianBlurFilter.this.mBottomY);
                }
                this.params[0] = SemGradientGaussianBlurFilter.this.mTopY;
                this.params[1] = SemGradientGaussianBlurFilter.this.mBottomY;
                SemGradientGaussianBlurFilter.this.mParams[4] = this.params[0];
                SemGradientGaussianBlurFilter.this.mParams[5] = this.params[1];
                imageFilterAnimator.setUniformf("filterParams", this.params, 4);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientAngle(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradientAngle aFraction = " + f);
                }
                SemGradientGaussianBlurFilter.this.mAngle = -(((f2 - f) * f) + f);
                float tan = (float) Math.tan(Math.toRadians((double) SemGradientGaussianBlurFilter.this.mAngle));
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradientAngle tanOfAngleInRadians = " + tan);
                }
                this.params[0] = tan;
                SemGradientGaussianBlurFilter.this.mParams[10] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 10);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientBottomY(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradientBottomY aFraction = " + f);
                }
                SemGradientGaussianBlurFilter.this.mBottomY = ((f2 - f) * f) + f;
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradientBottomY mBottomY = " + SemGradientGaussianBlurFilter.this.mBottomY);
                }
                this.params[0] = SemGradientGaussianBlurFilter.this.mBottomY;
                SemGradientGaussianBlurFilter.this.mParams[5] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 5);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientTopY(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradientTopY aFraction = " + f);
                }
                SemGradientGaussianBlurFilter.this.mTopY = ((f2 - f) * f) + f;
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateGradientTopY mTopY = " + SemGradientGaussianBlurFilter.this.mTopY);
                }
                this.params[0] = SemGradientGaussianBlurFilter.this.mTopY;
                SemGradientGaussianBlurFilter.this.mParams[4] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 4);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateMaxRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] data = new float[128];
            private float[] data1 = new float[64];
            private float[] data2 = new float[64];
            private float[] params = new float[3];

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                float -get2;
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateMaxRadius aFraction = " + f);
                }
                SemGradientGaussianBlurFilter.this.mMaxRadius = ((f2 - f) * f) + f;
                if (SemGradientGaussianBlurFilter.this.mMaxRadius < 1.0f) {
                    SemGradientGaussianBlurFilter.this.mMaxRadius = 1.0f;
                } else if (SemGradientGaussianBlurFilter.this.mMaxRadius > SemGradientGaussianBlurFilter.MAX_RADIUS) {
                    SemGradientGaussianBlurFilter.this.mMaxRadius = SemGradientGaussianBlurFilter.MAX_RADIUS;
                }
                int -get22 = (int) (SemGradientGaussianBlurFilter.this.mMaxRadius * 0.6f);
                if (-get22 > 64) {
                    -get2 = SemGradientGaussianBlurFilter.this.mMaxRadius / ((float) -get22);
                    -get22 = 64;
                } else if (-get22 < 1) {
                    -get2 = 1.0f;
                    -get22 = 1;
                } else {
                    -get2 = 1.67777f;
                }
                if (SemGradientGaussianBlurFilter.this.mMaxRadius <= 0.0f) {
                    this.data1[0] = 1.0f;
                    this.data2[0] = 0.0f;
                    return;
                }
                int i;
                float -get23 = (SemGradientGaussianBlurFilter.this.mMaxRadius * 0.3f) + 0.6f;
                float sqrt = (float) (1.0d / (Math.sqrt((double) 6.2831855f) * ((double) -get23)));
                float f2 = -1.0f / ((SprDocument.DEFAULT_DENSITY_SCALE * -get23) * -get23);
                float f3 = 0.0f;
                float -get24 = SemGradientGaussianBlurFilter.this.mMaxRadius / ((float) -get22);
                float f4 = 0.0f;
                for (i = 1; i < -get22; i++) {
                    this.data[i] = (float) ((((double) sqrt) * Math.pow(2.7182817459106445d, (double) ((f4 * f4) * f2))) * ((double) -get2));
                    f3 += this.data[i];
                    f4 += -get24;
                }
                this.data[0] = sqrt * -get2;
                f3 = (f3 * SprDocument.DEFAULT_DENSITY_SCALE) + this.data[0];
                for (i = 0; i < -get22; i++) {
                    float[] fArr = this.data;
                    fArr[i] = fArr[i] / f3;
                }
                f4 = 0.0f;
                for (i = 0; i < -get22; i++) {
                    this.data1[i] = this.data[i];
                    this.data2[i] = f4;
                    f4 += -get24;
                }
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateMaxRadius mMaxRadius = " + SemGradientGaussianBlurFilter.this.mMaxRadius + "; interFactor = " + -get22);
                }
                this.params[0] = SemGradientGaussianBlurFilter.this.mMaxRadius;
                this.params[1] = 1.0f;
                this.params[2] = (float) -get22;
                ImageFilterAnimator imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterParams", this.params, 0);
                imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterData01", this.data1, 0);
                imageFilterAnimator2 = imageFilterAnimator;
                imageFilterAnimator2.setUniformf("filterData02", this.data2, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateMinRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateRadius aFraction = " + f);
                }
                SemGradientGaussianBlurFilter.this.mMinRadius = ((f2 - f) * f) + f;
                if (SemGradientGaussianBlurFilter.this.mMinRadius < 1.0f) {
                    SemGradientGaussianBlurFilter.this.mMinRadius = 1.0f;
                } else if (SemGradientGaussianBlurFilter.this.mMinRadius > SemGradientGaussianBlurFilter.MAX_RADIUS) {
                    SemGradientGaussianBlurFilter.this.mMinRadius = SemGradientGaussianBlurFilter.MAX_RADIUS;
                }
                int -get3 = (int) (SemGradientGaussianBlurFilter.this.mMinRadius * 0.6f);
                if (-get3 > 64) {
                    -get3 = 64;
                } else if (-get3 < 1) {
                    -get3 = 1;
                }
                if (SemGradientGaussianBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemGradientGaussianBlurFilter animateRadius mMinRadius = " + SemGradientGaussianBlurFilter.this.mMinRadius + "; interFactor = " + -get3);
                }
                this.params[0] = (float) -get3;
                SemGradientGaussianBlurFilter.this.mParams[3] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 3);
            }
        }, j, j2, timeInterpolator);
    }

    protected void computeGaussCoefs() {
        float f;
        int i = (int) (this.mMaxRadius * 0.6f);
        if (i > 64) {
            f = this.mMaxRadius / ((float) i);
            i = 64;
        } else if (i < 1) {
            f = 1.0f;
            i = 1;
        } else {
            f = 1.67777f;
        }
        if (this.mMaxRadius <= 0.0f) {
            this.mData1[0] = 1.0f;
            this.mData2[0] = 0.0f;
            return;
        }
        int i2;
        float f2 = (this.mMaxRadius * 0.3f) + 0.6f;
        float sqrt = (float) (1.0d / (Math.sqrt((double) 6.2831855f) * ((double) f2)));
        float f3 = -1.0f / ((SprDocument.DEFAULT_DENSITY_SCALE * f2) * f2);
        float f4 = 0.0f;
        float[] fArr = new float[128];
        float f5 = this.mMaxRadius / ((float) i);
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
        setParam(0, this.mMaxRadius);
        setParam(1, 1.0f);
        setParam(2, (float) i);
        setFilterData01Changed();
        setFilterData02Changed();
    }

    public void setGradient(float f, float f2) {
        this.mTopY = f;
        this.mBottomY = f2;
        setParam(4, this.mTopY);
        setParam(5, this.mBottomY);
        notifyWorkerFilters();
    }

    public void setGradientAngle(float f) {
        super.setValue(1, -f);
        this.mAngle = -f;
        setParam(10, (float) Math.tan(Math.toRadians((double) this.mAngle)));
        notifyWorkerFilters();
    }

    public void setMaxRadius(float f) {
        if (this.mMaxRadius != f) {
            if (this.mMaxRadius > MAX_RADIUS) {
                this.mMaxRadius = MAX_RADIUS;
            } else if (this.mMaxRadius < 1.0f) {
                this.mMaxRadius = 1.0f;
            } else {
                this.mMaxRadius = f;
            }
            setupDownSampling();
            computeGaussCoefs();
            notifyWorkerFilters();
        }
    }

    public void setMinRadius(float f) {
        if (f < 1.0f) {
            this.mMinRadius = 1.0f;
        } else if (this.mMinRadius > MAX_RADIUS) {
            this.mMinRadius = MAX_RADIUS;
        } else {
            this.mMinRadius = f;
        }
        int i = (int) (this.mMinRadius * 0.6f);
        if (i > 64) {
            i = 64;
        } else if (i < 1) {
            i = 1;
        }
        setParam(3, (float) i);
        notifyWorkerFilters();
    }

    protected void setupDownSampling() {
        float sqrt = (float) Math.sqrt((double) this.mMaxRadius);
        if (sqrt < 1.0f) {
            sqrt = 1.0f;
        }
        setSamplingRate(0, sqrt, 1.0f);
        setSamplingRate(1, sqrt, sqrt);
    }
}
