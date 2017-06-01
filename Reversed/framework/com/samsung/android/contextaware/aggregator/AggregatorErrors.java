package com.samsung.android.contextaware.aggregator;

import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.contextaware.manager.fault.IContextAwareErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public enum AggregatorErrors implements IContextAwareErrors {
    SUCCESS("Success"),
    ERROR_UNKNOWN("ERROR : Unknown"),
    ERROR_ARRIVING_ON_STATUS_FAULT("ERROR : Status of arriving on POI is fault"),
    ERROR_UPDATED_CONTEXT_TYPE_FAULT("ERROR : Updated context type is fault"),
    ERROR_UPDATED_CONTEXT_NULL_EXCEPTION("ERROR : Updated context is null"),
    ERROR_SUB_COLLECTOR_FALSE("ERROR : Sub collector is false"),
    ERROR_SUB_COLLECTOR_NULL_EXCEPTION("ERROR : Sub collector is null");
    
    private String message;

    private AggregatorErrors(String str) {
        this.message = str;
    }

    public static final String getMessage(int i) {
        String str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        for (Enum enumR : values()) {
            if (enumR.ordinal() == i) {
                str = enumR.message;
                break;
            }
        }
        if (str.isEmpty()) {
            CaLogger.error("Message code is fault");
        }
        return str;
    }

    public final int getCode() {
        return ordinal();
    }

    public final String getMessage() {
        return this.message;
    }

    public void notifyFatalError() {
    }
}
