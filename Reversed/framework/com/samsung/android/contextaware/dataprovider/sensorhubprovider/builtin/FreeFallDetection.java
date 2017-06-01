package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class FreeFallDetection extends LibTypeProvider {
    public FreeFallDetection(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        CaLogger.info("FFD FreeFallDetection Create");
    }

    public final String getContextType() {
        CaLogger.info("FFD getContextType");
        return ContextType.SENSORHUB_RUNNER_FREE_FALL_DETECTION.getCode();
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected byte getInstLibType() {
        CaLogger.info("FFD getInstLibType");
        return ISensorHubCmdProtocol.TYPE_FREE_FALL_DETECTION;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        CaLogger.info("parse start:" + i);
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        long j = 0;
        int i3 = 0;
        int i4 = i2;
        while (i3 <= 3) {
            j = (j << 8) + ((long) (bArr[i4] & 255));
            i3++;
            i4++;
        }
        CaLogger.info("FFD height =" + j);
        super.getContextBean().putContext("height", j);
        super.notifyObserver();
        CaLogger.info("parse end:" + i4);
        return i4;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        return true;
    }
}
