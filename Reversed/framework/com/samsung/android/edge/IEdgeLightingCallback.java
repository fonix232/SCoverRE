package com.samsung.android.edge;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEdgeLightingCallback extends IInterface {

    public static abstract class Stub extends Binder implements IEdgeLightingCallback {
        private static final String DESCRIPTOR = "com.samsung.android.edge.IEdgeLightingCallback";
        static final int TRANSACTION_onEdgeLightingStarted = 4;
        static final int TRANSACTION_onEdgeLightingStopped = 5;
        static final int TRANSACTION_onScreenChanged = 3;
        static final int TRANSACTION_onStartEdgeLighting = 1;
        static final int TRANSACTION_onStopEdgeLighting = 2;

        private static class Proxy implements IEdgeLightingCallback {
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

            public void onEdgeLightingStarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEdgeLightingStopped() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onScreenChanged(boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStartEdgeLighting(String str, SemEdgeLightingInfo semEdgeLightingInfo, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (semEdgeLightingInfo != null) {
                        obtain.writeInt(1);
                        semEdgeLightingInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStopEdgeLighting(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEdgeLightingCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IEdgeLightingCallback)) ? new Proxy(iBinder) : (IEdgeLightingCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            boolean z = false;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onStartEdgeLighting(parcel.readString(), parcel.readInt() != 0 ? (SemEdgeLightingInfo) SemEdgeLightingInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    onStopEdgeLighting(parcel.readString(), parcel.readInt());
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    onScreenChanged(z);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    onEdgeLightingStarted();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    onEdgeLightingStopped();
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onEdgeLightingStarted() throws RemoteException;

    void onEdgeLightingStopped() throws RemoteException;

    void onScreenChanged(boolean z) throws RemoteException;

    void onStartEdgeLighting(String str, SemEdgeLightingInfo semEdgeLightingInfo, int i) throws RemoteException;

    void onStopEdgeLighting(String str, int i) throws RemoteException;
}
