package com.samsung.android.app;

import android.app.ActivityManager;
import android.content.Context;
import java.util.List;

public class SemAppLockManager {
    private static final String TAG = "SemAppLockManager";
    private ActivityManager mActivityManager = ((ActivityManager) this.mContext.getSystemService("activity"));
    private final Context mContext;

    public SemAppLockManager(Context context) {
        this.mContext = context;
    }

    private ActivityManager getActivityManager() {
        if (this.mActivityManager == null) {
            this.mActivityManager = (ActivityManager) this.mContext.getSystemService("activity");
        }
        return this.mActivityManager;
    }

    public String getCheckAction() {
        return getActivityManager() != null ? this.mActivityManager.getAppLockedCheckAction() : null;
    }

    public List<String> getPackageList() {
        return getActivityManager() != null ? this.mActivityManager.getAppLockedPackageList() : null;
    }

    public boolean isPackageLocked(String str) {
        return getActivityManager() != null ? this.mActivityManager.isAppLockedPackage(str) : false;
    }
}
