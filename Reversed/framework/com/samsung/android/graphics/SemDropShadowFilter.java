package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemDropShadowFilter extends SemGenericImageFilter {
    private static final int ALPHA = 6;
    private static final int BLUE = 5;
    private static final int CYCLES_COUNT = 7;
    private static final int DIRECTION_X = 1;
    private static final int DIRECTION_Y = 2;
    private static final int DISTANCE = 0;
    private static final int GREEN = 4;
    private static final int RED = 3;
    private static final int SHORTDISTANCE = 8;
    private static String[] mFragmentShaderCode = new String[]{"precision highp float;\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float areaW;\nuniform float areaH;\nvoid main(void) {\n    vec2 uv = vec2(outTexCoords);\n    float c = areaW / areaH;\n    vec2 vDirection = vec2(filterParams[1] / areaW, filterParams[2] / areaH);\n    if(areaW>areaH) vDirection.y*=c; else vDirection.x/=c; \n    vec4 savedColor = texture2D(baseSampler, uv);\n    if (savedColor.a != 1.0) {\n        float ccl = filterParams[7];\n        for (float i = 0.0; i < ccl; i += 1.0) {\n                uv -= vDirection;\n                if (uv.x<0.0 || uv.x>1.0 || uv.y<0.0 || uv.y>1.0)\n                   i = ccl;\n                if (texture2D(baseSampler, uv).a == 1.0) {                   savedColor = savedColor + vec4(filterParams[3], filterParams[4], filterParams[5], filterParams[6]) * (1.0 - savedColor.a) * (1.0-i/ccl);\n                   i = ccl;\n                }\n        }\n    }\n    gl_FragColor = savedColor;\n}\n\n"};
    private float mAngle = -10.0f;
    private float mDistance = 5.0f;
    private float mQuality = 9.0f;

    public SemDropShadowFilter() {
        super(1, new String[]{SemGenericImageFilter.mVertexShaderCodeCommon}, mFragmentShaderCode);
        setDistance(20.0f);
        setAngle(-10.0f);
        setShadowColor(1.0f, 0.5f, 0.5f, 1.0f);
        setQuality(15.0f);
        ((SemCustomFilter) getFilterAt(0)).setValue(4, 1.0f);
        ((SemCustomFilter) getFilterAt(0)).setValue(5, 1.0f);
        ((SemCustomFilter) getFilterAt(0)).setValue(6, 7.0f);
        preSetupShader();
    }

    public int[] animateAngle(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params1 = new float[2];
            private float[] params2 = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemDropShadowFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDropShadowFilter animateAngle aFraction = " + f);
                }
                SemDropShadowFilter.this.mAngle = ((f2 - f) * f) + f;
                float -get0 = (SemDropShadowFilter.this.mAngle / 180.0f) * 3.141592f;
                float -get1 = SemDropShadowFilter.this.mDistance / SemDropShadowFilter.this.mQuality;
                float cos = ((float) Math.cos((double) -get0)) * -get1;
                float sin = ((float) Math.sin((double) -get0)) * -get1;
                if (SemDropShadowFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDropShadowFilter animateAngle mAngle = " + SemDropShadowFilter.this.mAngle);
                    Log.e("HWUIIF", "SemDropShadowFilter animateAngle mQuality = " + SemDropShadowFilter.this.mQuality);
                    Log.e("HWUIIF", "SemDropShadowFilter animateAngle xdir = " + cos);
                    Log.e("HWUIIF", "SemDropShadowFilter animateAngle ydir = " + sin);
                }
                this.params1[0] = cos;
                this.params1[1] = sin;
                SemDropShadowFilter.this.mParams[1] = this.params1[0];
                SemDropShadowFilter.this.mParams[2] = this.params1[1];
                imageFilterAnimator.setUniformf("filterParams", this.params1, 1);
                this.params2[0] = SemDropShadowFilter.this.mQuality;
                SemDropShadowFilter.this.mParams[7] = this.params2[0];
                imageFilterAnimator.setUniformf("filterParams", this.params2, 7);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateDistance(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params1 = new float[2];
            private float[] params2 = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemDropShadowFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDropShadowFilter animateDistance aFraction = " + f);
                }
                float f2 = ((f2 - f) * f) + (f * 1.5f);
                if (f2 > 0.0f) {
                    SemDropShadowFilter.this.mDistance = f2;
                    float -get0 = (SemDropShadowFilter.this.mAngle / 180.0f) * 3.141592f;
                    float -get1 = SemDropShadowFilter.this.mDistance / SemDropShadowFilter.this.mQuality;
                    float cos = ((float) Math.cos((double) -get0)) * -get1;
                    float sin = ((float) Math.sin((double) -get0)) * -get1;
                    if (SemDropShadowFilter.sLogingEnabled) {
                        Log.e("HWUIIF", "SemDropShadowFilter animateDistance normDistance = " + -get1);
                        Log.e("HWUIIF", "SemDropShadowFilter animateDistance mQuality = " + SemDropShadowFilter.this.mQuality);
                        Log.e("HWUIIF", "SemDropShadowFilter animateDistance xdir = " + cos);
                        Log.e("HWUIIF", "SemDropShadowFilter animateDistance ydir = " + sin);
                    }
                    this.params1[0] = cos;
                    this.params1[1] = sin;
                    SemDropShadowFilter.this.mParams[1] = this.params1[0];
                    SemDropShadowFilter.this.mParams[2] = this.params1[1];
                    imageFilterAnimator.setUniformf("filterParams", this.params1, 1);
                    this.params2[0] = SemDropShadowFilter.this.mQuality;
                    SemDropShadowFilter.this.mParams[7] = this.params2[0];
                    imageFilterAnimator.setUniformf("filterParams", this.params2, 7);
                }
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateQuality(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params1 = new float[2];
            private float[] params2 = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemDropShadowFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDropShadowFilter animateQuality aFraction = " + f);
                }
                SemDropShadowFilter.this.mQuality = (float) ((int) (5.0f + (Math.max(0.0f, Math.min(((f2 - f) * f) + f, 1000.0f)) * 0.4f)));
                float -get0 = (SemDropShadowFilter.this.mAngle / 180.0f) * 3.141592f;
                float -get1 = SemDropShadowFilter.this.mDistance / SemDropShadowFilter.this.mQuality;
                float cos = ((float) Math.cos((double) -get0)) * -get1;
                float sin = ((float) Math.sin((double) -get0)) * -get1;
                if (SemDropShadowFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDropShadowFilter animateQuality mQuality = " + SemDropShadowFilter.this.mQuality);
                    Log.e("HWUIIF", "SemDropShadowFilter animateQuality xdir = " + cos);
                    Log.e("HWUIIF", "SemDropShadowFilter animateQuality ydir = " + sin);
                }
                this.params1[0] = cos;
                this.params1[1] = sin;
                SemDropShadowFilter.this.mParams[1] = this.params1[0];
                SemDropShadowFilter.this.mParams[2] = this.params1[1];
                imageFilterAnimator.setUniformf("filterParams", this.params1, 1);
                this.params2[0] = SemDropShadowFilter.this.mQuality;
                SemDropShadowFilter.this.mParams[7] = this.params2[0];
                imageFilterAnimator.setUniformf("filterParams", this.params2, 7);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateShadowColor(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, long j, long j2, TimeInterpolator timeInterpolator) {
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
                if (SemDropShadowFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDropShadowFilter animateShadowColor aFraction = " + f);
                }
                float f2 = ((f11 - f12) * f) + f12;
                float f3 = ((f13 - f14) * f) + f14;
                float f4 = ((f15 - f16) * f) + f16;
                float max = Math.max(0.0f, Math.min(((f9 - f10) * f) + f10, 1.0f));
                f2 = Math.max(0.0f, Math.min(f2, 1.0f));
                f3 = Math.max(0.0f, Math.min(f3, 1.0f));
                f4 = Math.max(0.0f, Math.min(f4, 1.0f));
                if (SemDropShadowFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDropShadowFilter animateShadowColor redColor = " + max);
                    Log.e("HWUIIF", "SemDropShadowFilter animateShadowColor greenColor = " + f2);
                    Log.e("HWUIIF", "SemDropShadowFilter animateShadowColor blueColor = " + f3);
                    Log.e("HWUIIF", "SemDropShadowFilter animateShadowColor alphaColor = " + f4);
                }
                this.params[0] = max;
                this.params[1] = f2;
                this.params[2] = f3;
                this.params[3] = f4;
                System.arraycopy(this.params, 0, SemDropShadowFilter.this.mParams, 3, this.params.length);
                imageFilterAnimator.setUniformf("filterParams", this.params, 3);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateShadowColor(int i, int i2, long j, long j2, TimeInterpolator timeInterpolator) {
        return animateShadowColor(((float) Color.red(i)) / 255.0f, ((float) Color.red(i2)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.green(i2)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.blue(i2)) / 255.0f, ((float) Color.alpha(i)) / 255.0f, ((float) Color.alpha(i2)) / 255.0f, j, j2, timeInterpolator);
    }

    public SemDropShadowFilter clone() throws CloneNotSupportedException {
        SemDropShadowFilter semDropShadowFilter = (SemDropShadowFilter) super.clone();
        semDropShadowFilter.mAngle = this.mAngle;
        semDropShadowFilter.mDistance = this.mDistance;
        semDropShadowFilter.mQuality = this.mQuality;
        return semDropShadowFilter;
    }

    public float getAngle() {
        return this.mAngle;
    }

    public float getDistance() {
        return this.mDistance;
    }

    public float getQuality() {
        return this.mQuality;
    }

    public float[] getShadowColor() {
        return new float[]{this.mParams[3], this.mParams[4], this.mParams[5], this.mParams[6]};
    }

    protected void preSetupShader() {
        float f = (this.mAngle / 180.0f) * 3.141592f;
        float f2 = this.mDistance / this.mQuality;
        float sin = ((float) Math.sin((double) f)) * f2;
        setParam(1, ((float) Math.cos((double) f)) * f2);
        setParam(2, sin);
        setParam(7, this.mQuality);
        notifyWorkerFilters();
    }

    public void setAngle(float f) {
        this.mAngle = f;
        preSetupShader();
    }

    public void setDistance(float f) {
        if (f > 0.0f) {
            this.mDistance = 1.5f * f;
            preSetupShader();
        }
    }

    public void setQuality(float f) {
        this.mQuality = (float) ((int) (5.0f + (Math.max(0.0f, Math.min(f, 1000.0f)) * 0.4f)));
        preSetupShader();
    }

    public void setShadowColor(float f, float f2, float f3, float f4) {
        this.mParams[3] = Math.max(0.0f, Math.min(f, 1.0f));
        this.mParams[4] = Math.max(0.0f, Math.min(f2, 1.0f));
        this.mParams[5] = Math.max(0.0f, Math.min(f3, 1.0f));
        this.mParams[6] = Math.max(0.0f, Math.min(f4, 1.0f));
        setFilterParamsChanged();
        preSetupShader();
    }

    public void setShadowColor(int i) {
        setShadowColor(((float) Color.red(i)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.alpha(i)) / 255.0f);
    }
}
