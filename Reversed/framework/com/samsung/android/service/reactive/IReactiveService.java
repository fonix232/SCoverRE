package com.samsung.android.service.reactive;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IReactiveService extends IInterface {

    public static abstract class Stub extends Binder implements IReactiveService {
        private static final String DESCRIPTOR = "com.samsung.android.service.reactive.IReactiveService";
        static final int TRANSACTION_getErrorCode = 9;
        static final int TRANSACTION_getFlag = 2;
        static final int TRANSACTION_getRandom = 11;
        static final int TRANSACTION_getServiceSupport = 1;
        static final int TRANSACTION_getString = 4;
        static final int TRANSACTION_removeString = 6;
        static final int TRANSACTION_sessionAccept = 7;
        static final int TRANSACTION_sessionComplete = 8;
        static final int TRANSACTION_setFlag = 3;
        static final int TRANSACTION_setString = 5;
        static final int TRANSACTION_verify = 10;

        private static class Proxy implements IReactiveService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int getErrorCode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getFlag(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public byte[] getRandom() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getServiceSupport() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getString() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int removeString() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] sessionAccept(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int sessionComplete(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public int setFlag(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setString(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int verify(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
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

        public static IReactiveService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IReactiveService)) ? new Proxy(iBinder) : (IReactiveService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int serviceSupport;
            byte[] sessionAccept;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = getServiceSupport();
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = getFlag(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = setFlag(parcel.readInt(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = getString();
                    parcel2.writeNoException();
                    parcel2.writeString(string);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = setString(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = removeString();
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    sessionAccept = sessionAccept(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(sessionAccept);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = sessionComplete(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = getErrorCode();
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    serviceSupport = verify(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(serviceSupport);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    sessionAccept = getRandom();
                    parcel2.writeNoException();
                    parcel2.writeByteArray(sessionAccept);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int getErrorCode() throws RemoteException;

    int getFlag(int i) throws RemoteException;

    byte[] getRandom() throws RemoteException;

    int getServiceSupport() throws RemoteException;

    String getString() throws RemoteException;

    int removeString() throws RemoteException;

    byte[] sessionAccept(byte[] bArr) throws RemoteException;

    int sessionComplete(byte[] bArr) throws RemoteException;

    int setFlag(int i, int i2, String str) throws RemoteException;

    int setString(String str) throws RemoteException;

    int verify(String str, int i) throws RemoteException;
}
