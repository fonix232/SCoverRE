package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class BatteryLowLedStateController extends AbsLedStateController {
    private static final int CMD_LED_BATTERY_LOW = 11;

    protected LedState getControllerLedState() {
        return LedState.BATTERY_LOW;
    }

    public int getPriority(QueueType type) {
        return 23;
    }

    public byte[][] getCommand(LedContext ledContext) {
        byte[][] cmd = convertHexString(ledContext.getGraceLEDCoverCMD().getBatteryAlertData());
        this.mTimeout = scrapeTimeoutFromCommand(cmd);
        return cmd;
    }

    public byte getCommandCodeByte() {
        return (byte) 11;
    }

    public LedState onIncomingCall(LedContext ledContext) {
        ledContext.addState(QueueType.DELAYED, LedState.BATTERY_LOW);
        return super.onIncomingCall(ledContext);
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.BATTERY_LOW);
        return LedState.ALARM;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.BATTERY_LOW);
        return LedState.TIMER;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.BATTERY_LOW);
        return LedState.CALENDAR;
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.IDLE;
    }

    public LedState onChargerConnected(LedContext ledContext) {
        super.onChargerConnected(ledContext);
        return LedState.BATTERY_CHARGING;
    }

    public LedState onBatteryFull(LedContext ledContext) {
        return LedState.BATTERY_FULL;
    }
}
