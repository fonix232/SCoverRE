package com.samsung.android.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISemMdnieManager extends IInterface {

    public static abstract class Stub extends Binder implements ISemMdnieManager {
        private static final String DESCRIPTOR = "com.samsung.android.hardware.display.ISemMdnieManager";
        static final int TRANSACTION_getContentMode = 2;
        static final int TRANSACTION_getCurrentPocIndex = 3;
        static final int TRANSACTION_getPocSettingValue = 4;
        static final int TRANSACTION_getScreenMode = 1;
        static final int TRANSACTION_getSupportedContentMode = 11;
        static final int TRANSACTION_getSupportedScreenMode = 9;
        static final int TRANSACTION_isContentModeSupported = 10;
        static final int TRANSACTION_isMdnieFisrtUsed = 5;
        static final int TRANSACTION_isMdniePocFused = 6;
        static final int TRANSACTION_isMdniePocSupported = 7;
        static final int TRANSACTION_isScreenModeSupported = 8;
        static final int TRANSACTION_setAmoledACL = 14;
        static final int TRANSACTION_setContentMode = 13;
        static final int TRANSACTION_setLightNotificationMode = 21;
        static final int TRANSACTION_setNightMode = 15;
        static final int TRANSACTION_setPocCancel = 23;
        static final int TRANSACTION_setPocSetting = 22;
        static final int TRANSACTION_setScreenMode = 12;
        static final int TRANSACTION_setmDNIeAccessibilityMode = 20;
        static final int TRANSACTION_setmDNIeColorBlind = 16;
        static final int TRANSACTION_setmDNIeEmergencyMode = 19;
        static final int TRANSACTION_setmDNIeNegative = 17;
        static final int TRANSACTION_setmDNIeScreenCurtain = 18;

        private static class Proxy implements ISemMdnieManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int getContentMode() throws RemoteException {
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

            public int getCurrentPocIndex() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getPocSettingValue() throws RemoteException {
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

            public int getScreenMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getSupportedContentMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getSupportedScreenMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isContentModeSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, obtain2, 0);
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

            public boolean isMdnieFisrtUsed() throws RemoteException {
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

            public boolean isMdniePocFused() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
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

            public boolean isMdniePocSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
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

            public boolean isScreenModeSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
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

            public boolean setAmoledACL(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(14, obtain, obtain2, 0);
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

            public boolean setContentMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, obtain2, 0);
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

            public boolean setLightNotificationMode(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setNightMode(boolean z, int i) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setPocCancel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, obtain, obtain2, 0);
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

            public boolean setPocSetting(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(22, obtain, obtain2, 0);
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

            public boolean setScreenMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(12, obtain, obtain2, 0);
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

            public boolean setmDNIeAccessibilityMode(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setmDNIeColorBlind(boolean z, int[] iArr) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setmDNIeEmergencyMode(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setmDNIeNegative(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setmDNIeScreenCurtain(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z2 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISemMdnieManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemMdnieManager)) ? new Proxy(iBinder) : (ISemMdnieManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = 0;
            int screenMode;
            boolean isMdnieFisrtUsed;
            int[] supportedScreenMode;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    screenMode = getScreenMode();
                    parcel2.writeNoException();
                    parcel2.writeInt(screenMode);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    screenMode = getContentMode();
                    parcel2.writeNoException();
                    parcel2.writeInt(screenMode);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    screenMode = getCurrentPocIndex();
                    parcel2.writeNoException();
                    parcel2.writeInt(screenMode);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    screenMode = getPocSettingValue();
                    parcel2.writeNoException();
                    parcel2.writeInt(screenMode);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = isMdnieFisrtUsed();
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = isMdniePocFused();
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = isMdniePocSupported();
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = isScreenModeSupported();
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    supportedScreenMode = getSupportedScreenMode();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(supportedScreenMode);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = isContentModeSupported();
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    supportedScreenMode = getSupportedContentMode();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(supportedScreenMode);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setScreenMode(parcel.readInt());
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setContentMode(parcel.readInt());
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setAmoledACL(parcel.readInt());
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setNightMode(parcel.readInt() != 0, parcel.readInt());
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setmDNIeColorBlind(parcel.readInt() != 0, parcel.createIntArray());
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setmDNIeNegative(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setmDNIeScreenCurtain(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setmDNIeEmergencyMode(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setmDNIeAccessibilityMode(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setLightNotificationMode(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setPocSetting(parcel.readInt());
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    isMdnieFisrtUsed = setPocCancel();
                    parcel2.writeNoException();
                    if (isMdnieFisrtUsed) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int getContentMode() throws RemoteException;

    int getCurrentPocIndex() throws RemoteException;

    int getPocSettingValue() throws RemoteException;

    int getScreenMode() throws RemoteException;

    int[] getSupportedContentMode() throws RemoteException;

    int[] getSupportedScreenMode() throws RemoteException;

    boolean isContentModeSupported() throws RemoteException;

    boolean isMdnieFisrtUsed() throws RemoteException;

    boolean isMdniePocFused() throws RemoteException;

    boolean isMdniePocSupported() throws RemoteException;

    boolean isScreenModeSupported() throws RemoteException;

    boolean setAmoledACL(int i) throws RemoteException;

    boolean setContentMode(int i) throws RemoteException;

    boolean setLightNotificationMode(boolean z) throws RemoteException;

    boolean setNightMode(boolean z, int i) throws RemoteException;

    boolean setPocCancel() throws RemoteException;

    boolean setPocSetting(int i) throws RemoteException;

    boolean setScreenMode(int i) throws RemoteException;

    boolean setmDNIeAccessibilityMode(int i, boolean z) throws RemoteException;

    boolean setmDNIeColorBlind(boolean z, int[] iArr) throws RemoteException;

    boolean setmDNIeEmergencyMode(boolean z) throws RemoteException;

    boolean setmDNIeNegative(boolean z) throws RemoteException;

    boolean setmDNIeScreenCurtain(boolean z) throws RemoteException;
}
