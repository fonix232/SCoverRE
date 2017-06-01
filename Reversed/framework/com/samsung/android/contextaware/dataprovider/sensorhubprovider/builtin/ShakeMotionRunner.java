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
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;

public class ShakeMotionRunner extends LibTypeProvider {
    private static final int DEFAULT_SHAKE_DURATION = 800;
    private static final int DEFAULT_SHAKE_STRENGTH = 2;
    private int mShakeDuration = DEFAULT_SHAKE_DURATION;
    private int mShakeStrength = 2;

    public ShakeMotionRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_SHAKE_MOTION.getCode();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r1 = new byte[3];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mShakeDuration, 2);
        r1[1] = intToByteArr[0];
        r1[2] = intToByteArr[1];
        return r1;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEIN;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        boolean z = true;
        if (i == 14) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Strength = " + Integer.toString(this.mShakeStrength));
            if (intValue <= 0 || intValue > 5) {
                CaLogger.warning("range error of shake strength (range : 1~5)");
                return false;
            }
            this.mShakeStrength = intValue;
        } else if (i == 15) {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(this.mShakeDuration));
            if (intValue2 <= 0 || intValue2 > 5000) {
                CaLogger.warning("range error of shake duration (range : 1~5000)");
                return false;
            }
            this.mShakeDuration = intValue2;
        } else {
            z = false;
        }
        return z;
    }
}
