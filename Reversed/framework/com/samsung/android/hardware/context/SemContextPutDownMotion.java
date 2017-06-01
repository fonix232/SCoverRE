package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextPutDownMotion extends SemContextEventContext {
    public static final Creator<SemContextPutDownMotion> CREATOR = new C01791();
    public static final int FALSE = 2;
    public static final int NONE = 0;
    public static final int TRUE = 1;
    private Bundle mContext;

    static class C01791 implements Creator<SemContextPutDownMotion> {
        C01791() {
        }

        public SemContextPutDownMotion createFromParcel(Parcel parcel) {
            return new SemContextPutDownMotion(parcel);
        }

        public SemContextPutDownMotion[] newArray(int i) {
            return new SemContextPutDownMotion[i];
        }
    }

    SemContextPutDownMotion() {
        this.mContext = new Bundle();
    }

    SemContextPutDownMotion(Parcel parcel) {
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
