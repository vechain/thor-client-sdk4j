package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.StringUtils;

import java.math.BigDecimal;

/**
 *
 */
public  class Amount {
    public static final Amount ZERO = new Zero();

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
        amount = BlockchainUtils.amount( noPrefixAmount, abstractToken.getPrecision().intValue(), abstractToken.getPrecision().intValue() );
    }

    /**
     * Set decimal amount string
     * @param decimalAmount decimal amount string.
     */
    public void setDecimalAmount(String decimalAmount){
        if(StringUtils.isBlank( decimalAmount )){
            throw new IllegalArgumentException( "Decimal amount string is blank" );
        }
        amount = new BigDecimal( decimalAmount );
    }


    /**
     * Get amount
     * @return {@link BigDecimal} value.
     */
    public BigDecimal getAmount(){
        return  amount;
    }

    /**
     * Convert to byte array.
     * @return byte[]
     */
    public byte[] toByteArray(){
        return BlockchainUtils.byteArrayAmount( amount, abstractToken.getPrecision().intValue() );
    }

    private static class Zero extends Amount{
        public byte[] toByteArray(){
            return  new byte[]{};
        }
        public BigDecimal getAmount(){
            return  new BigDecimal( 0 );
        }
    }

}
