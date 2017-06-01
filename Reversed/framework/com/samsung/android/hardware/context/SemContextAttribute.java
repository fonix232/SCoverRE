package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SemContextAttribute implements Parcelable {
    public static final Creator<SemContextAttribute> CREATOR = new C01431();
    private Bundle mAttribute = new Bundle();

    static class C01431 implements Creator<SemContextAttribute> {
        C01431() {
        }

        public SemContextAttribute createFromParcel(Parcel parcel) {
            return new SemContextAttribute(parcel);
        }

        public SemContextAttribute[] newArray(int i) {
            return new SemContextAttribute[i];
        }
    }

    public SemContextAttribute(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mAttribute = parcel.readBundle(getClass().getClassLoader());
    }

    public boolean checkAttribute() {
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public Bundle getAttribute(int i) {
        String num = Integer.toString(i);
        return !this.mAttribute.containsKey(num) ? null : this.mAttribute.getBundle(num);
    }

    public void setAttribute(int i, Bundle bundle) {
        this.mAttribute.putBundle(Integer.toString(i), bundle);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mAttribute);
    }
}
