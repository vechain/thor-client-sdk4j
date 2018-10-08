package com.vechain.thorclient.core.model.blockchain;

import java.util.ArrayList;

public class EventSubscribingResponse {
    private String address;
    private ArrayList<String> topics;
    private String data;
    private boolean obsolete;
    private LogMeta meta;

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

    public LogMeta getMeta() {
        return meta;
    }

    public void setMeta(LogMeta meta) {
        this.meta = meta;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }
}
