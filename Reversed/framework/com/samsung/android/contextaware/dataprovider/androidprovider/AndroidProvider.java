package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.DataProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class AndroidProvider extends DataProvider {
    protected AndroidProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
    }

    protected final void getContextInfo(Listener listener) {
        CaLogger.error(ContextAwareServiceErrors.ERROR_NOT_SUPPORT_CMD.getMessage());
    }

    public Bundle getFaultDetectionResult() {
        return super.getFaultDetectionResult(0, ContextAwareServiceErrors.SUCCESS.getMessage());
    }

    public void updateAPStatus(int i) {
        super.setAPStatus(i);
    }
}
