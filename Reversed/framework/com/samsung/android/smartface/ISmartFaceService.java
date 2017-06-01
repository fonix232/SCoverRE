package com.samsung.android.smartface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISmartFaceService extends IInterface {

    public static abstract class Stub extends Binder implements ISmartFaceService {
        private static final String DESCRIPTOR = "com.samsung.android.smartface.ISmartFaceService";
        static final int TRANSACTION_getSupportedServices = 6;
        static final int TRANSACTION_register = 1;
        static final int TRANSACTION_registerAsync = 3;
        static final int TRANSACTION_setValue = 5;
        static final int TRANSACTION_unregister = 2;
        static final int TRANSACTION_unregisterAsync = 4;

        private static class Proxy implements ISmartFaceService {
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

            public int getSupportedServices() throws RemoteException {
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

            public boolean register(ISmartFaceClient iSmartFaceClient, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSmartFaceClient != null) {
                        iBinder = iSmartFaceClient.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
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

            public void registerAsync(ISmartFaceClient iSmartFaceClient, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSmartFaceClient != null) {
                        iBinder = iSmartFaceClient.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setValue(ISmartFaceClient iSmartFaceClient, String str, String str2) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSmartFaceClient != null) {
                        iBinder = iSmartFaceClient.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregister(ISmartFaceClient iSmartFaceClient) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSmartFaceClient != null) {
                        iBinder = iSmartFaceClient.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterAsync(ISmartFaceClient iSmartFaceClient) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSmartFaceClient != null) {
                        iBinder = iSmartFaceClient.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISmartFaceService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISmartFaceService)) ? new Proxy(iBinder) : (ISmartFaceService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean register = register(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(register ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregister(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerAsync(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterAsync(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    setValue(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    int supportedServices = getSupportedServices();
                    parcel2.writeNoException();
                    parcel2.writeInt(supportedServices);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int getSupportedServices() throws RemoteException;

    boolean register(ISmartFaceClient iSmartFaceClient, int i) throws RemoteException;

    void registerAsync(ISmartFaceClient iSmartFaceClient, int i) throws RemoteException;

    void setValue(ISmartFaceClient iSmartFaceClient, String str, String str2) throws RemoteException;

    void unregister(ISmartFaceClient iSmartFaceClient) throws RemoteException;

    void unregisterAsync(ISmartFaceClient iSmartFaceClient) throws RemoteException;
}
