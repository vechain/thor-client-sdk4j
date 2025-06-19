package com.vechain.thorclient.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.MaxFees;
import com.vechain.thorclient.utils.MaxFeeCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;
import java.math.BigInteger;

@RunWith(JUnit4.class)
public class MaxFeeCalculatorTest extends BaseTest {

    @Test
    public void getBestBlockBaseFeePerGasTest() {
        BigInteger baseFee = MaxFeeCalculator.getBestBlockBaseFeePerGas();
        Assert.assertTrue(baseFee.compareTo(BigInteger.ZERO) > 0);
    }

    @Test
    public void calculateMaxPriorityFeePerGasFromBestBlockTest() {
        BigInteger maxPriorityFee = MaxFeeCalculator.calculateMaxPriorityFeePerGasFromBestBlock();
        Assert.assertTrue(maxPriorityFee.compareTo(BigInteger.ZERO) >= 0);
    }

    @Test
    public void calculateMaxFeeWithUserInput() {
        MaxFees userProvided = new MaxFees(BigInteger.valueOf(100000L), BigInteger.valueOf(100L));
        MaxFees computedFees = MaxFeeCalculator.calculateMaxFees(userProvided);
        Assert.assertEquals(0, computedFees.maxFeePerGas.compareTo(userProvided.maxFeePerGas));
        Assert.assertEquals(0, computedFees.maxPriorityFeePerGas.compareTo(userProvided.maxPriorityFeePerGas));
    }

    @Test
    public void calculateMaxFeeWithoutUserInput() {
        MaxFees userProvided = new MaxFees(null, null);
        MaxFees computedFees = MaxFeeCalculator.calculateMaxFees(userProvided);
        Assert.assertTrue(computedFees.maxFeePerGas.compareTo(BigInteger.ZERO) >= 0);
        Assert.assertTrue(computedFees.maxPriorityFeePerGas.compareTo(BigInteger.ZERO) >= 0);
    }

    @Test
    public void calculateMaxFeeWithPartialUserInput() {
        // when only maxFeePerGas provided
        MaxFees userProvided = new MaxFees(BigInteger.valueOf(100000L), null);
        MaxFees computedFees = MaxFeeCalculator.calculateMaxFees(userProvided);
        Assert.assertEquals(0, computedFees.maxFeePerGas.compareTo(userProvided.maxFeePerGas));
        Assert.assertTrue(computedFees.maxPriorityFeePerGas.compareTo(BigInteger.ZERO) >= 0);
        // when only maxPriorityFeePerGas provided
        userProvided = new MaxFees(null, BigInteger.valueOf(1000L));
        computedFees = MaxFeeCalculator.calculateMaxFees(userProvided);
        Assert.assertEquals(0, computedFees.maxPriorityFeePerGas.compareTo(userProvided.maxPriorityFeePerGas));
        Assert.assertTrue(computedFees.maxFeePerGas.compareTo(BigInteger.ZERO) >= 0);
    }
}
