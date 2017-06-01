package com.samsung.android.hardware.display;

import android.os.RemoteException;
import android.util.Slog;

public final class SemMdnieManager {
    public static final int CONTENT_MODE_BROWSER = 8;
    public static final int CONTENT_MODE_CAMERA = 4;
    public static final int CONTENT_MODE_DMB = 20;
    public static final int CONTENT_MODE_EBOOK = 9;
    public static final int CONTENT_MODE_GALLERY = 6;
    public static final int CONTENT_MODE_GAME_HIGH = 13;
    public static final int CONTENT_MODE_GAME_LOW = 11;
    public static final int CONTENT_MODE_GAME_MID = 12;
    public static final int CONTENT_MODE_UI = 0;
    public static final int CONTENT_MODE_VIDEO = 1;
    public static final int CONTENT_MODE_VIDEO_ENHANCER = 14;
    public static final int CONTENT_MODE_VIDEO_ENHANCER_2 = 15;
    public static final int MDNIE_SUPPORT_BLUE_FILTER = 4096;
    public static final int MDNIE_SUPPORT_COLOR_ADJUSTMENT = 2048;
    public static final int MDNIE_SUPPORT_CONTENT_GAME_MODE = 2;
    public static final int MDNIE_SUPPORT_CONTENT_MODE = 1;
    public static final int MDNIE_SUPPORT_CONTENT_VIDEO_ENGANCE_MODE = 4;
    public static final int MDNIE_SUPPORT_GRAYSCALE = 512;
    public static final int MDNIE_SUPPORT_HDR = 16384;
    public static final int MDNIE_SUPPORT_HMT = 8192;
    public static final int MDNIE_SUPPORT_LIGHT_NOTIFICATION = 32768;
    public static final int MDNIE_SUPPORT_NEGATIVE = 256;
    public static final int MDNIE_SUPPORT_READING_MODE = 32;
    public static final int MDNIE_SUPPORT_SCREENCURTAIN = 1024;
    public static final int MDNIE_SUPPORT_SCREEN_MODE = 16;
    private static int RETURN_ERROR = -1;
    public static final int SCREEN_MODE_ADAPTIVE = 4;
    public static final int SCREEN_MODE_AMOLED_CINEMA = 0;
    public static final int SCREEN_MODE_AMOLED_PHOTO = 1;
    public static final int SCREEN_MODE_BASIC = 2;
    public static final int SCREEN_MODE_READING = 5;
    private static final String TAG = "SemMdnieManager";
    final ISemMdnieManager mService;

    public SemMdnieManager(ISemMdnieManager iSemMdnieManager) {
        if (iSemMdnieManager == null) {
            Slog.i(TAG, "In Constructor Stub-Service(ISemMdnieManager) is null");
        }
        this.mService = iSemMdnieManager;
    }

    public int getContentMode() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getContentMode();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public int getCurrentPocIndex() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getCurrentPocIndex();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public int getPocSettingValue() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getPocSettingValue();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public int getScreenMode() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getScreenMode();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public int[] getSupportedContentMode() {
        int[] iArr = new int[0];
        if (this.mService == null) {
            return iArr;
        }
        try {
            return this.mService.getSupportedContentMode();
        } catch (RemoteException e) {
            return iArr;
        }
    }

    public int[] getSupportedScreenMode() {
        int[] iArr = new int[0];
        if (this.mService == null) {
            return iArr;
        }
        try {
            return this.mService.getSupportedScreenMode();
        } catch (RemoteException e) {
            return iArr;
        }
    }

    public boolean isContentModeSupported() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isContentModeSupported();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isMdnieFisrtUsed() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isMdnieFisrtUsed();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isMdniePocFused() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isMdniePocFused();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isMdniePocSupported() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isMdniePocSupported();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isScreenModeSupported() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isScreenModeSupported();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setAmoledACL(int i) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setAmoledACL(i);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setContentMode(int i) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setContentMode(i);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setLightNotificationMode(boolean z) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setLightNotificationMode(z);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setNightMode(boolean z, int i) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setNightMode(z, i);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setPocCancel() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setPocCancel();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setPocSetting(int i) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setPocSetting(i);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setScreenMode(int i) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setScreenMode(i);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setmDNIeAccessibilityMode(int i, boolean z) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setmDNIeAccessibilityMode(i, z);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setmDNIeColorBlind(boolean z, int[] iArr) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setmDNIeColorBlind(z, iArr);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setmDNIeEmergencyMode(boolean z) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setmDNIeEmergencyMode(z);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setmDNIeNegative(boolean z) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setmDNIeNegative(z);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setmDNIeScreenCurtain(boolean z) {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.setmDNIeScreenCurtain(z);
        } catch (RemoteException e) {
            return false;
        }
    }
}
