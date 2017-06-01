package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils.PedoHistory;
import com.samsung.android.contextaware.manager.CaUserInfo;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import com.sec.enterprise.knoxcustom.CustomDeviceManagerProxy;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PedometerRunner extends LibTypeProvider {
    private final Lock _mutex = new ReentrantLock(true);
    private double accumulativeCalorie;
    private double accumulativeDistance;
    private long accumulativeRunDownStepCount;
    private long accumulativeRunStepCount;
    private long accumulativeRunUpStepCount;
    private long accumulativeTotalStepCount;
    private long accumulativeUpDownStepCount;
    private long accumulativeWalkDownStepCount;
    private long accumulativeWalkStepCount;
    private long accumulativeWalkUpStepCount;
    private final PedoHistory mPedoHistory = PedoHistory.getInstance();

    public PedometerRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        CaLogger.info("PedometerRunner is created");
    }

    private void sendHistoryStepBuffer() {
        String[] contextValueNames = getContextValueNames();
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        long j5 = 0;
        long j6 = 0;
        long j7 = 0;
        double d = 0.0d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        int bufferSize = this.mPedoHistory.getBufferSize();
        if (bufferSize == 0) {
            CaLogger.warning("History Data Buffer is null!!");
        }
        long[] jArr = new long[bufferSize];
        double[] dArr = new double[bufferSize];
        double[] dArr2 = new double[bufferSize];
        double[] dArr3 = new double[bufferSize];
        long[] jArr2 = new long[bufferSize];
        long[] jArr3 = new long[bufferSize];
        long[] jArr4 = new long[bufferSize];
        long[] jArr5 = new long[bufferSize];
        long[] jArr6 = new long[bufferSize];
        long[] jArr7 = new long[bufferSize];
        long[] jArr8 = new long[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            jArr[i] = this.mPedoHistory.getTimeStampSingle(i);
            jArr8[i] = this.mPedoHistory.getTotalStepCountSingle(i);
            j += jArr8[i];
            jArr2[i] = this.mPedoHistory.getWalkStepCountSingle(i);
            j2 += jArr2[i];
            jArr3[i] = this.mPedoHistory.getRunStepCountSingle(i);
            j3 += jArr3[i];
            jArr4[i] = this.mPedoHistory.getWalkUpStepCountSingle(i);
            j4 += jArr4[i];
            jArr6[i] = this.mPedoHistory.getRunUpStepCountSingle(i);
            j6 += jArr6[i];
            jArr5[i] = this.mPedoHistory.getWalkDnStepCountSingle(i);
            j5 += jArr5[i];
            jArr7[i] = this.mPedoHistory.getRunDnStepCountSingle(i);
            j7 += jArr7[i];
            dArr[i] = this.mPedoHistory.getDistanceArraySingle(i);
            d += dArr[i];
            dArr2[i] = this.mPedoHistory.getCalorieArraySingle(i);
            d2 += dArr2[i];
            dArr3[i] = this.mPedoHistory.getSpeedArraySingle(i);
            d3 += dArr3[i];
        }
        long j8 = ((j4 + j5) + j6) + j7;
        d3 = bufferSize > 0 ? d3 / ((double) bufferSize) : 0.0d;
        getContextBean().putContext(contextValueNames[0], j2);
        getContextBean().putContext(contextValueNames[1], this.accumulativeWalkStepCount);
        getContextBean().putContext(contextValueNames[2], j3);
        getContextBean().putContext(contextValueNames[3], this.accumulativeRunStepCount);
        getContextBean().putContext(contextValueNames[4], j8);
        getContextBean().putContext(contextValueNames[5], this.accumulativeUpDownStepCount);
        getContextBean().putContext(contextValueNames[6], j);
        getContextBean().putContext(contextValueNames[7], this.accumulativeTotalStepCount);
        getContextBean().putContext(contextValueNames[8], d);
        getContextBean().putContext(contextValueNames[9], this.accumulativeDistance);
        getContextBean().putContext(contextValueNames[10], d3);
        getContextBean().putContext(contextValueNames[11], -1);
        getContextBean().putContext(contextValueNames[12], d2);
        getContextBean().putContext(contextValueNames[13], this.accumulativeCalorie);
        getContextBean().putContext(contextValueNames[14], 0.0d);
        getContextBean().putContext(contextValueNames[15], j4);
        getContextBean().putContext(contextValueNames[16], this.accumulativeWalkUpStepCount);
        getContextBean().putContext(contextValueNames[17], j5);
        getContextBean().putContext(contextValueNames[18], this.accumulativeWalkDownStepCount);
        getContextBean().putContext(contextValueNames[19], j6);
        getContextBean().putContext(contextValueNames[20], this.accumulativeRunUpStepCount);
        getContextBean().putContext(contextValueNames[21], j7);
        getContextBean().putContext(contextValueNames[22], this.accumulativeRunDownStepCount);
        getContextBean().putContext(contextValueNames[23], bufferSize);
        getContextBean().putContext(contextValueNames[24], jArr);
        getContextBean().putContext(contextValueNames[25], dArr);
        getContextBean().putContext(contextValueNames[26], dArr2);
        getContextBean().putContext(contextValueNames[27], dArr3);
        getContextBean().putContext(contextValueNames[28], jArr2);
        getContextBean().putContext(contextValueNames[29], jArr3);
        getContextBean().putContext(contextValueNames[30], jArr4);
        getContextBean().putContext(contextValueNames[31], jArr5);
        getContextBean().putContext(contextValueNames[32], jArr6);
        getContextBean().putContext(contextValueNames[33], jArr7);
        getContextBean().putContext(contextValueNames[34], jArr8);
        getContextBean().putContext(contextValueNames[35], 1);
        super.notifyObserver();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this._mutex.lock();
        try {
            this.accumulativeWalkStepCount = 0;
            this.accumulativeWalkUpStepCount = 0;
            this.accumulativeWalkDownStepCount = 0;
            this.accumulativeRunStepCount = 0;
            this.accumulativeRunUpStepCount = 0;
            this.accumulativeRunDownStepCount = 0;
            this.accumulativeUpDownStepCount = 0;
            this.accumulativeTotalStepCount = 0;
            this.accumulativeDistance = 0.0d;
            this.accumulativeCalorie = 0.0d;
            this.mPedoHistory.erase();
        } finally {
            this._mutex.unlock();
        }
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    protected void display() {
        BaseBundle contextBundleForDisplay = getContextBean().getContextBundleForDisplay();
        if (contextBundleForDisplay != null && !contextBundleForDisplay.isEmpty()) {
            CaLogger.debug("================= " + getContextType() + " =================");
            StringBuffer stringBuffer = new StringBuffer();
            for (String str : contextBundleForDisplay.keySet()) {
                if (str == null || str.isEmpty()) {
                    break;
                }
                StringBuffer stringBuffer2 = new StringBuffer();
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) < 'a') {
                        stringBuffer2.append(str.charAt(i));
                    }
                }
                stringBuffer.append(stringBuffer2 + "=[" + getDisplayContents(contextBundleForDisplay, str) + "], ");
            }
            if (stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                stringBuffer.delete(stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), stringBuffer.length());
            }
            CaLogger.info(stringBuffer.toString());
        }
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"WalkStepCountDiff", "WalkStepCount", "RunStepCountDiff", "RunStepCount", "UpDownStepCountDiff", "UpDownStepCount", "TotalStepCountDiff", "TotalStepCount", "DistanceDiff", "Distance", "Speed", "StepStatus", "CalorieDiff", "Calorie", "WalkingFrequency", "WalkUpStepCountDiff", "WalkUpStepCount", "WalkDownStepCountDiff", "WalkDownStepCount", "RunUpStepCountDiff", "RunUpStepCount", "RunDownStepCountDiff", "RunDownStepCount", "LoggingCount", "TimeStampArray", "DistanceDiffArray", "CalorieDiffArray", "SpeedArray", "WalkStepCountDiffArray", "RunStepCountDiffArray", "WalkUpStepCountDiffArray", "WalkDownStepCountDiffArray", "RunUpStepCountDiffArray", "RunDownStepCountDiffArray", "TotalStepCountDiffArray", "PreviousStepBuffer"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) ((int) CaUserInfo.getInstance().getUserHeight()), (byte) ((int) CaUserInfo.getInstance().getUserWeight()), (byte) CaUserInfo.getInstance().getUserGender()};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final Bundle getInitContextBundle() {
        String[] contextValueNames = getContextValueNames();
        BaseBundle bundle = new Bundle();
        bundle.putLong(contextValueNames[0], 0);
        bundle.putLong(contextValueNames[1], 0);
        bundle.putLong(contextValueNames[2], 0);
        bundle.putLong(contextValueNames[3], 0);
        bundle.putLong(contextValueNames[4], 0);
        bundle.putLong(contextValueNames[5], 0);
        bundle.putLong(contextValueNames[6], 0);
        bundle.putLong(contextValueNames[7], 0);
        bundle.putDouble(contextValueNames[8], 0.0d);
        bundle.putDouble(contextValueNames[9], 0.0d);
        bundle.putDouble(contextValueNames[10], 0.0d);
        bundle.putInt(contextValueNames[11], -1);
        bundle.putDouble(contextValueNames[12], 0.0d);
        bundle.putDouble(contextValueNames[13], 0.0d);
        bundle.putDouble(contextValueNames[14], 0.0d);
        bundle.putLong(contextValueNames[15], 0);
        bundle.putLong(contextValueNames[16], 0);
        bundle.putLong(contextValueNames[17], 0);
        bundle.putLong(contextValueNames[18], 0);
        bundle.putLong(contextValueNames[19], 0);
        bundle.putLong(contextValueNames[20], 0);
        bundle.putLong(contextValueNames[21], 0);
        bundle.putLong(contextValueNames[22], 0);
        return bundle;
    }

    protected final byte getInstLibType() {
        return (byte) 3;
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

    protected final void notifyInitContext() {
        if (ListenerListManager.getInstance().getUsedTotalCount(getContextType()) == 1) {
            super.notifyInitContext();
        }
    }

    public int parse(byte[] bArr, int i) {
        Throwable th;
        int i2 = i;
        CaLogger.info("parse start:" + i);
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        this._mutex.lock();
        String[] contextValueNames = getContextValueNames();
        i2 = i + 1;
        byte b = bArr[i];
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        long j5 = 0;
        long j6 = 0;
        long j7 = 0;
        double d = 0.0d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        Calendar instance = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int i3 = instance.get(11);
        int i4 = instance.get(12);
        long j8 = (long) (((((i3 * 3600) + (i4 * 60)) + instance.get(13)) * 1000) + instance.get(14));
        long timeInMillis = instance.getTimeInMillis();
        int i5;
        int i6;
        int i7;
        byte b2;
        if ((b & 128) == 0) {
            if ((bArr.length - i2) - 14 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                this._mutex.unlock();
                return -1;
            }
            i5 = b & CustomDeviceManagerProxy.SENSOR_ALL;
            super.getContextBean().putContext(contextValueNames[0], (long) i5);
            j2 = (long) i5;
            this.accumulativeWalkStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[1], this.accumulativeWalkStepCount);
            i6 = i2 + 1;
            b = bArr[i2];
            super.getContextBean().putContext(contextValueNames[2], (long) b);
            j3 = (long) b;
            this.accumulativeRunStepCount += (long) b;
            super.getContextBean().putContext(contextValueNames[3], this.accumulativeRunStepCount);
            i2 = i6 + 1;
            b = bArr[i6];
            super.getContextBean().putContext(contextValueNames[4], (long) b);
            this.accumulativeUpDownStepCount += (long) b;
            super.getContextBean().putContext(contextValueNames[5], this.accumulativeUpDownStepCount);
            i6 = i2 + 1;
            i7 = (bArr[i2] & 255) << 8;
            i2 = i6 + 1;
            i5 = i7 + (bArr[i6] & 255);
            super.getContextBean().putContext(contextValueNames[6], (long) i5);
            j = (long) i5;
            this.accumulativeTotalStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[7], this.accumulativeTotalStepCount);
            i6 = i2 + 1;
            i7 = (bArr[i2] & 255) << 8;
            i2 = i6 + 1;
            i5 = i7 + (bArr[i6] & 255);
            super.getContextBean().putContext(contextValueNames[8], ((double) i5) / 100.0d);
            d = ((double) i5) / 100.0d;
            this.accumulativeDistance += ((double) i5) / 100.0d;
            super.getContextBean().putContext(contextValueNames[9], this.accumulativeDistance);
            i6 = i2 + 1;
            d3 = ((double) CaConvertUtil.getCompleteOfTwo(bArr[i2])) / 10.0d;
            super.getContextBean().putContext(contextValueNames[10], d3);
            i2 = i6 + 1;
            i5 = bArr[i6];
            b2 = i5;
            super.getContextBean().putContext(contextValueNames[11], i5);
            i6 = i2 + 1;
            d2 = ((double) CaConvertUtil.getCompleteOfTwo(bArr[i2])) / 100.0d;
            super.getContextBean().putContext(contextValueNames[12], d2);
            this.accumulativeCalorie += d2;
            super.getContextBean().putContext(contextValueNames[13], this.accumulativeCalorie);
            i2 = i6 + 1;
            super.getContextBean().putContext(contextValueNames[14], ((double) bArr[i6]) / 10.0d);
            i6 = i2 + 1;
            b = bArr[i2];
            super.getContextBean().putContext(contextValueNames[15], (long) b);
            j4 = (long) b;
            this.accumulativeWalkUpStepCount += (long) b;
            super.getContextBean().putContext(contextValueNames[16], this.accumulativeWalkUpStepCount);
            i2 = i6 + 1;
            b = bArr[i6];
            super.getContextBean().putContext(contextValueNames[17], (long) b);
            j5 = (long) b;
            this.accumulativeWalkDownStepCount += (long) b;
            super.getContextBean().putContext(contextValueNames[18], this.accumulativeWalkDownStepCount);
            i6 = i2 + 1;
            b = bArr[i2];
            super.getContextBean().putContext(contextValueNames[19], (long) b);
            j6 = (long) b;
            this.accumulativeRunUpStepCount += (long) b;
            super.getContextBean().putContext(contextValueNames[20], this.accumulativeRunUpStepCount);
            i2 = i6 + 1;
            b = bArr[i6];
            super.getContextBean().putContext(contextValueNames[21], (long) b);
            j7 = (long) b;
            this.accumulativeRunDownStepCount += (long) b;
            super.getContextBean().putContext(contextValueNames[22], this.accumulativeRunDownStepCount);
            if (j > 0) {
                this.mPedoHistory.updateBufferIndex(1);
                this.mPedoHistory.putTotalStepInfoSingle(j);
                this.mPedoHistory.putWalkStepInfoSingle(j2);
                this.mPedoHistory.putRunStepInfoSingle(j3);
                this.mPedoHistory.putWalkUpStepInfoSingle(j4);
                this.mPedoHistory.putRunUpStepInfoSingle(j6);
                this.mPedoHistory.putWalkDnStepInfoSingle(j5);
                this.mPedoHistory.putRunDnStepInfoSingle(j7);
                this.mPedoHistory.putDistanceInfoSingle(d);
                this.mPedoHistory.putCalorieInfoSingle(d2);
                this.mPedoHistory.putSpeedInfoSingle(d3);
                this.mPedoHistory.setDataMode(1);
            }
        } else if (((b & 192) >> 6) == 3) {
            if ((bArr.length - i2) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                this._mutex.unlock();
                return -1;
            }
            i6 = i2 + 1;
            byte b3 = bArr[i2];
            if (b3 <= (byte) 0) {
                CaLogger.error(SensorHubErrors.ERROR_LOGGING_PACKAGE_SIZE.getMessage());
                this._mutex.unlock();
                return -1;
            }
            long[] jArr = new long[b3];
            double[] dArr = new double[b3];
            double[] dArr2 = new double[b3];
            double[] dArr3 = new double[b3];
            long[] jArr2 = new long[b3];
            long[] jArr3 = new long[b3];
            long[] jArr4 = new long[b3];
            long[] jArr5 = new long[b3];
            long[] jArr6 = new long[b3];
            long[] jArr7 = new long[b3];
            long[] jArr8 = new long[b3];
            byte b4 = (byte) 0;
            while (b4 < b3) {
                if ((bArr.length - i6) - 20 < 0) {
                    CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                    this._mutex.unlock();
                    return -1;
                }
                i2 = i6 + 1;
                i7 = (bArr[i6] & 255) << 24;
                i6 = i2 + 1;
                i7 += (bArr[i2] & 255) << 16;
                i2 = i6 + 1;
                i7 += (bArr[i6] & 255) << 8;
                i6 = i2 + 1;
                jArr[b4] = CaTimeManager.getInstance().getTimeStampForUTC24(j8, timeInMillis, (long) (i7 + (bArr[i2] & 255)));
                i2 = i6 + 1;
                i7 = (bArr[i6] & 255) << 8;
                i6 = i2 + 1;
                dArr[b4] = ((double) (i7 + (bArr[i2] & 255))) / 100.0d;
                i2 = i6 + 1;
                i7 = (bArr[i6] & 255) << 8;
                i6 = i2 + 1;
                dArr2[b4] = ((double) (i7 + (bArr[i2] & 255))) / 100.0d;
                i2 = i6 + 1;
                try {
                    jArr2[b4] = (long) CaConvertUtil.getCompleteOfTwo(bArr[i6]);
                    i6 = i2 + 1;
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    i7 = (bArr[i2] & 255) << 8;
                    i2 = i6 + 1;
                    jArr3[b4] = (long) (i7 + (bArr[i6] & 255));
                    i6 = i2 + 1;
                    jArr4[b4] = (long) CaConvertUtil.getCompleteOfTwo(bArr[i2]);
                    i2 = i6 + 1;
                    jArr5[b4] = (long) CaConvertUtil.getCompleteOfTwo(bArr[i6]);
                    i6 = i2 + 1;
                    i7 = (bArr[i2] & 255) << 8;
                    i2 = i6 + 1;
                    jArr6[b4] = (long) (i7 + (bArr[i6] & 255));
                    i6 = i2 + 1;
                    i7 = (bArr[i2] & 255) << 8;
                    i2 = i6 + 1;
                    jArr7[b4] = (long) (i7 + (bArr[i6] & 255));
                    i6 = i2 + 1;
                    dArr3[b4] = ((double) CaConvertUtil.getCompleteOfTwo(bArr[i2])) / 10.0d;
                    i2 = i6 + 1;
                    i7 = (bArr[i6] & 255) << 8;
                    i6 = i2 + 1;
                    jArr8[b4] = (long) (i7 + (bArr[i2] & 255));
                    d += dArr[b4];
                    d2 += dArr2[b4];
                    d3 += dArr3[b4];
                    j += jArr8[b4];
                    j2 += jArr2[b4];
                    j3 += jArr3[b4];
                    j4 += jArr4[b4];
                    j5 += jArr5[b4];
                    j6 += jArr6[b4];
                    j7 += jArr7[b4];
                    this.mPedoHistory.updateBufferIndex(2);
                    this.mPedoHistory.putTimestamp(jArr[b4]);
                    this.mPedoHistory.putTotalStepInfo(jArr8[b4]);
                    this.mPedoHistory.putWalkStepInfo(jArr2[b4]);
                    this.mPedoHistory.putRunStepInfo(jArr3[b4]);
                    this.mPedoHistory.putWalkUpStepInfo(jArr4[b4]);
                    this.mPedoHistory.putRunUpStepInfo(jArr6[b4]);
                    this.mPedoHistory.putWalkDnStepInfo(jArr5[b4]);
                    this.mPedoHistory.putRunDnStepInfo(jArr7[b4]);
                    this.mPedoHistory.putDistanceInfo(dArr[b4]);
                    this.mPedoHistory.putCalorieInfo(dArr2[b4]);
                    this.mPedoHistory.putSpeedInfo(dArr3[b4]);
                    this.mPedoHistory.setDataMode(2);
                    b4++;
                } catch (Throwable th3) {
                    th = th3;
                    i2 = i6;
                }
            }
            long j9 = ((j4 + j5) + j6) + j7;
            d3 /= (double) b3;
            getContextBean().putContext(contextValueNames[0], j2);
            this.accumulativeWalkStepCount += j2;
            getContextBean().putContext(contextValueNames[1], this.accumulativeWalkStepCount);
            getContextBean().putContext(contextValueNames[2], j3);
            this.accumulativeRunStepCount += j3;
            getContextBean().putContext(contextValueNames[3], this.accumulativeRunStepCount);
            getContextBean().putContext(contextValueNames[4], j9);
            this.accumulativeUpDownStepCount += j9;
            getContextBean().putContext(contextValueNames[5], this.accumulativeUpDownStepCount);
            getContextBean().putContext(contextValueNames[6], j);
            this.accumulativeTotalStepCount += j;
            getContextBean().putContext(contextValueNames[7], this.accumulativeTotalStepCount);
            getContextBean().putContext(contextValueNames[8], d);
            this.accumulativeDistance += d;
            getContextBean().putContext(contextValueNames[9], this.accumulativeDistance);
            getContextBean().putContext(contextValueNames[10], d3);
            getContextBean().putContext(contextValueNames[11], -1);
            getContextBean().putContext(contextValueNames[12], d2);
            this.accumulativeCalorie += d2;
            getContextBean().putContext(contextValueNames[13], this.accumulativeCalorie);
            getContextBean().putContext(contextValueNames[14], 0.0d);
            getContextBean().putContext(contextValueNames[15], j4);
            this.accumulativeWalkUpStepCount += j4;
            getContextBean().putContext(contextValueNames[16], this.accumulativeWalkUpStepCount);
            getContextBean().putContext(contextValueNames[17], j5);
            this.accumulativeWalkDownStepCount += j5;
            getContextBean().putContext(contextValueNames[18], this.accumulativeWalkDownStepCount);
            getContextBean().putContext(contextValueNames[19], j6);
            this.accumulativeRunUpStepCount += j6;
            getContextBean().putContext(contextValueNames[20], this.accumulativeRunUpStepCount);
            getContextBean().putContext(contextValueNames[21], j7);
            this.accumulativeRunDownStepCount += j7;
            getContextBean().putContext(contextValueNames[22], this.accumulativeRunDownStepCount);
            getContextBean().putContext(contextValueNames[23], (int) b3);
            getContextBean().putContext(contextValueNames[24], jArr);
            getContextBean().putContext(contextValueNames[25], dArr);
            getContextBean().putContext(contextValueNames[26], dArr2);
            getContextBean().putContext(contextValueNames[27], dArr3);
            getContextBean().putContext(contextValueNames[28], jArr2);
            getContextBean().putContext(contextValueNames[29], jArr3);
            getContextBean().putContext(contextValueNames[30], jArr4);
            getContextBean().putContext(contextValueNames[31], jArr5);
            getContextBean().putContext(contextValueNames[32], jArr6);
            getContextBean().putContext(contextValueNames[33], jArr7);
            getContextBean().putContext(contextValueNames[34], jArr8);
            i2 = i6;
        } else if ((bArr.length - i2) - 32 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            this._mutex.unlock();
            return -1;
        } else {
            i6 = i2 + 1;
            i7 = (bArr[i2] & 255) << 16;
            i2 = i6 + 1;
            i7 += (bArr[i6] & 255) << 8;
            i6 = i2 + 1;
            i5 = i7 + (bArr[i2] & 255);
            super.getContextBean().putContext(contextValueNames[0], (long) i5);
            j2 = (long) i5;
            this.accumulativeWalkStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[1], this.accumulativeWalkStepCount);
            i2 = i6 + 1;
            i7 = (bArr[i6] & 255) << 16;
            i6 = i2 + 1;
            i7 += (bArr[i2] & 255) << 8;
            i2 = i6 + 1;
            i5 = i7 + (bArr[i6] & 255);
            super.getContextBean().putContext(contextValueNames[2], (long) i5);
            j3 = (long) i5;
            this.accumulativeRunStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[3], this.accumulativeRunStepCount);
            i6 = i2 + 1;
            i7 = (bArr[i2] & 255) << 16;
            i2 = i6 + 1;
            i7 += (bArr[i6] & 255) << 8;
            i6 = i2 + 1;
            i5 = i7 + (bArr[i2] & 255);
            super.getContextBean().putContext(contextValueNames[4], (long) i5);
            this.accumulativeUpDownStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[5], this.accumulativeUpDownStepCount);
            i2 = i6 + 1;
            i7 = (bArr[i6] & 255) << 16;
            i6 = i2 + 1;
            i7 += (bArr[i2] & 255) << 8;
            i2 = i6 + 1;
            i5 = i7 + (bArr[i6] & 255);
            super.getContextBean().putContext(contextValueNames[6], (long) i5);
            j = (long) i5;
            this.accumulativeTotalStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[7], this.accumulativeTotalStepCount);
            i6 = i2 + 1;
            i7 = (bArr[i2] & 255) << 16;
            i2 = i6 + 1;
            i7 += (bArr[i6] & 255) << 8;
            i6 = i2 + 1;
            i5 = i7 + (bArr[i2] & 255);
            super.getContextBean().putContext(contextValueNames[8], ((double) i5) / 100.0d);
            d = ((double) i5) / 100.0d;
            this.accumulativeDistance += ((double) i5) / 100.0d;
            super.getContextBean().putContext(contextValueNames[9], this.accumulativeDistance);
            i2 = i6 + 1;
            d3 = ((double) CaConvertUtil.getCompleteOfTwo(bArr[i6])) / 10.0d;
            super.getContextBean().putContext(contextValueNames[10], d3);
            i6 = i2 + 1;
            i5 = bArr[i2];
            b2 = i5;
            super.getContextBean().putContext(contextValueNames[11], i5);
            i2 = i6 + 1;
            i7 = (bArr[i6] & 255) << 8;
            i6 = i2 + 1;
            i5 = i7 + (bArr[i2] & 255);
            super.getContextBean().putContext(contextValueNames[12], ((double) i5) / 10.0d);
            d2 = ((double) i5) / 10.0d;
            this.accumulativeCalorie += ((double) i5) / 10.0d;
            super.getContextBean().putContext(contextValueNames[13], this.accumulativeCalorie);
            i2 = i6 + 1;
            super.getContextBean().putContext(contextValueNames[14], ((double) bArr[i6]) / 10.0d);
            i6 = i2 + 1;
            i7 = (bArr[i2] & 255) << 16;
            i2 = i6 + 1;
            i7 += (bArr[i6] & 255) << 8;
            i6 = i2 + 1;
            i5 = i7 + (bArr[i2] & 255);
            super.getContextBean().putContext(contextValueNames[15], (long) i5);
            j4 = (long) i5;
            this.accumulativeWalkUpStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[16], this.accumulativeWalkUpStepCount);
            i2 = i6 + 1;
            i7 = (bArr[i6] & 255) << 16;
            i6 = i2 + 1;
            i7 += (bArr[i2] & 255) << 8;
            i2 = i6 + 1;
            i5 = i7 + (bArr[i6] & 255);
            super.getContextBean().putContext(contextValueNames[17], (long) i5);
            j5 = (long) i5;
            this.accumulativeWalkDownStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[18], this.accumulativeWalkDownStepCount);
            i6 = i2 + 1;
            i7 = (bArr[i2] & 255) << 16;
            i2 = i6 + 1;
            i7 += (bArr[i6] & 255) << 8;
            i6 = i2 + 1;
            i5 = i7 + (bArr[i2] & 255);
            super.getContextBean().putContext(contextValueNames[19], (long) i5);
            j6 = (long) i5;
            this.accumulativeRunUpStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[20], this.accumulativeRunUpStepCount);
            i2 = i6 + 1;
            i7 = (bArr[i6] & 255) << 16;
            i6 = i2 + 1;
            i7 += (bArr[i2] & 255) << 8;
            i2 = i6 + 1;
            i5 = i7 + (bArr[i6] & 255);
            super.getContextBean().putContext(contextValueNames[21], (long) i5);
            j7 = (long) i5;
            this.accumulativeRunDownStepCount += (long) i5;
            super.getContextBean().putContext(contextValueNames[22], this.accumulativeRunDownStepCount);
            if (j > 0) {
                this.mPedoHistory.updateBufferIndex(1);
                this.mPedoHistory.putTotalStepInfoSingle(j);
                this.mPedoHistory.putWalkStepInfoSingle(j2);
                this.mPedoHistory.putRunStepInfoSingle(j3);
                this.mPedoHistory.putWalkUpStepInfoSingle(j4);
                this.mPedoHistory.putRunUpStepInfoSingle(j6);
                this.mPedoHistory.putWalkDnStepInfoSingle(j5);
                this.mPedoHistory.putRunDnStepInfoSingle(j7);
                this.mPedoHistory.putDistanceInfoSingle(d);
                this.mPedoHistory.putCalorieInfoSingle(d2);
                this.mPedoHistory.putSpeedInfoSingle(d3);
                this.mPedoHistory.setDataMode(1);
            }
        }
        super.notifyObserver();
        this._mutex.unlock();
        CaLogger.info("parse end:" + i2);
        return i2;
        this._mutex.unlock();
        throw th;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        byte[] bArr = new byte[1];
        if (i == 5) {
            CaLogger.info("Height");
            CaUserInfo.getInstance().setUserHeight(((Double) ((ContextAwarePropertyBundle) e).getValue()).doubleValue());
            bArr[0] = (byte) ((int) CaUserInfo.getInstance().getUserHeight());
            return sendCommonValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_CIRCEASEINOUT, bArr);
        } else if (i == 4) {
            CaLogger.info("Weight");
            CaUserInfo.getInstance().setUserWeight(((Double) ((ContextAwarePropertyBundle) e).getValue()).doubleValue());
            bArr[0] = (byte) ((int) CaUserInfo.getInstance().getUserWeight());
            return sendCommonValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_CUBICEASEIN, bArr);
        } else if (i == 6) {
            CaLogger.info("Gender");
            CaUserInfo.getInstance().setUserGender(((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue());
            bArr[0] = (byte) CaUserInfo.getInstance().getUserGender();
            return sendCommonValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_CUBICEASEOUT, bArr);
        } else if (i == 17) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Delivery Count = " + Integer.toString(intValue));
            return sendCommonValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_CUBICEASEINOUT, CaConvertUtil.intToByteArr(intValue, 1));
        } else if (i != 2) {
            return false;
        } else {
            CaLogger.info("History Data");
            this._mutex.lock();
            try {
                sendHistoryStepBuffer();
                return true;
            } finally {
                this._mutex.unlock();
            }
        }
    }
}
