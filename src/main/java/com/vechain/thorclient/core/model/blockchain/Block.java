package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.core.model.clients.BlockRef;

public class Block {

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public byte[] getTxsRoot() {
        return txsRoot;
    }

    public void setTxsRoot(byte[] txsRoot) {
        this.txsRoot = txsRoot;
    }

    public byte[] getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(byte[] stateRoot) {
        this.stateRoot = stateRoot;
    }

    public byte[] getReceiptsRoot() {
        return receiptsRoot;
    }

    public void setReceiptsRoot(byte[] receiptsRoot) {
        this.receiptsRoot = receiptsRoot;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public void setTransactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

    public String toString(){
        return "number:" + this.number + "  block:"+ id + " parentId:" + parentId;
    }

    public BlockRef blockRef(){
        return BlockRef.create( this.id );
    }

    private String number;
    private String id;
    private long size;
    private String parentId;
    private long timestamp;
    private long gasLimit;
    private String beneficiary;
    private long gasUsed;
    private long totalScore;
    private byte[] txsRoot; //32 bytes
    private byte[] stateRoot; //32 bytes
    private byte[] receiptsRoot; //32 bytes
    private String signer;

    private Transaction[] transactions;


}
