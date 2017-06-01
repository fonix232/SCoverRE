package com.samsung.android.graphics.spr.animation.interpolator;

import android.animation.TimeInterpolator;
import java.util.TimeZone;

public class SprTimeInterpolator implements TimeInterpolator {
    static final int DAY_MILLISECONDS = 86400000;
    public static final int DAY_TYPE = 1;
    static final int WEEK_MILLISECONDS = 604800000;
    public static final int WEEK_TYPE = 2;
    private int mDuration = 0;
    private int mPeriodType = 0;
    private int mQuotient = 1;

    public SprTimeInterpolator(int i, int i2, int i3) {
        this.mDuration = i;
        this.mPeriodType = i2;
        this.mQuotient = i3;
    }

    public float getInterpolation(float f) {
        long currentTimeMillis = System.currentTimeMillis();
        long offset = currentTimeMillis + ((long) TimeZone.getDefault().getOffset(currentTimeMillis));
        long j = (this.mPeriodType == 1 ? offset % 86400000 : (offset - 259200000) % 604800000) % ((long) this.mDuration);
        if (this.mQuotient > 1) {
            j = (j / ((long) this.mQuotient)) * ((long) this.mQuotient);
        }
        return ((float) j) / ((float) this.mDuration);
    }

    public void setDuration(int i) {
        this.mDuration = i;
    }

    public void setPeriodType(int i) {
        this.mPeriodType = i;
    }

    public void setQuotient(int i) {
        this.mQuotient = i;
    }
}
