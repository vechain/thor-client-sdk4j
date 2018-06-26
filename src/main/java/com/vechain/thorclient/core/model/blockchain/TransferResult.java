package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class TransferResult implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
