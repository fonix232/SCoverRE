package com.samsung.android.hardware.context;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

public interface ISemContextService extends IInterface {

    public static abstract class Stub extends Binder implements ISemContextService {
        private static final String DESCRIPTOR = "com.samsung.android.hardware.context.ISemContextService";
        static final int TRANSACTION_changeParameters = 4;
        static final int TRANSACTION_getAvailableServiceMap = 5;
        static final int TRANSACTION_getMotionRecognitionService = 9;
        static final int TRANSACTION_initializeService = 3;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_requestHistoryData = 8;
        static final int TRANSACTION_requestToUpdate = 7;
        static final int TRANSACTION_setReferenceData = 6;
        static final int TRANSACTION_unregisterCallback = 2;

        private static class Proxy implements ISemContextService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean changeParameters(IBinder iBinder, int i, SemContextAttribute semContextAttribute) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    if (semContextAttribute != null) {
                        obtain.writeInt(1);
                        semContextAttribute.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

            public Map getAvailableServiceMap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
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

            public IBinder getMotionRecognitionService() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    IBinder readStrongBinder = obtain2.readStrongBinder();
                    return readStrongBinder;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void initializeService(IBinder iBinder, int i) throws RemoteException {
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

            public void registerCallback(IBinder iBinder, int i, SemContextAttribute semContextAttribute) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    if (semContextAttribute != null) {
                        obtain.writeInt(1);
                        semContextAttribute.writeToParcel(obtain, 0);
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

            public void requestHistoryData(IBinder iBinder, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestToUpdate(IBinder iBinder, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setReferenceData(int i, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
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

            public boolean unregisterCallback(IBinder iBinder, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
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

        public static ISemContextService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemContextService)) ? new Proxy(iBinder) : (ISemContextService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = 0;
            boolean unregisterCallback;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCallback(parcel.readStrongBinder(), parcel.readInt(), parcel.readInt() != 0 ? (SemContextAttribute) SemContextAttribute.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterCallback = unregisterCallback(parcel.readStrongBinder(), parcel.readInt());
                    parcel2.writeNoException();
                    if (unregisterCallback) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    initializeService(parcel.readStrongBinder(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterCallback = changeParameters(parcel.readStrongBinder(), parcel.readInt(), parcel.readInt() != 0 ? (SemContextAttribute) SemContextAttribute.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (unregisterCallback) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    Map availableServiceMap = getAvailableServiceMap();
                    parcel2.writeNoException();
                    parcel2.writeMap(availableServiceMap);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterCallback = setReferenceData(parcel.readInt(), parcel.createByteArray());
                    parcel2.writeNoException();
                    if (unregisterCallback) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestToUpdate(parcel.readStrongBinder(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestHistoryData(parcel.readStrongBinder(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    IBinder motionRecognitionService = getMotionRecognitionService();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(motionRecognitionService);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean changeParameters(IBinder iBinder, int i, SemContextAttribute semContextAttribute) throws RemoteException;

    Map getAvailableServiceMap() throws RemoteException;

    IBinder getMotionRecognitionService() throws RemoteException;

    void initializeService(IBinder iBinder, int i) throws RemoteException;

    void registerCallback(IBinder iBinder, int i, SemContextAttribute semContextAttribute) throws RemoteException;

    void requestHistoryData(IBinder iBinder, int i) throws RemoteException;

    void requestToUpdate(IBinder iBinder, int i) throws RemoteException;

    boolean setReferenceData(int i, byte[] bArr) throws RemoteException;

    boolean unregisterCallback(IBinder iBinder, int i) throws RemoteException;
}
