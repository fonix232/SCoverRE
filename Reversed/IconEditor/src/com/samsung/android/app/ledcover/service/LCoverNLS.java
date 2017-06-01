package com.samsung.android.app.ledcover.service;

import android.app.ActivityManager;
import android.app.ActivityManager.AppTask;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Telephony.Sms;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.samsung.android.app.ledcover.BuildConfig;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import com.samsung.android.app.ledcover.info.NotiBundleInfo;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import java.util.ArrayList;
import java.util.List;

public class LCoverNLS extends NotificationListenerService {
    private static final String ACTION_COVER_READY = "com.sec.android.cover.ledcover.action.COVER_READY";
    private static final String ACTION_REMOVE_PACKAGE = "android.intent.action.PACKAGE_REMOVED";
    public static final String TAG = "[LED_COVER]LedCoverNLS";
    private String PKG_DEFAULT_MSG;
    private AppChangedFromIconReceiver appChangedFromIconReceiver;
    private boolean mConnectedListener;
    private Context mContext;
    private boolean mCoverDetached;
    private ScoverManager mCoverMgr;
    private StateListener mCoverStateListener;
    private LCoverDbAccessor mLedCoverDbAccessor;
    BroadcastReceiver mLedCoverServiceReadyReceiver;
    private ArrayList<LCoverAppInfo> mLedNotiAppInfoList;
    private String mPreNotiGroupKey;
    private long mPreNotiPostTime;
    private RemovePackageReceiver removePkgReceiver;
    private String[] sbnPkgArr;

    /* renamed from: com.samsung.android.app.ledcover.service.LCoverNLS.2 */
    class C02602 extends BroadcastReceiver {
        C02602() {
        }

        public void onReceive(Context context, Intent intent) {
            SLog.m12v(LCoverNLS.TAG, "mLedCoverServiceReadyReceiver onReceive ");
            SLog.m12v(LCoverNLS.TAG, "LED Cover Attached in mLedCoverServiceReadyReceiver");
            LCoverNLS.this.sendAllNotifications();
        }
    }

    class AppChangedFromIconReceiver extends BroadcastReceiver {
        AppChangedFromIconReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            boolean isChanged = intent.getBooleanExtra("isChanged", false);
            SLog.m12v(LCoverNLS.TAG, "AppChangedFromIconReceiver onReceive(), isChanged : " + isChanged);
            if (LCoverNLS.this.mConnectedListener) {
                String[] packages = intent.getStringArrayExtra("packages");
                ArrayList<String> pkgArray = new ArrayList();
                Bundle notificationData = new Bundle();
                int countMatchedApp = 0;
                int countPackages = packages.length;
                StatusBarNotification[] sbnArray = null;
                if (LCoverNLS.this.mConnectedListener) {
                    SLog.m12v(LCoverNLS.TAG, "Noti Listener alive ");
                    sbnArray = LCoverNLS.this.getActiveNotifications();
                }
                if (sbnArray != null) {
                    int j;
                    SLog.m12v(LCoverNLS.TAG, "getActiveNotifications count : " + sbnArray.length);
                    int i = 0;
                    while (true) {
                        int length = sbnArray.length;
                        if (i >= r0) {
                            break;
                        }
                        StatusBarNotification sbn = sbnArray[i];
                        for (j = 0; j < countPackages; j++) {
                            if (sbn.getPackageName().equals(packages[j])) {
                                if (!pkgArray.contains(sbn.getPackageName())) {
                                    countMatchedApp++;
                                    pkgArray.add(packages[j]);
                                    break;
                                }
                            }
                        }
                        i++;
                    }
                    SLog.m12v(LCoverNLS.TAG, "Matched App Count : " + countMatchedApp);
                    if (countMatchedApp > 0) {
                        String[] iconArray = new String[countMatchedApp];
                        int[] iconId = new int[countMatchedApp];
                        int[] effectId = new int[countMatchedApp];
                        String[] pkgNameArray = (String[]) pkgArray.toArray(new String[countMatchedApp]);
                        for (i = 0; i < countMatchedApp; i++) {
                            iconId[i] = -1;
                            iconArray[i] = null;
                        }
                        notificationData.putInt("count", countMatchedApp);
                        notificationData.putStringArray("pkg_name", pkgNameArray);
                        notificationData.putIntArray(Defines.ICON_COL_ID, iconId);
                        notificationData.putStringArray("icon_data", iconArray);
                        try {
                            if (LCoverNLS.this.mCoverMgr == null) {
                                LCoverNLS.this.mCoverMgr = new ScoverManager(LCoverNLS.this);
                            }
                            LCoverNLS.this.mCoverMgr.removeLedNotification(notificationData);
                        } catch (SsdkUnsupportedException e) {
                            SLog.m12v(LCoverNLS.TAG, "SCover SDK Unsupported Exception Occured!");
                        }
                        if (isChanged) {
                            if (LCoverNLS.this.mLedCoverDbAccessor == null) {
                                LCoverNLS.this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(LCoverNLS.this.mContext);
                            }
                            LCoverNLS.this.mLedNotiAppInfoList = LCoverNLS.this.mLedCoverDbAccessor.getSelectedAppsInfo();
                            if (LCoverNLS.this.mLedNotiAppInfoList != null) {
                                for (i = 0; i < countMatchedApp; i++) {
                                    j = 0;
                                    while (true) {
                                        if (j >= LCoverNLS.this.mLedNotiAppInfoList.size()) {
                                            break;
                                        }
                                        if (pkgNameArray[i].equals(((LCoverAppInfo) LCoverNLS.this.mLedNotiAppInfoList.get(j)).getPackageName())) {
                                            break;
                                        }
                                        j++;
                                    }
                                    iconArray[i] = ((LCoverAppInfo) LCoverNLS.this.mLedNotiAppInfoList.get(j)).getIconArray();
                                    if (iconArray[i].equals(Defines.PRESET_ICON_ARRAY)) {
                                        iconId[i] = ((LCoverAppInfo) LCoverNLS.this.mLedNotiAppInfoList.get(j)).getIconId();
                                        iconArray[i] = null;
                                    } else {
                                        iconId[i] = -1;
                                    }
                                    SLog.m12v(LCoverNLS.TAG, "iconId[" + i + "] : " + iconId[i]);
                                    SLog.m12v(LCoverNLS.TAG, "iconArray[" + i + "] : " + iconArray[i]);
                                }
                            }
                            notificationData.putInt("count", countMatchedApp);
                            notificationData.putStringArray("pkg_name", pkgNameArray);
                            notificationData.putIntArray(Defines.ICON_COL_ID, iconId);
                            notificationData.putStringArray("icon_data", iconArray);
                            notificationData.putIntArray("Pre-definedEffect", effectId);
                            try {
                                if (LCoverNLS.this.mCoverMgr == null) {
                                    LCoverNLS.this.mCoverMgr = new ScoverManager(LCoverNLS.this);
                                }
                                LCoverNLS.this.mCoverMgr.addLedNotification(notificationData);
                                return;
                            } catch (SsdkUnsupportedException e2) {
                                SLog.m12v(LCoverNLS.TAG, "SCover SDK Unsupported Exception Occured!");
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            SLog.m12v(LCoverNLS.TAG, "Notification Listener is not connected!");
        }
    }

    class RemovePackageReceiver extends BroadcastReceiver {
        public static final String TAG = "[LED_COVER]RemovePackageReceiver";

        RemovePackageReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ArrayList<NotiBundleInfo> mMatchedAppArray = new ArrayList();
            String packageName = intent.getData().getSchemeSpecificPart();
            Bundle extras = intent.getExtras();
            if (extras.containsKey("android.intent.extra.REPLACING") && extras.getBoolean("android.intent.extra.REPLACING")) {
                SLog.m12v(TAG, "Update package : " + packageName);
                return;
            }
            int i;
            SLog.m12v(TAG, "Remove package : " + packageName);
            if (LCoverNLS.this.mLedCoverDbAccessor == null) {
                LCoverNLS.this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(context);
            }
            LCoverNLS.this.mLedNotiAppInfoList = LCoverNLS.this.mLedCoverDbAccessor.getSelectedAppsInfo();
            if (LCoverNLS.this.mLedNotiAppInfoList != null) {
                for (i = 0; i < LCoverNLS.this.mLedNotiAppInfoList.size(); i++) {
                    if (((LCoverAppInfo) LCoverNLS.this.mLedNotiAppInfoList.get(i)).getPackageName().equals(packageName)) {
                        if (LCoverNLS.this.mLedCoverDbAccessor.deleteSelectedApps("name=?", new String[]{packageName})) {
                            SLog.m12v(TAG, "Succeed to delete removed package : " + packageName);
                            mMatchedAppArray.add(new NotiBundleInfo(1, ((LCoverAppInfo) LCoverNLS.this.mLedNotiAppInfoList.get(i)).getPackageName(), ((LCoverAppInfo) LCoverNLS.this.mLedNotiAppInfoList.get(i)).getIconId(), ((LCoverAppInfo) LCoverNLS.this.mLedNotiAppInfoList.get(i)).getIconArray()));
                            break;
                        }
                    }
                }
            }
            if (mMatchedAppArray.size() > 0) {
                try {
                    Bundle notificationData = LCoverNLS.this.makeNotificationBundle(mMatchedAppArray);
                    if (LCoverNLS.this.mCoverMgr == null) {
                        LCoverNLS.this.mCoverMgr = new ScoverManager(context);
                    }
                    String[] test = notificationData.getStringArray("pkg_name");
                    for (i = 0; i < mMatchedAppArray.size(); i++) {
                        SLog.m12v(TAG, "RemovedPkgReceiver removeLedNotification : " + test[i]);
                    }
                    LCoverNLS.this.mCoverMgr.removeLedNotification(notificationData);
                } catch (SsdkUnsupportedException e) {
                    SLog.m12v(TAG, "SCover SDK Unsupported Exception Occured!");
                }
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.service.LCoverNLS.1 */
    class C04201 extends StateListener {
        C04201() {
        }

        public void onCoverStateChanged(ScoverState state) {
            if (state.getAttachState()) {
                if (LCoverNLS.this.mCoverDetached && state.getType() == 7) {
                    SLog.m12v(LCoverNLS.TAG, "LED Cover Attached");
                    LCoverNLS.this.mCoverDetached = false;
                }
            } else if (!LCoverNLS.this.mCoverDetached && state.getType() == 7) {
                SLog.m12v(LCoverNLS.TAG, "LED Cover Detached");
                LCoverNLS.this.mCoverDetached = true;
                List<AppTask> tasks = ((ActivityManager) LCoverNLS.this.mContext.getSystemService("activity")).getAppTasks();
                if (tasks != null && tasks.size() > 0) {
                    SLog.m12v(LCoverNLS.TAG, "LED Cover App Running! -> FinishAndRemoveTask");
                    ((AppTask) tasks.get(0)).finishAndRemoveTask();
                }
            }
        }
    }

    public LCoverNLS() {
        this.PKG_DEFAULT_MSG = " ";
        this.mLedNotiAppInfoList = null;
        this.mCoverMgr = null;
        this.mConnectedListener = false;
        this.mPreNotiGroupKey = C0316a.f163d;
        this.mPreNotiPostTime = 0;
        this.mCoverDetached = false;
        this.mLedCoverServiceReadyReceiver = new C02602();
    }

    public void onCreate() {
        super.onCreate();
        SLog.m12v(TAG, "onCreate");
        this.removePkgReceiver = new RemovePackageReceiver();
        IntentFilter removePkgFilter = new IntentFilter();
        removePkgFilter.addAction(ACTION_REMOVE_PACKAGE);
        removePkgFilter.addDataScheme("package");
        registerReceiver(this.removePkgReceiver, removePkgFilter);
        this.appChangedFromIconReceiver = new AppChangedFromIconReceiver();
        IntentFilter appChangedFromIconFilter = new IntentFilter();
        appChangedFromIconFilter.addAction(Defines.BROADCAST_ACTION_APP_CHANGED_FROM_ICON);
        registerReceiver(this.appChangedFromIconReceiver, appChangedFromIconFilter, Defines.PERMISSION_LCOVER_LAUNCH, null);
        IntentFilter serviceIntentFilter = new IntentFilter();
        serviceIntentFilter.addAction(ACTION_COVER_READY);
        registerReceiver(this.mLedCoverServiceReadyReceiver, serviceIntentFilter);
        if (this.mCoverMgr == null) {
            this.mCoverMgr = new ScoverManager(this);
        }
        if (this.mCoverMgr != null) {
            ScoverState tempCoverState = this.mCoverMgr.getCoverState();
            if (tempCoverState == null) {
                SLog.m12v(TAG, "mCoverMgr.getCoverState() null pointer");
            } else if (tempCoverState.getType() == 7 && !tempCoverState.getAttachState()) {
                SLog.m12v(TAG, "Initially LED Cover detached!");
                this.mCoverDetached = true;
            }
        } else {
            SLog.m12v(TAG, "mCoverMgr.getCoverState() null pointer");
        }
        this.mCoverStateListener = new C04201();
        this.mCoverMgr.registerListener(this.mCoverStateListener);
        this.mContext = getApplicationContext();
        this.PKG_DEFAULT_MSG = getDefaultMessageApp();
        if (!isIconServiceRunningCheck()) {
            startIconService();
        }
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        SLog.m12v(TAG, "onNotificationPosted");
        SLog.m12v(TAG, "Notification package name : " + sbn.getPackageName());
        SLog.m12v(TAG, "Notification group key : " + sbn.getGroupKey());
        SLog.m12v(TAG, "Notification post time : " + sbn.getPostTime());
        if (VERSION.SDK_INT >= 19) {
            ArrayList<NotiBundleInfo> mMatchedAppArray = new ArrayList();
            String notiGroupKey = sbn.getGroupKey();
            long notiPostTime = sbn.getPostTime();
            Bundle notificationData = new Bundle();
            String defaultMessageApp = getDefaultMessageApp();
            if (this.PKG_DEFAULT_MSG == null || !this.PKG_DEFAULT_MSG.equals(defaultMessageApp)) {
                this.PKG_DEFAULT_MSG = defaultMessageApp;
            }
            SLog.m12v(TAG, "Notification diff time : " + (notiPostTime - this.mPreNotiPostTime));
            if (!this.mPreNotiGroupKey.equals(notiGroupKey) || notiPostTime - this.mPreNotiPostTime > 700) {
                int i;
                this.mPreNotiGroupKey = notiGroupKey;
                this.mPreNotiPostTime = notiPostTime;
                if (this.mLedCoverDbAccessor == null) {
                    this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
                }
                this.mLedNotiAppInfoList = this.mLedCoverDbAccessor.getSelectedAppsInfo();
                if (sbn.getPackageName().equals(Defines.PKG_MISSED_CALL)) {
                    mMatchedAppArray.add(new NotiBundleInfo(1, Defines.PKG_MISSED_CALL, -1, C0316a.f163d, true, false));
                } else if (sbn.getPackageName().equals(Defines.PKG_MISSED_CALL_SKT)) {
                    SLog.m12v(TAG, "SKT dialer noti key : " + sbn.getKey());
                    SLog.m12v(TAG, "SKT dialer noti isOngoing : " + sbn.isOngoing());
                    if (!sbn.isOngoing() && sbn.getKey().contains("com.skt.prod.dialer|1|")) {
                        mMatchedAppArray.add(new NotiBundleInfo(1, Defines.PKG_MISSED_CALL_SKT, -1, C0316a.f163d, true, false));
                    }
                } else if (sbn.getPackageName().equals(this.PKG_DEFAULT_MSG)) {
                    mMatchedAppArray.add(new NotiBundleInfo(1, this.PKG_DEFAULT_MSG, -1, C0316a.f163d, false, true));
                } else if (this.mLedNotiAppInfoList != null) {
                    for (i = 0; i < this.mLedNotiAppInfoList.size(); i++) {
                        if (sbn.getPackageName().equals(((LCoverAppInfo) this.mLedNotiAppInfoList.get(i)).getPackageName())) {
                            mMatchedAppArray.add(new NotiBundleInfo(1, ((LCoverAppInfo) this.mLedNotiAppInfoList.get(i)).getPackageName(), ((LCoverAppInfo) this.mLedNotiAppInfoList.get(i)).getIconId(), ((LCoverAppInfo) this.mLedNotiAppInfoList.get(i)).getIconArray()));
                            break;
                        }
                    }
                }
                if (mMatchedAppArray.size() > 0) {
                    SLog.m12v(TAG, "Posted mMatchedAppArray.size : " + mMatchedAppArray.size());
                    notificationData = makeNotificationBundle(mMatchedAppArray);
                    try {
                        if (this.mCoverMgr == null) {
                            this.mCoverMgr = new ScoverManager(this);
                        }
                        String[] test = notificationData.getStringArray("pkg_name");
                        if (test != null) {
                            for (i = 0; i < mMatchedAppArray.size(); i++) {
                                SLog.m12v(TAG, "Posted addLedNotification : " + test[i]);
                            }
                        }
                        this.mCoverMgr.addLedNotification(notificationData);
                    } catch (SsdkUnsupportedException e) {
                        SLog.m12v(TAG, "Posted SCover SDK Unsupported Exception Occured!");
                    }
                }
                super.onNotificationPosted(sbn);
                return;
            }
            SLog.m12v(TAG, "Duplicated Notifications in 700 msec, so the second notification ignored");
        }
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        SLog.m12v(TAG, "onNotificationRemoved : " + sbn.getPackageName());
        ArrayList<NotiBundleInfo> mMatchedAppArray = new ArrayList();
        Bundle notificationData = new Bundle();
        if (this.mLedCoverDbAccessor == null) {
            this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
        }
        this.mLedNotiAppInfoList = this.mLedCoverDbAccessor.getSelectedAppsInfo();
        String defaultMessageApp = getDefaultMessageApp();
        if (this.PKG_DEFAULT_MSG == null || !this.PKG_DEFAULT_MSG.equals(defaultMessageApp)) {
            this.PKG_DEFAULT_MSG = defaultMessageApp;
        }
        if (sbn.getPackageName() != null) {
            mMatchedAppArray.add(new NotiBundleInfo(1, sbn.getPackageName(), -1, C0316a.f163d, false, false));
        }
        if (mMatchedAppArray.size() > 0) {
            SLog.m12v(TAG, "Removed mMatchedAppArray.size : " + mMatchedAppArray.size());
            notificationData = makeNotificationBundle(mMatchedAppArray);
            try {
                if (this.mCoverMgr == null) {
                    this.mCoverMgr = new ScoverManager(this);
                }
                String[] test = notificationData.getStringArray("pkg_name");
                if (test != null) {
                    for (int i = 0; i < mMatchedAppArray.size(); i++) {
                        SLog.m12v(TAG, "Removed addLedNotification : " + test[i]);
                    }
                }
                this.mCoverMgr.removeLedNotification(notificationData);
            } catch (SsdkUnsupportedException e) {
                SLog.m12v(TAG, "Removed SCover SDK Unsupported Exception Occured!");
            }
        }
        super.onNotificationRemoved(sbn);
    }

    public void onDestroy() {
        this.mCoverMgr.unregisterListener(this.mCoverStateListener);
        unregisterReceiver(this.removePkgReceiver);
        unregisterReceiver(this.appChangedFromIconReceiver);
        unregisterReceiver(this.mLedCoverServiceReadyReceiver);
        super.onDestroy();
    }

    public void onListenerConnected() {
        SLog.m12v(TAG, "onListenerConnected()");
        this.mConnectedListener = true;
        ScoverState state = this.mCoverMgr.getCoverState();
        if (state != null && state.getAttachState() && state.getType() == 7) {
            sendAllNotifications();
        }
    }

    public StatusBarNotification[] getActiveNotifications() {
        SLog.m12v(TAG, "getActiveNotifications");
        return super.getActiveNotifications();
    }

    private ArrayList<NotiBundleInfo> getMatchedAppListFromList(String[] strArr) {
        int i;
        SLog.m12v(TAG, "getMatchedAppListFromList");
        ArrayList<NotiBundleInfo> mMatchedAppArray = new ArrayList();
        if (this.mLedCoverDbAccessor == null) {
            this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
        }
        ArrayList<LCoverAppInfo> mLedNotiAppInfoList = this.mLedCoverDbAccessor.getSelectedAppsInfo();
        String defaultMessageApp = getDefaultMessageApp();
        if (this.PKG_DEFAULT_MSG == null || !this.PKG_DEFAULT_MSG.equals(defaultMessageApp)) {
            this.PKG_DEFAULT_MSG = defaultMessageApp;
        }
        if (mLedNotiAppInfoList != null) {
            for (i = 0; i < strArr.length; i++) {
                if (strArr[i].equals(Defines.PKG_MISSED_CALL)) {
                    mMatchedAppArray.add(new NotiBundleInfo(1, Defines.PKG_MISSED_CALL, -1, C0316a.f163d, true, false));
                }
                if (strArr[i].equals(Defines.PKG_MISSED_CALL_SKT)) {
                    mMatchedAppArray.add(new NotiBundleInfo(1, Defines.PKG_MISSED_CALL_SKT, -1, C0316a.f163d, true, false));
                }
                if (strArr[i].equals(this.PKG_DEFAULT_MSG)) {
                    mMatchedAppArray.add(new NotiBundleInfo(1, this.PKG_DEFAULT_MSG, -1, C0316a.f163d, false, true));
                }
                for (int j = 0; j < mLedNotiAppInfoList.size(); j++) {
                    if (strArr[i].equals(((LCoverAppInfo) mLedNotiAppInfoList.get(j)).getPackageName())) {
                        mMatchedAppArray.add(new NotiBundleInfo(1, ((LCoverAppInfo) mLedNotiAppInfoList.get(j)).getPackageName(), ((LCoverAppInfo) mLedNotiAppInfoList.get(j)).getIconId(), ((LCoverAppInfo) mLedNotiAppInfoList.get(j)).getIconArray()));
                    }
                }
            }
        }
        for (i = 0; i < mMatchedAppArray.size(); i++) {
            SLog.m12v(TAG, "mMatchedAppArray cnt : " + ((NotiBundleInfo) mMatchedAppArray.get(i)).mCount + ", pkg name : " + ((NotiBundleInfo) mMatchedAppArray.get(i)).mPkgName + ", icon id : " + ((NotiBundleInfo) mMatchedAppArray.get(i)).mIconId + ", icon data : " + ((NotiBundleInfo) mMatchedAppArray.get(i)).mIconData + ", missed call : " + ((NotiBundleInfo) mMatchedAppArray.get(i)).mIsMissedCall + ", missed msg : " + ((NotiBundleInfo) mMatchedAppArray.get(i)).mIsDefaultMsg);
        }
        return mMatchedAppArray;
    }

    private Bundle makeNotificationBundle(ArrayList<NotiBundleInfo> arr) {
        SLog.m12v(TAG, "makeNotificationBundle");
        Bundle mNotiBundle = new Bundle();
        String[] pkgNameArray = new String[arr.size()];
        int[] iconId = new int[arr.size()];
        String[] iconArray = new String[arr.size()];
        boolean[] missedCallArray = new boolean[arr.size()];
        boolean[] missedMsgArray = new boolean[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            if (((NotiBundleInfo) arr.get(i)).mIconData.equals(Defines.PRESET_ICON_ARRAY)) {
                iconId[i] = ((NotiBundleInfo) arr.get(i)).mIconId;
                iconArray[i] = null;
            } else {
                iconId[i] = -1;
                iconArray[i] = ((NotiBundleInfo) arr.get(i)).mIconData;
            }
            pkgNameArray[i] = ((NotiBundleInfo) arr.get(i)).mPkgName;
            missedCallArray[i] = ((NotiBundleInfo) arr.get(i)).mIsMissedCall;
            missedMsgArray[i] = ((NotiBundleInfo) arr.get(i)).mIsDefaultMsg;
        }
        mNotiBundle.putInt("count", arr.size());
        mNotiBundle.putStringArray("pkg_name", pkgNameArray);
        mNotiBundle.putIntArray(Defines.ICON_COL_ID, iconId);
        mNotiBundle.putStringArray("icon_data", iconArray);
        mNotiBundle.putBooleanArray("icon_call", missedCallArray);
        mNotiBundle.putBooleanArray("icon_msg", missedMsgArray);
        return mNotiBundle;
    }

    private String getDefaultMessageApp() {
        String msgApp = Sms.getDefaultSmsPackage(this.mContext);
        SLog.m12v(TAG, "getDefaultMessageApp " + msgApp);
        if (msgApp == null) {
            return " ";
        }
        return msgApp;
    }

    private void sendAllNotifications() {
        Bundle notificationData = new Bundle();
        StatusBarNotification[] sbnArray = null;
        if (this.mConnectedListener) {
            SLog.m12v(TAG, "Noti Listener alive ");
            sbnArray = getActiveNotifications();
        }
        if (sbnArray != null) {
            int i;
            this.sbnPkgArr = new String[sbnArray.length];
            for (i = 0; i < sbnArray.length; i++) {
                this.sbnPkgArr[i] = sbnArray[i].getPackageName();
                SLog.m12v(TAG, "sbnPkgArr : " + this.sbnPkgArr[i]);
            }
            ArrayList mMatchedAppArray = getMatchedAppListFromList(this.sbnPkgArr);
            int matchedAppSize = mMatchedAppArray.size();
            notificationData = makeNotificationBundle(mMatchedAppArray);
            notificationData.putBoolean("clear_notifications", true);
            SLog.m12v(TAG, "Attached matchedAppSize : " + matchedAppSize);
            if (matchedAppSize > 0) {
                try {
                    if (this.mCoverMgr == null) {
                        this.mCoverMgr = new ScoverManager(this.mContext);
                    }
                    String[] test = notificationData.getStringArray("pkg_name");
                    if (test != null) {
                        for (i = 0; i < matchedAppSize; i++) {
                            SLog.m12v(TAG, "Attached addLedNotification : " + test[i]);
                        }
                    }
                    this.mCoverMgr.addLedNotification(notificationData);
                } catch (SsdkUnsupportedException e) {
                    SLog.m12v(TAG, "Attached SCover SDK Unsupported Exception Occured!");
                }
            }
        }
    }

    private boolean isIconServiceRunningCheck() {
        for (RunningServiceInfo service : ((ActivityManager) getSystemService("activity")).getRunningServices(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED)) {
            if (Defines.ICON_SERVICE_NAME.equals(service.service.getClassName())) {
                SLog.m12v(TAG, "icon service is running");
                return true;
            }
        }
        SLog.m12v(TAG, "icon service is not running");
        return false;
    }

    private void startIconService() {
        Intent iService = new Intent();
        iService.setComponent(new ComponentName(BuildConfig.APPLICATION_ID, Defines.ICON_SERVICE_NAME));
        startService(iService);
    }
}
