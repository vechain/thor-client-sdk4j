package com.vechain.thorclient.clients;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.alibaba.fastjson.JSON;
import com.vechain.thorclient.base.BaseTest;
import com.vechain.thorclient.core.model.blockchain.Block;
import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.core.model.clients.Revision;
import com.vechain.thorclient.utils.BytesUtils;
import com.vechain.thorclient.utils.Prefix;

@RunWith(JUnit4.class)
public class BlockClientTest extends BaseTest {

	@Test
	public void testGetBlock() throws IOException {
		Revision revision = Revision.create(1232);
		Block block = BlockClient.getBlock(revision);

		logger.info("block:" + JSON.toJSONString(block));
		logger.info("blockRef;" + BytesUtils.toHexString(block.blockRef().toByteArray(), Prefix.ZeroLowerX));
		Assert.assertNotNull(block);
	}

	public void testUnirest() {
		NodeProvider nodeProvider = NodeProvider.getNodeProvider();
		nodeProvider.setProvider(nodeProviderUrl);
		nodeProvider.setTimeout(5000);
		Block best = BlockClient.getBlock(Revision.BEST);
		for (int i = 0; i < Long.parseLong(best.getNumber()); i++) {
			try {
				Revision revision = Revision.create(i);
				Block block = BlockClient.getBlock(revision);
				logger.info("block:{}", block.getId());

			} catch (Exception e) {
			}
		}

	}
}
