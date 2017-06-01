package com.samsung.android.contextaware.dataprovider.androidprovider;

import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.contextaware.manager.fault.IContextAwareErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public enum AndroidProviderErrors implements IContextAwareErrors {
    SUCCESS("Success"),
    ERROR_UNKNOWN("ERROR : Unknown");
    
    private String message;

    private AndroidProviderErrors(String str) {
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
