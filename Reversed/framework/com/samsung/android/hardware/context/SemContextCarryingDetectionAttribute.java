package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextCarryingDetectionAttribute extends SemContextAttribute {
    public static final Creator<SemContextCarryingDetectionAttribute> CREATOR = new C01531();
    private static final int DATA = 1;
    private static final int MODE = 2;
    private static final String TAG = "SemContextCarryingDetection";
    private int mData = 1;
    private int mMode = 2;

    static class C01531 implements Creator<SemContextCarryingDetectionAttribute> {
        C01531() {
        }

        public SemContextCarryingDetectionAttribute createFromParcel(Parcel parcel) {
            return new SemContextCarryingDetectionAttribute(parcel);
        }

        public SemContextCarryingDetectionAttribute[] newArray(int i) {
            return new SemContextCarryingDetectionAttribute[i];
        }
    }

    SemContextCarryingDetectionAttribute() {
        setAttribute();
    }

    public SemContextCarryingDetectionAttribute(int i, int i2) {
        this.mMode = i;
        this.mData = i2;
        setAttribute();
    }

    SemContextCarryingDetectionAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("dpcm_mode", this.mMode);
        bundle.putInt("dpcm_data", this.mData);
        super.setAttribute(51, bundle);
    }

    public boolean checkAttribute() {
        if (this.mMode < 1 || this.mMode > 6) {
            Log.d(TAG, "Mode value is wrong!!");
            return false;
        } else if (this.mData == 1 || this.mData == 2 || this.mData == 4 || this.mData == 8 || this.mData == 16 || this.mData == 32 || this.mData == 64) {
            return true;
        } else {
            Log.d(TAG, "Data value is wrong!!");
            return false;
        }
    }
}
