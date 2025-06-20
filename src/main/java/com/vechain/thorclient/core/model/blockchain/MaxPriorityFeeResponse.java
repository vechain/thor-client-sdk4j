package com.vechain.thorclient.core.model.blockchain;

import java.io.Serializable;

public class MaxPriorityFeeResponse implements Serializable {
    private String maxPriorityFeePerGas;

    public String getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }
}
