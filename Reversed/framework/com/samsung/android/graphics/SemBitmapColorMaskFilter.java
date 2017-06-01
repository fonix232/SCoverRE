package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemBitmapColorMaskFilter extends SemGenericImageFilter {
    private static final int ALPHA = 4;
    private static final int BLUE = 3;
    private static final int ENABLE_GRADIENT = 0;
    private static final int GRADIENT_ALPHA = 8;
    private static final int GRADIENT_BLUE = 7;
    private static final int GRADIENT_ENDX = 11;
    private static final int GRADIENT_ENDY = 12;
    private static final int GRADIENT_GREEN = 6;
    private static final int GRADIENT_RED = 5;
    private static final int GRADIENT_STARTX = 9;
    private static final int GRADIENT_STARTY = 10;
    private static final int GREEN = 2;
    private static final int RED = 1;
    private static String mFragmentShaderCodeGradient = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\n\nvoid main(void) {\n    vec4 startColor = vec4(filterParams[1], filterParams[2], filterParams[3], filterParams[4]);\n    vec4 endColor = vec4(filterParams[5], filterParams[6], filterParams[7], filterParams[8]);\n    vec2 startPoint = vec2(filterParams[9], filterParams[10]);\n    vec2 endPoint = vec2(filterParams[11], filterParams[12]);\n    vec2 send = endPoint - startPoint;\n    vec2 scur = outTexCoords - startPoint;\n    float proj = dot(send, scur) / dot(send, send);\n    vec4 mask = mix(startColor, endColor, smoothstep(0.0, 1.0, proj));\n    vec4 texColor = texture2D(baseSampler, outTexCoords);\n    mask.rgb *= mask.a;\n    gl_FragColor = mask + texColor * (1.0 - mask.a);\n}\n\n";
    private static String mFragmentShaderCodeMask = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\nuniform float filterParams[16];\n\nvoid main(void) {\n   vec4 texColor = texture2D(baseSampler, outTexCoords);\n  vec4 maskColor = texture2D(maskSampler, outTexCoords);\n   vec4 domColor = vec4(filterParams[1], filterParams[2], filterParams[3], filterParams[4]) * texColor;\n float alpha = domColor.a * maskColor.a;\n  domColor.rgb = domColor.rgb * alpha;\n domColor.a = alpha;\n  gl_FragColor = domColor + texColor * (1.0 - domColor.a);\n}\n\n";
    private boolean mGradientEnabled = true;

    public SemBitmapColorMaskFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCodeGradient);
        useFilterParams();
        if (sLogingEnabled) {
            Log.d("HWUI_IMAGE_FILTER", String.format("{0x%x}->SemBitmapColorMaskFilter()", new Object[]{Integer.valueOf(hashCode())}));
        }
    }

    public int[] animateEndColor(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, long j, long j2, TimeInterpolator timeInterpolator) {
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
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientColor aFraction = " + f);
                }
                float f2 = ((f11 - f12) * f) + f12;
                float f3 = ((f13 - f14) * f) + f14;
                float f4 = ((f15 - f16) * f) + f16;
                float max = Math.max(0.0f, Math.min(((f9 - f10) * f) + f10, 1.0f));
                f2 = Math.max(0.0f, Math.min(f2, 1.0f));
                f3 = Math.max(0.0f, Math.min(f3, 1.0f));
                f4 = Math.max(0.0f, Math.min(f4, 1.0f));
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientColor redColor = " + max);
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientColor greenColor = " + f2);
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientColor blueColor = " + f3);
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientColor alphaColor = " + f4);
                }
                this.params[0] = max;
                this.params[1] = f2;
                this.params[2] = f3;
                this.params[3] = f4;
                System.arraycopy(this.params, 0, SemBitmapColorMaskFilter.this.mParams, 5, this.params.length);
                imageFilterAnimator.setUniformf("filterParams", this.params, 5);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateEndColor(int i, int i2, long j, long j2, TimeInterpolator timeInterpolator) {
        return animateEndColor(((float) Color.red(i)) / 255.0f, ((float) Color.red(i2)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.green(i2)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.blue(i2)) / 255.0f, ((float) Color.alpha(i)) / 255.0f, ((float) Color.alpha(i2)) / 255.0f, j, j2, timeInterpolator);
    }

    public int[] animateGradientEndX(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientEndX aFraction = " + f);
                }
                float f2 = ((f2 - f) * f) + f;
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientEndX endX = " + f2);
                }
                this.params[0] = f2;
                SemBitmapColorMaskFilter.this.mParams[11] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 11);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientEndY(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientEndY aFraction = " + f);
                }
                float f2 = 1.0f - (((f2 - f) * f) + f);
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientEndY endY = " + f2);
                }
                this.params[0] = f2;
                SemBitmapColorMaskFilter.this.mParams[12] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 12);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientStartX(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientStartX aFraction = " + f);
                }
                float f2 = ((f2 - f) * f) + f;
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientStartX startX = " + f2);
                }
                this.params[0] = f2;
                SemBitmapColorMaskFilter.this.mParams[9] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 9);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateGradientStartY(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientStartY aFraction = " + f);
                }
                float f2 = 1.0f - (((f2 - f) * f) + f);
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateGradientStartY startY = " + f2);
                }
                this.params[0] = f2;
                SemBitmapColorMaskFilter.this.mParams[10] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 10);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateStartColor(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, long j, long j2, TimeInterpolator timeInterpolator) {
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
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateColor aFraction = " + f);
                }
                float f2 = ((f11 - f12) * f) + f12;
                float f3 = ((f13 - f14) * f) + f14;
                float f4 = ((f15 - f16) * f) + f16;
                float max = Math.max(0.0f, Math.min(((f9 - f10) * f) + f10, 1.0f));
                f2 = Math.max(0.0f, Math.min(f2, 1.0f));
                f3 = Math.max(0.0f, Math.min(f3, 1.0f));
                f4 = Math.max(0.0f, Math.min(f4, 1.0f));
                if (SemBitmapColorMaskFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateColor redColor = " + max);
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateColor greenColor = " + f2);
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateColor blueColor = " + f3);
                    Log.e("HWUIIF", "SemBitmapColorMaskFilter animateColor alphaColor = " + f4);
                }
                this.params[0] = max;
                this.params[1] = f2;
                this.params[2] = f3;
                this.params[3] = f4;
                System.arraycopy(this.params, 0, SemBitmapColorMaskFilter.this.mParams, 1, this.params.length);
                imageFilterAnimator.setUniformf("filterParams", this.params, 1);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateStartColor(int i, int i2, long j, long j2, TimeInterpolator timeInterpolator) {
        return animateStartColor(((float) Color.red(i)) / 255.0f, ((float) Color.red(i2)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.green(i2)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.blue(i2)) / 255.0f, ((float) Color.alpha(i)) / 255.0f, ((float) Color.alpha(i2)) / 255.0f, j, j2, timeInterpolator);
    }

    public SemBitmapColorMaskFilter clone() throws CloneNotSupportedException {
        SemBitmapColorMaskFilter semBitmapColorMaskFilter = (SemBitmapColorMaskFilter) super.clone();
        semBitmapColorMaskFilter.mGradientEnabled = this.mGradientEnabled;
        return semBitmapColorMaskFilter;
    }

    public void enableGradient() {
        setParam(0, 1.0f);
        if (!this.mGradientEnabled) {
            this.mGradientEnabled = true;
            String[] strArr = new String[]{SemGenericImageFilter.mVertexShaderCodeCommon};
            String[] strArr2 = new String[1];
            strArr2[0] = this.mGradientEnabled ? mFragmentShaderCodeGradient : mFragmentShaderCodeMask;
            setup(1, strArr, strArr2);
            if (this.mView != null) {
                this.mView.syncImageFilter();
            }
        }
        notifyWorkerFilters();
    }

    public float[] getColor() {
        return new float[]{getParam(1), getParam(2), getParam(3), getParam(4)};
    }

    public float[] getGradient() {
        return new float[]{getParam(9), 1.0f - getParam(10), getParam(1), getParam(2), getParam(3), getParam(4), getParam(11), 1.0f - getParam(12), getParam(5), getParam(6), getParam(7), getParam(8)};
    }

    public boolean getGradientEnabled() {
        return this.mGradientEnabled;
    }

    public void resetGradient() {
        if (sLogingEnabled) {
            Log.d("HWUI_IMAGE_FILTER", String.format("{0x%x}->SemBitmapColorMaskFilter.resetGradient()", new Object[]{Integer.valueOf(hashCode())}));
        }
        setParam(0, 0.0f);
        if (this.mGradientEnabled) {
            this.mGradientEnabled = false;
            String[] strArr = new String[]{SemGenericImageFilter.mVertexShaderCodeCommon};
            String[] strArr2 = new String[1];
            strArr2[0] = this.mGradientEnabled ? mFragmentShaderCodeGradient : mFragmentShaderCodeMask;
            setup(1, strArr, strArr2);
            if (this.mView != null) {
                this.mView.syncImageFilter();
            }
        }
        notifyWorkerFilters();
    }

    public void setBitmap(Bitmap bitmap) {
        super.setBitmap("maskSampler", bitmap);
    }

    public void setColor(float f, float f2, float f3, float f4) {
        setParam(1, Math.max(0.0f, Math.min(f, 1.0f)));
        setParam(2, Math.max(0.0f, Math.min(f2, 1.0f)));
        setParam(3, Math.max(0.0f, Math.min(f3, 1.0f)));
        setParam(4, Math.max(0.0f, Math.min(f4, 1.0f)));
        setFilterParamsChanged();
        resetGradient();
    }

    public void setColor(int i) {
        setColor(((float) Color.red(i)) / 255.0f, ((float) Color.green(i)) / 255.0f, ((float) Color.blue(i)) / 255.0f, ((float) Color.alpha(i)) / 255.0f);
    }

    public void setGradient(float f, float f2, int i, float f3, float f4, int i2) {
        if (sLogingEnabled) {
            Log.d("HWUI_IMAGE_FILTER", String.format("{0x%x}->SemBitmapColorMaskFilter.setGradient(%f,%f,0x%x,  %f, %f, 0x%x)", new Object[]{Integer.valueOf(hashCode()), Float.valueOf(f), Float.valueOf(f2), Integer.valueOf(i), Float.valueOf(f3), Float.valueOf(f4), Integer.valueOf(i2)}));
        }
        float green = ((float) Color.green(i)) / 255.0f;
        float blue = ((float) Color.blue(i)) / 255.0f;
        float alpha = ((float) Color.alpha(i)) / 255.0f;
        setParam(1, Math.max(0.0f, Math.min(((float) Color.red(i)) / 255.0f, 1.0f)));
        setParam(2, Math.max(0.0f, Math.min(green, 1.0f)));
        setParam(3, Math.max(0.0f, Math.min(blue, 1.0f)));
        setParam(4, Math.max(0.0f, Math.min(alpha, 1.0f)));
        green = ((float) Color.green(i2)) / 255.0f;
        blue = ((float) Color.blue(i2)) / 255.0f;
        alpha = ((float) Color.alpha(i2)) / 255.0f;
        setParam(5, Math.max(0.0f, Math.min(((float) Color.red(i2)) / 255.0f, 1.0f)));
        setParam(6, Math.max(0.0f, Math.min(green, 1.0f)));
        setParam(7, Math.max(0.0f, Math.min(blue, 1.0f)));
        setParam(8, Math.max(0.0f, Math.min(alpha, 1.0f)));
        setParam(9, f);
        setParam(10, 1.0f - f2);
        setParam(11, f3);
        setParam(12, 1.0f - f4);
        enableGradient();
    }
}
