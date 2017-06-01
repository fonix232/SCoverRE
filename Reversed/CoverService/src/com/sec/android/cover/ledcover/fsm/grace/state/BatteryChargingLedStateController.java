package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class BatteryChargingLedStateController extends AbsLedStateController {
    private static final long BATTERY_CHARGING_TIMEOUT = 4600;
    private static final int CMD_LED_BATTERY_CHARGING = 12;

    protected LedState getControllerLedState() {
        return LedState.BATTERY_CHARGING;
    }

    public int getPriority(QueueType type) {
        int i = C00551.f26xdfcb6abe[type.ordinal()];
        return 60;
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case COVER_CLOSE:
            case POWER_BUTTON:
            case MAIN:
                return true;
            default:
                return false;
        }
    }

    public byte[][] getCommand(LedContext ledContext) {
        byte[][] cmd = convertHexString(ledContext.getGraceLEDCoverCMD().getBatteryChargingData(ledContext.getBatteryStatus().level));
        this.mTimeout = scrapeTimeoutFromCommand(cmd);
        return cmd;
    }

    public byte getCommandCodeByte() {
        return (byte) 12;
    }

    protected long getDefaultTimeout() {
        return BATTERY_CHARGING_TIMEOUT;
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

    public LedState onCableWithWirelessConnected(LedContext ledContext) {
        super.onCableWithWirelessConnected(ledContext);
        return LedState.IDLE;
    }

    public void onStateExit(LedContext ledContext) {
        if (ledContext.getBatteryStatus().isPluggedIn() && !ledContext.getBatteryStatus().isWirelssCharged()) {
            ledContext.addState(LedState.BATTERY_CHARGING);
        }
    }
}
