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

public class SpecificPoseAlertRunner extends LibTypeProvider {
    private static final int DEFAULT_MAXIMUM_ANGLE = 90;
    private static final int DEFAULT_MINIMUM_ANGLE = -90;
    private static final int DEFAULT_MOVING_THRS = 1;
    private static final int DEFAULT_RETENTION_TIME = 1;
    private int mMaximumAngle = 90;
    private int mMinimumAngle = DEFAULT_MINIMUM_ANGLE;
    private int mMovingThrs = 1;
    private int mRetentionTime = 1;

    public SpecificPoseAlertRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_SPECIFIC_POSE_ALERT.getCode();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r1 = new byte[6];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mRetentionTime, 2);
        byte[] intToByteArr2 = CaConvertUtil.intToByteArr(this.mMovingThrs, 2);
        r1[0] = intToByteArr[0];
        r1[1] = intToByteArr[1];
        r1[2] = (byte) this.mMinimumAngle;
        r1[3] = (byte) this.mMaximumAngle;
        r1[4] = intToByteArr2[0];
        r1[5] = intToByteArr2[1];
        return r1;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_EXPOEASEIN;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 33) {
            this.mRetentionTime = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Retention Time = " + Integer.toString(this.mRetentionTime));
            return true;
        } else if (i == 34) {
            this.mMinimumAngle = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Minimum Angle = " + Integer.toString(this.mMinimumAngle));
            return true;
        } else if (i == 35) {
            this.mMaximumAngle = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Maximum Angle = " + Integer.toString(this.mMaximumAngle));
            return true;
        } else if (i != 36) {
            return false;
        } else {
            this.mMovingThrs = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Moving Thrs = " + Integer.toString(this.mMovingThrs));
            return true;
        }
    }
}
