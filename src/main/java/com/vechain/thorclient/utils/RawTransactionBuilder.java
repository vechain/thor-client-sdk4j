package com.vechain.thorclient.utils;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.core.model.clients.RawTransaction;
import com.vechain.thorclient.core.model.clients.RawTransactionEIP1559;

import java.util.HashMap;

public class RawTransactionBuilder {

    private final HashMap<String, Object> refValue;
    private RawClause[] clauses;

    /**
     * Constructs a new instance of the RawTransactionBuilder.
     * This builder is designed to create and assemble a {@link RawTransaction} object.
     * It initializes an internal map to hold field values for the transaction.
     */
    public RawTransactionBuilder() {
        this.refValue = new HashMap<>();
    }

    /**
     * Updates the internal map with the specified field and value.
     *
     * @param value the Byte value to associate with the specified field
     * @param field the String representing the field to be updated
     * @return the current instance of RawTransactionBuilder to allow method chaining
     */
    public RawTransactionBuilder update(final Byte value, final String field) {
        this.refValue.put(field, value);
        return this;
    }

    /**
     * Updates the internal map with the specified field and value.
     *
     * @param value the byte array to associate with the specified field
     * @param field the String representing the field to be updated
     * @return the current instance of RawTransactionBuilder to allow method chaining
     */
    public RawTransactionBuilder update(final byte[] value, final String field) {
        this.refValue.put(field, value);
        return this;
    }

    /**
     * Updates the internal clauses with the provided array of {@link RawClause} instances.
     * This method replaces the existing clauses with the specified array.
     *
     * @param clauses an array of {@link RawClause} objects representing the new transaction clauses
     * @return the current instance of {@code RawTransactionBuilder} to allow method chaining
     */
    public RawTransactionBuilder update(RawClause[] clauses) {
        this.clauses = clauses;
        return this;
    }

    /**
     * Builds and returns a new instance of legacy {@link RawTransaction} using the current state of the builder.
     * The method populates the {@code RawTransaction} instance with the reference value
     * and clauses that were provided to the builder.
     *
     * @return a newly constructed legacy {@link RawTransaction} instance containing the data set in the builder
     */
    public RawTransaction build() {
        RawTransaction rawTx = new RawTransaction();
        BeanRefUtils.setFieldValue(rawTx, this.refValue);
        rawTx.setClauses(clauses);
        return rawTx;
    }

    /**
     * Builds and returns a new instance of {@link RawTransactionEIP1559} using the current state of the builder.
     * The method populates the {@code RawTransactionEIP1559} instance with the reference values
     * and clauses set in the builder.
     *
     * @return a newly constructed {@link RawTransactionEIP1559} instance containing the data configured in the builder
     */
    public RawTransactionEIP1559 buildEIP1559() {
        RawTransactionEIP1559 rawTxEIP1559 = new RawTransactionEIP1559();
        BeanRefUtils.setFieldValue(rawTxEIP1559, this.refValue);
        rawTxEIP1559.setClauses(clauses);
        return rawTxEIP1559;
    }
}
