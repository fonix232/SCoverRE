package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorProvider;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BarometerSensorRunner extends EnvironmentSensorProvider {
    private int[] mBarometerData;
    private int mBarometerInitData;

    public BarometerSensorRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mBarometerInitData = 0;
        this.mBarometerData = null;
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
        return ContextType.SENSORHUB_RUNNER_RAW_BAROMETER_SENSOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Barometer"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r1 = new byte[3];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(super.getInterval(), 2);
        r1[0] = (byte) 3;
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
        if ((bArr2.length - i) - 1 < 0) {
            return -1;
        }
        int i3;
        i2 = i + 1;
        int i4 = bArr2[i];
        if (i4 <= 0) {
            i4 *= -1;
            i3 = i2 + 1;
            i2 = i3 + 1;
            this.mBarometerInitData = (bArr2[i2] << 8) + (bArr2[i3] & 255);
        }
        this.mBarometerData = new int[i4];
        int i5 = 0;
        i3 = i2;
        while (i5 < i4) {
            if ((bArr2.length - i3) - 1 < 0) {
                return -1;
            }
            if (i5 <= 0) {
                i2 = i3 + 1;
                this.mBarometerData[i5] = this.mBarometerInitData + bArr2[i3];
            } else {
                i2 = i3 + 1;
                this.mBarometerData[i5] = this.mBarometerData[i5 - 1] + bArr2[i3];
            }
            this.mBarometerInitData = this.mBarometerData[i5];
            i5++;
            i3 = i2;
        }
        super.getContextBean().putContext(getContextValueNames()[0], this.mBarometerData);
        super.notifyObserver();
        return i3;
    }
}
