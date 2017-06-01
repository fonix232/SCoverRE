package com.samsung.android.content.smartclip;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputEvent;

public interface ISpenGestureService extends IInterface {

    public static abstract class Stub extends Binder implements ISpenGestureService {
        private static final String DESCRIPTOR = "com.samsung.android.content.smartclip.ISpenGestureService";
        static final int TRANSACTION_getScrollableAreaInfo = 4;
        static final int TRANSACTION_getScrollableViewInfo = 5;
        static final int TRANSACTION_getSmartClipDataByScreenRect = 1;
        static final int TRANSACTION_injectInputEvent = 3;
        static final int TRANSACTION_registerHoverListener = 8;
        static final int TRANSACTION_sendSmartClipRemoteRequestResult = 2;
        static final int TRANSACTION_setHoverStayDetectEnabled = 6;
        static final int TRANSACTION_setHoverStayValues = 7;
        static final int TRANSACTION_unregisterHoverListener = 9;

        private static class Proxy implements ISpenGestureService {
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

            public Bundle getScrollableAreaInfo(Rect rect, IBinder iBinder) throws RemoteException {
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
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(4, obtain, obtain2, 0);
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

            public Bundle getScrollableViewInfo(Rect rect, int i, IBinder iBinder) throws RemoteException {
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
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(5, obtain, obtain2, 0);
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

            public SemSmartClipDataRepository getSmartClipDataByScreenRect(Rect rect, IBinder iBinder, int i) throws RemoteException {
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
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    SemSmartClipDataRepository semSmartClipDataRepository = obtain2.readInt() != 0 ? (SemSmartClipDataRepository) SemSmartClipDataRepository.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return semSmartClipDataRepository;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void injectInputEvent(int i, int i2, InputEvent[] inputEventArr, boolean z, IBinder iBinder) throws RemoteException {
                int i3 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedArray(inputEventArr, 0);
                    if (z) {
                        i3 = 1;
                    }
                    obtain.writeInt(i3);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerHoverListener(ISpenGestureHoverListener iSpenGestureHoverListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSpenGestureHoverListener != null) {
                        iBinder = iSpenGestureHoverListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sendSmartClipRemoteRequestResult(SmartClipRemoteRequestResult smartClipRemoteRequestResult) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (smartClipRemoteRequestResult != null) {
                        obtain.writeInt(1);
                        smartClipRemoteRequestResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setHoverStayDetectEnabled(boolean z) throws RemoteException {
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

            public void setHoverStayValues(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterHoverListener(ISpenGestureHoverListener iSpenGestureHoverListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSpenGestureHoverListener != null) {
                        iBinder = iSpenGestureHoverListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(9, obtain, obtain2, 0);
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

        public static ISpenGestureService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISpenGestureService)) ? new Proxy(iBinder) : (ISpenGestureService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            Bundle scrollableAreaInfo;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    SemSmartClipDataRepository smartClipDataByScreenRect = getSmartClipDataByScreenRect(parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readStrongBinder(), parcel.readInt());
                    parcel2.writeNoException();
                    if (smartClipDataByScreenRect != null) {
                        parcel2.writeInt(1);
                        smartClipDataByScreenRect.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    sendSmartClipRemoteRequestResult(parcel.readInt() != 0 ? (SmartClipRemoteRequestResult) SmartClipRemoteRequestResult.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    injectInputEvent(parcel.readInt(), parcel.readInt(), (InputEvent[]) parcel.createTypedArray(InputEvent.CREATOR), parcel.readInt() != 0, parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    scrollableAreaInfo = getScrollableAreaInfo(parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readStrongBinder());
                    parcel2.writeNoException();
                    if (scrollableAreaInfo != null) {
                        parcel2.writeInt(1);
                        scrollableAreaInfo.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    scrollableAreaInfo = getScrollableViewInfo(parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readStrongBinder());
                    parcel2.writeNoException();
                    if (scrollableAreaInfo != null) {
                        parcel2.writeInt(1);
                        scrollableAreaInfo.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    setHoverStayDetectEnabled(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    setHoverStayValues(parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerHoverListener(com.samsung.android.content.smartclip.ISpenGestureHoverListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterHoverListener(com.samsung.android.content.smartclip.ISpenGestureHoverListener.Stub.asInterface(parcel.readStrongBinder()));
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

    Bundle getScrollableAreaInfo(Rect rect, IBinder iBinder) throws RemoteException;

    Bundle getScrollableViewInfo(Rect rect, int i, IBinder iBinder) throws RemoteException;

    SemSmartClipDataRepository getSmartClipDataByScreenRect(Rect rect, IBinder iBinder, int i) throws RemoteException;

    void injectInputEvent(int i, int i2, InputEvent[] inputEventArr, boolean z, IBinder iBinder) throws RemoteException;

    void registerHoverListener(ISpenGestureHoverListener iSpenGestureHoverListener) throws RemoteException;

    void sendSmartClipRemoteRequestResult(SmartClipRemoteRequestResult smartClipRemoteRequestResult) throws RemoteException;

    void setHoverStayDetectEnabled(boolean z) throws RemoteException;

    void setHoverStayValues(int i, int i2, int i3) throws RemoteException;

    void unregisterHoverListener(ISpenGestureHoverListener iSpenGestureHoverListener) throws RemoteException;
}
