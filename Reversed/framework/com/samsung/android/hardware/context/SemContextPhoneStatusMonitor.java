package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextPhoneStatusMonitor extends SemContextEventContext {
    public static final Creator<SemContextPhoneStatusMonitor> CREATOR = new C01781();
    public static final int PROXIMITY_CLOSE = 2;
    public static final int PROXIMITY_NONE = 0;
    public static final int PROXIMITY_OPEN = 1;
    public static final int SCREEN_DOWN = 4;
    public static final int SCREEN_NONE = 0;
    public static final int SCREEN_PERFECT_DOWN = 5;
    public static final int SCREEN_PERFECT_UP = 1;
    public static final int SCREEN_TILT = 3;
    public static final int SCREEN_UP = 2;
    private Bundle mContext;

    static class C01781 implements Creator<SemContextPhoneStatusMonitor> {
        C01781() {
        }

        public SemContextPhoneStatusMonitor createFromParcel(Parcel parcel) {
            return new SemContextPhoneStatusMonitor(parcel);
        }

        public SemContextPhoneStatusMonitor[] newArray(int i) {
            return new SemContextPhoneStatusMonitor[i];
        }
    }

    SemContextPhoneStatusMonitor() {
        this.mContext = new Bundle();
    }

    SemContextPhoneStatusMonitor(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getProximity() {
        return this.mContext.getInt("embower");
    }

    public int getScreenDirection() {
        return this.mContext.getInt("lcddirect");
    }

    public long getTimeStamp() {
        return this.mContext.getLong("timestamp");
    }

    public boolean isInClosedSpace() {
        return this.mContext.getBoolean("lcdOffRecommend");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
