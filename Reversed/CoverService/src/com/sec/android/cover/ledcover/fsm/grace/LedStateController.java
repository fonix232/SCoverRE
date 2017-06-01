package com.sec.android.cover.ledcover.fsm.grace;

import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;

public interface LedStateController {
    byte[][] getCommand(LedContext ledContext);

    byte getCommandCodeByte();

    int getPriority(QueueType queueType);

    long getTimeout();

    int getTouchEventListenerType();

    boolean includeInQueue(QueueType queueType, LedContext ledContext);

    boolean isDataReady(LedContext ledContext);

    boolean isInfinite();

    LedState onAcceptCall(LedContext ledContext);

    LedState onAlarmStart(LedContext ledContext);

    LedState onAlarmStop(LedContext ledContext);

    LedState onBatteryFull(LedContext ledContext);

    LedState onBatteryLow(LedContext ledContext);

    LedState onCableWithWirelessConnected(LedContext ledContext);

    LedState onCableWithWirelessDisConnected(LedContext ledContext);

    LedState onCalendarStart(LedContext ledContext);

    LedState onCalendarStop(LedContext ledContext);

    LedState onChargerConnected(LedContext ledContext);

    LedState onChargerDisconnected(LedContext ledContext);

    LedState onCustomNotificationAdded(LedContext ledContext);

    LedState onCustomNotificationRemoved(LedContext ledContext);

    LedState onEndCall(LedContext ledContext);

    LedState onHeadsetPlugStateChanged(LedContext ledContext);

    LedState onIncomingCall(LedContext ledContext);

    LedState onLedLampNoti(LedContext ledContext);

    LedState onMissedEventCleared(LedContext ledContext);

    LedState onMusicPause(LedContext ledContext);

    LedState onMusicPlay(LedContext ledContext);

    LedState onMusicStop(LedContext ledContext);

    LedState onNewMessage(LedContext ledContext);

    LedState onNewMissedCall(LedContext ledContext);

    LedState onNewMutedMessage(LedContext ledContext);

    LedState onNewMutedMissedCall(LedContext ledContext);

    LedState onRejectCall(LedContext ledContext);

    void onSendingState(LedContext ledContext);

    void onStateEnter(LedContext ledContext);

    void onStateExit(LedContext ledContext);

    LedState onTimeout(LedContext ledContext);

    LedState onTimerStart(LedContext ledContext);

    LedState onTimerStop(LedContext ledContext);

    LedState onVoiceRecorderPlay(LedContext ledContext);

    LedState onVoiceRecorderStart(LedContext ledContext);

    LedState onVoiceRecorderStop(LedContext ledContext);

    LedState onVolumeChange(LedContext ledContext);

    boolean shouldWakeupForLedLamp();
}
