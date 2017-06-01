package com.samsung.android.app.ledcover.app;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.samsung.android.app.ledcover.common.SLog;

public class CustomViewPager extends ViewPager {
    public static final String TAG = "[LED_COVER]CustomViewPager";

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        SLog.m12v(TAG, "CustomViewPager");
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, 0));
            int h = view.getMeasuredHeight();
            if (height < h) {
                height = h;
            }
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, 1073741824));
    }
}
