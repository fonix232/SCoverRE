package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemDistortionFilter extends SemGenericImageFilter {
    private static final int DISTORTION = 0;
    private static final float MAX_RADIUS = 1024.0f;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nvoid main(void) {\n   vec2 xy = 2.0 * outTexCoords.xy - 1.0;\n   vec2 uv;\n   float d = length(xy);\n   if ( d < 1.0 ) {\n       float theta = atan(xy.y, xy.x);\n       float radius = length(xy);\n       radius = pow(radius, filterParams[0]+1.0);\n       xy.x = radius * cos(theta);\n       xy.y = radius * sin(theta);\n       uv = 0.5 * (xy + 1.0);\n   } else {\n       uv = outTexCoords.xy;\n   }\n   gl_FragColor = texture2D(baseSampler, uv);\n}\n\n";
    private float mRadius = 0.0f;

    public SemDistortionFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
    }

    public int[] animateDistortion(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemDistortionFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDistortionFilter animateDistortion aFraction = " + f);
                }
                SemDistortionFilter.this.mRadius = ((f2 - f) * f) + f;
                if (SemDistortionFilter.this.mRadius > SemDistortionFilter.MAX_RADIUS) {
                    SemDistortionFilter.this.mRadius = SemDistortionFilter.MAX_RADIUS;
                } else if (SemDistortionFilter.this.mRadius < 0.0f) {
                    SemDistortionFilter.this.mRadius = 0.0f;
                }
                if (SemDistortionFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDistortionFilter animateDistortion mRadius = " + SemDistortionFilter.this.mRadius);
                }
                this.params[0] = SemDistortionFilter.this.mRadius;
                imageFilterAnimator.setUniformf("filterParams", this.params, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public void setDistortion(float f) {
        if (this.mRadius != f) {
            if (this.mRadius > MAX_RADIUS) {
                this.mRadius = MAX_RADIUS;
            } else if (this.mRadius < 0.0f) {
                this.mRadius = 0.0f;
            } else {
                this.mRadius = f;
            }
            setParam(0, this.mRadius);
            notifyWorkerFilters();
        }
    }
}
