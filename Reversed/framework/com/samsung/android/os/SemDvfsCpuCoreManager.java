package com.samsung.android.os;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

class SemDvfsCpuCoreManager extends SemDvfsManager {
    protected SemDvfsCpuCoreManager(Context context, String str, int i) {
        super(context, str, i);
        adjustCPUCoreTable();
    }

    private void adjustCPUCoreTable() {
        if (this.mSupportedValues != null) {
            int i = 0;
            if ("isla".equals("") || "carmen2".equals("")) {
                i = 1;
            }
            if (i > 0 && this.mSupportedValues.length > i) {
                int[] iArr = new int[(this.mSupportedValues.length - i)];
                for (int i2 = 0; i2 < this.mSupportedValues.length - i; i2++) {
                    iArr[i2] = this.mSupportedValues[i2 + i];
                }
                this.mSupportedValues = iArr;
            }
        }
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
                int i2;
                if (this.mType == 14) {
                    if (this.mSupportedValues != null) {
                        i2 = this.mDvfsValue;
                        if (i2 > 0 && this.mDvfsRequest == null) {
                            this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(4, i2, (long) i, this.mTagName, this.mContext);
                        }
                        if (i2 > 0 && this.mDvfsRequest != null) {
                            if (i > 0) {
                                this.mDvfsRequest.setValueTimeout((long) i);
                            }
                            if (!this.mDvfsRequest.setValueFreq(i2)) {
                                logOnEng(this.LOG_TAG, "acquire:: DVFS setvalue doesn't work : TYPE_CPU_CORE_NUM_MIN_coreNum = " + i2);
                                return;
                            }
                        }
                    }
                } else if (this.mType == 15 && this.mSupportedValues != null) {
                    i2 = this.mDvfsValue;
                    if ((i2 > 0 || "msm8992".equals(this.BOARD_PLATFORM)) && this.mDvfsRequest == null) {
                        this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(5, i2, (long) i, this.mTagName, this.mContext);
                    }
                    if (i2 > 0 && this.mDvfsRequest != null) {
                        if (i > 0) {
                            this.mDvfsRequest.setValueTimeout((long) i);
                        }
                        if (!this.mDvfsRequest.setValueFreq(i2)) {
                            logOnEng(this.LOG_TAG, "acquire:: DVFS setvalue doesn't work : TYPE_CPU_CORE_NUM_MAX_coreNum = " + i2);
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
