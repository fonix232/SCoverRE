package com.samsung.android.os;

import android.content.Context;
import android.os.CustomFrequencyManager;
import android.os.SystemProperties;

class SemDvfsPresets {
    static final String BASE_MODEL = "";
    static String BOARD_PLATFORM = SystemProperties.get("ro.board.platform");
    static final String CHIP_NAME = SystemProperties.get("ro.chipname");
    static final String DEVICE_TYPE = SystemProperties.get("ro.build.characteristics");
    static final String HARDWARE_NAME = SystemProperties.get("ro.hardware");
    static final String SIOP_MODEL = "ssrm_dream2l_xx";
    static boolean sIsDebugLevelHigh = "0x4948".equals(SystemProperties.get("ro.debug_level", "0x4f4c"));
    private CustomFrequencyManager mCustomFreqManager = null;
    int[] mSupportedCPUCoreNum = null;
    int[] mSupportedCPUCoreNumForSSRM = null;
    int[] mSupportedCPUFrequency = null;
    int[] mSupportedCPUFrequencyForSSRM = null;

    protected SemDvfsPresets(Context context) {
        this.mCustomFreqManager = (CustomFrequencyManager) context.getSystemService("CustomFrequencyManagerService");
        if (this.mCustomFreqManager != null) {
            this.mSupportedCPUFrequency = this.mCustomFreqManager.getSupportedCPUFrequency();
            this.mSupportedCPUFrequencyForSSRM = this.mCustomFreqManager.getSupportedCPUFrequency();
            adjustCPUFreqTable();
            this.mSupportedCPUCoreNum = this.mCustomFreqManager.getSupportedCPUCoreNum();
            this.mSupportedCPUCoreNumForSSRM = this.mCustomFreqManager.getSupportedCPUCoreNum();
            adjustCPUCoreTable();
        }
    }

    private void adjustCPUCoreTable() {
        if (this.mSupportedCPUCoreNum != null) {
            int i = 0;
            if ("isla".equals("") || "carmen2".equals("")) {
                i = 1;
            }
            if (i > 0 && this.mSupportedCPUCoreNum.length > i) {
                int[] iArr = new int[(this.mSupportedCPUCoreNum.length - i)];
                for (int i2 = 0; i2 < this.mSupportedCPUCoreNum.length - i; i2++) {
                    iArr[i2] = this.mSupportedCPUCoreNum[i2 + i];
                }
                this.mSupportedCPUCoreNum = iArr;
            }
        }
    }

    private void adjustCPUFreqTable() {
        if (this.mSupportedCPUFrequency != null) {
            int i = 0;
            if ("hf".equals("")) {
                i = 1;
            } else if ("hrl".equals("")) {
                i = 6;
            } else if ("island".equals("")) {
                if (SIOP_MODEL.contains("novel")) {
                    i = 1;
                }
            } else if ("hrq".equals("") || "kf".equals("") || "ka".equals("") || "tr3ca".equals("") || "zl".equals("") || "zq".equals("")) {
                i = 2;
            } else if ("tf".equals("")) {
                i = this.mSupportedCPUFrequency[0] == 2649600 ? 5 : 2;
            }
            if (SIOP_MODEL.contains("lentis") || SIOP_MODEL.contains("kcat6") || "ta".equals("")) {
                i = 2;
            } else if (SIOP_MODEL.contains("ja_kor")) {
                i = 3;
            }
            if (i > 0 && this.mSupportedCPUFrequency.length > i) {
                int[] iArr = new int[(this.mSupportedCPUFrequency.length - i)];
                for (int i2 = 0; i2 < this.mSupportedCPUFrequency.length - i; i2++) {
                    iArr[i2] = this.mSupportedCPUFrequency[i2 + i];
                }
                this.mSupportedCPUFrequency = iArr;
            }
        }
    }
}
