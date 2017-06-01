package com.samsung.android.thememanager;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

public interface IThemeManager extends IInterface {

    public static abstract class Stub extends Binder implements IThemeManager {
        private static final String DESCRIPTOR = "com.samsung.android.thememanager.IThemeManager";
        static final int TRANSACTION_applyEventTheme = 29;
        static final int TRANSACTION_applyThemeComponent = 38;
        static final int TRANSACTION_applyThemePackage = 20;
        static final int TRANSACTION_changeThemeState = 8;
        static final int TRANSACTION_deleteThemePackage = 21;
        static final int TRANSACTION_getActiveAODPackage = 49;
        static final int TRANSACTION_getActiveAppIconPackage = 42;
        static final int TRANSACTION_getActiveComponents = 9;
        static final int TRANSACTION_getActiveFestivalPackage = 28;
        static final int TRANSACTION_getActiveMyEvents = 11;
        static final int TRANSACTION_getActiveThemeComponent = 50;
        static final int TRANSACTION_getActiveWallpaperPackage = 43;
        static final int TRANSACTION_getCategoryList = 12;
        static final int TRANSACTION_getChineseFestivalList = 3;
        static final int TRANSACTION_getComponentCustomData = 53;
        static final int TRANSACTION_getComponentPackageMap = 2;
        static final int TRANSACTION_getCoverAttachStatus = 25;
        static final int TRANSACTION_getCurrentThemePackage = 10;
        static final int TRANSACTION_getCustomData = 32;
        static final int TRANSACTION_getInstalledComponentList = 39;
        static final int TRANSACTION_getInstalledComponentsCount = 40;
        static final int TRANSACTION_getListByCategory = 15;
        static final int TRANSACTION_getPreviousToCoverPackage = 26;
        static final int TRANSACTION_getSpecialEditionThemePackage = 30;
        static final int TRANSACTION_getStateComponentPackage = 48;
        static final int TRANSACTION_getStateThemePackage = 17;
        static final int TRANSACTION_getThemeDetailsList = 1;
        static final int TRANSACTION_getThemeVersionForMasterPackage = 33;
        static final int TRANSACTION_getThemesForComponent = 18;
        static final int TRANSACTION_getVersionForThemeFramework = 5;
        static final int TRANSACTION_getWallpaperFilePath = 44;
        static final int TRANSACTION_hideApplyProgress = 51;
        static final int TRANSACTION_installThemeComponent = 36;
        static final int TRANSACTION_installThemePackage = 4;
        static final int TRANSACTION_isComponentExist = 41;
        static final int TRANSACTION_isOnTrialMode = 6;
        static final int TRANSACTION_isSupportThemePackage = 35;
        static final int TRANSACTION_isSupportThemeVersion = 34;
        static final int TRANSACTION_isThemePackageExist = 24;
        static final int TRANSACTION_registerStatusListener = 22;
        static final int TRANSACTION_removeThemeComponent = 37;
        static final int TRANSACTION_removeThemePackage = 7;
        static final int TRANSACTION_setComponentCustomData = 52;
        static final int TRANSACTION_setCustomData = 31;
        static final int TRANSACTION_setDeleteMyEvents = 14;
        static final int TRANSACTION_setFestivalPackage = 27;
        static final int TRANSACTION_setFestivalPackageFrom = 47;
        static final int TRANSACTION_setStateComponentPackage = 45;
        static final int TRANSACTION_setStateThemePackage = 16;
        static final int TRANSACTION_setTimeForMyEvent = 13;
        static final int TRANSACTION_stopTrial = 46;
        static final int TRANSACTION_stopTrialThemePackage = 19;
        static final int TRANSACTION_unregisterStatusListener = 23;

        private static class Proxy implements IThemeManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public void applyEventTheme(String str, int i, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeString(str2);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean applyThemeComponent(String str, String str2, boolean z, String str3, String str4) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    this.mRemote.transact(38, obtain, obtain2, 0);
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

            public boolean applyThemePackage(String str, boolean z) throws RemoteException {
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

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean changeThemeState(String str, int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(8, obtain, obtain2, 0);
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

            public void deleteThemePackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getActiveAODPackage() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getActiveAppIconPackage() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getActiveComponents() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    String[] createStringArray = obtain2.createStringArray();
                    return createStringArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getActiveFestivalPackage() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getActiveMyEvents() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getActiveThemeComponent(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(50, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getActiveWallpaperPackage() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List getCategoryList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    List readArrayList = obtain2.readArrayList(getClass().getClassLoader());
                    return readArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List getChineseFestivalList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    List readArrayList = obtain2.readArrayList(getClass().getClassLoader());
                    return readArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getComponentCustomData(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(53, obtain, obtain2, 0);
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

            public Map getComponentPackageMap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    Map readHashMap = obtain2.readHashMap(getClass().getClassLoader());
                    return readHashMap;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getCoverAttachStatus(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(25, obtain, obtain2, 0);
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

            public String getCurrentThemePackage() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getCustomData(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(32, obtain, obtain2, 0);
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

            public List<String> getInstalledComponentList(String str, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getInstalledComponentsCount(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(40, obtain, obtain2, 0);
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

            public List getListByCategory(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    List readArrayList = obtain2.readArrayList(getClass().getClassLoader());
                    return readArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getPreviousToCoverPackage() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getSpecialEditionThemePackage() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getStateComponentPackage(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getStateThemePackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Map getThemeDetailsList(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    Map readHashMap = obtain2.readHashMap(getClass().getClassLoader());
                    return readHashMap;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getThemeVersionForMasterPackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getThemesForComponent(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getVersionForThemeFramework() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getWallpaperFilePath(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                    List<String> createStringArrayList = obtain2.createStringArrayList();
                    return createStringArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void hideApplyProgress() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(51, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void installThemeComponent(String str, Uri uri, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void installThemePackage(Uri uri, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isComponentExist(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(41, obtain, obtain2, 0);
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

            public boolean isOnTrialMode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
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

            public boolean isSupportThemePackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(35, obtain, obtain2, 0);
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

            public boolean isSupportThemeVersion(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(34, obtain, obtain2, 0);
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

            public boolean isThemePackageExist(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
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

            public void registerStatusListener(IStatusListener iStatusListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iStatusListener != null) {
                        iBinder = iStatusListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeThemeComponent(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeThemePackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setComponentCustomData(String str, String str2, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(52, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCustomData(String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDeleteMyEvents(List<String> list, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStringList(list);
                    obtain.writeString(str);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFestivalPackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFestivalPackageFrom(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setStateComponentPackage(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setStateThemePackage(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setTimeForMyEvent(String str, String str2, String str3, String str4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeString(str4);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean stopTrial() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, obtain, obtain2, 0);
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

            public boolean stopTrialThemePackage() throws RemoteException {
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

            public void unregisterStatusListener(IStatusListener iStatusListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iStatusListener != null) {
                        iBinder = iStatusListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(23, obtain, obtain2, 0);
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

        public static IThemeManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IThemeManager)) ? new Proxy(iBinder) : (IThemeManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            Map themeDetailsList;
            List chineseFestivalList;
            String versionForThemeFramework;
            boolean isOnTrialMode;
            List activeMyEvents;
            int stateThemePackage;
            Bundle customData;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    themeDetailsList = getThemeDetailsList(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeMap(themeDetailsList);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    themeDetailsList = getComponentPackageMap();
                    parcel2.writeNoException();
                    parcel2.writeMap(themeDetailsList);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    chineseFestivalList = getChineseFestivalList();
                    parcel2.writeNoException();
                    parcel2.writeList(chineseFestivalList);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    installThemePackage(parcel.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getVersionForThemeFramework();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = isOnTrialMode(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeThemePackage(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = changeThemeState(parcel.readString(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    String[] activeComponents = getActiveComponents();
                    parcel2.writeNoException();
                    parcel2.writeStringArray(activeComponents);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getCurrentThemePackage();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    activeMyEvents = getActiveMyEvents();
                    parcel2.writeNoException();
                    parcel2.writeStringList(activeMyEvents);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    chineseFestivalList = getCategoryList();
                    parcel2.writeNoException();
                    parcel2.writeList(chineseFestivalList);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    setTimeForMyEvent(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDeleteMyEvents(parcel.createStringArrayList(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    chineseFestivalList = getListByCategory(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeList(chineseFestivalList);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    stateThemePackage = setStateThemePackage(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(stateThemePackage);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    stateThemePackage = getStateThemePackage(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(stateThemePackage);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    activeMyEvents = getThemesForComponent(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStringList(activeMyEvents);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = stopTrialThemePackage();
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = applyThemePackage(parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    deleteThemePackage(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerStatusListener(com.samsung.android.thememanager.IStatusListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterStatusListener(com.samsung.android.thememanager.IStatusListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = isThemePackageExist(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = getCoverAttachStatus(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getPreviousToCoverPackage();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFestivalPackage(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getActiveFestivalPackage();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    applyEventTheme(parcel.readString(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getSpecialEditionThemePackage();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    setCustomData(parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    customData = getCustomData(parcel.readString());
                    parcel2.writeNoException();
                    if (customData != null) {
                        parcel2.writeInt(1);
                        customData.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getThemeVersionForMasterPackage(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = isSupportThemeVersion(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = isSupportThemePackage(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    installThemeComponent(parcel.readString(), parcel.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeThemeComponent(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = applyThemeComponent(parcel.readString(), parcel.readString(), parcel.readInt() != 0, parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    activeMyEvents = getInstalledComponentList(parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStringList(activeMyEvents);
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    stateThemePackage = getInstalledComponentsCount(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(stateThemePackage);
                    return true;
                case 41:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = isComponentExist(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 42:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getActiveAppIconPackage();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 43:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getActiveWallpaperPackage();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 44:
                    parcel.enforceInterface(DESCRIPTOR);
                    activeMyEvents = getWallpaperFilePath(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeStringList(activeMyEvents);
                    return true;
                case 45:
                    parcel.enforceInterface(DESCRIPTOR);
                    stateThemePackage = setStateComponentPackage(parcel.readString(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(stateThemePackage);
                    return true;
                case 46:
                    parcel.enforceInterface(DESCRIPTOR);
                    isOnTrialMode = stopTrial();
                    parcel2.writeNoException();
                    parcel2.writeInt(isOnTrialMode ? 1 : 0);
                    return true;
                case 47:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFestivalPackageFrom(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 48:
                    parcel.enforceInterface(DESCRIPTOR);
                    stateThemePackage = getStateComponentPackage(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(stateThemePackage);
                    return true;
                case 49:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getActiveAODPackage();
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 50:
                    parcel.enforceInterface(DESCRIPTOR);
                    versionForThemeFramework = getActiveThemeComponent(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(versionForThemeFramework);
                    return true;
                case 51:
                    parcel.enforceInterface(DESCRIPTOR);
                    hideApplyProgress();
                    parcel2.writeNoException();
                    return true;
                case 52:
                    parcel.enforceInterface(DESCRIPTOR);
                    setComponentCustomData(parcel.readString(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 53:
                    parcel.enforceInterface(DESCRIPTOR);
                    customData = getComponentCustomData(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    if (customData != null) {
                        parcel2.writeInt(1);
                        customData.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void applyEventTheme(String str, int i, String str2) throws RemoteException;

    boolean applyThemeComponent(String str, String str2, boolean z, String str3, String str4) throws RemoteException;

    boolean applyThemePackage(String str, boolean z) throws RemoteException;

    boolean changeThemeState(String str, int i, boolean z) throws RemoteException;

    void deleteThemePackage(String str) throws RemoteException;

    String getActiveAODPackage() throws RemoteException;

    String getActiveAppIconPackage() throws RemoteException;

    String[] getActiveComponents() throws RemoteException;

    String getActiveFestivalPackage() throws RemoteException;

    List<String> getActiveMyEvents() throws RemoteException;

    String getActiveThemeComponent(String str) throws RemoteException;

    String getActiveWallpaperPackage() throws RemoteException;

    List getCategoryList() throws RemoteException;

    List getChineseFestivalList() throws RemoteException;

    Bundle getComponentCustomData(String str, String str2) throws RemoteException;

    Map getComponentPackageMap() throws RemoteException;

    boolean getCoverAttachStatus(String str) throws RemoteException;

    String getCurrentThemePackage() throws RemoteException;

    Bundle getCustomData(String str) throws RemoteException;

    List<String> getInstalledComponentList(String str, int i, int i2, int i3) throws RemoteException;

    int getInstalledComponentsCount(String str) throws RemoteException;

    List getListByCategory(int i) throws RemoteException;

    String getPreviousToCoverPackage() throws RemoteException;

    String getSpecialEditionThemePackage() throws RemoteException;

    int getStateComponentPackage(String str, String str2) throws RemoteException;

    int getStateThemePackage(String str) throws RemoteException;

    Map getThemeDetailsList(String str) throws RemoteException;

    String getThemeVersionForMasterPackage(String str) throws RemoteException;

    List<String> getThemesForComponent(String str, int i) throws RemoteException;

    String getVersionForThemeFramework() throws RemoteException;

    List<String> getWallpaperFilePath(String str) throws RemoteException;

    void hideApplyProgress() throws RemoteException;

    void installThemeComponent(String str, Uri uri, boolean z) throws RemoteException;

    void installThemePackage(Uri uri, boolean z) throws RemoteException;

    boolean isComponentExist(String str, String str2) throws RemoteException;

    boolean isOnTrialMode(String str) throws RemoteException;

    boolean isSupportThemePackage(String str) throws RemoteException;

    boolean isSupportThemeVersion(int i) throws RemoteException;

    boolean isThemePackageExist(String str) throws RemoteException;

    void registerStatusListener(IStatusListener iStatusListener) throws RemoteException;

    void removeThemeComponent(String str, String str2) throws RemoteException;

    void removeThemePackage(String str) throws RemoteException;

    void setComponentCustomData(String str, String str2, Bundle bundle) throws RemoteException;

    void setCustomData(String str, Bundle bundle) throws RemoteException;

    void setDeleteMyEvents(List<String> list, String str) throws RemoteException;

    void setFestivalPackage(String str) throws RemoteException;

    void setFestivalPackageFrom(String str, String str2) throws RemoteException;

    int setStateComponentPackage(String str, String str2, int i) throws RemoteException;

    int setStateThemePackage(String str, int i) throws RemoteException;

    void setTimeForMyEvent(String str, String str2, String str3, String str4) throws RemoteException;

    boolean stopTrial() throws RemoteException;

    boolean stopTrialThemePackage() throws RemoteException;

    void unregisterStatusListener(IStatusListener iStatusListener) throws RemoteException;
}
