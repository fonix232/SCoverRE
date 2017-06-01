package com.samsung.android.toolbox;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITwToolBoxService extends IInterface {

    public static abstract class Stub extends Binder implements ITwToolBoxService {
        private static final String DESCRIPTOR = "com.samsung.android.toolbox.ITwToolBoxService";
        static final int TRANSACTION_getToolList = 5;
        static final int TRANSACTION_isContain = 3;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_sendMessage = 4;
        static final int TRANSACTION_setToolList = 6;
        static final int TRANSACTION_unregisterCallback = 2;

        private static class Proxy implements ITwToolBoxService {
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

            public String getToolList() throws RemoteException {
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

            public boolean isContain(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
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

            public boolean registerCallback(ITwToolBoxServiceCallback iTwToolBoxServiceCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iTwToolBoxServiceCallback != null) {
                        iBinder = iTwToolBoxServiceCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
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

            public void sendMessage(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setToolList(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean unregisterCallback(ITwToolBoxServiceCallback iTwToolBoxServiceCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iTwToolBoxServiceCallback != null) {
                        iBinder = iTwToolBoxServiceCallback.asBinder();
                    }
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITwToolBoxService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ITwToolBoxService)) ? new Proxy(iBinder) : (ITwToolBoxService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = 0;
            boolean registerCallback;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = registerCallback(com.samsung.android.toolbox.ITwToolBoxServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (registerCallback) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = unregisterCallback(com.samsung.android.toolbox.ITwToolBoxServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (registerCallback) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback = isContain(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    if (registerCallback) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    sendMessage(parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    String toolList = getToolList();
                    parcel2.writeNoException();
                    parcel2.writeString(toolList);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    setToolList(parcel.readString());
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

    String getToolList() throws RemoteException;

    boolean isContain(int i, int i2) throws RemoteException;

    boolean registerCallback(ITwToolBoxServiceCallback iTwToolBoxServiceCallback) throws RemoteException;

    void sendMessage(String str, int i, int i2) throws RemoteException;

    void setToolList(String str) throws RemoteException;

    boolean unregisterCallback(ITwToolBoxServiceCallback iTwToolBoxServiceCallback) throws RemoteException;
}
