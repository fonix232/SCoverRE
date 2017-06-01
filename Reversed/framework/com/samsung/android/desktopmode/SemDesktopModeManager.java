package com.samsung.android.desktopmode;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.samsung.android.desktopmode.IDesktopModeCallback.Stub;
import java.lang.ref.WeakReference;
import java.util.Map;

public final class SemDesktopModeManager {
    public static final String LAUNCHER_APPNAME = "DesktopLauncher";
    public static final String LAUNCHER_CLSNAME = "com.android.launcher3.Launcher";
    public static final String LAUNCHER_PACKAGE = "com.sec.android.app.desktoplauncher";
    public static final int MODE_DISABLED = 16;
    public static final int MODE_DISABLING = 0;
    public static final int MODE_ENABLED = 48;
    public static final int MODE_ENABLING = 32;
    public static final int MODE_MASK = 48;
    public static final int STATE_BEFORE_CONFIG_CHANGE = 3;
    public static final int STATE_CONFIG_CHANGE_FINISHED = 5;
    public static final int STATE_CONFIG_CHANGE_STARTED = 4;
    public static final int STATE_LOADING_SCREEN_SHOWN = 2;
    public static final int STATE_MASK = 15;
    public static final int STATE_UNDEFINED = 0;
    public static final int STATE_WELCOME_DIALOG_SHOWN = 1;
    private static final String TAG = SemDesktopModeManager.class.getSimpleName();
    private static Map<EventListener, EventListenerDelegate> mEventListeners = null;
    private static final Object mLock = new Object();
    private static IDesktopMode mService = null;

    public interface EventListener {
        void onDesktopDockConnectionChanged(boolean z);

        void onDesktopModeChanged(boolean z);
    }

    private static class EventListenerDelegate extends Stub {
        private final WeakReference<EventListener> mEventListenerRef;

        EventListenerDelegate(EventListener eventListener) {
            this.mEventListenerRef = new WeakReference(eventListener);
        }

        void destroy() {
            this.mEventListenerRef.clear();
        }

        public void onDesktopDockConnectionChanged(boolean z) throws RemoteException {
            EventListener eventListener = (EventListener) this.mEventListenerRef.get();
            if (eventListener != null) {
                Log.d(SemDesktopModeManager.TAG, "onDesktopDockConnectionChanged() connected=" + z + ", listener=" + eventListener);
                eventListener.onDesktopDockConnectionChanged(z);
            }
        }

        public void onDesktopModeChanged(boolean z) throws RemoteException {
            EventListener eventListener = (EventListener) this.mEventListenerRef.get();
            if (eventListener != null) {
                Log.d(SemDesktopModeManager.TAG, "onDesktopModeChanged() enabled=" + z + ", listener=" + eventListener);
                eventListener.onDesktopModeChanged(z);
            }
        }

        public String toString() {
            EventListener eventListener = (EventListener) this.mEventListenerRef.get();
            return eventListener != null ? eventListener.toString() : super.toString();
        }
    }

    public SemDesktopModeManager(IDesktopMode iDesktopMode) {
        mService = iDesktopMode;
    }

    public static boolean isDesktopDockConnected() {
        if (mService == null) {
            Log.w(TAG, "isDesktopDockConnected: Desktop Mode feature not available");
            return false;
        }
        try {
            return mService.isDesktopDockConnected();
        } catch (Throwable e) {
            Log.w(TAG, "isDesktopDockConnected: Failure communicating with DesktopModeService", e);
            return false;
        }
    }

    public static boolean isDesktopMode() {
        if (mService == null) {
            Log.w(TAG, "isDesktopMode: Desktop Mode feature not available");
            return false;
        }
        try {
            return mService.isDesktopMode();
        } catch (Throwable e) {
            Log.w(TAG, "isDesktopMode: Failure communicating with DesktopModeService", e);
            return false;
        }
    }

    public static void registerListener(EventListener eventListener) {
        synchronized (mLock) {
            if (eventListener == null) {
                Log.w(TAG, "registerListener: Listener is null");
                return;
            } else if (mService == null) {
                Log.w(TAG, "registerListener: Desktop Mode feature not available");
                return;
            } else {
                if (mEventListeners == null) {
                    mEventListeners = new ArrayMap();
                }
                if (mEventListeners.containsKey(eventListener)) {
                    Log.w(TAG, "registerListener: " + eventListener + " already registered");
                    return;
                }
                IDesktopModeCallback eventListenerDelegate = new EventListenerDelegate(eventListener);
                try {
                    mService.registerStateCallback(eventListenerDelegate);
                    mEventListeners.put(eventListener, eventListenerDelegate);
                    Log.i(TAG, "registerListener: " + eventListener);
                } catch (Throwable e) {
                    Log.w(TAG, "registerListener: Failure communicating with DesktopModeService", e);
                }
            }
        }
    }

    public static void unregisterListener(EventListener eventListener) {
        synchronized (mLock) {
            if (eventListener == null) {
                Log.w(TAG, "unregisterListener: Listener is null");
                return;
            } else if (mService == null) {
                Log.w(TAG, "unregisterListener: Desktop Mode feature not available");
                return;
            } else if (mEventListeners == null) {
                return;
            } else {
                EventListenerDelegate eventListenerDelegate = (EventListenerDelegate) mEventListeners.remove(eventListener);
                if (eventListenerDelegate == null) {
                    Log.w(TAG, "unregisterListener: " + eventListener + " already unregistered");
                    return;
                }
                if (mEventListeners.isEmpty()) {
                    mEventListeners = null;
                }
                eventListenerDelegate.destroy();
                try {
                    mService.unregisterStateCallback(eventListenerDelegate);
                    Log.i(TAG, "unregisterListener: " + eventListener);
                } catch (Throwable e) {
                    Log.e(TAG, "unregisterListener: Failure removing event listener", e);
                }
            }
        }
    }

    public void forceDisableDesktopMode() {
        if (mService == null) {
            Log.w(TAG, "forceDisableDesktopMode: Desktop Mode feature not available");
        } else if (isDesktopMode()) {
            try {
                mService.setHdmiSettings(false);
            } catch (Throwable e) {
                Log.w(TAG, "forceDisableDesktopMode: Failure communicating with DesktopModeService", e);
            }
        } else {
            Log.w(TAG, "forceDisableDesktopMode: Already not in Desktop Mode");
        }
    }

    public Bundle getDesktopModeKillPolicy() {
        Bundle bundle = null;
        if (mService == null) {
            Log.w(TAG, "isDesktopMode: Desktop Mode feature not available");
            return null;
        }
        try {
            bundle = mService.getDesktopModeKillPolicy();
        } catch (Throwable e) {
            Log.w(TAG, "getDesktopModeKillPolicy: Failure communicating with DesktopModeService", e);
        }
        return bundle;
    }

    public Bundle getLaunchModePolicyList() {
        Bundle bundle = null;
        if (mService == null) {
            Log.w(TAG, "getLaunchModePolicyList: Desktop Mode feature not available");
            return null;
        }
        try {
            bundle = mService.getLaunchModePolicyList();
        } catch (Throwable e) {
            Log.w(TAG, "getLaunchModePolicyList: Failure communicating with DesktopModeService", e);
        }
        return bundle;
    }

    public int getLaunchPolicyForPackage(ApplicationInfo applicationInfo, ActivityInfo activityInfo) {
        int i = 0;
        if (mService == null) {
            Log.w(TAG, "getLaunchPolicyForPackage: Desktop Mode feature not available");
            return -1;
        }
        try {
            i = mService.getLaunchPolicyForPackage(applicationInfo, activityInfo);
        } catch (Throwable e) {
            Log.w(TAG, "getLaunchPolicyForPackage : Failure communicating with DesktopModeService", e);
        }
        return i;
    }

    public boolean getLaunchPolicyRunnable(ApplicationInfo applicationInfo) {
        boolean z = false;
        if (mService == null) {
            Log.w(TAG, "getLaunchPolicyRunnable: Desktop Mode feature not available");
            return false;
        }
        try {
            z = mService.getLaunchPolicyRunnable(applicationInfo);
        } catch (Throwable e) {
            Log.w(TAG, "getLaunchPolicyRunnable : Failure communicating with DesktopModeService", e);
        }
        return z;
    }

    public int getModeChangePolicy(String str) {
        int i = 0;
        if (mService == null) {
            Log.w(TAG, "isDesktopMode: Desktop Mode feature not available");
            return -1;
        }
        try {
            i = mService.getModeChangePolicy(str);
        } catch (Throwable e) {
            Log.w(TAG, "getModeChangePolicy: Failure communicating with DesktopModeService", e);
        }
        return i;
    }

    public boolean isDesktopModeAvailable() {
        return isDesktopModeAvailableEx(true, true);
    }

    public boolean isDesktopModeAvailableEx(boolean z, boolean z2) {
        if (mService == null) {
            Log.w(TAG, "isDesktopModeAvailable: Desktop Mode feature not available");
            return false;
        }
        try {
            return mService.isDesktopModeAvailableEx(z, z2);
        } catch (Throwable e) {
            Log.w(TAG, "isDesktopModeAvailable: Failure communicating with DesktopModeService", e);
            return false;
        }
    }

    public boolean isDesktopModeForPreparing() {
        if (mService == null) {
            Log.w(TAG, "isDesktopMode: Desktop Mode feature not available");
            return false;
        }
        try {
            return mService.isDesktopModeForPreparing();
        } catch (Throwable e) {
            Log.w(TAG, "isDesktopMode: Failure communicating with DesktopModeService", e);
            return false;
        }
    }

    public void setDefaultDisplayOn(boolean z, String str) {
        if (mService == null) {
            Log.w(TAG, "setDefaultDisplayOn: Desktop Mode feature not available");
        } else if (!isDesktopMode()) {
            Log.w(TAG, "setDefaultDisplayOn: Desktop Mode is not enabled");
        } else if (str == null || str.isEmpty()) {
            Log.w(TAG, "setDefaultDisplayOn: callerPkgName must not be null or empty");
        } else {
            try {
                mService.setDefaultDisplayOn(z, str);
            } catch (Throwable e) {
                Log.w(TAG, "setDefaultDisplayOn: Failure communicating with DesktopModeService", e);
            }
        }
    }

    public void setTouchScreenOn(boolean z, String str) {
        if (mService == null) {
            Log.w(TAG, "setTouchScreenOn: Desktop Mode feature not available");
        } else if (!isDesktopMode()) {
            Log.w(TAG, "setTouchScreenOn: Desktop Mode is not enabled");
        } else if (str == null || str.isEmpty()) {
            Log.w(TAG, "setTouchScreenOn: callerPkgName must not be null or empty");
        } else {
            try {
                mService.setTouchScreenOn(z, str);
            } catch (Throwable e) {
                Log.w(TAG, "setTouchScreenOn: Failure communicating with DesktopModeService", e);
            }
        }
    }
}
