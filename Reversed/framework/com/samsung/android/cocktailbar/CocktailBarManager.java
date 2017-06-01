package com.samsung.android.cocktailbar;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.DragEvent;
import android.widget.RemoteViews;
import com.samsung.android.cocktailbar.CocktailInfo.Builder;
import com.samsung.android.cocktailbar.ICocktailBarStateCallback.Stub;
import com.samsung.android.util.SemLog;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CocktailBarManager extends SemCocktailBarManager {
    public static final String ACTION_COCKTAIL_BAR_COCKTAIL_UNINSTALLED = "com.samsung.android.app.cocktailbarservice.action.COCKTAIL_BAR_COCKTAIL_UNINSTALLED";
    @Deprecated
    public static final String ACTION_COCKTAIL_BAR_WAKE_UP_STATE = "com.samsung.android.app.cocktailbarservice.action.COCKTAIL_BAR_WAKE_UP_STATE";
    public static final String ACTION_COCKTAIL_DISABLED = "com.samsung.android.cocktail.action.COCKTAIL_DISABLED";
    @Deprecated
    public static final String ACTION_COCKTAIL_DROPED = "com.samsung.android.cocktail.action.COCKTAIL_DROPED";
    public static final String ACTION_COCKTAIL_ENABLED = "com.samsung.android.cocktail.action.COCKTAIL_ENABLED";
    public static final String ACTION_COCKTAIL_UPDATE = "com.samsung.android.cocktail.action.COCKTAIL_UPDATE";
    public static final String ACTION_COCKTAIL_UPDATE_V2 = "com.samsung.android.cocktail.v2.action.COCKTAIL_UPDATE";
    public static final String ACTION_COCKTAIL_VISIBILITY_CHANGED = "com.samsung.android.cocktail.action.COCKTAIL_VISIBILITY_CHANGED";
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL = 65536;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_CALLING = 65538;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_COMMAND = 65543;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_HEADSET = 65541;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_INCOMING_CALL = 65537;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_NOTIFICATION = 65540;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_SPEN = 65542;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_TICKER = 65539;
    public static final int COCKTAIL_CATEGORY_GLOBAL = 1;
    public static final int COCKTAIL_DISPLAY_POLICY_ALL = 159;
    public static final int COCKTAIL_DISPLAY_POLICY_GENERAL = 1;
    @Deprecated
    public static final int COCKTAIL_DISPLAY_POLICY_INDEX_MODE = 16;
    public static final int COCKTAIL_DISPLAY_POLICY_LOCKSCREEN = 2;
    public static final int COCKTAIL_DISPLAY_POLICY_NOT_PROVISION = 128;
    public static final int COCKTAIL_DISPLAY_POLICY_SCOVER = 4;
    public static final int COCKTAIL_DISPLAY_POLICY_TABLE_MODE = 8;
    public static final int COCKTAIL_VISIBILITY_HIDE = 2;
    public static final int COCKTAIL_VISIBILITY_SHOW = 1;
    @Deprecated
    public static final String EXTRA_COCKTAIL_BAR_WAKE_UP_STATE = "cocktailbarWakeupState";
    public static final String EXTRA_COCKTAIL_ID = "cocktailId";
    public static final String EXTRA_COCKTAIL_IDS = "cocktailIds";
    @Deprecated
    public static final String EXTRA_COCKTAIL_PROVIDER = "cocktailProvider";
    public static final String EXTRA_COCKTAIL_VISIBILITY = "cocktailVisibility";
    @Deprecated
    public static final String EXTRA_DRAG_EVENT = "com.samsung.android.intent.extra.DRAG_EVENT";
    public static final int INVALID_COCKTAIL_ID = 0;
    public static final String META_DATA_COCKTAIL_PROVIDER = "com.samsung.android.cocktail.provider";
    private static final String TAG = CocktailBarManager.class.getSimpleName();
    public static final int TYPE_WAKEUP_GESTURE_PICKUP = 1;
    public static final int TYPE_WAKEUP_GESTURE_RUB = 2;
    private int mCocktailBarSize = -1;
    private final CopyOnWriteArrayList<CocktailBarStateListenerDelegate> mCocktailBarStateListenerDelegates = new CopyOnWriteArrayList();
    private Object mStateListnerDelegatesLock = new Object();

    @Deprecated
    public static class CocktailBarFeedsListener {
        @Deprecated
        public void onFeedsUpdated(int i, List<FeedsInfo> list) {
        }
    }

    public interface CocktailBarStateChangedListener {
        void onCocktailBarStateChanged(CocktailBarStateInfo cocktailBarStateInfo);
    }

    @Deprecated
    public static class CocktailBarStateListener {
        public void onCocktailBarBackgroundTypeChanged(int i) {
        }

        public void onCocktailBarPositionChanged(int i) {
        }

        public void onCocktailBarStateChanged(CocktailBarStateInfo cocktailBarStateInfo) {
        }

        public void onCocktailBarVisibilityChanged(int i) {
        }

        public void onCocktailBarWindowTypeChanged(int i) {
        }
    }

    private class CocktailBarStateListenerDelegate extends Stub {
        private static final int MSG_LISTEN_COCKTAIL_BAR_STATE_CHANGE = 0;
        private Handler mHandler;
        @Deprecated
        private final CocktailBarStateListener mListener;
        private final CocktailBarStateChangedListener mStateChangedListner;

        public CocktailBarStateListenerDelegate(CocktailBarStateChangedListener cocktailBarStateChangedListener, Handler handler) {
            this.mStateChangedListner = cocktailBarStateChangedListener;
            this.mListener = null;
            this.mHandler = new Handler(handler == null ? CocktailBarManager.this.mContext.getMainLooper() : handler.getLooper()) {
                public void handleMessage(Message message) {
                    if (CocktailBarStateListenerDelegate.this.mStateChangedListner != null) {
                        switch (message.what) {
                            case 0:
                                CocktailBarStateInfo cocktailBarStateInfo = (CocktailBarStateInfo) message.obj;
                                if (cocktailBarStateInfo.changeFlag != 0) {
                                    CocktailBarStateListenerDelegate.this.mStateChangedListner.onCocktailBarStateChanged(cocktailBarStateInfo);
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        @Deprecated
        public CocktailBarStateListenerDelegate(CocktailBarStateListener cocktailBarStateListener, Handler handler) {
            this.mListener = cocktailBarStateListener;
            this.mStateChangedListner = null;
            this.mHandler = new Handler(handler == null ? CocktailBarManager.this.mContext.getMainLooper() : handler.getLooper()) {
                public void handleMessage(Message message) {
                    if (CocktailBarStateListenerDelegate.this.mListener != null) {
                        switch (message.what) {
                            case 0:
                                CocktailBarStateInfo cocktailBarStateInfo = (CocktailBarStateInfo) message.obj;
                                if (cocktailBarStateInfo.changeFlag != 0) {
                                    CocktailBarStateListenerDelegate.this.mListener.onCocktailBarStateChanged(cocktailBarStateInfo);
                                    if ((cocktailBarStateInfo.changeFlag & 1) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarVisibilityChanged(cocktailBarStateInfo.visibility);
                                    }
                                    if ((cocktailBarStateInfo.changeFlag & 2) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarBackgroundTypeChanged(cocktailBarStateInfo.backgroundType);
                                    }
                                    if ((cocktailBarStateInfo.changeFlag & 4) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarPositionChanged(cocktailBarStateInfo.position);
                                    }
                                    if ((cocktailBarStateInfo.changeFlag & 128) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarWindowTypeChanged(cocktailBarStateInfo.windowType);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        @Deprecated
        public CocktailBarStateListener getListener() {
            return this.mListener;
        }

        public CocktailBarStateChangedListener getStateChangedListener() {
            return this.mStateChangedListner;
        }

        public void onCocktailBarStateChanged(CocktailBarStateInfo cocktailBarStateInfo) throws RemoteException {
            Message.obtain(this.mHandler, 0, cocktailBarStateInfo).sendToTarget();
        }
    }

    public static class SemManagerStateChangedListenerWrapper implements CocktailBarStateChangedListener {
        public final com.samsung.android.cocktailbar.SemCocktailBarManager.CocktailBarStateChangedListener mSemlistener;

        public SemManagerStateChangedListenerWrapper(com.samsung.android.cocktailbar.SemCocktailBarManager.CocktailBarStateChangedListener cocktailBarStateChangedListener) {
            this.mSemlistener = cocktailBarStateChangedListener;
        }

        public void onCocktailBarStateChanged(CocktailBarStateInfo cocktailBarStateInfo) {
            SemCocktailBarStateInfo semCocktailBarStateInfo = new SemCocktailBarStateInfo();
            semCocktailBarStateInfo.background = cocktailBarStateInfo.background;
            semCocktailBarStateInfo.position = cocktailBarStateInfo.position;
            semCocktailBarStateInfo.visibility = cocktailBarStateInfo.visibility;
            semCocktailBarStateInfo.windowType = cocktailBarStateInfo.windowType;
            this.mSemlistener.onCocktailBarStateChanged(semCocktailBarStateInfo);
        }
    }

    @Deprecated
    public static class States {
        @Deprecated
        public static final int COCKTAIL_BAR_BACKGROUND_DIM = 2;
        @Deprecated
        public static final int COCKTAIL_BAR_BACKGROUND_OPAQUE = 1;
        @Deprecated
        public static final int COCKTAIL_BAR_BACKGROUND_TRANSPARENT = 3;
        @Deprecated
        public static final int COCKTAIL_BAR_BACKGROUND_UNKNOWN = 0;
        @Deprecated
        public static final int COCKTAIL_BAR_FULLSCREEN_TYPE = 2;
        public static final int COCKTAIL_BAR_LOCK_HIDE = 2;
        public static final int COCKTAIL_BAR_LOCK_NONE = 0;
        public static final int COCKTAIL_BAR_LOCK_RESTRICT = 4;
        public static final int COCKTAIL_BAR_LOCK_SHOW = 1;
        @Deprecated
        public static final int COCKTAIL_BAR_MINIMIZE_TYPE = 1;
        @Deprecated
        public static final int COCKTAIL_BAR_MODE_IMMERSIVE = 2;
        @Deprecated
        public static final int COCKTAIL_BAR_MODE_MULTITASKING = 1;
        @Deprecated
        public static final int COCKTAIL_BAR_MODE_UNKNOWN = 0;
        public static final int COCKTAIL_BAR_POSITION_BOTTOM = 4;
        public static final int COCKTAIL_BAR_POSITION_LEFT = 1;
        public static final int COCKTAIL_BAR_POSITION_RIGHT = 2;
        public static final int COCKTAIL_BAR_POSITION_TOP = 3;
        public static final int COCKTAIL_BAR_POSITION_UNKNOWN = 0;
        public static final int COCKTAIL_BAR_STATE_INVISIBLE = 2;
        public static final int COCKTAIL_BAR_STATE_VISIBLE = 1;
        public static final int COCKTAIL_BAR_TYPE_FULLSCREEN = 2;
        public static final int COCKTAIL_BAR_TYPE_MINIMIZE = 1;
        public static final int COCKTAIL_BAR_UNKNOWN_TYPE = 0;

        private States() {
        }
    }

    @Deprecated
    public static class SysFs {
        public static final int SYSFS_DEADZONE_ALL = 3;
        public static final int SYSFS_DEADZONE_CLEAR = 6;
        public static final int SYSFS_DEADZONE_LEFT = 1;
        public static final int SYSFS_DEADZONE_OFF = 0;
        public static final int SYSFS_DEADZONE_RIGHT = 2;

        private SysFs() {
        }
    }

    public static class WakeUp {
        public static final int REASON_BY_DISMISS_KEYGUARD = 3;
        public static final int REASON_BY_NONE = 0;
        public static final int REASON_BY_POWER_MANAGER = 4;
        public static final int REASON_BY_SCREEN_TURN_ON = 2;
        public static final int REASON_BY_WINDOW_POLICY = 1;

        private WakeUp() {
        }
    }

    public static class WindowTypes {
        public static final int WINDOW_TYPE_COCKTAIL_BAR_BACKGROUND = 8;
        public static final int WINDOW_TYPE_IMMERSIVE = 2;
        public static final int WINDOW_TYPE_INPUT_METHOD = 4;
        public static final int WINDOW_TYPE_KEYGUARD = 5;
        public static final int WINDOW_TYPE_NORMAL = 1;
        public static final int WINDOW_TYPE_POPUP = 6;
        public static final int WINDOW_TYPE_RESERVE = 4096;
        public static final int WINDOW_TYPE_SCOVER = 7;
        public static final int WINDOW_TYPE_STATUS_BAR = 3;

        private WindowTypes() {
        }
    }

    public CocktailBarManager(Context context, ICocktailBarService iCocktailBarService) {
        super(context, iCocktailBarService);
    }

    public static CocktailBarManager getInstance(Context context) {
        return (CocktailBarManager) context.getSystemService("CocktailBarService");
    }

    private ICocktailBarService getService() {
        if (this.mService == null) {
            this.mService = ICocktailBarService.Stub.asInterface(ServiceManager.getService("CocktailBarService"));
        }
        return this.mService;
    }

    @Deprecated
    public void activateCocktailBar() {
        if (getService() != null) {
            try {
                this.mService.activateCocktailBar();
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void bindRemoteViewsService(String str, int i, Intent intent, IBinder iBinder) {
        if (getService() != null) {
            try {
                this.mService.bindRemoteViewsService(str, i, intent, iBinder);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void closeCocktail(int i) {
        if (getService() != null) {
            try {
                this.mService.closeCocktail(this.mPackageName, i, 65536);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void closeCocktail(int i, int i2) {
        if (getService() != null) {
            try {
                this.mService.closeCocktail(this.mPackageName, i, i2);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void cocktailBarreboot() {
        if (getService() != null) {
            try {
                this.mService.cocktailBarreboot(this.mPackageName);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void cocktailBarshutdown() {
        if (getService() != null) {
            try {
                this.mService.cocktailBarshutdown(this.mPackageName);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void deactivateCocktailBar() {
        if (getService() != null) {
            try {
                this.mService.deactivateCocktailBar();
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void disableCocktail(ComponentName componentName) {
        if (getService() != null) {
            try {
                this.mService.disableCocktail(this.mPackageName, componentName);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public int[] getAllCocktailIds() {
        if (getService() == null) {
            return null;
        }
        try {
            return this.mService.getAllCocktailIds();
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public String getCategoryFilterStr() {
        if (getService() == null) {
            Log.m29d(TAG, "getCategoryFilterStr getService is null");
            return null;
        }
        try {
            return this.mService.getCategoryFilterStr();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getCocktaiBarWakeUpState() {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.getCocktaiBarWakeUpState();
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public Cocktail getCocktail(int i) {
        if (getService() == null) {
            return null;
        }
        try {
            return this.mService.getCocktail(i);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public int getCocktailBarSize() {
        return 160;
    }

    @Deprecated
    public int getCocktailBarVisibility() {
        if (getService() == null) {
            return 2;
        }
        try {
            return this.mService.getCocktailBarVisibility();
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public int getCocktailBarWindowType() {
        if (getService() == null) {
            return 0;
        }
        try {
            return this.mService.getCocktailBarStateInfo().windowType;
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public int getCocktailId(ComponentName componentName) {
        if (getService() == null || componentName == null) {
            return 0;
        }
        try {
            return this.mService.getCocktailId(this.mPackageName, componentName);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public int[] getCocktailIds(ComponentName componentName) {
        if (getService() == null || componentName == null) {
            return new int[]{0};
        }
        try {
            return this.mService.getCocktailIds(this.mPackageName, componentName);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public int getConfigVersion() {
        if (getService() == null) {
            Log.m29d(TAG, "getConfigVersion getService is null");
            return -1;
        }
        try {
            return this.mService.getConfigVersion();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public int[] getEnabledCocktailIds() {
        if (getService() == null) {
            return null;
        }
        try {
            return this.mService.getEnabledCocktailIds();
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public String getHideEdgeListStr() {
        if (getService() == null) {
            Log.m29d(TAG, "getHideEdgeListStr getService is null");
            return null;
        }
        try {
            return this.mService.getHideEdgeListStr();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPreferWidth() {
        if (getService() == null) {
            Log.m29d(TAG, "getPreferWidth getService is null");
            return -1;
        }
        try {
            return this.mService.getPreferWidth();
        } catch (Throwable e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Deprecated
    public boolean isAllowTransientBarCocktailBar() {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.isAllowTransientBarCocktailBar();
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public boolean isCocktailBarShifted() {
        return false;
    }

    public boolean isCocktailEnabled(ComponentName componentName) {
        if (getService() == null || componentName == null) {
            return false;
        }
        try {
            return this.mService.isEnabledCocktail(this.mPackageName, componentName);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public boolean isEnabledCocktail(ComponentName componentName) {
        if (getService() == null || componentName == null) {
            return false;
        }
        try {
            return this.mService.isEnabledCocktail(this.mPackageName, componentName);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public boolean isImmersiveMode() {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.getWindowType() == 2;
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void notifyCocktailViewDataChanged(int i, int i2) {
        if (getService() != null) {
            try {
                this.mService.notifyCocktailViewDataChanged(this.mPackageName, i, i2);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void notifyCocktailVisibiltyChanged(int i, int i2) {
        if (getService() != null) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mService.notifyCocktailVisibiltyChanged(i, i2);
                Binder.restoreCallingIdentity(clearCallingIdentity);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void notifyKeyguardState(boolean z) {
        if (getService() != null) {
            try {
                this.mService.notifyKeyguardState(z);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void partiallyUpdateCocktail(int i, RemoteViews remoteViews) {
        if (getService() != null) {
            try {
                this.mService.partiallyUpdateCocktail(this.mPackageName, remoteViews, i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void partiallyUpdateHelpView(int i, RemoteViews remoteViews) {
        if (getService() != null) {
            try {
                this.mService.partiallyUpdateHelpView(this.mPackageName, remoteViews, i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void registerListener(CocktailBarStateListener cocktailBarStateListener) {
        Throwable th;
        if (getService() != null) {
            if (cocktailBarStateListener == null) {
                SemLog.w(TAG, "registerListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                try {
                    CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate;
                    IBinder cocktailBarStateListenerDelegate2;
                    Iterator it = this.mCocktailBarStateListenerDelegates.iterator();
                    while (it.hasNext()) {
                        CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate3 = (CocktailBarStateListenerDelegate) it.next();
                        if (cocktailBarStateListenerDelegate3.getListener().equals(cocktailBarStateListener)) {
                            cocktailBarStateListenerDelegate = cocktailBarStateListenerDelegate3;
                            break;
                        }
                    }
                    cocktailBarStateListenerDelegate = null;
                    if (cocktailBarStateListenerDelegate == null) {
                        try {
                            cocktailBarStateListenerDelegate2 = new CocktailBarStateListenerDelegate(cocktailBarStateListener, null);
                            this.mCocktailBarStateListenerDelegates.add(cocktailBarStateListenerDelegate2);
                        } catch (Throwable th2) {
                            th = th2;
                            CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate4 = cocktailBarStateListenerDelegate;
                            throw th;
                        }
                    }
                    Object obj = cocktailBarStateListenerDelegate;
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(cocktailBarStateListenerDelegate2 == null || componentName == null)) {
                        this.mService.registerCocktailBarStateListenerCallback(cocktailBarStateListenerDelegate2, componentName);
                    }
                } catch (Throwable e) {
                    SemLog.e(TAG, "registerListener : RemoteException : ", e);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
        return;
    }

    @Deprecated
    public void registerOnFeedsUpdatedListener(CocktailBarFeedsListener cocktailBarFeedsListener) {
        if (getService() != null) {
            if (cocktailBarFeedsListener == null) {
                SemLog.w(TAG, "registerOnFeedsUpdatedListener : listener is null");
                return;
            }
            throw new RuntimeException("registerOnFeedsUpdatedListener not supported.");
        }
    }

    public void registerStateListener(CocktailBarStateChangedListener cocktailBarStateChangedListener) {
        Throwable th;
        if (getService() != null) {
            if (cocktailBarStateChangedListener == null) {
                SemLog.w(TAG, "registerListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                try {
                    CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate;
                    IBinder cocktailBarStateListenerDelegate2;
                    Iterator it = this.mCocktailBarStateListenerDelegates.iterator();
                    while (it.hasNext()) {
                        CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate3 = (CocktailBarStateListenerDelegate) it.next();
                        if (cocktailBarStateListenerDelegate3.getStateChangedListener().equals(cocktailBarStateChangedListener)) {
                            cocktailBarStateListenerDelegate = cocktailBarStateListenerDelegate3;
                            break;
                        }
                    }
                    cocktailBarStateListenerDelegate = null;
                    if (cocktailBarStateListenerDelegate == null) {
                        try {
                            cocktailBarStateListenerDelegate2 = new CocktailBarStateListenerDelegate(cocktailBarStateChangedListener, null);
                            this.mCocktailBarStateListenerDelegates.add(cocktailBarStateListenerDelegate2);
                        } catch (Throwable th2) {
                            th = th2;
                            CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate4 = cocktailBarStateListenerDelegate;
                            throw th;
                        }
                    }
                    Object obj = cocktailBarStateListenerDelegate;
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(cocktailBarStateListenerDelegate2 == null || componentName == null)) {
                        this.mService.registerCocktailBarStateListenerCallback(cocktailBarStateListenerDelegate2, componentName);
                    }
                } catch (Throwable e) {
                    SemLog.e(TAG, "registerListener : RemoteException : ", e);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
        return;
    }

    public void registerStateListener(com.samsung.android.cocktailbar.SemCocktailBarManager.CocktailBarStateChangedListener cocktailBarStateChangedListener) {
        Throwable th;
        if (getService() != null) {
            if (cocktailBarStateChangedListener == null) {
                SemLog.w(TAG, "registerListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                try {
                    CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate;
                    IBinder cocktailBarStateListenerDelegate2;
                    Iterator it = this.mCocktailBarStateListenerDelegates.iterator();
                    while (it.hasNext()) {
                        CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate3 = (CocktailBarStateListenerDelegate) it.next();
                        Object stateChangedListener = cocktailBarStateListenerDelegate3.getStateChangedListener();
                        if ((stateChangedListener instanceof SemManagerStateChangedListenerWrapper) && cocktailBarStateChangedListener.equals(stateChangedListener.mSemlistener)) {
                            cocktailBarStateListenerDelegate = cocktailBarStateListenerDelegate3;
                            break;
                        }
                    }
                    cocktailBarStateListenerDelegate = null;
                    if (cocktailBarStateListenerDelegate == null) {
                        try {
                            cocktailBarStateListenerDelegate2 = new CocktailBarStateListenerDelegate(new SemManagerStateChangedListenerWrapper(cocktailBarStateChangedListener), null);
                            this.mCocktailBarStateListenerDelegates.add(cocktailBarStateListenerDelegate2);
                        } catch (Throwable th2) {
                            th = th2;
                            CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate4 = cocktailBarStateListenerDelegate;
                            throw th;
                        }
                    }
                    Object obj = cocktailBarStateListenerDelegate;
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(cocktailBarStateListenerDelegate2 == null || componentName == null)) {
                        this.mService.registerCocktailBarStateListenerCallback(cocktailBarStateListenerDelegate2, componentName);
                    }
                } catch (Throwable e) {
                    SemLog.e(TAG, "registerListener : RemoteException : ", e);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
        return;
    }

    public void removeCocktailUIService() {
        if (getService() != null) {
            try {
                this.mService.removeCocktailUIService();
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public boolean requestToDisableCocktail(int i) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToDisableCocktail(i);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean requestToDisableCocktailByCategory(int i) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToDisableCocktailByCategory(i);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean requestToUpdateCocktail(int i) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToUpdateCocktail(i);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean requestToUpdateCocktailByCategory(int i) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToUpdateCocktailByCategory(i);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public void sendDragEvent(int i, DragEvent dragEvent) {
        if (getService() != null) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mService.sendDragEvent(i, dragEvent);
                Binder.restoreCallingIdentity(clearCallingIdentity);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    @Deprecated
    public void sendExtraDataToCocktailBar(Bundle bundle) {
        if (getService() != null) {
            try {
                this.mService.sendExtraDataToCocktailBar(bundle);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void setCocktailBarStatus(boolean z, boolean z2) {
        if (getService() != null) {
            try {
                this.mService.setCocktailBarStatus(z, z2);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void setCocktailBarWakeUpState(boolean z) {
        if (getService() != null) {
            try {
                this.mService.setCocktailBarWakeUpState(z);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void setDisableTickerView(int i) {
        if (getService() != null) {
            try {
                this.mService.setDisableTickerView(i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void setEnabledCocktailIds(int[] iArr) {
        if (getService() != null) {
            try {
                this.mService.setEnabledCocktailIds(iArr);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void setOnPullPendingIntent(int i, int i2, PendingIntent pendingIntent) {
        if (getService() != null) {
            try {
                this.mService.setOnPullPendingIntent(this.mPackageName, i, i2, pendingIntent);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void showAndLockCocktailBar() {
        if (getService() != null) {
            try {
                this.mService.showAndLockCocktailBar();
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void showCocktail(int i) {
        if (getService() != null) {
            try {
                this.mService.showCocktail(this.mPackageName, i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void switchDefaultCocktail() {
        if (getService() != null) {
            try {
                this.mService.switchDefaultCocktail();
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void unbindRemoteViewsService(String str, int i, Intent intent) {
        if (getService() != null) {
            try {
                this.mService.unbindRemoteViewsService(str, i, intent);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void unlockCocktailBar(int i) {
        if (getService() != null) {
            try {
                this.mService.unlockCocktailBar(i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void unregisterListener(CocktailBarStateListener cocktailBarStateListener) {
        if (getService() != null) {
            if (cocktailBarStateListener == null) {
                SemLog.w(TAG, "unregisterListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                IBinder iBinder = null;
                Iterator it = this.mCocktailBarStateListenerDelegates.iterator();
                while (it.hasNext()) {
                    CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate = (CocktailBarStateListenerDelegate) it.next();
                    if (cocktailBarStateListenerDelegate.getListener().equals(cocktailBarStateListener)) {
                        iBinder = cocktailBarStateListenerDelegate;
                        break;
                    }
                }
                if (iBinder == null) {
                    SemLog.w(TAG, "unregisterListener : cannot find the listener");
                    return;
                }
                try {
                    this.mService.unregisterCocktailBarStateListenerCallback(iBinder);
                    this.mCocktailBarStateListenerDelegates.remove(iBinder);
                } catch (Throwable e) {
                    SemLog.e(TAG, "unregisterListener : RemoteException : ", e);
                }
            }
        } else {
            return;
        }
    }

    @Deprecated
    public void unregisterOnFeedsUpdatedListener(CocktailBarFeedsListener cocktailBarFeedsListener) {
        if (getService() != null) {
            if (cocktailBarFeedsListener == null) {
                SemLog.w(TAG, "unregisterOnFeedsUpdatedListener : listener is null");
                return;
            }
            throw new RuntimeException("unregisterOnFeedsUpdatedListener not supported.");
        }
    }

    public void unregisterStateListener(CocktailBarStateChangedListener cocktailBarStateChangedListener) {
        if (getService() != null) {
            if (cocktailBarStateChangedListener == null) {
                SemLog.w(TAG, "unregisterListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                IBinder iBinder = null;
                Iterator it = this.mCocktailBarStateListenerDelegates.iterator();
                while (it.hasNext()) {
                    CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate = (CocktailBarStateListenerDelegate) it.next();
                    if (cocktailBarStateListenerDelegate.getStateChangedListener().equals(cocktailBarStateChangedListener)) {
                        iBinder = cocktailBarStateListenerDelegate;
                        break;
                    }
                }
                if (iBinder == null) {
                    SemLog.w(TAG, "unregisterListener : cannot find the listener");
                    return;
                }
                try {
                    this.mService.unregisterCocktailBarStateListenerCallback(iBinder);
                    this.mCocktailBarStateListenerDelegates.remove(iBinder);
                } catch (Throwable e) {
                    SemLog.e(TAG, "unregisterListener : RemoteException : ", e);
                }
            }
        } else {
            return;
        }
    }

    public void unregisterStateListener(com.samsung.android.cocktailbar.SemCocktailBarManager.CocktailBarStateChangedListener cocktailBarStateChangedListener) {
        if (getService() != null) {
            if (cocktailBarStateChangedListener == null) {
                SemLog.w(TAG, "unregisterListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                IBinder iBinder = null;
                Iterator it = this.mCocktailBarStateListenerDelegates.iterator();
                while (it.hasNext()) {
                    CocktailBarStateListenerDelegate cocktailBarStateListenerDelegate = (CocktailBarStateListenerDelegate) it.next();
                    Object stateChangedListener = cocktailBarStateListenerDelegate.getStateChangedListener();
                    if ((stateChangedListener instanceof SemManagerStateChangedListenerWrapper) && cocktailBarStateChangedListener.equals(stateChangedListener.mSemlistener)) {
                        iBinder = cocktailBarStateListenerDelegate;
                        break;
                    }
                }
                if (iBinder == null) {
                    SemLog.w(TAG, "unregisterListener : cannot find the listener");
                    return;
                }
                try {
                    this.mService.unregisterCocktailBarStateListenerCallback(iBinder);
                    this.mCocktailBarStateListenerDelegates.remove(iBinder);
                } catch (Throwable e) {
                    SemLog.e(TAG, "unregisterListener : RemoteException : ", e);
                }
            }
        } else {
            return;
        }
    }

    @Deprecated
    public void updateCocktail(int i, int i2, int i3, RemoteViews remoteViews, Bundle bundle) {
        if (getService() != null) {
            try {
                this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(i2).setCategory(i3).setContentView(remoteViews).setContentInfo(bundle).build(), i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktail(int i, int i2, int i3, RemoteViews remoteViews, RemoteViews remoteViews2) {
        if (getService() == null) {
            SemLog.w(TAG, "updateCocktail : service is not running " + i);
            return;
        }
        try {
            this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(i2).setCategory(i3).setContentView(remoteViews).setHelpView(remoteViews2).build(), i);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public void updateCocktail(int i, int i2, int i3, RemoteViews remoteViews, RemoteViews remoteViews2, Bundle bundle) {
        if (getService() != null) {
            try {
                this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(i2).setCategory(i3).setContentView(remoteViews).setHelpView(remoteViews2).setContentInfo(bundle).build(), i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void updateCocktail(int i, int i2, int i3, RemoteViews remoteViews, RemoteViews remoteViews2, Bundle bundle, ComponentName componentName) {
        if (getService() != null) {
            try {
                this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(i2).setCategory(i3).setContentView(remoteViews).setHelpView(remoteViews2).setContentInfo(bundle).setClassloader(componentName).build(), i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktail(int i, int i2, int i3, Class<? extends SemAbsCocktailLoadablePanel> cls, Bundle bundle, RemoteViews remoteViews) {
        if (getService() == null) {
            SemLog.w(TAG, "updateCocktail : service is not running " + i);
            return;
        }
        try {
            this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(i2).setCategory(i3).setHelpView(remoteViews).setContentInfo(bundle).setClassloader(new ComponentName(getContext(), cls)).build(), i);
        } catch (Throwable e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void updateCocktailBarPosition(int i) {
        if (getService() != null) {
            try {
                this.mService.updateCocktailBarPosition(i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void updateCocktailBarStateFromSystem(int i) {
        if (getService() != null) {
        }
    }

    public void updateCocktailBarVisibility(int i) {
        if (getService() != null) {
            try {
                this.mService.updateCocktailBarVisibility(i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktailBarWindowType(int i) {
        if (getService() != null) {
            try {
                this.mService.updateCocktailBarWindowType(this.mContext.getPackageName(), i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktailHelpView(int i, RemoteViews remoteViews) {
        if (getService() != null) {
            try {
                this.mService.partiallyUpdateHelpView(this.mPackageName, remoteViews, i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktailView(int i, RemoteViews remoteViews) {
        if (getService() != null) {
            try {
                this.mService.partiallyUpdateCocktail(this.mPackageName, remoteViews, i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void updateFeeds(int i, List<FeedsInfo> list) {
        if (getService() != null) {
            if (list == null) {
                SemLog.e(TAG, "updateFeeds : feedsInfoList is null");
                return;
            }
            throw new RuntimeException("updateFeeds not supported.");
        }
    }

    @Deprecated
    public void updateLongpressGesture(boolean z) {
        if (getService() != null) {
            try {
                this.mService.updateLongpressGesture(z);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void updateSysfsBarLength(int i) {
        if (getService() != null) {
            try {
                this.mService.updateSysfsBarLength(i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void updateSysfsDeadZone(int i) {
        if (getService() != null) {
            try {
                this.mService.updateSysfsDeadZone(i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    @Deprecated
    public void updateSysfsGripDisable(boolean z) {
        if (getService() != null) {
        }
    }

    @Deprecated
    public void updateWakeupArea(int i) {
        if (getService() != null) {
            try {
                this.mService.updateWakeupArea(i);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateWakeupGesture(int i, boolean z) {
        if (getService() != null) {
            try {
                this.mService.updateWakeupGesture(i, z);
            } catch (Throwable e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void wakeupCocktailBar(boolean z, int i, int i2) {
        if (getService() != null) {
        }
    }
}
