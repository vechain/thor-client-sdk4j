package com.vechain.thorclient.core.model.blockchain;

/**
 * [TxMeta](http://localhost:8669/doc/stoplight-ui/#/schemas/TxMeta)
 *
 * @version galactica
 */
public class TxMeta {
    private String blockID;
    private int blockNumber;
    private long blockTimestamp;

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

}
