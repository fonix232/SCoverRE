package com.samsung.android.camera.iris;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IIrisServiceReceiver extends IInterface {

    public static abstract class Stub extends Binder implements IIrisServiceReceiver {
        private static final String DESCRIPTOR = "com.samsung.android.camera.iris.IIrisServiceReceiver";
        static final int TRANSACTION_onAcquired = 2;
        static final int TRANSACTION_onAuthenticationFailed = 4;
        static final int TRANSACTION_onAuthenticationSucceeded = 3;
        static final int TRANSACTION_onEnrollResult = 1;
        static final int TRANSACTION_onError = 5;
        static final int TRANSACTION_onIRImage = 7;
        static final int TRANSACTION_onRemoved = 6;

        private static class Proxy implements IIrisServiceReceiver {
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

            public void onAcquired(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAuthenticationFailed(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAuthenticationSucceeded(long j, Iris iris, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (iris != null) {
                        obtain.writeInt(1);
                        iris.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEnrollResult(long j, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onError(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onIRImage(long j, byte[] bArr, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRemoved(long j, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIrisServiceReceiver asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IIrisServiceReceiver)) ? new Proxy(iBinder) : (IIrisServiceReceiver) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onEnrollResult(parcel.readLong(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    onAcquired(parcel.readLong(), parcel.readInt());
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    onAuthenticationSucceeded(parcel.readLong(), parcel.readInt() != 0 ? (Iris) Iris.CREATOR.createFromParcel(parcel) : null, parcel.createByteArray());
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    onAuthenticationFailed(parcel.readLong());
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    onError(parcel.readLong(), parcel.readInt());
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRemoved(parcel.readLong(), parcel.readInt(), parcel.readInt());
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    onIRImage(parcel.readLong(), parcel.createByteArray(), parcel.readInt(), parcel.readInt());
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onAcquired(long j, int i) throws RemoteException;

    void onAuthenticationFailed(long j) throws RemoteException;

    void onAuthenticationSucceeded(long j, Iris iris, byte[] bArr) throws RemoteException;

    void onEnrollResult(long j, int i, int i2, int i3) throws RemoteException;

    void onError(long j, int i) throws RemoteException;

    void onIRImage(long j, byte[] bArr, int i, int i2) throws RemoteException;

    void onRemoved(long j, int i, int i2) throws RemoteException;
}
