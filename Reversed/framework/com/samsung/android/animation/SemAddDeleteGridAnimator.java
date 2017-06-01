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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class SemAddDeleteGridAnimator extends SemAbsAddDeleteAnimator {
    private static final String TAG = SemAddDeleteGridAnimator.class.getSimpleName();
    private boolean deletePending = false;
    private boolean insertPending = false;
    private GridView mGridView;
    LinkedHashMap<Long, ViewInfo> mOldViewCache = new LinkedHashMap();
    private OnAddDeleteListener mOnAddDeleteListener;

    class C09361 implements OnPreDrawListener {
        C09361() {
        }

        public boolean onPreDraw() {
            SemAddDeleteGridAnimator.this.mGridView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (SemAddDeleteGridAnimator.this.mDeleteRunnable != null) {
                SemAddDeleteGridAnimator.this.mDeleteRunnable.run();
                SemAddDeleteGridAnimator.this.mDeleteRunnable = null;
            }
            return true;
        }
    }

    class C09393 implements OnPreDrawListener {
        C09393() {
        }

        public boolean onPreDraw() {
            SemAddDeleteGridAnimator.this.mGridView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (SemAddDeleteGridAnimator.this.mInsertRunnable != null) {
                SemAddDeleteGridAnimator.this.mInsertRunnable.run();
                SemAddDeleteGridAnimator.this.mInsertRunnable = null;
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

    public SemAddDeleteGridAnimator(Context context, GridView gridView) {
        this.mGridView = gridView;
        this.mGridView.setAddDeleteGridAnimator(this);
        this.mHostView = gridView;
    }

    private void ensureAdapterAndListener() {
        ListAdapter adapter = this.mGridView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("Adapter need to be set before performing add/delete operations.");
        } else if (!adapter.hasStableIds()) {
            throw new IllegalStateException("SemAddDeleteGridAnimator requires an adapter that has stable ids");
        } else if (this.mOnAddDeleteListener == null) {
            throw new IllegalStateException("OnAddDeleteListener need to be supplied before performing add/delete operations");
        }
    }

    private int getNextAppearingViewPosition(HashSet<Integer> hashSet, int i) {
        int i2 = i + 1;
        while (hashSet.contains(Integer.valueOf(i2))) {
            i2++;
        }
        return i2;
    }

    private void prepareDelete(ArrayList<Integer> arrayList) {
        this.deletePending = true;
        ensureAdapterAndListener();
        final Object arrayList2 = new ArrayList(arrayList);
        Collections.sort(arrayList2);
        final HashSet hashSet = new HashSet(arrayList2);
        ViewGroup viewGroup = this.mGridView;
        ListAdapter adapter = viewGroup.getAdapter();
        int childCount = viewGroup.getChildCount();
        int firstVisiblePosition = viewGroup.getFirstVisiblePosition();
        int lastVisiblePosition = viewGroup.getLastVisiblePosition();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            long itemId = adapter.getItemId(i + firstVisiblePosition);
            HashMap hashMap = this.mOldViewCache;
            hashMap.put(Long.valueOf(itemId), new ViewInfo(SemAnimatorUtils.getBitmapDrawableFromView(childAt), i + firstVisiblePosition, childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom()));
        }
        final int height = viewGroup.getChildAt(0).getHeight();
        final ViewGroup viewGroup2 = viewGroup;
        final int i2 = firstVisiblePosition;
        final int i3 = lastVisiblePosition;
        final ListAdapter listAdapter = adapter;
        this.mDeleteRunnable = new Runnable() {

            class C09371 extends AnimatorListenerAdapter {
                C09371() {
                }

                public void onAnimationEnd(Animator animator) {
                    SemAddDeleteGridAnimator.this.mGhostViewSnapshots.clear();
                    SemAddDeleteGridAnimator.this.mGridView.invalidate();
                    SemAddDeleteGridAnimator.this.mGridView.setEnabled(true);
                    if (SemAddDeleteGridAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteGridAnimator.this.mOnAddDeleteListener.onAnimationEnd(false);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    SemAddDeleteGridAnimator.this.mGridView.setEnabled(false);
                    if (SemAddDeleteGridAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteGridAnimator.this.mOnAddDeleteListener.onAnimationStart(false);
                    }
                }
            }

            public void run() {
                Collection arrayList = new ArrayList();
                int childCount = viewGroup2.getChildCount();
                int firstVisiblePosition = viewGroup2.getFirstVisiblePosition();
                int numColumns = viewGroup2.getNumColumns();
                int top = childCount > numColumns ? viewGroup2.getChildAt(numColumns).getTop() - viewGroup2.getChildAt(0).getTop() : height;
                Object obj = 1;
                int i = i2 - firstVisiblePosition;
                int i2 = i;
                int i3 = i3;
                for (int i4 = 0; i4 < childCount; i4++) {
                    float f;
                    float f2;
                    View childAt = viewGroup2.getChildAt(i4);
                    ViewInfo viewInfo = (ViewInfo) SemAddDeleteGridAnimator.this.mOldViewCache.remove(Long.valueOf(listAdapter.getItemId(i4 + firstVisiblePosition)));
                    float left = (float) childAt.getLeft();
                    float top2 = (float) childAt.getTop();
                    if (viewInfo != null) {
                        obj = null;
                        viewInfo.recycleBitmap();
                        if (((float) viewInfo.left) != left || ((float) viewInfo.top) != top2) {
                            f = ((float) viewInfo.left) - left;
                            f2 = ((float) viewInfo.top) - top2;
                            arrayList.add(SemAddDeleteGridAnimator.this.getTranslateAnim(childAt, f, f2));
                        }
                    } else {
                        int -wrap0;
                        int i5 = i4 + firstVisiblePosition;
                        if (i2 <= 0 || r23 == null) {
                            -wrap0 = SemAddDeleteGridAnimator.this.getNextAppearingViewPosition(hashSet, i3);
                            i3 = -wrap0;
                        } else {
                            -wrap0 = i5 - i;
                            i2--;
                        }
                        int floor = ((int) Math.floor(((double) -wrap0) / ((double) numColumns))) - (i5 / numColumns);
                        int i6 = -wrap0 % numColumns;
                        if (i6 < 0) {
                            i6 += numColumns;
                        }
                        f = ((float) (childCount > i6 ? viewGroup2.getChildAt(i6).getLeft() : viewGroup2.getChildAt(0).getLeft() + (viewGroup2.getChildAt(0).getWidth() * i6))) - left;
                        f2 = ((float) (childAt.getTop() + (floor * top))) - top2;
                        arrayList.add(SemAddDeleteGridAnimator.this.getTranslateAnim(childAt, f, f2));
                    }
                }
                Object obj2 = null;
                for (Entry value : SemAddDeleteGridAnimator.this.mOldViewCache.entrySet()) {
                    float left2;
                    float f3;
                    viewInfo = (ViewInfo) value.getValue();
                    SemAddDeleteGridAnimator.this.mGhostViewSnapshots.add(viewInfo);
                    Rect rect = new Rect(viewInfo.left, viewInfo.top, viewInfo.right, viewInfo.bottom);
                    int newPosition = SemAddDeleteGridAnimator.this.getNewPosition(viewInfo.oldPosition, arrayList2);
                    boolean contains = hashSet.contains(Integer.valueOf(viewInfo.oldPosition));
                    int i7 = newPosition - firstVisiblePosition;
                    if (i7 < 0 || i7 >= childCount) {
                        left2 = childCount > newPosition % numColumns ? (float) viewGroup2.getChildAt(newPosition % numColumns).getLeft() : (float) viewGroup2.getPaddingLeft();
                        f3 = (float) (viewInfo.top - (((viewInfo.oldPosition / numColumns) - (newPosition / numColumns)) * top));
                    } else {
                        left2 = (float) viewGroup2.getChildAt(i7).getLeft();
                        f3 = (float) viewGroup2.getChildAt(i7).getTop();
                    }
                    f = left2 - ((float) viewInfo.left);
                    f2 = f3 - ((float) viewInfo.top);
                    Rect rect2 = new Rect(rect);
                    rect2.offset((int) f, (int) f2);
                    if (contains) {
                        int width = (int) (((1.0f - SemAddDeleteGridAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.width()));
                        int height = (int) (((1.0f - SemAddDeleteGridAnimator.START_SCALE_FACTOR) / 2.0f) * ((float) rect2.height()));
                        rect2 = new Rect(rect2.left + width, rect2.top + height, rect2.right - width, rect2.bottom - height);
                    }
                    PropertyValuesHolder ofObject = PropertyValuesHolder.ofObject("bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect2});
                    PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{255, 0});
                    ValueAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(viewInfo.viewSnapshot, new PropertyValuesHolder[]{ofObject, ofInt});
                    if (obj2 == null) {
                        ofPropertyValuesHolder.addUpdateListener(SemAddDeleteGridAnimator.this.mBitmapUpdateListener);
                        obj2 = 1;
                    }
                    arrayList.add(ofPropertyValuesHolder);
                }
                SemAddDeleteGridAnimator.this.mOldViewCache.clear();
                Animator animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.addListener(new C09371());
                animatorSet.setInterpolator(SemAddDeleteGridAnimator.DELETE_INTERPOLATOR);
                animatorSet.setDuration((long) SemAddDeleteGridAnimator.TRANSLATION_DURATION);
                animatorSet.start();
            }
        };
    }

    private void prepareInsert(ArrayList<Integer> arrayList) {
        this.insertPending = true;
        ensureAdapterAndListener();
        Collections.sort(arrayList);
        HashSet hashSet = new HashSet(arrayList);
        ViewGroup viewGroup = this.mGridView;
        ListAdapter adapter = viewGroup.getAdapter();
        int childCount = viewGroup.getChildCount();
        int firstVisiblePosition = viewGroup.getFirstVisiblePosition();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            long itemId = adapter.getItemId(i + firstVisiblePosition);
            HashMap hashMap = this.mOldViewCache;
            hashMap.put(Long.valueOf(itemId), new ViewInfo(SemAnimatorUtils.getBitmapDrawableFromView(childAt), i + firstVisiblePosition, childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom()));
        }
        final HashMap hashMap2 = new HashMap();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            int intValue = ((Integer) arrayList.get(i2)).intValue();
            if (viewGroup.getChildAt((intValue - i2) - firstVisiblePosition) != null) {
                hashMap2.put(Integer.valueOf(intValue), new float[]{(float) r21.getLeft(), (float) r21.getTop()});
            }
        }
        final ListAdapter listAdapter = adapter;
        final ArrayList<Integer> arrayList2 = arrayList;
        final HashSet hashSet2 = hashSet;
        this.mInsertRunnable = new Runnable() {

            class C09401 extends AnimatorListenerAdapter {
                C09401() {
                }

                public void onAnimationEnd(Animator animator) {
                    SemAddDeleteGridAnimator.this.mGhostViewSnapshots.clear();
                    SemAddDeleteGridAnimator.this.mGridView.invalidate();
                    SemAddDeleteGridAnimator.this.mGridView.setEnabled(true);
                    if (SemAddDeleteGridAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteGridAnimator.this.mOnAddDeleteListener.onAnimationEnd(true);
                    }
                }

                public void onAnimationStart(Animator animator) {
                    SemAddDeleteGridAnimator.this.mGridView.setEnabled(false);
                    if (SemAddDeleteGridAnimator.this.mOnAddDeleteListener != null) {
                        SemAddDeleteGridAnimator.this.mOnAddDeleteListener.onAnimationStart(true);
                    }
                }
            }

            public void run() {
                int i;
                ViewGroup -get0 = SemAddDeleteGridAnimator.this.mGridView;
                int firstVisiblePosition = -get0.getFirstVisiblePosition();
                int childCount = -get0.getChildCount();
                Collection arrayList = new ArrayList();
                int numColumns = -get0.getNumColumns();
                int i2 = 0;
                if (childCount > numColumns) {
                    i2 = -get0.getChildAt(numColumns).getTop() - -get0.getChildAt(0).getTop();
                }
                for (int i3 = 0; i3 < childCount; i3++) {
                    long itemId = listAdapter.getItemId(i3 + firstVisiblePosition);
                    View childAt = -get0.getChildAt(i3);
                    float[] fArr = (float[]) hashMap2.get(Integer.valueOf(i3 + firstVisiblePosition));
                    float left = (float) childAt.getLeft();
                    float top = (float) childAt.getTop();
                    ViewInfo viewInfo = (ViewInfo) SemAddDeleteGridAnimator.this.mOldViewCache.remove(Long.valueOf(itemId));
                    if (viewInfo != null) {
                        viewInfo.recycleBitmap();
                        if (((float) viewInfo.left) != left || ((float) viewInfo.top) != top) {
                            arrayList.add(SemAddDeleteGridAnimator.this.getTranslateAnim(childAt, ((float) viewInfo.left) - left, ((float) viewInfo.top) - top));
                        }
                    } else if (fArr != null) {
                        arrayList.add(SemAddDeleteGridAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, fArr[0] - left, fArr[1] - top));
                    } else {
                        i = i3 + firstVisiblePosition;
                        int shiftCount = i - SemAddDeleteGridAnimator.this.getShiftCount(i, arrayList2);
                        float left2 = ((float) -get0.getChildAt(shiftCount % numColumns).getLeft()) - left;
                        float top2 = ((float) (childAt.getTop() - (((i / numColumns) - (shiftCount / numColumns)) * i2))) - top;
                        arrayList.add(hashSet2.contains(Integer.valueOf(i)) ? SemAddDeleteGridAnimator.this.getInsertTranslateAlphaScaleAnim(childAt, left2, top2) : SemAddDeleteGridAnimator.this.getTranslateAnim(childAt, left2, top2));
                    }
                }
                i = -get0.getLastVisiblePosition();
                Object obj = null;
                for (Entry value : SemAddDeleteGridAnimator.this.mOldViewCache.entrySet()) {
                    i++;
                    if (!arrayList2.contains(Integer.valueOf(i))) {
                        ViewInfo viewInfo2 = (ViewInfo) value.getValue();
                        left = (float) -get0.getChildAt(i % numColumns).getLeft();
                        top = (float) (viewInfo2.top + (((i / numColumns) - (viewInfo2.oldPosition / numColumns)) * i2));
                        Rect rect = new Rect(viewInfo2.left, viewInfo2.top, viewInfo2.right, viewInfo2.bottom);
                        rect = new Rect((int) left, (int) top, (int) (((float) rect.width()) + left), ((int) top) + rect.height());
                        SemAddDeleteGridAnimator.this.mGhostViewSnapshots.add(viewInfo2);
                        ValueAnimator ofObject = ObjectAnimator.ofObject(viewInfo2.viewSnapshot, "bounds", SemAnimatorUtils.BOUNDS_EVALUATOR, new Object[]{rect, rect});
                        arrayList.add(ofObject);
                        if (obj == null) {
                            ofObject.addUpdateListener(SemAddDeleteGridAnimator.this.mBitmapUpdateListener);
                            obj = 1;
                        }
                    }
                }
                SemAddDeleteGridAnimator.this.mOldViewCache.clear();
                Animator animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.setInterpolator(SemAddDeleteGridAnimator.INSERT_INTERPOLATOR);
                animatorSet.addListener(new C09401());
                animatorSet.setDuration((long) SemAddDeleteGridAnimator.TRANSLATION_DURATION);
                animatorSet.start();
            }
        };
    }

    public void deleteFromAdapterCompleted() {
        if (this.deletePending) {
            this.deletePending = false;
            this.mGridView.getViewTreeObserver().addOnPreDrawListener(new C09361());
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
            this.mGridView.getViewTreeObserver().addOnPreDrawListener(new C09393());
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
