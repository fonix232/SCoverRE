package com.samsung.android.security;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.storage.IMountService;
import android.os.storage.IMountService.Stub;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import com.android.internal.widget.LockPatternUtils;
import com.samsung.android.util.SemLog;

public class DirEncryptionWrapper {
    private static final boolean LOCAL_LOGD = Build.TYPE.contains("eng");
    private static final boolean LOCAL_LOGE = Build.TYPE.contains("eng");
    private static final String TAG = "DirEncryptWrapper";
    private static String mExternalSDvolFsUuid = null;
    private static String mExternalSDvolId = null;
    private static String mExternalSDvolState = null;
    private static int mSavedUserId = 0;
    private static boolean mUserDiff = false;
    private Context mContext = null;
    private IMountService mMountService = null;
    private StorageManager mStorageManager = null;

    public DirEncryptionWrapper(Context context) {
        this.mContext = context;
    }

    private StorageVolume[] getVolumeList() {
        StorageManager storageManager = null;
        try {
            storageManager = (StorageManager) this.mContext.getSystemService("storage");
        } catch (Throwable e) {
            logE("Exception:: unable to get Storage Service");
            e.printStackTrace();
        }
        return storageManager == null ? null : storageManager.getVolumeList();
    }

    private static void logD(String str) {
        if (LOCAL_LOGD) {
            SemLog.m16d(TAG, str);
        }
    }

    private static void logE(String str) {
        if (LOCAL_LOGE) {
            SemLog.m18e(TAG, str);
        }
    }

    private String semGetSubSystem(StorageVolume storageVolume) {
        return storageVolume.semGetSubSystem();
    }

    public int getActivePasswordQuality() {
        return new LockPatternUtils(this.mContext).getActivePasswordQuality(getCurrentUserID());
    }

    public int getCurrentUserID() {
        return UserHandle.myUserId();
    }

    public String getExternalSDvolFsUuid() {
        return mExternalSDvolFsUuid;
    }

    public String getExternalSDvolId() {
        return mExternalSDvolId;
    }

    public String getExternalSDvolState() {
        return mExternalSDvolState;
    }

    public String getExternalSdPath() {
        StorageVolume[] volumeList = getVolumeList();
        if (volumeList == null) {
            return null;
        }
        for (StorageVolume storageVolume : volumeList) {
            String semGetSubSystem = semGetSubSystem(storageVolume);
            if (semGetSubSystem != null && semGetSubSystem.equals("sd") && storageVolume.isRemovable()) {
                return storageVolume.getPath();
            }
        }
        return null;
    }

    public int getKeyguardStoredPasswordQuality() {
        return new LockPatternUtils(this.mContext).getKeyguardStoredPasswordQuality(getCurrentUserID());
    }

    public IMountService getMountService() {
        if (this.mMountService == null) {
            IBinder service = ServiceManager.getService("mount");
            if (service != null) {
                this.mMountService = Stub.asInterface(service);
            } else {
                logD("Can't get mount service");
            }
        }
        return this.mMountService;
    }

    public int getSavedUserID() {
        return mSavedUserId;
    }

    public boolean getUserDiff() {
        return mUserDiff;
    }

    public String getVolumeState() {
        StorageManager storageManager = (StorageManager) this.mContext.getSystemService("storage");
        String externalSdPath = getExternalSdPath();
        return (storageManager == null || externalSdPath == null) ? null : storageManager.getVolumeState(externalSdPath);
    }

    public boolean isExternalSDRemovable() {
        StorageVolume[] volumeList = getVolumeList();
        if (volumeList == null) {
            return false;
        }
        for (StorageVolume storageVolume : volumeList) {
            String semGetSubSystem = semGetSubSystem(storageVolume);
            if (semGetSubSystem != null && semGetSubSystem.equals("sd") && storageVolume.isRemovable()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSecure() {
        return new LockPatternUtils(this.mContext).isSecure(getCurrentUserID());
    }

    public boolean mountVolume() {
        try {
            getMountService().mountVolume(getExternalSdPath());
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerStorageEventListener(StorageEventListener storageEventListener) {
        if (this.mStorageManager != null) {
            return false;
        }
        this.mStorageManager = (StorageManager) this.mContext.getSystemService("storage");
        if (this.mStorageManager == null) {
            return false;
        }
        this.mStorageManager.registerListener(storageEventListener);
        return true;
    }

    public void setExternalSDvolFsUuid(String str) {
        mExternalSDvolFsUuid = str;
    }

    public void setExternalSDvolId(String str) {
        mExternalSDvolId = str;
    }

    public void setExternalSDvolState(String str) {
        mExternalSDvolState = str;
    }

    public void setSavedUserID(int i) {
        mSavedUserId = i;
    }

    public void setUserDiff(boolean z) {
        mUserDiff = z;
    }

    public boolean unmountHiddenVolume() {
        try {
            getMountService().unmountVolume(getExternalSdPath() + " hidden", true, false);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unmountVolume() {
        try {
            getMountService().unmountVolume(getExternalSdPath(), true, false);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unmountVolumeByDiffUser() {
        String externalSDvolState = getExternalSDvolState();
        if ("mounted".equals(externalSDvolState)) {
            mUserDiff = true;
            return unmountVolume();
        } else if (!SemSdCardEncryption.VOLUME_STATE_HIDDEN.equals(externalSDvolState)) {
            return (("unmounted".equals(externalSDvolState) || "bad_removal".equals(externalSDvolState)) && getCurrentUserID() == 0) ? mountVolume() : false;
        } else {
            mUserDiff = true;
            return unmountHiddenVolume();
        }
    }
}
