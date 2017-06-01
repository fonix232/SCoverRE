package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextApproachAttribute extends SemContextAttribute {
    public static final Creator<SemContextApproachAttribute> CREATOR = new C01421();
    private static final String TAG = "SemContextApproachAttribute";
    private int mUserID = -1;

    static class C01421 implements Creator<SemContextApproachAttribute> {
        C01421() {
        }

        public SemContextApproachAttribute createFromParcel(Parcel parcel) {
            return new SemContextApproachAttribute(parcel);
        }

        public SemContextApproachAttribute[] newArray(int i) {
            return new SemContextApproachAttribute[i];
        }
    }

    SemContextApproachAttribute() {
        setAttribute();
    }

    public SemContextApproachAttribute(int i) {
        this.mUserID = i;
        setAttribute();
    }

    SemContextApproachAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("UserID", this.mUserID);
        super.setAttribute(1, bundle);
    }

    public boolean checkAttribute() {
        return true;
    }
}
