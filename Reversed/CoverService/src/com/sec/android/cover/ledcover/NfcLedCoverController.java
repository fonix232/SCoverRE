package com.sec.android.cover.ledcover;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.LedSystemEventListener;
import com.samsung.android.sdk.cover.ScoverManager.NfcLedCoverTouchListener;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.cover.BaseCoverController;
import com.sec.android.cover.Constants;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.CoverUtils;
import com.sec.android.cover.ledcover.reflection.content.RefContext;
import com.sec.android.cover.ledcover.reflection.os.RefUserHandle;
import com.sec.android.cover.manager.MissedEventManager.MissedEventListener;
import com.sec.android.cover.monitor.CoverPlaybackStateMonitor.OnPlaybackStateChangedListener;
import com.sec.android.cover.monitor.CoverUpdateMonitor.BatteryStatus;
import com.sec.android.cover.monitor.CoverUpdateMonitor.RemoteViewInfo;
import com.sec.android.cover.monitor.CoverUpdateMonitorCallback;
import java.nio.ByteBuffer;

public class NfcLedCoverController extends BaseCoverController {
    private static final int CMD_LED_ALARM = 6;
    private static final int CMD_LED_BATTERY_CHARGING = 12;
    private static final int CMD_LED_BATTERY_CHARGING_WIRELESS = 19;
    private static final int CMD_LED_BATTERY_LOW = 11;
    private static final int CMD_LED_CALL_END = 4;
    private static final int CMD_LED_CALL_INCOMING = 2;
    private static final int CMD_LED_CALL_INPROGRESS = 65534;
    private static final int CMD_LED_CLEAR_ONGOING_EVENT = 65520;
    private static final int CMD_LED_CLOCK = 15;
    private static final int CMD_LED_CLOCK_TIME_TICK = 65535;
    private static final int CMD_LED_INDICATOR = 16;
    private static final int CMD_LED_MISSED_EVENT = 9;
    private static final int CMD_LED_MUSIC = 13;
    private static final int CMD_LED_NEW_MESSAGE = 5;
    private static final int CMD_LED_OFF = 18;
    private static final int CMD_LED_POWER_ON = 1;
    private static final int CMD_LED_VIDEO_CALL_INCOMING = 20;
    private static final int CMD_LED_VOICE_RECORDER = 14;
    private static final int CMD_LED_VOLUME = 10;
    private static final String INTENT_ACTION_ALARM_ALERT = "com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT";
    private static final String INTENT_ACTION_ALARM_START_ALERT = "com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT";
    private static final String INTENT_ACTION_ALARM_STOP_ALERT = "com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT";
    private static final String INTENT_ACTION_CALL_TIME = "com.sec.android.phone.action.ACTION_CALL_TIME";
    private static final String INTENT_ACTION_PHONESTATE = "android.intent.action.PHONE_STATE";
    private static final String INTENT_ACTION_TIMER_START_ALERT = "com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT";
    private static final String INTENT_ACTION_TIMER_STOP_ALERT = "com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT";
    public static final String LCD_COVER_SYSTEM_EVENT_KEY_TYPE_DISABLE_LCD_OFF_BY_COVER = "lcd_off_disabled_by_cover";
    private static final int LED_CALL_EVENT_NONE = -1;
    private static final int LED_DEFAULT_PRIORITY = -1;
    private static final int LED_MISSED_CALL = 65533;
    private static final String TAG = ("LedService." + NfcLedCoverController.class.getSimpleName());
    private static final int VOLUME_BLOCK_DELAY = 300;
    private boolean isBatteryCharged = false;
    private boolean isBatteryCharging = false;
    private boolean isBatteryChargingWireless = false;
    private boolean isCallInProgress = false;
    private boolean isCallPriorityStateDisplayed = false;
    private boolean isIncomingCallStatus = false;
    private boolean isScreenOn = false;
    private long mAlarmStartTime = 0;
    private BroadcastReceiver mBroadcastReceiver = new C00254();
    private byte[] mCallDurationData;
    private byte[] mCallStartMillisData;
    private CallTouchListener mCallTouchListener;
    private boolean mCoverEventsDisabledForSamsungPay;
    private ScoverManager mCoverManager = null;
    private boolean mHeadsetPlugged = false;
    private boolean mIsAlarmActive;
    private boolean mIsTimerActive;
    private Intent mLastInCallIntent = null;
    private int mMissedCallsCount;
    private MissedEventListener mMissedEventListener = new C00243();
    private int mMissedMessagesCount;
    private MusicPlayStatus mMusicStatus = MusicPlayStatus.STOPPED;
    private NfcAdapter mNfcAdapter;
    private OnPlaybackStateChangedListener mPlaybackStateChangedListener = new C00232();
    private boolean mPrevCoverStateWasOpen;
    private int mPrevInCallEvent = -1;
    private CoverManagerSystemEventListener mSystemEventListener;
    private TelephonyManager mTelephonyManager;
    private long mTimerStartTime = 0;
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00221();
    private VoiceRecorderStatus mVoiceRecorderStatus = VoiceRecorderStatus.STOPPED;
    private long mVolumeBlockStartTime;
    private int prevCallSatus = -1;
    private String[] strIntentAction = new String[]{INTENT_ACTION_PHONESTATE, "com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT", "com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT", "com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT", "com.sec.android.phone.action.ACTION_CALL_TIME", "android.intent.action.HEADSET_PLUG", "com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT", "com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT", "android.intent.action.TIME_TICK"};

    class C00221 extends CoverUpdateMonitorCallback {
        C00221() {
        }

        public void onRemoteViewUpdated(RemoteViewInfo remoteViewInfo) {
            NfcLedCoverController.this.updateRemoteView(remoteViewInfo);
        }

        public void onBatteryLow() {
            NfcLedCoverController.this.sendCommandByPriority(11);
        }

        public void onBatteryCritical() {
            NfcLedCoverController.this.sendCommandByPriority(11);
        }

        public void onScreenTurnedOn() {
            Log.d(NfcLedCoverController.TAG, "onScreenTurnedOn");
            NfcLedCoverController.this.isScreenOn = true;
            if (!NfcLedCoverController.this.isCoverOpen()) {
                NfcLedCoverController.this.coverEventFinished();
            }
        }

        public void onScreenTurnedOff() {
            Log.d(NfcLedCoverController.TAG, "onScreenTurnedOff");
            NfcLedCoverController.this.isScreenOn = false;
        }

        public void onVolumeChanged(int streamType, int val) {
            Log.d(NfcLedCoverController.TAG, "onVolumeChanged streamType=" + streamType + " val=" + val);
            if (streamType == 4) {
                Log.d(NfcLedCoverController.TAG, "onVolumeChanged : blocking next volume changes");
                NfcLedCoverController.this.mVolumeBlockStartTime = System.currentTimeMillis();
            }
            NfcLedCoverController.this.sendCommandByPriority(10);
        }

        public void onRefreshBatteryInfo(BatteryStatus status) {
            Log.d(NfcLedCoverController.TAG, "onRefreshBatteryInfo status=" + String.valueOf(status));
            if (status != null) {
                boolean wirelessCharging = status.isWirelssCharged();
                boolean isPlugged = status.isPluggedIn();
                boolean isFullyCharged = status.isCharged();
                Log.d(NfcLedCoverController.TAG, "onRefreshBatteryInfo isBatteryCharging=" + String.valueOf(NfcLedCoverController.this.isBatteryCharging) + " status.isPluggedIn=" + String.valueOf(isPlugged) + " isBatteryChargingWireless=" + String.valueOf(NfcLedCoverController.this.isBatteryChargingWireless) + " status.isWirelssCharged=" + String.valueOf(wirelessCharging) + " isBatteryCharged=" + String.valueOf(NfcLedCoverController.this.isBatteryCharged) + " status.isFullyCharged=" + String.valueOf(isFullyCharged));
                if (NfcLedCoverController.this.isBatteryCharging != isPlugged) {
                    if (!(wirelessCharging || NfcLedCoverController.this.isBatteryChargingWireless)) {
                        NfcLedCoverController.this.sendCommandByPriority(12);
                    }
                    NfcLedCoverController.this.isBatteryCharging = isPlugged;
                } else if (NfcLedCoverController.this.isBatteryCharged != isFullyCharged) {
                    NfcLedCoverController.this.isBatteryCharged = isFullyCharged;
                    if (isFullyCharged) {
                        NfcLedCoverController.this.sendCommandByPriority(12);
                    }
                }
                NfcLedCoverController.this.isBatteryChargingWireless = wirelessCharging;
            }
        }
    }

    class C00232 implements OnPlaybackStateChangedListener {
        C00232() {
        }

        public void onPlaybackStateChanged(PlaybackState playbackState) {
            Log.d(NfcLedCoverController.TAG, "onPlaybackStateChanged state=" + String.valueOf(playbackState));
            MusicPlayStatus musicStatus = MusicPlayStatus.STOPPED;
            if (playbackState != null) {
                switch (playbackState.getState()) {
                    case 0:
                    case 1:
                        musicStatus = MusicPlayStatus.STOPPED;
                        break;
                    case 2:
                        musicStatus = MusicPlayStatus.PAUSED;
                        break;
                    case 3:
                        musicStatus = MusicPlayStatus.PLAYING;
                        break;
                }
            }
            if (NfcLedCoverController.this.mMusicStatus != musicStatus) {
                NfcLedCoverController.this.mMusicStatus = musicStatus;
                NfcLedCoverController.this.sendCommandByPriority(13);
                return;
            }
            Log.d(NfcLedCoverController.TAG, "No music state change");
        }

        public void onMetadataChanged(MediaMetadata metadata) {
        }

        public void onSessionDestroyed() {
            if (NfcLedCoverController.this.mMusicStatus != MusicPlayStatus.STOPPED) {
                NfcLedCoverController.this.mMusicStatus = MusicPlayStatus.STOPPED;
                NfcLedCoverController.this.sendCommandByPriority(13);
            }
        }
    }

    class C00243 implements MissedEventListener {
        C00243() {
        }

        public void onUnreadMessagesCountChanged(int oldCount, int newCount, boolean muted, int userId) {
            Log.d(NfcLedCoverController.TAG, "onUnreadMessagesCountChanged : oldCount=" + String.valueOf(oldCount) + " newCount=" + String.valueOf(newCount) + " muted=" + String.valueOf(muted) + " userId=" + String.valueOf(userId));
            if (NfcLedCoverController.this.getCurrentUserId() == userId) {
                NfcLedCoverController.this.mMissedMessagesCount = newCount;
            }
            if (muted) {
                Log.d(NfcLedCoverController.TAG, "onUnreadMessagesCountChanged : ignore");
            } else if (newCount > oldCount) {
                NfcLedCoverController.this.sendCommandByPriority(5);
            }
        }

        public void onMissedCallsCountChanged(int oldCount, int newCount, boolean muted, int userId) {
            Log.d(NfcLedCoverController.TAG, "onMissedCallsCountChanged : oldCount=" + String.valueOf(oldCount) + " newCount=" + String.valueOf(newCount) + " muted=" + String.valueOf(muted) + " userId=" + String.valueOf(userId));
            if (NfcLedCoverController.this.getCurrentUserId() == userId) {
                NfcLedCoverController.this.mMissedCallsCount = newCount;
            }
            if (muted || NfcLedCoverController.this.getCurrentUserId() != userId) {
                Log.d(NfcLedCoverController.TAG, "onMissedCallsCountChanged : ignore");
            } else if (newCount > oldCount) {
                NfcLedCoverController.this.sendCommandByPriority(NfcLedCoverController.LED_MISSED_CALL);
            }
        }
    }

    class C00254 extends BroadcastReceiver {
        C00254() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(NfcLedCoverController.TAG, "BroadcastReceiver onReceive() : action : " + action);
            if ("android.intent.action.TIME_TICK".equals(action)) {
                Log.d(NfcLedCoverController.TAG, "android.intent.action.TIME_TICK, screen on: " + NfcLedCoverController.this.isScreenOn);
                if (!NfcLedCoverController.this.isCoverOpen()) {
                    NfcLedCoverController.this.sendLedData(NfcLedCoverController.CMD_LED_CLOCK_TIME_TICK, null);
                }
            } else if ("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT".equals(action) || "com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT".equals(action)) {
                if (!NfcLedCoverController.this.isCoverOpen()) {
                    NfcLedCoverController.this.mIsAlarmActive = true;
                    NfcLedCoverController.this.mAlarmStartTime = System.currentTimeMillis();
                    NfcLedCoverController.this.sendCommandByPriority(6);
                }
            } else if ("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT".equals(action)) {
                if (NfcLedCoverController.this.mIsAlarmActive) {
                    NfcLedCoverController.this.mIsAlarmActive = false;
                    NfcLedCoverController.this.mAlarmStartTime = System.currentTimeMillis();
                    NfcLedCoverController.this.sendCommandByPriority(6);
                }
            } else if ("com.sec.android.app.clockpackage.timer.TIMER_STARTED_IN_ALERT".equals(action)) {
                if (!NfcLedCoverController.this.isCoverOpen()) {
                    NfcLedCoverController.this.mIsTimerActive = true;
                    NfcLedCoverController.this.mTimerStartTime = System.currentTimeMillis();
                    NfcLedCoverController.this.sendCommandByPriority(6);
                }
            } else if ("com.sec.android.app.clockpackage.timer.TIMER_STOPPED_IN_ALERT".equals(action)) {
                if (NfcLedCoverController.this.mIsTimerActive) {
                    NfcLedCoverController.this.mIsTimerActive = false;
                    NfcLedCoverController.this.mTimerStartTime = System.currentTimeMillis();
                    NfcLedCoverController.this.sendCommandByPriority(6);
                }
            } else if (NfcLedCoverController.INTENT_ACTION_PHONESTATE.equals(action)) {
                NfcLedCoverController.this.processCallEvent(intent);
            } else if ("com.sec.android.phone.action.ACTION_CALL_TIME".equals(action)) {
                NfcLedCoverController.this.processCallTimeInformation(intent);
            } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {
                NfcLedCoverController.this.processHeadsetPlugInformation(intent);
            }
        }
    }

    private enum AlarmType {
        NONE,
        ALARM,
        TIMER
    }

    private class CallTouchListener extends NfcLedCoverTouchListener {
        private CallTouchListener() {
        }

        public void onCoverTouchReject() {
            NfcLedCoverController.this.isCallPriorityStateDisplayed = false;
        }
    }

    private class CoverManagerSystemEventListener extends LedSystemEventListener {
        private static final int SYSTEM_EVENT_LCD_OFF_DISABLED_BY_COVER = 4;
        private static final int SYSTEM_EVENT_LED_OFF = 0;
        private static final String SYSTEM_EVENT_LED_OFF_COMMAND = "led_off_command";
        private static final int SYSTEM_EVENT_POWER_BUTTON = 1;

        private CoverManagerSystemEventListener() {
        }

        public void onSystemCoverEvent(int event, Bundle args) {
            Log.d(NfcLedCoverController.TAG, "onSystemCoverEvent: " + event + " " + args);
            switch (event) {
                case 0:
                    if (!NfcLedCoverController.this.isCoverOpen()) {
                        if (args == null || !args.containsKey(SYSTEM_EVENT_LED_OFF_COMMAND)) {
                            Log.e(NfcLedCoverController.TAG, "Incorrect arguments for LED_OFF callback");
                            return;
                        } else {
                            NfcLedCoverController.this.handleLedOffEvent(args.getInt(SYSTEM_EVENT_LED_OFF_COMMAND));
                            return;
                        }
                    }
                    return;
                case 4:
                    NfcLedCoverController.this.handleLcdOffDisabledByCover(args.getBoolean("lcd_off_disabled_by_cover"));
                    return;
                default:
                    Log.e(NfcLedCoverController.TAG, "Unknown event: " + event);
                    return;
            }
        }
    }

    private enum MusicPlayStatus {
        STOPPED,
        PLAYING,
        PAUSED
    }

    private enum VoiceRecorderStatus {
        STOPPED,
        RECORDING,
        PLAYING
    }

    public NfcLedCoverController(Context context) {
        super(context);
        this.mCoverManager = new ScoverManager(context);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mPrevCoverStateWasOpen = false;
        this.mSystemEventListener = new CoverManagerSystemEventListener();
        this.mCallTouchListener = new CallTouchListener();
    }

    private void sendCommandByPriority(int cmd) {
        Log.d(TAG, "sendCommandByPriority: " + cmd);
        if (!isCoverOpen()) {
            if (this.isCallPriorityStateDisplayed) {
                Log.d(TAG, "Call displayed, ignore non-call commands");
                if (this.isIncomingCallStatus) {
                    if (cmd == -1) {
                        sendCallIncomingInformation(this.mLastInCallIntent);
                    } else if (shouldDisplayCmdAfterCall(cmd)) {
                        this.mPrevInCallEvent = cmd;
                    }
                } else if (this.isCallInProgress) {
                    if (cmd == 10) {
                        sendVolumeInformation();
                    } else if (cmd == -1) {
                        sendCallInProgress();
                    } else if (shouldDisplayCmdAfterCall(cmd)) {
                        this.mPrevInCallEvent = cmd;
                    }
                } else if (cmd == LED_MISSED_CALL) {
                    sendNewMissedCallInformation();
                } else if (shouldDisplayCmdAfterCall(cmd)) {
                    this.mPrevInCallEvent = cmd;
                }
            } else if (cmd == 5) {
                sendNewMessageInformation();
            } else if (cmd == LED_MISSED_CALL) {
                sendNewMissedCallInformation();
            } else if (cmd == 6 || (isAlarmOrTimerActive() && cmd == -1)) {
                if (cmd == -1 || cmd == 6) {
                    sendAlarmInformation();
                }
            } else if (cmd == 9 || (getMissedEventsCount() > 0 && cmd == -1)) {
                if (cmd == -1 || cmd == 9) {
                    sendMissedEventInformation();
                }
            } else if (cmd == 13 || (this.mMusicStatus == MusicPlayStatus.PLAYING && (cmd == -1 || cmd == 10))) {
                if (!isAlarmOrTimerActive()) {
                    if (cmd == 10) {
                        sendVolumeInformation();
                    } else if (cmd == -1 || cmd == 13) {
                        sendMusicPlayerInformation();
                    }
                }
            } else if (cmd == 14 || (this.mVoiceRecorderStatus != VoiceRecorderStatus.STOPPED && (cmd == -1 || cmd == 10))) {
                if (!isAlarmOrTimerActive()) {
                    if (cmd == 10 && this.mVoiceRecorderStatus == VoiceRecorderStatus.PLAYING) {
                        sendVolumeInformation();
                    } else if (cmd == -1 || cmd == 14) {
                        sendVoiceRecorderInformation();
                    }
                }
            } else if ((cmd == 12 || (this.isBatteryCharging && cmd == -1)) && !this.isBatteryChargingWireless) {
                sendBatteryChargingInformation();
            } else if (cmd == 11) {
                sendLowBatteryInformation();
            } else if (cmd == -1 || cmd == 15) {
                sendLedData(15, null);
            }
        }
    }

    private boolean shouldDisplayCmdAfterCall(int cmd) {
        switch (cmd) {
            case 5:
            case 6:
            case 9:
            case 11:
                return true;
            case 12:
                if (getLastBatteryInformation() == 100) {
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean isAlarmOrTimerActive() {
        return this.mIsAlarmActive || this.mIsTimerActive;
    }

    private boolean sendMissedEventInformation() {
        boolean sendCmd = false;
        byte[] content = new byte[]{(byte) 0, (byte) 48, (byte) 48, (byte) 48, (byte) 48};
        Log.d(TAG, "sendMissedEventInformation messagesCount=" + String.valueOf(getMissedMessagesCount()) + " callsCount=" + String.valueOf(getMissedCallsCount()));
        if (getMissedMessagesCount() > 0) {
            content[0] = (byte) 2;
            if (getMissedMessagesCount() < 100) {
                byte[] cntMissedMsg = String.format(null, "%02d", new Object[]{Integer.valueOf(getMissedMessagesCount())}).getBytes();
                content[3] = cntMissedMsg[0];
                content[4] = cntMissedMsg[1];
            } else {
                content[3] = (byte) 43;
                content[4] = (byte) 43;
            }
            sendCmd = true;
        }
        if (getMissedCallsCount() > 0) {
            content[0] = (byte) 1;
            if (getMissedCallsCount() < 100) {
                byte[] cntMissedCall = String.format(null, "%02d", new Object[]{Integer.valueOf(getMissedCallsCount())}).getBytes();
                content[1] = cntMissedCall[0];
                content[2] = cntMissedCall[1];
            } else {
                content[1] = (byte) 43;
                content[2] = (byte) 43;
            }
            sendCmd = true;
        }
        if (sendCmd) {
            sendLedData(9, content);
        }
        return sendCmd;
    }

    private boolean sendNewMessageInformation() {
        boolean sendCmd = false;
        byte[] content = new byte[]{(byte) 0, (byte) 0};
        if (getMissedMessagesCount() > 0 || CoverUtils.isTPhoneEnabled(this.mContext)) {
            sendCmd = true;
        }
        if (sendCmd) {
            sendLedData(5, content);
        }
        return sendCmd;
    }

    private void sendNewMissedCallInformation() {
        boolean sendCmd = false;
        byte[] content = new byte[]{(byte) 0, (byte) 48, (byte) 48, (byte) 48, (byte) 48};
        if (getMissedCallsCount() > 0) {
            byte[] cntMissedCall = String.format(null, "%02d", new Object[]{Integer.valueOf(getMissedCallsCount())}).getBytes();
            content[0] = (byte) 1;
            content[1] = cntMissedCall[0];
            content[2] = cntMissedCall[1];
            sendCmd = true;
        }
        if (sendCmd) {
            sendLedData(9, content);
        }
    }

    private void sendVolumeInformation() {
        Log.d(TAG, "sendVolumeInformation");
        if (System.currentTimeMillis() >= this.mVolumeBlockStartTime + 300) {
            byte[] bytesVol = new byte[]{(byte) 0};
            bytesVol[0] = (byte) getVolumeInformation();
            sendLedData(10, bytesVol);
        }
    }

    private void sendClearOngoing(int code) {
        Log.d(TAG, "sendClearOngoing");
        sendLedData(CMD_LED_CLEAR_ONGOING_EVENT, new byte[]{(byte) code});
    }

    private int getVolumeInformation() {
        int volume;
        boolean z = false;
        AudioManager am = (AudioManager) this.mContext.getSystemService("audio");
        if (this.prevCallSatus != 2) {
            volume = am.getStreamVolume(3);
        } else if (am.isBluetoothScoOn()) {
            volume = am.getStreamVolume(4);
        } else {
            volume = am.getStreamVolume(0) * 3;
            if (volume <= 0) {
                volume = 1;
            } else if (volume > 15) {
                volume = 15;
            }
        }
        String str = TAG;
        StringBuilder append = new StringBuilder().append("getVolumeInformation AudioManager: volume = ").append(volume).append(" is call voulme? ");
        if (this.prevCallSatus == 2) {
            z = true;
        }
        Log.d(str, append.append(z).toString());
        return volume;
    }

    private void sendCallIncomingInformation(Intent intent) {
        resetAlarmType();
        byte[] callerId = getIncomingCallId(intent);
        if (this.mTelephonyManager.semIsVideoCall()) {
            sendLedData(20, null);
        } else {
            sendLedData(2, callerId);
        }
    }

    private void sendCallInProgress() {
        if (this.mCallStartMillisData != null && !this.mTelephonyManager.semIsVideoCall()) {
            sendLedData(CMD_LED_CALL_INPROGRESS, this.mCallStartMillisData);
        }
    }

    private void sendLowBatteryInformation() {
        sendLedData(11, getLowBatteryLevel());
    }

    private void sendBatteryChargingInformation() {
        byte[] batteryPercent = new byte[]{(byte) 0};
        int battLevel = getLastBatteryInformation();
        batteryPercent[0] = (byte) battLevel;
        Log.d(TAG, "Battery level = " + battLevel);
        sendLedData(12, batteryPercent);
    }

    private void resetAlarmType() {
        this.mIsAlarmActive = false;
        this.mIsTimerActive = false;
        this.mAlarmStartTime = System.currentTimeMillis();
        this.mTimerStartTime = System.currentTimeMillis();
    }

    private void sendAlarmInformation() {
        byte[] content = new byte[]{(byte) 0};
        if (this.mIsAlarmActive && this.mIsTimerActive) {
            if (this.mAlarmStartTime >= this.mTimerStartTime) {
                content[0] = (byte) 1;
            } else {
                content[0] = (byte) 2;
            }
        } else if (this.mIsAlarmActive) {
            content[0] = (byte) 1;
        } else if (this.mIsTimerActive) {
            content[0] = (byte) 2;
        } else {
            sendLedData(18, null);
            return;
        }
        sendLedData(6, content);
    }

    private void sendMusicPlayerInformation() {
        byte[] content = new byte[]{(byte) 0};
        if (this.mMusicStatus == MusicPlayStatus.PLAYING) {
            if (this.mHeadsetPlugged) {
                content[0] = (byte) 3;
            } else {
                content[0] = (byte) 1;
            }
        } else if (this.mMusicStatus == MusicPlayStatus.PAUSED || this.mMusicStatus == MusicPlayStatus.STOPPED) {
            if (this.mHeadsetPlugged) {
                content[0] = (byte) 4;
            } else {
                content[0] = (byte) 2;
            }
        }
        sendLedData(13, content);
        if (this.mMusicStatus == MusicPlayStatus.PAUSED || this.mMusicStatus == MusicPlayStatus.STOPPED) {
            sendClearOngoing(13);
        }
    }

    private void sendVoiceRecorderInformation() {
        byte[] content = new byte[]{(byte) 0};
        int cmd = 14;
        if (this.mVoiceRecorderStatus == VoiceRecorderStatus.RECORDING) {
            content[0] = (byte) 1;
        } else if (this.mVoiceRecorderStatus == VoiceRecorderStatus.PLAYING) {
            content[0] = (byte) 2;
        } else if (this.mVoiceRecorderStatus == VoiceRecorderStatus.STOPPED) {
            cmd = 18;
        }
        sendLedData(cmd, content);
        if (this.mVoiceRecorderStatus == VoiceRecorderStatus.STOPPED) {
            sendClearOngoing(14);
        }
    }

    private void updateRemoteView(RemoteViewInfo info) {
        String type = info.mType;
        Log.d(TAG, "onRemoteViewUpdated : Type=" + type);
        if (Constants.TYPE_VOICE_RECORDER.equals(type)) {
            int status = info.mIntent.getIntExtra(Constants.KEY_VOICE_RECORDER_STATUS, -1);
            Log.d(TAG, "TYPE_VOICE_RECORDER status: " + status);
            if (status == 1) {
                if (this.mVoiceRecorderStatus != VoiceRecorderStatus.RECORDING) {
                    this.mVoiceRecorderStatus = VoiceRecorderStatus.RECORDING;
                    sendCommandByPriority(14);
                }
            } else if (status == 2) {
                if (this.mVoiceRecorderStatus != VoiceRecorderStatus.PLAYING) {
                    this.mVoiceRecorderStatus = VoiceRecorderStatus.PLAYING;
                    sendCommandByPriority(14);
                }
            } else if (status == 0 && this.mVoiceRecorderStatus == VoiceRecorderStatus.PLAYING) {
                this.mVoiceRecorderStatus = VoiceRecorderStatus.STOPPED;
                sendCommandByPriority(14);
            } else {
                this.mVoiceRecorderStatus = VoiceRecorderStatus.STOPPED;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCoverAttached(com.samsung.android.sdk.cover.ScoverState r10) {
        /*
        r9 = this;
        r4 = 0;
        super.onCoverAttached(r10);
        r0 = r9.mContext;
        r0 = com.sec.android.cover.CoverExecutiveObservator.getInstance(r0);
        r0 = r0.getCoverUpdateMonitor();
        r1 = r9.mUpdateMonitorCallback;
        r0.registerCallback(r1);
        r0 = r9.mContext;
        r0 = com.sec.android.cover.CoverExecutiveObservator.getInstance(r0);
        r0 = r0.getCoverPlaybackStateMonitor();
        r1 = r9.mPlaybackStateChangedListener;
        r0.setOnPlaybackStateChangedListener(r1);
        r0 = r9.mContext;
        r0 = com.sec.android.cover.CoverExecutiveObservator.getInstance(r0);
        r0 = r0.getMissedEventManager();
        r1 = r9.mMissedEventListener;
        r0.setListener(r1);
        r0 = r9.mContext;
        r0 = android.nfc.NfcAdapter.getDefaultAdapter(r0);
        r9.mNfcAdapter = r0;
        r0 = r9.mNfcAdapter;	 Catch:{ Exception -> 0x0055 }
        if (r0 == 0) goto L_0x003d;
    L_0x003d:
        r9.requestRemoteViews();
        r3 = new android.content.IntentFilter;
        r3.<init>();
        r0 = r9.strIntentAction;
        r8 = r0.length;
        r7 = 0;
    L_0x0049:
        if (r7 >= r8) goto L_0x005e;
    L_0x004b:
        r0 = r9.strIntentAction;
        r0 = r0[r7];
        r3.addAction(r0);
        r7 = r7 + 1;
        goto L_0x0049;
    L_0x0055:
        r6 = move-exception;
        r0 = TAG;
        r1 = "Error setting LedCoverNotificationCallback";
        android.util.Log.e(r0, r1, r6);
        goto L_0x003d;
    L_0x005e:
        r0 = r9.mContext;
        r1 = r9.mBroadcastReceiver;
        r2 = android.os.UserHandle.SEM_ALL;
        r5 = r4;
        r0.semRegisterReceiverAsUser(r1, r2, r3, r4, r5);
        r0 = r9.mCoverManager;	 Catch:{ SsdkUnsupportedException -> 0x007e }
        r1 = 0;
        r2 = r9.mCallTouchListener;	 Catch:{ SsdkUnsupportedException -> 0x007e }
        r0.registerNfcTouchListener(r1, r2);	 Catch:{ SsdkUnsupportedException -> 0x007e }
        r0 = r9.mCoverManager;	 Catch:{ SsdkUnsupportedException -> 0x007e }
        r1 = r9.mSystemEventListener;	 Catch:{ SsdkUnsupportedException -> 0x007e }
        r0.registerLedSystemListener(r1);	 Catch:{ SsdkUnsupportedException -> 0x007e }
    L_0x0077:
        r0 = r9.isCoverOpen();
        r9.mPrevCoverStateWasOpen = r0;
        return;
    L_0x007e:
        r6 = move-exception;
        r0 = TAG;
        r1 = "Error registering listener";
        android.util.Log.e(r0, r1, r6);
        goto L_0x0077;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.cover.ledcover.NfcLedCoverController.onCoverAttached(com.samsung.android.sdk.cover.ScoverState):void");
    }

    public void onCoverDetatched(ScoverState state) {
        CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        CoverExecutiveObservator.getInstance(this.mContext).getCoverPlaybackStateMonitor().setOnPlaybackStateChangedListener(null);
        CoverExecutiveObservator.getInstance(this.mContext).getMissedEventManager().setListener(null);
        try {
            this.mCoverManager.unregisterNfcTouchListener(this.mCallTouchListener);
            this.mCoverManager.unregisterLedSystemEventListener(this.mSystemEventListener);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "Error unregistering listener", e);
        }
        super.onCoverDetatched(state);
    }

    public void onCoverEvent(ScoverState state) {
        boolean z = false;
        Log.d(TAG, "onCoverEvent state: " + state + " mPrevStateWasOpen: " + this.mPrevCoverStateWasOpen);
        super.onCoverEvent(state);
        if (this.mCoverEventsDisabledForSamsungPay) {
            Log.d(TAG, "onCoverEvent: SamsungPay active - ignore cover events");
            if (state != null && state.getSwitchState()) {
                coverEventFinished();
                return;
            }
            return;
        }
        if (isCoverOpen()) {
            resetAlarmType();
            if (!this.mPrevCoverStateWasOpen) {
                sendClearOngoing(0);
                sendLedData(18, null);
                CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager().wakeUpWithReason();
            }
            this.isCallPriorityStateDisplayed = false;
        } else {
            if (this.isIncomingCallStatus || this.isCallInProgress) {
                z = true;
            }
            this.isCallPriorityStateDisplayed = z;
            if (this.mPrevCoverStateWasOpen) {
                sendCommandByPriority(-1);
            } else {
                Log.e(TAG, "Not sending command, cover was already closed");
            }
            coverEventFinished();
            CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager().goToSleep();
        }
        this.mPrevCoverStateWasOpen = isCoverOpen();
    }

    public boolean isCoverOpen() {
        if (this.mCoverEventsDisabledForSamsungPay) {
            return true;
        }
        return super.isCoverOpen();
    }

    private void sendLedData(int command, byte[] data) {
        Log.d(TAG, "sendLedData : command = " + getLedCommandStr(command));
        if (data == null) {
            data = new byte[]{(byte) 0};
        }
        try {
            this.mCoverManager.sendDataToNfcLedCover(command, data);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "SendData: SDK unsupported", e);
        }
    }

    private String getLedCommandStr(int command) {
        String cmdStr = "";
        switch (command) {
            case 2:
                return "INCOMING_CALL";
            case 4:
                return "CALL_END";
            case 5:
                return "NEW_MESSAGE";
            case 6:
                return "ALARM";
            case 9:
                return "MISSED_EVENT";
            case 10:
                return "VOLUME";
            case 11:
                return "LOW_BATTERY_ALERT";
            case 12:
                return "BATTERY_CHARGING";
            case 13:
                return "MUSIC";
            case 14:
                return "VOICE_RECORDER";
            case 15:
                return "CLOCK";
            case 16:
                return "INDICATOR";
            case 18:
                return "LED_OFF";
            case 20:
                return "INCOMING_VIDEO_CALL";
            case CMD_LED_CALL_INPROGRESS /*65534*/:
                return "CALL_INPROGRESS";
            default:
                return "Unknown cmd:" + command;
        }
    }

    private int getLastBatteryInformation() {
        return CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().getLastBatteryUpdateState().level;
    }

    private void processCallEvent(Intent intent) {
        byte[] bArr = null;
        if (CoverUtils.isTphoneRelaxMode(this.mContext)) {
            Log.d(TAG, "TPhone relax mode enabled. Ignore PhoneState change");
            return;
        }
        String state = intent.getStringExtra("state");
        if (state == null) {
            return;
        }
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            this.isIncomingCallStatus = true;
            this.mLastInCallIntent = intent;
            this.isCallInProgress = false;
            this.isCallPriorityStateDisplayed = true;
            this.mCallDurationData = null;
            if (this.prevCallSatus != 1) {
                sendCommandByPriority(-1);
            }
            this.prevCallSatus = 1;
        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            this.isIncomingCallStatus = false;
            this.mLastInCallIntent = null;
            this.isCallInProgress = false;
            this.mCallStartMillisData = null;
            if (this.prevCallSatus == 2) {
                sendClearOngoing(CMD_LED_CALL_INPROGRESS);
                if (!isCoverOpen()) {
                    bArr = this.mCallDurationData;
                }
                sendLedData(4, bArr);
            } else if (this.prevCallSatus != 0) {
                sendClearOngoing(2);
                sendClearOngoing(20);
                sendLedData(18, null);
            }
            this.prevCallSatus = 0;
        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            this.isIncomingCallStatus = false;
            this.mLastInCallIntent = null;
            this.isCallInProgress = true;
            this.isCallPriorityStateDisplayed = true;
            this.mCallDurationData = null;
            if (this.prevCallSatus != 2) {
                if (this.mCallStartMillisData != null) {
                    sendCommandByPriority(-1);
                } else {
                    sendLedData(18, null);
                }
            }
            this.prevCallSatus = 2;
        } else {
            this.isIncomingCallStatus = false;
            this.mLastInCallIntent = null;
            this.isCallInProgress = false;
            this.mCallStartMillisData = null;
            this.mCallDurationData = null;
            this.isCallPriorityStateDisplayed = false;
            this.prevCallSatus = -1;
        }
    }

    private boolean sendScheduledCommand() {
        if (this.mPrevInCallEvent == -1) {
            return false;
        }
        if (this.mPrevInCallEvent == 6 && !isAlarmOrTimerActive()) {
            this.mPrevInCallEvent = -1;
            return false;
        } else if (this.mPrevInCallEvent == -1) {
            return false;
        } else {
            Log.d(TAG, "Schedule cached command: " + this.mPrevInCallEvent);
            sendCommandByPriority(this.mPrevInCallEvent);
            this.mPrevInCallEvent = -1;
            return true;
        }
    }

    private void processCallTimeInformation(Intent intent) {
        if (intent == null) {
            Log.e(TAG, "No call info, intent is null");
        } else if (Long.valueOf(intent.getLongExtra("connectedTime", -1)).longValue() == -1) {
            String strCallTime = intent.getStringExtra("calldurationmillis");
            if (strCallTime != null) {
                Log.d(TAG, "strCallTime : " + strCallTime);
                byte[] timeData = strCallTime.getBytes();
                if (timeData.length < 5) {
                    Log.e(TAG, "Time data too short, ignore");
                    return;
                }
                byte[] bArr;
                this.mCallDurationData = new byte[4];
                this.mCallDurationData[3] = timeData[timeData.length - 1];
                this.mCallDurationData[2] = timeData[timeData.length - 2];
                this.mCallDurationData[1] = timeData[timeData.length - 4];
                this.mCallDurationData[0] = timeData[timeData.length - 5];
                String[] separated = strCallTime.split(":");
                if (separated.length == 3) {
                    try {
                        int hour = Integer.valueOf(separated[0]).intValue();
                        int minute = Integer.valueOf(separated[1]).intValue();
                        if (hour == 1 && minute <= 39) {
                            this.mCallDurationData[0] = (byte) (this.mCallDurationData[0] + 6);
                        } else if (hour > 0) {
                            Log.e(TAG, "Call time to long to be shown on LED cover show 99:99");
                            this.mCallDurationData[3] = (byte) 57;
                            this.mCallDurationData[2] = (byte) 57;
                            this.mCallDurationData[1] = (byte) 57;
                            this.mCallDurationData[0] = (byte) 57;
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid Call duration String data", e);
                    }
                }
                this.mCallStartMillisData = null;
                if (isCoverOpen()) {
                    bArr = null;
                } else {
                    bArr = this.mCallDurationData;
                }
                sendLedData(4, bArr);
                return;
            }
            Log.d(TAG, "call duration info is null");
        } else {
            long baseTime = intent.getLongExtra("calldurationmillis", -1);
            if (baseTime != -1) {
                ByteBuffer buffer = ByteBuffer.allocate(64);
                buffer.putLong(baseTime);
                this.mCallStartMillisData = buffer.array();
                if (this.isCallInProgress) {
                    sendCommandByPriority(-1);
                    return;
                }
                return;
            }
            Log.e(TAG, "processCallTimeInformation: BaseTime not added");
        }
    }

    private void processHeadsetPlugInformation(Intent intent) {
        if (intent == null) {
            Log.e(TAG, "No call info, intent is null");
            return;
        }
        switch (intent.getIntExtra("state", -1)) {
            case 0:
                Log.i(TAG, "Headset unplugged");
                this.mHeadsetPlugged = false;
                return;
            case 1:
                Log.i(TAG, "Headset plugged");
                this.mHeadsetPlugged = true;
                this.mVolumeBlockStartTime = System.currentTimeMillis();
                if (this.mMusicStatus == MusicPlayStatus.PLAYING) {
                    sendCommandByPriority(13);
                    return;
                } else if (this.mVoiceRecorderStatus == VoiceRecorderStatus.PLAYING) {
                    sendCommandByPriority(14);
                    return;
                } else {
                    return;
                }
            default:
                Log.i(TAG, "Headset state unknown");
                return;
        }
    }

    private void requestRemoteViews() {
        Log.d(TAG, "requestRemoteViews : Requesting latest remote views to apps..");
        this.mContext.sendBroadcast(new Intent(Constants.ACTION_REQUEST_REMOTE_VIEW));
    }

    private byte[] getLowBatteryLevel() {
        byte b = (byte) 1;
        byte[] batLevel = new byte[1];
        int level = getLastBatteryInformation();
        if (level <= 10) {
            b = (byte) 2;
        }
        batLevel[0] = b;
        Log.d(TAG, "LowBattery level: " + level + " (" + batLevel[0] + ")");
        return batLevel;
    }

    private byte[] getIncomingCallId(Intent intent) {
        byte[] callerId = new byte[]{(byte) -1};
        if (intent != null) {
            String number = intent.getStringExtra("incoming_number");
            if (number == null || number.isEmpty()) {
                Log.e(TAG, "number is null or empty");
            } else {
                Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
                String[] projection = new String[]{"_id", "sec_led"};
                String selection = "sec_led >0";
                Context context = this.mContext;
                int currentUserID = ActivityManager.semGetCurrentUser();
                try {
                    context = RefContext.get().createPackageContextAsUser(this.mContext, "android", 0, RefUserHandle.get().getUserHandle(currentUserID));
                    Log.d(TAG, "getContextForUser: " + currentUserID);
                } catch (NameNotFoundException e) {
                    Log.e(TAG, "Error getting context for user: " + currentUserID);
                }
                Cursor contactLookup = context.getContentResolver().query(uri, projection, selection, null, null);
                int secLed = -1;
                if (contactLookup == null || contactLookup.getCount() <= 0) {
                    Log.d(TAG, "contact not found");
                } else {
                    if (contactLookup.moveToFirst()) {
                        String contactId = contactLookup.getString(0);
                        secLed = contactLookup.getInt(1);
                        int i = 0 + 1;
                        Log.d(TAG, "getSecLedByNumber i: " + 0 + " contactId: " + contactId + " secLed: " + secLed);
                        int i2 = i;
                    } else {
                        Log.e(TAG, "getSecLedByNumber : Cannot access first result");
                    }
                    if (secLed > 0) {
                        callerId[0] = (byte) secLed;
                    }
                }
                if (contactLookup != null) {
                    contactLookup.close();
                }
            }
        }
        return callerId;
    }

    private void handleLedOffEvent(int event) {
        switch (event) {
            case 2:
                if (!this.isCallPriorityStateDisplayed) {
                    sendScheduledCommand();
                    return;
                }
                return;
            case 4:
            case 9:
                if (this.isCallPriorityStateDisplayed) {
                    this.isCallPriorityStateDisplayed = false;
                    sendScheduledCommand();
                    return;
                }
                return;
            default:
                return;
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
        this.mPrevCoverStateWasOpen = true;
        onCoverEvent(getCoverState());
    }

    private int getMissedCallsCount() {
        return this.mMissedCallsCount;
    }

    private int getMissedMessagesCount() {
        return this.mMissedMessagesCount;
    }

    private int getMissedEventsCount() {
        return getMissedMessagesCount() + getMissedCallsCount();
    }
}
