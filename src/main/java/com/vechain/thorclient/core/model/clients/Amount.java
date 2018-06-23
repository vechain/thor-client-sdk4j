package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.StringUtils;

import java.math.BigDecimal;

/**
 *
 */
public final class Amount {
    private AbstractToken abstractToken;
    private BigDecimal amount;

    /**
     * Create {@link Amount} from abstractToken
     * @param token {@link AbstractToken}
     * @return {@link Amount} object
     */
    public static Amount createFromToken(AbstractToken token){
        Amount amount = new Amount();
        amount.abstractToken = token;
        return amount;
    }

    private Amount(){
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
