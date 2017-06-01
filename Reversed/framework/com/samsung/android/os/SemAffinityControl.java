package com.samsung.android.os;

import android.os.Build;
import android.util.Slog;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SemAffinityControl {
    public static final boolean DEBUG = (!"user".equals(Build.TYPE));
    private static final int HMP_CORE_FRONT = 0;
    private static final int HMP_CORE_REAR = 1;
    private static final String HMP_PROPERTY = "4:4";
    private static final String TAG = "SemAffinityControl";
    private static int[] nBig = null;
    private static int[] nLittle = null;
    private static String[] strHmpCore = null;
    private int bigIndex = -1;
    private int littleIndex = -1;

    public SemAffinityControl() {
        logOnEng(TAG, "[Java Side], SemAffinityControl Class Initialized");
        if (HMP_PROPERTY != null && HMP_PROPERTY.length() > 0) {
            strHmpCore = HMP_PROPERTY.split(":");
            if (strHmpCore.length <= 2 || !"B".equals(strHmpCore[2])) {
                this.littleIndex = 0;
                this.bigIndex = 1;
            } else {
                this.littleIndex = 1;
                this.bigIndex = 0;
            }
            nLittle = new int[Integer.parseInt(strHmpCore[this.littleIndex])];
            nBig = new int[Integer.parseInt(strHmpCore[this.bigIndex])];
            int i = 0;
            int length = nLittle.length;
            if (this.littleIndex == 1) {
                i = nBig.length;
                length = 0;
            }
            for (int i2 = 0; i2 < nLittle.length; i2++) {
                nLittle[i2] = i2 + i;
            }
            for (int i3 = 0; i3 < nBig.length; i3++) {
                nBig[i3] = i3 + length;
            }
        }
    }

    public static void logOnEng(String str, String str2) {
        if (DEBUG) {
            Slog.d(str, str2);
        }
    }

    private native int native_set_affinity(int i, int[] iArr);

    public static String readSysfs(String str, String str2) {
        Throwable e;
        Throwable th;
        String str3 = null;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(str2), "UTF-16"));
            try {
                str3 = bufferedReader2.readLine();
                logOnEng(str, "readSysfs:: path = " + str2 + ", result = " + str3);
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (Throwable e2) {
                        logOnEng(str, "e = " + e2.getMessage());
                    }
                }
                bufferedReader = bufferedReader2;
            } catch (IOException e3) {
                e2 = e3;
                bufferedReader = bufferedReader2;
                try {
                    logOnEng(str, "e = " + e2.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable e22) {
                            logOnEng(str, "e = " + e22.getMessage());
                        }
                    }
                    return str3;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable e222) {
                            logOnEng(str, "e = " + e222.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            logOnEng(str, "e = " + e222.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return str3;
        }
        return str3;
    }

    public int clearAffinity(int i) {
        String readSysfs = readSysfs(TAG, "/sys/devices/system/cpu/kernel_max");
        if (readSysfs != null) {
            int parseInt = Integer.parseInt(readSysfs);
            int[] iArr = new int[(parseInt + 1)];
            for (int i2 = 0; i2 <= parseInt; i2++) {
                iArr[i2] = i2;
            }
            if (native_set_affinity(i, iArr) == 1) {
                logOnEng(TAG, "clear_affinity_failed");
                return 1;
            }
            logOnEng(TAG, "clear_affinity_success");
            return 0;
        }
        logOnEng(TAG, "clear_affinity_failed");
        return 1;
    }

    public int setAffinity(int i, int... iArr) {
        if (native_set_affinity(i, iArr) == 1) {
            logOnEng(TAG, "sched_set_affinity_failed");
            return 1;
        }
        logOnEng(TAG, "sched_set_affinity_success");
        return 0;
    }

    public int setAffinityForBig(int i) {
        if (HMP_PROPERTY == null || HMP_PROPERTY.length() <= 0) {
            return 1;
        }
        if (native_set_affinity(i, nBig) == 1) {
            logOnEng(TAG, "sched_set_affinity_failed");
            return 1;
        }
        logOnEng(TAG, "sched_set_affinity_success");
        return 0;
    }

    public int setAffinityForLittle(int i) {
        if (HMP_PROPERTY == null || HMP_PROPERTY.length() <= 0) {
            return 1;
        }
        if (native_set_affinity(i, nLittle) == 1) {
            logOnEng(TAG, "sched_set_affinity_failed");
            return 1;
        }
        logOnEng(TAG, "sched_set_affinity_success");
        return 0;
    }
}
