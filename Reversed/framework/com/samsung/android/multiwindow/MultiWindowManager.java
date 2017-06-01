package com.samsung.android.multiwindow;

import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.util.Singleton;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge.IMultiWindowManagerBridge;
import com.samsung.android.multiwindow.IMultiWindowManager.Stub;
import java.util.HashMap;
import java.util.List;

public class MultiWindowManager implements IMultiWindowManagerBridge {
    public static final String TAG = MultiWindowManager.class.getSimpleName();
    private static final Singleton<IMultiWindowManager> gDefault = new C02261();

    static class C02261 extends Singleton<IMultiWindowManager> {
        C02261() {
        }

        protected IMultiWindowManager create() {
            return Stub.asInterface(ServiceManager.getService("multiwindow"));
        }
    }

    private static IMultiWindowManager getDefault() {
        return (IMultiWindowManager) gDefault.get();
    }

    private static void warningException(Exception exception) {
        Log.w(TAG, "warningException() : caller=" + Debug.getCaller() + exception.getMessage());
    }

    public void activityPaused(IBinder iBinder, Bundle bundle) {
        try {
            getDefault().activityPaused(iBinder, bundle);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void alignTasksToStackBounds() {
        try {
            getDefault().alignTasksToStackBounds();
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void changeFreeformMode() {
        try {
            getDefault().changeFreeformMode();
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void completeToggleSplitScreen() {
        try {
            getDefault().completeToggleSplitScreen();
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void enterFreeformTask(int i) {
        try {
            getDefault().enterFreeformTask(i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public boolean exitMultiWindow(IBinder iBinder) {
        try {
            return getDefault().exitMultiWindow(iBinder);
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public List<RunningTaskInfo> getFreeformTasks(int i) throws SecurityException {
        try {
            return getDefault().getFreeformTasks(i);
        } catch (Exception e) {
            warningException(e);
            return null;
        }
    }

    public int getImeTargetFreeformTaskId() {
        try {
            return getDefault().getImeTargetFreeformTaskId();
        } catch (Exception e) {
            warningException(e);
            return -1;
        }
    }

    public int getMultiWindowModeStates(int i) {
        try {
            return getDefault().getMultiWindowModeStates(i);
        } catch (Exception e) {
            warningException(e);
            return 0;
        }
    }

    public Rect getSnapTargetAspectRatioRect() {
        try {
            return getDefault().getSnapTargetAspectRatioRect();
        } catch (Exception e) {
            warningException(e);
            return null;
        }
    }

    public List<RunningTaskInfo> getTopRunningTaskInfo(int i) {
        try {
            return getDefault().getTopRunningTaskInfo(i);
        } catch (Exception e) {
            warningException(e);
            return null;
        }
    }

    public boolean hasDockedStack() {
        try {
            return getDefault().hasDockedStack();
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public boolean isExpandedDockedStack() {
        try {
            return getDefault().isExpandedDockedStack();
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public boolean isForceResizable(ActivityInfo activityInfo) {
        try {
            return getDefault().isForceResizable(activityInfo);
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public boolean isLaunchableForMultiInstance(ActivityInfo activityInfo) {
        try {
            return getDefault().isLaunchableForMultiInstance(activityInfo);
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public boolean isSnapWindowRunning() {
        try {
            return getDefault().isSnapWindowRunning();
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public void makeGhostFreeform(IBinder iBinder, int i) {
        try {
            getDefault().makeGhostFreeform(iBinder, i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void maximizeStackByDivider(boolean z) {
        try {
            getDefault().maximizeStackByDivider(z);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public boolean maximizeTopTask() {
        try {
            return getDefault().maximizeTopTask();
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public void minimizeAllFreeform(int i) {
        try {
            getDefault().minimizeAllFreeform(i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void minimizeOhterFreeforms(IBinder iBinder) {
        try {
            getDefault().minimizeOhterFreeforms(iBinder);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void minimizeTask(int i) {
        try {
            getDefault().minimizeTask(i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public boolean minimizeTopTask() {
        try {
            return getDefault().minimizeTopTask();
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public boolean moveActivityTaskToBack(IBinder iBinder, boolean z, boolean z2) {
        try {
            return getDefault().moveActivityTaskToBack(iBinder, z, z2);
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public void moveMultiWindowTasksToFullScreen() {
        try {
            getDefault().moveMultiWindowTasksToFullScreen();
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void registerMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) {
        try {
            getDefault().registerMultiWindowDividerPanelListener(iMultiWindowDividerPanelListener);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void registerMultiWindowEventListener(IMultiWindowEventListener iMultiWindowEventListener) {
        try {
            getDefault().registerMultiWindowEventListener(iMultiWindowEventListener);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void registerMultiWindowFocusedFrameListener(IMultiWindowFocusedFrameListener iMultiWindowFocusedFrameListener) {
        try {
            getDefault().registerMultiWindowFocusedFrameListener(iMultiWindowFocusedFrameListener);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void registerMultiWindowServiceCallback(IMultiWindowCallback iMultiWindowCallback) {
        try {
            getDefault().registerMultiWindowServiceCallback(iMultiWindowCallback);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void removeFreeformTasks(int i) throws SecurityException {
        try {
            getDefault().removeFreeformTasks(i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public boolean removeSearchedTask(String str) {
        try {
            return getDefault().removeSearchedTask(str);
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public boolean removeTaskIfNeeded(boolean z) {
        try {
            return getDefault().removeTaskIfNeeded(z);
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public void reportMinimizeContainerBounds(Rect rect) {
        try {
            getDefault().reportMinimizeContainerBounds(rect);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void rotateFreeformTask(IBinder iBinder) {
        try {
            getDefault().rotateFreeformTask(iBinder);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void setAutoResizingEnabled(boolean z) {
        try {
            getDefault().setAutoResizingEnabled(z);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void setDividerButtonsDimLayer(boolean z, float f, int i) {
        try {
            getDefault().setDividerButtonsDimLayer(z, f, i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void setDockedStackDividerButtonsTouchRegion(Rect rect) {
        try {
            getDefault().setDockedStackDividerButtonsTouchRegion(rect);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void setLaunchBounds(int i, Rect rect) {
        try {
            getDefault().setLaunchBounds(i, rect);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void setMultiWindowEnabled(String str, String str2, boolean z) {
        setMultiWindowEnabledForUser(str, str2, z, UserHandle.myUserId());
    }

    public void setMultiWindowEnabledForUser(String str, String str2, boolean z, int i) {
        try {
            getDefault().setMultiWindowEnabledForUser(str, str2, z, i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void setSlideMode(int i, boolean z) {
        try {
            getDefault().setSlideMode(i, z);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void setSnapWindow(boolean z, Rect rect, int i, String str) {
        try {
            getDefault().setSnapWindow(z, rect, i, str);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void showRecentApps() {
        try {
            getDefault().showRecentApps();
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void showSnapWindowGuideView(int i) {
        try {
            getDefault().showSnapWindowGuideView(i);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void slideFreeform(IBinder iBinder) {
        try {
            getDefault().slideFreeform(iBinder);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void slideOrUnslideAllFreeform(boolean z) {
        try {
            getDefault().slideOrUnslideAllFreeform(z);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void startDividerDragging() {
        try {
            getDefault().startDividerDragging();
        } catch (Exception e) {
            warningException(e);
        }
    }

    public boolean startFreeform() {
        try {
            return getDefault().startFreeform();
        } catch (Exception e) {
            warningException(e);
            return false;
        }
    }

    public void startResizingFreeformTask(IBinder iBinder, int i, int i2) {
        try {
            getDefault().startResizingFreeformTask(iBinder, i, i2);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void stopDividerDragging() {
        try {
            getDefault().stopDividerDragging();
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void unregisterMultiWindowDividerPanelListener(IMultiWindowDividerPanelListener iMultiWindowDividerPanelListener) {
        try {
            getDefault().unregisterMultiWindowDividerPanelListener(iMultiWindowDividerPanelListener);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void unregisterMultiWindowServiceCallback(IMultiWindowCallback iMultiWindowCallback) {
        try {
            getDefault().unregisterMultiWindowServiceCallback(iMultiWindowCallback);
        } catch (Exception e) {
            warningException(e);
        }
    }

    public void updateTaskPositionInTaskBar(HashMap<Integer, Point> hashMap) {
        try {
            getDefault().updateTaskPositionInTaskBar(hashMap);
        } catch (Exception e) {
            warningException(e);
        }
    }
}
