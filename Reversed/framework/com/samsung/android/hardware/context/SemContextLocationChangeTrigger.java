package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextLocationChangeTrigger extends SemContextEventContext {
    public static final Creator<SemContextLocationChangeTrigger> CREATOR = new C01691();
    public static final int RESULT_FALSE = 2;
    public static final int RESULT_SENSOR_OUT = 3;
    public static final int RESULT_TRUE = 1;
    public static final int TYPE_MOVING = 2;
    public static final int TYPE_MOVING_AUTO = 3;
    public static final int TYPE_STATIONARY = 1;
    private Bundle mContext;

    static class C01691 implements Creator<SemContextLocationChangeTrigger> {
        C01691() {
        }

        public SemContextLocationChangeTrigger createFromParcel(Parcel parcel) {
            return new SemContextLocationChangeTrigger(parcel);
        }

        public SemContextLocationChangeTrigger[] newArray(int i) {
            return new SemContextLocationChangeTrigger[i];
        }
    }

    SemContextLocationChangeTrigger() {
        this.mContext = new Bundle();
    }

    SemContextLocationChangeTrigger(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getProperty() {
        return this.mContext.getInt("property");
    }

    public int getResult() {
        return this.mContext.getInt("result");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
