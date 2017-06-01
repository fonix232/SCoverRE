package com.samsung.android.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ListView;

public class SemSweepListAnimator extends SemAbsSweepListAnimator {
    private static final boolean DEBUGGABLE_LOW = Debug.semIsProductDev();
    public static final int SWEEP_ANIMATION_TRANSLATION = 2;
    public static final int SWEEP_ANIMATION_WAVE = 1;
    private static final String TAG = "SemSweepListAnimator";
    private static float mPreviousDeltaX = 0.0f;
    private static boolean mSkipActionUpAnimation = false;
    private final boolean DEBUGGABLE = false;
    private Context mContext;
    private SweepConfiguration mCurrentSweepConfig = null;
    private boolean mEnableSweep = true;
    private OnTouchListener mListOnTouchListener = new C09781();
    private OnSweepListener mOnSweepListener;
    private SemAbsSweepAnimationFilter mSweepAnimationFilter = null;
    private int mSweepAnimationType = -1;
    private BitmapDrawable mSweepBdToFade = null;
    private int mViewToRemovePosition;

    class C09781 implements OnTouchListener {
        C09781() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return (SemSweepListAnimator.this.mListView.isEnabled() && motionEvent.getActionMasked() == 0) ? false : false;
        }
    }

    public interface OnSweepListener {
        void onSweep(int i, float f, Canvas canvas);

        void onSweepEnd(int i, float f);

        SweepConfiguration onSweepStart(int i, float f, Rect rect);
    }

    public static class SweepConfiguration {
        public boolean allowLeftToRight;
        public boolean allowRightToLeft;
        public int childIdForLocationHint;
        public Drawable drawableLeftToRight;
        public int drawablePadding;
        public Drawable drawableRightToLeft;
        public String textLeftToRight;
        public String textRightToLeft;
        public float textSize;

        public SweepConfiguration() {
            this(true, true, 0);
        }

        public SweepConfiguration(boolean z, boolean z2) {
            this(z, z2, 0);
        }

        public SweepConfiguration(boolean z, boolean z2, int i) {
            this.allowLeftToRight = z;
            this.allowRightToLeft = z2;
            this.childIdForLocationHint = i;
        }
    }

    public SemSweepListAnimator(Context context, ListView listView, int i) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        } else if (i <= 0) {
            throw new IllegalArgumentException("Resource ids should be positive integer");
        } else {
            this.mContext = context;
            this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            this.mForegroundViewResId = i;
            this.mListView = listView;
            if (this.mListView != null) {
                this.mListView.setSweepListAnimator(this);
            }
        }
    }

    private void resetSweepAnimatinFilter() {
        if (this.mSweepAnimationFilter != null) {
            this.mSweepAnimationFilter.doRefresh();
        }
    }

    private void resetSweepInfo() {
        this.mSwiping = false;
        this.mSwipingPosition = -1;
        if (this.mListView != null) {
            this.mListView.setEnabled(true);
        }
    }

    public void draw(Canvas canvas) {
        if (this.mSwiping) {
            this.mSweepAnimationFilter.draw(canvas);
        }
        if (this.mSweepBdToFade != null) {
            this.mSweepBdToFade.draw(canvas);
        }
    }

    public boolean isSweepAnimatorEnabled() {
        return this.mEnableSweep;
    }

    public boolean isSwiping() {
        return this.mSwiping;
    }

    public void onActionCancel(MotionEvent motionEvent, View view) {
        showForeground(view);
        this.mSwiping = false;
        resetTouchState();
        resetSweepAnimatinFilter();
        this.mListView.setPressed(false);
    }

    public void onActionDown(MotionEvent motionEvent) {
        this.mActivePointerId = motionEvent.getPointerId(0);
        this.mItemPressed = true;
        this.mDownX = motionEvent.getX();
    }

    public void onActionMove(MotionEvent motionEvent, View view, int i) {
        float x = motionEvent.getX() - this.mDownX;
        float abs = Math.abs(x);
        if (this.mSwiping) {
            if (view != null && this.mCurrentSweepConfig != null) {
                float width = x / ((float) view.getWidth());
                if ((this.mCurrentSweepConfig.allowLeftToRight && x >= 0.0f) || (this.mCurrentSweepConfig.allowRightToLeft && x <= 0.0f)) {
                    this.mSweepAnimationFilter.doMoveAction(view, x, i);
                } else if (Math.signum(mPreviousDeltaX) != Math.signum(x) && this.mSweepAnimationType == 2) {
                    view.setTranslationX(0.0f);
                    view.setAlpha(1.0f);
                    Rect bitmapDrawableBound = this.mSweepAnimationFilter.getBitmapDrawableBound();
                    if (bitmapDrawableBound != null) {
                        Rect rect = new Rect(bitmapDrawableBound);
                        if (rect != null) {
                            resetSweepAnimatinFilter();
                            if (this.mListView != null) {
                                this.mListView.invalidate(rect);
                            }
                        }
                    }
                    mSkipActionUpAnimation = true;
                }
                this.mVelocityTracker.computeCurrentVelocity(VELOCITY_UNITS);
                float[] fArr = this.mHistoricalVelocities;
                int i2 = this.mHistoricalVelocityIndex;
                this.mHistoricalVelocityIndex = i2 + 1;
                fArr[i2 % HISTORICAL_VELOCITY_COUNT] = this.mVelocityTracker.getXVelocity();
            }
        } else if (abs > ((float) this.mScaledTouchSlop)) {
            this.mDownX = motionEvent.getX();
            this.mSwiping = true;
            this.mSwipingPosition = i;
            if (this.mListView != null) {
                this.mListView.requestDisallowInterceptTouchEvent(true);
                this.mListView.removePendingCallbacks();
            }
            mPreviousDeltaX = x;
            if (!(this.mOnSweepListener == null || view == null)) {
                this.mCurrentSweepConfig = this.mOnSweepListener.onSweepStart(i, 0.0f, new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
                if (!(this.mSweepAnimationFilter == null || this.mCurrentSweepConfig == null)) {
                    if ((!this.mCurrentSweepConfig.allowLeftToRight || x < 0.0f) && (!this.mCurrentSweepConfig.allowRightToLeft || x > 0.0f)) {
                        this.mSwiping = false;
                        this.mSweepAnimationFilter.setForegroundView(view);
                    } else {
                        View findViewById;
                        Drawable selector;
                        if (this.mCurrentSweepConfig.childIdForLocationHint != 0) {
                            if (view.findViewById(this.mCurrentSweepConfig.childIdForLocationHint) != null) {
                                findViewById = view.findViewById(this.mCurrentSweepConfig.childIdForLocationHint);
                                this.mForegroundView = findViewById;
                                if (this.mListView != null) {
                                    selector = this.mListView.getSelector();
                                    this.mListView.setPressed(false);
                                    selector.jumpToCurrentState();
                                }
                                this.mSweepAnimationFilter.initAnimationFilter(findViewById, x, i, this.mOnSweepListener, this.mCurrentSweepConfig);
                            }
                        }
                        findViewById = view;
                        if (this.mListView != null) {
                            selector = this.mListView.getSelector();
                            this.mListView.setPressed(false);
                            selector.jumpToCurrentState();
                        }
                        this.mSweepAnimationFilter.initAnimationFilter(findViewById, x, i, this.mOnSweepListener, this.mCurrentSweepConfig);
                    }
                }
            }
        }
    }

    public void onActionUp(MotionEvent motionEvent, View view, int i, boolean z) {
        float f = 0.0f;
        Object obj = null;
        if (mSkipActionUpAnimation) {
            mSkipActionUpAnimation = false;
            this.mSwiping = false;
            this.mSwipingPosition = -1;
            this.mListView.setEnabled(true);
            resetTouchState();
            if (this.mOnSweepListener != null) {
                this.mOnSweepListener.onSweepEnd(i, Math.signum(this.mSweepAnimationFilter.getEndXOfActionUpAnimator()));
            }
            return;
        }
        if (this.mSwiping) {
            if (view == null) {
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "SemSweepListAnimator : onActionUp : viewForeground = " + view);
                }
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "SemSweepListAnimator : **** End onActionUp *****, return #1");
                }
                return;
            }
            float translationX = view.getTranslationX();
            if (this.mSweepAnimationType == 2) {
                f = motionEvent.getX();
            } else if (this.mSweepAnimationType == 1) {
                f = motionEvent.getX();
            }
            float f2 = f - this.mDownX;
            final int width = view.getWidth();
            float adjustedVelocityX = getAdjustedVelocityX(this.mHistoricalVelocities);
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : viewForeground = " + view);
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : adjustedVelocityX = " + adjustedVelocityX);
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : mScaledTouchSlop = " + this.mScaledTouchSlop);
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : deltaX = " + f2);
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : isSweepPattern = " + z);
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : mSweepAnimationFilter = " + this.mSweepAnimationFilter);
            }
            if (this.mSweepAnimationFilter == null) {
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "SemSweepListAnimator : onActionUp : mSweepAnimationFilter is null");
                }
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "SemSweepListAnimator : **** End onActionUp *****, return #2");
                }
                return;
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : create sweepAnimation.. #1");
            }
            Animator createActionUpAnimator = this.mSweepAnimationFilter.createActionUpAnimator(view, adjustedVelocityX, this.mScaledTouchSlop, f2, z);
            final int i2 = i;
            createActionUpAnimator.addListener(new AnimatorListenerAdapter() {

                class C09791 implements AnimatorUpdateListener {
                    C09791() {
                    }

                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        if (SemSweepListAnimator.this.mSweepBdToFade != null && SemSweepListAnimator.this.mListView != null) {
                            SemSweepListAnimator.this.mSweepBdToFade.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
                            SemSweepListAnimator.this.mListView.invalidate(SemSweepListAnimator.this.mSweepBdToFade.getBounds());
                        }
                    }
                }

                public void onAnimationEnd(Animator animator) {
                    if (SemSweepListAnimator.this.mSweepAnimationFilter == null || SemSweepListAnimator.this.mSweepAnimationFilter.isAnimationBack() || SemSweepListAnimator.this.mSweepAnimationType != 2) {
                        if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                            Log.m29d(SemSweepListAnimator.TAG, "animator : onAnimationEnd : Animation is back, call resetSweepInfo()");
                        }
                        SemSweepListAnimator.this.resetSweepInfo();
                        if (SemSweepListAnimator.this.mOnSweepListener != null) {
                            if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                Log.m29d(SemSweepListAnimator.TAG, "animator : onAnimationEnd : send onSweepEnd");
                            }
                            SemSweepListAnimator.this.mOnSweepListener.onSweepEnd(i2, Math.signum(SemSweepListAnimator.this.mSweepAnimationFilter.getEndXOfActionUpAnimator()));
                        }
                    } else {
                        if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                            Log.m29d(SemSweepListAnimator.TAG, "SemSweepListAnimator : onActionUp : animator : onAnimationEnd : prepare copy bitmap to animate fade.. ");
                        }
                        Drawable sweepBitmapDrawable = ((SemSweepTranslationFilter) SemSweepListAnimator.this.mSweepAnimationFilter).getSweepBitmapDrawable();
                        final Bitmap copy = sweepBitmapDrawable.getBitmap().copy(Config.ARGB_8888, true);
                        SemSweepListAnimator.this.mSweepBdToFade = new BitmapDrawable(SemSweepListAnimator.this.mContext.getResources(), copy);
                        SemSweepListAnimator.this.mSweepBdToFade.setBounds(sweepBitmapDrawable.getBounds());
                        if (SemSweepListAnimator.this.mSweepBdToFade != null) {
                            if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                Log.m29d(SemSweepListAnimator.TAG, "animator : create fadeOut animator #2");
                            }
                            if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                Log.m29d(SemSweepListAnimator.TAG, "animator : sweepBdToFade = " + SemSweepListAnimator.this.mSweepBdToFade);
                            }
                            Animator ofInt = ValueAnimator.ofInt(new int[]{255, 0});
                            ofInt.setDuration(300);
                            ofInt.addUpdateListener(new C09791());
                            final int i = i2;
                            ofInt.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animator) {
                                    if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                        Log.m29d(SemSweepListAnimator.TAG, "fadeOutAnimator : onAnimationEnd");
                                    }
                                    SemSweepListAnimator.this.resetSweepInfo();
                                    if (SemSweepListAnimator.this.mOnSweepListener != null) {
                                        if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                            Log.m29d(SemSweepListAnimator.TAG, "fadeOutAnimator : onAnimationEnd : send onSweepEnd");
                                        }
                                        SemSweepListAnimator.this.mOnSweepListener.onSweepEnd(i, Math.signum(SemSweepListAnimator.this.mSweepAnimationFilter.getEndXOfActionUpAnimator()));
                                    }
                                    if (SemSweepListAnimator.this.mSweepBdToFade != null) {
                                        Bitmap bitmap = SemSweepListAnimator.this.mSweepBdToFade.getBitmap();
                                        if (bitmap != null) {
                                            if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                                Log.m29d(SemSweepListAnimator.TAG, "fadeOutAnimator : onAnimationEnd : recycle mSweepBdToFade");
                                            }
                                            bitmap.recycle();
                                        }
                                        SemSweepListAnimator.this.mSweepBdToFade = null;
                                    }
                                    if (copy != null) {
                                        if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                            Log.m29d(SemSweepListAnimator.TAG, "fadeOutAnimator : onAnimationEnd : recycle copiedBitmap");
                                        }
                                        copy.recycle();
                                    }
                                }

                                public void onAnimationStart(Animator animator) {
                                    if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                        Log.m29d(SemSweepListAnimator.TAG, "fadeOutAnimator : onAnimationStart");
                                    }
                                }
                            });
                            if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                                Log.m29d(SemSweepListAnimator.TAG, "animator : onAnimationEnd : fadeOutAnimator.start()");
                            }
                            ofInt.start();
                        }
                    }
                    if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                        Log.m29d(SemSweepListAnimator.TAG, "animator : onAnimationEnd : call resetSweepAnimatinFilter ");
                    }
                    SemSweepListAnimator.this.resetSweepAnimatinFilter();
                }

                public void onAnimationStart(Animator animator) {
                    if (SemSweepListAnimator.DEBUGGABLE_LOW) {
                        Log.m29d(SemSweepListAnimator.TAG, "animator : onAnimationStart");
                    }
                }
            });
            if (this.mOnSweepListener != null) {
                final View view2 = view;
                final int i3 = i;
                createActionUpAnimator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float f = -1.0f;
                        if (SemSweepListAnimator.this.mSweepAnimationType == 2) {
                            f = view2.getTranslationX() / ((float) width);
                        } else if (SemSweepListAnimator.this.mSweepAnimationType == 1) {
                            f = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                        }
                        SemSweepListAnimator.this.mSweepAnimationFilter.doUpActionWhenAnimationUpdate(i3, f);
                    }
                });
            }
            this.mListView.setEnabled(false);
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "SemSweepListAnimator : onActionUp : call animator.start()");
            }
            createActionUpAnimator.start();
            Drawable selector = this.mListView.getSelector();
            if ((selector instanceof StateListDrawable) && this.mSweepAnimationType == 2) {
                Drawable current = selector.getCurrent();
                if (current instanceof RippleDrawable) {
                    current.jumpToCurrentState();
                }
            }
            obj = 1;
        }
        resetTouchState();
        if (!this.mSwiping && r8 == null) {
            resetSweepAnimatinFilter();
        }
    }

    public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public void setForegroundViewResId(int i) {
        this.mForegroundViewResId = i;
    }

    public void setOnSweepListener(OnSweepListener onSweepListener) {
        this.mOnSweepListener = onSweepListener;
    }

    public void setSweepAnimationType(int i) {
        this.mSweepAnimationType = i;
        switch (this.mSweepAnimationType) {
            case 1:
                this.mSweepAnimationFilter = new SemSweepWaveFilter(this.mListView);
                return;
            case 2:
                this.mSweepAnimationFilter = new SemSweepTranslationFilter(this.mListView, this.mContext);
                return;
            default:
                return;
        }
    }

    public void setSweepAnimatorEnabled(boolean z) {
        this.mEnableSweep = z;
    }
}
