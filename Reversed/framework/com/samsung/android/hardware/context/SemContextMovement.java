package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextMovement extends SemContextEventContext {
    public static final int ACTION = 1;
    public static final Creator<SemContextMovement> CREATOR = new C01741();
    public static final int NONE = 0;
    private Bundle mContext;

    static class C01741 implements Creator<SemContextMovement> {
        C01741() {
        }

        public SemContextMovement createFromParcel(Parcel parcel) {
            return new SemContextMovement(parcel);
        }

        public SemContextMovement[] newArray(int i) {
            return new SemContextMovement[i];
        }
    }

    SemContextMovement() {
        this.mContext = new Bundle();
    }

    SemContextMovement(Parcel parcel) {
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
