package com.samsung.android.knox.tima;

import android.os.ServiceManager;
import android.service.tima.ITimaService;
import android.service.tima.ITimaService.Stub;
import android.util.Log;

public class SemTimaServiceManager {
    private static final String TAG = "SemTimaServiceManager";
    private ITimaService mTimaService = Stub.asInterface(ServiceManager.getService("tima"));

    public SemTimaServiceManager() {
        Log.d(TAG, TAG);
        if (this.mTimaService == null) {
            Log.e(TAG, "failed to get Tima Service");
        }
    }

    public String getTimaVersion() {
        Log.d(TAG, "getTimaVersion");
        if (this.mTimaService == null) {
            Log.e(TAG, "failed to et Tima Service");
            return "";
        }
        try {
            return this.mTimaService.getTimaVersion();
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException : " + e.getMessage());
            return "";
        } catch (Throwable e2) {
            Log.e(TAG, "Exception : " + e2.getMessage());
            return "";
        }
    }
}
