package com.samsung.android.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SemHorizontalListView;
import com.samsung.android.animation.SemAbsDragAndDropAnimator.DragAndDropController;

public class SemDragAndDropHorizontalListAnimator extends SemAbsDragAndDropAnimator {
    private static final String TAG = "SemDragAndDropHorizontalListAnimator";
    private ItemAnimationListener mItemAnimationListener;
    private SemHorizontalListView mListView;
    SparseIntArray mNonMovableItems = new SparseIntArray();
    private OnItemLongClickListener mOnItemLongClickListener;
    private final int mScrollBarSize = 10;

    class C09621 implements ItemAnimationListener {
        C09621() {
        }

        public void onItemAnimatorEnd() {
            if (SemDragAndDropHorizontalListAnimator.this.mListItemSelectionAnimating) {
                SemDragAndDropHorizontalListAnimator.this.mListItemSelectionAnimating = false;
                return;
            }
            if (SemDragAndDropHorizontalListAnimator.this.mDropDonePending) {
                SemDragAndDropHorizontalListAnimator.this.mDropDonePending = false;
                if (SemDragAndDropHorizontalListAnimator.this.mDndController != null) {
                    SemDragAndDropHorizontalListAnimator.this.mDndController.dropDone(SemDragAndDropHorizontalListAnimator.this.mFirstDragPos, SemDragAndDropHorizontalListAnimator.this.mDragPos);
                    SemDragAndDropHorizontalListAnimator.this.speakDragReleaseForAccessibility(SemDragAndDropHorizontalListAnimator.this.mDragPos);
                }
                SemDragAndDropHorizontalListAnimator.this.mItemAnimator.removeAll();
                SemDragAndDropHorizontalListAnimator.this.resetDndPositionValues();
                if (SemDragAndDropHorizontalListAnimator.this.mDndListener != null) {
                    Log.m29d(SemDragAndDropHorizontalListAnimator.TAG, "dndListener.onDragAndDropEnd() from onItemAnimatorEnd()");
                    SemDragAndDropHorizontalListAnimator.this.mDndListener.onDragAndDropEnd();
                }
                SemDragAndDropHorizontalListAnimator.this.mListView.setEnabled(true);
            }
        }
    }

    class C09632 implements OnItemLongClickListener {
        C09632() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            return SemDragAndDropHorizontalListAnimator.this.initDragIfNecessary(i);
        }
    }

    class C09643 implements AnimatorUpdateListener {
        C09643() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SemDragAndDropHorizontalListAnimator.this.mDragViewBitmapTranslateX = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            SemDragAndDropHorizontalListAnimator.this.mListView.invalidate();
        }
    }

    class C09654 extends AnimatorListenerAdapter {
        C09654() {
        }

        public void onAnimationEnd(Animator animator) {
            if (SemDragAndDropHorizontalListAnimator.this.mFirstDragPos != SemDragAndDropHorizontalListAnimator.this.mDragPos) {
                SemDragAndDropHorizontalListAnimator.this.mDndController.dropDone(SemDragAndDropHorizontalListAnimator.this.mFirstDragPos, SemDragAndDropHorizontalListAnimator.this.mDragPos);
                SemDragAndDropHorizontalListAnimator.this.speakDragReleaseForAccessibility(SemDragAndDropHorizontalListAnimator.this.mDragPos);
            }
            SemDragAndDropHorizontalListAnimator.this.mItemAnimator.removeAll();
            SemDragAndDropHorizontalListAnimator.this.resetDndState();
            if (SemDragAndDropHorizontalListAnimator.this.mDndListener != null) {
                Log.m29d(SemDragAndDropHorizontalListAnimator.TAG, "dndListener.onDragAndDropEnd() from onAnimationEnd()");
                SemDragAndDropHorizontalListAnimator.this.mDndListener.onDragAndDropEnd();
            }
        }
    }

    private class HeaderFooterDndController implements DragAndDropController {
        private final DragAndDropController mWrappedController;

        HeaderFooterDndController(DragAndDropController dragAndDropController) {
            this.mWrappedController = dragAndDropController;
        }

        public boolean canDrag(int i) {
            return (this.mWrappedController == null || i < SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount() || i >= SemDragAndDropHorizontalListAnimator.this.mListView.getCount() - SemDragAndDropHorizontalListAnimator.this.mListView.getFooterViewsCount()) ? false : this.mWrappedController.canDrag(i - SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount());
        }

        public boolean canDrop(int i, int i2) {
            return (this.mWrappedController == null || i2 < SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount() || i2 >= SemDragAndDropHorizontalListAnimator.this.mListView.getCount() - SemDragAndDropHorizontalListAnimator.this.mListView.getFooterViewsCount()) ? false : this.mWrappedController.canDrop(i - SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount(), i2 - SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount());
        }

        public void dropDone(int i, int i2) {
            if (this.mWrappedController != null) {
                if (i < SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount()) {
                    i = SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount();
                } else if (i > SemDragAndDropHorizontalListAnimator.this.mListView.getCount() - SemDragAndDropHorizontalListAnimator.this.mListView.getFooterViewsCount()) {
                    i = (SemDragAndDropHorizontalListAnimator.this.mListView.getCount() - SemDragAndDropHorizontalListAnimator.this.mListView.getFooterViewsCount()) - 1;
                }
                if (i2 < SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount()) {
                    i2 = SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount();
                } else if (i2 >= SemDragAndDropHorizontalListAnimator.this.mListView.getCount() - SemDragAndDropHorizontalListAnimator.this.mListView.getFooterViewsCount()) {
                    i2 = (SemDragAndDropHorizontalListAnimator.this.mListView.getCount() - SemDragAndDropHorizontalListAnimator.this.mListView.getFooterViewsCount()) - 1;
                }
                this.mWrappedController.dropDone(i - SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount(), i2 - SemDragAndDropHorizontalListAnimator.this.mListView.getHeaderViewsCount());
            }
        }
    }

    public SemDragAndDropHorizontalListAnimator(Context context, SemHorizontalListView semHorizontalListView) {
        super(context, semHorizontalListView);
        this.mListView = semHorizontalListView;
        this.mListView.setDndListAnimator(this);
        initListeners();
        this.mDndAnimationCore.setAnimationListener(this.mItemAnimationListener);
        this.mListView.setOnItemLongClickListener(this.mOnItemLongClickListener);
    }

    private void addNewTranslation(int i, int i2) {
        ItemAnimation itemAnimation = this.mItemAnimator.getItemAnimation(i);
        ItemAnimation translateItemAnimation = itemAnimation instanceof TranslateItemAnimation ? itemAnimation : new TranslateItemAnimation();
        int i3 = 0;
        if (!translateItemAnimation.isFinished()) {
            i3 = (int) translateItemAnimation.getCurrentTranslateX();
        }
        translateItemAnimation.translate(i2, i2 - i3, 0, 0);
        if (translateItemAnimation.isFinished()) {
            translateItemAnimation.setStartAndDuration(0);
        } else {
            translateItemAnimation.setStartAndDuration(translateItemAnimation.getProgress());
        }
        this.mItemAnimator.putItemAnimation(i, translateItemAnimation);
    }

    private void addReturningTranslation(int i) {
        ItemAnimation itemAnimation = this.mItemAnimator.getItemAnimation(i);
        if (itemAnimation instanceof TranslateItemAnimation) {
            ItemAnimation itemAnimation2 = itemAnimation;
            itemAnimation2.translate(0, -((int) itemAnimation2.getCurrentTranslateX()), 0, 0);
            itemAnimation2.setStartAndDuration(itemAnimation2.getProgress());
        }
    }

    private boolean checkDndGrabHandle(int i, int i2, int i3) {
        if (activatedByLongPress()) {
            return true;
        }
        Rect rect = new Rect();
        this.mListView.getChildAt(i3 - this.mListView.getFirstVisiblePosition()).getHitRect(rect);
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
            int indexOfChild = this.mListView.indexOfChild(view) + this.mListView.getFirstVisiblePosition();
            if (this.mListView.getAdapter().isEnabled(indexOfChild) && !isHeaderOrFooterViewPos(indexOfChild)) {
                view.getHitRect(this.mTempRect);
                drawDragHandle(canvas, this.mTempRect, false, this.mDndController.canDrag(indexOfChild));
            }
        }
    }

    private int findDragItemPosition(int i) {
        int childCount = this.mListView.getChildCount();
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        if (childCount > 0) {
            for (int i2 = 0; i2 < childCount; i2++) {
                this.mListView.getChildAt(i2).getHitRect(this.mTempRect);
                if (this.mTempRect.contains(i, this.mTempRect.centerY())) {
                    return i2 + firstVisiblePosition;
                }
            }
        }
        return -1;
    }

    private int findMovedItemPosition(int i) {
        int childCount = this.mListView.getChildCount();
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        if (childCount > 0) {
            for (int i2 = 0; i2 < childCount; i2++) {
                if (i2 != this.mFirstDragPos - firstVisiblePosition) {
                    this.mListView.getChildAt(i2).getHitRect(this.mTempRect);
                    ItemAnimation itemAnimation = this.mItemAnimator.getItemAnimation(i2 + firstVisiblePosition);
                    if (itemAnimation instanceof TranslateItemAnimation) {
                        if (this.mTempRect.contains(i - itemAnimation.getDestOffsetX(), this.mTempRect.centerY())) {
                            return i2 + firstVisiblePosition;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return -1;
    }

    private void getDragGrabHandleHitRect(Rect rect, Rect rect2) {
        if (this.mDragGrabHandleDrawable != null) {
            int intrinsicWidth = this.mDragGrabHandleDrawable.getIntrinsicWidth();
            int intrinsicHeight = this.mDragGrabHandleDrawable.getIntrinsicHeight();
            rect.left += this.mDragGrabHandlePadding.left;
            rect.top += this.mDragGrabHandlePadding.top;
            rect.right += this.mDragGrabHandlePadding.right;
            rect.bottom += this.mDragGrabHandlePadding.bottom;
            rect.top -= 10;
            rect.bottom -= 10;
            Gravity.apply(this.mDragGrabHandlePosGravity, intrinsicWidth, intrinsicHeight, rect, rect2);
        }
    }

    private boolean initDrag(int i) {
        this.mDragView = this.mListView.getChildAt(i - this.mListView.getFirstVisiblePosition());
        if (this.mDragView == null) {
            Log.m29d(TAG, "initDrag : #4 return false, mDragView is null.");
            return false;
        }
        this.mListView.setEnableHoverDrawable(false);
        this.mDndTouchMode = 1;
        this.mFirstDragPos = i;
        this.mDragPos = this.mFirstDragPos;
        this.mDragView.getHitRect(this.mDragViewRect);
        speakDragStartForAccessibility(i);
        if (!this.mUserSetDragItemBitmap) {
            if (this.mDragViewBitmap != null) {
                this.mDragViewBitmap.recycle();
            }
            this.mDragViewBitmap = SemAnimatorUtils.getBitmapDrawableFromView(this.mDragView).getBitmap();
        }
        setDragViewAlpha(this.mDragViewBitmapAlpha);
        if (this.mDragViewBitmap != null) {
            this.mDndTouchOffsetX = this.mDndTouchX - this.mDragViewRect.left;
        }
        startSelectHighlightingAnimation(this.mDragView);
        if (this.mDndListener != null) {
            Log.m29d(TAG, "dndListener.OnDragAndDropStart()");
            this.mDndListener.onDragAndDropStart();
        } else {
            Log.m29d(TAG, "dndListener is null ");
        }
        this.mListView.invalidate();
        return true;
    }

    private boolean initDragIfNecessary(int i) {
        if (isDraggable() && activatedByLongPress() && this.mListView.getCount() > 1) {
            if (i >= 0 && i < this.mListView.getCount() && checkStartDnd(this.mDndTouchX, this.mDndTouchY, i)) {
                return initDrag(i);
            }
            resetDndState();
        }
        return false;
    }

    private void initListeners() {
        this.mItemAnimationListener = new C09621();
        this.mOnItemLongClickListener = new C09632();
    }

    private boolean isHeaderOrFooterViewPos(int i) {
        return i < this.mListView.getHeaderViewsCount() || i >= this.mListView.getCount() - this.mListView.getFooterViewsCount();
    }

    private void onTouchMove(MotionEvent motionEvent) {
        int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
        if (findPointerIndex == -1) {
            findPointerIndex = 0;
            this.mActivePointerId = motionEvent.getPointerId(0);
        }
        this.mDndTouchX = (int) motionEvent.getX(findPointerIndex);
        this.mDndTouchY = (int) motionEvent.getY(findPointerIndex);
        if (this.mDndTouchX > (this.mListView.getRight() - this.mListView.getPaddingRight()) - this.mListView.getLeft()) {
            this.mDndTouchX = (this.mListView.getRight() - this.mListView.getPaddingRight()) - this.mListView.getLeft();
        } else if (this.mDndTouchX < this.mListView.getPaddingLeft()) {
            this.mDndTouchX = this.mListView.getPaddingLeft();
        }
        if (!(this.mScaleUpAndDownAnimation == null || this.mScaleUpAndDownAnimation.isFinished() || ((float) Math.abs(this.mDndTouchX - this.mFirstTouchX)) <= 15.0f)) {
            this.mListItemSelectionAnimating = false;
        }
        this.mDndTouchMode = 2;
        Object obj = null;
        int paddingLeft = this.mListView.getPaddingLeft();
        View childAt = this.mListView.getChildAt(0);
        if (childAt != null) {
            paddingLeft += childAt.getWidth() / 2;
        }
        int right = (this.mListView.getRight() - this.mListView.getPaddingRight()) - this.mListView.getLeft();
        childAt = this.mListView.getChildAt(this.mListView.getChildCount() - 1);
        if (childAt != null) {
            right -= childAt.getWidth() / 2;
        }
        if (this.mDndTouchX > right || this.mDndTouchX < paddingLeft) {
            obj = 1;
            if (this.mDndAutoScrollMode == 0) {
                this.mListView.postOnAnimationDelayed(this.mAutoScrollRunnable, 150);
            }
            if (this.mDndTouchX > right) {
                this.mDndAutoScrollMode = 2;
            }
            if (this.mDndTouchX < paddingLeft) {
                this.mDndAutoScrollMode = 1;
            }
        }
        if (obj == null) {
            this.mDndAutoScrollMode = 0;
        }
        if (this.mDndAutoScrollMode == 0) {
            this.mListView.removeCallbacks(this.mAutoScrollRunnable);
        }
        reorderIfNeeded();
    }

    private void onTouchUpCancel(MotionEvent motionEvent) {
        this.mActivePointerId = -1;
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        if (this.mDndTouchMode == 1) {
            resetDndState();
            if (this.mDndListener != null) {
                Log.m29d(TAG, "dndListener.onDragAndDropEnd() DND_TOUCH_STATUS_START");
                this.mDndListener.onDragAndDropEnd();
            }
        }
        if (this.mDndTouchMode == 2) {
            View childAt = this.mListView.getChildAt(this.mFirstDragPos - firstVisiblePosition);
            View childAt2 = this.mListView.getChildAt(this.mDragPos - firstVisiblePosition);
            if (childAt == null || childAt2 == null) {
                int left;
                int i = this.mDndTouchX - this.mDndTouchOffsetX;
                if ((childAt2 != null ? 1 : null) != null) {
                    left = childAt2.getLeft() - i;
                } else if (this.mDragPos < firstVisiblePosition) {
                    left = -((i - this.mListView.getChildAt(0).getLeft()) + this.mDragViewRect.width());
                } else if (this.mListView.getChildCount() > 0) {
                    left = this.mListView.getChildAt(this.mListView.getChildCount() - 1).getRight() - i;
                } else {
                    Log.m31e(TAG, "mListView.getChildCount()=" + this.mListView.getChildCount());
                    return;
                }
                Log.m35v(TAG, "dndListener.onTouchUp() dragView == null, distance=" + left);
                Animator ofInt = ValueAnimator.ofInt(new int[]{0, left});
                ofInt.addUpdateListener(new C09643());
                ofInt.addListener(new C09654());
                ofInt.setDuration(210);
                ofInt.setInterpolator(SINE_IN_OUT_70);
                ofInt.start();
            } else if (this.mListItemSelectionAnimating) {
                resetDndState();
                if (this.mDndListener != null) {
                    Log.m29d(TAG, "dndListener.onDragAndDropEnd() mListItemSelectionAnimating is true");
                    this.mDndListener.onDragAndDropEnd();
                }
            } else {
                int left2 = childAt2.getLeft() - childAt.getLeft();
                int left3 = childAt2.getLeft() - (this.mDndTouchX - this.mDndTouchOffsetX);
                ItemAnimation translateItemAnimation = new TranslateItemAnimation();
                translateItemAnimation.translate(left2, left3, 0, 0);
                translateItemAnimation.setStartAndDuration(0.7f);
                this.mItemAnimator.putItemAnimation(this.mFirstDragPos, translateItemAnimation);
                this.mItemAnimator.start();
                this.mRetainFirstDragViewPos = this.mFirstDragPos - firstVisiblePosition;
                this.mListView.setEnabled(false);
                this.mDropDonePending = true;
                resetDndTouchValuesAndBitmap();
                Log.m29d(TAG, "onTouchUp() start last animation");
            }
            this.mDndAutoScrollMode = 0;
            this.mListView.removeCallbacks(this.mAutoScrollRunnable);
            this.mListView.invalidate();
        }
    }

    private void recalculateOffset(int i, int i2) {
        int dividerHeight = this.mListView.getDividerHeight();
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        int width = this.mDragViewRect.width() + dividerHeight;
        int i3;
        View childAt;
        int i4;
        int i5;
        View childAt2;
        if (i2 > i) {
            for (i3 = i + 1; i3 <= i2; i3++) {
                if (i3 <= this.mFirstDragPos) {
                    childAt = this.mListView.getChildAt(i3 - firstVisiblePosition);
                    if (childAt == null) {
                        Log.m31e(TAG, "recalculateOffset('dragging down') no such item, i=" + i3);
                    } else {
                        addReturningTranslation(findMovedItemPosition(SemAnimatorUtils.getViewCenterX(childAt)));
                    }
                } else if (this.mDndController.canDrop(this.mFirstDragPos, i3)) {
                    i4 = width;
                    i5 = i3;
                    while (true) {
                        i5--;
                        if (this.mNonMovableItems.indexOfKey(i5) < 0) {
                            break;
                        }
                        i4 += this.mNonMovableItems.get(i5);
                    }
                    if (this.mListView.isLayoutRtl()) {
                        addNewTranslation(i3, i4);
                    } else {
                        addNewTranslation(i3, -i4);
                    }
                } else {
                    childAt2 = this.mListView.getChildAt(i3 - firstVisiblePosition);
                    if (childAt2 != null) {
                        this.mNonMovableItems.put(i3, childAt2.getWidth() + dividerHeight);
                    }
                }
            }
            return;
        }
        for (i3 = i - 1; i3 >= i2; i3--) {
            if (i3 >= this.mFirstDragPos) {
                childAt = this.mListView.getChildAt(i3 - firstVisiblePosition);
                if (childAt == null) {
                    Log.m31e(TAG, "recalculateOffset('dragging up') no such item, i=" + i3);
                } else {
                    addReturningTranslation(findMovedItemPosition(SemAnimatorUtils.getViewCenterX(childAt)));
                }
            } else if (this.mDndController.canDrop(this.mFirstDragPos, i3)) {
                i4 = width;
                i5 = i3;
                while (true) {
                    i5++;
                    if (this.mNonMovableItems.indexOfKey(i5) < 0) {
                        break;
                    }
                    i4 += this.mNonMovableItems.get(i5);
                }
                if (this.mListView.isLayoutRtl()) {
                    addNewTranslation(i3, -i4);
                } else {
                    addNewTranslation(i3, i4);
                }
            } else if (this.mNonMovableItems.get(i3, -1) == -1) {
                childAt2 = this.mListView.getChildAt(i3 - firstVisiblePosition);
                if (childAt2 != null) {
                    this.mNonMovableItems.put(i3, childAt2.getWidth() + dividerHeight);
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

    public void dispatchDraw(Canvas canvas) {
        if (isDraggable() && this.mDragViewBitmap != null) {
            int paddingTop = this.mListView.getPaddingTop();
            int i = this.mDndTouchX - this.mDndTouchOffsetX;
            if (!this.mListItemSelectionAnimating && !this.mDragViewBitmap.isRecycled()) {
                canvas.drawBitmap(this.mDragViewBitmap, (float) (this.mDragViewBitmapTranslateX + i), (float) paddingTop, this.mDragViewBitmapPaint);
                this.mTempRect.top = -this.mListView.getPaddingTop();
                this.mTempRect.left = this.mDragViewBitmapTranslateX + i;
                this.mTempRect.right = this.mTempRect.left + this.mDragViewRect.width();
                this.mTempRect.bottom = this.mTempRect.top + this.mListView.getHeight();
                drawDragHandle(canvas, this.mTempRect, true, true);
            }
        }
    }

    public OnItemLongClickListener getDragAndDropOnItemLongClickListener() {
        return this.mOnItemLongClickListener;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                if (!this.mListView.isEnabled()) {
                    return false;
                }
                if (this.mTempEvent != null) {
                    this.mTempEvent.recycle();
                }
                this.mTempEvent = MotionEvent.obtain(motionEvent);
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mDndTouchX = (int) motionEvent.getX();
                this.mDndTouchY = (int) motionEvent.getY();
                this.mFirstTouchX = this.mDndTouchX;
                if (isDraggable() && this.mListView.getCount() > 1) {
                    int pointToPosition = this.mListView.pointToPosition(this.mDndTouchX, this.mDndTouchY);
                    if (pointToPosition == -1) {
                        Log.m29d(TAG, "onInterceptTouchEvent : #1 return false, itemPosition invalid.");
                        return false;
                    } else if (activatedByLongPress()) {
                        Log.m29d(TAG, "onInterceptTouchEvent : #2 return false, activated By longPress.");
                        return false;
                    } else if (pointToPosition < 0 || pointToPosition >= this.mListView.getCount() || !checkStartDnd(this.mDndTouchX, this.mDndTouchY, pointToPosition)) {
                        Log.m29d(TAG, "onInterceptTouchEvent : #3 resetDndState");
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
                return isDraggable() && this.mDndTouchMode == 1 && this.mListView.getCount() > 1 && activatedByLongPress();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i = 0;
        if (!isDraggable() || this.mDndTouchMode == 0) {
            return false;
        }
        int action = motionEvent.getAction();
        switch (action & 255) {
            case 1:
            case 3:
                onTouchUpCancel(motionEvent);
                break;
            case 2:
                onTouchMove(motionEvent);
                break;
            case 6:
                int i2 = (65280 & action) >> 8;
                if (motionEvent.getPointerId(i2) == this.mActivePointerId) {
                    if (i2 == 0) {
                        i = 1;
                    }
                    this.mActivePointerId = motionEvent.getPointerId(i);
                    break;
                }
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
        int indexOfChild = this.mListView.indexOfChild(view) + this.mListView.getFirstVisiblePosition();
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
        int findDragItemPosition = findDragItemPosition((this.mDndTouchX - this.mDndTouchOffsetX) + (this.mDragViewRect.width() / 2));
        if (findDragItemPosition != -1 && this.mDndController.canDrop(this.mFirstDragPos, findDragItemPosition)) {
            this.mDragPos = findDragItemPosition;
        }
        if (!(i == this.mDragPos || this.mListItemSelectionAnimating)) {
            recalculateOffset(i, this.mDragPos);
            this.mItemAnimator.start();
        }
        if (i != this.mDragPos || this.mDragViewBitmap != null) {
            this.mListView.invalidate();
        }
    }

    void resetDndPositionValues() {
        super.resetDndPositionValues();
        this.mListView.setEnableHoverDrawable(true);
    }

    void resetDndTouchValuesAndBitmap() {
        super.resetDndTouchValuesAndBitmap();
        this.mNonMovableItems.clear();
    }

    public void setDragAndDropController(DragAndDropController dragAndDropController) {
        if (dragAndDropController == null) {
            this.mDndController = null;
            return;
        }
        if (this.mListView.getHeaderViewsCount() == 0 && this.mListView.getFooterViewsCount() == 0) {
            this.mDndController = dragAndDropController;
        } else {
            this.mDndController = new HeaderFooterDndController(dragAndDropController);
        }
    }

    public boolean startDrag() {
        return this.mTempEvent == null ? false : initDragIfNecessary(this.mListView.pointToPosition((int) this.mTempEvent.getX(), (int) this.mTempEvent.getY()));
    }
}
