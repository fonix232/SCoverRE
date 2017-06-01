package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;

public class IncomingVideoCallLedStateController extends IncomingCallLedStateController {
    protected LedState getControllerLedState() {
        return LedState.INCOMING_VIDEO_CALL;
    }

    public byte[] getCommand(LedContext ledContext) {
        return composeCommand(new byte[]{(byte) 1, (byte) 3});
    }

    public int getTouchEventListenerType() {
        return -1;
    }
}
