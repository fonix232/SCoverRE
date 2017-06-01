package com.sec.knox.container.util;

public class DaemonConnectorException extends Exception {
    private String mCmd;
    private DaemonEvent mEvent;

    public DaemonConnectorException(String str) {
        super(str);
    }

    public DaemonConnectorException(String str, DaemonEvent daemonEvent) {
        super("command '" + str + "' failed with '" + daemonEvent + "'");
        this.mCmd = str;
        this.mEvent = daemonEvent;
    }

    public DaemonConnectorException(String str, Throwable th) {
        super(str, th);
    }

    public String getCmd() {
        return this.mCmd;
    }

    public int getCode() {
        return this.mEvent.getCode();
    }

    public IllegalArgumentException rethrowAsParcelableException() {
        throw new IllegalStateException(getMessage(), this);
    }
}
