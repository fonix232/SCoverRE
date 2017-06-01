package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.IPassiveCurrrentPositionObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import com.samsung.android.location.SemLocationManager;

public class CaPassiveCurrentPositionManager implements IUtilManager, IPassiveCurrentPositionObservable {
    private static final float ACCURACY_GOOD_THRESHOLE = 4800.0f;
    private static final float GPS_MIN_DISTANCE = 0.0f;
    private static final long GPS_MIN_TIME = 1000;
    private boolean mEnable = false;
    private PositionContextBean mGpsInfo;
    private final LocationListener mGpsListener = new C00311();
    private LocationManager mGpsManager;
    private IPassiveCurrrentPositionObserver mListener;
    private final Looper mLooper;
    private PositionContextBean mPrePosition;
    private int mSatelliteCount;
    private final NmeaListener m_nmea_listener = new C00322();

    class C00311 implements LocationListener {
        C00311() {
        }

        public final void onLocationChanged(Location location) {
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float accuracy = location.getAccuracy();
            double calculationDistance = PositionContextBean.calculationDistance(CaPassiveCurrentPositionManager.this.mPrePosition.getLatitude(), CaPassiveCurrentPositionManager.this.mPrePosition.getLongitude(), latitude, longitude);
            int i = 1;
            if (location.getProvider().equals("network")) {
                i = 2;
            } else if (location.getProvider().equals("fused")) {
                i = 3;
            }
            CaPassiveCurrentPositionManager.this.mGpsInfo.setPosition(i, utcTime, latitude, longitude, altitude, calculationDistance, speed, accuracy, CaPassiveCurrentPositionManager.this.mSatelliteCount);
            if (accuracy <= CaPassiveCurrentPositionManager.ACCURACY_GOOD_THRESHOLE) {
                CaPassiveCurrentPositionManager.this.notifyPassiveCurrentPositionObserver(CaPassiveCurrentPositionManager.this.mGpsInfo);
            }
        }

        public final void onProviderDisabled(String str) {
            CaLogger.info("Location service is disabled");
        }

        public final void onProviderEnabled(String str) {
            CaLogger.info("Location service is enabled");
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    class C00322 implements NmeaListener {
        C00322() {
        }

        public void onNmeaReceived(long j, String str) {
            String[] split = str.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            if (split[0].equals("$GPGGA")) {
                if (split[7].equals("")) {
                    CaLogger.error("satelliteCount null");
                    return;
                }
                CaPassiveCurrentPositionManager.this.mSatelliteCount = CaConvertUtil.strToInt(split[7]);
                CaLogger.info("Satellite Count : " + split[7]);
            }
        }
    }

    class C00333 implements Runnable {
        C00333() {
        }

        public void run() {
            CaPassiveCurrentPositionManager.this.registerGpsListener();
        }
    }

    class C00344 implements Runnable {
        C00344() {
        }

        public void run() {
            CaPassiveCurrentPositionManager.this.unregisterGpsListener();
        }
    }

    public CaPassiveCurrentPositionManager(Context context, Looper looper, IPassiveCurrrentPositionObserver iPassiveCurrrentPositionObserver) {
        this.mLooper = looper;
        initializeManager(context);
        registerPassiveCurrentPositionObserver(iPassiveCurrrentPositionObserver);
    }

    private void registerGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
            return;
        }
        this.mGpsManager.requestLocationUpdates("passive", GPS_MIN_TIME, GPS_MIN_DISTANCE, this.mGpsListener, this.mLooper);
        this.mGpsManager.addNmeaListener(this.m_nmea_listener);
    }

    public static int sendPositionToSensorHub(int i, int[] iArr, double d, double d2, double d3, double d4, float f, float f2, int i2) {
        byte[] bArr = new byte[22];
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000000.0d * d), 4), 0, bArr, 0, 4);
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000000.0d * d2), 4), 0, bArr, 4, 4);
        int i3 = 4 + 4;
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000.0d * d3), 4), 0, bArr, i3, 4);
        i3 += 4;
        System.arraycopy(CaConvertUtil.intToByteArr((int) f2, 1), 0, bArr, i3, 1);
        i3++;
        System.arraycopy(CaConvertUtil.intToByteArr(iArr[0], 1), 0, bArr, i3, 1);
        i3++;
        System.arraycopy(CaConvertUtil.intToByteArr(iArr[1], 1), 0, bArr, i3, 1);
        i3++;
        System.arraycopy(CaConvertUtil.intToByteArr(iArr[2], 1), 0, bArr, i3, 1);
        i3++;
        System.arraycopy(CaConvertUtil.intToByteArr(i2, 1), 0, bArr, i3, 1);
        i3++;
        System.arraycopy(CaConvertUtil.intToByteArr((int) (100.0f * f), 2), 0, bArr, i3, 2);
        i3 += 2;
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000.0d * d4), 2), 0, bArr, i3, 2);
        System.arraycopy(CaConvertUtil.intToByteArr(i, 1), 0, bArr, i3 + 2, 1);
        return SensorHubCommManager.getInstance().sendCmdToSensorHub(bArr, (byte) -63, SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEIN);
    }

    private void unregisterGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
            return;
        }
        this.mGpsManager.removeUpdates(this.mGpsListener);
        this.mGpsManager.removeNmeaListener(this.m_nmea_listener);
    }

    public final void disable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mEnable) {
            CaLogger.trace();
            new Handler(this.mLooper).postDelayed(new C00344(), 0);
            this.mEnable = false;
        }
    }

    public final void enable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mLooper == null) {
            CaLogger.error("Looper is null");
        } else if (!this.mEnable) {
            CaLogger.trace();
            this.mEnable = true;
            this.mSatelliteCount = 0;
            this.mGpsInfo = new PositionContextBean(1000.0f);
            this.mPrePosition = new PositionContextBean();
            new Handler(this.mLooper).postDelayed(new C00333(), 0);
        }
    }

    public final void initializeManager(Context context) {
        if (context != null) {
            this.mGpsManager = (LocationManager) context.getSystemService(SemLocationManager.GEOFENCE_LOCATION);
            if (this.mGpsManager == null) {
                CaLogger.error("mGpsManager is null");
            }
            this.mGpsInfo = new PositionContextBean(1000.0f);
            this.mPrePosition = new PositionContextBean();
            this.mEnable = false;
        }
    }

    public final void notifyPassiveCurrentPositionObserver(PositionContextBean positionContextBean) {
        if (this.mListener != null) {
            this.mPrePosition = positionContextBean;
            this.mListener.updatePassiveCurrentPosition(positionContextBean.getType(), positionContextBean.getUtcTime(), positionContextBean.getLatitude(), positionContextBean.getLongitude(), positionContextBean.getAltitude(), positionContextBean.getDistance(), positionContextBean.getSpeed(), positionContextBean.getAccuracy(), positionContextBean.getSatelliteCount());
        }
    }

    public final void registerPassiveCurrentPositionObserver(IPassiveCurrrentPositionObserver iPassiveCurrrentPositionObserver) {
        this.mListener = iPassiveCurrrentPositionObserver;
    }

    public final void terminateManager() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mGpsListener);
        }
    }

    public final void unregisterPassiveCurrentPositionObserver() {
        this.mListener = null;
    }
}
