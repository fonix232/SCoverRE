package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintEvent;

public class SemContextActivityLocationLoggingAttribute extends SemContextAttribute {
    public static final Creator<SemContextActivityLocationLoggingAttribute> CREATOR = new C01311();
    private static final String TAG = "SemContextActivityLocationLoggingAttribute";
    private int mAreaRadius = 150;
    private int mLppResolution = 0;
    private int mStayingRadius = 50;
    private int mStopPeriod = 60;
    private int mWaitPeriod = FingerprintEvent.STATUS_ENROLL_FAILURE_SERVICE_FAILURE;

    static class C01311 implements Creator<SemContextActivityLocationLoggingAttribute> {
        C01311() {
        }

        public SemContextActivityLocationLoggingAttribute createFromParcel(Parcel parcel) {
            return new SemContextActivityLocationLoggingAttribute(parcel);
        }

        public SemContextActivityLocationLoggingAttribute[] newArray(int i) {
            return new SemContextActivityLocationLoggingAttribute[i];
        }
    }

    SemContextActivityLocationLoggingAttribute() {
        setAttribute();
    }

    public SemContextActivityLocationLoggingAttribute(int i, int i2, int i3, int i4, int i5) {
        this.mStopPeriod = i;
        this.mWaitPeriod = i2;
        this.mStayingRadius = i3;
        this.mAreaRadius = i4;
        this.mLppResolution = i5;
        setAttribute();
    }

    SemContextActivityLocationLoggingAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("stop_period", this.mStopPeriod);
        bundle.putInt("wait_period", this.mWaitPeriod);
        bundle.putInt("staying_radius", this.mStayingRadius);
        bundle.putInt("area_radius", this.mAreaRadius);
        bundle.putInt("lpp_resolution", this.mLppResolution);
        super.setAttribute(24, bundle);
    }

    public boolean checkAttribute() {
        if (this.mStopPeriod < 0) {
            Log.e(TAG, "The stop period is wrong.");
            return false;
        } else if (this.mWaitPeriod < 0) {
            Log.e(TAG, "The wait period is wrong.");
            return false;
        } else if (this.mStayingRadius < 0) {
            Log.e(TAG, "The staying radius is wrong.");
            return false;
        } else if (this.mAreaRadius < 0) {
            Log.e(TAG, "The area radius is wrong.");
            return false;
        } else if (this.mLppResolution >= 0 && this.mLppResolution <= 2) {
            return true;
        } else {
            Log.e(TAG, "The lpp resolution is wrong.");
            return false;
        }
    }
}
