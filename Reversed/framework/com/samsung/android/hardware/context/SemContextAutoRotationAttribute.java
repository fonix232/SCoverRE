package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextAutoRotationAttribute extends SemContextAttribute {
    public static final Creator<SemContextAutoRotationAttribute> CREATOR = new C01471();
    private static final String TAG = "SemContextAutoRotationAttribute";
    private int mDeviceType = 0;

    static class C01471 implements Creator<SemContextAutoRotationAttribute> {
        C01471() {
        }

        public SemContextAutoRotationAttribute createFromParcel(Parcel parcel) {
            return new SemContextAutoRotationAttribute(parcel);
        }

        public SemContextAutoRotationAttribute[] newArray(int i) {
            return new SemContextAutoRotationAttribute[i];
        }
    }

    SemContextAutoRotationAttribute() {
        setAttribute();
    }

    public SemContextAutoRotationAttribute(int i) {
        this.mDeviceType = i;
        setAttribute();
    }

    SemContextAutoRotationAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("device_type", this.mDeviceType);
        super.setAttribute(6, bundle);
    }

    public boolean checkAttribute() {
        if (this.mDeviceType == 0 || this.mDeviceType == 2 || this.mDeviceType == 4) {
            return true;
        }
        Log.e(TAG, "The device type is wrong.");
        return false;
    }
}
