package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class InterruptContextProvider extends ContextProviderDecorator {
    public InterruptContextProvider(ContextComponent contextComponent) {
        super(contextComponent);
    }

    public void start(Listener listener, int i) {
        CaLogger.trace();
        this.mProvider.initialize();
        this.mProvider.clear();
        this.mProvider.enableForStart(i);
        this.mProvider.registerApPowerObserver();
        if (i == 1) {
            this.mProvider.notifyFaultDetectionResult();
        }
    }

    public void stop(Listener listener, int i) {
        CaLogger.trace();
        this.mProvider.clear();
        this.mProvider.unregisterApPowerObserver();
        this.mProvider.disableForStop(i);
        if (i == 1) {
            this.mProvider.notifyFaultDetectionResult();
        }
        this.mProvider.terminate();
    }
}
