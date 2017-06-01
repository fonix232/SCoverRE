package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;

public class TemperatureAlertRunner extends LibTypeProvider {
    private static final int DEFAULT_HIGH_TEMPERATURE = 127;
    private static final boolean DEFAULT_IS_INCLUDING = true;
    private static final int DEFAULT_LOW_TEMPERATURE = 70;
    private int mHighTemperature = 127;
    private boolean mIsIncluding = true;
    private int mLowTemperature = 70;

    public TemperatureAlertRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_TEMPERATURE_ALERT.getCode();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] bArr = new byte[3];
        bArr[0] = (byte) this.mLowTemperature;
        bArr[1] = (byte) this.mHighTemperature;
        if (this.mIsIncluding) {
            bArr[2] = (byte) 1;
        } else {
            bArr[2] = (byte) 0;
        }
        return bArr;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEINOUT;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 20) {
            this.mLowTemperature = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Low Temperature = " + Integer.toString(this.mLowTemperature));
            return true;
        } else if (i == 21) {
            this.mHighTemperature = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("High Temperature = " + Integer.toString(this.mHighTemperature));
            return true;
        } else if (i != 22) {
            return false;
        } else {
            this.mIsIncluding = ((boolean[]) ((ContextAwarePropertyBundle) e).getValue())[0];
            CaLogger.info("Is Including= " + Boolean.toString(this.mIsIncluding));
            return true;
        }
    }
}
