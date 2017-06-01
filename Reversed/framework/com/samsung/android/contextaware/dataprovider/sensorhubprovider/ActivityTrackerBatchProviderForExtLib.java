package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.ContextList.ContextType;

public abstract class ActivityTrackerBatchProviderForExtLib extends ExtLibTypeProvider {
    protected ActivityTrackerBatchProviderForExtLib(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
    }

    public /* bridge */ /* synthetic */ void enable() {
        super.enable();
    }

    protected final String getDependentService() {
        return ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ACTIVITY_TRACKER_SERVICE;
    }

    public /* bridge */ /* synthetic */ void occurTimeOut() {
        super.occurTimeOut();
    }
}
