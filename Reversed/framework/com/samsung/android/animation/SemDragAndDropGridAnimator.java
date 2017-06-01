package com.samsung.android.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import com.android.internal.C0717R;
import com.samsung.android.animation.SemAbsDragAndDropAnimator.DragAndDropController;
import java.util.HashSet;

public class SemDragAndDropGridAnimator extends SemAbsDragAndDropAnimator {
    private static final String TAG = "SemDragAndDropGridAnimator";
    private GridView mGridView;
    private ItemAnimationListener mItemAnimationListener;
    private int mItemHeight;
    private int mItemWidth;
    HashSet<Integer> mNonMovableItems = new HashSet();
    private OnItemLongClickListener mOnItemLongClickListener;

    class C09581 implements ItemAnimationListener {
        C09581() {
        }

        public void onItemAnimatorEnd() {
            if (SemDragAndDropGridAnimator.this.mListItemSelectionAnimating) {
                SemDragAndDropGridAnimator.this.mListItemSelectionAnimating = false;
                SemDragAndDropGridAnimator.this.updateDragViewBitmap();
                return;
            }
            if (SemDragAndDropGridAnimator.this.mDropDonePending) {
                SemDragAndDropGridAnimator.this.mDropDonePending = false;
                if (SemDragAndDropGridAnimator.this.mDragPos != SemDragAndDropGridAnimator.this.mFirstDragPos) {
                    SemDragAndDropGridAnimator.this.mDndController.dropDone(SemDragAndDropGridAnimator.this.mFirstDragPos, SemDragAndDropGridAnimator.this.mDragPos);
                    SemDragAndDropGridAnimator.this.speakDragReleaseForAccessibility(SemDragAndDropGridAnimator.this.mDragPos);
                }
                SemDragAndDropGridAnimator.this.mItemAnimator.removeAll();
                SemDragAndDropGridAnimator.this.resetDndPositionValues();
                if (SemDragAndDropGridAnimator.this.mDndListener != null) {
                    Log.m29d(SemDragAndDropGridAnimator.TAG, "dndListener.onDragAndDropEnd() from onAllAnimationsFinished()");
                    SemDragAndDropGridAnimator.this.mDndListener.onDragAndDropEnd();
                }
                SemDragAndDropGridAnimator.this.mGridView.setEnabled(true);
            }
        }
    }

    class C09592 implements OnItemLongClickListener {
        C09592() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            return SemDragAndDropGridAnimator.this.initDragIfNecessary(i);
        }
    }

    class C09614 extends AnimatorListenerAdapter {
        C09614() {
        }

        public void onAnimationEnd(Animator animator) {
            if (SemDragAndDropGridAnimator.this.mFirstDragPos != SemDragAndDropGridAnimator.this.mDragPos) {
                SemDragAndDropGridAnimator.this.mDndController.dropDone(SemDragAndDropGridAnimator.this.mFirstDragPos, SemDragAndDropGridAnimator.this.mDragPos);
                SemDragAndDropGridAnimator.this.speakDragReleaseForAccessibility(SemDragAndDropGridAnimator.this.mDragPos);
            }
            SemDragAndDropGridAnimator.this.mItemAnimator.removeAll();
            SemDragAndDropGridAnimator.this.resetDndState();
            if (SemDragAndDropGridAnimator.this.mDndListener != null) {
                Log.m29d(SemDragAndDropGridAnimator.TAG, "dndListener.onDragAndDropEnd() from AnimationEnd");
                SemDragAndDropGridAnimator.this.mDndListener.onDragAndDropEnd();
            }
        }
    }

    public SemDragAndDropGridAnimator(Context context, GridView gridView) {
        super(context, gridView);
        this.mGridView = gridView;
        this.mGridView.setDndGridAnimator(this);
        this.mItemWidth = Integer.MIN_VALUE;
        this.mItemHeight = Integer.MIN_VALUE;
        initListeners();
        this.mDndAnimationCore.setAnimationListener(this.mItemAnimationListener);
        this.mGridView.setOnItemLongClickListener(this.mOnItemLongClickListener);
        this.mGridView.setSelector((int) C0717R.color.transparent);
    }

    private void addNewTranslation(int i, int i2, int i3) {
        ItemAnimation itemAnimation = this.mItemAnimator.getItemAnimation(i);
        ItemAnimation translateItemAnimation = itemAnimation instanceof TranslateItemAnimation ? itemAnimation : new TranslateItemAnimation();
        int destOffsetX = translateItemAnimation.getDestOffsetX();
        int destOffsetY = translateItemAnimation.getDestOffsetY();
        int i4 = 0;
        int i5 = 0;
        if (!translateItemAnimation.isFinished()) {
            i4 = (int) translateItemAnimation.getCurrentTranslateX();
            i5 = (int) translateItemAnimation.getCurrentTranslateY();
        }
        if (translateItemAnimation.isFinished()) {
            translateItemAnimation.setStartAndDuration(0);
        } else {
            translateItemAnimation.setStartAndDuration(translateItemAnimation.getProgress());
        }
        int i6 = i2 + destOffsetX;
        int i7 = i3 + destOffsetY;
        translateItemAnimation.translate(i6, i6 - i4, i7, i7 - i5);
        this.mItemAnimator.putItemAnimation(i, translateItemAnimation);
    }

    private void addReturningTranslation(int i) {
        ItemAnimation itemAnimation;
        ItemAnimation itemAnimation2 = this.mItemAnimator.getItemAnimation(i);
        int i2 = 0;
        int i3 = 0;
        if (itemAnimation2 instanceof TranslateItemAnimation) {
            itemAnimation = itemAnimation2;
            i2 = (int) itemAnimation2.getCurrentTranslateX();
            i3 = (int) itemAnimation2.getCurrentTranslateY();
        } else {
            itemAnimation = new TranslateItemAnimation();
        }
        itemAnimation.translate(0, -i2, 0, -i3);
        itemAnimation.setStartAndDuration(itemAnimation.getProgress());
        this.mItemAnimator.putItemAnimation(i, itemAnimation);
    }

    private boolean checkDndGrabHandle(int i, int i2, int i3) {
        if (activatedByLongPress()) {
            return true;
        }
        Rect rect = new Rect();
        this.mGridView.getChildAt(i3 - this.mGridView.getFirstVisiblePosition()).getHitRect(rect);
        getDragGrabHandleHitRect(rect, this.mTempRect);
        return this.mTempRect.contains(i, i2);
    }

    private boolean checkStartDnd(int i, int i2, int i3) {
        if (!checkDndGrabHandle(i, i2, i3)) {
            return false;
        }
        boolean canDrag = this.mDndController.canDrag(i3);
        if (!canDrag) {
            speakNotDraggableForAccessibility(i3);
        }
        return canDrag;
    }

    private void drawDragHandle(Canvas canvas, Rect rect, boolean z, boolean z2) {
        if (this.mDragGrabHandleDrawable != null && z2) {
            getDragGrabHandleHitRect(rect, this.mTempRect);
            this.mDragGrabHandleDrawable.setBounds(this.mTempRect);
            this.mDragGrabHandleDrawable.setState(z ? PRESSED_STATE_SET : EMPTY_STATE_SET);
            this.mDragGrabHandleDrawable.setAlpha(this.mDragHandleAlpha);
            this.mDragGrabHandleDrawable.draw(canvas);
        }
    }

    private void drawDragHandlerIfNeeded(Canvas canvas, View view, long j) {
        if (isDraggable()) {
            int indexOfChild = this.mGridView.indexOfChild(view) + this.mGridView.getFirstVisiblePosition();
            if (this.mGridView.getAdapter().isEnabled(indexOfChild)) {
                view.getHitRect(this.mTempRect);
                drawDragHandle(canvas, this.mTempRect, false, this.mDndController.canDrag(indexOfChild));
            }
        }
    }

    private int findMovedItemIndex(View view) {
        int viewCenterX = SemAnimatorUtils.getViewCenterX(view);
        int viewCenterY = SemAnimatorUtils.getViewCenterY(view);
        int childCount = this.mGridView.getChildCount();
        int firstVisiblePosition = this.mGridView.getFirstVisiblePosition();
        if (childCount > 0) {
            for (int i = childCount - 1; i >= 0; i--) {
                this.mGridView.getChildAt(i).getHitRect(this.mTempRect);
                int i2 = 0;
                int i3 = 0;
                ItemAnimation itemAnimation = this.mItemAnimator.getItemAnimation(i + firstVisiblePosition);
                if (itemAnimation instanceof TranslateItemAnimation) {
                    i2 = itemAnimation.getDestOffsetX();
                    i3 = itemAnimation.getDestOffsetY();
                }
                if (i != this.mFirstDragPos - firstVisiblePosition && this.mTempRect.contains(viewCenterX - r6, viewCenterY - r8)) {
                    return i + firstVisiblePosition;
                }
            }
        }
        return -1;
    }

    private void findMovingArrage() {
        View childAt;
        View childAt2;
        if (this.mGridView.getCount() >= 2) {
            childAt = this.mGridView.getChildAt(0);
            childAt2 = this.mGridView.getChildAt(1);
            if (childAt != null && childAt2 != null) {
                Rect rect = new Rect();
                Rect rect2 = new Rect();
                childAt.getHitRect(rect);
                childAt2.getHitRect(rect2);
                this.mItemWidth = Math.abs(rect2.left - rect.left);
            } else {
                return;
            }
        }
        this.mItemWidth = 0;
        if (this.mGridView.getCount() > this.mGridView.getNumColumns()) {
            childAt = this.mGridView.getChildAt(0);
            childAt2 = this.mGridView.getChildAt(this.mGridView.getNumColumns());
            if (childAt != null && childAt2 != null) {
                rect = new Rect();
                rect2 = new Rect();
                childAt.getHitRect(rect);
                childAt2.getHitRect(rect2);
                this.mItemHeight = Math.abs(rect2.top - rect.top);
            } else {
                return;
            }
        }
        this.mItemHeight = 0;
    }

    private void getDragGrabHandleHitRect(Rect rect, Rect rect2) {
        if (this.mDragGrabHandleDrawable != null) {
            int intrinsicWidth = this.mDragGrabHandleDrawable.getIntrinsicWidth();
            int intrinsicHeight = this.mDragGrabHandleDrawable.getIntrinsicHeight();
            if (this.mGridView.isLayoutRtl()) {
                rect.left += this.mDragGrabHandlePadding.right;
                rect.top += this.mDragGrabHandlePadding.top;
                rect.right -= this.mDragGrabHandlePadding.left;
                rect.bottom += this.mDragGrabHandlePadding.bottom;
                Gravity.apply(this.mDragGrabHandlePosGravity, intrinsicWidth, intrinsicHeight, rect, rect2, 1);
                return;
            }
            rect.left += this.mDragGrabHandlePadding.left;
            rect.top += this.mDragGrabHandlePadding.top;
            rect.right += this.mDragGrabHandlePadding.right;
            rect.bottom += this.mDragGrabHandlePadding.bottom;
            Gravity.apply(this.mDragGrabHandlePosGravity, intrinsicWidth, intrinsicHeight, rect, rect2, 0);
        }
    }

    private boolean initDrag(int i) {
        findMovingArrage();
        this.mDragView = this.mGridView.getChildAt(i - this.mGridView.getFirstVisiblePosition());
        if (this.mDragView == null) {
            return false;
        }
        this.mGridView.setEnableHoverDrawable(false);
        this.mDndTouchMode = 2;
        this.mFirstDragPos = i;
        this.mDragPos = this.mFirstDragPos;
        this.mDragView.setPressed(false);
        this.mDragView.getHitRect(this.mDragViewRect);
        speakDragStartForAccessibility(i);
        if (this.mDragViewBitmap != null) {
            this.mDragViewBitmap.recycle();
        }
        updateDragViewBitmap();
        setDragViewAlpha(this.mDragViewBitmapAlpha);
        if (this.mDragViewBitmap != null) {
            this.mDndTouchOffsetX = this.mDndTouchX - this.mDragViewRect.left;
            this.mDndTouchOffsetY = this.mDndTouchY - this.mDragViewRect.top;
        }
        startSelectHighlightingAnimation(this.mDragView);
        if (this.mDndListener != null) {
            Log.m29d(TAG, "dndListener.OnDragAndDropStart()");
            this.mDndListener.onDragAndDropStart();
        }
        this.mGridView.invalidate();
        return true;
    }

    private boolean initDragIfNecessary(int i) {
        if (isDraggable() && activatedByLongPress() && this.mGridView.getCount() > 1) {
            if (i >= 0 && i < this.mGridView.getCount() && checkStartDnd(this.mDndTouchX, this.mDndTouchY, i)) {
                return initDrag(i);
            }
            resetDndState();
        }
        return false;
    }

    private void initListeners() {
        this.mItemAnimationListener = new C09581();
        this.mOnItemLongClickListener = new C09592();
    }

    private void onTouchMove(MotionEvent motionEvent) {
        this.mDndTouchX = (int) motionEvent.getX();
        this.mDndTouchY = (int) motionEvent.getY();
        if (this.mListItemSelectionAnimating && this.mScaleUpAndDownAnimation != null && !this.mScaleUpAndDownAnimation.isFinished() && ((float) Math.max(Math.abs(this.mDndTouchX - this.mFirstTouchX), Math.abs(this.mDndTouchY - this.mFirstTouchY))) > 15.0f) {
            this.mListItemSelectionAnimating = false;
            updateDragViewBitmap();
        }
        int paddingTop = this.mGridView.getPaddingTop();
        View childAt = this.mGridView.getChildAt(0);
        if (childAt != null) {
            paddingTop += childAt.getHeight() / 2;
        }
        int bottom = (this.mGridView.getBottom() - this.mGridView.getPaddingBottom()) - this.mGridView.getTop();
        childAt = this.mGridView.getChildAt(this.mGridView.getChildCount() - 1);
        if (childAt != null) {
            bottom -= childAt.getHeight() / 2;
        }
        Object obj = null;
        if (this.mDndTouchY > bottom || this.mDndTouchY < paddingTop) {
            obj = 1;
            if (this.mDndAutoScrollMode == 0) {
                this.mGridView.postOnAnimationDelayed(this.mAutoScrollRunnable, 150);
            }
            if (this.mDndTouchY > bottom) {
                this.mDndAutoScrollMode = 2;
            }
            if (this.mDndTouchY < paddingTop) {
                this.mDndAutoScrollMode = 1;
            }
        }
        if (obj == null) {
            this.mDndAutoScrollMode = 0;
        }
        if (this.mDndAutoScrollMode == 0) {
            this.mGridView.removeCallbacks(this.mAutoScrollRunnable);
        }
        reorderIfNeeded();
    }

    private void onTouchUpCancel(MotionEvent motionEvent) {
        if (this.mDndTouchMode == 1) {
            resetDndState();
            if (this.mDndListener != null) {
                Log.m29d(TAG, "dndListener.onDragAndDropEnd() DND_TOUCH_STATUS_START");
                this.mDndListener.onDragAndDropEnd();
            }
        }
        if (this.mDndTouchMode == 2) {
            int firstVisiblePosition = this.mGridView.getFirstVisiblePosition();
            int childCount = this.mGridView.getChildCount();
            View childAt = this.mGridView.getChildAt(this.mFirstDragPos - firstVisiblePosition);
            View childAt2 = this.mGridView.getChildAt(this.mDragPos - firstVisiblePosition);
            if (childAt == null || childAt2 == null) {
                int left;
                int top;
                int i = this.mDndTouchX - this.mDndTouchOffsetX;
                int i2 = this.mDndTouchY - this.mDndTouchOffsetY;
                if ((childAt2 != null ? 1 : null) != null) {
                    left = childAt2.getLeft() - i;
                    top = childAt2.getTop() - i2;
                } else {
                    int numColumns = this.mGridView.getNumColumns();
                    if (childCount < numColumns) {
                        Log.m31e(TAG, "Child cound (" + this.mGridView.getChildCount() + ") is smaller than column count (" + numColumns + ")");
                        resetDndState();
                        return;
                    } else if (this.mDragPos < firstVisiblePosition) {
                        left = this.mGridView.getChildAt(this.mDragPos % numColumns).getLeft() - i;
                        top = ((-this.mGridView.getPaddingTop()) - i2) - this.mDragViewRect.height();
                    } else {
                        left = this.mGridView.getChildAt((this.mGridView.getChildCount() + (this.mDragPos % numColumns)) - numColumns).getLeft() - i;
                        top = this.mGridView.getHeight() - i2;
                    }
                }
                final int i3 = top;
                final int i4 = left;
                Log.m35v(TAG, "dndListener.onTouchUp() dragView == null, distanceX=" + left + ", distanceY=" + top);
                Animator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                ofFloat.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        SemDragAndDropGridAnimator.this.mDragViewBitmapTranslateX = (int) (((float) i4) * valueAnimator.getAnimatedFraction());
                        SemDragAndDropGridAnimator.this.mDragViewBitmapTranslateY = (int) (((float) i3) * valueAnimator.getAnimatedFraction());
                        SemDragAndDropGridAnimator.this.mGridView.invalidate();
                    }
                });
                ofFloat.addListener(new C09614());
                ofFloat.setDuration(210);
                ofFloat.setInterpolator(SINE_IN_OUT_70);
                ofFloat.start();
            } else if (this.mListItemSelectionAnimating) {
                resetDndState();
                if (this.mDndListener != null) {
                    Log.m29d(TAG, "dndListener.onDragAndDropEnd() mListItemSelectionAnimating is true");
                    this.mDndListener.onDragAndDropEnd();
                }
            } else {
                int left2 = childAt2.getLeft() - childAt.getLeft();
                int top2 = childAt2.getTop() - childAt.getTop();
                int left3 = childAt2.getLeft() - (this.mDndTouchX - this.mDndTouchOffsetX);
                int top3 = childAt2.getTop() - (this.mDndTouchY - this.mDndTouchOffsetY);
                ItemAnimation translateItemAnimation = new TranslateItemAnimation();
                translateItemAnimation.translate(left2, left3, top2, top3);
                translateItemAnimation.setStartAndDuration(0.7f);
                this.mItemAnimator.putItemAnimation(this.mFirstDragPos, translateItemAnimation);
                this.mItemAnimator.start();
                this.mRetainFirstDragViewPos = this.mFirstDragPos - firstVisiblePosition;
                this.mGridView.setEnabled(false);
                this.mDropDonePending = true;
                resetDndTouchValuesAndBitmap();
            }
            this.mGridView.removeCallbacks(this.mAutoScrollRunnable);
            this.mGridView.invalidate();
        }
    }

    private void recalculateOffset(int i, int i2) {
        int firstVisiblePosition = this.mGridView.getFirstVisiblePosition();
        int numColumns = this.mGridView.getNumColumns();
        boolean isLayoutRtl = this.mGridView.isLayoutRtl();
        int i3;
        int i4;
        int i5;
        View childAt;
        if (i2 > i) {
            for (i3 = i + 1; i3 <= i2; i3++) {
                if (i3 > this.mFirstDragPos) {
                    if (this.mDndController.canDrop(this.mFirstDragPos, i3)) {
                        i4 = 0;
                        for (i5 = i3 - 1; this.mNonMovableItems.contains(Integer.valueOf(i5)); i5--) {
                            i4++;
                        }
                        int i6 = i3;
                        int i7 = (i3 - 1) - i4;
                        int i8 = (i7 % numColumns) - (i6 % numColumns);
                        addNewTranslation(i3, isLayoutRtl ? (this.mItemWidth * i8) * -1 : i8 * this.mItemWidth, ((i7 / numColumns) - (i6 / numColumns)) * this.mItemHeight);
                    } else {
                        this.mNonMovableItems.add(Integer.valueOf(i3));
                    }
                } else {
                    childAt = this.mGridView.getChildAt(i3 - firstVisiblePosition);
                    if (childAt == null) {
                        Log.m31e(TAG, "recalculateOffset('dragging down') no such item, i=" + i3);
                    } else {
                        addReturningTranslation(findMovedItemIndex(childAt));
                    }
                }
            }
            return;
        }
        for (i3 = i - 1; i3 >= i2; i3--) {
            if (i3 < this.mFirstDragPos) {
                if (this.mDndController.canDrop(this.mFirstDragPos, i3)) {
                    i4 = 0;
                    for (i5 = i3 + 1; this.mNonMovableItems.contains(Integer.valueOf(i5)); i5++) {
                        i4++;
                    }
                    i6 = i3;
                    i7 = (i3 + 1) + i4;
                    i8 = (i7 % numColumns) - (i6 % numColumns);
                    addNewTranslation(i3, isLayoutRtl ? (this.mItemWidth * i8) * -1 : i8 * this.mItemWidth, ((i7 / numColumns) - (i6 / numColumns)) * this.mItemHeight);
                } else {
                    this.mNonMovableItems.add(Integer.valueOf(i3));
                }
            } else {
                childAt = this.mGridView.getChildAt(i3 - firstVisiblePosition);
                if (childAt == null) {
                    Log.m31e(TAG, "recalculateOffset('dragging up') no such item, i=" + i3);
                } else {
                    addReturningTranslation(findMovedItemIndex(childAt));
                }
            }
        }
    }

    private void startSelectHighlightingAnimation(View view) {
        Rect rect = new Rect();
        view.getHitRect(rect);
        this.mListItemSelectionAnimating = true;
        this.mScaleUpAndDownAnimation = new ItemSelectHighlightingAnimation(rect);
        this.mScaleUpAndDownAnimation.setStartAndDuration(0);
        this.mItemAnimator.putItemAnimation(this.mFirstDragPos, this.mScaleUpAndDownAnimation);
        this.mItemAnimator.start();
    }

    private void updateDragViewBitmap() {
        if (this.mDragView != null) {
            this.mDragViewBitmap = SemAnimatorUtils.getBitmapDrawableFromView(this.mDragView).getBitmap();
        }
    }

    public void dispatchDraw(Canvas canvas) {
        if (isDraggable() && this.mDragViewBitmap != null && !this.mListItemSelectionAnimating) {
            int i = this.mDndTouchX - this.mDndTouchOffsetX;
            int i2 = this.mDndTouchY - this.mDndTouchOffsetY;
            canvas.drawBitmap(this.mDragViewBitmap, (float) (this.mDragViewBitmapTranslateX + i), (float) (this.mDragViewBitmapTranslateY + i2), this.mDragViewBitmapPaint);
            this.mTempRect.left = this.mDragViewBitmapTranslateX + i;
            this.mTempRect.top = this.mDragViewBitmapTranslateY + i2;
            this.mTempRect.bottom = this.mTempRect.top + this.mDragViewRect.height();
            this.mTempRect.right = this.mTempRect.left + this.mDragViewRect.width();
            drawDragHandle(canvas, this.mTempRect, true, true);
        }
    }

    public OnItemLongClickListener getDragAndDropOnItemLongClickListener() {
        return this.mOnItemLongClickListener;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                if (!this.mGridView.isEnabled()) {
                    return false;
                }
                this.mFirstTouchX = this.mDndTouchX;
                this.mFirstTouchY = this.mDndTouchY;
                this.mDndTouchX = (int) motionEvent.getX();
                this.mDndTouchY = (int) motionEvent.getY();
                if (isDraggable() && this.mGridView.getCount() > 1) {
                    int pointToPosition = this.mGridView.pointToPosition(this.mDndTouchX, this.mDndTouchY);
                    if (pointToPosition == -1 || activatedByLongPress()) {
                        return false;
                    }
                    if (pointToPosition < 0 || pointToPosition >= this.mGridView.getCount() || !checkStartDnd(this.mDndTouchX, this.mDndTouchY, pointToPosition)) {
                        resetDndState();
                        break;
                    } else if (initDrag(pointToPosition)) {
                        return true;
                    }
                }
                break;
            case 1:
            case 3:
                if (isDraggable() && this.mDndTouchMode != 0) {
                    onTouchUpCancel(motionEvent);
                    break;
                }
            case 2:
                return isDraggable() && this.mDndTouchMode == 1 && this.mGridView.getCount() > 1 && activatedByLongPress();
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        return this.mOnItemLongClickListener.onItemLongClick(adapterView, view, i, j);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isDraggable() || this.mDndTouchMode == 0) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 1:
            case 3:
                onTouchUpCancel(motionEvent);
                break;
            case 2:
                onTouchMove(motionEvent);
                break;
        }
        return true;
    }

    public void postDrawChild(Canvas canvas, View view, long j) {
        drawDragHandlerIfNeeded(canvas, view, j);
        if (this.mCanvasSaveCount > 0) {
            canvas.restoreToCount(this.mCanvasSaveCount);
        }
    }

    public boolean preDrawChild(Canvas canvas, View view, long j) {
        int indexOfChild = this.mGridView.indexOfChild(view) + this.mGridView.getFirstVisiblePosition();
        if (isDraggable() && indexOfChild == this.mFirstDragPos && !this.mDropDonePending && !this.mListItemSelectionAnimating) {
            return false;
        }
        ItemAnimation itemAnimation = this.mItemAnimator.getItemAnimation(indexOfChild);
        this.mCanvasSaveCount = 0;
        if (itemAnimation != null) {
            itemAnimation.getTransformation(this.mTempTrans);
            this.mCanvasSaveCount = canvas.save();
            canvas.concat(this.mTempTrans.getMatrix());
        }
        return true;
    }

    void reorderIfNeeded() {
        int i = this.mDragPos;
        int pointToPosition = this.mGridView.pointToPosition((this.mDndTouchX - this.mDndTouchOffsetX) + (this.mDragViewRect.width() / 2), (this.mDndTouchY - this.mDndTouchOffsetY) + (this.mDragViewRect.height() / 2));
        if (pointToPosition != -1) {
            if (this.mDndController.canDrop(this.mFirstDragPos, pointToPosition)) {
                this.mDragPos = pointToPosition;
            } else if (this.mDragPos > pointToPosition) {
                for (r3 = pointToPosition + 1; r3 < this.mDragPos; r3++) {
                    if (this.mDndController.canDrop(this.mFirstDragPos, r3)) {
                        this.mDragPos = r3;
                        break;
                    }
                }
            } else {
                for (r3 = pointToPosition - 1; r3 > this.mDragPos; r3--) {
                    if (this.mDndController.canDrop(this.mFirstDragPos, r3)) {
                        this.mDragPos = r3;
                        break;
                    }
                }
            }
        }
        if (i != this.mDragPos) {
            this.mListItemSelectionAnimating = false;
            recalculateOffset(i, this.mDragPos);
            this.mItemAnimator.start();
        }
        if (i != this.mDragPos || this.mDragViewBitmap != null) {
            this.mGridView.invalidate();
        }
    }

    void resetDndPositionValues() {
        super.resetDndPositionValues();
        this.mGridView.setEnableHoverDrawable(true);
    }

    void resetDndTouchValuesAndBitmap() {
        super.resetDndTouchValuesAndBitmap();
        this.mItemWidth = Integer.MIN_VALUE;
        this.mItemHeight = Integer.MIN_VALUE;
        this.mNonMovableItems.clear();
    }

    public void setDragAndDropController(DragAndDropController dragAndDropController) {
        this.mDndController = dragAndDropController;
    }
}
