package com.samsung.android.allaroundsensing;

import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.util.SemLog;

public final class SemAllAroundSensingManager {
    private static float RETURN_ERROR = -1.0f;
    private static int RETURN_ERROR_INT = -1;
    private static final String TAG = "SemAllAroundSensingManager";
    final ISemAllAroundSensingManager mService;

    public SemAllAroundSensingManager(ISemAllAroundSensingManager iSemAllAroundSensingManager) {
        if (iSemAllAroundSensingManager == null) {
            SemLog.i(TAG, "In Constructor Stub-Service(ISemAllAroundSensingManager) is null");
        }
        this.mService = iSemAllAroundSensingManager;
    }

    private void onError(Exception exception) {
        Log.m32e(TAG, "Error SemAllAroundSensingManager", exception);
    }

    public float getBrightnessValue() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getBrightnessValue();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public boolean getBrightnessValueEnable() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.getBrightnessValueEnable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getPlatformBrightnessValue() {
        if (this.mService == null) {
            return RETURN_ERROR_INT;
        }
        try {
            return this.mService.getPlatformBrightnessValue();
        } catch (RemoteException e) {
            return RETURN_ERROR_INT;
        }
    }

    public void setBrightnessValue(float f) {
        try {
            this.mService.setBrightnessValue(f);
        } catch (Exception e) {
            onError(e);
        }
    }

    public void setBrightnessValue(long j) {
    }

    public void setBrightnessValueEnabled(boolean z) {
        try {
            this.mService.setBrightnessValueEnabled(z);
        } catch (Exception e) {
            onError(e);
        }
    }

    public void setPlatformBrightnessValue(int i) {
        try {
            this.mService.setPlatformBrightnessValue(i);
        } catch (Exception e) {
            onError(e);
        }
    }
}
