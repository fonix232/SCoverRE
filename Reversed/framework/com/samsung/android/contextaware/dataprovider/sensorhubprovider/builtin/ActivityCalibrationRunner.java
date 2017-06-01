package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class ActivityCalibrationRunner extends LibTypeProvider {
    public ActivityCalibrationRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
        CaLogger.info("ActivityCalibrationRunner is created");
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ACTIVITY_CALIBRATION.getCode();
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ACTIVITY_CALIBRATION_SERVICE;
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

    public final <E> boolean setPropertyValue(int i, E e) {
        byte[] bArr = new byte[2];
        if (i == 92) {
            CaLogger.info("CALIBRATION_SPEED");
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_ACTIVITY_CALIBRATION_SERVICE, (byte) 2, CaConvertUtil.intToByteArr((int) (1000.0f * ((Float) ((ContextAwarePropertyBundle) e).getValue()).floatValue()), 4));
            return true;
        } else if (i != 91) {
            return false;
        } else {
            CaLogger.info("CALIBRATION_CURRENT_STATE");
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_ACTIVITY_CALIBRATION_SERVICE, (byte) 1, CaConvertUtil.stringToByteArray((String) ((ContextAwarePropertyBundle) e).getValue()));
            return true;
        }
    }
}
