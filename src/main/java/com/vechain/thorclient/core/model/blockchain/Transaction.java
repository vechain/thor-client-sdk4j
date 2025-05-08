package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * [GetTxResponse](http://localhost:8669/doc/stoplight-ui/#/schemas/GetTxResponse)
 * or
 * [RawTx](http://localhost:8669/doc/stoplight-ui/#/schemas/RawTx)]
 *
 * @version galactica
 */
public class Transaction implements Serializable {

    // [GetTxResponse](http://localhost:8669/doc/stoplight-ui/#/schemas/GetTxResponse)
    // transactions/{id}?raw=false
    private String id; //32 bytes
    private int type; // galactica
    private int chainTag;
    private String blockRef; //8 bytes
    private long expiration;
    private ArrayList<Clause> clauses;
    private int gasPriceCoef;
    private String maxFeePerGas; // hex galactica
    private String maxPriorityFeePerGas; // hex galactica
    private long gas;
    private String origin;
    private String delegator;
    private String nonce;
    private String dependsOn;
    private int size;
    private TxMeta meta;

    // [RawTx](http://localhost:8669/doc/stoplight-ui/#/schemas/RawTx)
    // transactions/{id}?raw=true
    private String raw;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
    }

    public int getChainTag() {
        return chainTag;
    }

    public void setChainTag(int chainTag) {
        this.chainTag = chainTag;
    }

    public String getBlockRef() {
        return blockRef;
    }

    public void setBlockRef(String blockRef) {
        this.blockRef = blockRef;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
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

    public String getMaxFeePerGas() {
        return maxFeePerGas;
    }

    public void setMaxFeePerGas(String maxFeePerGas) {
        this.maxFeePerGas = maxFeePerGas;
    }

    public String getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }

    public void setMaxPriorityFeePerGas(String maxPriorityFeePerGas) {
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
    }

    public long getGas() {
        return gas;
    }

    public void setGas(long gas) {
        this.gas = gas;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDelegator() {
        return delegator;
    }

    public void setDelegator(String delegator) {
        this.delegator = delegator;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public TxMeta getMeta() {
        return meta;
    }

    public void setMeta(TxMeta meta) {
        this.meta = meta;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
