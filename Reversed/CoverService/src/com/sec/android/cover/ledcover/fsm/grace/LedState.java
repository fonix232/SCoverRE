package com.sec.android.cover.ledcover.fsm.grace;

import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;
import com.sec.android.cover.ledcover.fsm.grace.state.AlarmLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.BatteryChargingLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.BatteryFullLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.BatteryLowLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.CalendarLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.ClockLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.DuringCallLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.EndCallLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.IdleLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.IncomingCallLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.IncomingVideoCallLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.LedLampNotiLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.MissedEventLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.MusicPausedLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.MusicPlayingLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.NewMissedCallLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.NewMissedEventLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.TimerLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.VoiceRecorderPlayingLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.VoiceRecorderRecordingLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.state.VolumeLedStateController;

public enum LedState implements LedStateController {
    IDLE(new IdleLedStateController()),
    INCOMING_CALL(new IncomingCallLedStateController()),
    DURING_CALL(new DuringCallLedStateController()),
    END_CALL(new EndCallLedStateController()),
    INCOMING_VIDEO_CALL(new IncomingVideoCallLedStateController()),
    NEW_MISSED_CALL(new NewMissedCallLedStateController()),
    NEW_MISSED_EVENT(new NewMissedEventLedStateController()),
    MISSED_EVENT(new MissedEventLedStateController()),
    VOLUME(new VolumeLedStateController()),
    VOICE_RECORDER_RECORDING(new VoiceRecorderRecordingLedStateController()),
    VOICE_RECORDER_PLAYING(new VoiceRecorderPlayingLedStateController()),
    MUSIC_PLAYING(new MusicPlayingLedStateController()),
    MUSIC_PAUSED(new MusicPausedLedStateController()),
    BATTERY_CHARGING(new BatteryChargingLedStateController()),
    BATTERY_LOW(new BatteryLowLedStateController()),
    BATTERY_FULL(new BatteryFullLedStateController()),
    CLOCK(new ClockLedStateController()),
    ALARM(new AlarmLedStateController()),
    TIMER(new TimerLedStateController()),
    LED_LAMP_NOTI(new LedLampNotiLedStateController()),
    CALENDAR(new CalendarLedStateController());
    
    private final LedStateController mController;

    private LedState(LedStateController controller) {
        this.mController = controller;
    }

    public int getPriority(QueueType type) {
        return this.mController.getPriority(type);
    }

    public long getTimeout() {
        return this.mController.getTimeout();
    }

    public byte[][] getCommand(LedContext ledContext) {
        return this.mController.getCommand(ledContext);
    }

    public byte getCommandCodeByte() {
        return this.mController.getCommandCodeByte();
    }

    public boolean isInfinite() {
        return this.mController.isInfinite();
    }

    public boolean isDataReady(LedContext ledContext) {
        return this.mController.isDataReady(ledContext);
    }

    public boolean shouldWakeupForLedLamp() {
        return this.mController.shouldWakeupForLedLamp();
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        return this.mController.includeInQueue(type, ledContext);
    }

    public int getTouchEventListenerType() {
        return this.mController.getTouchEventListenerType();
    }

    public LedState onIncomingCall(LedContext ledContext) {
        return this.mController.onIncomingCall(ledContext);
    }

    public LedState onRejectCall(LedContext ledContext) {
        return this.mController.onRejectCall(ledContext);
    }

    public LedState onAcceptCall(LedContext ledContext) {
        return this.mController.onAcceptCall(ledContext);
    }

    public LedState onEndCall(LedContext ledContext) {
        return this.mController.onEndCall(ledContext);
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        return this.mController.onNewMissedCall(ledContext);
    }

    public LedState onNewMutedMissedCall(LedContext ledContext) {
        return this.mController.onNewMutedMissedCall(ledContext);
    }

    public LedState onNewMessage(LedContext ledContext) {
        return this.mController.onNewMessage(ledContext);
    }

    public LedState onNewMutedMessage(LedContext ledContext) {
        return this.mController.onNewMutedMessage(ledContext);
    }

    public LedState onAlarmStart(LedContext ledContext) {
        return this.mController.onAlarmStart(ledContext);
    }

    public LedState onAlarmStop(LedContext ledContext) {
        return this.mController.onAlarmStop(ledContext);
    }

    public LedState onTimerStart(LedContext ledContext) {
        return this.mController.onTimerStart(ledContext);
    }

    public LedState onTimerStop(LedContext ledContext) {
        return this.mController.onTimerStop(ledContext);
    }

    public LedState onCalendarStart(LedContext ledContext) {
        return this.mController.onCalendarStart(ledContext);
    }

    public LedState onCalendarStop(LedContext ledContext) {
        return this.mController.onCalendarStop(ledContext);
    }

    public LedState onMusicPlay(LedContext ledContext) {
        return this.mController.onMusicPlay(ledContext);
    }

    public LedState onMusicPause(LedContext ledContext) {
        return this.mController.onMusicPause(ledContext);
    }

    public LedState onVolumeChange(LedContext ledContext) {
        return this.mController.onVolumeChange(ledContext);
    }

    public LedState onBatteryLow(LedContext ledContext) {
        return this.mController.onBatteryLow(ledContext);
    }

    public LedState onBatteryFull(LedContext ledContext) {
        return this.mController.onBatteryFull(ledContext);
    }

    public LedState onChargerConnected(LedContext ledContext) {
        return this.mController.onChargerConnected(ledContext);
    }

    public LedState onChargerDisconnected(LedContext ledContext) {
        return this.mController.onChargerDisconnected(ledContext);
    }

    public LedState onTimeout(LedContext ledContext) {
        return this.mController.onTimeout(ledContext);
    }

    public LedState onVoiceRecorderStart(LedContext ledContext) {
        return this.mController.onVoiceRecorderStart(ledContext);
    }

    public LedState onVoiceRecorderPlay(LedContext ledContext) {
        return this.mController.onVoiceRecorderPlay(ledContext);
    }

    public LedState onVoiceRecorderStop(LedContext ledContext) {
        return this.mController.onVoiceRecorderStop(ledContext);
    }

    public LedState onHeadsetPlugStateChanged(LedContext ledContext) {
        return this.mController.onHeadsetPlugStateChanged(ledContext);
    }

    public LedState onMusicStop(LedContext ledContext) {
        return this.mController.onMusicStop(ledContext);
    }

    public LedState onMissedEventCleared(LedContext ledContext) {
        return this.mController.onMissedEventCleared(ledContext);
    }

    public LedState onCustomNotificationAdded(LedContext ledContext) {
        return this.mController.onCustomNotificationAdded(ledContext);
    }

    public LedState onCustomNotificationRemoved(LedContext ledContext) {
        return this.mController.onCustomNotificationRemoved(ledContext);
    }

    public LedState onLedLampNoti(LedContext ledContext) {
        return this.mController.onLedLampNoti(ledContext);
    }

    public LedState onCableWithWirelessConnected(LedContext ledContext) {
        return this.mController.onCableWithWirelessConnected(ledContext);
    }

    public LedState onCableWithWirelessDisConnected(LedContext ledContext) {
        return this.mController.onCableWithWirelessDisConnected(ledContext);
    }

    public void onStateEnter(LedContext ledContext) {
        this.mController.onStateEnter(ledContext);
    }

    public void onStateExit(LedContext ledContext) {
        this.mController.onStateExit(ledContext);
    }

    public void onSendingState(LedContext ledContext) {
        this.mController.onSendingState(ledContext);
    }
}
