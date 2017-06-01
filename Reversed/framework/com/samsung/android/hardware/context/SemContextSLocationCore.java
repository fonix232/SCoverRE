package com.samsung.android.hardware.context;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SemContextSLocationCore extends SemContextEventContext {
    public static final int ACTION_CURRENT_LOCATION_AR_START = 11;
    public static final int ACTION_CURRENT_LOCATION_AR_STOP = 12;
    public static final int ACTION_CURRENT_LOCATION_DISTANCE_CALLBACK = 4;
    public static final int ACTION_CURRENT_LOCATION_INJECT_PASSIVE_LOCATION = 8;
    public static final int ACTION_CURRENT_LOCATION_REQUEST_DISTANCE = 13;
    public static final int ACTION_CURRENT_LOCATION_RESET_DISTANCE = 14;
    public static final int ACTION_DUMPSTATE = 6;
    public static final int ACTION_FLP_BATCHING_CORE_CALLBACK = 7;
    public static final int ACTION_FLP_BATCHING_CORE_CLEANUP = 22;
    public static final int ACTION_FLP_BATCHING_CORE_FLUSH = 20;
    public static final int ACTION_FLP_BATCHING_CORE_INJECT_LOCATION = 21;
    public static final int ACTION_FLP_BATCHING_CORE_REQUEST_LOCATION = 19;
    public static final int ACTION_FLP_BATCHING_CORE_START = 16;
    public static final int ACTION_FLP_BATCHING_CORE_STOP = 18;
    public static final int ACTION_FLP_BATCHING_CORE_UPDATE = 17;
    public static final int ACTION_GEOFENCE_CORE_ADD = 1;
    public static final int ACTION_GEOFENCE_CORE_AR_START = 9;
    public static final int ACTION_GEOFENCE_CORE_AR_STOP = 10;
    public static final int ACTION_GEOFENCE_CORE_AR_TRACKING_CALLBACK = 3;
    public static final int ACTION_GEOFENCE_CORE_ERROR_CALLBACK = 5;
    public static final int ACTION_GEOFENCE_CORE_ERROR_CODE_GENERIC = -100;
    public static final int ACTION_GEOFENCE_CORE_ERROR_CODE_SUCCESS = 0;
    public static final int ACTION_GEOFENCE_CORE_GPS_PAUSE = 3;
    public static final int ACTION_GEOFENCE_CORE_GPS_RESUME = 4;
    public static final int ACTION_GEOFENCE_CORE_NLP_PAUSE = 5;
    public static final int ACTION_GEOFENCE_CORE_NLP_RESUME = 6;
    public static final int ACTION_GEOFENCE_CORE_REMOVE = 2;
    public static final int ACTION_GEOFENCE_CORE_STATUS_ENTER = 0;
    public static final int ACTION_GEOFENCE_CORE_STATUS_EXIT = 1;
    public static final int ACTION_GEOFENCE_CORE_STATUS_REMOVE = 15;
    public static final int ACTION_GEOFENCE_CORE_TRANSITION_CALLBACK = 1;
    public static final int ACTION_GEOFENCE_CORE_UPDATE = 7;
    public static final int ACTION_GEOFENCE_CORE_UPDATE_CALLBACK = 2;
    public static final int ACTION_GEOFENCE_CORE_VERSION = 0;
    public static final int ACTION_GEOFENCE_CORE_VERSION_CALLBACK = 0;
    public static final int ACTION_UNKNOWN = -1;
    public static final Creator<SemContextSLocationCore> CREATOR = new C01801();
    public static final int MODE_CURRENT_LOCATION = 1;
    public static final int MODE_DUMPSTATE = 2;
    public static final int MODE_FLP_BATCHING_CORE = 3;
    public static final int MODE_GEOFENCE_CORE = 0;
    public static final int MODE_UNKNOWN = -1;
    private Bundle mContext;

    static class C01801 implements Creator<SemContextSLocationCore> {
        C01801() {
        }

        public SemContextSLocationCore createFromParcel(Parcel parcel) {
            return new SemContextSLocationCore(parcel);
        }

        public SemContextSLocationCore[] newArray(int i) {
            return new SemContextSLocationCore[i];
        }
    }

    SemContextSLocationCore() {
        this.mContext = new Bundle();
    }

    SemContextSLocationCore(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mContext = parcel.readBundle(getClass().getClassLoader());
    }

    public int getAccuracy() {
        return this.mContext.getInt("Accuracy");
    }

    public int[] getAccuracyArray() {
        return this.mContext.getIntArray("AccuracyArray");
    }

    public int getAction() {
        return this.mContext.getInt("Action");
    }

    public int[] getAltitudeArray() {
        return this.mContext.getIntArray("AltitudeArray");
    }

    public int[] getBearingArray() {
        return this.mContext.getIntArray("BearingArray");
    }

    public int[] getDataArray() {
        return this.mContext.getIntArray("DataArray");
    }

    public int getDataSequence() {
        return this.mContext.getInt("DataSequence");
    }

    public int getDataSize() {
        return this.mContext.getInt("DataCount");
    }

    public float getDistance() {
        return this.mContext.getFloat("Distance");
    }

    public int getErrorCode() {
        return this.mContext.getInt("ErrorCode");
    }

    public int getFenceID() {
        return this.mContext.getInt("GeoFenceId");
    }

    public int getFuncType() {
        return this.mContext.getInt("FunctionType");
    }

    public double getLatitude() {
        return this.mContext.getDouble("Latitude");
    }

    public double[] getLatitudeArray() {
        return this.mContext.getDoubleArray("LatitudeArray");
    }

    public double getLongitude() {
        return this.mContext.getDouble("Longitude");
    }

    public double[] getLongitudeArray() {
        return this.mContext.getDoubleArray("LongitudeArray");
    }

    public int getMode() {
        return this.mContext.getInt("Mode");
    }

    public int[] getSpeedArray() {
        return this.mContext.getIntArray("SpeedArray");
    }

    public int getStatus() {
        return this.mContext.getInt("GeoFenceStatus");
    }

    public int[] getStatusArray() {
        return this.mContext.getIntArray("EventStatusArray");
    }

    public int getSuccessGpsCount() {
        return this.mContext.getInt("SuccessGpsCount");
    }

    public long getTimeStamp() {
        return this.mContext.getLong("Timestamp");
    }

    public long[] getTimeStampArray() {
        return this.mContext.getLongArray("TimeStampArray");
    }

    public int getTotalGpsCount() {
        return this.mContext.getInt("TotalGpsCount");
    }

    public int getTotalSequence() {
        return this.mContext.getInt("TotalSequence");
    }

    public int[] getTypeArray() {
        return this.mContext.getIntArray("EventTypeArray");
    }

    public void setValues(Bundle bundle) {
        this.mContext = bundle;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.mContext);
    }
}
