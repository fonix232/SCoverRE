package com.samsung.android.animation;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ListView;
import java.util.HashMap;

abstract class SemAbsSweepListAnimator {
    private static final boolean DEBUGGABLE_LOW = Debug.semIsProductDev();
    private static final int DIRECTION_LEFT_TO_RIGHT = 0;
    private static final int DIRECTION_RIGHT_TO_LEFT = 1;
    protected static int HISTORICAL_VELOCITY_COUNT = 4;
    private static int INVALID_POINTER_ID = -1;
    protected static final int MOVE_DURATION = 150;
    protected static final int SWIPE_DURATION = 600;
    private static final String TAG = "SemAbsSweepListAnimator";
    protected static int VELOCITY_UNITS = 500;
    protected static Interpolator sAccelDecel = new AccelerateDecelerateInterpolator();
    protected static Interpolator sDecel = new DecelerateInterpolator();
    private final boolean DEBUGGABLE = false;
    private float SWEEP_OPPOSITE_DIRECTION_DISTANCES_RATIO = 0.2f;
    private float downX;
    private float downY;
    protected int mActivePointerId = INVALID_POINTER_ID;
    protected int mCurrentFirstVisiblePos = -1;
    protected int mCurrentLastVisiblePos = -1;
    protected int mCurrentPosition = -1;
    protected float mDownX;
    protected View mForegroundView = null;
    protected int mForegroundViewResId;
    protected float[] mHistoricalVelocities = new float[HISTORICAL_VELOCITY_COUNT];
    protected int mHistoricalVelocityIndex = 0;
    protected HashMap<Long, Integer> mItemIdTopMap = new HashMap();
    protected boolean mItemPressed = false;
    protected ListView mListView;
    private int mPrevSweepDirection = -1;
    protected int mScaledTouchSlop = -1;
    private int mSweepDirection = -1;
    private float mSweepLeftDistance = 0.0f;
    private float mSweepPrevPosX = -1.0f;
    private float mSweepRightDistance = 0.0f;
    protected boolean mSwiping = false;
    protected int mSwipingPosition = -1;
    protected VelocityTracker mVelocityTracker;
    protected View mViewToRemoveFg;
    private float upX;
    private float upY;

    SemAbsSweepListAnimator() {
    }

    private void addVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
    }

    private float calculateDistanceX(MotionEvent motionEvent) {
        return Math.abs(this.mDownX - motionEvent.getX());
    }

    private View findTouchedView(MotionEvent motionEvent) {
        View childAt = this.mListView.getChildAt(this.mListView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY()) - this.mListView.getFirstVisiblePosition());
        return childAt != null ? childAt instanceof ViewGroup ? childAt.findViewById(this.mForegroundViewResId) : childAt : null;
    }

    private String getCurrentSweepDirection(int i) {
        switch (i) {
            case 0:
                return "Left to Right";
            case 1:
                return "Right to Left";
            default:
                return "No direction";
        }
    }

    private boolean handleTouchCancelEvent(MotionEvent motionEvent) {
        onActionCancel(motionEvent, this.mForegroundView);
        initSweepDistanceVariables();
        if (!this.mSwiping) {
            return false;
        }
        this.mSwiping = false;
        this.mListView.removePendingCallbacks();
        return true;
    }

    private boolean handleTouchDownEvent(MotionEvent motionEvent) {
        if (this.mListView == null) {
            return false;
        }
        this.mForegroundView = findTouchedView(motionEvent);
        if (this.mForegroundView == null) {
            return false;
        }
        this.mCurrentPosition = this.mListView.getPositionForView(this.mForegroundView);
        if (this.mCurrentPosition == -1 || this.mCurrentPosition < this.mListView.getFirstVisiblePosition() || this.mCurrentPosition > this.mListView.getLastVisiblePosition()) {
            return false;
        }
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        onActionDown(motionEvent);
        return true;
    }

    private boolean handleTouchMoveEvent(MotionEvent motionEvent) {
        if (this.mListView != null && (this.mCurrentPosition < this.mListView.getFirstVisiblePosition() || this.mCurrentPosition > this.mListView.getLastVisiblePosition())) {
            return false;
        }
        if (this.mListView != null) {
            boolean z = this.mListView.mSemFastScrollEffectState;
            Object obj = this.mListView.semGetLastScrollState() == 0 ? 1 : null;
            if (z || obj == null) {
                return false;
            }
        }
        this.upX = motionEvent.getX();
        this.upY = motionEvent.getY();
        float f = this.downX - this.upX;
        float f2 = this.downY - this.upY;
        double cos = Math.cos(Math.abs(((double) f) / Math.sqrt((double) (Math.abs(f * f) + Math.abs(f2 * f2)))));
        if (this.mSwiping) {
            onActionMove(motionEvent, this.mForegroundView, this.mCurrentPosition);
            trackSweepDistanceAndDirection(motionEvent);
            return true;
        } else if (((int) (1000.0d * cos)) >= 570) {
            return false;
        } else {
            onActionMove(motionEvent, this.mForegroundView, this.mCurrentPosition);
            return true;
        }
    }

    private void handleTouchUpEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerId(motionEvent.getActionIndex()) == this.mActivePointerId && this.mItemPressed) {
            onActionUp(motionEvent, this.mForegroundView, this.mCurrentPosition, sweepPatternIsIndeedFling(this.mVelocityTracker.getXVelocity()));
        }
        this.mCurrentPosition = -1;
        initSweepDistanceVariables();
    }

    private void initSweepDistanceVariables() {
        this.mSweepLeftDistance = 0.0f;
        this.mSweepRightDistance = 0.0f;
        this.mSweepPrevPosX = -1.0f;
        this.mSweepDirection = -1;
    }

    private boolean isTouchEventSkipped() {
        return (this.mSwiping && this.mSwipingPosition != this.mCurrentPosition) || !this.mListView.isEnabled();
    }

    private boolean sweepPatternIsIndeedFling(float f) {
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "***** Start sweepPatternIsIndeedFling *****");
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "sweepPatternIsIndeedFling : velocity =" + f);
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "sweepPatternIsIndeedFling : mSweepRightDistance = " + this.mSweepRightDistance);
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "sweepPatternIsIndeedFling : mSweepLeftDistance = " + this.mSweepLeftDistance);
        }
        int i = this.mScaledTouchSlop * 2;
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "sweepPatternIsIndeedFling : minimalDistanceThreshold = " + i);
        }
        if ((f <= 0.0f || this.mSweepRightDistance >= ((float) i)) && (f >= 0.0f || this.mSweepLeftDistance >= ((float) i))) {
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "sweepPatternIsIndeedFling : return true #2");
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "***** End sweepPatternIsIndeedFling *****");
            }
            return true;
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "sweepPatternIsIndeedFling : SweepDistance is less than minDistance, return false #1");
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d(TAG, "***** End sweepPatternIsIndeedFling *****");
        }
        return false;
    }

    private void trackSweepDistanceAndDirection(MotionEvent motionEvent) {
        if (this.mSweepPrevPosX == -1.0f) {
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "trackSweepDistanceAndDirection : first calling trackSweepDistanceAndDirection");
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "trackSweepDistanceAndDirection : mSweepPrevPosX is set to mDownX, mSweepPrevPosX = " + this.mDownX);
            }
            this.mSweepPrevPosX = this.mDownX;
        }
        if (this.mSweepPrevPosX != -1.0f) {
            if (this.mSweepPrevPosX > motionEvent.getX()) {
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "trackSweepDistanceAndDirection : sweep to left");
                }
                this.mSweepDirection = 1;
                this.mSweepLeftDistance += this.mSweepPrevPosX - motionEvent.getX();
            } else if (this.mSweepPrevPosX < motionEvent.getX()) {
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "trackSweepDistanceAndDirection : sweep to right");
                }
                this.mSweepDirection = 0;
                this.mSweepRightDistance += motionEvent.getX() - this.mSweepPrevPosX;
            }
        }
        if (!(this.mPrevSweepDirection == -1 || this.mPrevSweepDirection == this.mSweepDirection)) {
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "trackSweepDistanceAndDirection : SweepDirection is changed");
            }
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "trackSweepDistanceAndDirection : changed direction = " + getCurrentSweepDirection(this.mSweepDirection));
            }
            if (this.mSweepDirection == 1) {
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "trackSweepDistanceAndDirection : Set mSweepRightDistance = 0");
                }
                this.mSweepRightDistance = 0.0f;
            } else if (this.mSweepDirection == 0) {
                if (DEBUGGABLE_LOW) {
                    Log.m29d(TAG, "trackSweepDistanceAndDirection : Set mSweepLeftDistance = 0");
                }
                this.mSweepLeftDistance = 0.0f;
            }
            this.mVelocityTracker.clear();
            for (int i = 0; i < this.mHistoricalVelocities.length; i++) {
                this.mHistoricalVelocities[i] = 0.0f;
            }
            this.mHistoricalVelocityIndex = 0;
            if (DEBUGGABLE_LOW) {
                Log.m29d(TAG, "trackSweepDistanceAndDirection : Clear velocityTracker");
            }
        }
        this.mPrevSweepDirection = this.mSweepDirection;
        this.mSweepPrevPosX = motionEvent.getX();
    }

    protected float getAdjustedVelocityX(float[] fArr) {
        if (this.mHistoricalVelocityIndex == 0) {
            return 0.0f;
        }
        float f = 0.0f;
        int i = 0;
        for (int i2 = 0; i2 < HISTORICAL_VELOCITY_COUNT; i2++) {
            float f2 = fArr[((this.mHistoricalVelocityIndex - 1) + i2) % HISTORICAL_VELOCITY_COUNT];
            if (f2 != 0.0f) {
                f += 1.0f * f2;
                i++;
            }
        }
        return f / ((float) i);
    }

    protected BitmapDrawable getBitmapDrawableFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        Drawable bitmapDrawable = new BitmapDrawable(this.mListView.getResources(), createBitmap);
        bitmapDrawable.setBounds(new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
        return bitmapDrawable;
    }

    abstract void onActionCancel(MotionEvent motionEvent, View view);

    abstract void onActionDown(MotionEvent motionEvent);

    abstract void onActionMove(MotionEvent motionEvent, View view, int i);

    abstract void onActionUp(MotionEvent motionEvent, View view, int i, boolean z);

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (isTouchEventSkipped()) {
            return false;
        }
        addVelocityTracker(motionEvent);
        switch (motionEvent.getAction()) {
            case 0:
                this.mDownX = motionEvent.getX();
                if (!handleTouchDownEvent(motionEvent)) {
                    return false;
                }
                break;
            case 1:
            case 6:
                handleTouchUpEvent(motionEvent);
                break;
            case 2:
                if (calculateDistanceX(motionEvent) > ((float) this.mScaledTouchSlop)) {
                    return true;
                }
                break;
            case 3:
                handleTouchCancelEvent(motionEvent);
                break;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (isTouchEventSkipped()) {
            return false;
        }
        addVelocityTracker(motionEvent);
        switch (motionEvent.getAction()) {
            case 1:
            case 6:
                handleTouchUpEvent(motionEvent);
                break;
            case 2:
                z = handleTouchMoveEvent(motionEvent);
                break;
            case 3:
                z = handleTouchCancelEvent(motionEvent);
                break;
        }
        return z;
    }

    protected void resetTouchState() {
        this.mItemPressed = false;
        this.mActivePointerId = INVALID_POINTER_ID;
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        for (int i = 0; i < this.mHistoricalVelocities.length; i++) {
            this.mHistoricalVelocities[i] = 0.0f;
        }
        this.mHistoricalVelocityIndex = 0;
    }

    abstract void setForegroundViewResId(int i);

    protected void showForeground(View view) {
        if (view != null) {
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setVisibility(0);
        }
    }
}
