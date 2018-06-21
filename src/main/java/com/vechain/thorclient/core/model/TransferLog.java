package com.vechain.thorclient.core.model;

public class TransferLog {
    private String sender;
    private String recipient;
    private String value;
    private BlockContext block;
    private TxContext tx;

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

    public BlockContext getBlock() {
        return block;
    }

    public void setBlock(BlockContext block) {
        this.block = block;
    }

    public TxContext getTx() {
        return tx;
    }

    public void setTx(TxContext tx) {
        this.tx = tx;
    }
}
