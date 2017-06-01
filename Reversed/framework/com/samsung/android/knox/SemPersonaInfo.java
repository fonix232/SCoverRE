package com.samsung.android.knox;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersonaHandle;
import android.os.UserHandle;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class SemPersonaInfo implements Parcelable {
    public static final int AUTH_TYPE_CMK = 2;
    public static final int AUTH_TYPE_PWD_HASH = 1;
    public static final Creator<SemPersonaInfo> CREATOR = new C02021();
    public static final int FLAG_EC_ENABLED = 65536;
    public static final int FLAG_ENCRYPT = 32;
    public static final int FLAG_MIGRATION = 256;
    public static final int FLAG_SECURE_FOLDER_CONTAINER = 8192;
    public static final int FLAG_SECURE_STORAGE = 64;
    public static final int KLMS_LOCKED = 9;
    public static final int KNOX_SECURITY_TIMEOUT_DEFAULT = 600000;
    public static final int KNOX_STATE_ADMIN_LOCKED = 8;
    public static final int KNOX_STATE_TIMA_COMPROMISED = 7;
    public static final int KNOX_STATE_UPGRADING = 6;
    private static final String LOG_TAG = "SemPersonaInfo";
    public static final String PERSONA_TYPE_DEFAULT = "default";
    public static final int STATE_ACTIVE = 0;
    public static final int STATE_CREATE = 4;
    public static final int STATE_DELETING = 3;
    public static final int STATE_INITIALIZE = 1;
    public static final int STATE_INVALID = -1;
    public static final int STATE_LOCKED = 2;
    public static final int STATE_RESET = 99;
    public static final int STATE_RESET_PASSWORD = 5;
    private String adminPackageName;
    private int adminUid;
    public int authenticationType;
    public boolean canUseBluetooth;
    public boolean canUseExtSdcard;
    public int cmkFormat;
    private int creatorUid;
    public String encryptedId;
    private String fidoRpContext;
    private int fingerCount;
    private List<String> fingerprintHashList;
    private int[] fingerprintIndexList;
    public int flags;
    public int fotaUpgradeVersion;
    public String fwversion;
    public int id;
    private List<String> installedPkgList;
    private boolean isAdminLockedJustBefore;
    public boolean isBBCContainer;
    public boolean isEnabledFingerprintIndex;
    public boolean isEulaShown;
    private boolean isFingerIdentifyFailed;
    private boolean isFingerReset;
    private boolean isFingerTimeout;
    public boolean isFsMounted;
    private boolean isIrisReset;
    private boolean isIrisTimeout;
    public boolean isPureContainer;
    private boolean isQuickAccessUIEnabled;
    public boolean isRestarting;
    public boolean isSdpMinor;
    public boolean isSecureFolder;
    private boolean isUnlockedAfterTurnOn;
    private boolean isUnlockedByCe;
    public boolean isUserManaged;
    public boolean kioskModeEnabled;
    private String knoxBackupPin;
    private int knoxSecurityTimeoutValue;
    private long lastKeyguardUnlockTime;
    public long lastLoggedOutTime;
    public boolean lightWeightContainer;
    public boolean lockInProgress;
    public boolean migratedToM;
    public boolean needsRestart;
    int parentId;
    public boolean partial;
    public int personaFwkVersion;
    public boolean removePersona;
    public boolean resetPassword;
    public boolean resetPersonaOnReboot;
    public String samsungAccount;
    public boolean sdpActive;
    public boolean sdpEnabled;
    public boolean setupComplete;
    private String setupWizardApkLocation;
    public boolean shownFolderHelp;
    public boolean shownLauncherHelp;
    public int timaEcrytfsIndex;
    public int timaPasswordHintIndex;
    public int timaPasswordIndex;
    public int timaPwdResetTokenIndex;
    public String timaVersion;
    public String type;
    public boolean upgradeInProgress;
    private boolean useEncoding;

    static class C02021 implements Creator<SemPersonaInfo> {
        C02021() {
        }

        public SemPersonaInfo createFromParcel(Parcel parcel) {
            return new SemPersonaInfo(parcel);
        }

        public SemPersonaInfo[] newArray(int i) {
            return new SemPersonaInfo[i];
        }
    }

    public SemPersonaInfo() {
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = "";
        this.adminPackageName = "";
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = "";
        this.isUserManaged = true;
        this.lightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isIrisTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isIrisReset = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.fidoRpContext = "";
        this.kioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.isSecureFolder = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = false;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = "";
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.isUnlockedByCe = false;
        this.useEncoding = true;
    }

    public SemPersonaInfo(int i, int i2, int i3, int i4) {
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = "";
        this.adminPackageName = "";
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = "";
        this.isUserManaged = true;
        this.lightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isIrisTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isIrisReset = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.fidoRpContext = "";
        this.kioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.isSecureFolder = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = false;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = "";
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.isUnlockedByCe = false;
        this.useEncoding = true;
        this.id = i;
        this.flags = i2;
        this.parentId = i3;
        this.creatorUid = i4;
    }

    private SemPersonaInfo(Parcel parcel) {
        boolean z = true;
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = "";
        this.adminPackageName = "";
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = "";
        this.isUserManaged = true;
        this.lightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isIrisTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isIrisReset = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.fidoRpContext = "";
        this.kioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.isSecureFolder = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = false;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = "";
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.isUnlockedByCe = false;
        this.useEncoding = true;
        this.id = parcel.readInt();
        this.flags = parcel.readInt();
        this.partial = parcel.readInt() != 0;
        this.parentId = parcel.readInt();
        this.type = parcel.readString();
        this.lastLoggedOutTime = parcel.readLong();
        this.creatorUid = parcel.readInt();
        this.setupWizardApkLocation = parcel.readString();
        this.adminPackageName = parcel.readString();
        this.adminUid = parcel.readInt();
        this.timaVersion = parcel.readString();
        this.timaEcrytfsIndex = parcel.readInt();
        this.timaPasswordIndex = parcel.readInt();
        this.timaPasswordHintIndex = parcel.readInt();
        this.removePersona = parcel.readInt() != 0;
        this.setupComplete = parcel.readInt() != 0;
        this.encryptedId = parcel.readString();
        this.samsungAccount = parcel.readString();
        this.isUserManaged = parcel.readInt() != 0;
        this.isSdpMinor = parcel.readInt() != 0;
        this.authenticationType = parcel.readInt();
        this.resetPassword = parcel.readInt() != 0;
        this.isFsMounted = parcel.readInt() != 0;
        this.fwversion = parcel.readString();
        this.personaFwkVersion = parcel.readInt();
        this.lightWeightContainer = parcel.readInt() != 0;
        this.kioskModeEnabled = parcel.readInt() != 0;
        this.isBBCContainer = parcel.readInt() != 0;
        this.isSecureFolder = parcel.readInt() != 0;
        this.resetPersonaOnReboot = parcel.readInt() != 0;
        this.canUseExtSdcard = parcel.readInt() != 0;
        this.canUseBluetooth = parcel.readInt() != 0;
        this.needsRestart = parcel.readInt() != 0;
        this.isRestarting = parcel.readInt() != 0;
        this.sdpEnabled = parcel.readInt() != 0;
        this.cmkFormat = parcel.readInt();
        this.sdpActive = parcel.readInt() != 0;
        this.isUnlockedAfterTurnOn = parcel.readInt() != 0;
        this.isQuickAccessUIEnabled = parcel.readInt() != 0;
        this.isFingerTimeout = parcel.readInt() != 0;
        this.isIrisTimeout = parcel.readInt() != 0;
        this.isFingerIdentifyFailed = parcel.readInt() != 0;
        this.isFingerReset = parcel.readInt() != 0;
        this.isIrisReset = parcel.readInt() != 0;
        this.fidoRpContext = parcel.readString();
        this.isAdminLockedJustBefore = parcel.readInt() != 0;
        this.lastKeyguardUnlockTime = parcel.readLong();
        this.fingerCount = parcel.readInt();
        this.useEncoding = parcel.readInt() != 0;
        this.shownLauncherHelp = parcel.readInt() != 0;
        this.shownFolderHelp = parcel.readInt() != 0;
        this.knoxSecurityTimeoutValue = parcel.readInt();
        this.isEulaShown = parcel.readInt() != 0;
        this.knoxBackupPin = parcel.readString();
        this.isEnabledFingerprintIndex = parcel.readInt() != 0;
        this.fingerprintHashList = new ArrayList();
        parcel.readList(this.fingerprintHashList, String.class.getClassLoader());
        this.migratedToM = parcel.readInt() != 0;
        if (parcel.readInt() == 0) {
            z = false;
        }
        this.isUnlockedByCe = z;
    }

    public SemPersonaInfo(SemPersonaInfo semPersonaInfo) {
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = "";
        this.adminPackageName = "";
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = "";
        this.isUserManaged = true;
        this.lightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isIrisTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isIrisReset = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.fidoRpContext = "";
        this.kioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.isSecureFolder = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = false;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = "";
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.isUnlockedByCe = false;
        this.useEncoding = true;
        this.id = semPersonaInfo.id;
        this.flags = semPersonaInfo.flags;
        this.partial = semPersonaInfo.partial;
        this.parentId = semPersonaInfo.getParentId();
        this.type = semPersonaInfo.type;
        this.lastLoggedOutTime = semPersonaInfo.lastLoggedOutTime;
        this.creatorUid = semPersonaInfo.getCreatorUid();
        this.setupWizardApkLocation = semPersonaInfo.getSetupWizardApkLocation();
        this.adminPackageName = semPersonaInfo.getAdminPackageName();
        this.adminUid = semPersonaInfo.getAdminUid();
        this.timaVersion = semPersonaInfo.timaVersion;
        this.timaEcrytfsIndex = semPersonaInfo.getTimaEcrytfsIndex();
        this.timaPasswordIndex = semPersonaInfo.getTimaPasswordIndex();
        this.timaPwdResetTokenIndex = semPersonaInfo.getTimaPwdResetTokenIndex();
        this.removePersona = semPersonaInfo.removePersona;
        this.setupComplete = semPersonaInfo.setupComplete;
        this.encryptedId = semPersonaInfo.encryptedId;
        this.samsungAccount = semPersonaInfo.samsungAccount;
        this.isUserManaged = semPersonaInfo.isUserManaged;
        this.isSdpMinor = semPersonaInfo.isSdpMinor;
        this.authenticationType = semPersonaInfo.authenticationType;
        this.resetPassword = semPersonaInfo.resetPassword;
        this.isFsMounted = semPersonaInfo.isFsMounted;
        this.installedPkgList = semPersonaInfo.installedPkgList;
        this.fwversion = semPersonaInfo.fwversion;
        this.personaFwkVersion = semPersonaInfo.personaFwkVersion;
        this.fotaUpgradeVersion = semPersonaInfo.fotaUpgradeVersion;
        this.lightWeightContainer = semPersonaInfo.lightWeightContainer;
        this.kioskModeEnabled = semPersonaInfo.kioskModeEnabled;
        this.isBBCContainer = semPersonaInfo.isBBCContainer;
        this.isSecureFolder = semPersonaInfo.isSecureFolder;
        this.resetPersonaOnReboot = semPersonaInfo.resetPersonaOnReboot;
        this.upgradeInProgress = semPersonaInfo.upgradeInProgress;
        this.timaPasswordHintIndex = semPersonaInfo.getTimaPasswordHintIndex();
        this.canUseExtSdcard = semPersonaInfo.canUseExtSdcard;
        this.canUseBluetooth = semPersonaInfo.canUseBluetooth;
        this.needsRestart = semPersonaInfo.needsRestart;
        this.isRestarting = semPersonaInfo.isRestarting;
        this.sdpEnabled = semPersonaInfo.sdpEnabled;
        this.cmkFormat = semPersonaInfo.cmkFormat;
        this.sdpActive = semPersonaInfo.sdpActive;
        this.isUnlockedAfterTurnOn = semPersonaInfo.isUnlockedAfterTurnOn;
        this.isQuickAccessUIEnabled = semPersonaInfo.isQuickAccessUIEnabled;
        this.isFingerTimeout = semPersonaInfo.isFingerTimeout;
        this.isIrisTimeout = semPersonaInfo.isIrisTimeout;
        this.isFingerIdentifyFailed = semPersonaInfo.isFingerIdentifyFailed;
        this.isFingerReset = semPersonaInfo.isFingerReset;
        this.isIrisReset = semPersonaInfo.isIrisReset;
        this.fidoRpContext = semPersonaInfo.fidoRpContext;
        this.isAdminLockedJustBefore = semPersonaInfo.isAdminLockedJustBefore;
        this.lastKeyguardUnlockTime = semPersonaInfo.lastKeyguardUnlockTime;
        this.fingerCount = semPersonaInfo.fingerCount;
        this.useEncoding = semPersonaInfo.useEncoding;
        this.shownLauncherHelp = semPersonaInfo.shownLauncherHelp;
        this.shownFolderHelp = semPersonaInfo.shownFolderHelp;
        this.knoxSecurityTimeoutValue = semPersonaInfo.knoxSecurityTimeoutValue;
        this.isEulaShown = semPersonaInfo.isEulaShown;
        this.knoxBackupPin = semPersonaInfo.knoxBackupPin;
        this.isEnabledFingerprintIndex = semPersonaInfo.isEnabledFingerprintIndex;
        this.fingerprintIndexList = semPersonaInfo.fingerprintIndexList;
        this.fingerprintHashList = semPersonaInfo.fingerprintHashList;
        this.isUnlockedByCe = semPersonaInfo.isUnlockedByCe;
    }

    private void showFingerprintIndexStatus() {
        int i;
        Log.d(LOG_TAG, "isEnabledFingerprintIndex = " + this.isEnabledFingerprintIndex);
        if (this.fingerprintIndexList != null) {
            Log.d(LOG_TAG, "fingerprintIndexList.length  = " + this.fingerprintIndexList.length);
            for (i = 0; i < this.fingerprintIndexList.length; i++) {
                Log.d(LOG_TAG, "fingerprintIndexList[" + i + "]  = " + this.fingerprintIndexList[i]);
            }
        } else {
            Log.d(LOG_TAG, "fingerprintIndexList is null");
        }
        if (this.fingerprintHashList != null) {
            Log.d(LOG_TAG, "fingerprintHashList.size = " + this.fingerprintHashList.size());
            for (i = 0; i < this.fingerprintHashList.size(); i++) {
                Log.d(LOG_TAG, "fingerprintHashList[" + i + "]  = " + ((String) this.fingerprintHashList.get(i)));
            }
            return;
        }
        Log.d(LOG_TAG, "fingerprintHashList is null");
    }

    public int describeContents() {
        return 0;
    }

    public String getAdminPackageName() {
        return this.adminPackageName;
    }

    public int getAdminUid() {
        return this.adminUid;
    }

    public int getAuthenticationType() {
        return this.authenticationType;
    }

    public int getCreatorUid() {
        Log.d(LOG_TAG, " getCreatorUid: for " + this.id + " is " + this.creatorUid);
        return this.creatorUid;
    }

    public String getFidoRpContext() {
        return this.fidoRpContext;
    }

    public int getFingerCount() {
        return this.fingerCount;
    }

    public List<String> getFingerprintHashList() {
        Log.d(LOG_TAG, "called getFingerprintHashList()");
        return this.fingerprintHashList;
    }

    public int[] getFingerprintIndexList() {
        Log.d(LOG_TAG, "called getFingerprintIndexList()");
        return this.fingerprintIndexList;
    }

    public int getFlags() {
        return this.flags;
    }

    public int getId() {
        return this.id;
    }

    public List<String> getInstalledPkgList() {
        return this.installedPkgList;
    }

    public boolean getIsAdminLockedJustBefore() {
        return this.isAdminLockedJustBefore;
    }

    public boolean getIsFingerIdentifyFailed() {
        return this.isFingerIdentifyFailed;
    }

    public boolean getIsFingerReset() {
        return this.isFingerReset;
    }

    public boolean getIsFingerTimeout() {
        return this.isFingerTimeout;
    }

    public boolean getIsIrisReset() {
        return this.isIrisReset;
    }

    public boolean getIsIrisTimeout() {
        return this.isIrisTimeout;
    }

    public boolean getIsQuickAccessUIEnabled() {
        return this.isQuickAccessUIEnabled;
    }

    public boolean getIsUnlockedAfterTurnOn() {
        return this.isUnlockedAfterTurnOn;
    }

    public boolean getIsUnlockedByCe() {
        return this.isUnlockedByCe;
    }

    public String getKnoxBackupPin() {
        return this.knoxBackupPin;
    }

    public int getKnoxSecurityTimeoutValue() {
        return this.knoxSecurityTimeoutValue;
    }

    public long getLastKeyguardUnlockTime() {
        return this.lastKeyguardUnlockTime;
    }

    public long getLastLoggedOutTime() {
        return this.lastLoggedOutTime;
    }

    public int getParentId() {
        return this.parentId;
    }

    public PersonaHandle getPersonaHandle() {
        return new PersonaHandle(this.id);
    }

    public String getSetupWizardApkLocation() {
        Log.d(LOG_TAG, "getSetupWizardApkLocation: " + this.setupWizardApkLocation);
        return this.setupWizardApkLocation;
    }

    public int getTimaEcrytfsIndex() {
        return this.timaEcrytfsIndex;
    }

    public int getTimaPasswordHintIndex() {
        return this.timaPasswordHintIndex;
    }

    public int getTimaPasswordIndex() {
        return this.timaPasswordIndex;
    }

    public int getTimaPwdResetTokenIndex() {
        return this.timaPwdResetTokenIndex;
    }

    public String getType() {
        return this.type;
    }

    public UserHandle getUserHandle() {
        return new UserHandle(this.id);
    }

    public String getsamsungAccount() {
        return this.samsungAccount;
    }

    public boolean isECContainer() {
        return (this.flags & 65536) == 65536;
    }

    public boolean isEncodingRequired() {
        return this.useEncoding;
    }

    public boolean isMigratedPersona() {
        return (this.flags & 256) == 256;
    }

    public boolean isSecureFileSystem() {
        return (this.flags & 32) == 32;
    }

    public boolean isSecureFolderEnabled() {
        return (this.flags & 8192) == 8192;
    }

    public boolean isSecureStorageEnabled() {
        return (this.flags & 64) == 64;
    }

    public void setAdminPackageName(String str) {
        this.adminPackageName = str;
    }

    public void setAdminUid(int i) {
        this.adminUid = i;
    }

    public void setAuthenticationType(int i) {
        Log.d(LOG_TAG, "setAuthenticationType: " + i);
        this.authenticationType = i;
    }

    public void setCreatorUid(int i) {
        Log.d(LOG_TAG, " setCreatorUid: for " + this.id + " is " + i);
        this.creatorUid = i;
    }

    public void setEncodingRequired(boolean z) {
        this.useEncoding = z;
    }

    public void setFidoRpContext(String str) {
        this.fidoRpContext = str;
    }

    public void setFingerCount(int i) {
        this.fingerCount = i;
    }

    public void setFingerprintHashList(List<String> list) {
        Log.d(LOG_TAG, "called setFingerprintHashList()");
        if (list == null || list.isEmpty()) {
            this.fingerprintHashList = null;
            return;
        }
        this.fingerprintHashList = new ArrayList();
        this.fingerprintHashList.addAll(list);
    }

    public void setFingerprintIndexList(int[] iArr) {
        Log.d(LOG_TAG, "called setFingerprintIndexList()");
        if (iArr == null || iArr.length <= 0) {
            this.fingerprintIndexList = null;
        } else {
            this.fingerprintIndexList = iArr;
        }
    }

    public void setInstalledPkgList(List<String> list) {
        if (list != null && !list.isEmpty()) {
            this.installedPkgList = new ArrayList();
            this.installedPkgList.addAll(list);
        }
    }

    public void setIsAdminLockedJustBefore(boolean z) {
        this.isAdminLockedJustBefore = z;
    }

    public void setIsFingerIdentifyFailed(boolean z) {
        this.isFingerIdentifyFailed = z;
    }

    public void setIsFingerReset(boolean z) {
        this.isFingerReset = z;
    }

    public void setIsFingerTimeout(boolean z) {
        this.isFingerTimeout = z;
    }

    public void setIsIrisReset(boolean z) {
        this.isIrisReset = z;
    }

    public void setIsIrisTimeout(boolean z) {
        this.isIrisTimeout = z;
    }

    public void setIsQuickAccessUIEnabled(boolean z) {
        this.isQuickAccessUIEnabled = z;
    }

    public void setIsUnlockedAfterTurnOn(boolean z) {
        this.isUnlockedAfterTurnOn = z;
    }

    public void setIsUnlockedByCe(boolean z) {
        this.isUnlockedByCe = z;
    }

    public void setKnoxBackupPin(String str) {
        this.knoxBackupPin = str;
    }

    public void setKnoxSecurityTimeoutValue(int i) {
        this.knoxSecurityTimeoutValue = i;
    }

    public void setLastKeyguardUnlockTime(long j) {
        this.lastKeyguardUnlockTime = j;
    }

    public void setSetupWizardApkLocation(String str) {
        Log.d(LOG_TAG, "setSetupWizardApkLocation: " + str);
        this.setupWizardApkLocation = str;
    }

    public void setTimaEcrytfsIndex(int i) {
        this.timaEcrytfsIndex = i;
    }

    public void setTimaPasswordHintIndex(int i) {
        this.timaPasswordHintIndex = i;
    }

    public void setTimaPasswordIndex(int i) {
        this.timaPasswordIndex = i;
    }

    public void setTimaPwdResetTokenIndex(int i) {
        this.timaPwdResetTokenIndex = i;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setsamsungAccount(String str) {
        this.samsungAccount = str;
    }

    public String toString() {
        return "SemPersonaInfo{" + this.id + ":" + Integer.toHexString(this.flags) + "}";
    }

    public boolean verifyKnoxBackupPin(String str) {
        return str.equals(this.knoxBackupPin);
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2 = 1;
        parcel.writeInt(this.id);
        parcel.writeInt(this.flags);
        parcel.writeInt(this.partial ? 1 : 0);
        parcel.writeInt(this.parentId);
        parcel.writeString(this.type);
        parcel.writeLong(this.lastLoggedOutTime);
        parcel.writeInt(this.creatorUid);
        parcel.writeString(this.setupWizardApkLocation);
        parcel.writeString(this.adminPackageName);
        parcel.writeInt(this.adminUid);
        parcel.writeString(this.timaVersion);
        parcel.writeInt(this.timaEcrytfsIndex);
        parcel.writeInt(this.timaPasswordIndex);
        parcel.writeInt(this.timaPasswordHintIndex);
        parcel.writeInt(this.removePersona ? 1 : 0);
        parcel.writeInt(this.setupComplete ? 1 : 0);
        parcel.writeString(this.encryptedId);
        parcel.writeString(this.samsungAccount);
        parcel.writeInt(this.isUserManaged ? 1 : 0);
        parcel.writeInt(this.isSdpMinor ? 1 : 0);
        parcel.writeInt(this.authenticationType);
        parcel.writeInt(this.resetPassword ? 1 : 0);
        parcel.writeInt(this.isFsMounted ? 1 : 0);
        parcel.writeString(this.fwversion);
        parcel.writeInt(this.personaFwkVersion);
        parcel.writeInt(this.lightWeightContainer ? 1 : 0);
        parcel.writeInt(this.kioskModeEnabled ? 1 : 0);
        parcel.writeInt(this.isBBCContainer ? 1 : 0);
        parcel.writeInt(this.isSecureFolder ? 1 : 0);
        parcel.writeInt(this.resetPersonaOnReboot ? 1 : 0);
        parcel.writeInt(this.canUseExtSdcard ? 1 : 0);
        parcel.writeInt(this.canUseBluetooth ? 1 : 0);
        parcel.writeInt(this.needsRestart ? 1 : 0);
        parcel.writeInt(this.isRestarting ? 1 : 0);
        parcel.writeInt(this.sdpEnabled ? 1 : 0);
        parcel.writeInt(this.cmkFormat);
        parcel.writeInt(this.sdpActive ? 1 : 0);
        parcel.writeInt(this.isUnlockedAfterTurnOn ? 1 : 0);
        parcel.writeInt(this.isQuickAccessUIEnabled ? 1 : 0);
        parcel.writeInt(this.isFingerTimeout ? 1 : 0);
        parcel.writeInt(this.isIrisTimeout ? 1 : 0);
        parcel.writeInt(this.isFingerIdentifyFailed ? 1 : 0);
        parcel.writeInt(this.isFingerReset ? 1 : 0);
        parcel.writeInt(this.isIrisReset ? 1 : 0);
        parcel.writeString(this.fidoRpContext);
        parcel.writeInt(this.isAdminLockedJustBefore ? 1 : 0);
        parcel.writeLong(this.lastKeyguardUnlockTime);
        parcel.writeInt(this.fingerCount);
        parcel.writeInt(this.useEncoding ? 1 : 0);
        parcel.writeInt(this.shownLauncherHelp ? 1 : 0);
        parcel.writeInt(this.shownFolderHelp ? 1 : 0);
        parcel.writeInt(this.knoxSecurityTimeoutValue);
        parcel.writeInt(this.isEulaShown ? 1 : 0);
        parcel.writeString(this.knoxBackupPin);
        parcel.writeInt(this.isEnabledFingerprintIndex ? 1 : 0);
        parcel.writeList(this.fingerprintHashList);
        parcel.writeInt(this.migratedToM ? 1 : 0);
        if (!this.isUnlockedByCe) {
            i2 = 0;
        }
        parcel.writeInt(i2);
    }
}
