package com.samsung.android.bio.face;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFaceDaemon extends IInterface {

    public static abstract class Stub extends Binder implements IFaceDaemon {
        private static final String DESCRIPTOR = "com.samsung.android.bio.face.IFaceDaemon";
        static final int TRANSACTION_authenticate = 1;
        static final int TRANSACTION_cancelAuthentication = 2;
        static final int TRANSACTION_cancelEnrollment = 4;
        static final int TRANSACTION_cancelEnumeration = 15;
        static final int TRANSACTION_closeHal = 10;
        static final int TRANSACTION_enroll = 3;
        static final int TRANSACTION_enumerate = 14;
        static final int TRANSACTION_getAuthenticatorId = 7;
        static final int TRANSACTION_init = 11;
        static final int TRANSACTION_openHal = 9;
        static final int TRANSACTION_postEnroll = 12;
        static final int TRANSACTION_preEnroll = 5;
        static final int TRANSACTION_processFrontImage = 16;
        static final int TRANSACTION_remove = 6;
        static final int TRANSACTION_request = 13;
        static final int TRANSACTION_setActiveGroup = 8;

        private static class Proxy implements IFaceDaemon {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int authenticate(long j, int i, int i2, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int cancelAuthentication() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int cancelEnrollment() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int cancelEnumeration() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int closeHal() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int enroll(byte[] bArr, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int enumerate() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getAuthenticatorId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void init(IFaceDaemonCallback iFaceDaemonCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iFaceDaemonCallback != null) {
                        iBinder = iFaceDaemonCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long openHal() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int postEnroll() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long preEnroll() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int processFrontImage(byte[] bArr, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int remove(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int request(int i, byte[] bArr, byte[] bArr2, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    if (bArr2 == null) {
                        obtain.writeInt(-1);
                    } else {
                        obtain.writeInt(bArr2.length);
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.readByteArray(bArr2);
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setActiveGroup(int i, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(8, obtain, obtain2, 0);
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

        public static IFaceDaemon asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceDaemon)) ? new Proxy(iBinder) : (IFaceDaemon) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int authenticate;
            long preEnroll;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = authenticate(parcel.readLong(), parcel.readInt(), parcel.readInt(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = cancelAuthentication();
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = enroll(parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = cancelEnrollment();
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    preEnroll = preEnroll();
                    parcel2.writeNoException();
                    parcel2.writeLong(preEnroll);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = remove(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    preEnroll = getAuthenticatorId();
                    parcel2.writeNoException();
                    parcel2.writeLong(preEnroll);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = setActiveGroup(parcel.readInt(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    preEnroll = openHal();
                    parcel2.writeNoException();
                    parcel2.writeLong(preEnroll);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = closeHal();
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    init(com.samsung.android.bio.face.IFaceDaemonCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = postEnroll();
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    int readInt = parcel.readInt();
                    byte[] createByteArray = parcel.createByteArray();
                    int readInt2 = parcel.readInt();
                    byte[] bArr = readInt2 < 0 ? null : new byte[readInt2];
                    authenticate = request(readInt, createByteArray, bArr, parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    parcel2.writeByteArray(bArr);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = enumerate();
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = cancelEnumeration();
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate = processFrontImage(parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(authenticate);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int authenticate(long j, int i, int i2, byte[] bArr) throws RemoteException;

    int cancelAuthentication() throws RemoteException;

    int cancelEnrollment() throws RemoteException;

    int cancelEnumeration() throws RemoteException;

    int closeHal() throws RemoteException;

    int enroll(byte[] bArr, int i, int i2) throws RemoteException;

    int enumerate() throws RemoteException;

    long getAuthenticatorId() throws RemoteException;

    void init(IFaceDaemonCallback iFaceDaemonCallback) throws RemoteException;

    long openHal() throws RemoteException;

    int postEnroll() throws RemoteException;

    long preEnroll() throws RemoteException;

    int processFrontImage(byte[] bArr, int i, int i2, int i3) throws RemoteException;

    int remove(int i, int i2) throws RemoteException;

    int request(int i, byte[] bArr, byte[] bArr2, int i2) throws RemoteException;

    int setActiveGroup(int i, byte[] bArr) throws RemoteException;
}
