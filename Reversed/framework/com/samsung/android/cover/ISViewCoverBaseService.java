package com.samsung.android.cover;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISViewCoverBaseService extends IInterface {

    public static abstract class Stub extends Binder implements ISViewCoverBaseService {
        private static final String DESCRIPTOR = "com.samsung.android.cover.ISViewCoverBaseService";
        static final int TRANSACTION_isCoverViewShowing = 5;
        static final int TRANSACTION_onCoverAppCovered = 6;
        static final int TRANSACTION_onSViewCoverHide = 3;
        static final int TRANSACTION_onSViewCoverShow = 2;
        static final int TRANSACTION_onSystemReady = 1;
        static final int TRANSACTION_updateCoverState = 4;

        private static class Proxy implements ISViewCoverBaseService {
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

            public boolean isCoverViewShowing() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
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

            public int onCoverAppCovered(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSViewCoverHide() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSViewCoverShow() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSystemReady() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void updateCoverState(CoverState coverState) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (coverState != null) {
                        obtain.writeInt(1);
                        coverState.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISViewCoverBaseService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISViewCoverBaseService)) ? new Proxy(iBinder) : (ISViewCoverBaseService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = 0;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onSystemReady();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    onSViewCoverShow();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    onSViewCoverHide();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateCoverState(parcel.readInt() != 0 ? (CoverState) CoverState.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isCoverViewShowing = isCoverViewShowing();
                    parcel2.writeNoException();
                    if (isCoverViewShowing) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    int onCoverAppCovered = onCoverAppCovered(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(onCoverAppCovered);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean isCoverViewShowing() throws RemoteException;

    int onCoverAppCovered(boolean z) throws RemoteException;

    void onSViewCoverHide() throws RemoteException;

    void onSViewCoverShow() throws RemoteException;

    void onSystemReady() throws RemoteException;

    void updateCoverState(CoverState coverState) throws RemoteException;
}
