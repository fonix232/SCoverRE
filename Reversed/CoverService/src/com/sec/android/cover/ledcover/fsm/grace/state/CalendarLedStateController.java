package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class CalendarLedStateController extends AbsLedStateController {
    private static final long CALENDAR_TIMEOUT = -1;
    private static final int CMD_LED_CALENDAR = 6;
    private static final int DEFAULT_PRIORITY = 22;

    protected LedState getControllerLedState() {
        return LedState.CALENDAR;
    }

    public int getPriority(QueueType type) {
        int i = C00561.f27xdfcb6abe[type.ordinal()];
        return 22;
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
        return convertHexString(ledContext.getGraceLEDCoverCMD().getAlarmData());
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
        return 3;
    }

    public boolean shouldWakeupForLedLamp() {
        return false;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        return LedState.ALARM;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        return LedState.TIMER;
    }

    public LedState onCalendarStop(LedContext ledContext) {
        super.onCalendarStop(ledContext);
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
        if (ledContext.isCalendar()) {
            ledContext.addState(LedState.CALENDAR);
        }
    }
}
