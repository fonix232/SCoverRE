package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.VoiceLibProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class WakeUpVoiceRunner extends VoiceLibProvider {
    private static final int DEFAULT_WAKE_UP_VOICE_MODE = 1;
    private int mMode = 1;

    public WakeUpVoiceRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
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
        return ContextType.SENSORHUB_RUNNER_WAKE_UP_VOICE.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Action"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] bArr = new byte[2];
        bArr[0] = (byte) this.mMode;
        return bArr;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return (byte) 1;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] contextValueNames = getContextValueNames();
        i2 = i + 1;
        super.getContextBean().putContext(contextValueNames[0], bArr[i]);
        super.notifyObserver();
        return i2;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        boolean z = true;
        String str;
        byte[] stringToByteArray;
        if (i == 16) {
            str = (String) ((ContextAwarePropertyBundle) e).getValue();
            if (str == null || str.isEmpty()) {
                CaLogger.error("value is null");
                return false;
            }
            stringToByteArray = CaConvertUtil.stringToByteArray(str);
            if (stringToByteArray == null || stringToByteArray.length <= 0) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode()));
                return false;
            }
            CaLogger.info("AM = " + str);
            z = sendPropertyValueToSensorHub((byte) 1, (byte) 1, stringToByteArray);
        } else if (i == 18) {
            str = (String) ((ContextAwarePropertyBundle) e).getValue();
            if (str == null || str.isEmpty()) {
                CaLogger.error("value is null");
                return false;
            }
            stringToByteArray = CaConvertUtil.stringToByteArray(str);
            if (stringToByteArray == null || stringToByteArray.length <= 0) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode()));
                return false;
            }
            CaLogger.info("GRAMMER = " + str);
            z = sendPropertyValueToSensorHub((byte) 1, (byte) 2, stringToByteArray);
        } else if (i == 53) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Mode = " + Integer.toString(this.mMode));
            this.mMode = intValue;
        } else {
            z = false;
        }
        return z;
    }
}
