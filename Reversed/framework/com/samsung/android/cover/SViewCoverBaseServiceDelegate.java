package com.samsung.android.cover;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Log;
import android.util.Pair;
import com.samsung.android.cover.ISViewCoverBaseService.Stub;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SViewCoverBaseServiceDelegate {
    private static final boolean DEBUG = true;
    private static final boolean SAFE_DEBUG = true;
    private static final String SVIEWCOVERBASE_CLASS = "com.android.systemui.cover.SViewCoverService";
    private static final String SVIEWCOVERBASE_PACKAGE = "com.android.systemui";
    private static final int SVIEWCOVER_UPDATE_COVERSTATE = 1;
    private static final String TAG = "SViewCoverBaseServiceDelegate";
    private ReentrantLock mCallLock = new ReentrantLock(true);
    private Context mContext;
    private boolean mIsBound = false;
    private final ArrayList<Pair<Integer, Object>> mPendingCommand = new ArrayList();
    protected SViewCoverBaseServiceWrapper mSViewCoverBaseService;
    private SViewCoverBaseState mSViewCoverBaseState = new SViewCoverBaseState();
    private final ServiceConnection mSViewCoverConnection = new C00491();
    private Intent mSViewCoverIntent;

    class C00491 implements ServiceConnection {
        C00491() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(SViewCoverBaseServiceDelegate.TAG, "*** SViewCoverBase service connected");
            SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService = new SViewCoverBaseServiceWrapper(Stub.asInterface(iBinder));
            if (SViewCoverBaseServiceDelegate.this.mSViewCoverBaseState.systemIsReady) {
                SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService.onSystemReady();
            }
            int size = SViewCoverBaseServiceDelegate.this.mPendingCommand.size();
            for (int i = 0; i < size; i++) {
                Pair pair = (Pair) SViewCoverBaseServiceDelegate.this.mPendingCommand.get(i);
                switch (((Integer) pair.first).intValue()) {
                    case 1:
                        SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService.updateCoverState((CoverState) pair.second);
                        break;
                    default:
                        break;
                }
            }
            SViewCoverBaseServiceDelegate.this.mPendingCommand.clear();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(SViewCoverBaseServiceDelegate.TAG, "*** SViewCoverBase service disconnected");
            SViewCoverBaseServiceDelegate.this.mSViewCoverBaseService = null;
        }
    }

    static final class SViewCoverBaseState {
        boolean systemIsReady;

        SViewCoverBaseState() {
        }
    }

    public SViewCoverBaseServiceDelegate(Context context) {
        this.mContext = context;
        this.mSViewCoverIntent = new Intent();
        this.mSViewCoverIntent.setClassName("com.android.systemui", SVIEWCOVERBASE_CLASS);
    }

    public boolean isCoverViewShowing() {
        return this.mSViewCoverBaseService != null ? this.mSViewCoverBaseService.isCoverViewShowing() : false;
    }

    public void onBindSViewCoverService() {
        this.mCallLock.lock();
        try {
            if (this.mIsBound) {
                Log.d(TAG, "*** SViewCoverBase : already started");
            } else if (this.mContext.bindServiceAsUser(this.mSViewCoverIntent, this.mSViewCoverConnection, 1, UserHandle.OWNER)) {
                this.mIsBound = true;
                Log.d(TAG, "*** SViewCoverBase : started");
            } else {
                this.mIsBound = false;
                Log.d(TAG, "*** SViewCoverBase : can't bind to com.android.systemui.cover.SViewCoverService");
            }
            this.mCallLock.unlock();
        } catch (Throwable th) {
            this.mCallLock.unlock();
        }
    }

    public int onCoverAppCovered(boolean z) {
        return this.mSViewCoverBaseService != null ? this.mSViewCoverBaseService.onCoverAppCovered(z) : 0;
    }

    public void onSViewCoverHide() {
        if (this.mSViewCoverBaseService != null) {
            this.mSViewCoverBaseService.onSViewCoverHide();
        }
    }

    public void onSViewCoverShow() {
        if (this.mSViewCoverBaseService != null) {
            this.mSViewCoverBaseService.onSViewCoverShow();
        }
    }

    public void onSystemReady() {
        if (this.mSViewCoverBaseService != null) {
            this.mSViewCoverBaseService.onSystemReady();
        } else {
            this.mSViewCoverBaseState.systemIsReady = true;
        }
    }

    public void onUnbindSViewCoverService() {
        this.mCallLock.lock();
        try {
            if (this.mIsBound) {
                this.mContext.unbindService(this.mSViewCoverConnection);
                this.mSViewCoverBaseService = null;
                this.mIsBound = false;
                Log.d(TAG, "*** SViewCoverBase : unbind");
            } else {
                Log.d(TAG, "*** SViewCoverBase : can't unbind. It already unbound");
            }
            this.mCallLock.unlock();
        } catch (Throwable th) {
            this.mCallLock.unlock();
        }
    }

    public void updateCoverState(CoverState coverState) {
        this.mCallLock.lock();
        try {
            SViewCoverBaseServiceWrapper sViewCoverBaseServiceWrapper = this.mSViewCoverBaseService;
            if (sViewCoverBaseServiceWrapper != null) {
                Log.d(TAG, "updateCoverState : service exits");
                sViewCoverBaseServiceWrapper.updateCoverState(coverState);
            } else if (this.mIsBound) {
                Log.d(TAG, "updateCoverState : service is null but bound");
                this.mPendingCommand.add(new Pair(Integer.valueOf(1), coverState));
            } else {
                Log.d(TAG, "updateCoverState : service is null and not bound");
            }
            this.mCallLock.unlock();
        } catch (Throwable th) {
            this.mCallLock.unlock();
        }
    }
}
