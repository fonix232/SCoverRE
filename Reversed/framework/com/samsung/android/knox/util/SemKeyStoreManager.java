package com.samsung.android.knox.util;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.samsung.android.knox.util.ISemKeyStoreService.Stub;

public class SemKeyStoreManager {
    public static final int KEYSTORE_STATUS_LOCKED = 2;
    public static final int KEYSTORE_STATUS_UNINITIALIZED = 3;
    public static final int KEYSTORE_STATUS_UNKNOWN = 0;
    public static final int KEYSTORE_STATUS_UNLOCKED = 1;
    private ISemKeyStoreService mRemoteServiceKeystore;

    private SemKeyStoreManager(IBinder iBinder) {
        this.mRemoteServiceKeystore = Stub.asInterface(iBinder);
    }

    public static SemKeyStoreManager getInstance() {
        return new SemKeyStoreManager(ServiceManager.getService("emailksproxy"));
    }

    public int getKeystoreStatus() throws RemoteException {
        return this.mRemoteServiceKeystore.getKeystoreStatus();
    }

    public void grantAccess(int i, String str) throws RemoteException {
        this.mRemoteServiceKeystore.grantAccessForAKS(i, str);
    }

    public boolean hasAlias(String str, boolean z) throws RemoteException {
        return this.mRemoteServiceKeystore.isAliasExists(str, z) == 0;
    }

    public int installCaCert(SemCertAndroidKeyStore semCertAndroidKeyStore) throws RemoteException {
        return this.mRemoteServiceKeystore.installCACert(semCertAndroidKeyStore);
    }

    public int installCertInAndroidKeyStore(SemCertByte semCertByte, String str, char[] cArr, boolean z, int i) throws RemoteException {
        return this.mRemoteServiceKeystore.installCertificateInAndroidKeyStore(semCertByte, str, cArr, z, i);
    }
}
