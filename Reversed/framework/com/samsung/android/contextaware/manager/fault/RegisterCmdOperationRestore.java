package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class RegisterCmdOperationRestore extends RestoreTransaction {
    protected RegisterCmdOperationRestore(ContextManager contextManager) {
        super(contextManager);
    }

    protected final String getRestoreType() {
        return RestoreManager.REGISTER_CMD_RESTORE;
    }

    protected final void runRestore(Listener listener, int i, IContextObserver iContextObserver) {
        if (listener == null) {
            CaLogger.error("listener is null");
        } else if (listener.getToken() == null) {
            CaLogger.error("token is null");
        } else {
            CaLogger.trace();
            if (listener.getServices().contains(Integer.valueOf(i))) {
                listener.getServices().remove(Integer.valueOf(i));
            }
            getContextManager().stop(listener, ContextList.getInstance().getServiceCode(i), iContextObserver, null, 2);
            if (listener.getServices().isEmpty()) {
                listener.getToken().unlinkToDeath(listener, 0);
                ListenerListManager.getInstance().removeListener(listener);
            }
        }
    }
}
