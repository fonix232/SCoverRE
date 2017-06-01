package com.samsung.android.multiwindow;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMultiWindowEventListener extends IInterface {

    public static abstract class Stub extends Binder implements IMultiWindowEventListener {
        private static final String DESCRIPTOR = "com.samsung.android.multiwindow.IMultiWindowEventListener";
        static final int TRANSACTION_onMultiWindowAppTransitionFinished = 3;
        static final int TRANSACTION_onMultiWindowDockedMinimizedChanged = 5;
        static final int TRANSACTION_onMultiWindowFocusChanged = 1;
        static final int TRANSACTION_onMultiWindowScreenFreezeAnimationFinished = 4;
        static final int TRANSACTION_onMultiWindowSettingChanged = 2;
        static final int TRANSACTION_onSnapWindowDismissed = 6;

        private static class Proxy implements IMultiWindowEventListener {
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

            public void onMultiWindowAppTransitionFinished() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMultiWindowDockedMinimizedChanged(boolean z, boolean z2, ActivityInfo activityInfo) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    if (activityInfo != null) {
                        obtain.writeInt(1);
                        activityInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMultiWindowFocusChanged(Rect rect, Rect rect2, int i, int i2, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect2 != null) {
                        obtain.writeInt(1);
                        rect2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMultiWindowScreenFreezeAnimationFinished() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMultiWindowSettingChanged(boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSnapWindowDismissed(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiWindowEventListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMultiWindowEventListener)) ? new Proxy(iBinder) : (IMultiWindowEventListener) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onMultiWindowFocusChanged(parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    onMultiWindowSettingChanged(parcel.readInt() != 0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    onMultiWindowAppTransitionFinished();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    onMultiWindowScreenFreezeAnimationFinished();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    onMultiWindowDockedMinimizedChanged(parcel.readInt() != 0, parcel.readInt() != 0, parcel.readInt() != 0 ? (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    onSnapWindowDismissed(parcel.readString());
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onMultiWindowAppTransitionFinished() throws RemoteException;

    void onMultiWindowDockedMinimizedChanged(boolean z, boolean z2, ActivityInfo activityInfo) throws RemoteException;

    void onMultiWindowFocusChanged(Rect rect, Rect rect2, int i, int i2, String str, Bundle bundle) throws RemoteException;

    void onMultiWindowScreenFreezeAnimationFinished() throws RemoteException;

    void onMultiWindowSettingChanged(boolean z) throws RemoteException;

    void onSnapWindowDismissed(String str) throws RemoteException;
}
