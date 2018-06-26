package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public enum Order implements Serializable {
    DESC("DESC"),
    ASC("ASC");

    private final String value;

    Order(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
