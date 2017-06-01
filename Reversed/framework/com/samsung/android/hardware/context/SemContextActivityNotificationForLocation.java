package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextActivityNotificationForLocation extends SemContextEventContext {
    public static final int ACCURACY_HIGH = 2;
    public static final int ACCURACY_LOW = 0;
    public static final int ACCURACY_MID = 1;
    public static final Creator<SemContextActivityNotificationForLocation> CREATOR = new C01361();
    public static final int STATUS_CYCLE = 5;
    public static final int STATUS_MOVEMENT = 30;
    public static final int STATUS_RUN = 3;
    public static final int STATUS_STATIONARY = 1;
    public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_VEHICLE = 4;
    public static final int STATUS_WALK = 2;
    private Bundle mContext;

    static class C01361 implements Creator<SemContextActivityNotificationForLocation> {
        C01361() {
        }

        public SemContextActivityNotificationForLocation createFromParcel(Parcel parcel) {
            return new SemContextActivityNotificationForLocation(parcel);
        }

        public SemContextActivityNotificationForLocation[] newArray(int i) {
            return new SemContextActivityNotificationForLocation[i];
        }
    }

    SemContextActivityNotificationForLocation() {
        this.mContext = new Bundle();
    }

    SemContextActivityNotificationForLocation(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAccuracy() {
        return this.mContext.getInt("Accuracy");
    }

    public int getStatus() {
        return this.mContext.getInt("ActivityType");
    }

    public long getTimeStamp() {
        return this.mContext.getLong("TimeStamp");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
