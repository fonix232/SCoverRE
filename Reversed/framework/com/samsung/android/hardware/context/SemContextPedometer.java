package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextPedometer extends SemContextEventContext {
    public static final Creator<SemContextPedometer> CREATOR = new C01761();
    public static final int EXERCISE_MODE_END = 2;
    public static final int EXERCISE_MODE_NONE = -1;
    public static final int EXERCISE_MODE_RUN = 1;
    public static final int EXERCISE_MODE_WALK = 0;
    public static final int GENDER_MAN = 1;
    public static final int GENDER_WOMAN = 2;
    public static final int HISTORY_MODE = 2;
    public static final int LOGGING_MODE = 1;
    public static final int NORMAL_MODE = 0;
    public static final int PARAMETERS_UNKNOWN = 0;
    public static final int STEP_STATUS_MARK = 1;
    public static final int STEP_STATUS_RUN = 4;
    public static final int STEP_STATUS_RUN_DOWN = 9;
    public static final int STEP_STATUS_RUN_UP = 8;
    public static final int STEP_STATUS_RUSH = 5;
    public static final int STEP_STATUS_STOP = 0;
    public static final int STEP_STATUS_STROLL = 2;
    public static final int STEP_STATUS_UNKNOWN = -1;
    public static final int STEP_STATUS_WALK = 3;
    public static final int STEP_STATUS_WALK_DOWN = 7;
    public static final int STEP_STATUS_WALK_UP = 6;
    private Bundle mContext;
    private int mMode;

    static class C01761 implements Creator<SemContextPedometer> {
        C01761() {
        }

        public SemContextPedometer createFromParcel(Parcel parcel) {
            return new SemContextPedometer(parcel);
        }

        public SemContextPedometer[] newArray(int i) {
            return new SemContextPedometer[i];
        }
    }

    SemContextPedometer() {
        this.mContext = new Bundle();
        this.mMode = 0;
    }

    SemContextPedometer(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
        this.mMode = parcel.readInt();
    }

    public double getCalorie() {
        return this.mContext.getDouble("CumulativeCalorie");
    }

    public double getCalorieDiff() {
        return this.mContext.getDouble("CalorieDiff");
    }

    public double[] getCalorieDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getDoubleArray("CalorieDiffArray") : null;
    }

    @Deprecated
    public double getCumulativeCalorie() {
        return this.mContext.getDouble("CumulativeCalorie");
    }

    @Deprecated
    public double getCumulativeDistance() {
        return this.mContext.getDouble("CumulativeDistance");
    }

    @Deprecated
    public long getCumulativeRunDownStepCount() {
        return this.mContext.getLong("CumulativeRunDownStepCount");
    }

    @Deprecated
    public long getCumulativeRunStepCount() {
        return this.mContext.getLong("CumulativeRunFlatStepCount");
    }

    @Deprecated
    public long getCumulativeRunUpStepCount() {
        return this.mContext.getLong("CumulativeRunUpStepCount");
    }

    @Deprecated
    public long getCumulativeTotalStepCount() {
        return this.mContext.getLong("CumulativeTotalStepCount");
    }

    @Deprecated
    public long getCumulativeWalkDownStepCount() {
        return this.mContext.getLong("CumulativeWalkDownStepCount");
    }

    @Deprecated
    public long getCumulativeWalkStepCount() {
        return this.mContext.getLong("CumulativeWalkFlatStepCount");
    }

    @Deprecated
    public long getCumulativeWalkUpStepCount() {
        return this.mContext.getLong("CumulativeWalkUpStepCount");
    }

    public double getDistance() {
        return this.mContext.getDouble("CumulativeDistance");
    }

    public double getDistanceDiff() {
        return this.mContext.getDouble("DistanceDiff");
    }

    public double[] getDistanceDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getDoubleArray("DistanceDiffArray") : null;
    }

    public int getLoggingCount() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getInt("LoggingCount") : 0;
    }

    public int getMode() {
        return this.mMode;
    }

    public long getRunDownStepCount() {
        return this.mContext.getLong("CumulativeRunDownStepCount");
    }

    public long getRunDownStepCountDiff() {
        return this.mContext.getLong("RunDownStepCountDiff");
    }

    public long[] getRunDownStepCountDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("RunDownStepCountDiffArray") : null;
    }

    public long getRunStepCount() {
        return this.mContext.getLong("CumulativeRunFlatStepCount");
    }

    public long getRunStepCountDiff() {
        return this.mContext.getLong("RunStepCountDiff");
    }

    public long[] getRunStepCountDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("RunStepCountDiffArray") : null;
    }

    public long getRunUpStepCount() {
        return this.mContext.getLong("CumulativeRunUpStepCount");
    }

    public long getRunUpStepCountDiff() {
        return this.mContext.getLong("RunUpStepCountDiff");
    }

    public long[] getRunUpStepCountDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("RunUpStepCountDiffArray") : null;
    }

    public double getSpeed() {
        return this.mContext.getDouble("Speed");
    }

    public double[] getSpeedArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getDoubleArray("SpeedArray") : null;
    }

    public int getStepStatus() {
        return this.mContext.getInt("StepStatus");
    }

    public long[] getTimeStampArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("TimeStampArray") : null;
    }

    public long getTotalStepCount() {
        return this.mContext.getLong("CumulativeTotalStepCount");
    }

    public long getTotalStepCountDiff() {
        return this.mContext.getLong("TotalStepCountDiff");
    }

    public long[] getTotalStepCountDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("TotalStepCountDiffArray") : null;
    }

    @Deprecated
    public long getUpDownStepCount() {
        return this.mContext.getLong("UpDownStepCount");
    }

    @Deprecated
    public long getUpDownStepCountDiff() {
        return this.mContext.getLong("UpDownStepCountDiff");
    }

    public long getWalkDownStepCount() {
        return this.mContext.getLong("CumulativeWalkDownStepCount");
    }

    public long getWalkDownStepCountDiff() {
        return this.mContext.getLong("WalkDownStepCountDiff");
    }

    public long[] getWalkDownStepCountDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("WalkDownStepCountDiffArray") : null;
    }

    public long getWalkStepCount() {
        return this.mContext.getLong("CumulativeWalkFlatStepCount");
    }

    public long getWalkStepCountDiff() {
        return this.mContext.getLong("WalkStepCountDiff");
    }

    public long[] getWalkStepCountDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("WalkStepCountDiffArray") : null;
    }

    public long getWalkUpStepCount() {
        return this.mContext.getLong("CumulativeWalkUpStepCount");
    }

    public long getWalkUpStepCountDiff() {
        return this.mContext.getLong("WalkUpStepCountDiff");
    }

    public long[] getWalkUpStepCountDiffArray() {
        return (this.mMode == 1 || this.mMode == 2) ? this.mContext.getLongArray("WalkUpStepCountDiffArray") : null;
    }

    public double getWalkingFrequency() {
        return this.mContext.getDouble("WalkingFrequency");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
        this.mMode = this.mContext.getInt("Mode");
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
        parcel.writeInt(this.mMode);
    }
}
