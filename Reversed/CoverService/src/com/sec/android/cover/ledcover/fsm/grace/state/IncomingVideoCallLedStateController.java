package com.sec.android.cover.ledcover.fsm.grace.state;

import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;

public class IncomingVideoCallLedStateController extends IncomingCallLedStateController {
    private static final int CMD_LED_VIDEO_CALL_INCOMING = 20;

    protected LedState getControllerLedState() {
        return LedState.INCOMING_VIDEO_CALL;
    }

    public byte[][] getCommand(LedContext ledContext) {
        return convertHexString(ledContext.getGraceLEDCoverCMD().getVideoCallData());
    }

    public int getTouchEventListenerType() {
        return -1;
    }

    public byte getCommandCodeByte() {
        return (byte) 20;
    }
}
