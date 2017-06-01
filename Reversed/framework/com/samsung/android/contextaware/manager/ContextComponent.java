package com.samsung.android.contextaware.manager;

import android.os.Bundle;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class ContextComponent implements IContextProvider, IContextObservable, ICmdProcessResultObservable {
    private final ContextBean mContextBean = new ContextBean();
    private final ContextObserverManager mContextObserver = new ContextObserverManager();

    protected ContextComponent() {
    }

    private void clearContextBean() {
        this.mContextBean.clearContextBean();
    }

    private void notifyObserver(String str, Bundle bundle) {
        display();
        this.mContextObserver.notifyObserver(str, bundle);
        clearContextBean();
    }

    protected boolean checkNotifyCondition() {
        return true;
    }

    public void clear() {
        clearContextBean();
    }

    protected void clearAccordingToRequest() {
        clear();
    }

    public void disable() {
    }

    protected void disableForStop(int i) {
    }

    protected void display() {
    }

    public void enable() {
    }

    protected void enableForStart(int i) {
    }

    protected final ContextBean getContextBean() {
        return this.mContextBean;
    }

    protected void getContextInfo(Listener listener) {
    }

    public abstract ContextProvider getContextProvider();

    public String getContextType() {
        return "";
    }

    public String[] getContextValueNames() {
        return new String[1];
    }

    protected Bundle getInitContextBundle() {
        return null;
    }

    protected final int getUsedServiceCount() {
        return ListenerListManager.getInstance().getUsedServiceCount(getContextType());
    }

    protected final int getUsedSubCollectionCount() {
        return ListenerListManager.getInstance().getUsedSubCollectionCount(getContextType());
    }

    protected final int getUsedTotalCount() {
        return ListenerListManager.getInstance().getUsedTotalCount(getContextType());
    }

    protected void initialize() {
    }

    protected final boolean isRunning() {
        return getUsedTotalCount() >= 1;
    }

    public void notifyCmdProcessResultObserver(String str, Bundle bundle) {
        this.mContextObserver.notifyCmdProcessResultObserver(str, bundle);
    }

    protected void notifyFaultDetectionResult() {
    }

    protected void notifyInitContext() {
        Bundle initContextBundle = getInitContextBundle();
        if (initContextBundle != null) {
            notifyObserver(getContextType(), initContextBundle);
        }
    }

    public final void notifyObserver() {
        if (checkNotifyCondition()) {
            notifyObserver(getContextType(), getContextBean().getContextBundle());
        }
    }

    public void pause() {
        disable();
    }

    protected void registerApPowerObserver() {
    }

    public final void registerCmdProcessResultObserver(ICmdProcessResultObserver iCmdProcessResultObserver) {
        this.mContextObserver.registerCmdProcessResultObserver(iCmdProcessResultObserver);
    }

    public final void registerObserver(IContextObserver iContextObserver) {
        this.mContextObserver.registerObserver(iContextObserver);
    }

    protected void reset() {
    }

    public void resume() {
        enable();
    }

    protected final <E> boolean setProperty(int i, E e) {
        if (e != null) {
            return setPropertyValue(i, e);
        }
        CaLogger.error("value is null");
        return false;
    }

    public <E> boolean setPropertyValue(int i, E e) {
        return true;
    }

    public abstract void start(Listener listener, int i);

    public abstract void stop(Listener listener, int i);

    protected void terminate() {
    }

    protected void unregisterApPowerObserver() {
    }

    public final void unregisterCmdProcessResultObserver(ICmdProcessResultObserver iCmdProcessResultObserver) {
        this.mContextObserver.unregisterCmdProcessResultObserver(iCmdProcessResultObserver);
    }

    public final void unregisterObserver(IContextObserver iContextObserver) {
        this.mContextObserver.unregisterObserver(iContextObserver);
    }

    public void updateApPowerStatusForPreparedCollection() {
    }
}
