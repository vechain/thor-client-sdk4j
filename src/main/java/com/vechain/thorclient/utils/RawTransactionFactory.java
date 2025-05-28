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
        // clause
        RawClause[] rawClauses = new RawClause[toClauses.length];
        int index = 0;
        for (ToClause clause : toClauses) {
            rawClauses[index] = new RawClause();
            rawClauses[index].setTo(clause.getTo().toByteArray());
            rawClauses[index].setValue(clause.getValue().toByteArray());
            rawClauses[index].setData(clause.getData().toByteArray());
            index++;
        }
        return createRawTransaction(chainTag, blockRef, expiration, gasInt, gasPriceCoef, nonce, rawClauses);
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

        // Expiration
        final byte[] expirationBytes = BytesUtils.longToBytes(expiration);
        builder.update(expirationBytes, "expiration");

        // BlockRef
        final byte[] currentBlockRef = BytesUtils.trimLeadingZeroes(blockRef);
        builder.update(currentBlockRef, "blockRef");

        // Nonce
        final byte[] trimedNonce = BytesUtils.trimLeadingZeroes(nonce);
        builder.update(trimedNonce, "nonce");

        // gas
        final byte[] gas = BytesUtils.longToBytes(gasInt);
        builder.update(gas, "gas");

        // gasPriceCoef
        builder.update(Byte.valueOf(gasPriceCoef), "gasPriceCoef");

        // update the clause
        builder.update(rawClauses);
        return builder.build();
    }

    public static RawTransactionFactory getInstance() {
        return INSTANCE;
    }

}
