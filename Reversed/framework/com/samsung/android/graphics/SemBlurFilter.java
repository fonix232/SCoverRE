package com.samsung.android.graphics;

public class SemBlurFilter extends SemImageFilter {
    public static final int TYPE_COSINE = 1;
    public static final int TYPE_GAUSSIAN = 0;
    public static final int TYPE_SGI = 2;

    public SemBlurFilter() {
        super(54);
    }

    public SemBlurFilter clone() throws CloneNotSupportedException {
        return (SemBlurFilter) super.clone();
    }

    public float getOptimization() {
        return super.getValue(1);
    }

    public float getRadius() {
        return super.getValue(0);
    }

    public void setOptimization(int i) {
        super.setValue(1, (float) i);
    }

    public void setRadius(float f) {
        super.setValue(0, Math.max(0.0f, Math.min(f, 250.0f)));
    }
}
