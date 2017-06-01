package com.samsung.android.os;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

class SemDvfsBusManager extends SemDvfsManager {
    protected SemDvfsBusManager(Context context, String str, int i) {
        super(context, str, i);
    }

    public void acquire() {
        acquire(-1);
    }

    public void acquire(int i) {
        if (this.mCustomFreqManager != null) {
            ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
            try {
                logOnEng(this.LOG_TAG, "acquire:: timeout = " + i + ", mIsAcquired = " + this.mIsAcquired + " , mTagName : " + this.mTagName);
                if (this.mIsAcquired && i == -1) {
                    logOnEng(this.LOG_TAG, "acquire:: DVFS lock is already acquired. Previous lock will be released first.");
                    release();
                }
                if (this.mType == 19) {
                    if (this.mSupportedValues != null) {
                        int i2 = this.mDvfsValue;
                        if (i2 != -1 && this.mDvfsRequest == null) {
                            this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(10, i2, (long) i, this.mTagName, this.mContext);
                        }
                        if (!(i2 == -1 || this.mDvfsRequest == null)) {
                            if (i > 0) {
                                this.mDvfsRequest.setValueTimeout((long) i);
                            }
                            if (!this.mDvfsRequest.setValueFreq(i2)) {
                                logOnEng(this.LOG_TAG, "acquire:: DVFS setvalue doesn't work : TYPE_BUS_MIN_busMinfreq = " + i2);
                                return;
                            }
                        }
                    }
                } else if (this.mType == 20 && this.mSupportedValues != null) {
                    int i3 = this.mDvfsValue;
                    if (i3 != -1 && this.mDvfsRequest == null) {
                        this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(11, i3, (long) i, this.mTagName, this.mContext);
                    }
                    if (!(i3 == -1 || this.mDvfsRequest == null)) {
                        if (i > 0) {
                            this.mDvfsRequest.setValueTimeout((long) i);
                        }
                        if (!this.mDvfsRequest.setValueFreq(i3)) {
                            logOnEng(this.LOG_TAG, "acquire:: DVFS setvalue doesn't work : TYPE_BUS_MAX_busMaxfreq = " + i3);
                            StrictMode.setThreadPolicy(allowThreadDiskWrites);
                            return;
                        }
                    }
                }
                if (this.mDvfsRequest != null) {
                    this.mDvfsRequest.doFrequencyRequest();
                }
                this.mIsAcquired = true;
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
            } finally {
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
            }
        }
    }

    public void clearDvfsValue() {
        this.mDvfsValue = -999;
    }

    public void setDvfsValue(int i) {
        this.mDvfsValue = i;
    }

    public void setDvfsValue(String str) {
    }
}
