package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.camera.iris.SemIrisManager;
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
import java.nio.ByteBuffer;

public class AbnormalShockRunner extends LibTypeProvider {
    private static final int DEFAULT_SHOCK_DURATION = 200;
    private static final int DEFAULT_SHOCK_STRENGTH = 3;
    private int mDuration = 200;
    private int mStrength = 3;

    public AbnormalShockRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_ABNORMAL_SHOCK.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"UserStatus"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r1 = new byte[3];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mDuration, 2);
        r1[1] = intToByteArr[0];
        r1[2] = intToByteArr[1];
        return r1;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ABNORMAL_SHOCK_SERVICE;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        CaLogger.info("parse:" + i);
        String str = getContextValueNames()[0];
        if ((bArr.length - i) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        r4 = new byte[4];
        i2 = i + 1;
        r4[2] = bArr[i];
        int i3 = i2 + 1;
        r4[3] = bArr[i2];
        super.getContextBean().putContext(str, ByteBuffer.wrap(r4).getInt());
        super.notifyObserver();
        return i3;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        boolean z = true;
        if (i == 44) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Strength = " + Integer.toString(intValue));
            if (intValue <= 0 || intValue > 5) {
                CaLogger.warning("range error of abnormal shock strength (range : 1~5)");
                return false;
            }
            this.mStrength = intValue;
        } else if (i == 45) {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(intValue2));
            if (intValue2 <= 0 || intValue2 > SemIrisManager.IRIS_ERROR_NEED_TO_RETRY) {
                CaLogger.warning("range error of abnormal shock duration (range : 1~5000)");
                return false;
            }
            this.mDuration = intValue2;
        } else if (i == 46) {
            int intValue3 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("User action = " + Integer.toString(intValue3));
            z = sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_ABNORMAL_SHOCK_SERVICE, (byte) 1, CaConvertUtil.intToByteArr(intValue3, 1));
        } else {
            z = false;
        }
        return z;
    }
}
