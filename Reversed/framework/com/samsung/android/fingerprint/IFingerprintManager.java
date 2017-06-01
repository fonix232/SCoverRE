package com.samsung.android.fingerprint;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFingerprintManager extends IInterface {

    public static abstract class Stub extends Binder implements IFingerprintManager {
        private static final String DESCRIPTOR = "com.samsung.android.fingerprint.IFingerprintManager";
        static final int TRANSACTION_cancel = 20;
        static final int TRANSACTION_enroll = 21;
        static final int TRANSACTION_getDaemonVersion = 11;
        static final int TRANSACTION_getEnrollRepeatCount = 37;
        static final int TRANSACTION_getEnrolledFingers = 2;
        static final int TRANSACTION_getFingerprintIdByFinger = 36;
        static final int TRANSACTION_getFingerprintIds = 35;
        static final int TRANSACTION_getIndexName = 29;
        static final int TRANSACTION_getLastImageQuality = 32;
        static final int TRANSACTION_getLastImageQualityMessage = 31;
        static final int TRANSACTION_getSensorInfo = 10;
        static final int TRANSACTION_getUserIdList = 12;
        static final int TRANSACTION_getVersion = 1;
        static final int TRANSACTION_hasPendingCommand = 5;
        static final int TRANSACTION_identify = 22;
        static final int TRANSACTION_identifyForMultiUser = 23;
        static final int TRANSACTION_identifyWithDialog = 25;
        static final int TRANSACTION_isEnrollSession = 34;
        static final int TRANSACTION_isSensorReady = 26;
        static final int TRANSACTION_isVZWPermissionGranted = 33;
        static final int TRANSACTION_notifyApplicationState = 17;
        static final int TRANSACTION_notifyEnrollBegin = 6;
        static final int TRANSACTION_notifyEnrollEnd = 7;
        static final int TRANSACTION_pauseEnroll = 8;
        static final int TRANSACTION_process = 28;
        static final int TRANSACTION_processFIDO = 27;
        static final int TRANSACTION_registerClient = 18;
        static final int TRANSACTION_removeAllEnrolledFingers = 4;
        static final int TRANSACTION_removeEnrolledFinger = 3;
        static final int TRANSACTION_request = 14;
        static final int TRANSACTION_resumeEnroll = 9;
        static final int TRANSACTION_setIndexName = 30;
        static final int TRANSACTION_setPassword = 16;
        static final int TRANSACTION_showIdentifyDialog = 24;
        static final int TRANSACTION_unregisterClient = 19;
        static final int TRANSACTION_verifyPassword = 15;
        static final int TRANSACTION_verifySensorState = 13;

        private static class Proxy implements IFingerprintManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int cancel(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int enroll(IBinder iBinder, String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getDaemonVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getEnrollRepeatCount() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getEnrolledFingers(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getFingerprintIdByFinger(int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(36, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getFingerprintIds(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(35, obtain, obtain2, 0);
                    obtain2.readException();
                    String[] createStringArray = obtain2.createStringArray();
                    return createStringArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getIndexName(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getLastImageQuality(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getLastImageQualityMessage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getSensorInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getUserIdList() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    String[] createStringArray = obtain2.createStringArray();
                    return createStringArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean hasPendingCommand() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
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

            public int identify(IBinder iBinder, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int identifyForMultiUser(IBinder iBinder, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int identifyWithDialog(String str, ComponentName componentName, IFingerprintClient iFingerprintClient, Bundle bundle) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (componentName != null) {
                        obtain.writeInt(1);
                        componentName.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (iFingerprintClient != null) {
                        iBinder = iFingerprintClient.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isEnrollSession() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, obtain, obtain2, 0);
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

            public boolean isSensorReady() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, obtain, obtain2, 0);
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

            public boolean isVZWPermissionGranted(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(33, obtain, obtain2, 0);
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

            public void notifyApplicationState(int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean notifyEnrollBegin() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean notifyEnrollEnd() throws RemoteException {
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

            public boolean pauseEnroll() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public byte[] process(IBinder iBinder, String str, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] processFIDO(IBinder iBinder, String str, String str2, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder registerClient(IFingerprintClient iFingerprintClient, Bundle bundle) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iFingerprintClient != null) {
                        iBinder = iFingerprintClient.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    IBinder readStrongBinder = obtain2.readStrongBinder();
                    return readStrongBinder;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean removeAllEnrolledFingers(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
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

            public boolean removeEnrolledFinger(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
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

            public int request(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean resumeEnroll() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
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

            public boolean setIndexName(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(30, obtain, obtain2, 0);
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

            public boolean setPassword(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(16, obtain, obtain2, 0);
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

            public int showIdentifyDialog(IBinder iBinder, ComponentName componentName, String str, boolean z) throws RemoteException {
                int i = 1;
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
                    obtain.writeString(str);
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean unregisterClient(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(19, obtain, obtain2, 0);
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

            public boolean verifyPassword(IBinder iBinder, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(15, obtain, obtain2, 0);
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

            public int verifySensorState(int i, int i2, int i3, int i4, int i5) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeInt(i5);
                    this.mRemote.transact(13, obtain, obtain2, 0);
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

        public static IFingerprintManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IFingerprintManager)) ? new Proxy(iBinder) : (IFingerprintManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int version;
            boolean removeEnrolledFinger;
            String sensorInfo;
            String[] userIdList;
            byte[] processFIDO;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = getVersion();
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = getEnrolledFingers(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = removeEnrolledFinger(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = removeAllEnrolledFingers(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = hasPendingCommand();
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = notifyEnrollBegin();
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = notifyEnrollEnd();
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = pauseEnroll();
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = resumeEnroll();
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    sensorInfo = getSensorInfo();
                    parcel2.writeNoException();
                    parcel2.writeString(sensorInfo);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    sensorInfo = getDaemonVersion();
                    parcel2.writeNoException();
                    parcel2.writeString(sensorInfo);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    userIdList = getUserIdList();
                    parcel2.writeNoException();
                    parcel2.writeStringArray(userIdList);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = verifySensorState(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = request(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = verifyPassword(parcel.readStrongBinder(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = setPassword(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    notifyApplicationState(parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    IBinder registerClient = registerClient(com.samsung.android.fingerprint.IFingerprintClient.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(registerClient);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = unregisterClient(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = cancel(parcel.readStrongBinder());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = enroll(parcel.readStrongBinder(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = identify(parcel.readStrongBinder(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = identifyForMultiUser(parcel.readStrongBinder(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = showIdentifyDialog(parcel.readStrongBinder(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null, parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = identifyWithDialog(parcel.readString(), parcel.readInt() != 0 ? (ComponentName) ComponentName.CREATOR.createFromParcel(parcel) : null, com.samsung.android.fingerprint.IFingerprintClient.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = isSensorReady();
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    processFIDO = processFIDO(parcel.readStrongBinder(), parcel.readString(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(processFIDO);
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    processFIDO = process(parcel.readStrongBinder(), parcel.readString(), parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(processFIDO);
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    sensorInfo = getIndexName(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(sensorInfo);
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = setIndexName(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    sensorInfo = getLastImageQualityMessage(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(sensorInfo);
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = getLastImageQuality(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = isVZWPermissionGranted(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    removeEnrolledFinger = isEnrollSession();
                    parcel2.writeNoException();
                    parcel2.writeInt(removeEnrolledFinger ? 1 : 0);
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    userIdList = getFingerprintIds(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeStringArray(userIdList);
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    sensorInfo = getFingerprintIdByFinger(parcel.readInt(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(sensorInfo);
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    version = getEnrollRepeatCount();
                    parcel2.writeNoException();
                    parcel2.writeInt(version);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int cancel(IBinder iBinder) throws RemoteException;

    int enroll(IBinder iBinder, String str, int i) throws RemoteException;

    String getDaemonVersion() throws RemoteException;

    int getEnrollRepeatCount() throws RemoteException;

    int getEnrolledFingers(String str) throws RemoteException;

    String getFingerprintIdByFinger(int i, String str, String str2) throws RemoteException;

    String[] getFingerprintIds(String str, String str2) throws RemoteException;

    String getIndexName(int i) throws RemoteException;

    int getLastImageQuality(String str) throws RemoteException;

    String getLastImageQualityMessage(String str) throws RemoteException;

    String getSensorInfo() throws RemoteException;

    String[] getUserIdList() throws RemoteException;

    int getVersion() throws RemoteException;

    boolean hasPendingCommand() throws RemoteException;

    int identify(IBinder iBinder, String str) throws RemoteException;

    int identifyForMultiUser(IBinder iBinder, int i, String str) throws RemoteException;

    int identifyWithDialog(String str, ComponentName componentName, IFingerprintClient iFingerprintClient, Bundle bundle) throws RemoteException;

    boolean isEnrollSession() throws RemoteException;

    boolean isSensorReady() throws RemoteException;

    boolean isVZWPermissionGranted(String str) throws RemoteException;

    void notifyApplicationState(int i, Bundle bundle) throws RemoteException;

    boolean notifyEnrollBegin() throws RemoteException;

    boolean notifyEnrollEnd() throws RemoteException;

    boolean pauseEnroll() throws RemoteException;

    byte[] process(IBinder iBinder, String str, byte[] bArr) throws RemoteException;

    byte[] processFIDO(IBinder iBinder, String str, String str2, byte[] bArr) throws RemoteException;

    IBinder registerClient(IFingerprintClient iFingerprintClient, Bundle bundle) throws RemoteException;

    boolean removeAllEnrolledFingers(String str) throws RemoteException;

    boolean removeEnrolledFinger(int i, String str) throws RemoteException;

    int request(int i, int i2) throws RemoteException;

    boolean resumeEnroll() throws RemoteException;

    boolean setIndexName(int i, String str) throws RemoteException;

    boolean setPassword(String str, String str2) throws RemoteException;

    int showIdentifyDialog(IBinder iBinder, ComponentName componentName, String str, boolean z) throws RemoteException;

    boolean unregisterClient(IBinder iBinder) throws RemoteException;

    boolean verifyPassword(IBinder iBinder, String str, String str2) throws RemoteException;

    int verifySensorState(int i, int i2, int i3, int i4, int i5) throws RemoteException;
}
