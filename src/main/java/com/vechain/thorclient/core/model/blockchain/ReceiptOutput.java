package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * [GetTxReceiptResponse.outputs](http://localhost:8669/doc/stoplight-ui/#/schemas/GetTxReceiptResponse)
 *
 * @version galactica
 */
public class ReceiptOutput implements Serializable {
    private String contractAddress;
    private ArrayList<Event> events;
    private ArrayList<Transfer> transfers;

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(ArrayList<Transfer> transfers) {
        this.transfers = transfers;
    }

}
