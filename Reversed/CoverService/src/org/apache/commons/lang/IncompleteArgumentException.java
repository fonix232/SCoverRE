package org.apache.commons.lang;

import java.util.Arrays;

public class IncompleteArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = 4954193403612068178L;

    public IncompleteArgumentException(String argName) {
        super(new StringBuffer().append(argName).append(" is incomplete.").toString());
    }

    public IncompleteArgumentException(String argName, String[] items) {
        super(new StringBuffer().append(argName).append(" is missing the following items: ").append(safeArrayToString(items)).toString());
    }

    private static final String safeArrayToString(Object[] array) {
        return array == null ? null : Arrays.asList(array).toString();
    }
}
