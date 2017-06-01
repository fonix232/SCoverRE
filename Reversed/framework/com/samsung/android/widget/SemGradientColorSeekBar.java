package com.samsung.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

class SemGradientColorSeekBar extends SeekBar {
    private static final int RIPPLE_EFFECT_OPACITY = 41;
    private static final int SEEKBAR_THUMB_OFFSET_DEFAULT = 5;
    private static final String TAG = "SemGradientColorSeekBar";
    private int[] mColors = new int[]{-16777216, -1};
    private Context mContext;
    private GradientDrawable progressDrawable;

    public SemGradientColorSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    private void initColor(int i) {
        r0 = new float[3];
        Color.colorToHSV(i, r0);
        float f = r0[2];
        r0[2] = 1.0f;
        this.mColors[1] = Color.HSVToColor(r0);
        setProgress(Math.round(((float) getMax()) * f));
    }

    private void initProgressDrawable() {
        int round = Math.round((float) this.mContext.getResources().getDimensionPixelSize(17105764));
        int color = getResources().getColor(17170841);
        int dimensionPixelSize = getResources().getDimensionPixelSize(17105765);
        this.progressDrawable = new GradientDrawable(Orientation.LEFT_RIGHT, this.mColors);
        this.progressDrawable.setCornerRadius((float) round);
        this.progressDrawable.setSize(0, round);
        this.progressDrawable.setStroke(dimensionPixelSize, color);
        setProgressDrawable(this.progressDrawable);
    }

    private void initThumb() {
        Drawable drawable = getResources().getDrawable(17303501);
        int dimensionPixelSize = getResources().getDimensionPixelSize(17105766);
        setThumb(resizeDrawable(getContext(), (BitmapDrawable) drawable, dimensionPixelSize, dimensionPixelSize));
        setThumbOffset((int) (getResources().getDisplayMetrics().density * 5.0f));
        setBackground(new RippleDrawable(ColorStateList.valueOf(Color.argb(41, 0, 0, 0)), null, null));
    }

    private static Drawable resizeDrawable(Context context, BitmapDrawable bitmapDrawable, int i, int i2) {
        if (bitmapDrawable == null) {
            return null;
        }
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Matrix matrix = new Matrix();
        float f = 0.0f;
        float f2 = 0.0f;
        if (i > 0) {
            f = ((float) i) / ((float) bitmap.getWidth());
        }
        if (i2 > 0) {
            f2 = ((float) i2) / ((float) bitmap.getHeight());
        }
        matrix.postScale(f, f2);
        return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
    }

    void changeColorBase(int i) {
        if (this.progressDrawable != null) {
            this.mColors[1] = i;
            this.progressDrawable.setColors(this.mColors);
            setProgressDrawable(this.progressDrawable);
            setProgress(getMax());
        }
    }

    void init(int i) {
        setMax(255);
        initColor(i);
        initProgressDrawable();
        initThumb();
    }

    void restoreColor(int i) {
        initColor(i);
        this.progressDrawable.setColors(this.mColors);
        setProgressDrawable(this.progressDrawable);
    }
}
