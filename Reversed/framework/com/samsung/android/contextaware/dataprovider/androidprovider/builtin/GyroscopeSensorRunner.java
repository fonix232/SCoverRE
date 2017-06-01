package com.samsung.android.contextaware.dataprovider.androidprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.androidprovider.RawSensorProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class GyroscopeSensorRunner extends RawSensorProvider {
    public GyroscopeSensorRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        this(i, context, looper, iSensorHubResetObservable, 60000);
    }

    public GyroscopeSensorRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable, int i2) {
        super(i, context, looper, iSensorHubResetObservable, i2);
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
        return ContextType.ANDROID_RUNNER_GYROSCOPE_SENSOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"GyroX", "GyroY", "GyroZ"};
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

    protected final int getSensorType() {
        return 4;
    }
}
