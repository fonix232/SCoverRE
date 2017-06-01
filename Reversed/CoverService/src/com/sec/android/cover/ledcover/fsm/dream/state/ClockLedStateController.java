package com.sec.android.cover.ledcover.fsm.dream.state;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;
import java.util.Calendar;

public class ClockLedStateController extends AbsLedStateController {
    private static final int CMD_LED_CLOCK = 15;
    private static final String TAG = ClockLedStateController.class.getSimpleName();

    protected LedState getControllerLedState() {
        return LedState.CLOCK;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case POWER_BUTTON:
                return 0;
            default:
                return Integer.MAX_VALUE;
        }
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case POWER_BUTTON:
            case COVER_CLOSE:
            case MAIN:
                return true;
            default:
                return false;
        }
    }

    public byte[] getCommand(LedContext ledContext) {
        long curTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(curTime);
        int hours = c.get(11);
        int minutes = c.get(12);
        if (!ledContext.is24HourFormat()) {
            hours %= 12;
            if (hours == 0) {
                hours = 12;
            }
        }
        Log.d(TAG, "getClockData(" + hours + ", " + minutes + ")");
        return composeCommand(new byte[]{(byte) hours, (byte) minutes});
    }

    public byte getCommandCodeByte() {
        return (byte) 15;
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.IDLE;
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

    public LedState onBixbyActivated(LedContext ledContext) {
        super.onBixbyActivated(ledContext);
        return LedState.BIXBY;
    }

    public void onStateExit(LedContext ledContext) {
        ledContext.addState(LedState.CLOCK);
    }
}
