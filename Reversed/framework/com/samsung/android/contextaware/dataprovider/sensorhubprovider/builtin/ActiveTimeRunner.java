package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;

public class ActiveTimeRunner extends LibTypeProvider {

    private enum ContextValIndex {
        DataType(0),
        ActiveTime(1);
        
        private int val;

        private ContextValIndex(int i) {
            this.val = i;
        }
    }

    public ActiveTimeRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_ACTIVE_TIME.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"DataType", "ActiveTimeDuration"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r0 = new byte[8];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        r0[5] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        r0[6] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        r0[7] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return r0;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 2};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE;
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
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        if (b != (byte) 2) {
            CaLogger.error(SensorHubErrors.ERROR_TYPE_VALUE.getMessage());
        } else if (bArr.length - i2 < 4) {
            CaLogger.debug("packet len:" + bArr.length + " tmpNext:" + i2);
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        } else {
            r5 = new byte[4];
            int i3 = i2 + 1;
            r5[0] = bArr[i2];
            i2 = i3 + 1;
            r5[1] = bArr[i3];
            i3 = i2 + 1;
            r5[2] = bArr[i2];
            i2 = i3 + 1;
            r5[3] = bArr[i3];
            getContextBean().putContext(contextValueNames[ContextValIndex.ActiveTime.val], ByteBuffer.wrap(r5).getInt());
            getContextBean().putContext(contextValueNames[ContextValIndex.DataType.val], b);
            super.notifyObserver();
        }
        return i2;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        return true;
    }
}
