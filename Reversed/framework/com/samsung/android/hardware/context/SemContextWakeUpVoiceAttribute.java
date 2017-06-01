package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemContextWakeUpVoiceAttribute extends SemContextAttribute {
    public static final Creator<SemContextWakeUpVoiceAttribute> CREATOR = new C01941();
    private static final int MODE_REFERENCE_DATA = 1;
    private static final int MODE_REGISTER = 0;
    private static final String TAG = "SemContextWakeUpVoiceAttribute";
    private byte[] mGramData;
    private int mMode;
    private byte[] mNetData;
    private int mVoiceMode;

    static class C01941 implements Creator<SemContextWakeUpVoiceAttribute> {
        C01941() {
        }

        public SemContextWakeUpVoiceAttribute createFromParcel(Parcel parcel) {
            return new SemContextWakeUpVoiceAttribute(parcel);
        }

        public SemContextWakeUpVoiceAttribute[] newArray(int i) {
            return new SemContextWakeUpVoiceAttribute[i];
        }
    }

    SemContextWakeUpVoiceAttribute() {
        this.mMode = -1;
        this.mVoiceMode = 1;
        this.mNetData = null;
        this.mGramData = null;
        this.mMode = 0;
        setAttribute();
    }

    SemContextWakeUpVoiceAttribute(int i) {
        this.mMode = -1;
        this.mVoiceMode = 1;
        this.mNetData = null;
        this.mGramData = null;
        this.mMode = 0;
        this.mVoiceMode = i;
        setAttribute();
    }

    SemContextWakeUpVoiceAttribute(Parcel parcel) {
        super(parcel);
        this.mMode = -1;
        this.mVoiceMode = 1;
        this.mNetData = null;
        this.mGramData = null;
    }

    public SemContextWakeUpVoiceAttribute(byte[] bArr, byte[] bArr2) {
        this.mMode = -1;
        this.mVoiceMode = 1;
        this.mNetData = null;
        this.mGramData = null;
        this.mMode = 1;
        this.mNetData = bArr;
        this.mGramData = bArr2;
        setAttribute();
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putInt("mode", this.mMode);
        if (this.mMode == 0) {
            bundle.putInt("voice_mode", this.mVoiceMode);
        } else {
            bundle.putByteArray("net_data", this.mNetData);
            bundle.putByteArray("gram_data", this.mGramData);
        }
        super.setAttribute(16, bundle);
    }

    public boolean checkAttribute() {
        boolean z = true;
        if (this.mMode == 0) {
            if (!(this.mVoiceMode == 1 || this.mVoiceMode == 2)) {
                z = false;
            }
            return z;
        } else if (this.mNetData == null) {
            Log.e(TAG, "The net data is null.");
            return false;
        } else if (this.mGramData != null) {
            return true;
        } else {
            Log.e(TAG, "The gram data is null.");
            return false;
        }
    }
}
