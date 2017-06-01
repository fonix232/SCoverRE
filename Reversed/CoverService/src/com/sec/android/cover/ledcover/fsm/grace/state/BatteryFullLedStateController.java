package com.sec.android.cover.ledcover.fsm.grace.state;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public class BatteryFullLedStateController extends AbsLedStateController {
    private static final long BATTERY_FULL_TIMEOUT = 4600;
    private static final int CMD_LED_BATTERY_FULL = 12;
    private static final String TAG = BatteryFullLedStateController.class.getSimpleName();

    protected LedState getControllerLedState() {
        return LedState.BATTERY_FULL;
    }

    public int getPriority(QueueType type) {
        return 24;
    }

    public byte[][] getCommand(LedContext ledContext) {
        int level = ledContext.getBatteryStatus().level;
        if (level != 100) {
            Log.w(TAG, "Battery is full but level is not 100%; level is: " + level);
        }
        byte[][] cmd = convertHexString(ledContext.getGraceLEDCoverCMD().getBatteryChargingData(level));
        this.mTimeout = scrapeTimeoutFromCommand(cmd);
        return cmd;
    }

    public byte getCommandCodeByte() {
        return (byte) 12;
    }

    protected long getDefaultTimeout() {
        return BATTERY_FULL_TIMEOUT;
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.IDLE;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        super.onAlarmStart(ledContext);
        return LedState.ALARM;
    }

    public LedState onTimerStart(LedContext ledContext) {
        super.onTimerStart(ledContext);
        return LedState.TIMER;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        super.onCalendarStart(ledContext);
        return LedState.CALENDAR;
    }

    public LedState onBatteryLow(LedContext ledContext) {
        return LedState.BATTERY_LOW;
    }

    public LedState onBatteryFull(LedContext ledContext) {
        return LedState.BATTERY_FULL;
    }

    public void onStateExit(LedContext ledContext) {
        if (ledContext.getBatteryStatus().isPluggedIn() && !ledContext.getBatteryStatus().isWirelssCharged()) {
            ledContext.addState(LedState.BATTERY_CHARGING);
        }
    }
}
