package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.StringUtils;

import java.math.BigDecimal;

/**
 *
 */
public final class Balance {
    private AbstractToken abstractToken;
    private BigDecimal amount;

    /**
     * Create {@link Balance} from abstractToken
     * @param token {@link AbstractToken}
     * @return {@link Balance} object
     */
    public static Balance createFromToken(AbstractToken token){
        Balance balance = new Balance();
        balance.abstractToken = token;
        return balance;
    }

    private Balance(){
    }

    /**
     * Set hex string to abstractToken value.
     * @param hexAmount hex amount
     */
    public void setHexAmount(String hexAmount){
        if(!StringUtils.isHex( hexAmount )){
            throw ClientArgumentException.exception( "setHexValue argument hex value." );
        }
        String noPrefixAmount = StringUtils.sanitizeHex( hexAmount );
        amount = BlockchainUtils.balance( noPrefixAmount, abstractToken.getPrecision().intValue(), abstractToken.getPrecision().intValue() );
    }

    /**
     * Get amount
     * @return {@link BigDecimal} value.
     */
    public BigDecimal getAmount(){
        return  amount;
    }
}
