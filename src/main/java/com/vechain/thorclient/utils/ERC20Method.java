package com.vechain.thorclient.utils;

public enum ERC20Method {
    TRANSFER("transfer", "a9059cbb"),
    BLANCEOF("balanceOf","70a08231"),
    ;
    private final String value;
    private final String name;

    ERC20Method(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
