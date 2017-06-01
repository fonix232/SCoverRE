package com.sec.android.cover.ledcover.fsm.dream.state;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.LedContext;
import com.sec.android.cover.ledcover.fsm.dream.LedContext.MusicState;
import com.sec.android.cover.ledcover.fsm.dream.LedState;
import com.sec.android.cover.ledcover.fsm.dream.LedStateMachine;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class MusicControllerLedStateController extends MusicPlayingLedStateController {
    private static final int CMD_LED_MUSIC_CONTROLLER = 19;
    private static final int DEFAULT_PRIORITY = 30;
    private static final String TAG = MusicControllerLedStateController.class.getSimpleName();
    MusicControllerState mMusicControllerState = MusicControllerState.PLAY;

    private enum MusicControllerState {
        PLAY,
        PAUSE
    }

    protected LedState getControllerLedState() {
        return LedState.MUSIC_CONTROLLER;
    }

    public int getPriority(QueueType type) {
        switch (type) {
            case POWER_BUTTON:
                return -2147483618;
            default:
                return Integer.MAX_VALUE;
        }
    }

    public boolean includeInQueue(QueueType type, LedContext ledContext) {
        switch (type) {
            case POWER_BUTTON:
                if (ledContext.isUPSMEnabled()) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public byte[] getCommand(LedContext ledContext) {
        int musicPlayingStatus;
        Log.d(TAG, "getCommand, MusicControllerLedState is: " + this.mMusicControllerState);
        if (ledContext.getMusicState() == MusicState.PLAY) {
            musicPlayingStatus = 1;
        } else {
            musicPlayingStatus = 0;
        }
        return composeCommand(new byte[]{(byte) musicPlayingStatus});
    }

    public byte getCommandCodeByte() {
        return (byte) 19;
    }

    public int getTouchEventListenerType() {
        if (this.mMusicControllerState == MusicControllerState.PLAY || this.mMusicControllerState == MusicControllerState.PAUSE) {
            return LedStateMachine.TOUCH_LISTENER_TYPE_MUSIC_CONTROLLER;
        }
        return super.getTouchEventListenerType();
    }

    public LedState onTimeout(LedContext ledContext) {
        return LedState.IDLE;
    }

    public LedState onMusicPlay(LedContext ledContext) {
        super.onMusicPause(ledContext);
        return LedState.MUSIC_CONTROLLER;
    }

    public LedState onMusicPause(LedContext ledContext) {
        super.onMusicPause(ledContext);
        return LedState.MUSIC_CONTROLLER;
    }

    public LedState onMusicStop(LedContext ledContext) {
        super.onMusicStop(ledContext);
        return LedState.MUSIC_PAUSED;
    }

    public LedState onMediaKeyPlayPause(LedContext ledContext) {
        refreshControllerState(ledContext);
        return null;
    }

    public void onStateEnter(LedContext ledContext) {
        refreshControllerState(ledContext);
    }

    public void onStateExit(LedContext ledContext) {
        if (ledContext.getMusicState() == MusicState.PLAY) {
            ledContext.addState(LedState.MUSIC_CONTROLLER);
            ledContext.addState(LedState.MUSIC_PLAYING);
        }
    }

    private void refreshControllerState(LedContext ledContext) {
        if (ledContext.getMusicState() == MusicState.PLAY) {
            this.mMusicControllerState = MusicControllerState.PLAY;
        } else {
            this.mMusicControllerState = MusicControllerState.PAUSE;
        }
    }
}
