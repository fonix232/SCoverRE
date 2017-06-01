package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextShakeMotion extends SemContextEventContext {
    public static final Creator<SemContextShakeMotion> CREATOR = new C01851();
    public static final int NONE = 0;
    public static final int START = 1;
    public static final int STOP = 2;
    private Bundle mContext;

    static class C01851 implements Creator<SemContextShakeMotion> {
        C01851() {
        }

        public SemContextShakeMotion createFromParcel(Parcel parcel) {
            return new SemContextShakeMotion(parcel);
        }

        public SemContextShakeMotion[] newArray(int i) {
            return new SemContextShakeMotion[i];
        }
    }

    SemContextShakeMotion() {
        this.mContext = new Bundle();
    }

    SemContextShakeMotion(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getShakeStatus() {
        return this.mContext.getInt("Action");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
