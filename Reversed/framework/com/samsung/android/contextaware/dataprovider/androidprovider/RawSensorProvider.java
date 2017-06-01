package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class RawSensorProvider extends AndroidProvider {
    protected static final int DEFAULT_SENSOR_RATE = 60000;
    private final SensorEventListener mSensorListener = new C10791();
    private SensorManager mSensorManager;
    private final int mSensorRate;

    class C10791 implements SensorEventListener {
        C10791() {
        }

        public final void onAccuracyChanged(Sensor sensor, int i) {
        }

        public final void onSensorChanged(SensorEvent sensorEvent) {
            RawSensorProvider.this.getContextBean().putContext("SystemTime", System.currentTimeMillis());
            RawSensorProvider.this.getContextBean().putContext("TimeStamp", sensorEvent.timestamp);
            RawSensorProvider.this.getContextBean().putContext("Accuracy", sensorEvent.accuracy);
            String[] contextValueNames = RawSensorProvider.this.getContextValueNames();
            int min = Math.min(contextValueNames.length, sensorEvent.values.length);
            for (int i = 0; i < min; i++) {
                RawSensorProvider.this.getContextBean().putContext(contextValueNames[i], sensorEvent.values[i]);
            }
            RawSensorProvider.this.notifyObserver();
        }
    }

    protected RawSensorProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable, int i2) {
        super(i, context, looper, iSensorHubResetObservable);
        this.mSensorRate = i2;
    }

    private void registerSensorListener() {
        if (this.mSensorManager == null || this.mSensorListener == null) {
            CaLogger.error("cannot register the sensor listener");
        } else {
            this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(getSensorType()), this.mSensorRate);
        }
    }

    public void disable() {
        unregisterSensorListener();
    }

    public void enable() {
        registerSensorListener();
    }

    protected abstract int getSensorType();

    protected final void initializeManager() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null");
            return;
        }
        this.mSensorManager = (SensorManager) super.getContext().getSystemService("sensor");
        if (this.mSensorManager == null) {
            CaLogger.error("cannot create the SensorManager object");
        }
    }

    protected final void terminateManager() {
        this.mSensorManager = null;
    }

    protected void unregisterSensorListener() {
        if (this.mSensorManager == null || this.mSensorListener == null) {
            CaLogger.error("cannot unregister the sensor listener");
        } else {
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
    }
}
