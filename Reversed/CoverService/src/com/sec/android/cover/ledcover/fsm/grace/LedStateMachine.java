package com.sec.android.cover.ledcover.fsm.grace;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.CallerData;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.grace.LedContext.VoiceRecorderState;
import com.sec.android.cover.ledcover.fsm.grace.LedStatePriorityQueue.QueueType;
import com.sec.android.cover.ledcover.fsm.grace.missedevent.MissedEvent;
import com.sec.android.cover.monitor.CoverUpdateMonitor.BatteryStatus;

public class LedStateMachine {
    private static final long HEADSET_PLUGGED_VOLUME_BLOCK_DELAY = 300;
    private static final long SAME_VOLUME_MIN_INTERVAL = 400;
    private static final String TAG = LedStateMachine.class.getSimpleName();
    public static final int TOUCH_LISTENER_TYPE_NONE = -1;
    private boolean mCoverClosed;
    private LedState mCurrentLedState = LedState.CLOCK;
    private long mHeadsetPluggedTime;
    private long mLastVolumeUpdateTime;
    private LedContext mLedContext = new LedContext();
    private LedStateMachineListener mListener;
    private boolean mPhoneStateDelayed;

    public interface LedStateMachineListener {
        void onStateChange(LedState ledState, LedContext ledContext, boolean z);
    }

    public void setListener(LedStateMachineListener listener) {
        this.mListener = listener;
    }

    public LedState getCurrentLedState() {
        return this.mCurrentLedState;
    }

    public LedContext getLedContext() {
        return this.mLedContext;
    }

    public void setCoverClosed(boolean coverClosed) {
        if (!coverClosed) {
            if (this.mLedContext.isAlarm()) {
                processAlarmStop();
            }
            if (this.mLedContext.isTimer()) {
                processTimerStop();
            }
            if (this.mLedContext.isCalendar()) {
                processCalendarStop();
            }
        }
        this.mCoverClosed = coverClosed;
    }

    private boolean processStateChange(LedState nextLedState) {
        Log.d(TAG, "processStateChange nextLedState=" + String.valueOf(nextLedState));
        if (nextLedState == null) {
            Log.d(TAG, "No state change, mCurrentLedState: " + this.mCurrentLedState);
            if (this.mCurrentLedState == LedState.IDLE) {
                this.mLedContext.getMissedEvents().clearCurrentQueue();
                this.mLedContext.getMissedEvents().clearNewEventQueue();
            }
            return false;
        }
        LedState prevLedState = this.mCurrentLedState;
        this.mLedContext.setPrevLedState(prevLedState);
        this.mCurrentLedState = nextLedState;
        if (this.mCurrentLedState != prevLedState) {
            prevLedState.onStateExit(this.mLedContext);
            this.mCurrentLedState.onStateEnter(this.mLedContext);
        }
        if (this.mCoverClosed) {
            boolean shouldTurnLedOn = nextLedState.isDataReady(this.mLedContext);
            if (this.mListener != null) {
                this.mListener.onStateChange(this.mCurrentLedState, this.mLedContext, shouldTurnLedOn);
            }
            if (this.mCurrentLedState == LedState.IDLE) {
                return processStateChange(this.mCurrentLedState.onTimeout(this.mLedContext));
            }
            return shouldTurnLedOn;
        }
        this.mLedContext.clearQueue(QueueType.POWER_BUTTON);
        this.mLedContext.clearQueue(QueueType.COVER_CLOSE);
        Log.d(TAG, "processStateChange Cover is opened, just process imediate state changes untill reaching IDLE state. prevLedState= " + prevLedState);
        return processStateChange(this.mCurrentLedState.onTimeout(this.mLedContext));
    }

    public void processVolumeChange(int level) {
        int prevLevel = this.mLedContext.getVolumeLevel();
        Log.d(TAG, "processVolumeChange level=" + String.valueOf(level));
        if (level < 0) {
            Log.d(TAG, "Volume level invalid");
            return;
        }
        if (level == prevLevel) {
            Log.w(TAG, "No volume level change");
            if (System.currentTimeMillis() - this.mLastVolumeUpdateTime < SAME_VOLUME_MIN_INTERVAL) {
                Log.d(TAG, "Min interval not elapsed, ignoring volume level change");
                return;
            }
        }
        this.mLedContext.setVolumeLevel(level);
        this.mLastVolumeUpdateTime = System.currentTimeMillis();
        if (System.currentTimeMillis() - this.mHeadsetPluggedTime < HEADSET_PLUGGED_VOLUME_BLOCK_DELAY) {
            Log.d(TAG, "Ignoring volume change after headset plugged");
        } else {
            processStateChange(this.mCurrentLedState.onVolumeChange(this.mLedContext));
        }
    }

    public void processBatteryStatusChange(BatteryStatus status) {
        Log.d(TAG, "processBatteryStatusChange status=" + String.valueOf(status));
        if (status == null) {
            Log.e(TAG, "Battery status null");
            return;
        }
        BatteryStatus prevStatus = this.mLedContext.getBatteryStatus();
        this.mLedContext.setBatteryStatus(status);
        if ((!status.isBatteryCritical() || prevStatus.isBatteryCritical()) && ((!status.isBatteryLow() || prevStatus.isBatteryLow()) && !((status.isBatteryLow() || status.isBatteryCritical()) && !status.isPluggedIn() && prevStatus.isPluggedIn()))) {
            if (!status.isCharged() || prevStatus.isCharged()) {
                if (status.isPluggedIn() != prevStatus.isPluggedIn()) {
                    if (status.isWirelssCharged()) {
                        Log.d(TAG, "Wireless charging not supported");
                    } else if (status.isPluggedIn()) {
                        processStateChange(this.mCurrentLedState.onChargerConnected(this.mLedContext));
                    } else if (!prevStatus.isWirelssCharged()) {
                        processStateChange(this.mCurrentLedState.onChargerDisconnected(this.mLedContext));
                    }
                } else if (status.isWirelssCharged() == prevStatus.isWirelssCharged()) {
                    Log.d(TAG, "No battery status change");
                } else if (!status.isWirelssCharged()) {
                    processStateChange(this.mCurrentLedState.onCableWithWirelessDisConnected(this.mLedContext));
                } else if (status.isWirelssCharged()) {
                    processStateChange(this.mCurrentLedState.onCableWithWirelessConnected(this.mLedContext));
                }
            } else if (status.isWirelssCharged() || prevStatus.isWirelssCharged()) {
                Log.d(TAG, "Battery fully charged on wireless charger not supported");
            } else {
                processStateChange(this.mCurrentLedState.onBatteryFull(this.mLedContext));
            }
        } else if (status.isWirelssCharged() || prevStatus.isWirelssCharged()) {
            Log.d(TAG, "Wireless charging not supported");
        } else {
            if (!status.isPluggedIn()) {
                processStateChange(this.mCurrentLedState.onChargerDisconnected(this.mLedContext));
            }
            processStateChange(this.mCurrentLedState.onBatteryLow(this.mLedContext));
        }
    }

    public void processMusicStateChange(MusicState state) {
        MusicState prevState = this.mLedContext.getMusicState();
        Log.d(TAG, "processMusicStateChange state=" + String.valueOf(state) + " prevState=" + String.valueOf(prevState));
        if (state == null) {
            Log.e(TAG, "Music state null");
        } else if (state == prevState) {
            Log.d(TAG, "No music state change");
        } else {
            this.mLedContext.setMusicState(state);
            if (!(prevState == MusicState.PAUSE && state == MusicState.STOP) && (prevState != null || state == MusicState.PLAY)) {
                switch (state) {
                    case PAUSE:
                        processStateChange(this.mCurrentLedState.onMusicPause(this.mLedContext));
                        return;
                    case PLAY:
                        processStateChange(this.mCurrentLedState.onMusicPlay(this.mLedContext));
                        return;
                    case STOP:
                        processStateChange(this.mCurrentLedState.onMusicStop(this.mLedContext));
                        return;
                    default:
                        return;
                }
            }
            Log.d(TAG, "Ignoring PAUSE->STOP or NULL -> STOP/PAUSE change");
        }
    }

    public void processVoiceRecStateChange(VoiceRecorderState state, long recordingStartTime) {
        Log.d(TAG, "processVoiceRecStateChange state=" + String.valueOf(state) + " recordingStartTime=" + String.valueOf(recordingStartTime));
        if (state == null) {
            Log.e(TAG, "Voice recorder state null");
        } else if (state == this.mLedContext.getVoiceRecorderState()) {
            Log.d(TAG, "No voice recorder state change");
        } else {
            this.mLedContext.setVoiceRecorderState(state);
            this.mLedContext.setVoiceRecorderRecordingStartTime(recordingStartTime);
            switch (state) {
                case RECORD:
                    processStateChange(this.mCurrentLedState.onVoiceRecorderStart(this.mLedContext));
                    return;
                case PLAY:
                    processStateChange(this.mCurrentLedState.onVoiceRecorderPlay(this.mLedContext));
                    return;
                case STOP:
                    processStateChange(this.mCurrentLedState.onVoiceRecorderStop(this.mLedContext));
                    return;
                default:
                    return;
            }
        }
    }

    public void processAlarmStart() {
        Log.d(TAG, "processAlarmStart mCoverClosed? " + this.mCoverClosed);
        if (this.mCoverClosed) {
            this.mLedContext.setAlarm(true);
            processStateChange(this.mCurrentLedState.onAlarmStart(this.mLedContext));
        }
    }

    public void processAlarmStop() {
        Log.d(TAG, "processAlarmStop mCoverClosed? " + this.mCoverClosed);
        if (this.mCoverClosed) {
            this.mLedContext.setAlarm(false);
            processStateChange(this.mCurrentLedState.onAlarmStop(this.mLedContext));
        }
    }

    public void processTimerStart() {
        Log.d(TAG, "processTimerStart mCoverClosed? " + this.mCoverClosed);
        if (this.mCoverClosed) {
            this.mLedContext.setTimer(true);
            processStateChange(this.mCurrentLedState.onTimerStart(this.mLedContext));
        }
    }

    public void processTimerStop() {
        Log.d(TAG, "processTimerStop mCoverClosed? " + this.mCoverClosed);
        if (this.mCoverClosed) {
            this.mLedContext.setTimer(false);
            processStateChange(this.mCurrentLedState.onTimerStop(this.mLedContext));
        }
    }

    public void processCalendarStart() {
        Log.d(TAG, "processCalendarStart mCoverClosed? " + this.mCoverClosed);
        if (this.mCoverClosed) {
            this.mLedContext.setCalendar(true);
            processStateChange(this.mCurrentLedState.onCalendarStart(this.mLedContext));
        }
    }

    public void processCalendarStop() {
        Log.d(TAG, "processCalendarStop mCoverClosed? " + this.mCoverClosed);
        if (this.mCoverClosed) {
            this.mLedContext.setCalendar(false);
            processStateChange(this.mCurrentLedState.onCalendarStop(this.mLedContext));
        }
    }

    public void processHeadsetPlugChange(boolean headsetPlugged) {
        Log.d(TAG, "processHeadsetPlugChange headsetPlugged=" + String.valueOf(headsetPlugged));
        if (headsetPlugged != this.mLedContext.isHeadsetPlugged()) {
            this.mLedContext.setHeadsetPlugged(headsetPlugged);
            this.mHeadsetPluggedTime = System.currentTimeMillis();
            processStateChange(this.mCurrentLedState.onHeadsetPlugStateChanged(this.mLedContext));
            return;
        }
        Log.d(TAG, "No headset plugged state change");
    }

    public void processTimeout() {
        Log.d(TAG, "processTimeout");
        processStateChange(this.mCurrentLedState.onTimeout(this.mLedContext));
    }

    public void processCallStateChange(int callState, CallerData callerData, boolean isVideoCall) {
        Log.d(TAG, "processCallStateChange callState=" + String.valueOf(callState) + " callerData=" + callerData + " isVideoCall=" + String.valueOf(isVideoCall));
        if (callState == 0 || callState == 2 || callState == 1) {
            int prevCallState = this.mLedContext.getCallState();
            Log.d(TAG, "processCallStateChange prevCallState=" + String.valueOf(prevCallState));
            if (prevCallState != callState || this.mPhoneStateDelayed) {
                if (callerData == null) {
                    callerData = new CallerData(0, null);
                }
                this.mLedContext.setCallState(callState);
                this.mLedContext.setCallerData(callerData);
                if (callState != 0) {
                    this.mLedContext.setVideoCall(isVideoCall);
                }
                switch (callState) {
                    case 0:
                        if (prevCallState == 2) {
                            this.mLedContext.setCallStartTime(-1);
                            processStateChange(this.mCurrentLedState.onEndCall(this.mLedContext));
                            return;
                        } else if (prevCallState == 1) {
                            this.mLedContext.setCallDuration(null);
                            if (this.mLedContext.isInCallTouchReject() || this.mPhoneStateDelayed) {
                                processStateChange(this.mCurrentLedState.onRejectCall(this.mLedContext));
                                this.mLedContext.setIsInCallTouchReject(false);
                                return;
                            }
                            this.mPhoneStateDelayed = true;
                            return;
                        } else if (prevCallState == 0 && this.mPhoneStateDelayed) {
                            processStateChange(this.mCurrentLedState.onRejectCall(this.mLedContext));
                            this.mPhoneStateDelayed = false;
                            return;
                        } else {
                            return;
                        }
                    case 1:
                        if (prevCallState == 0) {
                            this.mLedContext.setCallStartTime(-1);
                            this.mLedContext.setCallDuration(null);
                            processStateChange(this.mCurrentLedState.onIncomingCall(this.mLedContext));
                            return;
                        } else if (prevCallState == 2) {
                            this.mLedContext.setCallDuration(null);
                            processStateChange(this.mCurrentLedState.onIncomingCall(this.mLedContext));
                            return;
                        } else {
                            return;
                        }
                    case 2:
                        if (prevCallState == 1) {
                            this.mLedContext.setCallDuration(null);
                            processStateChange(this.mCurrentLedState.onAcceptCall(this.mLedContext));
                            return;
                        } else if (prevCallState == 0) {
                            this.mLedContext.setCallDuration(null);
                            return;
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
            Log.d(TAG, "No call state change");
            return;
        }
        Log.e(TAG, "Invalid call state");
    }

    public void processUnreadMessage(int count, boolean muted, MissedEvent messageEvent) {
        Log.d(TAG, "processUnreadMessage count=" + String.valueOf(count) + " muted=" + String.valueOf(muted));
        int prevCount = this.mLedContext.getUnreadMessagesCount();
        this.mLedContext.setUnreadMessagesCount(count);
        if (prevCount < count) {
            this.mLedContext.getMissedEvents().addMissedEvent(messageEvent);
            if (muted) {
                processStateChange(this.mCurrentLedState.onNewMutedMessage(this.mLedContext));
            } else {
                processStateChange(this.mCurrentLedState.onNewMessage(this.mLedContext));
            }
        } else if (count > 0) {
            this.mLedContext.getMissedEvents().addMutedMissedEvent(messageEvent);
        } else {
            this.mLedContext.getMissedEvents().removeMissedEvent(messageEvent);
            if (this.mLedContext.getMissedEvents().isEmpty()) {
                processStateChange(this.mCurrentLedState.onMissedEventCleared(this.mLedContext));
            }
        }
    }

    public void processMissedCall(int count, boolean muted, MissedEvent callEvent) {
        Log.d(TAG, "processMissedCall count=" + String.valueOf(count) + " muted=" + String.valueOf(muted));
        int prevCount = this.mLedContext.getMissedCallsCount();
        this.mLedContext.setMissedCallsCount(count);
        if (prevCount < count) {
            if (this.mPhoneStateDelayed) {
                this.mPhoneStateDelayed = false;
            }
            this.mLedContext.getMissedEvents().removeMissedEvent(callEvent);
            this.mLedContext.getMissedEvents().addMutedMissedEvent(callEvent);
            if (muted) {
                processStateChange(this.mCurrentLedState.onNewMutedMissedCall(this.mLedContext));
            } else {
                processStateChange(this.mCurrentLedState.onNewMissedCall(this.mLedContext));
            }
        } else if (count > 0) {
            this.mLedContext.getMissedEvents().addMutedMissedEvent(callEvent);
        } else {
            this.mLedContext.getMissedEvents().removeMissedEvent(callEvent);
            if (this.mLedContext.getMissedEvents().isEmpty()) {
                processStateChange(this.mCurrentLedState.onMissedEventCleared(this.mLedContext));
            }
        }
    }

    public void processUnreadMessageForInactiveUser(MissedEvent messageEvent) {
        Log.d(TAG, "processUnreadMessageForInactiveUser");
        this.mLedContext.getMissedEvents().addInactiveUserMissedEvent(messageEvent);
        processStateChange(this.mCurrentLedState.onNewMessage(this.mLedContext));
    }

    public void processMissedCallForInactiveUser() {
        Log.d(TAG, "processMissedCallForInactiveUser : ignoring");
    }

    public void processCallDisconnectReason(int type, String number, int userId) {
        if (type == 5) {
            Log.d(TAG, "processCallDisconnectReason REJECTED_TYPE");
            this.mPhoneStateDelayed = true;
            processCallStateChange(this.mLedContext.getCallState(), null, false);
        }
    }

    public void processCallTime(long startTime) {
        boolean isCallInProgress = this.mLedContext.getCallState() == 2;
        Log.d(TAG, "processCallTime startTime=" + String.valueOf(startTime) + " is call inprogress? " + isCallInProgress + ", isVideoCall? " + this.mLedContext.isVideoCall());
        this.mLedContext.setCallStartTime(startTime);
        if (isCallInProgress) {
            processStateChange(this.mCurrentLedState.onAcceptCall(this.mLedContext));
        }
    }

    public void processCallEnd(String duration) {
        boolean callStateIdle = this.mLedContext.getCallState() == 0;
        Log.d(TAG, "processCallEnd duration=" + duration + " is call finished? " + callStateIdle + ", isVideoCall? " + this.mLedContext.isVideoCall());
        this.mLedContext.setCallDuration(duration);
        if (callStateIdle) {
            processStateChange(this.mCurrentLedState.onEndCall(this.mLedContext));
        }
    }

    public void processCustomNotificationsAdded(MissedEvent[] addedNotis) {
        Log.d(TAG, "processCustomNotificationsAdded");
        this.mLedContext.getMissedEvents().addMissedEvents(addedNotis);
        processStateChange(this.mCurrentLedState.onCustomNotificationAdded(this.mLedContext));
    }

    public void processCustomNotificationsRemoved(MissedEvent[] removedNotis) {
        Log.d(TAG, "processCustomNotificationsRemoved");
        this.mLedContext.getMissedEvents().removeMissedEvents(removedNotis);
        processStateChange(this.mCurrentLedState.onCustomNotificationRemoved(this.mLedContext));
    }

    public boolean processPowerButtonWakeUp(boolean powerButtonWakeup) {
        Log.d(TAG, "processPowerButtonWakeUp: powerButtonWakeup=" + powerButtonWakeup);
        this.mLedContext.setIsCoverClosedWakeUp(false);
        this.mLedContext.clearQueue(QueueType.COVER_CLOSE);
        if (this.mCurrentLedState == LedState.IDLE) {
            this.mLedContext.clearQueue(QueueType.DELAYED);
            this.mLedContext.getMissedEvents().clearNewEventQueue();
        }
        this.mLedContext.setIsPowerButtonWakeUp(powerButtonWakeup);
        if (!powerButtonWakeup) {
            this.mLedContext.clearQueue(QueueType.POWER_BUTTON);
            this.mLedContext.getMissedEvents().clearCurrentQueue();
        } else if (!(!this.mLedContext.isNoCallStateDisplayed() || this.mLedContext.hasState(LedState.ALARM) || this.mLedContext.hasState(LedState.TIMER) || this.mLedContext.hasState(LedState.CALENDAR))) {
            this.mLedContext.resetAndFillQueue(QueueType.POWER_BUTTON);
            this.mLedContext.getMissedEvents().addLatestEventsToCurrentQueue();
        }
        LedState nextState = this.mCurrentLedState.onTimeout(this.mLedContext);
        if (nextState == LedState.IDLE) {
            this.mLedContext.clearQueue(QueueType.DELAYED);
            this.mLedContext.getMissedEvents().clearNewEventQueue();
        }
        return processStateChange(nextState);
    }

    public void processLedLampNoti() {
        Log.d(TAG, "processLedLampNoti");
        processStateChange(this.mCurrentLedState.onLedLampNoti(this.mLedContext));
    }

    public void processCoverCloseWakeUp(boolean coverClosedState) {
        Log.d(TAG, "processCoverCloseWakeUp: coverClosedState=" + coverClosedState);
        this.mLedContext.setIsPowerButtonWakeUp(false);
        this.mLedContext.clearQueue(QueueType.POWER_BUTTON);
        this.mLedContext.setIsCoverClosedWakeUp(coverClosedState);
        if (coverClosedState && this.mLedContext.isNoCallStateDisplayed()) {
            this.mLedContext.resetAndFillQueue(QueueType.COVER_CLOSE);
            this.mLedContext.getMissedEvents().addLatestEventsToCurrentQueue();
        }
        processStateChange(this.mCurrentLedState.onTimeout(this.mLedContext));
    }

    public void reset() {
        this.mLedContext = new LedContext();
        this.mCoverClosed = false;
        this.mCurrentLedState = LedState.CLOCK;
        this.mPhoneStateDelayed = false;
    }
}
