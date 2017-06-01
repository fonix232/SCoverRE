package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextShakeMotionAttribute extends SemContextAttribute {
    public static final Creator<SemContextShakeMotionAttribute> CREATOR = new C01861();
    private static final String TAG = "SemContextShakeMotionAttribute";
    private int mDuration = 800;
    private int mStrength = 2;

    static class C01861 implements Creator<SemContextShakeMotionAttribute> {
        C01861() {
        }

        public SemContextShakeMotionAttribute createFromParcel(Parcel parcel) {
            return new SemContextShakeMotionAttribute(parcel);
        }

        public SemContextShakeMotionAttribute[] newArray(int i) {
            return new SemContextShakeMotionAttribute[i];
        }
    }

    SemContextShakeMotionAttribute() {
        setAttribute();
    }

    public SemContextShakeMotionAttribute(int i, int i2) {
        this.mStrength = i;
        this.mDuration = i2;
        setAttribute();
    }

    SemContextShakeMotionAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("strength", this.mStrength);
        bundle.putInt("duration", this.mDuration);
        super.setAttribute(12, bundle);
    }

    public boolean checkAttribute() {
        if (this.mStrength < 0) {
            Log.e(TAG, "The strength is wrong.");
            return false;
        } else if (this.mDuration >= 0) {
            return true;
        } else {
            Log.e(TAG, "The duration is wrong.");
            return false;
        }
    }
}
