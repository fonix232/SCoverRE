package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;
import com.sec.android.cover.ledcover.fsm.dream.missedevent.MissedEvent;

public class NewMissedCallLedStateController extends EndCallLedStateController {
    protected LedState getControllerLedState() {
        return LedState.NEW_MISSED_CALL;
    }

    public int getPriority(QueueType type) {
        return 11;
    }

    public byte[] getCommand(LedContext ledContext) {
        MissedEvent event = ledContext.getMissedCallEvent();
        if (event == null) {
            return null;
        }
        return composeCommand(event.getEventLedCommand(), getTimeoutByteVal(), event.getEventLedData());
    }

    public byte getCommandCodeByte() {
        return (byte) 0;
    }

    public LedState onTimeout(LedContext ledContext) {
        if (ledContext.getMissedCallEvent() == null) {
            return null;
        }
        return LedState.IDLE;
    }

    public boolean isDataReady(LedContext ledContext) {
        if (ledContext.getMissedCallEvent() != null) {
            return true;
        }
        return false;
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        return LedState.NEW_MISSED_CALL;
    }

    public LedState onRejectCall(LedContext ledContext) {
        super.onRejectCall(ledContext);
        return LedState.IDLE;
    }

    public LedState onBixbyActivated(LedContext ledContext) {
        super.onBixbyActivated(ledContext);
        return LedState.BIXBY;
    }

    public void onStateExit(LedContext ledContext) {
        if (!ledContext.getMissedEvents().isEmpty()) {
            ledContext.addState(LedState.MISSED_EVENT);
        }
        ledContext.setMissedCallEvent(null);
    }
}
