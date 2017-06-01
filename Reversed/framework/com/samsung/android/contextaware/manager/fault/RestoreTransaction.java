package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;

abstract class RestoreTransaction {
    private final ContextManager mContextManager;

    protected RestoreTransaction(ContextManager contextManager) {
        this.mContextManager = contextManager;
    }

    protected final ContextManager getContextManager() {
        return this.mContextManager;
    }

    protected abstract String getRestoreType();

    protected abstract void runRestore(Listener listener, int i, IContextObserver iContextObserver);
}
