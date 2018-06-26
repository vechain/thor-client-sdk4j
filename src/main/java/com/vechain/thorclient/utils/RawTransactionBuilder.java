package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;

import java.util.HashMap;

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
