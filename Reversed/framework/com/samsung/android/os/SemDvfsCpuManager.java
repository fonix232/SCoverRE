package com.samsung.android.os;

import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

final class SemDvfsCpuManager extends SemDvfsManager {
    protected SemDvfsCpuManager(Context context, String str, int i) {
        super(context, str, i);
        adjustCPUFreqTable();
    }

    private void adjustCPUFreqTable() {
        if (this.mSupportedValues != null) {
            int i = 0;
            if ("hf".equals("")) {
                i = 1;
            } else if ("hrl".equals("")) {
                i = 6;
            } else if ("island".equals("")) {
                if ("ssrm_dream2l_xx".contains("novel")) {
                    i = 1;
                }
            } else if ("hrq".equals("") || "kf".equals("") || "ka".equals("") || "tr3ca".equals("") || "zl".equals("") || "dvfs_policy_kangchen_xx".contains("msm8996") || "dvfs_policy_kangchen_xx".contains("kangchen") || "zq".equals("")) {
                i = 2;
            } else if ("tf".equals("")) {
                i = this.mSupportedValues[0] == 2649600 ? 5 : 2;
            } else if ("dvfs_policy_kangchen_xx".contains("dvfs")) {
                i = 2;
            }
            if ("ssrm_dream2l_xx".contains("lentis") || "ssrm_dream2l_xx".contains("kcat6") || "ta".equals("")) {
                i = 2;
            } else if ("ssrm_dream2l_xx".contains("ja_kor")) {
                i = 3;
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
        if (this.mCustomFreqManager != null && this.mDvfsValue != -999 && this.mDvfsValue != -1) {
            ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
            try {
                logOnEng(this.LOG_TAG, "acquire:: timeout = " + i + ", mIsAcquired = " + this.mIsAcquired + " , mTagName : " + this.mTagName);
                if (this.mIsAcquired && i == -1) {
                    logOnEng(this.LOG_TAG, "acquire:: DVFS lock is already acquired. Previous lock will be released first.");
                    release();
                }
                int i2;
                if (this.mType == 12) {
                    if (this.mSupportedValues != null) {
                        i2 = this.mDvfsValue;
                        if ("ja".equals("") && i2 > 1600000) {
                            i2 = getApproximateFrequency(1600000);
                        }
                        if (i2 != -1 && this.mDvfsRequest == null) {
                            this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(6, i2, (long) i, this.mTagName, this.mContext);
                        }
                        if (!(i2 == -1 || this.mDvfsRequest == null)) {
                            if (i > 0) {
                                this.mDvfsRequest.setValueTimeout((long) i);
                            }
                            if (!this.mDvfsRequest.setValueFreq(i2)) {
                                logOnEng(this.LOG_TAG, "acquire:: DVFS setvalue doesn't work : TYPE_CPU_MIN_freq = " + i2);
                                return;
                            }
                        }
                    }
                } else if (this.mType == 13 && this.mSupportedValues != null) {
                    i2 = this.mDvfsValue;
                    if (i2 != -1 && this.mDvfsRequest == null) {
                        this.mDvfsRequest = this.mCustomFreqManager.newFrequencyRequest(7, i2, (long) i, this.mTagName, this.mContext);
                    }
                    if (!(i2 == -1 || this.mDvfsRequest == null)) {
                        if (i > 0) {
                            this.mDvfsRequest.setValueTimeout((long) i);
                        }
                        if (!this.mDvfsRequest.setValueFreq(i2)) {
                            logOnEng(this.LOG_TAG, "acquire:: DVFS setvalue doesn't work : TYPE_CPU_MAX_freq = " + i2);
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

    public int getApproximateFrequencyByPercentForSSRM(double d) {
        return (this.mSupportedValuesForSsrm != null && this.mSupportedValuesForSsrm.length > 0) ? getApproximateFrequencyForSsrm((int) (((double) this.mSupportedValuesForSsrm[0]) * d)) : -1;
    }

    public void setDvfsValue(int i) {
        this.mDvfsValue = i;
    }

    public void setDvfsValue(String str) {
    }
}
