package com.samsung.android.service.SecurityManager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISecurityManagerService extends IInterface {

    public static abstract class Stub extends Binder implements ISecurityManagerService {
        private static final String DESCRIPTOR = "com.samsung.android.service.SecurityManager.ISecurityManagerService";
        static final int TRANSACTION_enableMDFPPMode = 1;
        static final int TRANSACTION_initCCMode = 2;
        static final int TRANSACTION_verifyVPN = 3;
        static final int TRANSACTION_verifyWPA = 4;

        private static class Proxy implements ISecurityManagerService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int enableMDFPPMode(boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, obtain2, 0);
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

            public int initCCMode() throws RemoteException {
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

            public int verifyVPN() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int verifyWPA() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISecurityManagerService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISecurityManagerService)) ? new Proxy(iBinder) : (ISecurityManagerService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            boolean z = false;
            int enableMDFPPMode;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    enableMDFPPMode = enableMDFPPMode(z);
                    parcel2.writeNoException();
                    parcel2.writeInt(enableMDFPPMode);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    enableMDFPPMode = initCCMode();
                    parcel2.writeNoException();
                    parcel2.writeInt(enableMDFPPMode);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    enableMDFPPMode = verifyVPN();
                    parcel2.writeNoException();
                    parcel2.writeInt(enableMDFPPMode);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    enableMDFPPMode = verifyWPA();
                    parcel2.writeNoException();
                    parcel2.writeInt(enableMDFPPMode);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int enableMDFPPMode(boolean z) throws RemoteException;

    int initCCMode() throws RemoteException;

    int verifyVPN() throws RemoteException;

    int verifyWPA() throws RemoteException;
}
