package com.samsung.android.knox;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.IKnoxModeChangeObserver;
import android.content.pm.IPersonaCallback;
import android.content.pm.ISystemPersonaObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PersonaAttribute;
import android.content.pm.PersonaNewEvent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ISemPersonaManager extends IInterface {

    public static abstract class Stub extends Binder implements ISemPersonaManager {
        private static final String DESCRIPTOR = "com.samsung.android.knox.ISemPersonaManager";
        static final int TRANSACTION_addAppForPersona = 50;
        static final int TRANSACTION_addLockOnImage = 31;
        static final int TRANSACTION_addPackageToInstallWhiteList = 56;
        static final int TRANSACTION_addPackageToNonSecureAppList = 137;
        static final int TRANSACTION_adminLockPersona = 46;
        static final int TRANSACTION_adminUnLockPersona = 47;
        static final int TRANSACTION_broadcastIntentThroughPersona = 159;
        static final int TRANSACTION_canAccess = 110;
        static final int TRANSACTION_clearAppListForPersona = 53;
        static final int TRANSACTION_clearNonSecureAppList = 139;
        static final int TRANSACTION_convertContainerType = 82;
        static final int TRANSACTION_createPersona = 8;
        static final int TRANSACTION_disablePersonaKeyGuard = 66;
        static final int TRANSACTION_doWhenUnlock = 78;
        static final int TRANSACTION_enablePersonaKeyGuard = 67;
        static final int TRANSACTION_exists = 16;
        static final int TRANSACTION_fireEvent = 4;
        static final int TRANSACTION_getAdminUidForPersona = 37;
        static final int TRANSACTION_getAppListForPersona = 52;
        static final int TRANSACTION_getAppPackageNamesAllWhiteLists = 157;
        static final int TRANSACTION_getContainerAppIcon = 150;
        static final int TRANSACTION_getContainerHideUsageStatsApps = 136;
        static final int TRANSACTION_getContainerName = 147;
        static final int TRANSACTION_getContainerOrder = 154;
        static final int TRANSACTION_getCustomBadgedResourceIdIconifRequired = 133;
        static final int TRANSACTION_getDefaultQuickSettings = 134;
        static final int TRANSACTION_getDisabledHomeLaunchers = 65;
        static final int TRANSACTION_getECBadge = 144;
        static final int TRANSACTION_getECIcon = 145;
        static final int TRANSACTION_getECName = 143;
        static final int TRANSACTION_getFidoRpContext = 151;
        static final int TRANSACTION_getFingerCount = 103;
        static final int TRANSACTION_getFingerprintHash = 129;
        static final int TRANSACTION_getFingerprintIndex = 128;
        static final int TRANSACTION_getFocusedUser = 114;
        static final int TRANSACTION_getForegroundUser = 113;
        static final int TRANSACTION_getIsAdminLockedJustBefore = 99;
        static final int TRANSACTION_getIsFingerAsSupplement = 83;
        static final int TRANSACTION_getIsFingerIdentifyFailed = 101;
        static final int TRANSACTION_getIsFingerReset = 95;
        static final int TRANSACTION_getIsFingerTimeout = 91;
        static final int TRANSACTION_getIsIrisReset = 97;
        static final int TRANSACTION_getIsIrisTimeout = 93;
        static final int TRANSACTION_getIsQuickAccessUIEnabled = 89;
        static final int TRANSACTION_getIsUnlockedAfterTurnOn = 87;
        static final int TRANSACTION_getKeyguardShowState = 79;
        static final int TRANSACTION_getKnoxIconChanged = 122;
        static final int TRANSACTION_getKnoxIconChangedAsUser = 124;
        static final int TRANSACTION_getKnoxNameChanged = 121;
        static final int TRANSACTION_getKnoxNameChangedAsUser = 123;
        static final int TRANSACTION_getKnoxSecurityTimeout = 111;
        static final int TRANSACTION_getLastKeyguardUnlockTime = 85;
        static final int TRANSACTION_getMoveToKnoxStatus = 24;
        static final int TRANSACTION_getMyknoxId = 149;
        static final int TRANSACTION_getNonSecureAppList = 138;
        static final int TRANSACTION_getNormalizedState = 28;
        static final int TRANSACTION_getPackageInfo = 135;
        static final int TRANSACTION_getPackagesFromInstallWhiteList = 59;
        static final int TRANSACTION_getParentId = 22;
        static final int TRANSACTION_getParentUserForCurrentPersona = 19;
        static final int TRANSACTION_getPasswordHint = 69;
        static final int TRANSACTION_getPersonaBackgroundTime = 32;
        static final int TRANSACTION_getPersonaIcon = 21;
        static final int TRANSACTION_getPersonaIdentification = 36;
        static final int TRANSACTION_getPersonaIds = 48;
        static final int TRANSACTION_getPersonaInfo = 15;
        static final int TRANSACTION_getPersonaSamsungAccount = 40;
        static final int TRANSACTION_getPersonaType = 26;
        static final int TRANSACTION_getPersonas = 14;
        static final int TRANSACTION_getPersonasForCreator = 18;
        static final int TRANSACTION_getPersonasForUser = 17;
        static final int TRANSACTION_getPreviousState = 2;
        static final int TRANSACTION_getScreenOffTime = 61;
        static final int TRANSACTION_getSecureFolderId = 142;
        static final int TRANSACTION_getState = 1;
        static final int TRANSACTION_getUserManagedPersonas = 42;
        static final int TRANSACTION_handleHomeShow = 44;
        static final int TRANSACTION_handleNotificationWhenUnlock = 160;
        static final int TRANSACTION_hideScrim = 81;
        static final int TRANSACTION_inState = 3;
        static final int TRANSACTION_installApplications = 29;
        static final int TRANSACTION_isAttribute = 6;
        static final int TRANSACTION_isBootCompleted = 155;
        static final int TRANSACTION_isECContainer = 146;
        static final int TRANSACTION_isEnabledFingerprintIndex = 127;
        static final int TRANSACTION_isExternalStorageEnabled = 156;
        static final int TRANSACTION_isFOTAUpgrade = 12;
        static final int TRANSACTION_isFingerLockscreenActivated = 106;
        static final int TRANSACTION_isFingerSupplementActivated = 105;
        static final int TRANSACTION_isFotaUpgradeVersionChanged = 140;
        static final int TRANSACTION_isIrisLockscreenActivated = 107;
        static final int TRANSACTION_isKioskContainerExistOnDevice = 71;
        static final int TRANSACTION_isKioskModeEnabled = 70;
        static final int TRANSACTION_isKnoxKeyguardShown = 80;
        static final int TRANSACTION_isKnoxMultiWindowExist = 153;
        static final int TRANSACTION_isNFCAllowed = 125;
        static final int TRANSACTION_isPackageInInstallWhiteList = 58;
        static final int TRANSACTION_isPossibleAddAppsToContainer = 148;
        static final int TRANSACTION_isResetPersonaOnRebootEnabled = 75;
        static final int TRANSACTION_isSessionExpired = 45;
        static final int TRANSACTION_launchPersonaHome = 10;
        static final int TRANSACTION_lockPersona = 43;
        static final int TRANSACTION_markForRemoval = 38;
        static final int TRANSACTION_mountOldContainer = 117;
        static final int TRANSACTION_needToSkipResetOnReboot = 13;
        static final int TRANSACTION_notifyKeyguardShow = 77;
        static final int TRANSACTION_onKeyguardBackPressed = 116;
        static final int TRANSACTION_onWakeLockChange = 64;
        static final int TRANSACTION_refreshTimer = 62;
        static final int TRANSACTION_registerKnoxModeChangeObserver = 33;
        static final int TRANSACTION_registerSystemPersonaObserver = 34;
        static final int TRANSACTION_registerUser = 7;
        static final int TRANSACTION_removeAppForPersona = 51;
        static final int TRANSACTION_removeKnoxAppsinFota = 141;
        static final int TRANSACTION_removePackageFromInstallWhiteList = 57;
        static final int TRANSACTION_removePersona = 11;
        static final int TRANSACTION_resetPassword = 55;
        static final int TRANSACTION_resetPersona = 35;
        static final int TRANSACTION_resetPersonaOnReboot = 73;
        static final int TRANSACTION_resetPersonaPassword = 131;
        static final int TRANSACTION_savePasswordInTima = 54;
        static final int TRANSACTION_setAccessPermission = 109;
        static final int TRANSACTION_setAttribute = 5;
        static final int TRANSACTION_setBackPressed = 72;
        static final int TRANSACTION_setFidoRpContext = 152;
        static final int TRANSACTION_setFingerCount = 104;
        static final int TRANSACTION_setFingerprintHash = 130;
        static final int TRANSACTION_setFingerprintIndex = 126;
        static final int TRANSACTION_setFocusedUser = 115;
        static final int TRANSACTION_setFsMountState = 68;
        static final int TRANSACTION_setIsAdminLockedJustBefore = 100;
        static final int TRANSACTION_setIsFingerAsSupplement = 84;
        static final int TRANSACTION_setIsFingerIdentifyFailed = 102;
        static final int TRANSACTION_setIsFingerReset = 96;
        static final int TRANSACTION_setIsFingerTimeout = 92;
        static final int TRANSACTION_setIsIrisReset = 98;
        static final int TRANSACTION_setIsIrisTimeout = 94;
        static final int TRANSACTION_setIsQuickAccessUIEnabled = 90;
        static final int TRANSACTION_setIsUnlockedAfterTurnOn = 88;
        static final int TRANSACTION_setKnoxBackupPin = 120;
        static final int TRANSACTION_setKnoxSecurityTimeout = 112;
        static final int TRANSACTION_setLastKeyguardUnlockTime = 86;
        static final int TRANSACTION_setMaximumScreenOffTimeoutFromDeviceAdmin = 60;
        static final int TRANSACTION_setMoveToKnoxStatus = 23;
        static final int TRANSACTION_setPersonaIcon = 20;
        static final int TRANSACTION_setPersonaName = 25;
        static final int TRANSACTION_setPersonaSamsungAccount = 41;
        static final int TRANSACTION_setPersonaType = 27;
        static final int TRANSACTION_setShownHelp = 108;
        static final int TRANSACTION_settingSyncAllowed = 49;
        static final int TRANSACTION_setupComplete = 132;
        static final int TRANSACTION_showKeyguard = 76;
        static final int TRANSACTION_startActivityThroughPersona = 158;
        static final int TRANSACTION_switchPersonaAndLaunch = 9;
        static final int TRANSACTION_unInstallSystemApplications = 30;
        static final int TRANSACTION_unmarkForRemoval = 39;
        static final int TRANSACTION_unmountOldContainer = 118;
        static final int TRANSACTION_updatePersonaInfo = 74;
        static final int TRANSACTION_userActivity = 63;
        static final int TRANSACTION_verifyKnoxBackupPin = 119;

        private static class Proxy implements ISemPersonaManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public void addAppForPersona(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bitmap addLockOnImage(Bitmap bitmap) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bitmap != null) {
                        obtain.writeInt(1);
                        bitmap.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    Bitmap bitmap2 = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addPackageToInstallWhiteList(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(56, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addPackageToNonSecureAppList(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_addPackageToNonSecureAppList, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean adminLockPersona(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean adminUnLockPersona(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean broadcastIntentThroughPersona(Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_broadcastIntentThroughPersona, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean canAccess(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(110, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearAppListForPersona(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearNonSecureAppList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_clearNonSecureAppList, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void convertContainerType(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(82, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int createPersona(String str, String str2, long j, String str3, String str4, Uri uri, String str5, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeLong(j);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str5);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean disablePersonaKeyGuard(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(66, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void doWhenUnlock(int i, SemIUnlockAction semIUnlockAction) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (semIUnlockAction != null) {
                        iBinder = semIUnlockAction.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(78, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean enablePersonaKeyGuard(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(67, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean exists(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SemPersonaState fireEvent(PersonaNewEvent personaNewEvent, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (personaNewEvent != null) {
                        obtain.writeInt(1);
                        personaNewEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    SemPersonaState semPersonaState = obtain2.readInt() != 0 ? (SemPersonaState) SemPersonaState.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return semPersonaState;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getAdminUidForPersona(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getAppListForPersona(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getAppPackageNamesAllWhiteLists(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getAppPackageNamesAllWhiteLists, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getContainerAppIcon(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(150, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getContainerHideUsageStatsApps() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getContainerHideUsageStatsApps, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getContainerName(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getContainerName, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getContainerOrder(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getContainerOrder, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCustomBadgedResourceIdIconifRequired(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getCustomBadgedResourceIdIconifRequired, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getDefaultQuickSettings() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getDefaultQuickSettings, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getDisabledHomeLaunchers(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(65, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getECBadge(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getECBadge, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getECIcon(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getECIcon, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getECName(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getECName, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getFidoRpContext(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getFidoRpContext, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getFingerCount(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(103, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getFingerprintHash(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getFingerprintHash, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getFingerprintIndex(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(128, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getFocusedUser() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(114, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getForegroundUser() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(113, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public boolean getIsAdminLockedJustBefore(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(99, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsFingerAsSupplement(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(83, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsFingerIdentifyFailed(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(101, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsFingerReset(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(95, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsFingerTimeout(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(91, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsIrisReset(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(97, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsIrisTimeout(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(93, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsQuickAccessUIEnabled(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(89, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getIsUnlockedAfterTurnOn(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(87, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getKeyguardShowState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(79, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bitmap getKnoxIconChanged(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(122, obtain, obtain2, 0);
                    obtain2.readException();
                    Bitmap bitmap = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bitmap getKnoxIconChangedAsUser(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_getKnoxIconChangedAsUser, obtain, obtain2, 0);
                    obtain2.readException();
                    Bitmap bitmap = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getKnoxNameChanged(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(121, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getKnoxNameChangedAsUser(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(123, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getKnoxSecurityTimeout(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(111, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getLastKeyguardUnlockTime(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(85, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getMoveToKnoxStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getMyknoxId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getMyknoxId, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getNonSecureAppList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getNonSecureAppList, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getNormalizedState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public PackageInfo getPackageInfo(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_getPackageInfo, obtain, obtain2, 0);
                    obtain2.readException();
                    PackageInfo packageInfo = obtain2.readInt() != 0 ? (PackageInfo) PackageInfo.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return packageInfo;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getPackagesFromInstallWhiteList(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getParentId(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getParentUserForCurrentPersona() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getPasswordHint() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(69, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getPersonaBackgroundTime(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bitmap getPersonaIcon(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    Bitmap bitmap = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getPersonaIdentification(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getPersonaIds() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SemPersonaInfo getPersonaInfo(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    SemPersonaInfo semPersonaInfo = obtain2.readInt() != 0 ? (SemPersonaInfo) SemPersonaInfo.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return semPersonaInfo;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getPersonaSamsungAccount(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getPersonaType(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<SemPersonaInfo> getPersonas(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    List<SemPersonaInfo> createTypedArrayList = obtain2.createTypedArrayList(SemPersonaInfo.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<SemPersonaInfo> getPersonasForCreator(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    List<SemPersonaInfo> createTypedArrayList = obtain2.createTypedArrayList(SemPersonaInfo.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<SemPersonaInfo> getPersonasForUser(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    List<SemPersonaInfo> createTypedArrayList = obtain2.createTypedArrayList(SemPersonaInfo.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SemPersonaState getPreviousState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    SemPersonaState semPersonaState = obtain2.readInt() != 0 ? (SemPersonaState) SemPersonaState.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return semPersonaState;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getScreenOffTime(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(61, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getSecureFolderId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSecureFolderId, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SemPersonaState getState(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    SemPersonaState semPersonaState = obtain2.readInt() != 0 ? (SemPersonaState) SemPersonaState.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return semPersonaState;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<SemPersonaInfo> getUserManagedPersonas(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                    List<SemPersonaInfo> createTypedArrayList = obtain2.createTypedArrayList(SemPersonaInfo.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean handleHomeShow() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void handleNotificationWhenUnlock(int i, PendingIntent pendingIntent, Bundle bundle, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(160, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void hideScrim() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(81, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean inState(SemPersonaState semPersonaState, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (semPersonaState != null) {
                        obtain.writeInt(1);
                        semPersonaState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean installApplications(int i, List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStringList(list);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isAttribute(PersonaAttribute personaAttribute, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (personaAttribute != null) {
                        obtain.writeInt(1);
                        personaAttribute.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isBootCompleted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isBootCompleted, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isECContainer(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_isECContainer, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isEnabledFingerprintIndex(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(127, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isExternalStorageEnabled(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_isExternalStorageEnabled, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isFOTAUpgrade() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isFingerLockscreenActivated(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(106, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isFingerSupplementActivated(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(105, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isFotaUpgradeVersionChanged() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isFotaUpgradeVersionChanged, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isIrisLockscreenActivated(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(107, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isKioskContainerExistOnDevice() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(71, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isKioskModeEnabled(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(70, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isKnoxKeyguardShown(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(80, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isKnoxMultiWindowExist() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(153, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isNFCAllowed(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(125, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isPackageInInstallWhiteList(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(58, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isPossibleAddAppsToContainer(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_isPossibleAddAppsToContainer, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isResetPersonaOnRebootEnabled(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(75, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isSessionExpired(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean launchPersonaHome(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void lockPersona(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void markForRemoval(int i, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean mountOldContainer(String str, String str2, String str3, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_mountOldContainer, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean needToSkipResetOnReboot() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyKeyguardShow(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(77, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onKeyguardBackPressed(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_onKeyguardBackPressed, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onWakeLockChange(boolean z, int i, int i2, int i3, String str) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i4 = 1;
                    }
                    obtain.writeInt(i4);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeString(str);
                    this.mRemote.transact(64, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void refreshTimer(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(62, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean registerKnoxModeChangeObserver(IKnoxModeChangeObserver iKnoxModeChangeObserver) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iKnoxModeChangeObserver != null) {
                        iBinder = iKnoxModeChangeObserver.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean registerSystemPersonaObserver(ISystemPersonaObserver iSystemPersonaObserver) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSystemPersonaObserver != null) {
                        iBinder = iSystemPersonaObserver.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean registerUser(IPersonaCallback iPersonaCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iPersonaCallback != null) {
                        iBinder = iPersonaCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeAppForPersona(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeKnoxAppsinFota(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_removeKnoxAppsinFota, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removePackageFromInstallWhiteList(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int removePersona(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean resetPassword(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int resetPersona(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean resetPersonaOnReboot(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(73, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void resetPersonaPassword(int i, String str, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_resetPersonaPassword, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean savePasswordInTima(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAccessPermission(String str, int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(109, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setAttribute(PersonaAttribute personaAttribute, boolean z, int i) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (personaAttribute != null) {
                        obtain.writeInt(1);
                        personaAttribute.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setBackPressed(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(72, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFidoRpContext(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_setFidoRpContext, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFingerCount(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(104, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFingerprintHash(int i, List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStringList(list);
                    this.mRemote.transact(Stub.TRANSACTION_setFingerprintHash, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFingerprintIndex(int i, boolean z, int[] iArr) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(Stub.TRANSACTION_setFingerprintIndex, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFocusedUser(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setFocusedUser, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFsMountState(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(68, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsAdminLockedJustBefore(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(100, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsFingerAsSupplement(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(84, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsFingerIdentifyFailed(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(102, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsFingerReset(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(96, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsFingerTimeout(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(92, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsIrisReset(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(98, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsIrisTimeout(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(94, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsQuickAccessUIEnabled(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(90, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setIsUnlockedAfterTurnOn(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(88, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setKnoxBackupPin(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(120, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setKnoxSecurityTimeout(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(112, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setLastKeyguardUnlockTime(int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    this.mRemote.transact(86, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMaximumScreenOffTimeoutFromDeviceAdmin(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    this.mRemote.transact(60, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMoveToKnoxStatus(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPersonaIcon(int i, Bitmap bitmap) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bitmap != null) {
                        obtain.writeInt(1);
                        bitmap.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPersonaName(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPersonaSamsungAccount(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPersonaType(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setShownHelp(int i, int i2, boolean z) throws RemoteException {
                int i3 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (z) {
                        i3 = 1;
                    }
                    obtain.writeInt(i3);
                    this.mRemote.transact(108, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean settingSyncAllowed(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setupComplete(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setupComplete, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showKeyguard(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(76, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean startActivityThroughPersona(Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_startActivityThroughPersona, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean switchPersonaAndLaunch(int i, Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int unInstallSystemApplications(int i, List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStringList(list);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unmarkForRemoval(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean unmountOldContainer(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_unmountOldContainer, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean updatePersonaInfo(int i, String str, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(74, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void userActivity(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(63, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean verifyKnoxBackupPin(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_verifyKnoxBackupPin, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISemPersonaManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemPersonaManager)) ? new Proxy(iBinder) : (ISemPersonaManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            SemPersonaState state;
            boolean inState;
            int createPersona;
            List personas;
            Bitmap personaIcon;
            String personaType;
            long personaBackgroundTime;
            int[] personaIds;
            List appListForPersona;
            byte[] eCBadge;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    state = getState(parcel.readInt());
                    parcel2.writeNoException();
                    if (state != null) {
                        parcel2.writeInt(1);
                        state.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    state = getPreviousState(parcel.readInt());
                    parcel2.writeNoException();
                    if (state != null) {
                        parcel2.writeInt(1);
                        state.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = inState(parcel.readInt() != 0 ? (SemPersonaState) SemPersonaState.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    state = fireEvent(parcel.readInt() != 0 ? (PersonaNewEvent) PersonaNewEvent.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    if (state != null) {
                        parcel2.writeInt(1);
                        state.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = setAttribute(parcel.readInt() != 0 ? (PersonaAttribute) PersonaAttribute.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0, parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isAttribute(parcel.readInt() != 0 ? (PersonaAttribute) PersonaAttribute.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = registerUser(android.content.pm.IPersonaCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = createPersona(parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = switchPersonaAndLaunch(parcel.readInt(), parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = launchPersonaHome(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = removePersona(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isFOTAUpgrade();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = needToSkipResetOnReboot();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    personas = getPersonas(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(personas);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    SemPersonaInfo personaInfo = getPersonaInfo(parcel.readInt());
                    parcel2.writeNoException();
                    if (personaInfo != null) {
                        parcel2.writeInt(1);
                        personaInfo.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = exists(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    personas = getPersonasForUser(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(personas);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    personas = getPersonasForCreator(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(personas);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getParentUserForCurrentPersona();
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPersonaIcon(parcel.readInt(), parcel.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaIcon = getPersonaIcon(parcel.readInt());
                    parcel2.writeNoException();
                    if (personaIcon != null) {
                        parcel2.writeInt(1);
                        personaIcon.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getParentId(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMoveToKnoxStatus(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getMoveToKnoxStatus();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPersonaName(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getPersonaType(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPersonaType(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getNormalizedState(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = installApplications(parcel.readInt(), parcel.createStringArrayList());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = unInstallSystemApplications(parcel.readInt(), parcel.createStringArrayList());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaIcon = addLockOnImage(parcel.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (personaIcon != null) {
                        parcel2.writeInt(1);
                        personaIcon.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaBackgroundTime = getPersonaBackgroundTime(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeLong(personaBackgroundTime);
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = registerKnoxModeChangeObserver(android.content.pm.IKnoxModeChangeObserver.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = registerSystemPersonaObserver(android.content.pm.ISystemPersonaObserver.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = resetPersona(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getPersonaIdentification(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getAdminUidForPersona(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    markForRemoval(parcel.readInt(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    unmarkForRemoval(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getPersonaSamsungAccount(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case 41:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPersonaSamsungAccount(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 42:
                    parcel.enforceInterface(DESCRIPTOR);
                    personas = getUserManagedPersonas(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeTypedList(personas);
                    return true;
                case 43:
                    parcel.enforceInterface(DESCRIPTOR);
                    lockPersona(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 44:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = handleHomeShow();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 45:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isSessionExpired(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 46:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = adminLockPersona(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 47:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = adminUnLockPersona(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 48:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaIds = getPersonaIds();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(personaIds);
                    return true;
                case 49:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = settingSyncAllowed(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 50:
                    parcel.enforceInterface(DESCRIPTOR);
                    addAppForPersona(parcel.readString(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 51:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeAppForPersona(parcel.readString(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 52:
                    parcel.enforceInterface(DESCRIPTOR);
                    appListForPersona = getAppListForPersona(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStringList(appListForPersona);
                    return true;
                case 53:
                    parcel.enforceInterface(DESCRIPTOR);
                    clearAppListForPersona(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 54:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = savePasswordInTima(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 55:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = resetPassword(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 56:
                    parcel.enforceInterface(DESCRIPTOR);
                    addPackageToInstallWhiteList(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 57:
                    parcel.enforceInterface(DESCRIPTOR);
                    removePackageFromInstallWhiteList(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 58:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isPackageInInstallWhiteList(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 59:
                    parcel.enforceInterface(DESCRIPTOR);
                    appListForPersona = getPackagesFromInstallWhiteList(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStringList(appListForPersona);
                    return true;
                case 60:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMaximumScreenOffTimeoutFromDeviceAdmin(parcel.readLong(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 61:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaBackgroundTime = getScreenOffTime(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeLong(personaBackgroundTime);
                    return true;
                case 62:
                    parcel.enforceInterface(DESCRIPTOR);
                    refreshTimer(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 63:
                    parcel.enforceInterface(DESCRIPTOR);
                    userActivity(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 64:
                    parcel.enforceInterface(DESCRIPTOR);
                    onWakeLockChange(parcel.readInt() != 0, parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 65:
                    parcel.enforceInterface(DESCRIPTOR);
                    appListForPersona = getDisabledHomeLaunchers(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeStringList(appListForPersona);
                    return true;
                case 66:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = disablePersonaKeyGuard(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 67:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = enablePersonaKeyGuard(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 68:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFsMountState(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 69:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getPasswordHint();
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case 70:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isKioskModeEnabled(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 71:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isKioskContainerExistOnDevice();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 72:
                    parcel.enforceInterface(DESCRIPTOR);
                    setBackPressed(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 73:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = resetPersonaOnReboot(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 74:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = updatePersonaInfo(parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 75:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isResetPersonaOnRebootEnabled(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 76:
                    parcel.enforceInterface(DESCRIPTOR);
                    showKeyguard(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 77:
                    parcel.enforceInterface(DESCRIPTOR);
                    notifyKeyguardShow(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 78:
                    parcel.enforceInterface(DESCRIPTOR);
                    doWhenUnlock(parcel.readInt(), com.samsung.android.knox.SemIUnlockAction.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 79:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getKeyguardShowState(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 80:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isKnoxKeyguardShown(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 81:
                    parcel.enforceInterface(DESCRIPTOR);
                    hideScrim();
                    parcel2.writeNoException();
                    return true;
                case 82:
                    parcel.enforceInterface(DESCRIPTOR);
                    convertContainerType(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 83:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsFingerAsSupplement(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 84:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsFingerAsSupplement(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 85:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaBackgroundTime = getLastKeyguardUnlockTime(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeLong(personaBackgroundTime);
                    return true;
                case 86:
                    parcel.enforceInterface(DESCRIPTOR);
                    setLastKeyguardUnlockTime(parcel.readInt(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 87:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsUnlockedAfterTurnOn(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 88:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsUnlockedAfterTurnOn(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 89:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsQuickAccessUIEnabled(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 90:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsQuickAccessUIEnabled(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 91:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsFingerTimeout(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 92:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsFingerTimeout(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 93:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsIrisTimeout(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 94:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsIrisTimeout(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 95:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsFingerReset(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 96:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsFingerReset(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 97:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsIrisReset(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 98:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsIrisReset(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 99:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsAdminLockedJustBefore(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 100:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsAdminLockedJustBefore(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 101:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = getIsFingerIdentifyFailed(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 102:
                    parcel.enforceInterface(DESCRIPTOR);
                    setIsFingerIdentifyFailed(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 103:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getFingerCount(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 104:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFingerCount(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 105:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isFingerSupplementActivated(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 106:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isFingerLockscreenActivated(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 107:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isIrisLockscreenActivated(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 108:
                    parcel.enforceInterface(DESCRIPTOR);
                    setShownHelp(parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 109:
                    parcel.enforceInterface(DESCRIPTOR);
                    setAccessPermission(parcel.readString(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 110:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = canAccess(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 111:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getKnoxSecurityTimeout(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 112:
                    parcel.enforceInterface(DESCRIPTOR);
                    setKnoxSecurityTimeout(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 113:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getForegroundUser();
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 114:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getFocusedUser();
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case TRANSACTION_setFocusedUser /*115*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFocusedUser(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onKeyguardBackPressed /*116*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onKeyguardBackPressed(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_mountOldContainer /*117*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = mountOldContainer(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_unmountOldContainer /*118*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = unmountOldContainer(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_verifyKnoxBackupPin /*119*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = verifyKnoxBackupPin(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 120:
                    parcel.enforceInterface(DESCRIPTOR);
                    setKnoxBackupPin(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 121:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getKnoxNameChanged(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case 122:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaIcon = getKnoxIconChanged(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    if (personaIcon != null) {
                        parcel2.writeInt(1);
                        personaIcon.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 123:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getKnoxNameChangedAsUser(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case TRANSACTION_getKnoxIconChangedAsUser /*124*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaIcon = getKnoxIconChangedAsUser(parcel.readInt());
                    parcel2.writeNoException();
                    if (personaIcon != null) {
                        parcel2.writeInt(1);
                        personaIcon.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 125:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isNFCAllowed(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_setFingerprintIndex /*126*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFingerprintIndex(parcel.readInt(), parcel.readInt() != 0, parcel.createIntArray());
                    parcel2.writeNoException();
                    return true;
                case 127:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isEnabledFingerprintIndex(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 128:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaIds = getFingerprintIndex(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeIntArray(personaIds);
                    return true;
                case TRANSACTION_getFingerprintHash /*129*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    appListForPersona = getFingerprintHash(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStringList(appListForPersona);
                    return true;
                case TRANSACTION_setFingerprintHash /*130*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFingerprintHash(parcel.readInt(), parcel.createStringArrayList());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_resetPersonaPassword /*131*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    resetPersonaPassword(parcel.readInt(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_setupComplete /*132*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    setupComplete(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getCustomBadgedResourceIdIconifRequired /*133*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getCustomBadgedResourceIdIconifRequired(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case TRANSACTION_getDefaultQuickSettings /*134*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getDefaultQuickSettings();
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case TRANSACTION_getPackageInfo /*135*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    PackageInfo packageInfo = getPackageInfo(parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    if (packageInfo != null) {
                        parcel2.writeInt(1);
                        packageInfo.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case TRANSACTION_getContainerHideUsageStatsApps /*136*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    appListForPersona = getContainerHideUsageStatsApps();
                    parcel2.writeNoException();
                    parcel2.writeStringList(appListForPersona);
                    return true;
                case TRANSACTION_addPackageToNonSecureAppList /*137*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    addPackageToNonSecureAppList(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getNonSecureAppList /*138*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    appListForPersona = getNonSecureAppList();
                    parcel2.writeNoException();
                    parcel2.writeStringList(appListForPersona);
                    return true;
                case TRANSACTION_clearNonSecureAppList /*139*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    clearNonSecureAppList();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_isFotaUpgradeVersionChanged /*140*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isFotaUpgradeVersionChanged();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_removeKnoxAppsinFota /*141*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeKnoxAppsinFota(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getSecureFolderId /*142*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getSecureFolderId();
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case TRANSACTION_getECName /*143*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getECName(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case TRANSACTION_getECBadge /*144*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    eCBadge = getECBadge(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(eCBadge);
                    return true;
                case TRANSACTION_getECIcon /*145*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    eCBadge = getECIcon(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(eCBadge);
                    return true;
                case TRANSACTION_isECContainer /*146*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isECContainer(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_getContainerName /*147*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getContainerName(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case TRANSACTION_isPossibleAddAppsToContainer /*148*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isPossibleAddAppsToContainer(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_getMyknoxId /*149*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getMyknoxId();
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case 150:
                    parcel.enforceInterface(DESCRIPTOR);
                    eCBadge = getContainerAppIcon(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(eCBadge);
                    return true;
                case TRANSACTION_getFidoRpContext /*151*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    personaType = getFidoRpContext(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(personaType);
                    return true;
                case TRANSACTION_setFidoRpContext /*152*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFidoRpContext(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 153:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isKnoxMultiWindowExist();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_getContainerOrder /*154*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    createPersona = getContainerOrder(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(createPersona);
                    return true;
                case TRANSACTION_isBootCompleted /*155*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isBootCompleted();
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_isExternalStorageEnabled /*156*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = isExternalStorageEnabled(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_getAppPackageNamesAllWhiteLists /*157*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    appListForPersona = getAppPackageNamesAllWhiteLists(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStringList(appListForPersona);
                    return true;
                case TRANSACTION_startActivityThroughPersona /*158*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = startActivityThroughPersona(parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case TRANSACTION_broadcastIntentThroughPersona /*159*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    inState = broadcastIntentThroughPersona(parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(inState ? 1 : 0);
                    return true;
                case 160:
                    parcel.enforceInterface(DESCRIPTOR);
                    handleNotificationWhenUnlock(parcel.readInt(), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void addAppForPersona(String str, String str2, int i) throws RemoteException;

    Bitmap addLockOnImage(Bitmap bitmap) throws RemoteException;

    void addPackageToInstallWhiteList(String str, int i) throws RemoteException;

    void addPackageToNonSecureAppList(String str) throws RemoteException;

    boolean adminLockPersona(int i, String str) throws RemoteException;

    boolean adminUnLockPersona(int i) throws RemoteException;

    boolean broadcastIntentThroughPersona(Intent intent) throws RemoteException;

    boolean canAccess(String str, int i) throws RemoteException;

    void clearAppListForPersona(String str, int i) throws RemoteException;

    void clearNonSecureAppList() throws RemoteException;

    void convertContainerType(int i, int i2) throws RemoteException;

    int createPersona(String str, String str2, long j, String str3, String str4, Uri uri, String str5, int i) throws RemoteException;

    boolean disablePersonaKeyGuard(int i) throws RemoteException;

    void doWhenUnlock(int i, SemIUnlockAction semIUnlockAction) throws RemoteException;

    boolean enablePersonaKeyGuard(int i) throws RemoteException;

    boolean exists(int i) throws RemoteException;

    SemPersonaState fireEvent(PersonaNewEvent personaNewEvent, int i) throws RemoteException;

    int getAdminUidForPersona(int i) throws RemoteException;

    List<String> getAppListForPersona(String str, int i) throws RemoteException;

    List<String> getAppPackageNamesAllWhiteLists(int i) throws RemoteException;

    byte[] getContainerAppIcon(int i) throws RemoteException;

    List<String> getContainerHideUsageStatsApps() throws RemoteException;

    String getContainerName(int i) throws RemoteException;

    int getContainerOrder(int i) throws RemoteException;

    int getCustomBadgedResourceIdIconifRequired(String str, int i) throws RemoteException;

    String getDefaultQuickSettings() throws RemoteException;

    List<String> getDisabledHomeLaunchers(int i, boolean z) throws RemoteException;

    byte[] getECBadge(int i) throws RemoteException;

    byte[] getECIcon(int i) throws RemoteException;

    String getECName(int i) throws RemoteException;

    String getFidoRpContext(int i) throws RemoteException;

    int getFingerCount(int i) throws RemoteException;

    List<String> getFingerprintHash(int i) throws RemoteException;

    int[] getFingerprintIndex(int i) throws RemoteException;

    int getFocusedUser() throws RemoteException;

    int getForegroundUser() throws RemoteException;

    boolean getIsAdminLockedJustBefore(int i) throws RemoteException;

    boolean getIsFingerAsSupplement(int i) throws RemoteException;

    boolean getIsFingerIdentifyFailed(int i) throws RemoteException;

    boolean getIsFingerReset(int i) throws RemoteException;

    boolean getIsFingerTimeout(int i) throws RemoteException;

    boolean getIsIrisReset(int i) throws RemoteException;

    boolean getIsIrisTimeout(int i) throws RemoteException;

    boolean getIsQuickAccessUIEnabled(int i) throws RemoteException;

    boolean getIsUnlockedAfterTurnOn(int i) throws RemoteException;

    boolean getKeyguardShowState(int i) throws RemoteException;

    Bitmap getKnoxIconChanged(String str, int i) throws RemoteException;

    Bitmap getKnoxIconChangedAsUser(int i) throws RemoteException;

    String getKnoxNameChanged(String str, int i) throws RemoteException;

    String getKnoxNameChangedAsUser(int i) throws RemoteException;

    int getKnoxSecurityTimeout(int i) throws RemoteException;

    long getLastKeyguardUnlockTime(int i) throws RemoteException;

    boolean getMoveToKnoxStatus() throws RemoteException;

    int getMyknoxId() throws RemoteException;

    List<String> getNonSecureAppList() throws RemoteException;

    int getNormalizedState(int i) throws RemoteException;

    PackageInfo getPackageInfo(String str, int i, int i2) throws RemoteException;

    List<String> getPackagesFromInstallWhiteList(int i) throws RemoteException;

    int getParentId(int i) throws RemoteException;

    int getParentUserForCurrentPersona() throws RemoteException;

    String getPasswordHint() throws RemoteException;

    long getPersonaBackgroundTime(int i) throws RemoteException;

    Bitmap getPersonaIcon(int i) throws RemoteException;

    String getPersonaIdentification(int i) throws RemoteException;

    int[] getPersonaIds() throws RemoteException;

    SemPersonaInfo getPersonaInfo(int i) throws RemoteException;

    String getPersonaSamsungAccount(int i) throws RemoteException;

    String getPersonaType(int i) throws RemoteException;

    List<SemPersonaInfo> getPersonas(boolean z) throws RemoteException;

    List<SemPersonaInfo> getPersonasForCreator(int i, boolean z) throws RemoteException;

    List<SemPersonaInfo> getPersonasForUser(int i, boolean z) throws RemoteException;

    SemPersonaState getPreviousState(int i) throws RemoteException;

    long getScreenOffTime(int i) throws RemoteException;

    int getSecureFolderId() throws RemoteException;

    SemPersonaState getState(int i) throws RemoteException;

    List<SemPersonaInfo> getUserManagedPersonas(boolean z) throws RemoteException;

    boolean handleHomeShow() throws RemoteException;

    void handleNotificationWhenUnlock(int i, PendingIntent pendingIntent, Bundle bundle, String str) throws RemoteException;

    void hideScrim() throws RemoteException;

    boolean inState(SemPersonaState semPersonaState, int i) throws RemoteException;

    boolean installApplications(int i, List<String> list) throws RemoteException;

    boolean isAttribute(PersonaAttribute personaAttribute, int i) throws RemoteException;

    boolean isBootCompleted() throws RemoteException;

    boolean isECContainer(int i) throws RemoteException;

    boolean isEnabledFingerprintIndex(int i) throws RemoteException;

    boolean isExternalStorageEnabled(int i) throws RemoteException;

    boolean isFOTAUpgrade() throws RemoteException;

    boolean isFingerLockscreenActivated(int i) throws RemoteException;

    boolean isFingerSupplementActivated(int i) throws RemoteException;

    boolean isFotaUpgradeVersionChanged() throws RemoteException;

    boolean isIrisLockscreenActivated(int i) throws RemoteException;

    boolean isKioskContainerExistOnDevice() throws RemoteException;

    boolean isKioskModeEnabled(int i) throws RemoteException;

    boolean isKnoxKeyguardShown(int i) throws RemoteException;

    boolean isKnoxMultiWindowExist() throws RemoteException;

    boolean isNFCAllowed(int i) throws RemoteException;

    boolean isPackageInInstallWhiteList(String str, int i) throws RemoteException;

    boolean isPossibleAddAppsToContainer(String str, int i) throws RemoteException;

    boolean isResetPersonaOnRebootEnabled(int i) throws RemoteException;

    boolean isSessionExpired(int i) throws RemoteException;

    boolean launchPersonaHome(int i) throws RemoteException;

    void lockPersona(int i) throws RemoteException;

    void markForRemoval(int i, ComponentName componentName) throws RemoteException;

    boolean mountOldContainer(String str, String str2, String str3, int i, int i2) throws RemoteException;

    boolean needToSkipResetOnReboot() throws RemoteException;

    void notifyKeyguardShow(int i, boolean z) throws RemoteException;

    void onKeyguardBackPressed(int i) throws RemoteException;

    void onWakeLockChange(boolean z, int i, int i2, int i3, String str) throws RemoteException;

    void refreshTimer(int i) throws RemoteException;

    boolean registerKnoxModeChangeObserver(IKnoxModeChangeObserver iKnoxModeChangeObserver) throws RemoteException;

    boolean registerSystemPersonaObserver(ISystemPersonaObserver iSystemPersonaObserver) throws RemoteException;

    boolean registerUser(IPersonaCallback iPersonaCallback) throws RemoteException;

    void removeAppForPersona(String str, String str2, int i) throws RemoteException;

    void removeKnoxAppsinFota(int i) throws RemoteException;

    void removePackageFromInstallWhiteList(String str, int i) throws RemoteException;

    int removePersona(int i) throws RemoteException;

    boolean resetPassword(String str) throws RemoteException;

    int resetPersona(int i) throws RemoteException;

    boolean resetPersonaOnReboot(int i, boolean z) throws RemoteException;

    void resetPersonaPassword(int i, String str, int i2) throws RemoteException;

    boolean savePasswordInTima(int i, String str) throws RemoteException;

    void setAccessPermission(String str, int i, boolean z) throws RemoteException;

    boolean setAttribute(PersonaAttribute personaAttribute, boolean z, int i) throws RemoteException;

    void setBackPressed(int i, boolean z) throws RemoteException;

    void setFidoRpContext(int i, String str) throws RemoteException;

    void setFingerCount(int i, int i2) throws RemoteException;

    void setFingerprintHash(int i, List<String> list) throws RemoteException;

    void setFingerprintIndex(int i, boolean z, int[] iArr) throws RemoteException;

    void setFocusedUser(int i) throws RemoteException;

    void setFsMountState(int i, boolean z) throws RemoteException;

    void setIsAdminLockedJustBefore(int i, boolean z) throws RemoteException;

    void setIsFingerAsSupplement(int i, boolean z) throws RemoteException;

    void setIsFingerIdentifyFailed(int i, boolean z) throws RemoteException;

    void setIsFingerReset(int i, boolean z) throws RemoteException;

    void setIsFingerTimeout(int i, boolean z) throws RemoteException;

    void setIsIrisReset(int i, boolean z) throws RemoteException;

    void setIsIrisTimeout(int i, boolean z) throws RemoteException;

    void setIsQuickAccessUIEnabled(int i, boolean z) throws RemoteException;

    void setIsUnlockedAfterTurnOn(int i, boolean z) throws RemoteException;

    void setKnoxBackupPin(int i, String str) throws RemoteException;

    void setKnoxSecurityTimeout(int i, int i2) throws RemoteException;

    void setLastKeyguardUnlockTime(int i, long j) throws RemoteException;

    void setMaximumScreenOffTimeoutFromDeviceAdmin(long j, int i) throws RemoteException;

    void setMoveToKnoxStatus(boolean z) throws RemoteException;

    void setPersonaIcon(int i, Bitmap bitmap) throws RemoteException;

    void setPersonaName(int i, String str) throws RemoteException;

    void setPersonaSamsungAccount(int i, String str) throws RemoteException;

    void setPersonaType(int i, String str) throws RemoteException;

    void setShownHelp(int i, int i2, boolean z) throws RemoteException;

    boolean settingSyncAllowed(int i) throws RemoteException;

    void setupComplete(int i) throws RemoteException;

    void showKeyguard(int i, int i2) throws RemoteException;

    boolean startActivityThroughPersona(Intent intent) throws RemoteException;

    boolean switchPersonaAndLaunch(int i, Intent intent) throws RemoteException;

    int unInstallSystemApplications(int i, List<String> list) throws RemoteException;

    void unmarkForRemoval(int i) throws RemoteException;

    boolean unmountOldContainer(String str) throws RemoteException;

    boolean updatePersonaInfo(int i, String str, int i2, int i3) throws RemoteException;

    void userActivity(int i) throws RemoteException;

    boolean verifyKnoxBackupPin(int i, String str) throws RemoteException;
}
