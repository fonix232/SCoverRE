package com.sec.android.cover.ledcover.fsm.dream.state;

import android.os.SystemClock;
import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class DuringCallLedStateController extends AbsLedStateController {
    private static final int CMD_LED_CALL_INPROGRESS = 3;
    private static final int DEFAULT_PRIORITY = 2;
    private static final String TAG = DuringCallLedStateController.class.getSimpleName();

    protected LedState getControllerLedState() {
        return LedState.DURING_CALL;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case POWER_BUTTON:
                return -2147483646;
            default:
                return 2;
        }
    }

    public byte[] getCommand(LedContext ledContext) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long callDurationSeconds = (elapsedRealtime - ledContext.getCallStartTime()) / 1000;
        int minute = (int) (callDurationSeconds / 60);
        Log.d(TAG, "elapsedRealtime=" + elapsedRealtime + " startTime=" + ledContext.getCallStartTime() + " getDuringCallData(" + minute + ", " + ((int) (callDurationSeconds % 60)) + ")");
        byte minuteByte0 = (byte) ((minute >> 8) & 255);
        byte minuteByte1 = (byte) (minute & 255);
        return composeCommand((byte) 3, (byte) 0, new byte[]{minuteByte0, minuteByte1, (byte) second});
    }

    public LedState onTimeTick(LedContext ledContext) {
        Log.i(TAG, "onTimeTick");
        return LedState.DURING_CALL_TIME_UPDATE;
    }

    public byte getCommandCodeByte() {
        return (byte) 3;
    }

    public boolean isDataReady(LedContext ledContext) {
        if (ledContext.getPrevLedState() == LedState.VOLUME || ledContext.getCallStartTime() == -1 || ledContext.getCallState() != 2) {
            return false;
        }
        return true;
    }

    public boolean shouldWakeupForLedLamp() {
        return false;
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        super.onNewMissedCall(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.NEW_MISSED_CALL);
        return null;
    }

    public LedState onVolumeChange(LedContext ledContext) {
        return LedState.VOLUME;
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
