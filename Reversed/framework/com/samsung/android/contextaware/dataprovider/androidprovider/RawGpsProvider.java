package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.content.smartclip.SemSmartClipMetaTagType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;

public abstract class RawGpsProvider extends AndroidProvider {
    private static final float GPS_MIN_DISTANCE = 0.0f;
    private static final long GPS_MIN_TIME = 1000;
    private LocationManager mGpsManager;
    private final Listener mGpsStatusListener = new C10762();
    private final LocationListener mLocationListener = new C10751();

    class C10751 implements LocationListener {
        C10751() {
        }

        public final void onLocationChanged(Location location) {
            if (location.getAccuracy() > 0.0f) {
                String[] contextValueNames = RawGpsProvider.this.getContextValueNames();
                RawGpsProvider.this.getContextBean().putContext("SystemTime", System.currentTimeMillis());
                RawGpsProvider.this.getContextBean().putContext("TimeStamp", location.getTime());
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[0], location.getLatitude());
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[1], location.getLongitude());
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[2], location.getAltitude());
                if (contextValueNames.length > 3) {
                    RawGpsProvider.this.getContextBean().putContext(contextValueNames[3], location.getBearing());
                    RawGpsProvider.this.getContextBean().putContext(contextValueNames[4], (float) (((double) location.getSpeed()) * 3.6d));
                    RawGpsProvider.this.getContextBean().putContext(contextValueNames[5], location.getAccuracy());
                    Iterator -wrap1 = RawGpsProvider.this.getGpsSatellites();
                    int i = 0;
                    if (-wrap1 != null) {
                        while (-wrap1.hasNext()) {
                            GpsSatellite gpsSatellite = (GpsSatellite) -wrap1.next();
                            if (gpsSatellite != null && gpsSatellite.usedInFix()) {
                                i++;
                            }
                        }
                    }
                    RawGpsProvider.this.getContextBean().putContext("Valid", 1);
                    RawGpsProvider.this.getContextBean().putContext("SVCount", i);
                }
                RawGpsProvider.this.notifyObserver();
            }
        }

        public final void onProviderDisabled(String str) {
            if (RawGpsProvider.this.getContextType() != null && !RawGpsProvider.this.getContextType().isEmpty()) {
                CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "Location service is disabled");
            }
        }

        public final void onProviderEnabled(String str) {
            if (RawGpsProvider.this.getContextType() != null && !RawGpsProvider.this.getContextType().isEmpty()) {
                CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "Location service is enabled");
            }
        }

        public final void onStatusChanged(String str, int i, Bundle bundle) {
            if (RawGpsProvider.this.getContextType() == null || RawGpsProvider.this.getContextType().isEmpty()) {
                CaLogger.error("getContextType() is null");
                return;
            }
            switch (i) {
                case 0:
                    CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "out of service");
                    break;
                case 1:
                    CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "temporarily unavailable");
                    break;
                case 2:
                    CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "available");
                    break;
            }
        }
    }

    class C10762 implements Listener {
        private final float[] mmAz = new float[32];
        private final float[] mmEl = new float[32];
        private final int[] mmMask = new int[3];
        private final int[] mmPrn = new int[32];
        private final float[] mmSnr = new float[32];

        C10762() {
        }

        public final void onGpsStatusChanged(int i) {
            if (i == 4) {
                Iterator -wrap1 = RawGpsProvider.this.getGpsSatellites();
                if (-wrap1 == null) {
                    CaLogger.error("gpsSatellites is null");
                    return;
                }
                int i2 = 0;
                int i3 = 0;
                while (-wrap1.hasNext()) {
                    GpsSatellite gpsSatellite = (GpsSatellite) -wrap1.next();
                    this.mmPrn[i2] = gpsSatellite.getPrn();
                    this.mmSnr[i2] = gpsSatellite.getSnr();
                    this.mmEl[i2] = gpsSatellite.getElevation();
                    this.mmAz[i2] = gpsSatellite.getAzimuth();
                    if (gpsSatellite.usedInFix()) {
                        i3++;
                    }
                    i2++;
                }
                this.mmMask[2] = i3;
                String[] contextValueNames = RawGpsProvider.this.getContextValueNames();
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[0], i2);
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[1], this.mmPrn);
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[2], this.mmSnr);
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[3], this.mmEl);
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[4], this.mmAz);
                RawGpsProvider.this.getContextBean().putContext(contextValueNames[5], this.mmMask);
                RawGpsProvider.this.notifyObserver();
            }
        }
    }

    class C10773 implements Runnable {
        C10773() {
        }

        public void run() {
            RawGpsProvider.this.registerGpsListener();
        }
    }

    class C10784 implements Runnable {
        C10784() {
        }

        public void run() {
            RawGpsProvider.this.unregisterGpsListener();
        }
    }

    protected RawGpsProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
    }

    private Iterator<GpsSatellite> getGpsSatellites() {
        return this.mGpsManager == null ? null : this.mGpsManager.getGpsStatus(null).getSatellites().iterator();
    }

    public void disable() {
        new Handler(super.getLooper()).postDelayed(new C10784(), 0);
    }

    public void enable() {
        new Handler(super.getLooper()).postDelayed(new C10773(), 0);
    }

    protected final LocationManager getGpsManager() {
        return this.mGpsManager;
    }

    protected final Listener getGpsStatusListener() {
        return this.mGpsStatusListener;
    }

    protected abstract String getLocationProvider();

    protected void initializeManager() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null");
            return;
        }
        this.mGpsManager = (LocationManager) super.getContext().getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
        if (this.mGpsManager == null) {
            CaLogger.error("cannot create the GpsManager object");
        }
    }

    protected void registerGpsListener() {
        String locationProvider = getLocationProvider();
        if (this.mGpsManager == null || super.getLooper() == null || locationProvider == null || locationProvider.isEmpty()) {
            CaLogger.error("cannot register the gps listener");
        } else {
            this.mGpsManager.requestLocationUpdates(locationProvider, 1000, 0.0f, this.mLocationListener, super.getLooper());
        }
    }

    protected final void terminateManager() {
    }

    protected void unregisterGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("cannot unregister the gps listener");
        } else {
            this.mGpsManager.removeUpdates(this.mLocationListener);
        }
    }
}
