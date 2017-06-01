package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class IncomingCallLedStateController extends AbsLedStateController {
    private static final int CMD_LED_CALL_INCOMING = 2;
    private static final int DEFAULT_PRIORITY = 1;

    protected LedState getControllerLedState() {
        return LedState.INCOMING_CALL;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case POWER_BUTTON:
                return -2147483647;
            default:
                return 1;
        }
    }

    public byte[][] getCommand(LedContext ledContext) {
        if (ledContext.getCallerData().isDefault()) {
            return convertHexString(ledContext.getGraceLEDCoverCMD().getUnknownIncomingCallData(""));
        }
        return convertHexString(ledContext.getCallerData().getIconData());
    }

    public byte getCommandCodeByte() {
        return (byte) 2;
    }

    public boolean isInfinite() {
        return true;
    }

    public int getTouchEventListenerType() {
        return 0;
    }

    public boolean shouldWakeupForLedLamp() {
        return false;
    }

    public LedState onRejectCall(LedContext ledContext) {
        super.onRejectCall(ledContext);
        if (ledContext.getCallState() == 2) {
            return LedState.DURING_CALL;
        }
        return LedState.IDLE;
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        if (ledContext.getCallState() == 0) {
            return super.onNewMissedCall(ledContext);
        }
        ledContext.addState(QueueType.DELAYED, LedState.NEW_MISSED_CALL);
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

    public LedState onMusicPlay(LedContext ledContext) {
        super.onMusicPlay(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.MUSIC_PLAYING);
        return null;
    }

    public LedState onHeadsetPlugStateChanged(LedContext ledContext) {
        return null;
    }

    public LedState onVolumeChange(LedContext ledContext) {
        return null;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.ALARM);
        return null;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.TIMER);
        return null;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.CALENDAR);
        return null;
    }
}
