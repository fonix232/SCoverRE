package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.ImageFilterAnimator;

public class SemSgiBlurFilter extends SemGenericImageFilter {
    private static final float MAX_RADIUS = 250.0f;
    private static final int RADIUS = 1;
    private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nvarying vec2 vNeighborTexCoord[12];\nuniform sampler2D baseSampler;\n\nvoid main(void) {\n   highp vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[0])  * 0.00903788620091937;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[1])  * 0.0217894371884468;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[2])  * 0.0447649434011506;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[3])  * 0.0783687553896893;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[4])  * 0.116912444814134;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[5])  * 0.148624846131112;\n   fragColorBlur += texture2D(baseSampler, outTexCoords        )  * 0.161003373749805;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[6])  * 0.148624846131112;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[7])  * 0.116912444814134;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[8])  * 0.0783687553896893;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[9])  * 0.0447649434011506;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[10]) * 0.0217894371884468;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[11]) * 0.00903788620091937;\n   gl_FragColor = fragColorBlur;\n}\n\n";
    private static String[] mVertexShaderCode = new String[]{"attribute vec2 texCoords;\nattribute vec4 position;\nuniform float areaW;\nuniform float sampleRate;\nuniform float filterParams[16];\nvarying vec2 outTexCoords;\nvarying vec2 vNeighborTexCoord[12];\nvoid main() {\n   outTexCoords = texCoords;\n   float v = filterParams[1] / 6.0 / areaW;\n   vNeighborTexCoord[0]  = outTexCoords + vec2(-6.0 * v, 0.0);\n   vNeighborTexCoord[1]  = outTexCoords + vec2(-5.0 * v, 0.0);\n   vNeighborTexCoord[2]  = outTexCoords + vec2(-4.0 * v, 0.0);\n   vNeighborTexCoord[3]  = outTexCoords + vec2(-3.0 * v, 0.0);\n   vNeighborTexCoord[4]  = outTexCoords + vec2(-2.0 * v, 0.0);\n   vNeighborTexCoord[5]  = outTexCoords + vec2(-1.0 * v, 0.0);\n   vNeighborTexCoord[6]  = outTexCoords + vec2( 1.0 * v, 0.0);\n   vNeighborTexCoord[7]  = outTexCoords + vec2( 2.0 * v, 0.0);\n   vNeighborTexCoord[8]  = outTexCoords + vec2( 3.0 * v, 0.0);\n   vNeighborTexCoord[9]  = outTexCoords + vec2( 4.0 * v, 0.0);\n   vNeighborTexCoord[10] = outTexCoords + vec2( 5.0 * v, 0.0);\n   vNeighborTexCoord[11] = outTexCoords + vec2( 6.0 * v, 0.0);\n   gl_Position = position;\n}\n", "attribute vec2 texCoords;\nattribute vec4 position;\nuniform float areaH;\nuniform float sampleRate;\nuniform float filterParams[16];\nvarying vec2 outTexCoords;\nvarying vec2 vNeighborTexCoord[12];\nvoid main() {\n   outTexCoords = texCoords;\n   float v = filterParams[1] / 6.0 / areaH;\n   vNeighborTexCoord[0]  = outTexCoords + vec2(0.0, -6.0 * v );\n   vNeighborTexCoord[1]  = outTexCoords + vec2(0.0, -5.0 * v );\n   vNeighborTexCoord[2]  = outTexCoords + vec2(0.0, -4.0 * v );\n   vNeighborTexCoord[3]  = outTexCoords + vec2(0.0, -3.0 * v );\n   vNeighborTexCoord[4]  = outTexCoords + vec2(0.0, -2.0 * v );\n   vNeighborTexCoord[5]  = outTexCoords + vec2(0.0, -1.0 * v );\n   vNeighborTexCoord[6]  = outTexCoords + vec2(0.0,  1.0 * v );\n   vNeighborTexCoord[7]  = outTexCoords + vec2(0.0,  2.0 * v );\n   vNeighborTexCoord[8]  = outTexCoords + vec2(0.0,  3.0 * v );\n   vNeighborTexCoord[9]  = outTexCoords + vec2(0.0,  4.0 * v );\n   vNeighborTexCoord[10] = outTexCoords + vec2(0.0,  5.0 * v );\n   vNeighborTexCoord[11] = outTexCoords + vec2(0.0,  6.0 * v );\n   gl_Position = position;\n}\n"};
    private float mRadius = 0.0f;

    public SemSgiBlurFilter() {
        super(2, mVertexShaderCode, new String[]{mFragmentShaderCode});
        setRadius(1.0f);
    }

    public int[] animateRadius(final float f, final float f2, long j, long j2, TimeInterpolator timeInterpolator) {
        return addAnimationForAllPasses(new IAnimationListener() {
            private float[] params = new float[]{0.0f};

            public void animate(float f, ImageFilterAnimator imageFilterAnimator) {
                if (SemSgiBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemSgiBlurFilter animateRadius aFraction = " + f);
                }
                SemSgiBlurFilter.this.mRadius = ((f2 - f) * f) + f;
                SemSgiBlurFilter.this.mRadius = Math.max(0.0f, Math.min(SemSgiBlurFilter.this.mRadius, 60.0f));
                if (SemSgiBlurFilter.sLogingEnabled) {
                    Log.e("HWUIIF", "SemSgiBlurFilter animateRadius mRadius = " + SemSgiBlurFilter.this.mRadius);
                }
                this.params[0] = SemSgiBlurFilter.this.mRadius;
                SemSgiBlurFilter.this.mParams[1] = this.params[0];
                imageFilterAnimator.setUniformf("filterParams", this.params, 1);
            }
        }, j, j2, timeInterpolator);
    }

    public void setRadius(float f) {
        if (this.mRadius != f) {
            this.mRadius = Math.max(0.0f, Math.min(f, MAX_RADIUS));
            setParam(1, this.mRadius);
            setupDownSampling();
            notifyWorkerFilters();
        }
    }

    protected void setupDownSampling() {
        float f = this.mRadius / 6.0f;
        if (f < 1.0f) {
            f = 1.0f;
        }
        setSamplingRate(0, f, 1.0f);
        setSamplingRate(1, f, f);
    }
}
