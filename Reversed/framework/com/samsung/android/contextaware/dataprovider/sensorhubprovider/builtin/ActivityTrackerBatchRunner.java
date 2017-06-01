package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils.ActivityHistory;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.ActivityTrackerProvider;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActivityTrackerBatchRunner extends ActivityTrackerProvider {
    private static long CHECK_PERIOD = 20000;
    private static int DEFAULT_ACTIVITY_RECORDING_PERIOD = 1800000;
    private static final int DEFAULT_BATCHING_PERIOD = 1200;
    private static final int MSG_TIMER_EXPIRED = 65261;
    private static final int mBatchingPeriod = 1200;
    private final ActivityHistory mActivityHistory = ActivityHistory.getInstance();
    private Handler mHandler;
    private boolean mHistoryDataReq = false;
    private final ArrayList<ActivityInfo> mListActivityInfo = new ArrayList();
    private final Lock mMutex = new ReentrantLock(true);

    private static class ActivityInfo {
        int accuracy;
        int activityType;
        long duration;
        long timestamp;

        private ActivityInfo() {
        }
    }

    public ActivityTrackerBatchRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        createHandler(looper);
    }

    private void createHandler(Looper looper) {
        this.mHandler = new Handler(looper) {
            public void handleMessage(Message message) {
                if (message.what == ActivityTrackerBatchRunner.MSG_TIMER_EXPIRED) {
                    ActivityTrackerBatchRunner.this.mHistoryDataReq = false;
                    ActivityTrackerBatchRunner.this.mMutex.lock();
                    try {
                        ActivityTrackerBatchRunner.this.sendHistoryData();
                    } finally {
                        ActivityTrackerBatchRunner.this.mMutex.unlock();
                    }
                }
            }
        };
    }

    private int getMostActivity() {
        int size = this.mListActivityInfo.size();
        CaLogger.info("size:" + size);
        if (size <= 0) {
            return 1;
        }
        if (((ActivityInfo) this.mListActivityInfo.get(size - 1)).duration > CHECK_PERIOD) {
            return ((ActivityInfo) this.mListActivityInfo.get(size - 1)).activityType;
        }
        long j = 0;
        int i = size - 1;
        float f = 0.0f;
        int i2 = 1;
        int i3 = size - 1;
        while (j <= 30000 && i >= 0) {
            if (((ActivityInfo) this.mListActivityInfo.get(i)).duration < 3000) {
                i--;
            } else {
                float f2 = ((float) ((ActivityInfo) this.mListActivityInfo.get(i)).duration) / ((float) i2);
                if (f < f2) {
                    f = f2;
                    i3 = i;
                    i2++;
                }
                j += ((ActivityInfo) this.mListActivityInfo.get(i)).duration;
                i--;
            }
        }
        return ((ActivityInfo) this.mListActivityInfo.get(i3)).activityType;
    }

    private void sendHistoryData() {
        CaLogger.warning("start");
        String[] contextValueNames = getContextValueNames();
        int bufferSize = this.mActivityHistory.getBufferSize();
        if (bufferSize == 0) {
            CaLogger.error("History Data Buffer is null!!");
        }
        int[] iArr = new int[bufferSize];
        int[] iArr2 = new int[bufferSize];
        long[] jArr = new long[bufferSize];
        long[] jArr2 = new long[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            iArr[i] = this.mActivityHistory.getActivityType(i);
            iArr2[i] = this.mActivityHistory.getActivityAccuracy(i);
            jArr[i] = this.mActivityHistory.getActivityDuration(i);
            jArr2[i] = this.mActivityHistory.getActivityTimestamp(i);
        }
        getContextBean().putContext(contextValueNames[0], (short) getModeType());
        getContextBean().putContext(contextValueNames[2], bufferSize);
        getContextBean().putContext(contextValueNames[3], iArr);
        getContextBean().putContext(contextValueNames[4], iArr2);
        getContextBean().putContext(contextValueNames[5], jArr);
        getContextBean().putContext(contextValueNames[6], getMostActivity());
        getContextBean().putContext(contextValueNames[7], jArr2);
        getContextBean().putContext(contextValueNames[8], 1);
        notifyObserver();
        CaLogger.warning("end");
    }

    private void updateActivityInfo(long j, int[] iArr, long[] jArr, int[] iArr2, int i) {
        int i2 = 0;
        while (i2 < i) {
            if (!(iArr[i2] == 0 || iArr2[i2] == 0)) {
                int size = this.mListActivityInfo.size();
                if (size == 0 || iArr[i2] != ((ActivityInfo) this.mListActivityInfo.get(size - 1)).activityType) {
                    ActivityInfo activityInfo = new ActivityInfo();
                    activityInfo.activityType = iArr[i2];
                    if (activityInfo.activityType == 4) {
                        activityInfo.activityType = 1;
                    }
                    activityInfo.duration = jArr[i2];
                    activityInfo.accuracy = iArr2[i2];
                    activityInfo.timestamp = j;
                    j += jArr[i2];
                    this.mListActivityInfo.add(activityInfo);
                } else {
                    ActivityInfo activityInfo2 = (ActivityInfo) this.mListActivityInfo.get(size - 1);
                    activityInfo2.duration += jArr[i2];
                }
            }
            i2++;
        }
        long currentTimeMillis = System.currentTimeMillis() - ((long) DEFAULT_ACTIVITY_RECORDING_PERIOD);
        i2 = 0;
        while (i2 < this.mListActivityInfo.size() && ((ActivityInfo) this.mListActivityInfo.get(i2)).timestamp < currentTimeMillis) {
            i2++;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            this.mListActivityInfo.remove(0);
        }
    }

    public final void clear() {
        CaLogger.trace();
        this.mMutex.lock();
        try {
            this.mListActivityInfo.clear();
            this.mActivityHistory.erase();
            super.clear();
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void disable() {
        CaLogger.trace();
        this.mHistoryDataReq = false;
        this.mHandler.removeMessages(MSG_TIMER_EXPIRED);
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    protected final int getBatchingPeriod() {
        return 1200;
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode();
    }

    public String[] getContextValueNames() {
        return new String[]{"OperationMode", "TimeStamp", "Count", "ActivityType", "Accuracy", "Duration", "MostActivity", "TimeStampArray", "HistoryMode"};
    }

    public final Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getModeType() {
        return (byte) 2;
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
        String[] contextValueNames = getContextValueNames();
        CaLogger.warning("parse start");
        if ((bArr.length - i) - 4 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        r17 = new byte[4];
        i2 = i + 1;
        r17[0] = bArr[i];
        int i3 = i2 + 1;
        r17[1] = bArr[i2];
        i2 = i3 + 1;
        r17[2] = bArr[i3];
        i3 = i2 + 1;
        r17[3] = bArr[i2];
        long timeStampForUTC = CaTimeManager.getInstance().getTimeStampForUTC(byteArrayToLong(r17));
        if ((bArr.length - i3) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i3 + 1;
        byte b = bArr[i3];
        if (b <= (byte) 0) {
            CaLogger.error(SensorHubErrors.ERROR_BATCH_DATA_COUNT.getMessage());
            return -1;
        }
        int[] iArr = new int[b];
        int[] iArr2 = new int[b];
        long[] jArr = new long[b];
        byte b2 = (byte) 0;
        i3 = i2;
        while (b2 < b) {
            if ((bArr.length - i3) - 5 < 0) {
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
            jArr[b2] = (long) ((((bArr[i3] & 255) << 16) + ((bArr[i2] & 255) << 8)) + (bArr[i3] & 255));
            b2++;
            i3 = i2;
        }
        this.mMutex.lock();
        try {
            updateActivityInfo(timeStampForUTC, iArr, jArr, iArr2, b);
            this.mActivityHistory.putActivityData(timeStampForUTC, iArr, iArr2, jArr);
            getContextBean().putContext(contextValueNames[0], (short) getModeType());
            getContextBean().putContext(contextValueNames[1], timeStampForUTC);
            getContextBean().putContext(contextValueNames[2], b);
            getContextBean().putContext(contextValueNames[3], iArr);
            getContextBean().putContext(contextValueNames[4], iArr2);
            getContextBean().putContext(contextValueNames[5], jArr);
            getContextBean().putContext(contextValueNames[6], getMostActivity());
            notifyObserver();
            if (this.mHistoryDataReq) {
                this.mHandler.removeMessages(MSG_TIMER_EXPIRED);
                this.mHistoryDataReq = false;
                sendHistoryData();
            }
            this.mMutex.unlock();
            CaLogger.warning("parse end");
            return i3;
        } catch (Throwable th) {
            this.mMutex.unlock();
        }
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i != 3) {
            return false;
        }
        CaLogger.info("History Data");
        sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 2, (byte) 0});
        this.mHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, 500);
        this.mHistoryDataReq = true;
        return true;
    }
}
