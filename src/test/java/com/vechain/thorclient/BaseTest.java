package com.vechain.thorclient;

import java.util.logging.Logger;

import com.vechain.thorclient.service.BlockchainAPI;
import com.vechain.thorclient.service.impl.BlockchainAPIImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vechain.thorclient.core.model.NodeProvider;

public abstract class BaseTest {

	BlockchainAPI blockchainAPI;
	static Logger		logger	= Logger.getLogger("Thor client");
	static String FromPrivKey = "0xc8c53657e41a8d669349fc287f57457bd746cb1fcfc38cf94d235deb2cfca81b";
	static String MyAccountAddr = "0xf881a94423f22ee9a0e3e1442f515f43c966b7ed";


	@Before
	public void setProvider() {
		blockchainAPI = new BlockchainAPIImpl();
		NodeProvider provider = new NodeProvider();
		provider.setProvider("<your thor node address>");
		if(!provider.getProvider().startsWith( "http" )){
			throw new RuntimeException( "Please set your own node address. " );
		}
		provider.setTimeout(5000);
		blockchainAPI.setProvider(provider);
	}

	@Test
	public void testThorAPI() {
		Assert.assertNotNull("blockchainAPI is null", this.blockchainAPI );
	}

}
