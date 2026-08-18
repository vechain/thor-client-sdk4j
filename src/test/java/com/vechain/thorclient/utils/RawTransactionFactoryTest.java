package com.vechain.thorclient.utils;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;

/**
 * Covers the per-transaction gas limit cap introduced by EIP-7825 (Interstellar).
 *
 * <p>These cases are offline on purpose: the cap is checked before any fee lookup, so no
 * thor node is needed to exercise it.</p>
 */
@RunWith(JUnit4.class)
public class RawTransactionFactoryTest extends BaseTest {

    private static final byte CHAIN_TAG = (byte) 0x27;
    private static final byte[] BLOCK_REF = BytesUtils.toByteArray("0x00000000aabbccdd");
    private static final byte[] NONCE = BytesUtils.toByteArray("0x0000000000000001");

    private static RawClause[] clauses() {
        RawClause[] clauses = new RawClause[1];
        clauses[0] = new RawClause();
        clauses[0].setTo(BytesUtils.toByteArray("0x42191bd624aBffFb1b65e92F1E51EB16f4d2A3Ce"));
        clauses[0].setValue(BytesUtils.defaultDecimalStringToByteArray("1"));
        clauses[0].setData(new byte[0]);
        return clauses;
    }

    @Test
    public void maxTransactionGasIsTwoToThePowerOf24() {
        Assert.assertEquals(16777216, RawTransactionFactory.MAX_TRANSACTION_GAS);
    }

    @Test
    public void legacyTransactionAtGasCapIsAccepted() {
        RawTransaction rawTransaction = RawTransactionFactory.getInstance().createRawTransaction(
                CHAIN_TAG,
                BLOCK_REF,
                720,
                RawTransactionFactory.MAX_TRANSACTION_GAS,
                (byte) 0x0,
                NONCE,
                clauses());
        Assert.assertEquals(
                RawTransactionFactory.MAX_TRANSACTION_GAS,
                new BigInteger(1, rawTransaction.getGas()).intValue());
    }

    @Test
    public void legacyTransactionAboveGasCapIsRejected() {
        try {
            RawTransactionFactory.getInstance().createRawTransaction(
                    CHAIN_TAG,
                    BLOCK_REF,
                    720,
                    RawTransactionFactory.MAX_TRANSACTION_GAS + 1,
                    (byte) 0x0,
                    NONCE,
                    clauses());
            Assert.fail("Expected the gas limit above the EIP-7825 cap to be rejected.");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(
                    "Unexpected message: " + e.getMessage(),
                    e.getMessage().contains("EIP-7825"));
        }
    }

    @Test
    public void eip1559TransactionAboveGasCapIsRejected() {
        try {
            RawTransactionFactory.getInstance().createRawTransaction(
                    CHAIN_TAG,
                    BLOCK_REF,
                    720,
                    RawTransactionFactory.MAX_TRANSACTION_GAS + 1,
                    BigInteger.valueOf(100L),
                    BigInteger.valueOf(100000L),
                    NONCE,
                    clauses());
            Assert.fail("Expected the gas limit above the EIP-7825 cap to be rejected.");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(
                    "Unexpected message: " + e.getMessage(),
                    e.getMessage().contains("EIP-7825"));
        }
    }
}
