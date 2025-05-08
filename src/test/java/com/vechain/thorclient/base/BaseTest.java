package com.vechain.thorclient.base;

import com.vechain.thorclient.core.model.blockchain.NodeProvider;
import com.vechain.thorclient.utils.StringUtils;
import com.vechain.thorclient.utils.crypto.ECKeyPair;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseTest implements SlatKeys {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String privateKey;
    protected String sponsorKey;
    protected String nodeProviderUrl;
    protected String nodeWSProviderUrl;
    protected String fromAddress;
    private Map<String, String> environment = new HashMap<String, String>();

    private static final int DEFAULT_TIMEOUT = 5000;

    private static boolean DEFAULT_PRETTY_FORMAT = false;

    private static final String PATH = "config.properties";

    private static final String PRETTY_FORMAT = "prettyFormat";

    public boolean isPretty() {
        return Boolean.parseBoolean(System.getProperty(PRETTY_FORMAT, String.valueOf(DEFAULT_PRETTY_FORMAT)));
    }

    @Before
    public void loadEnv() {
        try (final InputStream is = BaseTest.class.getClassLoader().getResourceAsStream(PATH)) {
            final Properties properties = new Properties();
            properties.load(is);
            properties.forEach((k, v) -> {
                final String key = k.toString();
                final String value = v.toString();
                if (!StringUtils.isBlank(value)) {
                    System.setProperty(key, value);
                }
            });
        } catch (Exception e) {
            logger.error("Can not load the file {} in classpath", PATH);
        }
        final NodeProvider nodeProvider = NodeProvider.getNodeProvider();
        nodeProvider.setProvider(System.getProperty(NODE_PROVIDER_URL));
        nodeProvider.setWsProvider(System.getProperty(NODE_WSPROVIDER_URL));
        nodeProvider.setTimeout(Integer.parseInt(System.getProperty(NODE_TIMEOUT, String.valueOf(DEFAULT_TIMEOUT))));
    }

    @Before
    public void setProvider() {
        privateKey = System.getenv(PRIVATE_KEY);
        sponsorKey = System.getenv(SPONSOR_KEY);
        nodeProviderUrl = System.getenv(NODE_PROVIDER_URL);
        nodeWSProviderUrl = System.getenv(NODE_WSPROVIDER_URL);
        String nodeTimeout = System.getenv(NODE_TIMEOUT);

        Properties properties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        int timeout = 5000;
        try {
            properties.load(is);
        } catch (Exception e) {
            logger.error("Can not find the file config.properties in classpath~");
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
            if (StringUtils.isBlank(sponsorKey)) {
                sponsorKey = properties.getProperty(SPONSOR_KEY);
            }
            environment.put(PRIVATE_KEY, privateKey);
            if (StringUtils.isBlank(nodeProviderUrl)) {
                nodeProviderUrl = properties.getProperty(NODE_PROVIDER_URL);
            }
            environment.put(NODE_PROVIDER_URL, nodeProviderUrl);

            if (StringUtils.isBlank(nodeWSProviderUrl)) {
                nodeWSProviderUrl = properties.getProperty(NODE_WSPROVIDER_URL);
            }
            environment.put(NODE_WSPROVIDER_URL, nodeWSProviderUrl);
        }
        if (StringUtils.isBlank(this.nodeProviderUrl) || !this.nodeProviderUrl.startsWith("http")) {
            throw new RuntimeException("Can not find valid nodeProviderUrl~");
        }

        NodeProvider nodeProvider = NodeProvider.getNodeProvider();
        nodeProvider.setProvider(this.nodeProviderUrl);
        nodeProvider.setWsProvider(this.nodeWSProviderUrl);
        nodeProvider.setTimeout(timeout);

        this.recoverAddress();
    }

    protected Map<String, String> getEnvironment() {
        return this.environment;
    }

    protected void recoverAddress() {
        if (!StringUtils.isBlank(privateKey)) {
            ECKeyPair keyPair = ECKeyPair.create(privateKey);
            fromAddress = keyPair.getHexAddress();
            environment.put(FROM_ADDRESS, fromAddress);
        }
    }

}
