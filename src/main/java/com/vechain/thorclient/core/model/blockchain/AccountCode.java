package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class AccountCode implements Serializable {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
