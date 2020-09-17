package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.ToClause;

public class RawTransactionFactory {

	private static RawTransactionFactory INSTANCE = new RawTransactionFactory();

	public static RawTransactionFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * create raw transaction.
	 * 
	 * @param chainTag     byte the last byte of genesis block id.
	 * @param blockRef     byte[] the first 8 bytes of the block id. Get from
	 *                     {@link com.vechain.thorclient.core.model.clients.BlockRef}
	 *                     toByteArray().
	 * @param expiration   the expiration of block size from best block to block
	 *                     reference.
	 * @param gasInt       must >= 21000.
	 * @param gasPriceCoef must > 0
	 * @param nonce        eight bytes array, random by cryptography method.
	 * @param toClauses    to clauses array.
	 * @return {@link RawTransaction} raw transaction.
	 * @throws IllegalArgumentException
	 */
	public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gasInt, byte gasPriceCoef, byte[] nonce, ToClause... toClauses)
			throws IllegalArgumentException {
		return createRawTransaction(chainTag, blockRef, expiration, gasInt, gasPriceCoef, nonce, null, toClauses);
	}

	/**
	 * create raw transaction.
	 * 
	 * @param chainTag
	 * @param blockRef
	 * @param expiration
	 * @param gasInt
	 * @param gasPriceCoef
	 * @param nonce
	 * @param rawClauses
	 * @return
	 * @throws IllegalArgumentException
	 */
	public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gasInt, byte gasPriceCoef, byte[] nonce, RawClause[] rawClauses)
			throws IllegalArgumentException {
		return createRawTransaction(chainTag, blockRef, expiration, gasInt, gasPriceCoef, nonce, null, rawClauses);
	}

	/**
	 * create raw transaction with dependsOn
	 * 
	 * @param chainTag
	 * @param blockRef
	 * @param expiration
	 * @param gasInt
	 * @param gasPriceCoef
	 * @param nonce
	 * @param txId
	 * @param toClauses
	 * @return
	 * @throws IllegalArgumentException
	 */
	public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gasInt, byte gasPriceCoef, byte[] nonce, String dependsOn, ToClause... toClauses)
			throws IllegalArgumentException {
		if (toClauses == null) {
			throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
		}

		// clause
		int size = toClauses.length;
		RawClause[] rawClauses = new RawClause[size];
		int index = 0;
		for (ToClause clause : toClauses) {
			rawClauses[index] = new RawClause();
			rawClauses[index].setTo(clause.getTo().toByteArray());
			rawClauses[index].setValue(clause.getValue().toByteArray());
			rawClauses[index].setData(clause.getData().toByteArray());
			index++;
		}

		return createRawTransaction(chainTag, blockRef, expiration, gasInt, gasPriceCoef, nonce, dependsOn, rawClauses);
	}

	/**
	 * create raw transaction with dependsOn
	 * 
	 * @param chainTag
	 * @param blockRef
	 * @param expiration
	 * @param gasInt
	 * @param gasPriceCoef
	 * @param nonce
	 * @param dependsOn
	 * @param rawClauses
	 * @return
	 * @throws IllegalArgumentException
	 */
	public RawTransaction createRawTransaction(byte chainTag, byte[] blockRef, int expiration, int gasInt, byte gasPriceCoef, byte[] nonce, String dependsOn,
			RawClause[] rawClauses) throws IllegalArgumentException {
		if (chainTag == 0 || blockRef == null || expiration <= 0 || gasInt < 21000 || rawClauses == null) {
			throw new IllegalArgumentException("The arguments of create raw transaction is illegal.");
		}
		RawTransactionBuilder builder = new RawTransactionBuilder();

		// chainTag
		builder.update(Byte.valueOf(chainTag), "chainTag");

		// Expiration
		byte[] expirationBytes = BytesUtils.longToBytes(expiration);
		builder.update(expirationBytes, "expiration");

		// BlockRef
		byte[] currentBlockRef = BytesUtils.trimLeadingZeroes(blockRef);
		builder.update(currentBlockRef, "blockRef");

		// Nonce
		byte[] trimedNonce = BytesUtils.trimLeadingZeroes(nonce);
		builder.update(trimedNonce, "nonce");

		// gas
		byte[] gas = BytesUtils.longToBytes(gasInt);
		builder.update(gas, "gas");

		// gasPriceCoef
		builder.update(Byte.valueOf(gasPriceCoef), "gasPriceCoef");

		// dependsOn
		if (!StringUtils.isBlank(dependsOn)) {
			builder.update(BytesUtils.toByteArray(dependsOn), "dependsOn");
		}

		// update the clause
		builder.update(rawClauses);
		RawTransaction rawTxn = builder.build();
		return rawTxn;
	}

}
