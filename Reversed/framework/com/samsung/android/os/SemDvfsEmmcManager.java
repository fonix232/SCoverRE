package com.samsung.android.os;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

class SemDvfsEmmcManager extends SemDvfsManager {
    protected SemDvfsEmmcManager(Context context, String str, int i) {
        super(context, str, i);
    }

    public void acquire() {
        acquire(-1);
    }

    public void acquire(int i) {
        if (this.mCustomFreqManager != null && this.mDvfsValue != -999) {
            ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
            try {
                logOnEng(this.LOG_TAG, "acquire:: timeout = " + i + ", mIsAcquired = " + this.mIsAcquired + " , mTagName : " + this.mTagName);
                if (this.mIsAcquired) {
                    logOnEng(this.LOG_TAG, "acquire:: DVFS lock is already acquired. Previous lock will be released first.");
                    release();
                }
                if (this.mType == 18) {
                    if (this.mDvfsRequest != null) {
                        this.mDvfsRequest.cancelFrequencyRequest();
                        this.mDvfsRequest = null;
                    }
                    this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(8, -1, (long) i, this.mTagName, this.mContext);
                    if (this.mDvfsRequest != null) {
                        this.mDvfsRequest.doFrequencyRequest();
                    }
                }
                this.mIsAcquired = true;
            } finally {
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
            }
        }
    }

    public void clearDvfsValue() {
        this.mDvfsValue = -1;
    }

    public void setDvfsValue(int i) {
        this.mDvfsValue = -1;
    }

    public void setDvfsValue(String str) {
    }
}
