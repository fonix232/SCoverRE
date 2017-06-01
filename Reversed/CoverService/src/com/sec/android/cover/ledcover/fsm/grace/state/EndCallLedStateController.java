package com.sec.android.cover.ledcover.fsm.grace.state;

import android.util.Log;
import com.sec.android.cover.ledcover.GraceLEDCoverCMD;
import com.sec.android.cover.ledcover.fsm.grace.AbsLedStateController;
import com.sec.android.cover.ledcover.fsm.grace.LedContext;
import com.sec.android.cover.ledcover.fsm.grace.LedState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EndCallLedStateController extends AbsLedStateController {
    private static final int CMD_LED_CALL_END = 4;
    private static final int DEFAULT_PRIORITY = 3;
    private static final String TAG = EndCallLedStateController.class.getSimpleName();

    protected LedState getControllerLedState() {
        return LedState.END_CALL;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case POWER_BUTTON:
                return -2147483645;
            default:
                return 3;
        }
    }

    public byte[][] getCommand(LedContext ledContext) {
        Exception e;
        if (ledContext.getCallDuration() == null) {
            return (byte[][]) null;
        }
        SimpleDateFormat sdf;
        String strCallTime = ledContext.getCallDuration();
        if (strCallTime.length() == 5) {
            sdf = new SimpleDateFormat("mm:ss");
        } else {
            sdf = new SimpleDateFormat("kk:mm:ss");
        }
        try {
            int minute;
            int second;
            Date callTime = sdf.parse(strCallTime);
            if (strCallTime.length() > 5) {
                minute = callTime.getHours();
                second = callTime.getMinutes();
            } else {
                minute = callTime.getMinutes();
                second = callTime.getSeconds();
            }
            Log.d(TAG, "processCallTime callTime=" + String.valueOf(callTime) + " getEndCallData(" + minute + ", " + second + ")");
            byte[][] cmd = convertHexString(new GraceLEDCoverCMD().getEndCallData(minute, second));
            this.mTimeout = scrapeTimeoutFromCommand(cmd);
            return cmd;
        } catch (IllegalArgumentException e2) {
            e = e2;
            Log.e(TAG, "cannot parse strCallTime", e);
            return (byte[][]) null;
        } catch (ParseException e3) {
            e = e3;
            Log.e(TAG, "cannot parse strCallTime", e);
            return (byte[][]) null;
        }
    }

    public byte getCommandCodeByte() {
        return (byte) 4;
    }

    public boolean isDataReady(LedContext ledContext) {
        return ledContext.getCallDuration() != null && ledContext.getCallState() == 0;
    }

    public boolean shouldWakeupForLedLamp() {
        return false;
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.IDLE;
    }

    public LedState onEndCall(LedContext ledContext) {
        LedState returnState = super.onEndCall(ledContext);
        if (isDataReady(ledContext)) {
            return returnState;
        }
        Log.e(TAG, "No valid call end data received, ignore call end");
        return LedState.IDLE;
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        super.onNewMissedCall(ledContext);
        ledContext.addState(QueueType.DELAYED, LedState.NEW_MISSED_CALL);
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
}
