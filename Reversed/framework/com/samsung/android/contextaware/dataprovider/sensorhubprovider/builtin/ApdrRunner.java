package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.location.Location;
import android.os.BaseBundle;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApdrRunner extends LibTypeProvider {
    private static final int DEFAULT_WAKE_UP_STEP_COUNT = 500;
    private static final int DEFAULT_WAKE_UP_TIME_COUNT = 600;
    private static final int STEP_COUNT_HIGH = 50;
    private static final int STEP_COUNT_LOW = 500;
    private static final int STEP_COUNT_MEDIUM = 300;
    private static final int TIME_COUNT_HIGH = 60;
    private static final int TIME_COUNT_LOW = 600;
    private static final int TIME_COUNT_MEDIUM = 120;
    private static final int TIME_SYNC_TIMER = 7200;
    private int mLppResolution = 0;
    private ScheduledExecutorService mSyncSched = Executors.newSingleThreadScheduledExecutor();
    private final Time mSyncTime = new Time();
    private Runnable mSyncTimeTask = null;
    private int mWakeUpStepCount = 500;
    private int mWakeUpTimeCount = 600;

    class C10821 implements Runnable {
        C10821() {
        }

        public void run() {
            ApdrRunner.this.sendCurTimeToSensorHub();
            ApdrRunner.this.mSyncSched = Executors.newSingleThreadScheduledExecutor();
            ApdrRunner.this.mSyncSched.schedule(ApdrRunner.this.mSyncTimeTask, 7200, TimeUnit.SECONDS);
        }
    }

    public enum ContextValIndex {
        Alert(0),
        Count(1),
        Hour(2),
        Minute(3),
        Second(4),
        doe(5),
        TimeDifference(6),
        IncrementEast(7),
        IncrementNorth(8),
        ActivityType(9),
        StayingArea(10);
        
        private int val;

        private ContextValIndex(int i) {
            this.val = i;
        }

        public int index() {
            return this.val;
        }
    }

    public ApdrRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    private void sendCurTimeToSensorHub() {
        Time time = new Time();
        time.setToNow();
        time.switchTimezone("GMT+00:00");
        this.mSyncTime.set(time);
        byte[] bArr = new byte[]{(byte) 0, (byte) 0, (byte) 0};
        bArr[0] = (byte) time.hour;
        bArr[1] = (byte) time.minute;
        bArr[2] = (byte) time.second;
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
        this.mSyncTimeTask = new C10821();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_APDR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Alert", "Count", "Hour", "Minute", "Second", "doe", "TimeDifference", "IncrementEast", "IncrementNorth", "ActivityType", "StayingArea"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) (this.mWakeUpStepCount / 5), (byte) (this.mWakeUpTimeCount / 5)};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_APDR_SERVICE;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final Time getSyncTime() {
        return this.mSyncTime;
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public void gpsAvailable() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 1});
    }

    public final void gpsBatchStarted() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 3});
    }

    public void gpsOffBatchStopped() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 4});
    }

    public void gpsOnBatchStopped() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 5});
    }

    public void gpsUnavailable() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 0});
    }

    public final void locationUpdate(Location location) {
        int latitude = (int) (location.getLatitude() * 1000000.0d);
        int longitude = (int) (location.getLongitude() * 1000000.0d);
        int altitude = (int) (location.getAltitude() * 1000.0d);
        byte accuracy = (byte) ((int) location.getAccuracy());
        Time time = new Time();
        time.set(location.getTime());
        time.switchTimezone("GMT+00:00");
        BaseBundle extras = location.getExtras();
        byte b = (byte) 0;
        if (extras != null) {
            b = (byte) extras.getInt("satellites");
            CaLogger.debug("satellites:" + b);
        }
        int speed = (int) (location.getSpeed() * 100.0f);
        byte b2 = (byte) 0;
        if (location.getProvider() != null) {
            if (location.getProvider().equals("gps")) {
                b2 = (byte) 1;
            } else if (location.getProvider().equals("network")) {
                b2 = (byte) 2;
            } else if (location.getProvider().equals("fused")) {
                b2 = (byte) 3;
            } else if (location.getProvider().equals("GPS batch")) {
                b2 = (byte) 4;
            }
        }
        ByteBuffer allocate = ByteBuffer.allocate(22);
        allocate.put(CaConvertUtil.intToByteArr(latitude, 4));
        allocate.put(CaConvertUtil.intToByteArr(longitude, 4));
        allocate.put(CaConvertUtil.intToByteArr(altitude, 4));
        allocate.put(accuracy);
        allocate.put((byte) time.hour);
        allocate.put((byte) time.minute);
        allocate.put((byte) time.second);
        allocate.put(b);
        allocate.put(CaConvertUtil.intToByteArr(speed, 2));
        allocate.put(CaConvertUtil.intToByteArr(0, 2));
        allocate.put(b2);
        Log.m29d("LPPApdrR", "loc time:" + location.getTime());
        Log.m29d("LPPApdrR", "hr:" + time.hour + " min:" + time.minute + " sec:" + time.second);
        Log.m29d("LPPApdrR", "hr:" + ((byte) time.hour) + " min:" + ((byte) time.minute) + " sec:" + ((byte) time.second));
        sendCommonValueToSensorHub((byte) 22, allocate.array());
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        String[] contextValueNames = getContextValueNames();
        Log.m29d("LppApdr", "parse:" + i);
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        } else if (bArr[i] == (byte) 2) {
            i2 = i + 1;
            super.getContextBean().putContext(contextValueNames[ContextValIndex.StayingArea.val], bArr[i2]);
            super.notifyObserver();
            return i2 + 1;
        } else {
            i2 = i + 1;
            if (bArr[i] != (byte) 1) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                return -1;
            }
            int i3 = i2 + 1;
            byte b = bArr[i2];
            CaLogger.debug("dataSize:" + b);
            if (b <= (byte) 0) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                return -1;
            } else if (bArr.length - i3 < (b * 5) + 4) {
                CaLogger.debug("packet len:" + bArr.length + " tmpNext:" + i3);
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else {
                i2 = i3 + 1;
                byte b2 = bArr[i3];
                i3 = i2 + 1;
                byte b3 = bArr[i2];
                i2 = i3 + 1;
                byte b4 = bArr[i3];
                i3 = i2 + 1;
                byte b5 = bArr[i2];
                long[] jArr = new long[b];
                int[] iArr = new int[b];
                int[] iArr2 = new int[b];
                int[] iArr3 = new int[b];
                byte b6 = (byte) 0;
                while (b6 < b) {
                    CaLogger.debug("packet length:" + bArr.length + "  tmpNext:" + i3);
                    i2 = i3 + 1;
                    i3 = i2 + 1;
                    jArr[b6] = (long) ((((bArr[i3] & 255) << 8) + (bArr[i2] & 255)) * 100);
                    i2 = i3 + 1;
                    iArr[b6] = bArr[i3] * 10;
                    i3 = i2 + 1;
                    iArr2[b6] = bArr[i2] * 10;
                    i2 = i3 + 1;
                    iArr3[b6] = bArr[i3];
                    b6++;
                    i3 = i2;
                }
                super.getContextBean().putContext(contextValueNames[ContextValIndex.Count.val], b);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.Hour.val], b2);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.Minute.val], b3);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.Second.val], b4);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.doe.val], b5);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.TimeDifference.val], jArr);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.IncrementEast.val], iArr);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.IncrementNorth.val], iArr2);
                super.getContextBean().putContext(contextValueNames[ContextValIndex.ActivityType.val], iArr3);
                super.notifyObserver();
                return i3;
            }
        }
    }

    public final void sendSleepModeCmdToSensorHub(byte[] bArr) {
        if (getInstLibType() >= (byte) 0 && bArr != null && bArr.length > 0) {
            byte[] bArr2 = new byte[(bArr.length + 1)];
            byte[] bArr3 = new byte[]{(byte) 1};
            System.arraycopy(bArr3, 0, bArr2, 0, bArr3.length);
            System.arraycopy(bArr, 0, bArr2, bArr3.length, bArr.length);
            super.sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_ADD, getInstLibType(), bArr2);
        }
    }

    public final void setLppResolution(int i) {
        this.mLppResolution = i;
        if (i == 0) {
            this.mWakeUpStepCount = 500;
            this.mWakeUpTimeCount = 600;
        } else if (i == 1) {
            this.mWakeUpStepCount = 300;
            this.mWakeUpTimeCount = 120;
        } else if (i == 2) {
            this.mWakeUpStepCount = 50;
            this.mWakeUpTimeCount = 60;
        }
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 1, CaConvertUtil.intToByteArr(this.mWakeUpStepCount / 5, 1));
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(this.mWakeUpTimeCount / 5, 1));
    }

    public final void setMagneticSensorOffset(int i, int i2, int i3) {
        byte[] intToByteArr = CaConvertUtil.intToByteArr(i, 2);
        byte[] intToByteArr2 = CaConvertUtil.intToByteArr(i2, 2);
        byte[] intToByteArr3 = CaConvertUtil.intToByteArr(i3, 2);
        ByteBuffer allocate = ByteBuffer.allocate(6);
        allocate.put(intToByteArr);
        allocate.put(intToByteArr2);
        allocate.put(intToByteArr3);
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 3, allocate.array());
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i != 32) {
            return false;
        }
        int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
        this.mLppResolution = intValue;
        setLppResolution(intValue);
        return true;
    }
}
