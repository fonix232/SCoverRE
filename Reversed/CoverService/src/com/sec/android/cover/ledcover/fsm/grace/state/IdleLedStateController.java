package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class IdleLedStateController extends AbsLedStateController {
    private static final int CMD_LED_IDLE = 18;
    private static final long IDLE_TIMEOUT = 0;

    protected LedState getControllerLedState() {
        return LedState.IDLE;
    }

    public boolean isDataReady(LedContext ledContext) {
        return false;
    }

    public int getPriority(QueueType type) {
        return Integer.MAX_VALUE;
    }

    public byte getCommandCodeByte() {
        return (byte) 18;
    }

    protected long getDefaultTimeout() {
        return 0;
    }

    public LedState onTimeout(LedContext ledContext) {
        if (!ledContext.isNoCallStateDisplayed()) {
            return ledContext.pollState();
        }
        if (ledContext.hasState(LedState.ALARM) || ledContext.hasState(LedState.TIMER) || ledContext.hasState(LedState.CALENDAR)) {
            return ledContext.pollState();
        }
        if (!ledContext.isEmpty(QueueType.DELAYED)) {
            return ledContext.peekState(QueueType.DELAYED);
        }
        if (!ledContext.isEmpty(QueueType.POWER_BUTTON)) {
            return ledContext.pollState(QueueType.POWER_BUTTON);
        }
        if (ledContext.isPowerButtonWakeUp()) {
            ledContext.setIsPowerButtonWakeUp(false);
        }
        if (!ledContext.isEmpty(QueueType.COVER_CLOSE)) {
            return ledContext.pollState(QueueType.COVER_CLOSE);
        }
        if (ledContext.isCoverClosedWakeUp()) {
            ledContext.setIsCoverClosedWakeUp(false);
        }
        return null;
    }

    public LedState onMusicPlay(LedContext ledContext) {
        super.onMusicPlay(ledContext);
        return LedState.MUSIC_PLAYING;
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
        return LedState.BATTERY_CHARGING;
    }

    public LedState onChargerDisconnected(LedContext ledContext) {
        super.onChargerDisconnected(ledContext);
        return LedState.BATTERY_CHARGING;
    }

    public LedState onNewMessage(LedContext ledContext) {
        return LedState.NEW_MISSED_EVENT;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        return LedState.ALARM;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        return LedState.TIMER;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        return LedState.CALENDAR;
    }

    public LedState onBatteryLow(LedContext ledContext) {
        return LedState.BATTERY_LOW;
    }

    public LedState onBatteryFull(LedContext ledContext) {
        return LedState.BATTERY_FULL;
    }

    public LedState onVoiceRecorderStart(LedContext ledContext) {
        super.onVoiceRecorderStart(ledContext);
        return LedState.VOICE_RECORDER_RECORDING;
    }

    public LedState onVoiceRecorderPlay(LedContext ledContext) {
        super.onVoiceRecorderPlay(ledContext);
        return LedState.VOICE_RECORDER_PLAYING;
    }

    public LedState onCustomNotificationAdded(LedContext ledContext) {
        super.onCustomNotificationAdded(ledContext);
        return LedState.NEW_MISSED_EVENT;
    }
}
