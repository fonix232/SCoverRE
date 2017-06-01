package com.samsung.android.service.RemoteLockControl;

import android.app.ActivityManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.IRemoteLockMonitorCallback;
import com.android.internal.widget.IRemoteLockMonitorCallback.Stub;
import com.android.internal.widget.RemoteLockInfo;
import com.android.internal.widget.RemoteLockInfo.Builder;
import com.samsung.android.mateservice.common.BundleArgs;
import com.samsung.android.service.vaultkeeper.VaultKeeperManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class RemoteLockControlManager {
    private static final String BLINK_STATE = "Blink";
    private static final String COMPLETED_STATE = "Completed";
    private static final String LOCKED_STATE = "Locked";
    private static final String NORMAL_STATE = "Normal";
    private static final int RLC_AES256_IV_SIZE = 16;
    private static final int RLC_AES256_KEY_SIZE = 32;
    private static final int RLC_DELAY_TIME = 30000;
    private static final int RLC_ERR_CERTIFICATE = -3;
    private static final int RLC_ERR_CRYPTO_FUNCTION = -8;
    private static final int RLC_ERR_GENERAL = -1;
    private static final int RLC_ERR_INVALID_ARGUMENT = -2;
    private static final int RLC_ERR_INVALID_TOKEN = -4;
    private static final int RLC_ERR_LOCKSCREEN = -9;
    private static final int RLC_ERR_MAX_FAILURE_COUNT_REACHED = -7;
    private static final int RLC_ERR_SERIALIZATION = -10;
    private static final int RLC_ERR_SERVER_RESULT_FAIL = -6;
    private static final int RLC_ERR_VAULTKEEPER = -5;
    private static final int RLC_FAILCOUNT_FOR_DELAY = 5;
    private static final int RLC_ID_SIZE = 40;
    private static final int RLC_KEY_SIZE = 32;
    private static final int RLC_NONCE_FLAG_VERIFY = 2;
    private static final int RLC_NONCE_FLAG_WRITE = 1;
    private static final int RLC_NONCE_SIZE = 32;
    private static final int RLC_SHA256_SIZE = 32;
    private static final String TAG = "RlcManager";
    private static boolean mCompleteUnlockingDone = false;
    private static Context mContext = null;
    private static CryptoManager mCrypto = null;
    private static ILockSettings mLockSettingsService = null;
    private static byte[] mNonceDev = new byte[32];
    private static byte[] mNonceSvr = new byte[32];
    private static IRemoteLockControlListener mRemoteLockControlListener = null;
    private static byte[] mRlcId = new byte[40];
    private static byte[] mRlcKey = new byte[32];
    private static final String mRlcVaultName = "RemoteLockControl";
    private static byte[] mServerCert;
    private static VaultKeeperManager mVkm;
    IRemoteLockMonitorCallback mRemoteLockMonitorCallback = new C02391();

    class C02391 extends Stub {
        C02391() {
        }

        public void changeRemoteLockState(RemoteLockInfo remoteLockInfo) throws RemoteException {
            Log.d(RemoteLockControlManager.TAG, "changeRemoteLockState data = " + remoteLockInfo.lockType);
        }

        public int checkRemoteLockPassword(String str) {
            int i = -1;
            Log.i(RemoteLockControlManager.TAG, "checkRemoteLockPassword");
            try {
                i = RemoteLockControlManager.this.completeUnlocking(str);
                if (i == 0) {
                    if (RemoteLockControlManager.mRemoteLockControlListener != null) {
                        RemoteLockControlManager.mRemoteLockControlListener.onUnlockedByPasscode();
                    } else {
                        Log.e(RemoteLockControlManager.TAG, "RemoteLockControlListener is null, can't call onUnlockedByPasscode()");
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                if (!RemoteLockControlManager.mCompleteUnlockingDone) {
                    throw new RuntimeException("Error in RLC Manager internally");
                }
            }
            return i;
        }
    }

    class CryptoManager {
        CryptoManager() {
        }

        public byte[] ecryptWithAES256CBC(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
            byte[] bArr4 = null;
            try {
                Key secretKeySpec = new SecretKeySpec(bArr2, "AES");
                Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
                instance.init(1, secretKeySpec, new IvParameterSpec(bArr3));
                bArr4 = instance.doFinal(bArr);
            } catch (Throwable e) {
                e.printStackTrace();
                RemoteLockControlManager.this.throwException(-8, "ecryptWithAES256CBC");
            }
            return bArr4;
        }

        public byte[] ecryptWithServerPubKey(byte[] bArr, byte[] bArr2) throws RemoteLockControlException {
            byte[] bArr3 = null;
            try {
                Key publicKey = ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bArr2))).getPublicKey();
                Cipher instance = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
                instance.init(1, publicKey);
                bArr3 = instance.doFinal(bArr);
            } catch (Throwable e) {
                e.printStackTrace();
                RemoteLockControlManager.this.throwException(-8, "ecryptWithServerPubKey");
            }
            return bArr3;
        }

        public void getRandom(byte[] bArr) {
            new SecureRandom().nextBytes(bArr);
        }

        public byte[] hmacSha256(byte[] bArr, byte[] bArr2) throws RemoteLockControlException {
            byte[] bArr3 = null;
            String str = "HmacSHA256";
            try {
                Mac instance = Mac.getInstance("HmacSHA256");
                instance.init(new SecretKeySpec(bArr2, "HmacSHA256"));
                bArr3 = instance.doFinal(bArr);
            } catch (Throwable e) {
                e.printStackTrace();
                RemoteLockControlManager.this.throwException(-8, "hmacSha256");
            }
            return bArr3;
        }

        public byte[] sha256(String str) throws RemoteLockControlException {
            byte[] bArr = null;
            try {
                MessageDigest instance = MessageDigest.getInstance("SHA-256");
                instance.update(str.getBytes());
                bArr = instance.digest();
            } catch (Throwable e) {
                e.printStackTrace();
                RemoteLockControlManager.this.throwException(-8, "sha256");
            }
            return bArr;
        }

        public boolean verifyCertChain(byte[] bArr) {
            if (RemoteLockControlManager.mVkm != null) {
                return RemoteLockControlManager.mVkm.verifyCertificate(bArr);
            }
            Log.e(RemoteLockControlManager.TAG, "Error from VaultKeeper Manager is null object");
            return false;
        }
    }

    static class RlcVaultData implements Serializable {
        static final long serialVersionUID = 1986081920160627777L;
        private String mClientData;
        private int mFailureCount;
        private String mNoticeMessage;
        private String mPhoneNumber;
        private String mRequesterName;

        RlcVaultData(int i, String str, String str2, String str3, String str4) {
            this.mFailureCount = i;
            this.mNoticeMessage = str;
            this.mClientData = str4;
            this.mRequesterName = str3;
            this.mPhoneNumber = str2;
        }

        public String getClientData() {
            return this.mClientData;
        }

        public int getFailureCount() {
            return this.mFailureCount;
        }

        public String getNoticeMessage() {
            return this.mNoticeMessage;
        }

        public String getPhoneNumber() {
            return this.mPhoneNumber;
        }

        public String getRequesterName() {
            return this.mRequesterName;
        }

        public void setClientData(String str) {
            this.mClientData = str;
        }

        public void setFailureCount(int i) {
            this.mFailureCount = i;
        }

        public void setNoticeMessage(String str) {
            this.mNoticeMessage = str;
        }

        public void setPhoneNumber(String str) {
            this.mPhoneNumber = str;
        }

        public void setRequesterName(String str) {
            this.mRequesterName = str;
        }
    }

    public RemoteLockControlManager(Context context) {
        mContext = context;
        mServerCert = null;
        mVkm = VaultKeeperManager.getInstance(mRlcVaultName);
        mCrypto = new CryptoManager();
        mRemoteLockControlListener = null;
        mLockSettingsService = null;
    }

    public RemoteLockControlManager(Context context, IRemoteLockControlListener iRemoteLockControlListener) {
        mContext = context;
        mServerCert = null;
        mVkm = VaultKeeperManager.getInstance(mRlcVaultName);
        mCrypto = new CryptoManager();
        mRemoteLockControlListener = iRemoteLockControlListener;
        mLockSettingsService = null;
    }

    private void clearRlcData() {
        Arrays.fill(mRlcKey, (byte) 0);
        Arrays.fill(mRlcId, (byte) 0);
        Arrays.fill(mNonceDev, (byte) 0);
        Arrays.fill(mNonceSvr, (byte) 0);
        if (mServerCert != null) {
            Arrays.fill(mServerCert, (byte) 0);
            mServerCert = null;
        }
    }

    private <T> T deserialize(byte[] bArr, Class<T> cls) throws RemoteLockControlException {
        ObjectInputStream objectInputStream;
        Throwable e;
        InputStream inputStream;
        Throwable th;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream2 = null;
        T t = null;
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            InputStream byteArrayInputStream2 = new ByteArrayInputStream(bArr);
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream2);
            } catch (Exception e2) {
                e = e2;
                inputStream = byteArrayInputStream2;
                try {
                    e.printStackTrace();
                    throwException(-10, "Error deserialize");
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    if (objectInputStream2 != null) {
                        try {
                            objectInputStream2.close();
                        } catch (IOException e4) {
                        }
                    }
                    return t;
                } catch (Throwable th2) {
                    th = th2;
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                    if (objectInputStream2 != null) {
                        try {
                            objectInputStream2.close();
                        } catch (IOException e6) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = byteArrayInputStream2;
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (objectInputStream2 != null) {
                    objectInputStream2.close();
                }
                throw th;
            }
            try {
                t = objectInputStream.readObject();
                if (byteArrayInputStream2 != null) {
                    try {
                        byteArrayInputStream2.close();
                    } catch (IOException e7) {
                    }
                }
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e8) {
                    }
                }
                objectInputStream2 = objectInputStream;
                inputStream = byteArrayInputStream2;
            } catch (Exception e9) {
                e = e9;
                objectInputStream2 = objectInputStream;
                inputStream = byteArrayInputStream2;
                e.printStackTrace();
                throwException(-10, "Error deserialize");
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (objectInputStream2 != null) {
                    objectInputStream2.close();
                }
                return t;
            } catch (Throwable th4) {
                th = th4;
                objectInputStream2 = objectInputStream;
                inputStream = byteArrayInputStream2;
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (objectInputStream2 != null) {
                    objectInputStream2.close();
                }
                throw th;
            }
        } catch (Exception e10) {
            e = e10;
            e.printStackTrace();
            throwException(-10, "Error deserialize");
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            if (objectInputStream2 != null) {
                objectInputStream2.close();
            }
            return t;
        }
        return t;
    }

    private RlcVaultData getRlcVaultData() throws RemoteLockControlException {
        Log.i(TAG, "getRlcVaultData");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            byte[] readData = mVkm.readData();
            if (readData == null) {
                throwException(-5, "Error from VaultKeeper (readData)");
            }
            if (readData.length != 0) {
                return (RlcVaultData) deserialize(readData, RlcVaultData.class);
            }
            Log.w(TAG, "No data in Vault");
            return null;
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    private byte[] makeDeviceMsg(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6) throws RemoteLockControlException {
        int i = 0;
        if (bArr2 != null) {
            i = bArr2.length + 0;
        }
        if (bArr3 != null) {
            i += bArr3.length;
        }
        if (bArr4 != null) {
            i += bArr4.length;
        }
        if (bArr5 != null) {
            i += bArr5.length;
        }
        if (bArr6 != null) {
            i += bArr6.length;
        }
        byte[] bArr7 = new byte[i];
        int i2 = 0;
        if (bArr2 != null) {
            try {
                System.arraycopy(bArr2, 0, bArr7, 0, bArr2.length);
                i2 = bArr2.length + 0;
            } catch (RemoteLockControlException e) {
                throw e;
            }
        }
        if (bArr3 != null) {
            System.arraycopy(bArr3, 0, bArr7, i2, bArr3.length);
            i2 += bArr3.length;
        }
        if (bArr4 != null) {
            System.arraycopy(bArr4, 0, bArr7, i2, bArr4.length);
            i2 += bArr4.length;
        }
        if (bArr5 != null) {
            System.arraycopy(bArr5, 0, bArr7, i2, bArr5.length);
            i2 += bArr5.length;
        }
        if (bArr6 != null) {
            System.arraycopy(bArr6, 0, bArr7, i2, bArr6.length);
            i2 += bArr6.length;
        }
        return encryptData(bArr7, bArr);
    }

    private byte[] makeResultDev() throws RemoteLockControlException {
        byte[] bArr = null;
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            String readState = mVkm.readState();
            if (readState == null) {
                throwException(-5, "Error from VaultKeeper (readState)");
            }
            bArr = makeDeviceMsg(mServerCert, null, mRlcId, mNonceSvr, null, readState.getBytes());
        } catch (RemoteLockControlException e) {
            throw e;
        } catch (Throwable e2) {
            e2.printStackTrace();
            throwException(-1, "Exception");
        }
        return bArr;
    }

    private void parameterChecking(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
        if (bArr == null) {
            throwException(-2, "serverCert is null");
        } else {
            if (!mCrypto.verifyCertChain(bArr)) {
                throwException(-3, "Failed to verify Certificate Chain");
            }
            mServerCert = (byte[]) bArr.clone();
        }
        if (!(bArr2 == null || bArr2.length == 32)) {
            throwException(-2, "nonceSvr size is wrong(" + bArr2.length + "), it should be " + 32);
        }
        if (!(bArr3 == null || bArr3.length == 40)) {
            throwException(-2, "rlcId size is wrong(" + bArr3.length + "), it should be " + 40);
        }
        if (bArr2 != null) {
            System.arraycopy(bArr2, 0, mNonceSvr, 0, 32);
        }
        if (bArr3 != null) {
            System.arraycopy(bArr3, 0, mRlcId, 0, 40);
        }
    }

    private byte[] serialize(Object obj) throws RemoteLockControlException {
        Throwable e;
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        byte[] bArr = null;
        if (obj == null) {
            return new byte[0];
        }
        try {
            ObjectOutputStream objectOutputStream2;
            OutputStream outputStream;
            OutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            try {
                objectOutputStream2 = new ObjectOutputStream(byteArrayOutputStream2);
            } catch (Exception e2) {
                e = e2;
                outputStream = byteArrayOutputStream2;
                try {
                    e.printStackTrace();
                    throwException(-10, "Error serialize");
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                    return bArr;
                } catch (Throwable th2) {
                    th = th2;
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e5) {
                        }
                    }
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = byteArrayOutputStream2;
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                throw th;
            }
            try {
                objectOutputStream2.writeObject(obj);
                bArr = byteArrayOutputStream2.toByteArray();
                if (byteArrayOutputStream2 != null) {
                    try {
                        byteArrayOutputStream2.close();
                    } catch (IOException e7) {
                    }
                }
                if (objectOutputStream2 != null) {
                    try {
                        objectOutputStream2.close();
                    } catch (IOException e8) {
                    }
                }
                objectOutputStream = objectOutputStream2;
                outputStream = byteArrayOutputStream2;
            } catch (Exception e9) {
                e = e9;
                objectOutputStream = objectOutputStream2;
                outputStream = byteArrayOutputStream2;
                e.printStackTrace();
                throwException(-10, "Error serialize");
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                return bArr;
            } catch (Throwable th4) {
                th = th4;
                objectOutputStream = objectOutputStream2;
                outputStream = byteArrayOutputStream2;
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                throw th;
            }
        } catch (Exception e10) {
            e = e10;
            e.printStackTrace();
            throwException(-10, "Error serialize");
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            return bArr;
        }
        return bArr;
    }

    private void setRemoteLockToLockscreen() throws RemoteLockControlException {
        boolean z = false;
        Log.i(TAG, "setRemoteLockToLockscreen");
        try {
            if (mLockSettingsService == null) {
                mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
            }
            RlcVaultData rlcVaultData = getRlcVaultData();
            if (rlcVaultData == null) {
                throwException(-9, "getRlcVaultData return null");
            }
            if (LOCKED_STATE.equals(query())) {
                z = true;
            }
            mLockSettingsService.setRemoteLock(ActivityManager.getCurrentUser(), new Builder(2, z).setClientName(rlcVaultData.getRequesterName()).setPhoneNumber(rlcVaultData.getPhoneNumber()).setMessage(rlcVaultData.getNoticeMessage()).setAllowFailCount(5).setLockTimeOut(30000).setBlockCount(0).build());
        } catch (Throwable e) {
            e.printStackTrace();
            throwException(-9, "Runtime Exception from Lockscreen");
        } catch (RemoteLockControlException e2) {
            throw e2;
        }
    }

    private void throwException(int i, String str) throws RemoteLockControlException {
        Log.e(TAG, "[" + i + "]" + str);
        throw new RemoteLockControlException(i, str);
    }

    public void bindToLockScreen() throws RemoteLockControlException {
        Log.i(TAG, "bindToLockScreen");
        try {
            if (mLockSettingsService == null) {
                mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
            }
            mLockSettingsService.registerRemoteLockCallback(2, this.mRemoteLockMonitorCallback);
            setRemoteLockToLockscreen();
        } catch (Throwable e) {
            e.printStackTrace();
            throwException(-9, "Runtime Exception from Lockscreen");
        } catch (RemoteLockControlException e2) {
            throw e2;
        }
    }

    public byte[] completeBlinking(boolean z, byte[] bArr, byte[] bArr2) throws RemoteLockControlException {
        Log.i(TAG, "completeBlinking");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        if (!z) {
            try {
                throwException(-6, "resultSvr is fail");
            } catch (RemoteLockControlException e) {
                throw e;
            } catch (Throwable th) {
                clearRlcData();
            }
        }
        if (bArr.length != 32) {
            throwException(-2, "passcode hash length is wrong(" + bArr.length + ")");
        }
        int write = mVkm.write(BLINK_STATE, bArr, bArr2);
        if (write != 0) {
            throwException(-5, "Error from VaultKeeper (write blink with passcode/" + write + ")");
        }
        unbindFromLockScreen();
        byte[] makeResultDev = makeResultDev();
        clearRlcData();
        return makeResultDev;
    }

    public byte[] completeCompleting(boolean z, byte[] bArr) throws RemoteLockControlException {
        Log.i(TAG, "completeCompleting");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        if (!z) {
            try {
                throwException(-6, "resultSvr is fail");
            } catch (RemoteLockControlException e) {
                throw e;
            } catch (Throwable th) {
                clearRlcData();
            }
        }
        int write = mVkm.write(COMPLETED_STATE, null, bArr);
        if (write != 0) {
            throwException(-5, "Error from VaultKeeper (write/" + write + ")");
        }
        setRemoteLockToLockscreen();
        byte[] makeResultDev = makeResultDev();
        clearRlcData();
        return makeResultDev;
    }

    public byte[] completeLocking(boolean z, byte[] bArr, byte[] bArr2, String str, String str2, String str3) throws RemoteLockControlException {
        Log.i(TAG, "completeLocking");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        if (!z) {
            try {
                throwException(-6, "resultSvr is fail");
            } catch (RemoteLockControlException e) {
                throw e;
            } catch (Throwable th) {
                clearRlcData();
            }
        }
        if (bArr.length != 32) {
            throwException(-2, "passcode hash length is wrong(" + bArr.length + ")");
        }
        if (str == null) {
            throwException(-2, "noticeMsg is null");
        }
        if (str.length() == 0) {
            throwException(-2, "noticeMsg has nothing");
        }
        if (str2 == null) {
            throwException(-2, "nophoneNumberticeMsg is null");
        }
        if (str2.length() == 0) {
            throwException(-2, "phoneNumber has nothing");
        }
        if (str3 == null) {
            throwException(-2, "requesterName is null");
        }
        if (str3.length() == 0) {
            throwException(-2, "requesterName has nothing");
        }
        int write = mVkm.write(LOCKED_STATE, bArr, bArr2);
        if (write != 0) {
            throwException(-5, "Error from VaultKeeper (write with passcode/" + write + ")");
        }
        if (!setLockscreenData(str, str2, str3)) {
            throwException(-5, "setLockscreenData");
        }
        bindToLockScreen();
        byte[] makeResultDev = makeResultDev();
        clearRlcData();
        return makeResultDev;
    }

    public byte[] completeRegistering(boolean z, byte[] bArr, byte[] bArr2) throws RemoteLockControlException {
        Log.i(TAG, "completeRegistering");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        if (!z) {
            try {
                throwException(-6, "resultSvr is fail");
            } catch (RemoteLockControlException e) {
                throw e;
            } catch (Throwable th) {
                clearRlcData();
            }
        }
        byte[] bArr3 = new byte[(NORMAL_STATE.getBytes().length + 32)];
        System.arraycopy(NORMAL_STATE.getBytes(), 0, bArr3, 0, NORMAL_STATE.getBytes().length);
        System.arraycopy(mNonceDev, 0, bArr3, NORMAL_STATE.getBytes().length, 32);
        if (!Arrays.equals(bArr, mCrypto.hmacSha256(bArr3, mRlcKey))) {
            throwException(-4, "Invalid token");
        }
        int initialize = mVkm.initialize(mRlcKey, NORMAL_STATE, mServerCert, bArr2);
        if (initialize != 0) {
            throwException(-5, "Error from VaultKeeper (initialization/" + initialize + ")");
        }
        byte[] makeResultDev = makeResultDev();
        clearRlcData();
        return makeResultDev;
    }

    public int completeUnlocking(String str) throws RemoteLockControlException {
        Log.i(TAG, "completeUnlocking(passcode)");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            mCompleteUnlockingDone = false;
            int failureCount = getFailureCount();
            byte[] nonce = mVkm.getNonce();
            if (nonce == null) {
                throwException(-5, "Error from VaultKeeper (getNonce)");
            }
            System.arraycopy(nonce, 0, mNonceDev, 0, 32);
            byte[] sha256 = mCrypto.sha256(str);
            if (sha256 == null) {
                throwException(-8, "Fail to hash for passcode");
            }
            byte[] bArr = new byte[(NORMAL_STATE.getBytes().length + 32)];
            System.arraycopy(NORMAL_STATE.getBytes(), 0, bArr, 0, NORMAL_STATE.getBytes().length);
            System.arraycopy(mNonceDev, 0, bArr, NORMAL_STATE.getBytes().length, 32);
            int write = mVkm.write(NORMAL_STATE, null, mCrypto.hmacSha256(bArr, sha256));
            if (write == 0) {
                failureCount = 0;
            } else {
                failureCount++;
                Log.e(TAG, "Incorrect passcode(VaultKeeper-write/" + write + "), current failure count (" + failureCount + ")");
            }
            mCompleteUnlockingDone = true;
            if (!setFailureCount(failureCount)) {
                Log.e(TAG, "Failed setFailureCount");
            }
            clearRlcData();
            return failureCount;
        } catch (RemoteLockControlException e) {
            throw e;
        } catch (Throwable th) {
            clearRlcData();
        }
    }

    public byte[] completeUnlocking(boolean z, byte[] bArr) throws RemoteLockControlException {
        Log.i(TAG, "completeUnlocking");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        if (!z) {
            try {
                throwException(-6, "resultSvr is fail");
            } catch (RemoteLockControlException e) {
                throw e;
            } catch (Throwable th) {
                clearRlcData();
            }
        }
        int write = mVkm.write(NORMAL_STATE, null, bArr);
        if (write != 0) {
            throwException(-5, "Error from VaultKeeper (write/" + write + ")");
        }
        setRemoteLockToLockscreen();
        byte[] makeResultDev = makeResultDev();
        clearRlcData();
        return makeResultDev;
    }

    public byte[] encryptData(byte[] bArr, byte[] bArr2) throws RemoteLockControlException {
        Log.i(TAG, "encryptClientData");
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[16];
        byte[] bArr5 = new byte[48];
        if (bArr == null || bArr.length == 0) {
            throwException(-2, "Invalid clientData");
        }
        try {
            parameterChecking(bArr2, null, null);
            mCrypto.getRandom(bArr4);
            mCrypto.getRandom(bArr3);
            System.arraycopy(bArr3, 0, bArr5, 0, 32);
            System.arraycopy(bArr4, 0, bArr5, 32, 16);
            byte[] ecryptWithServerPubKey = mCrypto.ecryptWithServerPubKey(bArr5, bArr2);
            byte[] ecryptWithAES256CBC = mCrypto.ecryptWithAES256CBC(bArr, bArr3, bArr4);
            if (ecryptWithServerPubKey == null || ecryptWithAES256CBC == null) {
                throwException(-8, BundleArgs.SECURITY_ENCRYPT_DATA);
            }
            byte[] bArr6 = new byte[(ecryptWithServerPubKey.length + ecryptWithAES256CBC.length)];
            System.arraycopy(ecryptWithServerPubKey, 0, bArr6, 0, ecryptWithServerPubKey.length);
            System.arraycopy(ecryptWithAES256CBC, 0, bArr6, ecryptWithServerPubKey.length, ecryptWithAES256CBC.length);
            Arrays.fill(bArr3, (byte) 0);
            Arrays.fill(bArr4, (byte) 0);
            Arrays.fill(bArr5, (byte) 0);
            return bArr6;
        } catch (RemoteLockControlException e) {
            throw e;
        } catch (Throwable th) {
            Arrays.fill(bArr3, (byte) 0);
            Arrays.fill(bArr4, (byte) 0);
            Arrays.fill(bArr5, (byte) 0);
        }
    }

    public String getClientData() throws RemoteLockControlException {
        Log.i(TAG, "getClientData");
        String str = "";
        try {
            RlcVaultData rlcVaultData = getRlcVaultData();
            if (rlcVaultData == null) {
                Log.w(TAG, "No data in Vault");
                return str;
            } else if (rlcVaultData.getClientData().length() != 0) {
                return rlcVaultData.getClientData();
            } else {
                Log.w(TAG, "No client data in Vault");
                return str;
            }
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public int getFailureCount() throws RemoteLockControlException {
        Log.i(TAG, "getFailureCount");
        try {
            RlcVaultData rlcVaultData = getRlcVaultData();
            if (rlcVaultData != null) {
                return rlcVaultData.getFailureCount();
            }
            Log.w(TAG, "No data in Vault");
            return 0;
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public String getNoticeMessage() throws RemoteLockControlException {
        Log.i(TAG, "getNoticeMessage");
        String str = "";
        try {
            RlcVaultData rlcVaultData = getRlcVaultData();
            if (rlcVaultData == null) {
                Log.w(TAG, "No data in Vault");
                return str;
            } else if (rlcVaultData.getNoticeMessage().length() != 0) {
                return rlcVaultData.getNoticeMessage();
            } else {
                Log.w(TAG, "No notice message in Vault");
                return str;
            }
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public String getPhoneNumber() throws RemoteLockControlException {
        Log.i(TAG, "getPhoneNumber");
        String str = "";
        try {
            RlcVaultData rlcVaultData = getRlcVaultData();
            if (rlcVaultData == null) {
                Log.w(TAG, "No data in Vault");
                return str;
            } else if (rlcVaultData.getPhoneNumber().length() != 0) {
                return rlcVaultData.getPhoneNumber();
            } else {
                Log.w(TAG, "No phone number in Vault");
                return str;
            }
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public String getRequesterName() throws RemoteLockControlException {
        Log.i(TAG, "getRequesterName");
        String str = "";
        try {
            RlcVaultData rlcVaultData = getRlcVaultData();
            if (rlcVaultData == null) {
                Log.w(TAG, "No data in Vault");
                return str;
            } else if (rlcVaultData.getNoticeMessage().length() != 0) {
                return rlcVaultData.getRequesterName();
            } else {
                Log.w(TAG, "No requester name in Vault");
                return str;
            }
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public byte[] prepareBlinking(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
        Log.i(TAG, "requestBlinking");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            parameterChecking(bArr, bArr2, bArr3);
            byte[] nonce = mVkm.getNonce();
            if (nonce == null) {
                throwException(-5, "Error from VaultKeeper (getNonce)");
            }
            System.arraycopy(nonce, 0, mNonceDev, 0, 32);
            return makeDeviceMsg(bArr, null, bArr3, bArr2, mNonceDev, null);
        } catch (RemoteLockControlException e) {
            clearRlcData();
            throw e;
        }
    }

    public byte[] prepareCompleting(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
        Log.i(TAG, "requestCompleting");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            parameterChecking(bArr, bArr2, bArr3);
            byte[] nonce = mVkm.getNonce();
            if (nonce == null) {
                throwException(-5, "Error from VaultKeeper (getNonce)");
            }
            System.arraycopy(nonce, 0, mNonceDev, 0, 32);
            return makeDeviceMsg(bArr, null, bArr3, bArr2, mNonceDev, null);
        } catch (RemoteLockControlException e) {
            clearRlcData();
            throw e;
        }
    }

    public byte[] prepareLocking(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
        Log.i(TAG, "requestLocking");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            parameterChecking(bArr, bArr2, bArr3);
            byte[] nonce = mVkm.getNonce();
            if (nonce == null) {
                throwException(-5, "Error from VaultKeeper (getNonce)");
            }
            System.arraycopy(nonce, 0, mNonceDev, 0, 32);
            return makeDeviceMsg(bArr, null, bArr3, bArr2, mNonceDev, null);
        } catch (RemoteLockControlException e) {
            clearRlcData();
            throw e;
        }
    }

    public byte[] prepareRegistering(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
        Log.i(TAG, "requestRegistering");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            parameterChecking(bArr, bArr2, bArr3);
            mCrypto.getRandom(mRlcKey);
            byte[] nonce = mVkm.getNonce();
            if (nonce == null) {
                throwException(-5, "Error from VaultKeeper (getNonce)");
            }
            System.arraycopy(nonce, 0, mNonceDev, 0, 32);
            return makeDeviceMsg(bArr, mRlcKey, bArr3, bArr2, mNonceDev, null);
        } catch (RemoteLockControlException e) {
            clearRlcData();
            throw e;
        }
    }

    public byte[] prepareUnlocking(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
        Log.i(TAG, "requestUnlocking");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            parameterChecking(bArr, bArr2, bArr3);
            byte[] nonce = mVkm.getNonce();
            if (nonce == null) {
                throwException(-5, "Error from VaultKeeper (getNonce)");
            }
            System.arraycopy(nonce, 0, mNonceDev, 0, 32);
            return makeDeviceMsg(bArr, null, bArr3, bArr2, mNonceDev, null);
        } catch (RemoteLockControlException e) {
            clearRlcData();
            throw e;
        }
    }

    public String query() throws RemoteLockControlException {
        Log.i(TAG, "query(void)");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            String readState = mVkm.readState();
            if (readState == null) {
                throwException(-5, "Error from VaultKeeper (readState)");
            }
            return readState;
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public byte[] query(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteLockControlException {
        Log.i(TAG, "query");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            parameterChecking(bArr, bArr2, bArr3);
            String readState = mVkm.readState();
            if (readState == null) {
                throwException(-5, "Error from VaultKeeper (readState)");
            }
            byte[] makeDeviceMsg = makeDeviceMsg(bArr, null, bArr3, bArr2, null, readState.getBytes());
            clearRlcData();
            return makeDeviceMsg;
        } catch (RemoteLockControlException e) {
            throw e;
        } catch (Throwable th) {
            clearRlcData();
        }
    }

    public String setClientData(String str) throws RemoteLockControlException {
        Log.i(TAG, "setClientData");
        String str2 = "";
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            Object rlcVaultData;
            byte[] readData = mVkm.readData();
            if (readData == null) {
                throwException(-5, "Error from VaultKeeper (readData)");
            }
            if (readData.length == 0) {
                rlcVaultData = new RlcVaultData(0, "", "", "", str);
            } else {
                RlcVaultData rlcVaultData2 = (RlcVaultData) deserialize(readData, RlcVaultData.class);
                if (rlcVaultData2 == null) {
                    throwException(-10, "Error deserialize");
                }
                str2 = rlcVaultData2.getClientData();
                rlcVaultData2.setClientData(str);
            }
            int write = mVkm.write(serialize(rlcVaultData), null, null);
            if (write != 0) {
                throwException(-5, "Error from VaultKeeper (write/ " + write + ")");
            }
            return str2;
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public boolean setFailureCount(int i) throws RemoteLockControlException {
        Log.i(TAG, "setFailureCount");
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            RlcVaultData rlcVaultData;
            byte[] readData = mVkm.readData();
            if (readData == null) {
                throwException(-5, "Error from VaultKeeper (readData)");
            }
            if (readData.length == 0) {
                rlcVaultData = new RlcVaultData(0, "", "", "", "");
            } else {
                rlcVaultData = (RlcVaultData) deserialize(readData, RlcVaultData.class);
                if (rlcVaultData == null) {
                    throwException(-10, "Error deserialize");
                }
            }
            rlcVaultData.setFailureCount(i);
            int write = mVkm.write(serialize(rlcVaultData), null, null);
            if (write != 0) {
                throwException(-5, "Error from VaultKeeper (write/" + write + ")");
            }
            return true;
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public boolean setLockscreenData(String str, String str2, String str3) throws RemoteLockControlException {
        Log.i(TAG, "setLockscreenData");
        if (str == null && str2 == null && str3 == null) {
            throwException(-2, "One of paratemers should not be null");
        }
        if (mVkm == null) {
            throwException(-5, "Error from VaultKeeper Manager is null object");
        }
        try {
            Object rlcVaultData;
            byte[] readData = mVkm.readData();
            if (readData == null) {
                throwException(-5, "Error from VaultKeeper (readData)");
            }
            if (readData.length == 0) {
                rlcVaultData = new RlcVaultData(0, str, str2, str3, "");
            } else {
                RlcVaultData rlcVaultData2 = (RlcVaultData) deserialize(readData, RlcVaultData.class);
                if (rlcVaultData2 == null) {
                    throwException(-10, "Error deserialize");
                }
                if (str != null) {
                    rlcVaultData2.setNoticeMessage(str);
                }
                if (str2 != null) {
                    rlcVaultData2.setPhoneNumber(str2);
                }
                if (str3 != null) {
                    rlcVaultData2.setRequesterName(str3);
                }
            }
            int write = mVkm.write(serialize(rlcVaultData), null, null);
            if (write != 0) {
                throwException(-5, "Error from VaultKeeper (write/" + write + ")");
            }
            return true;
        } catch (RemoteLockControlException e) {
            throw e;
        }
    }

    public void unbindFromLockScreen() throws RemoteLockControlException {
        Log.i(TAG, "unbindFromLockScreen");
        try {
            if (mLockSettingsService == null) {
                mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
            }
            mLockSettingsService.unregisterRemoteLockCallback(2, this.mRemoteLockMonitorCallback);
            setRemoteLockToLockscreen();
        } catch (Throwable e) {
            e.printStackTrace();
            throwException(-9, "Runtime Exception from Lockscreen");
        } catch (RemoteLockControlException e2) {
            throw e2;
        }
    }
}
