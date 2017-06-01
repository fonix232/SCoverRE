package com.samsung.android.service.RemoteLockControl;

public final class RemoteLockControlException extends Exception {
    private int mRlcErrorCode;

    public RemoteLockControlException(int i, String str) {
        super("[" + i + "]" + str);
        this.mRlcErrorCode = i;
    }

    public int getRlcErrorCode() {
        return this.mRlcErrorCode;
    }
}
