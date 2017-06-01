package com.sec.android.cover.lib.sviewcoverservicereflector;

import android.os.RemoteException;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ISViewCoverBaseService.Stub;

public class SViewCoverServiceReflector {
    private final Stub mStubBinder = new Stub() {
        public boolean isCoverViewShowing() throws RemoteException {
            return SViewCoverServiceReflector.this.mStubListener.isCoverViewShowing();
        }

        public int onCoverAppCovered(boolean covered) throws RemoteException {
            return SViewCoverServiceReflector.this.mStubListener.onCoverAppCovered(covered);
        }

        public void onSViewCoverHide() throws RemoteException {
            SViewCoverServiceReflector.this.mStubListener.onSViewCoverHide();
        }

        public void onSViewCoverShow() throws RemoteException {
            SViewCoverServiceReflector.this.mStubListener.onSViewCoverShow();
        }

        public void onSystemReady() throws RemoteException {
            SViewCoverServiceReflector.this.mStubListener.onSystemReady();
        }

        public void updateCoverState(CoverState state) throws RemoteException {
            SViewCoverServiceReflector.this.mStubListener.updateCoverState(state);
        }
    };
    private SViewCoverBaseServiceStubListener mStubListener;

    public interface SViewCoverBaseServiceStubListener {
        boolean isCoverViewShowing();

        int onCoverAppCovered(boolean z);

        void onSViewCoverHide();

        void onSViewCoverShow();

        void onSystemReady();

        void updateCoverState(Object obj);
    }

    public SViewCoverServiceReflector(SViewCoverBaseServiceStubListener stubListener) {
        if (stubListener == null) {
            throw new IllegalArgumentException();
        }
        this.mStubListener = stubListener;
    }

    public Object getStubBinder() {
        return this.mStubBinder;
    }
}
