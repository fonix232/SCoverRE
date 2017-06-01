package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextAbnormalPressure extends SemContextEventContext {
    public static final Creator<SemContextAbnormalPressure> CREATOR = new C01251();
    private Bundle mContext;

    static class C01251 implements Creator<SemContextAbnormalPressure> {
        C01251() {
        }

        public SemContextAbnormalPressure createFromParcel(Parcel parcel) {
            return new SemContextAbnormalPressure(parcel);
        }

        public SemContextAbnormalPressure[] newArray(int i) {
            return new SemContextAbnormalPressure[i];
        }
    }

    SemContextAbnormalPressure() {
        this.mContext = new Bundle();
    }

    SemContextAbnormalPressure(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public float getAccX() {
        return this.mContext.getFloat("xaxis");
    }

    public float getAccY() {
        return this.mContext.getFloat("yaxis");
    }

    public float getAccZ() {
        return this.mContext.getFloat("zaxis");
    }

    public float getPressure() {
        return this.mContext.getFloat("barometer");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
