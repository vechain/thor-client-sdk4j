package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class StorageData implements Serializable {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
