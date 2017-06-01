package org.apache.commons.lang;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.commons.lang.exception.Nestable;
import org.apache.commons.lang.exception.NestableDelegate;

public class NotImplementedException extends UnsupportedOperationException implements Nestable {
    private static final String DEFAULT_MESSAGE = "Code is not implemented";
    private static final long serialVersionUID = -6894122266938754088L;
    private Throwable cause;
    private NestableDelegate delegate;

    public NotImplementedException() {
        super(DEFAULT_MESSAGE);
        this.delegate = new NestableDelegate(this);
    }

    public NotImplementedException(String msg) {
        if (msg == null) {
            msg = DEFAULT_MESSAGE;
        }
        super(msg);
        this.delegate = new NestableDelegate(this);
    }

    public NotImplementedException(Throwable cause) {
        super(DEFAULT_MESSAGE);
        this.delegate = new NestableDelegate(this);
        this.cause = cause;
    }

    public NotImplementedException(String msg, Throwable cause) {
        if (msg == null) {
            msg = DEFAULT_MESSAGE;
        }
        super(msg);
        this.delegate = new NestableDelegate(this);
        this.cause = cause;
    }

    public NotImplementedException(Class clazz) {
        String str;
        if (clazz == null) {
            str = DEFAULT_MESSAGE;
        } else {
            str = new StringBuffer().append("Code is not implemented in ").append(clazz).toString();
        }
        super(str);
        this.delegate = new NestableDelegate(this);
    }

    public Throwable getCause() {
        return this.cause;
    }

    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        }
        if (this.cause != null) {
            return this.cause.toString();
        }
        return null;
    }

    public String getMessage(int index) {
        if (index == 0) {
            return super.getMessage();
        }
        return this.delegate.getMessage(index);
    }

    public String[] getMessages() {
        return this.delegate.getMessages();
    }

    public Throwable getThrowable(int index) {
        return this.delegate.getThrowable(index);
    }

    public int getThrowableCount() {
        return this.delegate.getThrowableCount();
    }

    public Throwable[] getThrowables() {
        return this.delegate.getThrowables();
    }

    public int indexOfThrowable(Class type) {
        return this.delegate.indexOfThrowable(type, 0);
    }

    public int indexOfThrowable(Class type, int fromIndex) {
        return this.delegate.indexOfThrowable(type, fromIndex);
    }

    public void printStackTrace() {
        this.delegate.printStackTrace();
    }

    public void printStackTrace(PrintStream out) {
        this.delegate.printStackTrace(out);
    }

    public void printStackTrace(PrintWriter out) {
        this.delegate.printStackTrace(out);
    }

    public final void printPartialStackTrace(PrintWriter out) {
        super.printStackTrace(out);
    }
}
