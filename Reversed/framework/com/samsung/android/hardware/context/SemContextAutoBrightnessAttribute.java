package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextAutoBrightnessAttribute extends SemContextAttribute {
    public static final Creator<SemContextAutoBrightnessAttribute> CREATOR = new C01451();
    private static final int MODE_CONFIGURATION = 1;
    private static final int MODE_DEVICE_MODE = 0;
    private static final String TAG = "SemContextAutoBrightnessAttribute";
    private int mDeviceMode = 0;
    private byte[] mLuminanceTable = null;
    private int mMode = -1;

    static class C01451 implements Creator<SemContextAutoBrightnessAttribute> {
        C01451() {
        }

        public SemContextAutoBrightnessAttribute createFromParcel(Parcel parcel) {
            return new SemContextAutoBrightnessAttribute(parcel);
        }

        public SemContextAutoBrightnessAttribute[] newArray(int i) {
            return new SemContextAutoBrightnessAttribute[i];
        }
    }

    SemContextAutoBrightnessAttribute() {
        setAttribute();
    }

    public SemContextAutoBrightnessAttribute(int i) {
        this.mDeviceMode = i;
        this.mMode = 0;
        setAttribute();
    }

    SemContextAutoBrightnessAttribute(Parcel parcel) {
        super(parcel);
    }

    public SemContextAutoBrightnessAttribute(byte[] bArr) {
        this.mLuminanceTable = bArr;
        this.mMode = 1;
        setAttribute();
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("mode", this.mMode);
        if (this.mMode == 1) {
            bundle.putByteArray("luminance_config_data", this.mLuminanceTable);
        } else if (this.mMode == 0) {
            bundle.putInt("device_mode", this.mDeviceMode);
        }
        super.setAttribute(39, bundle);
    }

    public boolean checkAttribute() {
        if (this.mMode == 0) {
            if (this.mDeviceMode < 0 || this.mDeviceMode > 2) {
                Log.e(TAG, "The device mode is wrong.");
                return false;
            }
        } else if (this.mMode == 1 && this.mLuminanceTable == null) {
            Log.e(TAG, "The luminance configuration data is null.");
            return false;
        }
        return true;
    }
}
