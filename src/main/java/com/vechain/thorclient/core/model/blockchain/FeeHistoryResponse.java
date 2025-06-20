package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class FeeHistoryResponse implements Serializable {
    private String oldestBlock;
    private String[] baseFeePerGas;
    private Number[] gasUsedRatio;
    private String[][] reward;

    public String getOldestBlock() {
        return oldestBlock;
    }

    public String[] getBaseFeePerGas() {
        return baseFeePerGas;
    }

    public Number[] getGasUsedRatio() {
        return gasUsedRatio;
    }

    public String[][] getReward() {
        return reward;
    }


}
