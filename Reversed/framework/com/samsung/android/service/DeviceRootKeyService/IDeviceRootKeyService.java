package com.samsung.android.service.DeviceRootKeyService;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDeviceRootKeyService extends IInterface {

    public static abstract class Stub extends Binder implements IDeviceRootKeyService {
        private static final String DESCRIPTOR = "com.samsung.android.service.DeviceRootKeyService.IDeviceRootKeyService";
        static final int TRANSACTION_createServiceKeySession = 3;
        static final int TRANSACTION_getDeviceRootKeyUID = 2;
        static final int TRANSACTION_isExistDeviceRootKey = 1;
        static final int TRANSACTION_releaseServiceKeySession = 4;
        static final int TRANSACTION_setDeviceRootKey = 5;

        private static class Proxy implements IDeviceRootKeyService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public byte[] createServiceKeySession(String str, int i, Tlv tlv) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (tlv != null) {
                        obtain.writeInt(1);
                        tlv.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getDeviceRootKeyUID(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
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

            public boolean isExistDeviceRootKey(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public int releaseServiceKeySession() throws RemoteException {
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

            public int setDeviceRootKey(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(5, obtain, obtain2, 0);
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

        public static IDeviceRootKeyService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IDeviceRootKeyService)) ? new Proxy(iBinder) : (IDeviceRootKeyService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int releaseServiceKeySession;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isExistDeviceRootKey = isExistDeviceRootKey(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isExistDeviceRootKey ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    String deviceRootKeyUID = getDeviceRootKeyUID(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(deviceRootKeyUID);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    byte[] createServiceKeySession = createServiceKeySession(parcel.readString(), parcel.readInt(), parcel.readInt() != 0 ? (Tlv) Tlv.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeByteArray(createServiceKeySession);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    releaseServiceKeySession = releaseServiceKeySession();
                    parcel2.writeNoException();
                    parcel2.writeInt(releaseServiceKeySession);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    releaseServiceKeySession = setDeviceRootKey(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(releaseServiceKeySession);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    byte[] createServiceKeySession(String str, int i, Tlv tlv) throws RemoteException;

    String getDeviceRootKeyUID(int i) throws RemoteException;

    boolean isExistDeviceRootKey(int i) throws RemoteException;

    int releaseServiceKeySession() throws RemoteException;

    int setDeviceRootKey(byte[] bArr) throws RemoteException;
}
