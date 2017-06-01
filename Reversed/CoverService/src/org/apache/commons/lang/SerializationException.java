package org.apache.commons.lang;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class SerializationException extends NestableRuntimeException {
    private static final long serialVersionUID = 4029025366392702726L;

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
