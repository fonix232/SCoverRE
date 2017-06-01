package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class UnregisterCmdOperationRestore extends RestoreTransaction {
    protected UnregisterCmdOperationRestore(ContextManager contextManager) {
        super(contextManager);
    }

    protected final String getRestoreType() {
        return RestoreManager.UNREGISTER_CMD_RESTORE;
    }

    protected final void runRestore(Listener listener, int i, IContextObserver iContextObserver) {
        if (listener == null) {
            CaLogger.error("listener is null");
            return;
        }
        CaLogger.trace();
        getContextManager().start(listener, ContextList.getInstance().getServiceCode(i), iContextObserver, 2);
        if (listener.getServices().containsKey(Integer.valueOf(i))) {
            listener.increaseServiceCount(i);
        } else {
            listener.getServices().put(Integer.valueOf(i), Integer.valueOf(1));
        }
    }
}
