package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextApproach extends SemContextEventContext {
    public static final Creator<SemContextApproach> CREATOR = new C01411();
    public static final int FAR = 0;
    public static final int NEAR = 1;
    private Bundle mContext;

    static class C01411 implements Creator<SemContextApproach> {
        C01411() {
        }

        public SemContextApproach createFromParcel(Parcel parcel) {
            return new SemContextApproach(parcel);
        }

        public SemContextApproach[] newArray(int i) {
            return new SemContextApproach[i];
        }
    }

    SemContextApproach() {
        this.mContext = new Bundle();
    }

    SemContextApproach(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getApproach() {
        return this.mContext.getInt("Proximity");
    }

    public int getUserID() {
        return this.mContext.getInt("UserID");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
