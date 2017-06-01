package com.samsung.android.service.EngineeringMode;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.service.EngineeringMode.IEngineeringModeService.Stub;

public final class EngineeringModeManager {
    public static final int ALLOWED = 1;
    public static final int DISABLE = 1;
    public static final int ENABLE = 0;
    public static final int ENG_KERNEL = 0;
    public static final byte[] ERRORBYTE_EM_SERVICE = new byte[]{(byte) -1};
    public static final int ERROR_EM_SERVICE = -1000;
    public static final int MODE_CP_DEBUG = 2;
    public static final int MODE_CUST_KERNEL = 3;
    public static final int MODE_ENG_KERNEL = 0;
    public static final int MODE_KNOX_TEST = 4;
    public static final int MODE_TEST_ENV = 1;
    public static final int MODE_USB_DEBUG = 1;
    public static final int NOK = 0;
    public static final int NOT_ALLOWED = 0;
    public static final int OK = 1;
    private static final String TAG = "EngineeringModeManager";
    public static final int USB_DEBUG = 1;
    public static final int USB_DEBUG_ALLOWED = 1;
    public static final int USB_DEBUG_NOT_ALLOWED = 0;
    private final Context mContext;
    private IEngineeringModeService mService = Stub.asInterface(ServiceManager.getService("EngineeringModeService"));

    public EngineeringModeManager(Context context) {
        this.mContext = context;
        Log.i(TAG, this.mContext.getPackageName() + " connects to EngineeringModeService.");
    }

    public byte[] getID() {
        Log.i(TAG, "getID() is called.");
        try {
            return this.mService.getID();
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        }
    }

    public int getNumOfModes() {
        Log.i(TAG, "getNumOfModes() is called.");
        try {
            return this.mService.getNumOfModes();
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERROR_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERROR_EM_SERVICE;
        }
    }

    public byte[] getRequestMsg(String str, String str2, byte[] bArr) {
        Log.i(TAG, "getRequestMsg() is called.");
        try {
            return this.mService.getRequestMsg(str, str2, bArr, 0);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        }
    }

    public byte[] getRequestMsg(String str, String str2, byte[] bArr, int i) {
        Log.i(TAG, "getRequestMsg() is called.");
        try {
            return this.mService.getRequestMsg(str, str2, bArr, i);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        }
    }

    public int getStatus(int i) {
        Log.i(TAG, "getStatus() is called.");
        try {
            return this.mService.getStatus(i);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERROR_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERROR_EM_SERVICE;
        }
    }

    public int installToken(byte[] bArr) {
        Log.i(TAG, "installToken() is called.");
        try {
            return this.mService.installToken(bArr);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERROR_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERROR_EM_SERVICE;
        }
    }

    public boolean isConnected() {
        return this.mService != null;
    }

    public int isTokenInstalled() {
        Log.i(TAG, "isTokenInstalled() is called.");
        try {
            return this.mService.isTokenInstalled();
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERROR_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERROR_EM_SERVICE;
        }
    }

    public int removeToken() {
        Log.i(TAG, "removeToken() is called.");
        try {
            return this.mService.removeToken();
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERROR_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERROR_EM_SERVICE;
        }
    }

    public int sendFuseCmd() {
        Log.i(TAG, "sendFuseCmd() is called.");
        try {
            return this.mService.sendFuseCmd();
        } catch (Throwable e) {
            Log.e(TAG, "Failed to connect service.");
            e.printStackTrace();
            return ERROR_EM_SERVICE;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return ERROR_EM_SERVICE;
        }
    }
}
