package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextStepLevelMonitorAttribute extends SemContextAttribute {
    public static final Creator<SemContextStepLevelMonitorAttribute> CREATOR = new C01921();
    private static final String TAG = "SemContextStepLevelMonitorAttribute";
    private int mDuration = 300;

    static class C01921 implements Creator<SemContextStepLevelMonitorAttribute> {
        C01921() {
        }

        public SemContextStepLevelMonitorAttribute createFromParcel(Parcel parcel) {
            return new SemContextStepLevelMonitorAttribute(parcel);
        }

        public SemContextStepLevelMonitorAttribute[] newArray(int i) {
            return new SemContextStepLevelMonitorAttribute[i];
        }
    }

    SemContextStepLevelMonitorAttribute() {
        setAttribute();
    }

    public SemContextStepLevelMonitorAttribute(int i) {
        this.mDuration = i;
        setAttribute();
    }

    SemContextStepLevelMonitorAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("duration", this.mDuration);
        super.setAttribute(33, bundle);
    }

    public boolean checkAttribute() {
        if (this.mDuration >= 0) {
            return true;
        }
        Log.e(TAG, "The duration is wrong.");
        return false;
    }
}
