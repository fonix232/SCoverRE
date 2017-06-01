package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextStepCountAlert extends SemContextEventContext {
    public static final Creator<SemContextStepCountAlert> CREATOR = new C01891();
    public static final int EXPIRED = 1;
    public static final int UNKNOWN = 0;
    private Bundle mContext;

    static class C01891 implements Creator<SemContextStepCountAlert> {
        C01891() {
        }

        public SemContextStepCountAlert createFromParcel(Parcel parcel) {
            return new SemContextStepCountAlert(parcel);
        }

        public SemContextStepCountAlert[] newArray(int i) {
            return new SemContextStepCountAlert[i];
        }
    }

    SemContextStepCountAlert() {
        this.mContext = new Bundle();
    }

    SemContextStepCountAlert(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAlert() {
        return this.mContext.getInt("Action") == 1 ? 1 : 0;
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
