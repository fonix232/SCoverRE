package com.samsung.android.knox.ccm;

import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.knox.keystore.IClientCertificateManager;
import com.samsung.android.knox.keystore.IClientCertificateManager.Stub;

public class SemClientCertificateManager {
    private static final String TAG = "SemClientCertificateManager";
    private IClientCertificateManager mCCMService = Stub.asInterface(ServiceManager.getService("downloadableccm_svc"));

    public SemClientCertificateManager() {
        Log.d(TAG, "CCMServiceManager");
        String str = "knox_ccm_policy";
        if (this.mCCMService == null) {
            Log.e(TAG, "failed to get CCM Service");
        }
    }

    public boolean setDefaultClientCertificateManagerProfile() {
        Log.d(TAG, "setDefaultClientCertificateManagerProfile");
        if (this.mCCMService == null) {
            Log.e(TAG, "failed to get CCM Service");
            return false;
        }
        try {
            return this.mCCMService.setDefaultCCMProfile();
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException : " + e.getMessage());
            return false;
        } catch (Throwable e2) {
            Log.e(TAG, "Exception : " + e2.getMessage());
            return false;
        }
    }
}
