package com.samsung.android.bio.face;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFaceServiceReceiver extends IInterface {

    public static abstract class Stub extends Binder implements IFaceServiceReceiver {
        private static final String DESCRIPTOR = "com.samsung.android.bio.face.IFaceServiceReceiver";
        static final int TRANSACTION_onAcquired = 2;
        static final int TRANSACTION_onAuthenticationFailed = 4;
        static final int TRANSACTION_onAuthenticationSucceeded = 3;
        static final int TRANSACTION_onEnrollResult = 1;
        static final int TRANSACTION_onError = 5;
        static final int TRANSACTION_onRemoved = 6;

        private static class Proxy implements IFaceServiceReceiver {
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

            public void onAcquired(long j, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeString(str);
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

            public void onAuthenticationSucceeded(long j, Face face, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    if (face != null) {
                        obtain.writeInt(1);
                        face.writeToParcel(obtain, 0);
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

            public void onError(long j, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(5, obtain, null, 1);
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

        public static IFaceServiceReceiver asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceServiceReceiver)) ? new Proxy(iBinder) : (IFaceServiceReceiver) queryLocalInterface;
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
                    onAcquired(parcel.readLong(), parcel.readInt(), parcel.readString());
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    onAuthenticationSucceeded(parcel.readLong(), parcel.readInt() != 0 ? (Face) Face.CREATOR.createFromParcel(parcel) : null, parcel.createByteArray());
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    onAuthenticationFailed(parcel.readLong());
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    onError(parcel.readLong(), parcel.readInt(), parcel.readString());
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRemoved(parcel.readLong(), parcel.readInt(), parcel.readInt());
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onAcquired(long j, int i, String str) throws RemoteException;

    void onAuthenticationFailed(long j) throws RemoteException;

    void onAuthenticationSucceeded(long j, Face face, byte[] bArr) throws RemoteException;

    void onEnrollResult(long j, int i, int i2, int i3) throws RemoteException;

    void onError(long j, int i, String str) throws RemoteException;

    void onRemoved(long j, int i, int i2) throws RemoteException;
}
