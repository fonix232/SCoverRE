package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class MovingAggregator extends Aggregator {
    public static final int PEDESTRIAN_MOVE = 0;
    public static final int PEDESTRIAN_STOP = 1;
    public static final int UNKNOWN = -1;
    public static final int VEHICLE_MOVE = 2;
    public static final int VEHICLE_STOP = 3;
    private int mOldMode;
    private int mOldMove;
    private int mOldTransMethod;

    public MovingAggregator(int i, CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, null, null, copyOnWriteArrayList, iSensorHubResetObservable);
    }

    private void notifyMovingContext(int i, int i2, int i3) {
        String[] contextValueNames = getContextValueNames();
        super.getContextBean().putContext(contextValueNames[0], i);
        super.getContextBean().putContext(contextValueNames[1], i2);
        super.getContextBean().putContext(contextValueNames[2], i3);
        super.notifyObserver();
        this.mOldMove = i;
        this.mOldTransMethod = i2;
        this.mOldMode = i3;
    }

    private int updateMovingMode(int i) {
        switch (this.mOldMode) {
            case -1:
            case 0:
                return i == 1 ? 6 : this.mOldMode;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return this.mOldMode;
            default:
                return -1;
        }
    }

    private void updatePedestrianStatus(Bundle bundle) {
        int i;
        int i2;
        switch (bundle.getInt("PedestrianStatus")) {
            case -1:
                i = -1;
                i2 = -1;
                break;
            case 0:
                i = 1;
                i2 = 0;
                break;
            case 1:
                i = 0;
                i2 = 0;
                break;
            case 2:
                i = 1;
                i2 = 1;
                break;
            case 3:
                i = 0;
                i2 = 1;
                break;
            default:
                i = -1;
                i2 = -1;
                break;
        }
        notifyMovingContext(i, i2, updateMovingMode(i2));
    }

    private void updatePedometerData(Bundle bundle) {
        this.mOldMode = bundle.getInt("StepStatus");
        notifyMovingContext(this.mOldMove, this.mOldTransMethod, updateMovingMode(this.mOldTransMethod));
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mOldMove = -1;
        this.mOldTransMethod = -1;
        this.mOldMode = -1;
        String[] contextValueNames = getContextValueNames();
        super.getContextBean().putContext(contextValueNames[0], this.mOldMove);
        super.getContextBean().putContext(contextValueNames[1], this.mOldTransMethod);
        super.getContextBean().putContext(contextValueNames[2], this.mOldMode);
        super.notifyObserver();
    }

    public final void disable() {
        CaLogger.trace();
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_MOVING.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"move", "transMethod", "mode"};
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final void initializeAggregator() {
        LocationAggregator locationAggregator = (LocationAggregator) getSubCollectionObj(ContextType.AGGREGATOR_LOCATION.getCode());
        if (locationAggregator != null) {
            locationAggregator.setPropertyValue(1, Integer.valueOf(10));
        }
    }

    public final void updateContext(String str, Bundle bundle) {
        if (str.equals(ContextType.AGGREGATOR_LOCATION.getCode())) {
            updatePedestrianStatus(bundle);
        } else if (str.equals(ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode())) {
            updatePedometerData(bundle);
        }
    }
}
