package com.samsung.android.media.fmradio;

public class SemFmPlayerException extends Exception {
    private static final long serialVersionUID = 1;
    private Throwable mThrowable;
    private String msg;

    public SemFmPlayerException(String str, Throwable th) {
        this.msg = str;
        this.mThrowable = th;
    }

    public String getMessage() {
        return this.msg;
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }
}
