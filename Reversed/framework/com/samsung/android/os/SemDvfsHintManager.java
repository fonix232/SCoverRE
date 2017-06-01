package com.samsung.android.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BaseBundle;
import android.os.ICustomFrequencyManager;
import android.os.ICustomFrequencyManager.Stub;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.share.SShareConstants;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.transcode.core.Encode.BitRate;
import java.util.ArrayList;

class SemDvfsHintManager extends SemDvfsManager {
    private String ACTION_RESOLUTION_CHANGED;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_L;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_LL;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_LM;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_M;
    final int APP_LAUNCH_BOOSTING_TIMEOUT_S;
    int mAppLaunchBoostTime;
    private final String[] mAppLaunchPackages;
    Context mContext;
    String mHint;
    private ArrayList<SemDvfsManager> mHintList;
    private int mHintTimeout;
    private BroadcastReceiver mPolicyUpdateReceiver;
    String mTagName;
    int mType;
    private volatile ICustomFrequencyManager sCfmsService;

    class C02271 extends BroadcastReceiver {
        C02271() {
        }

        public void onReceive(Context context, Intent intent) {
            if (SemDvfsHintManager.this.sIsDebugLevelHigh) {
                Log.e(SemDvfsHintManager.this.LOG_TAG, "ResolutionChangeReceive hint Name : " + SemDvfsHintManager.this.mHint);
            }
            SemDvfsHintManager.this.update(SemDvfsHintManager.this.mContext, SemDvfsHintManager.this.mHint);
        }
    }

    protected SemDvfsHintManager(Context context, String str, int i) {
        super(context, str, i);
        this.mContext = null;
        this.mTagName = null;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_LL = 4000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_L = 2000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_LM = SShareConstants.MSG_GETDATA_APP_SELECTION;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_S = BitRate.MIN_VIDEO_D1_BITRATE;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_M = 1000;
        this.mType = -1;
        this.mAppLaunchBoostTime = 0;
        this.mHintTimeout = -1;
        this.mHintList = null;
        this.sCfmsService = null;
        this.ACTION_RESOLUTION_CHANGED = "com.samsung.ssrm.RESOLUTION_CHANGED";
        this.mPolicyUpdateReceiver = new C02271();
        this.mAppLaunchPackages = new String[]{m14x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 84, 9, 24, 8, 21, 13, 9, 31, 8}), m14x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 9, 84, 25, 18, 8, 21, 23, 31}), m14x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 24, 8, 21, 13, 9, 31, 8}), m14x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 23}), m14x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 31, 23, 27, 19, 22, 84, 15, 19}), m14x(new int[]{25, 21, 23, 84, 28, 27, 25, 31, 24, 21, 21, 17, 84, 17, 27, 14, 27, 20, 27}), m14x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 31, 20, 30, 19, 20, 29}), m14x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 31, 12, 31, 8, 29, 22, 27, 30, 31, 9, 84, 12, 19, 30, 31, 21}), m14x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 19, 30, 31, 21}), m14x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 27, 22, 22, 31, 8, 3, 73, 30}), m14x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 23, 27, 10, 9}), m14x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 27, 10, 10, 9, 31, 27, 8, 25, 18}), m14x(new int[]{25, 21, 23, 84, 9, 19, 20, 27, 84, 13, 31, 19, 24, 21}), m14x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 56, 27, 19, 30, 15, 55, 27, 10}), m14x(new int[]{25, 21, 23, 84, 14, 13, 19, 14, 14, 31, 8, 84, 27, 20, 30, 8, 21, 19, 30})};
        this.mContext = context;
        this.LOG_TAG = SemDvfsHintManager.class.getSimpleName();
        this.mHintList = new ArrayList();
        createHintNotifier(context, str);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(this.ACTION_RESOLUTION_CHANGED);
        context.registerReceiver(this.mPolicyUpdateReceiver, intentFilter);
    }

    protected SemDvfsHintManager(Context context, String str, int i, int i2) {
        super(context, str, i);
        this.mContext = null;
        this.mTagName = null;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_LL = 4000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_L = 2000;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_LM = SShareConstants.MSG_GETDATA_APP_SELECTION;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_S = BitRate.MIN_VIDEO_D1_BITRATE;
        this.APP_LAUNCH_BOOSTING_TIMEOUT_M = 1000;
        this.mType = -1;
        this.mAppLaunchBoostTime = 0;
        this.mHintTimeout = -1;
        this.mHintList = null;
        this.sCfmsService = null;
        this.ACTION_RESOLUTION_CHANGED = "com.samsung.ssrm.RESOLUTION_CHANGED";
        this.mPolicyUpdateReceiver = new C02271();
        this.mAppLaunchPackages = new String[]{m14x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 84, 9, 24, 8, 21, 13, 9, 31, 8}), m14x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 27, 10, 10, 9, 84, 25, 18, 8, 21, 23, 31}), m14x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 24, 8, 21, 13, 9, 31, 8}), m14x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 23}), m14x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 31, 23, 27, 19, 22, 84, 15, 19}), m14x(new int[]{25, 21, 23, 84, 28, 27, 25, 31, 24, 21, 21, 17, 84, 17, 27, 14, 27, 20, 27}), m14x(new int[]{25, 21, 23, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 31, 20, 30, 19, 20, 29}), m14x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 31, 12, 31, 8, 29, 22, 27, 30, 31, 9, 84, 12, 19, 30, 31, 21}), m14x(new int[]{25, 21, 23, 84, 9, 27, 23, 9, 15, 20, 29, 84, 27, 20, 30, 8, 21, 19, 30, 84, 12, 19, 30, 31, 21}), m14x(new int[]{25, 21, 23, 84, 9, 31, 25, 84, 27, 20, 30, 8, 21, 19, 30, 84, 29, 27, 22, 22, 31, 8, 3, 73, 30}), m14x(new int[]{25, 21, 23, 84, 29, 21, 21, 29, 22, 31, 84, 27, 20, 30, 8, 21, 19, 30, 84, 23, 27, 10, 9}), m14x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 27, 10, 10, 9, 31, 27, 8, 25, 18}), m14x(new int[]{25, 21, 23, 84, 9, 19, 20, 27, 84, 13, 31, 19, 24, 21}), m14x(new int[]{25, 21, 23, 84, 24, 27, 19, 30, 15, 84, 56, 27, 19, 30, 15, 55, 27, 10}), m14x(new int[]{25, 21, 23, 84, 14, 13, 19, 14, 14, 31, 8, 84, 27, 20, 30, 8, 21, 19, 30})};
        this.mContext = context;
        this.LOG_TAG = SemDvfsHintManager.class.getSimpleName();
        this.mHintList = new ArrayList();
        createHintNotifier(context, str);
    }

    private void createHintNotifier(Context context, String str) {
        if (this.sCfmsService == null) {
            try {
                this.sCfmsService = Stub.asInterface(ServiceManager.getService("CustomFrequencyManagerService"));
                if (this.sCfmsService == null) {
                    return;
                }
            } catch (Exception e) {
                if (this.sIsDebugLevelHigh) {
                    Log.e(this.LOG_TAG, "createHintNotifier:: failed to get cfms service.");
                }
                if (this.sCfmsService == null) {
                    return;
                }
            } catch (Throwable th) {
                if (this.sCfmsService == null) {
                    return;
                }
            }
        }
        this.mHint = str;
        Intent intent = null;
        try {
            intent = this.sCfmsService.getDvfsPolicyByHint(str);
        } catch (Exception e2) {
            Log.e(this.LOG_TAG, "createHintNotifier:: failed to call getDvfsPolicyByHint.");
        }
        if (intent != null) {
            BaseBundle extras = intent.getExtras();
            for (String str2 : extras.keySet()) {
                String string = extras.getString(str2);
                SemDvfsManager semDvfsManager = null;
                String str3 = "";
                int[] iArr = new int[]{0};
                if ("CPU_MIN".equalsIgnoreCase(str2)) {
                    semDvfsManager = new SemDvfsCpuManager(context, str + "@CPU_MIN", 12);
                    str3 = "CPU";
                    iArr = semDvfsManager.getSupportedFrequencyForSsrm();
                } else if ("GPU_MIN".equalsIgnoreCase(str2)) {
                    semDvfsManager = new SemDvfsGpuManager(context, str + "@GPU_MIN", 16);
                    str3 = "GPU";
                    iArr = semDvfsManager.getSupportedFrequencyForSsrm();
                } else if ("BUS_MIN".equalsIgnoreCase(str2)) {
                    str3 = "BUS";
                    semDvfsManager = new SemDvfsBusManager(context, str + "@BUS_MIN", 19);
                    iArr = semDvfsManager.getSupportedFrequency();
                } else if ("CORE_NUM_MIN".equalsIgnoreCase(str2)) {
                    semDvfsManager = new SemDvfsCpuCoreManager(context, str + "@CORE_NUM_MIN", 14);
                    str3 = "CORE_NUM";
                    iArr = semDvfsManager.getSupportedFrequency();
                } else if ("POWER_COLLAPSE".equalsIgnoreCase(str2)) {
                    semDvfsManager = new SemDvfsPowerCollapseManager(context, str + "@POWER_COLLAPSE", 23);
                    str3 = "POWER_COLLAPSE";
                } else if ("timeout".equalsIgnoreCase(str2)) {
                    this.mHintTimeout = Integer.parseInt(string);
                }
                if (semDvfsManager != null) {
                    if ("max".equalsIgnoreCase(string) || SmartFaceManager.PAGE_MIDDLE.equals(string)) {
                        if (iArr != null) {
                            semDvfsManager.setDvfsValue(iArr[0]);
                        }
                        if (this.sIsDebugLevelHigh && iArr != null) {
                            Log.e(this.LOG_TAG, "Max_hint : " + str + ", moduleName = " + str3 + ", freq = " + iArr[0] + ", timeOut = " + this.mHintTimeout);
                        }
                    } else if (!string.endsWith("%")) {
                        semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyForSsrm(Integer.parseInt(string)));
                        if (this.sIsDebugLevelHigh) {
                            Log.e(this.LOG_TAG, "hint(Normal) : " + str + ", moduleName = " + str3 + ", freq = " + Integer.parseInt(string) + ", timeOut = " + this.mHintTimeout);
                        }
                    } else if ("CPU".equals(str3)) {
                        semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(string.substring(0, string.indexOf("%"))) / 100.0d));
                        if (this.sIsDebugLevelHigh) {
                            Log.e(this.LOG_TAG, "hint(%) : " + str + ", moduleName = " + str3 + ", freq = " + semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(string.substring(0, string.indexOf("%"))) / 100.0d) + ", timeOut = " + this.mHintTimeout);
                        }
                    } else if ("GPU".equals(str3)) {
                        semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(string.substring(0, string.indexOf("%"))) / 100.0d));
                        if (this.sIsDebugLevelHigh) {
                            Log.e(this.LOG_TAG, "hint(%) : " + str + ", moduleName = " + str3 + ", freq = " + semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(string.substring(0, string.indexOf("%"))) / 100.0d) + ", timeOut = " + this.mHintTimeout);
                        }
                    } else if ("BUS".equals(str3)) {
                        semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(string.substring(0, string.indexOf("%"))) / 100.0d));
                        if (this.sIsDebugLevelHigh) {
                            Log.e(this.LOG_TAG, "hint(%) : " + str + ", moduleName = " + str3 + ", freq = " + semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(string.substring(0, string.indexOf("%"))) / 100.0d) + ", timeOut = " + this.mHintTimeout);
                        }
                    }
                    this.mHintList.add(semDvfsManager);
                }
            }
        }
    }

    private boolean isPackageExistInAppLaunch(String str) {
        for (CharSequence contains : this.mAppLaunchPackages) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    private String m14x(int[] iArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : iArr) {
            stringBuilder.append((char) (i ^ FingerprintEvent.STATUS_IDENTIFY_FAILURE_DATABASE_FAILURE));
        }
        return stringBuilder.toString();
    }

    public void acquire() {
        acquire(-1);
    }

    public void acquire(int i) {
        if (this.mHintList != null) {
            for (SemDvfsManager semDvfsManager : this.mHintList) {
                if ("CPU".equals(semDvfsManager.getName()) || "CPU_CORE".equals(semDvfsManager.getName()) || "GPU".equals(semDvfsManager.getName()) || "BUS".equals(semDvfsManager.getName()) || "POWER_COLLAPSE".equals(semDvfsManager.getName())) {
                    if (i == -1) {
                        semDvfsManager.acquire(this.mHintTimeout);
                    } else {
                        semDvfsManager.acquire(i);
                    }
                }
            }
        }
    }

    public void acquire(String str) {
        this.mAppLaunchBoostTime = 0;
        if (str.contains("com.sec.android.app.camera")) {
            this.mAppLaunchBoostTime = 1000;
        } else if (isPackageExistInAppLaunch(str)) {
            this.mAppLaunchBoostTime = SShareConstants.MSG_GETDATA_APP_SELECTION;
        }
        if (this.mHintList != null) {
            for (SemDvfsManager semDvfsManager : this.mHintList) {
                if ("CPU".equals(semDvfsManager.getName()) || "CPU_CORE".equals(semDvfsManager.getName()) || "GPU".equals(semDvfsManager.getName()) || "BUS".equals(semDvfsManager.getName()) || "POWER_COLLAPSE".equals(semDvfsManager.getName())) {
                    semDvfsManager.acquire(this.mHintTimeout + this.mAppLaunchBoostTime);
                }
            }
        }
    }

    public void clearDvfsValue() {
        this.mDvfsValue = -999;
    }

    public int getApproximateFrequencyByPercentForSSRM(double d) {
        return -1;
    }

    public void release() {
        if (this.mHintList != null) {
            for (SemDvfsManager semDvfsManager : this.mHintList) {
                if ("CPU".equals(semDvfsManager.getName()) || "CPU_CORE".equals(semDvfsManager.getName()) || "GPU".equals(semDvfsManager.getName()) || "BUS".equals(semDvfsManager.getName()) || "POWER_COLLAPSE".equals(semDvfsManager.getName())) {
                    semDvfsManager.release();
                }
            }
        }
    }

    public void setDvfsValue(int i) {
        this.mDvfsValue = i;
    }

    public void setDvfsValue(String str) {
    }

    public void setValueAtUpdate(SemDvfsManager semDvfsManager, int[] iArr, String str, String str2) {
        if (semDvfsManager == null) {
            return;
        }
        if ("CPU".equalsIgnoreCase(semDvfsManager.getName()) || "CPU_CORE".equalsIgnoreCase(semDvfsManager.getName()) || "GPU".equalsIgnoreCase(semDvfsManager.getName()) || "BUS".equalsIgnoreCase(semDvfsManager.getName()) || "POWER_COLLAPSE".equalsIgnoreCase(semDvfsManager.getName())) {
            if (iArr != null) {
                iArr = semDvfsManager.getSupportedFrequencyForSsrm();
            }
            if ("max".equalsIgnoreCase(str) || SmartFaceManager.PAGE_MIDDLE.equals(str)) {
                if (iArr != null) {
                    semDvfsManager.setDvfsValue(iArr[0]);
                }
                if (this.sIsDebugLevelHigh && iArr != null) {
                    Log.e(this.LOG_TAG, "setValueAtUpdateHint Max_hint : " + str2 + ", mgr.getName() = " + semDvfsManager.getName() + ", freq = " + iArr[0] + ", timeOut = " + this.mHintTimeout);
                }
            } else if (!str.endsWith("%")) {
                semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyForSsrm(Integer.parseInt(str)));
                if (this.sIsDebugLevelHigh) {
                    Log.e(this.LOG_TAG, "setValueAtUpdateHint(Normal) : " + str2 + ", mgr.getName() = " + semDvfsManager.getName() + ", freq = " + Integer.parseInt(str) + ", timeOut = " + this.mHintTimeout);
                }
            } else if ("CPU".equalsIgnoreCase(semDvfsManager.getName())) {
                semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(str.substring(0, str.indexOf("%"))) / 100.0d));
                if (this.sIsDebugLevelHigh) {
                    Log.e(this.LOG_TAG, "setValueAtUpdateHint(%) : " + str2 + ", mgr.getName() = " + semDvfsManager.getName() + ", freq = " + semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(str.substring(0, str.indexOf("%"))) / 100.0d) + ", timeOut = " + this.mHintTimeout);
                }
            } else if ("GPU".equalsIgnoreCase(semDvfsManager.getName())) {
                semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(str.substring(0, str.indexOf("%"))) / 100.0d));
                if (this.sIsDebugLevelHigh) {
                    Log.e(this.LOG_TAG, "setValueAtUpdateHint(%) : " + str2 + ", mgr.getName() = " + semDvfsManager.getName() + ", freq = " + semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(str.substring(0, str.indexOf("%"))) / 100.0d) + ", timeOut = " + this.mHintTimeout);
                }
            } else if ("BUS".equalsIgnoreCase(semDvfsManager.getName())) {
                semDvfsManager.setDvfsValue(semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(str.substring(0, str.indexOf("%"))) / 100.0d));
                if (this.sIsDebugLevelHigh) {
                    Log.e(this.LOG_TAG, "setValueAtUpdateHint(%) : " + str2 + ", mgr.getName() = " + semDvfsManager.getName() + ", freq = " + semDvfsManager.getApproximateFrequencyByPercent(Double.parseDouble(str.substring(0, str.indexOf("%"))) / 100.0d) + ", timeOut = " + this.mHintTimeout);
                }
            }
        }
    }

    public void update(Context context, String str) {
        if (this.sCfmsService == null) {
            try {
                this.sCfmsService = Stub.asInterface(ServiceManager.getService("CustomFrequencyManagerService"));
                if (this.sCfmsService == null) {
                    return;
                }
            } catch (Exception e) {
                if (this.sIsDebugLevelHigh) {
                    Log.e(this.LOG_TAG, "createHintNotifier:: failed to get cfms service.");
                }
                if (this.sCfmsService == null) {
                    return;
                }
            } catch (Throwable th) {
                if (this.sCfmsService == null) {
                    return;
                }
            }
        }
        Intent intent = null;
        try {
            intent = this.sCfmsService.getDvfsPolicyByHint(str);
        } catch (Exception e2) {
            Log.e(this.LOG_TAG, "createHintNotifier:: failed to call getDvfsPolicyByHint.");
        }
        if (intent != null) {
            BaseBundle extras = intent.getExtras();
            for (String str2 : extras.keySet()) {
                String string = extras.getString(str2);
                String str3 = "";
                if (this.sIsDebugLevelHigh) {
                    Log.e(this.LOG_TAG, "Check Updatehint update Key : " + str2 + ", value : " + string);
                }
                if (this.mHintList != null) {
                    for (SemDvfsManager semDvfsManager : this.mHintList) {
                        int[] iArr = new int[]{0};
                        String str4 = "";
                        if (str2 != null) {
                            str4 = str2.toUpperCase();
                        }
                        if (str4.contains(semDvfsManager.getName())) {
                            setValueAtUpdate(semDvfsManager, iArr, string, str);
                            if (this.sIsDebugLevelHigh) {
                                Log.e(this.LOG_TAG, "Updatehint update Key : " + str2 + ", mgr.getName() : " + semDvfsManager.getName());
                            }
                        }
                    }
                }
            }
        }
    }
}
