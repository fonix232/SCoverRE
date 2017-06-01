package com.samsung.android.animation;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import com.samsung.android.animation.SemSweepListAnimator.OnSweepListener;
import com.samsung.android.animation.SemSweepListAnimator.SweepConfiguration;

public class SemSweepWaveFilter extends SemAbsSweepAnimationFilter {
    private static final int SWIPE_DURATION = 600;
    private static final String TAG = "SemSweepWaveFilter";
    private static final int WAVE_ANIMATION_DURATION = 1300;
    private static final int WAVE_BG_ALPHA = 225;
    private static Interpolator sDecel = new DecelerateInterpolator();
    private final boolean DEBUGGABLE = false;
    private final Interpolator WAVE_INTERPOLATOR = new LinearInterpolator();
    private float incrementYdown = 0.0f;
    private float incrementYup = 0.0f;
    private final int leftColor = Color.rgb(97, 170, 19);
    private Paint mBaseWaveColor = new Paint();
    private Paint mBgLeftGreen = initPaintWithAlphaAntiAliasing(this.leftColor);
    private Paint mBgMiddleBlue = initPaintWithAlphaAntiAliasing(this.middleColor);
    private Paint mBgRightYellow = initPaintWithAlphaAntiAliasing(this.rightColor);
    private float mDeltaX = 0.0f;
    private BitmapDrawable mDrawSweepBitmapDrawable = null;
    private float mEndXOfActionUpAnimator = 0.0f;
    private float mGradientWidth = 400.0f;
    private boolean mIsActionMove = false;
    private ListView mListView;
    private RectF mMiddleBlueRect = new RectF();
    private Path mPathDown;
    private Path mPathUp;
    private int mPosition = -1;
    private Bitmap mSweepBitmap = null;
    private OnSweepListener mSweepListener = null;
    private float mSweepProgress = 0.0f;
    private Rect mSweepRect = null;
    private View mViewForeground = null;
    private final int middleColor = Color.rgb(12, 92, 126);
    private final int rightColor = Color.rgb(232, 156, 0);
    private int waveBaseColor = Color.rgb(255, 255, 255);
    private float waveControlPointHeight = 0.0f;
    private float waveHeight = 0.0f;
    private ValueAnimator waveValueAnimator;
    private float waveWidth = 0.0f;

    SemSweepWaveFilter(ListView listView) {
        this.mBaseWaveColor.setColor(this.waveBaseColor);
        this.mListView = listView;
    }

    private void cancelRunningAnimator() {
        if (this.waveValueAnimator != null) {
            this.waveValueAnimator.cancel();
        }
    }

    private void doDrawWaveEffect(View view, float f, int i) {
        float width = f / ((float) view.getWidth());
        Canvas drawWaveToBitmapCanvas = drawWaveToBitmapCanvas(view, width);
        if (!(this.mSweepListener == null || drawWaveToBitmapCanvas == null || !this.mIsActionMove)) {
            this.mSweepListener.onSweep(i, width, drawWaveToBitmapCanvas);
        }
        if (this.mDrawSweepBitmapDrawable == null) {
            this.mDrawSweepBitmapDrawable = new BitmapDrawable();
        }
        this.mDrawSweepBitmapDrawable = getBitmapDrawableToSweepBitmap();
        if (this.mDrawSweepBitmapDrawable != null) {
            this.mListView.invalidate(this.mDrawSweepBitmapDrawable.getBounds());
        }
    }

    private void drawWave(Canvas canvas, Rect rect, float f) {
        rect.offset(0, -rect.top);
        int width = this.mListView.getWidth();
        canvas.drawRect(rect, this.mBaseWaveColor);
        float f2 = (((float) width) + this.mGradientWidth) * f;
        if (f2 > 0.0f) {
            drawWaveInto(canvas, rect, (f2 - this.mGradientWidth) + (this.mGradientWidth / 2.0f), false, this.mBgLeftGreen, this.mBgMiddleBlue);
        } else if (f2 < 0.0f) {
            drawWaveInto(canvas, rect, (((float) width) + f2) + (this.mGradientWidth / 2.0f), true, this.mBgMiddleBlue, this.mBgRightYellow);
        } else {
            this.mMiddleBlueRect.set(rect);
            canvas.drawRect(this.mMiddleBlueRect, this.mBgMiddleBlue);
        }
    }

    private void drawWaveInto(Canvas canvas, Rect rect, float f, boolean z, Paint paint, Paint paint2) {
        float f2 = f + (this.waveWidth / 2.0f);
        float f3 = this.incrementYdown - (this.waveHeight * 2.0f);
        float width = (float) this.mListView.getWidth();
        this.mPathDown.reset();
        this.mPathDown.moveTo(0.0f, f3);
        this.mPathDown.lineTo(this.waveWidth + f2, f3);
        this.mPathDown.cubicTo(this.waveWidth + f2, this.waveControlPointHeight + f3, f2, (this.waveHeight + f3) - this.waveControlPointHeight, f2, f3 + this.waveHeight);
        this.mPathDown.cubicTo(f2, (this.waveHeight + f3) + this.waveControlPointHeight, f2 + this.waveWidth, ((this.waveHeight * 2.0f) + f3) - this.waveControlPointHeight, f2 + this.waveWidth, f3 + (this.waveHeight * 2.0f));
        this.mPathDown.cubicTo(this.waveWidth + f2, ((this.waveHeight * 2.0f) + f3) + this.waveControlPointHeight, f2, ((this.waveHeight * 3.0f) + f3) - this.waveControlPointHeight, f2, f3 + (this.waveHeight * 3.0f));
        this.mPathDown.cubicTo(f2, ((this.waveHeight * 3.0f) + f3) + this.waveControlPointHeight, f2 + this.waveWidth, ((this.waveHeight * 4.0f) + f3) - this.waveControlPointHeight, f2 + this.waveWidth, f3 + (this.waveHeight * 4.0f));
        this.mPathDown.lineTo(0.0f, (this.waveHeight * 4.0f) + f3);
        this.mPathDown.close();
        this.mPathUp.reset();
        this.mPathUp.moveTo(width, f3);
        this.mPathUp.lineTo(this.waveWidth + f2, f3);
        this.mPathUp.cubicTo(this.waveWidth + f2, this.waveControlPointHeight + f3, f2, (this.waveHeight + f3) - this.waveControlPointHeight, f2, f3 + this.waveHeight);
        this.mPathUp.cubicTo(f2, (this.waveHeight + f3) + this.waveControlPointHeight, f2 + this.waveWidth, ((this.waveHeight * 2.0f) + f3) - this.waveControlPointHeight, f2 + this.waveWidth, f3 + (this.waveHeight * 2.0f));
        this.mPathUp.cubicTo(this.waveWidth + f2, ((this.waveHeight * 2.0f) + f3) + this.waveControlPointHeight, f2, ((this.waveHeight * 3.0f) + f3) - this.waveControlPointHeight, f2, f3 + (this.waveHeight * 3.0f));
        this.mPathUp.cubicTo(f2, ((this.waveHeight * 3.0f) + f3) + this.waveControlPointHeight, f2 + this.waveWidth, ((this.waveHeight * 4.0f) + f3) - this.waveControlPointHeight, f2 + this.waveWidth, f3 + (this.waveHeight * 4.0f));
        this.mPathUp.lineTo(width, (this.waveHeight * 4.0f) + f3);
        this.mPathUp.close();
        int save = canvas.save();
        canvas.clipRect(rect);
        if (z) {
            canvas.drawPath(this.mPathDown, paint);
            canvas.drawPath(this.mPathUp, paint2);
        } else {
            canvas.drawPath(this.mPathUp, paint2);
            canvas.drawPath(this.mPathDown, paint);
        }
        canvas.restoreToCount(save);
    }

    private Canvas drawWaveToBitmapCanvas(View view, float f) {
        int i = 0;
        int width = view.getWidth();
        int height = view.getHeight();
        int left = view.getLeft();
        View view2 = (View) view.getParent();
        if (view2 != null && (view2 instanceof ViewGroup)) {
            i = view2 instanceof ListView ? view.getTop() : view.getTop() + view2.getTop();
        }
        this.mSweepRect = new Rect(left, i, width, i + height);
        if (this.mSweepBitmap == null) {
            this.mSweepBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(this.mSweepBitmap);
        drawWave(canvas, new Rect(0, 0, width, height), f);
        return canvas;
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
        paint.setAlpha(225);
        paint.setAntiAlias(true);
        return paint;
    }

    private void initWaveParams(float f, final int i, OnSweepListener onSweepListener) {
        float f2 = f;
        int i2 = i;
        View childAt = this.mListView.getChildAt(i - this.mListView.getFirstVisiblePosition());
        if (childAt != null) {
            this.mSweepListener = onSweepListener;
            int height = childAt.getHeight();
            this.mPathDown = new Path();
            this.mPathDown.reset();
            this.mPathUp = new Path();
            this.mPathUp.reset();
            this.waveHeight = (float) (height / 2);
            this.waveWidth = (float) (height / 13);
            this.waveControlPointHeight = (float) (height / 4);
            if (this.waveValueAnimator != null) {
                this.waveValueAnimator.cancel();
                this.waveValueAnimator.start();
            } else {
                this.waveValueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.waveValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedFraction = valueAnimator.getAnimatedFraction();
                        SemSweepWaveFilter.this.incrementYdown = (SemSweepWaveFilter.this.waveHeight * animatedFraction) * 2.0f;
                        SemSweepWaveFilter.this.incrementYup = ((-animatedFraction) * SemSweepWaveFilter.this.waveHeight) * 2.0f;
                        SemSweepWaveFilter.this.doDrawWaveEffect(SemSweepWaveFilter.this.mViewForeground, SemSweepWaveFilter.this.mDeltaX, i);
                    }
                });
                this.waveValueAnimator.setRepeatCount(-1);
                this.waveValueAnimator.setRepeatMode(1);
                this.waveValueAnimator.setDuration(1300);
                this.waveValueAnimator.setInterpolator(this.WAVE_INTERPOLATOR);
                this.waveValueAnimator.start();
            }
        }
    }

    private void removeCachedBitmap() {
        if (this.mDrawSweepBitmapDrawable != null) {
            this.mDrawSweepBitmapDrawable.getBitmap().recycle();
            this.mDrawSweepBitmapDrawable = null;
            this.mSweepBitmap = null;
        }
    }

    public ValueAnimator createActionUpAnimator(View view, float f, int i, float f2, boolean z) {
        long j;
        float signum;
        int width = view.getWidth();
        float width2 = f2 / ((float) view.getWidth());
        float abs = Math.abs(f2);
        if (f2 > ((float) width)) {
            f2 = (float) width;
        }
        if (Math.abs(f) > ((float) (i * 6)) && z) {
            j = 600;
            signum = Math.signum(f);
        } else if (abs > ((float) width) / 2.0f) {
            j = 600;
            signum = Math.signum(f2);
        } else {
            j = (long) ((int) ((1.0f - (Math.abs(f2) / ((float) width))) * 600.0f));
            signum = 0.0f;
        }
        this.mEndXOfActionUpAnimator = signum;
        float f3 = width2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{width2, signum});
        ofFloat.setDuration(j);
        ofFloat.setInterpolator(sDecel);
        return ofFloat;
    }

    public void doMoveAction(View view, float f, int i) {
        float width = f / ((float) view.getWidth());
        this.mDeltaX = f;
        this.mSweepProgress = width;
        this.mIsActionMove = true;
    }

    public void doRefresh() {
        this.mIsActionMove = false;
        removeCachedBitmap();
        cancelRunningAnimator();
    }

    public void doUpActionWhenAnimationUpdate(int i, float f) {
        Canvas canvas = null;
        if (this.mViewForeground != null) {
            canvas = drawWaveToBitmapCanvas(this.mViewForeground, f);
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

    public void initAnimationFilter(View view, float f, int i, OnSweepListener onSweepListener, SweepConfiguration sweepConfiguration) {
        this.mViewForeground = view;
        initWaveParams(f, i, onSweepListener);
    }

    public /* bridge */ /* synthetic */ boolean isAnimationBack() {
        return super.isAnimationBack();
    }

    public void setForegroundView(View view) {
        this.mViewForeground = view;
    }
}
