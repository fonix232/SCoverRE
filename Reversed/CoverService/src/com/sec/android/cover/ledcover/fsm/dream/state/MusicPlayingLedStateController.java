package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class MusicPlayingLedStateController extends AbsLedStateController {
    private static final int CMD_LED_MUSIC = 13;
    private static final long MUSIC_TIMEOUT = 9200;

    protected LedState getControllerLedState() {
        return LedState.MUSIC_PLAYING;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case COVER_CLOSE:
            case POWER_BUTTON:
                return 30;
            default:
                return 40;
        }
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case COVER_CLOSE:
            case MAIN:
                if (ledContext.isUPSMEnabled()) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public byte[] getCommand(LedContext ledContext) {
        int musicPlayingStatus;
        if (ledContext.isHeadsetPlugged()) {
            musicPlayingStatus = 3;
        } else {
            musicPlayingStatus = 1;
        }
        return composeCommand(new byte[]{(byte) musicPlayingStatus});
    }

    public byte getCommandCodeByte() {
        return (byte) 13;
    }

    public long getTimeout() {
        return MUSIC_TIMEOUT;
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

    public LedState onHeadsetPlugStateChanged(LedContext ledContext) {
        return LedState.MUSIC_PLAYING;
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

    public LedState onVoiceRecorderStart(LedContext ledContext) {
        super.onVoiceRecorderStart(ledContext);
        return LedState.VOICE_RECORDER_RECORDING;
    }

    public LedState onVoiceRecorderPlay(LedContext ledContext) {
        super.onVoiceRecorderPlay(ledContext);
        return LedState.VOICE_RECORDER_PLAYING;
    }

    public LedState onMusicPause(LedContext ledContext) {
        super.onMusicPause(ledContext);
        return LedState.MUSIC_PAUSED;
    }

    public LedState onMusicStop(LedContext ledContext) {
        super.onMusicStop(ledContext);
        return LedState.MUSIC_PAUSED;
    }

    public LedState onBixbyActivated(LedContext ledContext) {
        super.onBixbyActivated(ledContext);
        return LedState.BIXBY;
    }

    public void onStateExit(LedContext ledContext) {
        if (ledContext.getMusicState() == MusicState.PLAY) {
            ledContext.addState(LedState.MUSIC_CONTROLLER);
            ledContext.addState(LedState.MUSIC_PLAYING);
        }
    }
}
