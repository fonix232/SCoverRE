package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorProvider;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class TemperatureHumiditySensorRunner extends EnvironmentSensorProvider {
    private double[] mHumidityData;
    private double[] mTemperatureData;

    public TemperatureHumiditySensorRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mTemperatureData = null;
        this.mHumidityData = null;
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
        return ContextType.SENSORHUB_RUNNER_RAW_TEMPERATURE_HUMIDITY_SENSOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"LoggingStatus", "Temperature", "Humidity"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r1 = new byte[3];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(super.getInterval(), 2);
        r1[0] = (byte) 6;
        r1[1] = intToByteArr[0];
        r1[2] = intToByteArr[1];
        return r1;
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

    public final int parse(byte[] bArr, int i) {
        byte[] bArr2 = (byte[]) bArr.clone();
        int i2 = i;
        if ((bArr2.length - i) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = (bArr2[i] << 8) + (bArr2[i2] & 255);
        if (i4 <= 0) {
            CaLogger.error(SensorHubErrors.ERROR_ENVIRONMENT_SENSOR_COUNT.getMessage());
            return -1;
        }
        this.mTemperatureData = new double[i4];
        this.mHumidityData = new double[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            if ((bArr2.length - i3) - 2 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            i2 = i3 + 1;
            i3 = i2 + 1;
            this.mTemperatureData[i5] = ((double) ((bArr2[i3] << 8) + (bArr2[i2] & 255))) / 100.0d;
            i2 = i3 + 1;
            i3 = i2 + 1;
            this.mHumidityData[i5] = ((double) ((bArr2[i3] << 8) + (bArr2[i2] & 255))) / 100.0d;
        }
        String[] contextValueNames = getContextValueNames();
        super.getContextBean().putContext(contextValueNames[0], super.getLoggingStatus());
        super.getContextBean().putContext(contextValueNames[1], this.mTemperatureData);
        super.getContextBean().putContext(contextValueNames[2], this.mHumidityData);
        super.notifyObserver();
        return i3;
    }
}
