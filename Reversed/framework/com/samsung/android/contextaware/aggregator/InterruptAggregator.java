package com.samsung.android.contextaware.aggregator;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.InterruptContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;

public class InterruptAggregator extends InterruptContextProvider {
    public InterruptAggregator(ContextComponent contextComponent) {
        super(contextComponent);
    }

    public final void start(Listener listener, int i) {
        CaLogger.trace();
        ((Aggregator) this.mProvider).initializeFaultDetectionResult();
        ((Aggregator) this.mProvider).registerObserver();
        Iterator it = ((Aggregator) this.mProvider).getSubCollectors().iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null) {
                contextComponent.start(listener, i);
            }
        }
        super.start(listener, i);
    }

    public final void stop(Listener listener, int i) {
        CaLogger.trace();
        ((Aggregator) this.mProvider).initializeFaultDetectionResult();
        Iterator it = ((Aggregator) this.mProvider).getSubCollectors().iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null) {
                contextComponent.stop(listener, i);
            }
        }
        super.stop(listener, i);
    }
}
