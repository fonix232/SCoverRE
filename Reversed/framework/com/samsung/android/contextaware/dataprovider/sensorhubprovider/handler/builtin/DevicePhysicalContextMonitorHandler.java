package com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin;

import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubModeHandler;
import java.util.concurrent.CopyOnWriteArrayList;

public class DevicePhysicalContextMonitorHandler extends SensorHubModeHandler {
    private static volatile DevicePhysicalContextMonitorHandler instance;

    public static DevicePhysicalContextMonitorHandler getInstance() {
        if (instance == null) {
            synchronized (DevicePhysicalContextMonitorHandler.class) {
                if (instance == null) {
                    instance = new DevicePhysicalContextMonitorHandler();
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
        copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_DEVICE_PHYSICAL_CONTEXT_MONITOR.getCode());
        return copyOnWriteArrayList;
    }
}
