package com.samsung.android.location;

import android.app.PendingIntent;
import android.hardware.location.IFusedLocationHardware;
import android.location.FusedBatchOptions;
import android.location.IGpsGeofenceHardware;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISLocationManager extends IInterface {

    public static abstract class Stub extends Binder implements ISLocationManager {
        private static final String DESCRIPTOR = "com.samsung.android.location.ISLocationManager";
        static final int TRANSACTION_addGeofence = 6;
        static final int TRANSACTION_checkPassiveLocation = 23;
        static final int TRANSACTION_cleanup = 39;
        static final int TRANSACTION_flushCoreBatchedLocations = 38;
        static final int TRANSACTION_getGeofenceIdList = 4;
        static final int TRANSACTION_injectDeviceContext = 40;
        static final int TRANSACTION_isCoreBatchingSupported = 36;
        static final int TRANSACTION_removeCurrentLocation = 22;
        static final int TRANSACTION_removeGeofence = 9;
        static final int TRANSACTION_removeLocation = 19;
        static final int TRANSACTION_removeSingleLocation = 20;
        static final int TRANSACTION_reportCellGeofenceDetected = 30;
        static final int TRANSACTION_reportCellGeofenceRequestFail = 31;
        static final int TRANSACTION_reportFlpHardwareLocation = 32;
        static final int TRANSACTION_reportGpsGeofenceAddStatus = 26;
        static final int TRANSACTION_reportGpsGeofencePauseStatus = 28;
        static final int TRANSACTION_reportGpsGeofenceRemoveStatus = 27;
        static final int TRANSACTION_reportGpsGeofenceResumeStatus = 29;
        static final int TRANSACTION_reportGpsGeofenceStatus = 25;
        static final int TRANSACTION_reportGpsGeofenceTransition = 24;
        static final int TRANSACTION_requestBatchOfLocations = 14;
        static final int TRANSACTION_requestCoreBatchOfLocations = 37;
        static final int TRANSACTION_requestCurrentLocation = 21;
        static final int TRANSACTION_requestLocation = 17;
        static final int TRANSACTION_requestLocationToPoi = 18;
        static final int TRANSACTION_requestSingleLocation = 16;
        static final int TRANSACTION_setFusedLocationHardware = 1;
        static final int TRANSACTION_setGeofenceCellInterface = 3;
        static final int TRANSACTION_setGpsGeofenceHardware = 2;
        static final int TRANSACTION_startCoreBatching = 33;
        static final int TRANSACTION_startGeofence = 7;
        static final int TRANSACTION_startLearning = 10;
        static final int TRANSACTION_startLocationBatching = 12;
        static final int TRANSACTION_stopCoreBatching = 34;
        static final int TRANSACTION_stopGeofence = 8;
        static final int TRANSACTION_stopLearning = 11;
        static final int TRANSACTION_stopLocationBatching = 13;
        static final int TRANSACTION_syncGeofence = 5;
        static final int TRANSACTION_updateBatchingOptions = 15;
        static final int TRANSACTION_updateCoreBatchingOptions = 35;

        private static class Proxy implements ISLocationManager {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public int addGeofence(SemGeofence semGeofence, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (semGeofence != null) {
                        obtain.writeInt(1);
                        semGeofence.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean checkPassiveLocation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, obtain, obtain2, 0);
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

            public void cleanup() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void flushCoreBatchedLocations() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getGeofenceIdList(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void injectDeviceContext(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(40, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isCoreBatchingSupported() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, obtain, obtain2, 0);
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

            public int removeCurrentLocation(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int removeGeofence(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int removeLocation(ISLocationListener iSLocationListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSLocationListener != null) {
                        iBinder = iSLocationListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int removeSingleLocation(PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportCellGeofenceDetected(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportCellGeofenceRequestFail(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportFlpHardwareLocation(Location[] locationArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedArray(locationArr, 0);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportGpsGeofenceAddStatus(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportGpsGeofencePauseStatus(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportGpsGeofenceRemoveStatus(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportGpsGeofenceResumeStatus(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportGpsGeofenceStatus(int i, int i2, double d, double d2, double d3, float f, float f2, float f3, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeDouble(d);
                    obtain.writeDouble(d2);
                    obtain.writeDouble(d3);
                    obtain.writeFloat(f);
                    obtain.writeFloat(f2);
                    obtain.writeFloat(f3);
                    obtain.writeLong(j);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportGpsGeofenceTransition(int i, int i2, double d, double d2, double d3, float f, float f2, float f3, long j, int i3, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeDouble(d);
                    obtain.writeDouble(d2);
                    obtain.writeDouble(d3);
                    obtain.writeFloat(f);
                    obtain.writeFloat(f2);
                    obtain.writeFloat(f3);
                    obtain.writeLong(j);
                    obtain.writeInt(i3);
                    obtain.writeLong(j2);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestBatchOfLocations() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestCoreBatchOfLocations(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(37, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestCurrentLocation(ISCurrentLocListener iSCurrentLocListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSCurrentLocListener != null) {
                        iBinder = iSCurrentLocListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestLocation(boolean z, ISLocationListener iSLocationListener) throws RemoteException {
                IBinder iBinder = null;
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    if (iSLocationListener != null) {
                        iBinder = iSLocationListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestLocationToPoi(double[] dArr, double[] dArr2, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeDoubleArray(dArr);
                    obtain.writeDoubleArray(dArr2);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int requestSingleLocation(int i, int i2, boolean z, PendingIntent pendingIntent) throws RemoteException {
                int i3 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!z) {
                        i3 = 0;
                    }
                    obtain.writeInt(i3);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setFusedLocationHardware(IFusedLocationHardware iFusedLocationHardware) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iFusedLocationHardware != null) {
                        iBinder = iFusedLocationHardware.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setGeofenceCellInterface(ISLocationCellInterface iSLocationCellInterface) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iSLocationCellInterface != null) {
                        iBinder = iSLocationCellInterface.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setGpsGeofenceHardware(IGpsGeofenceHardware iGpsGeofenceHardware) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (iGpsGeofenceHardware != null) {
                        iBinder = iGpsGeofenceHardware.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startCoreBatching(int i, FusedBatchOptions fusedBatchOptions, ISLocationListener iSLocationListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (fusedBatchOptions != null) {
                        obtain.writeInt(1);
                        fusedBatchOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (iSLocationListener != null) {
                        iBinder = iSLocationListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(33, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startGeofence(int i, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startLearning(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startLocationBatching(int i, ISLocationListener iSLocationListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (iSLocationListener != null) {
                        iBinder = iSLocationListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopCoreBatching(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(34, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int stopGeofence(int i, PendingIntent pendingIntent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int stopLearning(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int stopLocationBatching(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int syncGeofence(int[] iArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeIntArray(iArr);
                    obtain.writeString(str);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int updateBatchingOptions(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateCoreBatchingOptions(int i, FusedBatchOptions fusedBatchOptions, ISLocationListener iSLocationListener) throws RemoteException {
                IBinder iBinder = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (fusedBatchOptions != null) {
                        obtain.writeInt(1);
                        fusedBatchOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (iSLocationListener != null) {
                        iBinder = iSLocationListener.asBinder();
                    }
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(35, obtain, obtain2, 0);
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

        public static ISLocationManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISLocationManager)) ? new Proxy(iBinder) : (ISLocationManager) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int syncGeofence;
            boolean checkPassiveLocation;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    setFusedLocationHardware(android.hardware.location.IFusedLocationHardware.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    setGpsGeofenceHardware(android.location.IGpsGeofenceHardware.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    setGeofenceCellInterface(com.samsung.android.location.ISLocationCellInterface.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    int[] geofenceIdList = getGeofenceIdList(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeIntArray(geofenceIdList);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = syncGeofence(parcel.createIntArray(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = addGeofence(parcel.readInt() != 0 ? (SemGeofence) SemGeofence.CREATOR.createFromParcel(parcel) : null, parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = startGeofence(parcel.readInt(), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = stopGeofence(parcel.readInt(), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = removeGeofence(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = startLearning(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = stopLearning(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = startLocationBatching(parcel.readInt(), com.samsung.android.location.ISLocationListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = stopLocationBatching(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = requestBatchOfLocations();
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = updateBatchingOptions(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = requestSingleLocation(parcel.readInt(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = requestLocation(parcel.readInt() != 0, com.samsung.android.location.ISLocationListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = requestLocationToPoi(parcel.createDoubleArray(), parcel.createDoubleArray(), parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = removeLocation(com.samsung.android.location.ISLocationListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = removeSingleLocation(parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = requestCurrentLocation(com.samsung.android.location.ISCurrentLocListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    syncGeofence = removeCurrentLocation(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(syncGeofence);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    checkPassiveLocation = checkPassiveLocation();
                    parcel2.writeNoException();
                    parcel2.writeInt(checkPassiveLocation ? 1 : 0);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceTransition(parcel.readInt(), parcel.readInt(), parcel.readDouble(), parcel.readDouble(), parcel.readDouble(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readLong(), parcel.readInt(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceStatus(parcel.readInt(), parcel.readInt(), parcel.readDouble(), parcel.readDouble(), parcel.readDouble(), parcel.readFloat(), parcel.readFloat(), parcel.readFloat(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceAddStatus(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceRemoveStatus(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportGpsGeofencePauseStatus(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceResumeStatus(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportCellGeofenceDetected(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportCellGeofenceRequestFail(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    reportFlpHardwareLocation((Location[]) parcel.createTypedArray(Location.CREATOR));
                    parcel2.writeNoException();
                    return true;
                case 33:
                    parcel.enforceInterface(DESCRIPTOR);
                    startCoreBatching(parcel.readInt(), parcel.readInt() != 0 ? (FusedBatchOptions) FusedBatchOptions.CREATOR.createFromParcel(parcel) : null, com.samsung.android.location.ISLocationListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 34:
                    parcel.enforceInterface(DESCRIPTOR);
                    stopCoreBatching(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 35:
                    parcel.enforceInterface(DESCRIPTOR);
                    updateCoreBatchingOptions(parcel.readInt(), parcel.readInt() != 0 ? (FusedBatchOptions) FusedBatchOptions.CREATOR.createFromParcel(parcel) : null, com.samsung.android.location.ISLocationListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 36:
                    parcel.enforceInterface(DESCRIPTOR);
                    checkPassiveLocation = isCoreBatchingSupported();
                    parcel2.writeNoException();
                    parcel2.writeInt(checkPassiveLocation ? 1 : 0);
                    return true;
                case 37:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestCoreBatchOfLocations(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 38:
                    parcel.enforceInterface(DESCRIPTOR);
                    flushCoreBatchedLocations();
                    parcel2.writeNoException();
                    return true;
                case 39:
                    parcel.enforceInterface(DESCRIPTOR);
                    cleanup();
                    parcel2.writeNoException();
                    return true;
                case 40:
                    parcel.enforceInterface(DESCRIPTOR);
                    injectDeviceContext(parcel.readInt());
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

    int addGeofence(SemGeofence semGeofence, String str) throws RemoteException;

    boolean checkPassiveLocation() throws RemoteException;

    void cleanup() throws RemoteException;

    void flushCoreBatchedLocations() throws RemoteException;

    int[] getGeofenceIdList(String str) throws RemoteException;

    void injectDeviceContext(int i) throws RemoteException;

    boolean isCoreBatchingSupported() throws RemoteException;

    int removeCurrentLocation(int i) throws RemoteException;

    int removeGeofence(int i, String str) throws RemoteException;

    int removeLocation(ISLocationListener iSLocationListener) throws RemoteException;

    int removeSingleLocation(PendingIntent pendingIntent) throws RemoteException;

    void reportCellGeofenceDetected(int i, int i2) throws RemoteException;

    void reportCellGeofenceRequestFail(int i) throws RemoteException;

    void reportFlpHardwareLocation(Location[] locationArr) throws RemoteException;

    void reportGpsGeofenceAddStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofencePauseStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofenceRemoveStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofenceResumeStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofenceStatus(int i, int i2, double d, double d2, double d3, float f, float f2, float f3, long j) throws RemoteException;

    void reportGpsGeofenceTransition(int i, int i2, double d, double d2, double d3, float f, float f2, float f3, long j, int i3, long j2) throws RemoteException;

    int requestBatchOfLocations() throws RemoteException;

    void requestCoreBatchOfLocations(int i) throws RemoteException;

    int requestCurrentLocation(ISCurrentLocListener iSCurrentLocListener) throws RemoteException;

    int requestLocation(boolean z, ISLocationListener iSLocationListener) throws RemoteException;

    int requestLocationToPoi(double[] dArr, double[] dArr2, PendingIntent pendingIntent) throws RemoteException;

    int requestSingleLocation(int i, int i2, boolean z, PendingIntent pendingIntent) throws RemoteException;

    void setFusedLocationHardware(IFusedLocationHardware iFusedLocationHardware) throws RemoteException;

    void setGeofenceCellInterface(ISLocationCellInterface iSLocationCellInterface) throws RemoteException;

    void setGpsGeofenceHardware(IGpsGeofenceHardware iGpsGeofenceHardware) throws RemoteException;

    void startCoreBatching(int i, FusedBatchOptions fusedBatchOptions, ISLocationListener iSLocationListener) throws RemoteException;

    int startGeofence(int i, PendingIntent pendingIntent) throws RemoteException;

    int startLearning(int i) throws RemoteException;

    int startLocationBatching(int i, ISLocationListener iSLocationListener) throws RemoteException;

    void stopCoreBatching(int i) throws RemoteException;

    int stopGeofence(int i, PendingIntent pendingIntent) throws RemoteException;

    int stopLearning(int i) throws RemoteException;

    int stopLocationBatching(int i) throws RemoteException;

    int syncGeofence(int[] iArr, String str) throws RemoteException;

    int updateBatchingOptions(int i, int i2) throws RemoteException;

    void updateCoreBatchingOptions(int i, FusedBatchOptions fusedBatchOptions, ISLocationListener iSLocationListener) throws RemoteException;
}
