package com.samsung.android.graphics;

import android.graphics.Bitmap;

public class SemCustomFilter extends SemImageFilter {
    public SemCustomFilter(String str, String str2) {
        super((int) SemImageFilter.TYPE_CUSTOM_FILTER);
        setVertexShader(str);
        setFragmentShader(str2);
    }

    public SemCustomFilter clone() throws CloneNotSupportedException {
        return (SemCustomFilter) super.clone();
    }

    public long getNativeBitmap(String str) {
        return super.getNativeBitmap(str);
    }

    public float[] getUniform1f(String str) {
        float[] fArr = new float[1];
        super.getUniformf(str, 1, 1, fArr);
        return fArr;
    }

    public int[] getUniform1i(String str) {
        int[] iArr = new int[1];
        super.getUniformi(str, 1, 1, iArr);
        return iArr;
    }

    public float[] getUniform2f(String str) {
        float[] fArr = new float[2];
        super.getUniformf(str, 2, 1, fArr);
        return fArr;
    }

    public int[] getUniform2i(String str) {
        int[] iArr = new int[2];
        super.getUniformi(str, 2, 1, iArr);
        return iArr;
    }

    public float[] getUniform3f(String str) {
        float[] fArr = new float[3];
        super.getUniformf(str, 3, 1, fArr);
        return fArr;
    }

    public int[] getUniform3i(String str) {
        int[] iArr = new int[3];
        super.getUniformi(str, 3, 1, iArr);
        return iArr;
    }

    public float[] getUniform4f(String str) {
        float[] fArr = new float[4];
        super.getUniformf(str, 4, 1, fArr);
        return fArr;
    }

    public int[] getUniform4i(String str) {
        int[] iArr = new int[4];
        super.getUniformi(str, 4, 1, iArr);
        return iArr;
    }

    public float[] getUniformMatrix(String str, int i, int i2) {
        float[] fArr = new float[(i * i2)];
        super.getUniformMatrix(str, i, i2, fArr);
        return fArr;
    }

    public float[] getUniformfv(String str, int i, int i2) {
        float[] fArr = new float[(i * i2)];
        super.getUniformf(str, i, i2, fArr);
        return fArr;
    }

    public int[] getUniformiv(String str, int i, int i2) {
        int[] iArr = new int[(i * i2)];
        super.getUniformi(str, i, i2, iArr);
        return iArr;
    }

    public int[] getUpdateMargin() {
        int[] iArr = new int[4];
        super.getUpdateMargin(iArr);
        return iArr;
    }

    public float getValue(int i) {
        return super.getValue(i);
    }

    public void setSamplerBitmap(String str, int i, Bitmap bitmap) {
        super.setBitmap(str, bitmap);
    }

    public void setUniform1f(String str, float f) {
        super.setUniformf(str, 1, 1, new float[]{f});
    }

    public void setUniform1i(String str, int i) {
        super.setUniformi(str, 1, 1, new int[]{i});
    }

    public void setUniform2f(String str, float f, float f2) {
        super.setUniformf(str, 2, 1, new float[]{f, f2});
    }

    public void setUniform2i(String str, int i, int i2) {
        super.setUniformi(str, 2, 1, new int[]{i, i2});
    }

    public void setUniform3f(String str, float f, float f2, float f3) {
        super.setUniformf(str, 3, 1, new float[]{f, f2, f3});
    }

    public void setUniform3i(String str, int i, int i2, int i3) {
        super.setUniformi(str, 3, 1, new int[]{i, i2, i3});
    }

    public void setUniform4f(String str, float f, float f2, float f3, float f4) {
        super.setUniformf(str, 4, 1, new float[]{f, f2, f3, f4});
    }

    public void setUniform4i(String str, int i, int i2, int i3, int i4) {
        super.setUniformi(str, 4, 1, new int[]{i, i2, i3, i4});
    }

    public void setUniformMatrix(String str, int i, int i2, float[] fArr) {
        super.setUniformMatrix(str, i, i2, fArr);
    }

    public void setUniformfv(String str, int i, int i2, float[] fArr) {
        super.setUniformf(str, i, i2, fArr);
    }

    public void setUniformiv(String str, int i, int i2, int[] iArr) {
        super.setUniformi(str, i, i2, iArr);
    }

    public void setUpdateMargin(int i, int i2, int i3, int i4) {
        super.setUpdateMargin(i, i2, i3, i4);
    }

    public void setValue(int i, float f) {
        super.setValue(i, f);
    }
}
