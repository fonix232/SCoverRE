package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextSedentaryTimer extends SemContextEventContext {
    public static final Creator<SemContextSedentaryTimer> CREATOR = new C01821();
    public static final int DEVICE_TYPE_MOBILE = 1;
    public static final int DEVICE_TYPE_WEARABLE = 2;
    public static final int STATUS_SEDENTARY = 2;
    public static final int STATUS_SEDENTARY_BREAK = 3;
    public static final int STATUS_SEDENTARY_START = 1;
    private Bundle mContext;

    static class C01821 implements Creator<SemContextSedentaryTimer> {
        C01821() {
        }

        public SemContextSedentaryTimer createFromParcel(Parcel parcel) {
            return new SemContextSedentaryTimer(parcel);
        }

        public SemContextSedentaryTimer[] newArray(int i) {
            return new SemContextSedentaryTimer[i];
        }
    }

    SemContextSedentaryTimer() {
        this.mContext = new Bundle();
    }

    SemContextSedentaryTimer(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getDuration() {
        return this.mContext.getInt("InactiveTimeDuration");
    }

    public int getStatus() {
        return this.mContext.getInt("InactiveStatus");
    }

    public boolean isTimeOutExpired() {
        return this.mContext.getBoolean("IsTimeOut");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
