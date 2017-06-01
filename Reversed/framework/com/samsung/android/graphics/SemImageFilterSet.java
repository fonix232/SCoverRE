package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.view.View;
import com.samsung.android.graphics.SemImageFilter.IAnimationListener;
import com.samsung.android.graphics.SemImageFilter.IImageFilterListener;
import java.util.ArrayList;

public class SemImageFilterSet extends SemImageFilter implements Cloneable {
    private ArrayList<SemImageFilter> mImageFilters = new ArrayList();

    public int addAnimation(int i, IAnimationListener iAnimationListener, long j, long j2, TimeInterpolator timeInterpolator) {
        return ((SemImageFilter) this.mImageFilters.get(i)).addAnimation(iAnimationListener, j, j2, timeInterpolator);
    }

    public int addAnimation(IAnimationListener iAnimationListener, long j, long j2, TimeInterpolator timeInterpolator) {
        throw new IllegalStateException("Use 'int addAnimation(int filterPass, IAnimationListener listener, long duration, long delay, final TimeInterpolator interpolator)' instead of 'int addAnimation(IAnimationListener listener, long duration, long delay, final TimeInterpolator interpolator)'");
    }

    public int[] addAnimationForAllPasses(IAnimationListener iAnimationListener, long j, long j2, TimeInterpolator timeInterpolator) {
        int[] iArr = new int[this.mImageFilters.size()];
        int i = -1;
        for (SemImageFilter addAnimation : this.mImageFilters) {
            i++;
            iArr[i] = addAnimation.addAnimation(iAnimationListener, j, j2, timeInterpolator);
        }
        return iArr;
    }

    public void addFilter(SemImageFilter semImageFilter) {
        if (semImageFilter != null) {
            this.mImageFilters.add(semImageFilter);
            semImageFilter.setView(this.mView);
            if (this.mView != null) {
                this.mView.syncImageFilter();
            }
        }
    }

    public void clearFilters() {
        this.mImageFilters.clear();
        if (this.mView != null) {
            this.mView.syncImageFilter();
        }
    }

    public SemImageFilter clone() throws CloneNotSupportedException {
        SemImageFilterSet semImageFilterSet = (SemImageFilterSet) super.clone();
        semImageFilterSet.mImageFilters = new ArrayList();
        for (int i = 0; i < this.mImageFilters.size(); i++) {
            semImageFilterSet.addFilter(((SemImageFilter) this.mImageFilters.get(i)).clone());
        }
        semImageFilterSet.setView(null);
        return semImageFilterSet;
    }

    public SemImageFilter getFilterAt(int i) {
        return (SemImageFilter) this.mImageFilters.get(i);
    }

    public int getFilterCount() {
        return this.mImageFilters.size();
    }

    public void onAttachedToView() {
        for (int i = 0; i < this.mImageFilters.size(); i++) {
            ((SemImageFilter) this.mImageFilters.get(i)).onAttachedToView();
        }
    }

    public void onViewSizeChanged() {
        for (int i = 0; i < this.mImageFilters.size(); i++) {
            ((SemImageFilter) this.mImageFilters.get(i)).onViewSizeChanged();
        }
    }

    public void setListener(IImageFilterListener iImageFilterListener) {
        this.mListener = iImageFilterListener;
        for (int i = 0; i < this.mImageFilters.size(); i++) {
            ((SemImageFilter) this.mImageFilters.get(i)).setListener(iImageFilterListener);
        }
    }

    public void setView(View view) {
        this.mView = view;
        for (int i = 0; i < this.mImageFilters.size(); i++) {
            ((SemImageFilter) this.mImageFilters.get(i)).setView(view);
        }
    }
}
