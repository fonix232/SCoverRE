package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextBean;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaAutoBrightnessTableManager;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;

public class AutoBrightnessRunner extends LibTypeProvider {
    byte mDeviceMode = (byte) 0;

    public AutoBrightnessRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_AUTO_BRIGHTNESS.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Candela", "AmbientLux"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{this.mDeviceMode};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return (byte) 48;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        String[] contextValueNames = getContextValueNames();
        if ((bArr.length - i) - 8 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        ContextBean contextBean = super.getContextBean();
        String str = contextValueNames[0];
        r5 = new byte[4];
        i2 = i + 1;
        r5[0] = bArr[i];
        int i3 = i2 + 1;
        r5[1] = bArr[i2];
        i2 = i3 + 1;
        r5[2] = bArr[i3];
        i3 = i2 + 1;
        r5[3] = bArr[i2];
        contextBean.putContext(str, ByteBuffer.wrap(r5).getInt());
        contextBean = super.getContextBean();
        str = contextValueNames[1];
        r5 = new byte[4];
        i2 = i3 + 1;
        r5[0] = bArr[i3];
        i3 = i2 + 1;
        r5[1] = bArr[i2];
        i2 = i3 + 1;
        r5[2] = bArr[i3];
        i3 = i2 + 1;
        r5[3] = bArr[i2];
        contextBean.putContext(str, ByteBuffer.wrap(r5).getInt());
        super.notifyObserver();
        return i3;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        boolean z = true;
        if (i == 64) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Mode = " + Integer.toString(intValue));
            if (intValue == 0 || intValue == 1 || intValue == 2) {
                this.mDeviceMode = (byte) intValue;
                sendPropertyValueToSensorHub((byte) 23, (byte) 48, (byte) 1, CaConvertUtil.intToByteArr(intValue, 1));
            } else {
                CaLogger.warning("invalid value for mode");
                return false;
            }
        } else if (i == 65) {
            String str = (String) ((ContextAwarePropertyBundle) e).getValue();
            if (str == null || str.isEmpty()) {
                CaLogger.error("value is null");
                return false;
            }
            CaLogger.info("CFG = " + str);
            byte[] stringToByteArray = CaConvertUtil.stringToByteArray(str);
            if (stringToByteArray == null || stringToByteArray.length <= 0) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode()));
                return false;
            }
            CaAutoBrightnessTableManager.getInstance().setOffsetTable(stringToByteArray);
            CaAutoBrightnessTableManager.getInstance().sendAutoBrightnessTableToSensorHub();
        } else {
            z = false;
        }
        return z;
    }
}
