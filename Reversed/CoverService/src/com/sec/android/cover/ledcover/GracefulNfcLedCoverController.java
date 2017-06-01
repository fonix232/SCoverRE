package com.sec.android.cover.ledcover;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.samsung.android.feature.SemCscFeature;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.LedSystemEventListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.samsung.android.util.SemLog;
import com.sec.android.cover.BaseCoverController;
import com.sec.android.cover.Constants;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.CoverUtils;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.CallerData;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.VoiceRecorderState;
import com.sec.android.cover.ledcover.fsm.grace.LedPowerOnOffStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedPowerOnOffStateController.LedOffCallback;
import com.sec.android.cover.ledcover.fsm.grace.LedPowerOnOffStateController.TransceiveCallback;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStateMachine;
import com.sec.android.cover.ledcover.fsm.grace.LedStateMachine.LedStateMachineListener;
import com.sec.android.cover.ledcover.fsm.grace.missedevent.MissedEvent;
import com.sec.android.cover.ledcover.reflection.content.RefContentResolver;
import com.sec.android.cover.ledcover.reflection.content.RefContext;
import com.sec.android.cover.ledcover.reflection.media.RefAudioManager;
import com.sec.android.cover.ledcover.reflection.os.RefUserHandle;
import com.sec.android.cover.ledcover.reflection.samsung.RefCscFeatureTagSetting;
import com.sec.android.cover.ledcover.reflection.text.RefDateFormat;
import com.sec.android.cover.ledcover.reflection.view.RefWindowManagerLayoutParams;
import com.sec.android.cover.manager.CallDisconnectReasonManager.CallDisconnectReasonListener;
import com.sec.android.cover.manager.MissedEventManager.MissedEventListener;
import com.sec.android.cover.monitor.CoverPlaybackStateMonitor.OnPlaybackStateChangedListener;
import com.sec.android.cover.monitor.CoverUpdateMonitor.BatteryStatus;
import com.sec.android.cover.monitor.CoverUpdateMonitor.RemoteViewInfo;
import com.sec.android.cover.monitor.CoverUpdateMonitorCallback;
import java.util.List;

public class GracefulNfcLedCoverController extends BaseCoverController implements Callback, LedStateMachineListener, TransceiveCallback {
    private static final int DOWNLOAD_POPUP_DELAY = 60000;
    private static final int LAMP_NOTI_TIMEOUT = 60000;
    private static final int MAX_VOLUME = 15;
    private static final int MIN_CALL_VOLUME = 1;
    private static final int MSG_EVENT_ALARM_START = 7;
    private static final int MSG_EVENT_ALARM_STOP = 8;
    private static final int MSG_EVENT_BATTERY_CHANGED = 5;
    private static final int MSG_EVENT_CALENDAR_START = 21;
    private static final int MSG_EVENT_CALENDAR_STOP = 22;
    private static final int MSG_EVENT_CALL_DISCONNECT_REASON = 26;
    private static final int MSG_EVENT_CALL_END = 12;
    private static final int MSG_EVENT_CALL_STATE_CHANGED = 14;
    private static final int MSG_EVENT_CALL_STATE_IDLE_DELAYED = 23;
    private static final int MSG_EVENT_CALL_TIME = 11;
    private static final int MSG_EVENT_CUSTOM_NOTIFICATION_ADD = 18;
    private static final int MSG_EVENT_CUSTOM_NOTIFICATION_REMOVE = 19;
    private static final int MSG_EVENT_DELAYED_DOWNLOAD_POPUP = 24;
    private static final int MSG_EVENT_HEADSET_PLUG = 13;
    private static final int MSG_EVENT_LED_LAMP_NOTI = 20;
    private static final int MSG_EVENT_MUSIC_STATE = 1;
    private static final int MSG_EVENT_NEW_MESSAGE = 2;
    private static final int MSG_EVENT_NEW_MESSAGE_INACTIVE_USER = 2000;
    private static final int MSG_EVENT_NEW_MISSED_CALL = 3;
    private static final int MSG_EVENT_NEW_MISSED_CALL_INACTIVE_USER = 3000;
    private static final int MSG_EVENT_POWER_BUTTON = 16;
    private static final int MSG_EVENT_TIMEOUT = 0;
    private static final int MSG_EVENT_TIMER_START = 9;
    private static final int MSG_EVENT_TIMER_STOP = 10;
    private static final int MSG_EVENT_VOICE_REC_STATE = 15;
    private static final int MSG_EVENT_VOLUME_CHANGED = 4;
    private static final int MSG_LCD_OFF_DISABLED_BY_COVER = 25;
    private static final long PLAYBACK_STATE_CHANGE_DELAY = 500;
    private static final String TAG = "LedService.GracefulNfcLedCoverController";
    private static final String[] strIntentAction = new String[]{"android.intent.action.PHONE_STATE", Constants.INTENT_ACTION_ALARM_ALERT, Constants.INTENT_ACTION_ALARM_START_ALERT, Constants.INTENT_ACTION_ALARM_STOP_ALERT, Constants.INTENT_ACTION_CALL_TIME, "android.intent.action.HEADSET_PLUG", Constants.INTENT_ACTION_TIMER_STOP_ALERT, Constants.INTENT_ACTION_TIMER_START_ALERT, Constants.INTENT_ACTION_LED_LAMP_NOTI, Constants.INTENT_ACTION_CALENDAR_START, Constants.INTENT_ACTION_CALENDAR_STOP};
    private AlarmManager mAlarmManager;
    private BroadcastReceiver mBroadcastReceiver = new C00147();
    private CallDisconnectReasonListener mCallDisconnectReasonListener = new C00136();
    private Object mCountryDetector;
    private boolean mCoverEventsDisabledForDemoMode;
    private boolean mCoverEventsDisabledForSamsungPay;
    private ScoverManager mCoverManager;
    private final ContentObserver mDeviceProvisionedObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange, Uri uri) {
            if (uri == null) {
                Log.e(GracefulNfcLedCoverController.TAG, "onChange contents, but uri is null");
                return;
            }
            boolean isSetupWizardRunning = CoverUtils.isSetupWizardRunning(GracefulNfcLedCoverController.this.mContext);
            Log.d(GracefulNfcLedCoverController.TAG, "DeviceProvisioned changed is setupWizard still running? " + isSetupWizardRunning);
            if (!isSetupWizardRunning) {
                GracefulNfcLedCoverController.this.unRegisterDeviceProvisionedObserver();
                if (SubscriptionManager.from(GracefulNfcLedCoverController.this.mContext).getActiveSubscriptionInfoCountMax() > 1) {
                    GracefulNfcLedCoverController.this.mHandler.sendEmptyMessageDelayed(24, 60000);
                    return;
                }
                Log.d(GracefulNfcLedCoverController.TAG, "setupWizard finished so show Led Cover App Download Popup");
                GracefulNfcLedCoverController.this.showDownloadLEDCoverFeaturePopup();
            }
        }
    };
    private boolean mDeviceProvisionedObserverRegistered;
    private Dialog mDownloadFeaturesDialog;
    private Handler mHandler;
    private BroadcastReceiver mLedAppUninstallBroadcastReceiver = new C00114();
    private boolean mLedCoverAppUninstallReceiverRegistered;
    private LedPowerOnOffStateController mLedPowerStateController;
    private LedStateMachine mLedStateMachine;
    private MissedEventListener mMissedEventListener = new C00103();
    private OnPlaybackStateChangedListener mPlaybackStateChangedListener = new C00092();
    private String mPreviousPhoneState;
    private CoverManagerSystemEventListener mSystemEventListener;
    private TelephonyManager mTelephonyManager;
    private WakeLock mTimeoutWakeLock;
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00081();

    class C00081 extends CoverUpdateMonitorCallback {
        C00081() {
        }

        public void onRefreshBatteryInfo(BatteryStatus status) {
            super.onRefreshBatteryInfo(status);
            Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(5);
            msg.obj = status;
            GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
        }

        public void onContentChanged(Uri uri) {
            if (uri.toString().contains("time_12_24")) {
                onTimeChanged();
            }
        }

        public void onTimeChanged() {
            GracefulNfcLedCoverController.this.mLedStateMachine.getLedContext().set24HourFormat(RefDateFormat.get().is24HourFormat(GracefulNfcLedCoverController.this.mContext, GracefulNfcLedCoverController.this.getCurrentUserId()));
        }

        public void onVolumeChanged(int streamType, int val) {
            super.onVolumeChanged(streamType, val);
            int value = GracefulNfcLedCoverController.this.getVolumeInformation(streamType);
            if (value >= 0) {
                Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(4);
                msg.arg1 = value;
                GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }

        public void onRemoteViewUpdated(RemoteViewInfo remoteViewInfo) {
            super.onRemoteViewUpdated(remoteViewInfo);
            if (remoteViewInfo == null) {
                Log.e(GracefulNfcLedCoverController.TAG, "onRemoteViewUpdated remoteViewInfo=null");
                return;
            }
            String type = remoteViewInfo.mType;
            Log.d(GracefulNfcLedCoverController.TAG, "onRemoteViewUpdated type=" + String.valueOf(type));
            if (Constants.TYPE_VOICE_RECORDER.equals(type)) {
                int status = remoteViewInfo.mIntent.getIntExtra(Constants.KEY_VOICE_RECORDER_STATUS, -1);
                long recordingStartTime = remoteViewInfo.mIntent.getLongExtra(Constants.KEY_VOICE_RECORDER_START_TIME, -1);
                VoiceRecorderState state = null;
                Log.d(GracefulNfcLedCoverController.TAG, "onRemoteViewUpdated status=" + String.valueOf(status) + " recordingStartTime=" + String.valueOf(recordingStartTime) + " recordedTime=" + String.valueOf((int) (SystemClock.elapsedRealtime() - recordingStartTime)));
                switch (status) {
                    case 0:
                        state = VoiceRecorderState.STOP;
                        break;
                    case 1:
                        state = VoiceRecorderState.RECORD;
                        break;
                    case 2:
                        state = VoiceRecorderState.PLAY;
                        break;
                    default:
                        Log.e(GracefulNfcLedCoverController.TAG, "onRemoteViewUpdated unknown status");
                        break;
                }
                if (state != null) {
                    Bundle voiceRecorderBundle = new Bundle();
                    voiceRecorderBundle.putInt(Constants.KEY_VOICE_RECORDER_STATUS, state.ordinal());
                    voiceRecorderBundle.putLong(Constants.KEY_VOICE_RECORDER_START_TIME, recordingStartTime);
                    Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(15);
                    msg.obj = voiceRecorderBundle;
                    GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
                }
            }
        }
    }

    class C00092 implements OnPlaybackStateChangedListener {
        C00092() {
        }

        public void onPlaybackStateChanged(PlaybackState playbackState) {
            Log.d(GracefulNfcLedCoverController.TAG, "onPlaybackStateChanged playbackState=" + String.valueOf(playbackState));
            MusicState musicState = MusicState.STOP;
            if (playbackState != null) {
                switch (playbackState.getState()) {
                    case 0:
                    case 1:
                        musicState = MusicState.STOP;
                        break;
                    case 2:
                        musicState = MusicState.PAUSE;
                        break;
                    case 3:
                        musicState = MusicState.PLAY;
                        break;
                }
            }
            GracefulNfcLedCoverController.this.mHandler.removeMessages(1);
            Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(1);
            msg.obj = musicState;
            if (musicState == MusicState.PAUSE) {
                GracefulNfcLedCoverController.this.mHandler.sendMessageDelayed(msg, GracefulNfcLedCoverController.PLAYBACK_STATE_CHANGE_DELAY);
            } else {
                GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }

        public void onMetadataChanged(MediaMetadata metadata) {
        }

        public void onSessionDestroyed() {
            MusicState currentState = GracefulNfcLedCoverController.this.mLedStateMachine.getLedContext().getMusicState();
            Log.d(GracefulNfcLedCoverController.TAG, "onSessionDestroyed Music currentState=" + currentState);
            if (currentState != MusicState.STOP) {
                Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(1);
                msg.obj = MusicState.STOP;
                GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }
    }

    class C00103 implements MissedEventListener {
        C00103() {
        }

        public void onUnreadMessagesCountChanged(int oldCount, int newCount, boolean muted, int userId) {
            Log.d(GracefulNfcLedCoverController.TAG, "onMissedMessagesCountChanged oldCount=" + String.valueOf(oldCount) + " newCount=" + String.valueOf(newCount) + " muted=" + String.valueOf(muted) + " userId=" + String.valueOf(userId));
            Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage();
            if (userId == GracefulNfcLedCoverController.this.getCurrentUserId()) {
                msg.what = 2;
                msg.arg1 = newCount;
                msg.arg2 = muted ? 1 : 0;
                msg.obj = MissedEvent.getMissedMessageEvent(newCount);
            } else if (!muted && newCount > oldCount) {
                msg.what = GracefulNfcLedCoverController.MSG_EVENT_NEW_MESSAGE_INACTIVE_USER;
                msg.obj = MissedEvent.getMissedMessageEvent(newCount);
            }
            GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
        }

        public void onMissedCallsCountChanged(int oldCount, int newCount, boolean muted, int userId) {
            Log.d(GracefulNfcLedCoverController.TAG, "onMissedCallsCountChanged oldCount=" + String.valueOf(oldCount) + " newCount=" + String.valueOf(newCount) + " muted=" + String.valueOf(muted) + " userId=" + String.valueOf(userId));
            Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage();
            if (userId == GracefulNfcLedCoverController.this.getCurrentUserId()) {
                msg.what = 3;
                msg.arg1 = newCount;
                msg.arg2 = muted ? 1 : 0;
                msg.obj = MissedEvent.getMissedCallEvent(newCount);
            } else if (!muted && newCount > oldCount) {
                msg.what = GracefulNfcLedCoverController.MSG_EVENT_NEW_MISSED_CALL_INACTIVE_USER;
                msg.obj = MissedEvent.getMissedCallEvent(newCount);
            }
            GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
        }
    }

    class C00114 extends BroadcastReceiver {
        C00114() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.e(GracefulNfcLedCoverController.TAG, "onReceive mLedAppUninstallBroadcastReceiverintent=null");
                return;
            }
            String packageName = intent.getDataString();
            Log.i(GracefulNfcLedCoverController.TAG, "onReceive; package removed " + packageName);
            if (packageName != null && packageName.endsWith(Constants.PACKAGE_NAME_GRACELED)) {
                Log.i(GracefulNfcLedCoverController.TAG, "onReceive; LedApp Uninstalled so remove all pending Custom Notifications");
                MissedEvent[] notifications = GracefulNfcLedCoverController.this.mLedStateMachine.getLedContext().getMissedEvents().getAllMissedEvents();
                if (notifications != null && notifications.length > 0) {
                    Log.i(GracefulNfcLedCoverController.TAG, "onReceive; there are total " + notifications.length + " notifications to remove");
                    GracefulNfcLedCoverController.this.mLedStateMachine.processCustomNotificationsRemoved(notifications);
                }
            }
        }
    }

    class C00136 implements CallDisconnectReasonListener {
        C00136() {
        }

        public void onCallDisconnectReasonChanged(int type, String number, int userId) {
            Log.d(GracefulNfcLedCoverController.TAG, "onCallDisconnectReasonChanged type=" + String.valueOf(type) + " number=" + number);
            if (type == 5 || type == 3) {
                if (type == 5 && GracefulNfcLedCoverController.this.mHandler.hasMessages(23)) {
                    GracefulNfcLedCoverController.this.mHandler.removeMessages(23);
                }
                Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(26);
                msg.arg1 = type;
                msg.arg2 = userId;
                msg.obj = number;
                GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }
    }

    class C00147 extends BroadcastReceiver {
        C00147() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.e(GracefulNfcLedCoverController.TAG, "onReceive intent=null");
                return;
            }
            String action = intent.getAction();
            Log.d(GracefulNfcLedCoverController.TAG, "onReceive action=" + String.valueOf(action));
            if (Constants.INTENT_ACTION_ALARM_ALERT.equals(action) || Constants.INTENT_ACTION_ALARM_START_ALERT.equals(action)) {
                GracefulNfcLedCoverController.this.mHandler.sendEmptyMessage(7);
            } else if (Constants.INTENT_ACTION_ALARM_STOP_ALERT.equals(action)) {
                GracefulNfcLedCoverController.this.mHandler.sendEmptyMessage(8);
            } else if (Constants.INTENT_ACTION_TIMER_START_ALERT.equals(action)) {
                GracefulNfcLedCoverController.this.mHandler.sendEmptyMessage(9);
            } else if (Constants.INTENT_ACTION_TIMER_STOP_ALERT.equals(action)) {
                GracefulNfcLedCoverController.this.mHandler.sendEmptyMessage(10);
            } else if (Constants.INTENT_ACTION_CALL_TIME.equals(action)) {
                processCallTime(intent);
            } else if ("android.intent.action.PHONE_STATE".equals(action)) {
                processPhoneState(intent);
            } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {
                processHeadsetPlug(intent);
            } else if (Constants.INTENT_ACTION_LED_LAMP_NOTI.equals(action)) {
                GracefulNfcLedCoverController.this.mHandler.sendEmptyMessage(20);
            } else if (Constants.INTENT_ACTION_CALENDAR_START.equals(action)) {
                if (intent.getBooleanExtra("isPopupDisplayed", false)) {
                    GracefulNfcLedCoverController.this.mHandler.sendEmptyMessage(21);
                }
            } else if (Constants.INTENT_ACTION_CALENDAR_STOP.equals(action)) {
                GracefulNfcLedCoverController.this.mHandler.sendEmptyMessage(22);
            }
        }

        private void processPhoneState(Intent intent) {
            if (intent == null) {
                Log.e(GracefulNfcLedCoverController.TAG, "processPhonestate intent=null");
                return;
            }
            String stateString = intent.getStringExtra("state");
            Log.e(GracefulNfcLedCoverController.TAG, "processPhonestate stateString=" + String.valueOf(stateString));
            if (!TextUtils.isEmpty(stateString)) {
                if (CoverUtils.isTphoneRelaxMode(GracefulNfcLedCoverController.this.mContext)) {
                    Log.w(GracefulNfcLedCoverController.TAG, "TPhone Relaxed mode enabled, ignode PhoneState changes");
                } else if (stateString.equals(GracefulNfcLedCoverController.this.mPreviousPhoneState)) {
                    Log.e(GracefulNfcLedCoverController.TAG, "processPhoneState: state already applied");
                } else {
                    int isVideoCall;
                    Message msg;
                    GracefulNfcLedCoverController.this.mPreviousPhoneState = stateString;
                    int state = 0;
                    if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(stateString)) {
                        state = 2;
                    } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(stateString)) {
                        state = 1;
                    }
                    Log.e(GracefulNfcLedCoverController.TAG, "processPhonestate state=" + String.valueOf(state));
                    CallerData callerData = getCallerData(intent.getStringExtra("incoming_number"));
                    if (GracefulNfcLedCoverController.this.mTelephonyManager.semIsVideoCall()) {
                        isVideoCall = 1;
                    } else {
                        isVideoCall = 0;
                    }
                    if (GracefulNfcLedCoverController.this.mHandler.hasMessages(23)) {
                        GracefulNfcLedCoverController.this.mHandler.removeMessages(23);
                        msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(14);
                        msg.arg1 = 0;
                        msg.arg2 = 0;
                        msg.obj = null;
                        GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
                    }
                    msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(14);
                    msg.arg1 = state;
                    msg.arg2 = isVideoCall;
                    msg.obj = callerData;
                    GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
                    if (state == 0 && !GracefulNfcLedCoverController.this.mLedStateMachine.getLedContext().isInCallTouchReject()) {
                        Message delayedMsg = GracefulNfcLedCoverController.this.mHandler.obtainMessage();
                        delayedMsg.copyFrom(msg);
                        delayedMsg.what = 23;
                        GracefulNfcLedCoverController.this.mHandler.sendMessageDelayed(delayedMsg, 1000);
                    }
                }
            }
        }

        private CallerData getCallerData(String number) {
            if (TextUtils.isEmpty(number)) {
                Log.e(GracefulNfcLedCoverController.TAG, "getCallerData number=null");
                return null;
            }
            Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            Context currentUserContext = GracefulNfcLedCoverController.this.mContext;
            int currentUserID = ActivityManager.semGetCurrentUser();
            try {
                currentUserContext = RefContext.get().createPackageContextAsUser(GracefulNfcLedCoverController.this.mContext, "android", 0, RefUserHandle.get().getUserHandle(currentUserID));
                Log.d(GracefulNfcLedCoverController.TAG, "getContextForUser: " + currentUserID);
            } catch (NameNotFoundException e) {
                Log.e(GracefulNfcLedCoverController.TAG, "Error getting context for user: " + currentUserID);
            }
            Cursor contactLookup = currentUserContext.getContentResolver().query(uri, new String[]{"_id", "sec_led"}, "sec_led > 0", null, null);
            int secLed = -1;
            if (contactLookup == null || contactLookup.getCount() <= 0) {
                Log.d(GracefulNfcLedCoverController.TAG, "contact not found");
            } else if (contactLookup.moveToFirst()) {
                String contactId = contactLookup.getString(0);
                secLed = contactLookup.getInt(1);
                Log.d(GracefulNfcLedCoverController.TAG, "getCallerData : contactId=" + String.valueOf(contactId) + " secLed=" + String.valueOf(secLed));
            } else {
                Log.e(GracefulNfcLedCoverController.TAG, "getCallerData : Cannot access first result");
            }
            if (contactLookup != null) {
                contactLookup.close();
            }
            List<String> iconData = null;
            if (secLed > 0 && secLed < 40) {
                iconData = GracefulNfcLedCoverController.this.mLedStateMachine.getLedContext().getGraceLEDCoverCMD().getIncomingCallPresetCallIdData(secLed);
            } else if (secLed >= 40) {
                iconData = getCustomCallerData(currentUserContext, secLed);
            }
            if (iconData == null) {
                String formattedNumber = CoverUtils.formatNumber(number, GracefulNfcLedCoverController.this.mTelephonyManager, GracefulNfcLedCoverController.this.mCountryDetector);
                if (TextUtils.isEmpty(formattedNumber)) {
                    Log.e(GracefulNfcLedCoverController.TAG, "Could not format given phone number, return default null one");
                    return null;
                }
                iconData = GracefulNfcLedCoverController.this.mLedStateMachine.getLedContext().getGraceLEDCoverCMD().getUnknownIncomingCallData(formattedNumber);
            }
            return new CallerData(secLed, iconData);
        }

        private List<String> getCustomCallerData(Context currentUserContext, int icon_id) {
            String customData = null;
            String ledCoverAppContentUri = "content://com.samsung.android.app.ledcovergrace.cp";
            try {
                Cursor c = currentUserContext.getContentResolver().query(Uri.parse("content://com.samsung.android.app.ledcovergrace.cp"), new String[]{"icon_id", "icon_array"}, "icon_id=" + icon_id, null, null);
                if (c != null) {
                    int contactCount = c.getCount();
                    SemLog.d(GracefulNfcLedCoverController.TAG, "checkCallerIDCount() Count : " + contactCount);
                    if (contactCount > 0) {
                        int dataColumnIndex = c.getColumnIndex("icon_array");
                        if (dataColumnIndex != -1 && c.moveToLast()) {
                            customData = c.getString(dataColumnIndex);
                        }
                    }
                    c.close();
                }
                if (customData == null || customData.isEmpty()) {
                    return null;
                }
                return GracefulNfcLedCoverController.this.mLedStateMachine.getLedContext().getGraceLEDCoverCMD().getUserIconIncomingCallData(customData, 0);
            } catch (Exception e) {
                Log.e(GracefulNfcLedCoverController.TAG, "Error retrieving custom icon data", e);
                return null;
            }
        }

        private void processHeadsetPlug(Intent intent) {
            if (intent == null) {
                Log.e(GracefulNfcLedCoverController.TAG, "processHeadsetPlug intent=null");
                return;
            }
            int state = intent.getIntExtra("state", -1);
            Log.d(GracefulNfcLedCoverController.TAG, "processHeadsetPlug state=" + String.valueOf(state));
            if (state == 0 || state == 1) {
                Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(13);
                msg.arg1 = state;
                GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }

        private void processCallTime(Intent intent) {
            if (intent == null) {
                Log.e(GracefulNfcLedCoverController.TAG, "processCallTime intent=null");
                return;
            }
            long connectedTime = intent.getLongExtra("connectedTime", -1);
            Log.d(GracefulNfcLedCoverController.TAG, "processCallTime connectedTime=" + String.valueOf(connectedTime));
            if (connectedTime == -1) {
                String strCallTime = intent.getStringExtra("calldurationmillis");
                Log.d(GracefulNfcLedCoverController.TAG, "processCallTime strCallTime=" + String.valueOf(strCallTime));
                if (!TextUtils.isEmpty(strCallTime) && strCallTime.length() >= 5) {
                    Message msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(12);
                    msg.obj = strCallTime;
                    GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
                    return;
                }
                return;
            }
            long baseTime = intent.getLongExtra("calldurationmillis", -1);
            Log.d(GracefulNfcLedCoverController.TAG, "processCallTime baseTime=" + String.valueOf(baseTime));
            if (baseTime != -1) {
                msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(11);
                msg.obj = Long.valueOf(baseTime);
                GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }
    }

    class C00158 implements LedOffCallback {
        C00158() {
        }

        public void onLedOff() {
            GracefulNfcLedCoverController.this.mLedStateMachine.processTimeout();
        }
    }

    class C00169 implements OnClickListener {
        C00169() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            GracefulNfcLedCoverController.this.sendDownloadPopupEventBoardcast();
        }
    }

    private class CoverManagerSystemEventListener extends LedSystemEventListener {
        private static final int SYSTEM_EVENT_LCD_OFF_DISABLED_BY_COVER = 4;
        private static final int SYSTEM_EVENT_LED_OFF = 0;
        private static final int SYSTEM_EVENT_NOTIFICATION_ADD = 2;
        private static final int SYSTEM_EVENT_NOTIFICATION_REMOVE = 3;
        private static final int SYSTEM_EVENT_POWER_BUTTON = 1;

        private CoverManagerSystemEventListener() {
        }

        public void onSystemCoverEvent(int event, Bundle args) {
            Log.d(GracefulNfcLedCoverController.TAG, "onSystemCoverEvent event=" + String.valueOf(event) + " args=" + args);
            Message msg;
            switch (event) {
                case 0:
                    return;
                case 1:
                    GracefulNfcLedCoverController.this.mHandler.sendMessage(GracefulNfcLedCoverController.this.mHandler.obtainMessage(16));
                    return;
                case 2:
                    if (args == null) {
                        Log.e(GracefulNfcLedCoverController.TAG, "Null add notification data");
                        return;
                    }
                    msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(18);
                    msg.obj = getLedNotificationsFromSystem(args);
                    GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
                    return;
                case 3:
                    if (args == null) {
                        Log.e(GracefulNfcLedCoverController.TAG, "Null remove notification data");
                        return;
                    }
                    msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(19);
                    msg.obj = getLedNotificationsFromSystem(args);
                    GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
                    return;
                case 4:
                    if (args == null) {
                        Log.e(GracefulNfcLedCoverController.TAG, "Null Lcd Off disabled by Cover data");
                        return;
                    }
                    msg = GracefulNfcLedCoverController.this.mHandler.obtainMessage(25);
                    msg.obj = args;
                    GracefulNfcLedCoverController.this.mHandler.sendMessage(msg);
                    return;
                default:
                    Log.e(GracefulNfcLedCoverController.TAG, "onSystemCoverEvent unknown event");
                    return;
            }
        }

        private MissedEvent[] getLedNotificationsFromSystem(Bundle notificationData) {
            MissedEvent[] notifications = null;
            try {
                notifications = MissedEvent.getCustomNotificationEvents(notificationData);
            } catch (IllegalArgumentException e) {
                Log.e(GracefulNfcLedCoverController.TAG, "Error creating notifications", e);
            }
            return notifications;
        }
    }

    public GracefulNfcLedCoverController(Context context) {
        super(context);
        this.mCoverManager = new ScoverManager(context);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mCountryDetector = this.mContext.getSystemService(RefContext.get().COUNTRY_DETECTOR);
        this.mSystemEventListener = new CoverManagerSystemEventListener();
        this.mHandler = new Handler(this);
        this.mLedStateMachine = new LedStateMachine();
        this.mLedStateMachine.setListener(this);
        this.mLedPowerStateController = new LedPowerOnOffStateController(this.mContext, this.mLedStateMachine, this);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        this.mTimeoutWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "timeout led cover");
        this.mTimeoutWakeLock.setReferenceCounted(false);
    }

    private int getVolumeInformation(int streamType) {
        int stream;
        AudioManager am = (AudioManager) this.mContext.getSystemService("audio");
        boolean isInCall = this.mLedStateMachine.getLedContext().getCallState() == 2;
        if (isInCall) {
            if (am.isBluetoothScoOn()) {
                stream = 4;
            } else {
                stream = 0;
            }
            if (streamType != stream) {
                Log.d(TAG, "No valid inCall stream volume change for streamType: " + streamType);
                return -1;
            }
        } else if (streamType == 3 || streamType == 4) {
            stream = 3;
        } else {
            Log.d(TAG, "No valid music stream volume change for streamType: " + streamType);
            return -1;
        }
        int rawVolume = am.getStreamVolume(stream);
        int maxVolume = am.getStreamMaxVolume(stream);
        int minVolume = RefAudioManager.get().getStreamMinVolume(am, stream);
        Log.d(TAG, "getVolumeInformation isInCall=" + String.valueOf(isInCall) + " isBluetoothScoOn=" + String.valueOf(am.isBluetoothScoOn()) + " stream=" + String.valueOf(stream) + " rawVolume=" + String.valueOf(rawVolume) + " maxVolume=" + String.valueOf(maxVolume) + " minVolume=" + String.valueOf(minVolume));
        if (stream == 0 && maxVolume - minVolume == 5) {
            Log.d(TAG, "getVolumeInformation adjust volume range for voice call");
            return Constants.CALL_VOLUME_6[rawVolume - minVolume];
        } else if (stream == 0 && maxVolume - minVolume == 7) {
            Log.d(TAG, "getVolumeInformation adjust volume range for voice call");
            return Constants.CALL_VOLUME_8[rawVolume - minVolume];
        } else {
            int volume;
            if ((stream == 4 || stream == 0) && minVolume < 1) {
                rawVolume += 1 - minVolume;
                maxVolume += 1 - minVolume;
            }
            if (rawVolume <= minVolume) {
                volume = 0;
            } else if (rawVolume >= maxVolume) {
                volume = 15;
            } else {
                volume = (int) ((((float) rawVolume) / ((float) (maxVolume - minVolume))) * 15.0f);
            }
            if (volume < 0) {
                volume = 0;
            } else if (volume > 15) {
                volume = 15;
            }
            Log.d(TAG, "getVolumeInformation volume=" + String.valueOf(volume));
            return volume;
        }
    }

    private void requestRemoteViews() {
        Log.d(TAG, "requestRemoteViews");
        this.mContext.sendBroadcast(new Intent(Constants.ACTION_REQUEST_REMOTE_VIEW));
    }

    public void onCoverAttached(ScoverState state) {
        boolean z = false;
        Log.d(TAG, "onCoverAttached state=" + String.valueOf(state));
        if (Secure.getInt(this.mContext.getContentResolver(), "nfc_led_cover_test", 0) == 42) {
            z = true;
        }
        this.mCoverEventsDisabledForDemoMode = z;
        if (this.mCoverEventsDisabledForDemoMode) {
            Log.d(TAG, "onCoverAttached: Demo mode - ignore cover events");
            return;
        }
        super.onCoverAttached(state);
        this.mLedPowerStateController.onCoverAttached();
        this.mLedStateMachine.processCallStateChange(this.mTelephonyManager.getCallState(), null, this.mTelephonyManager.semIsVideoCall());
        CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().registerCallback(this.mUpdateMonitorCallback);
        IntentFilter filter = new IntentFilter();
        for (String addAction : strIntentAction) {
            filter.addAction(addAction);
        }
        this.mContext.semRegisterReceiverAsUser(this.mBroadcastReceiver, UserHandle.SEM_ALL, filter, null, null);
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPlaybackStateMonitor().setOnPlaybackStateChangedListener(this.mPlaybackStateChangedListener);
        CoverExecutiveObservator.getInstance(this.mContext).getMissedEventManager().setListener(this.mMissedEventListener);
        CoverExecutiveObservator.getInstance(this.mContext).getCallDisconnectReasonManager().setListener(this.mCallDisconnectReasonListener);
        requestRemoteViews();
        try {
            this.mCoverManager.registerLedSystemListener(this.mSystemEventListener);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "Error registering listener", e);
        }
        if (CoverUtils.isPackageExist(this.mContext, Constants.PACKAGE_NAME_GRACELED)) {
            Log.d(TAG, "Grace LED APP installed - no need to show popup");
        } else if (CoverUtils.isSetupWizardRunning(this.mContext)) {
            registerDeviceProvisionedObserver();
        } else {
            showDownloadLEDCoverFeaturePopup();
        }
        registerLedCoverAppUninstallReceiver();
    }

    public void onCoverDetatched(ScoverState state) {
        Log.d(TAG, "onCoverDetatched state=" + String.valueOf(state));
        if (this.mCoverEventsDisabledForDemoMode) {
            Log.d(TAG, "onCoverDetatched: Demo mode - ignore cover events");
            return;
        }
        unRegisterLedCoverAppUninstallReceiver();
        unRegisterDeviceProvisionedObserver();
        this.mLedPowerStateController.onCoverDetached();
        this.mLedStateMachine.reset();
        CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPlaybackStateMonitor().setOnPlaybackStateChangedListener(null);
        CoverExecutiveObservator.getInstance(this.mContext).getMissedEventManager().setListener(null);
        CoverExecutiveObservator.getInstance(this.mContext).getCallDisconnectReasonManager().setListener(null);
        try {
            this.mCoverManager.unregisterLedSystemEventListener(this.mSystemEventListener);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "Error unregistering listener", e);
        }
        cancelLedLampNoti();
        if (this.mDownloadFeaturesDialog != null) {
            if (this.mDownloadFeaturesDialog.isShowing()) {
                this.mDownloadFeaturesDialog.dismiss();
            }
            this.mDownloadFeaturesDialog = null;
        }
        this.mHandler.removeCallbacksAndMessages(null);
        super.onCoverDetatched(state);
    }

    public void onCoverEvent(ScoverState state) {
        Log.d(TAG, "onCoverEvent state=" + String.valueOf(state));
        super.onCoverEvent(state);
        if (this.mCoverEventsDisabledForDemoMode || this.mCoverEventsDisabledForSamsungPay) {
            Log.d(TAG, "onCoverEvent: Demo mode or SamsungPay active - ignore cover events");
            if (state != null && !state.getSwitchState()) {
                coverEventFinished();
                if (!this.mCoverEventsDisabledForSamsungPay) {
                    CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager().goToSleep();
                    return;
                }
                return;
            } else if (!this.mCoverEventsDisabledForSamsungPay) {
                CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager().wakeUpWithReason();
                return;
            } else {
                return;
            }
        }
        this.mLedStateMachine.setCoverClosed(!isCoverOpen());
        if (isCoverOpen()) {
            cancelTimeout();
            this.mLedStateMachine.processCoverCloseWakeUp(false);
            this.mLedPowerStateController.onCoverOpened();
            CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager().wakeUpWithReason();
            cancelLedLampNoti();
            return;
        }
        this.mLedStateMachine.processCoverCloseWakeUp(true);
        this.mLedPowerStateController.onCoverClosed();
        coverEventFinished();
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager().goToSleep();
    }

    public boolean isCoverOpen() {
        if (this.mCoverEventsDisabledForSamsungPay || this.mCoverEventsDisabledForDemoMode) {
            return true;
        }
        return super.isCoverOpen();
    }

    public void onStateChange(LedState ledState, LedContext ledContext, boolean shouldTurnLedOn) {
        Log.d(TAG, "onStateChange ledState=" + String.valueOf(ledState) + " ledContext=" + String.valueOf(ledContext) + " shouldTurnLedOn=" + String.valueOf(shouldTurnLedOn));
        if (ledState != null && ledContext != null) {
            if (this.mLedPowerStateController.isLedTurnedOnOrRestarting()) {
                cancelTimeout();
            }
            this.mLedPowerStateController.onStateChange(ledState, ledContext, shouldTurnLedOn);
            if (ledState == LedState.IDLE && !isCoverOpen()) {
                cancelLedLampNoti();
                initLedLampNoti();
            }
        }
    }

    private void sendDownloadPopupEventBoardcast() {
        Intent intent = new Intent(Constants.ACTION_DOWNLOAD_REQUEST_LINK);
        byte[] pkg = Constants.PACKAGE_NAME_GRACELED.getBytes();
        byte[] uri = new byte[(pkg.length + 2)];
        uri[0] = (byte) (pkg.length + 1);
        uri[1] = (byte) 3;
        System.arraycopy(pkg, 0, uri, 2, pkg.length);
        intent.putExtra("URI", uri);
        intent.putExtra("DEVICE_TYPE", "cover");
        intent.putExtra("NAME", "LED Cover");
        this.mContext.sendBroadcastAsUser(intent, UserHandle.SEM_ALL);
        Log.d(TAG, "show grace led download popup");
    }

    private void initLedLampNoti() {
        boolean hasMissedEvent = this.mLedStateMachine.getLedContext().hasState(LedState.MISSED_EVENT);
        boolean shouldWakeForLedLamp = this.mLedStateMachine.getLedContext().shouldWakeupForLedLamp();
        Log.d(TAG, "initLedLampNoti: hasMissedEvent=" + hasMissedEvent + " hasCustomNoti=" + " shouldWakeForLedLamp=" + shouldWakeForLedLamp);
        if (hasMissedEvent && shouldWakeForLedLamp) {
            Log.d(TAG, "Missed notifications exist. InitLedLampNoti");
            this.mAlarmManager.setExact(0, System.currentTimeMillis() + 60000, getLedLampNotiPendingIntent());
        }
    }

    private void cancelLedLampNoti() {
        Log.d(TAG, "cancelLedLampNoti");
        PendingIntent pIntent = getLedLampNotiPendingIntent();
        pIntent.cancel();
        this.mAlarmManager.cancel(pIntent);
    }

    private PendingIntent getLedLampNotiPendingIntent() {
        return PendingIntent.getBroadcast(this.mContext, 0, new Intent(Constants.INTENT_ACTION_LED_LAMP_NOTI).addFlags(268435456), 134217728);
    }

    public void onStateSent(LedState state) {
        Log.d(TAG, "onStateSent state=" + String.valueOf(state));
        this.mLedStateMachine.getLedContext().setIsInCallTouchReject(false);
        if (state == null) {
            scheduleTimeout(0);
            return;
        }
        cancelTimeout();
        if (!state.isInfinite()) {
            long timeout = state.getTimeout();
            Log.d(TAG, "onStateSent timeout=" + String.valueOf(timeout));
            scheduleTimeout(timeout);
        }
    }

    public void onTouchEvent(boolean reject) {
        Log.d(TAG, "onTouchEvent reject=" + reject);
        this.mLedStateMachine.getLedContext().setIsInCallTouchReject(reject);
    }

    private void registerLedCoverAppUninstallReceiver() {
        Log.i(TAG, "registerLedCoverAppUninstallReceiver; registered? " + this.mLedCoverAppUninstallReceiverRegistered);
        if (!this.mLedCoverAppUninstallReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            intentFilter.addDataSchemeSpecificPart(Constants.PACKAGE_NAME_GRACELED, 0);
            this.mContext.registerReceiver(this.mLedAppUninstallBroadcastReceiver, intentFilter);
            this.mLedCoverAppUninstallReceiverRegistered = true;
        }
    }

    private void unRegisterLedCoverAppUninstallReceiver() {
        Log.i(TAG, "unRegisterLedCoverAppUninstallReceiver; registered? " + this.mLedCoverAppUninstallReceiverRegistered);
        if (this.mLedCoverAppUninstallReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mLedAppUninstallBroadcastReceiver);
            this.mLedCoverAppUninstallReceiverRegistered = false;
        }
    }

    private void registerDeviceProvisionedObserver() {
        Log.i(TAG, "registerDeviceProvisionedObserver; registered? " + this.mDeviceProvisionedObserverRegistered);
        if (!this.mDeviceProvisionedObserverRegistered) {
            RefContentResolver.get().registerContentObserver(this.mContext.getContentResolver(), Global.getUriFor("device_provisioned"), true, this.mDeviceProvisionedObserver, UserHandle.SEM_ALL.semGetIdentifier());
            this.mDeviceProvisionedObserverRegistered = true;
        }
    }

    private void unRegisterDeviceProvisionedObserver() {
        Log.i(TAG, "unRegisterDeviceProvisionedObserver; registered? " + this.mDeviceProvisionedObserverRegistered);
        if (this.mDeviceProvisionedObserverRegistered) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mDeviceProvisionedObserver);
            this.mDeviceProvisionedObserverRegistered = false;
        }
    }

    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage msg=" + String.valueOf(msg));
        switch (msg.what) {
            case 0:
                this.mLedPowerStateController.onLedTimeOut(new C00158());
                if (!this.mHandler.hasMessages(0)) {
                    CoverUtils.releaseWakeLockSafely(this.mTimeoutWakeLock);
                }
                return true;
            case 1:
                if (msg.obj instanceof MusicState) {
                    this.mLedStateMachine.processMusicStateChange((MusicState) msg.obj);
                    return true;
                }
                Log.e(TAG, "handleMessage MSG_EVENT_MUSIC_STATE obj is not MusicState");
                return false;
            case 2:
                this.mLedStateMachine.processUnreadMessage(msg.arg1, msg.arg2 == 1, (MissedEvent) msg.obj);
                return true;
            case 3:
                this.mLedStateMachine.processMissedCall(msg.arg1, msg.arg2 == 1, (MissedEvent) msg.obj);
                return true;
            case 4:
                this.mLedStateMachine.processVolumeChange(msg.arg1);
                return true;
            case 5:
                if (msg.obj instanceof BatteryStatus) {
                    this.mLedStateMachine.processBatteryStatusChange((BatteryStatus) msg.obj);
                    return true;
                }
                Log.e(TAG, "handleMessage MSG_EVENT_BATTERY_CHANGED obj is not BatteryStatus");
                return false;
            case 7:
                this.mLedStateMachine.processAlarmStart();
                return true;
            case 8:
                this.mLedStateMachine.processAlarmStop();
                return true;
            case 9:
                this.mLedStateMachine.processTimerStart();
                return true;
            case 10:
                this.mLedStateMachine.processTimerStop();
                return true;
            case 11:
                if (msg.obj instanceof Long) {
                    this.mLedStateMachine.processCallTime(((Long) msg.obj).longValue());
                    return true;
                }
                Log.e(TAG, "handleMessage MSG_EVENT_CALL_TIME obj is not Long");
                return false;
            case 12:
                if (msg.obj instanceof String) {
                    this.mLedStateMachine.processCallEnd((String) msg.obj);
                    return true;
                }
                Log.e(TAG, "handleMessage MSG_EVENT_CALL_END obj is not String");
                return false;
            case 13:
                this.mLedStateMachine.processHeadsetPlugChange(msg.arg1 == 1);
                return true;
            case 14:
            case 23:
                if (msg.obj == null || (msg.obj instanceof CallerData)) {
                    this.mLedStateMachine.processCallStateChange(msg.arg1, msg.obj, msg.arg2 == 1);
                    return true;
                }
                Log.e(TAG, "handleMessage MSG_EVENT_CALL_STATE_CHANGED obj is not CallerData");
                return false;
            case 15:
                if (msg.obj instanceof Bundle) {
                    Bundle bundle = msg.obj;
                    this.mLedStateMachine.processVoiceRecStateChange(VoiceRecorderState.values()[bundle.getInt(Constants.KEY_VOICE_RECORDER_STATUS, 0)], bundle.getLong(Constants.KEY_VOICE_RECORDER_START_TIME, -1));
                    return true;
                }
                Log.e(TAG, "handleMessage MSG_EVENT_VOICE_REC_STATE obj is not Bundle");
                return false;
            case 16:
                if (this.mCoverEventsDisabledForSamsungPay) {
                    Log.d(TAG, "handleMessage MSG_EVENT_POWER_BUTTON: SamsungPay active - ignore power button events");
                    return true;
                }
                cancelTimeout();
                Log.i(TAG, "Power button was pressed, isLedTurnedOnOrRestarting: " + this.mLedPowerStateController.isLedTurnedOnOrRestarting());
                if (this.mLedPowerStateController.isLedTurnedOnOrRestarting()) {
                    this.mLedStateMachine.processPowerButtonWakeUp(false);
                    this.mLedPowerStateController.onPowerKeyToCover(false);
                } else {
                    this.mLedStateMachine.processPowerButtonWakeUp(true);
                    this.mLedPowerStateController.onPowerKeyToCover(true);
                    cancelLedLampNoti();
                }
                return true;
            case 18:
                this.mLedStateMachine.processCustomNotificationsAdded((MissedEvent[]) msg.obj);
                return true;
            case 19:
                this.mLedStateMachine.processCustomNotificationsRemoved((MissedEvent[]) msg.obj);
                return true;
            case 20:
                if (!this.mLedPowerStateController.isLedTurnedOnOrRestarting()) {
                    this.mLedStateMachine.processLedLampNoti();
                }
                return true;
            case 21:
                this.mLedStateMachine.processCalendarStart();
                return true;
            case 22:
                this.mLedStateMachine.processCalendarStop();
                return true;
            case 24:
                showDownloadLEDCoverFeaturePopup();
                return true;
            case 25:
                handleLcdOffDisabledByCover(msg.obj.getBoolean("lcd_off_disabled_by_cover"));
                return true;
            case 26:
                this.mLedStateMachine.processCallDisconnectReason(msg.arg1, (String) msg.obj, msg.arg2);
                return true;
            case MSG_EVENT_NEW_MESSAGE_INACTIVE_USER /*2000*/:
                this.mLedStateMachine.processUnreadMessageForInactiveUser((MissedEvent) msg.obj);
                return true;
            case MSG_EVENT_NEW_MISSED_CALL_INACTIVE_USER /*3000*/:
                this.mLedStateMachine.processMissedCallForInactiveUser();
                return true;
            default:
                return false;
        }
    }

    private void handleLcdOffDisabledByCover(boolean lcdOffDisabledByCover) {
        Log.d(TAG, "handleLcdOffDisabledByCover mCoverEventsDisabledForSamsungPay=" + this.mCoverEventsDisabledForSamsungPay + " lcdOffDisabledByCover=" + lcdOffDisabledByCover);
        if (this.mCoverEventsDisabledForSamsungPay == lcdOffDisabledByCover) {
            return;
        }
        if (lcdOffDisabledByCover) {
            ScoverState oldState = getCoverState();
            onCoverEvent(null);
            this.mCoverEventsDisabledForSamsungPay = lcdOffDisabledByCover;
            setCoverState(oldState);
            return;
        }
        this.mCoverEventsDisabledForSamsungPay = lcdOffDisabledByCover;
        onCoverEvent(getCoverState());
    }

    private void showDownloadLEDCoverFeaturePopup() {
        Log.d(TAG, "showDownloadLEDCoverFeaturePopup");
        if (this.mDownloadFeaturesDialog == null) {
            this.mDownloadFeaturesDialog = createDownloadLEDCoverFeaturePopup();
        }
        this.mDownloadFeaturesDialog.show();
    }

    private Dialog createDownloadLEDCoverFeaturePopup() {
        String menuTreeCode = SemCscFeature.getInstance().getString(RefCscFeatureTagSetting.get().TAG_CSCFEATURE_SETTING_CONFIGOPMENUSTRUCTURE);
        Log.d(TAG, "createDownloadLEDCoverFeaturePopup");
        Builder ab = new Builder(this.mContext);
        ab.setTitle(C0026R.string.sview_led_cover_download_feature_title);
        if (menuTreeCode != null) {
            if ("VZW".equals(menuTreeCode)) {
                ab.setMessage(C0026R.string.sview_led_cover_download_feature_summary_vzw);
            } else {
                ab.setMessage(C0026R.string.sview_led_cover_download_feature_summary);
            }
        }
        ab.setPositiveButton(C0026R.string.monotype_dialog_button, new C00169());
        ab.setNegativeButton(17039360, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        Dialog d = ab.create();
        d.getWindow().setType(RefWindowManagerLayoutParams.get().TYPE_SVIEW_COVER_DIALOG);
        d.getWindow().getAttributes().semAddPrivateFlags(16);
        return d;
    }

    private void scheduleTimeout(long timeout) {
        CoverUtils.acquireWakeLockSafely(this.mTimeoutWakeLock);
        this.mHandler.sendEmptyMessageDelayed(0, timeout);
    }

    private void cancelTimeout() {
        this.mHandler.removeMessages(0);
        CoverUtils.releaseWakeLockSafely(this.mTimeoutWakeLock);
    }
}
