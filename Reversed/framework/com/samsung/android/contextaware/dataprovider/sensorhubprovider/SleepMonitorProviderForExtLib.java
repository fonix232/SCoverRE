package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.ContextList.ContextType;

public abstract class SleepMonitorProviderForExtLib extends ExtLibTypeProvider {
    protected SleepMonitorProviderForExtLib(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
    }

    public /* bridge */ /* synthetic */ void enable() {
        super.enable();
    }

    protected final String getDependentService() {
        return ContextType.SENSORHUB_RUNNER_SLEEP_MONITOR.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 37;
    }

    public /* bridge */ /* synthetic */ void occurTimeOut() {
        super.occurTimeOut();
    }
}
