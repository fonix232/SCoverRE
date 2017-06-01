package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class AlarmLedStateController extends AbsLedStateController {
    private static final int ALARM_DATA_VALUE = 1;
    private static final long ALARM_TIMEOUT = -1;
    private static final int CMD_LED_ALARM = 6;
    private static final int DEFAULT_PRIORITY = 20;

    protected LedState getControllerLedState() {
        return LedState.ALARM;
    }

    public int getPriority(QueueType type) {
        int i = C00331.f5xb4b54709[type.ordinal()];
        return 20;
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case MAIN:
                return true;
            default:
                return false;
        }
    }

    public byte[] getCommand(LedContext ledContext) {
        return composeCommand(new byte[]{(byte) 1});
    }

    public byte getCommandCodeByte() {
        return (byte) 6;
    }

    public long getTimeout() {
        return -1;
    }

    public byte getTimeoutByteVal() {
        return (byte) -1;
    }

    public int getTouchEventListenerType() {
        return 1;
    }

    public boolean shouldWakeupForLedLamp() {
        return false;
    }

    public LedState onAlarmStop(LedContext ledContext) {
        super.onAlarmStop(ledContext);
        return LedState.IDLE;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        return LedState.TIMER;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        return LedState.CALENDAR;
    }

    public LedState onVolumeChange(LedContext ledContext) {
        return null;
    }

    public LedState onBatteryLow(LedContext ledContext) {
        ledContext.addState(QueueType.DELAYED, LedState.BATTERY_LOW);
        return null;
    }

    public LedState onBatteryFull(LedContext ledContext) {
        ledContext.addState(QueueType.DELAYED, LedState.BATTERY_FULL);
        return null;
    }

    public LedState onBixbyActivated(LedContext ledContext) {
        super.onBixbyActivated(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.ALARM);
        return LedState.BIXBY;
    }

    public void onStateExit(LedContext ledContext) {
        if (ledContext.isAlarm()) {
            ledContext.addState(LedState.ALARM);
        }
    }
}
