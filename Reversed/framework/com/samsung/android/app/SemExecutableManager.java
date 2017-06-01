package com.samsung.android.app;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.app.ISemExecuteManager.Stub;
import java.util.List;

public class SemExecutableManager {
    public static final String EXTRA_EXECUTABLE_ICON = "com.samsung.android.execute.extra.ICON";
    public static final String EXTRA_EXECUTABLE_INTENT = "com.samsung.android.execute.extra.INTENT";
    public static final String EXTRA_EXECUTABLE_NAME = "com.samsung.android.execute.extra.NAME";
    public static final String EXTRA_EXECUTABLE_SMALL_ICON = "com.samsung.android.execute.extra.SMALLICON";
    private static final String TAG = "SemExecutableManager";
    private static ISemExecuteManager mService;
    private final Context mContext;

    public SemExecutableManager(Context context) {
        this.mContext = context;
        mService = Stub.asInterface(ServiceManager.getService("execute"));
    }

    public SemExecutableInfo getExecutableInfo(String str) {
        try {
            return mService.getExecutableInfo(str);
        } catch (Exception e) {
            Log.m31e(TAG, "getExecutableInfo() failed: " + e);
            return null;
        }
    }

    public List<SemExecutableInfo> getExecutableInfos() {
        try {
            return mService.getExecutableInfos();
        } catch (Exception e) {
            Log.m31e(TAG, "getExecutableInfo() failed: " + e);
            return null;
        }
    }
}
