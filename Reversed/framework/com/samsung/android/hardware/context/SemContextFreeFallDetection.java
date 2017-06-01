package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextFreeFallDetection extends SemContextEventContext {
    public static final Creator<SemContextFreeFallDetection> CREATOR = new C01641();
    public static final int END = 2;
    public static final int START = 1;
    public static final int UNKNOWN = 0;
    private Bundle mContext;

    static class C01641 implements Creator<SemContextFreeFallDetection> {
        C01641() {
        }

        public SemContextFreeFallDetection createFromParcel(Parcel parcel) {
            return new SemContextFreeFallDetection(parcel);
        }

        public SemContextFreeFallDetection[] newArray(int i) {
            return new SemContextFreeFallDetection[i];
        }
    }

    SemContextFreeFallDetection() {
        this.mContext = new Bundle();
    }

    SemContextFreeFallDetection(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public long getHeight() {
        return this.mContext.getLong("height");
    }

    public int getStatus() {
        return this.mContext.getInt("status");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
