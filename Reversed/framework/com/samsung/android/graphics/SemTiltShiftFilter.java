package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.IImageFilterListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemTiltShiftFilter extends SemGenericImageFilter implements IImageFilterListener {
    private static final int BOTTOM_Y = 5;
    private static final int BRIGHTNESS = 8;
    private static final int CONTRAST = 6;
    private static final int GRADIENT_ANGLE = 10;
    private static final int GRADIENT_RATE = 9;
    private static final int MAX_PARAMS = 16;
    private static final int SATURATION = 7;
    private static final int TOP_Y = 4;
    private static final String mFragmentShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D originalSampler;\nuniform float filterParams[16];\nuniform float areaH;\n\nvec3 ContrastSaturationBrightness(vec3 color, float brt, float sat, float con)\n{\n   float AvgLumR = 0.5;\n   float AvgLumG = 0.5;\n   float AvgLumB = 0.5;\n\n   vec3 LumCoeff = vec3(0.2125, 0.7154, 0.0721);\n\n   vec3 AvgLumin = vec3(AvgLumR, AvgLumG, AvgLumB);\n   vec3 brtColor = color * brt;\n   vec3 intensity = vec3(dot(brtColor, LumCoeff));\n   vec3 satColor = mix(intensity, brtColor, sat);\n   vec3 conColor = mix(AvgLumin, satColor, con);\n   return conColor;\n}\n\nvoid main(void) {\n    vec4 colorBlur = texture2D(baseSampler, outTexCoords);\n    vec4 originalColor = texture2D(originalSampler, outTexCoords);\n    float point1 = filterParams[4] / areaH;\n    float point2 = filterParams[5] / areaH;\n    float kx = filterParams[10] * outTexCoords.x - outTexCoords.y;\n    float k1 = (kx + point1) * filterParams[9];\n    float k2 = (kx + point2) * filterParams[9];\n    float s = abs(point1 - point2);\n    float mixCoef = clamp(clamp(1.0 - min(abs(k1), abs(k2)) / (s * 0.5), 0.0, 1.0) * 1.1 + clamp(sign(k1 * k2), 0.0, 1.0), 0.0, 1.0);\n    vec4 color = mix(originalColor, colorBlur, mixCoef);\n    color.rgb = ContrastSaturationBrightness(color.rgb, filterParams[8], filterParams[7], filterParams[6]);\n    gl_FragColor = color;\n}\n\n";
    private static final String mVertexShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nattribute vec2 texCoords;\nattribute vec4 position;\nvarying vec2 outTexCoords;\nuniform mat4 projection;\nuniform float filterParams[16];\nuniform float areaH;\nvoid main() {\n   outTexCoords = texCoords;\n   gl_Position = projection * position;\n}\n";
    private SemGenericImageFilter mBlurFilter = null;
    private float mBottomY = 0.0f;
    private float mBrightness = 1.0f;
    private float mContrast = 1.0f;
    private float mGradientAngle = 0.0f;
    private float mGradientRate = 1.0f;
    private float mSaturation = 1.5f;
    private float mTopY = 0.0f;

    public SemTiltShiftFilter() {
        super(mVertexShader, mFragmentShader);
        useFilterParams();
        useFilterData01();
        useFilterData02();
        setParam(6, this.mContrast);
        setParam(7, this.mSaturation);
        setParam(8, this.mBrightness);
        setParam(9, this.mGradientRate);
    }

    private void buildFilters() {
        super.clearFilters();
        if (this.mBlurFilter != null) {
            for (int i = 0; i < this.mBlurFilter.getFilterCount(); i++) {
                addFilter(this.mBlurFilter.getFilterAt(i));
            }
        }
        addFilter(SemImageFilter.createCustomFilter(mVertexShader, mFragmentShader));
    }

    private void copyBlurShaders() {
        int filterCount = this.mBlurFilter.getFilterCount() + 1;
        String[] strArr = new String[filterCount];
        String[] strArr2 = new String[filterCount];
        for (int i = 0; i < filterCount - 1; i++) {
            strArr[i] = this.mBlurFilter.getVertexShaderCode(i);
            strArr2[i] = this.mBlurFilter.getFragmentShaderCode(i);
        }
        strArr[filterCount - 1] = mVertexShader;
        strArr2[filterCount - 1] = mFragmentShader;
        super.setup(filterCount, strArr, strArr2);
    }

    private float[] copyParams() {
        float[] fArr = new float[16];
        for (int i = 0; i < 16; i++) {
            float param = this.mBlurFilter.getParam(i);
            float param2 = getParam(i);
            if (param != 0.0f) {
                param2 = param;
            }
            fArr[i] = param2;
        }
        return fArr;
    }

    private void setFiltersParams(float[] fArr) {
        for (int i = 0; i < 16; i++) {
            setParam(i, fArr[i]);
        }
        notifyWorkerFilters();
    }

    public int[] animateBrightness(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateBrightness aFraction = " + f);
                }
                SemTiltShiftFilter.this.mBrightness = ((f2 - f) * f) + f;
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateBrightness mBrightness = " + SemTiltShiftFilter.this.mBrightness);
                }
                this.params[0] = SemTiltShiftFilter.this.mBrightness;
                SemTiltShiftFilter.this.mParams[8] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 8);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateContrast(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateContrast aFraction = " + f);
                }
                SemTiltShiftFilter.this.mContrast = ((f2 - f) * f) + f;
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateContrast mContrast = " + SemTiltShiftFilter.this.mContrast);
                }
                this.params[0] = SemTiltShiftFilter.this.mContrast;
                SemTiltShiftFilter.this.mParams[6] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 6);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradient(float f, float f2, float f3, float f4, long j, long j2, TimeInterpolator timeInterpolator) {
        final float f5 = f2;
        final float f6 = f;
        final float f7 = f4;
        final float f8 = f3;
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[2];

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradient aFraction = " + f);
                }
                SemTiltShiftFilter.this.mTopY = ((f5 - f6) * f) + f6;
                SemTiltShiftFilter.this.mBottomY = ((f7 - f8) * f) + f8;
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradient mTopY = " + SemTiltShiftFilter.this.mTopY);
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradient mBottomY = " + SemTiltShiftFilter.this.mBottomY);
                }
                this.params[0] = SemTiltShiftFilter.this.mTopY;
                this.params[1] = SemTiltShiftFilter.this.mBottomY;
                SemTiltShiftFilter.this.mParams[4] = this.params[0];
                SemTiltShiftFilter.this.mParams[5] = this.params[1];
                imageFilterAnimator.setUniformf("filterParams", this.params, 4);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientAngle(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientAngle aFraction = " + f);
                }
                SemTiltShiftFilter.this.mGradientAngle = -(((f2 - f) * f) + f);
                float tan = (float) Math.tan(Math.toRadians((double) SemTiltShiftFilter.this.mGradientAngle));
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientAngle tanOfAngleInRadians = " + tan);
                }
                this.params[0] = tan;
                SemTiltShiftFilter.this.mParams[10] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 10);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientBottomY(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientBottomY aFraction = " + f);
                }
                SemTiltShiftFilter.this.mBottomY = ((f2 - f) * f) + f;
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientBottomY mBottomY = " + SemTiltShiftFilter.this.mBottomY);
                }
                this.params[0] = SemTiltShiftFilter.this.mBottomY;
                SemTiltShiftFilter.this.mParams[5] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 5);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientRate(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientRate aFraction = " + f);
                }
                SemTiltShiftFilter.this.mGradientRate = ((f2 - f) * f) + f;
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientRate mGradientRate = " + SemTiltShiftFilter.this.mGradientRate);
                }
                this.params[0] = SemTiltShiftFilter.this.mGradientRate;
                SemTiltShiftFilter.this.mParams[9] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 9);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientTopY(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientTopY aFraction = " + f);
                }
                SemTiltShiftFilter.this.mTopY = ((f2 - f) * f) + f;
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateGradientTopY mTopY = " + SemTiltShiftFilter.this.mTopY);
                }
                this.params[0] = SemTiltShiftFilter.this.mTopY;
                SemTiltShiftFilter.this.mParams[4] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 4);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateSaturation(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateSaturation aFraction = " + f);
                }
                SemTiltShiftFilter.this.mSaturation = ((f2 - f) * f) + f;
                if (SemTiltShiftFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemTiltShiftFilter animateSaturation mSaturation = " + SemTiltShiftFilter.this.mSaturation);
                }
                this.params[0] = SemTiltShiftFilter.this.mSaturation;
                SemTiltShiftFilter.this.mParams[7] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 7);
            }
        }, j, j2, timeInterpolator);
    }

    public void onAttachedToView() {
    }

    public void onParamsChanged() {
        if (this.mBlurFilter != null) {
            setFiltersParams(copyParams());
        }
    }

    public void onViewSizeChanged() {
    }

    public void setBlurFilter(SemGenericImageFilter semGenericImageFilter) {
        this.mBlurFilter = semGenericImageFilter;
        if (this.mBlurFilter != null) {
            this.mBlurFilter.setListener(this);
            float[] copyParams = copyParams();
            copyBlurShaders();
            buildFilters();
            setFiltersParams(copyParams);
        }
    }

    public void setBrightness(float f) {
        this.mBrightness = f;
        setParam(8, this.mBrightness);
        notifyWorkerFilters();
    }

    public void setContrast(float f) {
        this.mContrast = f;
        setParam(6, this.mContrast);
        notifyWorkerFilters();
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
        this.mGradientAngle = -f;
        setParam(10, (float) Math.tan(Math.toRadians((double) this.mGradientAngle)));
        notifyWorkerFilters();
    }

    public void setGradientRate(float f) {
        this.mGradientRate = f;
        setParam(9, this.mGradientRate);
        notifyWorkerFilters();
    }

    public void setSaturation(float f) {
        this.mSaturation = f;
        setParam(7, this.mSaturation);
        notifyWorkerFilters();
    }
}
