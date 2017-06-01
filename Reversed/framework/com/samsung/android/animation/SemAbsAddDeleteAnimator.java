package com.samsung.android.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.util.ArrayList;
import java.util.Iterator;

abstract class SemAbsAddDeleteAnimator {
    static Interpolator DELETE_INTERPOLATOR = new PathInterpolator(0.33f, 0.0f, MultiWindowManagerBridge.FREEFORM_DEFAULT_SHORT_SIZE_RATIO, 1.0f);
    static Interpolator INSERT_INTERPOLATOR = new PathInterpolator(0.33f, 0.0f, MultiWindowManagerBridge.FREEFORM_DEFAULT_SHORT_SIZE_RATIO, 1.0f);
    static float START_SCALE_FACTOR = 0.95f;
    static int TRANSLATION_DURATION = 300;
    Rect mBitmapUpdateBounds = new Rect();
    AnimatorUpdateListener mBitmapUpdateListener = new C09331();
    Runnable mDeleteRunnable;
    ArrayList<ViewInfo> mGhostViewSnapshots = new ArrayList();
    View mHostView;
    Runnable mInsertDeleteRunnable;
    Runnable mInsertRunnable;

    class C09331 implements AnimatorUpdateListener {
        C09331() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int size = SemAbsAddDeleteAnimator.this.mGhostViewSnapshots.size();
            if (size != 0) {
                SemAbsAddDeleteAnimator.this.mBitmapUpdateBounds.setEmpty();
                for (int i = 0; i < size; i++) {
                    SemAbsAddDeleteAnimator.this.mBitmapUpdateBounds.union(((ViewInfo) SemAbsAddDeleteAnimator.this.mGhostViewSnapshots.get(i)).viewSnapshot.getBounds());
                }
                SemAbsAddDeleteAnimator.this.mHostView.invalidate(SemAbsAddDeleteAnimator.this.mBitmapUpdateBounds);
            }
        }
    }

    class SetDeletePendingIsNotCalledBefore extends RuntimeException {
        public SetDeletePendingIsNotCalledBefore() {
            super("setDeletePending() should be called prior to calling deleteFromAdapterCompleted()");
        }
    }

    class SetInsertPendingIsNotCalledBefore extends RuntimeException {
        public SetInsertPendingIsNotCalledBefore() {
            super("setInsertPending() should be called prior to calling insertFromAdapterCompleted()");
        }
    }

    static class ViewInfo {
        int bottom;
        int left;
        int oldPosition;
        int right;
        int top;
        BitmapDrawable viewSnapshot;

        public ViewInfo(BitmapDrawable bitmapDrawable, int i, int i2, int i3, int i4, int i5) {
            this.viewSnapshot = bitmapDrawable;
            this.oldPosition = i;
            this.top = i3;
            this.left = i2;
            this.right = i4;
            this.bottom = i5;
        }

        public void recycleBitmap() {
            this.viewSnapshot.getBitmap().recycle();
        }
    }

    SemAbsAddDeleteAnimator() {
    }

    abstract void deleteFromAdapterCompleted();

    public void draw(Canvas canvas) {
        if (this.mGhostViewSnapshots.size() != 0) {
            for (ViewInfo viewInfo : this.mGhostViewSnapshots) {
                viewInfo.viewSnapshot.draw(canvas);
            }
        }
    }

    ObjectAnimator getInsertTranslateAlphaScaleAnim(View view, float f, float f2) {
        view.setTranslationX(f);
        view.setTranslationY(f2);
        view.setAlpha(0.0f);
        view.setScaleX(START_SCALE_FACTOR);
        view.setScaleY(START_SCALE_FACTOR);
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{getPropertyValuesHolder(View.TRANSLATION_X, 0.0f), getPropertyValuesHolder(View.TRANSLATION_Y, 0.0f), getPropertyValuesHolder(View.SCALE_X, 1.0f), getPropertyValuesHolder(View.SCALE_Y, 1.0f), getPropertyValuesHolder(View.ALPHA, 1.0f)});
    }

    int getNewPosition(int i, ArrayList<Integer> arrayList) {
        int i2 = i;
        Iterator it = arrayList.iterator();
        while (it.hasNext() && ((Integer) it.next()).intValue() < i) {
            i2--;
        }
        return i2;
    }

    int getNewPosition(int i, ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2) {
        int i2 = i;
        Iterator it = arrayList2.iterator();
        while (it.hasNext() && ((Integer) it.next()).intValue() < i) {
            i2--;
        }
        int i3 = 0;
        it = arrayList.iterator();
        while (it.hasNext() && ((Integer) it.next()).intValue() <= i + i3) {
            i2++;
            i3++;
        }
        return i2;
    }

    int getNewPositionForInsert(int i, ArrayList<Integer> arrayList) {
        int i2 = i;
        Iterator it = arrayList.iterator();
        while (it.hasNext() && ((Integer) it.next()).intValue() <= i2) {
            i2++;
        }
        return i2;
    }

    PropertyValuesHolder getPropertyValuesHolder(Property<?, Float> property, float f) {
        return PropertyValuesHolder.ofFloat(property, new float[]{f});
    }

    int getShiftCount(int i, ArrayList<Integer> arrayList) {
        int i2 = 0;
        Iterator it = arrayList.iterator();
        while (it.hasNext() && ((Integer) it.next()).intValue() < i) {
            i2++;
        }
        return i2;
    }

    int getShiftCount(int i, ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2) {
        int i2 = 0;
        Iterator it = arrayList.iterator();
        while (it.hasNext() && ((Integer) it.next()).intValue() < i) {
            i2++;
        }
        it = arrayList2.iterator();
        while (it.hasNext() && ((Integer) it.next()).intValue() < i) {
            i2--;
        }
        return i2;
    }

    ObjectAnimator getTranslateAnim(View view, float f, float f2) {
        view.setTranslationX(f);
        view.setTranslationY(f2);
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{getPropertyValuesHolder(View.TRANSLATION_X, 0.0f), getPropertyValuesHolder(View.TRANSLATION_Y, 0.0f)});
    }

    abstract void insertIntoAdapterCompleted();

    abstract void setDelete(ArrayList<Integer> arrayList);

    abstract void setDeletePending(ArrayList<Integer> arrayList);

    abstract void setInsert(ArrayList<Integer> arrayList);

    abstract void setInsertPending(ArrayList<Integer> arrayList);

    public void setTransitionDuration(int i) {
        TRANSLATION_DURATION = i;
    }
}
