package com.samsung.android.app;

import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;

public class SemMultiWindowManager {
    public static final int MODE_FREEFORM = 1;
    public static final int MODE_NONE = 0;
    public static final int MODE_PICTURE_IN_PICTURE = 4;
    public static final int MODE_SPLIT_SCREEN = 2;
    public static final int SNAP_WINDOW_GUIDE_VIEW_RATIO_16_9 = 2;
    public static final int SNAP_WINDOW_GUIDE_VIEW_RATIO_21_9 = 3;
    public static final int SNAP_WINDOW_GUIDE_VIEW_SIZE_DEFAULT = 0;
    public static final int SNAP_WINDOW_GUIDE_VIEW_SIZE_MINIMAL = 1;
    private static final String TAG = "SemMultiWindowManager";
    private MultiWindowManagerBridge mMultiWindowManager = new MultiWindowManagerBridge();

    public int getMode() {
        return this.mMultiWindowManager.getMultiWindowModeStates(0);
    }

    public void setMultiWindowEnabled(String str, boolean z) {
        this.mMultiWindowManager.setMultiWindowEnabled(str, "SEM_API", z);
    }

    public void showSnapWindowGuideView(int i) {
        this.mMultiWindowManager.showSnapWindowGuideView(i);
    }
}
