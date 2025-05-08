package com.vechain.thorclient.core.model.blockchain;

/**
 * [LogMeta](http://127.0.0.1:8669/doc/stoplight-ui/#/schemas/LogMeta)
 *
 * @version galactica
 */
public class LogMeta {

    private String blockID;
    private int blockNumber;
    private long blockTimestamp;
    private String txID;
    private String txOrigin;
    private int clauseIndex;
    private int txIndex;
    private int logIndex;

    public String getBlockID() {
        return blockID;
    }

    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public String getTxID() {
        return txID;
    }

    public void setTxID(String txID) {
        this.txID = txID;
    }

    public String getTxOrigin() {
        return txOrigin;
    }

    public void setTxOrigin(String txOrigin) {
        this.txOrigin = txOrigin;
    }

    public long getClauseIndex() {
        return clauseIndex;
    }

    public void setClauseIndex(int clauseIndex) {
        this.clauseIndex = clauseIndex;
    }

    public long getTxIndex() {
        return txIndex;
    }

    public void setTxIndex(int txIndex) {
        this.txIndex = txIndex;
    }

    public long getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(int logIndex) {
        this.logIndex = logIndex;
    }

}
