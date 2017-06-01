package com.samsung.android.desktop;

import android.content.Context;
import android.util.Slog;
import com.samsung.android.desktopmode.IDesktopMode;
import com.samsung.android.desktopmode.IDesktopModeCallback;
import com.samsung.android.desktopmode.IDesktopModeCallback.Stub;
import java.util.HashMap;

public final class DesktopManager {
    public static final String LAUNCHER_APPNAME = "DesktopLauncher";
    public static final String LAUNCHER_CLSNAME = "com.android.launcher3.Launcher";
    public static final String LAUNCHER_PACKAGE = "com.sec.android.app.desktoplauncher";
    private static final String TAG = DesktopManager.class.getSimpleName();
    private Context mContext = null;
    private HashMap<String, IDesktopModeCallback> mRegisteredCallbacks = new HashMap();
    private IDesktopMode mService = null;

    public interface DesktopEventListener {
        void onChangeDesktopDockState(boolean z);

        void onChangeDesktopModeState(boolean z);
    }

    public DesktopManager(Context context, IDesktopMode iDesktopMode) {
        this.mService = iDesktopMode;
        this.mContext = context;
    }

    public boolean isDeskDockConnected() {
        boolean z = false;
        try {
            z = this.mService.isDesktopDockConnected();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return z;
    }

    public boolean isKnoxDesktopMode() {
        boolean z = false;
        try {
            z = this.mService.isDesktopMode();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return z;
    }

    public void registerListener(final DesktopEventListener desktopEventListener) {
        Slog.i(TAG, "registerListener : " + desktopEventListener);
        if (desktopEventListener != null) {
            if (this.mRegisteredCallbacks.containsKey(desktopEventListener.toString())) {
                Slog.w(TAG, "Already exist callback");
                return;
            }
            IDesktopModeCallback c00501 = new Stub() {
                public void onDesktopDockConnectionChanged(boolean z) {
                    Slog.d(DesktopManager.TAG, "onDesktopDockConnectionChanged() - " + z + ", listener : " + desktopEventListener);
                    if (desktopEventListener != null) {
                        desktopEventListener.onChangeDesktopDockState(z);
                    }
                }

                public void onDesktopModeChanged(boolean z) {
                    Slog.d(DesktopManager.TAG, "onDesktopModeChanged() - " + z + ", listener : " + desktopEventListener);
                    if (desktopEventListener != null) {
                        desktopEventListener.onChangeDesktopModeState(z);
                    }
                }
            };
            try {
                this.mService.registerStateCallback(c00501);
                this.mRegisteredCallbacks.put(desktopEventListener.toString(), c00501);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterListener(DesktopEventListener desktopEventListener) {
        if (desktopEventListener != null) {
            IDesktopModeCallback iDesktopModeCallback = (IDesktopModeCallback) this.mRegisteredCallbacks.get(desktopEventListener.toString());
            if (iDesktopModeCallback != null) {
                try {
                    this.mService.unregisterStateCallback(iDesktopModeCallback);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
