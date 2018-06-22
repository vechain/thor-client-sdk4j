package com.vechain.thorclient.core.model.exception;

public class ClientArgumentException extends IllegalArgumentException {
    private ClientArgumentException(String description) {
        super(description);
    }

    public static ClientArgumentException exception(String description){
        return new ClientArgumentException( description );
    }
}
