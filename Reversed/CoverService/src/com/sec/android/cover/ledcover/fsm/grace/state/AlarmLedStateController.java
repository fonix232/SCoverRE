package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class AlarmLedStateController extends AbsLedStateController {
    private static final long ALARM_TIMEOUT = -1;
    private static final int CMD_LED_ALARM = 6;
    private static final int DEFAULT_PRIORITY = 20;

    protected LedState getControllerLedState() {
        return LedState.ALARM;
    }

    public int getPriority(QueueType type) {
        int i = C00541.f25xdfcb6abe[type.ordinal()];
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

    public byte[][] getCommand(LedContext ledContext) {
        return convertHexString(ledContext.getGraceLEDCoverCMD().getAlarmData());
    }

    public byte getCommandCodeByte() {
        return (byte) 6;
    }

    protected long getDefaultTimeout() {
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

    public void onStateExit(LedContext ledContext) {
        if (ledContext.isAlarm()) {
            ledContext.addState(LedState.ALARM);
        }
    }
}
