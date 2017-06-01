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
import android.widget.ListView;
import com.samsung.android.animation.SemAbsDragAndDropAnimator.DragAndDropController;

public class SemDragAndDropListAnimator extends SemAbsDragAndDropAnimator {
    private static final String TAG = "SemDragAndDropListAnimator";
    private ItemAnimationListener mItemAnimationListener;
    private OnItemLongClickListener mItemLongClickListener;
    private ListView mListView;
    SparseIntArray mNonMovableItems = new SparseIntArray();
    private final int mScrollBarSize = 10;

    class C09661 implements ItemAnimationListener {
        C09661() {
        }

        public void onItemAnimatorEnd() {
            if (SemDragAndDropListAnimator.this.mListItemSelectionAnimating) {
                SemDragAndDropListAnimator.this.mListItemSelectionAnimating = false;
                return;
            }
            if (SemDragAndDropListAnimator.this.mDropDonePending) {
                SemDragAndDropListAnimator.this.mDropDonePending = false;
                if (SemDragAndDropListAnimator.this.mDndController != null) {
                    Log.m29d(SemDragAndDropListAnimator.TAG, "initListeners : onItemAnimatorEnd : mDndController.dropDone #1 , mFirstDragPos = " + SemDragAndDropListAnimator.this.mFirstDragPos + ", mDragPos = " + SemDragAndDropListAnimator.this.mDragPos);
                    SemDragAndDropListAnimator.this.mDndController.dropDone(SemDragAndDropListAnimator.this.mFirstDragPos, SemDragAndDropListAnimator.this.mDragPos);
                    SemDragAndDropListAnimator.this.speakDragReleaseForAccessibility(SemDragAndDropListAnimator.this.mDragPos);
                }
                SemDragAndDropListAnimator.this.mItemAnimator.removeAll();
                SemDragAndDropListAnimator.this.resetDndPositionValues();
                if (SemDragAndDropListAnimator.this.mDndListener != null) {
                    Log.m29d(SemDragAndDropListAnimator.TAG, "initListeners : onItemAnimatorEnd : dndListener.onDragAndDropEnd() #1");
                    SemDragAndDropListAnimator.this.mDndListener.onDragAndDropEnd();
                }
                SemDragAndDropListAnimator.this.mListView.setEnabled(true);
            }
        }
    }

    class C09672 implements OnItemLongClickListener {
        C09672() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            return SemDragAndDropListAnimator.this.initDragIfNecessary(i);
        }
    }

    class C09683 implements AnimatorUpdateListener {
        C09683() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SemDragAndDropListAnimator.this.mDragViewBitmapTranslateY = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            SemDragAndDropListAnimator.this.mListView.invalidate();
        }
    }

    class C09694 extends AnimatorListenerAdapter {
        C09694() {
        }

        public void onAnimationEnd(Animator animator) {
            if (SemDragAndDropListAnimator.this.mFirstDragPos != SemDragAndDropListAnimator.this.mDragPos) {
                Log.m29d(SemDragAndDropListAnimator.TAG, "onTouchUpCancel : onAnimationEnd : mDndController.dropDone #2 , mFirstDragPos = " + SemDragAndDropListAnimator.this.mFirstDragPos + ", mDragPos = " + SemDragAndDropListAnimator.this.mDragPos);
                SemDragAndDropListAnimator.this.mDndController.dropDone(SemDragAndDropListAnimator.this.mFirstDragPos, SemDragAndDropListAnimator.this.mDragPos);
                SemDragAndDropListAnimator.this.speakDragReleaseForAccessibility(SemDragAndDropListAnimator.this.mDragPos);
            }
            SemDragAndDropListAnimator.this.mItemAnimator.removeAll();
            SemDragAndDropListAnimator.this.resetDndState();
            if (SemDragAndDropListAnimator.this.mDndListener != null) {
                Log.m29d(SemDragAndDropListAnimator.TAG, "dndListener.onDragAndDropEnd() from onAnimationEnd() #3");
                SemDragAndDropListAnimator.this.mDndListener.onDragAndDropEnd();
            }
        }
    }

    private class HeaderFooterDndController implements DragAndDropController {
        private final DragAndDropController mWrappedController;

        HeaderFooterDndController(DragAndDropController dragAndDropController) {
            this.mWrappedController = dragAndDropController;
        }

        public boolean canDrag(int i) {
            if (this.mWrappedController == null || i < SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount() || i >= SemDragAndDropListAnimator.this.mListView.getCount() - SemDragAndDropListAnimator.this.mListView.getFooterViewsCount()) {
                return false;
            }
            Log.m29d(SemDragAndDropListAnimator.TAG, "HeaderFooterDndController : canDrag #3 mListView.getHeaderViewsCount() = " + SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount());
            return this.mWrappedController.canDrag(i - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount());
        }

        public boolean canDrop(int i, int i2) {
            if (this.mWrappedController == null || i2 < SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount() || i2 >= SemDragAndDropListAnimator.this.mListView.getCount() - SemDragAndDropListAnimator.this.mListView.getFooterViewsCount()) {
                return false;
            }
            Log.m29d(SemDragAndDropListAnimator.TAG, "HeaderFooterDndController : canDrop #4 startPos - mListView.getHeaderViewsCount() = " + (i - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount()) + ", destPos = " + i2);
            return this.mWrappedController.canDrop(i - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount(), i2 - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount());
        }

        public void dropDone(int i, int i2) {
            if (this.mWrappedController != null) {
                if (i < SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount()) {
                    i = SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount();
                } else if (i > SemDragAndDropListAnimator.this.mListView.getCount() - SemDragAndDropListAnimator.this.mListView.getFooterViewsCount()) {
                    i = (SemDragAndDropListAnimator.this.mListView.getCount() - SemDragAndDropListAnimator.this.mListView.getFooterViewsCount()) - 1;
                }
                if (i2 < SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount()) {
                    i2 = SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount();
                } else if (i2 >= SemDragAndDropListAnimator.this.mListView.getCount() - SemDragAndDropListAnimator.this.mListView.getFooterViewsCount()) {
                    i2 = (SemDragAndDropListAnimator.this.mListView.getCount() - SemDragAndDropListAnimator.this.mListView.getFooterViewsCount()) - 1;
                }
                Log.m29d(SemDragAndDropListAnimator.TAG, "HeaderFooterDndController : dropDone : mWrappedController.dropDone #3");
                Log.m29d(SemDragAndDropListAnimator.TAG, "HeaderFooterDndController : dropDone : startPos - mListView.getHeaderViewsCount() = " + (i - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount()));
                Log.m29d(SemDragAndDropListAnimator.TAG, "HeaderFooterDndController : dropDone : destPos - mListView.getHeaderViewsCount() = " + (i2 - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount()));
                this.mWrappedController.dropDone(i - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount(), i2 - SemDragAndDropListAnimator.this.mListView.getHeaderViewsCount());
            }
        }
    }

    public SemDragAndDropListAnimator(Context context, ListView listView) {
        super(context, listView);
        this.mListView = listView;
        initListeners();
        this.mDndAnimationCore.setAnimationListener(this.mItemAnimationListener);
        this.mListView.setDndListAnimator(this);
        this.mListView.setOnItemLongClickListener(this.mItemLongClickListener);
    }

    private void addNewTranslation(int i, int i2) {
        ItemAnimation itemAnimation = this.mItemAnimator.getItemAnimation(i);
        ItemAnimation translateItemAnimation = itemAnimation instanceof TranslateItemAnimation ? itemAnimation : new TranslateItemAnimation();
        int i3 = 0;
        if (!translateItemAnimation.isFinished()) {
            i3 = (int) translateItemAnimation.getCurrentTranslateY();
        }
        translateItemAnimation.translate(0, 0, i2, i2 - i3);
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
            itemAnimation2.translate(0, 0, 0, -((int) itemAnimation2.getCurrentTranslateY()));
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
        if (this.mListView.semIsLongPressTriggeredByKey()) {
            Log.m29d(TAG, "checkStartDnd : LongPress is triggered by key, return false");
            return false;
        } else if (!checkDndGrabHandle(i, i2, i3)) {
            return false;
        } else {
            Log.m29d(TAG, "checkStartDnd : canDrag #1 itemPosition = " + i3);
            boolean canDrag = this.mDndController.canDrag(i3);
            if (!canDrag) {
                speakNotDraggableForAccessibility(i3);
            }
            return canDrag;
        }
    }

    private void drawDragHandle(Canvas canvas, Rect rect, boolean z, boolean z2) {
        Log.m29d(TAG, "drawDragHandle : isAllowDragItem = " + z2);
        if (this.mDragGrabHandleDrawable == null || !z2) {
            Log.m29d(TAG, "drawDragHandle : not draw drageGrabHandle~~!! ");
            return;
        }
        getDragGrabHandleHitRect(rect, this.mTempRect);
        this.mDragGrabHandleDrawable.setBounds(this.mTempRect);
        this.mDragGrabHandleDrawable.setState(z ? PRESSED_STATE_SET : EMPTY_STATE_SET);
        this.mDragGrabHandleDrawable.setAlpha(this.mDragHandleAlpha);
        Log.m29d(TAG, "drawDragHandle : call mDragGrabHandleDrawable.draw.. ");
        this.mDragGrabHandleDrawable.draw(canvas);
    }

    private void drawDragHandlerIfNeeded(Canvas canvas, View view, long j) {
        if (isDraggable()) {
            int indexOfChild = this.mListView.indexOfChild(view) + this.mListView.getFirstVisiblePosition();
            if (this.mListView.getAdapter().isEnabled(indexOfChild) && !isHeaderOrFooterViewPos(indexOfChild)) {
                view.getHitRect(this.mTempRect);
                Log.m29d(TAG, "drawDragHandlerIfNeeded : canDrag #2 pos = " + indexOfChild);
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
                if (this.mTempRect.contains(this.mTempRect.centerX(), i)) {
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
                        if (this.mTempRect.contains(this.mTempRect.centerX(), i - itemAnimation.getDestOffsetY())) {
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
            if (this.mListView.isLayoutRtl()) {
                rect.left += this.mDragGrabHandlePadding.right;
                rect.top += this.mDragGrabHandlePadding.top;
                rect.right -= this.mDragGrabHandlePadding.left;
                rect.bottom += this.mDragGrabHandlePadding.bottom;
                rect.left += 10;
                rect.right += 10;
                Gravity.apply(this.mDragGrabHandlePosGravity, intrinsicWidth, intrinsicHeight, rect, rect2, 1);
                return;
            }
            rect.left += this.mDragGrabHandlePadding.left;
            rect.top += this.mDragGrabHandlePadding.top;
            rect.right += this.mDragGrabHandlePadding.right;
            rect.bottom += this.mDragGrabHandlePadding.bottom;
            rect.left -= 10;
            rect.right -= 10;
            Gravity.apply(this.mDragGrabHandlePosGravity, intrinsicWidth, intrinsicHeight, rect, rect2, 0);
        }
    }

    private boolean initDrag(int i) {
        this.mDragView = this.mListView.getChildAt(i - this.mListView.getFirstVisiblePosition());
        if (this.mDragView == null) {
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
            this.mDndTouchOffsetY = this.mDndTouchY - this.mDragViewRect.top;
        }
        startSelectHighlightingAnimation(this.mDragView);
        if (this.mDndListener != null) {
            Log.m29d(TAG, "dndListener.OnDragAndDropStart() initDrag");
            this.mDndListener.onDragAndDropStart();
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
        this.mItemAnimationListener = new C09661();
        this.mItemLongClickListener = new C09672();
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
        if (this.mDndTouchY > (this.mListView.getBottom() - this.mListView.getPaddingBottom()) - this.mListView.getTop()) {
            this.mDndTouchY = (this.mListView.getBottom() - this.mListView.getPaddingBottom()) - this.mListView.getTop();
        } else if (this.mDndTouchY < this.mListView.getPaddingTop()) {
            this.mDndTouchY = this.mListView.getPaddingTop();
        }
        if (!(this.mScaleUpAndDownAnimation == null || this.mScaleUpAndDownAnimation.isFinished() || ((float) Math.abs(this.mDndTouchY - this.mFirstTouchY)) <= 15.0f)) {
            this.mListItemSelectionAnimating = false;
        }
        this.mDndTouchMode = 2;
        Object obj = null;
        int paddingTop = this.mListView.getPaddingTop();
        View childAt = this.mListView.getChildAt(0);
        if (childAt != null) {
            paddingTop += childAt.getHeight() / 2;
        }
        int bottom = (this.mListView.getBottom() - this.mListView.getPaddingBottom()) - this.mListView.getTop();
        childAt = this.mListView.getChildAt(this.mListView.getChildCount() - 1);
        if (childAt != null) {
            bottom -= childAt.getHeight() / 2;
        }
        if (this.mDndTouchY > bottom || this.mDndTouchY < paddingTop) {
            obj = 1;
            if (this.mDndAutoScrollMode == 0) {
                this.mListView.postOnAnimationDelayed(this.mAutoScrollRunnable, 150);
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
                Log.m29d(TAG, "dndListener.onDragAndDropEnd() onTouchUpCancel DND_TOUCH_STATUS_START #2");
                this.mDndListener.onDragAndDropEnd();
            }
        }
        if (this.mDndTouchMode == 2) {
            if (this.mListView.getChildCount() == 0) {
                resetDndState();
                return;
            }
            View childAt = this.mListView.getChildAt(this.mFirstDragPos - firstVisiblePosition);
            View childAt2 = this.mListView.getChildAt(this.mDragPos - firstVisiblePosition);
            if (childAt == null || childAt2 == null) {
                int i = this.mDndTouchY - this.mDndTouchOffsetY;
                int top = (childAt2 != null ? 1 : null) != null ? childAt2.getTop() - i : this.mDragPos < firstVisiblePosition ? -((i - this.mListView.getChildAt(0).getTop()) + this.mDragViewRect.height()) : this.mListView.getChildAt(this.mListView.getChildCount() - 1).getBottom() - i;
                Log.m35v(TAG, "dndListener.onTouchUp() dragView == null, distance=" + top);
                Animator ofInt = ValueAnimator.ofInt(new int[]{0, top});
                ofInt.addUpdateListener(new C09683());
                ofInt.addListener(new C09694());
                ofInt.setDuration(210);
                ofInt.setInterpolator(SINE_IN_OUT_70);
                ofInt.start();
            } else if (this.mListItemSelectionAnimating) {
                resetDndState();
                if (this.mDndListener != null) {
                    Log.m29d(TAG, "dndListener.onDragAndDropEnd() mListItemSelectionAnimating is true #4");
                    this.mDndListener.onDragAndDropEnd();
                }
            } else {
                int top2 = childAt2.getTop() - childAt.getTop();
                int top3 = childAt2.getTop() - (this.mDndTouchY - this.mDndTouchOffsetY);
                ItemAnimation translateItemAnimation = new TranslateItemAnimation();
                translateItemAnimation.translate(0, 0, top2, top3);
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
        int height = this.mDragViewRect.height() + dividerHeight;
        int i3;
        int i4;
        int i5;
        View childAt;
        View childAt2;
        if (i2 > i) {
            for (i3 = i + 1; i3 <= i2; i3++) {
                if (i3 > this.mFirstDragPos) {
                    Log.m29d(TAG, "recalculateOffset : canDrop #2 mFirstDragPos = " + this.mFirstDragPos + ", i = " + i3);
                    if (this.mDndController.canDrop(this.mFirstDragPos, i3)) {
                        i4 = height;
                        i5 = i3;
                        while (true) {
                            i5--;
                            if (this.mNonMovableItems.indexOfKey(i5) < 0) {
                                break;
                            }
                            i4 += this.mNonMovableItems.get(i5);
                        }
                        addNewTranslation(i3, -i4);
                    } else {
                        childAt = this.mListView.getChildAt(i3 - firstVisiblePosition);
                        if (childAt != null) {
                            this.mNonMovableItems.put(i3, childAt.getHeight() + dividerHeight);
                        }
                    }
                } else {
                    childAt2 = this.mListView.getChildAt(i3 - firstVisiblePosition);
                    if (childAt2 == null) {
                        Log.m31e(TAG, "recalculateOffset('dragging down') no such item, i=" + i3);
                    } else {
                        addReturningTranslation(findMovedItemPosition(SemAnimatorUtils.getViewCenterY(childAt2)));
                    }
                }
            }
            return;
        }
        for (i3 = i - 1; i3 >= i2; i3--) {
            if (i3 < this.mFirstDragPos) {
                Log.m29d(TAG, "recalculateOffset : canDrop #3 mFirstDragPos = " + this.mFirstDragPos + ", i = " + i3);
                if (this.mDndController.canDrop(this.mFirstDragPos, i3)) {
                    i4 = height;
                    i5 = i3;
                    while (true) {
                        i5++;
                        if (this.mNonMovableItems.indexOfKey(i5) < 0) {
                            break;
                        }
                        i4 += this.mNonMovableItems.get(i5);
                    }
                    addNewTranslation(i3, i4);
                } else if (this.mNonMovableItems.get(i3, -1) == -1) {
                    childAt = this.mListView.getChildAt(i3 - firstVisiblePosition);
                    if (childAt != null) {
                        this.mNonMovableItems.put(i3, childAt.getHeight() + dividerHeight);
                    }
                }
            } else {
                childAt2 = this.mListView.getChildAt(i3 - firstVisiblePosition);
                if (childAt2 == null) {
                    Log.m31e(TAG, "recalculateOffset('dragging up') no such item, i=" + i3);
                } else {
                    addReturningTranslation(findMovedItemPosition(SemAnimatorUtils.getViewCenterY(childAt2)));
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
            int paddingLeft = this.mListView.getPaddingLeft();
            int i = this.mDndTouchY - this.mDndTouchOffsetY;
            if (!this.mListItemSelectionAnimating && !this.mDragViewBitmap.isRecycled()) {
                canvas.drawBitmap(this.mDragViewBitmap, (float) paddingLeft, (float) (this.mDragViewBitmapTranslateY + i), this.mDragViewBitmapPaint);
                if ((this.mDragGrabHandlePosGravity & 5) == 5) {
                    this.mTempRect.left = -this.mListView.getPaddingRight();
                } else {
                    this.mTempRect.left = this.mListView.getPaddingLeft();
                }
                this.mTempRect.top = this.mDragViewBitmapTranslateY + i;
                this.mTempRect.bottom = this.mTempRect.top + this.mDragViewRect.height();
                this.mTempRect.right = this.mTempRect.left + this.mListView.getWidth();
                drawDragHandle(canvas, this.mTempRect, true, true);
            }
        }
    }

    public OnItemLongClickListener getDragAndDropOnItemLongClickListener() {
        return this.mItemLongClickListener;
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
                this.mFirstTouchY = this.mDndTouchY;
                if (isDraggable() && this.mListView.getCount() > 1) {
                    int pointToPosition = this.mListView.pointToPosition(this.mDndTouchX, this.mDndTouchY);
                    if (pointToPosition == -1 || activatedByLongPress()) {
                        return false;
                    }
                    if (pointToPosition < 0 || pointToPosition >= this.mListView.getCount() || !checkStartDnd(this.mDndTouchX, this.mDndTouchY, pointToPosition)) {
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
        Log.m29d(TAG, "postDrawChild : call drawDragHandlerIfNeeded");
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
        int findDragItemPosition = findDragItemPosition((this.mDndTouchY - this.mDndTouchOffsetY) + (this.mDragViewRect.height() / 2));
        Log.m29d(TAG, "reorderIfNeeded : canDrop #1 mFirstDragPos = " + this.mFirstDragPos + ", dragPos = " + findDragItemPosition);
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
