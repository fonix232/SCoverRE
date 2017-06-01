package com.sec.android.cover.ledcover.fsm.grace.state;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;
import com.sec.android.cover.ledcover.fsm.grace.missedevent.MissedEvent;
import com.sec.android.cover.ledcover.fsm.grace.missedevent.MissedEvent.EventType;
import java.util.List;

public class MissedEventLedStateController extends AbsLedStateController {
    private static final int CMD_LED_CUSTOM_NOTIFICATION = 16;
    private static final long MISSED_EVENT_TIMEOUT = 3700;
    private static final String TAG = MissedEventLedStateController.class.getSimpleName();
    private MissedEvent mCurrentMissedEvent;

    protected LedState getControllerLedState() {
        return LedState.MISSED_EVENT;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case COVER_CLOSE:
            case POWER_BUTTON:
                return 40;
            default:
                return 30;
        }
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

    public byte getCommandCodeByte() {
        return com.sec.android.cover.ledcover.fsm.dream.missedevent.MissedEvent.CMD_LED_DEFAULT_NOTIFICATION;
    }

    public byte[][] getCommand(LedContext ledContext) {
        if (this.mCurrentMissedEvent == null) {
            Log.e(TAG, "Error: Missed event state without notifications to display!");
            this.mTimeout = 0;
            return (byte[][]) null;
        }
        Log.d(TAG, "getCommand : mCurrentMissedEvent=" + String.valueOf(this.mCurrentMissedEvent));
        List<String> data = this.mCurrentMissedEvent.getMissedEventLedData(ledContext.getGraceLEDCoverCMD());
        if (data == null && this.mCurrentMissedEvent.getType() == EventType.CUSTOM_NOTIFICATION) {
            this.mTimeout = MISSED_EVENT_TIMEOUT;
            return composeCommand(getCommandCodeByte(), (byte) 0, new byte[]{(byte) 0});
        }
        byte[][] cmd = convertHexString(data);
        this.mTimeout = scrapeTimeoutFromCommand(cmd);
        return cmd;
    }

    protected long getDefaultTimeout() {
        return MISSED_EVENT_TIMEOUT;
    }

    public LedState onTimeout(LedContext ledContext) {
        this.mCurrentMissedEvent = ledContext.getMissedEvents().pollCurrentMissedEventFromQueue();
        if (this.mCurrentMissedEvent != null) {
            return LedState.MISSED_EVENT;
        }
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
        this.mCurrentMissedEvent = null;
        return LedState.NEW_MISSED_EVENT;
    }

    public LedState onCustomNotificationRemoved(LedContext ledContext) {
        if (ledContext.getMissedEvents().hasMissedEvent(this.mCurrentMissedEvent)) {
            return null;
        }
        this.mCurrentMissedEvent = ledContext.getMissedEvents().pollCurrentMissedEventFromQueue();
        if (this.mCurrentMissedEvent != null) {
            return LedState.MISSED_EVENT;
        }
        return LedState.IDLE;
    }

    public void onStateEnter(LedContext ledContext) {
        super.onStateEnter(ledContext);
        this.mCurrentMissedEvent = ledContext.getMissedEvents().pollCurrentMissedEventFromQueue();
    }

    public void onStateExit(LedContext ledContext) {
        this.mCurrentMissedEvent = null;
        if (!ledContext.getMissedEvents().isEmpty()) {
            ledContext.addState(LedState.MISSED_EVENT);
        }
        if (ledContext.isPowerButtonWakeUp() && !ledContext.getMissedEvents().isCurrentQueueEmpty()) {
            ledContext.addState(QueueType.POWER_BUTTON, LedState.MISSED_EVENT);
        }
        if (ledContext.isCoverClosedWakeUp() && !ledContext.getMissedEvents().isCurrentQueueEmpty()) {
            ledContext.addState(QueueType.COVER_CLOSE, LedState.MISSED_EVENT);
        }
    }
}
