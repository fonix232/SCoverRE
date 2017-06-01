package com.samsung.android.contextaware;

import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.ActivityTrackerHandler;

public class InterruptModeContextList {
    private static volatile InterruptModeContextList instance;

    public enum InterruptModeContextType implements IParserHandler {
        SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.getCode()) {
            public final ISensorHubParser getParserHandler() {
                return ActivityTrackerHandler.getInstance();
            }
        };
        
        private String code;

        private InterruptModeContextType(String str) {
            this.code = str;
        }

        public String getCode() {
            return this.code;
        }

        public ISensorHubParser getParserHandler() {
            return null;
        }
    }

    public static InterruptModeContextList getInstance() {
        if (instance == null) {
            synchronized (InterruptModeContextList.class) {
                if (instance == null) {
                    instance = new InterruptModeContextList();
                }
            }
        }
        return instance;
    }

    public final boolean isInterruptModeType(int i) {
        return isInterruptModeType(ContextList.getInstance().getServiceCode(i));
    }

    public final boolean isInterruptModeType(String str) {
        for (InterruptModeContextType -get0 : InterruptModeContextType.values()) {
            if (-get0.code.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
