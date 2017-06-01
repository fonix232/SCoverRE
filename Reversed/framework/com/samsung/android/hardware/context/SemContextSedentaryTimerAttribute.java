package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextSedentaryTimerAttribute extends SemContextAttribute {
    public static final Creator<SemContextSedentaryTimerAttribute> CREATOR = new C01831();
    private static final String TAG = "SemContextSedentaryTimerAttribute";
    private int mAlertCount = 1;
    private int mDeviceType = 1;
    private int mDuration = 3600;
    private int mEndTime = 1500;
    private int mStartTime = 1500;

    static class C01831 implements Creator<SemContextSedentaryTimerAttribute> {
        C01831() {
        }

        public SemContextSedentaryTimerAttribute createFromParcel(Parcel parcel) {
            return new SemContextSedentaryTimerAttribute(parcel);
        }

        public SemContextSedentaryTimerAttribute[] newArray(int i) {
            return new SemContextSedentaryTimerAttribute[i];
        }
    }

    SemContextSedentaryTimerAttribute() {
        setAttribute();
    }

    public SemContextSedentaryTimerAttribute(int i, int i2, int i3, int i4, int i5) {
        this.mDeviceType = i;
        this.mDuration = i2;
        this.mAlertCount = i3;
        this.mStartTime = i4;
        this.mEndTime = i5;
        setAttribute();
    }

    SemContextSedentaryTimerAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("device_type", this.mDeviceType);
        bundle.putInt("duration", this.mDuration);
        bundle.putInt("alert_count", this.mAlertCount);
        bundle.putInt("start_time", this.mStartTime);
        bundle.putInt("end_time", this.mEndTime);
        super.setAttribute(35, bundle);
    }

    public boolean checkAttribute() {
        if (this.mDeviceType != 1 && this.mDeviceType != 2) {
            Log.e(TAG, "The device type is wrong.");
            return false;
        } else if (this.mDuration < 0) {
            Log.e(TAG, "The duration is wrong.");
            return false;
        } else if (this.mAlertCount < 0) {
            Log.e(TAG, "The alert count is wrong.");
            return false;
        } else if (this.mStartTime < 0) {
            Log.e(TAG, "The start time is wrong.");
            return false;
        } else if (this.mEndTime >= 0) {
            return true;
        } else {
            Log.e(TAG, "The end time is wrong.");
            return false;
        }
    }
}
