package com.samsung.android.os;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

class SemDvfsArrangedSetsManager extends SemDvfsManager {
    protected SemDvfsArrangedSetsManager(Context context, String str, int i) {
        super(context, str, i);
    }

    public void acquire() {
        acquire(-1);
    }

    public void acquire(int i) {
        if (this.mCustomFreqManager != null) {
            if (this.mType == 22) {
                if (this.mDvfsValue < 1) {
                    this.mDvfsValue = 1;
                } else if (this.mDvfsValue > 99) {
                    this.mDvfsValue = 99;
                }
            } else if (this.mType > 22) {
                this.mDvfsValue = 0;
            }
            ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
            logOnEng(this.LOG_TAG, "acquire:: timeout = " + i + ", mIsAcquired = " + this.mIsAcquired + " , mTagName : " + this.mTagName);
            if (this.mIsAcquired && i == -1) {
                logOnEng(this.LOG_TAG, "acquire:: DVFS lock is already acquired. Previous lock will be released first.");
                release();
            }
            if (this.mType != 22 || this.mDvfsRequest != null) {
                try {
                    if (this.mType == 23 && this.mDvfsRequest == null) {
                        this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(12, 0, (long) i, this.mTagName, this.mContext);
                    } else if (this.mType == 24 && this.mDvfsRequest == null) {
                        this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(13, 0, (long) i, this.mTagName, this.mContext);
                    } else if (this.mType == 25 && this.mDvfsRequest == null) {
                        this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(14, 0, (long) i, this.mTagName, this.mContext);
                    } else if (this.mType == 26 && this.mDvfsRequest == null) {
                        this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(15, 0, (long) i, this.mTagName, this.mContext);
                    }
                } finally {
                    StrictMode.setThreadPolicy(allowThreadDiskWrites);
                }
            } else if (this.mDvfsValue >= 0 && this.mDvfsValue < 99) {
                this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(3, this.mDvfsValue, (long) i, this.mTagName, this.mContext);
            }
            if (this.mDvfsRequest != null) {
                this.mDvfsRequest.doFrequencyRequest();
            }
            this.mIsAcquired = true;
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
