package com.vechain.thorclient.core.model.blockchain;

public enum Order {
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
