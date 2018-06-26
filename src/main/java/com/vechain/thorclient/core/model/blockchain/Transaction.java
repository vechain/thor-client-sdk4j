package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class Transaction implements Serializable {

    private String id; //32 bytes
    private int   size;
    private String chainTag;
    private String  blockRef; //8 bytes
    private int expiration;
    private ArrayList<Clause> clauses;

    private int gasPriceCoef;

    private long gas;

    private String dependsOn;

    private String nonce;

    private String origin;

    private BlockContext block;

    private String raw;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getChainTag() {
        return chainTag;
    }

    public void setChainTag(String chainTag) {
        this.chainTag = chainTag;
    }

    public String getBlockRef() {
        return blockRef;
    }

    public void setBlockRef(String blockRef) {
        this.blockRef = blockRef;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public ArrayList<Clause> getClauses() {
        return clauses;
    }

    public void setClauses(ArrayList<Clause> clauses) {
        this.clauses = clauses;
    }

    public int getGasPriceCoef() {
        return gasPriceCoef;
    }

    public void setGasPriceCoef(int gasPriceCoef) {
        this.gasPriceCoef = gasPriceCoef;
    }

    public long getGas() {
        return gas;
    }

    public void setGas(long gas) {
        this.gas = gas;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public BlockContext getBlock() {
        return block;
    }

    public void setBlock(BlockContext block) {
        this.block = block;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
