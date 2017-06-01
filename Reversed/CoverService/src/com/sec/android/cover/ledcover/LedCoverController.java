package com.sec.android.cover.ledcover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.cover.BaseCoverController;
import com.sec.android.cover.Constants;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.manager.CoverMissedEventManager;
import com.sec.android.cover.manager.CoverMissedEventManager.RemoteViewsItem;
import com.sec.android.cover.manager.CoverPowerManager;
import com.sec.android.cover.manager.CoverRemoteViewManager;
import com.sec.android.cover.monitor.CoverUpdateMonitor.RemoteViewInfo;
import com.sec.android.cover.monitor.CoverUpdateMonitorCallback;
import java.util.Calendar;

public class LedCoverController extends BaseCoverController {
    public static final int LED_COMMAND_ALARM = 13;
    public static final int LED_COMMAND_BATTERY_CHARGING = 10;
    public static final int LED_COMMAND_BATTERY_CHARGING_END = 11;
    public static final int LED_COMMAND_BRIGHTNESS = 17;
    public static final int LED_COMMAND_CALL_END = 12;
    public static final int LED_COMMAND_CLOCK = 1;
    public static final int LED_COMMAND_INCOMING_CALL = 4;
    public static final int LED_COMMAND_LED_OFF = 15;
    public static final int LED_COMMAND_LED_ON = 14;
    public static final int LED_COMMAND_LOW_BATTERY_ALERT = 7;
    public static final int LED_COMMAND_MISSED_CALL_MSG = 2;
    public static final int LED_COMMAND_MUSIC = 5;
    public static final int LED_COMMAND_PEDOMETER = 8;
    public static final int LED_COMMAND_POWER_ON_OFF = 6;
    public static final int LED_COMMAND_REQ_LED_STATUS = 9;
    public static final int LED_COMMAND_VOLUME = 3;
    private static final String TAG = LedCoverController.class.getSimpleName();
    private final int ALARM_EXPIRED = 1;
    private final long ALARM_EXPIRE_TIME_DURATION = 60000;
    private final String INTENT_ACTION_ALARM_ALERT = Constants.INTENT_ACTION_ALARM_ALERT;
    private final String INTENT_ACTION_ALARM_START_ALERT = Constants.INTENT_ACTION_ALARM_START_ALERT;
    private final String INTENT_ACTION_ALARM_STOP_ALERT = Constants.INTENT_ACTION_ALARM_STOP_ALERT;
    private final String INTENT_ACTION_CALL_ENDED = "com.sec.android.phone.action.ACTION_CALL_ENDED";
    private final String INTENT_ACTION_PHONESTATE = "android.intent.action.PHONE_STATE";
    private final String INTENT_ACTION_SPLANNER = Constants.INTENT_ACTION_CALENDAR_START;
    private final String INTENT_ACTION_TIMER = "com.sec.android.app.clockpackage.timer.REMOTE_TIMER_FINISH";
    private final String INTENT_ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";
    private boolean isBatteryCharging = false;
    private boolean isIncomingCallStatus = false;
    private boolean isScreenOn = false;
    private long mAlarmStartTime = 0;
    private Handler mAlarmTimerHandler = new C00181();
    private AlarmType mAlarmType = AlarmType.NONE;
    private BroadcastReceiver mBroadcastReceiver = new C00203();
    private ScoverManager mCoverManager = null;
    private MusicPlayStatus mMusicStatus = MusicPlayStatus.STOPPED;
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00192();
    private final byte[] nullContent = new byte[]{(byte) 0};
    private int prevCallSatus;
    private String[] strIntentAction = new String[]{"android.intent.action.SCREEN_ON", "android.intent.action.SCREEN_OFF", "android.intent.action.BATTERY_LOW", "android.intent.action.ACTION_POWER_CONNECTED", "android.intent.action.ACTION_POWER_DISCONNECTED", "android.media.VOLUME_CHANGED_ACTION", "android.intent.action.PHONE_STATE", Constants.INTENT_ACTION_ALARM_ALERT, Constants.INTENT_ACTION_ALARM_START_ALERT, Constants.INTENT_ACTION_ALARM_STOP_ALERT, "com.sec.android.app.clockpackage.timer.REMOTE_TIMER_FINISH", "com.sec.android.phone.action.ACTION_CALL_ENDED", Constants.INTENT_ACTION_CALENDAR_START};

    class C00181 extends Handler {
        C00181() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LedCoverController.this.resetAlarmType();
                    LedCoverController.this.sendLedData(15, null);
                    CoverExecutiveObservator.getInstance(LedCoverController.this.mContext).getCoverPowerManager().goToSleep();
                    return;
                default:
                    return;
            }
        }
    }

    class C00192 extends CoverUpdateMonitorCallback {
        C00192() {
        }

        public void onRemoteViewUpdated(RemoteViewInfo remoteViewInfo) {
            LedCoverController.this.updateRemoteView(remoteViewInfo);
        }

        public void onPowerConnectionUpdate(boolean connected) {
            LedCoverController.this.isBatteryCharging = connected;
            LedCoverController.this.sendBatteryChargingInformation(null);
        }

        public void onTimeChanged() {
            if (LedCoverController.this.isScreenOn) {
                LedCoverController.this.sendClockInformation();
            }
        }
    }

    class C00203 extends BroadcastReceiver {
        C00203() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(LedCoverController.TAG, "BroadcastReceiver onReceive() : action : " + action);
            if (LedCoverController.this.mAlarmType != AlarmType.NONE) {
                long curTime = System.currentTimeMillis();
                if (curTime - LedCoverController.this.mAlarmStartTime < 0 || curTime - LedCoverController.this.mAlarmStartTime > 60000) {
                    LedCoverController.this.mAlarmType = AlarmType.NONE;
                    Log.d(LedCoverController.TAG, "Reset alarm type to NONE");
                }
            }
            if (action.equals("android.intent.action.SCREEN_ON")) {
                LedCoverController.this.isScreenOn = true;
                if (!LedCoverController.this.isCoverOpen()) {
                    LedCoverController.this.sendCommandByPriority();
                    LedCoverController.this.coverEventFinished();
                }
            } else if (action.equals("android.intent.action.SCREEN_OFF")) {
                LedCoverController.this.isScreenOn = false;
            }
            if (action.equals("android.intent.action.BATTERY_LOW")) {
                LedCoverController.this.sendLowBatteryInformation(intent);
            } else if (Constants.INTENT_ACTION_ALARM_ALERT.equals(action) || Constants.INTENT_ACTION_ALARM_START_ALERT.equals(action)) {
                if (!LedCoverController.this.isCoverOpen()) {
                    LedCoverController.this.setAlarmType(AlarmType.ALARM);
                    LedCoverController.this.sendAlarmInformation();
                }
            } else if (Constants.INTENT_ACTION_ALARM_STOP_ALERT.equals(action)) {
                if (LedCoverController.this.mAlarmType == AlarmType.ALARM) {
                    LedCoverController.this.resetAlarmType();
                    if (!LedCoverController.this.isIncomingCallStatus) {
                        LedCoverController.this.sendLedData(15, null);
                    }
                }
            } else if ("com.sec.android.app.clockpackage.timer.REMOTE_TIMER_FINISH".equals(action)) {
                if (!LedCoverController.this.isCoverOpen()) {
                    LedCoverController.this.setAlarmType(AlarmType.TIMER);
                    LedCoverController.this.sendAlarmInformation();
                }
            } else if (Constants.INTENT_ACTION_CALENDAR_START.equals(action)) {
                if (!LedCoverController.this.isCoverOpen()) {
                    LedCoverController.this.setAlarmType(AlarmType.SPLANNER);
                    LedCoverController.this.sendAlarmInformation();
                }
            } else if ("android.intent.action.PHONE_STATE".equals(action)) {
                LedCoverController.this.processCallEvent(intent);
            } else if ("android.media.VOLUME_CHANGED_ACTION".equals(action)) {
                if ((LedCoverController.this.prevCallSatus == 2 || LedCoverController.this.mMusicStatus == MusicPlayStatus.PLAYING) && LedCoverController.this.mAlarmType == AlarmType.NONE && !LedCoverController.this.isIncomingCallStatus) {
                    LedCoverController.this.sendVolumeInformation(intent);
                }
            } else if ("com.sec.android.phone.action.ACTION_CALL_ENDED".equals(action) && LedCoverController.this.mAlarmType == AlarmType.NONE) {
                LedCoverController.this.sendEndCallInformation(intent);
            }
        }
    }

    private enum AlarmType {
        NONE,
        ALARM,
        TIMER,
        SPLANNER
    }

    private enum MusicPlayStatus {
        STOPPED,
        PLAYING,
        PAUSED
    }

    public LedCoverController(Context context) {
        super(context);
        this.mCoverManager = new ScoverManager(context);
        updateMissedEvent(false);
    }

    private void sendCommandByPriority() {
        if (this.isIncomingCallStatus) {
            sendCallInformation(null);
        } else if (this.mAlarmType != AlarmType.NONE) {
            sendAlarmInformation();
        } else if (CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().getMissedEventCount() > 0) {
            sendMissedEventInformation();
        } else if (this.mMusicStatus == MusicPlayStatus.PLAYING) {
            sendMusicPlayerInformation();
        } else if (this.isBatteryCharging) {
            sendBatteryChargingInformation(null);
        } else {
            sendClockInformation();
        }
    }

    protected boolean sendMissedEventInformation() {
        boolean z = false;
        byte[] content = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        if (this.isIncomingCallStatus || this.mAlarmType != AlarmType.NONE) {
            return 0;
        }
        if (CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().getMissedEventCallCount() > 0) {
            content[0] = (byte) 1;
            byte[] cntMissedCall = intTo2ByteArray(CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().getMissedEventCallCount());
            content[2] = cntMissedCall[0];
            content[3] = cntMissedCall[1];
            z = true;
        }
        if (CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().getMissedEventMessageCount() > 0) {
            content[1] = (byte) 1;
            cntMissedCall = intTo2ByteArray(CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().getMissedEventMessageCount());
            content[4] = cntMissedCall[0];
            content[5] = cntMissedCall[1];
            z = true;
        }
        if (z) {
            sendLedData(2, content);
        }
        return z;
    }

    protected void sendClockInformation() {
        if (isCoverOpen()) {
            Log.d(TAG, "sendClockInformation : Cover is opend");
            return;
        }
        String curTimeStr;
        boolean bIs24HTime = DateFormat.is24HourFormat(this.mContext);
        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(curTime);
        int hours = c.get(11);
        int minutes = c.get(12);
        if (bIs24HTime) {
            curTimeStr = String.format(null, "%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes)});
        } else {
            hours %= 12;
            if (hours == 0) {
                hours = 12;
            }
            curTimeStr = String.format(null, "%2d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes)});
        }
        Log.d(TAG, "sendClockInformation : " + curTimeStr);
        sendLedData(1, curTimeStr.getBytes());
    }

    protected void sendVolumeInformation(Intent intent) {
        int volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
        int type = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -999);
        byte[] bytesVol = new byte[]{(byte) 0};
        Log.d(TAG, "sendVolumeInformation : volume = " + volume);
        if (this.prevCallSatus == 2) {
            if (type == 0) {
                volume *= 3;
                if (volume <= 0) {
                    volume = 1;
                } else if (volume > 15) {
                    volume = 15;
                }
            } else {
                return;
            }
        }
        bytesVol[0] = (byte) volume;
        sendLedData(3, bytesVol);
    }

    protected void sendCallInformation(Intent intent) {
        resetAlarmType();
        sendLedData(4, this.nullContent);
    }

    @Deprecated
    protected void sendCallEndInformation(Intent intent) {
        byte[] content = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        content[2] = (byte) 58;
        sendLedData(12, content);
    }

    protected void sendLowBatteryInformation(Intent intent) {
        sendLedData(7, this.nullContent);
    }

    protected void sendBatteryChargingInformation(Intent intent) {
        byte[] batteryPercent = new byte[]{(byte) 0};
        int battLevel = getLastBatteryInformation();
        batteryPercent[0] = (byte) battLevel;
        Log.d(TAG, "Battery level = " + battLevel);
        sendLedData(10, batteryPercent);
    }

    protected void sendChargingEndInformation(Intent intent) {
        byte[] content = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        int battLevel = getLastBatteryInformation();
        byte[] timeData = DateFormat.format("kk:mm", System.currentTimeMillis()).toString().getBytes();
        content[0] = (byte) battLevel;
        try {
            System.arraycopy(timeData, 0, content, 1, timeData.length);
        } catch (IndexOutOfBoundsException ioobe) {
            ioobe.printStackTrace();
        } catch (ArrayStoreException ase) {
            ase.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        sendLedData(11, content);
    }

    protected void resetAlarmType() {
        this.mAlarmType = AlarmType.NONE;
        this.mAlarmStartTime = System.currentTimeMillis();
    }

    protected void setAlarmType(AlarmType type) {
        this.mAlarmType = type;
        this.mAlarmStartTime = System.currentTimeMillis();
    }

    protected void sendAlarmInformation() {
        byte[] content = new byte[]{(byte) 0};
        if (!this.isIncomingCallStatus && this.mAlarmType != AlarmType.NONE) {
            if (this.mAlarmType == AlarmType.ALARM) {
                content[0] = (byte) 1;
            } else if (this.mAlarmType == AlarmType.TIMER) {
                content[0] = (byte) 2;
            } else if (this.mAlarmType == AlarmType.SPLANNER) {
                content[0] = (byte) 3;
            } else {
                content[0] = (byte) 1;
            }
            sendLedData(13, content);
        }
    }

    protected void sendCurrentBrightnessGrade() {
        CoverPowerManager cpm = CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager();
        if (cpm != null) {
            byte[] brightnessGrade = new byte[1];
            float currentBrighness = cpm.getCurrentBrightness();
            if (currentBrighness < 50.0f) {
                brightnessGrade[0] = (byte) 1;
            } else if (currentBrighness < 205.0f) {
                brightnessGrade[0] = (byte) 2;
            } else {
                brightnessGrade[0] = (byte) 3;
            }
            Log.d(TAG, "LED currentBrighness = " + currentBrighness + " | brightnessGrade = " + brightnessGrade[0]);
            sendLedData(17, brightnessGrade);
        }
    }

    @Deprecated
    protected void sendBatteryChargingInformation() {
        sendLedData(10, this.nullContent);
    }

    private void sendMusicPlayerInformation() {
        byte[] content = new byte[]{(byte) 0};
        if (this.mMusicStatus == MusicPlayStatus.PLAYING) {
            content[0] = (byte) 1;
        } else if (this.mMusicStatus == MusicPlayStatus.PAUSED) {
            content[0] = (byte) 2;
        }
        if (!this.isIncomingCallStatus && this.mAlarmType == AlarmType.NONE) {
            sendLedData(5, content);
        }
    }

    private void updateRemoteView(RemoteViewInfo info) {
        String type = info.mType;
        Log.d(TAG, "onRemoteViewUpdated : Type=" + type);
        if (this.mAlarmType != AlarmType.NONE) {
            long curTime = System.currentTimeMillis();
            if (curTime - this.mAlarmStartTime < 0 || curTime - this.mAlarmStartTime > 60000) {
                this.mAlarmType = AlarmType.NONE;
                Log.d(TAG, "Reset alarm type to NONE");
            }
        }
        if (Constants.TYPE_MISSED_MESSAGE.equals(type) || Constants.TYPE_MISSED_CALL.equals(type)) {
            updateMissedEvent(true);
        } else if (Constants.TYPE_MUSIC_PLAYER.equals(type)) {
            int playstate = info.mIntent.getIntExtra("playstate", 2);
            boolean bPlayStateChanged = info.mIntent.getBooleanExtra("playstatechanged", false);
            if (playstate == 0) {
                this.mMusicStatus = MusicPlayStatus.PLAYING;
                if (bPlayStateChanged) {
                    sendMusicPlayerInformation();
                }
            } else if (playstate == 1) {
                this.mMusicStatus = MusicPlayStatus.PAUSED;
                if (bPlayStateChanged) {
                    sendMusicPlayerInformation();
                }
            } else {
                this.mMusicStatus = MusicPlayStatus.STOPPED;
            }
        }
    }

    public void updateMissedEvent(boolean sendDataToCover) {
        Log.d(TAG, "updateMissedEvent()");
        CoverRemoteViewManager coverRemoteViewManager = CoverExecutiveObservator.getInstance(this.mContext).getCoverRemoteViewManager();
        RemoteViewInfo messageRemoteViewInfo = coverRemoteViewManager.getRemoteViewInfo(Constants.TYPE_MISSED_MESSAGE);
        RemoteViewInfo callRemoteViewInfo = coverRemoteViewManager.getRemoteViewInfo(Constants.TYPE_MISSED_CALL);
        CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().clearMissedEvent();
        if (callRemoteViewInfo != null && callRemoteViewInfo.mCount > 0) {
            CoverMissedEventManager coverMissedEventManager = CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager();
            coverMissedEventManager.getClass();
            CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().addMissedEvent(new RemoteViewsItem(1, System.currentTimeMillis(), callRemoteViewInfo.mRemoteViews, callRemoteViewInfo.mPendingIntent, callRemoteViewInfo.mCount, callRemoteViewInfo.mType));
        }
        if (messageRemoteViewInfo != null && messageRemoteViewInfo.mCount > 0) {
            coverMissedEventManager = CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager();
            coverMissedEventManager.getClass();
            CoverExecutiveObservator.getInstance(this.mContext).getCoverMissedEventManager().addMissedEvent(new RemoteViewsItem(1, System.currentTimeMillis(), messageRemoteViewInfo.mRemoteViews, messageRemoteViewInfo.mPendingIntent, messageRemoteViewInfo.mCount, messageRemoteViewInfo.mType));
        }
        if (sendDataToCover) {
            sendMissedEventInformation();
        }
    }

    public void onCoverAttached(ScoverState state) {
        super.onCoverAttached(state);
        CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().registerCallback(this.mUpdateMonitorCallback);
        requestRemoteViews();
        IntentFilter filter = new IntentFilter();
        for (String addAction : this.strIntentAction) {
            filter.addAction(addAction);
        }
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        sendClockInformation();
    }

    public void onCoverDetatched(ScoverState state) {
        CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        super.onCoverDetatched(state);
    }

    public void onCoverEvent(ScoverState state) {
        super.onCoverEvent(state);
        CoverPowerManager cpm = CoverExecutiveObservator.getInstance(this.mContext).getCoverPowerManager();
        if (isCoverOpen()) {
            resetAlarmType();
            sendLedData(15, null);
            cpm.wakeUpWithReason();
            return;
        }
        sendCurrentBrightnessGrade();
        sendCommandByPriority();
        coverEventFinished();
        if (cpm != null) {
            cpm.goToSleep();
        }
    }

    private void sendLedData(int command, byte[] data) {
        Log.d(TAG, "sendLedData : command = " + getLedCommandStr(command));
        if (data == null) {
            data = new byte[]{(byte) 0};
        }
        if (command == 15) {
            try {
                this.mCoverManager.sendDataToCover(command, data);
            } catch (SsdkUnsupportedException ue) {
                ue.printStackTrace();
            }
        } else if (!isCoverOpen()) {
            this.mCoverManager.sendDataToCover(command, data);
            this.mCoverManager.sendDataToCover(14, this.nullContent);
        }
    }

    private String getLedCommandStr(int command) {
        String cmdStr = "";
        switch (command) {
            case 1:
                return "CLOCK";
            case 2:
                return "MISSED_CALL_MSG";
            case 3:
                return "VOLUME";
            case 4:
                return "INCOMING_CALL";
            case 5:
                return "MUSIC";
            case 6:
                return "POWER_ON_OFF";
            case 7:
                return "LOW_BATTERY_ALERT";
            case 8:
                return "PEDOMETER";
            case 9:
                return "REQ_LED_STATUS";
            case 10:
                return "BATTERY_CHARGING";
            case 11:
                return "BATTERY_CHARGING_END";
            case 12:
                return "CALL_END";
            case 13:
                return "ALARM";
            case 14:
                return "LED_ON";
            case 15:
                return "LED_OFF";
            case 17:
                return "LED_BRIGHTNESS";
            default:
                return "Unknown cmd:" + command;
        }
    }

    public static byte[] intTo2ByteArray(int a) {
        return new byte[]{(byte) ((a >> 8) & 255), (byte) (a & 255)};
    }

    private int getLastBatteryInformation() {
        return CoverExecutiveObservator.getInstance(this.mContext).getCoverUpdateMonitor().getLastBatteryUpdateState().level;
    }

    private void processCallEvent(Intent intent) {
        String state = intent.getStringExtra("state");
        if (state != null) {
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                this.isIncomingCallStatus = true;
                sendCallInformation(intent);
                this.prevCallSatus = 1;
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                this.isIncomingCallStatus = false;
                if (!(this.prevCallSatus == 2 || this.prevCallSatus == 0)) {
                    sendLedData(15, this.nullContent);
                }
                this.prevCallSatus = 0;
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                this.isIncomingCallStatus = false;
                sendLedData(15, this.nullContent);
                this.prevCallSatus = 2;
            } else {
                this.isIncomingCallStatus = false;
                this.prevCallSatus = -1;
            }
        }
    }

    private void sendEndCallInformation(Intent intent) {
        String strCallTime = intent.getStringExtra("calldurationmillis");
        if (strCallTime != null) {
            Log.d(TAG, "strCallTime : " + strCallTime);
            sendLedData(12, strCallTime.getBytes());
            return;
        }
        Log.d(TAG, "call duration info is null");
    }

    private void requestRemoteViews() {
        Log.d(TAG, "requestRemoteViews : Requesting latest remote views to apps..");
        this.mContext.sendBroadcast(new Intent(Constants.ACTION_REQUEST_REMOTE_VIEW));
    }
}
