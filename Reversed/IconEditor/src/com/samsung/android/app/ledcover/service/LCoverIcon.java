package com.samsung.android.app.ledcover.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import com.samsung.android.app.ledcover.BuildConfig;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.SharedPreferencesUtil;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.noti.LCoverNotiUtils;
import com.samsung.android.app.ledcover.wrapperlibrary.C0270R;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.samsung.context.sdk.samsunganalytics.C0316a;

public class LCoverIcon extends Service {
    private static final String INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String TAG = "[LED_COVER]LCoverIcon";
    private static final String UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private Context mAppContext;
    private SharedPreferences mAppPreferences;
    private boolean mCoverDetached;
    private ScoverManager mCoverMgr;
    private StateListener mCoverStateListener;
    private Intent mHomeScreenShortcut;
    private ShortcutEnableReceiver shortcutEnableReceiver;

    class ShortcutEnableReceiver extends BroadcastReceiver {
        ShortcutEnableReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            SLog.m12v(LCoverIcon.TAG, "ShortcutEnableReceiver onReceive()");
            LCoverIcon.this.makeShortcut(intent.getBooleanExtra("isChecked", true));
        }
    }

    public static class ShutDownReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
                SLog.m12v(LCoverIcon.TAG, "ShutDownReceiver onReceive() ACTION_SHUTDOWN");
                SharedPreferencesUtil.setKeyRebootingFlag(context, true);
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.service.LCoverIcon.1 */
    class C04191 extends StateListener {
        C04191() {
        }

        public void onCoverStateChanged(ScoverState state) {
            if (state.getAttachState()) {
                if (LCoverIcon.this.mCoverDetached && state.getType() == 7) {
                    SLog.m12v(LCoverIcon.TAG, "LED Cover Attached");
                    LCoverIcon.this.mCoverDetached = false;
                    LCoverIcon.this.makeShortcut(true);
                }
            } else if (!LCoverIcon.this.mCoverDetached && state.getType() == 7) {
                SLog.m12v(LCoverIcon.TAG, "LED Cover Detached");
                LCoverIcon.this.mCoverDetached = true;
                LCoverIcon.this.makeShortcut(false);
                LCoverIcon.disableNotificationAccess(LCoverIcon.this.mAppContext);
                LCoverIcon.this.stopSelf();
            }
        }
    }

    public LCoverIcon() {
        this.mCoverMgr = null;
        this.mCoverStateListener = null;
        this.mAppPreferences = null;
        this.shortcutEnableReceiver = null;
        this.mCoverDetached = false;
        this.mAppContext = null;
        this.mHomeScreenShortcut = null;
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        SLog.m12v(TAG, "onCreate");
        this.shortcutEnableReceiver = new ShortcutEnableReceiver();
        IntentFilter shortcutEnableFilter = new IntentFilter();
        shortcutEnableFilter.addAction(Defines.BROADCAST_ACTION_SHORTCUT_ENABLE_CHANGED);
        registerReceiver(this.shortcutEnableReceiver, shortcutEnableFilter, Defines.PERMISSION_LCOVER_LAUNCH, null);
        this.mAppContext = getApplicationContext();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        SLog.m12v(TAG, "onStartCommand");
        if (this.mCoverMgr == null) {
            this.mCoverMgr = new ScoverManager(this);
        }
        if (this.mCoverMgr != null) {
            ScoverState coverState = this.mCoverMgr.getCoverState();
            if (coverState == null) {
                SLog.m12v(TAG, "getCoverState() failed!");
            } else {
                SLog.m12v(TAG, "onStartCommand getKeyRebootingFlag : " + SharedPreferencesUtil.getKeyRebootingFlag(this.mAppContext));
                if (!SharedPreferencesUtil.getKeyRebootingFlag(this.mAppContext)) {
                    boolean isAccessibilityON = LCoverNotiUtils.isAccessibilityON(this.mAppContext);
                    SLog.m12v(TAG, "isAccessibilityON(this)" + isAccessibilityON);
                    if (coverState.getAttachState() && coverState.getType() == 7) {
                        this.mCoverDetached = false;
                        makeShortcut(true);
                        if (!isAccessibilityON) {
                            setNotificationAccess();
                        }
                    } else {
                        this.mCoverDetached = true;
                        makeShortcut(false);
                        if (isAccessibilityON) {
                            disableNotificationAccess(this.mAppContext);
                        }
                        stopSelf();
                    }
                }
            }
            if (this.mCoverStateListener == null) {
                this.mCoverStateListener = new C04191();
                this.mCoverMgr.registerListener(this.mCoverStateListener);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        SLog.m12v(TAG, "onDestroy");
        this.mCoverMgr.unregisterListener(this.mCoverStateListener);
        super.onDestroy();
    }

    public void makeShortcut(boolean isMake) {
        SLog.m12v(TAG, "makeShortcut(" + isMake + ")");
        PackageManager p = getPackageManager();
        SharedPreferencesUtil.setKeyPDAVersion(this, Utils.readPDAVersion());
        this.mAppPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!isMake) {
            removeShortcutHome();
            p.setComponentEnabledSetting(getShortcutComponentName(), 2, 1);
        } else if (!this.mAppPreferences.getBoolean("isShortcutEnable", true)) {
            SLog.m12v(TAG, "Shortcut Disabled!");
        } else if (SharedPreferencesUtil.isShortcutAdded(this.mAppContext)) {
            SLog.m12v(TAG, "Check by SharedPreferences: Shortcut already exist in home screen : do nothing");
        } else {
            SLog.m12v(TAG, "Check by SharedPreferences: Shortcut not exist in home screen : add it");
            addShortcutHome();
            p.setComponentEnabledSetting(getShortcutComponentName(), 1, 1);
        }
    }

    private void setNotificationAccess() {
        SLog.m12v(TAG, "setNotificationAccess");
        if (VERSION.SDK_INT >= 18) {
            boolean update = false;
            String NOTIFICATION_LISTENER = "com.samsung.android.app.ledcoverdream/com.samsung.android.app.ledcover.service.LCoverNLS";
            try {
                SLog.m12v(TAG, "SetNotificationListenerService enable");
                String notiAccessSet = Secure.getString(getContentResolver(), "enabled_notification_listeners");
                SLog.m12v(TAG, "Before set, notiAccessSet " + notiAccessSet);
                if (notiAccessSet == null) {
                    notiAccessSet = NOTIFICATION_LISTENER;
                    update = true;
                } else if (!notiAccessSet.contains(NOTIFICATION_LISTENER)) {
                    if (notiAccessSet.length() > 0) {
                        notiAccessSet = notiAccessSet + ":" + NOTIFICATION_LISTENER;
                    } else {
                        notiAccessSet = NOTIFICATION_LISTENER;
                    }
                    update = true;
                }
                if (update) {
                    Secure.putString(getContentResolver(), "enabled_notification_listeners", notiAccessSet);
                    SLog.m12v(TAG, "After set, notificationAccessSetting " + notiAccessSet);
                }
            } catch (Exception e) {
                SLog.m12v(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void disableNotificationAccess(Context context) {
        SLog.m12v(TAG, "disableNotificationAccess");
        if (VERSION.SDK_INT >= 18) {
            boolean update = false;
            String NOTIFICATION_LISTENER = "com.samsung.android.app.ledcoverdream/com.samsung.android.app.ledcover.service.LCoverNLS";
            try {
                SLog.m12v(TAG, "SetNotificationListenerService enable");
                String notiAccessSet = Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
                SLog.m12v(TAG, "Before disable, notiAccessSet " + notiAccessSet);
                if (notiAccessSet == null) {
                    update = false;
                } else if (notiAccessSet.contains(NOTIFICATION_LISTENER)) {
                    notiAccessSet = notiAccessSet.replace(":" + NOTIFICATION_LISTENER, C0316a.f163d);
                    update = true;
                }
                if (update) {
                    Secure.putString(context.getContentResolver(), "enabled_notification_listeners", notiAccessSet);
                    SLog.m12v(TAG, "After disable, notificationAccessSetting " + notiAccessSet);
                }
            } catch (Exception e) {
                SLog.m12v(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static ComponentName getShortcutComponentName() {
        SLog.m12v(TAG, "getShortcutComponentName()");
        return new ComponentName(BuildConfig.APPLICATION_ID, "com.samsung.android.app.ledcover.app.LCoverMainLauncher");
    }

    private Intent getHomeScreenShortcutIntent() {
        SLog.m12v(TAG, "getHomeScreenShortcutIntent()");
        if (this.mHomeScreenShortcut == null) {
            this.mHomeScreenShortcut = new Intent();
            this.mHomeScreenShortcut.setComponent(getShortcutComponentName());
            this.mHomeScreenShortcut.setAction("android.intent.action.MAIN");
            this.mHomeScreenShortcut.addCategory("android.intent.category.LAUNCHER");
            this.mHomeScreenShortcut.addFlags(270532608);
        }
        return this.mHomeScreenShortcut;
    }

    private void addShortcutHome() {
        SLog.m12v(TAG, "addShortcutHome()");
        Intent addIntent = new Intent();
        addIntent.putExtra("android.intent.extra.shortcut.INTENT", getHomeScreenShortcutIntent());
        addIntent.putExtra("android.intent.extra.shortcut.NAME", getString(C0270R.string.app_name));
        addIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(getApplicationContext(), C0198R.drawable.led_view_cover));
        addIntent.setAction(INSTALL_SHORTCUT);
        addIntent.setFlags(268435456);
        getApplicationContext().sendBroadcast(addIntent);
        SharedPreferencesUtil.setShortcutAdded(this.mAppContext, true);
    }

    private void removeShortcutHome() {
        SLog.m12v(TAG, "removeShortcutHome()");
        Intent removeIntent = new Intent();
        removeIntent.putExtra("android.intent.extra.shortcut.INTENT", getHomeScreenShortcutIntent());
        removeIntent.putExtra("android.intent.extra.shortcut.NAME", this.mAppPreferences.getString("appName", getString(C0270R.string.app_name)));
        removeIntent.setAction(UNINSTALL_SHORTCUT);
        removeIntent.setFlags(268435456);
        getApplicationContext().sendBroadcast(removeIntent);
        SharedPreferencesUtil.setShortcutAdded(this.mAppContext, false);
    }
}
