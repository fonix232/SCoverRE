package com.samsung.android.os;

import android.content.Context;
import android.os.CustomFrequencyManager;
import android.os.CustomFrequencyManager.FrequencyRequest;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemProperties;
import android.util.Log;

public abstract class SemDvfsManager {
    public static final String HINT_AMS_ACT_RESUME = "AMS_ACT_RESUME";
    public static final String HINT_AMS_ACT_START = "AMS_ACT_START";
    public static final String HINT_AMS_APP_HOME = "AMS_APP_HOME";
    public static final String HINT_AMS_APP_SWITCH = "AMS_APP_SWITCH";
    public static final String HINT_AMS_RELAUNCH_RESUME = "AMS_RELAUNCH_RESUME";
    public static final String HINT_AMS_RESUME = "AMS_RESUME";
    public static final String HINT_AMS_RESUME_TAIL = "AMS_RESUME_TAIL";
    public static final String HINT_AMS_RESUME_TAIL_CSTATE = "AMS_RESUME_TAIL_CSTATE";
    public static final String HINT_APP_LAUNCH = "APP_LAUNCH";
    public static final String HINT_BADGE_UPDATE = "BADGE_UPDATE";
    public static final String HINT_BROWSER_FLING = "BROWSER_FLING";
    public static final String HINT_BROWSER_TOUCH = "BROWSER_TOUCH";
    public static final String HINT_CONTACT_SCROLL = "CONTACT_SCROLL";
    public static final String HINT_DEVICE_WAKEUP = "DEVICE_WAKEUP";
    public static final String HINT_GALLERY_SCROLL = "GALLERY_SCROLL";
    public static final String HINT_GALLERY_TOUCH = "GALLERY_TOUCH";
    public static final String HINT_GALLERY_TOUCH_TAIL = "GALLERY_TOUCH_TAIL";
    public static final String HINT_GESTURE_DETECTED = "GESTURE_DETECTED";
    public static final String HINT_HOME_KEY_TOUCH = "HOME_KEY_TOUCH";
    public static final String HINT_LAUNCHER_TOUCH = "LAUNCHER_TOUCH";
    public static final String HINT_LISTVIEW_SCROLL = "LISTVIEW_SCROLL";
    public static final String HINT_PWM_ROTATION = "PWM_ROTATION";
    public static final String HINT_SMOOTH_SCROLL = "SMOOTH_SCROLL";
    public static final int TYPE_BUS_MAX = 20;
    public static final int TYPE_BUS_MIN = 19;
    public static final int TYPE_CPUCTL = 28;
    public static final int TYPE_CPUSET = 27;
    public static final int TYPE_CPU_CORE_NUM_MAX = 15;
    public static final int TYPE_CPU_CORE_NUM_MIN = 14;
    public static final int TYPE_CPU_HOTPLUG_DISABLE = 25;
    public static final int TYPE_CPU_LEGACY_SCHEDULER = 24;
    public static final int TYPE_CPU_MAX = 13;
    public static final int TYPE_CPU_MIN = 12;
    public static final int TYPE_CPU_POWER_COLLAPSE_DISABLE = 23;
    public static final int TYPE_EMMC_BURST_MODE = 18;
    public static final int TYPE_FPS_MAX = 22;
    public static final int TYPE_GPU_MAX = 17;
    public static final int TYPE_GPU_MIN = 16;
    public static final int TYPE_HINT = 21;
    public static final int TYPE_MAX = 29;
    public static final int TYPE_NONE = 11;
    public static final int TYPE_PCIE_PSM_DISABLE = 26;
    static int mToken = 0;
    final String BASE_MODEL = "";
    String BOARD_PLATFORM = SystemProperties.get("ro.board.platform");
    final String DVFS_FILENAME = "dvfs_policy_kangchen_xx";
    String LOG_TAG = SemDvfsManager.class.getSimpleName();
    final String SIOP_MODEL = "ssrm_dream2l_xx";
    Context mContext = null;
    CustomFrequencyManager mCustomFreqManager = null;
    FrequencyRequest mDvfsRequest = null;
    int mDvfsValue = -999;
    volatile boolean mIsAcquired = false;
    String mName = null;
    int[] mSupportedValues = null;
    int[] mSupportedValuesForSsrm = null;
    String mTagName = null;
    int mType = 11;
    boolean sIsDebugLevelHigh = "0x4948".equals(SystemProperties.get("ro.debug_level", "0x4f4c"));

    protected SemDvfsManager(Context context, String str, int i) {
        if (context != null) {
            this.mContext = context;
            this.mTagName = str;
            this.mType = i;
            this.mCustomFreqManager = (CustomFrequencyManager) this.mContext.getSystemService("CustomFrequencyManagerService");
            if (this.mCustomFreqManager == null) {
                Log.i(this.LOG_TAG, "SemDvfsManager:: failed to load CFMS");
                return;
            }
            logOnEng(this.LOG_TAG, "SemDvfsManager:: New instance is created for " + this.mTagName + ", type = " + this.mType);
            mToken++;
            if (str != null) {
                this.mTagName = str + "@" + mToken;
            } else {
                this.mTagName = context.getPackageName() + "@" + mToken;
            }
        }
    }

    public static SemDvfsManager createInstance(Context context, int i) {
        return createInstance(context, context.getPackageName(), i);
    }

    public static SemDvfsManager createInstance(Context context, String str, int i) {
        if (i == 12 || i == 13) {
            return new SemDvfsCpuManager(context, str, i);
        }
        if (i == 14 || i == 15) {
            return new SemDvfsCpuCoreManager(context, str, i);
        }
        if (i == 16 || i == 17) {
            return new SemDvfsGpuManager(context, str, i);
        }
        if (i == 18) {
            return new SemDvfsEmmcManager(context, str, i);
        }
        if (i == 19 || i == 20) {
            return new SemDvfsBusManager(context, str, i);
        }
        if (i == 23) {
            return new SemDvfsPowerCollapseManager(context, str, i);
        }
        if (i == 25) {
            return new SemDvfsCpuHotplugManager(context, str, i);
        }
        if (i == 21) {
            return new SemDvfsHintManager(context, str, i);
        }
        if (i > 21) {
            return new SemDvfsArrangedSetsManager(context, str, i);
        }
        mToken++;
        return null;
    }

    public static SemDvfsManager createInstance(Context context, String str, int i, int i2) {
        if (i == 21) {
            return new SemDvfsHintManager(context, str, i, i2);
        }
        mToken++;
        return null;
    }

    public abstract void acquire();

    public void acquire(int i) {
    }

    public void acquire(String str) {
    }

    public abstract void clearDvfsValue();

    public int getApproximateFrequency(int i) {
        if (this.mSupportedValues == null || i < 0) {
            return -1;
        }
        int length = this.mSupportedValues.length;
        if (length <= 0) {
            return -1;
        }
        int i2 = this.mSupportedValues[0];
        while (length > 0) {
            if (this.mSupportedValues[length - 1] >= i) {
                i2 = this.mSupportedValues[length - 1];
                break;
            }
            length--;
        }
        return i2;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getApproximateFrequencyByPercent(double r8) {
        /*
        r7 = this;
        r5 = 0;
        r4 = -1;
        r1 = "CPU_CORE";
        r2 = r7.mName;
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x0011;
    L_0x000d:
        r1 = r7.mSupportedValues;
        if (r1 != 0) goto L_0x0012;
    L_0x0011:
        return r4;
    L_0x0012:
        r2 = 0;
        r1 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
        if (r1 < 0) goto L_0x0011;
    L_0x0018:
        r2 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r1 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x0011;
    L_0x001e:
        r1 = r7.mSupportedValues;
        r0 = r1.length;
        if (r0 > 0) goto L_0x0024;
    L_0x0023:
        return r4;
    L_0x0024:
        r1 = r7.mSupportedValues;
        r1 = r1[r5];
        r2 = (double) r1;
        r2 = r2 * r8;
        r1 = (int) r2;
        r1 = r7.getApproximateFrequency(r1);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.os.SemDvfsManager.getApproximateFrequencyByPercent(double):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getApproximateFrequencyByPercentForSsrm(double r8) {
        /*
        r7 = this;
        r5 = 0;
        r4 = -1;
        r1 = "CPU_CORE";
        r2 = r7.mName;
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x0011;
    L_0x000d:
        r1 = r7.mSupportedValuesForSsrm;
        if (r1 != 0) goto L_0x0012;
    L_0x0011:
        return r4;
    L_0x0012:
        r2 = 0;
        r1 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
        if (r1 < 0) goto L_0x0011;
    L_0x0018:
        r2 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r1 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x0011;
    L_0x001e:
        r1 = r7.mSupportedValuesForSsrm;
        r0 = r1.length;
        if (r0 > 0) goto L_0x0024;
    L_0x0023:
        return r4;
    L_0x0024:
        r1 = r7.mSupportedValuesForSsrm;
        r1 = r1[r5];
        r2 = (double) r1;
        r2 = r2 * r8;
        r1 = (int) r2;
        r1 = r7.getApproximateFrequencyForSsrm(r1);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.os.SemDvfsManager.getApproximateFrequencyByPercentForSsrm(double):int");
    }

    public int getApproximateFrequencyForSsrm(int i) {
        logOnEng(this.LOG_TAG, "getApproximateFrequencyForSsrm , mName : " + this.mName + " , freq : " + i);
        if (!"CPU".equals(this.mName) && !"CORE_NUM".equals(this.mName) && !"GPU".equals(this.mName) && !"BUS".equals(this.mName)) {
            return getApproximateFrequency(i);
        }
        if (this.mSupportedValuesForSsrm == null || i < 0) {
            return -1;
        }
        int length = this.mSupportedValuesForSsrm.length;
        if (length <= 0) {
            return -1;
        }
        int i2 = this.mSupportedValuesForSsrm[0];
        while (length > 0) {
            if (this.mSupportedValuesForSsrm[length - 1] >= i) {
                i2 = this.mSupportedValuesForSsrm[length - 1];
                break;
            }
            length--;
        }
        logOnEng(this.LOG_TAG, "getApproximateFrequencyForSsrm = " + i2 + ", mName" + this.mName + " , freq : " + i);
        return i2;
    }

    public String getName() {
        return this.mName;
    }

    public int[] getSupportedFrequency() {
        return this.mSupportedValues;
    }

    public int[] getSupportedFrequencyForSsrm() {
        return this.mSupportedValuesForSsrm;
    }

    public void logOnEng(String str, String str2) {
        if (this.sIsDebugLevelHigh) {
            Log.i(str, str2);
        }
    }

    public void release() {
        if (this.mCustomFreqManager != null) {
            ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
            try {
                logOnEng(this.LOG_TAG, "release():: mIsAcquired = " + this.mIsAcquired + ", mName" + this.mName + " , mTagName : " + this.mTagName);
                if (this.mIsAcquired) {
                    if (this.mDvfsRequest != null) {
                        this.mDvfsRequest.cancelFrequencyRequest();
                        this.mDvfsRequest = null;
                    }
                    this.mIsAcquired = false;
                    StrictMode.setThreadPolicy(allowThreadDiskWrites);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
            }
        }
    }

    public abstract void setDvfsValue(int i);

    public abstract void setDvfsValue(String str);

    public void update(Context context, String str) {
    }
}
