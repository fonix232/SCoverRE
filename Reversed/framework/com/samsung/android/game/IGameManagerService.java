package com.samsung.android.game;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

public interface IGameManagerService extends IInterface {

    public static abstract class Stub extends Binder implements IGameManagerService {
        private static final String DESCRIPTOR = "com.samsung.android.game.IGameManagerService";
        static final int TRANSACTION_addGame = 10;
        static final int TRANSACTION_cancelDeathRestart = 16;
        static final int TRANSACTION_getForegroundApp = 3;
        static final int TRANSACTION_getGameList = 4;
        static final int TRANSACTION_getGameMap = 9;
        static final int TRANSACTION_getMode = 7;
        static final int TRANSACTION_getVersion = 13;
        static final int TRANSACTION_identifyForegroundApp = 2;
        static final int TRANSACTION_identifyGamePackage = 1;
        static final int TRANSACTION_initGameManager = 11;
        static final int TRANSACTION_notifyMultiWindowStyleChangedLocked = 12;
        static final int TRANSACTION_registerCallback = 5;
        static final int TRANSACTION_requestDeathRestart = 15;
        static final int TRANSACTION_requestWithJson = 14;
        static final int TRANSACTION_setMode = 8;
        static final int TRANSACTION_unregisterCallback = 6;

        private static class Proxy implements IGameManagerService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public boolean addGame(String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
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

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void cancelDeathRestart(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getForegroundApp() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getGameList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Map getGameMap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    Map readHashMap = obtain2.readHashMap(getClass().getClassLoader());
                    return readHashMap;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int identifyForegroundApp() throws RemoteException {
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

            public int identifyGamePackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean initGameManager(int i, Map map) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeMap(map);
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

            public void notifyMultiWindowStyleChangedLocked(ComponentName componentName, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean registerCallback(IGameManagerCallback iGameManagerCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iGameManagerCallback != null) {
                        iBinder = iGameManagerCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
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

            public void requestDeathRestart(IBinder iBinder, Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String requestWithJson(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
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

            public boolean unregisterCallback(IGameManagerCallback iGameManagerCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iGameManagerCallback != null) {
                        iBinder = iGameManagerCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGameManagerService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IGameManagerService)) ? new Proxy(iBinder) : (IGameManagerService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int identifyGamePackage;
            String foregroundApp;
            boolean registerCallback;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    identifyGamePackage = identifyGamePackage(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(identifyGamePackage);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    identifyGamePackage = identifyForegroundApp();
                    parcel2.writeNoException();
                    parcel2.writeInt(identifyGamePackage);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    foregroundApp = getForegroundApp();
                    parcel2.writeNoException();
                    parcel2.writeString(foregroundApp);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    List gameList = getGameList();
                    parcel2.writeNoException();
                    parcel2.writeStringList(gameList);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = registerCallback(com.samsung.android.game.IGameManagerCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(registerCallback ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = unregisterCallback(com.samsung.android.game.IGameManagerCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(registerCallback ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    identifyGamePackage = getMode();
                    parcel2.writeNoException();
                    parcel2.writeInt(identifyGamePackage);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = setMode(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(registerCallback ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    Map gameMap = getGameMap();
                    parcel2.writeNoException();
                    parcel2.writeMap(gameMap);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = addGame(parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(registerCallback ? 1 : 0);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = initGameManager(parcel.readInt(), parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    parcel2.writeInt(registerCallback ? 1 : 0);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    notifyMultiWindowStyleChangedLocked(parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    foregroundApp = getVersion();
                    parcel2.writeNoException();
                    parcel2.writeString(foregroundApp);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    foregroundApp = requestWithJson(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(foregroundApp);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestDeathRestart(parcel.readStrongBinder(), parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    cancelDeathRestart(parcel.readStrongBinder());
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

    boolean addGame(String str, boolean z) throws RemoteException;

    void cancelDeathRestart(IBinder iBinder) throws RemoteException;

    String getForegroundApp() throws RemoteException;

    List<String> getGameList() throws RemoteException;

    Map getGameMap() throws RemoteException;

    int getMode() throws RemoteException;

    String getVersion() throws RemoteException;

    int identifyForegroundApp() throws RemoteException;

    int identifyGamePackage(String str) throws RemoteException;

    boolean initGameManager(int i, Map map) throws RemoteException;

    void notifyMultiWindowStyleChangedLocked(ComponentName componentName, boolean z) throws RemoteException;

    boolean registerCallback(IGameManagerCallback iGameManagerCallback) throws RemoteException;

    void requestDeathRestart(IBinder iBinder, Intent intent) throws RemoteException;

    String requestWithJson(String str, String str2) throws RemoteException;

    boolean setMode(int i) throws RemoteException;

    boolean unregisterCallback(IGameManagerCallback iGameManagerCallback) throws RemoteException;
}
