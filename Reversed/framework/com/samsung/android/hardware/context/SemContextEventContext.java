package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SemContextEventContext implements Parcelable {
    public static final Creator<SemContextEventContext> CREATOR = new C01581();
    protected static final long serialVersionUID = 4514449696888150558L;

    static class C01581 implements Creator<SemContextEventContext> {
        C01581() {
        }

        public SemContextEventContext createFromParcel(Parcel parcel) {
            return new SemContextEventContext(parcel);
        }

        public SemContextEventContext[] newArray(int i) {
            return new SemContextEventContext[i];
        }
    }

    public SemContextEventContext(Parcel parcel) {
    }

    public int describeContents() {
        return 0;
    }

    public void setValues(Bundle bundle) {
    }

    public void writeToParcel(Parcel parcel, int i) {
    }
}
