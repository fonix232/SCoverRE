package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextBounceShortMotion extends SemContextEventContext {
    public static final Creator<SemContextBounceShortMotion> CREATOR = new C01491();
    public static final int LEFT = 2;
    public static final int NONE = 0;
    public static final int RIGHT = 1;
    private Bundle mContext;

    static class C01491 implements Creator<SemContextBounceShortMotion> {
        C01491() {
        }

        public SemContextBounceShortMotion createFromParcel(Parcel parcel) {
            return new SemContextBounceShortMotion(parcel);
        }

        public SemContextBounceShortMotion[] newArray(int i) {
            return new SemContextBounceShortMotion[i];
        }
    }

    SemContextBounceShortMotion() {
        this.mContext = new Bundle();
    }

    SemContextBounceShortMotion(Parcel parcel) {
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
