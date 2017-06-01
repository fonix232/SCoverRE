package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class SleepMonitorRunner extends LibTypeProvider {
    private static final int DEFAULT_SAMPLING_INTERVAL = 100;
    private static final int DEFAULT_SENSIBILITY = 80;
    private int mSamplingInterval = 100;
    private int mSensibility = 80;

    public SleepMonitorRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_SLEEP_MONITOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"SleepStatus", "PIM", "ZCM", "Stage", "Wrist", "Flag"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r0 = new byte[8];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mSensibility, 4);
        r0[0] = intToByteArr[0];
        r0[1] = intToByteArr[1];
        r0[2] = intToByteArr[2];
        r0[3] = intToByteArr[3];
        byte[] intToByteArr2 = CaConvertUtil.intToByteArr(this.mSamplingInterval, 4);
        r0[4] = intToByteArr2[0];
        r0[5] = intToByteArr2[1];
        r0[6] = intToByteArr2[2];
        r0[7] = intToByteArr2[3];
        return r0;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return (byte) 37;
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
        i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = bArr[i] + ((bArr[i2] & 255) << 8);
        if (i4 <= 0 || i4 % 17 != 0) {
            CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
            return -1;
        }
        int i5 = i4 / 17;
        String[] contextValueNames = getContextValueNames();
        int[] iArr = new int[i5];
        float[] fArr = new float[i5];
        int[] iArr2 = new int[i5];
        int[] iArr3 = new int[i5];
        int[] iArr4 = new int[i5];
        int[] iArr5 = new int[i5];
        int i6 = 0;
        while (i6 < i5) {
            i2 = i3 + 1;
            iArr[i6] = bArr[i3];
            i3 = i2 + 1;
            i2 = i3 + 1;
            i3 = i2 + 1;
            i2 = i3 + 1;
            fArr[i6] = ((float) ((((bArr[i2] & 255) + ((bArr[i3] & 255) << 8)) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 24))) / 10.0f;
            i3 = i2 + 1;
            i2 = i3 + 1;
            i3 = i2 + 1;
            i2 = i3 + 1;
            iArr2[i6] = (((bArr[i2] & 255) + ((bArr[i3] & 255) << 8)) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 24);
            i3 = i2 + 1;
            i2 = i3 + 1;
            i3 = i2 + 1;
            i2 = i3 + 1;
            iArr3[i6] = (((bArr[i2] & 255) + ((bArr[i3] & 255) << 8)) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 24);
            i3 = i2 + 1;
            i2 = i3 + 1;
            iArr4[i6] = (bArr[i2] & 255) + ((bArr[i3] & 255) << 8);
            i3 = i2 + 1;
            i2 = i3 + 1;
            iArr5[i6] = (bArr[i2] & 255) + ((bArr[i3] & 255) << 8);
            CaLogger.info("status[" + i6 + "] = " + iArr[i6] + ", pim[" + i6 + "] = " + fArr[i6] + ", zcm[" + i6 + " ] = " + iArr2[i6] + ", stage[" + i6 + " ] = " + iArr3[i6] + ", wrist[" + i6 + " ] = " + iArr4[i6] + ", flag[" + i6 + " ] = " + iArr5[i6]);
            i6++;
            i3 = i2;
        }
        super.getContextBean().putContext(contextValueNames[0], iArr);
        super.getContextBean().putContext(contextValueNames[1], fArr);
        super.getContextBean().putContext(contextValueNames[2], iArr2);
        super.getContextBean().putContext(contextValueNames[3], iArr3);
        super.getContextBean().putContext(contextValueNames[4], iArr4);
        super.getContextBean().putContext(contextValueNames[5], iArr5);
        super.notifyObserver();
        return i3;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        boolean z = true;
        if (i == 42) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            if (this.mSensibility <= 0) {
                CaLogger.warning("sensibility must be above 0.");
                return false;
            }
            this.mSensibility = intValue;
            CaLogger.info("Sensibility = " + Integer.toString(this.mSensibility));
        } else if (i == 43) {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            if (intValue2 <= 0) {
                CaLogger.warning("sampling interval must be above 0.");
                return false;
            }
            this.mSamplingInterval = intValue2;
            CaLogger.info("Sampling interval = " + Integer.toString(this.mSamplingInterval));
        } else {
            z = false;
        }
        return z;
    }
}
