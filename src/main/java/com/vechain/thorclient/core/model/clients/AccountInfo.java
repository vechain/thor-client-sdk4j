package com.vechain.thorclient.core.model.clients;

import java.util.ArrayList;

public class AccountInfo {
    ArrayList<Balance> balances;
    boolean hasCode;

    public ArrayList<Balance> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<Balance> balances) {
        this.balances = balances;
    }

    public boolean isHasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }
}
