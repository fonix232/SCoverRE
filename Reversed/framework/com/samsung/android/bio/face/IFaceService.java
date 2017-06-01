package com.samsung.android.bio.face;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IFaceService extends IInterface {

    public static abstract class Stub extends Binder implements IFaceService {
        private static final String DESCRIPTOR = "com.samsung.android.bio.face.IFaceService";
        static final int TRANSACTION_authenticate = 1;
        static final int TRANSACTION_cancelAuthentication = 2;
        static final int TRANSACTION_cancelEnrollment = 4;
        static final int TRANSACTION_enroll = 3;
        static final int TRANSACTION_getAuthenticatorId = 12;
        static final int TRANSACTION_getEnrolledFaces = 7;
        static final int TRANSACTION_hasEnrolledFaces = 11;
        static final int TRANSACTION_isEnrollSession = 13;
        static final int TRANSACTION_isHardwareDetected = 8;
        static final int TRANSACTION_isSessionClosed = 18;
        static final int TRANSACTION_postEnroll = 10;
        static final int TRANSACTION_preEnroll = 9;
        static final int TRANSACTION_remove = 5;
        static final int TRANSACTION_rename = 6;
        static final int TRANSACTION_requestSessionClose = 17;
        static final int TRANSACTION_requestSessionOpen = 16;
        static final int TRANSACTION_resetTimeout = 14;
        static final int TRANSACTION_setActiveUser = 15;

        private static class Proxy implements IFaceService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void authenticate(IBinder iBinder, IBinder iBinder2, int i, int i2, int i3, int i4, long j, int i5, IFaceServiceReceiver iFaceServiceReceiver, int i6, String str, Bundle bundle, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeLong(j);
                    obtain.writeInt(i5);
                    obtain.writeStrongBinder(iFaceServiceReceiver != null ? iFaceServiceReceiver.asBinder() : null);
                    obtain.writeInt(i6);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cancelAuthentication(IBinder iBinder, String str) throws RemoteException {
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

            public void cancelEnrollment(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enroll(IBinder iBinder, IBinder iBinder2, int i, int i2, int i3, int i4, byte[] bArr, int i5, IFaceServiceReceiver iFaceServiceReceiver, int i6, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i5);
                    obtain.writeStrongBinder(iFaceServiceReceiver != null ? iFaceServiceReceiver.asBinder() : null);
                    obtain.writeInt(i6);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getAuthenticatorId(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<Face> getEnrolledFaces(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    List<Face> createTypedArrayList = obtain2.createTypedArrayList(Face.CREATOR);
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public boolean hasEnrolledFaces(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(11, obtain, obtain2, 0);
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

            public boolean isEnrollSession() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, obtain, obtain2, 0);
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

            public boolean isHardwareDetected(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(8, obtain, obtain2, 0);
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

            public boolean isSessionClosed() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, obtain, obtain2, 0);
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

            public int postEnroll(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long preEnroll(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    long readLong = obtain2.readLong();
                    return readLong;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void remove(IBinder iBinder, int i, int i2, int i3, IFaceServiceReceiver iFaceServiceReceiver) throws RemoteException {
                IBinder iBinder2 = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (iFaceServiceReceiver != null) {
                        iBinder2 = iFaceServiceReceiver.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder2);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void rename(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestSessionClose() throws RemoteException {
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

            public void requestSessionOpen() throws RemoteException {
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

            public void resetTimeout(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setActiveUser(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, obtain2, 0);
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

        public static IFaceService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceService)) ? new Proxy(iBinder) : (IFaceService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            boolean isHardwareDetected;
            long preEnroll;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    authenticate(parcel.readStrongBinder(), parcel.readStrongBinder(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readLong(), parcel.readInt(), com.samsung.android.bio.face.IFaceServiceReceiver.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null, parcel.createByteArray());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    cancelAuthentication(parcel.readStrongBinder(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    enroll(parcel.readStrongBinder(), parcel.readStrongBinder(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.createByteArray(), parcel.readInt(), com.samsung.android.bio.face.IFaceServiceReceiver.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readString(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    cancelEnrollment(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    remove(parcel.readStrongBinder(), parcel.readInt(), parcel.readInt(), parcel.readInt(), com.samsung.android.bio.face.IFaceServiceReceiver.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    rename(parcel.readInt(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    List enrolledFaces = getEnrolledFaces(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeTypedList(enrolledFaces);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    isHardwareDetected = isHardwareDetected(parcel.readLong(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isHardwareDetected ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    preEnroll = preEnroll(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    parcel2.writeLong(preEnroll);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    int postEnroll = postEnroll(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    parcel2.writeInt(postEnroll);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    isHardwareDetected = hasEnrolledFaces(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(isHardwareDetected ? 1 : 0);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    preEnroll = getAuthenticatorId(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeLong(preEnroll);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    isHardwareDetected = isEnrollSession();
                    parcel2.writeNoException();
                    parcel2.writeInt(isHardwareDetected ? 1 : 0);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    resetTimeout(parcel.createByteArray());
                    parcel2.writeNoException();
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    setActiveUser(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestSessionOpen();
                    parcel2.writeNoException();
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestSessionClose();
                    parcel2.writeNoException();
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    isHardwareDetected = isSessionClosed();
                    parcel2.writeNoException();
                    parcel2.writeInt(isHardwareDetected ? 1 : 0);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void authenticate(IBinder iBinder, IBinder iBinder2, int i, int i2, int i3, int i4, long j, int i5, IFaceServiceReceiver iFaceServiceReceiver, int i6, String str, Bundle bundle, byte[] bArr) throws RemoteException;

    void cancelAuthentication(IBinder iBinder, String str) throws RemoteException;

    void cancelEnrollment(IBinder iBinder) throws RemoteException;

    void enroll(IBinder iBinder, IBinder iBinder2, int i, int i2, int i3, int i4, byte[] bArr, int i5, IFaceServiceReceiver iFaceServiceReceiver, int i6, String str, Bundle bundle) throws RemoteException;

    long getAuthenticatorId(String str) throws RemoteException;

    List<Face> getEnrolledFaces(int i, String str) throws RemoteException;

    boolean hasEnrolledFaces(int i, String str) throws RemoteException;

    boolean isEnrollSession() throws RemoteException;

    boolean isHardwareDetected(long j, String str) throws RemoteException;

    boolean isSessionClosed() throws RemoteException;

    int postEnroll(IBinder iBinder) throws RemoteException;

    long preEnroll(IBinder iBinder) throws RemoteException;

    void remove(IBinder iBinder, int i, int i2, int i3, IFaceServiceReceiver iFaceServiceReceiver) throws RemoteException;

    void rename(int i, int i2, String str) throws RemoteException;

    void requestSessionClose() throws RemoteException;

    void requestSessionOpen() throws RemoteException;

    void resetTimeout(byte[] bArr) throws RemoteException;

    void setActiveUser(int i) throws RemoteException;
}
