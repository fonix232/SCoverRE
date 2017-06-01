package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class MusicPausedLedStateController extends AbsLedStateController {
    private static final int CMD_LED_MUSIC = 13;
    private static final long MUSIC_TIMEOUT = 9200;

    protected LedState getControllerLedState() {
        return LedState.MUSIC_PAUSED;
    }

    public int getPriority(QueueType type) {
        return 40;
    }

    public byte[][] getCommand(LedContext ledContext) {
        return convertHexString(ledContext.getGraceLEDCoverCMD().getMusicData(ledContext.isHeadsetPlugged() ? 4 : 2));
    }

    public byte getCommandCodeByte() {
        return (byte) 13;
    }

    public long getDefaultTimeout() {
        return MUSIC_TIMEOUT;
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.IDLE;
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
}
