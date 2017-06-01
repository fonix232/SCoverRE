package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextFlatMotion extends SemContextEventContext {
    public static final Creator<SemContextFlatMotion> CREATOR = new C01591();
    public static final int FALSE = 2;
    public static final int TRUE = 1;
    public static final int UNKNOWN = 0;
    private Bundle mContext;

    static class C01591 implements Creator<SemContextFlatMotion> {
        C01591() {
        }

        public SemContextFlatMotion createFromParcel(Parcel parcel) {
            return new SemContextFlatMotion(parcel);
        }

        public SemContextFlatMotion[] newArray(int i) {
            return new SemContextFlatMotion[i];
        }
    }

    SemContextFlatMotion() {
        this.mContext = new Bundle();
    }

    SemContextFlatMotion(Parcel parcel) {
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
