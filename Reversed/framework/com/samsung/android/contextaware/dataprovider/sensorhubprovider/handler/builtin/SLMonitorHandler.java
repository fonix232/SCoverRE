package com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin;

import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubModeHandler;
import java.util.concurrent.CopyOnWriteArrayList;

public class SLMonitorHandler extends SensorHubModeHandler {
    private static volatile SLMonitorHandler instance;

    public static SLMonitorHandler getInstance() {
        if (instance == null) {
            synchronized (SLMonitorHandler.class) {
                if (instance == null) {
                    instance = new SLMonitorHandler();
                }
            }
        }
        return instance;
    }

    protected final void disable() {
    }

    protected final void enable() {
    }

    protected final CopyOnWriteArrayList<String> getModeList() {
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_SL_MONITOR.getCode());
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_SL_MONITOR_EXTENDED_INTERRUPT.getCode());
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_ACTIVE_TIME.getCode());
        return copyOnWriteArrayList;
    }
}
