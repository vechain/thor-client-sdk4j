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
 * Represents token amounts with decimal precision for VeChain transactions.
 * 
 * <p>Amount handles token values with proper decimal formatting, supporting both 
 * native VET and ERC20 tokens like VTHO. It provides conversion between decimal 
 * representations and wei (smallest unit) values.</p>
 * 
 * <h3>Supported Tokens</h3>
 * <ul>
 *   <li><strong>VET</strong>: Native VeChain token (18 decimals)</li>
 *   <li><strong>VTHO</strong>: VeChain energy token (18 decimals)</li>
 *   <li><strong>Custom ERC20</strong>: Any ERC20 token with configurable decimals</li>
 * </ul>
 * 
 * <h3>Usage Examples</h3>
 * <pre>{@code
 * // Create VET amount
 * Amount vetAmount = Amount.createFromToken(AbstractToken.VET);
 * vetAmount.setDecimalAmount("10.5");
 * System.out.println("VET: " + vetAmount.getDecimalAmount());
 * 
 * // Create VTHO amount
 * Amount vthoAmount = Amount.createFromToken(ERC20Token.VTHO);
 * vthoAmount.setDecimalAmount("1000");
 * 
 * // Convert to wei (BigInteger)
 * BigInteger weiValue = vetAmount.toBigInteger();
 * 
 * // Use zero amount
 * Amount zero = Amount.ZERO;
 * 
 * // Convenient factory methods
 * Amount vet = Amount.VET().setDecimalAmount("5.0");
 * Amount vtho = Amount.VTHO().setDecimalAmount("100");
 * }</pre>
 * 
 * @see AbstractToken
 * @see ERC20Token
 * @see ToClause
 * @since 0.1.0
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
