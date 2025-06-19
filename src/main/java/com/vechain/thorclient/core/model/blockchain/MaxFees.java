package com.vechain.thorclient.core.model.blockchain;

import java.math.BigInteger;

public class MaxFees {
    public final BigInteger maxFeePerGas;
    public final BigInteger maxPriorityFeePerGas;

    public MaxFees(BigInteger maxFeePerGas, BigInteger maxPriorityFeePerGas) {
        this.maxFeePerGas = maxFeePerGas;
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
    }
}
