package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.ConcurrentHashMap;

public class RestoreManager {
    public static final String REGISTER_CMD_RESTORE = "REGISTER_CMD_RESTORE";
    public static final String UNREGISTER_CMD_RESTORE = "UNREGISTER_CMD_RESTORE";
    private boolean mIsRestore = false;
    private ConcurrentHashMap<String, RestoreTransaction> mRestoreTransaction = new ConcurrentHashMap();

    protected RestoreManager(ContextManager contextManager) {
        this.mRestoreTransaction.put(REGISTER_CMD_RESTORE, new RegisterCmdOperationRestore(contextManager));
        this.mRestoreTransaction.put(UNREGISTER_CMD_RESTORE, new UnregisterCmdOperationRestore(contextManager));
    }

    protected final void initializeManager() {
        this.mIsRestore = false;
    }

    protected final boolean isRestoreEnable() {
        return this.mIsRestore;
    }

    protected final void runRestore(String str, Listener listener, int i, IContextObserver iContextObserver) {
        if (this.mIsRestore) {
            for (RestoreTransaction restoreTransaction : this.mRestoreTransaction.values()) {
                if (str.equals(restoreTransaction.getRestoreType())) {
                    restoreTransaction.runRestore(listener, i, iContextObserver);
                    break;
                }
            }
            return;
        }
        CaLogger.error("mIsRestore is false");
    }

    protected final void setRestoreEnable() {
        this.mIsRestore = true;
    }
}
