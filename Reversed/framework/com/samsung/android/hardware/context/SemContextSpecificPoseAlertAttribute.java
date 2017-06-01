package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextSpecificPoseAlertAttribute extends SemContextAttribute {
    public static final Creator<SemContextSpecificPoseAlertAttribute> CREATOR = new C01881();
    private static final String TAG = "SemContextSpecificPoseAlertAttribute";
    private int mMaximumAngle = 90;
    private int mMinimumAngle = -90;
    private int mMovingThrs = 1;
    private int mRetentionTime = 1;

    static class C01881 implements Creator<SemContextSpecificPoseAlertAttribute> {
        C01881() {
        }

        public SemContextSpecificPoseAlertAttribute createFromParcel(Parcel parcel) {
            return new SemContextSpecificPoseAlertAttribute(parcel);
        }

        public SemContextSpecificPoseAlertAttribute[] newArray(int i) {
            return new SemContextSpecificPoseAlertAttribute[i];
        }
    }

    SemContextSpecificPoseAlertAttribute() {
        setAttribute();
    }

    public SemContextSpecificPoseAlertAttribute(int i, int i2, int i3, int i4) {
        this.mRetentionTime = i;
        this.mMinimumAngle = i2;
        this.mMaximumAngle = i3;
        this.mMovingThrs = i4;
        setAttribute();
    }

    SemContextSpecificPoseAlertAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("retention_time", this.mRetentionTime);
        bundle.putInt("minimum_angle", this.mMinimumAngle);
        bundle.putInt("maximum_angle", this.mMaximumAngle);
        bundle.putInt("moving_thrs", this.mMovingThrs);
        super.setAttribute(28, bundle);
    }

    public boolean checkAttribute() {
        if (this.mRetentionTime < 0) {
            Log.e(TAG, "The retention time is wrong.");
            return false;
        } else if (this.mMinimumAngle < -90 || this.mMinimumAngle > 90) {
            Log.e(TAG, "The minimum angle is wrong. The angle must be between -90 and 90.");
            return false;
        } else if (this.mMaximumAngle < -90 || this.mMaximumAngle > 90) {
            Log.e(TAG, "The maximum angle is wrong. The angle must be between -90 and 90.");
            return false;
        } else if (this.mMinimumAngle > this.mMaximumAngle) {
            Log.e(TAG, "The minimum angle must be less than the maximum angle.");
            return false;
        } else if (this.mMovingThrs >= 0) {
            return true;
        } else {
            Log.e(TAG, "The moving threshold is wrong.");
            return false;
        }
    }
}
