package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import java.nio.ByteBuffer;

public class SLMonitorExtendedInterruptRunner extends LibTypeProvider {
    private static final int DEFAULT_DEVICE_TYPE = 1;
    private static final int DEFAULT_STEP_LEVEL_DURATION = 300;
    private static final int DEFAULT_STEP_TYPE = 1;
    private int mDeviceType = 1;
    private int mDuration = DEFAULT_STEP_LEVEL_DURATION;
    private int mNotiCount = 2;

    private enum ContextValIndex {
        InactiveStatus(0),
        IsTimeOut(1),
        Duration(2);
        
        private int val;

        private ContextValIndex(int i) {
            this.val = i;
        }
    }

    public SLMonitorExtendedInterruptRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_SL_MONITOR_EXTENDED_INTERRUPT.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"InactiveStatus", "IsTimeOut", "InactiveTimeDuration"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] bArr = new byte[8];
        bArr[0] = (byte) 3;
        if (this.mDeviceType == 1) {
            bArr[1] = (byte) 5;
        } else if (this.mDeviceType == 2) {
            bArr[1] = (byte) 2;
        }
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mDuration, 2);
        bArr[2] = intToByteArr[0];
        bArr[3] = intToByteArr[1];
        bArr[4] = CaConvertUtil.intToByteArr(this.mNotiCount, 1)[0];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        bArr[5] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        bArr[6] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        bArr[7] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return bArr;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 3};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT50;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        String[] contextValueNames = getContextValueNames();
        CaLogger.info("parse:" + i);
        if (bArr.length - i < 6) {
            CaLogger.debug("packet len:" + bArr.length + " tmpNext:" + i);
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        int i3 = bArr[i];
        int i4 = i2 + 1;
        boolean z = bArr[i2] != (byte) 0;
        r7 = new byte[4];
        i2 = i4 + 1;
        r7[0] = bArr[i4];
        i4 = i2 + 1;
        r7[1] = bArr[i2];
        i2 = i4 + 1;
        r7[2] = bArr[i4];
        i4 = i2 + 1;
        r7[3] = bArr[i2];
        int i5 = ByteBuffer.wrap(r7).getInt();
        getContextBean().putContext(contextValueNames[ContextValIndex.InactiveStatus.val], i3);
        getContextBean().putContext(contextValueNames[ContextValIndex.IsTimeOut.val], z);
        getContextBean().putContext(contextValueNames[ContextValIndex.Duration.val], i5);
        super.notifyObserver();
        return i4;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 59) {
            this.mDuration = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(this.mDuration));
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT50, (byte) 5, CaConvertUtil.intToByteArr(this.mDuration, 2));
            return true;
        } else if (i == 60) {
            this.mNotiCount = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Notification count = " + Integer.toString(this.mNotiCount));
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT50, (byte) 1, CaConvertUtil.intToByteArr(this.mNotiCount, 1));
            return true;
        } else if (i == 57) {
            this.mDeviceType = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Device type = " + Integer.toString(this.mDeviceType));
            return true;
        } else if (i == 55) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Notification start = " + Integer.toString(intValue));
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT50, (byte) 2, CaConvertUtil.intToByteArr(intValue, 2));
            return true;
        } else if (i != 56) {
            return false;
        } else {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Notification end = " + Integer.toString(intValue2));
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT50, (byte) 3, CaConvertUtil.intToByteArr(intValue2, 2));
            return true;
        }
    }
}
