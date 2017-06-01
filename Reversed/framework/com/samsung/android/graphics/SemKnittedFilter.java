package com.samsung.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.samsung.android.graphics.SemImageFilter.IImageFilterListener;
import com.samsung.android.share.SShareConstants;

public class SemKnittedFilter extends SemGenericImageFilter implements IImageFilterListener {
    private static final int ELEM_COUNT = 8;
    private static final int INV_ELEM_COUNT = 9;
    private static final int INV_SAMPLER_H = 5;
    private static final int INV_SAMPLER_W = 4;
    private static final int INV_TEXEL_HEIGHT = 7;
    private static final int INV_TEXEL_WIDTH = 6;
    private static final int LINE_W_RATIO = 10;
    private static final String mFragmentShaderFirstPass = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D originalSampler;\nuniform sampler2D maskSampler;\nuniform float filterParams[16];\n\n\nvoid main(void) {\n     vec4 colorBase = texture2D(baseSampler, outTexCoords);\n     gl_FragColor = colorBase;\n}\n\n";
    private static final String mFragmentShaderSecondPass = "#ifdef GL_ES\nprecision highp float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D originalSampler;\nuniform sampler2D maskSampler;\nuniform float filterParams[16];\n\nfloat rand(float a){                                                                   \n  return fract(abs(sin(dot(vec2(a, 1.0 - a), vec2(12.9898,78.233)))) * 43758.5453);    \n}                                                                                      \n\nvec3 getColor(float shiftY, float shiftPx) {                                           \n    vec2 texel = vec2(floor(outTexCoords.x * filterParams[6]),                          \n                      floor(outTexCoords.y * filterParams[7] + shiftY));                \n                                                                                       \n   float maxValue = filterParams[7] + shiftY;                                          \n    float shiftX = floor(rand(texel.y / maxValue) * filterParams[8]) * filterParams[9]; \n                                                                                        \n    vec2 maskUV = vec2( outTexCoords.x * filterParams[10] + shiftX,                     \n                        outTexCoords.y * filterParams[7] - texel.y + shiftY );          \n                                                                                       \n    vec2 samplerUV = vec2( texel.x * filterParams[4],                                   \n                          (texel.y *2.0 + shiftPx) * filterParams[5] );                 \n                                                                                       \n    return texture2D(baseSampler, samplerUV).rgb * texture2D(maskSampler, maskUV).b;    \n}                                                                                      \n\n\nvoid main(void) {                                                                    \n     gl_FragColor.rgb = getColor(0.0, 2.0) + getColor(0.5, 1.0);                        \n     gl_FragColor.a = 1.0;                                                              \n}\n\n";
    private static final String mVertexShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nattribute vec2 texCoords;\nattribute vec4 position;\nvarying vec2 outTexCoords;\nuniform mat4 projection;\nuniform float filterParams[16];\nvoid main() {\n   outTexCoords = texCoords;\n   gl_Position = projection * position;\n}\n";
    private final float DEFAULT_DOWNSAMPLING_RATE = 40.0f;
    private final float DEFAULT_MASK_ITEM_SIZE = 30.0f;
    private final String DEFAULT_MASK_NAME = "zknitted_filter_mask";
    private final String SAMPLER_MASK_NAME = "maskSampler";
    private float mDownSampleRate = 1.0f;
    private float mInvDownSamplerHeight = 0.0f;
    private float mInvDownSamplerWidth = 0.0f;
    private float mInvItemsCount = 0.0f;
    private float mInvTexelSizeX = 0.0f;
    private float mInvTexelSizeY = 0.0f;
    private float mItemWidth = 0.0f;
    private float mItemsCount = 0.0f;
    private float mMaskWidth = 0.0f;
    private float mSamplerLineRatio = 0.0f;

    public SemKnittedFilter() {
        super(2, new String[]{mVertexShader, mVertexShader}, new String[]{mFragmentShaderFirstPass, mFragmentShaderSecondPass});
        setListener(this);
        useFilterParams();
        setFiltering(0, 2, 2);
        setFiltering(1, 2, 1);
        setDownSampleRate(40.0f);
    }

    private void calculateAndSetAllParams() {
        if (this.mView != null && this.mView.getWidth() > 0 && this.mView.getHeight() > 0) {
            float width = ((float) this.mView.getWidth()) / this.mDownSampleRate;
            float height = ((float) this.mView.getHeight()) / this.mDownSampleRate;
            this.mInvTexelSizeX = width;
            this.mInvTexelSizeY = 0.5f * height;
            this.mInvDownSamplerWidth = 1.0f / width;
            this.mInvDownSamplerHeight = 1.0f / height;
            this.mItemsCount = this.mMaskWidth / this.mItemWidth;
            this.mInvItemsCount = this.mItemWidth / this.mMaskWidth;
            this.mSamplerLineRatio = this.mInvTexelSizeX * this.mInvItemsCount;
            setParam(4, this.mInvDownSamplerWidth);
            setParam(5, this.mInvDownSamplerHeight);
            setParam(6, this.mInvTexelSizeX);
            setParam(7, this.mInvTexelSizeY);
            setParam(8, this.mItemsCount);
            setParam(9, this.mInvItemsCount);
            setParam(10, this.mSamplerLineRatio);
            notifyWorkerFilters();
        }
    }

    public void onAttachedToView() {
        int identifier = this.mView.getResources().getIdentifier("zknitted_filter_mask", "drawable", SShareConstants.RESOLVER_GUIDE_ACTIVITY_PKG);
        if (identifier > 0) {
            setMask(BitmapFactory.decodeResource(this.mView.getResources(), identifier), 30.0f);
        }
        calculateAndSetAllParams();
    }

    public void onParamsChanged() {
    }

    public void onViewSizeChanged() {
        calculateAndSetAllParams();
    }

    public void setDownSampleRate(float f) {
        setSamplingRate(0, f, f);
        this.mDownSampleRate = f;
        calculateAndSetAllParams();
    }

    public void setMask(Bitmap bitmap, float f) {
        setBitmap("maskSampler", bitmap);
        setBitmapWrap(0, "maskSampler", true, false);
        setBitmapWrap(1, "maskSampler", true, false);
        this.mItemWidth = f;
        this.mMaskWidth = (float) bitmap.getWidth();
        calculateAndSetAllParams();
    }
}
