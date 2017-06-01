package com.samsung.android.displaysolution;

import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.util.SemLog;

public final class SemDisplaySolutionManager {
    private static long RETURN_ERROR = -1;
    private static float RETURN_ERROR_F = -1.0f;
    public static final int SUPPORT_CHANGABLE_NORMAL_AUTO_BRIGHTNESS = 2;
    public static final int SUPPORT_CHANGABLE_NUMBER_AUTO_BRIGHTNESS = 1;
    public static final int SUPPORT_ONLY_MANUAL_BRIGHTNESS = 0;
    public static final int SUPPORT_PERSONAL_AUTOBRIGHTNESS_CONTROL = 3;
    public static final int SUPPORT_PERSONAL_AUTOBRIGHTNESS_CONTROL_V3 = 4;
    private static final String TAG = "SemDisplaySolutionManager";
    final ISemDisplaySolutionManager mService;

    public SemDisplaySolutionManager(ISemDisplaySolutionManager iSemDisplaySolutionManager) {
        if (iSemDisplaySolutionManager == null) {
            SemLog.m20i(TAG, "In Constructor Stub-Service(ISemDisplaySolutionManager) is null");
        }
        this.mService = iSemDisplaySolutionManager;
    }

    private void onError(Exception exception) {
        Log.e(TAG, "Error SemDisplaySolutionManager", exception);
    }

    public float getAlphaBlendingValue() {
        if (this.mService == null) {
            return RETURN_ERROR_F;
        }
        try {
            return this.mService.getAlphaBlendingValue();
        } catch (RemoteException e) {
            return RETURN_ERROR_F;
        }
    }

    public boolean getGalleryModeEnable() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.getGalleryModeEnable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean getVideoModeEnable() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.getVideoModeEnable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isMdnieScenarioControlServiceEnabled() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isMdnieScenarioControlServiceEnabled();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setGalleryModeEnable(boolean z) {
        try {
            this.mService.setGalleryModeEnable(z);
        } catch (Exception e) {
            onError(e);
        }
    }

    public void setMdnieScenarioControlServiceEnable(boolean z) {
        try {
            this.mService.setMdnieScenarioControlServiceEnable(z);
        } catch (Exception e) {
            onError(e);
        }
    }

    public void setMultipleScreenBrightness(String str) {
        try {
            this.mService.setMultipleScreenBrightness(str);
        } catch (Exception e) {
            onError(e);
        }
    }

    public void setScreenBrightnessForPreview(int i) {
        try {
            this.mService.setScreenBrightnessForPreview(i);
        } catch (Exception e) {
            onError(e);
        }
    }

    public void setVideoModeEnable(boolean z) {
        try {
            this.mService.setVideoModeEnable(z);
        } catch (Exception e) {
            onError(e);
        }
    }
}
