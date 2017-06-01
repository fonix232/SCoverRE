package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.VoiceRecorderState;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class VoiceRecorderRecordingLedStateController extends AbsLedStateController {
    private static final int CMD_LED_VOICE_RECORDER = 14;
    private static final long VOICE_RECORDER_TIMEOUT = 9200;

    protected LedState getControllerLedState() {
        return LedState.VOICE_RECORDER_RECORDING;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case COVER_CLOSE:
            case POWER_BUTTON:
                return 35;
            default:
                return 45;
        }
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case COVER_CLOSE:
            case POWER_BUTTON:
                return !ledContext.isUPSMEnabled();
            case MAIN:
                return true;
            default:
                return false;
        }
    }

    public byte[] getCommand(LedContext ledContext) {
        return composeCommand(new byte[]{(byte) 0});
    }

    public byte getCommandCodeByte() {
        return (byte) 14;
    }

    public long getTimeout() {
        return VOICE_RECORDER_TIMEOUT;
    }

    public LedState onCustomNotificationAdded(LedContext ledContext) {
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

    public LedState onBatteryLow(LedContext ledContext) {
        return LedState.BATTERY_LOW;
    }

    public LedState onBatteryFull(LedContext ledContext) {
        return LedState.BATTERY_FULL;
    }

    public LedState onChargerConnected(LedContext ledContext) {
        super.onChargerConnected(ledContext);
        return LedState.BATTERY_CHARGING;
    }

    public LedState onChargerDisconnected(LedContext ledContext) {
        super.onChargerDisconnected(ledContext);
        return LedState.BATTERY_CHARGING;
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.IDLE;
    }

    public LedState onVoiceRecorderStop(LedContext ledContext) {
        super.onVoiceRecorderStop(ledContext);
        return LedState.IDLE;
    }

    public LedState onVoiceRecorderPlay(LedContext ledContext) {
        super.onVoiceRecorderPlay(ledContext);
        return LedState.VOICE_RECORDER_PLAYING;
    }

    public LedState onVoiceRecorderStart(LedContext ledContext) {
        super.onVoiceRecorderStart(ledContext);
        return LedState.VOICE_RECORDER_RECORDING;
    }

    public void onStateExit(LedContext ledContext) {
        if (ledContext.getVoiceRecorderState() == VoiceRecorderState.RECORD) {
            ledContext.addState(LedState.VOICE_RECORDER_RECORDING);
        }
    }
}
