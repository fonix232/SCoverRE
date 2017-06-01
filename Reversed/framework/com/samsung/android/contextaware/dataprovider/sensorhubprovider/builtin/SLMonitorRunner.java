package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils.SLMHistory;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SLMonitorRunner extends LibTypeProvider {
    private static final int DEFAULT_POWER_STEP_START_DURATION = 300;
    private static final int DEFAULT_STEP_LEVEL_TYPE = 4;
    private final Lock _mutex = new ReentrantLock(true);
    private int mDuration = DEFAULT_POWER_STEP_START_DURATION;
    private final SLMHistory mSLMHistory = SLMHistory.getInstance();
    private int mStepLevelType = 4;

    private enum ContextValIndex {
        DataType(0),
        Timestamp(1),
        DataCount(2),
        DataBundle(3),
        StepType(4),
        StepCount(5),
        Distance(6),
        Calorie(7),
        Duration(8),
        ActiveTime(9),
        TimeStampArray(10),
        HistoryMode(11);
        
        private int val;

        private ContextValIndex(int i) {
            this.val = i;
        }
    }

    public SLMonitorRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    private void display(Bundle bundle) {
        if (bundle != null && !bundle.isEmpty()) {
            CaLogger.debug("================= " + getContextType() + " =================");
            StringBuffer stringBuffer = new StringBuffer();
            int i = bundle.getInt("DataType");
            if (i == 1) {
                int i2;
                int i3 = bundle.getInt("DataCount");
                int i4 = bundle.getInt("HistoryMode", 0);
                stringBuffer.append("DT=[" + i + "], ");
                stringBuffer.append("DC=[" + i3 + "], ");
                if (i4 == 1) {
                    long[] longArray = bundle.getLongArray("TimeStampArray");
                    stringBuffer.append("TS=[");
                    for (i2 = 0; i2 < i3; i2++) {
                        stringBuffer.append(longArray[i2]);
                        if (i2 != i3 - 1) {
                            stringBuffer.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                        }
                    }
                    stringBuffer.append("], ");
                } else {
                    stringBuffer.append("TS=[" + bundle.getLong("TimeStamp") + "], ");
                }
                BaseBundle bundle2 = bundle.getBundle("DataBundle");
                if (bundle2 != null) {
                    int[] intArray = bundle2.getIntArray("StepTypeArray");
                    int[] intArray2 = bundle2.getIntArray("StepCountArray");
                    double[] doubleArray = bundle2.getDoubleArray("DistanceArray");
                    double[] doubleArray2 = bundle2.getDoubleArray("CalorieArray");
                    int[] intArray3 = bundle2.getIntArray("DurationArray");
                    if (intArray != null && intArray2 != null && doubleArray != null && doubleArray2 != null && intArray3 != null) {
                        stringBuffer.append("ST=[");
                        for (i2 = 0; i2 < i3; i2++) {
                            if (intArray[i2] == 5) {
                                stringBuffer.append("IN");
                            }
                            if (intArray[i2] == 4) {
                                stringBuffer.append("PO");
                            }
                            if (intArray[i2] == 3) {
                                stringBuffer.append("NO");
                            }
                            if (intArray[i2] == 2) {
                                stringBuffer.append("SE");
                            }
                            if (intArray[i2] == 1) {
                                stringBuffer.append("ST");
                            }
                            if (i2 != i3 - 1) {
                                stringBuffer.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        stringBuffer.append("], ");
                        stringBuffer.append("SC=[");
                        for (i2 = 0; i2 < i3; i2++) {
                            stringBuffer.append(intArray2[i2]);
                            if (i2 != i3 - 1) {
                                stringBuffer.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        stringBuffer.append("], ");
                        stringBuffer.append("DI=[");
                        for (i2 = 0; i2 < i3; i2++) {
                            stringBuffer.append(doubleArray[i2]);
                            if (i2 != i3 - 1) {
                                stringBuffer.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        stringBuffer.append("], ");
                        stringBuffer.append("CA=[");
                        for (i2 = 0; i2 < i3; i2++) {
                            stringBuffer.append(doubleArray2[i2]);
                            if (i2 != i3 - 1) {
                                stringBuffer.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        stringBuffer.append("], ");
                        stringBuffer.append("DU=[");
                        for (i2 = 0; i2 < i3; i2++) {
                            stringBuffer.append(intArray3[i2]);
                            if (i2 != i3 - 1) {
                                stringBuffer.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        stringBuffer.append("]");
                        if (i4 == 1) {
                            stringBuffer.append(", HistoryMode=[" + i4 + "], ");
                        }
                    } else {
                        return;
                    }
                }
                return;
            } else if (i == 2) {
                int i5 = bundle.getInt("ActiveTimeDuration");
                stringBuffer.append("DT=[" + i + "], ");
                stringBuffer.append("DU=[" + i5 + "]");
            }
            CaLogger.info(stringBuffer.toString());
        }
    }

    private void sendHistorySLMBuffer() {
        CaLogger.warning("sendSLMHistoryData");
        String[] contextValueNames = getContextValueNames();
        int bufferSize = this.mSLMHistory.getBufferSize();
        if (bufferSize == 0) {
            CaLogger.error("History Data Buffer is null!");
        }
        long[] jArr = new long[bufferSize];
        int[] iArr = new int[bufferSize];
        int[] iArr2 = new int[bufferSize];
        double[] dArr = new double[bufferSize];
        double[] dArr2 = new double[bufferSize];
        int[] iArr3 = new int[bufferSize];
        Bundle bundle = new Bundle();
        for (int i = 0; i < bufferSize; i++) {
            jArr[i] = this.mSLMHistory.getTimeStampSingle(i);
            iArr[i] = this.mSLMHistory.getmStepTypeArraySingle(i);
            iArr2[i] = this.mSLMHistory.getmStepCountArraySingle(i);
            dArr[i] = this.mSLMHistory.getmDistanceArraySingle(i);
            dArr2[i] = this.mSLMHistory.getmCalorieArraySingle(i);
            iArr3[i] = this.mSLMHistory.getmDurationArraySingle(i);
        }
        bundle.putIntArray(contextValueNames[ContextValIndex.StepType.val], iArr);
        bundle.putIntArray(contextValueNames[ContextValIndex.StepCount.val], iArr2);
        bundle.putDoubleArray(contextValueNames[ContextValIndex.Distance.val], dArr);
        bundle.putDoubleArray(contextValueNames[ContextValIndex.Calorie.val], dArr2);
        bundle.putIntArray(contextValueNames[ContextValIndex.Duration.val], iArr3);
        getContextBean().putContext(contextValueNames[ContextValIndex.DataCount.val], bufferSize);
        getContextBean().putContext(contextValueNames[ContextValIndex.TimeStampArray.val], jArr);
        getContextBean().putContext(contextValueNames[ContextValIndex.DataBundle.val], bundle);
        getContextBean().putContext(contextValueNames[ContextValIndex.DataType.val], 1);
        getContextBean().putContext(contextValueNames[ContextValIndex.HistoryMode.val], 1);
        BaseBundle bundle2 = new Bundle();
        bundle2.putInt(contextValueNames[ContextValIndex.DataCount.val], bufferSize);
        bundle2.putLongArray(contextValueNames[ContextValIndex.TimeStampArray.val], jArr);
        bundle2.putBundle(contextValueNames[ContextValIndex.DataBundle.val], bundle);
        bundle2.putInt(contextValueNames[ContextValIndex.DataType.val], 1);
        bundle2.putInt(contextValueNames[ContextValIndex.HistoryMode.val], 1);
        display(bundle2);
        super.notifyObserver();
    }

    public final void clear() {
        CaLogger.trace();
        this._mutex.lock();
        try {
            this.mSLMHistory.erase();
            super.clear();
        } finally {
            this._mutex.unlock();
        }
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    protected void display() {
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SL_MONITOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"DataType", "TimeStamp", "DataCount", "DataBundle", "StepTypeArray", "StepCountArray", "DistanceArray", "CalorieArray", "DurationArray", "ActiveTimeDuration", "TimeStampArray", "HistoryMode"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r0 = new byte[8];
        byte[] intToByteArr = CaConvertUtil.intToByteArr(this.mDuration, 2);
        r0[2] = intToByteArr[0];
        r0[3] = intToByteArr[1];
        r0[4] = (byte) 0;
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        r0[5] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        r0[6] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        r0[7] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return r0;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT50;
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
        Throwable e;
        int i2 = i;
        String[] contextValueNames = getContextValueNames();
        CaLogger.info("parse:" + i);
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        if (b != (byte) 1) {
            CaLogger.error(SensorHubErrors.ERROR_TYPE_VALUE.getMessage());
        } else if ((bArr.length - i2) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        } else {
            int i3 = i2 + 1;
            byte b2 = bArr[i2];
            CaLogger.debug("dataSize:" + b2);
            if (b2 <= (byte) 0) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                return -1;
            } else if (bArr.length - i3 < (b2 * 12) + 4) {
                CaLogger.debug("packet len:" + bArr.length + " tmpNext:" + i3);
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else {
                byte[] bArr2 = new byte[8];
                bArr2[0] = (byte) 0;
                bArr2[1] = (byte) 0;
                bArr2[2] = (byte) 0;
                bArr2[3] = (byte) 0;
                i2 = i3 + 1;
                bArr2[4] = bArr[i3];
                i3 = i2 + 1;
                bArr2[5] = bArr[i2];
                i2 = i3 + 1;
                bArr2[6] = bArr[i3];
                i3 = i2 + 1;
                bArr2[7] = bArr[i2];
                long j = ByteBuffer.wrap(bArr2).getLong();
                Calendar instance = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
                int i4 = instance.get(11);
                int i5 = instance.get(12);
                j = CaTimeManager.getInstance().getTimeStampForUTC24((long) (((((i4 * 3600) + (i5 * 60)) + instance.get(13)) * 1000) + instance.get(14)), instance.getTimeInMillis(), j);
                int[] iArr = new int[b2];
                int[] iArr2 = new int[b2];
                double[] dArr = new double[b2];
                double[] dArr2 = new double[b2];
                int[] iArr3 = new int[b2];
                long[] jArr = new long[b2];
                jArr[0] = j;
                BaseBundle bundle = new Bundle();
                byte b3 = (byte) 0;
                while (b3 < b2) {
                    i2 = i3 + 1;
                    iArr[b3] = bArr[i3];
                    bArr2 = new byte[4];
                    bArr2[0] = (byte) 0;
                    bArr2[1] = (byte) 0;
                    i3 = i2 + 1;
                    bArr2[2] = bArr[i2];
                    i2 = i3 + 1;
                    bArr2[3] = bArr[i3];
                    iArr2[b3] = ByteBuffer.wrap(bArr2).getInt();
                    bArr2 = new byte[4];
                    bArr2[0] = (byte) 0;
                    i3 = i2 + 1;
                    bArr2[1] = bArr[i2];
                    i2 = i3 + 1;
                    bArr2[2] = bArr[i3];
                    i3 = i2 + 1;
                    try {
                        bArr2[3] = bArr[i2];
                        dArr[b3] = ((double) ByteBuffer.wrap(bArr2).getInt()) / 100.0d;
                        bArr2 = new byte[4];
                        bArr2[0] = (byte) 0;
                        bArr2[1] = (byte) 0;
                        i2 = i3 + 1;
                    } catch (Exception e2) {
                        e = e2;
                        i2 = i3;
                    } catch (Throwable th) {
                        this._mutex.unlock();
                    }
                    try {
                        bArr2[2] = bArr[i3];
                        i3 = i2 + 1;
                        bArr2[3] = bArr[i2];
                        dArr2[b3] = ((double) ByteBuffer.wrap(bArr2).getInt()) / 100.0d;
                        bArr2 = new byte[4];
                        i2 = i3 + 1;
                        bArr2[0] = bArr[i3];
                        i3 = i2 + 1;
                        bArr2[1] = bArr[i2];
                        i2 = i3 + 1;
                        bArr2[2] = bArr[i3];
                        i3 = i2 + 1;
                        bArr2[3] = bArr[i2];
                        iArr3[b3] = ByteBuffer.wrap(bArr2).getInt();
                        if (iArr[b3] < 1 || iArr[b3] > 5) {
                            throw new Exception("Invalid stepType : " + iArr[b3]);
                        }
                        this._mutex.lock();
                        this.mSLMHistory.putSLMData(jArr[b3], iArr[b3], iArr2[b3], dArr[b3], dArr2[b3], iArr3[b3]);
                        this._mutex.unlock();
                        if (b3 < b2 - 1) {
                            jArr[b3 + 1] = jArr[b3] + ((long) iArr3[b3]);
                        }
                        b3++;
                    } catch (Exception e3) {
                        e = e3;
                    }
                }
                bundle.putIntArray(contextValueNames[ContextValIndex.StepType.val], iArr);
                bundle.putIntArray(contextValueNames[ContextValIndex.StepCount.val], iArr2);
                bundle.putDoubleArray(contextValueNames[ContextValIndex.Distance.val], dArr);
                bundle.putDoubleArray(contextValueNames[ContextValIndex.Calorie.val], dArr2);
                bundle.putIntArray(contextValueNames[ContextValIndex.Duration.val], iArr3);
                getContextBean().putContext(contextValueNames[ContextValIndex.DataCount.val], (int) b2);
                getContextBean().putContext(contextValueNames[ContextValIndex.Timestamp.val], j);
                getContextBean().putContext(contextValueNames[ContextValIndex.DataBundle.val], (Bundle) bundle);
                getContextBean().putContext(contextValueNames[ContextValIndex.DataType.val], (int) b);
                BaseBundle bundle2 = new Bundle();
                bundle2.putInt(contextValueNames[ContextValIndex.DataCount.val], b2);
                bundle2.putLong(contextValueNames[ContextValIndex.Timestamp.val], j);
                bundle2.putBundle(contextValueNames[ContextValIndex.DataBundle.val], bundle);
                bundle2.putInt(contextValueNames[ContextValIndex.DataType.val], b);
                display(bundle2);
                super.notifyObserver();
                i2 = i3;
            }
        }
        return i2;
        CaLogger.error("SLMonitor Runner Exception : " + e.getMessage().toString());
        return i2;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 58) {
            this.mStepLevelType = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Step Level Type = " + Integer.toString(this.mStepLevelType));
            return true;
        } else if (i == 59) {
            this.mDuration = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(this.mDuration));
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT50, (byte) 4, CaConvertUtil.intToByteArr(this.mDuration, 2));
            return true;
        } else if (i != 4) {
            return false;
        } else {
            CaLogger.info("History Data");
            this._mutex.lock();
            try {
                sendHistorySLMBuffer();
                return true;
            } finally {
                this._mutex.unlock();
            }
        }
    }
}
