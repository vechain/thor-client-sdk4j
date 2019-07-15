package com.vechain.thorclient.clients;

import com.vechain.thorclient.core.model.clients.ToClause;

import java.util.ArrayList;

public class AccountCall {
    private ArrayList<ToClause> clauses;
    private long gas;
    private String gasPrice;
    private String caller;
    private String provedWork;
    private String gasPayer;
    private long expiration;
    private String blockRef;

    public ArrayList<ToClause> getClauses() {
        return clauses;
    }

    public void setClauses(ArrayList<ToClause> clauses) {
        this.clauses = clauses;
    }

    public long getGas() {
        return gas;
    }

    public void setGas(long gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getProvedWork() {
        return provedWork;
    }

    public void setProvedWork(String provedWork) {
        this.provedWork = provedWork;
    }

    public String getGasPayer() {
        return gasPayer;
    }

    public void setGasPayer(String gasPayer) {
        this.gasPayer = gasPayer;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getBlockRef() {
        return blockRef;
    }

    public void setBlockRef(String blockRef) {
        this.blockRef = blockRef;
    }
}
