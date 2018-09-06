package com.vechain.thorclient.core.model.blockchain;

public class TransferSubscribingRequest extends WSRequest{
    private String pos;
    private String txOrigin;
    private String sender;
    private String recipient;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getTxOrigin() {
        return txOrigin;
    }

    public void setTxOrigin(String txOrigin) {
        this.txOrigin = txOrigin;
    }

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


}
