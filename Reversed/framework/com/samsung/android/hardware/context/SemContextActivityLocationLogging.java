package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextActivityLocationLogging extends SemContextEventContext {
    public static final Creator<SemContextActivityLocationLogging> CREATOR = new C01301();
    public static final int LPP_RESOLUTION_HIGH = 2;
    public static final int LPP_RESOLUTION_LOW = 0;
    public static final int LPP_RESOLUTION_MID = 1;
    public static final int TYPE_MOVING = 2;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_STAYING = 1;
    public static final int TYPE_TRAJECTORY = 3;
    private Bundle mContext;
    private Bundle mInfo;
    private int mType;

    static class C01301 implements Creator<SemContextActivityLocationLogging> {
        C01301() {
        }

        public SemContextActivityLocationLogging createFromParcel(Parcel parcel) {
            return new SemContextActivityLocationLogging(parcel);
        }

        public SemContextActivityLocationLogging[] newArray(int i) {
            return new SemContextActivityLocationLogging[i];
        }
    }

    SemContextActivityLocationLogging() {
        this.mContext = new Bundle();
        this.mInfo = new Bundle();
    }

    SemContextActivityLocationLogging(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
        this.mInfo = parcel.readBundle(getClass().getClassLoader());
        this.mType = parcel.readInt();
    }

    public double[] getAltitude() {
        return this.mType == 1 ? this.mInfo.getDoubleArray("StayingAreaAltitude") : this.mType == 3 ? this.mInfo.getDoubleArray("TrajectoryAltitude") : null;
    }

    public double[] getLatitude() {
        return this.mType == 1 ? this.mInfo.getDoubleArray("StayingAreaLatitude") : this.mType == 3 ? this.mInfo.getDoubleArray("TrajectoryLatitude") : null;
    }

    public int getLoggingSize() {
        return this.mType == 1 ? this.mInfo.getInt("StayingAreaCount") : this.mType == 2 ? this.mInfo.getInt("MovingCount") : this.mType == 3 ? this.mInfo.getInt("TrajectoryCount") : 0;
    }

    public double[] getLongitude() {
        return this.mType == 1 ? this.mInfo.getDoubleArray("StayingAreaLongitude") : this.mType == 3 ? this.mInfo.getDoubleArray("TrajectoryLongitude") : null;
    }

    public int[] getStayingAreaRadius() {
        return this.mInfo.getIntArray("StayingAreaRadius");
    }

    public int[] getStayingAreaStatus() {
        return this.mInfo.getIntArray("StayingAreaStatus");
    }

    public int[] getStayingTimeDuration() {
        return this.mInfo.getIntArray("StayingAreaTimeDuration");
    }

    public long[] getTimestamp() {
        if (this.mType == 1) {
            return this.mInfo.getLongArray("StayingAreaTimeStamp");
        }
        if (this.mType != 2) {
            return this.mType == 3 ? this.mInfo.getLongArray("TrajectoryTimeStamp") : null;
        } else {
            int[] intArray = this.mInfo.getIntArray("MovingTimeDuration");
            if (intArray == null) {
                return null;
            }
            long[] jArr = new long[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                if (i == 0) {
                    jArr[i] = this.mInfo.getLong("MovingTimeStamp");
                } else {
                    jArr[i] = jArr[i - 1] + ((long) intArray[i - 1]);
                }
            }
            return jArr;
        }
    }

    public int getType() {
        return this.mType;
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
        this.mInfo = bundle.getBundle("LoggingBundle");
        this.mType = this.mContext.getInt("LoggingType");
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
        parcel.writeBundle(this.mInfo);
        parcel.writeInt(this.mType);
    }
}
