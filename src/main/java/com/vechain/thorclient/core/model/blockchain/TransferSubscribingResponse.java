package com.vechain.thorclient.core.model.blockchain;

public class TransferSubscribingResponse {
    private String sender;
    private String recipient;
    private String amount;
    private LogMeta meta;
    private boolean obsolete;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LogMeta getMeta() {
        return meta;
    }

    public void setMeta(LogMeta meta) {
        this.meta = meta;
    }

    public boolean isObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }
}
