package com.sec.smartcard.openssl;

import android.sec.enterprise.ClientCertificateManager;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.util.Log;
import com.android.org.conscrypt.OpenSSLEngine;
import java.security.InvalidKeyException;
import java.security.PrivateKey;

public class OpenSSLHelper {
    private static final String FUNCTION_LIST_NAME = "TZ_CCM_C_GetFunctionList";
    private static final String LIBRARY_NAME = "libtlc_tz_ccm.so";
    static final String TAG = "OpenSSLHelper";
    private PrivateKey pkey = null;

    static {
        System.loadLibrary("secopenssl_engine");
    }

    public native int deregisterEngineKeychain();

    public boolean deregister_engine() {
        Log.d(TAG, "deregister_engine function");
        if (new OpenSSLHelper().deregisterEngineKeychain() != 0) {
            return false;
        }
        Log.e(TAG, "DeRegister engine success");
        return true;
    }

    public PrivateKey getPrivateKey(String str) {
        Log.d(TAG, "getPrivateKey function");
        if (this.pkey == null) {
            OpenSSLEngine customInstance = OpenSSLEngine.getCustomInstance("secpkcs11");
            if (customInstance != null) {
                try {
                    this.pkey = customInstance.getPrivateKeyById(str);
                } catch (InvalidKeyException e) {
                    Log.d(TAG, "InvalidKeyException");
                }
            }
        }
        return this.pkey;
    }

    protected long getSlotID(String str) {
        Log.d(TAG, "getSlotID function");
        ClientCertificateManager clientCertificateManager = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
        return clientCertificateManager != null ? clientCertificateManager.getSlotIdForCaller(str) : -1;
    }

    public boolean registerEngine(String str) {
        Log.d(TAG, "registerEngine function");
        long slotID = getSlotID(str);
        if (0 > slotID) {
            Log.d(TAG, "registerEngine - getSlotID returned invalid slotid = " + slotID);
            return false;
        } else if (new OpenSSLHelper().registerEngineKeychain(LIBRARY_NAME, FUNCTION_LIST_NAME, slotID) != 0) {
            return false;
        } else {
            Log.e(TAG, "Register engine success");
            return true;
        }
    }

    public native int registerEngineKeychain(String str, String str2, long j);
}
