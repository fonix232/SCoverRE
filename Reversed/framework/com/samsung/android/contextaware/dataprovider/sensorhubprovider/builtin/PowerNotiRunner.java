package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.share.executor.ExecutorCommandHandler;

public class PowerNotiRunner extends LibTypeProvider {
    public PowerNotiRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
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
        return ContextType.SENSORHUB_RUNNER_POWER_NOTI.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Noti"};
    }

    public final Bundle getFaultDetectionResult() {
        if (super.getUsedTotalCount() > 1) {
            return super.getFaultDetectionResult();
        }
        CaLogger.debug(ExecutorCommandHandler.RESULT_SUCCESS);
        return super.getFaultDetectionResult(0, ContextAwareServiceErrors.SUCCESS.getMessage());
    }

    protected final byte getInstLibType() {
        return (byte) -1;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void updateApPowerStatus(int i, long j) {
        int i2;
        String[] contextValueNames = getContextValueNames();
        if (i == -47) {
            i2 = 1;
        } else if (i == -46) {
            i2 = 2;
        } else {
            return;
        }
        super.getContextBean().putContext(contextValueNames[0], i2);
        super.notifyObserver();
    }
}
