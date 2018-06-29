package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class FilteredTransfer implements Serializable {
    private String sender;
    private String recipient;
    private String value;
    private LogMeta meta;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }




    public LogMeta getMeta() {
        return meta;
    }

    public void setMeta(LogMeta meta) {
        this.meta = meta;
    }
}
