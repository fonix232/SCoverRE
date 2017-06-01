package com.samsung.android.contextaware;

import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.ActivityTrackerHandler;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.DevicePhysicalContextMonitorHandler;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.SLMonitorHandler;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class MultiModeContextList {
    private static volatile MultiModeContextList instance;

    public enum MultiModeContextType implements IParserHandler {
        SENSORHUB_RUNNER_ACTIVITY_TRACKER(DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString()) {
            public final ISensorHubParser getParserHandler() {
                return ActivityTrackerHandler.getInstance();
            }
        },
        SENSORHUB_RUNNER_STEP_LEVEL_MONITOR(DATA_TYPE.LIBRARY_DATATYPE_STEP_LEVEL_MONITOR.toString()) {
            public final ISensorHubParser getParserHandler() {
                return SLMonitorHandler.getInstance();
            }
        },
        SENSORHUB_RUNNER_DEVICE_PHYSICAL_CONTEXT_MONITOR(DATA_TYPE.LIBRARY_DATATYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR.toString()) {
            public final ISensorHubParser getParserHandler() {
                return DevicePhysicalContextMonitorHandler.getInstance();
            }
        };
        
        private final String code;

        private MultiModeContextType(String str) {
            this.code = str;
        }

        public String getCode() {
            return this.code;
        }

        public ISensorHubParser getParserHandler() {
            return null;
        }
    }

    public static MultiModeContextList getInstance() {
        if (instance == null) {
            synchronized (MultiModeContextList.class) {
                if (instance == null) {
                    instance = new MultiModeContextList();
                }
            }
        }
        return instance;
    }

    public final String getServiceCode(int i) {
        String str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        for (Enum enumR : MultiModeContextType.values()) {
            if (enumR.ordinal() == i) {
                str = enumR.getCode();
                break;
            }
        }
        if (str.isEmpty()) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_CODE_NULL_EXCEPTION.getCode()));
        }
        return str;
    }

    public final int getServiceOrdinal(String str) {
        for (Enum enumR : MultiModeContextType.values()) {
            if (enumR.getCode().equals(str)) {
                return enumR.ordinal();
            }
        }
        return 0;
    }

    public final boolean isIncluded(String str) {
        try {
            MultiModeContextType.valueOf(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public final boolean isMultiModeType(String str) {
        for (MultiModeContextType -get0 : MultiModeContextType.values()) {
            if (-get0.code.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
