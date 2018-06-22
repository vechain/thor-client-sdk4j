package com.vechain.thorclient.core.model.blockchain;

public class ReceiptOutput {
    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public ReceiptEvent[] getEvents() {
        return events;
    }

    public void setEvents(ReceiptEvent[] events) {
        this.events = events;
    }

    public ReceiptTransfer[] getTransfers() {
        return transfers;
    }

    public void setTransfers(ReceiptTransfer[] transfers) {
        this.transfers = transfers;
    }

    private String contractAddress;
    private ReceiptEvent[] events;
    private ReceiptTransfer[] transfers;
}
