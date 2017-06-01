package com.sec.android.cover.ledcover.fsm.dream;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.VoiceRecorderState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public abstract class AbsLedStateController implements LedStateController {
    private static final byte[] COMMAND_MAGICAL_PREFIX = new byte[]{(byte) 0, (byte) -95, (byte) 0, (byte) 0};
    private static final long DEFAULT_TIMEOUT = 3700;
    protected static final long INFINITE_TIMEOUT = -1;
    private static final String TAG = AbsLedStateController.class.getSimpleName();

    public abstract byte getCommandCodeByte();

    protected abstract LedState getControllerLedState();

    public long getTimeout() {
        return DEFAULT_TIMEOUT;
    }

    public byte[] getCommand(LedContext ledContext) {
        return composeCommand(null);
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case MAIN:
                return true;
            default:
                return false;
        }
    }

    public boolean isInfinite() {
        if (getTimeout() == -1) {
            return true;
        }
        return false;
    }

    public boolean isContinuation() {
        return false;
    }

    public boolean isDataReady(LedContext ledContext) {
        return true;
    }

    public int getTouchEventListenerType() {
        return -1;
    }

    public boolean shouldWakeupForLedLamp() {
        return true;
    }

    protected byte getTimeoutByteVal() {
        return (byte) 0;
    }

    protected final byte[] composeCommand(byte[] data) {
        return composeCommand(getCommandCodeByte(), getTimeoutByteVal(), data);
    }

    protected final byte[] composeCommand(byte commandCode, byte timeout, byte[] data) {
        if (data == null) {
            data = new byte[]{(byte) 0};
        }
        int ledPacketLength = getLenByteValue(data);
        int size = 5 + ledPacketLength;
        byte[] genData = new byte[(5 + ledPacketLength)];
        genData[0] = COMMAND_MAGICAL_PREFIX[0];
        genData[1] = COMMAND_MAGICAL_PREFIX[1];
        genData[2] = COMMAND_MAGICAL_PREFIX[2];
        genData[3] = COMMAND_MAGICAL_PREFIX[3];
        genData[4] = (byte) ledPacketLength;
        genData[5] = (byte) ledPacketLength;
        genData[6] = commandCode;
        genData[7] = timeout;
        System.arraycopy(data, 0, genData, 8, data.length);
        return genData;
    }

    private int getLenByteValue(byte[] data) {
        if (data != null) {
            return 3 + data.length;
        }
        return 3 + 1;
    }

    public LedState onIncomingCall(LedContext ledContext) {
        ledContext.removeState(LedState.DURING_CALL);
        if (ledContext.isVideoCall()) {
            ledContext.addState(LedState.INCOMING_VIDEO_CALL);
            return LedState.INCOMING_VIDEO_CALL;
        }
        ledContext.addState(LedState.INCOMING_CALL);
        return LedState.INCOMING_CALL;
    }

    public LedState onRejectCall(LedContext ledContext) {
        ledContext.removeState(LedState.INCOMING_VIDEO_CALL);
        ledContext.removeState(LedState.INCOMING_CALL);
        return null;
    }

    public LedState onAcceptCall(LedContext ledContext) {
        ledContext.addState(LedState.DURING_CALL);
        ledContext.removeState(LedState.INCOMING_VIDEO_CALL);
        ledContext.removeState(LedState.INCOMING_CALL);
        return LedState.DURING_CALL;
    }

    public LedState onEndCall(LedContext ledContext) {
        if (ledContext.getCallState() != 0) {
            return null;
        }
        ledContext.removeState(LedState.INCOMING_VIDEO_CALL);
        ledContext.removeState(LedState.INCOMING_CALL);
        ledContext.removeState(LedState.DURING_CALL);
        return LedState.END_CALL;
    }

    public LedState onNewMissedCall(LedContext ledContext) {
        ledContext.addState(LedState.MISSED_EVENT);
        ledContext.removeState(LedState.INCOMING_VIDEO_CALL);
        ledContext.removeState(LedState.INCOMING_CALL);
        return LedState.NEW_MISSED_CALL;
    }

    public LedState onAlarmStart(LedContext ledContext) {
        ledContext.addState(LedState.ALARM);
        return null;
    }

    public LedState onAlarmStop(LedContext ledContext) {
        ledContext.removeState(LedState.ALARM);
        return null;
    }

    public LedState onTimerStart(LedContext ledContext) {
        ledContext.addState(LedState.TIMER);
        return null;
    }

    public LedState onTimerStop(LedContext ledContext) {
        ledContext.removeState(LedState.TIMER);
        return null;
    }

    public LedState onCalendarStart(LedContext ledContext) {
        ledContext.addState(LedState.CALENDAR);
        return null;
    }

    public LedState onCalendarStop(LedContext ledContext) {
        ledContext.removeState(LedState.CALENDAR);
        return null;
    }

    public LedState onMusicPlay(LedContext ledContext) {
        ledContext.addState(LedState.MUSIC_PLAYING);
        ledContext.addState(LedState.MUSIC_CONTROLLER);
        ledContext.removeState(LedState.MUSIC_PAUSED);
        return null;
    }

    public LedState onMusicPause(LedContext ledContext) {
        ledContext.removeState(LedState.MUSIC_PLAYING);
        ledContext.removeState(LedState.MUSIC_CONTROLLER);
        ledContext.removeState(LedState.MUSIC_PAUSED);
        return null;
    }

    public LedState onMusicStop(LedContext ledContext) {
        ledContext.removeState(LedState.MUSIC_PLAYING);
        ledContext.removeState(LedState.MUSIC_CONTROLLER);
        ledContext.removeState(LedState.MUSIC_PAUSED);
        return null;
    }

    public LedState onVolumeChange(LedContext ledContext) {
        if (ledContext.getMusicState() == MusicState.PLAY || ledContext.getVoiceRecorderState() == VoiceRecorderState.PLAY) {
            return LedState.VOLUME;
        }
        return null;
    }

    public LedState onBatteryLow(LedContext ledContext) {
        return null;
    }

    public LedState onBatteryFull(LedContext ledContext) {
        return null;
    }

    public LedState onChargerConnected(LedContext ledContext) {
        ledContext.addState(LedState.BATTERY_CHARGING);
        return null;
    }

    public LedState onChargerDisconnected(LedContext ledContext) {
        ledContext.removeState(LedState.BATTERY_CHARGING);
        return null;
    }

    public LedState onTimeout(LedContext ledContext) {
        return null;
    }

    public LedState onVoiceRecorderStart(LedContext ledContext) {
        ledContext.addState(LedState.VOICE_RECORDER_RECORDING);
        ledContext.removeState(LedState.VOICE_RECORDER_PLAYING);
        return null;
    }

    public LedState onVoiceRecorderPlay(LedContext ledContext) {
        ledContext.addState(LedState.VOICE_RECORDER_PLAYING);
        ledContext.removeState(LedState.VOICE_RECORDER_RECORDING);
        return null;
    }

    public LedState onVoiceRecorderStop(LedContext ledContext) {
        ledContext.removeState(LedState.VOICE_RECORDER_PLAYING);
        ledContext.removeState(LedState.VOICE_RECORDER_RECORDING);
        return null;
    }

    public LedState onHeadsetPlugStateChanged(LedContext ledContext) {
        if (ledContext.isHeadsetPlugged() && ledContext.getMusicState() == MusicState.PLAY && ledContext.hasState(LedState.MUSIC_PLAYING)) {
            return LedState.MUSIC_PLAYING;
        }
        return null;
    }

    public LedState onMissedEventCleared(LedContext ledContext) {
        ledContext.removeState(LedState.MISSED_EVENT);
        ledContext.removeState(LedState.NEW_MISSED_EVENT);
        ledContext.removeState(LedState.NEW_MISSED_CALL);
        return null;
    }

    public LedState onCustomNotificationAdded(LedContext ledContext) {
        ledContext.addState(LedState.MISSED_EVENT);
        ledContext.addState(QueueType.DELAYED, LedState.NEW_MISSED_EVENT);
        return null;
    }

    public LedState onCustomNotificationAddedSilently(LedContext ledContext) {
        ledContext.addState(LedState.MISSED_EVENT);
        return null;
    }

    public LedState onCustomNotificationRemoved(LedContext ledContext) {
        if (ledContext.getMissedEvents().isEmpty()) {
            ledContext.removeState(LedState.MISSED_EVENT);
        }
        return null;
    }

    public LedState onLedLampNoti(LedContext ledContext) {
        if (shouldWakeupForLedLamp()) {
            return LedState.LED_LAMP_NOTI;
        }
        return null;
    }

    public LedState onMediaKeyPlayPause(LedContext ledContext) {
        return null;
    }

    public void onStateEnter(LedContext ledContext) {
        if (ledContext.hasState(QueueType.DELAYED, getControllerLedState())) {
            ledContext.removeState(QueueType.DELAYED, getControllerLedState());
        }
    }

    public void onStateExit(LedContext ledContext) {
    }

    public void onSendingState(LedContext ledContext) {
        if (ledContext.hasState(QueueType.DELAYED, getControllerLedState())) {
            ledContext.removeState(QueueType.DELAYED, getControllerLedState());
        }
    }

    public LedState onCableWithWirelessDisConnected(LedContext ledContext) {
        Log.i(TAG, "onCableWithWirelessDisConnected");
        if (ledContext.getBatteryStatus().isPluggedIn()) {
            ledContext.addState(LedState.BATTERY_CHARGING);
        }
        return null;
    }

    public LedState onCableWithWirelessConnected(LedContext ledContext) {
        Log.i(TAG, "onCableWithWirelessConnected");
        ledContext.removeState(LedState.BATTERY_CHARGING);
        return null;
    }

    public LedState onTimeTick(LedContext ledContext) {
        return null;
    }

    public LedState onBixbyActivated(LedContext ledContext) {
        ledContext.addState(LedState.BIXBY);
        return null;
    }

    public LedState onBixbyDeactivated(LedContext ledContext) {
        ledContext.removeState(LedState.BIXBY);
        return null;
    }

    public LedState onBixbyActiveStateChanged(LedContext ledContext) {
        return null;
    }
}
