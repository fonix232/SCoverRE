package com.samsung.android.iccc;

import android.os.RemoteException;
import android.util.Log;

public class IntegrityControlCheckCenter {
    public static final int FLAG_ABOOT_RP_VER = -1048576;
    public static final int FLAG_AFW_STATUS = -1048565;
    public static final int FLAG_BOOT_IMAGE = -1048562;
    public static final int FLAG_CACHE_IMAGE = -1048559;
    public static final int FLAG_CC_MODE = -1048568;
    public static final int FLAG_CURRENT_BIN_STATUS = -1048566;
    public static final int FLAG_DMV_STATUS = -15728640;
    public static final int FLAG_FRP_LOCK = -1048569;
    public static final int FLAG_KAP_STATUS = -1048563;
    public static final int FLAG_KERNEL_RP_VER = -1048575;
    public static final int FLAG_KIWI_LOCK = -1048570;
    public static final int FLAG_MDM_MODE = -1048567;
    public static final int FLAG_PKM_RO = -16777215;
    public static final int FLAG_PKM_TEXT = -16777216;
    public static final int FLAG_REACT_LOCK = -1048571;
    public static final int FLAG_RECOVERY_IMAGE = -1048561;
    public static final int FLAG_RESERVE_IMAGE1 = -1048558;
    public static final int FLAG_RESERVE_IMAGE2 = -1048557;
    public static final int FLAG_SEC_BOOT = -1048572;
    public static final int FLAG_SELINUX_STATUS = -16777214;
    public static final int FLAG_SYSSCOPE_STATUS = -14680064;
    public static final int FLAG_SYSTEM_IMAGE = -1048560;
    public static final int FLAG_SYSTEM_RP_VER = -1048574;
    public static final int FLAG_TEST_BIT = -1048573;
    public static final int FLAG_TIMA_VERSION = -14680062;
    public static final int FLAG_TRUSTBOOT_STATUS = -14680063;
    public static final int FLAG_WB_STATUS = -1048564;
    IIntegrityControlCheckCenter mService;

    public IntegrityControlCheckCenter(IIntegrityControlCheckCenter iIntegrityControlCheckCenter) {
        this.mService = iIntegrityControlCheckCenter;
    }

    public synchronized int getSecureData(int i) throws RemoteException {
        Log.d("ICCC", "Method getSecureData in IntegrityControlCheckCenter Class");
        if (this.mService == null) {
            return -1;
        }
        return this.mService.getSecureData(i);
    }

    public synchronized int getTrustedBootData() throws RemoteException {
        Log.d("ICCC", "Method getTrustedBootData in IntegrityControlCheckCenter Class");
        if (this.mService == null) {
            return -1;
        }
        return this.mService.getTrustedBootData();
    }

    public synchronized int setSecureData(int i, int i2) throws RemoteException {
        Log.d("ICCC", "Method setSecureData in IntegrityControlCheckCenter Class");
        if (this.mService == null) {
            return -1;
        }
        return this.mService.setSecureData(i, i2);
    }
}
