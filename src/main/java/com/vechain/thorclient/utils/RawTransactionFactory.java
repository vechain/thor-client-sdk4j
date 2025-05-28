package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;

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

    public RawTransaction createRawTransaction(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gas,
            final long maxPriorityFeePerGas,
            final long maxFeePerGas,
            final byte[] nonce,
            final ToClause... toClauses
    ) {
        return createRawTransaction(chainTag, blockRef, expiration, gas, maxPriorityFeePerGas, maxFeePerGas, nonce, fillClauses(toClauses));
    }

    public RawTransaction createRawTransaction(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gas,
            final long maxPriorityFeePerGas,
            final long maxFeePerGas,
            final byte[] nonce,
            final RawClause[] rawClauses
    ) {
        if (chainTag == 0 || blockRef == null || expiration <= 0 || gas < 21000 || maxPriorityFeePerGas < 0 || maxFeePerGas < 0 || rawClauses == null) {
            throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
        }
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
        builder.update(BytesUtils.longToBytes(maxPriorityFeePerGas), "maxPriorityFeePerGas");
        // maxFeePerGas
        builder.update(BytesUtils.longToBytes(maxFeePerGas), "maxFeePerGas");
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
