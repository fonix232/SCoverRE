package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BottomFlatDetectorRunner extends LibTypeProvider {
    public BottomFlatDetectorRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_BOTTOM_FLAT_DETECTOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Status"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) 0, (byte) 1};
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0, (byte) 2};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_BOTTOM_FLAT_DETECTOR_SERVICE;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        CaLogger.trace();
        String[] contextValueNames = getContextValueNames();
        if ((bArr.length - i) - 4 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = ((i + 1) + 1) + 1;
        int i3 = i2 + 1;
        super.getContextBean().putContext(contextValueNames[0], (short) bArr[i2]);
        super.notifyObserver();
        return i3;
    }
}
