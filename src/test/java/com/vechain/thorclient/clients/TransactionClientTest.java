package com.vechain.thorclient.clients;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Receipt;
import com.vechain.thorclient.core.model.blockchain.Transaction;

@RunWith(JUnit4.class)
public class TransactionClientTest extends BaseTest {

    static String hexId = "0xa8488f297015797667adf92abd942c3016320710882e1a1dd867f6070455553f";

    @Test
    public void testGetTransaction() throws IOException {

        Transaction transaction = TransactionClient.getTransaction(hexId, false, null);
        logger.info("Transaction:" + JSON.toJSONString(transaction));
        Assert.assertNotNull(transaction);
    }

    @Test
    public void testGetTransactionRaw() throws IOException {
        Transaction transaction = TransactionClient.getTransaction(hexId, true, null);
        logger.info("Transaction:" + JSON.toJSONString(transaction));
        Assert.assertNotNull(transaction);
        Assert.assertNotNull(transaction.getRaw());
    }

    @Test
    public void testGetTransactionReceipt() throws IOException {
        Receipt receipt = TransactionClient.getTransactionReceipt(hexId, null);
        logger.info("Receipt:" + JSON.toJSONString(receipt));
        Assert.assertNotNull(receipt);
    }

    @Test
    public void testSendVETTransaction() throws IOException {

        // RawTransaction transaction =
        // RawTransactionFactory.getInstance().createRawTransaction( 720,21000,0x1, )
    }

}
