package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.MaxFees;
import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;

import java.math.BigInteger;

public class RawTransactionFactory {

    private static final RawTransactionFactory INSTANCE = new RawTransactionFactory();

    /**
     * Create a legacy raw transaction.
     *
     * @param chainTag     byte the last byte of genesis block id.
     * @param blockRef     byte[] the first 8 bytes of the block id. Get from
     *                     {@link com.vechain.thorclient.core.model.clients.BlockRef} toByteArray().
     * @param expiration   the expiration of block size from best block to block reference.
     * @param gasInt       must >= 21000.
     * @param gasPriceCoef must > 0
     * @param nonce        eight bytes array, random by cryptography method.
     * @param toClauses    to clauses array.
     * @return {@link RawTransaction} raw transaction.
     * @throws IllegalArgumentException
     */
    public RawTransaction createRawTransaction(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gasInt,
            final byte gasPriceCoef,
            final byte[] nonce,
            final ToClause... toClauses
    ) throws IllegalArgumentException {
        return createRawTransaction(chainTag, blockRef, expiration, gasInt, gasPriceCoef, nonce, fillClauses(toClauses));
    }

    /**
     * Create a legacy raw transaction.
     *
     * @param chainTag     byte the last byte of genesis block id.
     * @param blockRef     byte[] the first 8 bytes of the block id. Get from
     *                     {@link com.vechain.thorclient.core.model.clients.BlockRef} toByteArray().
     * @param expiration   the expiration of block size from best block to block reference.
     * @param gasInt       must >= 21000.
     * @param gasPriceCoef must > 0
     * @param nonce        eight bytes array, random by cryptography method.
     * @param rawClauses   to clauses array.
     * @return {@link RawTransaction} raw transaction.
     * @throws IllegalArgumentException
     */
    public RawTransaction createRawTransaction(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gasInt,
            final byte gasPriceCoef,
            final byte[] nonce,
            final RawClause[] rawClauses
    ) throws IllegalArgumentException {
        if (chainTag == 0 || blockRef == null || expiration <= 0 || gasInt < 21000 || rawClauses == null) {
            throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
        }
        final RawTransactionBuilder builder = new RawTransactionBuilder();
        // chainTag
        builder.update(Byte.valueOf(chainTag), "chainTag");
        // blockRef
        final byte[] currentBlockRef = BytesUtils.trimLeadingZeroes(blockRef);
        builder.update(currentBlockRef, "blockRef");
        // expiration
        final byte[] expirationBytes = BytesUtils.longToBytes(expiration);
        builder.update(expirationBytes, "expiration");
        // clauses
        builder.update(rawClauses);
        // gasPriceCoef
        builder.update(Byte.valueOf(gasPriceCoef), "gasPriceCoef");
        // gas
        final byte[] gas = BytesUtils.longToBytes(gasInt);
        builder.update(gas, "gas");
        // nonce
        final byte[] trimedNonce = BytesUtils.trimLeadingZeroes(nonce);
        builder.update(trimedNonce, "nonce");
        return builder.build();
    }

    /**
     * Creates a raw transaction.
     *
     * @param chainTag The chain identifier byte, typically derived from the genesis block ID.
     * @param blockRef A byte array containing the first 8 bytes of the block ID, representing the block reference.
     * @param expiration The block expiration value, indicating the number of blocks from the block reference within which the transaction is valid.
     * @param gas The gas limit for the transaction, must be greater than or equal to 21000.
     * @param maxPriorityFeePerGas The maximum priority fee in wei units for miners, used in EIP-1559 transactions.
     * @param maxFeePerGas The maximum fee per gas in wei units allowed for the transaction, used in EIP-1559 transactions.
     * @param nonce A unique 8-byte cryptographically generated random number to ensure transaction uniqueness.
     * @param toClauses An array of {@link ToClause} objects defining the recipient(s), value(s), and data for the transaction.
     * @return A {@link RawTransaction} object representing the created transaction.
     */
    public RawTransaction createRawTransaction(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gas,
            final BigInteger maxPriorityFeePerGas,
            final BigInteger maxFeePerGas,
            final byte[] nonce,
            final ToClause... toClauses
    ) {
        return createRawTransaction(chainTag, blockRef, expiration, gas, maxPriorityFeePerGas, maxFeePerGas, nonce, fillClauses(toClauses));
    }

    /**
     * Creates a raw transaction.
     *
     * @param chainTag The chain identifier byte, typically derived from the genesis block ID.
     * @param blockRef A byte array containing the first 8 bytes of the block ID, representing the block reference.
     * @param expiration The block expiration value, indicating the number of blocks from the block reference within which the transaction is valid.
     * @param gas The gas limit for the transaction, must be greater than or equal to 21000.
     * @param maxPriorityFeePerGas The maximum priority fee in wei for miners, used in EIP-1559 transactions.
     * @param maxFeePerGas The maximum fee per gas in wei allowed for the transaction, used in EIP-1559 transactions.
     * @param nonce A unique 8-byte cryptographically generated random number to ensure transaction uniqueness.
     * @param rawClauses An array of {@link RawClause} objects defining the recipients, values, and data for the transaction.
     * @return A {@link RawTransaction} object representing the created transaction.
     * @throws IllegalArgumentException If any of the parameters are invalid, such as a null blockRef,
     *                                  an expiration less than or equal to 0, a gas value less than 21000,
     *                                  negative maxPriorityFeePerGas or maxFeePerGas, or a null rawClauses array.
     */
    public RawTransaction createRawTransaction(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gas,
            final BigInteger maxPriorityFeePerGas,
            final BigInteger maxFeePerGas,
            final byte[] nonce,
            final RawClause[] rawClauses
    ) {
        if (chainTag == 0 || blockRef == null || expiration <= 0 || gas < 21000 || rawClauses == null) {
            throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
        }
        // get user supplied or computed max fees
        MaxFees userProvidedMaxFees = new MaxFees(maxFeePerGas, maxPriorityFeePerGas);
        MaxFees maxFeeSettings = MaxFeeCalculator.calculateMaxFees(userProvidedMaxFees);
        // build the tx
        final RawTransactionBuilder builder = new RawTransactionBuilder();
        // chainTag
        builder.update(Byte.valueOf(chainTag), "chainTag");
        // blockRef
        builder.update(BytesUtils.trimLeadingZeroes(blockRef), "blockRef");
        // expiration
        builder.update(BytesUtils.longToBytes(expiration), "expiration");
        // clauses
        builder.update(rawClauses);
        // maxPriorityFeePerGas
        builder.update(BytesUtils.trimLeadingZeroes(maxFeeSettings.maxPriorityFeePerGas.toByteArray()), "maxPriorityFeePerGas");
        // maxFeePerGas
        builder.update(BytesUtils.trimLeadingZeroes(maxFeeSettings.maxFeePerGas.toByteArray()), "maxFeePerGas");
        // gas
        builder.update(BytesUtils.longToBytes(gas), "gas");
        // nonce
        final byte[] trimedNonce = BytesUtils.trimLeadingZeroes(nonce);
        builder.update(trimedNonce, "nonce");
        return builder.build();

    }

    public static RawTransactionFactory getInstance() {
        return INSTANCE;
    }

    private static RawClause[] fillClauses(final ToClause... toClauses) {
        final RawClause[] rawClauses = new RawClause[toClauses.length];
        int index = 0;
        for (ToClause clause : toClauses) {
            rawClauses[index] = new RawClause();
            rawClauses[index].setTo(clause.getTo().toByteArray());
            rawClauses[index].setValue(clause.getValue().toByteArray());
            rawClauses[index].setData(clause.getData().toByteArray());
            index++;
        }
        return rawClauses;
    }


}
