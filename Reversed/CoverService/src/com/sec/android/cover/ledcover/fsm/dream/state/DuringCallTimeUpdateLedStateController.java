package com.sec.android.cover.ledcover.fsm.dream.state;

import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;

public class DuringCallTimeUpdateLedStateController extends DuringCallLedStateController {
    public boolean isContinuation() {
        return true;
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.DURING_CALL;
    }
}
