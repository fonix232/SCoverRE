package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ICurrentPositionRequestObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.IPassiveCurrrentPositionObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.CurrentPositionRequestRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.CurrentPositionRequestRunner.Position;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.ICurrentPositionRequest;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.ISensorHubRequestParser;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.CaPassiveCurrentPositionManager;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class LifeLogComponentRunner extends LibTypeProvider implements ICurrentPositionRequestObserver, IPassiveCurrrentPositionObserver {
    private static final int DEFAULT_BATCHING_PERIOD = 65535;
    private static final int DEFAULT_STOP_PERIOD = 300;
    private static final int DEFAULT_WAIT_PERIOD = 1500;
    private final ICurrentPositionRequest mCurrentPositionRequest;
    protected final CaPassiveCurrentPositionManager mPassiveCurrentPosition;
    private int mStopPeriod = 300;
    private int mWaitPeriod = DEFAULT_WAIT_PERIOD;

    public LifeLogComponentRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        this.mPassiveCurrentPosition = new CaPassiveCurrentPositionManager(context, looper, this);
        this.mCurrentPositionRequest = new CurrentPositionRequestRunner(getContext(), getLooper());
        this.mCurrentPositionRequest.registerObserver(this);
        addRequestParser((ISensorHubRequestParser) this.mCurrentPositionRequest);
    }

    private boolean checkMovingPacket(byte[] bArr, int i) {
        return i + 5 <= bArr.length;
    }

    private boolean checkStayingAreaPacket(byte[] bArr, int i) {
        return i + 23 <= bArr.length;
    }

    private int parseForMoving(byte[] bArr, int i, long j, long j2) {
        int i2 = i;
        if ((bArr.length - i) - 4 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        int i3 = i2 + 1;
        i2 = i3 + 1;
        i3 = i2 + 1;
        long timeStampForUTC = CaTimeManager.getInstance().getTimeStampForUTC(j, j2, (long) (((((bArr[i] & 255) << 24) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 8)) + (bArr[i2] & 255)));
        if ((bArr.length - i3) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i3 + 1;
        byte b = bArr[i3];
        if (b <= (byte) 0) {
            return i2;
        }
        int[] iArr = new int[b];
        int[] iArr2 = new int[b];
        int[] iArr3 = new int[b];
        byte b2 = (byte) 0;
        i3 = i2;
        while (b2 < b) {
            if (!checkMovingPacket(bArr, i3) || b2 > b - 1) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            i2 = i3 + 1;
            iArr[b2] = bArr[i3];
            i3 = i2 + 1;
            iArr2[b2] = bArr[i2];
            i2 = i3 + 1;
            i3 = i2 + 1;
            i2 = i3 + 1;
            iArr3[b2] = (((bArr[i3] & 255) << 16) + ((bArr[i2] & 255) << 8)) + (bArr[i3] & 255);
            b2++;
            i3 = i2;
        }
        String[] contextValueNames = getContextValueNames();
        getContextBean().putContext(contextValueNames[8], b);
        getContextBean().putContext(contextValueNames[9], timeStampForUTC);
        getContextBean().putContext(contextValueNames[10], iArr);
        getContextBean().putContext(contextValueNames[11], iArr2);
        getContextBean().putContext(contextValueNames[12], iArr3);
        return i3;
    }

    private int parseForStayingArea(byte[] bArr, int i, long j, long j2) {
        int i2 = i;
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        if (b <= (byte) 0) {
            return i2;
        }
        long[] jArr = new long[b];
        double[] dArr = new double[b];
        double[] dArr2 = new double[b];
        double[] dArr3 = new double[b];
        int[] iArr = new int[b];
        int[] iArr2 = new int[b];
        int[] iArr3 = new int[b];
        byte b2 = (byte) 0;
        int i3 = i2;
        while (b2 < b) {
            if (checkStayingAreaPacket(bArr, i3) || b2 < b - 1) {
                i2 = i3 + 1;
                i3 = i2 + 1;
                i2 = i3 + 1;
                i3 = i2 + 1;
                jArr[b2] = CaTimeManager.getInstance().getTimeStampForUTC(j, j2, (long) (((((bArr[i3] & 255) << 24) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 8)) + (bArr[i2] & 255)));
                i2 = i3 + 1;
                i3 = i2 + 1;
                i2 = i3 + 1;
                i3 = i2 + 1;
                dArr[b2] = ((double) (((((bArr[i3] & 255) << 24) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 8)) + (bArr[i2] & 255))) / 1000000.0d;
                i2 = i3 + 1;
                i3 = i2 + 1;
                i2 = i3 + 1;
                i3 = i2 + 1;
                dArr2[b2] = ((double) (((((bArr[i3] & 255) << 24) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 8)) + (bArr[i2] & 255))) / 1000000.0d;
                i2 = i3 + 1;
                i3 = i2 + 1;
                i2 = i3 + 1;
                i3 = i2 + 1;
                dArr3[b2] = ((double) (((((bArr[i3] & 255) << 24) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 8)) + (bArr[i2] & 255))) / 1000.0d;
                i2 = i3 + 1;
                i3 = i2 + 1;
                i2 = i3 + 1;
                i3 = i2 + 1;
                iArr[b2] = ((((bArr[i3] & 255) << 24) + ((bArr[i2] & 255) << 16)) + ((bArr[i3] & 255) << 8)) + (bArr[i2] & 255);
                i2 = i3 + 1;
                i3 = i2 + 1;
                iArr2[b2] = ((bArr[i3] & 255) << 8) + (bArr[i2] & 255);
                i2 = i3 + 1;
                iArr3[b2] = bArr[i3];
                b2++;
                i3 = i2;
            } else {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
        }
        String[] contextValueNames = getContextValueNames();
        getContextBean().putContext(contextValueNames[0], b);
        getContextBean().putContext(contextValueNames[1], jArr);
        getContextBean().putContext(contextValueNames[2], dArr);
        getContextBean().putContext(contextValueNames[3], dArr2);
        getContextBean().putContext(contextValueNames[4], dArr3);
        getContextBean().putContext(contextValueNames[5], iArr);
        getContextBean().putContext(contextValueNames[6], iArr2);
        getContextBean().putContext(contextValueNames[7], iArr3);
        return i3;
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        this.mPassiveCurrentPosition.disable();
        super.disable();
    }

    protected void display() {
    }

    public final void enable() {
        CaLogger.trace();
        this.mPassiveCurrentPosition.enable();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"StayingAreaCount", "StayingAreaTimeStamp", "StayingAreaLatitude", "StayingAreaLongitude", "StayingAreaAltitude", "StayingAreaTimeDuration", "StayingAreaRadius", "StayingAreaStatus", "MovingCount", "MovingTimeStamp", "MovingActivity", "MovingAccuracy", "MovingTimeDuration"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r1 = new byte[9];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mStopPeriod, 2);
        r1[0] = intToByteArr[0];
        r1[1] = intToByteArr[1];
        intToByteArr = CaConvertUtil.intToByteArr(this.mWaitPeriod, 2);
        r1[2] = intToByteArr[0];
        r1[3] = intToByteArr[1];
        intToByteArr = CaConvertUtil.intToByteArr(65535, 2);
        r1[4] = intToByteArr[0];
        r1[5] = intToByteArr[1];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        r1[6] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        r1[7] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        r1[8] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return r1;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE;
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

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        Calendar instance = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int i3 = instance.get(11);
        int i4 = instance.get(12);
        long j = (long) (((((i3 * 3600) + (i4 * 60)) + instance.get(13)) * 1000) + instance.get(14));
        long timeInMillis = instance.getTimeInMillis();
        CaLogger.info("parse start:" + i);
        i2 = parseForStayingArea(bArr, i, j, timeInMillis);
        if (i2 <= 0) {
            return i2;
        }
        i2 = parseForMoving(bArr, i2, j, timeInMillis);
        if (i2 <= 0) {
            return i2;
        }
        super.notifyObserver();
        CaLogger.info("parse end:" + i2);
        return i2;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 28) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("StopPeriod = " + Integer.toString(intValue));
            this.mStopPeriod = intValue;
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 1, CaConvertUtil.intToByteArr(intValue, 2));
        } else if (i == 29) {
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("WaitPeriod = " + Integer.toString(intValue2));
            this.mWaitPeriod = intValue2;
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(intValue2, 2));
        } else if (i == 30) {
            int intValue3 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("StayingRadius = " + Integer.toString(intValue3));
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 3, CaConvertUtil.intToByteArr(intValue3, 2));
        } else if (i != 31) {
            return false;
        } else {
            int intValue4 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("StayingAreaRadius = " + Integer.toString(intValue4));
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 4, CaConvertUtil.intToByteArr(intValue4, 2));
        }
    }

    public void updatePassiveCurrentPosition(int i, int[] iArr, double d, double d2, double d3, double d4, float f, float f2, int i2) {
        if (!isDisable()) {
            CaLogger.debug("send the passive current position to SensorHub");
            int sendPositionToSensorHub = CaPassiveCurrentPositionManager.sendPositionToSensorHub(i, iArr, d, d2, d3, d4, f, f2, i2);
            if (sendPositionToSensorHub != SensorHubErrors.SUCCESS.getCode()) {
                CaLogger.error(SensorHubErrors.getMessage(sendPositionToSensorHub));
            }
        }
    }

    public final void updatePosition(Position position) {
        if (!isDisable()) {
            if (position == null) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_CURRENT_POSITION_NULL_EXCEPTION.getCode()));
                return;
            }
            Position position2 = position;
            int latitude = (int) (position.getLatitude() * 1000000.0d);
            int longitude = (int) (position.getLongitude() * 1000000.0d);
            int altitude = (int) (position.getAltitude() * 1000.0d);
            int accuracy = (int) position.getAccuracy();
            int[] utcTime = position.getUtcTime();
            int satelliteCount = position.getSatelliteCount();
            int speed = (int) (position.getSpeed() * 100.0f);
            int distance = (int) (position.getDistance() * 1000.0d);
            int type = position.getType();
            byte[] bArr = new byte[22];
            System.arraycopy(CaConvertUtil.intToByteArr(latitude, 4), 0, bArr, 0, 4);
            System.arraycopy(CaConvertUtil.intToByteArr(longitude, 4), 0, bArr, 4, 4);
            int i = 4 + 4;
            System.arraycopy(CaConvertUtil.intToByteArr(altitude, 4), 0, bArr, i, 4);
            i += 4;
            System.arraycopy(CaConvertUtil.intToByteArr(accuracy, 1), 0, bArr, i, 1);
            i++;
            System.arraycopy(CaConvertUtil.intToByteArr(utcTime[0], 1), 0, bArr, i, 1);
            i++;
            System.arraycopy(CaConvertUtil.intToByteArr(utcTime[1], 1), 0, bArr, i, 1);
            i++;
            System.arraycopy(CaConvertUtil.intToByteArr(utcTime[2], 1), 0, bArr, i, 1);
            i++;
            System.arraycopy(CaConvertUtil.intToByteArr(satelliteCount, 1), 0, bArr, i, 1);
            i++;
            System.arraycopy(CaConvertUtil.intToByteArr(speed, 2), 0, bArr, i, 2);
            i += 2;
            System.arraycopy(CaConvertUtil.intToByteArr(distance, 2), 0, bArr, i, 2);
            System.arraycopy(CaConvertUtil.intToByteArr(type, 1), 0, bArr, i + 2, 1);
            sendCommonValueToSensorHub((byte) 22, bArr);
        }
    }
}
