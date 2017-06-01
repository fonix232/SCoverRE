package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SemContextWristUpMotion extends SemContextEventContext {
    public static final Creator<SemContextWristUpMotion> CREATOR = new C01961();
    @Deprecated
    public static final int NONE = 0;
    @Deprecated
    public static final int NORMAL = 1;
    private Bundle mContext;

    static class C01961 implements Creator<SemContextWristUpMotion> {
        C01961() {
        }

        public SemContextWristUpMotion createFromParcel(Parcel parcel) {
            return new SemContextWristUpMotion(parcel);
        }

        public SemContextWristUpMotion[] newArray(int i) {
            return new SemContextWristUpMotion[i];
        }
    }

    SemContextWristUpMotion() {
        this.mContext = new Bundle();
    }

    SemContextWristUpMotion(Parcel parcel) {
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
