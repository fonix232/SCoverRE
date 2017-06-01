package com.samsung.android.cover;

import android.os.IBinder;
import android.util.Slog;

public class SViewCoverBaseServiceWrapper implements ISViewCoverBaseService {
    private String TAG = "SViewCoverBaseServiceWrapper";
    private ISViewCoverBaseService mService;

    public SViewCoverBaseServiceWrapper(ISViewCoverBaseService iSViewCoverBaseService) {
        this.mService = iSViewCoverBaseService;
    }

    public IBinder asBinder() {
        return this.mService.asBinder();
    }

    public boolean isCoverViewShowing() {
        boolean z = false;
        try {
            return this.mService.isCoverViewShowing();
        } catch (Throwable e) {
            Slog.w(this.TAG, "Remote Exception", e);
            return z;
        } catch (Throwable th) {
            return z;
        }
    }

    public int onCoverAppCovered(boolean z) {
        try {
            return this.mService.onCoverAppCovered(z);
        } catch (Throwable e) {
            Slog.w(this.TAG, "Remote Exception", e);
            return 0;
        }
    }

    public void onSViewCoverHide() {
        try {
            this.mService.onSViewCoverHide();
        } catch (Throwable e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onSViewCoverShow() {
        try {
            this.mService.onSViewCoverShow();
        } catch (Throwable e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onSystemReady() {
        try {
            this.mService.onSystemReady();
        } catch (Throwable e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void updateCoverState(CoverState coverState) {
        try {
            this.mService.updateCoverState(coverState);
        } catch (Throwable e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }
}
