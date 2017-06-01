package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextCarryingDetection extends SemContextEventContext {
    public static final int AOD_OVERTURN_DURATION = 4;
    public static final int AOD_PROXIMITY_CHECK_DURATION = 6;
    public static final int AOD_PROXIMITY_USE_DURATION = 5;
    public static final int AOD_SCENARIO_CHECK_OVERTURN = 2;
    public static final int AOD_SCENARIO_CHECK_PROXIMITY_PERIODICALLY = 4;
    public static final int AOD_SCENARIO_CHECK_TIMEOUT = 1;
    public static final int AOD_SCENARIO_CHECK_USER_CYCLE = 64;
    public static final int AOD_SCENARIO_CHECK_USER_RUNNING = 16;
    public static final int AOD_SCENARIO_CHECK_USER_VEHICLE = 32;
    public static final int AOD_SCENARIO_CHECK_USER_WALKING = 8;
    public static final int AOD_STATUS_OFF = 2;
    public static final int AOD_STATUS_ON = 1;
    public static final int AOD_TIMEOUT_DURATION = 3;
    public static final Creator<SemContextCarryingDetection> CREATOR = new C01521();
    public static final int REASON_OFF_CARRYING_IN = 9;
    public static final int REASON_OFF_NO_MOVE_SCREEN_DOWN_TIME_OUT = 6;
    public static final int REASON_OFF_NO_MOVE_SCREEN_UP_TIME_OUT = 7;
    public static final int REASON_OFF_RUNNING_START = 10;
    public static final int REASON_OFF_SCREEN_DOWN_START_STATE = 8;
    public static final int REASON_ON_CARRYING_OUT = 4;
    public static final int REASON_ON_DISPLAY_INIT = 1;
    public static final int REASON_ON_MOVEMENT_WITH_SCREEN_DOWN = 2;
    public static final int REASON_ON_MOVEMENT_WITH_SCREEN_UP = 3;
    public static final int REASON_ON_RUNNING_STOPPED = 5;
    private Bundle mContext;

    static class C01521 implements Creator<SemContextCarryingDetection> {
        C01521() {
        }

        public SemContextCarryingDetection createFromParcel(Parcel parcel) {
            return new SemContextCarryingDetection(parcel);
        }

        public SemContextCarryingDetection[] newArray(int i) {
            return new SemContextCarryingDetection[i];
        }
    }

    SemContextCarryingDetection() {
        this.mContext = new Bundle();
    }

    SemContextCarryingDetection(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getCarryingReason() {
        return this.mContext.getInt("AODReason");
    }

    public int getCarryingStatus() {
        return this.mContext.getInt("AODStatus");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
