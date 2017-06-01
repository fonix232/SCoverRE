package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextFlatMotionForTableMode extends SemContextEventContext {
    public static final Creator<SemContextFlatMotionForTableMode> CREATOR = new C01601();
    public static final int FALSE = 2;
    public static final int NONE = 0;
    public static final int TRUE = 1;
    private Bundle mContext;

    static class C01601 implements Creator<SemContextFlatMotionForTableMode> {
        C01601() {
        }

        public SemContextFlatMotionForTableMode createFromParcel(Parcel parcel) {
            return new SemContextFlatMotionForTableMode(parcel);
        }

        public SemContextFlatMotionForTableMode[] newArray(int i) {
            return new SemContextFlatMotionForTableMode[i];
        }
    }

    SemContextFlatMotionForTableMode() {
        this.mContext = new Bundle();
    }

    SemContextFlatMotionForTableMode(Parcel parcel) {
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
