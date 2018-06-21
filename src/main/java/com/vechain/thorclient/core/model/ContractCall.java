package com.vechain.thorclient.core.model;

public class ContractCall {
    private String value;
    private String data;

    public ContractCall(){
        value = "0x0";
        data = "0x0";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
