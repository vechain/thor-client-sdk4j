package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.blockchain.Event;
import com.vechain.thorclient.core.model.blockchain.Transfer;
import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.ToClause;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;

import java.math.BigInteger;
import java.util.ArrayList;

public class AccountCallResult {
    private static final long serialVersionUID = -2695526756954990834L;
    private String data;
    private ArrayList<Event> events;
    private ArrayList<Transfer> transfers;

    private BigInteger gasUsed;
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

    public Amount getBalance(AbstractToken token) {
        Amount balance = Amount.createFromToken(token);
        balance.setHexAmount(data);
        return balance;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
    }

    /**
     * Utility to build an AccountCall object for /account endpoint.
     */
    public static AccountCall buildAccountCall(
            java.util.List<ToClause> clauses,
            long gas,
            String gasPrice,
            String caller,
            String provedWork,
            String gasPayer,
            long expiration,
            String blockRef) {
        AccountCall accountCall = new AccountCall();
        accountCall.setClauses(clauses != null ? new java.util.ArrayList<>(clauses) : new java.util.ArrayList<>());
        accountCall.setGas(gas);
        accountCall.setGasPrice(gasPrice);
        accountCall.setCaller(caller);
        accountCall.setProvedWork(provedWork);
        accountCall.setGasPayer(gasPayer);
        accountCall.setExpiration(expiration);
        accountCall.setBlockRef(blockRef);
        return accountCall;
    }

}
