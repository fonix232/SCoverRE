package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.contextaware.manager.CaUserInfo;

public class SemContextPedometerAttribute extends SemContextAttribute {
    public static final Creator<SemContextPedometerAttribute> CREATOR = new C01771();
    private static final int MODE_EXERCISE = 1;
    private static final int MODE_USER_INFO = 0;
    private static final String TAG = "SemContextPedometerAttribute";
    private int mExerciseMode;
    private int mGender;
    private double mHeight;
    private int mMode;
    private double mWeight;

    static class C01771 implements Creator<SemContextPedometerAttribute> {
        C01771() {
        }

        public SemContextPedometerAttribute createFromParcel(Parcel parcel) {
            return new SemContextPedometerAttribute(parcel);
        }

        public SemContextPedometerAttribute[] newArray(int i) {
            return new SemContextPedometerAttribute[i];
        }
    }

    SemContextPedometerAttribute() {
        this.mGender = 1;
        this.mHeight = CaUserInfo.DEFAULT_HEIGHT;
        this.mWeight = CaUserInfo.DEFAULT_WEIGHT;
        this.mExerciseMode = -1;
        this.mMode = -1;
        this.mMode = 0;
        setAttribute();
    }

    public SemContextPedometerAttribute(int i) {
        this.mGender = 1;
        this.mHeight = CaUserInfo.DEFAULT_HEIGHT;
        this.mWeight = CaUserInfo.DEFAULT_WEIGHT;
        this.mExerciseMode = -1;
        this.mMode = -1;
        this.mMode = 1;
        this.mExerciseMode = i;
        setAttribute();
    }

    public SemContextPedometerAttribute(int i, double d, double d2) {
        this.mGender = 1;
        this.mHeight = CaUserInfo.DEFAULT_HEIGHT;
        this.mWeight = CaUserInfo.DEFAULT_WEIGHT;
        this.mExerciseMode = -1;
        this.mMode = -1;
        this.mMode = 0;
        this.mGender = i;
        this.mHeight = d;
        this.mWeight = d2;
        setAttribute();
    }

    SemContextPedometerAttribute(Parcel parcel) {
        super(parcel);
        this.mGender = 1;
        this.mHeight = CaUserInfo.DEFAULT_HEIGHT;
        this.mWeight = CaUserInfo.DEFAULT_WEIGHT;
        this.mExerciseMode = -1;
        this.mMode = -1;
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("mode", this.mMode);
        if (this.mMode == 0) {
            bundle.putInt("gender", this.mGender);
            bundle.putDouble("height", this.mHeight);
            bundle.putDouble("weight", this.mWeight);
        } else {
            bundle.putInt("exercise_mode", this.mExerciseMode);
        }
        super.setAttribute(2, bundle);
    }

    public boolean checkAttribute() {
        if (this.mGender < 1 || this.mGender > 2) {
            Log.e(TAG, "The gender is wrong.");
            return false;
        } else if (this.mHeight <= 0.0d) {
            Log.e(TAG, "The height is wrong.");
            return false;
        } else if (this.mWeight <= 0.0d) {
            Log.e(TAG, "The weight is wrong.");
            return false;
        } else if (this.mExerciseMode >= -1 && this.mExerciseMode <= 2) {
            return true;
        } else {
            Log.e(TAG, "The exercise mode is wrong.");
            return false;
        }
    }
}
