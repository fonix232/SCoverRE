package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class IncomingCallLedStateController extends AbsLedStateController {
    private static final int CMD_LED_CALL_INCOMING = 2;
    private static final int CMD_LED_CALL_INCOMING_USER_ICON = 22;
    private static final int DEFAULT_PRIORITY = 1;
    private static final int MAX_PHONE_NUMBER_LENGTH_DISPLAY = 14;

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

    public byte[] getCommand(LedContext ledContext) {
        byte[] data;
        byte[] iconData = ledContext.getCallerData().getIconData();
        int callerId = ledContext.getCallerData().getCallerID();
        if (ledContext.getCallerData().isEmergencyModeIcon()) {
            data = new byte[]{(byte) 1, (byte) 1};
        } else if (iconData != null) {
            return composeCommand((byte) 22, getTimeoutByteVal(), iconData);
        } else {
            if (callerId > 0 && callerId < 55) {
                data = new byte[]{(byte) 2, (byte) callerId};
            } else if (ledContext.getCallerData().isKnownNumber()) {
                data = new byte[]{(byte) 1, (byte) 1};
            } else if (ledContext.getCallerData().isRestricted()) {
                data = new byte[]{(byte) 1, (byte) 2};
            } else {
                byte[] numberData = ledContext.getCallerData().getNumber().getBytes();
                int uxNumberDataLength = Math.min(numberData.length, 14);
                if (numberData.length <= 14) {
                    data = new byte[(uxNumberDataLength + 3)];
                    data[2] = (byte) uxNumberDataLength;
                } else {
                    data = new byte[((uxNumberDataLength + 3) + 1)];
                    data[2] = (byte) (uxNumberDataLength + 1);
                    data[data.length - 1] = Byte.MAX_VALUE;
                }
                data[0] = (byte) 3;
                data[1] = (byte) 0;
                System.arraycopy(numberData, 0, data, 3, uxNumberDataLength);
            }
        }
        return composeCommand((byte) 2, getTimeoutByteVal(), data);
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

    public LedState onBixbyActivated(LedContext ledContext) {
        super.onBixbyActivated(ledContext);
        ledContext.addState(QueueType.DELAYED, getControllerLedState());
        return LedState.BIXBY;
    }
}
