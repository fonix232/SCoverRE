package com.samsung.android.graphics;

import android.graphics.Bitmap;

public class SemGenericImageFilter extends SemImageFilterSet {
    protected static final float FALSE = 0.0f;
    protected static final int FILTER_BLEND_DST_FACTOR = 6;
    protected static final int FILTER_BLEND_SRC_FACTOR = 5;
    protected static final int FILTER_BLEND_USAGE = 4;
    protected static final int FILTER_DOWN_SAMPLE_RATE_H = 8;
    protected static final int FILTER_DOWN_SAMPLE_RATE_V = 9;
    protected static final int FILTER_FILTERING_STATE_ANTIALIASING_DISABLED = 2;
    protected static final int FILTER_FILTERING_STATE_ANTIALIASING_ENABLED = 1;
    protected static final int FILTER_FILTERING_STATE_UNCHANGED = 0;
    protected static final int FILTER_HAS_SAMPLERS = 3;
    protected static final int FILTER_HAS_UNIFORMS = 2;
    protected static final int FILTER_INPUT_TEXTURE_FILTERING_STATE = 10;
    protected static final int FILTER_IS_CHANGED = 1;
    protected static final int FILTER_IS_ENABLED = 0;
    protected static final int FILTER_OUTPUT_TEXTURE_FILTERING_STATE = 11;
    protected static final int FILTER_TRANSFORM = 7;
    protected static final int FILTER_WRAP_STATE_CLAMP_TO_EDGE = 1;
    protected static final int FILTER_WRAP_STATE_MIRRORED = 3;
    protected static final int FILTER_WRAP_STATE_REPEAT = 2;
    protected static final int FILTER_WRAP_STATE_UNCHANGED = 0;
    protected static final float GL_CONSTANT_ALPHA = 12.0f;
    protected static final float GL_CONSTANT_COLOR = 10.0f;
    protected static final float GL_DST_ALPHA = 8.0f;
    protected static final float GL_DST_COLOR = 4.0f;
    protected static final float GL_ONE = 1.0f;
    protected static final float GL_ONE_MINUS_CONSTANT_ALPHA = 13.0f;
    protected static final float GL_ONE_MINUS_CONSTANT_COLOR = 11.0f;
    protected static final float GL_ONE_MINUS_DST_ALPHA = 9.0f;
    protected static final float GL_ONE_MINUS_DST_COLOR = 5.0f;
    protected static final float GL_ONE_MINUS_SRC_ALPHA = 7.0f;
    protected static final float GL_ONE_MINUS_SRC_COLOR = 3.0f;
    protected static final float GL_SRC_ALPHA = 6.0f;
    protected static final float GL_SRC_ALPHA_SATURATE = 14.0f;
    protected static final float GL_SRC_COLOR = 2.0f;
    protected static final float GL_ZERO = 0.0f;
    protected static final float TRUE = 1.0f;
    public static final String mVertexShaderCodeCommon = "attribute vec2 texCoords;\nattribute vec4 position;\nvarying vec2 outTexCoords;\nuniform mat4 projection;\nvoid main() {\n   outTexCoords = texCoords;\n   gl_Position = projection * position;\n}\n";
    protected float[] mData1;
    protected float[] mData2;
    protected String[] mFrag;
    protected boolean mIsFilterData01Modified;
    protected boolean mIsFilterData01Used;
    protected boolean mIsFilterData02Modified;
    protected boolean mIsFilterData02Used;
    protected boolean mIsFilterParamsModified;
    protected boolean mIsFilterParamsUsed;
    protected float[] mParams;
    protected int mPassNum;
    protected String[] mVert;

    public SemGenericImageFilter(int i, String[] strArr, String[] strArr2) {
        this.mPassNum = 0;
        this.mParams = new float[16];
        this.mData1 = new float[64];
        this.mData2 = new float[64];
        this.mIsFilterParamsUsed = false;
        this.mIsFilterData01Used = false;
        this.mIsFilterData02Used = false;
        this.mIsFilterParamsModified = false;
        this.mIsFilterData01Modified = false;
        this.mIsFilterData02Modified = false;
        setup(i, strArr, strArr2);
    }

    public SemGenericImageFilter(String str, String str2) {
        this.mPassNum = 0;
        this.mParams = new float[16];
        this.mData1 = new float[64];
        this.mData2 = new float[64];
        this.mIsFilterParamsUsed = false;
        this.mIsFilterData01Used = false;
        this.mIsFilterData02Used = false;
        this.mIsFilterParamsModified = false;
        this.mIsFilterData01Modified = false;
        this.mIsFilterData02Modified = false;
        this.mPassNum = 1;
        setup(1, new String[]{str}, new String[]{str2});
    }

    protected void buildWorkerFilters() {
        super.clearFilters();
        for (int i = 0; i < this.mPassNum; i++) {
            addFilter(SemImageFilter.createCustomFilter(getVertexShaderCode(i), getFragmentShaderCode(i)));
        }
    }

    public SemGenericImageFilter clone() throws CloneNotSupportedException {
        SemGenericImageFilter semGenericImageFilter = (SemGenericImageFilter) super.clone();
        semGenericImageFilter.mPassNum = this.mPassNum;
        System.arraycopy(this.mParams, 0, semGenericImageFilter.mParams, 0, this.mParams.length);
        System.arraycopy(this.mData1, 0, semGenericImageFilter.mData1, 0, this.mData1.length);
        System.arraycopy(this.mData2, 0, semGenericImageFilter.mData2, 0, this.mData2.length);
        semGenericImageFilter.mIsFilterParamsUsed = this.mIsFilterParamsUsed;
        semGenericImageFilter.mIsFilterData01Used = this.mIsFilterData01Used;
        semGenericImageFilter.mIsFilterData02Used = this.mIsFilterData02Used;
        semGenericImageFilter.mIsFilterParamsModified = this.mIsFilterParamsModified;
        semGenericImageFilter.mIsFilterData01Modified = this.mIsFilterData01Modified;
        semGenericImageFilter.mIsFilterData02Modified = this.mIsFilterData02Modified;
        System.arraycopy(this.mVert, 0, semGenericImageFilter.mVert, 0, this.mVert.length);
        System.arraycopy(this.mFrag, 0, semGenericImageFilter.mFrag, 0, this.mFrag.length);
        return semGenericImageFilter;
    }

    protected String getFragmentShaderCode(int i) {
        return i < 0 ? null : i >= this.mFrag.length ? this.mFrag[this.mFrag.length - 1] : this.mFrag[i];
    }

    protected float getParam(int i) {
        return (i < 0 || i >= this.mParams.length) ? 0.0f : this.mParams[i];
    }

    protected String getVertexShaderCode(int i) {
        return i < 0 ? null : i >= this.mVert.length ? this.mVert[this.mVert.length - 1] : this.mVert[i];
    }

    protected String getVertexShaderCodeCommon() {
        return mVertexShaderCodeCommon;
    }

    protected void notifyWorkerFilters() {
        for (int i = 0; i < this.mPassNum; i++) {
            SemCustomFilter semCustomFilter = (SemCustomFilter) getFilterAt(i);
            if (this.mIsFilterParamsUsed && this.mIsFilterParamsModified) {
                semCustomFilter.setUniformfv("filterParams", 1, this.mParams.length, this.mParams);
            }
            if (this.mIsFilterData01Used && this.mIsFilterData01Modified) {
                semCustomFilter.setUniformfv("filterData01", 1, this.mData1.length, this.mData1);
            }
            if (this.mIsFilterData02Used && this.mIsFilterData02Modified) {
                semCustomFilter.setUniformfv("filterData02", 1, this.mData2.length, this.mData2);
            }
        }
        if (this.mListener != null) {
            this.mListener.onParamsChanged();
        }
    }

    protected void resetFilterData01Changed() {
        this.mIsFilterData01Modified = false;
    }

    protected void resetFilterData02Changed() {
        this.mIsFilterData02Modified = false;
    }

    protected void resetFilterParamsChanged() {
        this.mIsFilterParamsModified = false;
    }

    public void setBitmap(String str, Bitmap bitmap) {
        for (int i = 0; i < this.mPassNum; i++) {
            getFilterAt(i).setBitmap(str, bitmap);
        }
    }

    protected void setBitmapFiltering(int i, String str, boolean z) {
        if (i >= 0 && i <= this.mPassNum && i <= getFilterCount()) {
            getFilterAt(i).setBitmapFiltering(str, z ? 1 : 2);
        }
    }

    protected void setBitmapWrap(int i, String str, boolean z, boolean z2) {
        if (i >= 0 && i <= this.mPassNum && i <= getFilterCount()) {
            int i2 = z ? z2 ? 3 : 2 : 1;
            getFilterAt(i).setBitmapWrap(str, i2);
        }
    }

    protected void setFilterData01Changed() {
        this.mIsFilterData01Modified = true;
    }

    protected void setFilterData02Changed() {
        this.mIsFilterData02Modified = true;
    }

    protected void setFilterParamsChanged() {
        this.mIsFilterParamsModified = true;
    }

    protected void setFiltering(int i, int i2, int i3) {
        if (i >= 0 && i <= this.mPassNum && i <= getFilterCount()) {
            SemImageFilter filterAt = getFilterAt(i);
            filterAt.setValue(10, (float) i2);
            filterAt.setValue(11, (float) i3);
        }
    }

    protected void setParam(int i, float f) {
        this.mParams[i] = f;
        useFilterParams();
        setFilterParamsChanged();
    }

    protected void setSamplingRate(int i, float f, float f2) {
        if (i >= 0 && i <= this.mPassNum && i <= getFilterCount()) {
            SemImageFilter filterAt = getFilterAt(i);
            filterAt.setValue(8, f);
            filterAt.setValue(9, f2);
        }
    }

    protected void setup(int i, String[] strArr, String[] strArr2) {
        this.mPassNum = i;
        this.mVert = strArr;
        this.mFrag = strArr2;
        buildWorkerFilters();
        notifyWorkerFilters();
    }

    protected void setup(String str, String str2) {
        this.mPassNum = 1;
        this.mVert = new String[]{str};
        this.mFrag = new String[]{str};
        buildWorkerFilters();
        notifyWorkerFilters();
    }

    protected void unUseFilterData01() {
        this.mIsFilterData01Used = false;
    }

    protected void unUseFilterData02() {
        this.mIsFilterData02Used = false;
    }

    protected void unUseFilterParams() {
        this.mIsFilterParamsUsed = false;
    }

    protected void useFilterData01() {
        this.mIsFilterData01Used = true;
    }

    protected void useFilterData02() {
        this.mIsFilterData02Used = true;
    }

    protected void useFilterParams() {
        this.mIsFilterParamsUsed = true;
    }
}
