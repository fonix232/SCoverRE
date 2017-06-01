package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextStepLevelMonitor extends SemContextEventContext {
    public static final Creator<SemContextStepLevelMonitor> CREATOR = new C01911();
    public static final int HISTORY_MODE = 1;
    public static final int NORMAL_MODE = 0;
    public static final int STEP_LEVEL_NORMAL = 3;
    public static final int STEP_LEVEL_POWER = 4;
    public static final int STEP_LEVEL_SEDENTARY = 2;
    public static final int STEP_LEVEL_STATIONARY = 1;
    private Bundle mContext;
    private Bundle mInfo;
    private int mMode;

    static class C01911 implements Creator<SemContextStepLevelMonitor> {
        C01911() {
        }

        public SemContextStepLevelMonitor createFromParcel(Parcel parcel) {
            return new SemContextStepLevelMonitor(parcel);
        }

        public SemContextStepLevelMonitor[] newArray(int i) {
            return new SemContextStepLevelMonitor[i];
        }
    }

    SemContextStepLevelMonitor() {
        this.mContext = new Bundle();
        this.mInfo = new Bundle();
        this.mMode = 0;
    }

    SemContextStepLevelMonitor(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
        this.mInfo = parcel.readBundle(getClass().getClassLoader());
        this.mMode = parcel.readInt();
    }

    public double[] getCalorieArray() {
        return this.mInfo.getDoubleArray("CalorieArray");
    }

    public int getCount() {
        return this.mContext.getInt("DataCount");
    }

    public double[] getDistanceArray() {
        return this.mInfo.getDoubleArray("DistanceArray");
    }

    public int[] getDurationArray() {
        return this.mInfo.getIntArray("DurationArray");
    }

    public int getMode() {
        return this.mInfo.getInt("Mode");
    }

    public int[] getStepCountArray() {
        return this.mInfo.getIntArray("StepCountArray");
    }

    public int[] getStepLevelArray() {
        return this.mInfo.getIntArray("StepTypeArray");
    }

    public long[] getTimeStampArray() {
        if (this.mMode != 0) {
            return this.mMode == 1 ? this.mContext.getLongArray("TimeStampArray") : null;
        } else {
            int i = this.mContext.getInt("DataCount");
            int[] intArray = this.mInfo.getIntArray("DurationArray");
            if (intArray == null) {
                return null;
            }
            long[] jArr = new long[i];
            for (int i2 = 0; i2 < i; i2++) {
                if (i2 == 0) {
                    jArr[i2] = this.mContext.getLong("TimeStamp");
                } else {
                    jArr[i2] = jArr[i2 - 1] + ((long) intArray[i2 - 1]);
                }
            }
            return jArr;
        }
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
        this.mInfo = bundle.getBundle("DataBundle");
        this.mMode = bundle.getInt("Mode");
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
        parcel.writeBundle(this.mInfo);
        parcel.writeInt(this.mMode);
    }
}
