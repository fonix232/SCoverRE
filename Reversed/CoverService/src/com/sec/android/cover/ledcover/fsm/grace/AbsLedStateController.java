package com.sec.android.cover.ledcover.fsm.grace;

import android.text.TextUtils;
import android.util.Log;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.VoiceRecorderState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;
import java.util.List;

public abstract class AbsLedStateController implements LedStateController {
    private static final byte[] COMMAND_MAGICAL_PREFIX = new byte[]{(byte) 0, (byte) -94, (byte) 0, (byte) 0};
    private static final long DEFAULT_TIMEOUT = 3700;
    protected static final long INFINITE_TIMEOUT = -1;
    protected static final long MANDATORY_OVERHEAD = 200;
    private static final String TAG = AbsLedStateController.class.getSimpleName();
    protected long mTimeout = getDefaultTimeout();

    public abstract byte getCommandCodeByte();

    protected abstract LedState getControllerLedState();

    protected long getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    public final long getTimeout() {
        return this.mTimeout;
    }

    public byte[][] getCommand(LedContext ledContext) {
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
        if (this.mTimeout == -1) {
            return true;
        }
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
        if (isInfinite()) {
            return (byte) -1;
        }
        return (byte) ((int) (getTimeout() / 100));
    }

    protected byte[][] composeCommand(byte[] data) {
        return composeCommand(getCommandCodeByte(), getTimeoutByteVal(), data);
    }

    protected byte[][] composeCommand(byte commandCode, byte timeout, byte[] data) {
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
        int i = size - 1;
        genData[size - 2] = (byte) -1;
        genData[i] = (byte) -1;
        return new byte[][]{genData};
    }

    private int getLenByteValue(byte[] data) {
        if (data != null) {
            return 5 + data.length;
        }
        return 5 + 1;
    }

    protected final byte[][] convertHexString(List<String> byteStrings) {
        if (byteStrings == null || byteStrings.isEmpty()) {
            return (byte[][]) null;
        }
        byte[][] command = new byte[byteStrings.size()][];
        int i = 0;
        while (i < byteStrings.size()) {
            if (TextUtils.isEmpty((CharSequence) byteStrings.get(i))) {
                return (byte[][]) null;
            }
            try {
                byte[] segment = getBytesFromHexString((String) byteStrings.get(i));
                byte[] prefixedSegment = new byte[((segment.length + COMMAND_MAGICAL_PREFIX.length) + 1)];
                System.arraycopy(COMMAND_MAGICAL_PREFIX, 0, prefixedSegment, 0, COMMAND_MAGICAL_PREFIX.length);
                prefixedSegment[COMMAND_MAGICAL_PREFIX.length] = segment[0];
                System.arraycopy(segment, 0, prefixedSegment, COMMAND_MAGICAL_PREFIX.length + 1, segment.length);
                command[i] = prefixedSegment;
                i++;
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error converting hex string", e);
                return (byte[][]) null;
            }
        }
        return command;
    }

    protected long scrapeTimeoutFromCommand(byte[][] cmd) {
        if (cmd == null) {
            Log.e(TAG, "scrapeTimeoutFromCommand cmd=null");
            return getDefaultTimeout();
        }
        byte timeoutByte = cmd[0][COMMAND_MAGICAL_PREFIX.length + 3];
        if (timeoutByte == (byte) -1) {
            Log.d(TAG, "scrapeTimeoutFromCommand timeoutByte==0xFF isInfinite=" + String.valueOf(isInfinite()) + " timeout=" + String.valueOf(getDefaultTimeout()));
            return -1;
        }
        long timeout = (((long) (timeoutByte & 255)) * 100) + MANDATORY_OVERHEAD;
        Log.d(TAG, "scrapeTimeoutFromCommand timeoutByte=" + String.valueOf(timeoutByte) + " timeout=" + String.valueOf(timeout));
        return timeout;
    }

    private byte[] getBytesFromHexString(String s) {
        if (TextUtils.isEmpty(s) || s.length() % 2 != 0) {
            throw new NumberFormatException("Invalid HEX string \"" + s + "\"");
        }
        byte[] data = new byte[(s.length() / 2)];
        for (int i = 0; i < s.length() / 2; i++) {
            int a = Character.digit(s.charAt(i * 2), 16);
            int b = Character.digit(s.charAt((i * 2) + 1), 16);
            if (a < 0 || b < 0) {
                throw new NumberFormatException("Invalid HEX string \"" + s + "\"");
            }
            data[i] = (byte) ((a << 4) + b);
        }
        return data;
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

    public LedState onNewMutedMissedCall(LedContext ledContext) {
        ledContext.addState(LedState.MISSED_EVENT);
        ledContext.removeState(LedState.INCOMING_VIDEO_CALL);
        ledContext.removeState(LedState.INCOMING_CALL);
        return null;
    }

    public LedState onNewMessage(LedContext ledContext) {
        ledContext.addState(LedState.MISSED_EVENT);
        ledContext.addState(QueueType.DELAYED, LedState.NEW_MISSED_EVENT);
        return null;
    }

    public LedState onNewMutedMessage(LedContext ledContext) {
        ledContext.addState(LedState.MISSED_EVENT);
        return null;
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
        ledContext.removeState(LedState.MUSIC_PAUSED);
        return null;
    }

    public LedState onMusicPause(LedContext ledContext) {
        ledContext.removeState(LedState.MUSIC_PLAYING);
        ledContext.removeState(LedState.MUSIC_PAUSED);
        return null;
    }

    public LedState onMusicStop(LedContext ledContext) {
        ledContext.removeState(LedState.MUSIC_PLAYING);
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
        return null;
    }

    public LedState onCustomNotificationAdded(LedContext ledContext) {
        ledContext.addState(LedState.MISSED_EVENT);
        ledContext.addState(QueueType.DELAYED, LedState.NEW_MISSED_EVENT);
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
}
