package com.vechain.thorclient.core.model.exception;

public class ThorException extends RuntimeException {

    public ThorException() {
        super();
    }

    public ThorException(String message) {
        super(message);
    }

    public ThorException(Throwable cause) {
        super(cause);
    }
}
