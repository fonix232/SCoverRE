package com.samsung.android.graphics.spr.animation.interpolator;

import android.animation.TimeInterpolator;
import java.util.Hashtable;

public class SprTimeInterpolatorFactory {
    private static Hashtable<Integer, SprTimeInterpolator> mTable;

    public static TimeInterpolator get(int i, int i2, int i3, int i4) {
        if (mTable == null) {
            mTable = new Hashtable();
        }
        SprTimeInterpolator sprTimeInterpolator = (SprTimeInterpolator) mTable.get(Integer.valueOf(i2 - i4));
        if (sprTimeInterpolator != null) {
            return sprTimeInterpolator;
        }
        TimeInterpolator sprTimeInterpolator2 = new SprTimeInterpolator(i2, i3, i4);
        mTable.put(Integer.valueOf(i2 - i4), sprTimeInterpolator2);
        return sprTimeInterpolator2;
    }
}
