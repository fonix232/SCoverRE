package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.ActivityTrackerProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class ActivityTrackerInterruptRunner extends ActivityTrackerProvider {
    private static final int DEFAULT_ACTIVITY_TYPE = 0;
    private int mActivityType = 0;

    public ActivityTrackerInterruptRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    protected final int getActivityType() {
        return this.mActivityType == 0 ? -1 : this.mActivityType;
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.getCode();
    }

    public final Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getModeType() {
        return (byte) 1;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i != 37) {
            return false;
        }
        int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
        CaLogger.info("Activity Type = " + Integer.toString(intValue));
        this.mActivityType |= 1 << intValue;
        return true;
    }
}
