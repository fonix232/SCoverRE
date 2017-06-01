package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextHallSensorAttribute extends SemContextAttribute {
    public static final Creator<SemContextHallSensorAttribute> CREATOR = new C01671();
    private static final String TAG = "SemContextHallSensorAttribute";
    private int mDisplayStatus = 0;

    static class C01671 implements Creator<SemContextHallSensorAttribute> {
        C01671() {
        }

        public SemContextHallSensorAttribute createFromParcel(Parcel parcel) {
            return new SemContextHallSensorAttribute(parcel);
        }

        public SemContextHallSensorAttribute[] newArray(int i) {
            return new SemContextHallSensorAttribute[i];
        }
    }

    SemContextHallSensorAttribute() {
        setAttribute();
    }

    public SemContextHallSensorAttribute(int i) {
        this.mDisplayStatus = i;
        setAttribute();
        Log.d(TAG, "constructor + " + i);
    }

    SemContextHallSensorAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("display_status", this.mDisplayStatus);
        Log.d(TAG, "hall sensor status   + " + bundle.getInt("display_status"));
        super.setAttribute(43, bundle);
    }

    public boolean checkAttribute() {
        if (this.mDisplayStatus >= 0 && this.mDisplayStatus <= 2) {
            return true;
        }
        Log.e(TAG, "The display status is wrong.");
        return false;
    }
}
