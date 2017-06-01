package com.samsung.android.graphics;

public class SemBlendingFilter extends SemGenericImageFilter {
    public static final int BLENDING_MODE_MULTIPLY = 1;
    public static final int BLENDING_MODE_NORMAL = 0;
    public static final int BLENDING_MODE_SCREEN = 2;
    private static String mFragmentShaderCodeMultipy = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\n\nvoid main(void) {\n       vec4 texColor = texture2D(baseSampler, outTexCoords);\n        vec4 mask = texture2D(maskSampler, outTexCoords);\n       gl_FragColor = texColor * mask;\n}\n\n";
    private static String mFragmentShaderCodeNormal = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\n\nvoid main(void) {\n   vec4 texColor = texture2D(baseSampler, outTexCoords);\n    vec4 mask = texture2D(maskSampler, outTexCoords);\n    gl_FragColor = mask + texColor * (1.0 - mask.a);\n}\n\n";
    private static String mFragmentShaderCodeScreen = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\n\nvoid main(void) {\n       vec4 unitColor = vec4(1.0);\n       vec4 texColor = texture2D(baseSampler, outTexCoords);\n       vec4 mask = texture2D(maskSampler, outTexCoords);\n       gl_FragColor = unitColor - ((unitColor-texColor) * (unitColor-mask));\n}\n\n";
    private int mBlendMode;

    public SemBlendingFilter() {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, getFragmentShader(0));
        this.mBlendMode = 0;
        this.mBlendMode = 0;
    }

    public SemBlendingFilter(int i) {
        super(SemGenericImageFilter.mVertexShaderCodeCommon, getFragmentShader(i));
        this.mBlendMode = 0;
        this.mBlendMode = i;
    }

    protected static String getFragmentShader(int i) {
        return i == 0 ? mFragmentShaderCodeNormal : i == 1 ? mFragmentShaderCodeMultipy : mFragmentShaderCodeScreen;
    }

    public void setOperation(int i) {
        if (this.mBlendMode != i) {
            this.mBlendMode = i;
            setup(SemGenericImageFilter.mVertexShaderCodeCommon, getFragmentShader(i));
        }
    }
}
