package com.samsung.android.contextaware.aggregator;

import com.samsung.android.contextaware.manager.BatchContextProvider;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BatchAggregator extends BatchContextProvider {
    public BatchAggregator(ContextComponent contextComponent) {
        super(contextComponent);
    }

    public final void start(Listener listener, int i) {
        CaLogger.trace();
        this.mProvider.start(listener, i);
    }

    public final void stop(Listener listener, int i) {
        CaLogger.trace();
        this.mProvider.stop(listener, i);
    }
}
