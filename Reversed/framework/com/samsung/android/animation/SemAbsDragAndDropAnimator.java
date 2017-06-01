package com.samsung.android.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.Transformation;
import com.android.internal.C0717R;

public abstract class SemAbsDragAndDropAnimator {
    static final int BITMAP_ALPHA = 179;
    static final int DND_AUTO_SCROLL_DELTA_VALUE = 7;
    static final int DND_AUTO_SCROLL_END = 2;
    static final int DND_AUTO_SCROLL_FRAME_DELAY = 10;
    static final int DND_AUTO_SCROLL_NONE = 0;
    static final int DND_AUTO_SCROLL_START = 1;
    static final int DND_TOUCH_STATUS_MOVING = 2;
    static final int DND_TOUCH_STATUS_NON = 0;
    static final int DND_TOUCH_STATUS_START = 1;
    static final float DRAGGING_RELEASE_ANIM_DURATION_MULTIPLICATOR = 0.7f;
    static final int DRAG_HANDLE_FADE_DURATION = 200;
    static int[] EMPTY_STATE_SET = new int[0];
    static final Interpolator FADE_IN_INTERPOLATOR = new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f);
    static final Interpolator FADE_OUT_INTERPOLATOR = new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f);
    static final int INVALID_POINTER_ID = -1;
    static int[] PRESSED_STATE_SET = new int[]{C0717R.attr.state_pressed};
    static final float SCALEUPDOWNANIM_RESISTANCE = 15.0f;
    static final PathInterpolator SINE_IN_OUT_70 = new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f);
    private static final String TAG = SemAbsDragAndDropAnimator.class.getName();
    int mActivePointerId = -1;
    int mAutoScrollBottomDelta;
    SemDragAutoScrollListener mAutoScrollListener;
    AutoScrollRunnable mAutoScrollRunnable;
    int mAutoScrollTopDelta;
    int mCanvasSaveCount = 0;
    Context mContext;
    private final float mDensity;
    SemDragAndDropAnimationCore mDndAnimationCore;
    int mDndAutoScrollMode;
    DragAndDropController mDndController;
    DragAndDropListener mDndListener;
    boolean mDndMode;
    int mDndTouchMode;
    int mDndTouchOffsetX;
    int mDndTouchOffsetY;
    int mDndTouchX;
    int mDndTouchY;
    Drawable mDragGrabHandleDrawable;
    Rect mDragGrabHandlePadding;
    int mDragGrabHandlePosGravity;
    int mDragHandleAlpha = 255;
    int mDragPos;
    View mDragView;
    Bitmap mDragViewBitmap;
    int mDragViewBitmapAlpha;
    Paint mDragViewBitmapPaint;
    int mDragViewBitmapTranslateX = 0;
    int mDragViewBitmapTranslateY = 0;
    Rect mDragViewRect;
    boolean mDropDonePending = false;
    int mFirstDragPos;
    int mFirstTouchX;
    int mFirstTouchY;
    ItemAnimator mItemAnimator;
    boolean mListItemSelectionAnimating = false;
    int mRetainFirstDragViewPos = -1;
    ItemSelectHighlightingAnimation mScaleUpAndDownAnimation;
    MotionEvent mTempEvent;
    Rect mTempRect = new Rect();
    Transformation mTempTrans = new Transformation();
    boolean mUserSetDragItemBitmap = false;
    private View mView;

    public interface SemDragAutoScrollListener {
        void onAutoScroll(int i);
    }

    private class AutoScrollRunnable implements Runnable {
        private AutoScrollRunnable() {
        }

        public void run() {
            SemAbsDragAndDropAnimator.this.mListItemSelectionAnimating = false;
            int i = 0;
            if (SemAbsDragAndDropAnimator.this.mDndAutoScrollMode == 1) {
                i = SemAbsDragAndDropAnimator.this.mAutoScrollTopDelta;
            }
            if (SemAbsDragAndDropAnimator.this.mDndAutoScrollMode == 2) {
                i = SemAbsDragAndDropAnimator.this.mAutoScrollBottomDelta;
            }
            if (!(i == 0 || SemAbsDragAndDropAnimator.this.mAutoScrollListener == null)) {
                SemAbsDragAndDropAnimator.this.mAutoScrollListener.onAutoScroll(i);
            }
            SemAbsDragAndDropAnimator.this.reorderIfNeeded();
            if (SemAbsDragAndDropAnimator.this.mDndAutoScrollMode != 0) {
                SemAbsDragAndDropAnimator.this.mView.postOnAnimationDelayed(this, 10);
            }
        }
    }

    public interface DragAndDropController {
        boolean canDrag(int i);

        boolean canDrop(int i, int i2);

        void dropDone(int i, int i2);
    }

    public interface DragAndDropListener {
        void onDragAndDropEnd();

        void onDragAndDropStart();
    }

    public SemAbsDragAndDropAnimator(Context context, View view) {
        if (context == null || view == null) {
            throw new RuntimeException("SemDragAndDropGridAnimator constructor arguments cannot be null");
        }
        this.mContext = context;
        this.mView = view;
        this.mDndAnimationCore = new SemDragAndDropAnimationCore(view);
        this.mItemAnimator = this.mDndAnimationCore.itemAnimator;
        this.mDndMode = false;
        this.mFirstDragPos = -1;
        this.mDragPos = -1;
        this.mDndTouchX = Integer.MIN_VALUE;
        this.mDndTouchY = Integer.MIN_VALUE;
        this.mDndTouchOffsetX = Integer.MIN_VALUE;
        this.mDndTouchOffsetY = Integer.MIN_VALUE;
        this.mDndTouchMode = 0;
        this.mDensity = this.mContext.getResources().getDisplayMetrics().density;
        this.mDragView = null;
        this.mDragViewRect = new Rect();
        this.mDragViewBitmapPaint = new Paint();
        this.mDragViewBitmapAlpha = 179;
        this.mDragGrabHandleDrawable = null;
        this.mDragGrabHandlePosGravity = 21;
        this.mDragGrabHandlePadding = new Rect();
        this.mAutoScrollRunnable = new AutoScrollRunnable();
        this.mAutoScrollTopDelta = (int) (this.mDensity * 7.0f);
        this.mAutoScrollBottomDelta = (int) (this.mDensity * -7.0f);
    }

    private void setDndModeInternal(boolean z) {
        this.mDndMode = z;
        if (!z) {
            this.mItemAnimator.removeAll();
            resetDndState();
        }
        this.mView.invalidate();
    }

    boolean activatedByLongPress() {
        return this.mDragGrabHandleDrawable != null ? SemAnimatorUtils.isTalkBackEnabled(this.mContext) : true;
    }

    public int getChildDrawingOrder(int i, int i2) {
        if (this.mRetainFirstDragViewPos != -1) {
            if (i2 == this.mRetainFirstDragViewPos) {
                return i - 1;
            }
            if (i2 == i - 1) {
                return this.mRetainFirstDragViewPos <= i + -1 ? this.mRetainFirstDragViewPos : i - 1;
            }
        }
        return i2;
    }

    public int getDragGrabHandlePaddingBottom() {
        return this.mDragGrabHandleDrawable != null ? this.mDragGrabHandlePadding.bottom : Integer.MIN_VALUE;
    }

    public int getDragGrabHandlePaddingLeft() {
        return this.mDragGrabHandleDrawable != null ? this.mDragGrabHandlePadding.left : Integer.MIN_VALUE;
    }

    public int getDragGrabHandlePaddingRight() {
        return this.mDragGrabHandleDrawable != null ? this.mDragGrabHandlePadding.right : Integer.MIN_VALUE;
    }

    public int getDragGrabHandlePaddingTop() {
        return this.mDragGrabHandleDrawable != null ? this.mDragGrabHandlePadding.top : Integer.MIN_VALUE;
    }

    public View getDragView() {
        return isDraggable() ? this.mDragView : null;
    }

    public boolean isDraggable() {
        return this.mDndMode;
    }

    abstract void reorderIfNeeded();

    void resetDndPositionValues() {
        this.mFirstDragPos = -1;
        this.mDragPos = this.mFirstDragPos;
        this.mRetainFirstDragViewPos = -1;
    }

    void resetDndState() {
        resetDndTouchValuesAndBitmap();
        resetDndPositionValues();
    }

    void resetDndTouchValuesAndBitmap() {
        this.mDndTouchMode = 0;
        this.mDndTouchX = Integer.MIN_VALUE;
        this.mDndTouchY = Integer.MIN_VALUE;
        this.mFirstTouchX = Integer.MIN_VALUE;
        this.mFirstTouchY = Integer.MIN_VALUE;
        this.mDragViewBitmapTranslateX = 0;
        this.mDragViewBitmapTranslateY = 0;
        this.mDragView = null;
        if (this.mDragViewBitmap != null) {
            this.mDragViewBitmap.recycle();
            this.mDragViewBitmap = null;
        }
        this.mDndAutoScrollMode = 0;
        this.mView.removeCallbacks(this.mAutoScrollRunnable);
    }

    public void setAutoScrollListener(SemDragAutoScrollListener semDragAutoScrollListener) {
        this.mAutoScrollListener = semDragAutoScrollListener;
    }

    public void setDragAndDropEventListener(DragAndDropListener dragAndDropListener) {
        this.mDndListener = dragAndDropListener;
    }

    public void setDragGrabHandleDrawable(int i) {
        setDragGrabHandleDrawable(this.mContext.getResources().getDrawable(i));
    }

    public void setDragGrabHandleDrawable(Drawable drawable) {
        this.mDragGrabHandleDrawable = drawable;
    }

    public void setDragGrabHandlePadding(int i, int i2, int i3, int i4) {
        if (this.mDragGrabHandleDrawable != null) {
            this.mDragGrabHandlePadding.left = i;
            this.mDragGrabHandlePadding.top = i2;
            this.mDragGrabHandlePadding.right = i3;
            this.mDragGrabHandlePadding.bottom = i4;
        }
    }

    public void setDragGrabHandlePositionGravity(int i) {
        this.mDragGrabHandlePosGravity = i;
    }

    public void setDragItemBitmap(Bitmap bitmap) {
        if (isDraggable()) {
            if (this.mDragViewBitmap != null) {
                this.mDragViewBitmap.recycle();
            }
            this.mDragViewBitmap = bitmap;
            this.mUserSetDragItemBitmap = true;
        }
    }

    public void setDragViewAlpha(int i) {
        if (this.mDragViewBitmapPaint != null) {
            this.mDragViewBitmapAlpha = i;
            this.mDragViewBitmapPaint.setAlpha(i);
        }
    }

    public void setDraggable(boolean z) {
        if (this.mDndController == null) {
            throw new RuntimeException("You must specify dndController to activate Drag&Drop.");
        } else if (!this.mView.isAttachedToWindow() || this.mDragGrabHandleDrawable == null) {
            setDndModeInternal(z);
        } else {
            if (this.mDndMode != z) {
                final boolean z2 = this.mDndMode;
                if (!z2) {
                    setDndModeInternal(true);
                    this.mDragHandleAlpha = 0;
                }
                Animator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                ofFloat.setDuration(200);
                ofFloat.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedFraction = valueAnimator.getAnimatedFraction();
                        if (z2) {
                            SemAbsDragAndDropAnimator.this.mDragHandleAlpha = (int) ((1.0f - animatedFraction) * 255.0f);
                        } else {
                            SemAbsDragAndDropAnimator.this.mDragHandleAlpha = (int) (255.0f * animatedFraction);
                        }
                        SemAbsDragAndDropAnimator.this.mView.invalidate();
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        if (z2) {
                            SemAbsDragAndDropAnimator.this.setDndModeInternal(false);
                        }
                        SemAbsDragAndDropAnimator.this.mDragHandleAlpha = 255;
                        SemAbsDragAndDropAnimator.this.mView.setEnabled(true);
                    }

                    public void onAnimationStart(Animator animator) {
                        SemAbsDragAndDropAnimator.this.mView.setEnabled(false);
                    }
                });
                ofFloat.setInterpolator(z2 ? FADE_OUT_INTERPOLATOR : FADE_IN_INTERPOLATOR);
                ofFloat.start();
            }
        }
    }

    public void speakDescriptionForAccessibility() {
        if (!SemAnimatorUtils.isTalkBackEnabled(this.mContext) || this.mView.getVisibility() != 0 || !isDraggable()) {
            return;
        }
        if (AccessibilityManager.getInstance(this.mContext).isTouchExplorationEnabled()) {
            this.mView.announceForAccessibility(this.mContext.getResources().getString(C0717R.string.dragndroplist_description_exp_by_touch));
        } else {
            this.mView.announceForAccessibility(this.mContext.getResources().getString(C0717R.string.dragndroplist_description));
        }
    }

    void speakDragReleaseForAccessibility(int i) {
        this.mView.announceForAccessibility(this.mContext.getResources().getString(C0717R.string.dragndroplist_drag_release, new Object[]{Integer.valueOf(i + 1)}));
    }

    void speakDragStartForAccessibility(int i) {
        this.mView.announceForAccessibility(this.mContext.getResources().getString(C0717R.string.dragndroplist_drag_start, new Object[]{Integer.valueOf(i + 1)}));
        this.mView.clearAccessibilityFocus();
    }

    void speakNotDraggableForAccessibility(int i) {
        this.mView.announceForAccessibility(this.mContext.getResources().getString(C0717R.string.dragndroplist_item_cannot_be_dragged, new Object[]{Integer.valueOf(i + 1)}));
    }
}
