package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemDesaturationFilter extends SemGenericImageFilter {
    private static final int DESATURATION = 0;
    private static final float MAX_DESATURATION = 1.0f;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nvoid main(void) {\n     vec4 texColor = texture2D(baseSampler, outTexCoords);\n    float lum = dot(vec3(0.2126,0.7152,0.0722), texColor.rgb);\n   vec4 grayColor = vec4(lum, lum, lum, texColor.a);\n    gl_FragColor = mix(grayColor, texColor, filterParams[0]);\n}\n\n";
    private float mDesaturation = 0.0f;

    public SemDesaturationFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
        setSaturation(MAX_DESATURATION);
    }

    public int[] animateSaturation(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemDesaturationFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDesaturationFilter animateSaturation aFraction = " + f);
                }
                SemDesaturationFilter.this.mDesaturation = ((f2 - f) * f) + f;
                if (SemDesaturationFilter.this.mDesaturation > SemDesaturationFilter.MAX_DESATURATION) {
                    SemDesaturationFilter.this.mDesaturation = SemDesaturationFilter.MAX_DESATURATION;
                } else if (SemDesaturationFilter.this.mDesaturation < 0.0f) {
                    SemDesaturationFilter.this.mDesaturation = 0.0f;
                }
                if (SemDesaturationFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemDesaturationFilter animateSaturation mDesaturation = " + SemDesaturationFilter.this.mDesaturation);
                }
                this.params[0] = SemDesaturationFilter.this.mDesaturation;
                imageFilterAnimator.setUniformf("filterParams", this.params, 0);
            }
        }, j, j2, timeInterpolator);
    }

    public void setSaturation(float f) {
        if (this.mDesaturation != f) {
            if (this.mDesaturation > MAX_DESATURATION) {
                this.mDesaturation = MAX_DESATURATION;
            } else if (this.mDesaturation < 0.0f) {
                this.mDesaturation = 0.0f;
            } else {
                this.mDesaturation = f;
            }
            setParam(0, this.mDesaturation);
            notifyWorkerFilters();
        }
    }
}
