package com.samsung.android.service.DeviceRootKeyService;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.service.DeviceRootKeyService.IDeviceRootKeyService.Stub;

public final class DeviceRootKeyServiceManager {
    public static final int ERR_SERVICE_ERROR = -10000;
    public static final int KEY_TYPE_EC = 4;
    public static final int KEY_TYPE_RSA = 1;
    public static final int KEY_TYPE_SYMM = 2;
    public static final int NO_ERROR = 0;
    private static final String TAG = "DeviceRootKeyServiceManager";
    private final Context mContext;
    private IDeviceRootKeyService mService = Stub.asInterface(ServiceManager.getService("DeviceRootKeyService"));

    public DeviceRootKeyServiceManager(Context context) {
        this.mContext = context;
        Log.i(TAG, this.mContext.getPackageName() + " connects to DeviceRootKeyService.");
    }

    public byte[] createServiceKeySession(String str, int i, Tlv tlv) {
        Log.i(TAG, "createServiceKeySession() is called.");
        try {
            return this.mService.createServiceKeySession(str, i, tlv);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return null;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public String getDeviceRootKeyUID(int i) {
        Log.i(TAG, "getDeviceRootKeyUID() is called.");
        try {
            return this.mService.getDeviceRootKeyUID(i);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return null;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public boolean isAliveDeviceRootKeyService() {
        return this.mService != null;
    }

    public boolean isExistDeviceRootKey(int i) {
        Log.i(TAG, "isExistDeviceRootKey() is called.");
        try {
            return this.mService.isExistDeviceRootKey(i);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return false;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public int releaseServiceKeySession() {
        Log.i(TAG, "releaseServiceKeySession() is called.");
        try {
            return this.mService.releaseServiceKeySession();
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERR_SERVICE_ERROR;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERR_SERVICE_ERROR;
        }
    }

    public int setDeviceRootKey(byte[] bArr) {
        Log.i(TAG, "setDeviceRootKey() is called.");
        try {
            return this.mService.setDeviceRootKey(bArr);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERR_SERVICE_ERROR;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERR_SERVICE_ERROR;
        }
    }
}
