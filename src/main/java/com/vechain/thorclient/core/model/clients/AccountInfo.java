package com.vechain.thorclient.core.model.clients;

import java.util.ArrayList;

public class AccountInfo {
    ArrayList<Amount> balances;
    boolean hasCode;

    public ArrayList<Amount> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<Amount> balances) {
        this.balances = balances;
    }

    public boolean isHasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }
}
