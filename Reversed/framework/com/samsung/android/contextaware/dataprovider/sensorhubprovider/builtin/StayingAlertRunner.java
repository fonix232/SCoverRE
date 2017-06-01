package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ICurrrentLocationObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentLocationManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;

public class StayingAlertRunner extends LibTypeProvider implements ICurrrentLocationObserver {
    private static final byte ASK_CURRENT_LOCATION = (byte) 2;
    private double mCurAltitude;
    private double mCurLatitude;
    private final CaCurrentLocationManager mCurLocationManager;
    private double mCurLongitude;
    private int mStopPeriod = 30;
    private int mWaitPeriod = 60;

    public StayingAlertRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        this.mCurLocationManager = new CaCurrentLocationManager(context, looper, this);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
        this.mCurLocationManager.unregisterCurrentLocationObserver();
    }

    protected void display() {
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
        this.mCurLocationManager.registerCurrentLocationObserver(this);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_STAYING_ALERT.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Action", "CurLatitude", "CurLongitude", "CurAltitude"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) this.mStopPeriod, (byte) this.mWaitPeriod};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_EXPOEASEINOUT;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        String[] contextValueNames = getContextValueNames();
        CaLogger.info("parse start:" + i);
        if (contextValueNames == null || contextValueNames.length <= 0 || (bArr.length - i) - 1 < 0) {
            return -1;
        }
        i2 = i + 1;
        int i3 = bArr[i];
        if (i3 == (byte) 2) {
            this.mCurLocationManager.enable();
        } else {
            super.getContextBean().putContext(contextValueNames[0], i3);
            super.getContextBean().putContext(contextValueNames[1], this.mCurLatitude);
            super.getContextBean().putContext(contextValueNames[2], this.mCurLongitude);
            super.getContextBean().putContext(contextValueNames[3], this.mCurAltitude);
            super.notifyObserver();
        }
        CaLogger.info("parse end:" + i2);
        return i2;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 23) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            this.mStopPeriod = intValue;
            CaLogger.info("Stop Period = " + Integer.toString(intValue));
            return sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_EXPOEASEINOUT, (byte) 1, CaConvertUtil.intToByteArr(intValue, 1));
        } else if (i != 24) {
            return false;
        } else {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            this.mWaitPeriod = intValue2;
            CaLogger.info("Wait Period = " + Integer.toString(intValue2));
            return sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_EXPOEASEINOUT, (byte) 2, CaConvertUtil.intToByteArr(intValue2, 1));
        }
    }

    public final void updateCurrentLocation(long j, long j2, double d, double d2, double d3) {
        this.mCurLatitude = d;
        this.mCurLongitude = d2;
        this.mCurAltitude = d3;
        byte[] intToByteArr = CaConvertUtil.intToByteArr((int) (1000000.0d * d), 4);
        byte[] intToByteArr2 = CaConvertUtil.intToByteArr((int) (1000000.0d * d2), 4);
        byte[] bArr = new byte[8];
        System.arraycopy(intToByteArr, 0, bArr, 0, 4);
        System.arraycopy(intToByteArr2, 0, bArr, 4, 4);
        sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_EXPOEASEINOUT, (byte) 3, bArr);
    }
}
