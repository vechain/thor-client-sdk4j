package com.vechain.thorclient.core.model.exception;

public class ClientArgumentException extends InvalidArgumentException {

    private ClientArgumentException(String description) {
        super(description);
    }

    public static ClientArgumentException exception(String description) {
        return new ClientArgumentException(description);
    }
}
