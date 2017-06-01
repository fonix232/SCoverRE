package com.samsung.android.directpeninput;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;

public interface ISemDirectPenInputManager extends IInterface {

    public static abstract class Stub extends Binder implements ISemDirectPenInputManager {
        private static final String DESCRIPTOR = "com.samsung.android.directpeninput.ISemDirectPenInputManager";
        static final int TRANSACTION_dismiss = 3;
        static final int TRANSACTION_getImage = 6;
        static final int TRANSACTION_isShowing = 7;
        static final int TRANSACTION_show = 1;
        static final int TRANSACTION_showPopup = 8;
        static final int TRANSACTION_showTemplate = 2;
        static final int TRANSACTION_updateDialog = 5;
        static final int TRANSACTION_updatePosition = 4;

        private static class Proxy implements ISemDirectPenInputManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void dismiss(int i, boolean z) throws RemoteException {
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
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ParcelFileDescriptor getImage(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    ParcelFileDescriptor parcelFileDescriptor = obtain2.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return parcelFileDescriptor;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public boolean isShowing() throws RemoteException {
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

            public void show(int i, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, Rect rect, Rect rect2, Rect rect3, ExtractedText extractedText, EditorInfo editorInfo, int i2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    obtain.writeStrongBinder(iBinder3);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect2 != null) {
                        obtain.writeInt(1);
                        rect2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect3 != null) {
                        obtain.writeInt(1);
                        rect3.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (extractedText != null) {
                        obtain.writeInt(1);
                        extractedText.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (editorInfo != null) {
                        obtain.writeInt(1);
                        editorInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i2);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showPopup(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showTemplate(int i, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, Rect rect, Rect rect2, Rect rect3, int i2, ExtractedText extractedText, EditorInfo editorInfo, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    obtain.writeStrongBinder(iBinder3);
                    if (rect != null) {
                        obtain.writeInt(1);
                        rect.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect2 != null) {
                        obtain.writeInt(1);
                        rect2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (rect3 != null) {
                        obtain.writeInt(1);
                        rect3.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i2);
                    if (extractedText != null) {
                        obtain.writeInt(1);
                        extractedText.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (editorInfo != null) {
                        obtain.writeInt(1);
                        editorInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i3);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateDialog(int i, Rect rect, Rect rect2) throws RemoteException {
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
                    if (rect2 != null) {
                        obtain.writeInt(1);
                        rect2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updatePosition(int i, Rect rect, Rect rect2) throws RemoteException {
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
                    if (rect2 != null) {
                        obtain.writeInt(1);
                        rect2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(4, obtain, obtain2, 0);
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

        public static ISemDirectPenInputManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemDirectPenInputManager)) ? new Proxy(iBinder) : (ISemDirectPenInputManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    show(parcel.readInt(), parcel.readStrongBinder(), parcel.readStrongBinder(), parcel.readStrongBinder(), parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (ExtractedText) ExtractedText.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (EditorInfo) EditorInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    showTemplate(parcel.readInt(), parcel.readStrongBinder(), parcel.readStrongBinder(), parcel.readStrongBinder(), parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt(), parcel.readInt() != 0 ? (ExtractedText) ExtractedText.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (EditorInfo) EditorInfo.CREATOR.createFromParcel(parcel) : null, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    dismiss(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    updatePosition(parcel.readInt(), parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateDialog(parcel.readInt(), parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null, parcel.readInt() != 0 ? (Rect) Rect.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor image = getImage(parcel.readInt());
                    parcel2.writeNoException();
                    if (image != null) {
                        parcel2.writeInt(1);
                        image.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isShowing = isShowing();
                    parcel2.writeNoException();
                    parcel2.writeInt(isShowing ? 1 : 0);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    showPopup(parcel.readInt(), parcel.readInt());
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

    void dismiss(int i, boolean z) throws RemoteException;

    ParcelFileDescriptor getImage(int i) throws RemoteException;

    boolean isShowing() throws RemoteException;

    void show(int i, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, Rect rect, Rect rect2, Rect rect3, ExtractedText extractedText, EditorInfo editorInfo, int i2, boolean z) throws RemoteException;

    void showPopup(int i, int i2) throws RemoteException;

    void showTemplate(int i, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, Rect rect, Rect rect2, Rect rect3, int i2, ExtractedText extractedText, EditorInfo editorInfo, int i3) throws RemoteException;

    void updateDialog(int i, Rect rect, Rect rect2) throws RemoteException;

    void updatePosition(int i, Rect rect, Rect rect2) throws RemoteException;
}
