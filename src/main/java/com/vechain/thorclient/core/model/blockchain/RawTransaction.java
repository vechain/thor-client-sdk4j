package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.utils.RLPUtils;

public class RawTransaction {
    private byte chainTag; // 1 bytes
    private byte[] blockRef; //8 bytes
    private byte[] expiration; //4 bytes
    private RawClause[] clauses;

    // 1-255 used baseprice 255 used 2x base price
    private byte gasPriceCoef;

    // gas limit the max gas for VET 21000 for VTHO 80000
    private byte[] gas;//64 bytes
    private byte[] dependsOn;
    private byte[] nonce;    //8 bytes
    private byte[] signature;
    private byte[][] reserved;

    public RawTransaction(){
        gasPriceCoef = 0;
        dependsOn = new byte[]{};

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

    public byte getGasPriceCoef() {
        return gasPriceCoef;
    }

    public void setGasPriceCoef(byte gasPriceCoef) {
        this.gasPriceCoef = gasPriceCoef;
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


    public byte[][] getReserved() {
        return reserved;
    }


    public byte[] encode(){
        return RLPUtils.encodeRawTransaction(this);
    }


}
