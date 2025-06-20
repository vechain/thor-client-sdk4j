package com.vechain.thorclient.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.FeeHistoryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;
import java.math.BigInteger;

@RunWith(JUnit4.class)
public class FeeClientTest extends BaseTest {

    @Test
    public void testPriorityFeeEndpoint() {
        BigInteger priorityFee = FeeClient.getPriorityFee();
        Assert.assertTrue(priorityFee.compareTo(BigInteger.ZERO) >= 0);
    }

    @Test
    public void testFeeHistoryEndpoint() {
        FeeHistoryResponse response = FeeClient.getFeeHistory(5, "25,50,75", "best");
        Assert.assertNotNull(response);
    }

}
