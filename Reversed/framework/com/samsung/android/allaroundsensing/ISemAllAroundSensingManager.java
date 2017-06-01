package com.samsung.android.allaroundsensing;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISemAllAroundSensingManager extends IInterface {

    public static abstract class Stub extends Binder implements ISemAllAroundSensingManager {
        private static final String DESCRIPTOR = "com.samsung.android.allaroundsensing.ISemAllAroundSensingManager";
        static final int TRANSACTION_getBrightnessValue = 1;
        static final int TRANSACTION_getBrightnessValueEnable = 3;
        static final int TRANSACTION_getPlatformBrightnessValue = 2;
        static final int TRANSACTION_setBrightnessValue = 4;
        static final int TRANSACTION_setBrightnessValueEnabled = 6;
        static final int TRANSACTION_setPlatformBrightnessValue = 5;

        private static class Proxy implements ISemAllAroundSensingManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public float getBrightnessValue() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getBrightnessValueEnable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
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

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getPlatformBrightnessValue() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setBrightnessValue(float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeFloat(f);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setBrightnessValueEnabled(boolean z) throws RemoteException {
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
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setPlatformBrightnessValue(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
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

        public static ISemAllAroundSensingManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemAllAroundSensingManager)) ? new Proxy(iBinder) : (ISemAllAroundSensingManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = 0;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    float brightnessValue = getBrightnessValue();
                    parcel2.writeNoException();
                    parcel2.writeFloat(brightnessValue);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    int platformBrightnessValue = getPlatformBrightnessValue();
                    parcel2.writeNoException();
                    parcel2.writeInt(platformBrightnessValue);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean brightnessValueEnable = getBrightnessValueEnable();
                    parcel2.writeNoException();
                    if (brightnessValueEnable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    setBrightnessValue(parcel.readFloat());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPlatformBrightnessValue(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    setBrightnessValueEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    float getBrightnessValue() throws RemoteException;

    boolean getBrightnessValueEnable() throws RemoteException;

    int getPlatformBrightnessValue() throws RemoteException;

    void setBrightnessValue(float f) throws RemoteException;

    void setBrightnessValueEnabled(boolean z) throws RemoteException;

    void setPlatformBrightnessValue(int i) throws RemoteException;
}
