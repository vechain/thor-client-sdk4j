package com.vechain.thorclient.core.model.blockchain;

import java.util.ArrayList;

/**
 * [SubscriptionBlockResponse](http://localhost:8669/doc/stoplight-ui/#/schemas/SubscriptionBlockResponse)
 */
public class BlockSubscribingResponse {
    private String number;
    private String id;
    private long size;
    private String parentID;
    private long timestamp;
    private long gasLimit;
    private String beneficiary;
    private long gasUsed;
    private String baseFeePerGas; // hex galactica
    private long totalScore;
    private String txsRoot; //32 bytes
    private long txsFeatures; // integer galactica
    private String stateRoot; //32 bytes
    private String receiptsRoot; //32 bytes
    private boolean com; // boolean galactica
    private String signer;
    private boolean obsolete;
    private ArrayList<String> transactions;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getBaseFeePerGas() {
        return baseFeePerGas;
    }

    public void setBaseFeePerGas(String baseFeePerGas) {
        this.baseFeePerGas = baseFeePerGas;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public String getTxsRoot() {
        return txsRoot;
    }

    public void setTxsRoot(String txsRoot) {
        this.txsRoot = txsRoot;
    }

    public long getTxsFeatures() {
        return txsFeatures;
    }

    public void setTxsFeatures(long txsFeatures) {
        this.txsFeatures = txsFeatures;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getReceiptsRoot() {
        return receiptsRoot;
    }

    public void setReceiptsRoot(String receiptsRoot) {
        this.receiptsRoot = receiptsRoot;
    }

    public boolean isCom() {
        return com;
    }

    public void setCom(boolean com) {
        this.com = com;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public boolean isObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<String> transactions) {
        this.transactions = transactions;
    }
}
