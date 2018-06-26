package com.vechain.thorclient.core.model.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThorException extends RuntimeException {

    private static final long serialVersionUID = -4699996009743045162L;

    protected Logger          logger           = LoggerFactory.getLogger(this.getClass());

    public ThorException() {
        super();
        logger.error("error occurs.");
    }

    public ThorException(String message) {
        super(message);
        logger.error(message);
    }

    public ThorException(Throwable cause) {
        super(cause);
        logger.error(cause.getMessage());
    }
}
