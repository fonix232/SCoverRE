package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextSensorStatusCheck extends SemContextEventContext {
    public static final int ACC_DATA_DEFAULT = 40000;
    public static final int ACC_DATA_OFFSET = 2;
    public static final int ACC_DATA_STUCK = 1;
    public static final Creator<SemContextSensorStatusCheck> CREATOR = new C01841();
    public static final int SENSORHUB_RESET = 3;
    public static final int SENSOR_DATA_NORMAL = 0;
    private Bundle mContext;

    static class C01841 implements Creator<SemContextSensorStatusCheck> {
        C01841() {
        }

        public SemContextSensorStatusCheck createFromParcel(Parcel parcel) {
            return new SemContextSensorStatusCheck(parcel);
        }

        public SemContextSensorStatusCheck[] newArray(int i) {
            return new SemContextSensorStatusCheck[i];
        }
    }

    SemContextSensorStatusCheck() {
        this.mContext = new Bundle();
    }

    SemContextSensorStatusCheck(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getResetCount() {
        return this.mContext.getInt("ResetCnt");
    }

    public long getSensorHubLastEventTimeStamp() {
        return this.mContext.getLong("SensorHubLastEventTime");
    }

    public long[] getSensorHubResetTimeStampArray() {
        return this.mContext.getLongArray("SensorHubResetTimeStampArray");
    }

    public int getSensorHubResetTimeStampArraySize() {
        return this.mContext.getInt("SensorHubResetTimeStampArraySize");
    }

    public int getStatus() {
        return this.mContext.getInt("Status");
    }

    public int getXAxis() {
        return this.mContext.getInt("XAxis");
    }

    public int getYAxis() {
        return this.mContext.getInt("YAxis");
    }

    public int getZAxis() {
        return this.mContext.getInt("ZAxis");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
