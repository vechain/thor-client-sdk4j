package com.vechain.thorclient.base;

import java.util.logging.Logger;

import com.vechain.thorclient.service.BlockchainAPI;
import com.vechain.thorclient.service.impl.BlockchainAPIImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vechain.thorclient.core.model.blockchain.NodeProvider;


public abstract class BaseTest {

	public BlockchainAPI blockchainAPI;
	public static Logger		logger	= Logger.getLogger("Thor client");
	public static String FromPrivKey = "0xc8c53657e41a8d669349fc287f57457bd746cb1fcfc38cf94d235deb2cfca81b";
	public static String MyAccountAddr = "0xA43751C42125dfB5fFE662F0ad527660885de3AE";
	public static String TokenAddr = "0x0000000000000000000000000000456e65726779";

	@Before
	public void setProvider() {
		blockchainAPI = new BlockchainAPIImpl();

		NodeProvider.getNodeProvider().setProvider("https://vethor-node-test.vechaindev.com");
		if(!NodeProvider.getNodeProvider().getProvider().startsWith( "http" )){
			throw new RuntimeException( "Please set your own node address. " );
		}
		NodeProvider.getNodeProvider().setTimeout(5000);
		blockchainAPI.setProvider(NodeProvider.getNodeProvider());
	}

	@Test
	public void testThorAPI() {
		Assert.assertNotNull("blockchainAPI is null", this.blockchainAPI );
	}

}
