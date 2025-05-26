package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.RawTransactionEIP1559;
import com.vechain.thorclient.core.model.clients.ToClause;

public class RawTransactionFactory {

    private static final RawTransactionFactory INSTANCE = new RawTransactionFactory();

    /**
     * Create a legacy raw transaction.
     *
     * @param chainTag     byte the last byte of genesis block id.
     * @param blockRef     byte[] the first 8 bytes of the block id. Get from
     *                     {@link com.vechain.thorclient.core.model.clients.BlockRef} toByteArray().
     * @param expiration   the expiration of block size from best-block to block reference.
     * @param gasInt       must >= 21000.
     * @param gasPriceCoef must > 0
     * @param nonce        eight bytes array, random by cryptography method.
     * @param toClauses    to clauses array.
     * @return {@link RawTransaction} raw transaction.
     * @throws IllegalArgumentException if any argument is illegal.
     */
    public RawTransaction createRawTransaction(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gasInt,
            final byte gasPriceCoef,
            final byte[] nonce, ToClause... toClauses
    ) throws IllegalArgumentException {
        final RawClause[] rawClauses = new RawClause[toClauses.length];
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
     * Create legacy raw transaction.
     *
     * @param chainTag     byte the last byte of genesis block id.
     * @param blockRef     byte[] the first 8 bytes of the block id. Get from
     *                     {@link com.vechain.thorclient.core.model.clients.BlockRef} toByteArray().
     * @param expiration   the expiration of block size from best-block to block reference.
     * @param gasInt       must >= 21000.
     * @param gasPriceCoef must > 0
     * @param nonce        eight bytes array, random by cryptography method.
     * @param rawClauses   clauses array.
     * @return {@link RawTransaction} raw transaction.
     * @throws IllegalArgumentException if any argument is illegal.
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
        final RawTransactionBuilder builder = createRawTransactionBuilder(chainTag, blockRef, expiration, gasInt, nonce);
        // gasPriceCoef
        builder.update(Byte.valueOf(gasPriceCoef), "gasPriceCoef");
        // update the clause
        if (rawClauses == null || rawClauses.length == 0) {
            throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
        }
        builder.update(rawClauses);
        return builder.build();
    }

    /**
     * Create EIP1559 dynamic fee raw transaction.
     *
     * @param chainTag             byte the last byte of genesis block id.
     * @param blockRef             byte[] the first 8 bytes of the block id. Get from
     *                             {@link com.vechain.thorclient.core.model.clients.BlockRef} toByteArray().
     * @param expiration           the expiration of block size from best-block to block reference.
     * @param gasInt               must >= 21000.
     * @param maxPriorityFeePerGas the maximum amount that can be tipped to the validato
     * @param maxFeePerGas         the maximum amount that can be spent to pay for base fee and priority fee
     * @param nonce                eight bytes array, random by cryptography method.
     * @param toClauses            to clauses array.
     * @throws IllegalArgumentException if any argument is illegal.
     */
    public RawTransactionEIP1559 createRawTransactionEIP1559(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gasInt,
            final long maxPriorityFeePerGas,
            final long maxFeePerGas,
            final byte[] nonce, ToClause... toClauses
    ) throws IllegalArgumentException {
        final RawClause[] rawClauses = new RawClause[toClauses.length];
        int index = 0;
        for (ToClause clause : toClauses) {
            rawClauses[index] = new RawClause();
            rawClauses[index].setTo(clause.getTo().toByteArray());
            rawClauses[index].setValue(clause.getValue().toByteArray());
            rawClauses[index].setData(clause.getData().toByteArray());
            index++;
        }
        return createRawTransactionEIP1559(chainTag, blockRef, expiration, gasInt, maxPriorityFeePerGas, maxFeePerGas, nonce, rawClauses);
    }

    /**
     * Create EIP1559 dynamic fee raw transaction.
     *
     * @param chainTag             byte the last byte of genesis block id.
     * @param blockRef             byte[] the first 8 bytes of the block id. Get from
     *                             {@link com.vechain.thorclient.core.model.clients.BlockRef} toByteArray().
     * @param expiration           the expiration of block size from best-block to block reference.
     * @param gasInt               must >= 21000.
     * @param maxPriorityFeePerGas the maximum amount that can be tipped to the validato
     * @param maxFeePerGas         the maximum amount that can be spent to pay for base fee and priority fee
     * @param nonce                eight bytes array, random by cryptography method.
     * @param rawClauses           clauses array.
     * @return {@link RawTransaction}   raw transaction.
     * @throws IllegalArgumentException if any argument is illegal.
     */
    public RawTransactionEIP1559 createRawTransactionEIP1559(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gasInt,
            final long maxPriorityFeePerGas,
            final long maxFeePerGas,
            final byte[] nonce,
            final RawClause[] rawClauses
    ) throws IllegalArgumentException {
        final RawTransactionBuilder builder = createRawTransactionBuilder(chainTag, blockRef, expiration, gasInt, nonce);
        // maxPriorityFeePerGas
        builder.update(BytesUtils.longToBytes(maxPriorityFeePerGas), "maxPriorityFeePerGas");
        // maxFeePerGas
        builder.update(BytesUtils.longToBytes(maxFeePerGas), "maxFeePerGas");
        // update the clause
        if (rawClauses == null || rawClauses.length == 0) {
            throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
        }
        builder.update(rawClauses);
        return builder.buildEIP1559();
    }

    private static RawTransactionBuilder createRawTransactionBuilder(
            final byte chainTag,
            final byte[] blockRef,
            final int expiration,
            final int gasInt,
            final byte[] nonce
    ) {
        if (chainTag == 0 || blockRef == null || expiration <= 0 || gasInt < 21000) {
            throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
        }
        final RawTransactionBuilder builder = new RawTransactionBuilder();
        // chainTag
        builder.update(Byte.valueOf(chainTag), "chainTag");
        // blockRef
        builder.update(BytesUtils.trimLeadingZeroes(blockRef), "blockRef");
        // expiration
        builder.update(BytesUtils.longToBytes(expiration), "expiration");
        // gas
        builder.update(BytesUtils.longToBytes(gasInt), "gas");
        // nonce
        builder.update(BytesUtils.trimLeadingZeroes(nonce), "nonce");
        return builder;
    }


    public static RawTransactionFactory getInstance() {
        return INSTANCE;
    }

}
