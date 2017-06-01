package com.samsung.android.displaysolution;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISemDisplaySolutionManager extends IInterface {

    public static abstract class Stub extends Binder implements ISemDisplaySolutionManager {
        private static final String DESCRIPTOR = "com.samsung.android.displaysolution.ISemDisplaySolutionManager";
        static final int TRANSACTION_getAlphaBlendingValue = 3;
        static final int TRANSACTION_getGalleryModeEnable = 2;
        static final int TRANSACTION_getVideoModeEnable = 1;
        static final int TRANSACTION_isMdnieScenarioControlServiceEnabled = 4;
        static final int TRANSACTION_setGalleryModeEnable = 6;
        static final int TRANSACTION_setMdnieScenarioControlServiceEnable = 7;
        static final int TRANSACTION_setMultipleScreenBrightness = 9;
        static final int TRANSACTION_setScreenBrightnessForPreview = 8;
        static final int TRANSACTION_setVideoModeEnable = 5;

        private static class Proxy implements ISemDisplaySolutionManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public float getAlphaBlendingValue() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    float readFloat = obtain2.readFloat();
                    return readFloat;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getGalleryModeEnable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
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

            public boolean getVideoModeEnable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean isMdnieScenarioControlServiceEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
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

            public void setGalleryModeEnable(boolean z) throws RemoteException {
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

            public void setMdnieScenarioControlServiceEnable(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMultipleScreenBrightness(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setScreenBrightnessForPreview(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setVideoModeEnable(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
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

        public static ISemDisplaySolutionManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemDisplaySolutionManager)) ? new Proxy(iBinder) : (ISemDisplaySolutionManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = 0;
            boolean videoModeEnable;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    videoModeEnable = getVideoModeEnable();
                    parcel2.writeNoException();
                    if (videoModeEnable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    videoModeEnable = getGalleryModeEnable();
                    parcel2.writeNoException();
                    if (videoModeEnable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    float alphaBlendingValue = getAlphaBlendingValue();
                    parcel2.writeNoException();
                    parcel2.writeFloat(alphaBlendingValue);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    videoModeEnable = isMdnieScenarioControlServiceEnabled();
                    parcel2.writeNoException();
                    if (videoModeEnable) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    setVideoModeEnable(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    setGalleryModeEnable(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMdnieScenarioControlServiceEnable(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    setScreenBrightnessForPreview(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMultipleScreenBrightness(parcel.readString());
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

    float getAlphaBlendingValue() throws RemoteException;

    boolean getGalleryModeEnable() throws RemoteException;

    boolean getVideoModeEnable() throws RemoteException;

    boolean isMdnieScenarioControlServiceEnabled() throws RemoteException;

    void setGalleryModeEnable(boolean z) throws RemoteException;

    void setMdnieScenarioControlServiceEnable(boolean z) throws RemoteException;

    void setMultipleScreenBrightness(String str) throws RemoteException;

    void setScreenBrightnessForPreview(int i) throws RemoteException;

    void setVideoModeEnable(boolean z) throws RemoteException;
}
