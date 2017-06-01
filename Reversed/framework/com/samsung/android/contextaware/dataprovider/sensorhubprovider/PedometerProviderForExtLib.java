package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.ContextList.ContextType;

public abstract class PedometerProviderForExtLib extends ExtLibTypeProvider {
    protected PedometerProviderForExtLib(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
    }

    public /* bridge */ /* synthetic */ void enable() {
        super.enable();
    }

    protected final String getDependentService() {
        return ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 3;
    }

    public /* bridge */ /* synthetic */ void occurTimeOut() {
        super.occurTimeOut();
    }
}
