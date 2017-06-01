package com.sec.android.cover.ledcover;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
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
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.LedSystemEventListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.samsung.android.util.SemLog;
import com.sec.android.cover.BaseCoverController;
import com.sec.android.cover.Constants;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.CoverUtils;
import com.sec.android.cover.ledcover.fsm.dream.DreamUtils;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.CallerData;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.VoiceRecorderState;
import com.sec.android.cover.ledcover.fsm.dream.LedPowerOnOffStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedPowerOnOffStateController.LedOffCallback;
import com.sec.android.cover.ledcover.fsm.dream.LedPowerOnOffStateController.OutgoingSystemEvent;
import com.sec.android.cover.ledcover.fsm.dream.LedPowerOnOffStateController.TransceiveCallback;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedStateMachine;
import com.sec.android.cover.ledcover.fsm.dream.LedStateMachine.LedStateMachineListener;
import com.sec.android.cover.ledcover.fsm.dream.MockeryLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.missedevent.MissedEvent;
import com.sec.android.cover.ledcover.reflection.content.RefContentResolver;
import com.sec.android.cover.ledcover.reflection.content.RefContext;
import com.sec.android.cover.ledcover.reflection.media.RefAudioManager;
import com.sec.android.cover.ledcover.reflection.os.RefFactoryTest;
import com.sec.android.cover.ledcover.reflection.os.RefUserHandle;
import com.sec.android.cover.ledcover.reflection.text.RefDateFormat;
import com.sec.android.cover.manager.CallDisconnectReasonManager.CallDisconnectReasonListener;
import com.sec.android.cover.monitor.CoverPlaybackStateMonitor.OnPlaybackStateChangedListener;
import com.sec.android.cover.monitor.CoverUpdateMonitor.BatteryStatus;
import com.sec.android.cover.monitor.CoverUpdateMonitor.RemoteViewInfo;
import com.sec.android.cover.monitor.CoverUpdateMonitorCallback;
import java.util.Arrays;

public class DreamyNfcLedCoverController extends BaseCoverController implements Callback, LedStateMachineListener, TransceiveCallback {
    private static final String ACTION_COVER_READY = "com.sec.android.cover.ledcover.action.COVER_READY";
    private static final int LAMP_NOTI_TIMEOUT = 60000;
    private static final String LED_NOTI_PERMISSION = "com.samsung.android.app.ledcover.LAUNCH";
    private static final int MAX_VOLUME = 15;
    private static final int MIN_CALL_VOLUME = 1;
    private static final int MSG_EVENT_ALARM_START = 7;
    private static final int MSG_EVENT_ALARM_STOP = 8;
    private static final int MSG_EVENT_BATTERY_CHANGED = 5;
    private static final int MSG_EVENT_BIXBY_STATE_CHANGE = 27;
    private static final int MSG_EVENT_CALENDAR_START = 21;
    private static final int MSG_EVENT_CALENDAR_STOP = 22;
    private static final int MSG_EVENT_CALL_DISCONNECT_REASON = 25;
    private static final int MSG_EVENT_CALL_END = 12;
    private static final int MSG_EVENT_CALL_STATE_CHANGED = 14;
    private static final int MSG_EVENT_CALL_STATE_IDLE_DELAYED = 23;
    private static final int MSG_EVENT_CALL_TIME = 11;
    private static final int MSG_EVENT_CUSTOM_NOTIFICATION_ADD = 18;
    private static final int MSG_EVENT_CUSTOM_NOTIFICATION_REMOVE = 19;
    private static final int MSG_EVENT_HEADSET_PLUG = 13;
    private static final int MSG_EVENT_LED_LAMP_NOTI = 20;
    private static final int MSG_EVENT_MEDIA_KEY = 24;
    private static final int MSG_EVENT_MUSIC_STATE = 1;
    private static final int MSG_EVENT_NOTI_DND_MODE_CHANGED = 28;
    private static final int MSG_EVENT_POWER_BUTTON = 16;
    private static final int MSG_EVENT_TIMEOUT = 0;
    private static final int MSG_EVENT_TIMER_START = 9;
    private static final int MSG_EVENT_TIMER_STOP = 10;
    private static final int MSG_EVENT_TIME_UPDATE = 102;
    private static final int MSG_EVENT_USER_SWITCHED = 26;
    private static final int MSG_EVENT_VOICE_REC_STATE = 15;
    private static final int MSG_EVENT_VOLUME_CHANGED = 4;
    private static final int MSG_LCD_OFF_DISABLED_BY_COVER = 100;
    private static final int MSG_SYSTEM_FOTA_IN_PROGRESS_START = 103;
    private static final int MSG_SYSTEM_FOTA_IN_PROGRESS_STOP = 104;
    private static final int MSG_SYSTEM_SEND_COMMAND = 101;
    private static final long PLAYBACK_STATE_CHANGE_DELAY = 500;
    private static final String TAG = "LedService.DreamyNfcLedCoverController";
    private static final String[] strIntentAction = new String[]{"android.intent.action.PHONE_STATE", Constants.INTENT_ACTION_ALARM_ALERT, Constants.INTENT_ACTION_ALARM_START_ALERT, Constants.INTENT_ACTION_ALARM_STOP_ALERT, Constants.INTENT_ACTION_CALL_TIME, "android.intent.action.HEADSET_PLUG", Constants.INTENT_ACTION_TIMER_STOP_ALERT, Constants.INTENT_ACTION_TIMER_START_ALERT, Constants.INTENT_ACTION_LED_LAMP_NOTI, Constants.INTENT_ACTION_CALENDAR_START, Constants.INTENT_ACTION_CALENDAR_STOP, "android.app.action.INTERRUPTION_FILTER_CHANGED", "android.app.action.NOTIFICATION_POLICY_CHANGED"};
    private AlarmManager mAlarmManager;
    private BroadcastReceiver mBroadcastReceiver = new C00056();
    private CallDisconnectReasonListener mCallDisconnectReasonListener = new C00045();
    private Object mCountryDetector;
    private boolean mCoverEventsDisabledForDemoMode;
    private boolean mCoverEventsDisabledForSamsungPay;
    private ScoverManager mCoverManager;
    private ContentObserver mDemoModeSettingObserver;
    private boolean mFotaInProgress;
    private Handler mHandler;
    private boolean mIsFactoryTest;
    private LedPowerOnOffStateController mLedPowerStateController;
    private LedStateMachine mLedStateMachine;
    private OnPlaybackStateChangedListener mPlaybackStateChangedListener = new C00034();
    private String mPreviousPhoneState;
    private CoverManagerSystemEventListener mSystemEventListener;
    private TelephonyManager mTelephonyManager;
    private WakeLock mTimeoutWakeLock;
    private ContentObserver mUPSMModeSettingObserver;
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00023();

    class C00023 extends CoverUpdateMonitorCallback {
        C00023() {
        }

        public void onRefreshBatteryInfo(BatteryStatus status) {
            super.onRefreshBatteryInfo(status);
            Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(5);
            msg.obj = status;
            DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
        }

        public void onContentChanged(Uri uri) {
            if (uri.toString().contains("time_12_24")) {
                onTimeChanged();
            }
        }

        public void onTimeChanged() {
            DreamyNfcLedCoverController.this.mLedStateMachine.getLedContext().set24HourFormat(RefDateFormat.get().is24HourFormat(DreamyNfcLedCoverController.this.mContext, DreamyNfcLedCoverController.this.getCurrentUserId()));
        }

        public void onVolumeChanged(int streamType, int val) {
            super.onVolumeChanged(streamType, val);
            int value = DreamyNfcLedCoverController.this.getVolumeInformation(streamType);
            if (value >= 0) {
                Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(4);
                msg.arg1 = value;
                DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }

        public void onRemoteViewUpdated(RemoteViewInfo remoteViewInfo) {
            super.onRemoteViewUpdated(remoteViewInfo);
            if (remoteViewInfo == null) {
                Log.e(DreamyNfcLedCoverController.TAG, "onRemoteViewUpdated remoteViewInfo=null");
                return;
            }
            String type = remoteViewInfo.mType;
            Log.d(DreamyNfcLedCoverController.TAG, "onRemoteViewUpdated type=" + String.valueOf(type));
            if (Constants.TYPE_VOICE_RECORDER.equals(type)) {
                int status = remoteViewInfo.mIntent.getIntExtra(Constants.KEY_VOICE_RECORDER_STATUS, -1);
                long recordingStartTime = remoteViewInfo.mIntent.getLongExtra(Constants.KEY_VOICE_RECORDER_START_TIME, -1);
                VoiceRecorderState state = null;
                Log.d(DreamyNfcLedCoverController.TAG, "onRemoteViewUpdated status=" + String.valueOf(status) + " recordingStartTime=" + String.valueOf(recordingStartTime) + " recordedTime=" + String.valueOf((int) (SystemClock.elapsedRealtime() - recordingStartTime)));
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
                        Log.e(DreamyNfcLedCoverController.TAG, "onRemoteViewUpdated unknown status");
                        break;
                }
                if (state != null) {
                    Bundle voiceRecorderBundle = new Bundle();
                    voiceRecorderBundle.putInt(Constants.KEY_VOICE_RECORDER_STATUS, state.ordinal());
                    voiceRecorderBundle.putLong(Constants.KEY_VOICE_RECORDER_START_TIME, recordingStartTime);
                    Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(15);
                    msg.obj = voiceRecorderBundle;
                    DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
                }
            }
        }

        public void onUserSwitched(int newUserId, int oldUserId) {
            DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(26);
        }

        public void onBixbyStateChanged(int state) {
            Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(27);
            msg.arg1 = state;
            DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
        }
    }

    class C00034 implements OnPlaybackStateChangedListener {
        C00034() {
        }

        public void onPlaybackStateChanged(PlaybackState playbackState) {
            Log.d(DreamyNfcLedCoverController.TAG, "onPlaybackStateChanged playbackState=" + String.valueOf(playbackState));
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
            DreamyNfcLedCoverController.this.mHandler.removeMessages(1);
            Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(1);
            msg.obj = musicState;
            if (musicState == MusicState.PAUSE) {
                DreamyNfcLedCoverController.this.mHandler.sendMessageDelayed(msg, DreamyNfcLedCoverController.PLAYBACK_STATE_CHANGE_DELAY);
            } else {
                DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }

        public void onMetadataChanged(MediaMetadata metadata) {
        }

        public void onSessionDestroyed() {
            MusicState currentState = DreamyNfcLedCoverController.this.mLedStateMachine.getLedContext().getMusicState();
            Log.d(DreamyNfcLedCoverController.TAG, "onSessionDestroyed Music currentState=" + currentState);
            if (currentState != MusicState.STOP) {
                Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(1);
                msg.obj = MusicState.STOP;
                DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }
    }

    class C00045 implements CallDisconnectReasonListener {
        C00045() {
        }

        public void onCallDisconnectReasonChanged(int type, String number, int userId) {
            Log.d(DreamyNfcLedCoverController.TAG, "onCallDisconnectReasonChanged type=" + String.valueOf(type));
            if (type == 5 || type == 3) {
                if (type == 5 && DreamyNfcLedCoverController.this.mHandler.hasMessages(23)) {
                    DreamyNfcLedCoverController.this.mHandler.removeMessages(23);
                }
                Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(25);
                msg.arg1 = type;
                msg.arg2 = userId;
                msg.obj = number;
                DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }
    }

    class C00056 extends BroadcastReceiver {
        C00056() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Log.e(DreamyNfcLedCoverController.TAG, "onReceive intent=null");
                return;
            }
            String action = intent.getAction();
            Log.d(DreamyNfcLedCoverController.TAG, "onReceive action=" + String.valueOf(action));
            if (Constants.INTENT_ACTION_ALARM_ALERT.equals(action) || Constants.INTENT_ACTION_ALARM_START_ALERT.equals(action)) {
                DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(7);
            } else if (Constants.INTENT_ACTION_ALARM_STOP_ALERT.equals(action)) {
                DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(8);
            } else if (Constants.INTENT_ACTION_TIMER_START_ALERT.equals(action)) {
                DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(9);
            } else if (Constants.INTENT_ACTION_TIMER_STOP_ALERT.equals(action)) {
                DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(10);
            } else if (Constants.INTENT_ACTION_CALL_TIME.equals(action)) {
                processCallTime(intent);
            } else if ("android.intent.action.PHONE_STATE".equals(action)) {
                processPhoneState(intent);
            } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {
                processHeadsetPlug(intent);
            } else if (Constants.INTENT_ACTION_LED_LAMP_NOTI.equals(action)) {
                DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(20);
            } else if (Constants.INTENT_ACTION_CALENDAR_START.equals(action)) {
                if (intent.getBooleanExtra("isPopupDisplayed", false)) {
                    DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(21);
                }
            } else if (Constants.INTENT_ACTION_CALENDAR_STOP.equals(action)) {
                DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(22);
            } else if ("android.app.action.INTERRUPTION_FILTER_CHANGED".equals(action) || "android.app.action.NOTIFICATION_POLICY_CHANGED".equals(action)) {
                DreamyNfcLedCoverController.this.mHandler.sendEmptyMessage(28);
            }
        }

        private void processPhoneState(Intent intent) {
            if (intent == null) {
                Log.e(DreamyNfcLedCoverController.TAG, "processPhonestate intent=null");
                return;
            }
            String stateString = intent.getStringExtra("state");
            Log.e(DreamyNfcLedCoverController.TAG, "processPhonestate stateString=" + String.valueOf(stateString));
            if (!TextUtils.isEmpty(stateString)) {
                if (CoverUtils.isTphoneRelaxMode(DreamyNfcLedCoverController.this.mContext)) {
                    Log.w(DreamyNfcLedCoverController.TAG, "TPhone Relaxed mode enabled, ignode PhoneState changes");
                } else if (stateString.equals(DreamyNfcLedCoverController.this.mPreviousPhoneState)) {
                    Log.e(DreamyNfcLedCoverController.TAG, "processPhoneState: state already applied");
                } else {
                    int isVideoCall;
                    Message msg;
                    DreamyNfcLedCoverController.this.mPreviousPhoneState = stateString;
                    int state = 0;
                    if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(stateString)) {
                        state = 2;
                    } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(stateString)) {
                        if (DreamyNfcLedCoverController.this.mLedStateMachine.getLedContext().isDoNotDisturbNotiOffPolicy()) {
                            Log.w(DreamyNfcLedCoverController.TAG, "Do not disturb mode on, ignore incomming call");
                        } else {
                            state = 1;
                        }
                    }
                    Log.e(DreamyNfcLedCoverController.TAG, "processPhonestate state=" + String.valueOf(state));
                    CallerData callerData = getCallerData(intent.getStringExtra("incoming_number"));
                    if (DreamyNfcLedCoverController.this.mTelephonyManager.semIsVideoCall()) {
                        isVideoCall = 1;
                    } else {
                        isVideoCall = 0;
                    }
                    if (DreamyNfcLedCoverController.this.mHandler.hasMessages(23)) {
                        DreamyNfcLedCoverController.this.mHandler.removeMessages(23);
                        msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(14);
                        msg.arg1 = 0;
                        msg.arg2 = 0;
                        msg.obj = null;
                        DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
                    }
                    msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(14);
                    msg.arg1 = state;
                    msg.arg2 = isVideoCall;
                    msg.obj = callerData;
                    DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
                    if (state == 0 && !DreamyNfcLedCoverController.this.mLedStateMachine.getLedContext().isInCallTouchReject()) {
                        Message delayedMsg = DreamyNfcLedCoverController.this.mHandler.obtainMessage();
                        delayedMsg.copyFrom(msg);
                        delayedMsg.what = 23;
                        DreamyNfcLedCoverController.this.mHandler.sendMessageDelayed(delayedMsg, 2000);
                    }
                }
            }
        }

        private CallerData getCallerData(String number) {
            if (TextUtils.isEmpty(number)) {
                Log.e(DreamyNfcLedCoverController.TAG, "getCallerData number=null");
                return null;
            } else if (DreamyNfcLedCoverController.this.mLedStateMachine.getLedContext().isUPSMEnabled()) {
                Log.d(DreamyNfcLedCoverController.TAG, "Call in emergency mode: skip number & icon check");
                return new CallerData(0, null, null, false, true);
            } else {
                Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
                Context currentUserContext = DreamyNfcLedCoverController.this.mContext;
                int currentUserID = ActivityManager.semGetCurrentUser();
                try {
                    currentUserContext = RefContext.get().createPackageContextAsUser(DreamyNfcLedCoverController.this.mContext, "android", 0, RefUserHandle.get().getUserHandle(currentUserID));
                    Log.d(DreamyNfcLedCoverController.TAG, "getContextForUser: " + currentUserID);
                } catch (NameNotFoundException e) {
                    Log.e(DreamyNfcLedCoverController.TAG, "Error getting context for user: " + currentUserID);
                }
                ContentResolver contentResolver = currentUserContext.getContentResolver();
                Cursor contactLookup = contentResolver.query(uri, new String[]{"_id"}, null, null, null);
                boolean numberSaved = contactLookup != null && contactLookup.getCount() > 0 && contactLookup.moveToFirst();
                if (contactLookup != null) {
                    contactLookup.close();
                }
                contactLookup = contentResolver.query(uri, new String[]{"_id", "sec_led"}, "sec_led > 0", null, null);
                int secLed = -1;
                if (contactLookup == null || contactLookup.getCount() <= 0) {
                    Log.d(DreamyNfcLedCoverController.TAG, "contact not found");
                } else if (contactLookup.moveToFirst()) {
                    String contactId = contactLookup.getString(0);
                    secLed = contactLookup.getInt(1);
                    Log.d(DreamyNfcLedCoverController.TAG, "getCallerData : contactId=" + String.valueOf(contactId) + " secLed=" + String.valueOf(secLed));
                } else {
                    Log.e(DreamyNfcLedCoverController.TAG, "getCallerData : Cannot access first result");
                }
                if (contactLookup != null) {
                    contactLookup.close();
                }
                byte[] iconData = null;
                if (secLed > 0 && secLed < 55) {
                    iconData = null;
                } else if (secLed >= 55) {
                    iconData = getCustomCallerData(currentUserContext, secLed);
                } else if (null == null) {
                    String formattedNumber = CoverUtils.formatNumber(number, DreamyNfcLedCoverController.this.mTelephonyManager, DreamyNfcLedCoverController.this.mCountryDetector);
                    if (TextUtils.isEmpty(formattedNumber)) {
                        Log.e(DreamyNfcLedCoverController.TAG, "Could not format given phone number, return default null one");
                        return null;
                    }
                    number = formattedNumber;
                }
                return new CallerData(secLed, number, iconData, numberSaved, false);
            }
        }

        private byte[] getCustomCallerData(Context currentUserContext, int icon_id) {
            String customData = null;
            String ledCoverAppContentUri = "content://com.samsung.android.app.ledcover.cp";
            try {
                Cursor c = currentUserContext.getContentResolver().query(Uri.parse("content://com.samsung.android.app.ledcover.cp"), new String[]{"icon_id", "icon_array"}, "icon_id=" + icon_id, null, null);
                if (c != null) {
                    int contactCount = c.getCount();
                    SemLog.d(DreamyNfcLedCoverController.TAG, "checkCallerIDCount() Count : " + contactCount);
                    if (contactCount > 0) {
                        int dataColumnIndex = c.getColumnIndex("icon_array");
                        if (dataColumnIndex != -1 && c.moveToLast()) {
                            customData = c.getString(dataColumnIndex);
                        }
                    }
                    c.close();
                }
                return DreamUtils.getPayload(customData);
            } catch (Exception e) {
                Log.e(DreamyNfcLedCoverController.TAG, "Error retrieving custom icon data", e);
                return null;
            }
        }

        private void processHeadsetPlug(Intent intent) {
            if (intent == null) {
                Log.e(DreamyNfcLedCoverController.TAG, "processHeadsetPlug intent=null");
                return;
            }
            int state = intent.getIntExtra("state", -1);
            Log.d(DreamyNfcLedCoverController.TAG, "processHeadsetPlug state=" + String.valueOf(state));
            if (state == 0 || state == 1) {
                Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(13);
                msg.arg1 = state;
                DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }

        private void processCallTime(Intent intent) {
            if (intent == null) {
                Log.e(DreamyNfcLedCoverController.TAG, "processCallTime intent=null");
                return;
            }
            long connectedTime = intent.getLongExtra("connectedTime", -1);
            Log.d(DreamyNfcLedCoverController.TAG, "processCallTime connectedTime=" + String.valueOf(connectedTime));
            if (connectedTime == -1) {
                String strCallTime = intent.getStringExtra("calldurationmillis");
                Log.d(DreamyNfcLedCoverController.TAG, "processCallTime strCallTime=" + String.valueOf(strCallTime));
                if (!TextUtils.isEmpty(strCallTime) && strCallTime.length() >= 5) {
                    Message msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(12);
                    msg.obj = strCallTime;
                    DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
                    return;
                }
                return;
            }
            long baseTime = intent.getLongExtra("calldurationmillis", -1);
            Log.d(DreamyNfcLedCoverController.TAG, "processCallTime baseTime=" + String.valueOf(baseTime));
            if (baseTime != -1) {
                msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(11);
                msg.obj = Long.valueOf(baseTime);
                DreamyNfcLedCoverController.this.mHandler.sendMessage(msg);
            }
        }
    }

    class C00067 implements LedOffCallback {
        C00067() {
        }

        public void onLedOff() {
            DreamyNfcLedCoverController.this.mLedStateMachine.processTimeout();
        }
    }

    private class CoverManagerSystemEventListener extends LedSystemEventListener {
        private static final int SYSTEM_EVENT_AUTH_RESPONSE = 6;
        private static final int SYSTEM_EVENT_FOTA_IN_PROGRESS_REQUEST = 7;
        private static final int SYSTEM_EVENT_FOTA_IN_PROGRESS_RESPONSE = 8;
        private static final int SYSTEM_EVENT_LCD_OFF_DISABLED_BY_COVER = 4;
        private static final int SYSTEM_EVENT_LED_OFF = 0;
        private static final int SYSTEM_EVENT_NOTIFICATION_ADD = 2;
        private static final int SYSTEM_EVENT_NOTIFICATION_REMOVE = 3;
        private static final int SYSTEM_EVENT_POWER_BUTTON = 1;
        private static final int SYSTEM_EVENT_SEND_COMMAND = 5;

        private CoverManagerSystemEventListener() {
        }

        public void onSystemCoverEvent(int event, Bundle args) {
            int i = 1;
            Log.d(DreamyNfcLedCoverController.TAG, "onSystemCoverEvent event=" + String.valueOf(event) + " args=" + args);
            Message msg;
            switch (event) {
                case 0:
                    return;
                case 1:
                    DreamyNfcLedCoverController.this.mHandler.obtainMessage(16).sendToTarget();
                    return;
                case 2:
                    if (args == null) {
                        Log.e(DreamyNfcLedCoverController.TAG, "Null add notification data");
                        return;
                    }
                    msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(18);
                    msg.obj = getLedNotificationsFromSystem(args);
                    if (!args.getBoolean("clear_notifications", false)) {
                        i = 0;
                    }
                    msg.arg1 = i;
                    msg.sendToTarget();
                    return;
                case 3:
                    if (args == null) {
                        Log.e(DreamyNfcLedCoverController.TAG, "Null remove notification data");
                        return;
                    }
                    msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(19);
                    msg.obj = getLedNotificationsFromSystem(args);
                    if (!args.getBoolean("clear_notifications", false)) {
                        i = 0;
                    }
                    msg.arg1 = i;
                    msg.sendToTarget();
                    return;
                case 4:
                    if (args == null) {
                        Log.e(DreamyNfcLedCoverController.TAG, "Null Lcd Off disabled by Cover data");
                        return;
                    }
                    msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(DreamyNfcLedCoverController.MSG_LCD_OFF_DISABLED_BY_COVER);
                    msg.obj = args;
                    msg.sendToTarget();
                    return;
                case 5:
                    if (args == null) {
                        Log.e(DreamyNfcLedCoverController.TAG, "Null send command data");
                        return;
                    }
                    msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(101);
                    msg.obj = args;
                    msg.sendToTarget();
                    return;
                case 7:
                    if (args == null) {
                        Log.e(DreamyNfcLedCoverController.TAG, "Null fota in progress request data");
                        return;
                    } else if (args.containsKey(Constants.SYSTEM_EVENT_KEY_FOTA_IN_PROGRESS)) {
                        msg = DreamyNfcLedCoverController.this.mHandler.obtainMessage(args.getBoolean(Constants.SYSTEM_EVENT_KEY_FOTA_IN_PROGRESS) ? DreamyNfcLedCoverController.MSG_SYSTEM_FOTA_IN_PROGRESS_START : DreamyNfcLedCoverController.MSG_SYSTEM_FOTA_IN_PROGRESS_STOP);
                        msg.arg1 = 1;
                        msg.sendToTarget();
                        return;
                    } else {
                        Log.e(DreamyNfcLedCoverController.TAG, "Fota in progress does not contain valid progress state");
                        return;
                    }
                default:
                    Log.e(DreamyNfcLedCoverController.TAG, "onSystemCoverEvent unknown event");
                    return;
            }
        }

        private MissedEvent[] getLedNotificationsFromSystem(Bundle notificationData) {
            MissedEvent[] notifications = null;
            try {
                notifications = MissedEvent.getCustomNotificationEvents(notificationData);
            } catch (IllegalArgumentException e) {
                Log.e(DreamyNfcLedCoverController.TAG, "Error creating notifications: " + e.getMessage());
            }
            return notifications;
        }
    }

    public DreamyNfcLedCoverController(Context context) {
        boolean z;
        super(context);
        if (RefFactoryTest.get().isFactoryBinary() || RefFactoryTest.get().isRunningFactoryApp()) {
            z = true;
        } else {
            z = false;
        }
        this.mIsFactoryTest = z;
        this.mCoverManager = new ScoverManager(context);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mCountryDetector = this.mContext.getSystemService(RefContext.get().COUNTRY_DETECTOR);
        this.mSystemEventListener = new CoverManagerSystemEventListener();
        this.mHandler = new Handler(this);
        this.mLedStateMachine = new LedStateMachine();
        this.mLedStateMachine.setListener(this);
        this.mLedPowerStateController = new LedPowerOnOffStateController(this.mContext, this.mLedStateMachine, this);
        if (this.mIsFactoryTest) {
            this.mLedPowerStateController.onFactoryTestStart();
        }
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
        boolean z;
        boolean isUPSMEnabled;
        Log.d(TAG, "onCoverAttached state=" + String.valueOf(state));
        if (Secure.getInt(this.mContext.getContentResolver(), "nfc_led_cover_test", 0) == 42) {
            z = true;
        } else {
            z = false;
        }
        this.mCoverEventsDisabledForDemoMode = z;
        if (this.mCoverEventsDisabledForDemoMode) {
            Log.d(TAG, "onCoverAttached: Demo mode ENABLED");
            this.mLedPowerStateController.onFactoryTestStart();
        }
        if (System.getInt(this.mContext.getContentResolver(), "emergency_mode", 0) == 1) {
            isUPSMEnabled = true;
        } else {
            isUPSMEnabled = false;
        }
        this.mLedStateMachine.getLedContext().setUPSMEnabled(isUPSMEnabled);
        super.onCoverAttached(state);
        this.mLedPowerStateController.onCoverAttached();
        if (getCoverState().getFotaMode() != 0) {
            Log.d(TAG, "Cover in FOTA Mode attached. Events not displayed");
            Message msg = this.mHandler.obtainMessage(MSG_SYSTEM_FOTA_IN_PROGRESS_START);
            msg.arg1 = 0;
            msg.sendToTarget();
        }
        this.mLedStateMachine.processCallStateChange(this.mTelephonyManager.getCallState(), null, this.mTelephonyManager.semIsVideoCall());
        CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().registerCallback(this.mUpdateMonitorCallback);
        IntentFilter filter = new IntentFilter();
        for (String addAction : strIntentAction) {
            filter.addAction(addAction);
        }
        this.mContext.semRegisterReceiverAsUser(this.mBroadcastReceiver, UserHandle.SEM_ALL, filter, null, null);
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPlaybackStateMonitor().setOnPlaybackStateChangedListener(this.mPlaybackStateChangedListener);
        CoverExecutiveObservator.getInstance(this.mContext).getCallDisconnectReasonManager().setListener(this.mCallDisconnectReasonListener);
        requestRemoteViews();
        handleDoNotDisturbChanged();
        try {
            this.mCoverManager.registerLedSystemListener(this.mSystemEventListener);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "Error registering listener", e);
        }
        registerDemoModeSettingObserver();
        registerUPSMModeSettingObserver();
        startLedIconService();
        notifyCoverReady();
    }

    public void onCoverDetatched(ScoverState state) {
        Log.d(TAG, "onCoverDetatched state=" + String.valueOf(state));
        unRegisterDemoModeSettingObserver();
        unRegisterUPSMModeSettingObserver();
        if (this.mCoverEventsDisabledForDemoMode) {
            Log.d(TAG, "onCoverDetatched: Demo mode ENABLED");
            this.mLedPowerStateController.onFactoryTestSop();
        }
        this.mLedPowerStateController.onCoverDetached();
        this.mLedStateMachine.reset();
        CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPlaybackStateMonitor().setOnPlaybackStateChangedListener(null);
        CoverExecutiveObservator.getInstance(this.mContext).getCallDisconnectReasonManager().setListener(null);
        try {
            this.mCoverManager.unregisterLedSystemEventListener(this.mSystemEventListener);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "Error unregistering listener", e);
        }
        cancelLedLampNoti();
        this.mHandler.removeCallbacksAndMessages(null);
        CoverUtils.releaseWakeLockSafely(this.mTimeoutWakeLock);
        super.onCoverDetatched(state);
    }

    public void onCoverEvent(ScoverState state) {
        Log.d(TAG, "onCoverEvent state=" + String.valueOf(state));
        super.onCoverEvent(state);
        if (this.mCoverEventsDisabledForDemoMode || this.mCoverEventsDisabledForSamsungPay || this.mIsFactoryTest) {
            Log.d(TAG, "onCoverEvent: Factory/Demo mode or SamsungPay active - ignore cover events");
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
            this.mHandler.removeMessages(MSG_EVENT_TIME_UPDATE);
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

    private void registerDemoModeSettingObserver() {
        Log.d(TAG, "registerDemoModeSettingObserver");
        this.mDemoModeSettingObserver = new ContentObserver(new Handler()) {
            public void onChange(boolean selfChange) {
                boolean z = false;
                super.onChange(selfChange);
                DreamyNfcLedCoverController dreamyNfcLedCoverController = DreamyNfcLedCoverController.this;
                if (Secure.getInt(DreamyNfcLedCoverController.this.mContext.getContentResolver(), "nfc_led_cover_test", 0) == 42) {
                    z = true;
                }
                dreamyNfcLedCoverController.mCoverEventsDisabledForDemoMode = z;
                Log.d(DreamyNfcLedCoverController.TAG, "onChange mCoverEventsDisabledForDemoMode: " + DreamyNfcLedCoverController.this.mCoverEventsDisabledForDemoMode);
                if (DreamyNfcLedCoverController.this.mCoverEventsDisabledForDemoMode) {
                    DreamyNfcLedCoverController.this.mLedPowerStateController.onFactoryTestStart();
                } else {
                    DreamyNfcLedCoverController.this.mLedPowerStateController.onFactoryTestSop();
                }
            }
        };
        RefContentResolver.get().registerContentObserver(this.mContext.getContentResolver(), Secure.getUriFor("nfc_led_cover_test"), true, this.mDemoModeSettingObserver, UserHandle.SEM_ALL.semGetIdentifier());
    }

    private void unRegisterDemoModeSettingObserver() {
        Log.d(TAG, "unRegisterDemoModeSettingObserver");
        this.mContext.getContentResolver().unregisterContentObserver(this.mDemoModeSettingObserver);
    }

    private void registerUPSMModeSettingObserver() {
        Log.d(TAG, "registerUPSMModeSettingObserver");
        this.mUPSMModeSettingObserver = new ContentObserver(new Handler()) {
            public void onChange(boolean selfChange) {
                boolean isUPSMEnabled = true;
                super.onChange(selfChange);
                if (System.getInt(DreamyNfcLedCoverController.this.mContext.getContentResolver(), "emergency_mode", 0) != 1) {
                    isUPSMEnabled = false;
                }
                Log.d(DreamyNfcLedCoverController.TAG, "onChange isUPSMEnabled: " + isUPSMEnabled);
                DreamyNfcLedCoverController.this.mLedStateMachine.getLedContext().setUPSMEnabled(isUPSMEnabled);
            }
        };
        RefContentResolver.get().registerContentObserver(this.mContext.getContentResolver(), System.getUriFor("emergency_mode"), false, this.mUPSMModeSettingObserver, UserHandle.SEM_ALL.semGetIdentifier());
    }

    private void unRegisterUPSMModeSettingObserver() {
        Log.d(TAG, "unRegisterUPSMModeSettingObserver");
        this.mContext.getContentResolver().unregisterContentObserver(this.mUPSMModeSettingObserver);
    }

    public boolean isCoverOpen() {
        if (this.mCoverEventsDisabledForSamsungPay || this.mCoverEventsDisabledForDemoMode || this.mIsFactoryTest || this.mFotaInProgress) {
            return true;
        }
        return super.isCoverOpen();
    }

    public void onStateChange(LedState ledState, LedContext ledContext, boolean shouldTurnLedOn) {
        Log.d(TAG, "onStateChange ledState=" + String.valueOf(ledState) + " ledContext=" + String.valueOf(ledContext) + " shouldTurnLedOn=" + String.valueOf(shouldTurnLedOn));
        if (ledState != null && ledContext != null) {
            if (this.mLedPowerStateController.isLedTurnedOnOrRestarting() && !ledState.isContinuation()) {
                cancelTimeout();
            }
            this.mLedPowerStateController.onStateChange(ledState, ledContext, shouldTurnLedOn);
            if (ledState == LedState.IDLE && !isCoverOpen() && !ledContext.isUPSMEnabled()) {
                cancelLedLampNoti();
                initLedLampNoti();
            }
        }
    }

    private void initLedLampNoti() {
        boolean hasMissedEvent = this.mLedStateMachine.getLedContext().hasState(LedState.MISSED_EVENT);
        boolean shouldWakeForLedLamp = this.mLedStateMachine.getLedContext().shouldWakeupForLedLamp();
        boolean doNotDisturb = this.mLedStateMachine.getLedContext().isDoNotDisturbNotiOffPolicy();
        Log.d(TAG, "initLedLampNoti: hasMissedEvent=" + hasMissedEvent + " hasCustomNoti=" + " shouldWakeForLedLamp=" + shouldWakeForLedLamp + " doNotDisturb=" + doNotDisturb);
        if (hasMissedEvent && shouldWakeForLedLamp && doNotDisturb) {
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

    public void onStateSent(LedStateController state) {
        Log.d(TAG, "onStateSent state=" + String.valueOf(state));
        this.mLedStateMachine.getLedContext().setIsInCallTouchReject(false);
        if (state == null) {
            cancelTimeout();
            scheduleTimeout(0);
            return;
        }
        if (state == LedState.DURING_CALL || state == LedState.DURING_CALL_TIME_UPDATE) {
            if (state != LedState.DURING_CALL_TIME_UPDATE || this.mHandler.hasMessages(0)) {
                long baseTime = this.mLedStateMachine.getLedContext().getCallStartTime();
                this.mHandler.removeMessages(MSG_EVENT_TIME_UPDATE);
                long delay = 1000 - ((SystemClock.elapsedRealtime() - baseTime) % 1000);
                Log.d(TAG, "onStateSent delay=" + String.valueOf(delay) + " SystemClock.elapsedRealtime() - baseTime " + (SystemClock.elapsedRealtime() - baseTime));
                this.mHandler.sendEmptyMessageDelayed(MSG_EVENT_TIME_UPDATE, delay);
            } else {
                return;
            }
        }
        if (!state.isContinuation() || !this.mHandler.hasMessages(0)) {
            cancelTimeout();
            if (!state.isInfinite()) {
                long timeout = state.getTimeout();
                Log.d(TAG, "onStateSent timeout=" + String.valueOf(timeout));
                scheduleTimeout(timeout);
            }
        }
    }

    public void onTouchEvent(boolean reject) {
        Log.d(TAG, "onTouchEvent reject=" + reject);
        this.mLedStateMachine.getLedContext().setIsInCallTouchReject(reject);
    }

    public void onMediaTouchEvent(int keycode) {
        Log.d(TAG, "onMediaEvent: " + keycode);
        Message msg = this.mHandler.obtainMessage(24);
        msg.arg1 = keycode;
        msg.sendToTarget();
    }

    public boolean handleMessage(Message msg) {
        Log.d(TAG, "handleMessage msg=" + String.valueOf(msg));
        switch (msg.what) {
            case 0:
                if (this.mHandler.hasMessages(MSG_EVENT_TIME_UPDATE)) {
                    this.mHandler.removeMessages(MSG_EVENT_TIME_UPDATE);
                }
                this.mLedPowerStateController.onLedTimeOut(new C00067());
                if (this.mHandler.hasMessages(0)) {
                    Log.w(TAG, "Sanity check failed MSG_EVENT_TIMEOUT still exist NO release wakelock");
                } else {
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
                this.mHandler.removeMessages(MSG_EVENT_TIME_UPDATE);
                if (isCoverOpen()) {
                    Log.d(TAG, "handleMessage MSG_EVENT_POWER_BUTTON: Cover is open, ignore power button events");
                    return true;
                }
                cancelTimeout();
                Log.i(TAG, "Power button was pressed, isLedTurnedOnOrRestarting: " + this.mLedPowerStateController.isLedTurnedOnOrRestarting());
                if (this.mLedPowerStateController.isLedTurnedOnOrRestarting()) {
                    this.mLedStateMachine.processPowerButtonWakeUp(false);
                    this.mLedPowerStateController.onPowerKeyToCover(false);
                } else {
                    if (!this.mLedStateMachine.processPowerButtonWakeUp(true)) {
                        this.mLedPowerStateController.onPowerKeyToCover(true);
                    }
                    cancelLedLampNoti();
                }
                return true;
            case 18:
                MissedEvent[] addedNotis = (MissedEvent[]) msg.obj;
                if (msg.arg1 == 1) {
                    this.mLedStateMachine.processAllCustomNotificationsRemoved();
                }
                if (addedNotis != null && addedNotis.length > 0) {
                    this.mLedStateMachine.processCustomNotificationsAdded(addedNotis);
                }
                return true;
            case 19:
                MissedEvent[] removedNotis = (MissedEvent[]) msg.obj;
                if (msg.arg1 == 1) {
                    this.mLedStateMachine.processAllCustomNotificationsRemoved();
                }
                if (removedNotis != null && removedNotis.length > 0) {
                    this.mLedStateMachine.processCustomNotificationsRemoved(removedNotis);
                }
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
                this.mLedStateMachine.processMediaKeyPress(msg.arg1);
                sendMediaButtonKeyEvent(msg.arg1);
                return true;
            case 25:
                this.mLedStateMachine.processCallDisconnectReason(msg.arg1, (String) msg.obj, msg.arg2);
                return true;
            case 26:
                this.mLedStateMachine.processAllCustomNotificationsRemoved();
                startLedIconService();
                return true;
            case 27:
                this.mLedStateMachine.processBixbyStateChanged(msg.arg1);
                return true;
            case 28:
                handleDoNotDisturbChanged();
                return true;
            case MSG_LCD_OFF_DISABLED_BY_COVER /*100*/:
                handleLcdOffDisabledByCover(msg.obj.getBoolean("lcd_off_disabled_by_cover"));
                return true;
            case 101:
                Bundle cmdData = msg.obj;
                if (cmdData.containsKey(Constants.SYSTEM_EVENT_KEY_SEND_COMMAND_ID) && cmdData.containsKey(Constants.SYSTEM_EVENT_KEY_SEND_COMMAND_CONTENT)) {
                    handleSendCommand(cmdData.getInt(Constants.SYSTEM_EVENT_KEY_SEND_COMMAND_ID), cmdData.getByteArray(Constants.SYSTEM_EVENT_KEY_SEND_COMMAND_CONTENT));
                    return true;
                }
                Log.e(TAG, "Command sent from CoverManager must contain both ID & content");
                return true;
            case MSG_EVENT_TIME_UPDATE /*102*/:
                if (this.mLedPowerStateController.isLedTurnedOnOrRestarting()) {
                    this.mLedStateMachine.processTimeTick();
                }
                return true;
            case MSG_SYSTEM_FOTA_IN_PROGRESS_START /*103*/:
                handleFotaInProgressStart(msg.arg1 == 1);
                return true;
            case MSG_SYSTEM_FOTA_IN_PROGRESS_STOP /*104*/:
                handleFotaInProgressStop(msg.arg1 == 1);
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

    private void handleSendCommand(int command, byte[] content) {
        if (this.mIsFactoryTest || this.mCoverEventsDisabledForDemoMode) {
            Log.d(TAG, "handleSendCommand: command=" + String.valueOf(command) + ", content=" + Arrays.toString(content));
            if (command == 18) {
                this.mLedPowerStateController.onFactoryTestLedOff();
                return;
            } else {
                this.mLedPowerStateController.onFactoryTestCase(new MockeryLedStateController(Integer.valueOf(command).byteValue(), content));
                return;
            }
        }
        Log.e(TAG, "Factory test not enabled, ignore command");
    }

    private void scheduleTimeout(long timeout) {
        CoverUtils.acquireWakeLockSafely(this.mTimeoutWakeLock);
        this.mHandler.sendEmptyMessageDelayed(0, timeout);
    }

    private void cancelTimeout() {
        this.mHandler.removeMessages(0);
        CoverUtils.releaseWakeLockSafely(this.mTimeoutWakeLock);
    }

    private void sendMediaButtonKeyEvent(int keyevent) {
        long currTime = SystemClock.uptimeMillis();
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPlaybackStateMonitor().sendMediaButtonEvent(new KeyEvent(currTime, currTime, 0, keyevent, 0));
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPlaybackStateMonitor().sendMediaButtonEvent(new KeyEvent(currTime, 1 + currTime, 1, keyevent, 0));
    }

    private void startLedIconService() {
        Intent iService = new Intent();
        iService.setComponent(new ComponentName("com.samsung.android.app.ledcoverdream", "com.samsung.android.app.ledcover.service.LCoverIcon"));
        try {
            Log.d(TAG, "Start LED Cover Icon Service");
            this.mContext.semStartServiceAsUser(iService, UserHandle.SEM_CURRENT);
        } catch (Exception e) {
            Log.e(TAG, "Error starting LED Icon service", e);
        }
    }

    private void notifyCoverReady() {
        Intent intent = new Intent();
        intent.setAction(ACTION_COVER_READY);
        this.mContext.sendBroadcast(intent, LED_NOTI_PERMISSION);
        Log.d(TAG, "Notified LED Cover ready");
    }

    private void handleFotaInProgressStart(final boolean notify) {
        if (!this.mFotaInProgress) {
            this.mFotaInProgress = true;
            onCoverEvent(getCoverState());
            this.mLedPowerStateController.onFotaStart(new LedOffCallback() {
                public void onLedOff() {
                    if (notify) {
                        DreamyNfcLedCoverController.this.notifyFotaInProgressApplied();
                    }
                }
            });
        } else if (notify) {
            notifyFotaInProgressApplied();
        }
    }

    private void handleFotaInProgressStop(boolean notify) {
        if (this.mFotaInProgress) {
            this.mLedPowerStateController.onFotaStop();
            this.mFotaInProgress = false;
            onCoverEvent(getCoverState());
            if (notify) {
                notifyFotaInProgressApplied();
            }
        } else if (notify) {
            notifyFotaInProgressApplied();
        }
    }

    private void notifyFotaInProgressApplied() {
        Bundle args = new Bundle();
        args.putInt(OutgoingSystemEvent.KEY_TYPE, 8);
        args.putBoolean(Constants.SYSTEM_EVENT_KEY_FOTA_IN_PROGRESS, this.mFotaInProgress);
        try {
            this.mCoverManager.sendSystemEvent(args);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "Error sending system event", e);
        }
    }

    private void handleDoNotDisturbChanged() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        int doNotDisturbMode = notificationManager.getCurrentInterruptionFilter();
        Log.i(TAG, "handleDoNotDisturbChanged current mode is=" + doNotDisturbMode);
        int suppressPolicy = notificationManager.getNotificationPolicy().suppressedVisualEffects;
        if ((doNotDisturbMode == 3 || doNotDisturbMode == 4 || doNotDisturbMode == 2) && (suppressPolicy & 1) != 0) {
            Log.i(TAG, "handleDoNotDisturbChanged supress OFF policy enabled");
            this.mLedStateMachine.getLedContext().setDoNotDisturbNotiOffPolicy(true);
            return;
        }
        Log.i(TAG, "handleDoNotDisturbChanged supress OFF policy disabled");
        this.mLedStateMachine.getLedContext().setDoNotDisturbNotiOffPolicy(false);
    }
}
