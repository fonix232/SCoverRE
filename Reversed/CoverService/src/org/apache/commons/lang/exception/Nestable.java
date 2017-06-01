package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public interface Nestable {
    Throwable getCause();

    String getMessage();

    String getMessage(int i);

    String[] getMessages();

    Throwable getThrowable(int i);

    int getThrowableCount();

    Throwable[] getThrowables();

    int indexOfThrowable(Class cls);

    int indexOfThrowable(Class cls, int i);

    void printPartialStackTrace(PrintWriter printWriter);

    void printStackTrace(PrintStream printStream);

    void printStackTrace(PrintWriter printWriter);
}
