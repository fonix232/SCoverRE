package com.sec.android.cover.ledcover.fsm.dream.state;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;
import com.sec.android.cover.ledcover.fsm.dream.missedevent.MissedEvent;

public class MissedEventLedStateController extends AbsLedStateController {
    private static final int CMD_LED_MISSED_EVENT_INDICATOR = 16;
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
                return !ledContext.isUPSMEnabled();
            case MAIN:
                return true;
            default:
                return false;
        }
    }

    public byte getCommandCodeByte() {
        if (this.mCurrentMissedEvent == null) {
            return MissedEvent.CMD_LED_DEFAULT_NOTIFICATION;
        }
        return this.mCurrentMissedEvent.getEventLedCommand();
    }

    public byte[] getCommand(LedContext ledContext) {
        Log.d(TAG, "getCommand : mCurrentMissedEvent=" + String.valueOf(this.mCurrentMissedEvent));
        MissedEvent currentMissedEvent = this.mCurrentMissedEvent;
        if (currentMissedEvent != null) {
            return composeCommand(currentMissedEvent.getEventLedCommand(), getTimeoutByteVal(), currentMissedEvent.getEventLedData());
        }
        Log.e(TAG, "Error: Missed event state without notifications to display!");
        return null;
    }

    public long getTimeout() {
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

    public LedState onMissedEventCleared(LedContext ledContext) {
        super.onMissedEventCleared(ledContext);
        this.mCurrentMissedEvent = null;
        return LedState.IDLE;
    }

    public LedState onBixbyActivated(LedContext ledContext) {
        super.onBixbyActivated(ledContext);
        return LedState.BIXBY;
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
