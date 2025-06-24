package com.vechain.thorclient.core.model.clients;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonValue;
import com.vechain.thorclient.core.model.clients.base.AbstractToken;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import com.vechain.thorclient.utils.BlockchainUtils;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;
import com.vechain.thorclient.utils.StringUtils;

/**
 * Amount for {@link ToClause} to use.
 */
public class Amount {
    /**
     * If you need to send amount 0.
     */
    public static final Amount ZERO = new Zero();

    private AbstractToken abstractToken;
    private BigDecimal amount;

    /**
     * Create {@link Amount} from abstractToken
     *
     * @param token {@link AbstractToken}
     * @return {@link Amount} object
     */
    public static Amount createFromToken(AbstractToken token) {
        Amount amount = new Amount();
        amount.abstractToken = token;
        return amount;
    }

    /**
     * Create a VET amount
     *
     * @return {@link Amount}
     */
    public static Amount VET() {
        return Amount.createFromToken(AbstractToken.VET);
    }

    /**
     * Create a VTHO amount
     *
     * @return {@link Amount}
     */
    public static Amount VTHO() {
        return Amount.createFromToken(ERC20Token.VTHO);
    }

    private Amount() {
    }

    /**
     * Set hex string to abstractToken value.
     *
     * @param hexAmount hex amount with "0x", if it is 0, use {@link Amount#ZERO}
     *                  constant
     *                  instance.
     * @return this;
     * @throws ClientArgumentException if `hexAmount` is an invalid hexadecimal
     *                                 expression;
     */
    public Amount setHexAmount(String hexAmount) {
        if (!StringUtils.isHex(hexAmount)) {
            throw ClientArgumentException.exception("setHexValue argument hex value.");
        }
        String noPrefixAmount = StringUtils.sanitizeHex(hexAmount);
        amount = BlockchainUtils.amount(noPrefixAmount, abstractToken.getPrecision().intValue(),
                abstractToken.getScale().intValue());
        return this;
    }

    /**
     * Set a decimal amount string
     *
     * @param decimalAmount decimal amount string.
     * @return this
     */
    public Amount setDecimalAmount(String decimalAmount) {
        if (StringUtils.isBlank(decimalAmount)) {
            throw new IllegalArgumentException("Decimal amount string is blank");
        }
        amount = new BigDecimal(decimalAmount);
        return this;
    }

    public String toHexString() {
        BigDecimal fullDecimal = amount.multiply(BigDecimal.TEN.pow(abstractToken.getPrecision().intValue()));
        byte[] bytes = BytesUtils.trimLeadingZeroes(fullDecimal.toBigInteger().toByteArray());
        return BytesUtils.toHexString(bytes, Prefix.ZeroLowerX);
    }

    public BigInteger toBigInteger() {
        BigDecimal fullDecimal = amount.multiply(BigDecimal.TEN.pow(abstractToken.getPrecision().intValue()));
        return fullDecimal.toBigInteger();
    }

    public AbstractToken getAbstractToken() {
        return abstractToken;
    }

    /**
     * Get amount
     *
     * @return {@link BigDecimal} value.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Convert to a byte array.
     *
     * @return byte[]
     */
    public byte[] toByteArray() {
        return BlockchainUtils.byteArrayAmount(amount, abstractToken.getPrecision().intValue());
    }

    private static class Zero extends Amount {

        public byte[] toByteArray() {
            return new byte[] {};
        }

        public BigDecimal getAmount() {
            return new BigDecimal(0);
        }
    }

    @JsonValue
    public String toHexAmount() {
        if (this == ZERO) {
            return "0x0";
        }
        return this.toHexString();
    }

}
