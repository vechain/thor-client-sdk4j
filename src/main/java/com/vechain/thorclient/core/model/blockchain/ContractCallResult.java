package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;

import java.io.Serializable;
import java.util.ArrayList;

public class ContractCallResult implements Serializable {

    private String data;
    private ArrayList<Event> events;
    private ArrayList<Transfer> transfers;

    private long gasUsed;
    private boolean reverted;
    private String vmError;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public boolean isReverted() {
        return reverted;
    }

    public void setReverted(boolean reverted) {
        this.reverted = reverted;
    }

    public String getVmError() {
        return vmError;
    }

    public void setVmError(String vmError) {
        this.vmError = vmError;
    }

    public Amount getBalance(AbstractToken token){
        Amount balance = Amount.createFromToken( token );
        balance.setHexAmount( data );
        return balance;
    }

}
