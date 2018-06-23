package com.vechain.thorclient.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.service.BlockchainAPI;
import com.vechain.thorclient.service.impl.BlockchainAPIImpl;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.crypto.ECKeyPair;

public abstract class BaseTest implements SlatKeys {

    protected Logger            logger        = LoggerFactory.getLogger(this.getClass());
    private final String        TOKEN_ADDRESS = "0x0000000000000000000000000000456e65726779";
    protected BlockchainAPI     blockchainAPI;
    protected String            privateKey;
    protected String            nodeProviderUrl;
    protected String            fromAddress;
    private Map<String, String> environment   = new HashMap<String, String>();

    @Before
    public void setProvider() {
        privateKey = System.getenv(PRIVATE_KEY);
        nodeProviderUrl = System.getenv(NODE_PROVIDER_URL);
        String nodeTimeout = System.getenv(NODE_TIMEOUT);

        Properties properties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        int timeout = 5000;
        try {
            properties.load(is);
        } catch (IOException e) {
            logger.error("Can not read file config.properties~");
        } finally {
            if (StringUtils.isBlank(nodeTimeout)) {
                nodeTimeout = properties.getProperty(NODE_TIMEOUT);
            }
            try {
                if (!StringUtils.isBlank(nodeTimeout)) {
                    timeout = Integer.parseInt(nodeTimeout);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            environment.put(NODE_TIMEOUT, String.valueOf(timeout));
            if (StringUtils.isBlank(privateKey)) {
                privateKey = properties.getProperty(PRIVATE_KEY);
            }
            environment.put(PRIVATE_KEY, privateKey);
            if (StringUtils.isBlank(nodeProviderUrl)) {
                nodeProviderUrl = properties.getProperty(NODE_PROVIDER_URL);
            }
            environment.put(NODE_PROVIDER_URL, nodeProviderUrl);
        }
        if (StringUtils.isBlank(this.nodeProviderUrl) || !this.nodeProviderUrl.startsWith("http")) {
            throw new RuntimeException("Can not find valid nodeProviderUrl~");
        }
        environment.put(VTHO_TOKEN_ADDRESS, TOKEN_ADDRESS);
        blockchainAPI = new BlockchainAPIImpl();
        NodeProvider nodeProvider = NodeProvider.getNodeProvider();
        nodeProvider.setProvider(this.nodeProviderUrl);
        nodeProvider.setTimeout(timeout);
        blockchainAPI.setProvider(nodeProvider);
        this.recoverAddress();
    }

    protected Map<String, String> getEnvironment() {
        return this.environment;
    }

    protected void recoverAddress() {
        if (!StringUtils.isBlank(privateKey)) {
            ECKeyPair keyPair = ECKeyPair.create(privateKey);
            fromAddress = keyPair.getAddress();
            environment.put(FROM_ADDRESS, fromAddress);
        }
    }

    @Test
    public void testThorAPI() {
        Assert.assertNotNull("blockchainAPI is null", this.blockchainAPI);
    }

}
