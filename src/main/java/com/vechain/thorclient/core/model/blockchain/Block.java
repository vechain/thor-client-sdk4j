package com.vechain.thorclient.core.model.blockchain;

import com.vechain.thorclient.core.model.clients.BlockRef;

import java.io.Serializable;
import java.util.ArrayList;

public class Block implements Serializable {

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

    public void setParentID(String parentId) {
        this.parentID = parentId;
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

    public String getTxsRoot() {
        return txsRoot;
    }

    public void setTxsRoot(String txsRoot) {
        this.txsRoot = txsRoot;
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

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public ArrayList<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<String> transactions) {
        this.transactions = transactions;
    }

    public String toString(){
        return "number:" + this.number + "  block:"+ id + " parentId:" + parentID;
    }
    public boolean getIsTrunk() {
        return isTrunk;
    }

    public void setIsTrunk(boolean trunk) {
        isTrunk = trunk;
    }

    public BlockRef blockRef(){
        return BlockRef.create( this.id );
    }

    private String number;
    private String id;
    private long size;
    private String parentID;
    private long timestamp;
    private long gasLimit;
    private String beneficiary;
    private long gasUsed;
    private long totalScore;
    private String txsRoot; //32 bytes
    private String stateRoot; //32 bytes
    private String receiptsRoot; //32 bytes
    private String signer;
    private boolean isTrunk;
    private ArrayList<String> transactions;



}
