package com.sec.android.cover.monitor;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.RemoteViews;
import com.samsung.android.util.SemLog;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.Constants;
import com.sec.android.cover.ledcover.reflection.content.RefContentResolver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;

public class CoverUpdateMonitor extends BaseCoverObservator {
    private static final String EXTRA_HIGHVOLTAGE_CHARGER = "hv_charger";
    private static final String EXTRA_ONLINE = "online";
    private static final int MSG_BATTERY_CRITICAL = 335;
    private static final int MSG_BATTERY_LOW = 306;
    private static final int MSG_BATTERY_UPDATE = 302;
    private static final int MSG_BIXBY_STATE_CHANGED = 336;
    private static final int MSG_CONTENT_CHANGED = 321;
    private static final int MSG_POWER_CONNECTION_UPDATE = 307;
    private static final int MSG_REMOTE_VIEW_UPDATED = 310;
    private static final int MSG_SCREEN_TURNED_OFF = 305;
    private static final int MSG_SCREEN_TURNED_ON = 304;
    private static final int MSG_TIME_UPDATE = 301;
    private static final int MSG_USER_SWITCHED = 318;
    private static final int MSG_VOLUME_CHANGED = 317;
    private static final String TAG = "CoverUpdateMonitor";
    private BatteryStatus mBatteryStatus;
    private final BroadcastReceiver mBroadcastReceiver = new C00743();
    private final ArrayList<WeakReference<CoverUpdateMonitorCallback>> mCallbacks = new ArrayList();
    private final ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange, Uri uri) {
            if (uri == null) {
                SemLog.e(CoverUpdateMonitor.TAG, "onChange contents, but uri is null");
                return;
            }
            SemLog.d(CoverUpdateMonitor.TAG, "onChange contents : " + uri.toString());
            Message msg = CoverUpdateMonitor.this.mHandler.obtainMessage(CoverUpdateMonitor.MSG_CONTENT_CHANGED);
            msg.obj = uri;
            CoverUpdateMonitor.this.mHandler.sendMessage(msg);
        }
    };
    private final Handler mHandler = new C00732();
    private final Uri[] mInitiallyRegistContentUriList = new Uri[]{System.getUriFor("time_12_24")};
    private final HashSet<Uri> mRegisteredContentUriSet = new HashSet();
    private final String[] mStrIntentActionList = new String[]{"android.intent.action.BATTERY_CHANGED", "android.intent.action.SCREEN_ON", "android.intent.action.SCREEN_OFF", "android.intent.action.BATTERY_LOW", "android.intent.action.ACTION_POWER_CONNECTED", "android.intent.action.ACTION_POWER_DISCONNECTED", Constants.ACTION_REMOTE_VIEW_UPDATED, "android.media.VOLUME_CHANGED_ACTION", "android.intent.action.USER_SWITCHED", "android.intent.action.DATE_CHANGED", "android.intent.action.TIME_SET", "android.intent.action.TIME_TICK", "android.intent.action.TIMEZONE_CHANGED"};

    class C00732 extends Handler {
        C00732() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CoverUpdateMonitor.MSG_TIME_UPDATE /*301*/:
                    CoverUpdateMonitor.this.handleTimeUpdate();
                    return;
                case CoverUpdateMonitor.MSG_BATTERY_UPDATE /*302*/:
                    CoverUpdateMonitor.this.handleBatteryUpdate((BatteryStatus) msg.obj);
                    return;
                case CoverUpdateMonitor.MSG_SCREEN_TURNED_ON /*304*/:
                    CoverUpdateMonitor.this.handleScreenTurnedOn();
                    return;
                case CoverUpdateMonitor.MSG_SCREEN_TURNED_OFF /*305*/:
                    CoverUpdateMonitor.this.handleScreenTurnedOff();
                    return;
                case CoverUpdateMonitor.MSG_BATTERY_LOW /*306*/:
                    CoverUpdateMonitor.this.handleBatteryLow();
                    return;
                case CoverUpdateMonitor.MSG_POWER_CONNECTION_UPDATE /*307*/:
                    CoverUpdateMonitor.this.handlePowerConnectionUpdate(((Boolean) msg.obj).booleanValue());
                    return;
                case CoverUpdateMonitor.MSG_REMOTE_VIEW_UPDATED /*310*/:
                    CoverUpdateMonitor.this.handleRemoteViewUpdated((RemoteViewInfo) msg.obj);
                    return;
                case CoverUpdateMonitor.MSG_VOLUME_CHANGED /*317*/:
                    CoverUpdateMonitor.this.handleVolumeChanged(msg.arg1, msg.arg2);
                    return;
                case CoverUpdateMonitor.MSG_USER_SWITCHED /*318*/:
                    CoverUpdateMonitor.this.handleUserSwitched(msg.arg1, msg.arg2);
                    return;
                case CoverUpdateMonitor.MSG_CONTENT_CHANGED /*321*/:
                    CoverUpdateMonitor.this.handleContentChanged((Uri) msg.obj);
                    return;
                case CoverUpdateMonitor.MSG_BATTERY_CRITICAL /*335*/:
                    CoverUpdateMonitor.this.handleBatteryCritical();
                    return;
                case CoverUpdateMonitor.MSG_BIXBY_STATE_CHANGED /*336*/:
                    CoverUpdateMonitor.this.handleSetBixbyState(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    class C00743 extends BroadcastReceiver {
        C00743() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            SemLog.d(CoverUpdateMonitor.TAG, "received broadcast " + action);
            if ("android.intent.action.DATE_CHANGED".equals(action) || "android.intent.action.TIME_SET".equals(action)) {
                CoverUpdateMonitor.this.mHandler.sendEmptyMessage(CoverUpdateMonitor.MSG_TIME_UPDATE);
            } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                CoverUpdateMonitor.this.mHandler.sendMessage(CoverUpdateMonitor.this.mHandler.obtainMessage(CoverUpdateMonitor.MSG_BATTERY_UPDATE, new BatteryStatus(intent)));
            } else if (action.equals("android.intent.action.SCREEN_ON")) {
                CoverUpdateMonitor.this.mHandler.sendEmptyMessage(CoverUpdateMonitor.MSG_SCREEN_TURNED_ON);
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                CoverUpdateMonitor.this.mHandler.sendEmptyMessage(CoverUpdateMonitor.MSG_SCREEN_TURNED_OFF);
            } else if (action.equals("android.intent.action.BATTERY_LOW")) {
                CoverUpdateMonitor.this.mHandler.sendEmptyMessage(CoverUpdateMonitor.MSG_BATTERY_LOW);
            } else if (action.equals("android.intent.action.ACTION_POWER_CONNECTED") || action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                CoverUpdateMonitor.this.mHandler.sendMessage(CoverUpdateMonitor.this.mHandler.obtainMessage(CoverUpdateMonitor.MSG_POWER_CONNECTION_UPDATE, Boolean.valueOf(action.equals("android.intent.action.ACTION_POWER_CONNECTED"))));
            } else if (action.equals(Constants.ACTION_REMOTE_VIEW_UPDATED)) {
                CoverUpdateMonitor.this.mHandler.sendMessage(CoverUpdateMonitor.this.mHandler.obtainMessage(CoverUpdateMonitor.MSG_REMOTE_VIEW_UPDATED, new RemoteViewInfo(intent)));
            } else if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                int val = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
                msg = CoverUpdateMonitor.this.mHandler.obtainMessage(CoverUpdateMonitor.MSG_VOLUME_CHANGED);
                msg.arg1 = streamType;
                msg.arg2 = val;
                CoverUpdateMonitor.this.mHandler.sendMessage(msg);
            } else if (action.equals("android.intent.action.USER_SWITCHED")) {
                int newUserId = intent.getIntExtra("android.intent.extra.user_handle", 0);
                int oldUserId = intent.getIntExtra("old_user_id", 0);
                msg = CoverUpdateMonitor.this.mHandler.obtainMessage(CoverUpdateMonitor.MSG_USER_SWITCHED);
                msg.arg1 = newUserId;
                msg.arg2 = oldUserId;
                CoverUpdateMonitor.this.mHandler.sendMessage(msg);
            } else if ("android.intent.action.TIME_TICK".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                CoverUpdateMonitor.this.mHandler.sendEmptyMessage(CoverUpdateMonitor.MSG_TIME_UPDATE);
            }
        }
    }

    public static class BatteryStatus {
        public final int batteryOnline;
        public final int health;
        public final boolean highVoltage;
        public final int level;
        public final int plugged;
        public final int status;

        public BatteryStatus(Intent intent) {
            if (intent == null) {
                this.status = 1;
                this.plugged = 0;
                this.level = 0;
                this.health = 1;
                this.batteryOnline = 1;
                this.highVoltage = false;
                return;
            }
            this.status = intent.getIntExtra("status", 1);
            this.plugged = intent.getIntExtra("plugged", 0);
            this.level = intent.getIntExtra("level", 0);
            this.health = intent.getIntExtra("health", 1);
            this.batteryOnline = intent.getIntExtra(CoverUpdateMonitor.EXTRA_ONLINE, 1);
            this.highVoltage = intent.getBooleanExtra(CoverUpdateMonitor.EXTRA_HIGHVOLTAGE_CHARGER, false);
        }

        public boolean isPluggedIn() {
            return this.plugged == 1 || this.plugged == 2 || this.plugged == 4;
        }

        public boolean isCharged() {
            return this.status == 5;
        }

        public boolean isBatteryLow() {
            return this.level <= 15;
        }

        public boolean isBatteryCritical() {
            return this.level <= 5;
        }

        public boolean isWirelssCharged() {
            return this.plugged == 4;
        }

        public boolean isWirelssFastCharged() {
            return this.batteryOnline == 100;
        }

        public boolean isFastCharging() {
            return this.highVoltage;
        }

        public String toString() {
            return String.format("status=%d / plugged=%d / level=%d / health=%d / batteryOnline=%d / highVoltage=%b", new Object[]{Integer.valueOf(this.status), Integer.valueOf(this.plugged), Integer.valueOf(this.level), Integer.valueOf(this.health), Integer.valueOf(this.batteryOnline), Boolean.valueOf(this.highVoltage)});
        }
    }

    public static class RemoteViewInfo {
        public final int mCount;
        public final Intent mIntent;
        public final boolean mIsPlaying;
        public final boolean mNonSecureOnly;
        public final PendingIntent mPendingIntent;
        public RemoteViews mRemoteViews;
        public RemoteViews mSecureModeRemoteViews;
        public final long mTime;
        public final String mType;
        public final boolean mVisibility;

        public RemoteViewInfo(Intent intent) {
            this.mTime = intent.getLongExtra(Constants.REMOTE_VIEW_INFO_TIME, 0);
            this.mType = intent.getStringExtra(Constants.REMOTE_VIEW_INFO_TYPE);
            this.mVisibility = intent.getBooleanExtra(Constants.REMOTE_VIEW_INFO_VISIBILITY, false);
            this.mNonSecureOnly = intent.getBooleanExtra(Constants.REMOTE_VIEW_INFO_NON_SECURE_ONLY, false);
            this.mRemoteViews = (RemoteViews) intent.getParcelableExtra(Constants.REMOTE_VIEW_INFO_REMOTE);
            this.mSecureModeRemoteViews = (RemoteViews) intent.getParcelableExtra(Constants.REMOTE_VIEW_INFO_SECURE_MODE_REMOTE);
            if (this.mSecureModeRemoteViews == null) {
                this.mSecureModeRemoteViews = this.mRemoteViews;
            }
            this.mCount = intent.getIntExtra(Constants.REMOTE_VIEW_INFO_COUNT, 0);
            this.mPendingIntent = (PendingIntent) intent.getParcelableExtra(Constants.REMOTE_VIEW_INFO_PENDING_INTENT);
            this.mIsPlaying = intent.getBooleanExtra(Constants.REMOTE_VIEW_INFO_IS_PLAYING, true);
            this.mIntent = intent;
        }

        public String toString() {
            return String.format("type=%s / time=%s / visible=%s / non secure only=%s / remote view=%s / secure remote view=%s / isPlaying=%s", new Object[]{this.mType, Long.valueOf(this.mTime), Boolean.valueOf(this.mVisibility), Boolean.toString(this.mNonSecureOnly), this.mRemoteViews, this.mSecureModeRemoteViews, Boolean.toString(this.mIsPlaying)});
        }
    }

    public CoverUpdateMonitor(Context context) {
        super(context);
        Log.d(Constants.TAG, "create CoverUpdateMonitor");
    }

    public void unregisterCallback(CoverUpdateMonitorCallback callback) {
        SemLog.v(TAG, "*** unregister callback for " + callback);
        synchronized (this.mCallbacks) {
            for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
                if (((WeakReference) this.mCallbacks.get(i)).get() == callback) {
                    this.mCallbacks.remove(i);
                }
            }
        }
    }

    public void registerCallback(CoverUpdateMonitorCallback callback) {
        SemLog.v(TAG, "*** register callback for " + callback);
        synchronized (this.mCallbacks) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                if (((WeakReference) this.mCallbacks.get(i)).get() == callback) {
                    SemLog.e(TAG, "Object tried to add another callback", new Exception("Called by"));
                    return;
                }
            }
            this.mCallbacks.add(new WeakReference(callback));
            unregisterCallback(null);
            sendUpdates(callback);
        }
    }

    private void sendUpdates(CoverUpdateMonitorCallback callback) {
        callback.onTimeChanged();
        callback.onRefreshBatteryInfo(this.mBatteryStatus);
    }

    public void setBixbyState(int state) {
        Message msg = this.mHandler.obtainMessage(MSG_BIXBY_STATE_CHANGED);
        msg.arg1 = state;
        msg.sendToTarget();
    }

    private void handleTimeUpdate() {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onTimeChanged();
                }
            }
        }
    }

    private void handleBatteryUpdate(BatteryStatus status) {
        boolean batteryUpdateInteresting = isBatteryUpdateInteresting(this.mBatteryStatus, status);
        if (status.isBatteryCritical() && !this.mBatteryStatus.isBatteryCritical()) {
            this.mHandler.sendEmptyMessage(MSG_BATTERY_CRITICAL);
        }
        this.mBatteryStatus = status;
        if (batteryUpdateInteresting) {
            synchronized (this.mCallbacks) {
                int count = this.mCallbacks.size();
                for (int i = 0; i < count; i++) {
                    CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                    if (cb != null) {
                        cb.onRefreshBatteryInfo(status);
                    }
                }
            }
        }
    }

    private void handleScreenTurnedOn() {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onScreenTurnedOn();
                }
            }
        }
    }

    private void handleScreenTurnedOff() {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onScreenTurnedOff();
                }
            }
        }
    }

    private void handleBatteryLow() {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onBatteryLow();
                }
            }
        }
    }

    private void handleBatteryCritical() {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onBatteryCritical();
                }
            }
        }
    }

    private void handlePowerConnectionUpdate(boolean connected) {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onPowerConnectionUpdate(connected);
                }
            }
        }
    }

    private void handleRemoteViewUpdated(RemoteViewInfo remoteViewInfo) {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onRemoteViewUpdated(remoteViewInfo);
                }
            }
        }
    }

    private void handleVolumeChanged(int streamType, int val) {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onVolumeChanged(streamType, val);
                }
            }
        }
    }

    private void handleUserSwitched(int newUserId, int oldUserId) {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onUserSwitched(newUserId, oldUserId);
                }
            }
        }
    }

    private void handleContentChanged(Uri uri) {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onContentChanged(uri);
                }
            }
        }
    }

    private void handleSetBixbyState(int state) {
        synchronized (this.mCallbacks) {
            int count = this.mCallbacks.size();
            for (int i = 0; i < count; i++) {
                CoverUpdateMonitorCallback cb = (CoverUpdateMonitorCallback) ((WeakReference) this.mCallbacks.get(i)).get();
                if (cb != null) {
                    cb.onBixbyStateChanged(state);
                }
            }
        }
    }

    public synchronized void registerContentObserver(Uri uri, boolean notifyForDescendents) {
        if (uri != null) {
            if (!this.mRegisteredContentUriSet.contains(uri)) {
                this.mRegisteredContentUriSet.add(uri);
                RefContentResolver.get().registerContentObserver(getContext().getContentResolver(), uri, notifyForDescendents, this.mContentObserver, UserHandle.SEM_ALL.semGetIdentifier());
            }
        }
    }

    private static boolean isBatteryUpdateInteresting(BatteryStatus old, BatteryStatus current) {
        boolean nowPluggedIn = current.isPluggedIn();
        boolean wasPluggedIn = old.isPluggedIn();
        boolean stateChangedWhilePluggedIn;
        if (wasPluggedIn && nowPluggedIn && old.status != current.status) {
            stateChangedWhilePluggedIn = true;
        } else {
            stateChangedWhilePluggedIn = false;
        }
        if (wasPluggedIn != nowPluggedIn || stateChangedWhilePluggedIn) {
            return true;
        }
        if (nowPluggedIn && old.level != current.level) {
            return true;
        }
        if (!nowPluggedIn && current.isBatteryLow() && current.level != old.level) {
            return true;
        }
        if ((old.level != 0 || current.level == 0) && old.batteryOnline == current.batteryOnline && old.highVoltage == current.highVoltage) {
            return false;
        }
        return true;
    }

    public BatteryStatus getLastBatteryUpdateState() {
        return this.mBatteryStatus;
    }

    public void start() {
        this.mBatteryStatus = new BatteryStatus(null);
        IntentFilter intentFilter = new IntentFilter();
        for (String action : this.mStrIntentActionList) {
            intentFilter.addAction(action);
        }
        getContext().registerReceiver(this.mBroadcastReceiver, intentFilter);
        for (Uri uri : this.mInitiallyRegistContentUriList) {
            registerContentObserver(uri, false);
        }
    }

    public void stop() {
        getContext().unregisterReceiver(this.mBroadcastReceiver);
        getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
        this.mRegisteredContentUriSet.clear();
    }
}
