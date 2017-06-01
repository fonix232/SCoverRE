package com.samsung.android.contextaware.aggregator;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorProvider;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class EnvironmentSensorAggregator extends Aggregator {
    private int mLoggingStatus = 0;

    protected EnvironmentSensorAggregator(int i, Context context, Looper looper, CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, copyOnWriteArrayList, iSensorHubResetObservable);
    }

    protected abstract boolean checkCompensationData(double[] dArr);

    protected abstract double[] compensateForRawData(double[][] dArr);

    protected final int getLoggingStatus() {
        return this.mLoggingStatus;
    }

    protected final double[] getRawSensorData(Bundle bundle, String str) {
        return bundle.getDoubleArray(str);
    }

    protected String[] getRawSensorValueNames() {
        return getContextValueNames();
    }

    protected void notifyCompensationData(double[] dArr) {
        super.getContextBean().putContext(getContextValueNames()[0], dArr);
        notifyObserver();
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        Iterator it = super.getSubCollectors().iterator();
        while (it.hasNext()) {
            ContextProvider contextProvider = (ContextProvider) it.next();
            if (contextProvider != null && (contextProvider instanceof EnvironmentSensorProvider)) {
                return contextProvider.setPropertyValue(i, e);
            }
        }
        return false;
    }

    public final void updateContext(String str, Bundle bundle) {
        int i = 0;
        if (bundle != null) {
            if (getRawSensorValueNames() == null) {
                CaLogger.error("getRawSensorValueNames() is null");
                return;
            }
            int length = getRawSensorValueNames().length;
            if (length <= 0) {
                CaLogger.error("length of getRawSensorValueNames() is zero");
                return;
            }
            this.mLoggingStatus = bundle.getInt("LoggingStatus");
            double[][] dArr = new double[length][];
            int i2 = 0;
            String[] rawSensorValueNames = getRawSensorValueNames();
            int length2 = rawSensorValueNames.length;
            while (i < length2) {
                dArr[i2] = getRawSensorData(bundle, rawSensorValueNames[i]);
                if (dArr[i2] == null || dArr[i2].length <= 0) {
                    CaLogger.error("rawData[" + Integer.toString(i2) + "].length is null");
                    return;
                } else {
                    i2++;
                    i++;
                }
            }
            double[] compensateForRawData = compensateForRawData(dArr);
            if (compensateForRawData != null && checkCompensationData(compensateForRawData)) {
                notifyCompensationData(compensateForRawData);
            }
        }
    }
}
