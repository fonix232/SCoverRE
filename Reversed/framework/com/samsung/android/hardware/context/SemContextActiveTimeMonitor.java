package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextActiveTimeMonitor extends SemContextEventContext {
    public static final Creator<SemContextActiveTimeMonitor> CREATOR = new C01261();
    private Bundle mContext;

    static class C01261 implements Creator<SemContextActiveTimeMonitor> {
        C01261() {
        }

        public SemContextActiveTimeMonitor createFromParcel(Parcel parcel) {
            return new SemContextActiveTimeMonitor(parcel);
        }

        public SemContextActiveTimeMonitor[] newArray(int i) {
            return new SemContextActiveTimeMonitor[i];
        }
    }

    SemContextActiveTimeMonitor() {
        this.mContext = new Bundle();
    }

    SemContextActiveTimeMonitor(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    @Deprecated
    public int getDuration() {
        return this.mContext.getInt("ActiveTimeDuration");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
