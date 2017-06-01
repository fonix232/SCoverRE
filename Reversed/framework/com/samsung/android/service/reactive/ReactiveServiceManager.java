package com.samsung.android.service.reactive;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.service.reactive.IReactiveService.Stub;
import com.samsung.android.smartface.SmartFaceManager;

public final class ReactiveServiceManager {
    public static final int FLAG_ACTIVATED = 1;
    public static final int FLAG_DEACTIVATED = 0;
    public static final int FLAG_DEACTIVATED_WITH_ACCOUNT = 3;
    public static final int FLAG_TRIGGERED = 2;
    public static final int FRP_FLAG = 2;
    public static final int FRP_SERIVCE_OPERATION_FAILED = -7;
    public static final int GOOGLE_FACTORY_RESET_PROTECTION_IS_SUPPORTED = 2;
    private static final int RC_VT_VALID_SIZE = 32;
    public static final int REACTIVATION_FLAG = 0;
    public static final int REACTIVE_SERVICE_EXCEPTION_ERROR = -10;
    public static final int REACTIVE_SERVICE_INVALID_ARGUMENTS = -8;
    public static final int REACTIVE_SERVICE_IS_NOT_EXIST = -9;
    public static final int REACTIVE_SERVICE_IS_NOT_SUPPORTED = 0;
    public static final int REACTIVE_SERVICE_OPERATION_FAILED = -6;
    public static final int REACTIVE_SERVICE_RETURN_FLAG_IS_NOT_EXIST = -3;
    public static final int REACTIVE_SERVICE_RETURN_NATIVE_ERROR = -1;
    public static final int REACTIVE_SERVICE_RETURN_NO_ERROR = 0;
    public static final int REACTIVE_SERVICE_RETURN_PERMISSION_DENIED = -5;
    public static final int REACTIVE_SERVICE_RETURN_STRING_IS_NOT_EXIST = -4;
    public static final int REACTIVE_SERVICE_RETURN_UNSUPPORTED_OPERATION = -2;
    private static final int RS_GOOGLE_NWD_SUPPORTED = 4;
    private static final int RS_IS_NOT_SUPPORTED = 0;
    private static final int RS_SAMSUNG_NWD_SUPPORTED = 2;
    private static final int RS_SAMSUNG_SWD_SUPPORTED = 1;
    public static final int SAMSUNG_GOOGLE_REACTIVE_SERVICES_ARE_SUPPORTED = 3;
    public static final int SAMSUNG_REACTIVE_SERVICE_IS_SUPPORTED = 1;
    public static final int SERVICE_FLAG = 1;
    private static final String TAG = "ReactiveServiceManager";
    private static final int USE_SAMSUNG_ACCOUNT = 0;
    private static final int USE_VERIFICATION_TOKEN = 1;
    private final Context mContext;
    private IReactiveService mService = Stub.asInterface(ServiceManager.getService("ReactiveService"));

    public ReactiveServiceManager(Context context) {
        this.mContext = context;
    }

    private String toHex(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b : bArr) {
            String str = SmartFaceManager.PAGE_MIDDLE + Integer.toHexString(b & 255);
            stringBuffer.append(str.substring(str.length() - 2));
        }
        return stringBuffer.toString();
    }

    public int disable(byte[] bArr) {
        if (bArr == null || bArr.length != 32) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int flag = this.mService.setFlag(0, 0, toHex(bArr));
            if (flag != 0) {
                Log.e(TAG, "disable() : error code[" + flag + "]");
            }
            return flag;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int disableWithAccountId(String str) {
        if (str == null) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int flag = this.mService.setFlag(0, 3, str);
            if (flag != 0) {
                Log.e(TAG, "disableWithAccountId() : error code[" + flag + "]");
            }
            return flag;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int enable(byte[] bArr) {
        if (bArr == null || bArr.length != 32) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int flag = this.mService.setFlag(0, 1, toHex(bArr));
            if (flag != 0) {
                Log.e(TAG, "enable() : error code[" + flag + "]");
            }
            return flag;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int getErrorCode() {
        try {
            return this.mService.getErrorCode();
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int getFlag(int i) {
        try {
            int flag = this.mService.getFlag(i);
            if (flag > 2 || flag < 0) {
                Log.e(TAG, "getFlag() : error code[" + flag + "]");
            }
            return flag;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public byte[] getRandom() {
        byte[] bArr = null;
        if (getServiceSupport() != 1) {
            Log.e(TAG, "Invalid operation.");
            return null;
        }
        int i;
        try {
            bArr = this.mService.getRandom();
            i = bArr == null ? -6 : 0;
        } catch (NullPointerException e) {
            i = -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            i = -10;
        }
        if (i < 0) {
            Log.e(TAG, "getRandom() : error code[" + i + "]");
        } else {
            Log.i(TAG, "Success of generate random numbers.");
        }
        return bArr;
    }

    public int getRawServiceValueForAtCommand() {
        try {
            return this.mService.getServiceSupport();
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int getServiceSupport() {
        try {
            int serviceSupport = this.mService.getServiceSupport();
            Log.i(TAG, "Supported : " + serviceSupport);
            switch (serviceSupport) {
                case 1:
                case 2:
                    return 1;
                case 4:
                    return 2;
                case 5:
                case 6:
                    return 3;
                default:
                    return 0;
            }
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int getStatus() {
        if (isConnected()) {
            int flag;
            int serviceSupport = getServiceSupport();
            if (serviceSupport == 1) {
                flag = getFlag(0);
            } else if (serviceSupport != 2) {
                return -3;
            } else {
                flag = getFlag(2);
            }
            if (flag == 2) {
                flag = 1;
            }
            return flag;
        }
        Log.e(TAG, "ReactiveService is not exist.");
        return -9;
    }

    public String getString() {
        try {
            return this.mService.getString();
        } catch (NullPointerException e) {
            Log.e(TAG, "getString() : Service is not exist.");
            return null;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public boolean isConnected() {
        return this.mService != null;
    }

    public int removeString() {
        try {
            int removeString = this.mService.removeString();
            if (removeString < 0) {
                Log.e(TAG, "removeString() : error code[" + removeString + "]");
            } else {
                Log.i(TAG, "removeString Success ");
            }
            return removeString;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public byte[] sessionAccept(byte[] bArr) {
        if (bArr == null) {
            Log.e(TAG, "SessionAccept() : Invalid argument");
            return null;
        }
        try {
            return this.mService.sessionAccept(bArr);
        } catch (NullPointerException e) {
            Log.e(TAG, "SessionAccpet() : Service is not exist.");
            return null;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public int sessionComplete(byte[] bArr) {
        if (bArr == null) {
            return -8;
        }
        try {
            int sessionComplete = this.mService.sessionComplete(bArr);
            if (sessionComplete != 0) {
                Log.e(TAG, "sessionComplete() : error code[" + sessionComplete + "]");
            }
            return sessionComplete;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int setFlag(int i, int i2, String str) {
        Log.e(TAG, "setFlag() - No longer used API");
        return -2;
    }

    public int setString(String str) {
        if (str == null) {
            return -8;
        }
        try {
            int string = this.mService.setString(str);
            if (string < 0) {
                Log.e(TAG, "setString() : error code[" + string + "]");
            } else {
                Log.i(TAG, "setString() : " + string + " characters are saved.");
            }
            return string;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int verify(byte[] bArr) {
        if (bArr == null || bArr.length != 32) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int verify = this.mService.verify(toHex(bArr), 1);
            if (verify < 0) {
                Log.e(TAG, "verify() : error code[" + verify + "]");
            } else {
                Log.i(TAG, "Verification success");
            }
            return verify;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }

    public int verifyWithAccountId(String str) {
        if (str == null) {
            return -8;
        }
        if (getServiceSupport() != 1) {
            return -2;
        }
        try {
            int verify = this.mService.verify(str, 0);
            if (verify < 0) {
                Log.e(TAG, "verifyWithAccountId() : error code[" + verify + "]");
            } else {
                Log.i(TAG, "Verification with id, success");
            }
            return verify;
        } catch (NullPointerException e) {
            return -9;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return -10;
        }
    }
}
