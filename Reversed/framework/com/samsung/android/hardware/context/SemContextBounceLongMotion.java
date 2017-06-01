package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextBounceLongMotion extends SemContextEventContext {
    public static final Creator<SemContextBounceLongMotion> CREATOR = new C01481();
    public static final int LEFT = 2;
    public static final int NONE = 0;
    public static final int RIGHT = 1;
    public static final int UNHAND = 3;
    private Bundle mContext;

    static class C01481 implements Creator<SemContextBounceLongMotion> {
        C01481() {
        }

        public SemContextBounceLongMotion createFromParcel(Parcel parcel) {
            return new SemContextBounceLongMotion(parcel);
        }

        public SemContextBounceLongMotion[] newArray(int i) {
            return new SemContextBounceLongMotion[i];
        }
    }

    SemContextBounceLongMotion() {
        this.mContext = new Bundle();
    }

    SemContextBounceLongMotion(Parcel parcel) {
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
