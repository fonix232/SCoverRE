package com.samsung.android.security;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IDirEncryptService;
import android.os.storage.IDirEncryptService.Stub;
import android.os.storage.IDirEncryptServiceListener;
import android.os.storage.IMountService;
import android.util.Log;
import java.io.File;

public class SemSdCardEncryption {
    public static final String ADMIN_START = "adminStart";
    public static final String CHECK_OTHER_DEVICE = "OtherDevice";
    private static final boolean DEBUG = true;
    public static final int DECRYPT = 3;
    public static final int ENCRYPT = 2;
    public static final int ENCRYPT_FULL_OFF = 5;
    public static final int ENCRYPT_FULL_ON = 4;
    public static final int ERROR_FEATURE_UNAVAILABLE = 200;
    public static final int ERR_INVALID_PARAMETER = 203;
    public static final int ERR_INVALID_PERMISSION = 204;
    public static final int ERR_NOK = 201;
    public static final int ERR_SD_NOT_MOUNTED = 202;
    public static final int EXCL_MEDIA_OFF = 7;
    public static final int EXCL_MEDIA_ON = 6;
    public static final String FLE_KEY_STORE = "/efs/sec_efs/";
    public static final String FLE_KEY_STORE_LEGACY = "/efs/";
    public static final String INTERNAL_STORAGE_PATH = "/mnt/sdcard";
    private static final boolean IS_SUPPORT_SDCARD_SLOT = new File("/sys/class/sec/sdcard").exists();
    public static final String MOVE_MOUNT = "MoveMount";
    private static final int MSG_BASE = 0;
    private static final int MSG_ERR_BASE = 200;
    public static final String NAME = "DirEncryptService";
    public static final int OK = 13;
    public static final int POLICY_ALREADY_SET = 10;
    public static final int POLICY_CAN_NOT_BE_SET_UNDER_BUSY_STATE = 15;
    public static final int POLICY_NOT_SAVED = 9;
    public static final int POLICY_SAVED = 8;
    public static final String SD_CARD_ENCRYPTION_ACTION = "com.sec.app.action.START_SDCARD_ENCRYPTION";
    public static int SECURITY_POLICY_NOTIFICATION_ID = -889275714;
    public static final String STATUS_BUSY = "busy";
    public static final String STATUS_DONE = "done";
    public static final String STATUS_FREE = "free";
    public static final String STATUS_MOUNT = "Mount";
    private static final String TAG = "SemSdCardEncryption";
    public static final String VOLUME_STATE_HIDDEN = "HiddenMount";
    private static boolean mPolicyChanged = false;
    private Context mContext = null;
    private DirEncryptionWrapper mDew = null;
    private IDirEncryptService m_InstDirEncSvc = null;

    public static final class EncryptionState {
        public static final int DECRYPTED = 3;
        public static final int DECRYPTING = 1;
        public static final int ENCRYPTED = 2;
        public static final int ENCRYPTING = 0;
        public static final int SET_ADMIN = -1;

        private EncryptionState() {
        }
    }

    public static final class Error {
        public static final int DECRYPT = 6;
        public static final int ENCRYPT = 5;
        public static final int FILE_OPEN = 11;
        public static final int MOUNT = 7;
        public static final int NO = 0;
        public static final int OTHER_ENCRYPT = 8;
        public static final int PRESCAN_FULL = 4;
        public static final int PWD_CREATE = 1;
        public static final int PWD_DELETE = 3;
        public static final int PWD_UPDATE = 2;
        public static final int UNMOUNT = 8;

        private Error() {
        }
    }

    public static final class Status {
        public static final int DECRYPTING = 3;
        public static final int ENCRYPTING = 2;
        public static final int FREE = 0;
        public static final int READY = 1;

        private Status() {
        }
    }

    public SemSdCardEncryption(Context context) {
        this.mContext = context;
        this.mDew = new DirEncryptionWrapper(this.mContext);
        this.m_InstDirEncSvc = Stub.asInterface(ServiceManager.getService(NAME));
        if (this.m_InstDirEncSvc == null) {
            Log.d(TAG, "Unable to get DirEncryptService instance.");
        }
        if (!IS_SUPPORT_SDCARD_SLOT) {
            Log.d(TAG, "Dir Encryption not available");
            this.m_InstDirEncSvc = null;
        }
    }

    public static boolean isEncryptionFeatureEnabled() {
        return IS_SUPPORT_SDCARD_SLOT;
    }

    public void SetMountSDcardToHelper(boolean z) {
        if (isEncryptionSupported() && this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.SetMountSDcardToHelper(z);
            } catch (RemoteException e) {
                Log.d(TAG, "Unable to communicate with DirEncryptService");
            }
        }
    }

    public void clearPrefs(String str) {
        Log.d(TAG, "clearPrefs : " + str);
        if (str != null) {
            try {
                this.m_InstDirEncSvc.clearPrefs(str);
            } catch (RemoteException e) {
                Log.d(TAG, "Unable to communicate with DirEncryptService");
            }
        }
    }

    public int encryptStorage(boolean z) {
        int i = 200;
        try {
            IBinder service = ServiceManager.getService("mount");
            if (service != null) {
                i = IMountService.Stub.asInterface(service).encryptExternalStorage(z);
            }
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return i;
    }

    public int getAdditionalSpaceRequired() {
        int i = 200;
        if (!isEncryptionSupported() || this.m_InstDirEncSvc == null) {
            return i;
        }
        try {
            i = this.m_InstDirEncSvc.getAdditionalSpaceRequired();
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return i;
    }

    public int getCurrentStatus() {
        int i = 200;
        if (!isEncryptionSupported() || this.m_InstDirEncSvc == null) {
            return i;
        }
        try {
            i = this.m_InstDirEncSvc.getCurrentStatus();
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return i;
    }

    public int getCurrentUserID() {
        return this.mDew.getCurrentUserID();
    }

    public String getExternalSDvolFsUuid() {
        return this.mDew.getExternalSDvolFsUuid();
    }

    public String getExternalSdPath() {
        return this.mDew.getExternalSdPath();
    }

    public int getKeyguardStoredPasswordQuality() {
        return this.mDew.getKeyguardStoredPasswordQuality();
    }

    public int getLastError() {
        int i = 200;
        if (!isEncryptionSupported() || this.m_InstDirEncSvc == null) {
            return i;
        }
        try {
            i = this.m_InstDirEncSvc.getLastError();
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return i;
    }

    public boolean getPolicyChanged() {
        return mPolicyChanged;
    }

    public int getSavedUserID() {
        return this.mDew.getSavedUserID();
    }

    public SemSdCardEncryptionPolicy getSdCardEncryptionPreferences() {
        return getSdCardEncryptionPreferences(null);
    }

    public SemSdCardEncryptionPolicy getSdCardEncryptionPreferences(String str) {
        if (this.m_InstDirEncSvc == null) {
            return null;
        }
        SemSdCardEncryptionPolicy semSdCardEncryptionPolicy = null;
        try {
            semSdCardEncryptionPolicy = this.m_InstDirEncSvc.getSdCardEncryptionPreferences(str);
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return semSdCardEncryptionPolicy;
    }

    public boolean getUserDiff() {
        return this.mDew.getUserDiff();
    }

    public String getVolumeState() {
        return this.mDew.getVolumeState();
    }

    public boolean isEncryptionAppliedSDCard() {
        boolean z = false;
        if (this.m_InstDirEncSvc == null) {
            return false;
        }
        try {
            z = this.m_InstDirEncSvc.isEncryptionAppliedSDCard();
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return z;
    }

    public boolean isEncryptionSupported() {
        return IS_SUPPORT_SDCARD_SLOT && getCurrentUserID() == 0;
    }

    public boolean isExternalSDRemovable() {
        return this.mDew.isExternalSDRemovable();
    }

    public boolean isStorageCardEncryptionPoliciesApplied() {
        boolean z = false;
        if (!isEncryptionSupported() || this.m_InstDirEncSvc == null) {
            return false;
        }
        try {
            z = this.m_InstDirEncSvc.isStorageCardEncryptionPoliciesApplied() == 1;
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return z;
    }

    public boolean mountVolume() {
        return this.mDew.mountVolume();
    }

    public void registerListener(IDirEncryptServiceListener iDirEncryptServiceListener) {
        if (this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.registerListener(iDirEncryptServiceListener);
            } catch (RemoteException e) {
                Log.d(TAG, "Unable to communicate with DirEncryptService");
            }
        }
    }

    public int setAdminPolicy(boolean z, String str) {
        if (!((DevicePolicyManager) this.mContext.getSystemService("device_policy")).semGetRequireStorageCardEncryption(null) || z) {
            return setSdCardEncryptionPolicy(z ? 1 : 0, -1, str);
        }
        Log.d(TAG, "DPM set the encryption policy yet");
        return 9;
    }

    public void setNeedToCreateKey(boolean z) {
        if (isEncryptionSupported() && this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.setNeedToCreateKey(z);
            } catch (RemoteException e) {
                Log.d(TAG, "Unable to communicate with DirEncryptService");
            }
        }
    }

    public int setPassword(String str) {
        int i = 200;
        if (!isEncryptionSupported() || this.m_InstDirEncSvc == null) {
            return 200;
        }
        try {
            IBinder service = ServiceManager.getService("mount");
            if (service != null) {
                i = IMountService.Stub.asInterface(service).setExternalEncryptionPassword(str);
            }
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        return i;
    }

    public void setPolicyChanged(boolean z) {
        mPolicyChanged = z;
    }

    public void setSavedUserID(int i) {
        this.mDew.setSavedUserID(i);
    }

    public int setSdCardEncryptionPolicy(int i, int i2, String str) {
        int i3 = 200;
        if (!isEncryptionSupported() || this.m_InstDirEncSvc == null) {
            return i3;
        }
        try {
            i3 = this.m_InstDirEncSvc.setSdCardEncryptionPolicy(i, i2, str);
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        Log.d(TAG, "setSdCardEncryptionPolicy result : " + i3);
        if (i3 == 8 || i3 == 10) {
            Log.d(TAG, "result : POLICY_SAVED || POLICY_ALREADY_SET");
            unmountSDCardByAdmin();
        }
        setPolicyChanged(true);
        return i3;
    }

    public int setStorageCardEncryptionPolicy(int i) {
        int i2 = 200;
        if (!isEncryptionSupported() || this.m_InstDirEncSvc == null) {
            return i2;
        }
        try {
            i2 = this.m_InstDirEncSvc.setStorageCardEncryptionPolicy(i, 4, 7);
        } catch (RemoteException e) {
            Log.d(TAG, "Unable to communicate with DirEncryptService");
        }
        Log.d(TAG, "setStorageCardEncryptionPolicy result : " + i2);
        if (i2 == 8 || i2 == 10) {
            Log.d(TAG, "result : POLICY_SAVED || POLICY_ALREADY_SET");
            unmountSDCardByAdmin();
        }
        setPolicyChanged(true);
        return i2;
    }

    public void setUserDiff(boolean z) {
        this.mDew.setUserDiff(z);
    }

    public void unmountSDCardByAdmin() {
        if (this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.unmountSDCardByAdmin();
            } catch (RemoteException e) {
                Log.d(TAG, "Unable to communicate with DirEncryptService");
            }
        }
    }

    public boolean unmountVolumeByDiffUser() {
        SemSdCardEncryptionPolicy sdCardEncryptionPreferences = getSdCardEncryptionPreferences();
        if (sdCardEncryptionPreferences == null) {
            sdCardEncryptionPreferences = new SemSdCardEncryptionPolicy();
        }
        return sdCardEncryptionPreferences.getEncryptState() == 2 ? this.mDew.unmountVolumeByDiffUser() : false;
    }

    public void unregisterListener(IDirEncryptServiceListener iDirEncryptServiceListener) {
        if (this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.unregisterListener(iDirEncryptServiceListener);
            } catch (RemoteException e) {
                Log.d(TAG, "Unable to communicate with DirEncryptService");
            }
        }
    }
}
