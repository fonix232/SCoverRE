package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextInterruptedGyroAttribute extends SemContextAttribute {
    public static final Creator<SemContextInterruptedGyroAttribute> CREATOR = new C01681();
    public static final int INTERRUPTED_GYRO_DISABLE_SYSFS_NODE = 0;
    public static final int INTERRUPTED_GYRO_ENABLE_SYSFS_NODE = 1;
    private static final String TAG = "SemContextInterruptedGyroAttribute";
    private int mEnabled = 0;

    static class C01681 implements Creator<SemContextInterruptedGyroAttribute> {
        C01681() {
        }

        public SemContextInterruptedGyroAttribute createFromParcel(Parcel parcel) {
            return new SemContextInterruptedGyroAttribute(parcel);
        }

        public SemContextInterruptedGyroAttribute[] newArray(int i) {
            return new SemContextInterruptedGyroAttribute[i];
        }
    }

    SemContextInterruptedGyroAttribute() {
        setAttribute();
    }

    public SemContextInterruptedGyroAttribute(int i) {
        this.mEnabled = i;
        setAttribute();
    }

    SemContextInterruptedGyroAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("interrupt_gyro", this.mEnabled);
        super.setAttribute(48, bundle);
    }

    public boolean checkAttribute() {
        if (this.mEnabled >= 0 && this.mEnabled <= 1) {
            return true;
        }
        Log.e(TAG, "The interrupt gyro value is wrong.");
        return false;
    }
}
