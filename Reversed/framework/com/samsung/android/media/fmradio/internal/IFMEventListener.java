package com.samsung.android.media.fmradio.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFMEventListener extends IInterface {

    public static abstract class Stub extends Binder implements IFMEventListener {
        private static final String DESCRIPTOR = "com.samsung.android.media.fmradio.internal.IFMEventListener";
        static final int TRANSACTION_onAlternateFrequencyReceived = 15;
        static final int TRANSACTION_onAlternateFrequencyStarted = 14;
        static final int TRANSACTION_onChannelFound = 3;
        static final int TRANSACTION_onHeadsetConnected = 8;
        static final int TRANSACTION_onHeadsetDisconnected = 9;
        static final int f18xac9d95b4 = 18;
        static final int TRANSACTION_onRadioDataSystemDisabled = 13;
        static final int TRANSACTION_onRadioDataSystemEnabled = 12;
        static final int TRANSACTION_onRadioDataSystemReceived = 10;
        static final int TRANSACTION_onRadioDisabled = 2;
        static final int TRANSACTION_onRadioEnabled = 1;
        static final int TRANSACTION_onRadioTextPlusReceived = 11;
        static final int TRANSACTION_onRecordingFinished = 17;
        static final int TRANSACTION_onScanFinished = 6;
        static final int TRANSACTION_onScanStarted = 4;
        static final int TRANSACTION_onScanStopped = 5;
        static final int TRANSACTION_onTuned = 7;
        static final int TRANSACTION_onVolumeLocked = 16;

        private static class Proxy implements IFMEventListener {
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

            public void onAlternateFrequencyReceived(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onAlternateFrequencyStarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onChannelFound(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onHeadsetConnected() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onHeadsetDisconnected() throws RemoteException {
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

            public void onProgrammeIdentificationExtendedCountryCodesReceived(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRadioDataSystemDisabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRadioDataSystemEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRadioDataSystemReceived(long j, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRadioDisabled(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRadioEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRadioTextPlusReceived(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeInt(i5);
                    obtain.writeInt(i6);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRecordingFinished() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onScanFinished(long[] jArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (jArr == null) {
                        obtain.writeInt(-1);
                    } else {
                        obtain.writeInt(jArr.length);
                    }
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    obtain2.readLongArray(jArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onScanStarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onScanStopped(long[] jArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (jArr == null) {
                        obtain.writeInt(-1);
                    } else {
                        obtain.writeInt(jArr.length);
                    }
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    obtain2.readLongArray(jArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onTuned(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onVolumeLocked() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, obtain, obtain2, 0);
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

        public static IFMEventListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IFMEventListener)) ? new Proxy(iBinder) : (IFMEventListener) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int readInt;
            long[] jArr;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRadioEnabled();
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRadioDisabled(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    onChannelFound(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    onScanStarted();
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    readInt = parcel.readInt();
                    jArr = readInt < 0 ? null : new long[readInt];
                    onScanStopped(jArr);
                    parcel2.writeNoException();
                    parcel2.writeLongArray(jArr);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    readInt = parcel.readInt();
                    jArr = readInt < 0 ? null : new long[readInt];
                    onScanFinished(jArr);
                    parcel2.writeNoException();
                    parcel2.writeLongArray(jArr);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    onTuned(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    onHeadsetConnected();
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    onHeadsetDisconnected();
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRadioDataSystemReceived(parcel.readLong(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRadioTextPlusReceived(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRadioDataSystemEnabled();
                    parcel2.writeNoException();
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRadioDataSystemDisabled();
                    parcel2.writeNoException();
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    onAlternateFrequencyStarted();
                    parcel2.writeNoException();
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    onAlternateFrequencyReceived(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    onVolumeLocked();
                    parcel2.writeNoException();
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRecordingFinished();
                    parcel2.writeNoException();
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    onProgrammeIdentificationExtendedCountryCodesReceived(parcel.readInt(), parcel.readInt());
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

    void onAlternateFrequencyReceived(long j) throws RemoteException;

    void onAlternateFrequencyStarted() throws RemoteException;

    void onChannelFound(long j) throws RemoteException;

    void onHeadsetConnected() throws RemoteException;

    void onHeadsetDisconnected() throws RemoteException;

    void onProgrammeIdentificationExtendedCountryCodesReceived(int i, int i2) throws RemoteException;

    void onRadioDataSystemDisabled() throws RemoteException;

    void onRadioDataSystemEnabled() throws RemoteException;

    void onRadioDataSystemReceived(long j, String str, String str2) throws RemoteException;

    void onRadioDisabled(int i) throws RemoteException;

    void onRadioEnabled() throws RemoteException;

    void onRadioTextPlusReceived(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void onRecordingFinished() throws RemoteException;

    void onScanFinished(long[] jArr) throws RemoteException;

    void onScanStarted() throws RemoteException;

    void onScanStopped(long[] jArr) throws RemoteException;

    void onTuned(long j) throws RemoteException;

    void onVolumeLocked() throws RemoteException;
}
