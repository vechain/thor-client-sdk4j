package com.vechain.thorclient.core.model.clients;

public class ToClause {
    private  Address to;
    private  Amount value;
    private  ToData data;
    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }

    public Amount getValue() {
        return value;
    }

    public void setValue(Amount value) {
        this.value = value;
    }

    public ToData getData() {
        return data;
    }

    public void setData(ToData data) {
        this.data = data;
    }

    public ToClause(Address to, Amount value, ToData data){
        this.to = to;
        this.value = value;
        this.data = data;
    }


}
