package com.samsung.android.os;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

class SemDvfsCpuHotplugManager extends SemDvfsManager {
    protected SemDvfsCpuHotplugManager(Context context, String str, int i) {
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
                if (this.mType == 25 && this.mDvfsRequest == null) {
                    this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(14, 0, (long) i, this.mTagName, this.mContext);
                }
                if (this.mDvfsRequest != null) {
                    this.mDvfsRequest.doFrequencyRequest();
                }
                this.mIsAcquired = true;
            } catch (Throwable e) {
                e.printStackTrace();
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
