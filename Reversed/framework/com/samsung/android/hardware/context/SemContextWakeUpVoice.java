package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextWakeUpVoice extends SemContextEventContext {
    public static final Creator<SemContextWakeUpVoice> CREATOR = new C01931();
    public static final int DATA_AM = 1;
    public static final int DATA_DOWNLOADED = -17;
    public static final int DATA_LM = 2;
    public static final int MODE_BABY_CRYING = 2;
    public static final int MODE_HI_GALAXY = 1;
    public static final int NONE = 0;
    public static final int RECOGNIZED = 1;
    private Bundle mContext;

    static class C01931 implements Creator<SemContextWakeUpVoice> {
        C01931() {
        }

        public SemContextWakeUpVoice createFromParcel(Parcel parcel) {
            return new SemContextWakeUpVoice(parcel);
        }

        public SemContextWakeUpVoice[] newArray(int i) {
            return new SemContextWakeUpVoice[i];
        }
    }

    SemContextWakeUpVoice() {
        this.mContext = new Bundle();
    }

    SemContextWakeUpVoice(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAction() {
        return this.mContext.getInt("Action");
    }

    public int getMode() {
        return this.mContext.getInt("Mode");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
