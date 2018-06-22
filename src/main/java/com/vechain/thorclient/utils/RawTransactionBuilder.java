package com.vechain.thorclient.utils;

import java.util.HashMap;
import java.util.List;

import com.vechain.thorclient.core.model.blockchain.Clause;
import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.blockchain.RawTransaction;
import com.vechain.thorclient.core.model.blockchain.TransactionAttributes.ERC20ContractMethod;

public class RawTransactionBuilder {


	private final HashMap<String, Object>	refValue;
	private RawClause[]						clauses;


	public RawTransactionBuilder() {
		this.refValue = new HashMap<>();
	}

	/**
	 * @param value
	 * @param field
	 * @return
	 */
	public RawTransactionBuilder update(Byte value, String field) {

		this.refValue.put(field, value);

		return this;
	}

	/**
	 * @param value
	 * @param field
	 * @return
	 */
	public RawTransactionBuilder update(byte[] value, String field) {
		this.refValue.put(field, value);
		return this;
	}

	/**
	 * @param clauses
	 * @return
	 */
	public RawTransactionBuilder update(RawClause[] clauses) {
		this.clauses = clauses;
		return this;
	}

	public void buildBaseRawAttributes(byte chainTag, byte[] blockRef, int expiration, int gas, byte gasPriceCoef) {
		// ChainTag
		this.update(Byte.valueOf(chainTag), "chainTag");

		// Expiration
		byte[] expirationBytes = BytesUtils.longToBytes(expiration);
		this.update(expirationBytes, "expiration");

		// BlockRef
		byte[] trimedBlockRef = BytesUtils.trimLeadingZeroes(blockRef);
		this.update(trimedBlockRef, "blockRef");

		// Nonce
		byte[] nonce = BytesUtils.trimLeadingZeroes(CryptoUtils.generateTxNonce());
		this.update(nonce, "nonce");

		// update gas bytes
		byte[] gasBytes = BytesUtils.longToBytes(gas);
		this.update(gasBytes, "gas");

		// update gas price coefficient
		this.update(gasPriceCoef, "gasPriceCoef");
	}

	public void buildVETClauses(List<Clause> clauses) {
		int size = clauses.size();
		RawClause[] rawClauses = new RawClause[size];
		int index = 0;
		for (Clause clause : clauses) {
			rawClauses[index] = new RawClause();
			if (!StringUtils.isBlank(clause.getTo())) {
				rawClauses[index].setTo(BytesUtils.toByteArray(clause.getTo()));
			}
			if (!StringUtils.isBlank(clause.getValue())) {
				rawClauses[index].setValue(BytesUtils.toByteArray(clause.getValue()));
			}
			if (!StringUtils.isBlank(clause.getData())) {
				rawClauses[index].setData(BytesUtils.toByteArray(clause.getData()));
			}
			index++;
		}
		// update the clause
		this.update(rawClauses);
	}


	public void buildVTHOClauses(List<Clause> clauses, String contractAddress) {
		if (StringUtils.isBlank(contractAddress)) {
			return;
		}
		int size = clauses.size();
		RawClause[] rawClauses = new RawClause[size];
		int index = 0;
		for (Clause clause : clauses) {
			rawClauses[index] = new RawClause();
			rawClauses[index].setValue("".getBytes());
			rawClauses[index].setTo(BytesUtils.toByteArray(contractAddress));

			if (StringUtils.isBlank(clause.getTo())) {
				throw new RuntimeException("illegal parameters error.");
			}
			String to = clause.getTo();
			if (StringUtils.isBlank(to)) {
				throw new RuntimeException("illegal parameters error.");
			}
			if (BlockchainUtils.isAddress(to)) {
				to = StringUtils.sanitizeHex(to);
			} else {
				throw new RuntimeException("illegal parameters error.");
			}
			String t = ERC20ContractMethod.TRANSFER.getId() + "000000000000000000000000" + to;
			String value = clause.getValue();
			if (StringUtils.isBlank(value)) {
				throw new RuntimeException("illegal parameters error.");
			}
			if (StringUtils.isHex(value)) {
				value = StringUtils.sanitizeHex(value);
			} else {
				throw new RuntimeException("illegal parameters error.");
			}
			value = BlockchainUtils.fillZeroBefore(value, 64);
			byte[] tmp = BytesUtils.toByteArray(t + value);
			rawClauses[index].setData(tmp);
			index++;
		}
		// update the clause
		this.update(rawClauses);
	}

	/**
	 * @return
	 */
	public RawTransaction build() {

		RawTransaction rawTransaction = new RawTransaction();
		BeanRefUtils.setFieldValue(rawTransaction, this.refValue);
		rawTransaction.setClauses(clauses);
		return rawTransaction;
	}
}
