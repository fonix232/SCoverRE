package com.samsung.android.location;

import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.location.ISLocationCellInterface.Stub;

public class CellGeofenceProvider {
    private static final String TAG = "CellGeofenceProvider";
    private static boolean mEnabled;
    private ISLocationCellInterface mSGeofenceCellInterface = new C02121();

    class C02121 extends Stub {
        C02121() {
        }

        public void addCellGeofence(int i) {
            CellGeofenceProvider.this.native_add_cell_geofence(i);
        }

        public void enableCellGeofence(int i, int i2) {
            CellGeofenceProvider.this.native_enable_cell_geofence(i, i2);
        }

        public void initCellGeofence(int i) {
            CellGeofenceProvider.this.native_init_cell_geofence(i);
        }

        public void removeCellGeofence(int i) {
            CellGeofenceProvider.this.native_remove_cell_geofence(i);
        }

        public void startCollectCell(int i) {
            CellGeofenceProvider.this.native_start_collect_cell(i);
        }

        public void stopCollectCell(int i) {
            CellGeofenceProvider.this.native_stop_collect_cell(i);
        }

        public void syncCellGeofence(int[] iArr, int i, int[] iArr2, int i2) {
            CellGeofenceProvider.this.native_sync_cell_geofence(iArr, i, iArr2, i2);
        }
    }

    static {
        mEnabled = false;
        mEnabled = class_init_native();
    }

    private static native boolean class_init_native();

    private native void native_add_cell_geofence(int i);

    private native void native_cleanup_cell_geofence();

    private native void native_enable_cell_geofence(int i, int i2);

    private native boolean native_init();

    private native void native_init_cell_geofence(int i);

    private native void native_remove_cell_geofence(int i);

    private native void native_start_collect_cell(int i);

    private native void native_stop_collect_cell(int i);

    private native void native_sync_cell_geofence(int[] iArr, int i, int[] iArr2, int i2);

    private void reportCellGeofenceDetected(int i, int i2) {
        ISLocationManager asInterface = ISLocationManager.Stub.asInterface(ServiceManager.getService("sec_location"));
        if (asInterface != null) {
            try {
                asInterface.reportCellGeofenceDetected(i2, i);
            } catch (Throwable e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void reportCellGeofenceRequestFail(int i) {
        ISLocationManager asInterface = ISLocationManager.Stub.asInterface(ServiceManager.getService("sec_location"));
        if (asInterface != null) {
            try {
                asInterface.reportCellGeofenceRequestFail(i);
            } catch (Throwable e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public void disable() {
        Log.d(TAG, "CellGeofenceProvider is disabled");
    }

    public void enable() {
        Log.d(TAG, "CellGeofenceProvider is enabled");
        if (native_init()) {
            ISLocationManager asInterface = ISLocationManager.Stub.asInterface(ServiceManager.getService("sec_location"));
            if (asInterface != null) {
                try {
                    asInterface.setGeofenceCellInterface(getSGeofenceCellInterface());
                    return;
                } catch (Throwable e) {
                    Log.e(TAG, e.toString());
                    return;
                }
            }
            return;
        }
        Log.d(TAG, "CellGeofenceProvider enable is failed....");
    }

    public ISLocationCellInterface getSGeofenceCellInterface() {
        return this.mSGeofenceCellInterface;
    }

    public boolean isEnabled() {
        return mEnabled;
    }
}
