package com.samsung.android.knox;

import android.app.Command;
import android.content.CustomCursor;
import android.content.ICommandExeCallBack;
import android.content.IProviderCallBack;
import android.content.IRCPGlobalContactsDir;
import android.content.IRCPInterface;
import android.content.ISyncCallBack;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRunnableCallback;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ISemRemoteContentManager extends IInterface {

    public static abstract class Stub extends Binder implements ISemRemoteContentManager {
        private static final String DESCRIPTOR = "com.samsung.android.knox.ISemRemoteContentManager";
        static final int TRANSACTION_cancelCopyChunks = 23;
        static final int TRANSACTION_changePermissionMigration = 17;
        static final int TRANSACTION_copyChunks = 22;
        static final int TRANSACTION_copyFile = 15;
        static final int TRANSACTION_copyFileInternal = 14;
        static final int TRANSACTION_deleteFile = 20;
        static final int TRANSACTION_doSyncForSyncer = 28;
        static final int TRANSACTION_exchangeData = 25;
        static final int TRANSACTION_executeCommandForPersona = 7;
        static final int TRANSACTION_getCallerInfo = 8;
        static final int TRANSACTION_getFileInfo = 21;
        static final int TRANSACTION_getFiles = 19;
        static final int TRANSACTION_getRCPInterface = 12;
        static final int TRANSACTION_getRCPProxy = 9;
        static final int TRANSACTION_handleShortcut = 27;
        static final int TRANSACTION_isFileExist = 18;
        static final int TRANSACTION_moveFile = 13;
        static final int TRANSACTION_moveFilesForApp = 16;
        static final int TRANSACTION_moveFilesForAppEx = 31;
        static final int TRANSACTION_queryAllProviders = 2;
        static final int TRANSACTION_queryProvider = 1;
        static final int TRANSACTION_registerCommandExe = 5;
        static final int TRANSACTION_registerExchangeData = 24;
        static final int TRANSACTION_registerMonitorCb = 26;
        static final int TRANSACTION_registerObserver = 29;
        static final int TRANSACTION_registerProvider = 3;
        static final int TRANSACTION_registerRCPGlobalContactsDir = 10;
        static final int TRANSACTION_registerRCPInterface = 11;
        static final int TRANSACTION_registerSync = 4;
        static final int TRANSACTION_switchPersona = 6;
        static final int TRANSACTION_unRegisterObserver = 30;

        private static class Proxy implements ISemRemoteContentManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void cancelCopyChunks(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int changePermissionMigration(String str, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int copyChunks(int i, String str, int i2, String str2, long j, int i3, long j2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    obtain.writeString(str2);
                    obtain.writeLong(j);
                    obtain.writeInt(i3);
                    obtain.writeLong(j2);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int copyFile(int i, String str, int i2, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    obtain.writeString(str2);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int copyFileInternal(int i, String str, int i2, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    obtain.writeString(str2);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean deleteFile(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
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

            public void doSyncForSyncer(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle exchangeData(String str, int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle2 = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle2;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void executeCommandForPersona(Command command) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (command != null) {
                        obtain.writeInt(1);
                        command.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public CustomCursor getCallerInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    CustomCursor customCursor = obtain2.readInt() != 0 ? (CustomCursor) CustomCursor.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return customCursor;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getFileInfo(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(21, obtain, obtain2, 0);
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

            public List<String> getFiles(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public IRCPInterface getRCPInterface() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    IRCPInterface asInterface = android.content.IRCPInterface.Stub.asInterface(obtain2.readStrongBinder());
                    return asInterface;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IRCPGlobalContactsDir getRCPProxy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    IRCPGlobalContactsDir asInterface = android.content.IRCPGlobalContactsDir.Stub.asInterface(obtain2.readStrongBinder());
                    return asInterface;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void handleShortcut(int i, String str, String str2, Bitmap bitmap, String str3, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bitmap != null) {
                        obtain.writeInt(1);
                        bitmap.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isFileExist(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
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

            public int moveFile(int i, String str, int i2, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    obtain.writeString(str2);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long moveFilesForApp(int i, List<String> list, List<String> list2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStringList(list);
                    obtain.writeStringList(list2);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long moveFilesForAppEx(int i, List<String> list, List<String> list2, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStringList(list);
                    obtain.writeStringList(list2);
                    obtain.writeInt(i2);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<CustomCursor> queryAllProviders(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeStringArray(strArr);
                    obtain.writeString(str3);
                    obtain.writeStringArray(strArr2);
                    obtain.writeString(str4);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    List<CustomCursor> createTypedArrayList = obtain2.createTypedArrayList(CustomCursor.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public CustomCursor queryProvider(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeStringArray(strArr);
                    obtain.writeString(str3);
                    obtain.writeStringArray(strArr2);
                    obtain.writeString(str4);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    CustomCursor customCursor = obtain2.readInt() != 0 ? (CustomCursor) CustomCursor.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return customCursor;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerCommandExe(ICommandExeCallBack iCommandExeCallBack, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iCommandExeCallBack != null) {
                        iBinder = iCommandExeCallBack.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean registerExchangeData(String str, IRunnableCallback iRunnableCallback, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (iRunnableCallback != null) {
                        iBinder = iRunnableCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(24, obtain, obtain2, 0);
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

            public boolean registerMonitorCb(String str, IRunnableCallback iRunnableCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (iRunnableCallback != null) {
                        iBinder = iRunnableCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(26, obtain, obtain2, 0);
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

            public void registerObserver(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerProvider(String str, IProviderCallBack iProviderCallBack, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (iProviderCallBack != null) {
                        iBinder = iProviderCallBack.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerRCPGlobalContactsDir(IRCPGlobalContactsDir iRCPGlobalContactsDir, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iRCPGlobalContactsDir != null) {
                        iBinder = iRCPGlobalContactsDir.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerRCPInterface(IRCPInterface iRCPInterface, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iRCPInterface != null) {
                        iBinder = iRCPInterface.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerSync(ISyncCallBack iSyncCallBack, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSyncCallBack != null) {
                        iBinder = iSyncCallBack.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void switchPersona(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unRegisterObserver(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(30, obtain, obtain2, 0);
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

        public static ISemRemoteContentManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemRemoteContentManager)) ? new Proxy(iBinder) : (ISemRemoteContentManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            CustomCursor queryProvider;
            int moveFile;
            long moveFilesForApp;
            boolean isFileExist;
            Bundle fileInfo;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    queryProvider = queryProvider(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.createStringArray(), parcel.readString(), parcel.createStringArray(), parcel.readString());
                    parcel2.writeNoException();
                    if (queryProvider != null) {
                        parcel2.writeInt(1);
                        queryProvider.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    List queryAllProviders = queryAllProviders(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.createStringArray(), parcel.readString(), parcel.createStringArray(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeTypedList(queryAllProviders);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerProvider(parcel.readString(), android.content.IProviderCallBack.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerSync(android.content.ISyncCallBack.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCommandExe(android.content.ICommandExeCallBack.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    switchPersona(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    executeCommandForPersona(parcel.readInt() != 0 ? (Command) Command.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    queryProvider = getCallerInfo(parcel.readString());
                    parcel2.writeNoException();
                    if (queryProvider != null) {
                        parcel2.writeInt(1);
                        queryProvider.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    IRCPGlobalContactsDir rCPProxy = getRCPProxy();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(rCPProxy != null ? rCPProxy.asBinder() : null);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerRCPGlobalContactsDir(android.content.IRCPGlobalContactsDir.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerRCPInterface(android.content.IRCPInterface.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    IRCPInterface rCPInterface = getRCPInterface();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(rCPInterface != null ? rCPInterface.asBinder() : null);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveFile = moveFile(parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(moveFile);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveFile = copyFileInternal(parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(moveFile);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveFile = copyFile(parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(moveFile);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveFilesForApp = moveFilesForApp(parcel.readInt(), parcel.createStringArrayList(), parcel.createStringArrayList());
                    parcel2.writeNoException();
                    parcel2.writeLong(moveFilesForApp);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveFile = changePermissionMigration(parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(moveFile);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    isFileExist = isFileExist(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isFileExist ? 1 : 0);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    List files = getFiles(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStringList(files);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    isFileExist = deleteFile(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isFileExist ? 1 : 0);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    fileInfo = getFileInfo(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    if (fileInfo != null) {
                        parcel2.writeInt(1);
                        fileInfo.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveFile = copyChunks(parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString(), parcel.readLong(), parcel.readInt(), parcel.readLong(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(moveFile);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    cancelCopyChunks(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    isFileExist = registerExchangeData(parcel.readString(), android.os.IRunnableCallback.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isFileExist ? 1 : 0);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    fileInfo = exchangeData(parcel.readString(), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (fileInfo != null) {
                        parcel2.writeInt(1);
                        fileInfo.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    isFileExist = registerMonitorCb(parcel.readString(), android.os.IRunnableCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(isFileExist ? 1 : 0);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    handleShortcut(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    doSyncForSyncer(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerObserver(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    unRegisterObserver(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveFilesForApp = moveFilesForAppEx(parcel.readInt(), parcel.createStringArrayList(), parcel.createStringArrayList(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeLong(moveFilesForApp);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void cancelCopyChunks(long j) throws RemoteException;

    int changePermissionMigration(String str, int i, int i2, int i3) throws RemoteException;

    int copyChunks(int i, String str, int i2, String str2, long j, int i3, long j2, boolean z) throws RemoteException;

    int copyFile(int i, String str, int i2, String str2) throws RemoteException;

    int copyFileInternal(int i, String str, int i2, String str2) throws RemoteException;

    boolean deleteFile(String str, int i) throws RemoteException;

    void doSyncForSyncer(String str, int i) throws RemoteException;

    Bundle exchangeData(String str, int i, Bundle bundle) throws RemoteException;

    void executeCommandForPersona(Command command) throws RemoteException;

    CustomCursor getCallerInfo(String str) throws RemoteException;

    Bundle getFileInfo(String str, int i) throws RemoteException;

    List<String> getFiles(String str, int i) throws RemoteException;

    IRCPInterface getRCPInterface() throws RemoteException;

    IRCPGlobalContactsDir getRCPProxy() throws RemoteException;

    void handleShortcut(int i, String str, String str2, Bitmap bitmap, String str3, String str4) throws RemoteException;

    boolean isFileExist(String str, int i) throws RemoteException;

    int moveFile(int i, String str, int i2, String str2) throws RemoteException;

    long moveFilesForApp(int i, List<String> list, List<String> list2) throws RemoteException;

    long moveFilesForAppEx(int i, List<String> list, List<String> list2, int i2) throws RemoteException;

    List<CustomCursor> queryAllProviders(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException;

    CustomCursor queryProvider(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) throws RemoteException;

    void registerCommandExe(ICommandExeCallBack iCommandExeCallBack, int i) throws RemoteException;

    boolean registerExchangeData(String str, IRunnableCallback iRunnableCallback, int i) throws RemoteException;

    boolean registerMonitorCb(String str, IRunnableCallback iRunnableCallback) throws RemoteException;

    void registerObserver(String str, int i) throws RemoteException;

    void registerProvider(String str, IProviderCallBack iProviderCallBack, int i) throws RemoteException;

    void registerRCPGlobalContactsDir(IRCPGlobalContactsDir iRCPGlobalContactsDir, int i) throws RemoteException;

    void registerRCPInterface(IRCPInterface iRCPInterface, int i) throws RemoteException;

    void registerSync(ISyncCallBack iSyncCallBack, int i) throws RemoteException;

    void switchPersona(int i) throws RemoteException;

    void unRegisterObserver(String str, int i) throws RemoteException;
}
