package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class ChLocTriggerRunner extends LibTypeProvider {
    public ChLocTriggerRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        CaLogger.info("LTG ChLocTriggerRunner Create");
    }

    public final String getContextType() {
        CaLogger.info("LTG getContextType");
        return ContextType.SENSORHUB_RUNNER_CH_LOC_TRIGGER.getCode();
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected byte getInstLibType() {
        CaLogger.info("LTG getInstLibType");
        return ISensorHubCmdProtocol.TYPE_CH_LOC_TRIGGER;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        CaLogger.info("parse start:" + i);
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        int i3 = i2 + 1;
        byte b2 = bArr[i2];
        CaLogger.info("LTG " + b + " / " + b2);
        super.getContextBean().putContext("result", b);
        super.getContextBean().putContext("property", b2);
        super.notifyObserver();
        CaLogger.info("parse end:" + i3);
        return i3;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 89) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("ChLocTriggerRunner setProperty type : " + intValue);
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_CH_LOC_TRIGGER, (byte) 1, CaConvertUtil.intToByteArr(intValue, 1));
            return true;
        } else if (i == 90) {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("ChLocTriggerRunner setProperty duration : " + intValue2);
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_CH_LOC_TRIGGER, (byte) 2, CaConvertUtil.intToByteArr(intValue2, 1));
            return true;
        } else if (i != 9) {
            return false;
        } else {
            CaLogger.info("ChLocTriggerRunner get current Info");
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[2]);
            return true;
        }
    }
}
