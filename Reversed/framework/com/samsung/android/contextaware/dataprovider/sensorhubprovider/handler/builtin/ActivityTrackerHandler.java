package com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubModeHandler;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActivityTrackerHandler extends SensorHubModeHandler {
    private static volatile ActivityTrackerHandler instance;

    public static ActivityTrackerHandler getInstance() {
        if (instance == null) {
            synchronized (ActivityTrackerHandler.class) {
                if (instance == null) {
                    instance = new ActivityTrackerHandler();
                }
            }
        }
        return instance;
    }

    protected final void disable() {
        if (!isRunning()) {
        }
    }

    protected final void enable() {
    }

    protected final CopyOnWriteArrayList<String> getModeList() {
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER.getCode());
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.getCode());
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode());
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_EXTANDED_INTERRUPT.getCode());
        return copyOnWriteArrayList;
    }

    protected final void initialize(Context context, Looper looper) {
    }
}
