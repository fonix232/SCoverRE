package com.samsung.android.content.smartclip;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.VideoView;
import java.util.ArrayList;

public class SmartClipRemoteRequestDispatcher {
    private static final String KEY_AIR_COMMAND_HIT_TEST_RESULT = "result";
    private static final String KEY_EVENT_INJECTION_EVENTS = "events";
    private static final String KEY_EVENT_INJECTION_WAIT_UNTIL_CONSUME = "waitUntilConsume";
    private static final String KEY_SCROLLABLE_AREA_INFO_ACTIVITY_NAME = "activityName";
    private static final String KEY_SCROLLABLE_AREA_INFO_DISPLAY_FRAME = "displayFrame";
    private static final String KEY_SCROLLABLE_AREA_INFO_PACKAGE_NAME = "packageName";
    private static final String KEY_SCROLLABLE_AREA_INFO_SCROLLABLE_VIEWS = "scrollableViews";
    private static final String KEY_SCROLLABLE_AREA_INFO_UNSCROLLABLE_VIEWS = "unscrollableViews";
    private static final String KEY_SCROLLABLE_AREA_INFO_VISIBLE_DISPLAY_FRAME = "visibleDisplayFrame";
    private static final String KEY_SCROLLABLE_AREA_INFO_WINDOW_LAYER = "windowLayer";
    private static final String KEY_SCROLLABLE_AREA_INFO_WINDOW_RECT = "windowRect";
    private static final String KEY_SCROLLABLE_VIEW_INFO_CHILD_VIEWS = "childViews";
    private static final String KEY_SCROLLABLE_VIEW_INFO_TARGET_VIEW = "targetView";
    private static final String KEY_VIEW_INFO_HASHCODE = "hashCode";
    private static final String KEY_VIEW_INFO_HIERARCHY = "hierarchy";
    private static final String KEY_VIEW_INFO_SCREEN_RECT = "screenRect";
    public static final String PERMISSION_EXTRACT_SMARTCLIP_DATA = "com.samsung.android.permission.EXTRACT_SMARTCLIP_DATA";
    public static final String PERMISSION_INJECT_INPUT_EVENT = "android.permission.INJECT_EVENTS";
    public static final String TAG = "SmartClipRemoteRequestDispatcher";
    private boolean DEBUG = false;
    private Context mContext;
    private Handler mHandler;
    private ViewRootImplGateway mViewRootImplGateway;

    public interface ViewRootImplGateway {
        void enqueueInputEvent(InputEvent inputEvent, InputEventReceiver inputEventReceiver, int i, boolean z);

        Handler getHandler();

        View getRootView();

        PointF getScaleFactor();

        PointF getTranslatedPoint();

        ViewRootImpl getViewRootImpl();
    }

    public SmartClipRemoteRequestDispatcher(Context context, ViewRootImplGateway viewRootImplGateway) {
        this.mContext = context;
        this.mViewRootImplGateway = viewRootImplGateway;
        this.mHandler = viewRootImplGateway.getHandler();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null && packageManager.hasSystemFeature("com.samsung.android.smartclip.DEBUG")) {
            this.DEBUG = true;
        }
    }

    private Bundle createViewInfoAsBundle(View view) {
        BaseBundle bundle = new Bundle();
        int hashCode = view.hashCode();
        Parcelable viewBoundsOnScreen = SmartClipUtils.getViewBoundsOnScreen(view);
        Iterable<String> viewHierarchyTable = getViewHierarchyTable(view);
        bundle.putInt(KEY_VIEW_INFO_HASHCODE, hashCode);
        bundle.putParcelable(KEY_VIEW_INFO_SCREEN_RECT, viewBoundsOnScreen);
        bundle.putStringArrayList(KEY_VIEW_INFO_HIERARCHY, viewHierarchyTable);
        if (this.DEBUG) {
            Log.m29d(TAG, "createScrollableViewInfo : Scrollable view hash=@" + Integer.toHexString(hashCode).toUpperCase() + " / Rect=" + viewBoundsOnScreen);
            for (String str : viewHierarchyTable) {
                Log.m29d(TAG, "createScrollableViewInfo :   + " + str);
            }
        }
        return bundle;
    }

    private void dispatchAirCommandHitTest(SmartClipRemoteRequestInfo smartClipRemoteRequestInfo) {
        if (this.mContext != null) {
            Object bundle = new Bundle();
            bundle.putInt("result", 0);
            sendResult(smartClipRemoteRequestInfo, bundle);
        }
    }

    private void dispatchInputEventInjection(SmartClipRemoteRequestInfo smartClipRemoteRequestInfo) {
        if (smartClipRemoteRequestInfo.mRequestData == null) {
            Log.m31e(TAG, "dispatchInputEventInjection : Empty input event!");
        } else if (smartClipRemoteRequestInfo.mRequestData instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) smartClipRemoteRequestInfo.mRequestData;
            transformTouchPosition(motionEvent);
            int action = motionEvent.getAction();
            if (!(this.DEBUG || action == 0 || action == 1)) {
                if (action == 3) {
                }
                enqueueInputEvent((InputEvent) smartClipRemoteRequestInfo.mRequestData, true);
            }
            Log.m29d(TAG, "dispatchInputEventInjection : Touch event action=" + MotionEvent.actionToString(action) + " x=" + ((int) motionEvent.getRawX()) + " y=" + ((int) motionEvent.getRawY()));
            enqueueInputEvent((InputEvent) smartClipRemoteRequestInfo.mRequestData, true);
        } else if (smartClipRemoteRequestInfo.mRequestData instanceof Bundle) {
            BaseBundle baseBundle = (Bundle) smartClipRemoteRequestInfo.mRequestData;
            final Parcelable[] parcelableArray = baseBundle.getParcelableArray(KEY_EVENT_INJECTION_EVENTS);
            if (parcelableArray != null) {
                final boolean z = baseBundle.getBoolean(KEY_EVENT_INJECTION_WAIT_UNTIL_CONSUME);
                long eventTime = parcelableArray.length > 0 ? ((InputEvent) parcelableArray[0]).getEventTime() : -1;
                if (this.DEBUG) {
                    Log.m29d(TAG, "dispatchInputEventInjection : wait = " + z + "  eventCount=" + parcelableArray.length);
                }
                for (Parcelable parcelable : parcelableArray) {
                    final InputEvent inputEvent = (InputEvent) parcelable;
                    if (inputEvent != null) {
                        if (inputEvent instanceof MotionEvent) {
                            transformTouchPosition(inputEvent);
                        }
                        final SmartClipRemoteRequestInfo smartClipRemoteRequestInfo2 = smartClipRemoteRequestInfo;
                        Runnable c10355 = new Runnable() {
                            public void run() {
                                long currentTimeMillis = System.currentTimeMillis();
                                if (SmartClipRemoteRequestDispatcher.this.DEBUG) {
                                    Log.m29d(SmartClipRemoteRequestDispatcher.TAG, "dispatchInputEventInjection : injecting.. " + inputEvent);
                                }
                                SmartClipRemoteRequestDispatcher.this.enqueueInputEvent(inputEvent, true);
                                if (inputEvent == parcelableArray[parcelableArray.length - 1]) {
                                    if (z) {
                                        SmartClipRemoteRequestDispatcher.this.sendResult(smartClipRemoteRequestInfo2, null);
                                    }
                                    Log.m29d(SmartClipRemoteRequestDispatcher.TAG, "dispatchInputEventInjection : injection finished. Elapsed = " + (System.currentTimeMillis() - currentTimeMillis));
                                }
                            }
                        };
                        long eventTime2 = inputEvent.getEventTime() - eventTime;
                        if (eventTime2 > 0) {
                            this.mHandler.postDelayed(c10355, eventTime2);
                        } else {
                            c10355.run();
                        }
                    }
                }
                return;
            }
            Log.m31e(TAG, "dispatchInputEventInjection : Event is null!");
        }
    }

    private void dispatchScrollableAreaInfo(SmartClipRemoteRequestInfo smartClipRemoteRequestInfo) {
        View rootView = this.mViewRootImplGateway.getRootView();
        if (rootView != null) {
            Iterable<View> arrayList = new ArrayList();
            Iterable<View> arrayList2 = new ArrayList();
            Parcelable viewBoundsOnScreen = SmartClipUtils.getViewBoundsOnScreen(rootView);
            Log.m29d(TAG, "dispatchScrollableAreaInfo : windowRect = " + viewBoundsOnScreen);
            findScrollableViews(rootView, viewBoundsOnScreen, arrayList, arrayList2);
            Object bundle = new Bundle();
            ArrayList arrayList3 = new ArrayList();
            for (View createViewInfoAsBundle : arrayList) {
                arrayList3.add(createViewInfoAsBundle(createViewInfoAsBundle));
            }
            Log.m29d(TAG, "dispatchScrollableAreaInfo : Scrollable view count = " + arrayList3.size());
            bundle.putParcelableArrayList(KEY_SCROLLABLE_AREA_INFO_SCROLLABLE_VIEWS, arrayList3);
            ArrayList arrayList4 = new ArrayList();
            for (View createViewInfoAsBundle2 : arrayList2) {
                arrayList4.add(createViewInfoAsBundle(createViewInfoAsBundle2));
            }
            Log.m29d(TAG, "dispatchScrollableAreaInfo : Unscrollable view count = " + arrayList4.size());
            bundle.putParcelableArrayList(KEY_SCROLLABLE_AREA_INFO_UNSCROLLABLE_VIEWS, arrayList4);
            bundle.putParcelable(KEY_SCROLLABLE_AREA_INFO_WINDOW_RECT, viewBoundsOnScreen);
            bundle.putInt(KEY_SCROLLABLE_AREA_INFO_WINDOW_LAYER, smartClipRemoteRequestInfo.mTargetWindowLayer);
            Parcelable rect = new Rect();
            Parcelable rect2 = new Rect();
            rootView.getWindowDisplayFrame(rect);
            rootView.getWindowVisibleDisplayFrame(rect2);
            bundle.putParcelable(KEY_SCROLLABLE_AREA_INFO_DISPLAY_FRAME, rect);
            bundle.putParcelable(KEY_SCROLLABLE_AREA_INFO_VISIBLE_DISPLAY_FRAME, rect2);
            String packageName = this.mContext.getPackageName();
            String str = null;
            bundle.putString("packageName", packageName);
            if (this.mContext instanceof Activity) {
                str = this.mContext.getClass().getName();
                bundle.putString(KEY_SCROLLABLE_AREA_INFO_ACTIVITY_NAME, str);
            }
            Log.m29d(TAG, "dispatchScrollableAreaInfo : Pkg=" + packageName + " Activity=" + str);
            sendResult(smartClipRemoteRequestInfo, bundle);
            return;
        }
        Log.m31e(TAG, "dispatchScrollableAreaInfo : Root view is null!");
    }

    private void dispatchScrollableViewInfo(SmartClipRemoteRequestInfo smartClipRemoteRequestInfo) {
        View rootView = this.mViewRootImplGateway.getRootView();
        if (rootView != null) {
            int i = ((Bundle) smartClipRemoteRequestInfo.mRequestData).getInt(KEY_VIEW_INFO_HASHCODE, -1);
            if (i != -1) {
                View findViewByHashCode = findViewByHashCode(rootView, i);
                Parcelable bundle = new Bundle();
                if (findViewByHashCode != null) {
                    bundle.putParcelable(KEY_SCROLLABLE_AREA_INFO_WINDOW_RECT, SmartClipUtils.getViewBoundsOnScreen(rootView));
                    bundle.putParcelable(KEY_SCROLLABLE_VIEW_INFO_TARGET_VIEW, createViewInfoAsBundle(findViewByHashCode));
                    ArrayList arrayList = new ArrayList();
                    if (findViewByHashCode instanceof ViewGroup) {
                        View view = findViewByHashCode;
                        int childCount = view.getChildCount();
                        for (int i2 = 0; i2 < childCount; i2++) {
                            arrayList.add(createViewInfoAsBundle(view.getChildAt(i2)));
                        }
                    }
                    bundle.putParcelableArrayList(KEY_SCROLLABLE_VIEW_INFO_CHILD_VIEWS, arrayList);
                    Log.m29d(TAG, "dispatchScrollableViewInfo : " + findViewByHashCode + "ChildCnt=" + arrayList.size());
                } else {
                    Log.m31e(TAG, "dispatchScrollableViewInfo : Could not found the view! hash=" + i);
                }
                sendResult(smartClipRemoteRequestInfo, bundle);
                return;
            }
            Log.m31e(TAG, "dispatchScrollableViewInfo : There is no hash value in request!");
        }
    }

    private void enqueueInputEvent(InputEvent inputEvent, boolean z) {
        if (this.mViewRootImplGateway == null) {
            Log.m31e(TAG, "enqueueInputEvent : Gateway is null!");
            return;
        }
        try {
            this.mViewRootImplGateway.enqueueInputEvent(inputEvent, null, 0, z);
        } catch (Exception e) {
            Log.m31e(TAG, "enqueueInputEvent : Exception thrown. e = " + e);
        }
    }

    private void findScrollableViews(View view, Rect rect, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        if (view != null && view.getVisibility() == 0 && view.getWidth() != 0 && view.getHeight() != 0) {
            String name = view.getClass().getName();
            String name2 = view.getClass().getSuperclass().getName();
            Rect viewBoundsOnScreen = SmartClipUtils.getViewBoundsOnScreen(view);
            if (Rect.intersects(rect, viewBoundsOnScreen)) {
                String toUpperCase = Integer.toHexString(view.hashCode()).toUpperCase();
                if ((view instanceof ScrollView) || (view instanceof AbsListView) || (view instanceof WebView)) {
                    if (this.DEBUG) {
                        Log.m29d(TAG, "findScrollableViews : Scrollable view = @" + toUpperCase + " " + name + "(" + name2 + ") / Rect=" + viewBoundsOnScreen + " H=" + viewBoundsOnScreen.height() + " Rect=" + viewBoundsOnScreen);
                    }
                    arrayList.add(view);
                    return;
                }
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                        findScrollableViews(viewGroup.getChildAt(childCount), rect, arrayList, arrayList2);
                    }
                }
                if ((view instanceof VideoView) || (view instanceof HorizontalScrollView)) {
                    if (this.DEBUG) {
                        Log.m29d(TAG, "findScrollableViews : Unscrollable view = @" + toUpperCase + " " + name + "(" + name2 + ") / Rect=" + viewBoundsOnScreen + " H=" + viewBoundsOnScreen.height() + " Rect=" + viewBoundsOnScreen);
                    }
                    arrayList2.add(view);
                    return;
                }
                Object obj = null;
                Object obj2 = null;
                Class cls = view.getClass();
                Class[] clsArr = new Class[]{MotionEvent.class};
                Class[] clsArr2 = new Class[]{Canvas.class};
                while (cls != null) {
                    String name3 = cls.getName();
                    if (!name3.startsWith("android.view.") && !name3.startsWith("android.widget.") && !name3.startsWith("com.android.internal.")) {
                        if (isMethodDeclared(cls, "dispatchTouchEvent", clsArr)) {
                            obj = 1;
                            if (this.DEBUG) {
                                Log.m29d(TAG, "findScrollableViews : @" + toUpperCase + " Have dispatchTouchEvent() " + name + " / " + cls.getName() + " / Rect=" + viewBoundsOnScreen);
                            }
                        }
                        if (isMethodDeclared(cls, "onTouchEvent", clsArr)) {
                            obj = 1;
                            if (this.DEBUG) {
                                Log.m29d(TAG, "findScrollableViews : @" + toUpperCase + " Have onTouchEvent() " + name + " / " + cls.getName() + " / Rect=" + viewBoundsOnScreen);
                            }
                        }
                        if (isMethodDeclared(cls, "onDraw", clsArr2)) {
                            obj2 = 1;
                            if (this.DEBUG) {
                                Log.m29d(TAG, "findScrollableViews : @" + toUpperCase + " Have onDraw() " + name + " / " + cls.getName() + " / Rect=" + viewBoundsOnScreen);
                            }
                        }
                        if (isMethodDeclared(cls, "draw", clsArr2)) {
                            obj2 = 1;
                            if (this.DEBUG) {
                                Log.m29d(TAG, "findScrollableViews : @" + toUpperCase + " Have draw() " + name + " / " + cls.getName() + " / Rect=" + viewBoundsOnScreen);
                            }
                        }
                        if (isMethodDeclared(cls, "dispatchDraw", clsArr2)) {
                            obj2 = 1;
                            if (this.DEBUG) {
                                Log.m29d(TAG, "findScrollableViews : @" + toUpperCase + " Have dispatchDraw() " + name + " / " + cls.getName() + " / Rect=" + viewBoundsOnScreen);
                            }
                        }
                        if (obj != null && r9 != null) {
                            break;
                        }
                        cls = cls.getSuperclass();
                    } else {
                        break;
                    }
                }
                if (obj != null) {
                    arrayList.add(view);
                }
            } else if (this.DEBUG) {
                Log.m29d(TAG, "findScrollableViews : Not in range - " + name + "(" + name2 + ") / Rect=" + viewBoundsOnScreen);
            }
        }
    }

    private View findViewByHashCode(View view, int i) {
        if (view == null) {
            return null;
        }
        if (view.hashCode() == i) {
            return view;
        }
        if (view instanceof ViewGroup) {
            View view2 = view;
            for (int childCount = view2.getChildCount() - 1; childCount >= 0; childCount--) {
                View findViewByHashCode = findViewByHashCode(view2.getChildAt(childCount), i);
                if (findViewByHashCode != null) {
                    return findViewByHashCode;
                }
            }
        }
        return null;
    }

    private ArrayList<String> getViewHierarchyTable(View view) {
        ArrayList<String> arrayList = new ArrayList();
        for (Class cls = view.getClass(); cls != null; cls = cls.getSuperclass()) {
            String name = cls.getName();
            arrayList.add(name);
            if ("android.view.View".equals(name)) {
                break;
            }
        }
        return arrayList;
    }

    private boolean isMethodDeclared(Class<?> cls, String str, Class<?>[] clsArr) {
        try {
            if (cls.getDeclaredMethod(str, clsArr) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private void sendResult(SmartClipRemoteRequestInfo smartClipRemoteRequestInfo, Parcelable parcelable) {
        ((SpenGestureManager) this.mContext.getSystemService("spengestureservice")).sendSmartClipRemoteRequestResult(new SmartClipRemoteRequestResult(smartClipRemoteRequestInfo.mRequestId, smartClipRemoteRequestInfo.mRequestType, parcelable));
    }

    private void transformTouchPosition(MotionEvent motionEvent) {
        View rootView = this.mViewRootImplGateway.getRootView();
        if (rootView == null) {
            Log.m31e(TAG, "transformTouchPosition : Root view is not exists");
            return;
        }
        Rect viewBoundsOnScreen = SmartClipUtils.getViewBoundsOnScreen(rootView);
        int i = viewBoundsOnScreen.left;
        int i2 = viewBoundsOnScreen.top;
        if (!(i == 0 && i2 == 0)) {
            float rawX = (motionEvent.getRawX() - ((float) i)) * 1.0f;
            float rawY = (motionEvent.getRawY() - ((float) i2)) * 1.0f;
            motionEvent.setLocation(rawX, rawY);
            if (this.DEBUG) {
                Log.m29d(TAG, "transformMotionEvent : Window offsetX=" + i + " offsetY=" + i2 + " dssScale=" + 1.0f + " destX=" + rawX + " destY=" + rawY);
            }
        }
        PointF translatedPoint = this.mViewRootImplGateway.getTranslatedPoint();
        if (!(translatedPoint == null || (translatedPoint.x == 0.0f && translatedPoint.y == 0.0f))) {
            motionEvent.offsetLocation(-translatedPoint.x, -translatedPoint.y);
        }
    }

    public void checkPermission(String str, int i, int i2) {
        Object obj = null;
        if (this.mContext.checkPermission(str, i, i2) == 0) {
            obj = 1;
        }
        if (obj == null) {
            String str2 = "Requires " + str + " permission";
            Log.m31e(TAG, "checkPermission : " + str2);
            throw new SecurityException(str2);
        }
    }

    public void dispatchSmartClipRemoteRequest(final SmartClipRemoteRequestInfo smartClipRemoteRequestInfo) {
        switch (smartClipRemoteRequestInfo.mRequestType) {
            case 2:
                checkPermission(PERMISSION_EXTRACT_SMARTCLIP_DATA, smartClipRemoteRequestInfo.mCallerPid, smartClipRemoteRequestInfo.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchAirCommandHitTest(smartClipRemoteRequestInfo);
                    }
                });
                return;
            case 3:
                checkPermission(PERMISSION_INJECT_INPUT_EVENT, smartClipRemoteRequestInfo.mCallerPid, smartClipRemoteRequestInfo.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchInputEventInjection(smartClipRemoteRequestInfo);
                    }
                });
                return;
            case 4:
                checkPermission(PERMISSION_EXTRACT_SMARTCLIP_DATA, smartClipRemoteRequestInfo.mCallerPid, smartClipRemoteRequestInfo.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchScrollableAreaInfo(smartClipRemoteRequestInfo);
                    }
                });
                return;
            case 5:
                checkPermission(PERMISSION_EXTRACT_SMARTCLIP_DATA, smartClipRemoteRequestInfo.mCallerPid, smartClipRemoteRequestInfo.mCallerUid);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        SmartClipRemoteRequestDispatcher.this.dispatchScrollableViewInfo(smartClipRemoteRequestInfo);
                    }
                });
                return;
            default:
                Log.m31e(TAG, "dispatchSmartClipRemoteRequest : Unknown request type(" + smartClipRemoteRequestInfo.mRequestType + ")");
                return;
        }
    }

    public boolean isDebugMode() {
        return this.DEBUG;
    }
}
