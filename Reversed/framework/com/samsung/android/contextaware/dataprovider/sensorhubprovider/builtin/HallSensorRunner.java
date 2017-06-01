package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class HallSensorRunner extends LibTypeProvider {
    public HallSensorRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
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

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_HALL_SENSOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Angle", "Type", "Intensity"};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return (byte) 50;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] contextValueNames = getContextValueNames();
        i2 = i + 1;
        int i3 = i2 + 1;
        short s = (short) ((bArr[i] & 255) + ((bArr[i2] & 255) << 8));
        i2 = i3 + 1;
        i3 = i2 + 1;
        short s2 = (short) ((bArr[i3] & 255) + ((bArr[i2] & 255) << 8));
        i2 = i3 + 1;
        i3 = i2 + 1;
        short s3 = (short) ((bArr[i3] & 255) + ((bArr[i2] & 255) << 8));
        super.getContextBean().putContext(contextValueNames[0], s);
        super.getContextBean().putContext(contextValueNames[1], s2);
        super.getContextBean().putContext(contextValueNames[2], s3);
        super.notifyObserver();
        return i3;
    }
}
