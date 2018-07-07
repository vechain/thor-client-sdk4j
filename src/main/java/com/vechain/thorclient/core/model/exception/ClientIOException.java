package com.vechain.thorclient.core.model.exception;

import java.io.IOException;

/**
 * A exception wrapper for {@link IOException}. You can get http status from
 * {@link #getHttpStatus} method.
 */
public class ClientIOException extends ThorException {

    private static final long serialVersionUID = 7447437432956198947L;

    private int               httpStatus;

    public ClientIOException(Throwable ex) {
        super(ex);
        httpStatus = -1;
    }

    public ClientIOException(String message) {
        super(message);
        httpStatus = -1;
    }

    public ClientIOException(String message, int status) {
        super(message);
        httpStatus = -1;
    }

    /**
     * Get http status from exception.
     * <p>
     * http status 400 means bad request. Likes out
     * of gas, address can not be recovered, any request parameters parsing error
     * and etc.
     * </p>
     * <p>
     * http status 403 means request forbidden, like transaction pool is full,
     * transaction is expired etc.
     * </p>
     * http status 404 means api path is not existed.
     *
     * @return response http code, while it -1, then it is not http error happening.
     */
    public int getHttpStatus() {
        return this.httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
