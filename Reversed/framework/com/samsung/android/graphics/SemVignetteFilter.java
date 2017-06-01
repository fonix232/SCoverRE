package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemVignetteFilter extends SemGenericImageFilter {
    private static final float MAX_RADIUS = 1.0f;
    private static final int RADIUS = 0;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nvarying vec2 resolution;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float areaW;\nuniform float areaH;\n\nvoid main(void) {\n   vec2 u_resolution = vec2(areaW, areaH);\n   vec4 texColor = texture2D(baseSampler, outTexCoords);\n   vec2 relativePosition = gl_FragCoord.xy / u_resolution - 0.5;\n   float len = length(relativePosition);\n   float vignette = smoothstep(filterParams[0] + 0.1, filterParams[0] - 0.1, len);\n   texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.9);\n   gl_FragColor = texColor;\n}\n\n";
    private float mRadius = 0.0f;

    public SemVignetteFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
        useFilterParams();
    }

    public int[] animateRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemVignetteFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemVignetteFilter animateRadius aFraction = " + f);
                }
                SemVignetteFilter.this.mRadius = ((f2 - f) * f) + f;
                if (SemVignetteFilter.this.mRadius > SemVignetteFilter.MAX_RADIUS) {
                    SemVignetteFilter.this.mRadius = SemVignetteFilter.MAX_RADIUS;
                } else if (SemVignetteFilter.this.mRadius < 0.0f) {
                    SemVignetteFilter.this.mRadius = 0.0f;
                }
                if (SemVignetteFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemVignetteFilter animateRadius mRadius = " + SemVignetteFilter.this.mRadius);
                }
                this.params[0] = SemVignetteFilter.this.mRadius;
                SemVignetteFilter.this.mParams[0] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public SemVignetteFilter clone() throws CloneNotSupportedException {
        SemVignetteFilter semVignetteFilter = (SemVignetteFilter) super.clone();
        semVignetteFilter.mRadius = this.mRadius;
        return semVignetteFilter;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setRadius(float f) {
        if (this.mRadius != f) {
            this.mRadius = f;
            if (this.mRadius > MAX_RADIUS) {
                this.mRadius = MAX_RADIUS;
            } else if (this.mRadius < 0.0f) {
                this.mRadius = 0.0f;
            }
            setParam(0, this.mRadius);
            notifyWorkerFilters();
        }
    }
}
