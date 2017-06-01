package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextAirMotion extends SemContextEventContext {
    public static final Creator<SemContextAirMotion> CREATOR = new C01391();
    public static final int DOWN = 3;
    public static final int LEFT = 2;
    public static final int RIGHT = 1;
    public static final int UNKNOWN = 0;
    public static final int UP = 4;
    private Bundle mContext;

    static class C01391 implements Creator<SemContextAirMotion> {
        C01391() {
        }

        public SemContextAirMotion createFromParcel(Parcel parcel) {
            return new SemContextAirMotion(parcel);
        }

        public SemContextAirMotion[] newArray(int i) {
            return new SemContextAirMotion[i];
        }
    }

    SemContextAirMotion() {
        this.mContext = new Bundle();
    }

    SemContextAirMotion(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAngle() {
        return this.mContext.getInt("Angle");
    }

    public int getDirection() {
        return this.mContext.getInt("Direction");
    }

    public int getSpeed() {
        return this.mContext.getInt("Speed");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
