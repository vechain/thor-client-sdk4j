package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.utils.RLPUtils;

public class RawTransactionEIP1559 implements TransactionCommonalities {
    private byte chainTag;                  // 1 byte max: numeric
    private byte[] blockRef;                // 8 bytes: compact fixed hex blob
    private byte[] expiration;              // 4 bytes max: numeric
    private RawClause[] clauses;
    private byte[] maxPriorityFeePerGas;    // 32 bytes max: numeric
    private byte[] maxFeePerGas;            // 32 bytes max: numeric
    private byte[] gas;                     // 8 bytes max: numeric
    private byte[] dependsOn;               // 32 bytes: optional fixed hex blob optional
    private byte[] nonce;                   // 8 bytes max: numeric
    private byte[] signature;
    private TransactionReserved reserved;

    public RawTransactionEIP1559() {
    }

    public byte getChainTag() {
        return chainTag;
    }

    public void setChainTag(byte chainTag) {
        this.chainTag = chainTag;
    }

    public byte[] getBlockRef() {
        return blockRef;
    }

    public void setBlockRef(byte[] blockRef) {
        this.blockRef = blockRef;
    }

    public byte[] getExpiration() {
        return expiration;
    }

    public void setExpiration(byte[] expiration) {
        this.expiration = expiration;
    }

    public RawClause[] getClauses() {
        return clauses;
    }

    public void setClauses(RawClause[] clauses) {
        this.clauses = clauses;
    }

    public byte[] getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }

    public void setMaxPriorityFeePerGas(byte[] maxPriorityFeePerGas) {
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
    }

    public byte[] getMaxFeePerGas() {
        return maxFeePerGas;
    }

    public void setMaxFeePerGas(byte[] maxFeePerGas) {
        this.maxFeePerGas = maxFeePerGas;
    }

    public byte[] getGas() {
        return gas;
    }

    public void setGas(byte[] gas) {
        this.gas = gas;
    }

    public byte[] getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(byte[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public TransactionReserved getReserved() {
        return reserved;
    }

    public void setReserved(TransactionReserved reserved) {
        this.reserved = reserved;
    }

    public byte[] encode() {
        return RLPUtils.encodeRawTransaction(this);
    }

    public RawTransactionEIP1559 copy() {
        RawTransactionEIP1559 transaction = new RawTransactionEIP1559();
        transaction.setChainTag(chainTag);
        transaction.setBlockRef(blockRef);
        transaction.setExpiration(expiration);
        transaction.setClauses(clauses);
        transaction.setMaxPriorityFeePerGas(maxPriorityFeePerGas);
        transaction.setMaxFeePerGas(maxFeePerGas);
        transaction.setGas(gas);
        transaction.setDependsOn(dependsOn);
        transaction.setNonce(nonce);
        transaction.setSignature(signature);
        transaction.setReserved(reserved);
        return transaction;
    }

}
