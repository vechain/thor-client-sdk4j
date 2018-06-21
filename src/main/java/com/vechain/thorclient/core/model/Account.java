package com.vechain.thorclient.core.model;

public class Account {
    /**
     * get hex string of balance defaultDecimalStringToByteArray, need to change to byte array.
     * Check the {@link com.vechain.thorclient.utils.BytesUtils} for amount
     * @return
     */
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getHasCode() {
        return hasCode;
    }

    public void setHasCode(String hasCode) {
        this.hasCode = hasCode;
    }

    private String balance;

    private String energy;

    private String hasCode;

}
