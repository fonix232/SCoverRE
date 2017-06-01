package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NestableDelegate implements Serializable {
    private static final transient String MUST_BE_THROWABLE = "The Nestable implementation passed to the NestableDelegate(Nestable) constructor must extend java.lang.Throwable";
    static Class class$org$apache$commons$lang$exception$Nestable = null;
    public static boolean matchSubclasses = true;
    private static final long serialVersionUID = 1;
    public static boolean topDown = true;
    public static boolean trimStackFrames = true;
    private Throwable nestable = null;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public NestableDelegate(Nestable nestable) {
        if (nestable instanceof Throwable) {
            this.nestable = (Throwable) nestable;
            return;
        }
        throw new IllegalArgumentException(MUST_BE_THROWABLE);
    }

    public String getMessage(int index) {
        Class class$;
        Throwable t = getThrowable(index);
        if (class$org$apache$commons$lang$exception$Nestable == null) {
            class$ = class$("org.apache.commons.lang.exception.Nestable");
            class$org$apache$commons$lang$exception$Nestable = class$;
        } else {
            class$ = class$org$apache$commons$lang$exception$Nestable;
        }
        if (class$.isInstance(t)) {
            return ((Nestable) t).getMessage(0);
        }
        return t.getMessage();
    }

    public String getMessage(String baseMsg) {
        Throwable nestedCause = ExceptionUtils.getCause(this.nestable);
        String causeMsg = nestedCause == null ? null : nestedCause.getMessage();
        if (nestedCause == null || causeMsg == null) {
            return baseMsg;
        }
        return baseMsg != null ? new StringBuffer().append(baseMsg).append(": ").append(causeMsg).toString() : causeMsg;
    }

    public String[] getMessages() {
        Throwable[] throwables = getThrowables();
        String[] msgs = new String[throwables.length];
        for (int i = 0; i < throwables.length; i++) {
            Class class$;
            if (class$org$apache$commons$lang$exception$Nestable == null) {
                class$ = class$("org.apache.commons.lang.exception.Nestable");
                class$org$apache$commons$lang$exception$Nestable = class$;
            } else {
                class$ = class$org$apache$commons$lang$exception$Nestable;
            }
            msgs[i] = class$.isInstance(throwables[i]) ? ((Nestable) throwables[i]).getMessage(0) : throwables[i].getMessage();
        }
        return msgs;
    }

    public Throwable getThrowable(int index) {
        if (index == 0) {
            return this.nestable;
        }
        return getThrowables()[index];
    }

    public int getThrowableCount() {
        return ExceptionUtils.getThrowableCount(this.nestable);
    }

    public Throwable[] getThrowables() {
        return ExceptionUtils.getThrowables(this.nestable);
    }

    public int indexOfThrowable(Class type, int fromIndex) {
        if (type == null) {
            return -1;
        }
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("The start index was out of bounds: ").append(fromIndex).toString());
        }
        Throwable[] throwables = ExceptionUtils.getThrowables(this.nestable);
        if (fromIndex >= throwables.length) {
            throw new IndexOutOfBoundsException(new StringBuffer().append("The start index was out of bounds: ").append(fromIndex).append(" >= ").append(throwables.length).toString());
        }
        int i;
        if (matchSubclasses) {
            for (i = fromIndex; i < throwables.length; i++) {
                if (type.isAssignableFrom(throwables[i].getClass())) {
                    return i;
                }
            }
        } else {
            for (i = fromIndex; i < throwables.length; i++) {
                if (type.equals(throwables[i].getClass())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream out) {
        synchronized (out) {
            PrintWriter pw = new PrintWriter(out, false);
            printStackTrace(pw);
            pw.flush();
        }
    }

    public void printStackTrace(PrintWriter out) {
        Throwable throwable = this.nestable;
        if (!ExceptionUtils.isThrowableNested()) {
            List stacks = new ArrayList();
            while (throwable != null) {
                stacks.add(getStackFrames(throwable));
                throwable = ExceptionUtils.getCause(throwable);
            }
            String separatorLine = "Caused by: ";
            if (!topDown) {
                separatorLine = "Rethrown as: ";
                Collections.reverse(stacks);
            }
            if (trimStackFrames) {
                trimStackFrames(stacks);
            }
            synchronized (out) {
                Iterator iter = stacks.iterator();
                while (iter.hasNext()) {
                    for (String println : (String[]) iter.next()) {
                        out.println(println);
                    }
                    if (iter.hasNext()) {
                        out.print(separatorLine);
                    }
                }
            }
        } else if (throwable instanceof Nestable) {
            ((Nestable) throwable).printPartialStackTrace(out);
        } else {
            throwable.printStackTrace(out);
        }
    }

    protected String[] getStackFrames(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        if (t instanceof Nestable) {
            ((Nestable) t).printPartialStackTrace(pw);
        } else {
            t.printStackTrace(pw);
        }
        return ExceptionUtils.getStackFrames(sw.getBuffer().toString());
    }

    protected void trimStackFrames(List stacks) {
        for (int i = stacks.size() - 1; i > 0; i--) {
            String[] curr = (String[]) stacks.get(i);
            String[] next = (String[]) stacks.get(i - 1);
            List currList = new ArrayList(Arrays.asList(curr));
            ExceptionUtils.removeCommonFrames(currList, new ArrayList(Arrays.asList(next)));
            int trimmed = curr.length - currList.size();
            if (trimmed > 0) {
                currList.add(new StringBuffer().append("\t... ").append(trimmed).append(" more").toString());
                stacks.set(i, currList.toArray(new String[currList.size()]));
            }
        }
    }
}
