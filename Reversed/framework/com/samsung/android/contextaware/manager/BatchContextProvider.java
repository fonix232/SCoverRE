package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BatchContextProvider extends ContextProviderDecorator {
    public BatchContextProvider(ContextComponent contextComponent) {
        super(contextComponent);
    }

    public void start(Listener listener, int i) {
        CaLogger.trace();
        this.mProvider.start(listener, i);
    }

    public void stop(Listener listener, int i) {
        CaLogger.trace();
        this.mProvider.stop(listener, i);
    }
}
