package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextAutoBrightness extends SemContextEventContext {
    public static final int CONFIG_DATA_DOWNLOADED = 1000;
    public static final Creator<SemContextAutoBrightness> CREATOR = new C01441();
    public static final int EBOOK_MODE = 1;
    public static final int NORMAL_MODE = 0;
    public static final int UPDATE_MODE = 2;
    private Bundle mContext;

    static class C01441 implements Creator<SemContextAutoBrightness> {
        C01441() {
        }

        public SemContextAutoBrightness createFromParcel(Parcel parcel) {
            return new SemContextAutoBrightness(parcel);
        }

        public SemContextAutoBrightness[] newArray(int i) {
            return new SemContextAutoBrightness[i];
        }
    }

    SemContextAutoBrightness() {
        this.mContext = new Bundle();
    }

    SemContextAutoBrightness(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAmbientLux() {
        return this.mContext.getInt("AmbientLux");
    }

    public int getCandela() {
        return this.mContext.getInt("Candela");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
