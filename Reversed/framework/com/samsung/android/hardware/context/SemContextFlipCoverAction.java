package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextFlipCoverAction extends SemContextEventContext {
    public static final int CLOSE = 1;
    public static final Creator<SemContextFlipCoverAction> CREATOR = new C01621();
    public static final int OPEN = 0;
    public static final int UNKNOWN = -1;
    private Bundle mContext;

    static class C01621 implements Creator<SemContextFlipCoverAction> {
        C01621() {
        }

        public SemContextFlipCoverAction createFromParcel(Parcel parcel) {
            return new SemContextFlipCoverAction(parcel);
        }

        public SemContextFlipCoverAction[] newArray(int i) {
            return new SemContextFlipCoverAction[i];
        }
    }

    SemContextFlipCoverAction() {
        this.mContext = new Bundle();
    }

    SemContextFlipCoverAction(Parcel parcel) {
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
