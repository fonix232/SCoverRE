package com.samsung.android.content.smartclip;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISpenGestureHoverListener extends IInterface {

    public static abstract class Stub extends Binder implements ISpenGestureHoverListener {
        private static final String DESCRIPTOR = "com.samsung.android.content.smartclip.ISpenGestureHoverListener";
        static final int TRANSACTION_onBackPressed = 4;
        static final int TRANSACTION_onHoverEnter = 1;
        static final int TRANSACTION_onHoverExit = 2;
        static final int TRANSACTION_onHoverExitTowardBack = 3;
        static final int TRANSACTION_onHoverStay = 5;

        private static class Proxy implements ISpenGestureHoverListener {
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

            public void onBackPressed() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onHoverEnter() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onHoverExit() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onHoverExitTowardBack() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onHoverStay(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISpenGestureHoverListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISpenGestureHoverListener)) ? new Proxy(iBinder) : (ISpenGestureHoverListener) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onHoverEnter();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    onHoverExit();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    onHoverExitTowardBack();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    onBackPressed();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    onHoverStay(parcel.readInt(), parcel.readInt());
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onBackPressed() throws RemoteException;

    void onHoverEnter() throws RemoteException;

    void onHoverExit() throws RemoteException;

    void onHoverExitTowardBack() throws RemoteException;

    void onHoverStay(int i, int i2) throws RemoteException;
}
