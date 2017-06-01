package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class TimerLedStateController extends AbsLedStateController {
    private static final int CMD_LED_ALARM = 6;
    private static final int DEFAULT_PRIORITY = 21;
    private static final long TIMER_TIMEOUT = -1;

    protected LedState getControllerLedState() {
        return LedState.TIMER;
    }

    public int getPriority(QueueType type) {
        int i = C00631.f34xdfcb6abe[type.ordinal()];
        return 21;
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case MAIN:
                return true;
            default:
                return false;
        }
    }

    public byte[][] getCommand(LedContext ledContext) {
        return convertHexString(ledContext.getGraceLEDCoverCMD().getTimerData());
    }

    public byte getCommandCodeByte() {
        return (byte) 6;
    }

    public long getDefaultTimeout() {
        return -1;
    }

    public byte getTimeoutByteVal() {
        return (byte) -1;
    }

    public int getTouchEventListenerType() {
        return 2;
    }

    public boolean shouldWakeupForLedLamp() {
        return false;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        return LedState.ALARM;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        return LedState.CALENDAR;
    }

    public LedState onTimerStop(LedContext ledContext) {
        super.onTimerStop(ledContext);
        return LedState.IDLE;
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

    public void onStateExit(LedContext ledContext) {
        if (ledContext.isTimer()) {
            ledContext.addState(LedState.TIMER);
        }
    }
}
