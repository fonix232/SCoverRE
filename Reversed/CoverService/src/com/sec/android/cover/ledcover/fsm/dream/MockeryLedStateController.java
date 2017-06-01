package com.sec.android.cover.ledcover.fsm.dream;

import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.LedStatePriorityQueue.QueueType;

public class MockeryLedStateController extends AbsLedStateController {
    private static final byte CMD_FACTORY_MODE = (byte) 112;
    public static final byte CMD_NV_READ = (byte) 116;
    public static final byte CMD_NV_WRITE = (byte) 115;
    private static final byte[] COMMAND_NV_READ = new byte[]{(byte) 0, (byte) 35, (byte) 0, (byte) 0, (byte) 5, (byte) 4, CMD_NV_READ, (byte) 0, (byte) 0};
    private static final byte[] COMMAND_NV_WRITE = new byte[]{(byte) 0, (byte) 33, (byte) 0, (byte) 0, (byte) 4, (byte) 4, CMD_NV_WRITE, (byte) 0, (byte) 0};
    public static final int NV_WRITE_COUNT_POS = 8;
    private static final String TAG = MockeryLedStateController.class.getSimpleName();
    private byte mCmdByte;
    private LedState mCorrespondingLedState = null;
    private byte[] mData;

    public MockeryLedStateController(byte cmdByte, byte[] data) {
        this.mCmdByte = cmdByte;
        this.mData = data;
        switch (cmdByte) {
            case (byte) 9:
                this.mCorrespondingLedState = LedState.MISSED_EVENT;
                return;
            case (byte) 112:
            case (byte) 115:
            case (byte) 116:
                return;
            default:
                LedState[] states = LedState.values();
                for (int i = 0; i < states.length; i++) {
                    if (states[i].getCommandCodeByte() == cmdByte) {
                        this.mCorrespondingLedState = states[i];
                        return;
                    }
                }
                return;
        }
    }

    protected LedState getControllerLedState() {
        return null;
    }

    public int getPriority(QueueType type) {
        return 0;
    }

    protected byte getTimeoutByteVal() {
        switch (this.mCmdByte) {
            case (byte) 112:
            case (byte) 115:
            case (byte) 116:
                return (byte) 0;
            default:
                return super.getTimeoutByteVal();
        }
    }

    public long getTimeout() {
        if (this.mCorrespondingLedState != null) {
            return this.mCorrespondingLedState.getTimeout();
        }
        if (this.mCmdByte == CMD_FACTORY_MODE || this.mCmdByte == CMD_NV_WRITE || this.mCmdByte == CMD_NV_READ) {
            return -1;
        }
        return super.getTimeout();
    }

    public byte[] getCommand(LedContext ledContext) {
        switch (getCommandCodeByte()) {
            case (byte) 115:
                return getNvWriteCommand();
            case (byte) 116:
                return COMMAND_NV_READ;
            default:
                return composeCommand(getCommandCodeByte(), getTimeoutByteVal(), this.mData);
        }
    }

    public byte getCommandCodeByte() {
        return this.mCmdByte;
    }

    public int getTouchEventListenerType() {
        if (this.mCmdByte == CMD_FACTORY_MODE || this.mCmdByte == LedState.MUSIC_CONTROLLER.getCommandCodeByte()) {
            return 5;
        }
        if (this.mCorrespondingLedState != null) {
            return this.mCorrespondingLedState.getTouchEventListenerType();
        }
        return super.getTouchEventListenerType();
    }

    public boolean isInfinite() {
        if (this.mCorrespondingLedState != null) {
            return this.mCorrespondingLedState.isInfinite();
        }
        if (this.mCmdByte == CMD_FACTORY_MODE || this.mCmdByte == CMD_NV_WRITE || this.mCmdByte == CMD_NV_READ) {
            return true;
        }
        return super.isInfinite();
    }

    private byte[] getNvWriteCommand() {
        if (this.mData == null || this.mData.length < 1) {
            Log.e(TAG, "NV WRITE command: missing data count");
            return null;
        }
        byte[] command = (byte[]) COMMAND_NV_WRITE.clone();
        command[8] = this.mData[0];
        return command;
    }
}
