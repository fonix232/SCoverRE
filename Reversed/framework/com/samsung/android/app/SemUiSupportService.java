package com.samsung.android.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.Window.Callback;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.policy.PhoneWindow;

public class SemUiSupportService extends Service implements Callback, KeyEvent.Callback {
    private static final String TAG = "SemUiSupportService";
    protected Context mContext;
    private View mDecor;
    private Window mWindow;
    private LayoutParams mWindowAttributes;
    protected WindowManager mWindowManager;

    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        Log.m33i(TAG, "addContentView() view, params");
        this.mWindow.addContentView(view, layoutParams);
    }

    public Window addWindow(View view, int i, int i2, int i3) {
        return addWindow(view, i, i2, i3, 0);
    }

    public Window addWindow(View view, int i, int i2, int i3, int i4) {
        Log.m33i(TAG, "addWindow view");
        Object obj = null;
        IllegalArgumentException illegalArgumentException = null;
        ViewGroup.LayoutParams createLayoutParams = createLayoutParams();
        if (this.mContext == null) {
            return null;
        }
        Window phoneWindow = new PhoneWindow(this.mContext);
        phoneWindow.requestFeature(1);
        phoneWindow.setWindowManager(this.mWindowManager, null, null);
        createLayoutParams.width = i;
        createLayoutParams.height = i2;
        createLayoutParams.type = i3;
        createLayoutParams.flags |= i4;
        phoneWindow.setAttributes(createLayoutParams);
        phoneWindow.setContentView(view);
        View decorView = phoneWindow.getDecorView();
        if (decorView != null) {
            decorView.setVisibility(0);
            do {
                try {
                    this.mWindowManager.addView(decorView, createLayoutParams);
                    continue;
                } catch (IllegalArgumentException e) {
                    if (obj != null) {
                        illegalArgumentException = e;
                        break;
                    }
                    Log.m31e(TAG, "View Problem: " + view.toString() + "w: " + i + "h: " + i2 + "t: " + i3);
                    obj = 1;
                    try {
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException e2) {
                        continue;
                    }
                }
            } while (obj != null);
        }
        if (illegalArgumentException == null) {
            return phoneWindow;
        }
        throw illegalArgumentException;
    }

    public LayoutParams createLayoutParams() {
        Log.m33i(TAG, "createLayoutParams");
        LayoutParams layoutParams = new LayoutParams(2002, 17040192, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.softInputMode = 32;
        layoutParams.setTitle(getClass().getName());
        return layoutParams;
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        return this.mWindow.superDispatchGenericMotionEvent(motionEvent);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        DispatcherState dispatcherState = null;
        if (this.mWindow.superDispatchKeyEvent(keyEvent)) {
            return true;
        }
        if (this.mDecor != null) {
            dispatcherState = this.mDecor.getKeyDispatcherState();
        }
        return keyEvent.dispatch(this, dispatcherState, this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        return this.mWindow.superDispatchKeyShortcutEvent(keyEvent);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.mWindow.superDispatchTouchEvent(motionEvent);
    }

    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        return this.mWindow.superDispatchTrackballEvent(motionEvent);
    }

    public View findViewById(int i) {
        Log.m33i(TAG, "findViewById()");
        return this.mWindow.findViewById(i);
    }

    public LayoutParams getAttributes() {
        Log.m33i(TAG, "getAttributes()");
        return this.mWindowAttributes;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public LayoutParams getWindowAttributes() {
        Log.m33i(TAG, "getWindowAttributes()");
        return this.mWindowAttributes;
    }

    public void onActionModeFinished(ActionMode actionMode) {
    }

    public void onActionModeStarted(ActionMode actionMode) {
    }

    public void onAttachedToWindow() {
    }

    public IBinder onBind(Intent intent) {
        Log.m33i(TAG, "onBind() : " + this);
        return null;
    }

    public void onContentChanged() {
    }

    public void onCreate() {
        super.onCreate();
        Log.m33i(TAG, "onCreate() : " + this);
        this.mWindowManager = (WindowManager) getSystemService("window");
        this.mContext = this;
        try {
            PackageManager packageManager = getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
                if (applicationInfo != null) {
                    this.mContext = new ContextThemeWrapper((Context) this, applicationInfo.theme);
                    Log.m33i(TAG, "loaded theme = " + applicationInfo.theme);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.mWindowAttributes = createLayoutParams();
        this.mWindow = new PhoneWindow(this.mContext);
        if (this.mWindow != null) {
            this.mWindow.setWindowManager(this.mWindowManager, null, null);
            this.mWindowManager = this.mWindow.getWindowManager();
            this.mWindow.requestFeature(1);
            this.mWindow.setCallback(this);
        }
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        return false;
    }

    public View onCreatePanelView(int i) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.mDecor != null) {
                this.mWindowManager.removeView(this.mDecor);
            }
        } catch (IllegalArgumentException e) {
            Log.m33i(TAG, "Already remove this view : " + this.mDecor);
        }
        Log.m33i(TAG, "onDestroy() : " + this);
        this.mContext = null;
        this.mDecor = null;
    }

    public void onDetachedFromWindow() {
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        stopService();
        return true;
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return false;
    }

    public boolean onMenuOpened(int i, Menu menu) {
        return false;
    }

    public void onPanelClosed(int i, Menu menu) {
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        return false;
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        return false;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        Log.m33i(TAG, "onStartCommand()");
        if (this.mWindow != null) {
            this.mWindow.setAttributes(this.mWindowAttributes);
            this.mDecor = this.mWindow.getDecorView();
            if (this.mDecor != null) {
                this.mDecor.setVisibility(0);
                ViewGroup.LayoutParams attributes = this.mWindow.getAttributes();
                if ((attributes.softInputMode & 256) == 0) {
                    ViewGroup.LayoutParams layoutParams = new LayoutParams();
                    layoutParams.copyFrom(attributes);
                    layoutParams.softInputMode |= 256;
                    attributes = layoutParams;
                }
                try {
                    this.mWindowManager.addView(this.mDecor, attributes);
                } catch (Throwable e) {
                    e.printStackTrace();
                    stopSelf();
                }
            }
        }
        return 1;
    }

    public void onWindowAttributesChanged(LayoutParams layoutParams) {
    }

    public void onWindowFocusChanged(boolean z) {
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        return null;
    }

    public boolean removeWindow(Window window) {
        Log.m33i(TAG, "removeWindow window");
        if (window == null) {
            return false;
        }
        this.mWindowManager.removeView(window.getDecorView());
        return true;
    }

    public void setAttributes(LayoutParams layoutParams) {
        Log.m33i(TAG, "setAttributes()");
        this.mWindow.setAttributes(layoutParams);
    }

    public void setContentView(int i) {
        Log.m33i(TAG, "setContentView() layoutResID");
        this.mWindow.setContentView(i);
    }

    public void setContentView(View view) {
        Log.m33i(TAG, "setContentView() view");
        this.mWindow.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        Log.m33i(TAG, "setContentView() view, params");
        this.mWindow.setContentView(view, layoutParams);
    }

    public void setWindowAttributes(LayoutParams layoutParams) {
        Log.m33i(TAG, "setAttributes()");
        this.mWindow.setAttributes(layoutParams);
    }

    public void stopService() {
        if (this.mDecor != null) {
            this.mWindowManager.removeView(this.mDecor);
            this.mDecor = null;
        }
        stopForeground(true);
        stopSelf();
    }

    public void stopUiSupportService() {
        if (this.mDecor != null) {
            this.mWindowManager.removeView(this.mDecor);
            this.mDecor = null;
        }
        stopForeground(true);
        stopSelf();
    }
}
