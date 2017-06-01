package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.content.smartclip.SemSmartClipMetaTagType;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;

public class ExerciseRunner extends LibTypeProvider {
    private static final byte DATA_TYPE_BATCH = (byte) 0;
    private static final byte DATA_TYPE_GPS_STATUS = (byte) 1;
    private Context mContext = null;
    private boolean mLastGpsEnabled = false;
    private final BroadcastReceiver mReceiver = new C10831();
    private int mSensorType = 0;
    private double mTotalPedoDistance = 0.0d;
    private long mTotalStepCount = 0;
    private long startTimeStamp = 0;

    class C10831 extends BroadcastReceiver {
        C10831() {
        }

        public final void onReceive(Context context, Intent intent) {
            int i = 1;
            if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
                boolean -wrap0 = ExerciseRunner.this.isGpsEnabled();
                if (-wrap0 != ExerciseRunner.this.mLastGpsEnabled) {
                    ExerciseRunner exerciseRunner = ExerciseRunner.this;
                    byte[] bArr = new byte[1];
                    if (!-wrap0) {
                        i = 0;
                    }
                    bArr[0] = (byte) i;
                    exerciseRunner.sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE, (byte) 37, bArr);
                    ExerciseRunner.this.mLastGpsEnabled = -wrap0;
                }
            }
        }
    }

    private enum ContextName {
        TimeStamp((byte) 0),
        DataCount((byte) 1),
        Latitude((byte) 2),
        Longitude((byte) 3),
        Altitude((byte) 4),
        Pressure((byte) 5),
        StepCountDiff((byte) 6),
        PedoDistanceDiff((byte) 7),
        PedoSpeed((byte) 8),
        Speed((byte) 9),
        GpsStatus((byte) 10);
        
        private byte val;

        private ContextName(byte b) {
            this.val = b;
        }
    }

    public ExerciseRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
        this.mContext = context;
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) this.mContext.getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
        return locationManager != null ? locationManager.isProviderEnabled("gps") : false;
    }

    public final void clear() {
        CaLogger.trace();
        this.mTotalStepCount = 0;
        this.mTotalPedoDistance = 0.0d;
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        this.mContext.unregisterReceiver(this.mReceiver);
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        this.mLastGpsEnabled = isGpsEnabled();
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        this.startTimeStamp = System.currentTimeMillis();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_EXERCISE.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"TimeStampArray", "DataCount", "LatitudeArray", "LongitudeArray", "AltitudeArray", "PressureArray", "StepCountDiffArray", "PedoDistanceDiffArray", "PedoSpeedArray", "SpeedArray", "GpsStatus", "TotalStepCount", "TotalPedoDistance"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return CaConvertUtil.intToByteArr(this.mSensorType, 1);
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE;
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

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        String[] contextValueNames = getContextValueNames();
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        int i3;
        if (b == (byte) 0) {
            if ((bArr.length - i2) - 6 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            r31 = new byte[8];
            i3 = i2 + 1;
            r31[4] = bArr[i2];
            i2 = i3 + 1;
            r31[5] = bArr[i3];
            i3 = i2 + 1;
            r31[6] = bArr[i2];
            i2 = i3 + 1;
            r31[7] = bArr[i3];
            long j = ByteBuffer.wrap(r31).getLong() + (this.startTimeStamp + 1000);
            r31 = new byte[4];
            i3 = i2 + 1;
            r31[2] = bArr[i2];
            i2 = i3 + 1;
            r31[3] = bArr[i3];
            int i4 = ByteBuffer.wrap(r31).getInt();
            if (i4 <= 0) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                return -1;
            } else if ((bArr.length - i2) - (i4 * 21) < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else {
                long[] jArr = new long[i4];
                double[] dArr = new double[i4];
                double[] dArr2 = new double[i4];
                float[] fArr = new float[i4];
                float[] fArr2 = new float[i4];
                long[] jArr2 = new long[i4];
                double[] dArr3 = new double[i4];
                double[] dArr4 = new double[i4];
                float[] fArr3 = new float[i4];
                int i5 = 0;
                i3 = i2;
                while (i5 < i4) {
                    long j2;
                    long j3;
                    jArr[i5] = ((long) (i5 * 1000)) + j;
                    i2 = i3 + 1;
                    byte b2 = bArr[i3];
                    i3 = i2 + 1;
                    byte b3 = bArr[i2];
                    i2 = i3 + 1;
                    byte b4 = bArr[i3];
                    i3 = i2 + 1;
                    byte b5 = bArr[i2];
                    i2 = i3 + 1;
                    byte b6 = bArr[i3];
                    if ((b2 & 128) == 128) {
                        j2 = ByteBuffer.wrap(new byte[]{(byte) -1, (byte) -1, (byte) -1, b2, b3, b4, b5, b6}).getLong() >> 4;
                    } else {
                        j2 = ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0, (byte) 0, b2, b3, b4, b5, b6}).getLong() >> 4;
                    }
                    dArr[i5] = ((double) j2) / 1.0E8d;
                    i3 = i2 + 1;
                    b2 = bArr[i2];
                    i2 = i3 + 1;
                    b3 = bArr[i3];
                    i3 = i2 + 1;
                    b4 = bArr[i2];
                    i2 = i3 + 1;
                    b5 = bArr[i3];
                    if ((b6 & 8) == 8) {
                        j3 = ByteBuffer.wrap(new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) ((b6 & 15) | 240), b2, b3, b4, b5}).getLong();
                    } else {
                        j3 = ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) (b6 & 15), b2, b3, b4, b5}).getLong();
                    }
                    dArr2[i5] = ((double) j3) / 1.0E8d;
                    r31 = new byte[4];
                    r31[0] = (byte) 0;
                    i3 = i2 + 1;
                    r31[1] = bArr[i2];
                    i2 = i3 + 1;
                    r31[2] = bArr[i3];
                    i3 = i2 + 1;
                    r31[3] = bArr[i2];
                    fArr[i5] = (float) (((double) ByteBuffer.wrap(r31).getInt()) / 100.0d);
                    r31 = new byte[4];
                    r31[0] = (byte) 0;
                    i2 = i3 + 1;
                    r31[1] = bArr[i3];
                    i3 = i2 + 1;
                    r31[2] = bArr[i2];
                    i2 = i3 + 1;
                    r31[3] = bArr[i3];
                    fArr2[i5] = (float) (((double) ByteBuffer.wrap(r31).getInt()) / 1000.0d);
                    i3 = i2 + 1;
                    this.mTotalStepCount += (long) bArr[i2];
                    jArr2[i5] = this.mTotalStepCount;
                    double d = this.mTotalPedoDistance;
                    r31 = new byte[4];
                    i2 = i3 + 1;
                    r31[2] = bArr[i3];
                    i3 = i2 + 1;
                    r31[3] = bArr[i2];
                    this.mTotalPedoDistance = d + (((double) ByteBuffer.wrap(r31).getInt()) / 100.0d);
                    dArr3[i5] = this.mTotalPedoDistance;
                    i2 = i3 + 1;
                    b2 = bArr[i3];
                    i3 = i2 + 1;
                    dArr4[i5] = ((double) (ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0, b2, bArr[i2]}).getInt() >> 4)) / 100.0d;
                    r31 = new byte[4];
                    r31[0] = (byte) 0;
                    r31[1] = (byte) 0;
                    r31[2] = (byte) (b6 & 15);
                    i2 = i3 + 1;
                    r31[3] = bArr[i3];
                    fArr3[i5] = (float) (((double) ByteBuffer.wrap(r31).getInt()) / 100.0d);
                    i5++;
                    i3 = i2;
                }
                super.getContextBean().putContext(contextValueNames[ContextName.TimeStamp.val], jArr);
                super.getContextBean().putContext(contextValueNames[ContextName.DataCount.val], i4);
                super.getContextBean().putContext(contextValueNames[ContextName.Latitude.val], dArr);
                super.getContextBean().putContext(contextValueNames[ContextName.Longitude.val], dArr2);
                super.getContextBean().putContext(contextValueNames[ContextName.Altitude.val], fArr);
                super.getContextBean().putContext(contextValueNames[ContextName.Pressure.val], fArr2);
                super.getContextBean().putContext(contextValueNames[ContextName.StepCountDiff.val], jArr2);
                super.getContextBean().putContext(contextValueNames[ContextName.PedoDistanceDiff.val], dArr3);
                super.getContextBean().putContext(contextValueNames[ContextName.PedoSpeed.val], dArr4);
                super.getContextBean().putContext(contextValueNames[ContextName.Speed.val], fArr3);
                super.notifyObserver();
                i2 = i3;
            }
        } else if (b != (byte) 1) {
            CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
            return -1;
        } else if ((bArr.length - i2) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        } else {
            i3 = i2 + 1;
            super.getContextBean().putContext(contextValueNames[ContextName.GpsStatus.val], (short) bArr[i2]);
            super.notifyObserver();
            i2 = i3;
        }
        return i2;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 63) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Exercise data type = " + Integer.toString(intValue));
            this.mSensorType |= intValue;
            byte[] bArr = new byte[1];
            bArr[0] = (byte) (isGpsEnabled() ? 1 : 0);
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE, (byte) 37, bArr);
            return true;
        } else if (i != 0) {
            return false;
        } else {
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 1, (byte) 0});
            return true;
        }
    }
}
