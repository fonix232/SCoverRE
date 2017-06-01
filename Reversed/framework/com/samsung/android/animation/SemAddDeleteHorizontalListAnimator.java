package com.samsung.android.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ListAdapter;
import android.widget.SemHorizontalListView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class SemAddDeleteHorizontalListAnimator extends SemAbsAddDeleteAnimator {
    private static String TAG = SemAddDeleteHorizontalListAnimator.class.getSimpleName();
    private boolean deletePending = false;
    private boolean insertPending = false;
    private SemHorizontalListView mHorizontalListView;
    LinkedHashMap<Long, ViewInfo> mOldHeaderFooterViewCache = new LinkedHashMap();
    LinkedHashMap<Long, ViewInfo> mOldViewCache = new LinkedHashMap();
    private OnAddDeleteListener mOnAddDeleteListener;

    class C09421 implements OnPreDrawListener {
        C09421() {
        }

        public boolean onPreDraw() {
            SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (SemAddDeleteHorizontalListAnimator.this.mDeleteRunnable != null) {
                SemAddDeleteHorizontalListAnimator.this.mDeleteRunnable.run();
                SemAddDeleteHorizontalListAnimator.this.mDeleteRunnable = null;
            }
            return true;
        }
    }

    class C09453 implements OnPreDrawListener {
        C09453() {
        }

        public boolean onPreDraw() {
            SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (SemAddDeleteHorizontalListAnimator.this.mInsertRunnable != null) {
                SemAddDeleteHorizontalListAnimator.this.mInsertRunnable.run();
                SemAddDeleteHorizontalListAnimator.this.mInsertRunnable = null;
            }
            return true;
        }
    }

    public interface OnAddDeleteListener {
        void onAdd();

        void onAnimationEnd(boolean z);

        void onAnimationStart(boolean z);

        void onDelete();
    }

    public SemAddDeleteHorizontalListAnimator(Context context, SemHorizontalListView semHorizontalListView) {
        this.mHorizontalListView = semHorizontalListView;
        this.mHorizontalListView.setAddDeleteListAnimator(this);
        this.mHostView = semHorizontalListView;
    }

    private void capturePreAnimationViewCoordinates() {
        ViewGroup viewGroup = this.mHorizontalListView;
        ListAdapter adapter = viewGroup.getAdapter();
        int childCount = viewGroup.getChildCount();
        int firstVisiblePosition = viewGroup.getFirstVisiblePosition();
        int count = adapter.getCount();
        int headerViewsCount = viewGroup.getHeaderViewsCount();
        int footerViewsCount = viewGroup.getFooterViewsCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            int i2 = i + firstVisiblePosition;
            long itemId = adapter.getItemId(i2);
            if (childAt.getHeight() == 0 || childAt.getWidth() == 0) {
                Log.m31e(TAG, "setDelete() child's one of dimensions is 0, i=" + i);
            } else {
                BitmapDrawable bitmapDrawableFromView = SemAnimatorUtils.getBitmapDrawableFromView(childAt);
                if (itemId == -1) {
                    if (i2 < headerViewsCount) {
                        itemId = (long) (i2 + 1);
                    } else if (i2 >= count - footerViewsCount) {
                        itemId = (long) (-(((i2 + footerViewsCount) - count) + 1));
                    }
                    this.mOldHeaderFooterViewCache.put(Long.valueOf(itemId), new ViewInfo(bitmapDrawableFromView, i2, childAt.getLeft(), 0, childAt.getRight(), 0));
                } else {
                    HashMap hashMap = this.mOldViewCache;
                    Long valueOf = Long.valueOf(itemId);
                    hashMap.put(valueOf, new ViewInfo(bitmapDrawableFromView, i + firstVisiblePosition, childAt.getLeft(), 0, childAt.getRight(), 0));
                }
            }
        }
    }

    private void ensureAdapterAndListener() {
        ListAdapter adapter = this.mHorizontalListView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("Adapter need to be set before performing add/delete operations.");
        } else if (!adapter.hasStableIds()) {
            throw new IllegalStateException("TwAddDeleteListAnimator requires an adapter that has stable ids");
        } else if (this.mOnAddDeleteListener == null) {
            throw new IllegalStateException("OnAddDeleteListener need to be supplied before performing add/delete operations");
        }
    }

    private int getChildMaxWidth() {
        int childCount = this.mHorizontalListView.getChildCount();
        int count = this.mHorizontalListView.getAdapter().getCount();
        int firstVisiblePosition = this.mHorizontalListView.getFirstVisiblePosition();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            int i3 = i2 + firstVisiblePosition;
            if (i3 >= this.mHorizontalListView.getHeaderViewsCount() && i3 < count - this.mHorizontalListView.getFooterViewsCount()) {
                int width = this.mHorizontalListView.getChildAt(i2).getWidth();
                if (width > i) {
                    i = width;
                }
            }
        }
        return i;
    }

    private void prepareDelete(ArrayList<Integer> arrayList) {
        int height;
        int top;
        this.deletePending = true;
        final Object arrayList2 = new ArrayList(arrayList);
        ensureAdapterAndListener();
        Collections.sort(arrayList2);
        final HashSet hashSet = new HashSet(arrayList2);
        final int childCount = this.mHorizontalListView.getChildCount();
        final int firstVisiblePosition = this.mHorizontalListView.getFirstVisiblePosition();
        final ListAdapter adapter = this.mHorizontalListView.getAdapter();
        if (this.mHorizontalListView.getChildAt(this.mHorizontalListView.getHeaderViewsCount()) != null) {
            height = this.mHorizontalListView.getChildAt(this.mHorizontalListView.getHeaderViewsCount()).getHeight();
            top = this.mHorizontalListView.getChildAt(this.mHorizontalListView.getHeaderViewsCount()).getTop();
        } else {
            height = this.mHorizontalListView.getHeight();
            top = 0;
        }
        capturePreAnimationViewCoordinates();
        this.mDeleteRunnable = new Runnable() {

            class C09431 extends AnimatorListenerAdapter {
                C09431() {
                }

                public void onAnimationEnd(Animator animator) {
                    SemAddDeleteHorizontalListAnimator.this.mGhostViewSnapshots.clear();
                    SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.invalidate();
                    SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.setEnabled(true);
                    if (SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener.onAnimationEnd(false);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.setEnabled(false);
                    if (SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener.onAnimationStart(false);
                    }
                }
            }

            public void run() {
                int top;
                int height;
                ViewInfo viewInfo;
                float f;
                View -get1 = SemAddDeleteHorizontalListAnimator.this.mHorizontalListView;
                int childCount = -get1.getChildCount();
                int firstVisiblePosition = -get1.getFirstVisiblePosition();
                int lastVisiblePosition = -get1.getLastVisiblePosition();
                int headerViewsCount = -get1.getHeaderViewsCount();
                int footerViewsCount = -get1.getFooterViewsCount();
                int count = adapter.getCount();
                Collection arrayList = new ArrayList();
                int i = 0;
                if (childCount > headerViewsCount) {
                    i = SemAddDeleteHorizontalListAnimator.this.getChildMaxWidth() + -get1.getDividerHeight();
                    top = -get1.getChildAt(headerViewsCount).getTop();
                    height = -get1.getChildAt(headerViewsCount).getHeight();
                } else {
                    top = top;
                    height = height;
                }
                Object obj = 1;
                int i2 = firstVisiblePosition - firstVisiblePosition;
                int i3 = i2;
                int i4 = (lastVisiblePosition + 1) + (childCount - childCount);
                for (int i5 = 0; i5 < childCount; i5++) {
                    View childAt = -get1.getChildAt(i5);
                    int i6 = i5 + firstVisiblePosition;
                    long itemId = adapter.getItemId(i6);
                    float left = (float) childAt.getLeft();
                    if (itemId == -1) {
                        if (i6 < headerViewsCount) {
                            itemId = (long) (i6 + 1);
                        } else if (i6 >= count - footerViewsCount) {
                            itemId = (long) (-(((i6 + footerViewsCount) - count) + 1));
                        }
                        viewInfo = (ViewInfo) SemAddDeleteHorizontalListAnimator.this.mOldHeaderFooterViewCache.remove(Long.valueOf(itemId));
                    } else {
                        viewInfo = (ViewInfo) SemAddDeleteHorizontalListAnimator.this.mOldViewCache.remove(Long.valueOf(itemId));
                    }
                    if (viewInfo != null) {
                        viewInfo.recycleBitmap();
                        obj = null;
                        if (((float) viewInfo.left) != left) {
                            f = ((float) viewInfo.left) - left;
                            arrayList.add(SemAddDeleteHorizontalListAnimator.this.getTranslateAnim(childAt, f, 0.0f));
                        }
                    } else {
                        int i7;
                        if (i3 <= 0 || r29 == null) {
                            i7 = i4 - i6;
                            i4++;
                        } else {
                            i7 = -i2;
                            i3--;
                        }
                        f = ((float) (SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.isLayoutRtl() ? childAt.getLeft() - (i7 * i) : childAt.getLeft() + (i7 * i))) - left;
                        arrayList.add(SemAddDeleteHorizontalListAnimator.this.getTranslateAnim(childAt, f, 0.0f));
                    }
                }
                Object obj2 = null;
                for (Entry value : SemAddDeleteHorizontalListAnimator.this.mOldViewCache.entrySet()) {
                    viewInfo = (ViewInfo) value.getValue();
                    SemAddDeleteHorizontalListAnimator.this.mGhostViewSnapshots.add(viewInfo);
                    Rect rect = new Rect(viewInfo.left, top, viewInfo.right, top + height);
                    int newPosition = SemAddDeleteHorizontalListAnimator.this.getNewPosition(viewInfo.oldPosition, arrayList2);
                    boolean contains = hashSet.contains(Integer.valueOf(viewInfo.oldPosition));
                    int i8 = newPosition - firstVisiblePosition;
                    if (i8 < 0 || i8 >= childCount) {
                        float width = childCount == 0 ? SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.isLayoutRtl() ? (float) (-get1.getWidth() - (viewInfo.right - viewInfo.left)) : (float) -get1.getPaddingLeft() : (float) -get1.getChildAt(0).getLeft();
                        f = width - ((float) viewInfo.left);
                        f = SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.isLayoutRtl() ? f + ((float) ((-i8) * i)) : f - ((float) ((-i8) * i));
                    } else {
                        f = ((float) -get1.getChildAt(i8).getLeft()) - ((float) viewInfo.left);
                    }
                    Rect rect2 = new Rect(rect);
                    rect2.offset((int) f, 0);
                    if (contains) {
                        int width2 = (int) (((1.0f - SemAddDeleteHorizontalListAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.width()));
                        int height2 = (int) (((1.0f - SemAddDeleteHorizontalListAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.height()));
                        rect2 = new Rect(rect2.left + width2, rect2.top + height2, rect2.right - width2, rect2.bottom - height2);
                    }
                    PropertyValuesHolder ofObject = PropertyValuesHolder.ofObject("bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect2});
                    PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{255, 0});
                    ValueAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(viewInfo.viewSnapshot, new PropertyValuesHolder[]{ofObject, ofInt});
                    if (obj2 == null) {
                        ofPropertyValuesHolder.addUpdateListener(SemAddDeleteHorizontalListAnimator.this.mBitmapUpdateListener);
                        obj2 = 1;
                    }
                    arrayList.add(ofPropertyValuesHolder);
                }
                SemAddDeleteHorizontalListAnimator.this.mOldViewCache.clear();
                SemAddDeleteHorizontalListAnimator.this.mOldHeaderFooterViewCache.clear();
                Animator animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.addListener(new C09431());
                animatorSet.setInterpolator(SemAddDeleteHorizontalListAnimator.DELETE_INTERPOLATOR);
                animatorSet.setDuration((long) SemAddDeleteHorizontalListAnimator.TRANSLATION_DURATION);
                animatorSet.start();
            }
        };
    }

    private void prepareInsert(ArrayList<Integer> arrayList) {
        this.insertPending = true;
        ensureAdapterAndListener();
        final ArrayList arrayList2 = new ArrayList(arrayList);
        Collections.sort(arrayList2);
        final HashSet hashSet = new HashSet(arrayList2);
        ViewGroup viewGroup = this.mHorizontalListView;
        ListAdapter adapter = viewGroup.getAdapter();
        int childCount = viewGroup.getChildCount();
        int count = adapter.getCount();
        int firstVisiblePosition = viewGroup.getFirstVisiblePosition();
        int footerViewsCount = viewGroup.getFooterViewsCount();
        for (int i = 0; i < childCount; i++) {
            int i2 = i + firstVisiblePosition;
            View childAt = viewGroup.getChildAt(i);
            long itemId = adapter.getItemId(i2);
            if (childAt.getHeight() == 0 || childAt.getWidth() == 0) {
                Log.m31e(TAG, "setInsert() child's one of dimensions is 0, i=" + i);
            } else {
                BitmapDrawable bitmapDrawableFromView = SemAnimatorUtils.getBitmapDrawableFromView(childAt);
                HashMap hashMap;
                if (itemId != -1) {
                    hashMap = this.mOldViewCache;
                    hashMap.put(Long.valueOf(itemId), new ViewInfo(bitmapDrawableFromView, i2, childAt.getLeft(), 0, childAt.getRight(), 0));
                } else if (i2 >= count - footerViewsCount) {
                    int i3 = ((i2 + footerViewsCount) - count) + 1;
                    hashMap = this.mOldHeaderFooterViewCache;
                    hashMap.put(Long.valueOf(-((long) i3)), new ViewInfo(bitmapDrawableFromView, i2, childAt.getLeft(), 0, childAt.getRight(), 0));
                }
            }
        }
        final HashMap hashMap2 = new HashMap();
        for (int i4 = 0; i4 < arrayList2.size(); i4++) {
            int intValue = ((Integer) arrayList2.get(i4)).intValue();
            View childAt2 = viewGroup.getChildAt((intValue - i4) - firstVisiblePosition);
            if (childAt2 != null) {
                hashMap2.put(Integer.valueOf(intValue), Integer.valueOf(childAt2.getLeft()));
            }
        }
        final ListAdapter listAdapter = adapter;
        this.mInsertRunnable = new Runnable() {

            class C09461 extends AnimatorListenerAdapter {
                C09461() {
                }

                public void onAnimationEnd(Animator animator) {
                    SemAddDeleteHorizontalListAnimator.this.mGhostViewSnapshots.clear();
                    SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.invalidate();
                    SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.setEnabled(true);
                    if (SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener.onAnimationEnd(true);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.setEnabled(false);
                    if (SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteHorizontalListAnimator.this.mOnAddDeleteListener.onAnimationStart(true);
                    }
                }
            }

            public void run() {
                int height;
                int i;
                ViewInfo viewInfo;
                int i2;
                ValueAnimator ofObject;
                View -get1 = SemAddDeleteHorizontalListAnimator.this.mHorizontalListView;
                int firstVisiblePosition = -get1.getFirstVisiblePosition();
                int headerViewsCount = -get1.getHeaderViewsCount();
                int footerViewsCount = -get1.getFooterViewsCount();
                int childCount = -get1.getChildCount();
                int count = listAdapter.getCount();
                Collection arrayList = new ArrayList();
                int i3 = 0;
                int i4 = 0;
                if (childCount > headerViewsCount) {
                    i3 = SemAddDeleteHorizontalListAnimator.this.getChildMaxWidth();
                    i4 = -get1.getChildAt(headerViewsCount).getTop();
                    height = -get1.getChildAt(0).getHeight();
                } else {
                    height = -get1.getHeight();
                }
                for (int i5 = 0; i5 < childCount; i5++) {
                    int i6 = i5 + firstVisiblePosition;
                    long itemId = listAdapter.getItemId(i6);
                    View childAt = -get1.getChildAt(i5);
                    float left = (float) childAt.getLeft();
                    ViewInfo viewInfo2;
                    if (itemId == -1) {
                        viewInfo2 = (ViewInfo) SemAddDeleteHorizontalListAnimator.this.mOldHeaderFooterViewCache.remove(Long.valueOf(-((long) (((i6 + footerViewsCount) - count) + 1))));
                        if (viewInfo2 == null) {
                            Log.m31e(SemAddDeleteHorizontalListAnimator.TAG, "AFTER header/footer SOMETHING WENT WRONG, in the new layout, header/footer is appearing that was not present before!");
                        } else {
                            viewInfo2.recycleBitmap();
                            if (((float) viewInfo2.left) == left) {
                                Log.m31e(SemAddDeleteHorizontalListAnimator.TAG, "AFTER header/footer something strange is happening, the coordinates are same after layout, viewInfo.left=" + viewInfo2.left + ", newX=" + left);
                            } else {
                                arrayList.add(SemAddDeleteHorizontalListAnimator.this.getTranslateAnim(childAt, ((float) viewInfo2.left) - left, 0.0f));
                            }
                        }
                    } else {
                        Integer num = (Integer) hashMap2.remove(Integer.valueOf(i6));
                        viewInfo2 = (ViewInfo) SemAddDeleteHorizontalListAnimator.this.mOldViewCache.remove(Long.valueOf(itemId));
                        if (viewInfo2 != null) {
                            viewInfo2.recycleBitmap();
                            if (((float) viewInfo2.left) != left) {
                                arrayList.add(SemAddDeleteHorizontalListAnimator.this.getTranslateAnim(childAt, ((float) viewInfo2.left) - left, 0.0f));
                            }
                        } else if (num != null) {
                            arrayList.add(SemAddDeleteHorizontalListAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, ((float) num.intValue()) - left, 0.0f));
                        } else {
                            i = i5 + firstVisiblePosition;
                            int shiftCount = i - (i - SemAddDeleteHorizontalListAnimator.this.getShiftCount(i, arrayList2));
                            float left2 = ((float) (SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.isLayoutRtl() ? childAt.getLeft() + (shiftCount * i3) : childAt.getLeft() - (shiftCount * i3))) - left;
                            arrayList.add(hashSet.contains(Integer.valueOf(i)) ? SemAddDeleteHorizontalListAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, left2, 0.0f) : SemAddDeleteHorizontalListAnimator.this.getTranslateAnim(childAt, left2, 0.0f));
                        }
                    }
                }
                hashMap2.clear();
                Object obj = null;
                i = -get1.getLastVisiblePosition();
                for (Entry value : SemAddDeleteHorizontalListAnimator.this.mOldViewCache.entrySet()) {
                    i++;
                    if (!arrayList2.contains(Integer.valueOf(i))) {
                        viewInfo = (ViewInfo) value.getValue();
                        int newPositionForInsert = SemAddDeleteHorizontalListAnimator.this.getNewPositionForInsert(viewInfo.oldPosition, arrayList2);
                        if (newPositionForInsert < -get1.getFirstVisiblePosition()) {
                            i--;
                            shiftCount = -get1.getFirstVisiblePosition() - newPositionForInsert;
                            int left3 = childCount != 0 ? -get1.getChildAt(0).getLeft() : -get1.getLeft();
                            i2 = SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.isLayoutRtl() ? left3 + (shiftCount * i3) : left3 - (shiftCount * i3);
                        } else {
                            shiftCount = i - viewInfo.oldPosition;
                            i2 = SemAddDeleteHorizontalListAnimator.this.mHorizontalListView.isLayoutRtl() ? viewInfo.left - (shiftCount * i3) : viewInfo.left + (shiftCount * i3);
                        }
                        Rect rect = new Rect(viewInfo.left, i4, viewInfo.right, i4 + height);
                        rect = new Rect(i2, i4, rect.width() + i2, rect.height() + i4);
                        SemAddDeleteHorizontalListAnimator.this.mGhostViewSnapshots.add(viewInfo);
                        ofObject = ObjectAnimator.ofObject(viewInfo.viewSnapshot, "bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect});
                        arrayList.add(ofObject);
                        if (obj == null) {
                            ofObject.addUpdateListener(SemAddDeleteHorizontalListAnimator.this.mBitmapUpdateListener);
                            obj = 1;
                        }
                    }
                }
                for (Entry value2 : SemAddDeleteHorizontalListAnimator.this.mOldHeaderFooterViewCache.entrySet()) {
                    viewInfo = (ViewInfo) value2.getValue();
                    i2 = viewInfo.left + (arrayList2.size() * i3);
                    rect = new Rect(viewInfo.left, i4, viewInfo.right, i4 + height);
                    rect = new Rect(i2, i4, rect.width() + i2, rect.height() + i4);
                    SemAddDeleteHorizontalListAnimator.this.mGhostViewSnapshots.add(viewInfo);
                    ofObject = ObjectAnimator.ofObject(viewInfo.viewSnapshot, "bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect});
                    if (obj == null) {
                        ofObject.addUpdateListener(SemAddDeleteHorizontalListAnimator.this.mBitmapUpdateListener);
                    }
                    arrayList.add(ofObject);
                }
                SemAddDeleteHorizontalListAnimator.this.mOldViewCache.clear();
                SemAddDeleteHorizontalListAnimator.this.mOldHeaderFooterViewCache.clear();
                Animator animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.setInterpolator(SemAddDeleteHorizontalListAnimator.INSERT_INTERPOLATOR);
                animatorSet.addListener(new C09461());
                animatorSet.setDuration((long) SemAddDeleteHorizontalListAnimator.TRANSLATION_DURATION);
                animatorSet.start();
            }
        };
    }

    public void deleteFromAdapterCompleted() {
        if (this.deletePending) {
            this.deletePending = false;
            this.mHorizontalListView.getViewTreeObserver().addOnPreDrawListener(new C09421());
            return;
        }
        throw new SetDeletePendingIsNotCalledBefore();
    }

    public /* bridge */ /* synthetic */ void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void insertIntoAdapterCompleted() {
        if (this.insertPending) {
            this.insertPending = false;
            this.mHorizontalListView.getViewTreeObserver().addOnPreDrawListener(new C09453());
            return;
        }
        throw new SetInsertPendingIsNotCalledBefore();
    }

    public void setDelete(ArrayList<Integer> arrayList) {
        if (arrayList.size() != 0) {
            prepareDelete(arrayList);
            this.mOnAddDeleteListener.onDelete();
            deleteFromAdapterCompleted();
        }
    }

    public void setDeletePending(ArrayList<Integer> arrayList) {
        if (arrayList.size() != 0) {
            prepareDelete(arrayList);
            this.mOnAddDeleteListener.onDelete();
        }
    }

    public void setInsert(ArrayList<Integer> arrayList) {
        if (arrayList.size() != 0) {
            prepareInsert(arrayList);
            this.mOnAddDeleteListener.onAdd();
            insertIntoAdapterCompleted();
        }
    }

    public void setInsertPending(ArrayList<Integer> arrayList) {
        if (arrayList.size() != 0) {
            prepareInsert(arrayList);
            this.mOnAddDeleteListener.onAdd();
        }
    }

    public void setOnAddDeleteListener(OnAddDeleteListener onAddDeleteListener) {
        this.mOnAddDeleteListener = onAddDeleteListener;
    }

    public /* bridge */ /* synthetic */ void setTransitionDuration(int i) {
        super.setTransitionDuration(i);
    }
}
