package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class NewMissedCallLedStateController extends EndCallLedStateController {
    private static final int CMD_LED_NEW_MISSED_CALL = 9;

    protected LedState getControllerLedState() {
        return LedState.NEW_MISSED_CALL;
    }

    public int getPriority(QueueType type) {
        return 11;
    }

    public byte[][] getCommand(LedContext ledContext) {
        byte[][] cmd = convertHexString(ledContext.getGraceLEDCoverCMD().getMissedEventData(ledContext.getMissedCallsCount(), 0));
        this.mTimeout = scrapeTimeoutFromCommand(cmd);
        return cmd;
    }

    public byte getCommandCodeByte() {
        return (byte) 9;
    }

    public boolean isDataReady(LedContext ledContext) {
        return true;
    }

    public void onStateExit(LedContext ledContext) {
        if (ledContext.getMissedCallsCount() > 0) {
            ledContext.addState(LedState.MISSED_EVENT);
        }
    }
}
