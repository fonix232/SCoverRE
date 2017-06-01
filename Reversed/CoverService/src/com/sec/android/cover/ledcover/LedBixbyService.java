package com.sec.android.cover.ledcover;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.ledcover.ILedBixbyService.Stub;

public class LedBixbyService extends Service {
    private static final String TAG = LedBixbyService.class.getSimpleName();
    private final Stub mBixbyBinder = new C00171();
    CoverExecutiveObservator mCoverExecutiveObservator;

    class C00171 extends Stub {
        C00171() {
        }

        public void setState(int state) throws RemoteException {
            Log.d(LedBixbyService.TAG, "setState state:" + state);
            LedBixbyService.this.mCoverExecutiveObservator.getCoverUpdateMonitor().setBixbyState(state);
        }
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        this.mCoverExecutiveObservator = CoverExecutiveObservator.getInstance(this);
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.mCoverExecutiveObservator.getCoverUpdateMonitor().setBixbyState(-1);
        super.onDestroy();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return this.mBixbyBinder;
    }
}
