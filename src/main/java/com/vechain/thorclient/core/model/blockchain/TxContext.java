package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class TxContext implements Serializable {
    private String id;
    private String origin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
