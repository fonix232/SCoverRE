package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextAnyMotionDetector extends SemContextEventContext {
    public static final Creator<SemContextAnyMotionDetector> CREATOR = new C01401();
    public static final int STATUS_ACTION = 1;
    public static final int STATUS_NONE = 0;
    private Bundle mContext;

    static class C01401 implements Creator<SemContextAnyMotionDetector> {
        C01401() {
        }

        public SemContextAnyMotionDetector createFromParcel(Parcel parcel) {
            return new SemContextAnyMotionDetector(parcel);
        }

        public SemContextAnyMotionDetector[] newArray(int i) {
            return new SemContextAnyMotionDetector[i];
        }
    }

    SemContextAnyMotionDetector() {
        this.mContext = new Bundle();
    }

    SemContextAnyMotionDetector(Parcel parcel) {
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
