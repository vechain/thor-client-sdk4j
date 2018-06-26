package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class ContractCall implements Serializable {
    private String value;
    private String data;
    private String gas;
    private String gasPrice;
    private String caller;

    public ContractCall(){
        value = "0x0";
        data = "0x0";
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
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
