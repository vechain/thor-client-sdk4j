package com.vechain.thorclient.core.model.blockchain;

/**
 * [ReceiptMeta](http://localhost:8669/doc/stoplight-ui/#/schemas/ReceiptMeta)
 *
 * @version galactica
 */
public class ReceiptMeta {
    String blockId;         // hex ^0x[0-9a-f]{64}$
    int blockNumber;        // integer
    long blockTimestamp;    // integer
    String txID;            // hex ^0x[0-9a-f]{64}$
    String txOrigin;        // hex address ^0x[0-9a-f]{40}$

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
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
}
