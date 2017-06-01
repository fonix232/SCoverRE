package com.samsung.android.multiwindow;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

public interface IMultiWindowManager extends IInterface {

    public static abstract class Stub extends Binder implements IMultiWindowManager {
        private static final String DESCRIPTOR = "com.samsung.android.multiwindow.IMultiWindowManager";
        static final int TRANSACTION_activityPaused = 1;
        static final int TRANSACTION_alignTasksToStackBounds = 48;
        static final int TRANSACTION_changeFreeformMode = 19;
        static final int TRANSACTION_completeToggleSplitScreen = 39;
        static final int TRANSACTION_enterFreeformTask = 44;
        static final int TRANSACTION_exitMultiWindow = 2;
        static final int TRANSACTION_getFreeformTasks = 31;
        static final int TRANSACTION_getImeTargetFreeformTaskId = 40;
        static final int TRANSACTION_getMultiWindowModeStates = 41;
        static final int TRANSACTION_getSnapTargetAspectRatioRect = 35;
        static final int TRANSACTION_getTopRunningTaskInfo = 47;
        static final int TRANSACTION_hasDockedStack = 26;
        static final int TRANSACTION_isExpandedDockedStack = 25;
        static final int TRANSACTION_isForceResizable = 45;
        static final int TRANSACTION_isLaunchableForMultiInstance = 46;
        static final int TRANSACTION_isSnapWindowRunning = 37;
        static final int TRANSACTION_makeGhostFreeform = 3;
        static final int TRANSACTION_maximizeStackByDivider = 21;
        static final int TRANSACTION_maximizeTopTask = 51;
        static final int TRANSACTION_minimizeAllFreeform = 4;
        static final int TRANSACTION_minimizeOhterFreeforms = 5;
        static final int TRANSACTION_minimizeTask = 6;
        static final int TRANSACTION_minimizeTopTask = 50;
        static final int TRANSACTION_moveActivityTaskToBack = 8;
        static final int TRANSACTION_moveMultiWindowTasksToFullScreen = 30;
        static final int TRANSACTION_registerMultiWindowDividerPanelListener = 10;
        static final int TRANSACTION_registerMultiWindowEventListener = 24;
        static final int TRANSACTION_registerMultiWindowFocusedFrameListener = 9;
        static final int TRANSACTION_registerMultiWindowServiceCallback = 12;
        static final int TRANSACTION_removeFreeformTasks = 32;
        static final int TRANSACTION_removeSearchedTask = 52;
        static final int TRANSACTION_removeTaskIfNeeded = 20;
        static final int TRANSACTION_reportMinimizeContainerBounds = 33;
        static final int TRANSACTION_rotateFreeformTask = 7;
        static final int TRANSACTION_setAutoResizingEnabled = 28;
        static final int TRANSACTION_setDividerButtonsDimLayer = 22;
        static final int TRANSACTION_setDockedStackDividerButtonsTouchRegion = 27;
        static final int TRANSACTION_setLaunchBounds = 16;
        static final int TRANSACTION_setMultiWindowEnabledForUser = 29;
        static final int TRANSACTION_setSlideMode = 14;
        static final int TRANSACTION_setSnapWindow = 34;
        static final int TRANSACTION_showRecentApps = 38;
        static final int TRANSACTION_showSnapWindowGuideView = 36;
        static final int TRANSACTION_slideFreeform = 17;
        static final int TRANSACTION_slideOrUnslideAllFreeform = 18;
        static final int TRANSACTION_startDividerDragging = 42;
        static final int TRANSACTION_startFreeform = 49;
        static final int TRANSACTION_startResizingFreeformTask = 23;
        static final int TRANSACTION_stopDividerDragging = 43;
        static final int TRANSACTION_unregisterMultiWindowDividerPanelListener = 11;
        static final int TRANSACTION_unregisterMultiWindowServiceCallback = 13;
        static final int TRANSACTION_updateTaskPositionInTaskBar = 15;

        private static class Proxy implements IMultiWindowManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public void activityPaused(IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void alignTasksToStackBounds() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void changeFreeformMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void completeToggleSplitScreen() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enterFreeformTask(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(44, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean exitMultiWindow(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
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

            public List getFreeformTasks(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    List readArrayList = obtain2.readArrayList(getClass().getClassLoader());
                    return readArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getImeTargetFreeformTaskId() throws RemoteException {
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

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getMultiWindowModeStates(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(41, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Rect getSnapTargetAspectRatioRect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    Rect rect = obtain2.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return rect;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List getTopRunningTaskInfo(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(47, obtain, obtain2, 0);
                    obtain2.readException();
                    List readArrayList = obtain2.readArrayList(getClass().getClassLoader());
                    return readArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean hasDockedStack() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean isExpandedDockedStack() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean isForceResizable(ActivityInfo activityInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (activityInfo != null) {
                        obtain.writeInt(1);
                        activityInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(45, obtain, obtain2, 0);
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

            public boolean isLaunchableForMultiInstance(ActivityInfo activityInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (activityInfo != null) {
                        obtain.writeInt(1);
                        activityInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

            public boolean isSnapWindowRunning() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, obtain, obtain2, 0);
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

            public void makeGhostFreeform(IBinder iBinder, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void maximizeStackByDivider(boolean z) throws RemoteException {
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

            public boolean maximizeTopTask() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(51, obtain, obtain2, 0);
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

            public void minimizeAllFreeform(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void minimizeOhterFreeforms(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void minimizeTask(int i) throws RemoteException {
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

            public boolean minimizeTopTask() throws RemoteException {
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

            public boolean moveActivityTaskToBack(IBinder iBinder, boolean z, boolean z2) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
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

            public void moveMultiWindowTasksToFullScreen() throws RemoteException {
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

            public void registerMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iMultiWindowDividerPanelListener != null) {
                        iBinder = iMultiWindowDividerPanelListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerMultiWindowEventListener(IMultiWindowEventListener iMultiWindowEventListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iMultiWindowEventListener != null) {
                        iBinder = iMultiWindowEventListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerMultiWindowFocusedFrameListener(IMultiWindowFocusedFrameListener iMultiWindowFocusedFrameListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iMultiWindowFocusedFrameListener != null) {
                        iBinder = iMultiWindowFocusedFrameListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerMultiWindowServiceCallback(IMultiWindowCallback iMultiWindowCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iMultiWindowCallback != null) {
                        iBinder = iMultiWindowCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeFreeformTasks(int i) throws RemoteException {
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

            public boolean removeSearchedTask(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
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

            public boolean removeTaskIfNeeded(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public void reportMinimizeContainerBounds(Rect rect) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void rotateFreeformTask(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setAutoResizingEnabled(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDividerButtonsDimLayer(boolean z, float f, int i) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    obtain.writeFloat(f);
                    obtain.writeInt(i);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDockedStackDividerButtonsTouchRegion(Rect rect) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setLaunchBounds(int i, Rect rect) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMultiWindowEnabledForUser(String str, String str2, boolean z, int i) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    obtain.writeInt(i);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSlideMode(int i, boolean z) throws RemoteException {
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
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSnapWindow(boolean z, Rect rect, int i, String str) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!z) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showRecentApps() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showSnapWindowGuideView(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void slideFreeform(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void slideOrUnslideAllFreeform(boolean z) throws RemoteException {
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
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startDividerDragging() throws RemoteException {
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

            public boolean startFreeform() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, obtain, obtain2, 0);
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

            public void startResizingFreeformTask(IBinder iBinder, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopDividerDragging() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iMultiWindowDividerPanelListener != null) {
                        iBinder = iMultiWindowDividerPanelListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterMultiWindowServiceCallback(IMultiWindowCallback iMultiWindowCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iMultiWindowCallback != null) {
                        iBinder = iMultiWindowCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateTaskPositionInTaskBar(Map map) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeMap(map);
                    this.mRemote.transact(15, obtain, obtain2, 0);
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

        public static IMultiWindowManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMultiWindowManager)) ? new Proxy(iBinder) : (IMultiWindowManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            boolean exitMultiWindow;
            List freeformTasks;
            int imeTargetFreeformTaskId;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    activityPaused(parcel.readStrongBinder(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = exitMultiWindow(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    makeGhostFreeform(parcel.readStrongBinder(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    minimizeAllFreeform(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    minimizeOhterFreeforms(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    minimizeTask(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    rotateFreeformTask(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = moveActivityTaskToBack(parcel.readStrongBinder(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerMultiWindowFocusedFrameListener(com.samsung.android.multiwindow.IMultiWindowFocusedFrameListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerMultiWindowDividerPanelListener(com.samsung.android.multiwindow.IMultiWindowDividerPanelListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterMultiWindowDividerPanelListener(com.samsung.android.multiwindow.IMultiWindowDividerPanelListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerMultiWindowServiceCallback(com.samsung.android.multiwindow.IMultiWindowCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterMultiWindowServiceCallback(com.samsung.android.multiwindow.IMultiWindowCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    setSlideMode(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateTaskPositionInTaskBar(parcel.readHashMap(getClass().getClassLoader()));
                    parcel2.writeNoException();
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    setLaunchBounds(parcel.readInt(), parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    slideFreeform(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    slideOrUnslideAllFreeform(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    changeFreeformMode();
                    parcel2.writeNoException();
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = removeTaskIfNeeded(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    maximizeStackByDivider(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDividerButtonsDimLayer(parcel.readInt() != 0, parcel.readFloat(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    startResizingFreeformTask(parcel.readStrongBinder(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerMultiWindowEventListener(com.samsung.android.multiwindow.IMultiWindowEventListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = isExpandedDockedStack();
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = hasDockedStack();
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDockedStackDividerButtonsTouchRegion(parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    setAutoResizingEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMultiWindowEnabledForUser(parcel.readString(), parcel.readString(), parcel.readInt() != 0, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    moveMultiWindowTasksToFullScreen();
                    parcel2.writeNoException();
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    freeformTasks = getFreeformTasks(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeList(freeformTasks);
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeFreeformTasks(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportMinimizeContainerBounds(parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    setSnapWindow(parcel.readInt() != 0, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    Rect snapTargetAspectRatioRect = getSnapTargetAspectRatioRect();
                    parcel2.writeNoException();
                    if (snapTargetAspectRatioRect != null) {
                        parcel2.writeInt(1);
                        snapTargetAspectRatioRect.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    showSnapWindowGuideView(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = isSnapWindowRunning();
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    showRecentApps();
                    parcel2.writeNoException();
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    completeToggleSplitScreen();
                    parcel2.writeNoException();
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    imeTargetFreeformTaskId = getImeTargetFreeformTaskId();
                    parcel2.writeNoException();
                    parcel2.writeInt(imeTargetFreeformTaskId);
                    return true;
                case 41:
                    parcel.enforceInterface(DESCRIPTOR);
                    imeTargetFreeformTaskId = getMultiWindowModeStates(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(imeTargetFreeformTaskId);
                    return true;
                case 42:
                    parcel.enforceInterface(DESCRIPTOR);
                    startDividerDragging();
                    parcel2.writeNoException();
                    return true;
                case 43:
                    parcel.enforceInterface(DESCRIPTOR);
                    stopDividerDragging();
                    parcel2.writeNoException();
                    return true;
                case 44:
                    parcel.enforceInterface(DESCRIPTOR);
                    enterFreeformTask(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 45:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = isForceResizable(parcel.readInt() != 0 ? (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 46:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = isLaunchableForMultiInstance(parcel.readInt() != 0 ? (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 47:
                    parcel.enforceInterface(DESCRIPTOR);
                    freeformTasks = getTopRunningTaskInfo(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeList(freeformTasks);
                    return true;
                case 48:
                    parcel.enforceInterface(DESCRIPTOR);
                    alignTasksToStackBounds();
                    parcel2.writeNoException();
                    return true;
                case 49:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = startFreeform();
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 50:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = minimizeTopTask();
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 51:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = maximizeTopTask();
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 52:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitMultiWindow = removeSearchedTask(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(exitMultiWindow ? 1 : 0);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void activityPaused(IBinder iBinder, Bundle bundle) throws RemoteException;

    void alignTasksToStackBounds() throws RemoteException;

    void changeFreeformMode() throws RemoteException;

    void completeToggleSplitScreen() throws RemoteException;

    void enterFreeformTask(int i) throws RemoteException;

    boolean exitMultiWindow(IBinder iBinder) throws RemoteException;

    List getFreeformTasks(int i) throws RemoteException;

    int getImeTargetFreeformTaskId() throws RemoteException;

    int getMultiWindowModeStates(int i) throws RemoteException;

    Rect getSnapTargetAspectRatioRect() throws RemoteException;

    List getTopRunningTaskInfo(int i) throws RemoteException;

    boolean hasDockedStack() throws RemoteException;

    boolean isExpandedDockedStack() throws RemoteException;

    boolean isForceResizable(ActivityInfo activityInfo) throws RemoteException;

    boolean isLaunchableForMultiInstance(ActivityInfo activityInfo) throws RemoteException;

    boolean isSnapWindowRunning() throws RemoteException;

    void makeGhostFreeform(IBinder iBinder, int i) throws RemoteException;

    void maximizeStackByDivider(boolean z) throws RemoteException;

    boolean maximizeTopTask() throws RemoteException;

    void minimizeAllFreeform(int i) throws RemoteException;

    void minimizeOhterFreeforms(IBinder iBinder) throws RemoteException;

    void minimizeTask(int i) throws RemoteException;

    boolean minimizeTopTask() throws RemoteException;

    boolean moveActivityTaskToBack(IBinder iBinder, boolean z, boolean z2) throws RemoteException;

    void moveMultiWindowTasksToFullScreen() throws RemoteException;

    void registerMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) throws RemoteException;

    void registerMultiWindowEventListener(IMultiWindowEventListener iMultiWindowEventListener) throws RemoteException;

    void registerMultiWindowFocusedFrameListener(IMultiWindowFocusedFrameListener iMultiWindowFocusedFrameListener) throws RemoteException;

    void registerMultiWindowServiceCallback(IMultiWindowCallback iMultiWindowCallback) throws RemoteException;

    void removeFreeformTasks(int i) throws RemoteException;

    boolean removeSearchedTask(String str) throws RemoteException;

    boolean removeTaskIfNeeded(boolean z) throws RemoteException;

    void reportMinimizeContainerBounds(Rect rect) throws RemoteException;

    void rotateFreeformTask(IBinder iBinder) throws RemoteException;

    void setAutoResizingEnabled(boolean z) throws RemoteException;

    void setDividerButtonsDimLayer(boolean z, float f, int i) throws RemoteException;

    void setDockedStackDividerButtonsTouchRegion(Rect rect) throws RemoteException;

    void setLaunchBounds(int i, Rect rect) throws RemoteException;

    void setMultiWindowEnabledForUser(String str, String str2, boolean z, int i) throws RemoteException;

    void setSlideMode(int i, boolean z) throws RemoteException;

    void setSnapWindow(boolean z, Rect rect, int i, String str) throws RemoteException;

    void showRecentApps() throws RemoteException;

    void showSnapWindowGuideView(int i) throws RemoteException;

    void slideFreeform(IBinder iBinder) throws RemoteException;

    void slideOrUnslideAllFreeform(boolean z) throws RemoteException;

    void startDividerDragging() throws RemoteException;

    boolean startFreeform() throws RemoteException;

    void startResizingFreeformTask(IBinder iBinder, int i, int i2) throws RemoteException;

    void stopDividerDragging() throws RemoteException;

    void unregisterMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) throws RemoteException;

    void unregisterMultiWindowServiceCallback(IMultiWindowCallback iMultiWindowCallback) throws RemoteException;

    void updateTaskPositionInTaskBar(Map map) throws RemoteException;
}
