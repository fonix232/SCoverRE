package com.samsung.android.contextaware.utilbundle;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
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
import java.util.Iterator;

public class CaCurrentPositionManager implements ICurrentPositionObservable, IUtilManager, ITimeOutCheckObserver {
    private static final float ACCURACY_BEST_THRESHOLE = 16.0f;
    private static final float ACCURACY_GOOD_THRESHOLE = 50.0f;
    private static final float GPS_MIN_DISTANCE = 0.0f;
    private static final long GPS_MIN_TIME = 1000;
    private static final int LOCATION_MODE_LOCATIONMANAGER = 1;
    private static final int LOCATION_MODE_SLOCATION = 2;
    private static final int LOCFROMLOCATIONMANAGER = 1;
    private static final int LOCFROMSLOCATION = 2;
    public static Context mContext;
    private int ACCURACY_CurrentLoc = 150;
    private final String CURLOC = "com.samsung.android.contextaware.SLOCATION";
    private int LocRequestSource = 0;
    private IntentFilter filter;
    private BroadcastReceiver mBrReceiver = null;
    private boolean mEnable = false;
    private PositionContextBean mGpsInfo;
    private final LocationListener mGpsListener = new C00201();
    private LocationManager mGpsManager;
    private ICurrrentPositionObserver mListener;
    private final Looper mLooper;
    private PositionContextBean mPrePosition;
    private CaTimeOutCheckManager mTimeOutCheck;
    private Thread mTimeOutCheckThreadHandler;
    private PositionContextBean mWpsInfo;
    private final LocationListener mWpsListener = new C00212();
    private SemLocationManager sLm;

    class C00201 implements LocationListener {
        C00201() {
        }

        public final void onLocationChanged(Location location) {
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float accuracy = location.getAccuracy();
            double calculationDistance = PositionContextBean.calculationDistance(CaCurrentPositionManager.this.mPrePosition.getLatitude(), CaCurrentPositionManager.this.mPrePosition.getLongitude(), latitude, longitude);
            Iterator -wrap0 = CaCurrentPositionManager.this.getGpsSatellites();
            int i = 0;
            if (-wrap0 != null) {
                while (-wrap0.hasNext()) {
                    GpsSatellite gpsSatellite = (GpsSatellite) -wrap0.next();
                    if (gpsSatellite != null && gpsSatellite.usedInFix()) {
                        i++;
                    }
                }
            }
            if (CaCurrentPositionManager.this.mGpsInfo.getAccuracy() >= accuracy) {
                CaCurrentPositionManager.this.mGpsInfo.setPosition(1, utcTime, latitude, longitude, altitude, calculationDistance, speed, accuracy, i);
            }
            if (accuracy <= CaCurrentPositionManager.ACCURACY_BEST_THRESHOLE) {
                CaCurrentPositionManager.this.notifyCurrentPositionObserver();
            }
        }

        public final void onProviderDisabled(String str) {
            CaLogger.info(str + " is disabled");
        }

        public final void onProviderEnabled(String str) {
            CaLogger.info(str + " is enabled");
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    class C00212 implements LocationListener {
        C00212() {
        }

        public final void onLocationChanged(Location location) {
            if (location.getAccuracy() <= CaCurrentPositionManager.GPS_MIN_DISTANCE) {
                CaLogger.warning("Accuracy is low");
                return;
            }
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            CaCurrentPositionManager.this.mWpsInfo.setPosition(2, utcTime, latitude, longitude, location.getAltitude(), PositionContextBean.calculationDistance(CaCurrentPositionManager.this.mPrePosition.getLatitude(), CaCurrentPositionManager.this.mPrePosition.getLongitude(), latitude, longitude), CaCurrentPositionManager.GPS_MIN_DISTANCE, CaCurrentPositionManager.GPS_MIN_DISTANCE, 0);
        }

        public final void onProviderDisabled(String str) {
            CaLogger.info(str + " is disabled");
        }

        public final void onProviderEnabled(String str) {
            CaLogger.info(str + " is enabled");
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    class C00223 extends BroadcastReceiver {
        C00223() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.samsung.android.contextaware.SLOCATION")) {
                BaseBundle extras = intent.getExtras();
                if (extras != null && extras.containsKey(SemLocationManager.CURRENT_LOCATION)) {
                    Location location = (Location) extras.get(SemLocationManager.CURRENT_LOCATION);
                    if (location != null) {
                        CaLogger.debug("RSL is OK.");
                        CaCurrentPositionManager.this.CurrentLocUpdate(new Location(location));
                        return;
                    }
                    CaLogger.debug("Loc is null");
                }
            }
        }
    }

    class C00234 implements Runnable {
        C00234() {
        }

        public void run() {
            CaCurrentPositionManager.this.registerGpsListener();
            CaCurrentPositionManager.this.registerWpsListener();
            CaCurrentPositionManager caCurrentPositionManager = CaCurrentPositionManager.this;
            caCurrentPositionManager.LocRequestSource = caCurrentPositionManager.LocRequestSource | 1;
        }
    }

    class C00245 implements Runnable {
        C00245() {
        }

        public void run() {
            CaCurrentPositionManager.this.requestCurrentLoc(10);
        }
    }

    class C00256 implements Runnable {
        C00256() {
        }

        public void run() {
            if ((CaCurrentPositionManager.this.LocRequestSource & 1) > 0) {
                CaCurrentPositionManager.this.unregisterGpsListener();
                CaCurrentPositionManager.this.unregisterWpsListener();
            }
            if ((CaCurrentPositionManager.this.LocRequestSource & 2) > 0) {
                CaCurrentPositionManager.this.removeCurrentLoc();
                if (CaCurrentPositionManager.this.mBrReceiver != null) {
                    CaCurrentPositionManager.mContext.unregisterReceiver(CaCurrentPositionManager.this.mBrReceiver);
                }
            }
            CaCurrentPositionManager.this.LocRequestSource = 0;
        }
    }

    public CaCurrentPositionManager(Context context, Looper looper, ICurrrentPositionObserver iCurrrentPositionObserver) {
        this.mLooper = looper;
        initializeManager(context);
        registerCurrentPositionObserver(iCurrrentPositionObserver);
        mContext = context;
    }

    private void clearTimeOutCheckService() {
        if (this.mTimeOutCheckThreadHandler != null) {
            this.mTimeOutCheckThreadHandler.interrupt();
            this.mTimeOutCheck = null;
            this.mTimeOutCheckThreadHandler = null;
        }
    }

    private Iterator<GpsSatellite> getGpsSatellites() {
        return this.mGpsManager == null ? null : this.mGpsManager.getGpsStatus(null).getSatellites().iterator();
    }

    private void registerGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.requestLocationUpdates("gps", GPS_MIN_TIME, GPS_MIN_DISTANCE, this.mGpsListener, this.mLooper);
        }
    }

    private void registerWpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
            return;
        }
        if (this.mGpsManager.isProviderEnabled("network")) {
            this.mGpsManager.requestLocationUpdates("network", GPS_MIN_TIME, GPS_MIN_DISTANCE, this.mWpsListener, this.mLooper);
        }
    }

    private void unregisterGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mGpsListener);
        }
    }

    private void unregisterWpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mWpsListener);
        }
    }

    private void updateEmptyPosition() {
        if (this.mListener != null) {
            PositionContextBean positionContextBean = new PositionContextBean();
            this.mListener.updateCurrentPosition(positionContextBean.getType(), positionContextBean.getUtcTime(), positionContextBean.getLatitude(), positionContextBean.getLongitude(), positionContextBean.getAltitude(), positionContextBean.getDistance(), positionContextBean.getSpeed(), positionContextBean.getAccuracy(), positionContextBean.getSatelliteCount());
        }
    }

    private void updateGpsPosition() {
        if (this.mListener != null) {
            this.mPrePosition = this.mGpsInfo;
            this.mListener.updateCurrentPosition(this.mGpsInfo.getType(), this.mGpsInfo.getUtcTime(), this.mGpsInfo.getLatitude(), this.mGpsInfo.getLongitude(), this.mGpsInfo.getAltitude(), this.mGpsInfo.getDistance(), this.mGpsInfo.getSpeed(), this.mGpsInfo.getAccuracy(), this.mGpsInfo.getSatelliteCount());
        }
    }

    private void updateWpsPosition() {
        if (this.mListener != null) {
            this.mPrePosition = this.mWpsInfo;
            this.mListener.updateCurrentPosition(this.mWpsInfo.getType(), this.mWpsInfo.getUtcTime(), this.mWpsInfo.getLatitude(), this.mWpsInfo.getLongitude(), this.mWpsInfo.getAltitude(), this.mWpsInfo.getDistance(), this.mWpsInfo.getSpeed(), this.mWpsInfo.getAccuracy(), this.mWpsInfo.getSatelliteCount());
        }
    }

    protected void CurrentLocUpdate(Location location) {
        if (this.mEnable) {
            CaLogger.debug("CurrentLocUpdate : provider " + location.getProvider());
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float accuracy = location.getAccuracy();
            this.mWpsInfo.setPosition(4, utcTime, latitude, longitude, altitude, PositionContextBean.calculationDistance(this.mPrePosition.getLatitude(), this.mPrePosition.getLongitude(), latitude, longitude), speed, accuracy, 0);
            CaLogger.debug("CurrentLUpda : SLO update! ");
            notifyCurrentPositionObserver();
        }
    }

    public final void disable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mEnable) {
            CaLogger.trace();
            clearTimeOutCheckService();
            new Handler(this.mLooper).postDelayed(new C00256(), 0);
            this.mEnable = false;
        }
    }

    public final void enable(int i) {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mLooper == null) {
            CaLogger.error("Looper is null");
        } else {
            if (this.mEnable) {
                CaLogger.warning("mEnable value is true.");
                disable();
            }
            CaLogger.trace();
            this.mEnable = true;
            this.mGpsInfo = new PositionContextBean(10000.0f);
            this.mWpsInfo = new PositionContextBean(10000.0f);
            this.mPrePosition = new PositionContextBean();
            clearTimeOutCheckService();
            this.mTimeOutCheck = new CaTimeOutCheckManager(this, i);
            this.mTimeOutCheckThreadHandler = new Thread(this.mTimeOutCheck);
            this.mTimeOutCheckThreadHandler.start();
            new Handler(this.mLooper).postDelayed(new C00234(), 0);
        }
    }

    public final void enable(int i, int i2) {
        switch (i) {
            case 1:
                enable(10);
                break;
            case 2:
                this.ACCURACY_CurrentLoc = i2;
                if (this.mLooper != null) {
                    if (this.mEnable) {
                        CaLogger.warning("mEnable value is true.");
                        disable();
                    }
                    CaLogger.trace();
                    this.mEnable = true;
                    this.mGpsInfo = new PositionContextBean(10000.0f);
                    this.mWpsInfo = new PositionContextBean(10000.0f);
                    this.mPrePosition = new PositionContextBean();
                    clearTimeOutCheckService();
                    this.mTimeOutCheck = new CaTimeOutCheckManager(this, 11);
                    this.mTimeOutCheckThreadHandler = new Thread(this.mTimeOutCheck);
                    this.mTimeOutCheckThreadHandler.start();
                    new Handler(this.mLooper).postDelayed(new C00245(), 0);
                    break;
                }
                CaLogger.error("Looper is null");
                return;
        }
    }

    public final void initializeManager(Context context) {
        this.mGpsManager = (LocationManager) context.getSystemService(SemLocationManager.GEOFENCE_LOCATION);
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        }
        this.mGpsInfo = new PositionContextBean(10000.0f);
        this.mWpsInfo = new PositionContextBean(10000.0f);
        this.mPrePosition = new PositionContextBean();
        this.mEnable = false;
        this.sLm = (SemLocationManager) context.getSystemService("sec_location");
        this.filter = new IntentFilter();
        this.filter.addAction("com.samsung.android.contextaware.SLOCATION");
        this.mBrReceiver = new C00223();
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public final void notifyCurrentPositionObserver() {
        if (this.mListener != null) {
            disable();
            if (this.mGpsInfo.getType() != 0) {
                if (this.mGpsInfo.getAccuracy() <= ACCURACY_GOOD_THRESHOLE) {
                    updateGpsPosition();
                } else if (this.mWpsInfo.getType() != 0) {
                    updateWpsPosition();
                } else {
                    updateEmptyPosition();
                }
            } else if (this.mWpsInfo.getType() != 0) {
                updateWpsPosition();
            } else {
                updateEmptyPosition();
            }
        }
    }

    public final void occurTimeOut() {
        notifyCurrentPositionObserver();
    }

    public final void registerCurrentPositionObserver(ICurrrentPositionObserver iCurrrentPositionObserver) {
        this.mListener = iCurrrentPositionObserver;
    }

    protected void removeCurrentLoc() {
        if (this.sLm != null) {
            this.sLm.removeSingleLocation(PendingIntent.getBroadcast(mContext, 0, new Intent("com.samsung.android.contextaware.SLOCATION"), 0));
            CaLogger.debug("Remove CurL");
        }
    }

    protected void requestCurrentLoc(int i) {
        Object obj = null;
        if (this.sLm != null) {
            int requestSingleLocation = this.sLm.requestSingleLocation(this.ACCURACY_CurrentLoc, i, false, PendingIntent.getBroadcast(mContext, 0, new Intent("com.samsung.android.contextaware.SLOCATION"), 0));
            CaLogger.debug("result of SLM req : " + requestSingleLocation);
            if (requestSingleLocation > -1) {
                obj = 1;
                this.LocRequestSource |= 2;
                mContext.registerReceiver(this.mBrReceiver, this.filter);
                CaLogger.debug("Request CurL");
            }
        } else {
            CaLogger.error("requestSingleL err - sLm is null ");
        }
        if (obj == null) {
            registerGpsListener();
            registerWpsListener();
            this.LocRequestSource |= 1;
        }
    }

    public final void terminateManager() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mGpsListener);
        }
    }

    public final void unregisterCurrentPositionObserver() {
        this.mListener = null;
    }
}
