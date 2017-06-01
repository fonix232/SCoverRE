package com.samsung.android.content.smartclip;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.InputEvent;
import java.util.ArrayList;

public class SemRemoteAppDataExtractionManager {
    private static final String TAG = "SemRemoteAppDataExtractionManager";
    private SpenGestureManager mManager = null;

    public SemRemoteAppDataExtractionManager(Context context) {
        if (context == null) {
            Log.m31e(TAG, "SemRemoteAppDataExtractionManager : Context is null! ");
            throw new RuntimeException("Context should not be null!");
        }
        this.mManager = (SpenGestureManager) context.getSystemService("spengestureservice");
        if (this.mManager == null) {
            Log.m31e(TAG, "SemRemoteAppDataExtractionManager : Failed to connect to the service");
            throw new RuntimeException("Failed to connect to the service. Feature is not supported");
        }
    }

    public Bundle getScrollableAreaInfo(Rect rect, IBinder iBinder) {
        if (rect != null) {
            return this.mManager.getScrollableAreaInfo(rect, iBinder);
        }
        Log.m31e(TAG, "getScrollableAreaInfo : rect is null!");
        return null;
    }

    public Bundle getScrollableViewInfo(Rect rect, int i, IBinder iBinder) {
        if (rect != null) {
            return this.mManager.getScrollableViewInfo(rect, i, iBinder);
        }
        Log.m31e(TAG, "getScrollableViewInfo : rect is null!");
        return null;
    }

    public SemSmartClipDataRepository getSmartClipDataByScreenRect(Rect rect, IBinder iBinder, int i) {
        return this.mManager.getSmartClipDataByScreenRect(rect, iBinder, i);
    }

    public SemSmartClipDataRepository getSmartClipDataFromCurrentScreen() {
        return getSmartClipDataByScreenRect(null, null, 1);
    }

    public boolean injectInputEvent(int i, int i2, ArrayList<InputEvent> arrayList, boolean z, IBinder iBinder) {
        if (arrayList == null || arrayList.size() == 0) {
            Log.m31e(TAG, "injectInputEvent : Empty input event");
            return false;
        }
        this.mManager.injectInputEvent(i, i2, arrayList, z, iBinder);
        return true;
    }
}
