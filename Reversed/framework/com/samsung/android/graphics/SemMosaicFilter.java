package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemMosaicFilter extends SemGenericImageFilter {
    private static final float MAX_RADIUS = 1024.0f;
    private static final float MIN_RADIUS = 1.0f;
    private static final int RADIUS = 0;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform mat4 projection;\nuniform mat4 transform;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float areaW;\nuniform float areaH;\n\nvoid main(void) {\n    float radius_h = filterParams[0] / areaW;\n    float radius_v = filterParams[0] / areaH;\n    vec2 texCoord = vec2((floor(outTexCoords.s / radius_h) + 0.5) * radius_h, (floor(outTexCoords.t / radius_v) + 0.5) * radius_v);\n  gl_FragColor = texture2D(baseSampler, texCoord);\n}\n\n";
    private float mRadius = 0.0f;

    public SemMosaicFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
        useFilterParams();
    }

    public int[] animateRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemMosaicFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemMosaicFilter animateRadius aFraction = " + f);
                }
                SemMosaicFilter.this.mRadius = ((f2 - f) * f) + f;
                if (SemMosaicFilter.this.mRadius > SemMosaicFilter.MAX_RADIUS) {
                    SemMosaicFilter.this.mRadius = SemMosaicFilter.MAX_RADIUS;
                } else if (SemMosaicFilter.this.mRadius < SemMosaicFilter.MIN_RADIUS) {
                    SemMosaicFilter.this.mRadius = SemMosaicFilter.MIN_RADIUS;
                }
                if (SemMosaicFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemMosaicFilter animateRadius mRadius = " + SemMosaicFilter.this.mRadius);
                }
                this.params[0] = SemMosaicFilter.this.mRadius;
                SemMosaicFilter.this.mParams[0] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public SemMosaicFilter clone() throws CloneNotSupportedException {
        SemMosaicFilter semMosaicFilter = (SemMosaicFilter) super.clone();
        semMosaicFilter.mRadius = this.mRadius;
        return semMosaicFilter;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setRadius(float f) {
        if (this.mRadius != f) {
            this.mRadius = f;
            if (this.mRadius > MAX_RADIUS) {
                this.mRadius = MAX_RADIUS;
            } else if (this.mRadius < MIN_RADIUS) {
                this.mRadius = MIN_RADIUS;
            }
            setParam(0, this.mRadius);
            notifyWorkerFilters();
        }
    }
}
