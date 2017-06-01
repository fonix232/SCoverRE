package com.samsung.android.cocktailbar;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICocktailBarStateCallback extends IInterface {

    public static abstract class Stub extends Binder implements ICocktailBarStateCallback {
        private static final String DESCRIPTOR = "com.samsung.android.cocktailbar.ICocktailBarStateCallback";
        static final int TRANSACTION_onCocktailBarStateChanged = 1;

        private static class Proxy implements ICocktailBarStateCallback {
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

            public void onCocktailBarStateChanged(CocktailBarStateInfo cocktailBarStateInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cocktailBarStateInfo != null) {
                        obtain.writeInt(1);
                        cocktailBarStateInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICocktailBarStateCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ICocktailBarStateCallback)) ? new Proxy(iBinder) : (ICocktailBarStateCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onCocktailBarStateChanged(parcel.readInt() != 0 ? (CocktailBarStateInfo) CocktailBarStateInfo.CREATOR.createFromParcel(parcel) : null);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onCocktailBarStateChanged(CocktailBarStateInfo cocktailBarStateInfo) throws RemoteException;
}
