package com.samsung.android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ISemExecuteManager extends IInterface {

    public static abstract class Stub extends Binder implements ISemExecuteManager {
        private static final String DESCRIPTOR = "com.samsung.android.app.ISemExecuteManager";
        static final int TRANSACTION_getExecutableInfo = 2;
        static final int TRANSACTION_getExecutableInfos = 1;

        private static class Proxy implements ISemExecuteManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public SemExecutableInfo getExecutableInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    SemExecutableInfo semExecutableInfo = obtain2.readInt() != 0 ? (SemExecutableInfo) SemExecutableInfo.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return semExecutableInfo;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<SemExecutableInfo> getExecutableInfos() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    List<SemExecutableInfo> createTypedArrayList = obtain2.createTypedArrayList(SemExecutableInfo.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISemExecuteManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISemExecuteManager)) ? new Proxy(iBinder) : (ISemExecuteManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    List executableInfos = getExecutableInfos();
                    parcel2.writeNoException();
                    parcel2.writeTypedList(executableInfos);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    SemExecutableInfo executableInfo = getExecutableInfo(parcel.readString());
                    parcel2.writeNoException();
                    if (executableInfo != null) {
                        parcel2.writeInt(1);
                        executableInfo.writeToParcel(parcel2, 1);
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

    SemExecutableInfo getExecutableInfo(String str) throws RemoteException;

    List<SemExecutableInfo> getExecutableInfos() throws RemoteException;
}
