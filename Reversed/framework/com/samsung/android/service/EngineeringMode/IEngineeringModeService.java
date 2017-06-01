package com.samsung.android.service.EngineeringMode;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEngineeringModeService extends IInterface {

    public static abstract class Stub extends Binder implements IEngineeringModeService {
        private static final String DESCRIPTOR = "com.samsung.android.service.EngineeringMode.IEngineeringModeService";
        static final int TRANSACTION_getID = 6;
        static final int TRANSACTION_getNumOfModes = 7;
        static final int TRANSACTION_getRequestMsg = 2;
        static final int TRANSACTION_getStatus = 1;
        static final int TRANSACTION_installToken = 3;
        static final int TRANSACTION_isTokenInstalled = 4;
        static final int TRANSACTION_removeToken = 5;
        static final int TRANSACTION_sendFuseCmd = 8;

        private static class Proxy implements IEngineeringModeService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public byte[] getID() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getNumOfModes() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getRequestMsg(String str, String str2, byte[] bArr, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public int installToken(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int isTokenInstalled() throws RemoteException {
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

            public int removeToken() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int sendFuseCmd() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

        public static IEngineeringModeService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IEngineeringModeService)) ? new Proxy(iBinder) : (IEngineeringModeService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int status;
            byte[] requestMsg;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    status = getStatus(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(status);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestMsg = getRequestMsg(parcel.readString(), parcel.readString(), parcel.createByteArray(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(requestMsg);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    status = installToken(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(status);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    status = isTokenInstalled();
                    parcel2.writeNoException();
                    parcel2.writeInt(status);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    status = removeToken();
                    parcel2.writeNoException();
                    parcel2.writeInt(status);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestMsg = getID();
                    parcel2.writeNoException();
                    parcel2.writeByteArray(requestMsg);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    status = getNumOfModes();
                    parcel2.writeNoException();
                    parcel2.writeInt(status);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    status = sendFuseCmd();
                    parcel2.writeNoException();
                    parcel2.writeInt(status);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    byte[] getID() throws RemoteException;

    int getNumOfModes() throws RemoteException;

    byte[] getRequestMsg(String str, String str2, byte[] bArr, int i) throws RemoteException;

    int getStatus(int i) throws RemoteException;

    int installToken(byte[] bArr) throws RemoteException;

    int isTokenInstalled() throws RemoteException;

    int removeToken() throws RemoteException;

    int sendFuseCmd() throws RemoteException;
}
