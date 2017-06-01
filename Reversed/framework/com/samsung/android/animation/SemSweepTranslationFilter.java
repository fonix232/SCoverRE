package com.samsung.android.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ListView;
import com.samsung.android.animation.SemSweepListAnimator.OnSweepListener;
import com.samsung.android.animation.SemSweepListAnimator.SweepConfiguration;

public class SemSweepTranslationFilter extends SemAbsSweepAnimationFilter {
    private static final int BG_ALPHA = 225;
    private static final int COEFFICIENT_FOR_VELOCITY_ADJUSTMENT = 23;
    private static final boolean DEBUGGABLE_LOW = Debug.semIsProductDev();
    private static final int DIRECTION_LEFT_TO_RIGHT = 0;
    private static final int DIRECTION_RIGHT_TO_LEFT = 1;
    private static final int SWEEP_TEXT_PADDING_DP = 16;
    private static int SWEEP_TEXT_PADDING_PX = 0;
    private static final int SWIPE_DURATION = 600;
    private static final String TAG = "SemSweepTranslationFilter";
    private static int VELOCITY_UNITS = 1000;
    private static Interpolator sDecel = new DecelerateInterpolator();
    private final boolean DEBUGGABLE = false;
    private final int leftColor = Color.rgb(110, 189, 82);
    private Paint mBgLeftGreen;
    private Paint mBgRightYellow;
    private Context mContext;
    private float mDeltaX = 0.0f;
    private BitmapDrawable mDrawSweepBitmapDrawable = null;
    private float mEndXOfActionUpAnimator = 0.0f;
    private ListView mListView;
    private Bitmap mSweepBitmap = null;
    private SweepConfiguration mSweepConfiguration = null;
    private int mSweepDirection = -1;
    private OnSweepListener mSweepListener = null;
    private Rect mSweepRect = null;
    private boolean mSweepRectFullyDrawn = false;
    private Paint mTextPaint;
    private int mTextPaintSize = 80;
    private View mViewForeground = null;
    private int mViewTop = 0;
    private final int rightColor = Color.rgb(235, 133, 0);

    SemSweepTranslationFilter(ListView listView, Context context) {
        this.mContext = context;
        this.mBgLeftGreen = initPaintWithAlphaAntiAliasing(this.leftColor);
        this.mBgRightYellow = initPaintWithAlphaAntiAliasing(this.rightColor);
        this.mTextPaint = initPaintWithAlphaAntiAliasing(Color.parseColor("#ffffff"));
        this.mTextPaint.setTextSize((float) this.mTextPaintSize);
        this.mListView = listView;
        SWEEP_TEXT_PADDING_PX = convertDipToPixels(this.mContext, 16);
    }

    private static int convertDipToPixels(Context context, int i) {
        return Math.round(((float) i) * context.getResources().getDisplayMetrics().density);
    }

    private void drawRectInto(Canvas canvas, Rect rect, Paint paint, int i, Rect rect2, String str, float f, Drawable drawable) {
        canvas.save();
        paint.setAlpha(i);
        this.mTextPaint.setAlpha(i);
        if (f != 0.0f) {
            this.mTextPaint.setTextSize(f);
        } else {
            this.mTextPaint.setTextSize((float) this.mTextPaintSize);
        }
        canvas.clipRect(rect);
        canvas.drawRect(rect, paint);
        if (drawable != null) {
            if (rect2 != null) {
                drawable.setBounds(rect2);
            }
            drawable.draw(canvas);
        }
        drawSweepText(canvas, this.mTextPaint, str, rect2);
        canvas.restore();
    }

    private Canvas drawRectToBitmapCanvas(View view, float f, float f2) {
        if (this.mSweepConfiguration == null) {
            return null;
        }
        int i = 0;
        int width = view.getWidth();
        int height = view.getHeight();
        int left = view.getLeft();
        View view2 = (View) view.getParent();
        if (view2 != null && (view2 instanceof ViewGroup)) {
            i = view2 instanceof ListView ? view.getTop() : view.getTop() + view2.getTop();
        }
        this.mViewTop = i;
        this.mSweepRect = new Rect(left, i, width, i + height);
        if (this.mSweepBitmap == null) {
            this.mSweepBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(this.mSweepBitmap);
        canvas.drawColor(0, Mode.CLEAR);
        float abs = Math.abs(f);
        float width2 = (abs / ((float) view.getWidth())) * 255.0f;
        Rect bounds;
        int width3;
        int height2;
        Rect rect;
        if (f2 > 0.0f) {
            this.mSweepDirection = 0;
            bounds = this.mSweepConfiguration.drawableLeftToRight.getBounds();
            width3 = bounds.width();
            height2 = bounds.height();
            Rect rect2 = new Rect(0, 0, (int) f, height);
            rect = new Rect(this.mSweepConfiguration.drawablePadding, 0, this.mSweepConfiguration.drawablePadding + width3, height2);
            rect.offset(0, (height - height2) / 2);
            drawRectInto(canvas, rect2, this.mBgLeftGreen, 255, rect, this.mSweepConfiguration.textLeftToRight, this.mSweepConfiguration.textSize, this.mSweepConfiguration.drawableLeftToRight);
            drawRectInto(canvas, new Rect((int) f, 0, width, height), this.mBgLeftGreen, (int) width2, rect, this.mSweepConfiguration.textLeftToRight, this.mSweepConfiguration.textSize, null);
        } else if (f2 < 0.0f) {
            this.mSweepDirection = 1;
            bounds = this.mSweepConfiguration.drawableRightToLeft.getBounds();
            width3 = bounds.width();
            height2 = bounds.height();
            Rect rect3 = new Rect(width - ((int) abs), 0, width, height);
            rect = new Rect((width - width3) - this.mSweepConfiguration.drawablePadding, 0, width - this.mSweepConfiguration.drawablePadding, height2);
            rect.offset(0, (height - height2) / 2);
            drawRectInto(canvas, rect3, this.mBgRightYellow, 255, rect, this.mSweepConfiguration.textRightToLeft, this.mSweepConfiguration.textSize, this.mSweepConfiguration.drawableRightToLeft);
            drawRectInto(canvas, new Rect(0, 0, width - ((int) abs), height), this.mBgRightYellow, (int) width2, rect, this.mSweepConfiguration.textRightToLeft, this.mSweepConfiguration.textSize, null);
        }
        return canvas;
    }

    private void drawSweepText(Canvas canvas, Paint paint, String str, Rect rect) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        Rect rect2 = new Rect();
        paint.setTextAlign(Align.LEFT);
        paint.getTextBounds(str, 0, str.length(), rect2);
        FontMetrics fontMetrics = paint.getFontMetrics();
        float abs = Math.abs(fontMetrics.top - fontMetrics.bottom);
        float f = 0.0f;
        if (rect != null) {
            if (this.mSweepDirection == 1) {
                f = (float) ((rect.left - SWEEP_TEXT_PADDING_PX) - (rect2.right - rect2.left));
            } else if (this.mSweepDirection == 0) {
                f = (float) (rect.right + SWEEP_TEXT_PADDING_PX);
            }
        }
        if (this.mSweepRectFullyDrawn) {
            height = this.mViewForeground.getHeight();
        }
        float f2 = ((((float) height) / 2.0f) + (abs / 2.0f)) - fontMetrics.bottom;
        if (this.mSweepRectFullyDrawn) {
            f2 += (float) this.mViewTop;
            this.mSweepRectFullyDrawn = false;
        }
        canvas.drawText(str, f, f2, paint);
    }

    private void drawTextToCenter(Canvas canvas, Paint paint, String str) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        Rect rect = new Rect();
        paint.setTextAlign(Align.LEFT);
        paint.getTextBounds(str, 0, str.length(), rect);
        canvas.drawText(str, ((((float) width) / 2.0f) - (((float) rect.width()) / 2.0f)) - ((float) rect.left), ((((float) height) / 2.0f) + (((float) rect.height()) / 2.0f)) - ((float) rect.bottom), paint);
    }

    private BitmapDrawable getBitmapDrawableToSweepBitmap() {
        if (this.mSweepBitmap == null) {
            return null;
        }
        Drawable bitmapDrawable = new BitmapDrawable(this.mListView.getResources(), this.mSweepBitmap);
        bitmapDrawable.setBounds(this.mSweepRect);
        return bitmapDrawable;
    }

    private Paint initPaintWithAlphaAntiAliasing(int i) {
        Paint paint = new Paint();
        paint.setColor(i);
        paint.setAntiAlias(true);
        return paint;
    }

    public ValueAnimator createActionUpAnimator(View view, float f, int i, float f2, boolean z) {
        long abs;
        float signum;
        float f3;
        float translationX = view.getTranslationX();
        int width = view.getWidth();
        float abs2 = Math.abs(f2);
        if (translationX > ((float) width)) {
            translationX = (float) width;
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "SemSweepTranslationFilter : createActionUpAnimator() : Math.abs(adjustedVelocityX) = " + Math.abs(f));
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "SemSweepTranslationFilter : createActionUpAnimator() : scaledTouchSlop * 23 = " + (i * 23));
        }
        if (Math.abs(f) > ((float) (i * 23)) && z) {
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepTranslationFilter : createActionUpAnimator() : kick in animation with given velocity, point #1");
            }
            int abs3 = width - ((int) Math.abs(translationX));
            abs = (long) ((int) ((1.0f - (Math.abs(translationX) / ((float) width))) * 600.0f));
            if (abs > 600) {
                abs = 600;
            }
            signum = Math.signum(f) * ((float) width);
            f3 = 0.0f;
        } else if (abs2 > ((float) width) / 2.0f) {
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepTranslationFilter : createActionUpAnimator() : Greater than a half of the width, point #2");
            }
            abs = (long) ((int) ((1.0f - (Math.abs(translationX) / ((float) width))) * 600.0f));
            signum = Math.signum(f2) * ((float) width);
            f3 = 0.0f;
        } else {
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepTranslationFilter : createActionUpAnimator() : Not far enough - animate it back, point #3");
            }
            abs = (long) ((int) ((Math.abs(translationX) * 600.0f) / ((float) width)));
            signum = 0.0f;
            f3 = 1.0f;
            this.mIsAnimationBack = true;
        }
        if (abs < 0) {
            abs = 600;
        }
        this.mEndXOfActionUpAnimator = signum;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{f3});
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{signum});
        ValueAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{ofFloat, ofFloat2});
        ofPropertyValuesHolder.setDuration(abs);
        ofPropertyValuesHolder.setInterpolator(sDecel);
        return ofPropertyValuesHolder;
    }

    public void doMoveAction(View view, float f, int i) {
        float width = f / ((float) view.getWidth());
        float abs = Math.abs(f);
        this.mViewForeground = view;
        this.mDeltaX = f;
        Canvas drawRectToBitmapCanvas = drawRectToBitmapCanvas(view, f, width);
        view.setTranslationX(f);
        view.setAlpha(1.0f - (abs / ((float) view.getWidth())));
        if (!(this.mSweepListener == null || drawRectToBitmapCanvas == null)) {
            this.mSweepListener.onSweep(i, width, drawRectToBitmapCanvas);
        }
        if (this.mDrawSweepBitmapDrawable == null) {
            this.mDrawSweepBitmapDrawable = new BitmapDrawable();
        }
        this.mDrawSweepBitmapDrawable = getBitmapDrawableToSweepBitmap();
        if (this.mDrawSweepBitmapDrawable != null) {
            this.mListView.invalidate(this.mDrawSweepBitmapDrawable.getBounds());
        }
    }

    public void doRefresh() {
        if (this.mViewForeground != null) {
            this.mViewForeground.setVisibility(0);
            this.mViewForeground.setTranslationX(0.0f);
            this.mViewForeground.setAlpha(1.0f);
        }
        this.mIsAnimationBack = false;
        removeCachedBitmap();
    }

    public void doUpActionWhenAnimationUpdate(int i, float f) {
        Canvas canvas = null;
        if (this.mViewForeground != null) {
            canvas = drawRectToBitmapCanvas(this.mViewForeground, f * ((float) this.mViewForeground.getWidth()), f);
        }
        if (!(this.mSweepListener == null || canvas == null)) {
            this.mSweepListener.onSweep(i, f, canvas);
        }
        this.mDrawSweepBitmapDrawable = getBitmapDrawableToSweepBitmap();
        if (this.mDrawSweepBitmapDrawable != null) {
            this.mListView.invalidate(this.mDrawSweepBitmapDrawable.getBounds());
        }
    }

    public void draw(Canvas canvas) {
        if (this.mDrawSweepBitmapDrawable != null) {
            this.mDrawSweepBitmapDrawable.draw(canvas);
        }
    }

    public Rect getBitmapDrawableBound() {
        Rect rect = new Rect();
        return this.mDrawSweepBitmapDrawable != null ? this.mDrawSweepBitmapDrawable.getBounds() : null;
    }

    public float getEndXOfActionUpAnimator() {
        return this.mEndXOfActionUpAnimator;
    }

    public BitmapDrawable getSweepBitmapDrawable() {
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "getSweepBitmapDrawable : mDrawSweepBitmapDrawable = " + this.mDrawSweepBitmapDrawable);
        }
        return this.mDrawSweepBitmapDrawable;
    }

    public void initAnimationFilter(View view, float f, int i, OnSweepListener onSweepListener, SweepConfiguration sweepConfiguration) {
        this.mSweepListener = onSweepListener;
        this.mViewForeground = view;
        this.mSweepConfiguration = sweepConfiguration;
    }

    public /* bridge */ /* synthetic */ boolean isAnimationBack() {
        return super.isAnimationBack();
    }

    public void removeCachedBitmap() {
        if (this.mDrawSweepBitmapDrawable != null) {
            this.mDrawSweepBitmapDrawable.getBitmap().recycle();
            this.mDrawSweepBitmapDrawable = null;
            this.mSweepBitmap = null;
        }
    }

    public void setForegroundView(View view) {
        this.mViewForeground = view;
    }
}
