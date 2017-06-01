package com.sec.android.cover.ledcover.fsm.dream;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.CallerData;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.VoiceRecorderState;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;
import com.sec.android.cover.ledcover.fsm.dream.missedevent.MissedEvent;
import com.sec.android.cover.monitor.CoverUpdateMonitor.BatteryStatus;

public class LedStateMachine {
    private static final long HEADSET_PLUGGED_VOLUME_BLOCK_DELAY = 300;
    private static final int INTERNAL_TOUCH_LISTENER_TYPE = 100;
    private static final long SAME_VOLUME_MIN_INTERVAL = 400;
    private static final String TAG = LedStateMachine.class.getSimpleName();
    public static final int TOUCH_LISTENER_TYPE_MUSIC_CONTROLLER = 101;
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
            if (this.mLedContext.getBixbyState() != -1) {
                processBixbyStateChanged(-1);
            }
        }
        this.mCoverClosed = coverClosed;
    }

    private boolean processStateChange(LedState nextLedState) {
        return processStateChange(nextLedState, true);
    }

    private boolean processStateChange(LedState nextLedState, boolean wakeUp) {
        boolean shouldTurnLedOn = false;
        Log.d(TAG, "processStateChange nextLedState=" + String.valueOf(nextLedState) + " wakeUp=" + wakeUp);
        if (nextLedState == null) {
            Log.d(TAG, "No state change, mCurrentLedState: " + this.mCurrentLedState);
            if (this.mCurrentLedState != LedState.IDLE) {
                return false;
            }
            this.mLedContext.getMissedEvents().clearCurrentQueue();
            this.mLedContext.getMissedEvents().clearNewEventQueue();
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
            if (nextLedState.isDataReady(this.mLedContext) && wakeUp) {
                shouldTurnLedOn = true;
            }
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

    public void processTimeTick() {
        Log.d(TAG, "processTimeTick mCoverClosed? " + this.mCoverClosed);
        if (this.mCoverClosed) {
            processStateChange(this.mCurrentLedState.onTimeTick(this.mLedContext));
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
        processStateChange(this.mCurrentLedState.onTimeout(this.mLedContext), !this.mCurrentLedState.isContinuation());
    }

    public void processCallStateChange(int callState, CallerData callerData, boolean isVideoCall) {
        Log.d(TAG, "processCallStateChange callState=" + String.valueOf(callState) + " callerData=" + callerData + " isVideoCall=" + String.valueOf(isVideoCall));
        if (callState == 0 || callState == 2 || callState == 1) {
            int prevCallState = this.mLedContext.getCallState();
            Log.d(TAG, "processCallStateChange prevCallState=" + String.valueOf(prevCallState));
            if (prevCallState != callState || this.mPhoneStateDelayed) {
                if (callerData == null) {
                    CallerData callerData2 = new CallerData(0, null, null, false, false);
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

    public void processCallDisconnectReason(int type, String number, int userId) {
        if (type == 5) {
            Log.d(TAG, "processCallDisconnectReason REJECTED_TYPE");
            this.mPhoneStateDelayed = true;
            processCallStateChange(this.mLedContext.getCallState(), null, false);
        } else if (type == 3 && this.mLedContext.getCallState() == 0 && this.mPhoneStateDelayed) {
            Log.d(TAG, "processCallDisconnectReason MISSED_TYPE");
            this.mLedContext.setMissedCallEvent(null);
            processStateChange(this.mCurrentLedState.onNewMissedCall(this.mLedContext));
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
        boolean newCallEvent = false;
        boolean addSilently = false;
        for (MissedEvent event : addedNotis) {
            if (event.isCallApp() && this.mPhoneStateDelayed) {
                Log.d(TAG, "processCustomNotificationsAdded: new missed call");
                this.mLedContext.getMissedEvents().removeMissedEvent(event);
                this.mLedContext.getMissedEvents().addMutedMissedEvent(event);
                this.mPhoneStateDelayed = false;
                this.mLedContext.setMissedCallEvent(event);
                newCallEvent = true;
            } else if (getLedContext().isDoNotDisturbNotiOffPolicy()) {
                Log.w(TAG, "Do not disturb mode on, process new notification silently");
                this.mLedContext.getMissedEvents().addMutedMissedEvent(event);
                addSilently = true;
            } else {
                this.mLedContext.getMissedEvents().addMissedEvent(event);
            }
        }
        if (newCallEvent) {
            processStateChange(this.mCurrentLedState.onNewMissedCall(this.mLedContext));
        } else if (addSilently) {
            processStateChange(this.mCurrentLedState.onCustomNotificationAddedSilently(this.mLedContext));
        } else {
            processStateChange(this.mCurrentLedState.onCustomNotificationAdded(this.mLedContext));
        }
    }

    public void processCustomNotificationsRemoved(MissedEvent[] removedNotis) {
        Log.d(TAG, "processCustomNotificationsRemoved");
        this.mLedContext.getMissedEvents().removeMissedEvents(removedNotis);
        processStateChange(this.mCurrentLedState.onCustomNotificationRemoved(this.mLedContext));
    }

    public void processAllCustomNotificationsRemoved() {
        Log.d(TAG, "processAllCustomNotificationsRemoved");
        this.mLedContext.getMissedEvents().removeAllMissedEvents();
        processStateChange(this.mCurrentLedState.onMissedEventCleared(this.mLedContext));
    }

    public void processMediaKeyPress(int keycode) {
        switch (keycode) {
            case 85:
                Log.d(TAG, "processMediaKeyPress PLAY_PAUSE");
                processStateChange(this.mCurrentLedState.onMediaKeyPlayPause(this.mLedContext));
                return;
            case 87:
            case 88:
                Log.d(TAG, "processMediaKeyPress NEXT/PREV");
                if (this.mLedContext.getMusicState() == MusicState.PLAY && this.mCurrentLedState == LedState.MUSIC_CONTROLLER) {
                    processStateChange(this.mCurrentLedState.onMusicPlay(this.mLedContext));
                    return;
                }
                return;
            default:
                return;
        }
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
            if (!this.mLedContext.hasState(LedState.MUSIC_CONTROLLER) || this.mLedContext.isUPSMEnabled()) {
                this.mLedContext.resetAndFillQueue(QueueType.POWER_BUTTON);
                this.mLedContext.getMissedEvents().addLatestEventsToCurrentQueue();
            } else {
                this.mLedContext.clearQueue(QueueType.POWER_BUTTON);
                this.mLedContext.addState(QueueType.POWER_BUTTON, LedState.MUSIC_CONTROLLER);
            }
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
        Log.d(TAG, "processCoverCloseWakeUp: coverClosedState=" + coverClosedState + " isNoCallStateDisplayed=" + this.mLedContext.isNoCallStateDisplayed());
        this.mLedContext.setIsPowerButtonWakeUp(false);
        this.mLedContext.clearQueue(QueueType.POWER_BUTTON);
        this.mLedContext.setIsCoverClosedWakeUp(coverClosedState);
        if (coverClosedState && this.mLedContext.isNoCallStateDisplayed()) {
            this.mLedContext.resetAndFillQueue(QueueType.COVER_CLOSE);
            this.mLedContext.getMissedEvents().addLatestEventsToCurrentQueue();
        }
        processStateChange(this.mCurrentLedState.onTimeout(this.mLedContext));
    }

    public void processBixbyStateChanged(int bixbyState) {
        if (this.mCoverClosed) {
            int filteredBixbyState = validateBixbyState(bixbyState);
            Log.d(TAG, "processBixbyStateChanged bixbyState=" + bixbyState + " filteredBixbyState=" + filteredBixbyState);
            bixbyState = filteredBixbyState;
            int prevBixbyState = this.mLedContext.getBixbyState();
            if (prevBixbyState == bixbyState) {
                Log.d(TAG, "No Bixby state change");
                return;
            }
            this.mLedContext.setBixbyState(bixbyState);
            if (bixbyState == -1) {
                processStateChange(this.mCurrentLedState.onBixbyDeactivated(this.mLedContext));
            } else if (prevBixbyState == -1) {
                processStateChange(this.mCurrentLedState.onBixbyActivated(this.mLedContext));
            } else {
                processStateChange(this.mCurrentLedState.onBixbyActiveStateChanged(this.mLedContext));
            }
        }
    }

    public void reset() {
        this.mLedContext = new LedContext();
        this.mCoverClosed = false;
        this.mCurrentLedState = LedState.CLOCK;
        this.mPhoneStateDelayed = false;
    }

    public static boolean isInternalTouchListenerType(int listenerType) {
        return listenerType >= INTERNAL_TOUCH_LISTENER_TYPE;
    }

    private int validateBixbyState(int bixbyState) {
        switch (bixbyState) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return bixbyState;
            default:
                return -1;
        }
    }
}
