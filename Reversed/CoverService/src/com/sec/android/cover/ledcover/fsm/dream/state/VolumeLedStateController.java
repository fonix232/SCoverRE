package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class VolumeLedStateController extends AbsLedStateController {
    private static final int CMD_LED_VOLUME = 10;

    protected LedState getControllerLedState() {
        return LedState.VOLUME;
    }

    public int getPriority(QueueType type) {
        return 0;
    }

    public byte[] getCommand(LedContext ledContext) {
        return composeCommand(new byte[]{(byte) ledContext.getVolumeLevel()});
    }

    public byte getCommandCodeByte() {
        return (byte) 10;
    }

    public LedState onMusicPause(LedContext ledContext) {
        super.onMusicPause(ledContext);
        return LedState.MUSIC_PAUSED;
    }

    public LedState onMusicStop(LedContext ledContext) {
        super.onMusicStop(ledContext);
        return LedState.MUSIC_PAUSED;
    }

    public LedState onChargerConnected(LedContext ledContext) {
        super.onChargerConnected(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.BATTERY_CHARGING;
        }
        return null;
    }

    public LedState onChargerDisconnected(LedContext ledContext) {
        super.onChargerDisconnected(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.BATTERY_CHARGING;
        }
        return null;
    }

    public LedState onTimeout(LedContext ledContext) {
        if (ledContext.hasState(LedState.DURING_CALL)) {
            return LedState.DURING_CALL;
        }
        if (ledContext.isAlarm()) {
            return LedState.ALARM;
        }
        if (ledContext.isTimer()) {
            return LedState.TIMER;
        }
        if (ledContext.isCalendar()) {
            return LedState.CALENDAR;
        }
        return LedState.IDLE;
    }

    public LedState onEndCall(LedContext ledContext) {
        if (ledContext.isNoCallStateDisplayed()) {
            return null;
        }
        return super.onEndCall(ledContext);
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        super.onNewMissedCall(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.NEW_MISSED_CALL;
        }
        return null;
    }

    public LedState onCustomNotificationAdded(LedContext ledContext) {
        super.onCustomNotificationAdded(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.NEW_MISSED_EVENT;
        }
        return null;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.ALARM;
        }
        return null;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.TIMER;
        }
        return null;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.CALENDAR;
        }
        return null;
    }

    public LedState onMusicPlay(LedContext ledContext) {
        super.onMusicPlay(ledContext);
        return LedState.MUSIC_PLAYING;
    }

    public LedState onVolumeChange(LedContext ledContext) {
        return LedState.VOLUME;
    }

    public LedState onBatteryLow(LedContext ledContext) {
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.BATTERY_LOW;
        }
        return null;
    }

    public LedState onBatteryFull(LedContext ledContext) {
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.BATTERY_FULL;
        }
        return null;
    }

    public LedState onVoiceRecorderStart(LedContext ledContext) {
        super.onVoiceRecorderStart(ledContext);
        return LedState.VOICE_RECORDER_RECORDING;
    }

    public LedState onVoiceRecorderPlay(LedContext ledContext) {
        super.onVoiceRecorderPlay(ledContext);
        return LedState.VOICE_RECORDER_PLAYING;
    }

    public LedState onBixbyActivated(LedContext ledContext) {
        super.onBixbyActivated(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            return LedState.BIXBY;
        }
        ledContext.addState(QueueType.DELAYED, LedState.BIXBY);
        return null;
    }
}
