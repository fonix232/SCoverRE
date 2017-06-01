package com.samsung.android.knox.keystore;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.knox.ContextInfo;
import java.util.List;

public interface IClientCertificateManager extends IInterface {

    public static abstract class Stub extends Binder implements IClientCertificateManager {
        private static final String DESCRIPTOR = "com.samsung.android.knox.keystore.IClientCertificateManager";
        static final int TRANSACTION_addPackageToExemptList = 20;
        static final int TRANSACTION_deleteCCMProfile = 7;
        static final int TRANSACTION_deleteCSRProfile = 15;
        static final int TRANSACTION_deleteCertificate = 9;
        static final int TRANSACTION_generateCSR = 1;
        static final int TRANSACTION_generateCSRUsingByteArray = 17;
        static final int TRANSACTION_generateCSRUsingString = 18;
        static final int TRANSACTION_generateCSRUsingTemplate = 16;
        static final int TRANSACTION_generateKeyPair = 4;
        static final int TRANSACTION_getAliasesForCaller = 25;
        static final int TRANSACTION_getAliasesForPackage = 37;
        static final int TRANSACTION_getAliasesForWiFi = 27;
        static final int TRANSACTION_getCCMProfile = 6;
        static final int TRANSACTION_getCCMVersion = 22;
        static final int TRANSACTION_getCertificateAliases = 32;
        static final int TRANSACTION_getCertificateAliasesHavingPrivateKey = 33;
        static final int TRANSACTION_getDefaultCertificateAlias = 24;
        static final int TRANSACTION_getKeyChainMarkedAliases = 30;
        static final int TRANSACTION_getSlotIdForCaller = 12;
        static final int TRANSACTION_getSlotIdForPackage = 13;
        static final int TRANSACTION_hasGrant = 42;
        static final int TRANSACTION_insertOrUpdateCCMProfile = 35;
        static final int TRANSACTION_insertOrUpdateCertificateProfile = 36;
        static final int TRANSACTION_installCertificate = 8;
        static final int TRANSACTION_installKeyPair = 39;
        static final int TRANSACTION_installObject = 3;
        static final int TRANSACTION_installObjectWithProfile = 34;
        static final int TRANSACTION_installObjectWithType = 23;
        static final int TRANSACTION_isAccessControlMethodPassword = 26;
        static final int TRANSACTION_isCCMEmptyForKeyChain = 31;
        static final int TRANSACTION_isCCMPolicyEnabledByAdmin = 19;
        static final int TRANSACTION_isCCMPolicyEnabledForCaller = 10;
        static final int TRANSACTION_isCCMPolicyEnabledForPackage = 11;
        static final int TRANSACTION_isKeyChainMarkedAlias = 28;
        static final int TRANSACTION_keychainMarkedReset = 29;
        static final int TRANSACTION_registerForDefaultCertificate = 2;
        static final int TRANSACTION_removeKeyPair = 40;
        static final int TRANSACTION_removePackageFromExemptList = 21;
        static final int TRANSACTION_setCCMProfile = 5;
        static final int TRANSACTION_setCSRProfile = 14;
        static final int TRANSACTION_setDefaultCCMProfile = 38;
        static final int TRANSACTION_setGrant = 41;

        private static class Proxy implements IClientCertificateManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public boolean addPackageToExemptList(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(20, obtain, obtain2, 0);
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

            public boolean deleteCCMProfile(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

            public boolean deleteCSRProfile(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(15, obtain, obtain2, 0);
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

            public boolean deleteCertificate(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
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

            public byte[] generateCSR(ContextInfo contextInfo, int i, String str, String str2, String str3, boolean z) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    if (!z) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] generateCSRUsingByteArray(ContextInfo contextInfo, String str, String str2, byte[] bArr, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] generateCSRUsingString(ContextInfo contextInfo, String str, String str2, String str3, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeInt(i);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] generateCSRUsingTemplate(ContextInfo contextInfo, String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] generateKeyPair(ContextInfo contextInfo, String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getAliasesForCaller(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getAliasesForPackage(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getAliasesForWiFi() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public CCMProfile getCCMProfile(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    CCMProfile cCMProfile = obtain2.readInt() != 0 ? (CCMProfile) CCMProfile.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return cCMProfile;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCCMVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getCertificateAliases(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getCertificateAliasesHavingPrivateKey(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getDefaultCertificateAlias() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public List<String> getKeyChainMarkedAliases(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getSlotIdForCaller(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getSlotIdForPackage(ContextInfo contextInfo, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean hasGrant(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(42, obtain, obtain2, 0);
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

            public boolean insertOrUpdateCCMProfile(ContextInfo contextInfo, CCMProfile cCMProfile) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (cCMProfile != null) {
                        obtain.writeInt(1);
                        cCMProfile.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(35, obtain, obtain2, 0);
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

            public boolean insertOrUpdateCertificateProfile(ContextInfo contextInfo, CertificateProfile certificateProfile) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (certificateProfile != null) {
                        obtain.writeInt(1);
                        certificateProfile.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(36, obtain, obtain2, 0);
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

            public boolean installCertificate(ContextInfo contextInfo, CertificateProfile certificateProfile, byte[] bArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (certificateProfile != null) {
                        obtain.writeInt(1);
                        certificateProfile.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
                    this.mRemote.transact(8, obtain, obtain2, 0);
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

            public boolean installKeyPair(ContextInfo contextInfo, byte[] bArr, byte[] bArr2, byte[] bArr3, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    obtain.writeByteArray(bArr3);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(39, obtain, obtain2, 0);
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

            public int installObject(ContextInfo contextInfo, int i, String str, String str2, String str3, int i2, byte[] bArr, String str4, boolean z) throws RemoteException {
                int i3 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeInt(i2);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str4);
                    if (!z) {
                        i3 = 0;
                    }
                    obtain.writeInt(i3);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean installObjectWithProfile(ContextInfo contextInfo, CertificateProfile certificateProfile, int i, byte[] bArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (certificateProfile != null) {
                        obtain.writeInt(1);
                        certificateProfile.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
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

            public boolean installObjectWithType(ContextInfo contextInfo, String str, int i, byte[] bArr, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str2);
                    this.mRemote.transact(23, obtain, obtain2, 0);
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

            public boolean isAccessControlMethodPassword(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(26, obtain, obtain2, 0);
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

            public boolean isCCMEmptyForKeyChain(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(31, obtain, obtain2, 0);
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

            public boolean isCCMPolicyEnabledByAdmin(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(19, obtain, obtain2, 0);
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

            public boolean isCCMPolicyEnabledForCaller(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

            public boolean isCCMPolicyEnabledForPackage(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(11, obtain, obtain2, 0);
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

            public boolean isKeyChainMarkedAlias(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(28, obtain, obtain2, 0);
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

            public boolean keychainMarkedReset(ContextInfo contextInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

            public int registerForDefaultCertificate(ContextInfo contextInfo, int i, String str, String str2, boolean z) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (!z) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean removeKeyPair(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(40, obtain, obtain2, 0);
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

            public boolean removePackageFromExemptList(ContextInfo contextInfo, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(21, obtain, obtain2, 0);
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

            public boolean setCCMProfile(ContextInfo contextInfo, CCMProfile cCMProfile) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (cCMProfile != null) {
                        obtain.writeInt(1);
                        cCMProfile.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(5, obtain, obtain2, 0);
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

            public boolean setCSRProfile(ContextInfo contextInfo, CSRProfile cSRProfile) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contextInfo != null) {
                        obtain.writeInt(1);
                        contextInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (cSRProfile != null) {
                        obtain.writeInt(1);
                        cSRProfile.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(14, obtain, obtain2, 0);
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

            public boolean setDefaultCCMProfile() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, obtain, obtain2, 0);
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

            public void setGrant(int i, String str, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IClientCertificateManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IClientCertificateManager)) ? new Proxy(iBinder) : (IClientCertificateManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            byte[] generateCSR;
            int registerForDefaultCertificate;
            boolean cCMProfile;
            long slotIdForCaller;
            String cCMVersion;
            List aliasesForCaller;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    generateCSR = generateCSR(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeByteArray(generateCSR);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerForDefaultCertificate = registerForDefaultCertificate(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(registerForDefaultCertificate);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerForDefaultCertificate = installObject(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.createByteArray(), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(registerForDefaultCertificate);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    generateCSR = generateKeyPair(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(generateCSR);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = setCCMProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (CCMProfile) CCMProfile.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    CCMProfile cCMProfile2 = getCCMProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (cCMProfile2 != null) {
                        parcel2.writeInt(1);
                        cCMProfile2.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = deleteCCMProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = installCertificate(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (CertificateProfile) CertificateProfile.CREATOR.createFromParcel(parcel) : null, parcel.createByteArray(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = deleteCertificate(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = isCCMPolicyEnabledForCaller(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = isCCMPolicyEnabledForPackage(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    slotIdForCaller = getSlotIdForCaller(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeLong(slotIdForCaller);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    slotIdForCaller = getSlotIdForPackage(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeLong(slotIdForCaller);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = setCSRProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (CSRProfile) CSRProfile.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = deleteCSRProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    generateCSR = generateCSRUsingTemplate(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(generateCSR);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    generateCSR = generateCSRUsingByteArray(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readString(), parcel.createByteArray(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(generateCSR);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    generateCSR = generateCSRUsingString(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(generateCSR);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = isCCMPolicyEnabledByAdmin(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = addPackageToExemptList(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = removePackageFromExemptList(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMVersion = getCCMVersion();
                    parcel2.writeNoException();
                    parcel2.writeString(cCMVersion);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = installObjectWithType(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readInt(), parcel.createByteArray(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMVersion = getDefaultCertificateAlias();
                    parcel2.writeNoException();
                    parcel2.writeString(cCMVersion);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    aliasesForCaller = getAliasesForCaller(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeStringList(aliasesForCaller);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = isAccessControlMethodPassword(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    aliasesForCaller = getAliasesForWiFi();
                    parcel2.writeNoException();
                    parcel2.writeStringList(aliasesForCaller);
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = isKeyChainMarkedAlias(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = keychainMarkedReset(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    aliasesForCaller = getKeyChainMarkedAliases(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeStringList(aliasesForCaller);
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = isCCMEmptyForKeyChain(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    aliasesForCaller = getCertificateAliases(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeStringList(aliasesForCaller);
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    aliasesForCaller = getCertificateAliasesHavingPrivateKey(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeStringList(aliasesForCaller);
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = installObjectWithProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (CertificateProfile) CertificateProfile.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.createByteArray(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = insertOrUpdateCCMProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (CCMProfile) CCMProfile.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = insertOrUpdateCertificateProfile(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (CertificateProfile) CertificateProfile.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    aliasesForCaller = getAliasesForPackage(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeStringList(aliasesForCaller);
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = setDefaultCCMProfile();
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = installKeyPair(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.createByteArray(), parcel.createByteArray(), parcel.createByteArray(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = removeKeyPair(parcel.readInt() != 0 ? (ContextInfo) ContextInfo.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 41:
                    parcel.enforceInterface(DESCRIPTOR);
                    setGrant(parcel.readInt(), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 42:
                    parcel.enforceInterface(DESCRIPTOR);
                    cCMProfile = hasGrant(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(cCMProfile ? 1 : 0);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean addPackageToExemptList(ContextInfo contextInfo, String str) throws RemoteException;

    boolean deleteCCMProfile(ContextInfo contextInfo) throws RemoteException;

    boolean deleteCSRProfile(ContextInfo contextInfo, String str) throws RemoteException;

    boolean deleteCertificate(ContextInfo contextInfo, String str) throws RemoteException;

    byte[] generateCSR(ContextInfo contextInfo, int i, String str, String str2, String str3, boolean z) throws RemoteException;

    byte[] generateCSRUsingByteArray(ContextInfo contextInfo, String str, String str2, byte[] bArr, int i) throws RemoteException;

    byte[] generateCSRUsingString(ContextInfo contextInfo, String str, String str2, String str3, int i) throws RemoteException;

    byte[] generateCSRUsingTemplate(ContextInfo contextInfo, String str, String str2, String str3) throws RemoteException;

    byte[] generateKeyPair(ContextInfo contextInfo, String str, int i) throws RemoteException;

    List<String> getAliasesForCaller(ContextInfo contextInfo) throws RemoteException;

    List<String> getAliasesForPackage(ContextInfo contextInfo, String str) throws RemoteException;

    List<String> getAliasesForWiFi() throws RemoteException;

    CCMProfile getCCMProfile(ContextInfo contextInfo) throws RemoteException;

    String getCCMVersion() throws RemoteException;

    List<String> getCertificateAliases(ContextInfo contextInfo) throws RemoteException;

    List<String> getCertificateAliasesHavingPrivateKey(ContextInfo contextInfo) throws RemoteException;

    String getDefaultCertificateAlias() throws RemoteException;

    List<String> getKeyChainMarkedAliases(ContextInfo contextInfo) throws RemoteException;

    long getSlotIdForCaller(ContextInfo contextInfo, String str) throws RemoteException;

    long getSlotIdForPackage(ContextInfo contextInfo, String str, String str2) throws RemoteException;

    boolean hasGrant(String str) throws RemoteException;

    boolean insertOrUpdateCCMProfile(ContextInfo contextInfo, CCMProfile cCMProfile) throws RemoteException;

    boolean insertOrUpdateCertificateProfile(ContextInfo contextInfo, CertificateProfile certificateProfile) throws RemoteException;

    boolean installCertificate(ContextInfo contextInfo, CertificateProfile certificateProfile, byte[] bArr, String str) throws RemoteException;

    boolean installKeyPair(ContextInfo contextInfo, byte[] bArr, byte[] bArr2, byte[] bArr3, String str, String str2) throws RemoteException;

    int installObject(ContextInfo contextInfo, int i, String str, String str2, String str3, int i2, byte[] bArr, String str4, boolean z) throws RemoteException;

    boolean installObjectWithProfile(ContextInfo contextInfo, CertificateProfile certificateProfile, int i, byte[] bArr, String str) throws RemoteException;

    boolean installObjectWithType(ContextInfo contextInfo, String str, int i, byte[] bArr, String str2) throws RemoteException;

    boolean isAccessControlMethodPassword(ContextInfo contextInfo) throws RemoteException;

    boolean isCCMEmptyForKeyChain(ContextInfo contextInfo) throws RemoteException;

    boolean isCCMPolicyEnabledByAdmin(int i) throws RemoteException;

    boolean isCCMPolicyEnabledForCaller(ContextInfo contextInfo) throws RemoteException;

    boolean isCCMPolicyEnabledForPackage(ContextInfo contextInfo, String str) throws RemoteException;

    boolean isKeyChainMarkedAlias(ContextInfo contextInfo, String str) throws RemoteException;

    boolean keychainMarkedReset(ContextInfo contextInfo) throws RemoteException;

    int registerForDefaultCertificate(ContextInfo contextInfo, int i, String str, String str2, boolean z) throws RemoteException;

    boolean removeKeyPair(ContextInfo contextInfo, String str) throws RemoteException;

    boolean removePackageFromExemptList(ContextInfo contextInfo, String str) throws RemoteException;

    boolean setCCMProfile(ContextInfo contextInfo, CCMProfile cCMProfile) throws RemoteException;

    boolean setCSRProfile(ContextInfo contextInfo, CSRProfile cSRProfile) throws RemoteException;

    boolean setDefaultCCMProfile() throws RemoteException;

    void setGrant(int i, String str, boolean z) throws RemoteException;
}
