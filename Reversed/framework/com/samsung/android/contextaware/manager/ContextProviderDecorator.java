package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;

abstract class ContextProviderDecorator extends ContextComponent {
    protected final ContextComponent mProvider;

    protected ContextProviderDecorator(ContextComponent contextComponent) {
        this.mProvider = contextComponent;
    }

    public final ContextProvider getContextProvider() {
        return this.mProvider.getContextProvider();
    }

    public abstract void start(Listener listener, int i);

    public abstract void stop(Listener listener, int i);
}
