package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.transcode.core.Encode.BitRate;

public class SemContextFlatMotionForTableModeAttribute extends SemContextAttribute {
    public static final Creator<SemContextFlatMotionForTableModeAttribute> CREATOR = new C01611();
    private static final String TAG = "SemContextFlatMotionForTableModeAttribute";
    private int mDuration = BitRate.MIN_VIDEO_D1_BITRATE;

    static class C01611 implements Creator<SemContextFlatMotionForTableModeAttribute> {
        C01611() {
        }

        public SemContextFlatMotionForTableModeAttribute createFromParcel(Parcel parcel) {
            return new SemContextFlatMotionForTableModeAttribute(parcel);
        }

        public SemContextFlatMotionForTableModeAttribute[] newArray(int i) {
            return new SemContextFlatMotionForTableModeAttribute[i];
        }
    }

    SemContextFlatMotionForTableModeAttribute() {
        setAttribute();
    }

    public SemContextFlatMotionForTableModeAttribute(int i) {
        this.mDuration = i;
        setAttribute();
    }

    SemContextFlatMotionForTableModeAttribute(Parcel parcel) {
        super(parcel);
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("duration", this.mDuration);
        super.setAttribute(36, bundle);
    }

    public boolean checkAttribute() {
        if (this.mDuration >= 0) {
            return true;
        }
        Log.e(TAG, "The duration is wrong.");
        return false;
    }
}
