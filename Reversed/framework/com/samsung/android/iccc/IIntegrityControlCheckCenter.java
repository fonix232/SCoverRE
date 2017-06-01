package com.samsung.android.iccc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IIntegrityControlCheckCenter extends IInterface {

    public static abstract class Stub extends Binder implements IIntegrityControlCheckCenter {
        private static final String DESCRIPTOR = "com.samsung.android.iccc.IIntegrityControlCheckCenter";
        static final int TRANSACTION_getSecureData = 1;
        static final int TRANSACTION_getTrustedBootData = 3;
        static final int TRANSACTION_setSecureData = 2;

        private static class Proxy implements IIntegrityControlCheckCenter {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getSecureData(int i) throws RemoteException {
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

            public int getTrustedBootData() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setSecureData(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIntegrityControlCheckCenter asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IIntegrityControlCheckCenter)) ? new Proxy(iBinder) : (IIntegrityControlCheckCenter) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int secureData;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    secureData = getSecureData(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(secureData);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    secureData = setSecureData(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(secureData);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    secureData = getTrustedBootData();
                    parcel2.writeNoException();
                    parcel2.writeInt(secureData);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int getSecureData(int i) throws RemoteException;

    int getTrustedBootData() throws RemoteException;

    int setSecureData(int i, int i2) throws RemoteException;
}
