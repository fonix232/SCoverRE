package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextActivityCalibration extends SemContextEventContext {
    public static final Creator<SemContextActivityCalibration> CREATOR = new C01281();
    public static final int DATA_CELL_ID = 2;
    public static final int DATA_GPS = 1;
    public static final int DATA_UNKNOWN = 0;
    public static final int DATA_WIFI = 3;
    public static final int STATUS_MOVING = 1;
    public static final int STATUS_STOP = 2;
    public static final int STATUS_UNKNOWN = 0;
    private Bundle mContext;

    static class C01281 implements Creator<SemContextActivityCalibration> {
        C01281() {
        }

        public SemContextActivityCalibration createFromParcel(Parcel parcel) {
            return new SemContextActivityCalibration(parcel);
        }

        public SemContextActivityCalibration[] newArray(int i) {
            return new SemContextActivityCalibration[i];
        }
    }

    SemContextActivityCalibration() {
        this.mContext = new Bundle();
    }

    SemContextActivityCalibration(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
