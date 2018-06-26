package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class FilteredEvent implements Serializable {
    private ArrayList<String> topics;
    private String data;
    private BlockContext block;
    private TxContext tx;

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BlockContext getBlock() {
        return block;
    }

    public void setBlock(BlockContext block) {
        this.block = block;
    }

    public TxContext getTx() {
        return tx;
    }

    public void setTx(TxContext tx) {
        this.tx = tx;
    }
}
