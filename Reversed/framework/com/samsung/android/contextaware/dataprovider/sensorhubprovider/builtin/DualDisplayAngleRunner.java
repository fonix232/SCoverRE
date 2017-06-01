package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
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

public class DualDisplayAngleRunner extends LibTypeProvider {
    private int mOffAngle = 0;
    private int mOnAngle = 0;

    public DualDisplayAngleRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_DUAL_DISPLAY_ANGLE.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Angle", "Type", "Intensity"};
    }

    protected byte[] getDataPacketToRegisterLib() {
        r2 = new byte[4];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mOnAngle, 2);
        byte[] intToByteArr2 = CaConvertUtil.intToByteArr(this.mOffAngle, 2);
        r2[0] = intToByteArr[0];
        r2[1] = intToByteArr[1];
        r2[2] = intToByteArr2[0];
        r2[3] = intToByteArr2[1];
        return r2;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_DUAL_DISPLAY_ANGLE_SERVICE;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] contextValueNames = getContextValueNames();
        i2 = i + 1;
        int i3 = i2 + 1;
        short s = (short) ((bArr[i] & 255) + ((bArr[i2] & 255) << 8));
        i2 = i3 + 1;
        i3 = i2 + 1;
        short s2 = (short) ((bArr[i3] & 255) + ((bArr[i2] & 255) << 8));
        i2 = i3 + 1;
        i3 = i2 + 1;
        short s3 = (short) ((bArr[i3] & 255) + ((bArr[i2] & 255) << 8));
        super.getContextBean().putContext(contextValueNames[0], s);
        super.getContextBean().putContext(contextValueNames[1], s2);
        super.getContextBean().putContext(contextValueNames[2], s3);
        super.notifyObserver();
        return i3;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 76) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("onAngle = " + Integer.toString(intValue));
            this.mOnAngle = intValue;
            return true;
        } else if (i != 77) {
            return false;
        } else {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("offAngle = " + Integer.toString(intValue2));
            this.mOffAngle = intValue2;
            return true;
        }
    }
}
