package org.apache.commons.lang;

public class NullArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1174360235354917591L;

    public NullArgumentException(String argName) {
        StringBuffer stringBuffer = new StringBuffer();
        if (argName == null) {
            argName = "Argument";
        }
        super(stringBuffer.append(argName).append(" must not be null.").toString());
    }
}
