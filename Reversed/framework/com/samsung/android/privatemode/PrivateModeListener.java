package com.samsung.android.privatemode;

import android.os.RemoteException;
import com.samsung.android.privatemode.IPrivateModeClient.Stub;

public abstract class PrivateModeListener {
    private final IPrivateModeClient mClient = new C02281();

    class C02281 extends Stub {
        C02281() {
        }

        public void onStateChange(int i, int i2) throws RemoteException {
            PrivateModeListener.this.onStateChanged(i, i2);
        }
    }

    public IPrivateModeClient getClient() {
        return this.mClient;
    }

    public abstract void onStateChanged(int i, int i2);
}
