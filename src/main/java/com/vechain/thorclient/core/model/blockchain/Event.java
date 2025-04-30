package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * [Event](http://localhost:8669/doc/stoplight-ui/#/schemas/Event)
 *
 * @version galactica
 */
public class Event implements Serializable {
    private String address;
    private ArrayList<String> topics;
    private String data;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
