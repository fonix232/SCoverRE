package com.samsung.android.bridge.multiwindow;

import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.GeneralUtil;
import android.util.Log;
import android.view.animation.ElasticCustom;
import android.view.animation.PathInterpolator;
import com.android.internal.telephony.IccCardConstants;
import com.samsung.android.framework.feature.MultiWindowFeatures;
import com.samsung.android.multiwindow.IMultiWindowDividerPanelListener;
import com.samsung.android.multiwindow.IMultiWindowEventListener;
import com.samsung.android.multiwindow.IMultiWindowFocusedFrameListener;
import java.util.HashMap;
import java.util.List;

public class MultiWindowManagerBridge {
    public static final int ANIM_RELAUNCH_FROM_FREEFORM = 2;
    public static final int ANIM_RELAUNCH_NONE = 0;
    public static final int ANIM_RELAUNCH_TO_FREEFORM = 1;
    public static final int ANIM_RELAUNCH_TO_SPLIT = 1;
    public static final int DIRECTION_PUSH_LEFT = 1;
    public static final int DIRECTION_PUSH_NONE = 0;
    public static final int DIRECTION_PUSH_RIGHT = 2;
    public static final int DIRECTION_PUSH_UNDEFIND = -1;
    public static final int FLAG_FREEFORM_TASK_CURRENT_USER = 1;
    public static final int FLAG_FREEFORM_TASK_MINIMIZE = 2;
    public static final float FREEFORM_DEFAULT_LONG_SIZE_RATIO = 0.5f;
    public static final float FREEFORM_DEFAULT_LONG_SIZE_RATIO_FOR_DEX = 0.5f;
    public static final float FREEFORM_DEFAULT_LONG_SIZE_RATIO_FOR_TABLET = 0.5f;
    public static final float FREEFORM_DEFAULT_SHORT_SIZE_RATIO = 0.67f;
    public static final float FREEFORM_DEFAULT_SHORT_SIZE_RATIO_FOR_DEX = 0.667f;
    public static final float FREEFORM_DEFAULT_SHORT_SIZE_RATIO_FOR_TABLET = 0.5f;
    public static final int FREEFORM_DIM_TYPE_NONE = 0;
    public static final int FREEFORM_DIM_TYPE_ROUNDED = 1;
    public static final int FREEFORM_DIM_TYPE_SQUARED = 2;
    public static final int FREEFORM_HIDDEN_MINIMIZE = 1;
    public static final int FREEFORM_HIDDEN_MOVE_BACK = 2;
    public static final int FREEFORM_HIDDEN_NONE = 0;
    public static final int FREEFORM_TRANSIT_MINIMIZE = 1;
    public static final int FREEFORM_TRANSIT_NONE = 0;
    public static final int FREEFORM_TRANSIT_UNMINIIZE = 2;
    public static final String METADATA_FREEFORM_DENSITY_CHANGE = "com.samsung.android.sdk.multiwindow.freeform.densitychange";
    public static final String METADATA_MULTIWINDOW_FLOATING_FORCE_HIDE = "com.samsung.android.sdk.multiwindow.force_hide_floating_multiwindow";
    public static final String METADATA_MULTIWINDOW_FREEFORM_BORDER = "com.samsung.android.sdk.multiwindow.freeform.border";
    public static final String METADATA_MULTIWINDOW_LAUNCH_IN_FOCUSEDSTACK = "com.samsung.android.sdk.multiwindow.freeform.launch_in_focusedstack";
    public static final String METADATA_MULTIWINDOW_MAX_HEIGHT = "com.samsung.android.sdk.multiwindow.maxHeight";
    public static final String METADATA_MULTIWINDOW_MAX_WIDTH = "com.samsung.android.sdk.multiwindow.maxWidth";
    public static final String METADATA_SUPPORT_MULTIWINDOW = "com.samsung.android.sdk.multiwindow.enable";
    public static final String METADATA_SUPPORT_MULTIWINDOW_BEFORE_HERO = "com.sec.android.support.multiwindow";
    public static final String MINIMIZE_CONTAINER_CLASS_NAME = "com.samsung.android.app.multiwindow.minimizecontainer.MinimizeContainerService";
    public static final String MINIMIZE_CONTAINER_PACKAGE_NAME = "com.samsung.android.app.multiwindow";
    private static final String MULTIWINDOW_MANAGER_CLASS_NAME = "com.samsung.android.multiwindow.MultiWindowManager";
    public static final int MULTIWINDOW_MODE_DOCKED_STATE = 2;
    public static final int MULTIWINDOW_MODE_FREEFORM_STATE = 1;
    public static final int MULTIWINDOW_MODE_NONE_STATE = 0;
    public static final int MULTIWINDOW_MODE_PINNED_STATE = 4;
    public static final int MW_ANIMATION_RESIZE = 201;
    public static final int MW_ANIMATION_SNAP_WINDOW = 203;
    public static final int MW_ANIMATION_SWAP = 202;
    public static final int MW_ANIMATION_UNSNAP_BY_DOCKED_TO_FULL = 205;
    public static final int MW_ANIMATION_UNSNAP_BY_SPLIT_RESIZE = 204;
    public static final int MW_DIVIDER_RESIZE_MARGIN = 30;
    public static int MW_FLAG_DIVIDER_BUTTON_INVISIBLE = MW_FLAG_SWAPPING;
    public static int MW_FLAG_SWAPPING = 1;
    public static final int NOTIFY_REASON_FORCE_CLEAR_MINIMIZE = 3;
    public static final int NOTIFY_REASON_MINIMIZE = 1;
    public static final int NOTIFY_REASON_UNMINIMIZE = 2;
    public static final int RESIZE_OVERLAP_AREA_IN_DP = 8;
    public static final int SLIDE_FREEFORM_ACCELERATION = 10;
    public static final int SNAP_WINDOW_GUIDE_VIEW_RATIO_16_9 = 2;
    public static final int SNAP_WINDOW_GUIDE_VIEW_RATIO_21_9 = 3;
    public static final int SNAP_WINDOW_GUIDE_VIEW_SIZE_DEFAULT = 0;
    public static final int SNAP_WINDOW_GUIDE_VIEW_SIZE_MINIMAL = 1;
    public static final String SNAP_WINDOW_INVISBLE_CALLER = "finish";
    public static final String SNAP_WINDOW_VISBLE_CALLER_APP_REQUEST = "appRequest";
    public static final String SNAP_WINDOW_VISBLE_CALLER_FULLAPP = "fromFullApp";
    public static final String SNAP_WINDOW_VISBLE_CALLER_RECENT = "fromRecent";
    public static final String SNAP_WINDOW_VISBLE_CALLER_SPLIT = "fromSplit";
    public static final String SNAP_WINDOW_VISBLE_CALLER_UNSET = "";
    public static final String TAG = "MultiWindowManagerBridge";
    private static IMultiWindowManagerBridge sIMultiWindowManagerBridge = ((IMultiWindowManagerBridge) createService(MULTIWINDOW_MANAGER_CLASS_NAME));

    public interface IMultiWindowManagerBridge {
        void activityPaused(IBinder iBinder, Bundle bundle);

        void alignTasksToStackBounds();

        void completeToggleSplitScreen();

        void enterFreeformTask(int i);

        boolean exitMultiWindow(IBinder iBinder);

        int getMultiWindowModeStates(int i);

        Rect getSnapTargetAspectRatioRect();

        List<RunningTaskInfo> getTopRunningTaskInfo(int i);

        boolean isForceResizable(ActivityInfo activityInfo);

        boolean isLaunchableForMultiInstance(ActivityInfo activityInfo);

        boolean isSnapWindowRunning();

        void maximizeStackByDivider(boolean z);

        boolean maximizeTopTask();

        boolean minimizeTopTask();

        void moveMultiWindowTasksToFullScreen();

        void registerMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener);

        void registerMultiWindowEventListener(IMultiWindowEventListener iMultiWindowEventListener);

        void registerMultiWindowFocusedFrameListener(IMultiWindowFocusedFrameListener iMultiWindowFocusedFrameListener);

        boolean removeSearchedTask(String str);

        boolean removeTaskIfNeeded(boolean z);

        void setAutoResizingEnabled(boolean z);

        void setDividerButtonsDimLayer(boolean z, float f, int i);

        void setDockedStackDividerButtonsTouchRegion(Rect rect);

        void setMultiWindowEnabled(String str, String str2, boolean z);

        void setMultiWindowEnabledForUser(String str, String str2, boolean z, int i);

        void setSnapWindow(boolean z, Rect rect, int i, String str);

        void showRecentApps();

        void showSnapWindowGuideView(int i);

        void startDividerDragging();

        boolean startFreeform();

        void stopDividerDragging();

        void unregisterMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener);

        void updateTaskPositionInTaskBar(HashMap<Integer, Point> hashMap);
    }

    public static class Utils {
        private static final boolean DEBUG_DENSITY = false;
        private static boolean isTablet = GeneralUtil.isTablet();
        public static ElasticCustom sElastic50Custom = new ElasticCustom(1.0f, 0.7f);
        public static PathInterpolator sSineInOut33Interpolator = new PathInterpolator(0.33f, 0.0f, MultiWindowManagerBridge.FREEFORM_DEFAULT_SHORT_SIZE_RATIO, 1.0f);
        public static PathInterpolator sSineInOut80Interpolator = new PathInterpolator(0.33f, 0.0f, 0.2f, 1.0f);
        public static PathInterpolator sSineOut90Interpolator = new PathInterpolator(0.17f, 0.17f, 0.1f, 1.0f);

        public static int convertToConfigurationOrientation(int i) {
            switch (i) {
                case 0:
                case 6:
                case 8:
                case 11:
                    return 2;
                case 1:
                case 7:
                case 9:
                case 12:
                    return 1;
                default:
                    return 0;
            }
        }

        private static String densityBucketToString(int i) {
            switch (i) {
                case 120:
                    return "DENSITY_LOW";
                case 160:
                    return "DENSITY_MEDIUM";
                case 240:
                    return "DENSITY_HIGH";
                case 320:
                    return "DENSITY_XHIGH";
                case DisplayMetrics.DENSITY_XXHIGH /*480*/:
                    return "DENSITY_XXHIGH";
                case DisplayMetrics.DENSITY_XXXHIGH /*640*/:
                    return "DENSITY_XXXHIGH";
                default:
                    return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            }
        }

        private static int getDensityBucket(int i) {
            return i <= 120 ? 120 : i <= 160 ? 160 : i <= 240 ? 240 : i <= 320 ? 320 : i <= DisplayMetrics.DENSITY_XXHIGH ? DisplayMetrics.DENSITY_XXHIGH : DisplayMetrics.DENSITY_XXXHIGH;
        }

        public static int getFreeformDensity(int i, int i2) {
            if (i >= 600 || isTablet) {
                return -1;
            }
            int i3 = (i2 * 75) / 100;
            int densityBucket = getDensityBucket(i2);
            if (getDensityBucket(i3) >= densityBucket) {
                return i3;
            }
            int i4 = densityBucket;
            return getMinimumDensityWithinBucket(densityBucket);
        }

        private static int getMinimumDensityWithinBucket(int i) {
            switch (i) {
                case 160:
                    return 121;
                case 240:
                    return 161;
                case 320:
                    return 241;
                case DisplayMetrics.DENSITY_XXHIGH /*480*/:
                    return 321;
                case DisplayMetrics.DENSITY_XXXHIGH /*640*/:
                    return 481;
                default:
                    return i;
            }
        }

        public static boolean isForcePreserveWindowConfigChange(int i) {
            return (-1612791173 & i) == 0;
        }

        public static boolean isFreeformMaximizing(int i, int i2) {
            boolean z = true;
            if (!MultiWindowFeatures.SAMSUNG_MULTIWINDOW_DYNAMIC_ENABLED) {
                return false;
            }
            if (i != 2) {
                z = false;
            } else if (!(i2 == 1 || i2 == 3)) {
                z = false;
            }
            return z;
        }

        public static boolean isMovingToFreeform(int i, int i2) {
            boolean z = true;
            boolean z2 = false;
            if (!MultiWindowFeatures.SAMSUNG_MULTIWINDOW_DYNAMIC_ENABLED) {
                return false;
            }
            if (i == 1 || i == 3) {
                if (i2 != 2) {
                    z = false;
                }
                z2 = z;
            }
            return z2;
        }
    }

    private static Object createService(String str) {
        Object obj = null;
        try {
            obj = Class.forName(str).newInstance();
        } catch (ClassNotFoundException e) {
            Log.m37w(TAG, "Not created this class : " + str + ", reason : " + e);
        } catch (InstantiationException e2) {
            Log.m37w(TAG, "Not created this class : " + str + ", reason : " + e2);
        } catch (IllegalAccessException e3) {
            Log.m37w(TAG, "Not created this class : " + str + ", reason : " + e3);
        } catch (Exception e4) {
            Log.m37w(TAG, "Not created this class : " + str + ", reason : " + e4);
        }
        return obj;
    }

    public void activityPaused(IBinder iBinder, Bundle bundle) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.activityPaused(iBinder, bundle);
        }
    }

    public void alignTasksToStackBounds() {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.alignTasksToStackBounds();
        }
    }

    public void completeToggleSplitScreen() {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.completeToggleSplitScreen();
        }
    }

    public void enterFreeformTask(int i) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.enterFreeformTask(i);
        }
    }

    public boolean exitMultiWindow(IBinder iBinder) {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.exitMultiWindow(iBinder) : false;
    }

    public int getMultiWindowModeStates(int i) {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.getMultiWindowModeStates(i) : 0;
    }

    public Rect getSnapTargetAspectRatioRect() {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.getSnapTargetAspectRatioRect() : null;
    }

    public List<RunningTaskInfo> getTopRunningTaskInfo(int i) {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.getTopRunningTaskInfo(i) : null;
    }

    public boolean isForceResizable(ActivityInfo activityInfo) {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.isForceResizable(activityInfo) : false;
    }

    public boolean isLaunchableForMultiInstance(ActivityInfo activityInfo) {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.isLaunchableForMultiInstance(activityInfo) : false;
    }

    public boolean isSnapWindowRunning() {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.isSnapWindowRunning() : false;
    }

    public void maximizeStackByDivider(boolean z) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.maximizeStackByDivider(z);
        }
    }

    public boolean maximizeTopTask() {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.maximizeTopTask() : false;
    }

    public boolean minimizeTopTask() {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.minimizeTopTask() : false;
    }

    public void moveMultiWindowTasksToFullScreen() {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.moveMultiWindowTasksToFullScreen();
        }
    }

    public void registerMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.registerMultiWindowDividerPanelListener(iMultiWindowDividerPanelListener);
        }
    }

    public void registerMultiWindowEventListener(IMultiWindowEventListener iMultiWindowEventListener) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.registerMultiWindowEventListener(iMultiWindowEventListener);
        }
    }

    public void registerMultiWindowFocusedFrameListener(IMultiWindowFocusedFrameListener iMultiWindowFocusedFrameListener) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.registerMultiWindowFocusedFrameListener(iMultiWindowFocusedFrameListener);
        }
    }

    public boolean removeSearchedTask(String str) {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.removeSearchedTask(str) : false;
    }

    public boolean removeTaskIfNeeded(boolean z) {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.removeTaskIfNeeded(z) : false;
    }

    public void setAutoResizingEnabled(boolean z) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.setAutoResizingEnabled(z);
        }
    }

    public void setDividerButtonsDimLayer(boolean z, float f, int i) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.setDividerButtonsDimLayer(z, f, i);
        }
    }

    public void setDockedStackDividerButtonsTouchRegion(Rect rect) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.setDockedStackDividerButtonsTouchRegion(rect);
        }
    }

    public void setMultiWindowEnabled(String str, String str2, boolean z) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.setMultiWindowEnabled(str, str2, z);
        }
    }

    public void setMultiWindowEnabledForUser(String str, String str2, boolean z, int i) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.setMultiWindowEnabledForUser(str, str2, z, i);
        }
    }

    public void setSnapWindow(boolean z, Rect rect, int i, String str) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.setSnapWindow(z, rect, i, str);
        }
    }

    public void showRecentApps() {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.showRecentApps();
        }
    }

    public void showSnapWindowGuideView(int i) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.showSnapWindowGuideView(i);
        }
    }

    public void startDividerDragging() {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.startDividerDragging();
        }
    }

    public boolean startFreeform() {
        return sIMultiWindowManagerBridge != null ? sIMultiWindowManagerBridge.startFreeform() : false;
    }

    public void stopDividerDragging() {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.stopDividerDragging();
        }
    }

    public void unregisterMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.unregisterMultiWindowDividerPanelListener(iMultiWindowDividerPanelListener);
        }
    }

    public void updateTaskPositionInTaskBar(HashMap<Integer, Point> hashMap) {
        if (sIMultiWindowManagerBridge != null) {
            sIMultiWindowManagerBridge.updateTaskPositionInTaskBar(hashMap);
        }
    }
}
