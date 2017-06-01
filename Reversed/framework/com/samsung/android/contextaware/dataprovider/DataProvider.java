package com.samsung.android.contextaware.dataprovider;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class DataProvider extends ContextProvider {
    protected DataProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
    }

    protected final void initialize() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null.");
        } else {
            initializeManager();
        }
    }

    protected abstract void initializeManager();

    protected final void terminate() {
        terminateManager();
    }

    protected abstract void terminateManager();
}
