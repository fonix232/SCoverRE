package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.manager.ICurrrentPositionObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.location.SemLocationManager;

public class CaGpsPositionManager implements ICurrentPositionObservable, IUtilManager {
    private Handler mDisabler = null;
    private boolean mEnable = false;
    private LocationManager mGpsManager;
    private long mGpsTime = 1000;
    private final LocationListener mLocationListener = new C00261();
    private final Looper mLooper;
    private ICurrrentPositionObserver mObserver;
    private double preLatitude = 0.0d;
    private double preLongitude = 0.0d;

    class C00261 implements LocationListener {
        C00261() {
        }

        public final void onLocationChanged(Location location) {
            CaGpsPositionManager.this.notifyListener(new Location(location));
            CaGpsPositionManager.this.preLatitude = location.getLatitude();
            CaGpsPositionManager.this.preLongitude = location.getLongitude();
        }

        public final void onProviderDisabled(String str) {
            CaLogger.info("Location service is disabled");
        }

        public final void onProviderEnabled(String str) {
            CaLogger.info("Location service is enabled");
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
            switch (i) {
                case 0:
                    CaLogger.info("out of service");
                    return;
                case 1:
                    CaLogger.info("temporarily unavailable");
                    return;
                case 2:
                    CaLogger.info("available");
                    return;
                default:
                    return;
            }
        }
    }

    class C00272 implements Runnable {
        C00272() {
        }

        public void run() {
            CaGpsPositionManager.this.mGpsManager.removeUpdates(CaGpsPositionManager.this.mLocationListener);
        }
    }

    class C00283 implements Runnable {
        C00283() {
        }

        public void run() {
            CaGpsPositionManager.this.mGpsManager.requestLocationUpdates("gps", 1000, 0.0f, CaGpsPositionManager.this.mLocationListener, CaGpsPositionManager.this.mLooper);
        }
    }

    class C00294 implements Runnable {
        C00294() {
        }

        public void run() {
            CaGpsPositionManager.this.disable();
        }
    }

    class C00305 implements Runnable {
        C00305() {
        }

        public void run() {
            CaGpsPositionManager.this.mGpsManager.removeUpdates(CaGpsPositionManager.this.mLocationListener);
        }
    }

    public CaGpsPositionManager(Context context, Looper looper, ICurrrentPositionObserver iCurrrentPositionObserver) {
        this.mLooper = looper;
        initializeManager(context);
        registerCurrentPositionObserver(iCurrrentPositionObserver);
    }

    private void notifyListener(Location location) {
        if (this.mObserver != null) {
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            BaseBundle extras = location.getExtras();
            int i = 0;
            if (extras != null) {
                i = extras.getInt("satellites");
            }
            int[] iArr = utcTime;
            this.mObserver.updateCurrentPosition(1, iArr, location.getLatitude(), location.getLongitude(), location.getAltitude(), PositionContextBean.calculationDistance(this.preLatitude, this.preLongitude, location.getLatitude(), location.getLongitude()), location.getSpeed(), location.getAccuracy(), i);
        }
    }

    public void disable() {
        if (this.mGpsManager == null) {
            CaLogger.error("cannot unregister the gps listener");
        } else if (this.mEnable) {
            CaLogger.trace();
            new Handler(this.mLooper).postDelayed(new C00305(), 0);
            this.mEnable = false;
            if (this.mDisabler != null) {
                this.mDisabler.removeCallbacksAndMessages(null);
            }
        }
    }

    public final void enable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mLooper == null) {
            CaLogger.error("Looper is null");
        } else {
            if (this.mEnable) {
                CaLogger.warning("mEnable value is true.");
                disable();
            }
            this.mEnable = true;
            if (this.mDisabler != null) {
                this.mDisabler.removeCallbacksAndMessages(null);
            }
            new Handler(this.mLooper).postDelayed(new C00283(), 0);
            this.mDisabler = new Handler(this.mLooper);
            this.mDisabler.postDelayed(new C00294(), this.mGpsTime);
        }
    }

    public final void initializeManager(Context context) {
        this.mGpsManager = (LocationManager) context.getSystemService(SemLocationManager.GEOFENCE_LOCATION);
        if (this.mGpsManager == null) {
            CaLogger.error("cannot create the GpsManager object");
        }
        this.mEnable = false;
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public void notifyCurrentPositionObserver() {
    }

    public void registerCurrentPositionObserver(ICurrrentPositionObserver iCurrrentPositionObserver) {
        this.mObserver = iCurrrentPositionObserver;
    }

    public void setGpsUpdateTime(long j) {
        this.mGpsTime = j;
    }

    public final void terminateManager() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            new Handler(this.mLooper).postDelayed(new C00272(), 0);
        }
    }

    public void unregisterCurrentPositionObserver() {
        this.mObserver = null;
    }
}
