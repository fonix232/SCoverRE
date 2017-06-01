package com.samsung.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.samsung.android.graphics.spr.document.SprDocument;

class SemGradientColorWheel extends View {
    private static final String TAG = "SemGradientColorWheel";
    private final int[] HUE_COLORS = new int[]{-65536, -65281, -16776961, -16711681, -16711936, -256, -65536};
    private float mCurX;
    private float mCurY;
    private Bitmap mCursorBitmap;
    private Drawable mCursorDrawable;
    private Paint mCursorPaint;
    private final int mCursorPaintSize = getResources().getDimensionPixelSize(17105757);
    private final int mCursorShadowSize = getResources().getDimensionPixelSize(17105758);
    private final int mCursorSize = getResources().getDimensionPixelSize(17105756);
    private Paint mHuePaint;
    private OnWheelColorChangedListener mListener;
    private int mOrbitalRadius;
    private int mRadius;
    private Paint mSaturationPaint;

    interface OnWheelColorChangedListener {
        void onWheelColorChanged(float f, float f2);
    }

    public SemGradientColorWheel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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

    private void updateCursorPosition(float f, float f2) {
        double d = (((double) f) * 3.141592653589793d) / 180.0d;
        float f3 = ((float) this.mOrbitalRadius) * f2;
        this.mCurX = (float) ((int) (((double) this.mRadius) + (((double) f3) * Math.cos(d))));
        this.mCurY = (float) ((int) (((double) this.mRadius) - (((double) f3) * Math.sin(d))));
        invalidate();
    }

    public void close() {
        this.mCursorPaint = null;
        this.mHuePaint = null;
        this.mSaturationPaint = null;
        this.mListener = null;
    }

    void init(int i) {
        i += this.mCursorShadowSize;
        this.mRadius = i / 2;
        this.mOrbitalRadius = this.mRadius - (this.mCursorShadowSize / 2);
        Shader sweepGradient = new SweepGradient((float) this.mRadius, (float) this.mRadius, this.HUE_COLORS, null);
        this.mHuePaint = new Paint(1);
        this.mHuePaint.setShader(sweepGradient);
        this.mHuePaint.setStyle(Style.FILL);
        Shader radialGradient = new RadialGradient((float) this.mRadius, (float) this.mRadius, (float) this.mOrbitalRadius, -1, 0, TileMode.CLAMP);
        this.mSaturationPaint = new Paint(1);
        this.mSaturationPaint.setShader(radialGradient);
        this.mCursorPaint = new Paint();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) resizeDrawable(getContext(), (BitmapDrawable) getResources().getDrawable(17303500, null), this.mCursorShadowSize, this.mCursorShadowSize);
        if (bitmapDrawable != null) {
            this.mCursorBitmap = bitmapDrawable.getBitmap();
        } else {
            Log.e(TAG, "resizeDrawable == null");
        }
        LayoutParams layoutParams = new FrameLayout.LayoutParams(i, i);
        layoutParams.gravity = 1;
        setLayoutParams(layoutParams);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((float) this.mRadius, (float) this.mRadius, (float) this.mOrbitalRadius, this.mHuePaint);
        canvas.drawCircle((float) this.mRadius, (float) this.mRadius, (float) this.mOrbitalRadius, this.mSaturationPaint);
        canvas.drawCircle(this.mCurX, this.mCurY, ((float) this.mCursorPaintSize) / SprDocument.DEFAULT_DENSITY_SCALE, this.mCursorPaint);
        canvas.drawBitmap(this.mCursorBitmap, this.mCurX - (((float) this.mCursorShadowSize) / SprDocument.DEFAULT_DENSITY_SCALE), this.mCurY - (((float) this.mCursorShadowSize) / SprDocument.DEFAULT_DENSITY_SCALE), this.mCursorPaint);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float sqrt = (float) Math.sqrt(Math.pow((double) (motionEvent.getX() - ((float) this.mRadius)), 2.0d) + Math.pow((double) (motionEvent.getY() - ((float) this.mRadius)), 2.0d));
        switch (motionEvent.getAction()) {
            case 0:
                if (sqrt <= ((float) this.mRadius)) {
                    playSoundEffect(0);
                    break;
                }
                return false;
            case 2:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                break;
        }
        this.mCurX = motionEvent.getX();
        this.mCurY = motionEvent.getY();
        if (sqrt > ((float) this.mOrbitalRadius)) {
            this.mCurX = ((float) this.mRadius) + ((((float) this.mOrbitalRadius) * (motionEvent.getX() - ((float) this.mRadius))) / sqrt);
            this.mCurY = ((float) this.mRadius) + ((((float) this.mOrbitalRadius) * (motionEvent.getY() - ((float) this.mRadius))) / sqrt);
        }
        if (this.mListener != null) {
            this.mListener.onWheelColorChanged(((float) ((Math.atan2((double) (this.mCurY - ((float) this.mRadius)), (double) (((float) this.mRadius) - this.mCurX)) * 180.0d) / 3.141592653589793d)) + 180.0f, sqrt / ((float) this.mOrbitalRadius));
        } else {
            Log.d(TAG, "Listener is not set.");
        }
        invalidate();
        return true;
    }

    void setColor(int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        updateCursorPosition(fArr[0], fArr[1]);
    }

    void setOnColorWheelInterface(OnWheelColorChangedListener onWheelColorChangedListener) {
        this.mListener = onWheelColorChangedListener;
    }

    void updateCursorColor(int i) {
        this.mCursorPaint.setColor(i);
    }
}
