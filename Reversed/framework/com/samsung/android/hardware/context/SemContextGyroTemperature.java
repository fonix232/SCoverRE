package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextGyroTemperature extends SemContextEventContext {
    public static final Creator<SemContextGyroTemperature> CREATOR = new C01651();
    private Bundle mContext;

    static class C01651 implements Creator<SemContextGyroTemperature> {
        C01651() {
        }

        public SemContextGyroTemperature createFromParcel(Parcel parcel) {
            return new SemContextGyroTemperature(parcel);
        }

        public SemContextGyroTemperature[] newArray(int i) {
            return new SemContextGyroTemperature[i];
        }
    }

    SemContextGyroTemperature() {
        this.mContext = new Bundle();
    }

    SemContextGyroTemperature(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public double getGyroTemperature() {
        return this.mContext.getDouble("GyroTemperature");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
