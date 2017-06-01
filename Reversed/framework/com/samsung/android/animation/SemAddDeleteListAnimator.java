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
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class SemAddDeleteListAnimator extends SemAbsAddDeleteAnimator {
    private static String TAG = SemAddDeleteListAnimator.class.getSimpleName();
    private boolean deletePending = false;
    private boolean insertDeletePending = false;
    private boolean insertPending = false;
    private boolean isInsertDelete = false;
    private ListView mListView;
    LinkedHashMap<Long, ViewInfo> mOldHeaderFooterViewCache = new LinkedHashMap();
    LinkedHashMap<Long, ViewInfo> mOldViewCache = new LinkedHashMap();
    private OnAddDeleteListener mOnAddDeleteListener;

    class C09481 implements OnPreDrawListener {
        C09481() {
        }

        public boolean onPreDraw() {
            SemAddDeleteListAnimator.this.mListView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (SemAddDeleteListAnimator.this.mDeleteRunnable != null) {
                SemAddDeleteListAnimator.this.mDeleteRunnable.run();
                SemAddDeleteListAnimator.this.mDeleteRunnable = null;
            }
            return true;
        }
    }

    class C09513 implements OnPreDrawListener {
        C09513() {
        }

        public boolean onPreDraw() {
            SemAddDeleteListAnimator.this.mListView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (SemAddDeleteListAnimator.this.mInsertRunnable != null) {
                SemAddDeleteListAnimator.this.mInsertRunnable.run();
                SemAddDeleteListAnimator.this.mInsertRunnable = null;
            }
            return true;
        }
    }

    class C09545 implements OnPreDrawListener {
        C09545() {
        }

        public boolean onPreDraw() {
            SemAddDeleteListAnimator.this.mListView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (SemAddDeleteListAnimator.this.mInsertDeleteRunnable != null) {
                SemAddDeleteListAnimator.this.mInsertDeleteRunnable.run();
                SemAddDeleteListAnimator.this.mInsertDeleteRunnable = null;
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

    public SemAddDeleteListAnimator(Context context, ListView listView) {
        this.mListView = listView;
        this.mListView.setAddDeleteListAnimator(this);
        this.mHostView = listView;
    }

    private void capturePreAnimationViewCoordinates() {
        ViewGroup viewGroup = this.mListView;
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
                    this.mOldHeaderFooterViewCache.put(Long.valueOf(itemId), new ViewInfo(bitmapDrawableFromView, i2, 0, childAt.getTop(), 0, childAt.getBottom()));
                } else {
                    HashMap hashMap = this.mOldViewCache;
                    Long valueOf = Long.valueOf(itemId);
                    hashMap.put(valueOf, new ViewInfo(bitmapDrawableFromView, i + firstVisiblePosition, 0, childAt.getTop(), 0, childAt.getBottom()));
                }
            }
        }
    }

    private void ensureAdapterAndListener() {
        ListAdapter adapter = this.mListView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("Adapter need to be set before performing add/delete operations.");
        } else if (!adapter.hasStableIds()) {
            throw new IllegalStateException("SemAddDeleteListAnimator requires an adapter that has stable ids");
        } else if (this.mOnAddDeleteListener == null) {
            throw new IllegalStateException("OnAddDeleteListener need to be supplied before performing add/delete operations");
        }
    }

    private int getChildMaxHeight() {
        int childCount = this.mListView.getChildCount();
        int count = this.mListView.getAdapter().getCount();
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            int i3 = i2 + firstVisiblePosition;
            if (i3 >= this.mListView.getHeaderViewsCount() && i3 < count - this.mListView.getFooterViewsCount()) {
                int height = this.mListView.getChildAt(i2).getHeight();
                if (height > i) {
                    i = height;
                }
            }
        }
        return i;
    }

    private void prepareDelete(ArrayList<Integer> arrayList) {
        this.deletePending = true;
        final Object arrayList2 = new ArrayList(arrayList);
        ensureAdapterAndListener();
        Collections.sort(arrayList2);
        final HashSet hashSet = new HashSet(arrayList2);
        final int childCount = this.mListView.getChildCount();
        final int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        final ListAdapter adapter = this.mListView.getAdapter();
        capturePreAnimationViewCoordinates();
        this.mDeleteRunnable = new Runnable() {

            class C09491 extends AnimatorListenerAdapter {
                C09491() {
                }

                public void onAnimationEnd(Animator animator) {
                    if (SemAddDeleteListAnimator.this.mGhostViewSnapshots.size() > 0) {
                        for (ViewInfo recycleBitmap : SemAddDeleteListAnimator.this.mGhostViewSnapshots) {
                            recycleBitmap.recycleBitmap();
                        }
                    }
                    SemAddDeleteListAnimator.this.mGhostViewSnapshots.clear();
                    SemAddDeleteListAnimator.this.mListView.invalidate();
                    SemAddDeleteListAnimator.this.mListView.setEnabled(true);
                    if (SemAddDeleteListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteListAnimator.this.mOnAddDeleteListener.onAnimationEnd(false);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    if (SemAddDeleteListAnimator.this.mListView.isPressed()) {
                        SemAddDeleteListAnimator.this.mListView.setPressed(false);
                    }
                    SemAddDeleteListAnimator.this.mListView.setEnabled(false);
                    if (SemAddDeleteListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteListAnimator.this.mOnAddDeleteListener.onAnimationStart(false);
                    }
                }
            }

            public void run() {
                int width;
                ViewInfo viewInfo;
                View -get1 = SemAddDeleteListAnimator.this.mListView;
                int childCount = -get1.getChildCount();
                int firstVisiblePosition = -get1.getFirstVisiblePosition();
                int lastVisiblePosition = -get1.getLastVisiblePosition();
                int headerViewsCount = -get1.getHeaderViewsCount();
                int footerViewsCount = -get1.getFooterViewsCount();
                int count = adapter.getCount();
                Collection arrayList = new ArrayList();
                int i = 0;
                int i2 = 0;
                if (childCount > headerViewsCount) {
                    i = SemAddDeleteListAnimator.this.getChildMaxHeight() + -get1.getDividerHeight();
                    i2 = -get1.getChildAt(headerViewsCount).getLeft();
                    width = -get1.getChildAt(headerViewsCount).getWidth();
                } else {
                    width = -get1.getWidth();
                }
                Object obj = 1;
                int i3 = firstVisiblePosition - firstVisiblePosition;
                int i4 = i3;
                int i5 = (lastVisiblePosition + 1) + (childCount - childCount);
                for (int i6 = 0; i6 < childCount; i6++) {
                    float f;
                    View childAt = -get1.getChildAt(i6);
                    int i7 = i6 + firstVisiblePosition;
                    long itemId = adapter.getItemId(i7);
                    float top = (float) childAt.getTop();
                    if (itemId == -1) {
                        if (i7 < headerViewsCount) {
                            itemId = (long) (i7 + 1);
                        } else if (i7 >= count - footerViewsCount) {
                            itemId = (long) (-(((i7 + footerViewsCount) - count) + 1));
                        }
                        viewInfo = (ViewInfo) SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.remove(Long.valueOf(itemId));
                    } else {
                        viewInfo = (ViewInfo) SemAddDeleteListAnimator.this.mOldViewCache.remove(Long.valueOf(itemId));
                    }
                    if (viewInfo != null) {
                        viewInfo.recycleBitmap();
                        obj = null;
                        if (((float) viewInfo.top) != top) {
                            f = ((float) viewInfo.top) - top;
                            arrayList.add(SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, f));
                        }
                    } else {
                        int i8;
                        if (i4 <= 0 || r29 == null) {
                            i8 = i5 - i7;
                            i5++;
                        } else {
                            i8 = -i3;
                            i4--;
                        }
                        f = ((float) (childAt.getTop() + (i8 * i))) - top;
                        arrayList.add(SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, f));
                    }
                }
                Object obj2 = null;
                for (Entry value : SemAddDeleteListAnimator.this.mOldViewCache.entrySet()) {
                    viewInfo = (ViewInfo) value.getValue();
                    SemAddDeleteListAnimator.this.mGhostViewSnapshots.add(viewInfo);
                    Rect rect = new Rect(i2, viewInfo.top, i2 + width, viewInfo.bottom);
                    int newPosition = SemAddDeleteListAnimator.this.getNewPosition(viewInfo.oldPosition, arrayList2);
                    boolean contains = hashSet.contains(Integer.valueOf(viewInfo.oldPosition));
                    int i9 = newPosition - firstVisiblePosition;
                    if (i9 < 0 || i9 >= childCount) {
                        f = ((childCount == 0 ? (float) -get1.getPaddingTop() : (float) -get1.getChildAt(0).getTop()) - ((float) viewInfo.top)) - ((float) ((-i9) * i));
                    } else {
                        f = ((float) -get1.getChildAt(i9).getTop()) - ((float) viewInfo.top);
                    }
                    Rect rect2 = new Rect(rect);
                    rect2.offset(0, (int) f);
                    if (contains) {
                        int width2 = (int) (((1.0f - SemAddDeleteListAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.width()));
                        int height = (int) (((1.0f - SemAddDeleteListAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.height()));
                        rect2 = new Rect(rect2.left + width2, rect2.top + height, rect2.right - width2, rect2.bottom - height);
                    }
                    PropertyValuesHolder ofObject = PropertyValuesHolder.ofObject("bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect2});
                    PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{255, 0});
                    ValueAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(viewInfo.viewSnapshot, new PropertyValuesHolder[]{ofObject, ofInt});
                    if (obj2 == null) {
                        ofPropertyValuesHolder.addUpdateListener(SemAddDeleteListAnimator.this.mBitmapUpdateListener);
                        obj2 = 1;
                    }
                    arrayList.add(ofPropertyValuesHolder);
                }
                SemAddDeleteListAnimator.this.mOldViewCache.clear();
                SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.clear();
                Animator animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.addListener(new C09491());
                animatorSet.setInterpolator(SemAddDeleteListAnimator.DELETE_INTERPOLATOR);
                animatorSet.setDuration((long) SemAddDeleteListAnimator.TRANSLATION_DURATION);
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
        ViewGroup viewGroup = this.mListView;
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
                    hashMap.put(Long.valueOf(itemId), new ViewInfo(bitmapDrawableFromView, i2, 0, childAt.getTop(), 0, childAt.getBottom()));
                } else if (i2 >= count - footerViewsCount) {
                    int i3 = ((i2 + footerViewsCount) - count) + 1;
                    hashMap = this.mOldHeaderFooterViewCache;
                    hashMap.put(Long.valueOf(-((long) i3)), new ViewInfo(bitmapDrawableFromView, i2, 0, childAt.getTop(), 0, childAt.getBottom()));
                }
            }
        }
        final HashMap hashMap2 = new HashMap();
        for (int i4 = 0; i4 < arrayList2.size(); i4++) {
            int intValue = ((Integer) arrayList2.get(i4)).intValue();
            View childAt2 = viewGroup.getChildAt((intValue - i4) - firstVisiblePosition);
            if (childAt2 != null) {
                hashMap2.put(Integer.valueOf(intValue), Integer.valueOf(childAt2.getTop()));
            }
        }
        final ListAdapter listAdapter = adapter;
        this.mInsertRunnable = new Runnable() {

            class C09521 extends AnimatorListenerAdapter {
                C09521() {
                }

                public void onAnimationEnd(Animator animator) {
                    SemAddDeleteListAnimator.this.mGhostViewSnapshots.clear();
                    SemAddDeleteListAnimator.this.mListView.invalidate();
                    SemAddDeleteListAnimator.this.mListView.setEnabled(true);
                    if (SemAddDeleteListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteListAnimator.this.mOnAddDeleteListener.onAnimationEnd(true);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    if (SemAddDeleteListAnimator.this.mListView.isPressed()) {
                        SemAddDeleteListAnimator.this.mListView.setPressed(false);
                    }
                    SemAddDeleteListAnimator.this.mListView.setEnabled(false);
                    if (SemAddDeleteListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteListAnimator.this.mOnAddDeleteListener.onAnimationStart(true);
                    }
                }
            }

            public void run() {
                int width;
                int i;
                ValueAnimator ofObject;
                View -get1 = SemAddDeleteListAnimator.this.mListView;
                int firstVisiblePosition = -get1.getFirstVisiblePosition();
                int headerViewsCount = -get1.getHeaderViewsCount();
                int footerViewsCount = -get1.getFooterViewsCount();
                int childCount = -get1.getChildCount();
                int count = listAdapter.getCount();
                Collection arrayList = new ArrayList();
                int i2 = 0;
                int i3 = 0;
                if (childCount > headerViewsCount) {
                    i2 = SemAddDeleteListAnimator.this.getChildMaxHeight() + -get1.getDividerHeight();
                    i3 = -get1.getChildAt(headerViewsCount).getLeft();
                    width = -get1.getChildAt(0).getWidth();
                } else {
                    width = -get1.getWidth();
                }
                for (int i4 = 0; i4 < childCount; i4++) {
                    int i5 = i4 + firstVisiblePosition;
                    long itemId = listAdapter.getItemId(i5);
                    View childAt = -get1.getChildAt(i4);
                    float top = (float) childAt.getTop();
                    ViewInfo viewInfo;
                    if (itemId == -1) {
                        viewInfo = (ViewInfo) SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.remove(Long.valueOf(-((long) (((i5 + footerViewsCount) - count) + 1))));
                        if (viewInfo == null) {
                            Log.m31e(SemAddDeleteListAnimator.TAG, "AFTER header/footer SOMETHING WENT WRONG, in the new layout, header/footer is appearing that was not present before!");
                        } else {
                            viewInfo.recycleBitmap();
                            if (((float) viewInfo.top) == top) {
                                Log.m31e(SemAddDeleteListAnimator.TAG, "AFTER header/footer something strange is happening, the coordinates are same after layout, viewInfo.top=" + viewInfo.top + ", newY=" + top);
                            } else {
                                arrayList.add(SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, ((float) viewInfo.top) - top));
                            }
                        }
                    } else {
                        Integer num = (Integer) hashMap2.remove(Integer.valueOf(i5));
                        viewInfo = (ViewInfo) SemAddDeleteListAnimator.this.mOldViewCache.remove(Long.valueOf(itemId));
                        if (viewInfo != null) {
                            viewInfo.recycleBitmap();
                            if (((float) viewInfo.top) != top) {
                                arrayList.add(SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, ((float) viewInfo.top) - top));
                            }
                        } else if (num != null) {
                            arrayList.add(SemAddDeleteListAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, 0.0f, ((float) num.intValue()) - top));
                        } else {
                            i = i4 + firstVisiblePosition;
                            float top2 = ((float) (childAt.getTop() - ((i - (i - SemAddDeleteListAnimator.this.getShiftCount(i, arrayList2))) * i2))) - top;
                            arrayList.add(hashSet.contains(Integer.valueOf(i)) ? SemAddDeleteListAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, 0.0f, top2) : SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, top2));
                        }
                    }
                }
                hashMap2.clear();
                Object obj = null;
                i = -get1.getLastVisiblePosition();
                for (Entry value : SemAddDeleteListAnimator.this.mOldViewCache.entrySet()) {
                    ViewInfo viewInfo2;
                    int top3;
                    i++;
                    if (!arrayList2.contains(Integer.valueOf(i))) {
                        viewInfo2 = (ViewInfo) value.getValue();
                        int newPositionForInsert = SemAddDeleteListAnimator.this.getNewPositionForInsert(viewInfo2.oldPosition, arrayList2);
                        if (newPositionForInsert < -get1.getFirstVisiblePosition()) {
                            i--;
                            top3 = -get1.getChildAt(0).getTop() - ((-get1.getFirstVisiblePosition() - newPositionForInsert) * i2);
                        } else {
                            top3 = viewInfo2.top + ((i - viewInfo2.oldPosition) * i2);
                        }
                        Rect rect = new Rect(i3, viewInfo2.top, i3 + width, viewInfo2.bottom);
                        rect = new Rect(i3, top3, rect.width() + i3, rect.height() + top3);
                        SemAddDeleteListAnimator.this.mGhostViewSnapshots.add(viewInfo2);
                        ofObject = ObjectAnimator.ofObject(viewInfo2.viewSnapshot, "bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect});
                        arrayList.add(ofObject);
                        if (obj == null) {
                            ofObject.addUpdateListener(SemAddDeleteListAnimator.this.mBitmapUpdateListener);
                            obj = 1;
                        }
                    }
                }
                for (Entry value2 : SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.entrySet()) {
                    viewInfo2 = (ViewInfo) value2.getValue();
                    top3 = viewInfo2.top + (arrayList2.size() * i2);
                    rect = new Rect(i3, viewInfo2.top, i3 + width, viewInfo2.bottom);
                    rect = new Rect(i3, top3, rect.width() + i3, rect.height() + top3);
                    SemAddDeleteListAnimator.this.mGhostViewSnapshots.add(viewInfo2);
                    ofObject = ObjectAnimator.ofObject(viewInfo2.viewSnapshot, "bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect});
                    if (obj == null) {
                        ofObject.addUpdateListener(SemAddDeleteListAnimator.this.mBitmapUpdateListener);
                    }
                    arrayList.add(ofObject);
                }
                SemAddDeleteListAnimator.this.mOldViewCache.clear();
                SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.clear();
                Animator animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.setInterpolator(SemAddDeleteListAnimator.INSERT_INTERPOLATOR);
                animatorSet.addListener(new C09521());
                animatorSet.setDuration((long) SemAddDeleteListAnimator.TRANSLATION_DURATION);
                animatorSet.start();
            }
        };
    }

    private void prepareInsertDelete(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2) {
        this.insertDeletePending = true;
        ensureAdapterAndListener();
        final ArrayList arrayList3 = new ArrayList(arrayList);
        Collections.sort(arrayList3);
        final HashSet hashSet = new HashSet(arrayList3);
        final ArrayList arrayList4 = new ArrayList(arrayList2);
        Collections.sort(arrayList4);
        final HashSet hashSet2 = new HashSet(arrayList4);
        ListAdapter adapter = this.mListView.getAdapter();
        int childCount = this.mListView.getChildCount();
        int count = adapter.getCount();
        int firstVisiblePosition = this.mListView.getFirstVisiblePosition();
        int headerViewsCount = this.mListView.getHeaderViewsCount();
        int footerViewsCount = this.mListView.getFooterViewsCount();
        for (int i = 0; i < childCount; i++) {
            int i2 = i + firstVisiblePosition;
            View childAt = this.mListView.getChildAt(i);
            long itemId = adapter.getItemId(i2);
            if (childAt.getHeight() == 0 || childAt.getWidth() == 0) {
                Log.m31e(TAG, "setInsert() child's one of dimensions is 0, i=" + i);
            } else {
                BitmapDrawable bitmapDrawableFromView = SemAnimatorUtils.getBitmapDrawableFromView(childAt);
                HashMap hashMap;
                if (itemId == -1) {
                    if (i2 < headerViewsCount) {
                        itemId = (long) (i2 + 1);
                    } else if (i2 >= count - footerViewsCount) {
                        itemId = (long) (-(((i2 + footerViewsCount) - count) + 1));
                    }
                    hashMap = this.mOldHeaderFooterViewCache;
                    hashMap.put(Long.valueOf(itemId), new ViewInfo(bitmapDrawableFromView, i2, 0, childAt.getTop(), 0, childAt.getBottom()));
                } else {
                    hashMap = this.mOldViewCache;
                    hashMap.put(Long.valueOf(itemId), new ViewInfo(bitmapDrawableFromView, i2, 0, childAt.getTop(), 0, childAt.getBottom()));
                }
            }
        }
        final HashMap hashMap2 = new HashMap();
        for (int i3 = 0; i3 < arrayList3.size(); i3++) {
            int intValue = ((Integer) arrayList3.get(i3)).intValue();
            int i4 = intValue - i3;
            for (int i5 = 0; i5 < arrayList4.size(); i5++) {
                if (((Integer) arrayList4.get(i5)).intValue() <= i4) {
                    i4++;
                }
            }
            View childAt2 = this.mListView.getChildAt(i4 - firstVisiblePosition);
            if (childAt2 != null) {
                hashMap2.put(Integer.valueOf(intValue), Integer.valueOf(childAt2.getTop()));
            }
        }
        final ListAdapter listAdapter = adapter;
        final int i6 = childCount;
        this.mInsertDeleteRunnable = new Runnable() {

            class C09551 extends AnimatorListenerAdapter {
                C09551() {
                }

                public void onAnimationEnd(Animator animator) {
                    SemAddDeleteListAnimator.this.mGhostViewSnapshots.clear();
                    SemAddDeleteListAnimator.this.mListView.invalidate();
                    SemAddDeleteListAnimator.this.mListView.setEnabled(true);
                    if (SemAddDeleteListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteListAnimator.this.mOnAddDeleteListener.onAnimationEnd(true);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    if (SemAddDeleteListAnimator.this.mListView.isPressed()) {
                        SemAddDeleteListAnimator.this.mListView.setPressed(false);
                    }
                    SemAddDeleteListAnimator.this.mListView.setEnabled(false);
                    if (SemAddDeleteListAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteListAnimator.this.mOnAddDeleteListener.onAnimationStart(true);
                    }
                }
            }

            public void run() {
                int width;
                View -get1 = SemAddDeleteListAnimator.this.mListView;
                int firstVisiblePosition = -get1.getFirstVisiblePosition();
                int lastVisiblePosition = -get1.getLastVisiblePosition();
                int headerViewsCount = -get1.getHeaderViewsCount();
                int footerViewsCount = -get1.getFooterViewsCount();
                int childCount = -get1.getChildCount();
                int count = listAdapter.getCount();
                Collection arrayList = new ArrayList();
                int i = 0;
                int i2 = 0;
                if (childCount > headerViewsCount) {
                    i = SemAddDeleteListAnimator.this.getChildMaxHeight() + -get1.getDividerHeight();
                    i2 = -get1.getChildAt(headerViewsCount).getLeft();
                    width = -get1.getChildAt(0).getWidth();
                } else {
                    width = -get1.getWidth();
                }
                Object obj = 1;
                int i3 = firstVisiblePosition;
                int i4 = firstVisiblePosition;
                int i5 = (lastVisiblePosition + 1) + (i6 - childCount);
                for (int i6 = 0; i6 < childCount; i6++) {
                    ViewInfo viewInfo;
                    int i7 = i6 + firstVisiblePosition;
                    long itemId = listAdapter.getItemId(i7);
                    View childAt = -get1.getChildAt(i6);
                    float top = (float) childAt.getTop();
                    if (itemId == -1) {
                        if (i7 < headerViewsCount) {
                            itemId = (long) (i7 + 1);
                        } else if (i7 >= count - footerViewsCount) {
                            itemId = -((long) (((i7 + footerViewsCount) - count) + 1));
                        }
                        viewInfo = (ViewInfo) SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.remove(Long.valueOf(itemId));
                        if (viewInfo == null) {
                            Log.m31e(SemAddDeleteListAnimator.TAG, "AFTER header/footer SOMETHING WENT WRONG, in the new layout, header/footer is appearing that was not present before!");
                        } else {
                            viewInfo.recycleBitmap();
                            if (((float) viewInfo.top) == top) {
                                Log.m31e(SemAddDeleteListAnimator.TAG, "AFTER header/footer something strange is happening, the coordinates are same after layout, viewInfo.top=" + viewInfo.top + ", newY=" + top);
                            } else {
                                arrayList.add(SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, ((float) viewInfo.top) - top));
                            }
                        }
                    } else {
                        Integer num = (Integer) hashMap2.remove(Integer.valueOf(i7));
                        viewInfo = (ViewInfo) SemAddDeleteListAnimator.this.mOldViewCache.remove(Long.valueOf(itemId));
                        if (viewInfo != null) {
                            viewInfo.recycleBitmap();
                            obj = null;
                            if (((float) viewInfo.top) != top) {
                                arrayList.add(SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, ((float) viewInfo.top) - top));
                            }
                        } else if (num != null) {
                            arrayList.add(SemAddDeleteListAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, 0.0f, ((float) num.intValue()) - top));
                        } else {
                            int i8 = i6 + firstVisiblePosition;
                            if (hashSet.contains(Integer.valueOf(i8))) {
                                arrayList.add(SemAddDeleteListAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, 0.0f, ((float) (childAt.getTop() - ((i8 - (i8 - SemAddDeleteListAnimator.this.getShiftCount(i8, arrayList3, arrayList4))) * i))) - top));
                            } else {
                                int i9;
                                if (i4 <= 0 || r34 == null) {
                                    i9 = i5 - i7;
                                    i5++;
                                } else {
                                    i9 = -SemAddDeleteListAnimator.this.getShiftCount(i8, arrayList3, arrayList4);
                                    i4--;
                                }
                                arrayList.add(SemAddDeleteListAnimator.this.getTranslateAnim(childAt, 0.0f, ((float) (childAt.getTop() + (i9 * i))) - top));
                            }
                        }
                    }
                }
                hashMap2.clear();
                int lastVisiblePosition2 = -get1.getLastVisiblePosition();
                Object obj2 = null;
                for (Entry value : SemAddDeleteListAnimator.this.mOldViewCache.entrySet()) {
                    float paddingTop;
                    ValueAnimator ofPropertyValuesHolder;
                    viewInfo = (ViewInfo) value.getValue();
                    SemAddDeleteListAnimator.this.mGhostViewSnapshots.add(viewInfo);
                    Rect rect = new Rect(i2, viewInfo.top, i2 + width, viewInfo.bottom);
                    int newPosition = SemAddDeleteListAnimator.this.getNewPosition(viewInfo.oldPosition, arrayList3, arrayList4);
                    boolean contains = hashSet2.contains(Integer.valueOf(viewInfo.oldPosition));
                    int i10 = newPosition - firstVisiblePosition;
                    if (i10 < 0) {
                        paddingTop = ((childCount == 0 ? (float) -get1.getPaddingTop() : (float) -get1.getChildAt(0).getTop()) - ((float) viewInfo.top)) - ((float) ((-i10) * i));
                    } else if (i10 >= childCount) {
                        paddingTop = (-get1.getChildAt(childCount + -1) == null ? (float) (0 - viewInfo.top) : (float) (-get1.getChildAt(childCount - 1).getTop() - viewInfo.top)) + ((float) (((i10 - childCount) + 1) * i));
                    } else {
                        paddingTop = ((float) -get1.getChildAt(i10).getTop()) - ((float) viewInfo.top);
                    }
                    Rect rect2 = new Rect(rect);
                    rect2.offset(0, (int) paddingTop);
                    PropertyValuesHolder ofObject;
                    if (contains) {
                        int width2 = (int) (((1.0f - SemAddDeleteListAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.width()));
                        int height = (int) (((1.0f - SemAddDeleteListAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.height()));
                        ofObject = PropertyValuesHolder.ofObject("bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, new Rect(rect2.left + width2, rect2.top + height, rect2.right - width2, rect2.bottom - height)});
                        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{255, 0});
                        ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(viewInfo.viewSnapshot, new PropertyValuesHolder[]{ofObject, ofInt});
                        rect2 = rect;
                    } else {
                        ofObject = PropertyValuesHolder.ofObject("bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect2});
                        ofPropertyValuesHolder = ObjectAnimator.ofObject(viewInfo.viewSnapshot, "bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect2});
                    }
                    if (obj2 == null) {
                        ofPropertyValuesHolder.addUpdateListener(SemAddDeleteListAnimator.this.mBitmapUpdateListener);
                        obj2 = 1;
                    }
                    arrayList.add(ofPropertyValuesHolder);
                }
                for (Entry value2 : SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.entrySet()) {
                    ViewInfo viewInfo2 = (ViewInfo) value2.getValue();
                    int size = viewInfo2.top + (arrayList3.size() * i);
                    rect = new Rect(i2, viewInfo2.top, i2 + width, viewInfo2.bottom);
                    rect = new Rect(i2, size, rect.width() + i2, rect.height() + size);
                    SemAddDeleteListAnimator.this.mGhostViewSnapshots.add(viewInfo2);
                    ValueAnimator ofObject2 = ObjectAnimator.ofObject(viewInfo2.viewSnapshot, "bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect});
                    if (obj2 == null) {
                        ofObject2.addUpdateListener(SemAddDeleteListAnimator.this.mBitmapUpdateListener);
                    }
                    arrayList.add(ofObject2);
                }
                SemAddDeleteListAnimator.this.mOldViewCache.clear();
                SemAddDeleteListAnimator.this.mOldHeaderFooterViewCache.clear();
                Animator animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.setInterpolator(SemAddDeleteListAnimator.INSERT_INTERPOLATOR);
                animatorSet.addListener(new C09551());
                animatorSet.setDuration((long) SemAddDeleteListAnimator.TRANSLATION_DURATION);
                animatorSet.start();
            }
        };
    }

    public void deleteFromAdapterCompleted() {
        if (this.deletePending) {
            this.deletePending = false;
            this.mListView.getViewTreeObserver().addOnPreDrawListener(new C09481());
            return;
        }
        throw new SetDeletePendingIsNotCalledBefore();
    }

    public /* bridge */ /* synthetic */ void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void insertDeleteFromAdapterCompleted() {
        if (this.insertDeletePending) {
            this.insertDeletePending = false;
            this.mListView.getViewTreeObserver().addOnPreDrawListener(new C09545());
            return;
        }
        throw new SetDeletePendingIsNotCalledBefore();
    }

    public void insertIntoAdapterCompleted() {
        if (this.insertPending) {
            this.insertPending = false;
            this.mListView.getViewTreeObserver().addOnPreDrawListener(new C09513());
            return;
        }
        throw new SetInsertPendingIsNotCalledBefore();
    }

    public boolean isInsertDeleting() {
        return this.isInsertDelete;
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

    public void setInsertDelete(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2) {
        if (arrayList.size() != 0 || arrayList2.size() != 0) {
            if (arrayList2.size() == 0) {
                prepareInsert(arrayList);
                this.mOnAddDeleteListener.onAdd();
                insertIntoAdapterCompleted();
            } else if (arrayList.size() == 0) {
                prepareDelete(arrayList2);
                this.mOnAddDeleteListener.onDelete();
                deleteFromAdapterCompleted();
            } else {
                prepareInsertDelete(arrayList, arrayList2);
                this.isInsertDelete = true;
                this.mOnAddDeleteListener.onDelete();
                this.mOnAddDeleteListener.onAdd();
                this.isInsertDelete = false;
                insertDeleteFromAdapterCompleted();
            }
        }
    }

    public void setInsertDeletePending(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2) {
        if (arrayList.size() != 0 || arrayList2.size() != 0) {
            if (arrayList2.size() == 0) {
                prepareInsert(arrayList);
                this.mOnAddDeleteListener.onAdd();
            } else if (arrayList.size() == 0) {
                prepareDelete(arrayList2);
                this.mOnAddDeleteListener.onDelete();
            } else {
                prepareInsertDelete(arrayList, arrayList2);
                this.isInsertDelete = true;
                this.mOnAddDeleteListener.onDelete();
                this.mOnAddDeleteListener.onAdd();
                this.isInsertDelete = false;
            }
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
