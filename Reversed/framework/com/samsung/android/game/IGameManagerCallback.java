package com.samsung.android.game;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGameManagerCallback extends IInterface {

    public static abstract class Stub extends Binder implements IGameManagerCallback {
        private static final String DESCRIPTOR = "com.samsung.android.game.IGameManagerCallback";
        static final int TRANSACTION_onGameAdded = 3;
        static final int TRANSACTION_onGamePause = 2;
        static final int TRANSACTION_onGameResume = 1;
        static final int TRANSACTION_onModeChanged = 4;

        private static class Proxy implements IGameManagerCallback {
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

            public void onGameAdded(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onGamePause(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onGameResume(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onModeChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGameManagerCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IGameManagerCallback)) ? new Proxy(iBinder) : (IGameManagerCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onGameResume(parcel.readString());
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    onGamePause(parcel.readString());
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    onGameAdded(parcel.readString());
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    onModeChanged(parcel.readInt());
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onGameAdded(String str) throws RemoteException;

    void onGamePause(String str) throws RemoteException;

    void onGameResume(String str) throws RemoteException;

    void onModeChanged(int i) throws RemoteException;
}
