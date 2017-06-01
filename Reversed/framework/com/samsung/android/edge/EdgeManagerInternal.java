package com.samsung.android.edge;

import android.content.ComponentName;
import android.service.notification.StatusBarNotification;

public abstract class EdgeManagerInternal {
    public abstract boolean hideForNotification(StatusBarNotification statusBarNotification);

    public abstract void hideForWakeLock(String str, int i);

    public abstract void hideForWakeLockByWindow(String str, String str2);

    public abstract boolean showForNotification(StatusBarNotification statusBarNotification, boolean z, boolean z2);

    public abstract void showForResumedActivity(ComponentName componentName);

    public abstract boolean showForToast(String str, String str2);

    public abstract boolean showForWakeLock(String str, int i);

    public abstract boolean showForWakeLockByWindow(String str, String str2);

    public abstract boolean showForWakeUp(String str, int i);

    public abstract boolean showForWakeUpByWindow(String str, String str2, int i);
}
