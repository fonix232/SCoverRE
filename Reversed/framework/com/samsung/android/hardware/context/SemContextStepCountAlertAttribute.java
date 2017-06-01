package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextStepCountAlertAttribute extends SemContextAttribute {
    public static final Creator<SemContextStepCountAlertAttribute> CREATOR = new C01901();
    public static final int INTERRUPT_GYRO_DISABLE_SYSFS_NODE = 0;
    public static final int INTERRUPT_GYRO_ENABLE_SYSFS_NODE = 1;
    private static final String TAG = "SemContextStepCountAlertAttribute";
    private int mStepCount = 10;

    static class C01901 implements Creator<SemContextStepCountAlertAttribute> {
        C01901() {
        }

        public SemContextStepCountAlertAttribute createFromParcel(Parcel parcel) {
            return new SemContextStepCountAlertAttribute(parcel);
        }

        public SemContextStepCountAlertAttribute[] newArray(int i) {
            return new SemContextStepCountAlertAttribute[i];
        }
    }

    SemContextStepCountAlertAttribute() {
        setAttribute();
    }

    public SemContextStepCountAlertAttribute(int i) {
        this.mStepCount = i;
        setAttribute();
    }

    SemContextStepCountAlertAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("step_count", this.mStepCount);
        super.setAttribute(3, bundle);
    }

    public boolean checkAttribute() {
        if (this.mStepCount >= 0) {
            return true;
        }
        Log.e(TAG, "The step count is wrong.");
        return false;
    }

    public int getStepCount() {
        return this.mStepCount;
    }
}
