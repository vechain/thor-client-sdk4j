package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.core.model.clients.Amount;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.clients.ERC20Token;

import java.io.Serializable;

/**
 * The Account information.
 */
public class Account implements Serializable {

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

    public boolean getHasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }

    /**
     * Get VET token {@link Balance} object
     * @return
     */
    public Amount VETBalance(){
        Amount balance = Amount.createFromToken( AbstractToken.VET );
        balance.setHexAmount( this.balance );
        return balance;
    }

    /**
     * On VeChain mainnet, it has two native currencies, one is VET, the other is VTHO
     * @return
     */
    public Amount energyBalance(){
        Amount balance = Amount.createFromToken( ERC20Token.VTHO );
        balance.setHexAmount( this.energy );
        return balance;
    }

    private String balance;

    private String energy;

    private boolean hasCode;

}
