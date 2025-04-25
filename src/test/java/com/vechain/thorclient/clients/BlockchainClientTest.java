package com.vechain.thorclient.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.PeerStatList;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BlockchainClientTest extends BaseTest {

    // Galactica tested: http://localhost:8669/doc/stoplight-ui/#/paths/blocks-revision/get
    // GET http://localhost:8669/blocks/0
    // Accept: application/json, text/plain
    @Test
    public void testGetChainTag() throws ClientArgumentException {
        byte chainTag = BlockchainClient.getChainTag();
        int chainTagInt = chainTag & 0xff;
        logger.info("chainTag: " + chainTagInt);
        Assert.assertTrue(chainTagInt > 0);
    }

    // Galactica tested: https://testnet.vechain.org/doc/stoplight-ui/#/paths/node-network-peers/get
    @Test
    public void testGetNodeStats() throws ClientArgumentException {
        PeerStatList list = BlockchainClient.getPeerStatusList();
        logger.info("nodes list:" + list);
        // Assert.assertNotNull(list);
    }

}
