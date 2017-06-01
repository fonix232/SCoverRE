package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

abstract class ExtLibTypeProvider extends SensorHubProvider implements ITimeOutCheckObserver {
    protected ExtLibTypeProvider(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    protected final boolean checkNotifyCondition() {
        return !getTimeOutCheckManager().isTimeOut();
    }

    public final void disable() {
    }

    public void enable() {
        clear();
        super.enable();
    }

    protected final byte getInstructionForDisable() {
        return (byte) 0;
    }

    protected final byte getInstructionForEnable() {
        return ISensorHubCmdProtocol.INST_LIB_GETVALUE;
    }

    public void occurTimeOut() {
        CaLogger.error(SensorHubErrors.ERROR_TIME_OUT.getMessage());
        super.getTimeOutCheckManager().setTimeOutOccurence(true);
        notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage()));
    }

    protected abstract int parse(int i, byte[] bArr);

    public final int parse(byte[] bArr, int i) {
        int parse = parse(i, bArr);
        if (getTimeOutCheckManager().getHandler() == null) {
            return parse;
        }
        if (!getTimeOutCheckManager().getHandler().isAlive()) {
            CaLogger.error(SensorHubErrors.ERROR_TIME_OUT_CHECK_THREAD_NOT_ALIVE.getMessage());
            return parse;
        } else if (getTimeOutCheckManager().getService() == null) {
            CaLogger.error(SensorHubErrors.ERROR_TIME_OUT_CHECK_SERVICE_NULL_EXCEPTION.getMessage());
            return parse;
        } else {
            if (parse >= 0) {
                getTimeOutCheckManager().getHandler().interrupt();
                notifyObserver();
                notifyFaultDetectionResult();
            }
            return parse;
        }
    }
}
