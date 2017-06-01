package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextEnvironmentAdaptiveDisplay extends SemContextEventContext {
    public static final Creator<SemContextEnvironmentAdaptiveDisplay> CREATOR = new C01551();
    private Bundle mContext;

    static class C01551 implements Creator<SemContextEnvironmentAdaptiveDisplay> {
        C01551() {
        }

        public SemContextEnvironmentAdaptiveDisplay createFromParcel(Parcel parcel) {
            return new SemContextEnvironmentAdaptiveDisplay(parcel);
        }

        public SemContextEnvironmentAdaptiveDisplay[] newArray(int i) {
            return new SemContextEnvironmentAdaptiveDisplay[i];
        }
    }

    SemContextEnvironmentAdaptiveDisplay() {
        this.mContext = new Bundle();
    }

    SemContextEnvironmentAdaptiveDisplay(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public float getBlue() {
        return this.mContext.getFloat("B");
    }

    public int getCCT() {
        return this.mContext.getInt("CCT");
    }

    public float getGreen() {
        return this.mContext.getFloat("G");
    }

    public long getLux() {
        return this.mContext.getLong("Lux");
    }

    public float getRed() {
        return this.mContext.getFloat("R");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
