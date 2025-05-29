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
     * Updates the internal reference map by associating the specified field with the given value.
     *
     * @param value the byte value to associate with the specified field
     * @param field the key with which the specified value is to be associated
     * @return the current instance of RawTransactionBuilder for method chaining
     */
	public RawTransactionBuilder update(final Byte value, final String field) {
		this.refValue.put(field, value);
		return this;
	}

    /**
     * Updates the internal reference map by associating the specified field with the given byte array value.
     *
     * @param value the byte array value to associate with the specified field
     * @param field the key with which the specified value is to be associated
     * @return the current instance of RawTransactionBuilder for method chaining
     */
	public RawTransactionBuilder update(final byte[] value, final String field) {
		this.refValue.put(field, value);
		return this;
	}

    /**
     * Updates the internal array of clauses with the specified array of RawClause objects.
     *
     * @param clauses an array of RawClause objects to be set as the new clauses
     * @return the current instance of RawTransactionBuilder for method chaining
     */
	public RawTransactionBuilder update(final RawClause[] clauses) {
		this.clauses = clauses;
		return this;
	}

    /**
     * Builds and returns a fully constructed instance of {@link RawTransaction}.
     * The method initializes a new {@code RawTransaction}, sets its fields with the current state
     * of the builder's internal reference values and clauses, and returns the resulting transaction.
     *
     * @return a new {@code RawTransaction} instance with the configured values
     */
	public RawTransaction build() {
		final RawTransaction rawTransaction = new RawTransaction();
		BeanRefUtils.setFieldValue(rawTransaction, this.refValue);
		rawTransaction.setClauses(clauses);
		return rawTransaction;
	}
}
