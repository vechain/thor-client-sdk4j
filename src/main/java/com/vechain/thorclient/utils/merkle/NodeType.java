package com.vechain.thorclient.utils.merkle;

public enum NodeType{
    single(0, "single node"),
    left(1, "left node"),
    right(2, "right node"),
    ;

    private int value;
    private String description;

    public int getValue() {
        return value;
    }
    public String getDescription() {
        return description;
    }
    NodeType(int value, String description){
        this.value = value;
        this.description = description;
    }


}