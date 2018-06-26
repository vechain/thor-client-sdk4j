package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceiptOutput implements Serializable {
    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public ArrayList<ReceiptEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<ReceiptEvent> events) {
        this.events = events;
    }

    public ArrayList<ReceiptTransfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(ArrayList<ReceiptTransfer> transfers) {
        this.transfers = transfers;
    }

    private String contractAddress;
    private ArrayList<ReceiptEvent> events;
    private ArrayList<ReceiptTransfer> transfers;
}
