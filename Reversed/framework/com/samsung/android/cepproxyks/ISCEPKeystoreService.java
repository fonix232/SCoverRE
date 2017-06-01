package com.samsung.android.cepproxyks;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISCEPKeystoreService extends IInterface {

    public static abstract class Stub extends Binder implements ISCEPKeystoreService {
        private static final String DESCRIPTOR = "com.samsung.android.cepproxyks.ISCEPKeystoreService";
        static final int TRANSACTION_deletecertificateEntry = 3;
        static final int TRANSACTION_getCertificate = 5;
        static final int TRANSACTION_grantAccessForAKS = 4;
        static final int TRANSACTION_installCACert = 6;
        static final int TRANSACTION_installCACertForWifiCCM = 7;
        static final int TRANSACTION_installCertificateInAndroidKeyStore = 2;
        static final int TRANSACTION_isAliasExists = 1;

        private static class Proxy implements ISCEPKeystoreService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int deletecertificateEntry(String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public CertificateAKS getCertificate(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    CertificateAKS certificateAKS = obtain2.readInt() != 0 ? (CertificateAKS) CertificateAKS.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return certificateAKS;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void grantAccessForAKS(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int installCACert(CertificateAKS certificateAKS) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (certificateAKS != null) {
                        obtain.writeInt(1);
                        certificateAKS.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int installCACertForWifiCCM(CertificateAKS certificateAKS, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (certificateAKS != null) {
                        obtain.writeInt(1);
                        certificateAKS.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int installCertificateInAndroidKeyStore(CertByte certByte, String str, char[] cArr, boolean z, int i) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (certByte != null) {
                        obtain.writeInt(1);
                        certByte.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeCharArray(cArr);
                    if (!z) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int isAliasExists(String str, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISCEPKeystoreService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISCEPKeystoreService)) ? new Proxy(iBinder) : (ISCEPKeystoreService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int isAliasExists;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    isAliasExists = isAliasExists(parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(isAliasExists);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    isAliasExists = installCertificateInAndroidKeyStore(parcel.readInt() != 0 ? (CertByte) CertByte.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.createCharArray(), parcel.readInt() != 0, parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isAliasExists);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    isAliasExists = deletecertificateEntry(parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(isAliasExists);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    grantAccessForAKS(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    CertificateAKS certificate = getCertificate(parcel.readString());
                    parcel2.writeNoException();
                    if (certificate != null) {
                        parcel2.writeInt(1);
                        certificate.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    isAliasExists = installCACert(parcel.readInt() != 0 ? (CertificateAKS) CertificateAKS.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(isAliasExists);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    isAliasExists = installCACertForWifiCCM(parcel.readInt() != 0 ? (CertificateAKS) CertificateAKS.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isAliasExists);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int deletecertificateEntry(String str, boolean z) throws RemoteException;

    CertificateAKS getCertificate(String str) throws RemoteException;

    void grantAccessForAKS(int i, String str) throws RemoteException;

    int installCACert(CertificateAKS certificateAKS) throws RemoteException;

    int installCACertForWifiCCM(CertificateAKS certificateAKS, String str) throws RemoteException;

    int installCertificateInAndroidKeyStore(CertByte certByte, String str, char[] cArr, boolean z, int i) throws RemoteException;

    int isAliasExists(String str, boolean z) throws RemoteException;
}
