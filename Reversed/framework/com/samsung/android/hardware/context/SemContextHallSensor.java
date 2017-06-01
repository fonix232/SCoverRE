package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextHallSensor extends SemContextEventContext {
    public static final Creator<SemContextHallSensor> CREATOR = new C01661();
    public static final int STATUS_BACKFOLD = 2;
    public static final int STATUS_FOLD = 0;
    public static final int STATUS_UNFOLD = 1;
    private Bundle mContext;

    static class C01661 implements Creator<SemContextHallSensor> {
        C01661() {
        }

        public SemContextHallSensor createFromParcel(Parcel parcel) {
            return new SemContextHallSensor(parcel);
        }

        public SemContextHallSensor[] newArray(int i) {
            return new SemContextHallSensor[i];
        }
    }

    public SemContextHallSensor() {
        this.mContext = new Bundle();
    }

    public SemContextHallSensor(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public short getAngle() {
        return this.mContext.getShort("Angle");
    }

    public short getIntensity() {
        return this.mContext.getShort("Intensity");
    }

    public short getType() {
        return this.mContext.getShort("Type");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
