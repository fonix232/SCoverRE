package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.aggregator.AggregatorErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class LifeLogAggregator extends Aggregator {
    public LifeLogAggregator(int i, CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, null, null, copyOnWriteArrayList, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected void clearAccordingToRequest() {
        CaLogger.trace();
        super.clearAccordingToRequest();
    }

    public final void disable() {
        CaLogger.trace();
    }

    protected void display() {
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_LIFE_LOG.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"LoggingType", "LoggingBundle"};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        String str = null;
        if (i == 28 || i == 29 || i == 30 || i == 31) {
            str = ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode();
        } else if (i == 32) {
            str = ContextType.AGGREGATOR_LPP.getCode();
        }
        if (str == null || str.isEmpty()) {
            return false;
        }
        ContextComponent subCollectionObj = getSubCollectionObj(str);
        if (subCollectionObj != null) {
            return subCollectionObj.setPropertyValue(i, e);
        }
        CaLogger.error(AggregatorErrors.getMessage(AggregatorErrors.ERROR_SUB_COLLECTOR_NULL_EXCEPTION.getCode()));
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void updateContext(java.lang.String r9, android.os.Bundle r10) {
        /*
        r8 = this;
        monitor-enter(r8);
        if (r9 == 0) goto L_0x0009;
    L_0x0003:
        r3 = r9.isEmpty();	 Catch:{ all -> 0x0246 }
        if (r3 == 0) goto L_0x0018;
    L_0x0009:
        r3 = com.samsung.android.contextaware.aggregator.AggregatorErrors.ERROR_UPDATED_CONTEXT_TYPE_FAULT;	 Catch:{ all -> 0x0246 }
        r3 = r3.getCode();	 Catch:{ all -> 0x0246 }
        r3 = com.samsung.android.contextaware.aggregator.AggregatorErrors.getMessage(r3);	 Catch:{ all -> 0x0246 }
        com.samsung.android.contextaware.utilbundle.logger.CaLogger.error(r3);	 Catch:{ all -> 0x0246 }
        monitor-exit(r8);
        return;
    L_0x0018:
        if (r10 != 0) goto L_0x0029;
    L_0x001a:
        r3 = com.samsung.android.contextaware.aggregator.AggregatorErrors.ERROR_UPDATED_CONTEXT_NULL_EXCEPTION;	 Catch:{ all -> 0x0246 }
        r3 = r3.getCode();	 Catch:{ all -> 0x0246 }
        r3 = com.samsung.android.contextaware.aggregator.AggregatorErrors.getMessage(r3);	 Catch:{ all -> 0x0246 }
        com.samsung.android.contextaware.utilbundle.logger.CaLogger.error(r3);	 Catch:{ all -> 0x0246 }
        monitor-exit(r8);
        return;
    L_0x0029:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0246 }
        r3.<init>();	 Catch:{ all -> 0x0246 }
        r4 = "updateContext:";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0246 }
        r3 = r3.append(r9);	 Catch:{ all -> 0x0246 }
        r3 = r3.toString();	 Catch:{ all -> 0x0246 }
        com.samsung.android.contextaware.utilbundle.logger.CaLogger.info(r3);	 Catch:{ all -> 0x0246 }
        r3 = com.samsung.android.contextaware.ContextList.ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT;	 Catch:{ all -> 0x0246 }
        r3 = r3.getCode();	 Catch:{ all -> 0x0246 }
        r3 = r9.equals(r3);	 Catch:{ all -> 0x0246 }
        if (r3 == 0) goto L_0x01af;
    L_0x004c:
        r3 = "StayingAreaCount";
        r3 = r10.getInt(r3);	 Catch:{ all -> 0x0246 }
        if (r3 <= 0) goto L_0x0111;
    L_0x0055:
        r1 = r8.getContextValueNames();	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 0;
        r4 = r1[r4];	 Catch:{ all -> 0x0246 }
        r5 = 1;
        r3.putContext(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 1;
        r4 = r1[r4];	 Catch:{ all -> 0x0246 }
        r3.putContext(r4, r10);	 Catch:{ all -> 0x0246 }
        r3 = com.samsung.android.contextaware.ContextList.ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT;	 Catch:{ all -> 0x0246 }
        r3 = r3.getCode();	 Catch:{ all -> 0x0246 }
        r2 = r8.getSubCollectionObj(r3);	 Catch:{ all -> 0x0246 }
        if (r2 != 0) goto L_0x0082;
    L_0x007a:
        r3 = "Sub-collection object is null";
        com.samsung.android.contextaware.utilbundle.logger.CaLogger.error(r3);	 Catch:{ all -> 0x0246 }
        monitor-exit(r8);
        return;
    L_0x0082:
        r0 = r2.getContextValueNames();	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 0;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 0;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getInt(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 1;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 1;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getLongArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 2;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 2;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getDoubleArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 3;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 3;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getDoubleArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 4;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 4;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getDoubleArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 5;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 5;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getIntArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 6;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 6;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getIntArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 7;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 7;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getIntArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r8.notifyObserver();	 Catch:{ all -> 0x0246 }
    L_0x0111:
        r3 = "MovingCount";
        r3 = r10.getInt(r3);	 Catch:{ all -> 0x0246 }
        if (r3 <= 0) goto L_0x01ad;
    L_0x011a:
        r1 = r8.getContextValueNames();	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 0;
        r4 = r1[r4];	 Catch:{ all -> 0x0246 }
        r5 = 2;
        r3.putContext(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 1;
        r4 = r1[r4];	 Catch:{ all -> 0x0246 }
        r3.putContext(r4, r10);	 Catch:{ all -> 0x0246 }
        r3 = com.samsung.android.contextaware.ContextList.ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT;	 Catch:{ all -> 0x0246 }
        r3 = r3.getCode();	 Catch:{ all -> 0x0246 }
        r2 = r8.getSubCollectionObj(r3);	 Catch:{ all -> 0x0246 }
        if (r2 != 0) goto L_0x0147;
    L_0x013f:
        r3 = "Sub-collection object is null";
        com.samsung.android.contextaware.utilbundle.logger.CaLogger.error(r3);	 Catch:{ all -> 0x0246 }
        monitor-exit(r8);
        return;
    L_0x0147:
        r0 = r2.getContextValueNames();	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 8;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 8;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getInt(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 9;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 9;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r6 = r10.getLong(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r6);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 10;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 10;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getIntArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 11;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 11;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getIntArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 12;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 12;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getIntArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r8.notifyObserver();	 Catch:{ all -> 0x0246 }
    L_0x01ad:
        monitor-exit(r8);
        return;
    L_0x01af:
        r3 = com.samsung.android.contextaware.ContextList.ContextType.AGGREGATOR_LPP;	 Catch:{ all -> 0x0246 }
        r3 = r3.getCode();	 Catch:{ all -> 0x0246 }
        r3 = r9.equals(r3);	 Catch:{ all -> 0x0246 }
        if (r3 == 0) goto L_0x01ad;
    L_0x01bb:
        r1 = r8.getContextValueNames();	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 0;
        r4 = r1[r4];	 Catch:{ all -> 0x0246 }
        r5 = 3;
        r3.putContext(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 1;
        r4 = r1[r4];	 Catch:{ all -> 0x0246 }
        r3.putContext(r4, r10);	 Catch:{ all -> 0x0246 }
        r3 = com.samsung.android.contextaware.ContextList.ContextType.AGGREGATOR_LPP;	 Catch:{ all -> 0x0246 }
        r3 = r3.getCode();	 Catch:{ all -> 0x0246 }
        r2 = r8.getSubCollectionObj(r3);	 Catch:{ all -> 0x0246 }
        if (r2 != 0) goto L_0x01e8;
    L_0x01e0:
        r3 = "Sub-collection object is null";
        com.samsung.android.contextaware.utilbundle.logger.CaLogger.error(r3);	 Catch:{ all -> 0x0246 }
        monitor-exit(r8);
        return;
    L_0x01e8:
        r0 = r2.getContextValueNames();	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 0;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 0;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getInt(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 1;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 1;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getLongArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 2;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 2;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getDoubleArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 3;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 3;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getDoubleArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r3 = r8.getContextBean();	 Catch:{ all -> 0x0246 }
        r4 = 4;
        r4 = r0[r4];	 Catch:{ all -> 0x0246 }
        r5 = 4;
        r5 = r0[r5];	 Catch:{ all -> 0x0246 }
        r5 = r10.getDoubleArray(r5);	 Catch:{ all -> 0x0246 }
        r3.putContextForDisplay(r4, r5);	 Catch:{ all -> 0x0246 }
        r8.notifyObserver();	 Catch:{ all -> 0x0246 }
        goto L_0x01ad;
    L_0x0246:
        r3 = move-exception;
        monitor-exit(r8);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.contextaware.aggregator.builtin.LifeLogAggregator.updateContext(java.lang.String, android.os.Bundle):void");
    }
}
