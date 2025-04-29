package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

/**
 * [Clause](http://localhost:8669/doc/stoplight-ui/#/schemas/Clause)
 *
 * @version galactica
 */
public class Clause implements Serializable {
    private String to; //to address
    private String value; // hex form of coin transferred
    private String data;

    public Clause() {

    }

    public Clause(String to, String value, String data) {
        this.to = to;
        this.value = value;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
