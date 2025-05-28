package com.vechain.thorclient.core.model.clients;

import com.vechain.thorclient.core.model.blockchain.RawClause;
import com.vechain.thorclient.utils.RLPUtils;

public class RawTransaction {

    public static final byte EIP1559 = 0x51; // = 81

    private byte chainTag;                  // 1 byte max: numeric
    private byte[] blockRef;                // 8 bytes: compact fixed hex blob
    private byte[] expiration;              // 4 bytes max: numeric
    private RawClause[] clauses;

    // 1-255 used baseprice 255 used 2x base price
    private Byte gasPriceCoef;              // 1 byte max: numeric, not null if legacy tx.
    private byte[] maxPriorityFeePerGas;    // 32 bytes max: numeric, null if Legacy tx, nullable if EIP-1559 tx.
    private byte[] maxFeePerGas;            // 32 bytes max: numeric, not null if EIP-1559 tx.

    // gas limit the max gas for VET 21000 for VTHO 80000
    private byte[] gas;                     // 8 bytes max: numeric
    private byte[] dependsOn;               // 32 bytes: optional fixed hex blob optional
    private byte[] nonce;                   // 8 bytes max: numeric
    private TransactionReserved reserved;
    private byte[] signature;

    public RawTransaction() {
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

    public Byte getGasPriceCoef() {
        return gasPriceCoef;
    }

    public void setGasPriceCoef(Byte gasPriceCoef) {
        this.gasPriceCoef = gasPriceCoef;
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

    public TransactionReserved getReserved() {
        return reserved;
    }

    public void setReserved(TransactionReserved reserved) {
        this.reserved = reserved;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }


    public RawTransaction copy() {
        RawTransaction transaction = new RawTransaction();
        transaction.chainTag = this.chainTag;
        transaction.blockRef = this.blockRef;
        transaction.expiration = this.expiration;
        transaction.clauses = this.clauses;
        transaction.gasPriceCoef = this.gasPriceCoef; // optional
        transaction.maxPriorityFeePerGas = this.maxPriorityFeePerGas; // optional
        transaction.maxFeePerGas = this.maxFeePerGas; // optional
        transaction.gas = this.gas;
        transaction.dependsOn = this.dependsOn;
        transaction.nonce = this.nonce;
        transaction.reserved = this.reserved;
        transaction.signature = this.signature;
        return transaction;
    }

    public byte[] encode() {
        final byte[] encoded = RLPUtils.encodeRawTransaction(this);
        if (this.isEIP1559()) {
            final byte[] eip1559Encoded = new byte[encoded.length + 1];
            eip1559Encoded[0] = EIP1559;
            System.arraycopy(encoded, 0, eip1559Encoded, 1, encoded.length);
            return eip1559Encoded;
        }
        return encoded;
    }

    public boolean isEIP1559() {
        return this.gasPriceCoef == null && this.maxFeePerGas != null;
    }

    public boolean isLegacy() {
        return this.gasPriceCoef != null && this.maxPriorityFeePerGas == null && this.maxFeePerGas == null;
    }


}
