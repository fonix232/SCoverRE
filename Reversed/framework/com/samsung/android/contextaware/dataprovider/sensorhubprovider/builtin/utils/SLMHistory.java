package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils;

import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;

public class SLMHistory implements ITimeChangeObserver {
    private static final int MAX_BUFFER_SIZE = 15;
    private static volatile SLMHistory mSLMHistory;
    private double mAccumulatedCalorie;
    private double mAccumulatedDistance;
    private int mAccumulatedDuration;
    private int mAccumulatedStepCount;
    private int mBufferIndex;
    private int mBufferSize;
    private double[] mCalorieArray = new double[15];
    private double[] mDistanceArray = new double[15];
    private int[] mDurationArray = new int[15];
    private int mLastStepType;
    private int[] mStepCountArray = new int[15];
    private int[] mStepTypeArray = new int[15];
    private long[] mTimeStampArray = new long[15];

    private SLMHistory() {
        initialize();
        CaTimeChangeManager.getInstance().registerObserver(this);
    }

    public static SLMHistory getInstance() {
        if (mSLMHistory == null) {
            synchronized (SLMHistory.class) {
                if (mSLMHistory == null) {
                    mSLMHistory = new SLMHistory();
                }
            }
        }
        return mSLMHistory;
    }

    private void initialize() {
        this.mBufferIndex = -1;
        this.mBufferSize = 0;
        this.mLastStepType = 0;
        resetAccumulatedStepInfo();
    }

    public void erase() {
        initialize();
    }

    public int getBufferSize() {
        return this.mBufferSize;
    }

    public int getLastStepType() {
        return this.mLastStepType;
    }

    public int getLatestBufferIndex() {
        return this.mBufferIndex;
    }

    public int getMaxBufferSize() {
        return 15;
    }

    public long getTimeStampSingle(int i) {
        int i2;
        if (this.mBufferSize >= 15) {
            i2 = (this.mBufferIndex + 1) + i;
            if (i2 >= 15) {
                i2 -= 15;
            }
        } else {
            i2 = i;
        }
        return this.mTimeStampArray[i2];
    }

    public double getmCalorieArraySingle(int i) {
        int i2;
        if (this.mBufferSize >= 15) {
            i2 = (this.mBufferIndex + 1) + i;
            if (i2 >= 15) {
                i2 -= 15;
            }
        } else {
            i2 = i;
        }
        return this.mCalorieArray[i2];
    }

    public double getmDistanceArraySingle(int i) {
        int i2;
        if (this.mBufferSize >= 15) {
            i2 = (this.mBufferIndex + 1) + i;
            if (i2 >= 15) {
                i2 -= 15;
            }
        } else {
            i2 = i;
        }
        return this.mDistanceArray[i2];
    }

    public int getmDurationArraySingle(int i) {
        int i2;
        if (this.mBufferSize >= 15) {
            i2 = (this.mBufferIndex + 1) + i;
            if (i2 >= 15) {
                i2 -= 15;
            }
        } else {
            i2 = i;
        }
        return this.mDurationArray[i2];
    }

    public int getmStepCountArraySingle(int i) {
        int i2;
        if (this.mBufferSize >= 15) {
            i2 = (this.mBufferIndex + 1) + i;
            if (i2 >= 15) {
                i2 -= 15;
            }
        } else {
            i2 = i;
        }
        return this.mStepCountArray[i2];
    }

    public int getmStepTypeArraySingle(int i) {
        int i2;
        if (this.mBufferSize >= 15) {
            i2 = (this.mBufferIndex + 1) + i;
            if (i2 >= 15) {
                i2 -= 15;
            }
        } else {
            i2 = i;
        }
        return this.mStepTypeArray[i2];
    }

    public void onTimeChanged() {
        erase();
    }

    public void putSLMData(long j, int i, int i2, double d, double d2, int i3) {
        if (this.mLastStepType != i) {
            this.mBufferIndex = (this.mBufferIndex + 1) % 15;
            if (this.mBufferSize < 15) {
                this.mBufferSize++;
            }
            resetAccumulatedStepInfo();
            this.mTimeStampArray[this.mBufferIndex] = j;
        }
        int[] iArr = this.mStepTypeArray;
        int i4 = this.mBufferIndex;
        this.mLastStepType = i;
        iArr[i4] = i;
        this.mAccumulatedStepCount += i2;
        this.mStepCountArray[this.mBufferIndex] = this.mAccumulatedStepCount;
        this.mAccumulatedDistance += d;
        this.mDistanceArray[this.mBufferIndex] = this.mAccumulatedDistance;
        this.mAccumulatedCalorie += d2;
        this.mCalorieArray[this.mBufferIndex] = this.mAccumulatedCalorie;
        this.mAccumulatedDuration += i3;
        this.mDurationArray[this.mBufferIndex] = this.mAccumulatedDuration;
    }

    public void resetAccumulatedStepInfo() {
        this.mAccumulatedStepCount = 0;
        this.mAccumulatedDistance = 0.0d;
        this.mAccumulatedCalorie = 0.0d;
        this.mAccumulatedDuration = 0;
    }
}
