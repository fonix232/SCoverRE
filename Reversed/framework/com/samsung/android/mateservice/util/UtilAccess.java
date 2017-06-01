package com.samsung.android.mateservice.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import com.samsung.android.mateservice.MateConst;
import com.samsung.android.mateservice.action.ActionBase;
import com.samsung.android.mateservice.common.FwDependency;

public class UtilAccess {
    private static final String ANDROID_PACKAGE_NAME = "android";
    private static final String[] PRIVILEGED_PACKAGES = new String[]{MateConst.MATE_AGENT_PACKAGE_NAME};
    private static final String TAG = "Access";

    public static boolean isAccessible(Context context, int i) {
        boolean z = false;
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        if (isValidSysSvcActionFormat(i) || isValidAgentSvcActionFormat(i)) {
            if ((65536 & i) != 0) {
                z = callingUid == FwDependency.getProcessSystemUid();
                UtilLog.m9v(TAG, "system uid", new Object[0]);
            } else if ((131072 & i) != 0) {
                z = isPrivilegedPkg(context, callingUid);
                UtilLog.m9v(TAG, "privileged pkg", new Object[0]);
            } else if ((262144 & i) != 0) {
                z = isSignedWithPlatformKey(context, callingUid);
                UtilLog.m9v(TAG, "platform key signed pkg", new Object[0]);
            }
        }
        if (!z) {
            UtilLog.m9v(TAG, "isAccessible returns false / action[0x%x], uid [%d], pid[%d]", Integer.valueOf(i), Integer.valueOf(callingUid), Integer.valueOf(callingPid));
        }
        return z;
    }

    private static boolean isPrivilegedPkg(Context context, int i) {
        PackageManager packageManager = context.getPackageManager();
        String[] packagesForUid = packageManager.getPackagesForUid(i);
        if (packagesForUid != null && packagesForUid.length > 0) {
            for (String str : packagesForUid) {
                if (str != null) {
                    for (String str2 : PRIVILEGED_PACKAGES) {
                        if (str.equals(str2) && packageManager.checkSignatures("android", str2) == 0) {
                            return true;
                        }
                    }
                    continue;
                }
            }
        }
        UtilLog.m9v(TAG, "isPrivilegedPkg uid[%d] is invalid", Integer.valueOf(i));
        return false;
    }

    private static boolean isSignedWithPlatformKey(Context context, int i) {
        PackageManager packageManager = context.getPackageManager();
        String[] packagesForUid = packageManager.getPackagesForUid(i);
        if (packagesForUid != null && packagesForUid.length > 0 && packageManager.checkSignatures("android", packagesForUid[0]) == 0) {
            return true;
        }
        UtilLog.m9v(TAG, "isSignedWithPlatformKey uid[%d] is invalid", Integer.valueOf(i));
        return false;
    }

    private static boolean isValidAgentSvcActionFormat(int i) {
        boolean z = false;
        if (!((2097152 & i) == 0 || (ActionBase.MASK_ATTR_ACCESS & i) == 0 || (i & ActionBase.MASK_ATTR_NUMBERING) == 0)) {
            z = true;
        }
        if (!z) {
            UtilLog.m7e(TAG, "invalid action [0x%x]", Integer.valueOf(i));
        }
        return z;
    }

    private static boolean isValidSysSvcActionFormat(int i) {
        int[] iArr = new int[]{-16777216, ActionBase.MASK_ATTR_TYPE, ActionBase.MASK_ATTR_ACCESS, ActionBase.MASK_ATTR_ACCESS_EXTRA, ActionBase.MASK_ATTR_NUMBERING};
        int[] iArr2 = new int[]{0, 1048576, 458752, 0, 0};
        int i2 = 0;
        int i3 = 0;
        for (int i4 : iArr) {
            switch (i2) {
                case 2:
                    if ((iArr2[i2] & (i & i4)) == 0) {
                        break;
                    }
                    i3++;
                    break;
                case 4:
                    if ((i & i4) <= iArr2[i2]) {
                        break;
                    }
                    i3++;
                    break;
                default:
                    if (iArr2[i2] != (i & i4)) {
                        break;
                    }
                    i3++;
                    break;
            }
            i2++;
        }
        return i3 + 1 == iArr.length;
    }

    public static void throwSecurityException(int i, String str) {
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        String str2 = TAG;
        String str3 = "illegal access: uid[%d] / pid[%d] / %d / %s";
        Object[] objArr = new Object[4];
        objArr[0] = Integer.valueOf(callingUid);
        objArr[1] = Integer.valueOf(callingPid);
        objArr[2] = Integer.valueOf(i);
        objArr[3] = str != null ? str : "";
        UtilLog.m7e(str2, str3, objArr);
        throw new SecurityException(UtilLog.getMsg("%s: reason(%d)", str, Integer.valueOf(i)));
    }
}
