package com.samsung.android.widget;

import android.content.Context;
import java.util.LinkedList;

public class SemRecentColorInfo {
    private static final String TAG = "SemRecentColorInfo";
    private static Integer mCurrentColor = null;
    private static Integer mNewColor = null;
    private static LinkedList<Integer> mRecentColorInfo;
    private static int[] mRecentlyUsedColors = null;
    private static int mSelectedColor = 0;
    private Context mContext;

    public SemRecentColorInfo(Context context) {
        this.mContext = context;
        mRecentColorInfo = new LinkedList();
    }

    public Integer getCurrentColor() {
        return mCurrentColor;
    }

    public Integer getNewColor() {
        return mNewColor;
    }

    public LinkedList<Integer> getRecentColorInfo() {
        return mRecentColorInfo;
    }

    public int[] getRecentlyUsedColor() {
        return mRecentlyUsedColors;
    }

    public Integer getSelectedColor() {
        return Integer.valueOf(mSelectedColor);
    }

    public void saveRecentColorInfo(int[] iArr) {
        mRecentlyUsedColors = iArr;
        if (iArr != null) {
            for (int length = iArr.length - 1; length >= 0; length--) {
                updateRecentColorInfo(Integer.valueOf(iArr[length]));
            }
        }
    }

    public void saveSelectedColor(int i) {
        mSelectedColor = i;
    }

    public void setCurrentColor(Integer num) {
        mCurrentColor = num;
    }

    public void setNewColor(Integer num) {
        mNewColor = num;
    }

    public void updateRecentColorInfo(Integer num) {
        if (mRecentColorInfo.contains(num)) {
            mRecentColorInfo.remove(num);
            mRecentColorInfo.addFirst(num);
            return;
        }
        if (mRecentColorInfo.size() >= 6) {
            mRecentColorInfo.removeLast();
        }
        mRecentColorInfo.addFirst(num);
    }
}
