package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextLocationChangeTriggerAttribute extends SemContextAttribute {
    public static final Creator<SemContextLocationChangeTriggerAttribute> CREATOR = new C01701();
    private static final String TAG = "SemContextLocationChangeTriggerAttribute";
    private int mDuration = 10;
    private int mTriggerType = 1;

    static class C01701 implements Creator<SemContextLocationChangeTriggerAttribute> {
        C01701() {
        }

        public SemContextLocationChangeTriggerAttribute createFromParcel(Parcel parcel) {
            return new SemContextLocationChangeTriggerAttribute(parcel);
        }

        public SemContextLocationChangeTriggerAttribute[] newArray(int i) {
            return new SemContextLocationChangeTriggerAttribute[i];
        }
    }

    SemContextLocationChangeTriggerAttribute() {
        setAttribute();
    }

    public SemContextLocationChangeTriggerAttribute(int i, int i2) {
        this.mTriggerType = i;
        this.mDuration = i2;
        setAttribute();
    }

    SemContextLocationChangeTriggerAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("trigger_type", this.mTriggerType);
        bundle.putInt("duration", this.mDuration);
        super.setAttribute(54, bundle);
    }

    public boolean checkAttribute() {
        boolean z = true;
        if (this.mTriggerType < 1 || this.mTriggerType > 3) {
            Log.e(TAG, "The display status is wrong.");
            return false;
        }
        if (this.mDuration < 0) {
            z = false;
        }
        return z;
    }
}
