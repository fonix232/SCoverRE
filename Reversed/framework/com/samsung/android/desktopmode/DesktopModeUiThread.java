package com.samsung.android.desktopmode;

import android.os.Handler;
import android.os.HandlerThread;

public class DesktopModeUiThread extends HandlerThread {
    private static Handler sHandler;
    private static DesktopModeUiThread sInstance;

    private DesktopModeUiThread() {
        super("DesktopModeUiThread", -4);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new DesktopModeUiThread();
            sInstance.start();
            sHandler = new Handler(sInstance.getLooper());
        }
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (DesktopModeUiThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }
}
