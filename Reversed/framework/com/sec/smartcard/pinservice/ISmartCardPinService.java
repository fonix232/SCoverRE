package com.sec.smartcard.pinservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISmartCardPinService extends IInterface {

    public static abstract class Stub extends Binder implements ISmartCardPinService {
        private static final String DESCRIPTOR = "com.sec.smartcard.pinservice.ISmartCardPinService";
        static final int TRANSACTION_getCardLoginAttemptRemain = 5;
        static final int TRANSACTION_getPin = 1;
        static final int TRANSACTION_isCardRegistered = 6;
        static final int TRANSACTION_isDeviceConnectedWithCard = 7;
        static final int TRANSACTION_isSmartCardAuthenticationAvailable = 8;
        static final int TRANSACTION_registerCard = 2;
        static final int TRANSACTION_showCardNotRegisteredDialog = 9;
        static final int TRANSACTION_unRegisterCard = 3;
        static final int TRANSACTION_verifyCard = 4;

        private static class Proxy implements ISmartCardPinService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void getCardLoginAttemptRemain(ISmartCardInfoCallback iSmartCardInfoCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSmartCardInfoCallback != null) {
                        iBinder = iSmartCardInfoCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void getPin(ISmartCardGetPinCallback iSmartCardGetPinCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSmartCardGetPinCallback != null) {
                        iBinder = iSmartCardGetPinCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isCardRegistered() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isDeviceConnectedWithCard() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isSmartCardAuthenticationAvailable() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerCard(char[] cArr, ISmartCardRegisterCallback iSmartCardRegisterCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeCharArray(cArr);
                    if (iSmartCardRegisterCallback != null) {
                        iBinder = iSmartCardRegisterCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showCardNotRegisteredDialog() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unRegisterCard(char[] cArr, ISmartCardRegisterCallback iSmartCardRegisterCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeCharArray(cArr);
                    if (iSmartCardRegisterCallback != null) {
                        iBinder = iSmartCardRegisterCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void verifyCard(char[] cArr, ISmartCardVerifyCallback iSmartCardVerifyCallback) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeCharArray(cArr);
                    if (iSmartCardVerifyCallback != null) {
                        iBinder = iSmartCardVerifyCallback.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
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

        public static ISmartCardPinService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface != null && (queryLocalInterface instanceof ISmartCardPinService)) ? (ISmartCardPinService) queryLocalInterface : new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = 0;
            boolean isCardRegistered;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    getPin(com.sec.smartcard.pinservice.ISmartCardGetPinCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    registerCard(parcel.createCharArray(), com.sec.smartcard.pinservice.ISmartCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    unRegisterCard(parcel.createCharArray(), com.sec.smartcard.pinservice.ISmartCardRegisterCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    verifyCard(parcel.createCharArray(), com.sec.smartcard.pinservice.ISmartCardVerifyCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    getCardLoginAttemptRemain(com.sec.smartcard.pinservice.ISmartCardInfoCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    isCardRegistered = isCardRegistered();
                    parcel2.writeNoException();
                    if (isCardRegistered) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    isCardRegistered = isDeviceConnectedWithCard();
                    parcel2.writeNoException();
                    if (isCardRegistered) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    isCardRegistered = isSmartCardAuthenticationAvailable();
                    parcel2.writeNoException();
                    if (isCardRegistered) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    showCardNotRegisteredDialog();
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

    void getCardLoginAttemptRemain(ISmartCardInfoCallback iSmartCardInfoCallback) throws RemoteException;

    void getPin(ISmartCardGetPinCallback iSmartCardGetPinCallback) throws RemoteException;

    boolean isCardRegistered() throws RemoteException;

    boolean isDeviceConnectedWithCard() throws RemoteException;

    boolean isSmartCardAuthenticationAvailable() throws RemoteException;

    void registerCard(char[] cArr, ISmartCardRegisterCallback iSmartCardRegisterCallback) throws RemoteException;

    void showCardNotRegisteredDialog() throws RemoteException;

    void unRegisterCard(char[] cArr, ISmartCardRegisterCallback iSmartCardRegisterCallback) throws RemoteException;

    void verifyCard(char[] cArr, ISmartCardVerifyCallback iSmartCardVerifyCallback) throws RemoteException;
}
