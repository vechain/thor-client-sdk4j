package com.vechain.thorclient.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vechain.thorclient.core.model.blockchain.Clause;
import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.blockchain.RawTransaction;
import com.vechain.thorclient.service.BlockchainAPI;

public class TransactionFactory {


    private static TransactionFactory INSTANCE = new TransactionFactory();

	/**
	 *
	 * @return
	 */
	public static TransactionFactory factory(){
		return INSTANCE;
	}

    /**
     * create raw transaction.
     * @param chainTag
     * @param blockRef
     * @param expiration
     * @param gasInt
     * @param gasPriceCoef
     * @param nonce
     * @param clauses
     * @return
     * @throws IllegalArgumentException
     */
    public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gasInt, byte gasPriceCoef, byte[] nonce, ArrayList<Clause> clauses) throws IllegalArgumentException{
        if(chainTag == 0
                || blockRef == null
                || expiration <= 0
                || gasInt <= 0
                || gasPriceCoef <= 0
                || clauses == null
                || clauses.size() == 0){
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

        int size = clauses.size();
        RawClause[] rawClauses = new RawClause[size];
        int index = 0;
        for (Clause clause: clauses){
            rawClauses[index] = new RawClause();
            if(!StringUtils.isBlank(clause.getTo())) {
                rawClauses[index].setTo(BytesUtils.toByteArray(clause.getTo()));
            }
            if(!StringUtils.isBlank(clause.getValue())){
                rawClauses[index].setValue(BytesUtils.defaultDecimalStringToByteArray(clause.getValue()));
            }
            if(!StringUtils.isBlank(clause.getData())){
                rawClauses[index].setValue(BytesUtils.toByteArray(clause.getData()));
            }
            index++;
        }
        //update the clause
        builder.update(rawClauses);

        RawTransaction rawTxn = builder.build();

        return rawTxn;
    }


    /**
     *
     * @param blockchainAPI
     * @param expiration
     * @param gas
     * @param gasPriceCoef
     * @param clauses
     * @return
     * @throws IllegalArgumentException
     */
    public RawTransaction createVETTransfer(BlockchainAPI blockchainAPI, int expiration, int gas, byte gasPriceCoef, ArrayList<Clause> clauses) throws IllegalArgumentException, IOException {

        if (blockchainAPI == null){
            throw new IllegalArgumentException("The argument thor blockchain of create VET is illegal.");
        }
        byte[] blockRef = blockchainAPI.getBestBlockRef();
        byte chainTag = blockchainAPI.getChainTag();
        return createRawTransaction(chainTag, blockRef, expiration, gas, gasPriceCoef, CryptoUtils.generateTxNonce(), clauses);
    }



	/**
	 *
	 * @param chainTag
	 * @param blockRef
	 * @param expiration
	 * @param gas
	 * @param gasPriceCoef
	 * @param clauses
	 * @return
	 * @throws IllegalArgumentException
	 */
	public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gas, byte gasPriceCoef, List<Clause> clauses)
			throws IllegalArgumentException {
		if (chainTag == 0 || blockRef == null || expiration < 0 || gas < 0 || gasPriceCoef < 0 || clauses == null || clauses.size() == 0) {
			throw new IllegalArgumentException("The arguments are illegal.");
		}
		RawTransactionBuilder builder = new RawTransactionBuilder();

		builder.buildBaseRawAttributes(chainTag, blockRef, expiration, gas, gasPriceCoef);
		builder.buildVETClauses(clauses);

		RawTransaction rawTxn = builder.build();

		return rawTxn;
	}

	/**
	 * Create raw transaction
	 * @param chainTag
	 * @param blockRef
	 * @param expiration
	 * @param gas
	 * @param gasPriceCoef
	 * @param clauses
	 * @param contractAddress
	 * @return {@link RawTransaction}
	 * @throws IllegalArgumentException
	 */
	public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gas, byte gasPriceCoef, List<Clause> clauses,
			String contractAddress) throws IllegalArgumentException {
		if (chainTag == 0 || blockRef == null || expiration < 0 || gas < 0 || gasPriceCoef < 0 || clauses == null || clauses.size() == 0
				|| StringUtils.isBlank(contractAddress)) {
			throw new IllegalArgumentException("The arguments are illegal.");
		}
		RawTransactionBuilder builder = new RawTransactionBuilder();

		builder.buildBaseRawAttributes(chainTag, blockRef, expiration, gas, gasPriceCoef);

		builder.buildVTHOClauses(clauses, contractAddress);

		RawTransaction rawTxn = builder.build();

		return rawTxn;
	}

	/**
	 *
	 * 
	 * @param blockchainAPI
	 * @param expiration
	 * @param gas
	 * @param gasPriceCoef
	 * @param clauses
	 * @param contractAddress
	 * @return
	 * @throws IllegalArgumentException
	 */
	public RawTransaction createRawTransaction(BlockchainAPI blockchainAPI, int expiration, int gas, byte gasPriceCoef, List<Clause> clauses, String contractAddress)
			throws IllegalArgumentException, IOException {
		if (blockchainAPI == null) {
			throw new IllegalArgumentException("The parameters are illegal.");
		}
		byte[] blockRef = blockchainAPI.getBestBlockRef();
		byte chainTag = blockchainAPI.getChainTag();
		if (StringUtils.isBlank(contractAddress)) {
			return createRawTransaction(chainTag, blockRef, expiration, gas, gasPriceCoef, clauses);
		} else {
			return createRawTransaction(chainTag, blockRef, expiration, gas, gasPriceCoef, clauses, contractAddress);
		}
	}

	public static TransactionFactory getInstance() {
		return INSTANCE;
	}

}
