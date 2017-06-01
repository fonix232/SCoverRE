package com.samsung.android.contextaware.aggregator;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.androidprovider.AndroidProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ContextProvider;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Aggregator extends ContextProvider implements IContextObserver, ICmdProcessResultObserver {
    private boolean mAggregatorFaultDetectionResult;
    private final CopyOnWriteArrayList<ContextComponent> mSubCollectors;

    protected Aggregator(int i, Context context, Looper looper, CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        this.mSubCollectors = copyOnWriteArrayList;
    }

    protected final boolean checkFaultDetectionResult() {
        return this.mAggregatorFaultDetectionResult;
    }

    protected final void clearExtension() {
        if (super.isRunning()) {
            Iterator it = this.mSubCollectors.iterator();
            while (it.hasNext()) {
                ContextComponent contextComponent = (ContextComponent) it.next();
                if (contextComponent != null) {
                    contextComponent.clear();
                }
            }
        }
    }

    protected final void disableExtension() {
        if (super.isDisable()) {
            Iterator it = this.mSubCollectors.iterator();
            while (it.hasNext()) {
                ContextComponent contextComponent = (ContextComponent) it.next();
                if (contextComponent != null) {
                    contextComponent.disable();
                }
            }
        }
    }

    protected final void disableForStop(boolean z) {
    }

    protected final void enableExtension() {
        if (super.isEnable()) {
            Iterator it = this.mSubCollectors.iterator();
            while (it.hasNext()) {
                ContextComponent contextComponent = (ContextComponent) it.next();
                if (contextComponent != null) {
                    CaLogger.trace();
                    contextComponent.enable();
                }
            }
        }
    }

    protected final void enableForStart(boolean z) {
    }

    protected final void getContextInfo(Listener listener) {
        CaLogger.error(ContextAwareServiceErrors.ERROR_NOT_SUPPORT_CMD.getMessage());
    }

    public Bundle getFaultDetectionResult() {
        return checkFaultDetectionResult() ? super.getFaultDetectionResult(0, ContextAwareServiceErrors.SUCCESS.getMessage()) : super.getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_SUB_COLLECTION.getMessage());
    }

    protected final ContextComponent getSubCollectionObj(String str) {
        Iterator it = this.mSubCollectors.iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null && contextComponent.getContextType().equals(str)) {
                return contextComponent;
            }
        }
        return null;
    }

    protected final CopyOnWriteArrayList<ContextComponent> getSubCollectors() {
        return this.mSubCollectors;
    }

    protected final void initialize() {
        initializeAggregator();
    }

    protected void initializeAggregator() {
    }

    protected final void initializeFaultDetectionResult() {
        this.mAggregatorFaultDetectionResult = true;
    }

    protected final void notifyApStatus() {
        Iterator it = this.mSubCollectors.iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null) {
                if (contextComponent instanceof AndroidProvider) {
                    contextComponent.updateAPStatus(super.getAPStatus());
                }
                contextComponent.updateApPowerStatusForPreparedCollection();
            }
        }
        super.setAPStatus(0);
    }

    public void pause() {
        Iterator it = this.mSubCollectors.iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null) {
                contextComponent.pause();
            }
        }
        super.pause();
    }

    protected final void registerObserver() {
        Iterator it = this.mSubCollectors.iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null) {
                contextComponent.registerObserver(this);
                contextComponent.registerCmdProcessResultObserver(this);
            }
        }
    }

    public void resume() {
        Iterator it = this.mSubCollectors.iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null) {
                contextComponent.resume();
            }
        }
        super.resume();
    }

    public final void start(Listener listener, int i) {
        initializeFaultDetectionResult();
        registerObserver();
        if (super.isEnable()) {
            Iterator it = this.mSubCollectors.iterator();
            while (it.hasNext()) {
                ContextComponent contextComponent = (ContextComponent) it.next();
                if (contextComponent != null) {
                    contextComponent.start(listener, i);
                }
            }
        }
        super.start(listener, i);
    }

    public final void stop(Listener listener, int i) {
        initializeFaultDetectionResult();
        if (super.isDisable()) {
            Iterator it = this.mSubCollectors.iterator();
            while (it.hasNext()) {
                ContextComponent contextComponent = (ContextComponent) it.next();
                if (contextComponent != null) {
                    contextComponent.stop(listener, i);
                }
            }
        }
        super.stop(listener, i);
    }

    protected final void terminate() {
        unregisterObserver();
        terminateAggregator();
    }

    protected void terminateAggregator() {
    }

    protected final void unregisterObserver() {
        Iterator it = this.mSubCollectors.iterator();
        while (it.hasNext()) {
            ContextComponent contextComponent = (ContextComponent) it.next();
            if (contextComponent != null) {
                contextComponent.unregisterObserver(this);
                contextComponent.unregisterCmdProcessResultObserver(this);
            }
        }
    }

    protected void updateApSleep() {
        notifyApStatus();
    }

    protected void updateApWakeup() {
        notifyApStatus();
    }

    public final void updateCmdProcessResult(String str, Bundle bundle) {
        if (str.equals(ContextType.CMD_PROCESS_FAULT_DETECTION.getCode()) && bundle.getInt("CheckResult") != 0) {
            this.mAggregatorFaultDetectionResult = false;
            CaLogger.debug(getContextType() + " : SubCollection(" + ContextList.getInstance().getServiceCode(bundle.getInt("Service")) + ") process result is failed.");
        }
    }

    public abstract void updateContext(String str, Bundle bundle);
}
