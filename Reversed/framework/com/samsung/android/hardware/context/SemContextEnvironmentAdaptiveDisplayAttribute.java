package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextEnvironmentAdaptiveDisplayAttribute extends SemContextAttribute {
    public static final Creator<SemContextEnvironmentAdaptiveDisplayAttribute> CREATOR = new C01561();
    private static final String TAG = "SemContextEnvironmentAdaptiveDisplayAttribute";
    private float mColorThreshold = 0.07f;
    private int mDuration = 35;

    static class C01561 implements Creator<SemContextEnvironmentAdaptiveDisplayAttribute> {
        C01561() {
        }

        public SemContextEnvironmentAdaptiveDisplayAttribute createFromParcel(Parcel parcel) {
            return new SemContextEnvironmentAdaptiveDisplayAttribute(parcel);
        }

        public SemContextEnvironmentAdaptiveDisplayAttribute[] newArray(int i) {
            return new SemContextEnvironmentAdaptiveDisplayAttribute[i];
        }
    }

    SemContextEnvironmentAdaptiveDisplayAttribute() {
        setAttribute();
    }

    public SemContextEnvironmentAdaptiveDisplayAttribute(float f, int i) {
        this.mColorThreshold = f;
        this.mDuration = i;
        setAttribute();
    }

    SemContextEnvironmentAdaptiveDisplayAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putFloat("color_threshold", this.mColorThreshold);
        bundle.putInt("duration", this.mDuration);
        Log.d(TAG, "setAttribute() mColorThreshold : " + bundle.getFloat("color_threshold"));
        Log.d(TAG, "setAttribute() mDuration : " + bundle.getInt("duration"));
        super.setAttribute(44, bundle);
    }

    public boolean checkAttribute() {
        if (this.mColorThreshold < 0.0f) {
            Log.e(TAG, "The color threshold value is wrong.");
            return false;
        } else if (this.mDuration >= 0 && this.mDuration <= 255) {
            return true;
        } else {
            Log.e(TAG, "The duration value is wrong.");
            return false;
        }
    }
}
