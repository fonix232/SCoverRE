package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextDevicePosition extends SemContextEventContext {
    public static final Creator<SemContextDevicePosition> CREATOR = new C01541();
    public static final int MOVING = 3;
    public static final int SCREEN_DOWN = 2;
    public static final int SCREEN_PERFECT_DOWN = 6;
    public static final int SCREEN_UP = 1;
    public static final int SCREEN_VERTICALITY = 4;
    public static final int SCREEN_VERTICALITY_REVERSE = 5;
    public static final int UNKNOWN = 0;
    private Bundle mContext;

    static class C01541 implements Creator<SemContextDevicePosition> {
        C01541() {
        }

        public SemContextDevicePosition createFromParcel(Parcel parcel) {
            return new SemContextDevicePosition(parcel);
        }

        public SemContextDevicePosition[] newArray(int i) {
            return new SemContextDevicePosition[i];
        }
    }

    SemContextDevicePosition() {
        this.mContext = new Bundle();
    }

    SemContextDevicePosition(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getPosition() {
        return this.mContext.getInt("Action");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
