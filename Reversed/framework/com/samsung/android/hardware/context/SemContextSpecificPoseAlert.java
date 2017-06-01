package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextSpecificPoseAlert extends SemContextEventContext {
    public static final int ACTION = 1;
    public static final Creator<SemContextSpecificPoseAlert> CREATOR = new C01871();
    public static final int NONE = 0;
    private Bundle mContext;

    static class C01871 implements Creator<SemContextSpecificPoseAlert> {
        C01871() {
        }

        public SemContextSpecificPoseAlert createFromParcel(Parcel parcel) {
            return new SemContextSpecificPoseAlert(parcel);
        }

        public SemContextSpecificPoseAlert[] newArray(int i) {
            return new SemContextSpecificPoseAlert[i];
        }
    }

    SemContextSpecificPoseAlert() {
        this.mContext = new Bundle();
    }

    SemContextSpecificPoseAlert(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAction() {
        return this.mContext.getInt("Action");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
