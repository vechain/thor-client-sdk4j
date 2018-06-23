package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;

public class RawTransactionFactory {


    private static RawTransactionFactory INSTANCE = new RawTransactionFactory();


    /**
     * create raw transaction.
     * @param chainTag byte the last byte of genesis block id.
     * @param blockRef byte[] the first 8 bytes of the block id.  Get from {@link com.vechain.thorclient.core.model.clients.BlockRef} toByteArray().
     * @param expiration the expiration of block size from best block to block reference.
     * @param gasInt must >= 21000.
     * @param gasPriceCoef must > 0
     * @param nonce  eight bytes array, random by cryptography method.
     * @param toClauses to clauses array.
     * @return {@link RawTransaction} raw transaction.
     * @throws IllegalArgumentException
     */
    public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gasInt, byte gasPriceCoef, byte[] nonce, ToClause ...toClauses) throws IllegalArgumentException{
        if(chainTag == 0
                || blockRef == null
                || expiration <= 0
                || gasInt < 21000
                || gasPriceCoef <= 0
                || toClauses == null){
            throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
        }
        RawTransactionBuilder builder  = new RawTransactionBuilder();

        //chainTag
        builder.update(Byte.valueOf(chainTag), "chainTag");

        //Expiration
        byte[] expirationBytes = BytesUtils.longToBytes(expiration);
        builder.update(expirationBytes, "expiration");

        //BlockRef
        byte[] currentBlockRef = BytesUtils.trimLeadingZeroes(blockRef);
        builder.update(currentBlockRef, "blockRef");

        //Nonce
        byte[] trimedNonce = BytesUtils.trimLeadingZeroes(nonce);
        builder.update(trimedNonce , "nonce");

        //gas
        byte[] gas = BytesUtils.longToBytes( gasInt );
        builder.update(gas, "gas");

        builder.update(Byte.valueOf( gasPriceCoef ), "gasPriceCoef");
        //clause

        int size = toClauses.length;
        RawClause[] rawClauses = new RawClause[size];
        int index = 0;
        for (ToClause clause: toClauses){
            rawClauses[index] = new RawClause();
            rawClauses[index].setTo(clause.getTo().toByteArray());
            rawClauses[index].setValue(clause.getValue().toByteArray());
            rawClauses[index].setData(clause.getData().toByteArray());
            index++;
        }
        //update the clause
        builder.update(rawClauses);
        RawTransaction rawTxn = builder.build();
        return rawTxn;
    }


	public static RawTransactionFactory getInstance() {
		return INSTANCE;
	}

}
