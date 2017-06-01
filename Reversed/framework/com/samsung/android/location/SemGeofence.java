package com.samsung.android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SemGeofence implements Parcelable {
    public static final Creator<SemGeofence> CREATOR = new C02131();
    private String mBssid;
    private double mLatitude;
    private double mLongitude;
    private int mRadius;
    private int mType;

    static class C02131 implements Creator<SemGeofence> {
        C02131() {
        }

        public SemGeofence createFromParcel(Parcel parcel) {
            return new SemGeofence(parcel);
        }

        public SemGeofence[] newArray(int i) {
            return new SemGeofence[i];
        }
    }

    public SemGeofence(int i, double d, double d2, int i2) {
        this.mType = i;
        this.mLatitude = d;
        this.mLongitude = d2;
        this.mRadius = i2;
        this.mBssid = null;
    }

    public SemGeofence(int i, String str) {
        this.mType = i;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mRadius = 0;
        this.mBssid = str;
    }

    private SemGeofence(Parcel parcel) {
        this.mType = parcel.readInt();
        this.mLatitude = parcel.readDouble();
        this.mLongitude = parcel.readDouble();
        this.mRadius = parcel.readInt();
        this.mBssid = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getBssid() {
        return this.mBssid;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public int getRadius() {
        return this.mRadius;
    }

    public int getType() {
        return this.mType;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeInt(this.mRadius);
        parcel.writeString(this.mBssid);
    }
}
