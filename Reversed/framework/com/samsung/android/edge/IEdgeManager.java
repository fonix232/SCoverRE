package com.samsung.android.edge;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IEdgeManager extends IInterface {

    public static abstract class Stub extends Binder implements IEdgeManager {
        private static final String DESCRIPTOR = "com.samsung.android.edge.IEdgeManager";
        static final int TRANSACTION_bindEdgeLightingService = 1;
        static final int TRANSACTION_disable = 11;
        static final int TRANSACTION_disableEdgeLightingNotification = 12;
        static final int TRANSACTION_getEdgeLightingState = 9;
        static final int TRANSACTION_isEdgeLightingNotificationAllowed = 10;
        static final int TRANSACTION_registerEdgeLightingListener = 5;
        static final int TRANSACTION_startEdgeLighting = 7;
        static final int TRANSACTION_stopEdgeLighting = 8;
        static final int TRANSACTION_unbindEdgeLightingService = 2;
        static final int TRANSACTION_unregisterEdgeLightingListener = 6;
        static final int TRANSACTION_updateEdgeLightingPackageList = 3;
        static final int TRANSACTION_updateEdgeLightingPolicy = 4;

        private static class Proxy implements IEdgeManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void bindEdgeLightingService(IBinder iBinder, int i, ComponentName componentName) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
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

            public void disable(int i, String str, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void disableEdgeLightingNotification(String str, boolean z) throws RemoteException {
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
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getEdgeLightingState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
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

            public boolean isEdgeLightingNotificationAllowed(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
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

            public void registerEdgeLightingListener(IBinder iBinder, ComponentName componentName) throws RemoteException {
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
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startEdgeLighting(String str, SemEdgeLightingInfo semEdgeLightingInfo, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (semEdgeLightingInfo != null) {
                        obtain.writeInt(1);
                        semEdgeLightingInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopEdgeLighting(String str, IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unbindEdgeLightingService(IBinder iBinder, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterEdgeLightingListener(IBinder iBinder, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateEdgeLightingPackageList(String str, List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStringList(list);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateEdgeLightingPolicy(String str, EdgeLightingPolicy edgeLightingPolicy) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (edgeLightingPolicy != null) {
                        obtain.writeInt(1);
                        edgeLightingPolicy.writeToParcel(obtain, 0);
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

        public static IEdgeManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IEdgeManager)) ? new Proxy(iBinder) : (IEdgeManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    bindEdgeLightingService(parcel.readStrongBinder(), parcel.readInt(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    unbindEdgeLightingService(parcel.readStrongBinder(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateEdgeLightingPackageList(parcel.readString(), parcel.createStringArrayList());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateEdgeLightingPolicy(parcel.readString(), parcel.readInt() != 0 ? (EdgeLightingPolicy) EdgeLightingPolicy.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerEdgeLightingListener(parcel.readStrongBinder(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregisterEdgeLightingListener(parcel.readStrongBinder(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    startEdgeLighting(parcel.readString(), parcel.readInt() != 0 ? (SemEdgeLightingInfo) SemEdgeLightingInfo.CREATOR.createFromParcel(parcel) : null, parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    stopEdgeLighting(parcel.readString(), parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    int edgeLightingState = getEdgeLightingState();
                    parcel2.writeNoException();
                    parcel2.writeInt(edgeLightingState);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isEdgeLightingNotificationAllowed = isEdgeLightingNotificationAllowed(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isEdgeLightingNotificationAllowed ? 1 : 0);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    disable(parcel.readInt(), parcel.readString(), parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    disableEdgeLightingNotification(parcel.readString(), parcel.readInt() != 0);
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

    void bindEdgeLightingService(IBinder iBinder, int i, ComponentName componentName) throws RemoteException;

    void disable(int i, String str, IBinder iBinder) throws RemoteException;

    void disableEdgeLightingNotification(String str, boolean z) throws RemoteException;

    int getEdgeLightingState() throws RemoteException;

    boolean isEdgeLightingNotificationAllowed(String str) throws RemoteException;

    void registerEdgeLightingListener(IBinder iBinder, ComponentName componentName) throws RemoteException;

    void startEdgeLighting(String str, SemEdgeLightingInfo semEdgeLightingInfo, IBinder iBinder) throws RemoteException;

    void stopEdgeLighting(String str, IBinder iBinder) throws RemoteException;

    void unbindEdgeLightingService(IBinder iBinder, String str) throws RemoteException;

    void unregisterEdgeLightingListener(IBinder iBinder, String str) throws RemoteException;

    void updateEdgeLightingPackageList(String str, List<String> list) throws RemoteException;

    void updateEdgeLightingPolicy(String str, EdgeLightingPolicy edgeLightingPolicy) throws RemoteException;
}
