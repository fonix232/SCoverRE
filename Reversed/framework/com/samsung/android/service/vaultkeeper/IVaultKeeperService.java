package com.samsung.android.service.vaultkeeper;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVaultKeeperService extends IInterface {

    public static abstract class Stub extends Binder implements IVaultKeeperService {
        private static final String DESCRIPTOR = "com.samsung.android.service.vaultkeeper.IVaultKeeperService";
        static final int TRANSACTION_destroy = 4;
        static final int TRANSACTION_getNonce = 8;
        static final int TRANSACTION_getPackageName = 1;
        static final int TRANSACTION_initialize = 3;
        static final int TRANSACTION_isInitialized = 2;
        static final int TRANSACTION_readData = 6;
        static final int TRANSACTION_readState = 5;
        static final int TRANSACTION_verifyCertificate = 9;
        static final int TRANSACTION_verifyComplete = 11;
        static final int TRANSACTION_verifyRequest = 10;
        static final int TRANSACTION_write = 7;

        private static class Proxy implements IVaultKeeperService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int destroy(String str, String str2, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(4, obtain, obtain2, 0);
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

            public byte[] getNonce(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getPackageName(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int initialize(String str, String str2, byte[] bArr, String str3, byte[] bArr2, byte[] bArr3, byte[] bArr4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str3);
                    obtain.writeByteArray(bArr2);
                    obtain.writeByteArray(bArr3);
                    obtain.writeByteArray(bArr4);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isInitialized(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
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

            public byte[] readData(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String readState(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean verifyCertificate(String str, String str2, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
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

            public int verifyComplete(String str, String str2, byte[] bArr, String str3, byte[] bArr2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str3);
                    obtain.writeByteArray(bArr2);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] verifyRequest(String str, String str2, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int write(String str, String str2, String str3, byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    obtain.writeByteArray(bArr3);
                    this.mRemote.transact(7, obtain, obtain2, 0);
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

        public static IVaultKeeperService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IVaultKeeperService)) ? new Proxy(iBinder) : (IVaultKeeperService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String packageName;
            boolean isInitialized;
            int initialize;
            byte[] readData;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    packageName = getPackageName(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(packageName);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    isInitialized = isInitialized(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isInitialized ? 1 : 0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    initialize = initialize(parcel.readString(), parcel.readString(), parcel.createByteArray(), parcel.readString(), parcel.createByteArray(), parcel.createByteArray(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(initialize);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    initialize = destroy(parcel.readString(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(initialize);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    packageName = readState(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(packageName);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    readData = readData(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(readData);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    initialize = write(parcel.readString(), parcel.readString(), parcel.readString(), parcel.createByteArray(), parcel.createByteArray(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(initialize);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    readData = getNonce(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(readData);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    isInitialized = verifyCertificate(parcel.readString(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(isInitialized ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    readData = verifyRequest(parcel.readString(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(readData);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    initialize = verifyComplete(parcel.readString(), parcel.readString(), parcel.createByteArray(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(initialize);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int destroy(String str, String str2, byte[] bArr) throws RemoteException;

    byte[] getNonce(String str, String str2) throws RemoteException;

    String getPackageName(String str) throws RemoteException;

    int initialize(String str, String str2, byte[] bArr, String str3, byte[] bArr2, byte[] bArr3, byte[] bArr4) throws RemoteException;

    boolean isInitialized(String str, String str2) throws RemoteException;

    byte[] readData(String str, String str2) throws RemoteException;

    String readState(String str, String str2) throws RemoteException;

    boolean verifyCertificate(String str, String str2, byte[] bArr) throws RemoteException;

    int verifyComplete(String str, String str2, byte[] bArr, String str3, byte[] bArr2) throws RemoteException;

    byte[] verifyRequest(String str, String str2, byte[] bArr) throws RemoteException;

    int write(String str, String str2, String str3, byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteException;
}
