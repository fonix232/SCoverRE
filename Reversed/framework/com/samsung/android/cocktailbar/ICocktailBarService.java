package com.samsung.android.cocktailbar;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.DragEvent;
import android.widget.RemoteViews;

public interface ICocktailBarService extends IInterface {

    public static abstract class Stub extends Binder implements ICocktailBarService {
        private static final String DESCRIPTOR = "com.samsung.android.cocktailbar.ICocktailBarService";
        static final int TRANSACTION_activateCocktailBar = 41;
        static final int TRANSACTION_bindRemoteViewsService = 21;
        static final int TRANSACTION_closeCocktail = 12;
        static final int TRANSACTION_cocktailBarreboot = 56;
        static final int TRANSACTION_cocktailBarshutdown = 55;
        static final int TRANSACTION_deactivateCocktailBar = 42;
        static final int TRANSACTION_disableCocktail = 14;
        static final int TRANSACTION_getAllCocktailIds = 6;
        static final int TRANSACTION_getCategoryFilterStr = 59;
        static final int TRANSACTION_getCocktaiBarWakeUpState = 50;
        static final int TRANSACTION_getCocktail = 7;
        static final int TRANSACTION_getCocktailBarStateInfo = 38;
        static final int TRANSACTION_getCocktailBarVisibility = 37;
        static final int TRANSACTION_getCocktailId = 13;
        static final int TRANSACTION_getCocktailIds = 15;
        static final int TRANSACTION_getConfigVersion = 57;
        static final int TRANSACTION_getEnabledCocktailIds = 5;
        static final int TRANSACTION_getHideEdgeListStr = 60;
        static final int TRANSACTION_getPreferWidth = 58;
        static final int TRANSACTION_getWindowType = 40;
        static final int TRANSACTION_isAllowTransientBarCocktailBar = 52;
        static final int TRANSACTION_isBoundCocktailPackage = 16;
        static final int TRANSACTION_isCocktailEnabled = 18;
        static final int TRANSACTION_isEnabledCocktail = 17;
        static final int TRANSACTION_notifyCocktailViewDataChanged = 19;
        static final int TRANSACTION_notifyCocktailVisibiltyChanged = 28;
        static final int TRANSACTION_notifyKeyguardState = 27;
        static final int TRANSACTION_partiallyUpdateCocktail = 9;
        static final int TRANSACTION_partiallyUpdateHelpView = 10;
        static final int TRANSACTION_registerCocktailBarStateListenerCallback = 35;
        static final int TRANSACTION_removeCocktailUIService = 54;
        static final int TRANSACTION_requestToDisableCocktail = 24;
        static final int TRANSACTION_requestToDisableCocktailByCategory = 26;
        static final int TRANSACTION_requestToUpdateCocktail = 23;
        static final int TRANSACTION_requestToUpdateCocktailByCategory = 25;
        static final int TRANSACTION_sendDragEvent = 29;
        static final int TRANSACTION_sendExtraDataToCocktailBar = 53;
        static final int TRANSACTION_setCocktailBarStatus = 34;
        static final int TRANSACTION_setCocktailBarWakeUpState = 49;
        static final int TRANSACTION_setCocktailHostCallbacks = 1;
        static final int TRANSACTION_setDisableTickerView = 43;
        static final int TRANSACTION_setEnabledCocktailIds = 4;
        static final int TRANSACTION_setOnPullPendingIntent = 20;
        static final int TRANSACTION_showAndLockCocktailBar = 30;
        static final int TRANSACTION_showCocktail = 11;
        static final int TRANSACTION_startListening = 2;
        static final int TRANSACTION_stopListening = 3;
        static final int TRANSACTION_switchDefaultCocktail = 51;
        static final int TRANSACTION_unbindRemoteViewsService = 22;
        static final int TRANSACTION_unlockCocktailBar = 31;
        static final int TRANSACTION_unregisterCocktailBarStateListenerCallback = 36;
        static final int TRANSACTION_updateCocktail = 8;
        static final int TRANSACTION_updateCocktailBarPosition = 33;
        static final int TRANSACTION_updateCocktailBarVisibility = 32;
        static final int TRANSACTION_updateCocktailBarWindowType = 39;
        static final int TRANSACTION_updateLongpressGesture = 46;
        static final int TRANSACTION_updateSysfsBarLength = 48;
        static final int TRANSACTION_updateSysfsDeadZone = 47;
        static final int TRANSACTION_updateWakeupArea = 45;
        static final int TRANSACTION_updateWakeupGesture = 44;

        private static class Proxy implements ICocktailBarService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public void activateCocktailBar() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void bindRemoteViewsService(String str, int i, Intent intent, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void closeCocktail(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cocktailBarreboot(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(56, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cocktailBarshutdown(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(55, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void deactivateCocktailBar() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void disableCocktail(String str, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getAllCocktailIds() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCategoryFilterStr() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(59, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean getCocktaiBarWakeUpState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(50, obtain, obtain2, 0);
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

            public Cocktail getCocktail(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    Cocktail cocktail = obtain2.readInt() != 0 ? (Cocktail) Cocktail.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return cocktail;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public CocktailBarStateInfo getCocktailBarStateInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                    CocktailBarStateInfo cocktailBarStateInfo = obtain2.readInt() != 0 ? (CocktailBarStateInfo) CocktailBarStateInfo.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return cocktailBarStateInfo;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCocktailBarVisibility() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCocktailId(String str, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getCocktailIds(String str, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getConfigVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(57, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getEnabledCocktailIds() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getHideEdgeListStr() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(60, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getPreferWidth() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getWindowType() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isAllowTransientBarCocktailBar() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(52, obtain, obtain2, 0);
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

            public boolean isBoundCocktailPackage(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
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

            public boolean isCocktailEnabled(String str, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

            public boolean isEnabledCocktail(String str, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

            public void notifyCocktailViewDataChanged(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyCocktailVisibiltyChanged(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyKeyguardState(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void partiallyUpdateCocktail(String str, RemoteViews remoteViews, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (remoteViews != null) {
                        obtain.writeInt(1);
                        remoteViews.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void partiallyUpdateHelpView(String str, RemoteViews remoteViews, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (remoteViews != null) {
                        obtain.writeInt(1);
                        remoteViews.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerCocktailBarStateListenerCallback(IBinder iBinder, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeCocktailUIService() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean requestToDisableCocktail(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean requestToDisableCocktailByCategory(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
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

            public boolean requestToUpdateCocktail(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
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

            public boolean requestToUpdateCocktailByCategory(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
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

            public void sendDragEvent(int i, DragEvent dragEvent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (dragEvent != null) {
                        obtain.writeInt(1);
                        dragEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sendExtraDataToCocktailBar(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(53, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCocktailBarStatus(boolean z, boolean z2) throws RemoteException {
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
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCocktailBarWakeUpState(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(49, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCocktailHostCallbacks(ICocktailHost iCocktailHost, String str, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iCocktailHost != null) {
                        iBinder = iCocktailHost.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDisableTickerView(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setEnabledCocktailIds(int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnPullPendingIntent(String str, int i, int i2, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showAndLockCocktailBar() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showCocktail(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startListening(ICocktailHost iCocktailHost, String str, int i) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iCocktailHost != null) {
                        iBinder = iCocktailHost.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopListening(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void switchDefaultCocktail() throws RemoteException {
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

            public void unbindRemoteViewsService(String str, int i, Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unlockCocktailBar(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterCocktailBarStateListenerCallback(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateCocktail(String str, CocktailInfo cocktailInfo, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (cocktailInfo != null) {
                        obtain.writeInt(1);
                        cocktailInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateCocktailBarPosition(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateCocktailBarVisibility(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateCocktailBarWindowType(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateLongpressGesture(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateSysfsBarLength(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateSysfsDeadZone(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateWakeupArea(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(45, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateWakeupGesture(int i, boolean z) throws RemoteException {
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
                    this.mRemote.transact(44, obtain, obtain2, 0);
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

        public static ICocktailBarService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ICocktailBarService)) ? new Proxy(iBinder) : (ICocktailBarService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int[] enabledCocktailIds;
            int cocktailId;
            boolean isBoundCocktailPackage;
            String categoryFilterStr;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    setCocktailHostCallbacks(com.samsung.android.cocktailbar.ICocktailHost.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    startListening(com.samsung.android.cocktailbar.ICocktailHost.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    stopListening(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    setEnabledCocktailIds(parcel.createIntArray());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    enabledCocktailIds = getEnabledCocktailIds();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(enabledCocktailIds);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    enabledCocktailIds = getAllCocktailIds();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(enabledCocktailIds);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    Cocktail cocktail = getCocktail(parcel.readInt());
                    parcel2.writeNoException();
                    if (cocktail != null) {
                        parcel2.writeInt(1);
                        cocktail.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateCocktail(parcel.readString(), parcel.readInt() != 0 ? (CocktailInfo) CocktailInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    partiallyUpdateCocktail(parcel.readString(), parcel.readInt() != 0 ? (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    partiallyUpdateHelpView(parcel.readString(), parcel.readInt() != 0 ? (RemoteViews) RemoteViews.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    showCocktail(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    closeCocktail(parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    cocktailId = getCocktailId(parcel.readString(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(cocktailId);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    disableCocktail(parcel.readString(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    enabledCocktailIds = getCocktailIds(parcel.readString(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeIntArray(enabledCocktailIds);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = isBoundCocktailPackage(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = isEnabledCocktail(parcel.readString(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = isCocktailEnabled(parcel.readString(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    notifyCocktailViewDataChanged(parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    setOnPullPendingIntent(parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    bindRemoteViewsService(parcel.readString(), parcel.readInt(), parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null, parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    unbindRemoteViewsService(parcel.readString(), parcel.readInt(), parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = requestToUpdateCocktail(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = requestToDisableCocktail(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = requestToUpdateCocktailByCategory(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = requestToDisableCocktailByCategory(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    notifyKeyguardState(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    notifyCocktailVisibiltyChanged(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    sendDragEvent(parcel.readInt(), parcel.readInt() != 0 ? (DragEvent) DragEvent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    showAndLockCocktailBar();
                    parcel2.writeNoException();
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    unlockCocktailBar(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateCocktailBarVisibility(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateCocktailBarPosition(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    setCocktailBarStatus(parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCocktailBarStateListenerCallback(parcel.readStrongBinder(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterCocktailBarStateListenerCallback(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    cocktailId = getCocktailBarVisibility();
                    parcel2.writeNoException();
                    parcel2.writeInt(cocktailId);
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    CocktailBarStateInfo cocktailBarStateInfo = getCocktailBarStateInfo();
                    parcel2.writeNoException();
                    if (cocktailBarStateInfo != null) {
                        parcel2.writeInt(1);
                        cocktailBarStateInfo.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateCocktailBarWindowType(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    cocktailId = getWindowType();
                    parcel2.writeNoException();
                    parcel2.writeInt(cocktailId);
                    return true;
                case 41:
                    parcel.enforceInterface(DESCRIPTOR);
                    activateCocktailBar();
                    parcel2.writeNoException();
                    return true;
                case 42:
                    parcel.enforceInterface(DESCRIPTOR);
                    deactivateCocktailBar();
                    parcel2.writeNoException();
                    return true;
                case 43:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDisableTickerView(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 44:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateWakeupGesture(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 45:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateWakeupArea(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 46:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateLongpressGesture(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 47:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateSysfsDeadZone(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 48:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateSysfsBarLength(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 49:
                    parcel.enforceInterface(DESCRIPTOR);
                    setCocktailBarWakeUpState(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 50:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = getCocktaiBarWakeUpState();
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 51:
                    parcel.enforceInterface(DESCRIPTOR);
                    switchDefaultCocktail();
                    parcel2.writeNoException();
                    return true;
                case 52:
                    parcel.enforceInterface(DESCRIPTOR);
                    isBoundCocktailPackage = isAllowTransientBarCocktailBar();
                    parcel2.writeNoException();
                    parcel2.writeInt(isBoundCocktailPackage ? 1 : 0);
                    return true;
                case 53:
                    parcel.enforceInterface(DESCRIPTOR);
                    sendExtraDataToCocktailBar(parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 54:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeCocktailUIService();
                    parcel2.writeNoException();
                    return true;
                case 55:
                    parcel.enforceInterface(DESCRIPTOR);
                    cocktailBarshutdown(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 56:
                    parcel.enforceInterface(DESCRIPTOR);
                    cocktailBarreboot(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 57:
                    parcel.enforceInterface(DESCRIPTOR);
                    cocktailId = getConfigVersion();
                    parcel2.writeNoException();
                    parcel2.writeInt(cocktailId);
                    return true;
                case 58:
                    parcel.enforceInterface(DESCRIPTOR);
                    cocktailId = getPreferWidth();
                    parcel2.writeNoException();
                    parcel2.writeInt(cocktailId);
                    return true;
                case 59:
                    parcel.enforceInterface(DESCRIPTOR);
                    categoryFilterStr = getCategoryFilterStr();
                    parcel2.writeNoException();
                    parcel2.writeString(categoryFilterStr);
                    return true;
                case 60:
                    parcel.enforceInterface(DESCRIPTOR);
                    categoryFilterStr = getHideEdgeListStr();
                    parcel2.writeNoException();
                    parcel2.writeString(categoryFilterStr);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void activateCocktailBar() throws RemoteException;

    void bindRemoteViewsService(String str, int i, Intent intent, IBinder iBinder) throws RemoteException;

    void closeCocktail(String str, int i, int i2) throws RemoteException;

    void cocktailBarreboot(String str) throws RemoteException;

    void cocktailBarshutdown(String str) throws RemoteException;

    void deactivateCocktailBar() throws RemoteException;

    void disableCocktail(String str, ComponentName componentName) throws RemoteException;

    int[] getAllCocktailIds() throws RemoteException;

    String getCategoryFilterStr() throws RemoteException;

    boolean getCocktaiBarWakeUpState() throws RemoteException;

    Cocktail getCocktail(int i) throws RemoteException;

    CocktailBarStateInfo getCocktailBarStateInfo() throws RemoteException;

    int getCocktailBarVisibility() throws RemoteException;

    int getCocktailId(String str, ComponentName componentName) throws RemoteException;

    int[] getCocktailIds(String str, ComponentName componentName) throws RemoteException;

    int getConfigVersion() throws RemoteException;

    int[] getEnabledCocktailIds() throws RemoteException;

    String getHideEdgeListStr() throws RemoteException;

    int getPreferWidth() throws RemoteException;

    int getWindowType() throws RemoteException;

    boolean isAllowTransientBarCocktailBar() throws RemoteException;

    boolean isBoundCocktailPackage(String str, int i) throws RemoteException;

    boolean isCocktailEnabled(String str, ComponentName componentName) throws RemoteException;

    boolean isEnabledCocktail(String str, ComponentName componentName) throws RemoteException;

    void notifyCocktailViewDataChanged(String str, int i, int i2) throws RemoteException;

    void notifyCocktailVisibiltyChanged(int i, int i2) throws RemoteException;

    void notifyKeyguardState(boolean z) throws RemoteException;

    void partiallyUpdateCocktail(String str, RemoteViews remoteViews, int i) throws RemoteException;

    void partiallyUpdateHelpView(String str, RemoteViews remoteViews, int i) throws RemoteException;

    void registerCocktailBarStateListenerCallback(IBinder iBinder, ComponentName componentName) throws RemoteException;

    void removeCocktailUIService() throws RemoteException;

    boolean requestToDisableCocktail(int i) throws RemoteException;

    boolean requestToDisableCocktailByCategory(int i) throws RemoteException;

    boolean requestToUpdateCocktail(int i) throws RemoteException;

    boolean requestToUpdateCocktailByCategory(int i) throws RemoteException;

    void sendDragEvent(int i, DragEvent dragEvent) throws RemoteException;

    void sendExtraDataToCocktailBar(Bundle bundle) throws RemoteException;

    void setCocktailBarStatus(boolean z, boolean z2) throws RemoteException;

    void setCocktailBarWakeUpState(boolean z) throws RemoteException;

    void setCocktailHostCallbacks(ICocktailHost iCocktailHost, String str, int i) throws RemoteException;

    void setDisableTickerView(int i) throws RemoteException;

    void setEnabledCocktailIds(int[] iArr) throws RemoteException;

    void setOnPullPendingIntent(String str, int i, int i2, PendingIntent pendingIntent) throws RemoteException;

    void showAndLockCocktailBar() throws RemoteException;

    void showCocktail(String str, int i) throws RemoteException;

    void startListening(ICocktailHost iCocktailHost, String str, int i) throws RemoteException;

    void stopListening(String str) throws RemoteException;

    void switchDefaultCocktail() throws RemoteException;

    void unbindRemoteViewsService(String str, int i, Intent intent) throws RemoteException;

    void unlockCocktailBar(int i) throws RemoteException;

    void unregisterCocktailBarStateListenerCallback(IBinder iBinder) throws RemoteException;

    void updateCocktail(String str, CocktailInfo cocktailInfo, int i) throws RemoteException;

    void updateCocktailBarPosition(int i) throws RemoteException;

    void updateCocktailBarVisibility(int i) throws RemoteException;

    void updateCocktailBarWindowType(String str, int i) throws RemoteException;

    void updateLongpressGesture(boolean z) throws RemoteException;

    void updateSysfsBarLength(int i) throws RemoteException;

    void updateSysfsDeadZone(int i) throws RemoteException;

    void updateWakeupArea(int i) throws RemoteException;

    void updateWakeupGesture(int i, boolean z) throws RemoteException;
}
