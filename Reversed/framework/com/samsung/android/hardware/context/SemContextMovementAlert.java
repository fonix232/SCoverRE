package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SemContextMovementAlert extends SemContextEventContext {
    public static final Creator<SemContextMovementAlert> CREATOR = new C01751();
    @Deprecated
    public static final int MOVE = 1;
    @Deprecated
    public static final int NO_MOVE = 2;
    @Deprecated
    public static final int UNKNOWN = 0;
    private Bundle mContext;

    static class C01751 implements Creator<SemContextMovementAlert> {
        C01751() {
        }

        public SemContextMovementAlert createFromParcel(Parcel parcel) {
            return new SemContextMovementAlert(parcel);
        }

        public SemContextMovementAlert[] newArray(int i) {
            return new SemContextMovementAlert[i];
        }
    }

    SemContextMovementAlert() {
        this.mContext = new Bundle();
    }

    SemContextMovementAlert(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAction() {
        return this.mContext.getInt("Action");
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
