package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils;

import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class PedoHistory implements ITimeChangeObserver {
    public static final int DATA_MODE_BATCH = 2;
    public static final int DATA_MODE_NORMAL = 1;
    private static final int LOG_BUFFER_SIZE = 1440;
    private static volatile PedoHistory mPedoHistory;
    private double mAccumulatedCalorie;
    private double mAccumulatedDistance;
    private long mAccumulatedRunDnStep;
    private long mAccumulatedRunStep;
    private long mAccumulatedRunUpStep;
    private long mAccumulatedTotalStep;
    private long mAccumulatedWalkDnStep;
    private long mAccumulatedWalkStep;
    private long mAccumulatedWalkUpStep;
    private double mAverageSpeed;
    private final double[] mCalorieArray = new double[LOG_BUFFER_SIZE];
    private final double[] mDistanceArray = new double[LOG_BUFFER_SIZE];
    private int mHistoryArrayIndex;
    private int mHistoryArraySize;
    private int mLastDataMode;
    private long mLastSavingTimestamp;
    private double mNumAccumulatedData;
    private final long[] mRunDnStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mRunStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mRunUpStepCountArray = new long[LOG_BUFFER_SIZE];
    private final double[] mSpeedArray = new double[LOG_BUFFER_SIZE];
    private final long[] mTimeStampArray = new long[LOG_BUFFER_SIZE];
    private final long[] mTotalStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mWalkDnStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mWalkStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mWalkUpStepCountArray = new long[LOG_BUFFER_SIZE];

    private PedoHistory() {
        initialize();
        CaTimeChangeManager.getInstance().registerObserver(this);
    }

    public static PedoHistory getInstance() {
        if (mPedoHistory == null) {
            synchronized (PedoHistory.class) {
                if (mPedoHistory == null) {
                    mPedoHistory = new PedoHistory();
                }
            }
        }
        return mPedoHistory;
    }

    private long getLastSavingTimestamp() {
        return this.mLastSavingTimestamp;
    }

    private int getLatestBufferIndex() {
        return this.mHistoryArrayIndex;
    }

    private void increaseBufferIndex() {
        this.mHistoryArrayIndex++;
        if (this.mHistoryArrayIndex == LOG_BUFFER_SIZE) {
            this.mHistoryArrayIndex = 0;
        }
        if (this.mHistoryArraySize < LOG_BUFFER_SIZE) {
            this.mHistoryArraySize++;
        }
    }

    private void initialize() {
        this.mHistoryArrayIndex = -1;
        this.mHistoryArraySize = 0;
        resetAccumulatedStepInfo();
        this.mLastSavingTimestamp = 0;
        this.mLastDataMode = 1;
    }

    private void resetAccumulatedStepInfo() {
        this.mAccumulatedTotalStep = 0;
        this.mAccumulatedWalkStep = 0;
        this.mAccumulatedRunStep = 0;
        this.mAccumulatedWalkUpStep = 0;
        this.mAccumulatedRunUpStep = 0;
        this.mAccumulatedWalkDnStep = 0;
        this.mAccumulatedRunDnStep = 0;
        this.mAccumulatedDistance = 0.0d;
        this.mAccumulatedCalorie = 0.0d;
        this.mAverageSpeed = 0.0d;
        this.mNumAccumulatedData = 0.0d;
    }

    public void erase() {
        initialize();
        CaLogger.warning("erased");
    }

    public int getBufferSize() {
        return this.mHistoryArraySize;
    }

    public double getCalorieArraySingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mCalorieArray[i2];
    }

    public double getDistanceArraySingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mDistanceArray[i2];
    }

    public int getLastCallBackMode() {
        return this.mLastDataMode;
    }

    public int getMaxBufferSize() {
        return LOG_BUFFER_SIZE;
    }

    public long getRunDnStepCountSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mRunDnStepCountArray[i2];
    }

    public long getRunStepCountSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mRunStepCountArray[i2];
    }

    public long getRunUpStepCountSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mRunUpStepCountArray[i2];
    }

    public double getSpeedArraySingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mSpeedArray[i2];
    }

    public long getTimeStampSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mTimeStampArray[i2];
    }

    public long getTotalStepCountSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mTotalStepCountArray[i2];
    }

    public long getWalkDnStepCountSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mWalkDnStepCountArray[i2];
    }

    public long getWalkStepCountSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mWalkStepCountArray[i2];
    }

    public long getWalkUpStepCountSingle(int i) {
        int i2;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            i2 = (this.mHistoryArrayIndex + 1) + i;
            if (i2 >= LOG_BUFFER_SIZE) {
                i2 -= 1440;
            }
        } else {
            i2 = i;
        }
        return this.mWalkUpStepCountArray[i2];
    }

    public void onTimeChanged() {
        erase();
    }

    public void putCalorieInfo(double d) {
        this.mCalorieArray[this.mHistoryArrayIndex] = d;
    }

    public void putCalorieInfoSingle(double d) {
        this.mAccumulatedCalorie += d;
        this.mCalorieArray[this.mHistoryArrayIndex] = this.mAccumulatedCalorie;
    }

    public void putDistanceInfo(double d) {
        this.mDistanceArray[this.mHistoryArrayIndex] = d;
    }

    public void putDistanceInfoSingle(double d) {
        this.mAccumulatedDistance += d;
        this.mDistanceArray[this.mHistoryArrayIndex] = this.mAccumulatedDistance;
    }

    public void putRunDnStepInfo(long j) {
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = j;
    }

    public void putRunDnStepInfoSingle(long j) {
        this.mAccumulatedRunDnStep += j;
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunDnStep;
    }

    public void putRunStepInfo(long j) {
        this.mRunStepCountArray[this.mHistoryArrayIndex] = j;
    }

    public void putRunStepInfoSingle(long j) {
        this.mAccumulatedRunStep += j;
        this.mRunStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunStep;
    }

    public void putRunUpStepInfo(long j) {
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = j;
    }

    public void putRunUpStepInfoSingle(long j) {
        this.mAccumulatedRunUpStep += j;
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunUpStep;
    }

    public void putSpeedInfo(double d) {
        this.mSpeedArray[this.mHistoryArrayIndex] = d;
    }

    public void putSpeedInfoSingle(double d) {
        this.mAverageSpeed = ((this.mAverageSpeed * this.mNumAccumulatedData) + d) / (this.mNumAccumulatedData + 1.0d);
        this.mNumAccumulatedData += 1.0d;
        this.mSpeedArray[this.mHistoryArrayIndex] = this.mAverageSpeed;
    }

    public void putStepInfo(long j, long j2, long j3, long j4, long j5, long j6, long j7, double d, double d2, double d3) {
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = j;
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = j2;
        this.mRunStepCountArray[this.mHistoryArrayIndex] = j3;
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = j4;
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = j5;
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = j6;
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = j7;
        this.mDistanceArray[this.mHistoryArrayIndex] = d;
        this.mCalorieArray[this.mHistoryArrayIndex] = d2;
        this.mSpeedArray[this.mHistoryArrayIndex] = d3;
        this.mLastDataMode = 2;
    }

    public void putStepInfoSingle(long j, long j2, long j3, long j4, long j5, long j6, long j7, double d, double d2, double d3) {
        this.mAccumulatedTotalStep += j;
        this.mAccumulatedWalkStep += j2;
        this.mAccumulatedRunStep += j3;
        this.mAccumulatedWalkUpStep += j4;
        this.mAccumulatedRunUpStep += j5;
        this.mAccumulatedWalkDnStep += j6;
        this.mAccumulatedRunDnStep += j7;
        this.mAccumulatedDistance += d;
        this.mAccumulatedCalorie += d2;
        this.mAverageSpeed = ((this.mAverageSpeed * this.mNumAccumulatedData) + d3) / (this.mNumAccumulatedData + 1.0d);
        this.mNumAccumulatedData += 1.0d;
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedTotalStep;
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkStep;
        this.mRunStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunStep;
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkUpStep;
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunUpStep;
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkDnStep;
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunDnStep;
        this.mDistanceArray[this.mHistoryArrayIndex] = this.mAccumulatedDistance;
        this.mCalorieArray[this.mHistoryArrayIndex] = this.mAccumulatedCalorie;
        this.mSpeedArray[this.mHistoryArrayIndex] = this.mAverageSpeed;
        this.mLastDataMode = 1;
    }

    public void putTimestamp(long j) {
        this.mLastSavingTimestamp = j;
        this.mTimeStampArray[this.mHistoryArrayIndex] = j;
    }

    public void putTotalStepInfo(long j) {
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = j;
    }

    public void putTotalStepInfoSingle(long j) {
        this.mAccumulatedTotalStep += j;
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedTotalStep;
    }

    public void putWalkDnStepInfo(long j) {
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = j;
    }

    public void putWalkDnStepInfoSingle(long j) {
        this.mAccumulatedWalkDnStep += j;
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkDnStep;
    }

    public void putWalkStepInfo(long j) {
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = j;
    }

    public void putWalkStepInfoSingle(long j) {
        this.mAccumulatedWalkStep += j;
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkStep;
    }

    public void putWalkUpStepInfo(long j) {
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = j;
    }

    public void putWalkUpStepInfoSingle(long j) {
        this.mAccumulatedWalkUpStep += j;
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkUpStep;
    }

    public void setDataMode(int i) {
        this.mLastDataMode = i;
    }

    public void updateBufferIndex(int i) {
        if (i == 2) {
            increaseBufferIndex();
        } else if (i == 1) {
            Calendar instance = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
            long timeInMillis = instance.getTimeInMillis();
            int i2 = instance.get(12);
            Calendar instance2 = Calendar.getInstance();
            instance2.setTimeInMillis(getLastSavingTimestamp());
            int i3 = instance2.get(12);
            long lastSavingTimestamp = timeInMillis - getLastSavingTimestamp();
            if (getLastCallBackMode() == 2) {
                increaseBufferIndex();
                resetAccumulatedStepInfo();
                putTimestamp(timeInMillis);
                return;
            }
            if (i2 == i3 && lastSavingTimestamp <= 60000) {
                if (getLatestBufferIndex() >= 0) {
                    return;
                }
            }
            increaseBufferIndex();
            resetAccumulatedStepInfo();
            putTimestamp(timeInMillis);
        }
    }
}
