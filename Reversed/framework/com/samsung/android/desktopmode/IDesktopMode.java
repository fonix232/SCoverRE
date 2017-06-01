package com.samsung.android.desktopmode;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDesktopMode extends IInterface {

    public static abstract class Stub extends Binder implements IDesktopMode {
        private static final String DESCRIPTOR = "com.samsung.android.desktopmode.IDesktopMode";
        static final int TRANSACTION_getDesktopModeKillPolicy = 8;
        static final int TRANSACTION_getDesktopModeState = 12;
        static final int TRANSACTION_getLaunchModePolicyList = 9;
        static final int TRANSACTION_getLaunchPolicyForPackage = 7;
        static final int TRANSACTION_getLaunchPolicyRunnable = 11;
        static final int TRANSACTION_getModeChangePolicy = 10;
        static final int TRANSACTION_isDefaultDisplayBlocked = 18;
        static final int TRANSACTION_isDesktopDockConnected = 1;
        static final int TRANSACTION_isDesktopMode = 2;
        static final int TRANSACTION_isDesktopModeAvailable = 13;
        static final int TRANSACTION_isDesktopModeAvailableEx = 14;
        static final int TRANSACTION_isDesktopModeEnablingOrEnabled = 15;
        static final int TRANSACTION_isDesktopModeForPreparing = 16;
        static final int TRANSACTION_isDesktopModeLoadingScreenShowing = 17;
        static final int TRANSACTION_isExternalDisplayConnected = 20;
        static final int TRANSACTION_isModeChangePending = 19;
        static final int TRANSACTION_registerStateCallback = 3;
        static final int TRANSACTION_setDefaultDisplayOn = 6;
        static final int TRANSACTION_setHdmiSettings = 21;
        static final int TRANSACTION_setTouchScreenOn = 5;
        static final int TRANSACTION_unregisterStateCallback = 4;

        private static class Proxy implements IDesktopMode {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public Bundle getDesktopModeKillPolicy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getDesktopModeState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
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

            public Bundle getLaunchModePolicyList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getLaunchPolicyForPackage(ApplicationInfo applicationInfo, ActivityInfo activityInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (applicationInfo != null) {
                        obtain.writeInt(1);
                        applicationInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (activityInfo != null) {
                        obtain.writeInt(1);
                        activityInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getLaunchPolicyRunnable(ApplicationInfo applicationInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (applicationInfo != null) {
                        obtain.writeInt(1);
                        applicationInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(11, obtain, obtain2, 0);
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

            public int getModeChangePolicy(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isDefaultDisplayBlocked() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, obtain, obtain2, 0);
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

            public boolean isDesktopDockConnected() throws RemoteException {
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

            public boolean isDesktopMode() throws RemoteException {
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

            public boolean isDesktopModeAvailable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean isDesktopModeAvailableEx(boolean z, boolean z2) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    boolean z3 = obtain2.readInt() != 0;
                    obtain2.recycle();
                    obtain.recycle();
                    return z3;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isDesktopModeEnablingOrEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, obtain, obtain2, 0);
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

            public boolean isDesktopModeForPreparing() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, obtain, obtain2, 0);
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

            public boolean isDesktopModeLoadingScreenShowing() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, obtain, obtain2, 0);
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

            public boolean isExternalDisplayConnected() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, obtain, obtain2, 0);
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

            public boolean isModeChangePending() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, obtain, obtain2, 0);
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

            public boolean registerStateCallback(IDesktopModeCallback iDesktopModeCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iDesktopModeCallback != null) {
                        iBinder = iDesktopModeCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
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

            public void setDefaultDisplayOn(boolean z, String str) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setHdmiSettings(boolean z) throws RemoteException {
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
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setTouchScreenOn(boolean z, String str) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean unregisterStateCallback(IDesktopModeCallback iDesktopModeCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iDesktopModeCallback != null) {
                        iBinder = iDesktopModeCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDesktopMode asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IDesktopMode)) ? new Proxy(iBinder) : (IDesktopMode) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            boolean isDesktopDockConnected;
            int launchPolicyForPackage;
            Bundle desktopModeKillPolicy;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDesktopDockConnected();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDesktopMode();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = registerStateCallback(com.samsung.android.desktopmode.IDesktopModeCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = unregisterStateCallback(com.samsung.android.desktopmode.IDesktopModeCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    setTouchScreenOn(parcel.readInt() != 0, parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDefaultDisplayOn(parcel.readInt() != 0, parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    launchPolicyForPackage = getLaunchPolicyForPackage(parcel.readInt() != 0 ? (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(launchPolicyForPackage);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    desktopModeKillPolicy = getDesktopModeKillPolicy();
                    parcel2.writeNoException();
                    if (desktopModeKillPolicy != null) {
                        parcel2.writeInt(1);
                        desktopModeKillPolicy.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    desktopModeKillPolicy = getLaunchModePolicyList();
                    parcel2.writeNoException();
                    if (desktopModeKillPolicy != null) {
                        parcel2.writeInt(1);
                        desktopModeKillPolicy.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    launchPolicyForPackage = getModeChangePolicy(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(launchPolicyForPackage);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = getLaunchPolicyRunnable(parcel.readInt() != 0 ? (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    launchPolicyForPackage = getDesktopModeState();
                    parcel2.writeNoException();
                    parcel2.writeInt(launchPolicyForPackage);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDesktopModeAvailable();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDesktopModeAvailableEx(parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDesktopModeEnablingOrEnabled();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDesktopModeForPreparing();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDesktopModeLoadingScreenShowing();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isDefaultDisplayBlocked();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isModeChangePending();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    isDesktopDockConnected = isExternalDisplayConnected();
                    parcel2.writeNoException();
                    parcel2.writeInt(isDesktopDockConnected ? 1 : 0);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    setHdmiSettings(parcel.readInt() != 0);
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

    Bundle getDesktopModeKillPolicy() throws RemoteException;

    int getDesktopModeState() throws RemoteException;

    Bundle getLaunchModePolicyList() throws RemoteException;

    int getLaunchPolicyForPackage(ApplicationInfo applicationInfo, ActivityInfo activityInfo) throws RemoteException;

    boolean getLaunchPolicyRunnable(ApplicationInfo applicationInfo) throws RemoteException;

    int getModeChangePolicy(String str) throws RemoteException;

    boolean isDefaultDisplayBlocked() throws RemoteException;

    boolean isDesktopDockConnected() throws RemoteException;

    boolean isDesktopMode() throws RemoteException;

    boolean isDesktopModeAvailable() throws RemoteException;

    boolean isDesktopModeAvailableEx(boolean z, boolean z2) throws RemoteException;

    boolean isDesktopModeEnablingOrEnabled() throws RemoteException;

    boolean isDesktopModeForPreparing() throws RemoteException;

    boolean isDesktopModeLoadingScreenShowing() throws RemoteException;

    boolean isExternalDisplayConnected() throws RemoteException;

    boolean isModeChangePending() throws RemoteException;

    boolean registerStateCallback(IDesktopModeCallback iDesktopModeCallback) throws RemoteException;

    void setDefaultDisplayOn(boolean z, String str) throws RemoteException;

    void setHdmiSettings(boolean z) throws RemoteException;

    void setTouchScreenOn(boolean z, String str) throws RemoteException;

    boolean unregisterStateCallback(IDesktopModeCallback iDesktopModeCallback) throws RemoteException;
}
