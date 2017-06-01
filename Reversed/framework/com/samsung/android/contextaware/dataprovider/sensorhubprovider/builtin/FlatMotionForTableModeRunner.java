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
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class FlatMotionForTableModeRunner extends LibTypeProvider {
    private static final int DEFAULT_DURATION = 500;
    private int mDuration = 500;

    public FlatMotionForTableModeRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_FLAT_MOTION_FOR_TABLE_MODE.getCode();
    }

    protected byte[] getDataPacketToRegisterLib() {
        r1 = new byte[2];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mDuration, 2);
        r1[0] = intToByteArr[0];
        r1[1] = intToByteArr[1];
        return r1;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_FLAT_MOTION_FOR_TABLE_MODE_SERVICE;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public <E> boolean setPropertyValue(int i, E e) {
        boolean z = true;
        if (i == 61) {
            CaLogger.info("Duration = " + Integer.toString(this.mDuration) + "-1");
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            if (this.mDuration <= 0) {
                CaLogger.warning("duration must be above 0.");
                return false;
            }
            CaLogger.info("Duration = " + Integer.toString(this.mDuration) + "0");
            this.mDuration = intValue;
            CaLogger.info("Duration = " + Integer.toString(this.mDuration));
        } else {
            z = false;
            CaLogger.info("Duration = " + Integer.toString(this.mDuration) + "1");
        }
        CaLogger.info("Duration = " + Integer.toString(this.mDuration) + "2");
        return z;
    }
}
