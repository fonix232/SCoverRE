package com.samsung.android.contextaware.manager;

import android.os.Bundle;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class ContextObserverManager {
    private final CopyOnWriteArrayList<ICmdProcessResultObserver> mCmdProcessResultObservers = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<IContextObserver> mObservers = new CopyOnWriteArrayList();

    ContextObserverManager() {
    }

    protected final void notifyCmdProcessResultObserver(String str, Bundle bundle) {
        Iterator it = this.mCmdProcessResultObservers.iterator();
        while (it.hasNext()) {
            ICmdProcessResultObserver iCmdProcessResultObserver = (ICmdProcessResultObserver) it.next();
            if (iCmdProcessResultObserver != null) {
                iCmdProcessResultObserver.updateCmdProcessResult(str, bundle);
            }
        }
    }

    protected final void notifyObserver(String str, Bundle bundle) {
        Iterator it = this.mObservers.iterator();
        while (it.hasNext()) {
            IContextObserver iContextObserver = (IContextObserver) it.next();
            if (iContextObserver != null) {
                iContextObserver.updateContext(str, bundle);
            }
        }
    }

    protected final void registerCmdProcessResultObserver(ICmdProcessResultObserver iCmdProcessResultObserver) {
        if (iCmdProcessResultObserver != null && !this.mCmdProcessResultObservers.contains(iCmdProcessResultObserver)) {
            this.mCmdProcessResultObservers.add(iCmdProcessResultObserver);
        }
    }

    protected final void registerObserver(IContextObserver iContextObserver) {
        if (iContextObserver != null && !this.mObservers.contains(iContextObserver)) {
            this.mObservers.add(iContextObserver);
        }
    }

    protected final void unregisterCmdProcessResultObserver(ICmdProcessResultObserver iCmdProcessResultObserver) {
        if (iCmdProcessResultObserver != null && this.mCmdProcessResultObservers.contains(iCmdProcessResultObserver)) {
            this.mCmdProcessResultObservers.remove(iCmdProcessResultObserver);
        }
    }

    protected final void unregisterObserver(IContextObserver iContextObserver) {
        if (iContextObserver != null && this.mObservers.contains(iContextObserver)) {
            this.mObservers.remove(iContextObserver);
        }
    }
}
