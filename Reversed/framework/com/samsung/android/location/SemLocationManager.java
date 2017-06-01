package com.samsung.android.location;

import android.app.PendingIntent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.samsung.android.location.ISCurrentLocListener.Stub;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SemLocationManager {
    public static final String ACTION_SERVICE_READY = "com.samsung.android.location.SERVICE_READY";
    public static final String CURRENT_LOCATION = "currentlocation";
    public static final String CURRENT_LOCATION_ADDRESS = "currentlocationaddress";
    public static final int ERROR_ALREADY_STARTED = -5;
    public static final int ERROR_EXCEPTION = -4;
    public static final int ERROR_ID_NOT_EXIST = -3;
    public static final int ERROR_ILLEGAL_ARGUMENT = -2;
    public static final int ERROR_LOCATION_CURRENTLY_UNAVAILABLE = -100;
    public static final int ERROR_NOT_INITIALIZED = -1;
    public static final int ERROR_TOO_MANY_GEOFENCE = -6;
    public static final int GEOFENCE_ENTER = 1;
    public static final int GEOFENCE_EXIT = 2;
    public static final String GEOFENCE_LOCATION = "location";
    public static final String GEOFENCE_TRANSITION = "transition";
    public static final int GEOFENCE_TYPE_BT = 3;
    public static final int GEOFENCE_TYPE_EVENT = 4;
    public static final int GEOFENCE_TYPE_GEOPOINT = 1;
    public static final int GEOFENCE_TYPE_WIFI = 2;
    public static final int GEOFENCE_UNKNOWN = 0;
    public static final int OPERATION_SUCCESS = 0;
    public static final String PERMISSION_ALWAYS_SCAN = "permissionalwaysscan";
    private static final String TAG = "SemLocationManager";
    private HashMap<SCurrentLocListener, CurrentLocListenerTransport> mCurrentLocListeners = new HashMap();
    private HashMap<SemLocationListener, LocListenerTransport> mLocListeners = new HashMap();
    private final ISLocationManager mService;

    private class CurrentLocListenerTransport extends Stub {
        public static final int TYPE_CURRENT_LOCATION = 1;
        private SCurrentLocListener mListener;
        private final Handler mListenerHandler = new C02141();

        class C02141 extends Handler {
            C02141() {
            }

            public void handleMessage(Message message) {
                CurrentLocListenerTransport.this._handleMessage(message);
            }
        }

        CurrentLocListenerTransport(SCurrentLocListener sCurrentLocListener) {
            this.mListener = sCurrentLocListener;
        }

        private void _handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    this.mListener.onCurrentLocation((Location) message.obj);
                    return;
                default:
                    return;
            }
        }

        public void onCurrentLocation(Location location) {
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = location;
            this.mListenerHandler.sendMessage(obtain);
        }
    }

    private class LocListenerTransport extends ISLocationListener.Stub {
        public static final int TYPE_LOCATION_AVAILABLE = 1;
        public static final int TYPE_LOCATION_CHANGED_ADDRESS = 2;
        private SemLocationListener mListener;
        private final Handler mListenerHandler = new C02151();

        class C02151 extends Handler {
            C02151() {
            }

            public void handleMessage(Message message) {
                LocListenerTransport.this._handleMessage(message);
            }
        }

        LocListenerTransport(SemLocationListener semLocationListener) {
            this.mListener = semLocationListener;
        }

        private void _handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    this.mListener.onLocationAvailable((Location[]) message.obj);
                    return;
                case 2:
                    this.mListener.onLocationChanged((Location) message.obj, (Address) ((Location) message.obj).getExtras().get(SemLocationManager.CURRENT_LOCATION_ADDRESS));
                    return;
                default:
                    return;
            }
        }

        public void onLocationAvailable(Location[] locationArr) {
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = locationArr;
            this.mListenerHandler.sendMessage(obtain);
        }

        public void onLocationChanged(Location location, Address address) {
            if (location == null) {
                Log.e(SemLocationManager.TAG, "onLocationChanged location is null");
                return;
            }
            Message obtain = Message.obtain();
            obtain.what = 2;
            Bundle bundle = new Bundle();
            bundle.putParcelable(SemLocationManager.CURRENT_LOCATION_ADDRESS, address);
            location.setExtras(bundle);
            obtain.obj = location;
            this.mListenerHandler.sendMessage(obtain);
        }
    }

    public SemLocationManager(ISLocationManager iSLocationManager) {
        this.mService = iSLocationManager;
    }

    private boolean isArgumentsValid(SemGeofence semGeofence) {
        int type = semGeofence.getType();
        if (type == 1 || type == 2 || type == 3 || type == 4) {
            if (type == 1) {
                double latitude = semGeofence.getLatitude();
                double longitude = semGeofence.getLongitude();
                int radius = semGeofence.getRadius();
                if (latitude < -90.0d || latitude > 90.0d) {
                    Log.e(TAG, "latitude is not correct");
                    return false;
                } else if (longitude < -180.0d || longitude > 180.0d) {
                    Log.e(TAG, "longitude is not correct");
                    return false;
                } else if (radius < 100) {
                    Log.e(TAG, "radius is not correct");
                    return false;
                }
            }
            if ((type != 2 && type != 3) || semGeofence.getBssid() != null) {
                return true;
            }
            Log.e(TAG, "bssid is null");
            return false;
        }
        Log.e(TAG, "geofenceType is not correct");
        return false;
    }

    public int addGeofence(SemGeofence semGeofence) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (!isArgumentsValid(semGeofence)) {
            return -2;
        } else {
            try {
                return this.mService.addGeofence(semGeofence, null);
            } catch (Throwable e) {
                Log.e(TAG, "getGeofenceId : RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int addGeofence(SemGeofence semGeofence, String str) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (!isArgumentsValid(semGeofence)) {
            return -2;
        } else {
            try {
                return this.mService.addGeofence(semGeofence, str);
            } catch (Throwable e) {
                Log.e(TAG, "getGeofenceId : RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public boolean checkPassiveLocation() {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return false;
        }
        try {
            Log.e(TAG, "checkPassiveLocation");
            return this.mService.checkPassiveLocation();
        } catch (Throwable e) {
            Log.e(TAG, "checkPassiveLocation: RemoteException " + e.toString());
            return false;
        }
    }

    public List<Integer> getGeofenceIdList(String str) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return null;
        }
        List<Integer> arrayList = new ArrayList();
        try {
            int[] geofenceIdList = this.mService.getGeofenceIdList(str);
            if (geofenceIdList != null) {
                for (int valueOf : geofenceIdList) {
                    arrayList.add(Integer.valueOf(valueOf));
                }
            }
            return arrayList;
        } catch (Throwable e) {
            Log.e(TAG, "getGeofenceIdList: RemoteException " + e.toString());
            return null;
        }
    }

    public int removeCurrentLocation(int i, SCurrentLocListener sCurrentLocListener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (sCurrentLocListener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                synchronized (this.mCurrentLocListeners) {
                    if (((CurrentLocListenerTransport) this.mCurrentLocListeners.remove(sCurrentLocListener)) == null) {
                        Log.e(TAG, "already removeCurrentLocation");
                        return -4;
                    }
                    Log.e(TAG, "removeCurrentLocation : " + i);
                    int removeCurrentLocation = this.mService.removeCurrentLocation(i);
                    return removeCurrentLocation;
                }
            } catch (Throwable e) {
                Log.e(TAG, "removeCurrentLocation: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int removeGeofence(int i) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        }
        try {
            return this.mService.removeGeofence(i, null);
        } catch (Throwable e) {
            Log.e(TAG, "removeGeofence: RemoteException " + e.toString());
            return -4;
        }
    }

    public int removeGeofence(int i, String str) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        }
        try {
            return this.mService.removeGeofence(i, str);
        } catch (Throwable e) {
            Log.e(TAG, "removeGeofence: RemoteException " + e.toString());
            return -4;
        }
    }

    public int removeLocationUpdates(SemLocationListener semLocationListener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (semLocationListener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                LocListenerTransport locListenerTransport = (LocListenerTransport) this.mLocListeners.remove(semLocationListener);
                if (locListenerTransport != null) {
                    return this.mService.removeLocation(locListenerTransport);
                }
                Log.e(TAG, "Already stopped location");
                return -3;
            } catch (Throwable e) {
                Log.e(TAG, "removeLocationUpdates: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int removeSingleLocation(PendingIntent pendingIntent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (pendingIntent == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.removeSingleLocation(pendingIntent);
            } catch (Throwable e) {
                Log.e(TAG, "removeSingleLocation: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int requestAddressFromLocation(double[] dArr, double[] dArr2, PendingIntent pendingIntent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (pendingIntent == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.requestLocationToPoi(dArr, dArr2, pendingIntent);
            } catch (Throwable e) {
                Log.e(TAG, "requestLocationToPoi: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int requestBatchOfLocations() {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        }
        try {
            Log.e(TAG, "requestBatchOfLocations ");
            return this.mService.requestBatchOfLocations();
        } catch (Throwable e) {
            Log.e(TAG, "requestBatchOfLocations: RemoteException " + e.toString());
            return -4;
        }
    }

    public int requestCurrentLocation(SCurrentLocListener sCurrentLocListener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (sCurrentLocListener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                int requestCurrentLocation;
                synchronized (this.mCurrentLocListeners) {
                    ISCurrentLocListener iSCurrentLocListener = (CurrentLocListenerTransport) this.mCurrentLocListeners.get(sCurrentLocListener);
                    if (iSCurrentLocListener == null) {
                        iSCurrentLocListener = new CurrentLocListenerTransport(sCurrentLocListener);
                    }
                    this.mCurrentLocListeners.put(sCurrentLocListener, iSCurrentLocListener);
                    Log.e(TAG, "requestCurrentLocation ");
                    requestCurrentLocation = this.mService.requestCurrentLocation(iSCurrentLocListener);
                }
                return requestCurrentLocation;
            } catch (Throwable e) {
                Log.e(TAG, "requestCurrentLocation: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int requestLocationUpdates(boolean z, SemLocationListener semLocationListener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (semLocationListener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                int requestLocation;
                synchronized (this.mLocListeners) {
                    ISLocationListener iSLocationListener = (LocListenerTransport) this.mLocListeners.get(semLocationListener);
                    if (iSLocationListener == null) {
                        iSLocationListener = new LocListenerTransport(semLocationListener);
                    }
                    this.mLocListeners.put(semLocationListener, iSLocationListener);
                    requestLocation = this.mService.requestLocation(z, iSLocationListener);
                }
                return requestLocation;
            } catch (Throwable e) {
                Log.e(TAG, "requestLocationUpdates: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int requestSingleLocation(int i, int i2, boolean z, PendingIntent pendingIntent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (pendingIntent == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.requestSingleLocation(i, i2, z, pendingIntent);
            } catch (Throwable e) {
                Log.e(TAG, "requestSingleLocation: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int startGeofenceMonitoring(int i, PendingIntent pendingIntent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (pendingIntent == null) {
            Log.e(TAG, "intent is null");
            return -2;
        } else {
            try {
                return this.mService.startGeofence(i, pendingIntent);
            } catch (Throwable e) {
                Log.e(TAG, "startGeofenceMonitoring : RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int startLearning(int i) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        }
        try {
            return this.mService.startLearning(i);
        } catch (Throwable e) {
            Log.e(TAG, "startLearning: RemoteException " + e.toString());
            return -4;
        }
    }

    public int startLocationBatching(int i, SemLocationListener semLocationListener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (semLocationListener == null || i <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                int startLocationBatching;
                synchronized (this.mLocListeners) {
                    ISLocationListener iSLocationListener = (LocListenerTransport) this.mLocListeners.get(semLocationListener);
                    if (iSLocationListener == null) {
                        iSLocationListener = new LocListenerTransport(semLocationListener);
                    }
                    this.mLocListeners.put(semLocationListener, iSLocationListener);
                    startLocationBatching = this.mService.startLocationBatching(i, iSLocationListener);
                }
                return startLocationBatching;
            } catch (Throwable e) {
                Log.e(TAG, "startBatching: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int stopGeofenceMonitoring(int i, PendingIntent pendingIntent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (pendingIntent == null) {
            Log.e(TAG, "intent is null");
            return -2;
        } else {
            try {
                return this.mService.stopGeofence(i, pendingIntent);
            } catch (Throwable e) {
                Log.e(TAG, "stopGeofenceMonitoring: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int stopLearning(int i) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        }
        try {
            return this.mService.stopLearning(i);
        } catch (Throwable e) {
            Log.e(TAG, "stopLearning: RemoteException " + e.toString());
            return -4;
        }
    }

    public int stopLocationBatching(int i, SemLocationListener semLocationListener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (semLocationListener == null || i <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                if (((LocListenerTransport) this.mLocListeners.remove(semLocationListener)) != null) {
                    return this.mService.stopLocationBatching(i);
                }
                Log.e(TAG, "Already stopped geofence");
                return -3;
            } catch (Throwable e) {
                Log.e(TAG, "stopBatching: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int syncGeofence(List<Integer> list) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (list == null) {
            Log.e(TAG, "geofenceIdList is null");
            return -2;
        } else {
            int[] iArr = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                iArr[i] = ((Integer) list.get(i)).intValue();
            }
            try {
                return this.mService.syncGeofence(iArr, null);
            } catch (Throwable e) {
                Log.e(TAG, "syncGeofence: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int syncGeofence(List<Integer> list, String str) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (list == null) {
            Log.e(TAG, "geofenceIdList is null");
            return -2;
        } else {
            int[] iArr = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                iArr[i] = ((Integer) list.get(i)).intValue();
            }
            try {
                return this.mService.syncGeofence(iArr, str);
            } catch (Throwable e) {
                Log.e(TAG, "syncGeofence: RemoteException " + e.toString());
                return -4;
            }
        }
    }

    public int updateBatchingOptions(int i, int i2) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (i <= 0 || i2 <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.updateBatchingOptions(i, i2);
            } catch (Throwable e) {
                Log.e(TAG, "updateBatchingOptions: RemoteException " + e.toString());
                return -4;
            }
        }
    }
}
