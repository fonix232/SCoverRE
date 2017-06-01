package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextCallPose extends SemContextEventContext {
    public static final Creator<SemContextCallPose> CREATOR = new C01511();
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UNKNOWN = 0;
    private Bundle mContext;

    static class C01511 implements Creator<SemContextCallPose> {
        C01511() {
        }

        public SemContextCallPose createFromParcel(Parcel parcel) {
            return new SemContextCallPose(parcel);
        }

        public SemContextCallPose[] newArray(int i) {
            return new SemContextCallPose[i];
        }
    }

    SemContextCallPose() {
        this.mContext = new Bundle();
    }

    SemContextCallPose(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getPose() {
        return this.mContext.getInt("Pose");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
