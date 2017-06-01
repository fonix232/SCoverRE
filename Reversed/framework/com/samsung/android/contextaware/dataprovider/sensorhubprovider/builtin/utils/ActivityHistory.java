package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils;

import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class ActivityHistory implements ITimeChangeObserver {
    private static final int MAX_BUFFER_SIZE = 1440;
    private static volatile ActivityHistory mActivityHistory;
    private int mBufferIndex;
    private int mBufferSize;
    private final HistoryData[] mHistoryData = new HistoryData[MAX_BUFFER_SIZE];

    private static class HistoryData {
        private int accuracy;
        private int activityType;
        private long duration;
        private long timeStamp;

        private HistoryData() {
            this.timeStamp = 0;
            this.activityType = 0;
            this.accuracy = 0;
            this.duration = 0;
        }
    }

    private ActivityHistory() {
        for (int i = 0; i < MAX_BUFFER_SIZE; i++) {
            this.mHistoryData[i] = new HistoryData();
        }
        initialize();
        CaTimeChangeManager.getInstance().registerObserver(this);
    }

    public static ActivityHistory getInstance() {
        if (mActivityHistory == null) {
            synchronized (ActivityHistory.class) {
                if (mActivityHistory == null) {
                    mActivityHistory = new ActivityHistory();
                }
            }
        }
        return mActivityHistory;
    }

    private void initialize() {
        this.mBufferIndex = -1;
        this.mBufferSize = 0;
    }

    public void erase() {
        initialize();
        CaLogger.warning("erased");
    }

    public int getActivityAccuracy(int i) {
        return this.mBufferSize >= MAX_BUFFER_SIZE ? this.mHistoryData[((this.mBufferIndex + 1) + i) % MAX_BUFFER_SIZE].accuracy : this.mHistoryData[i].accuracy;
    }

    public long getActivityDuration(int i) {
        return this.mBufferSize >= MAX_BUFFER_SIZE ? this.mHistoryData[((this.mBufferIndex + 1) + i) % MAX_BUFFER_SIZE].duration : this.mHistoryData[i].duration;
    }

    public long getActivityTimestamp(int i) {
        return this.mBufferSize >= MAX_BUFFER_SIZE ? this.mHistoryData[((this.mBufferIndex + 1) + i) % MAX_BUFFER_SIZE].timeStamp : this.mHistoryData[i].timeStamp;
    }

    public int getActivityType(int i) {
        return this.mBufferSize >= MAX_BUFFER_SIZE ? this.mHistoryData[((this.mBufferIndex + 1) + i) % MAX_BUFFER_SIZE].activityType : this.mHistoryData[i].activityType;
    }

    public int getBufferSize() {
        return this.mBufferSize;
    }

    public void onTimeChanged() {
        erase();
    }

    public void putActivityData(long j, int[] iArr, int[] iArr2, long[] jArr) {
        for (int i = 0; i < iArr.length; i++) {
            if (jArr[i] != 0) {
                this.mBufferIndex = (this.mBufferIndex + 1) % MAX_BUFFER_SIZE;
                if (this.mBufferSize < MAX_BUFFER_SIZE) {
                    this.mBufferSize++;
                }
                this.mHistoryData[this.mBufferIndex].timeStamp = j;
                j += jArr[i];
                this.mHistoryData[this.mBufferIndex].activityType = iArr[i];
                this.mHistoryData[this.mBufferIndex].accuracy = iArr2[i];
                this.mHistoryData[this.mBufferIndex].duration = jArr[i];
            }
        }
    }
}
