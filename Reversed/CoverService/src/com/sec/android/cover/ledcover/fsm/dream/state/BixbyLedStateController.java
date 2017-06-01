package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class BixbyLedStateController extends AbsLedStateController {
    private static final byte CMD_BIXBY = (byte) 25;
    private static final int DEFAULT_PRIORITY = 10;
    private static final int IDLE_LISTENER_TYPE = 6;

    protected LedState getControllerLedState() {
        return LedState.BIXBY;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case DELAYED:
                return -2147483638;
            default:
                return Integer.MAX_VALUE;
        }
    }

    public byte getCommandCodeByte() {
        return CMD_BIXBY;
    }

    public boolean isInfinite() {
        return true;
    }

    public byte[] getCommand(LedContext ledContext) {
        byte[] data = new byte[1];
        switch (ledContext.getBixbyState()) {
            case 0:
                data[0] = (byte) 5;
                break;
            case 1:
                data[0] = (byte) 1;
                break;
            case 2:
                data[0] = (byte) 2;
                break;
            case 3:
                data[0] = (byte) 3;
                break;
            case 4:
                data[0] = (byte) 4;
                break;
            default:
                return null;
        }
        return composeCommand(data);
    }

    public int getTouchEventListenerType() {
        return 6;
    }

    public LedState onIncomingCall(LedContext ledContext) {
        ledContext.addState(QueueType.DELAYED, LedState.BIXBY);
        return super.onIncomingCall(ledContext);
    }

    public LedState onBixbyActiveStateChanged(LedContext ledContext) {
        return LedState.BIXBY;
    }

    public LedState onBixbyDeactivated(LedContext ledContext) {
        super.onBixbyDeactivated(ledContext);
        return LedState.IDLE;
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        super.onNewMissedCall(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.NEW_MISSED_CALL);
        return null;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            ledContext.addState(QueueType.DELAYED, LedState.BIXBY);
            return LedState.ALARM;
        }
        ledContext.addState(QueueType.DELAYED, LedState.ALARM);
        return null;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            ledContext.addState(QueueType.DELAYED, LedState.BIXBY);
            return LedState.TIMER;
        }
        ledContext.addState(QueueType.DELAYED, LedState.TIMER);
        return null;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        if (ledContext.isNoCallStateDisplayed()) {
            ledContext.addState(QueueType.DELAYED, LedState.BIXBY);
            return LedState.CALENDAR;
        }
        ledContext.addState(QueueType.DELAYED, LedState.CALENDAR);
        return null;
    }

    public LedState onVolumeChange(LedContext ledContext) {
        return null;
    }

    public LedState onBatteryLow(LedContext ledContext) {
        if (ledContext.isNoCallStateDisplayed()) {
            ledContext.addState(QueueType.DELAYED, LedState.BIXBY);
            return LedState.BATTERY_LOW;
        }
        ledContext.addState(QueueType.DELAYED, LedState.BATTERY_LOW);
        return null;
    }

    public LedState onHeadsetPlugStateChanged(LedContext ledContext) {
        return null;
    }
}
