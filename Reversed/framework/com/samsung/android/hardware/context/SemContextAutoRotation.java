package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextAutoRotation extends SemContextEventContext {
    public static final int ANGLE_0 = 0;
    public static final int ANGLE_180 = 2;
    public static final int ANGLE_270 = 3;
    public static final int ANGLE_90 = 1;
    public static final Creator<SemContextAutoRotation> CREATOR = new C01461();
    public static final int DEVICE_TYPE_MOBILE = 0;
    public static final int DEVICE_TYPE_TABLET = 2;
    public static final int DEVICE_TYPE_WIDE_TABLET = 4;
    public static final int NONE = -1;
    private Bundle mContext;

    static class C01461 implements Creator<SemContextAutoRotation> {
        C01461() {
        }

        public SemContextAutoRotation createFromParcel(Parcel parcel) {
            return new SemContextAutoRotation(parcel);
        }

        public SemContextAutoRotation[] newArray(int i) {
            return new SemContextAutoRotation[i];
        }
    }

    SemContextAutoRotation() {
        this.mContext = new Bundle();
    }

    SemContextAutoRotation(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAngle() {
        return this.mContext.getInt("Angle");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
