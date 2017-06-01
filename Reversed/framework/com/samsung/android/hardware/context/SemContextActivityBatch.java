package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextActivityBatch extends SemContextEventContext {
    public static final int ACCURACY_HIGH = 2;
    public static final int ACCURACY_LOW = 0;
    public static final int ACCURACY_MID = 1;
    public static final Creator<SemContextActivityBatch> CREATOR = new C01271();
    public static final int HISTORY_MODE = 1;
    public static final int NORMAL_MODE = 0;
    public static final int STATUS_BIKE = 5;
    public static final int STATUS_RUN = 3;
    public static final int STATUS_STATIONARY = 1;
    public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_VEHICLE = 4;
    public static final int STATUS_WALK = 2;
    private Bundle mContext;
    private int mMode;

    static class C01271 implements Creator<SemContextActivityBatch> {
        C01271() {
        }

        public SemContextActivityBatch createFromParcel(Parcel parcel) {
            return new SemContextActivityBatch(parcel);
        }

        public SemContextActivityBatch[] newArray(int i) {
            return new SemContextActivityBatch[i];
        }
    }

    SemContextActivityBatch() {
        this.mContext = new Bundle();
        this.mMode = 0;
    }

    SemContextActivityBatch(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
        this.mMode = parcel.readInt();
    }

    public int[] getAccuracyArray() {
        return this.mContext.getIntArray("Accuracy");
    }

    public int getMode() {
        return this.mMode;
    }

    public int getMostActivity() {
        return this.mContext.getInt("MostActivity");
    }

    public int[] getStatusArray() {
        return this.mContext.getIntArray("ActivityType");
    }

    public long[] getTimeStampArray() {
        if (this.mMode != 0) {
            return this.mMode == 1 ? this.mContext.getLongArray("TimeStampArray") : null;
        } else {
            int i = this.mContext.getInt("Count");
            long[] longArray = this.mContext.getLongArray("Duration");
            if (longArray == null) {
                return null;
            }
            long[] jArr = new long[i];
            for (int i2 = 0; i2 < i; i2++) {
                if (i2 == 0) {
                    jArr[i2] = this.mContext.getLong("TimeStamp");
                } else {
                    jArr[i2] = jArr[i2 - 1] + longArray[i2 - 1];
                }
            }
            return jArr;
        }
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
