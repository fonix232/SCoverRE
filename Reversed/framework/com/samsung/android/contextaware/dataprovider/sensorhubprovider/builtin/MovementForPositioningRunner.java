package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class MovementForPositioningRunner extends LibTypeProvider {
    private static final double DEFAULT_MOVE_DISTANCE = 100.0d;
    private static final int DEFAULT_MOVE_DURATION = 20;
    private static final int DEFAULT_MOVE_MIN_DURATION = 5;
    private static final int DEFAULT_NOMOVE_DURATION = 60;
    private double mMoveDistanceThrs = DEFAULT_MOVE_DISTANCE;
    private int mMoveDurationThrs = 20;
    private int mMoveMinDurationThrs = 5;
    private int mNoMoveDurationThrs = 60;

    public MovementForPositioningRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_MOVEMENT_FOR_POSITIONING.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Alert"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] bArr = new byte[7];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mNoMoveDurationThrs, 2);
        bArr[0] = intToByteArr[0];
        bArr[1] = intToByteArr[1];
        intToByteArr = CaConvertUtil.intToByteArr(this.mMoveDurationThrs, 2);
        bArr[2] = intToByteArr[0];
        bArr[3] = intToByteArr[1];
        intToByteArr = CaConvertUtil.intToByteArr(this.mMoveMinDurationThrs, 2);
        bArr[4] = intToByteArr[0];
        bArr[5] = intToByteArr[1];
        bArr[6] = (byte) ((int) this.mMoveDistanceThrs);
        return bArr;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return (byte) 9;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 9) {
            this.mNoMoveDurationThrs = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Enter Threshold = " + Integer.toString(this.mNoMoveDurationThrs));
            return true;
        } else if (i == 10) {
            this.mMoveDurationThrs = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Exit Distance Threshold = " + Integer.toString(this.mMoveDurationThrs));
            return true;
        } else if (i == 11) {
            this.mMoveMinDurationThrs = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Exit Time Threshold = " + Integer.toString(this.mMoveMinDurationThrs));
            return true;
        } else if (i != 12) {
            return false;
        } else {
            this.mMoveDistanceThrs = ((Double) ((ContextAwarePropertyBundle) e).getValue()).doubleValue();
            CaLogger.info("Movement Threshold = " + Double.toString(this.mMoveDistanceThrs));
            return true;
        }
    }
}
