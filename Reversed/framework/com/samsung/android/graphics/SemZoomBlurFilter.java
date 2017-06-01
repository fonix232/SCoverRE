package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemZoomBlurFilter extends SemGenericImageFilter {
    private static final int QUALITY = 1;
    private static final int ZOOM = 0;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float filterData01[64];\nvoid main(void) {\nvec2 xy = 2.0 * outTexCoords.xy - 1.0;\nvec2 uv = outTexCoords;\nvec2 dir = 0.5 - uv;\nfloat dist = sqrt(dir.x*dir.x + dir.y*dir.y);\ndir = dir/dist;\nvec4 color = texture2D(baseSampler,uv);\nfloat scaledStep = 0.0;\nvec4 sum = color;\nfor(int i = 0; i < int(filterParams[1])/2; i++){\n   sum += texture2D( baseSampler, uv + dir * -filterData01[i]);\n   sum += texture2D( baseSampler, uv + dir * filterData01[i]);\n}\nsum *= 1.0/filterParams[1];\nfloat t = dist * filterParams[0];\nt = clamp( t ,0.0, 1.0);\ngl_FragColor = mix( color, sum, t );\n}\n";
    private static String mVertexShaderCode = "attribute vec2 texCoords;\nattribute vec4 position;\nvarying vec2 outTexCoords;\nvoid main() {\n   outTexCoords = texCoords;\n   gl_Position = position;\n}\n";
    private int mQuality = 0;
    private float mZoom = 1.0f;

    public SemZoomBlurFilter() {
        super(mVertexShaderCode, mFragmentShaderCode);
        useFilterParams();
        useFilterData01();
    }

    private void computeZoomBlur() {
        if (this.mQuality < 10) {
            this.mQuality = 10;
        }
        if (this.mQuality > 64) {
            this.mQuality = 64;
        }
        for (int i = 0; i < this.mQuality / 2; i++) {
            this.mData1[i] = (1.0f / ((float) (this.mQuality * 2))) * ((float) i);
        }
        setFilterData01Changed();
        setParam(0, this.mZoom);
        setParam(1, (float) this.mQuality);
    }

    private void paramsChanged() {
        computeZoomBlur();
        notifyWorkerFilters();
    }

    public int[] animateQuality(final int i, final int i2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] data = new float[64];
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemZoomBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemZoomBlurFilter animateQuality aFraction = " + f);
                }
                SemZoomBlurFilter.this.mQuality = ((int) (((float) (i2 - i)) * f)) + i;
                if (SemZoomBlurFilter.this.mQuality < 10) {
                    SemZoomBlurFilter.this.mQuality = 10;
                }
                if (SemZoomBlurFilter.this.mQuality > 64) {
                    SemZoomBlurFilter.this.mQuality = 64;
                }
                for (int i = 0; i < SemZoomBlurFilter.this.mQuality / 2; i++) {
                    this.data[i] = (1.0f / ((float) (SemZoomBlurFilter.this.mQuality * 2))) * ((float) i);
                }
                if (SemZoomBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemZoomBlurFilter animateQuality mQuality = " + SemZoomBlurFilter.this.mQuality);
                }
                this.params[0] = (float) SemZoomBlurFilter.this.mQuality;
                SemZoomBlurFilter.this.mParams[1] = this.params[0];
                System.arraycopy(this.data, 0, SemZoomBlurFilter.this.mData1, 0, this.data.length);
                imageFilterAnimator.setUniformf("filterData01", this.data, 0);
                imageFilterAnimator.setUniformf("filterParams", this.params, 1);
            }
        }, j, j2, timeInterpolator);
    }

    public int[] animateZoom(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemZoomBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemZoomBlurFilter animateZoom aFraction = " + f);
                }
                SemZoomBlurFilter.this.mZoom = ((f2 - f) * f) + f;
                if (SemZoomBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemZoomBlurFilter animateZoom mZoom = " + SemZoomBlurFilter.this.mZoom);
                }
                this.params[0] = SemZoomBlurFilter.this.mZoom;
                SemZoomBlurFilter.this.mParams[0] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public void setPivot(float f, float f2) {
    }

    public void setQuality(int i) {
        this.mQuality = i;
        paramsChanged();
    }

    public void setZoomRatio(float f) {
        this.mZoom = f;
        paramsChanged();
    }
}
