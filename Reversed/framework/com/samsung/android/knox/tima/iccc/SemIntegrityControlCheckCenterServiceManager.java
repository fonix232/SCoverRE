package com.samsung.android.knox.tima.iccc;

import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.iccc.IIntegrityControlCheckCenter;
import com.samsung.android.iccc.IIntegrityControlCheckCenter.Stub;

public class SemIntegrityControlCheckCenterServiceManager {
    private static final String TAG = "SemIntegrityControlCheckCenterServiceManager";
    public static final int TYPE_BOOT_LOADER_AFW_VALUE = -1048565;
    public static final int TYPE_BOOT_LOADER_CACHE_IMAGE_STATUS = -1048559;
    public static final int TYPE_BOOT_LOADER_CHECK_SKIP_ROLLBACK_PREVENTION = -1048573;
    public static final int TYPE_BOOT_LOADER_CURRENT_BINARY_STATUS = -1048566;
    public static final int TYPE_BOOT_LOADER_FACTORY_RESET_PROTECTION_LOCK_STATUS = -1048569;
    public static final int TYPE_BOOT_LOADER_FLASH_CUSTOM_BINARY_ALLOWED = -1048568;
    public static final int TYPE_BOOT_LOADER_FLASH_CUSTOM_KERNEL_ALLOWED = -1048570;
    public static final int TYPE_BOOT_LOADER_IMAGE_STATUS = -1048562;
    public static final int TYPE_BOOT_LOADER_KERNEL_ROLLBACK_PREVENTION_VERSION = -1048575;
    public static final int TYPE_BOOT_LOADER_KNOX_ACTIVE_PROTECTION_STATUS = -1048563;
    public static final int TYPE_BOOT_LOADER_MDM_RECOVERY_ALLOWED = -1048567;
    public static final int TYPE_BOOT_LOADER_REACTIVATION_LOCK_STATUS = -1048571;
    public static final int TYPE_BOOT_LOADER_RECOVERY_IMAGE_STATUS = -1048561;
    public static final int TYPE_BOOT_LOADER_ROLLBACK_PREVENTION_VERSION = -1048576;
    public static final int TYPE_BOOT_LOADER_SECURE_BOOT_STATUS = -1048572;
    public static final int TYPE_BOOT_LOADER_SYSTEM_IMAGE_STATUS = -1048560;
    public static final int TYPE_BOOT_LOADER_SYSTEM_ROLLBACK_PREVENTION_VERSION = -1048574;
    public static final int TYPE_BOOT_LOADER_WARRANTY_BIT = -1048564;
    public static final int TYPE_IMAGE_RESERVED_1 = -1048558;
    public static final int TYPE_IMAGE_RESERVED_2 = -1048557;
    public static final int TYPE_KERNEL_PARAMETERS_DEVICE_MAPPER_VERITY_STATUS = -15728640;
    public static final int TYPE_SYSTEM_PARAMETERS_SYSTEM_SECURITY_DIAGNOSIS_STATUS = -14680064;
    public static final int TYPE_SYSTEM_PARAMETERS_TRUST_BOOT_STATUS = -14680063;
    public static final int TYPE_TIMA_VERSION = -14680062;
    public static final int TYPE_TRUST_APPLICATION_PERIODIC_KERNEL_MONITORING_READONLY = -16777215;
    public static final int TYPE_TRUST_APPLICATION_PERIODIC_KERNEL_MONITORING_TEXT = -16777216;
    public static final int TYPE_TRUST_APPLICATION_SECURITY_ENABLED_LINUX_STATUS = -16777214;
    private IIntegrityControlCheckCenter mIcccService = Stub.asInterface(ServiceManager.getService("iccc"));

    public SemIntegrityControlCheckCenterServiceManager() {
        Log.d(TAG, TAG);
        if (this.mIcccService == null) {
            Log.e(TAG, "failed to get Iccc Service");
        }
    }

    public int getSecureData(int i) {
        Log.d(TAG, "getSecureData");
        if (this.mIcccService == null) {
            Log.e(TAG, "failed to get Iccc Service");
            return -1;
        }
        try {
            return this.mIcccService.getSecureData(i);
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException : " + e.getMessage());
            return -1;
        } catch (Throwable e2) {
            Log.e(TAG, "Exception : " + e2.getMessage());
            return -1;
        }
    }
}
