package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.PositioningProviderForExtLib;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CurrentStatusForMovementPositioningRunner extends PositioningProviderForExtLib {
    public CurrentStatusForMovementPositioningRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.REQUEST_SENSORHUB_MOVEMENT_FOR_POSITIONING_CURRENT_STATUS.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Status"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) 1, (byte) 0};
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

    protected final ITimeOutCheckObserver getTimeOutCheckObserver() {
        return this;
    }

    public void occurTimeOut() {
        CaLogger.error(SensorHubErrors.ERROR_TIME_OUT.getMessage());
        super.occurTimeOut();
    }

    protected final int parse(int i, byte[] bArr) {
        int i2 = i;
        String str = getContextValueNames()[0];
        if (str == null || str.isEmpty() || (bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        super.getContextBean().putContext(str, bArr[i]);
        return i2;
    }
}
