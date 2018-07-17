package com.vechain.thorclient.clients;

import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.exception.ClientArgumentException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public class BlockchainClientTest extends BaseTest {

	@Test
	public void testGetChainTag() throws ClientArgumentException {
		byte chainTag = BlockchainClient.getChainTag();
		int chainTagInt = chainTag & 0xff;
		logger.info("chainTag: " + chainTagInt);
		Assert.assertTrue(chainTagInt > 0);
	}

	@Test
	public void testGetNodeStats() throws ClientArgumentException {
		ArrayList list = BlockchainClient.getPeerStatusList();
		logger.info("nodes list:" + list);
		// Assert.assertNotNull(list);
	}

}
