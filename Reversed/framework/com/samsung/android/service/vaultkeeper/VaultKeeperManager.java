package com.samsung.android.service.vaultkeeper;

import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.service.vaultkeeper.IVaultKeeperService.Stub;

public final class VaultKeeperManager {
    public static final int ERR_EXCEPTION = -3;
    public static final int ERR_GENERAL_FAILED = -1;
    public static final int ERR_INVALID_ARGUMENT = -2;
    public static final int ERR_SERVICE_NOT_SUPPORT = -4;
    public static final int MAX_LEN_VAULT_NAME = 32;
    public static final int SUCCESS = 0;
    private static final String TAG = "VaultKeeperManager";
    public static final int VAULT_HMAC_LEN = 32;
    public static final int VAULT_KEY_LEN = 32;
    private String mClientPkgName;
    private IVaultKeeperService mService;
    private String mVaultName;

    private VaultKeeperManager() {
    }

    private VaultKeeperManager(String str) {
        this.mService = Stub.asInterface(ServiceManager.getService("VaultKeeperService"));
        this.mVaultName = new String(str);
        try {
            this.mClientPkgName = this.mService.getPackageName(this.mVaultName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static VaultKeeperManager getInstance(String str) {
        if (str == null) {
            Log.e(TAG, "vaultName is null");
            return null;
        } else if (str.length() == 0 || str.length() > 32) {
            Log.e(TAG, "vaultName length is wrong(" + str.length() + "). It should be less than (" + 32 + ")");
            return null;
        } else {
            VaultKeeperManager vaultKeeperManager = new VaultKeeperManager(str);
            if (vaultKeeperManager.mClientPkgName != null) {
                return vaultKeeperManager;
            }
            Log.e(TAG, "Unauthorized Pkg. Manager can't be provided.");
            return null;
        }
    }

    public int destroy(byte[] bArr) {
        try {
            return this.mService.destroy(this.mClientPkgName, this.mVaultName, bArr);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }

    public byte[] getNonce() {
        try {
            return this.mService.getNonce(this.mClientPkgName, this.mVaultName);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public int initialize(byte[] bArr, String str, byte[] bArr2, byte[] bArr3) {
        try {
            return this.mService.initialize(this.mClientPkgName, this.mVaultName, bArr, str, null, bArr2, bArr3);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }

    public int initialize(byte[] bArr, String str, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        try {
            return this.mService.initialize(this.mClientPkgName, this.mVaultName, bArr, str, bArr2, bArr3, bArr4);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }

    public int initialize(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        try {
            Log.i(TAG, "initialize (byte[] key)");
            return this.mService.initialize(this.mClientPkgName, this.mVaultName, bArr, null, null, bArr2, bArr3);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }

    public boolean isInitialized() {
        try {
            return this.mService.isInitialized(this.mClientPkgName, this.mVaultName);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] readData() {
        try {
            return this.mService.readData(this.mClientPkgName, this.mVaultName);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public String readState() {
        try {
            return this.mService.readState(this.mClientPkgName, this.mVaultName);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verifyCertificate(byte[] bArr) {
        try {
            return this.mService.verifyCertificate(this.mClientPkgName, this.mVaultName, bArr);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public int verifyComplete(byte[] bArr, String str, byte[] bArr2) {
        try {
            return this.mService.verifyComplete(this.mClientPkgName, this.mVaultName, bArr, str, bArr2);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }

    public byte[] verifyRequest(byte[] bArr) {
        try {
            return this.mService.verifyRequest(this.mClientPkgName, this.mVaultName, bArr);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public int write(String str, byte[] bArr, byte[] bArr2) {
        try {
            return this.mService.write(this.mClientPkgName, this.mVaultName, str, null, bArr, bArr2);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }

    public int write(String str, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        try {
            return this.mService.write(this.mClientPkgName, this.mVaultName, str, bArr, bArr2, bArr3);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }

    public int write(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        try {
            return this.mService.write(this.mClientPkgName, this.mVaultName, null, bArr, bArr2, bArr3);
        } catch (Throwable e) {
            e.printStackTrace();
            return -3;
        }
    }
}
