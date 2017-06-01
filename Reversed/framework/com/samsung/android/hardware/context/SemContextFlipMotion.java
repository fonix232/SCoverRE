package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SemContextFlipMotion extends SemContextEventContext {
    public static final Creator<SemContextFlipMotion> CREATOR = new C01631();
    public static final int STATUS_BACK = 2;
    public static final int STATUS_FRONT = 1;
    public static final int STATUS_RESET = 4;
    public static final int STATUS_START = 3;
    public static final int STATUS_UNKNOWN = 0;
    private Bundle mContext;

    static class C01631 implements Creator<SemContextFlipMotion> {
        C01631() {
        }

        public SemContextFlipMotion createFromParcel(Parcel parcel) {
            return new SemContextFlipMotion(parcel);
        }

        public SemContextFlipMotion[] newArray(int i) {
            return new SemContextFlipMotion[i];
        }
    }

    SemContextFlipMotion() {
        this.mContext = new Bundle();
    }

    SemContextFlipMotion(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getStatus() {
        return this.mContext.getInt("Action");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
