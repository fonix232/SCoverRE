package com.samsung.android.codecsolution;

import com.samsung.android.util.SemLog;

public class SemCodecSolutionManager {
    private static final String TAG = "CodecSolutionManager";
    private ICodecSolutionManagerService mService = null;

    public SemCodecSolutionManager(ICodecSolutionManagerService iCodecSolutionManagerService) {
        if (iCodecSolutionManagerService == null) {
            SemLog.i(TAG, "In Constructor Stub-Service(ICodecSolutionManagerService) is null");
        }
        this.mService = iCodecSolutionManagerService;
    }

    public int checkblackbarstatus() {
        try {
            return this.mService.checkblackbarstatus();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void debug() {
        try {
            this.mService.debug();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public int getSmartFittingMode() {
        try {
            return this.mService.getSmartFittingMode();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getWhiteListStatus() {
        try {
            return this.mService.getWhiteListStatus();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void hideSmartFittingButton() {
        try {
            this.mService.hideSmartFittingButton();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void reportMediaStatisticsEvent(String str) {
        try {
            this.mService.reportMediaStatisticsEvent(str);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setMetaData(int i, int i2, int i3, int i4) {
        try {
            this.mService.setMhdrMetaData(i, i2, i3, i4);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setSecVideoUseSmartFitting(int i) {
        try {
            this.mService.setSecVideoUseSmartFitting(i);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setSmartFittingMode(int i) {
        try {
            this.mService.setSmartFittingMode(i);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setWhiteListStatus(int i) {
        try {
            this.mService.setWhiteListStatus(i);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void showSmartFittingButton() {
        try {
            this.mService.showSmartFittingButton();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void startMhdrService(int i, String str) {
        try {
            this.mService.startMhdrService(i, str, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void startSmartFittingService() {
        try {
            this.mService.startSmartFittingService();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void stopMhdrService() {
        try {
            this.mService.stopMhdrService();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void stopSmartFittingService() {
        try {
            this.mService.stopSmartFittingService();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void updateblackbarstatus(int i) {
        try {
            this.mService.updateblackbarstatus(i);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
