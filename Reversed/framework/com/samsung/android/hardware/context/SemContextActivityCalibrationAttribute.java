package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextActivityCalibrationAttribute extends SemContextAttribute {
    public static final Creator<SemContextActivityCalibrationAttribute> CREATOR = new C01291();
    private static final String TAG = "SemContextActivityCalibrationAttribute";
    private int mData = 0;
    private float mSpeed = 0.0f;
    private int mStatus = 0;

    static class C01291 implements Creator<SemContextActivityCalibrationAttribute> {
        C01291() {
        }

        public SemContextActivityCalibrationAttribute createFromParcel(Parcel parcel) {
            return new SemContextActivityCalibrationAttribute(parcel);
        }

        public SemContextActivityCalibrationAttribute[] newArray(int i) {
            return new SemContextActivityCalibrationAttribute[i];
        }
    }

    SemContextActivityCalibrationAttribute() {
        setAttribute();
    }

    public SemContextActivityCalibrationAttribute(int i, int i2) {
        this.mStatus = i;
        this.mData = i2;
        setAttribute();
    }

    public SemContextActivityCalibrationAttribute(int i, int i2, float f) {
        this.mStatus = i;
        this.mData = i2;
        this.mSpeed = f;
        setAttribute();
    }

    SemContextActivityCalibrationAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        Bundle bundle = new Bundle();
        byte[] bArr = new byte[]{(byte) this.mStatus, (byte) this.mData};
        bundle.putByteArray("activity_calibration", bArr);
        bundle.putFloat("activity_speed", this.mSpeed);
        Log.d(TAG, "Activity Status Data : " + bArr[0] + bArr[1] + ", Speed : " + this.mSpeed);
        super.setAttribute(53, bundle);
    }

    public boolean checkAttribute() {
        if (this.mStatus < 0 || this.mStatus > 2) {
            Log.e(TAG, "Moving Status is wrong!!");
            return false;
        } else if (this.mData >= 0 && this.mData <= 3) {
            return true;
        } else {
            Log.e(TAG, "Data of calibration is wrong!!");
            return false;
        }
    }
}
