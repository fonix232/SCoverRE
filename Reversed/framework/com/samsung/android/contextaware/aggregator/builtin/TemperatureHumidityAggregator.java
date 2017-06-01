package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import android.util.TimeUtils;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.EnvironmentSensorAggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.lib.builtin.TemperatureHumidityCompensationLibEngine;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class TemperatureHumidityAggregator extends EnvironmentSensorAggregator {
    private TemperatureHumidityCompensationLibEngine mCompensationEngine = null;
    private long mSleepTime = 0;
    private long mWakeupTime = 0;

    public TemperatureHumidityAggregator(int i, CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, null, null, copyOnWriteArrayList, iSensorHubResetObservable);
    }

    protected final boolean checkCompensationData(double[] dArr) {
        return dArr == null || dArr.length > 0;
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected void clearAccordingToRequest() {
        CaLogger.trace();
        super.clearAccordingToRequest();
    }

    protected final double[] compensateForRawData(double[][] dArr) {
        if (dArr == null || dArr.length <= 1) {
            return null;
        }
        double[] dArr2 = new double[1];
        double[] dArr3 = new double[1];
        int length = dArr[0].length;
        int length2 = dArr[1].length;
        int loggingStatus = getLoggingStatus();
        if (length == 0 || length2 == 0) {
            return null;
        }
        int i = length <= length2 ? length * 2 : length2 * 2;
        double[] dArr4 = new double[i];
        this.mWakeupTime = System.currentTimeMillis() * TimeUtils.NANOS_PER_MS;
        CaLogger.info("sleepTime = " + this.mSleepTime + ", wakeupTime = " + this.mWakeupTime);
        long j = (this.mWakeupTime - this.mSleepTime) / ((long) (i / 2));
        long j2 = i > 2 ? this.mSleepTime + (((this.mWakeupTime - this.mSleepTime) - (((long) ((i / 2) - 1)) * j)) / 2) : this.mSleepTime + ((this.mWakeupTime - this.mSleepTime) / 2);
        for (int i2 = 0; i2 < i / 2; i2++) {
            if (loggingStatus == 1) {
                this.mCompensationEngine.native_temperaturehumidity_getLastCompensatedData(dArr2, dArr3);
                CaLogger.info("getLastCompensatedData : compensatedTemp = " + dArr2[0] + ", compensatedHumid = " + dArr3[0]);
            } else if (loggingStatus == 2) {
                long j3 = j2 + (((long) i2) * j);
                this.mCompensationEngine.native_temperaturehumidity_getCompensatedData(dArr[0][i2], dArr[1][i2], dArr2, dArr3, j3);
                CaLogger.info("RawData : rawTempData = " + dArr[0][i2] + ", rawHumidData = " + dArr[1][i2] + ", compensatedTemp = " + dArr2[0] + ",  compensatedHumid = " + dArr3[0] + ", timestamp = " + j3);
            }
            dArr4[i2] = dArr2[0];
            dArr4[i2 + length] = dArr3[0];
        }
        this.mSleepTime = this.mWakeupTime;
        return dArr4;
    }

    public final void disable() {
        CaLogger.trace();
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_TEMPERATURE_HUMIDITY.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Temperature", "Humidity"};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final String[] getRawSensorValueNames() {
        return new String[]{"Temperature", "Humidity"};
    }

    protected final void initializeAggregator() {
        this.mCompensationEngine = new TemperatureHumidityCompensationLibEngine();
    }

    protected final void notifyCompensationData(double[] dArr) {
        String[] contextValueNames = getContextValueNames();
        int length = dArr.length / 2;
        double[] dArr2 = new double[length];
        double[] dArr3 = new double[length];
        for (int i = 0; i < length; i++) {
            dArr2[i] = dArr[i];
            dArr3[i] = dArr[i + length];
        }
        super.getContextBean().putContext(contextValueNames[0], dArr2);
        super.getContextBean().putContext(contextValueNames[1], dArr3);
        super.notifyObserver();
    }

    protected final void terminateAggregator() {
        this.mCompensationEngine = null;
    }

    protected final void updateApSleep() {
        long timeStampForApStatus = super.getTimeStampForApStatus() * TimeUtils.NANOS_PER_MS;
        CaLogger.info("timeStamp = " + Long.toString(timeStampForApStatus));
        this.mSleepTime = timeStampForApStatus;
        super.updateApSleep();
    }
}
