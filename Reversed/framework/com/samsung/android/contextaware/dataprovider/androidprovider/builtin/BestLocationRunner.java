package com.samsung.android.contextaware.dataprovider.androidprovider.builtin;

import android.content.Context;
import android.location.Criteria;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.androidprovider.RawGpsProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BestLocationRunner extends RawGpsProvider {
    private Criteria mCriteria;

    public BestLocationRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.ANDROID_RUNNER_BEST_LOCATION.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Latitude", "Longitude", "Altitude"};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final String getLocationProvider() {
        if (getGpsManager() == null) {
            CaLogger.error("getGpsManager() is null");
            return null;
        }
        String bestProvider = getGpsManager().getBestProvider(this.mCriteria, true);
        CaLogger.info("BestProvider : " + bestProvider);
        return bestProvider;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final void initializeManager() {
        super.initializeManager();
        this.mCriteria = new Criteria();
        this.mCriteria.setAccuracy(1);
        this.mCriteria.setPowerRequirement(2);
        this.mCriteria.setAltitudeRequired(false);
        this.mCriteria.setBearingRequired(false);
        this.mCriteria.setSpeedRequired(false);
        this.mCriteria.setCostAllowed(true);
    }
}
